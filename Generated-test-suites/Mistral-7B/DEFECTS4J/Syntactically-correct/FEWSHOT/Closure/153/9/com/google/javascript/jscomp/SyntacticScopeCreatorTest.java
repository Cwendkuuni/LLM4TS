package com.google.javascript.jscomp;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopeCreator;
import com.google.javascript.jscomp.Util;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.JSType;

@RunWith(MockitoJUnitRunner.class)
public class SyntacticScopeCreatorTest {

  @Mock
  private AbstractCompiler compiler;

  @Mock
  private NodeUtil nodeUtil;

  @Mock
  private Scope scope;

  @InjectMocks
  private SyntacticScopeCreator scopeCreator;

  @Mock
  private RedeclarationHandler redeclarationHandler;

  @Test
  public void testCreateScope() {
    Node rootNode = Node.newNode(Token.SCRIPT);
    Scope parentScope = Node.newScope();

    Scope resultScope = scopeCreator.createScope(rootNode, parentScope);

    assertNotNull(resultScope);
    assertEquals(parentScope, resultScope.getParent());
  }

  @Test
  public void testScanRootFunctionExpression() {
    String sourceName = "test.js";

    Node functionNode = Node.newNode(Token.FUNCTION, null, null);
    functionNode.setProp(Node.SOURCENAME_PROP, sourceName);
    Node functionNameNode = Node.newNode(Token.NAME, "test");
    Node argsNode = Node.newNode(Token.LP, null);
    Node bodyNode = Node.newNode(Token.BLOCK, null);

    scopeCreator.scanRoot(functionNode, null);

    assertEquals(sourceName, scopeCreator.sourceName);
    assertEquals("test", scopeCreator.scope.getVar("test").getName().getIdent());
  }

  @Test
  public void testScanRootFunctionDeclaration() {
    String sourceName = "test.js";

    Node functionNode = Node.newNode(Token.FUNCTION, null, null);
    functionNode.setProp(Node.SOURCENAME_PROP, sourceName);
    Node functionNameNode = Node.newNode(Token.NAME, "test");
    Node argsNode = Node.newNode(Token.LP, null);
    Node varNode = Node.newNode(Token.VAR, null, null);
    Node varNameNode = Node.newNode(Token.NAME, "test");
    Node bodyNode = Node.newNode(Token.BLOCK, null);

    scopeCreator.scanRoot(functionNode, null);

    assertEquals(sourceName, scopeCreator.sourceName);
    assertEquals("test", scopeCreator.scope.getVar("test").getName().getIdent());
  }

  @Test
  public void testScanRootGlobal() {
    String sourceName = "test.js";

    Node rootNode = Node.newNode(Token.SCRIPT, null, null);
    rootNode.setProp(Node.SOURCENAME_PROP, sourceName);
    Node varNode1 = Node.newNode(Token.VAR, null, null);
    Node varNameNode1 = Node.newNode(Token.NAME, "test1");
    Node varNode2 = Node.newNode(Token.VAR, null, null);
    Node varNameNode2 = Node.newNode(Token.NAME, "test2");

    scopeCreator.scanRoot(rootNode, null);

    assertEquals(sourceName, scopeCreator.sourceName);
    assertEquals("test1", scopeCreator.scope.getVar("test1").getName().getIdent());
    assertEquals("test2", scopeCreator.scope.getVar("test2").getName().getIdent());
  }

  @Test
  public void testScanVarsFunctionExpression() {
    String sourceName = "test.js";

    Node functionNode = Node.newNode(Token.FUNCTION, null, null);
    functionNode.setProp(Node.SOURCENAME_PROP, sourceName);
    Node fnNameNode = Node.newNode(Token.NAME, "test");
    Node argsNode = Node.newNode(Token.LP, null);
    Node bodyNode = Node.newNode(Token.BLOCK, null);

    scopeCreator.scanVars(bodyNode, functionNode);
  }

  @Test
  public void testScanVarsFunctionDeclaration() {
    String sourceName = "test.js";

    Node functionNode = Node.newNode(Token.FUNCTION, null, null);
    functionNode.setProp(Node.SOURCENAME_PROP, sourceName);
    Node fnNameNode = Node.newNode(Token.NAME, "test");
    Node argsNode = Node.newNode(Token.LP, null);
    Node varNode1 = Node.newNode(Token.VAR, null, null);
    Node varNameNode1 = Node.newNode(Token.NAME, "test1");
    Node varNode2 = Node.newNode(Token.VAR, null, null);
    Node varNameNode2 = Node.newNode(Token.NAME, "test2");
    Node bodyNode = Node.newNode(Token.BLOCK, null);

    scopeCreator.scanVars(bodyNode, functionNode);

    assertEquals("test1", scopeCreator.scope.getVar("test1").getName().getIdent());
    assertEquals("test2", scopeCreator.scope.getVar("test2").getName().getIdent());
  }

  @Test
  public void testScanVarsStatementBlock() {
    String sourceName = "test.js";

    Node rootNode = Node.newNode(Token.SCRIPT, null, null);
    rootNode.setProp(Node.SOURCENAME_PROP, sourceName);
    Node varNode1 = Node.newNode(Token.VAR, null, null);
    Node varNameNode1 = Node.newNode(Token.NAME, "test1");
    Node varNode2 = Node.newNode(Token.VAR, null, null);
    Node varNameNode2 = Node.newNode(Token.NAME, "test2");
    Node statementBlockNode = Node.newNode(Token.BLOCK, null);

    scopeCreator.scanVars(statementBlockNode, rootNode);

    assertEquals("test1", scopeCreator.scope.getVar("test1").getName().getIdent());
    assertEquals("test2", scopeCreator.scope.getVar("test2").getName().getIdent());
  }

  @Test
  public void testOnRedeclarationGlobal() {
    String sourceName = "test.js";

    String name = "test";
    Node n = Node.newNode(Token.NAME, name);
    Node parent = Node.newNode(Token.CATCH, null);
    Node gramps = Node.newNode(Token.CATCH, null);
    Node nodeWithLineNumber = Node.newNode(Token.NAME, name);

    scopeCreator.redeclarationHandler.onRedeclaration(
        scope, name, n, parent, gramps, nodeWithLineNumber);

    assertNotNull(scope.getVar(name));
  }

  @Test
  public void testOnRedeclarationLocal() {
    String sourceName = "test.js";

    String name = "test";
    Node n = Node.newNode(Token.NAME, name);
    Node parent = Node.newNode(Token.CATCH, null);
    Node gramps = Node.newNode(Token.CATCH, null);
    Node nodeWithLineNumber = Node.newNode(Token.NAME, name);

    scopeCreator.declareVar(name, n, parent, gramps, null, nodeWithLineNumber);
    scopeCreator.redeclarationHandler.onRedeclaration(
        scope, name, n, parent, gramps, nodeWithLineNumber);

    assertNotNull(scope.getVar(name));
  }

  @Test
  public void testOnRedeclarationGlobalDuplicate() {
    String sourceName = "test.js";

    String name = "test";
    Node n = Node.newNode(Token.NAME, name);
    Node parent = Node.newNode(Token.CATCH, null);
    Node gramps = Node.newNode(Token.CATCH, null);
    Node nodeWithLineNumber = Node.newNode(Token.NAME, name);

    scopeCreator.declareVar(name, n, parent, gramps, null, nodeWithLineNumber);
    scopeCreator.redeclarationHandler.onRedeclaration(
        scope, name, n, parent, gramps, nodeWithLineNumber);

    assertNotNull(scope.getVar(name));
  }

  @Test
  public void testOnRedeclarationLocalDuplicate() {
    String sourceName = "test.js";

    String name = "test";
    Node n = Node.newNode(Token.NAME, name);
    Node parent = Node.newNode(Token.CATCH, null);
    Node gramps = Node.newNode(Token.CATCH, null);
    Node nodeWithLineNumber = Node.newNode(Token.NAME, name);

    scopeCreator.declareVar(name, n, null, null, null, nodeWithLineNumber);
    scopeCreator.redeclarationHandler.onRedeclaration(
        scope, name, n, parent, gramps, nodeWithLineNumber);

    assertNotNull(scope.getVar(name));
  }

  @Test
  public void testOnRedeclarationLocalArgument() {
    String sourceName = "test.js";

    String name = "arguments";
    Node n = Node.newNode(Token.NAME, name);
    Node parent = Node.newNode(Token.CATCH, null);
    Node gramps = Node.newNode(Token.CATCH, null);
    Node nodeWithLineNumber = Node.newNode(Token.NAME, name);

    scopeCreator.redeclarationHandler.onRedeclaration(
        scope, name, n, parent, gramps, nodeWithLineNumber);

    assertNull(scope.getVar(name));
  }
}

class RedeclarationHandlerMock implements RedeclarationHandler {
  @Override
  public void onRedeclaration(
      Scope s, String name, Node n, Node parent, Node gramps, Node nodeWithLineNumber) {
    // Empty implementation
  }
}

class NodeUtilMock {
  static boolean isControlStructure(Node node) {
    return false;
  }

  static boolean isFunctionExpression(Node node) {
    return false;
  }

  static boolean isStatementBlock(Node node) {
    return false;
  }
}