import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.javascript.jscomp.JSError.*;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.JSDocInfo;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopeCreator;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

public class SyntacticScopeCreatorTest {

  private static AbstractCompiler compiler;
  private static SyntacticScopeCreator syntacticScopeCreator;

  @BeforeClass
  public static void setup() {
    compiler = new AbstractCompiler() {};
    syntacticScopeCreator = new SyntacticScopeCreator(compiler);
  }

  @Test
  public void testCreateScope() {
    Scope expectedScope = new Scope(Token.SCRIPT, null);
    Scope actualScope = syntacticScopeCreator.createScope(Token.SCRIPT, null);
    assertEquals(expectedScope, actualScope);
  }

  @Test
  public void testFunctionScope() {
    Node functionNode = new Node(Token.FUNCTION);
    Node functionNameNode = new Node(Token.NAME, "test");
    Node argNode = new Node(Token.NAME, "arg");
    Node bodyNode = new Node(Token.BLOCK);

    Scope expectedParentScope = new Scope(Token.SCRIPT, null);
    Scope expectedScope = new Scope(functionNode, expectedParentScope);

    Scope actualParentScope = null;
    Scope actualScope = syntacticScopeCreator.createScope(functionNode, actualParentScope);

    assertEquals(expectedScope, actualScope);
    assertEquals(expectedParentScope, actualScope.getParent());

    // Test function name declaration
    expectedScope.declare("test", functionNameNode, null, null);

    // Test arguments declaration
    expectedScope.declare("arg", argNode, null, null);

    // Test body variables declaration
    Node bodyVariableNode = new Node(Token.VAR, Token.BLOCK);
    Node bodyVariableNameNode = new Node(Token.NAME, "bodyVar");
    bodyVariableNode.addChild(bodyVariableNameNode);
    bodyNode.addChild(bodyVariableNode);
    expectedScope.declare("bodyVar", bodyVariableNameNode, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateScopeWithNullParent() {
    syntacticScopeCreator.createScope(Token.SCRIPT, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateScopeWithNullNode() {
    syntacticScopeCreator.createScope(null, null);
  }

  @Test
  public void testVarMultiplyDeclaredError() {
    Node functionNode = new Node(Token.FUNCTION);
    Node functionNameNode = new Node(Token.NAME, "test");
    Node argNode1 = new Node(Token.NAME, "arg1");
    Node argNode2 = new Node(Token.NAME, "arg1");
    Node bodyNode = new Node(Token.BLOCK);

    Scope parentScope = new Scope(Token.SCRIPT, null);
    Scope expectedScope = new Scope(functionNode, parentScope);

    Scope actualScope = syntacticScopeCreator.createScope(functionNode, parentScope);

    // Test function name declaration
    expectedScope.declare("test", functionNameNode, null, null);

    // Test first argument declaration
    expectedScope.declare("arg1", argNode1, null, null);

    // Test second argument declaration
    expectedScope.declare("arg1", argNode2, null, null);

    syntacticScopeCreator.redeclarationHandler.onRedeclaration(
        expectedScope, "arg1", argNode2, functionNode, null, null);

    Node errorNode = assertError(
        JSError.make(
            "test.js", argNode2,
            DiagnosticType.error("JSC_VAR_MULTIPLE_DECLARED_ERROR"),
            "arg1",
            "(test)"));

    assertEquals("arg1 first declared in test", errorNode.getString());
  }

  @Test
  public void testVarArgumentsShadowedError() {
    Node functionNode = new Node(Token.FUNCTION);
    Node functionNameNode = new Node(Token.NAME, "test");
    Node argNode = new Node(Token.NAME, "arg");
    Node argumentsNode = new Node(Token.LP);
    Node bodyNode = new Node(Token.BLOCK);

    Scope parentScope = new Scope(Token.SCRIPT, null);
    Scope expectedScope = new Scope(functionNode, parentScope);

    Scope actualScope = syntacticScopeCreator.createScope(functionNode, parentScope);

    // Test function name declaration
    expectedScope.declare("test", functionNameNode, null, null);

    // Test arguments declaration
    expectedScope.declare("arguments", argumentsNode, JSType.ANY, null);

    // Test 'arguments' shadowing
    Node argumentsShadowNode = new Node(Token.NAME, "arguments");
    expectedScope.declare("arguments", argumentsShadowNode, null, null);

    syntacticScopeCreator.redeclarationHandler.onRedeclaration(
        expectedScope, "arguments", argumentsShadowNode, functionNode, null, null);

    Node errorNode = assertError(
        JSError.make(
            "test.js", argumentsShadowNode,
            DiagnosticType.error("JSC_VAR_ARGUMENTS_SHADOWED_ERROR")));

    assertEquals("Shadowing \"arguments\" is not allowed", errorNode.getString());
  }

  private Node assertError(JSError expectedError) {
    Node node = new Node(Token.BLOCK);
    compiler.compile(node, null, null);

    DiagnosticMessage diagMessage = compiler.getDiagnostics().getError(0);
    assertEquals(expectedError.getType(), diagMessage.getType());
    assertEquals(expectedError.getMessage(), diagMessage.getMessage());
    assertEquals(expectedError.getSourceName(), diagMessage.getSourceName());
    assertEquals(expectedError.getLineNumber(), diagMessage.getLineNumber());
    assertEquals(expectedError.getColumn(), diagMessage.getColumn());

    return node;
  }

  @AfterClass
  public static void teardown() {
    compiler = null;
    syntacticScopeCreator = null;
  }
}