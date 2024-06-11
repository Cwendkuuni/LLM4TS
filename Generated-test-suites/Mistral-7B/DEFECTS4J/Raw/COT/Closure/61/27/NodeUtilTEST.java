import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  Node createNode(int type, int firstChild, int lastChild) {
    Node n = new Node();
    n.setType(type);
    n.setFirstChild(firstChild);
    n.setLastChild(lastChild);
    return n;
  }

  @Test
  public void testIsForIn() {
    Node forNode = createNode(FOR, 0, 2);
    Node notForNode = createNode(IF, 0, 0);
    assertTrue("isForIn should return true for a FOR node", NodeUtil.isForIn(forNode));
    assertFalse("isForIn should return false for a non-FOR node", NodeUtil.isForIn(notForNode));
  }

  @Test
  public void testIsStatement_statementNode() {
    Node statementNode = createNode(SCRIPT, 0, 0);
    Node notStatementNode = createNode(IF, 0, 0);
    assertTrue("isStatement should return true for a script node", NodeUtil.isStatement(statementNode));
    assertFalse("isStatement should return false for a non-statement node", NodeUtil.isStatement(notStatementNode));
  }

  @Test
  public void testIsStatement_functionDeclarationNode_inScript() {
    Node functionNode = createNode(FUNCTION, 0, 0);
    Node scriptNode = createNode(SCRIPT, 0, 0);
    Node functionInScriptNode = new Node();
    functionInScriptNode.setType(FUNCTION);
    functionInScriptNode.setParent(scriptNode);
    functionInScriptNode.setFirstChild(functionNode);
    assertTrue("isStatement should return true for a function node in a script", NodeUtil.isStatement(functionInScriptNode));
  }

  @Test
  public void testIsStatement_functionDeclarationNode_inFunction() {
    Node functionNode = createNode(FUNCTION, 0, 0);
    Node functionNodeParent = createNode(FUNCTION, 0, 0);
    Node functionInFunctionNode = new Node();
    functionInFunctionNode.setType(FUNCTION);
    functionInFunctionNode.setParent(functionNodeParent);
    functionInFunctionNode.setFirstChild(functionNode);
    assertFalse("isStatement should return false for a function node in another function", NodeUtil.isStatement(functionInFunctionNode));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsStatement_nullParentNode() {
    Node functionNode = createNode(FUNCTION, 0, 0);
    Node nullParentNode = null;
    Node functionInNullParentNode = new Node();
    functionInNullParentNode.setType(FUNCTION);
    functionInNullParentNode.setParent(nullParentNode);
    functionInNullParentNode.setFirstChild(functionNode);
    NodeUtil.isStatement(functionInNullParentNode);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = createNode(FUNCTION, 0, 0);
    Node notFunctionDeclarationNode = createNode(IF, 0, 0);
    assertTrue("isFunctionDeclaration should return true for a function declaration node", NodeUtil.isFunctionDeclaration(functionDeclarationNode));
    assertFalse("isFunctionDeclaration should return false for a non-function declaration node", NodeUtil.isFunctionDeclaration(notFunctionDeclarationNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclarationNode = createNode(FUNCTION, 0, 0);
    Node functionDeclarationNodeInFunctionNode = new Node();
    functionDeclarationNodeInFunctionNode.setType(FUNCTION);
    functionDeclarationNodeInFunctionNode.setParent(hoistedFunctionDeclarationNode);
    functionDeclarationNodeInFunctionNode.setFirstChild(hoistedFunctionDeclarationNode);
    Node functionInFunctionNode = new Node();
    functionInFunctionNode.setType(FUNCTION);
    functionInFunctionNode.setParent(functionDeclarationNodeInFunctionNode);
    functionInFunctionNode.setFirstChild(hoistedFunctionDeclarationNode);
    Node scriptNode = createNode(SCRIPT, 0, 0);
    Node functionDeclarationNodeInScriptNode = new Node();
    functionDeclarationNodeInScriptNode.setType(FUNCTION);
    functionDeclarationNodeInScriptNode.setParent(scriptNode);
    functionDeclarationNodeInScriptNode.setFirstChild(hoistedFunctionDeclarationNode);
    Node functionInScriptNode = new Node();
    functionInScriptNode.setType(FUNCTION);
    functionInScriptNode.setParent(functionDeclarationNodeInScriptNode);
    functionInScriptNode.setFirstChild(hoistedFunctionDeclarationNode);
    assertTrue("isHoistedFunctionDeclaration should return true for a function declaration node that is hoisted in a script", NodeUtil.isHoistedFunctionDeclaration(functionDeclarationNodeInScriptNode));
    assertFalse("isHoistedFunctionDeclaration should return false for a function declaration node that is not hoisted", NodeUtil.isHoistedFunctionDeclaration(functionDeclarationNodeInFunctionNode));
  }
}