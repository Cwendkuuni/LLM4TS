import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.Aliases;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.PreprocessorSymbolTable;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.Traversal.ScopeCall;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.SourceFile;
import com.google.javascript.rhino.Token;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    CompilerOptions options = new CompilerOptions();
    options.setAliases(new AliasTransformation[] {new AliasTransformation()});
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);

    SourceFile sourceFile = new SourceFile("Test.java", "test code");

    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    when(compiler.createPreprocessorSymbolTable()).thenReturn(preprocessorSymbolTable);
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node root = createNode("Test.java", "test code");

    scopedAliases.process(null, root);

    verify(compiler, times(1)).reportCodeChange();
    verify(transformationHandler, times(1)).logAliasTransformation(
        mock(String.class), any(SourcePosition.class));

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(0, aliasDefinitionsInOrder.size());

    List<ScopeCall> scopeCalls = scopedAliases.getScopeCalls();
    assertEquals(0, scopeCalls.size());

    List<Node> aliasUsages = scopedAliases.getAliasUsages();
    assertEquals(0, aliasUsages.size());

    assertFalse(scopedAliases.hasErrors());
  }

  @Test
  public void testScopedAliasesWithAliases() {
    CompilerOptions options = new CompilerOptions();
    options.setAliases(new AliasTransformation[] {new AliasTransformation()});
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);

    SourceFile sourceFile = new SourceFile("Test.java", "test code\n" +
        "goog.scope(function(){\n" +
        "  var dom = goog.dom;\n" +
        "  var DIV = dom.TagName.DIV;\n" +
        "  goog.dom.createElement(DIV);\n" +
        "});");

    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    when(compiler.createPreprocessorSymbolTable()).thenReturn(preprocessorSymbolTable);
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node root = createNode("Test.java", "test code");

    Node googScope = createNode("Test.java", "test code", null,
        createNode("Test.java", "test code", null, "goog.scope"),
        createNode("Test.java", "test code", null, "function(){\n", null),
        createNode("Test.java", "test code", null, "}", null),
        createNode("Test.java", "test code", null, "{\n", null),
        createNode("Test.java", "test code", null, "  var dom =", null),
        createNode("Test.java", "test code", null, "  goog.dom;", null),
        createNode("Test.java", "test code", null, "  var DIV =", null),
        createNode("Test.java", "test code", null, "  goog.dom.TagName.DIV;", null),
        createNode("Test.java", "test code", null, "  goog.dom.createElement(", null),
        createNode("Test.java", "test code", null, "  ", null),
        createNode("Test.java", "test code", null, "goog.dom.TagName.DIV", null),
        createNode("Test.java", "test code", null, "};"));

    Node googScopeCall = createNode("Test.java", "test code", null,
        createNode("Test.java", "test code", null, "goog.scope"),
        createNode("Test.java", "test code", null, "(function(){\n", null),
        createNode("Test.java", "test code", null, "}", null),
        createNode("Test.java", "test code", null, "{\n", null));

    when(preprocessorSymbolTable.getReference(googScope.getFirstChild()))
        .thenReturn(createVariable("Test.java", "test code", "dom", null, null, null));
    when(preprocessorSymbolTable.getReference(googScope.getChildAtIndex(1)))
        .thenReturn(createVariable("Test.java", "test code", "DIV", null, null, null));

    when(compiler.reportCodeChange()).thenReturn(mock(CodeChange.class));
    when(transformationHandler.logAliasTransformation(
        eq("dom"), any(SourcePosition.class)))
        .thenReturn(new AliasTransformation(
            SourcePosition.positionAt(0, 0), "Test.java", null, "dom", "goog.dom"));

    scopedAliases.process(null, root);

    verify(compiler, times(1)).reportCodeChange();
    verify(transformationHandler, times(1)).logAliasTransformation(
        eq("dom"), any(SourcePosition.class));

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(1, aliasDefinitionsInOrder.size());
    assertSame(googScopeCall, aliasDefinitionsInOrder.get(0));

    List<ScopeCall> scopeCalls = scopedAliases.getScopeCalls();
    assertEquals(1, scopeCalls.size());
    assertSame(googScopeCall, scopeCalls.get(0));

    List<Node> aliasUsages = scopedAliases.getAliasUsages();
    assertEquals(1, aliasUsages.size());
    assertSame(createNode("Test.java", "test code", null, "goog.dom.createElement", null), aliasUsages.get(0));

    assertFalse(scopedAliases.hasErrors());
  }

  @Test
  public void testScopedAliasesWithErrors() {
    CompilerOptions options = new CompilerOptions();
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);

    SourceFile sourceFile = new SourceFile("Test.java", "test code\n" +
        "goog.scope(function(){\n" +
        "  var dom = goog.dom;\n" +
        "  var DIV = dom.TagName.DIV;\n" +
        "  goog.dom.createElement(DIV);\n" +
        "  return 42;\n" +
        "});");

    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    when(compiler.createPreprocessorSymbolTable()).thenReturn(preprocessorSymbolTable);
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node root = createNode("Test.java", "test code");

    Node googScope = createNode("Test.java", "test code", null,
        createNode("Test.java", "test code", null, "goog.scope"),
        createNode("Test.java", "test code", null, "function(){\n", null),
        createNode("Test.java", "test code", null, "}", null),
        createNode("Test.java", "test code", null, "{\n", null),
        createNode("Test.java", "test code", null, "  var dom =", null),
        createNode("Test.java", "test code", null, "  goog.dom;", null),
        createNode("Test.java", "test code", null, "  var DIV =", null),
        createNode("Test.java", "test code", null, "  goog.dom.TagName.DIV;", null),
        createNode("Test.java", "test code", null, "  goog.dom.createElement(", null),
        createNode("Test.java", "test code", null, "  ", null),
        createNode("Test.java", "test code", null, "goog.dom.TagName.DIV", null),
        createNode("Test.java", "test code", null, "};"));

    Node returnNode = createNode("Test.java", "test code", null, "return", null);

    when(preprocessorSymbolTable.getReference(googScope.getFirstChild()))
        .thenReturn(createVariable("Test.java", "test code", "dom", null, null, null));
    when(preprocessorSymbolTable.getReference(googScope.getChildAtIndex(1)))
        .thenReturn(createVariable("Test.java", "test code", "DIV", null, null, null));

    when(compiler.reportCodeChange())
        .thenThrow(new RuntimeException("Code change failed"));

    scopedAliases.process(null, root);

    verify(compiler, times(1)).reportCodeChange();
    verify(transformationHandler, never()).logAliasTransformation(any(String.class), any(SourcePosition.class));

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(0, aliasDefinitionsInOrder.size());

    List<ScopeCall> scopeCalls = scopedAliases.getScopeCalls();
    assertEquals(1, scopeCalls.size());
    assertSame(googScope, scopeCalls.get(0));

    List<Node> aliasUsages = scopedAliases.getAliasUsages();
    assertEquals(1, aliasUsages.size());
    assertSame(createNode("Test.java", "test code", null, "goog.dom.createElement", null), aliasUsages.get(0));

    assertTrue(scopedAliases.hasErrors());
  }

  private Node createNode(String fileName, String code, Node parent, Node... children) {
    Node node = new Node(fileName, code, createPosition(fileName, 0, 0), parent);
    for (Node child : children) {
      node.appendChild(child);
    }
    return node;
  }

  private Var createVariable(String fileName, String code, String name, Node initialValue,
      JSDocInfo jsDocInfo, List<Node> typeNodes) {
    Var variable = mock(Var.class);
    when(variable.getNode()).thenReturn(createNode(fileName, code, null, name));
    when(variable.getInitialValue()).thenReturn(initialValue);
    when(variable.getJsDocInfo()).thenReturn(jsDocInfo);
    when(variable.getTypeNodes()).thenReturn(typeNodes);
    when(variable.getSymbolName()).thenReturn(name);
    return variable;
  }

  private SourcePosition createPosition(String fileName, int line, int charPosition) {
    return new SourcePosition(fileName, line, charPosition);
  }
}