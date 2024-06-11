import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.*;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.SourcePosition;

import org.junit.Test;

public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    String jsSource =
        "goog.scope(function() {\n" +
        "  var dom = goog.dom;\n" +
        "  var DIV = dom.TagName.DIV;\n" +
        "  var el = dom.createElement(DIV);\n" +
        "});";

    Compiler compiler = new Compiler();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    Node root = compiler.parseTree(jsSource, "test.js");
    scopedAliases.process(null, root);

    Traversal traversal = scopedAliases.getTraversal();
    assertEquals(0, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(0, traversal.getScopeCalls().size());
    assertEquals(2, traversal.getAliasUsages().size());

    Node aliasUsage1 = traversal.getAliasUsages().get(0);
    assertEquals("dom", aliasUsage1.toString());

    Node aliasUsage2 = traversal.getAliasUsages().get(1);
    assertEquals("DIV", aliasUsage2.toString());

    Node expectedTransformedSource =
        "goog.dom.createElement(goog.dom.TagName.DIV);";
    List<Node> transformations = scopedAliases.getTraversal().getTransformations();
    Node transformation = transformations.get(0);
    String transformedSource =
        new SourcePosition<AliasTransformation>(
            transformation.getSourcePosition()).getSourceCode();
    assertEquals(expectedTransformedSource, transformedSource);
  }

  @Test(expected = AssertionError.class)
  public void testMultipleAliasDefinitions() {
    String jsSource =
        "goog.scope(function() {\n" +
        "  var dom = goog.dom;\n" +
        "  var DIV = goog.dom.TagName.DIV;\n" +
        "  var DIV2 = goog.dom.TagName.DIV;\n" +
        "});";

    Compiler compiler = new Compiler();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    Node root = compiler.parseTree(jsSource, "test.js");
    scopedAliases.process(null, root);

    Traversal traversal = scopedAliases.getTraversal();
    assertEquals(2, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(0, traversal.getScopeCalls().size());
    assertEquals(4, traversal.getAliasUsages().size());

    String expectedErrorMessage = "The alias DIV is assigned a value more than once.";
    assertTrue(traversal.hasErrors());
    assertEquals(1, traversal.getErrors().size());
    DiagnosticType error = traversal.getErrors().get(0).getType();
    assertEquals(GOOG_SCOPE_ALIAS_REDEFINED, error);
    assertArrayEquals(new String[] { "DIV" }, error.getArgs());
  }

  @Test(expected = AssertionError.class)
  public void testNonAliasLocalVariable() {
    String jsSource =
        "goog.scope(function() {\n" +
        "  var el = document.createElement('div');\n" +
        "});";

    Compiler compiler = new Compiler();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    Node root = compiler.parseTree(jsSource, "test.js");
    scopedAliases.process(null, root);

    Traversal traversal = scopedAliases.getTraversal();
    assertEquals(0, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(0, traversal.getScopeCalls().size());
    assertEquals(1, traversal.getAliasUsages().size());

    String expectedErrorMessage = "The local variable el is in a goog.scope and is not an alias.";
    assertTrue(traversal.hasErrors());
    assertEquals(1, traversal.getErrors().size());
    DiagnosticType error = traversal.getErrors().get(0).getType();
    assertEquals(GOOG_SCOPE_NON_ALIAS_LOCAL, error);
    assertArrayEquals(new String[] { "el" }, error.getArgs());
  }

  @Test(expected = AssertionError.class)
  public void testGoogScopeWithArguments() {
    String jsSource =
        "goog.scope(function(arg1, arg2) {\n" +
        "});";

    Compiler compiler = new Compiler();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    Node root = compiler.parseTree(jsSource, "test.js");
    scopedAliases.process(null, root);

    Traversal traversal = scopedAliases.getTraversal();
    assertEquals(0, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(0, traversal.getScopeCalls().size());
    assertEquals(0, traversal.getAliasUsages().size());

    String expectedErrorMessage =
        "The call to goog.scope must be alone in a single statement.";
    assertTrue(traversal.hasErrors());
    assertEquals(1, traversal.getErrors().size());
    DiagnosticType error = traversal.getErrors().get(0).getType();
    assertEquals(GOOG_SCOPE_USED_IMPROPERLY, error);
  }

  @Test(expected = AssertionError.class)
  public void testGoogScopeWithThisKeyword() {
    String jsSource =
        "goog.scope(function() {\n" +
        "  return this;\n" +
        "});";

    Compiler compiler = new Compiler();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    Node root = compiler.parseTree(jsSource, "test.js");
    scopedAliases.process(null, root);

    Traversal traversal = scopedAliases.getTraversal();
    assertEquals(0, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(0, traversal.getScopeCalls().size());
    assertEquals(1, traversal.getAliasUsages().size());

    String expectedErrorMessage = "The body of a goog.scope function cannot reference 'this'.";
    assertTrue(traversal.hasErrors());
    assertEquals(1, traversal.getErrors().size());
    DiagnosticType error = traversal.getErrors().get(0).getType();
    assertEquals(GOOG_SCOPE_REFERENCES_THIS, error);
  }

  @Test(expected = AssertionError.class)
  public void testGoogScopeWithReturnStatement() {
    String jsSource =
        "goog.scope(function() {\n" +
        "  return 42;\n" +
        "});";

    Compiler compiler = new Compiler();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    Node root = compiler.parseTree(jsSource, "test.js");
    scopedAliases.process(null, root);

    Traversal traversal = scopedAliases.getTraversal();
    assertEquals(0, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(0, traversal.getScopeCalls().size());
    assertEquals(1, traversal.getAliasUsages().size());

    String expectedErrorMessage = "The body of a goog.scope function cannot use 'return'.";
    assertTrue(traversal.hasErrors());
    assertEquals(1, traversal.getErrors().size());
    DiagnosticType error = traversal.getErrors().get(0).getType();
    assertEquals(GOOG_SCOPE_USES_RETURN, error);
  }

  @Test(expected = AssertionError.class)
  public void testGoogScopeWithThrowStatement() {
    String jsSource =
        "goog.scope(function() {\n" +
        "  throw new Error('Test Error');\n" +
        "});";

    Compiler compiler = new Compiler();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    Node root = compiler.parseTree(jsSource, "test.js");
    scopedAliases.process(null, root);

    Traversal traversal = scopedAliases.getTraversal();
    assertEquals(0, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(0, traversal.getScopeCalls().size());
    assertEquals(1, traversal.getAliasUsages().size());

    String expectedErrorMessage = "The body of a goog.scope function cannot use 'throw'.";
    assertTrue(traversal.hasErrors());
    assertEquals(1, traversal.getErrors().size());
    DiagnosticType error = traversal.getErrors().get(0).getType();
    assertEquals(GOOG_SCOPE_USES_THROW, error);
  }
}