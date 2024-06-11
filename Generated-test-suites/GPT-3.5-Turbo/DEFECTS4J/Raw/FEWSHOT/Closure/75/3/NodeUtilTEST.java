import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {
  
  private static final Node NODE = new Node(Token.EMPTY);
  
  @Test
  public void testIsForIn() {
    Node forInNode = new Node(Token.FOR);
    forInNode.addChildToFront(new Node(Token.EMPTY));
    forInNode.addChildAfter(new Node(Token.EMPTY), null);
    assertFalse(NodeUtil.isForIn(NODE));
    assertTrue(NodeUtil.isForIn(forInNode));
  }
  
  @Test
  public void testIsStatement() {
    Node scriptNode = new Node(Token.SCRIPT);
    Node blockNode = new Node(Token.BLOCK);
    Node labelNode = new Node(Token.LABEL);
    
    scriptNode.addChildToFront(NODE);
    blockNode.addChildToFront(NODE);
    labelNode.addChildToFront(NODE);
    
    assertTrue(NodeUtil.isStatement(scriptNode));
    assertTrue(NodeUtil.isStatement(blockNode));
    assertTrue(NodeUtil.isStatement(labelNode));
    assertFalse(NodeUtil.isStatement(NODE));
  }
  
  @Test
  public void testIsFunctionDeclaration() {
    Node functionNode = new Node(Token.FUNCTION);
    Node parentScriptNode = new Node(Token.SCRIPT);
    Node parentFunctionNode = new Node(Token.FUNCTION);
    
    functionNode.addChildToFront(NODE);
    parentScriptNode.addChildToFront(NODE);
    parentFunctionNode.addChildToFront(NODE);
    
    assertTrue(NodeUtil.isFunctionDeclaration(functionNode));
    assertFalse(NodeUtil.isFunctionDeclaration(NODE));
    assertFalse(NodeUtil.isFunctionDeclaration(parentScriptNode));
    assertFalse(NodeUtil.isFunctionDeclaration(parentFunctionNode));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node functionNode = new Node(Token.FUNCTION);
    Node scriptParent = new Node(Token.SCRIPT);
    Node functionParent = new Node(Token.FUNCTION);
    
    functionNode.addChildToFront(NODE);
    scriptParent.addChildToFront(NODE);
    functionParent.addChildToFront(NODE);
    
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(NODE));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(scriptParent));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionParent));
  }
}