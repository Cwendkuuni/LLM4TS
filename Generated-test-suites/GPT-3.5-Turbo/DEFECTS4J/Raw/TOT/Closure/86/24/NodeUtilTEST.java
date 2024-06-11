import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn_normalCase() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.INIT));
    node.addChildToBack(new Node(Token.CONDITION));
    node.addChildToBack(new Node(Token.INCREMENT));

    assertTrue(NodeUtil.isForIn(node));
  }
  
  @Test
  public void testIsForIn_emptyNode() {
    Node node = new Node(Token.FOR);
    
    assertFalse(NodeUtil.isForIn(node));
  }
  
  @Test
  public void testIsForIn_incorrectType() {
    Node node = new Node(Token.FUNCTION);
    node.addChildToBack(new Node(Token.INIT));
    node.addChildToBack(new Node(Token.CONDITION));
    node.addChildToBack(new Node(Token.INCREMENT));

    assertFalse(NodeUtil.isForIn(node));
  }
  
  @Test
  public void testIsStatement_scriptParent() {
    Node node = new Node(Token.NAME);
    node.getParent().setType(Token.SCRIPT);

    assertTrue(NodeUtil.isStatement(node));
  }
  
  @Test
  public void testIsStatement_blockParent() {
    Node node = new Node(Token.NAME);
    node.getParent().setType(Token.BLOCK);

    assertTrue(NodeUtil.isStatement(node));
  }
  
  @Test
  public void testIsStatement_labelParent() {
    Node node = new Node(Token.NAME);
    node.getParent().setType(Token.LABEL);

    assertTrue(NodeUtil.isStatement(node));
  }
  
  @Test
  public void testIsStatement_nonStatementParent() {
    Node node = new Node(Token.NAME);
    node.getParent().setType(Token.FUNCTION);

    assertFalse(NodeUtil.isStatement(node));
  }
  
  @Test
  public void testIsFunctionDeclaration_functionNode() {
    Node node = new Node(Token.FUNCTION);

    assertTrue(NodeUtil.isFunctionDeclaration(node));
  }
  
  @Test
  public void testIsFunctionDeclaration_statementParent() {
    Node node = new Node(Token.FUNCTION);
    node.getParent().setType(Token.SCRIPT);

    assertTrue(NodeUtil.isFunctionDeclaration(node));
  }
  
  @Test
  public void testIsFunctionDeclaration_nonStatementParent() {
    Node node = new Node(Token.FUNCTION);
    node.getParent().setType(Token.EXPRESSION);

    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_functionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    node.setParent(parent);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_scriptParent() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(new Node(Token.OTHER));
    node.setParent(parent);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_functionParent() {
    Node node = new Node(Token.FUNCTION);
    Node parentFunction = new Node(Token.FUNCTION);
    Node parentScript = new Node(Token.SCRIPT);
    parentFunction.addChildToBack(node);
    parentScript.addChildToBack(parentFunction);
    node.setParent(parentFunction);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_nonHoistedFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.EXPRESSION);
    node.setParent(parent);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }
}