import static org.junit.Assert.*;
import static com.google.javascript.jscomp.DiagnosticType.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.SourcePosition;

import org.junit.Test;

public class ScopedAliasesTest {

  @Test
  public void testValidScopedAlias() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    String sourceCode =
        " goog.scope(function() {" +
        "   var x = goog.x;" +
        " });";

    Node root = Parser.parse(sourceCode, "test.js", null);
    scopedAliases.process(null, root);

    Traversal traversal = scopedAliases.traversal;
    assertTrue(traversal.getAliasUsages().size() == 1);
    AliasUsage aliasUsage = traversal.getAliasUsages().get(0);
    assertEquals("x", aliasUsage.aliasReference.getString());
    assertEquals("goog.x", aliasUsage.aliasDefinition.getString());
  }

  @Test
  public void testMultipleScopedAliases() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    String sourceCode =
        " goog.scope(function() {" +
        "   var x = goog.x;" +
        "   var y = goog.y;" +
        " });";

    Node root = Parser.parse(sourceCode, "test.js", null);
    scopedAliases.process(null, root);

    Traversal traversal = scopedAliases.traversal;
    assertTrue(traversal.getAliasUsages().size() == 2);
    AliasUsage aliasUsageX = traversal.getAliasUsages().get(0);
    assertEquals("x", aliasUsageX.aliasReference.getString());
    assertEquals("goog.x", aliasUsageX.aliasDefinition.getString());

    AliasUsage aliasUsageY = traversal.getAliasUsages().get(1);
    assertEquals("y", aliasUsageY.aliasReference.getString());
    assertEquals("goog.y", aliasUsageY.aliasDefinition.getString());
  }

  @Test
  public void testScopedAliasRedefinition() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    String sourceCode =
        " goog.scope(function() {" +
        "   var x = goog.x;" +
        "   var x = goog.y;" +
        " });";

    Node root = Parser.parse(sourceCode, "test.js", null);
    scopedAliases.process(null, root);

    Traversal traversal = scopedAliases.traversal;
    assertTrue(traversal.getAliasUsages().size() == 1);
    AliasUsage aliasUsage = traversal.getAliasUsages().get(0);
    assertEquals("x", aliasUsage.aliasReference.getString());
    assertEquals("goog.y", aliasUsage.aliasDefinition.getString());
  }

  @Test
  public void testScopedAliasShadowing() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    String sourceCode =
        " goog.scope(function() {" +
        "   var goog = {x: 1};" +
        "   var x = goog.x;" +
        " });";

    Node root = Parser.parse(sourceCode, "test.js", null);
    scopedAliases.process(null, root);

    Traversal traversal = scopedAliases.traversal;
    assertTrue(traversal.getAliasUsages().isEmpty());
    assertTrue(traversal.getAliasDefinitionsInOrder().isEmpty());
  }

  @Test
  public void testScopedAliasInvalidFunction() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    String sourceCode =
        " goog.scope(" +
        "   function() {" +
        "   }" +
        ");";

    Node root = Parser.parse(sourceCode, "test.js", null);
    scopedAliases.process(null, root);

    Traversal traversal = scopedAliases.traversal;
    assertTrue(traversal.getErrors().size() == 1);
    DiagnosticType actualError = traversal.getErrors().get(0).getType();
    assertEquals(JSC_GOOG_SCOPE_USED_IMPROPERLY, actualError);
  }

  @Test
  public void testScopedAliasMultipleParameters() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    String sourceCode =
        " goog.scope(function(a, b) {" +
        "   var x = goog.x;" +
        " });";

    Node root = Parser.parse(sourceCode, "test.js", null);
    scopedAliases.process(null, root);

    Traversal traversal = scopedAliases.traversal;
    assertTrue(traversal.getErrors().size() == 1);
    DiagnosticType actualError = traversal.getErrors().get(0).getType();
    assertEquals(JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS, actualError);
  }

  @Test
  public void testScopedAliasReturnStatement() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    String sourceCode =
        " goog.scope(function() {" +
        "   return 123;" +
        " });";

    Node root = Parser.parse(sourceCode, "test.js", null);
    scopedAliases.process(null, root);

    Traversal traversal = scopedAliases.traversal;
    assertTrue(traversal.getErrors().size() == 1);
    DiagnosticType actualError = traversal.getErrors().get(0).getType();
    assertEquals(JSC_GOOG_SCOPE_USES_RETURN, actualError);
  }

  @Test
  public void testScopedAliasThrowStatement() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    String sourceCode =
        " goog.scope(function() {" +
        "   throw new Error('This should not happen');" +
        " });";

    Node root = Parser.parse(sourceCode, "test.js", null);
    scopedAliases.process(null, root);

    Traversal traversal = scopedAliases.traversal;
    assertTrue(traversal.getErrors().size() == 1);
    DiagnosticType actualError = traversal.getErrors().get(0).getType();
    assertEquals(JSC_GOOG_SCOPE_USES_THROW, actualError);
  }

  @Test
  public void testScopedAliasUsingThis() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    String sourceCode =
        " goog.scope(function() {" +
        "   this.x = 123;" +
        " });";

    Node root = Parser.parse(sourceCode, "test.js", null);
    scopedAliases.process(null, root);

    Traversal traversal = scopedAliases.traversal;
    assertTrue(traversal.getErrors().size() == 1);
    DiagnosticType actualError = traversal.getErrors().get(0).getType();
    assertEquals(JSC_GOOG_SCOPE_REFERENCES_THIS, actualError);
  }

  @Test
  public void testScopedAliasWithNamespace() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    String sourceCode =
        " goog.scope(function() {" +
        "   var goog = {x: 123};" +
        "   var x = goog.x;" +
        " });";

    Node root = Parser.parse(sourceCode, "test.js", null);
    scopedAliases.process(null, root);

    Traversal traversal = scopedAliases.traversal;
    assertTrue(traversal.getErrors().isEmpty());

    Traversal traversal2 = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal2);

    // The alias should not be present in the final code.
    assertTrue(traversal2.getAliasDefinitionsInOrder().isEmpty());
  }

  @Test
  public void testScopedAliasWithJSDocInfo() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    String sourceCode =
        " /**\n" +
        "  * @returns {number}\n" +
        "  */\n" +
        " goog.scope(function() {" +
        "   var x = goog.x;" +
        "   return x + 1;" +
        " });";

    Node root = Parser.parse(sourceCode, "test.js", null);
    scopedAliases.process(null, root);

    Traversal traversal = scopedAliases.traversal;
    assertTrue(traversal.getErrors().isEmpty());

    Traversal traversal2 = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal2);

    // The alias should not be present in the final code.
    assertTrue(traversal2.getAliasDefinitionsInOrder().isEmpty());

    // The JSDocInfo should not be affected.
    Node jsdocInfoNode = root.getFirstChild().getLastChild();
    assertNotNull(jsdocInfoNode.getJSDocInfo());
    assertEquals("@returns {number}", jsdocInfoNode.getJSDocInfo().getSummary());
  }
}