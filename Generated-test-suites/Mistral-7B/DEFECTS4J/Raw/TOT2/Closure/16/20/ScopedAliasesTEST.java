import static org.junit.Assert.*;
import static com.google.javascript.jscomp.DiagnosticType.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.ScopedAliases;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;
import com.google.javascript.rhino.Token;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Unit tests for the ScopedAliases class.
 */
public class ScopedAliasesTest {

  @Test
  public void testValidScopedAliases() {
    AbstractCompiler compiler = new AbstractCompiler();
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);

    Node rootNode = getTestNode();
    Node externs = null;
    scopedAliases.process(externs, rootNode);

    assertTrue(scopedAliases.hasErrors());
    assertEquals(6, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(0, scopedAliases.getScopeCalls().size());
    assertEquals(6, scopedAliases.getAliasUsages().size());

    checkAliasDefinition(scopedAliases.getAliasDefinitionsInOrder().get(0), "DIV", "goog.dom.TagName.DIV");
    checkAliasDefinition(scopedAliases.getAliasDefinitionsInOrder().get(1), "dom", "goog.dom");
    checkAliasDefinition(scopedAliases.getAliasDefinitionsInOrder().get(2), "goog", "window");
    checkAliasDefinition(scopedAliases.getAliasDefinitionsInOrder().get(3), "goog", "goog");
    checkAliasDefinition(scopedAliases.getAliasDefinitionsInOrder().get(4), "x", "goog.x");
    checkAliasDefinition(scopedAliases.getAliasDefinitionsInOrder().get(5), "y", "goog.y");

    checkAliasUsage(
        scopedAliases.getAliasUsages().get(0), "dom.createElement(DIV)", "goog.dom.createElement(DIV)");
    checkAliasUsage(
        scopedAliases.getAliasUsages().get(1), "goog.scope(function() { var dom = goog.dom; })()",
        "goog.dom.scope(function() { var dom = goog.dom; })();");
    checkAliasUsage(
        scopedAliases.getAliasUsages().get(2), "goog.scope(function() { var x = goog.x; })()",
        "goog.x.scope(function() { var x = goog.x; })();");
    checkAliasUsage(
        scopedAliases.getAliasUsages().get(3), "goog.scope(function() { var y = goog.y; })()",
        "goog.y.scope(function() { var y = goog.y; })();");
    checkAliasUsage(
        scopedAliases.getAliasUsages().get(4), "goog.scope(function() { var x = goog.goog.x; })()",
        "goog.goog.x.scope(function() { var x = goog.goog.x; })();");
    checkAliasUsage(
        scopedAliases.getAliasUsages().get(5), "goog.scope(function() { var y = goog.goog.y; })()",
        "goog.goog.y.scope(function() { var y = goog.goog.y; })();");

    // Assert no errors were reported
    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testInvalidScopedAliases() {
    AbstractCompiler compiler = new AbstractCompiler();
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);

    Node rootNode = getInvalidTestNode();
    Node externs = null;
    scopedAliases.process(externs, rootNode);

    assertTrue(scopedAliases.hasErrors());
    assertEquals(0, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(0, scopedAliases.getScopeCalls().size());
    assertEquals(0, scopedAliases.getAliasUsages().size());

    // Assert errors were reported
    List<DiagnosticType> diagnostics = Arrays.asList(
        JSC_GOOG_SCOPE_USED_IMPROPERLY,
        JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS,
        JSC_GOOG_SCOPE_REFERENCES_THIS,
        JSC_GOOG_SCOPE_USES_RETURN,
        JSC_GOOG_SCOPE_USES_THROW);
    assertDiagnosticTypes(diagnostics, compiler.getDiagnostics());
  }

  private Node getTestNode() {
    String testCode =
        "goog.scope(function() {" +
            "  var DIV = goog.dom.TagName.DIV;" +
            "  var dom = goog.dom;" +
            "  var x = goog.x;" +
            "  var y = goog.y;" +
            "  var goog = goog;" +
            "})();";

    return Node.parse(testCode);
  }

  private Node getInvalidTestNode() {
    String testCode =
        "goog.scope(function() {" +
            "  this.x = goog.x;" +
            "  return goog.scope(function() { throw new Error(); })();" +
            "})();";

    return Node.parse(testCode);
  }

  private void checkAliasDefinition(Node node, String aliasName, String qualifiedName) {
    assertEquals(
        NAME, node.getType());
    assertEquals(aliasName, node.getString());
    assertEquals(ASSIGNMENT, node.getChildAtIndex(0).getType());
    Node left = node.getChildAtIndex(0);
    assertEquals(TOKEN_ASSIGN, left.getType());
    assertEquals(QUALIFIED_NAME, left.getChildAtIndex(0).getType());
    assertEquals(aliasName, left.getChildAtIndex(0).getString());
    Node right = node.getChildAtIndex(1);
    assertEquals(QUALIFIED_NAME, right.getType());
    assertEquals(qualifiedName, right.getString());
  }

  private void checkAliasUsage(Node node, String usage, String expected) {
    assertEquals(CALL, node.getType());
    Node scopeCall = node.getChildAtIndex(0);
    assertEquals(NAME, scopeCall.getType());
    assertEquals("goog.scope", scopeCall.getString());
    Node functionNode = scopeCall.getLastChild();
    assertEquals(FUNCTION, functionNode.getType());
    Node body = functionNode.getChildAtIndex(0);
    assertEquals(TOKEN_LPAREN, body.getType());
    Node params = body.getChildAtIndex(1);
    assertEquals(TOKEN_RPAREN, params.getType());
    Node statement = body.getChildAtIndex(2);
    assertEquals(ASSIGNMENT, statement.getType());
    Node left = statement.getChildAtIndex(0);
    assertEquals(TOKEN_IDENT, left.getType());
    assertEquals(usage, left.getString());
    Node right = statement.getChildAtIndex(1);
    assertEquals(QUALIFIED_NAME, right.getType());
    assertEquals(expected, right.getString());
  }

  private void assertDiagnosticTypes(List<DiagnosticType> diagnostics,
      Collection<DiagnosticType> expectedTypes) {
    for (DiagnosticType diagnosticType : diagnostics) {
      assertTrue(expectedTypes.contains(diagnosticType));
      expectedTypes.remove(diagnosticType);
    }
    assertTrue(expectedTypes.isEmpty());
  }
}