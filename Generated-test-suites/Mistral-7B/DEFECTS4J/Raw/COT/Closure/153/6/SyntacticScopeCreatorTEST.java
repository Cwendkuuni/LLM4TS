import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.javascript.jscomp.Scope.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.RedeclarationHandler;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopeCreator;
import com.google.javascript.jscomp.SyntacticScopeCreator;
import com.google.javascript.jscomp.DefaultRedeclarationHandler;
import com.google.javascript.rhino.JSDocInfo;

@RunWith(JUnit4.class)
public class SyntacticScopeCreatorTest {

  @Test
  public void testCreateScope() {
    AbstractCompiler compiler = new AbstractCompiler();
    SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);

    Scope parent = null;
    Node rootNode = Node.createTree("function foo() {}");
    Scope scope = creator.createScope(rootNode, parent);

    assertEquals(1, scope.size());
    assertNull(scope.getParent());
  }

  @Test
  public void testCreateScopeWithParent() {
    AbstractCompiler compiler = new AbstractCompiler();
    SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);

    Scope parent = new Scope(null, Node.createTree("function bar() {}"));
    Node rootNode = Node.createTree("function foo() { var x = 1; }");
    Scope scope = creator.createScope(rootNode, parent);

    assertEquals(2, scope.size());
    assertEquals(parent, scope.getParent());
  }

  @Test
  public void testCreateFunctionScope() {
    AbstractCompiler compiler = new AbstractCompiler();
    SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);

    Scope parent = null;
    Node rootNode = Node.createTree("function foo() { function bar() {} }");
    Scope scope = creator.createScope(rootNode, parent);

    assertEquals(2, scope.size());
    assertEquals(parent, scope.getParent());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateScopeWithNullParent() {
    AbstractCompiler compiler = new AbstractCompiler();
    SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);

    Node rootNode = Node.createTree("function foo() {}");
    creator.createScope(rootNode, null);
  }

  @Test
  public void testDeclareVariable() {
    AbstractCompiler compiler = new AbstractCompiler();
    SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
    DefaultRedeclarationHandler redeclarationHandler = new DefaultRedeclarationHandler();
    creator.redeclarationHandler = redeclarationHandler;

    Scope parent = null;
    Node rootNode = Node.createTree("function foo() { var x = 1; }");
    Scope scope = creator.createScope(rootNode, parent);

    String variableName = "x";
    Node variableNode = rootNode.getFirstChild();

    creator.declareVar(variableName, variableNode, rootNode, null, null, rootNode);

    Var variable = scope.getVar(variableName);
    assertEquals(variableNode, variable.getNode());
    assertEquals(JSType.NUMBER, variable.getType());
  }

  @Test
  public void testDeclareVariableWithJSDocType() {
    AbstractCompiler compiler = new AbstractCompiler();
    SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
    DefaultRedeclarationHandler redeclarationHandler = new DefaultRedeclarationHandler();
    creator.redeclarationHandler = redeclarationHandler;

    Scope parent = null;
    Node rootNode = Node.createTree("function foo() { /** @type {number} */ var x = 1; }");
    Scope scope = creator.createScope(rootNode, parent);

    String variableName = "x";
    Node variableNode = rootNode.getFirstChild();

    JSDocInfo jsDocInfo = rootNode.getJSDocInfo();
    JSType variableType = jsDocInfo.getDeclaredTypes().get(0);

    creator.declareVar(variableName, variableNode, rootNode, null, variableType, rootNode);

    Var variable = scope.getVar(variableName);
    assertEquals(variableType, variable.getType());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDeclareVariableWithInvalidNodeType() {
    AbstractCompiler compiler = new AbstractCompiler();
    SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);

    Scope parent = null;
    Node rootNode = Node.createTree("function foo() {}");
    creator.createScope(rootNode, parent);

    String variableName = "x";
    Node invalidNode = Node.createTree("1");

    creator.declareVar(variableName, invalidNode, rootNode, null, null, rootNode);
  }

  @Test
  public void testHandleDuplicateVariable() {
    AbstractCompiler compiler = new AbstractCompiler();
    SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
    DefaultRedeclarationHandler redeclarationHandler = new DefaultRedeclarationHandler();
    creator.redeclarationHandler = redeclarationHandler;

    Scope parent = null;
    Node rootNode = Node.createTree("function foo() { var x = 1; var x = 2; }");
    Scope scope = creator.createScope(rootNode, parent);

    String variableName = "x";

    creator.declareVar(variableName, rootNode.getFirstChild(), rootNode, null, null, rootNode);
    creator.declareVar(variableName, rootNode.getChild(1), rootNode, null, null, rootNode);

    assertTrue(scope.containsError());
    assertEquals(1, scope.getErrorCount());
    assertEquals(DiagnosticType.JSC_VAR_MULTIPLY_DECLARED_ERROR,
                 scope.getError(0).getType());
  }

  @Test
  public void testHandleDuplicateVariableWithAllowDupe() {
    AbstractCompiler compiler = new AbstractCompiler();
    SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
    DefaultRedeclarationHandler redeclarationHandler = new DefaultRedeclarationHandler();
    creator.redeclarationHandler = redeclarationHandler;

    Scope parent = null;
    Node rootNode = Node.createTree("function foo() { /** @suppress {duplicate} */ var x = 1; var x = 2; }");
    Scope scope = creator.createScope(rootNode, parent);

    String variableName = "x";

    creator.declareVar(variableName, rootNode.getFirstChild(), rootNode, null, null, rootNode);
    creator.declareVar(variableName, rootNode.getChild(1), rootNode, null, null, rootNode);

    assertFalse(scope.containsError());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testHandleDuplicateVariableWithInvalidNode() {
    AbstractCompiler compiler = new AbstractCompiler();
    SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
    DefaultRedeclarationHandler redeclarationHandler = new DefaultRedeclarationHandler();
    creator.redeclarationHandler = redeclarationHandler;

    Scope parent = null;
    Node rootNode = Node.createTree("function foo() {}");
    Scope scope = creator.createScope(rootNode, parent);

    String variableName = "x";
    Node invalidNode = Node.createTree("1");

    creator.declareVar(variableName, invalidNode, rootNode, null, null, rootNode);
    creator.declareVar(variableName, rootNode.getFirstChild(), rootNode, null, null, rootNode);
  }

  @Test
  public void testHandleFunctionDeclaration() {
    AbstractCompiler compiler = new AbstractCompiler();
    SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
    DefaultRedeclarationHandler redeclarationHandler = new DefaultRedeclarationHandler();
    creator.redeclarationHandler = redeclarationHandler;

    Scope parent = null;
    Node rootNode = Node.createTree("function foo() { function bar() {} }");
    Scope scope = creator.createScope(rootNode, parent);

    String functionName = "bar";

    creator.declareVar(functionName, rootNode.getChild(1).getFirstChild(), rootNode, rootNode, null, rootNode);

    Function function = scope.getFunction(functionName);
    assertEquals(functionNode -> functionNode.getName().equals("bar"), function.getNode());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testHandleFunctionDeclarationWithInvalidNode() {
    AbstractCompiler compiler = new AbstractCompiler();
    SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
    DefaultRedeclarationHandler redeclarationHandler = new DefaultRedeclarationHandler();
    creator.redeclarationHandler = redeclarationHandler;

    Scope parent = null;
    Node rootNode = Node.createTree("function foo() {}");
    Scope scope = creator.createScope(rootNode, parent);

    String functionName = "bar";

    creator.declareVar(functionName, Node.createTree("1"), rootNode, null, null, rootNode);
  }

  @Test
  public void testHandleArgumentsDeclaration() {
    AbstractCompiler compiler = new AbstractCompiler();
    SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
    DefaultRedeclarationHandler redeclarationHandler = new DefaultRedeclarationHandler();
    creator.redeclarationHandler = redeclarationHandler;

    Scope parent = null;
    Node rootNode = Node.createTree("function foo(x, y) {}");
    Scope scope = creator.createScope(rootNode, parent);

    String argumentName = "x";

    creator.declareVar(argumentName, rootNode.getChild(1), rootNode, null, null, rootNode);

    Var argument = scope.getVar(argumentName);
    assertEquals(argumentNode -> argumentNode.getParent().getType() == Token.FUNCTION, argument.getNode().getParent().getType());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testHandleArgumentsDeclarationWithInvalidNode() {
    AbstractCompiler compiler = new AbstractCompiler();
    SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
    DefaultRedeclarationHandler redeclarationHandler = new DefaultRedeclarationHandler();
    creator.redeclarationHandler = redeclarationHandler;

    Scope parent = null;
    Node rootNode = Node.createTree("function foo(x, y) {}");
    Scope scope = creator.createScope(rootNode, parent);

    String argumentName = "x";

    creator.declareVar(argumentName, Node.createTree("1"), rootNode, null, null, rootNode);
  }

  @Test
  public void testHandleArgumentsDeclarationWithShadowedArguments() {
    AbstractCompiler compiler = new AbstractCompiler();
    SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
    DefaultRedeclarationHandler redeclarationHandler = new DefaultRedeclarationHandler();
    creator.redeclarationHandler = redeclarationHandler;

    Scope parent = null;
    Node rootNode = Node.createTree("function foo(x) { var arguments = {} }");
    Scope scope = creator.createScope(rootNode, parent);

    String argumentName = "arguments";

    try {
      creator.declareVar(argumentName, rootNode.getChild(1), rootNode, null, null, rootNode);
      fail("Expected an IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }

    assertTrue(scope.containsError());
    assertEquals(1, scope.getErrorCount());
    assertEquals(DiagnosticType.JSC_VAR_ARGUMENTS_SHADOWED_ERROR, scope.getError(0).getType());
  }
}