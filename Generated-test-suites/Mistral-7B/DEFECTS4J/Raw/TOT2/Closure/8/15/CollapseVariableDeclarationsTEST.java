import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CollapseVariableDeclarations.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;

import org.junit.Test;

public class CollapseVariableDeclarationsTest {

  @Test
  public void testCollapseSingleVariableDeclaration() {
    Compiler compiler = new Compiler();
    CollapseVariableDeclarations collapseVariableDeclarations = new CollapseVariableDeclarations(compiler);

    Node root = new Node(Token.PROGRAM);
    Node varNode = new Node(Token.VAR, "a");
    varNode.setJSDocInfo(new JSDocInfo().withName("testVariable"));
    root.addChildToBack(varNode);

    compiler.process(null, root);
    assertEquals(1, root.getChildCount());

    Node collapsedVarNode = new Node(Token.VAR, "a");
    collapsedVarNode.setJSDocInfo(varNode.getJSDocInfo());
    collapsedVarNode.addChildToBack(varNode.getLastChild());

    Node expectedRoot = new Node(Token.PROGRAM);
    expectedRoot.addChildToBack(collapsedVarNode);

    assertEquals(expectedRoot.toString(), root.toString());
  }

  @Test
  public void testCollapseMultipleVariableDeclarations() {
    Compiler compiler = new Compiler();
    CollapseVariableDeclarations collapseVariableDeclarations = new CollapseVariableDeclarations(compiler);

    Node root = new Node(Token.PROGRAM);
    Node varNode1 = new Node(Token.VAR, "a");
    Node varNode2 = new Node(Token.VAR, "b");
    root.addChildToBack(varNode1);
    root.addChildToBack(varNode2);

    compiler.process(null, root);
    assertEquals(1, root.getChildCount());

    Node collapsedVarNode = new Node(Token.VAR);
    collapsedVarNode.setJSDocInfo(new JSDocInfo().withName("testVariable1,testVariable2"));
    collapsedVarNode.addChildToBack(varNode1);
    collapsedVarNode.addChildToBack(varNode2);

    Node expectedRoot = new Node(Token.PROGRAM);
    expectedRoot.addChildToBack(collapsedVarNode);

    assertEquals(expectedRoot.toString(), root.toString());
  }

  @Test
  public void testCollapseVariableDeclarationWithAssignment() {
    Compiler compiler = new Compiler();
    CollapseVariableDeclarations collapseVariableDeclarations = new CollapseVariableDeclarations(compiler);

    Node root = new Node(Token.PROGRAM);
    Node varNode1 = new Node(Token.VAR, "a");
    Node assignNode1 = new Node(Token.ASSIGN, null, new Node(Token.NUM, 1));
    Node varNode2 = new Node(Token.VAR, "b");
    Node assignNode2 = new Node(Token.ASSIGN, null, new Node(Token.NUM, 2));
    root.addChildToBack(varNode1);
    root.addChildToBack(assignNode1);
    root.addChildToBack(varNode2);
    root.addChildToBack(assignNode2);

    compiler.process(null, root);
    assertEquals(1, root.getChildCount());

    Node collapsedVarNode = new Node(Token.VAR);
    collapsedVarNode.setJSDocInfo(new JSDocInfo().withName("testVariable1,testVariable2"));
    collapsedVarNode.addChildToBack(varNode1);
    collapsedVarNode.addChildToBack(assignNode1);
    collapsedVarNode.addChildToBack(varNode2);
    collapsedVarNode.addChildToBack(assignNode2);

    Node expectedRoot = new Node(Token.PROGRAM);
    expectedRoot.addChildToBack(collapsedVarNode);

    assertEquals(expectedRoot.toString(), root.toString());
  }

  @Test
  public void testCollapseVariableDeclarationsWithIfStatement() {
    Compiler compiler = new Compiler();
    CollapseVariableDeclarations collapseVariableDeclarations = new CollapseVariableDeclarations(compiler);

    Node root = new Node(Token.PROGRAM);
    Node varNode1 = new Node(Token.VAR, "a");
    Node ifNode = new Node(Token.IF, null, null);
    Node varNode2 = new Node(Token.VAR, "b");
    root.addChildToBack(varNode1);
    root.addChildToBack(ifNode);
    root.addChildToBack(varNode2);

    compiler.process(null, root);
    assertEquals(1, root.getChildCount());

    Node expectedRoot = new Node(Token.PROGRAM);
    expectedRoot.addChildToBack(root);

    assertEquals(expectedRoot.toString(), root.toString());
  }

  @Test(expected = AssertionError.class)
  public void testCollapseVariableDeclarationsWithInvalidNode() {
    Compiler compiler = new Compiler();
    CollapseVariableDeclarations collapseVariableDeclarations = new CollapseVariableDeclarations(compiler);

    Node root = new Node(Token.PROGRAM);
    Node varNode1 = new Node(Token.VAR, "a");
    Node ifNode = new Node(Token.IF, null, null);
    Node varNode2 = new Node(Token.VAR, "b");
    root.addChildToBack(varNode1);
    root.addChildToBack(ifNode);
    root.addChildToBack(new Node(Token.NUM, 1)); // Invalid node
    root.addChildToBack(varNode2);

    compiler.process(null, root);

    fail("Expected AssertionError");
  }

  @Test
  public void testCollapseVariableDeclarationsWithDifferentScopes() {
    Compiler compiler = new Compiler();
    CollapseVariableDeclarations collapseVariableDeclarations = new CollapseVariableDeclarations(compiler);

    Node root = new Node(Token.PROGRAM);
    Node functionNode = new Node(Token.FUNCTION, null, null);
    root.addChildToBack(functionNode);
    Node varNode1 = new Node(Token.VAR, "a");
    Node assignNode1 = new Node(Token.ASSIGN, null, new Node(Token.NUM, 1));
    functionNode.addChildToBack(varNode1);
    functionNode.addChildToBack(assignNode1);
    Node varNode2 = new Node(Token.VAR, "b");
    Node assignNode2 = new Node(Token.ASSIGN, null, new Node(Token.NUM, 2));
    root.addChildToBack(varNode2);
    root.addChildToBack(assignNode2);

    compiler.process(null, root);
    assertEquals(2, root.getChildCount());

    Node expectedFunctionNode = new Node(Token.FUNCTION, null, null);
    Node collapsedVarNode = new Node(Token.VAR);
    collapsedVarNode.setJSDocInfo(new JSDocInfo().withName("testVariable1"));
    collapsedVarNode.addChildToBack(varNode1);
    collapsedVarNode.addChildToBack(assignNode1);
    expectedFunctionNode.addChildToBack(collapsedVarNode);

    Node collapsedRoot = new Node(Token.PROGRAM);
    collapsedRoot.addChildToBack(expectedFunctionNode);
    Node collapsedVarNode2 = new Node(Token.VAR);
    collapsedVarNode2.setJSDocInfo(new JSDocInfo().withName("testVariable2"));
    collapsedVarNode2.addChildToBack(varNode2);
    collapsedVarNode2.addChildToBack(assignNode2);
    collapsedRoot.addChildToBack(collapsedVarNode2);

    assertEquals(collapsedRoot.toString(), root.toString());
  }

  @Test
  public void testCollapseVariableDeclarationsWithSameNameButDifferentScopes() {
    Compiler compiler = new Compiler();
    CollapseVariableDeclarations collapseVariableDeclarations = new CollapseVariableDeclarations(compiler);

    Node root = new Node(Token.PROGRAM);
    Node functionNode = new Node(Token.FUNCTION, null, null);
    root.addChildToBack(functionNode);
    Node varNode1 = new Node(Token.VAR, "a");
    Node assignNode1 = new Node(Token.ASSIGN, null, new Node(Token.NUM, 1));
    functionNode.addChildToBack(varNode1);
    functionNode.addChildToBack(assignNode1);
    Node varNode2 = new Node(Token.VAR, "a");
    Node assignNode2 = new Node(Token.ASSIGN, null, new Node(Token.NUM, 2));
    root.addChildToBack(varNode2);
    root.addChildToBack(assignNode2);

    compiler.process(null, root);
    assertEquals(2, root.getChildCount());

    Node expectedFunctionNode = new Node(Token.FUNCTION, null, null);
    Node collapsedVarNode = new Node(Token.VAR);
    collapsedVarNode.setJSDocInfo(new JSDocInfo().withName("testVariable1"));
    collapsedVarNode.addChildToBack(varNode1);
    collapsedVarNode.addChildToBack(assignNode1);
    expectedFunctionNode.addChildToBack(collapsedVarNode);

    Node collapsedRoot = new Node(Token.PROGRAM);
    collapsedRoot.addChildToBack(expectedFunctionNode);
    collapsedRoot.addChildToBack(new Node(Token.VAR, "a")); // Different variable
    collapsedRoot.addChildToBack(assignNode2);

    assertEquals(collapsedRoot.toString(), root.toString());
  }
}