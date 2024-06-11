import static com.google.javascript.jscomp.DiagnosticType.*;
import static org.junit.Assert.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.PreprocessorSymbolTable;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.Traversal.ScopedCallback;
import com.google.javascript.rhino.NodeTraversal;

import org.junit.Before;
import org.junit.Test;

public class ScopedAliasesTest {
  private AbstractCompiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private HotSwapCompilerPass scopedAliases;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    compiler.setOption(CompilerOptions.ALLOW_GOOG_SCOPE, true);
    compiler.setOption(CompilerOptions.ALLOW_ONLY_GOOG_SCOPE, false);
    preprocessorSymbolTable = new PreprocessorSymbolTable();
    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, null);
  }

  @Test
  public void testProcessNoAliases() {
    Node root = Node.newProgram(1, null, null);
    scopedAliases.process(null, root);
    assertFalse("No errors expected", compiler.hasDiagnostics());
  }

  @Test
  public void testProcessInvalidGoogScopeCall() {
    String script = "goog.scope(function() {\n" +
        "  // This call to goog.scope is not alone in a statement.\n" +
        "  var x = goog.scope(function() {\n" +
        "  });\n" +
        "});";
    Node root = Node.parse(script);
    scopedAliases.process(null, root);
    assertEquals(1, compiler.getDiagnostics().size());
    DiagnosticType diagnostic = compiler.getDiagnostics().get(0).getType();
    assertTrue(diagnostic.equals(JSC_GOOG_SCOPE_USED_IMPROPERLY));
  }

  @Test
  public void testProcessInvalidGoogScopeParameters() {
    String script = "goog.scope(function() {\n" +
        "  // This call to goog.scope takes more than 1 parameter.\n" +
        "  goog.scope(function(a) {}, 1);\n" +
        "});";
    Node root = Node.parse(script);
    scopedAliases.process(null, root);
    assertEquals(1, compiler.getDiagnostics().size());
    DiagnosticType diagnostic = compiler.getDiagnostics().get(0).getType();
    assertTrue(diagnostic.equals(JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS));
  }

  @Test
  public void testProcessInvalidGoogScopeReturn() {
    String script = "goog.scope(function() {\n" +
        "  // This call to goog.scope uses return.\n" +
        "  return 1;\n" +
        "});";
    Node root = Node.parse(script);
    scopedAliases.process(null, root);
    assertEquals(1, compiler.getDiagnostics().size());
    DiagnosticType diagnostic = compiler.getDiagnostics().get(0).getType();
    assertTrue(diagnostic.equals(JSC_GOOG_SCOPE_USES_RETURN));
  }

  @Test
  public void testProcessInvalidGoogScopeThrow() {
    String script = "goog.scope(function() {\n" +
        "  // This call to goog.scope uses throw.\n" +
        "  throw new Error();\n" +
        "});";
    Node root = Node.parse(script);
    scopedAliases.process(null, root);
    assertEquals(1, compiler.getDiagnostics().size());
    DiagnosticType diagnostic = compiler.getDiagnostics().get(0).getType();
    assertTrue(diagnostic.equals(JSC_GOOG_SCOPE_USES_THROW));
  }

  @Test
  public void testProcessInvalidGoogScopeAliasRedefined() {
    String script = "goog.scope(function() {\n" +
        "  var a = 1;\n" +
        "  var a = 2;\n" +// Redefines the alias 'a'.
        "});";
    Node root = Node.parse(script);
    scopedAliases.process(null, root);
    assertEquals(1, compiler.getDiagnostics().size());
    DiagnosticType diagnostic = compiler.getDiagnostics().get(0).getType();
    assertTrue(diagnostic.equals(JSC_GOOG_SCOPE_ALIAS_REDEFINED));
  }

  @Test
  public void testProcessInvalidGoogScopeNonAliasLocal() {
    String script = "goog.scope(function() {\n" +
        "  var a = 1;\n" +
        "});";
    Node root = Node.parse(script);
    scopedAliases.process(null, root);
    assertEquals(1, compiler.getDiagnostics().size());
    DiagnosticType diagnostic = compiler.getDiagnostics().get(0).getType();
    assertTrue(diagnostic.equals(JSC_GOOG_SCOPE_NON_ALIAS_LOCAL));
  }

  @Test
  public void testProcessAlias() {
    String script = "goog.scope(function() {\n" +
        "  var dom = goog.dom;\n" +
        "  var DIV = dom.TagName.DIV;\n" +
        "  goog.dom.createElement(DIV);\n" +
        "});";
    Node root = Node.parse(script);
    scopedAliases.process(null, root);
    assertFalse("No errors expected", compiler.hasDiagnostics());
    Node googScope = root.getFirstChild();
    Node googScopeCall = googScope.getLastChild();
    Node scopeBody = googScopeCall.getLastChild();
    assertEquals(2, scopeBody.getChildCount());
    Node aliasDefinition = scopeBody.getChildAtIndex(0);
    Node aliasReference = scopeBody.getChildAtIndex(1);
    assertEquals("dom", aliasDefinition.getString());
    assertEquals("goog.dom", aliasReference.getQualifiedName());
  }

  @Test
  public void testProcessAliasTransformation() {
    String script = "goog.scope(function() {\n" +
        "  var dom = goog.dom;\n" +
        "  var DIV = dom.TagName.DIV;\n" +
        "  goog.dom.createElement(DIV);\n" +
        "});";
    Node root = Node.parse(script);
    AbstractCompiler oldCompiler = compiler;
    compiler = new AbstractCompiler();
    compiler.setOption(CompilerOptions.ALLOW_GOOG_SCOPE, true);
    compiler.setOption(CompilerOptions.ALLOW_ONLY_GOOG_SCOPE, false);
    compiler.setOption(CompilerOptions.ENABLE_ALIAS_TRANSFORM, true);
    PreprocessorSymbolTable oldPreprocessorSymbolTable = preprocessorSymbolTable;
    preprocessorSymbolTable = new PreprocessorSymbolTable();
    HotSwapCompilerPass oldScopedAliases = scopedAliases;
    scopedAliases = new ScopedAliases(
        compiler, preprocessorSymbolTable, null);
    scopedAliases.process(null, root);
    assertFalse("No errors expected", compiler.hasDiagnostics());
    String result = NodeUtil.getSourceCode(compiler.getSourceRootNode());
    assertTrue(
        result.contains("goog.dom.createElement(goog.dom.TagName.DIV)"));
    compiler = oldCompiler;
    preprocessorSymbolTable = oldPreprocessorSymbolTable;
    scopedAliases = oldScopedAliases;
  }

  @Test
  public void testProcessAliasErrors() {
    String script = "goog.scope(function() {\n" +
        "  var a = 1;\n" +
        "  var a = 2;\n" +// Redefines the alias 'a'.
        "  goog.scope.this = 1;\n" +// References 'this'.
        "});";
    Node root = Node.parse(script);
    scopedAliases.process(null, root);
    assertEquals(3, compiler.getDiagnostics().size());
    DiagnosticType diagnostic1 = compiler.getDiagnostics().get(0).getType();
    DiagnosticType diagnostic2 = compiler.getDiagnostics().get(1).getType();
    DiagnosticType diagnostic3 = compiler.getDiagnostics().get(2).getType();
    assertTrue(diagnostic1.equals(JSC_GOOG_SCOPE_ALIAS_REDEFINED));
    assertTrue(diagnostic2.equals(JSC_GOOG_SCOPE_REFERENCES_THIS));
    assertTrue(diagnostic3.equals(JSC_GOOG_SCOPE_USES_THROW)); // False positive.
  }
}