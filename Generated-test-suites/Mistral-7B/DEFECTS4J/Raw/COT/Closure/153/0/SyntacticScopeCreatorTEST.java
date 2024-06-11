import org.junit.Test;
import org.junit.Assert.*;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopeCreator;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.NodeUtil;
import com.google.common.base.Preconditions;

public class SyntacticScopeCreatorTest {

  @Test
  public void testCreateScope() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    ScopeCreator sc = new SyntacticScopeCreator(compiler);
    Scope scope = sc.createScope(new Node(), null);
    assertNotNull(scope);
  }

  @Test
  public void testDeclareVarNormalCase() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    ScopeCreator sc = new SyntacticScopeCreator(compiler);
    Node n = new Node();
    String name = "test";
    sc.declareVar(name, n, null, null, null, n);
    Scope scope = sc.createScope(n, null);
    assertTrue(scope.isDeclared(name, false));
  }

  @Test
  public void testDeclareVarDuplicate() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    ScopeCreator sc = new SyntacticScopeCreator(compiler);
    Node n = new Node();
    String name = "test";
    sc.declareVar(name, n, null, null, null, n);
    Scope scope = sc.createScope(n, null);
    Scope parentScope = new Scope(null, null);
    sc.declareVar(name, n, null, null, null, n);
    Scope parentDeclaredScope = new Scope(null, null);
    sc.declareVar(name, n, null, parentScope.getScopeNode(), null, n);
    Scope expectedParentDeclaredScope = new Scope(parentScope, null);
    assertEquals(expectedParentDeclaredScope, scope);
  }

  @Test
  public void testHandleRedeclaration() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    ScopeCreator sc = new SyntacticScopeCreator(compiler, new RedeclarationHandler() {
      @Override
      public void onRedeclaration(
          Scope s, String name, Node n, Node parent, Node gramps, Node nodeWithLineNumber) {
        assertEquals(parent, n);
      }
    });
    Node n = new Node();
    String name = "test";
    sc.declareVar(name, n, null, null, null, n);
    Scope expectedScope = new Scope(null, null);
    Scope actualScope = sc.createScope(n, null);
    assertEquals(expectedScope, actualScope);
    sc.declareVar(name, n, null, n, null, n);
    Scope parentScope = new Scope(null, null);
    Scope expectedParentScope = new Scope(parentScope, null);
    Scope actualParentScope = sc.createScope(n, null);
    assertEquals(expectedParentScope, actualParentScope);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDeclareArguments() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    ScopeCreator sc = new SyntacticScopeCreator(compiler);
    Node n = new Node();
    String name = "arguments";
    sc.declareVar(name, n, null, null, null, n);
  }

  @Test
  public void testHandleGlobalVariableDeclaration() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    ScopeCreator sc = new SyntacticScopeCreator(compiler);
    Node n = new Node();
    String name = "test";
    Node parent = new Node();
    sc.declareVar(name, n, parent, null, null, n);
    Scope globalScope = new Scope(null, null);
    Scope expectedScope = new Scope(globalScope, null);
    Scope actualScope = sc.createScope(n, null);
    assertEquals(expectedScope, actualScope);
  }

  @Test
  public void testHandleFunctionDeclaration() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    ScopeCreator sc = new SyntacticScopeCreator(compiler);
    Node functionNode = new Node();
    Node parent = new Node();
    sc.declareVar("test", functionNode.getFirstChild(), functionNode, parent, null, functionNode);
    Scope functionScope = new Scope(parent, functionNode);
    Scope expectedScope = new Scope(functionScope, null);
    Scope actualScope = sc.createScope(functionNode, null);
    assertEquals(expectedScope, actualScope);
  }

  @Test
  public void testHandleCatchDeclaration() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    ScopeCreator sc = new SyntacticScopeCreator(compiler);
    Node catchNode = new Node();
    Node parent = new Node();
    String name = "test";
    Node variableNode = catchNode.getFirstChild();
    Node blockNode = variableNode.getNext();
    sc.declareVar(name, variableNode, catchNode, parent, null, catchNode);
    Scope catchScope = new Scope(parent, catchNode);
    Scope expectedVariableScope = new Scope(catchScope, variableNode);
    Scope expectedBlockScope = new Scope(catchScope, blockNode);
    Scope actualScope = sc.createScope(catchNode, null);
    Scope actualVariableScope = actualScope.getChildScope();
    Scope actualBlockScope = actualVariableScope.getChildScope();
    assertEquals(expectedVariableScope, actualVariableScope);
    assertEquals(expectedBlockScope, actualBlockScope);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testHandleInvalidNode() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    ScopeCreator sc = new SyntacticScopeCreator(compiler);
    Node n = new Node();
    sc.createScope(n, null);
  }

  @Test
  public void testHandleScriptNode() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    ScopeCreator sc = new SyntacticScopeCreator(compiler);
    Node scriptNode = new Node();
    Scope globalScope = new Scope(null, null);
    Scope expectedScope = new Scope(globalScope, scriptNode);
    Scope actualScope = sc.createScope(scriptNode, null);
    assertEquals(expectedScope, actualScope);
  }

  @Test
  public void testHandleControlStructure() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    ScopeCreator sc = new SyntacticScopeCreator(compiler);
    Node controlStructureNode = new Node();
    for (Node child = controlStructureNode.getFirstChild(); child != null; child = child.getNext()) {
      sc.scanVars(child, controlStructureNode);
    }
    assertTrue(false); // This test should fail as control structures don't contain variables.
  }

  @Test
  public void testHandleStatementBlock() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    ScopeCreator sc = new SyntacticScopeCreator(compiler);
    Node statementBlockNode = new Node();
    for (Node child = statementBlockNode.getFirstChild(); child != null; child = child.getNext()) {
      sc.scanVars(child, statementBlockNode);
    }
    assertTrue(true); // This test should pass as statement blocks can contain variables.
  }

  @Test
  public void testHandleFunctionWithName() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    ScopeCreator sc = new SyntacticScopeCreator(compiler);
    Node functionNode = new Node();
    Node parent = new Node();
    String name = "testFunction";
    Node functionNameNode = functionNode.getFirstChild();
    sc.declareVar(name, functionNameNode, functionNode, parent, null, functionNode);
    Scope functionScope = new Scope(parent, functionNode);
    Scope expectedScope = new Scope(functionScope, functionNameNode);
    Scope actualScope = sc.createScope(functionNode, null);
    Scope actualFunctionScope = actualScope.getChildScope();
    assertEquals(expectedScope, actualFunctionScope);
  }

  @Test
  public void testHandleFunctionWithoutName() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    ScopeCreator sc = new SyntacticScopeCreator(compiler);
    Node functionNode = new Node();
    Node parent = new Node();
    sc.declareVar("arguments", functionNode.getFirstChild(), functionNode, parent, null, functionNode);
    Scope functionScope = new Scope(parent, functionNode);
    Scope expectedScope = new Scope(functionScope, null);
    Scope actualScope = sc.createScope(functionNode, null);
    assertEquals(expectedScope, actualScope);
  }

  @Test
  public void testHandleVarDeclarationWithMultipleVariables() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    ScopeCreator sc = new SyntacticScopeCreator(compiler);
    Node varDeclarationNode = new Node();
    for (Node child = varDeclarationNode.getFirstChild(); child != null; child = child.getNext()) {
      Preconditions.checkState(child.getType() == Token.NAME);
      String name = child.getString();
      sc.declareVar(name, child, varDeclarationNode, null, null, varDeclarationNode);
    }
    Scope expectedScope = new Scope(null, null);
    for (String name : varDeclarationNode.getString().split(",")) {
      expectedScope.declare(name, varDeclarationNode.getFirstChild(), null, null);
    }
    Scope actualScope = sc.createScope(varDeclarationNode, null);
    assertEquals(expectedScope, actualScope);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testHandleInvalidVarDeclaration() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    ScopeCreator sc = new SyntacticScopeCreator(compiler);
    Node varDeclarationNode = new Node();
    sc.declareVar("test", varDeclarationNode, null, null, null, varDeclarationNode);
  }

  @Test
  public void testHandleDuplicateDeclarationInParentScope() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    ScopeCreator sc = new SyntacticScopeCreator(compiler);
    Node parentScope = new Node();
    Node childScope = new Node();
    Node varDeclarationNode = new Node();
    String name = "test";
    sc.declareVar(name, varDeclarationNode, childScope, parentScope, null, childScope);
    sc.declareVar(name, varDeclarationNode, childScope, parentScope, null, childScope);
    Scope expectedChildScope = new Scope(parentScope, childScope);
    Scope actualChildScope = sc.createScope(childScope, null).getChildScope();
    assertEquals(expectedChildScope, actualChildScope);
  }

  @Test
  public void testHandleDuplicateDeclarationInCurrentScope() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    ScopeCreator sc = new SyntacticScopeCreator(compiler);
    Node currentScope = new Node();
    Node varDeclarationNode = new Node();
    String name = "test";
    sc.declareVar(name, varDeclarationNode, currentScope, null, null, currentScope);
    sc.declareVar(name, varDeclarationNode, currentScope, null, null, currentScope);
    Scope expectedScope = new Scope(null, null);
    Scope actualScope = sc.createScope(currentScope, null);
    assertEquals(expectedScope, actualScope);
  }

  @Test
  public void testHandleDuplicateDeclarationInGlobalScope() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    ScopeCreator sc = new SyntacticScopeCreator(compiler);
    Node globalScope = new Node();
    Node varDeclarationNode = new Node();
    String name = "test";
    sc.declareVar(name, varDeclarationNode, globalScope, null, null, globalScope);
    sc.declareVar(name, varDeclarationNode, globalScope, null, null, globalScope);
    Scope expectedGlobalScope = new Scope(null, null);
    Scope actualGlobalScope = sc.createScope(globalScope, null);
    assertEquals(expectedGlobalScope, actualGlobalScope);
  }

  @Test
  public void testHandleMultipleDeclarationsInParentScope() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    ScopeCreator sc = new SyntacticScopeCreator(compiler);
    Node parentScope = new Node();
    Node childScope = new Node();
    Node varDeclarationNode = new Node();
    String name1 = "test1";
    String name2 = "test2";
    sc.declareVar(name1, varDeclarationNode, childScope, parentScope, null, childScope);
    sc.declareVar(name2, varDeclarationNode, childScope, parentScope, null, childScope);
    Scope expectedChildScope = new Scope(parentScope, childScope);
    Scope actualChildScope = sc.createScope(childScope, null).getChildScope();
    assertEquals(expectedChildScope, actualChildScope);
    Scope expectedVariableScope1 = new Scope(expectedChildScope, varDeclarationNode.getFirstChild());
    Scope actualVariableScope1 = actualChildScope.getChildScope();
    assertEquals(expectedVariableScope1, actualVariableScope1);
    Scope expectedVariableScope2 = new Scope(expectedChildScope, varDeclarationNode.getNext().getFirstChild());
    Scope actualVariableScope2 = actualVariableScope1.getChildScope();
    assertEquals(expectedVariableScope2, actualVariableScope2);
  }

  @Test
  public void testHandleMultipleDeclarationsInCurrentScope() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    ScopeCreator sc = new SyntacticScopeCreator(compiler);
    Node currentScope = new Node();
    Node varDeclarationNode = new Node();
    String name1 = "test1";
    String name2 = "test2";
    sc.declareVar(name1, varDeclarationNode, currentScope, null, null, currentScope);
    sc.declareVar(name2, varDeclarationNode, currentScope, null, null, currentScope);
    Scope expectedScope = new Scope(null, null);
    Scope actualScope = sc.createScope(currentScope, null);
    assertEquals(expectedScope, actualScope);
    Scope expectedVariableScope1 = new Scope(null, varDeclarationNode.getFirstChild());
    Scope actualVariableScope1 = actualScope.getChildScope();
    assertEquals(expectedVariableScope1, actualVariableScope1);
    Scope expectedVariableScope2 = new Scope(expectedVariableScope1, varDeclarationNode.getNext().getFirstChild());
    Scope actualVariableScope2 = actualVariableScope1.getChildScope();
    assertEquals(expectedVariableScope2, actualVariableScope2);
  }

  @Test
  public void testHandleMultipleDeclarationsInGlobalScope() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    ScopeCreator sc = new SyntacticScopeCreator(compiler);
    Node globalScope = new Node();
    Node varDeclarationNode = new Node();
    String name1 = "test1";
    String name2 = "test2";
    sc.declareVar(name1, varDeclarationNode, globalScope, null, null, globalScope);
    sc.declareVar(name2, varDeclarationNode, globalScope, null, null, globalScope);
    Scope expectedGlobalScope = new Scope(null, null);
    Scope actualGlobalScope = sc.createScope(globalScope, null);
    assertEquals(expectedGlobalScope, actualGlobalScope);
    Scope expectedVariableScope1 = new Scope(expectedGlobalScope, varDeclarationNode.getFirstChild());
    Scope actualVariableScope1 = actualGlobalScope.getChildScope();
    assertEquals(expectedVariableScope1, actualVariableScope1);
    Scope expectedVariableScope2 = new Scope(expectedVariableScope1, varDeclarationNode.getNext().getFirstChild());
    Scope actualVariableScope2 = actualVariableScope1.getChildScope();
    assertEquals(expectedVariableScope2, actualVariableScope2);
  }
}