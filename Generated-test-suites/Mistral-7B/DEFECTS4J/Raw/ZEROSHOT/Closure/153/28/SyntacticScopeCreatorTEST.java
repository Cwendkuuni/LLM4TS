import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopeCreator;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;

public class SyntacticScopeCreatorTest {

  private AbstractCompiler compiler;
  private Diagnostics diagnostics;
  private ScopeCreator syntacticScopeCreator;
  private Scope parentScope;
  private Node rootNode;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    diagnostics = mock(Diagnostics.class);
    parentScope = mock(Scope.class);
    syntacticScopeCreator = new SyntacticScopeCreator(compiler);
    syntacticScopeCreator = new SyntacticScopeCreator(compiler, null);
    rootNode = Node.newNode(Token.SCRIPT, null);
  }

  @Test
  public void testCreateGlobalScope() {
    Scope globalScope = syntacticScopeCreator.createScope(rootNode, null);
    assertNotNull(globalScope);
  }

  @Test
  public void testCreateFunctionScope() {
    Node functionNode = Node.newNode(Token.FUNCTION, null);
    Node argNode = Node.newNode(Token.NAME, "arg");
    Node bodyNode = Node.newNode(Token.BLOCK, null);

    Node functionDeclarationNode =
        Node.newNode(Token.VAR, null, argNode, bodyNode);

    functionNode.setFirstChild(argNode);
    functionNode.setLastChild(functionDeclarationNode);

    Scope functionScope = syntacticScopeCreator.createScope(functionNode, parentScope);
    assertNotNull(functionScope);

    Var argVar = functionScope.getVar("arg");
    assertNotNull(argVar);
  }

  @Test(expected = AssertionError.class)
  public void testCreateFunctionScopeWithDuplicateArgument() {
    Node functionNode = Node.newNode(Token.FUNCTION, null);
    Node argNode1 = Node.newNode(Token.NAME, "arg");
    Node argNode2 = Node.newNode(Token.NAME, "arg");
    Node bodyNode = Node.newNode(Token.BLOCK, null);

    Node functionDeclarationNode =
        Node.newNode(Token.VAR, null, argNode1, bodyNode);

    functionNode.setFirstChild(argNode1);
    functionNode.setLastChild(functionDeclarationNode);

    Scope functionScope = syntacticScopeCreator.createScope(functionNode, parentScope);
  }

  @Test
  public void testCreateFunctionScopeWithArgumentVariableAndFunctionVariable() {
    Node functionNode = Node.newNode(Token.FUNCTION, null);
    Node argNode = Node.newNode(Token.NAME, "arg");
    Node varNameNode = Node.newNode(Token.NAME, "varName");
    Node functionVariableNode = Node.newNode(Token.VAR, null, varNameNode);
    Node bodyNode = Node.newNode(Token.BLOCK, null);

    Node functionDeclarationNode =
        Node.newNode(Token.VAR, null, argNode, functionVariableNode, bodyNode);

    functionNode.setFirstChild(argNode);
    functionNode.setLastChild(functionDeclarationNode);

    Scope functionScope = syntacticScopeCreator.createScope(functionNode, parentScope);
    assertNotNull(functionScope);

    Var argVar = functionScope.getVar("arg");
    assertNotNull(argVar);

    Var varNameVar = functionScope.getVar("varName");
    assertNotNull(varNameVar);
  }

  @Test
  public void testCreateFunctionScopeWithDuplicateFunctionVariable() {
    Node functionNode = Node.newNode(Token.FUNCTION, null);
    Node varNameNode1 = Node.newNode(Token.NAME, "varName");
    Node varNameNode2 = Node.newNode(Token.NAME, "varName");
    Node bodyNode = Node.newNode(Token.BLOCK, null);

    Node functionDeclarationNode =
        Node.newNode(Token.VAR, null, varNameNode1, bodyNode);

    functionNode.setFirstChild(varNameNode1);
    functionNode.setLastChild(functionDeclarationNode);

    Scope functionScope = syntacticScopeCreator.createScope(functionNode, parentScope);

    verify(compiler).report(any());
  }

  @Test
  public void testCreateFunctionScopeWithGlobalVariable() {
    Node functionNode = Node.newNode(Token.FUNCTION, null);
    Node argNode = Node.newNode(Token.NAME, "arg");
    Node varNameNode = Node.newNode(Token.NAME, "varName");
    Node functionVariableNode = Node.newNode(Token.VAR, null, varNameNode);
    Node bodyNode = Node.newNode(Token.BLOCK, null);
    Node globalVarNode =
        Node.newNode(Token.VAR, null, Node.newNode(Token.NAME, "varName"));

    Node functionDeclarationNode =
        Node.newNode(Token.VAR, null, argNode, functionVariableNode, bodyNode);

    functionNode.setFirstChild(argNode);
    functionNode.setLastChild(functionDeclarationNode);

    Scope functionScope = syntacticScopeCreator.createScope(functionNode, parentScope);

    Var argVar = functionScope.getVar("arg");
    assertNotNull(argVar);

    Var globalVar = functionScope.getVar("varName");
    assertNotNull(globalVar);

    Var functionVar = functionScope.getVar("varName");
    assertNotNull(functionVar);

    assertEquals(functionVar.getParentNode(), functionNode);
    assertEquals(globalVar.getParentNode(), rootNode);
  }

  @Test
  public void testCreateFunctionScopeWithJSDoc() {
    Node functionNode = Node.newNode(Token.FUNCTION, null);
    Node argNode = Node.newNode(Token.NAME, "arg");
    Node bodyNode = Node.newNode(Token.BLOCK, null);

    JSDocInfo functionJSDocInfo = new JSDocInfo();
    functionJSDocInfo.putSuppression("duplicate");
    functionNode.setJSDocInfo(functionJSDocInfo);

    Scope functionScope = syntacticScopeCreator.createScope(functionNode, parentScope);
    assertNotNull(functionScope);

    assertNull(functionScope.getVar("arg"));
  }

  @Test
  public void testCreateFunctionScopeWithArgumentsVariable() {
    Node functionNode = Node.newNode(Token.FUNCTION, null);
    Node argNode = Node.newNode(Token.NAME, "arg");
    Node bodyNode = Node.newNode(Token.BLOCK, null);

    Scope functionScope = syntacticScopeCreator.createScope(functionNode, parentScope);

    Var argumentsVar = functionScope.getVar("arguments");
    assertNotNull(argumentsVar);

    assertEquals(argumentsVar.getName(), "arguments");
    assertEquals(argumentsVar.getType(), JSType.UNDEFINED);
  }

  @Test
  public void testCreateVarScope() {
    Node varNode = Node.newNode(Token.VAR, null);
    Node nameNode = Node.newNode(Token.NAME, "varName");
    Node bodyNode = Node.newNode(Token.BLOCK, null);

    Node varDeclarationNode = Node.newNode(Token.VAR, null, nameNode, bodyNode);

    varNode.setFirstChild(nameNode);
    varNode.setLastChild(varDeclarationNode);

    Scope varScope = syntacticScopeCreator.createScope(varNode, parentScope);
    assertNotNull(varScope);

    Var varNameVar = varScope.getVar("varName");
    assertNotNull(varNameVar);
  }

  @Test
  public void testCreateVarScopeWithDuplicateVariable() {
    Node varNode = Node.newNode(Token.VAR, null);
    Node nameNode1 = Node.newNode(Token.NAME, "varName");
    Node nameNode2 = Node.newNode(Token.NAME, "varName");
    Node bodyNode = Node.newNode(Token.BLOCK, null);

    Node varDeclarationNode1 = Node.newNode(Token.VAR, null, nameNode1, bodyNode);
    Node varDeclarationNode2 = Node.newNode(Token.VAR, null, nameNode2, bodyNode);

    varNode.setFirstChild(nameNode1);
    varNode.setLastChild(varDeclarationNode1);

    Scope varScope = syntacticScopeCreator.createScope(varNode, parentScope);

    verify(compiler).report(any());
  }
}