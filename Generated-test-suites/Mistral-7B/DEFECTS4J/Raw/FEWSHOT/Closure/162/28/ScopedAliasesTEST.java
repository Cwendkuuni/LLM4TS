package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;
import com.google.javascript.rhino.Token;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class ScopedAliasesTest {
  @Test
  public void testScopedAliases() {
    AbstractCompiler mockCompiler = mock(AbstractCompiler.class);
    PreprocessorSymbolTable mockSymbolTable = mock(PreprocessorSymbolTable.class);
    AliasTransformationHandler mockHandler = mock(AliasTransformationHandler.class);

    ScopedAliases scopedAliases = new ScopedAliases(mockCompiler, mockSymbolTable, mockHandler);

    Node root = Node.makeSourceNode("test.js", "function foo() {\n" +
        "  goog.scope(function() {\n" +
        "    var bar = goog.dom;\n" +
        "    var DIV = bar.TagName.DIV;\n" +
        "    goog.dom.createElement(DIV);\n" +
        "  });\n" +
        "  return 'result';\n" +
        "}", null);

    scopedAliases.process(null, root);

    List<Node> aliasDefinitions = Lists.newArrayList(root.getFirstChild().getLastChild().getFirstChild());
    assertEquals(3, aliasDefinitions.size());

    Var barAlias = aliasDefinitions.get(0).getParent().getParent().getParent().getParent().getParent().getParent();
    assertEquals("bar", barAlias.getName());
    assertEquals("goog.dom", barAlias.getInitialValue().getString());

    Var divAlias = aliasDefinitions.get(1).getParent().getParent().getParent().getParent().getParent().getParent();
    assertEquals("DIV", divAlias.getName());
    assertEquals("goog.dom.TagName.DIV", divAlias.getInitialValue().getQualifiedName());

    Var googAlias = aliasDefinitions.get(2).getParent().getParent().getParent();
    assertEquals("goog", googAlias.getName());
    assertEquals("goog", googAlias.getInitialValue().getString());

    assertEquals(1, scopedAliases.getAliasUsages().size());
    AliasUsage aliasUsage = scopedAliases.getAliasUsages().get(0);
    assertInstanceOf(AliasedNode.class, aliasUsage);
    AliasedNode aliasedNode = (AliasedNode) aliasUsage;
    assertEquals("bar", aliasedNode.aliasReference.getString());
    assertEquals("goog.dom", aliasedNode.aliasDefinition.getString());

    assertEquals(0, scopedAliases.getScopeCalls().size());

    assertEquals(0, scopedAliases.getAliasDefinitionsInOrder().size());
  }

  @Test
  public void testScopedAliasesWithError() {
    AbstractCompiler mockCompiler = mock(AbstractCompiler.class);
    PreprocessorSymbolTable mockSymbolTable = mock(PreprocessorSymbolTable.class);
    AliasTransformationHandler mockHandler = mock(AliasTransformationHandler.class);

    ScopedAliases scopedAliases = new ScopedAliases(mockCompiler, mockSymbolTable, mockHandler);

    Node root = Node.makeSourceNode("test.js", "function foo() {\n" +
        "  goog.scope(function() {\n" +
        "    goog.scope(function() {}); // extra goog.scope call\n" +
        "    var bar = goog.dom;\n" +
        "    var DIV = bar.TagName.DIV;\n" +
        "    goog.dom.createElement(DIV);\n" +
        "  });\n" +
        "  return 'result';\n" +
        "}", null);

    scopedAliases.process(null, root);

    assertEquals(1, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(1, scopedAliases.getScopeCalls().size());
    assertEquals(1, scopedAliases.getAliasUsages().size());

    // Check that error was reported
    AliasTransformation aliasTransformation = when(mockHandler.logAliasTransformation(
        eq("test.js"), any(SourcePosition.class))).thenReturn(mock(AliasTransformation.class));
    verify(mockCompiler).report(any(Diagnostic.class));
  }

  @Test
  public void testScopedAliasesWithInvalidFunction() {
    AbstractCompiler mockCompiler = mock(AbstractCompiler.class);
    PreprocessorSymbolTable mockSymbolTable = mock(PreprocessorSymbolTable.class);
    AliasTransformationHandler mockHandler = mock(AliasTransformationHandler.class);

    ScopedAliases scopedAliases = new ScopedAliases(mockCompiler, mockSymbolTable, mockHandler);

    Node root = Node.makeSourceNode("test.js", "function foo() {\n" +
        "  goog.scope(function() {}); // invalid goog.scope function\n" +
        "}", null);

    scopedAliases.process(null, root);

    assertEquals(0, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(0, scopedAliases.getScopeCalls().size());
    assertEquals(0, scopedAliases.getAliasUsages().size());

    // Check that error was reported
    verify(mockCompiler).report(any(Diagnostic.class));
  }

  @Test
  public void testScopedAliasesWithReturnStatement() {
    AbstractCompiler mockCompiler = mock(AbstractCompiler.class);
    PreprocessorSymbolTable mockSymbolTable = mock(PreprocessorSymbolTable.class);
    AliasTransformationHandler mockHandler = mock(AliasTransformationHandler.class);

    ScopedAliases scopedAliases = new ScopedAliases(mockCompiler, mockSymbolTable, mockHandler);

    Node root = Node.makeSourceNode("test.js", "function foo() {\n" +
        "  goog.scope(function() {\n" +
        "    return 'result';\n" +
        "  });\n" +
        "}", null);

    scopedAliases.process(null, root);

    assertEquals(0, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(0, scopedAliases.getScopeCalls().size());
    assertEquals(0, scopedAliases.getAliasUsages().size());

    // Check that error was reported
    verify(mockCompiler).report(any(Diagnostic.class));
  }

  @Test
  public void testScopedAliasesWithThrowStatement() {
    AbstractCompiler mockCompiler = mock(AbstractCompiler.class);
    PreprocessorSymbolTable mockSymbolTable = mock(PreprocessorSymbolTable.class);
    AliasTransformationHandler mockHandler = mock(AliasTransformationHandler.class);

    ScopedAliases scopedAliases = new ScopedAliases(mockCompiler, mockSymbolTable, mockHandler);

    Node root = Node.makeSourceNode("test.js", "function foo() {\n" +
        "  goog.scope(function() {\n" +
        "    throw new Error('test');\n" +
        "  });\n" +
        "}", null);

    scopedAliases.process(null, root);

    assertEquals(0, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(0, scopedAliases.getScopeCalls().size());
    assertEquals(0, scopedAliases.getAliasUsages().size());

    // Check that error was reported
    verify(mockCompiler).report(any(Diagnostic.class));
  }

  @Test
  public void testScopedAliasesWithThisKeyword() {
    AbstractCompiler mockCompiler = mock(AbstractCompiler.class);
    PreprocessorSymbolTable mockSymbolTable = mock(PreprocessorSymbolTable.class);
    AliasTransformationHandler mockHandler = mock(AliasTransformationHandler.class);

    ScopedAliases scopedAliases = new ScopedAliases(mockCompiler, mockSymbolTable, mockHandler);

    Node root = Node.makeSourceNode("test.js", "function foo() {\n" +
        "  goog.scope(function() {\n" +
        "    this.bar = 'baz';\n" +
        "  });\n" +
        "}", null);

    scopedAliases.process(null, root);

    assertEquals(0, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(0, scopedAliases.getScopeCalls().size());
    assertEquals(0, scopedAliases.getAliasUsages().size());

    // Check that error was reported
    verify(mockCompiler).report(any(Diagnostic.class));
  }

  @Test
  public void testScopedAliasesWithInvalidAliasDefinition() {
    AbstractCompiler mockCompiler = mock(AbstractCompiler.class);
    PreprocessorSymbolTable mockSymbolTable = mock(PreprocessorSymbolTable.class);
    AliasTransformationHandler mockHandler = mock(AliasTransformationHandler.class);

    ScopedAliases scopedAliases = new ScopedAliases(mockCompiler, mockSymbolTable, mockHandler);

    Node root = Node.makeSourceNode("test.js", "function foo() {\n" +
        "  goog.scope(function() {\n" +
        "    var foo = 'bar';\n" +
        "  });\n" +
        "}", null);

    scopedAliases.process(null, root);

    assertEquals(0, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(0, scopedAliases.getScopeCalls().size());
    assertEquals(0, scopedAliases.getAliasUsages().size());

    // Check that error was reported
    verify(mockCompiler).report(any(Diagnostic.class));
  }
}