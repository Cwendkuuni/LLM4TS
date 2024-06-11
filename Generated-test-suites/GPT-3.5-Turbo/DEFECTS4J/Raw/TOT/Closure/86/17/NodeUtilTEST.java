import org.junit.Assert;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn_Typical() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.BLOCK));
    node.addChildToBack(new Node(Token.NAME));
    node.addChildToBack(new Node(Token.TRUE));

    boolean expectedResult = true;
    boolean actualResult = NodeUtil.isForIn(node);

    Assert.assertEquals(expectedResult, actualResult);
  }

  @Test
  public void testIsForIn_EmptyBlock() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.BLOCK));
    node.addChildToBack(new Node(Token.NAME));
    node.addChildToBack(new Node(Token.NULL));

    boolean expectedResult = true;
    boolean actualResult = NodeUtil.isForIn(node);

    Assert.assertEquals(expectedResult, actualResult);
  }

  @Test
  public void testIsForIn_InvalidNode() {
    Node node = new Node(Token.FOR);
    node.addChildrenToFront(new Node(Token.NAME));
    node.addChildrenToFront(new Node(Token.FALSE));

    boolean expectedResult = false;
    boolean actualResult = NodeUtil.isForIn(node);

    Assert.assertEquals(expectedResult, actualResult);
  }

  @Test
  public void testIsStatement_Script() {
    Node node = new Node(Token.SCRIPT);

    boolean expectedResult = true;
    boolean actualResult = NodeUtil.isStatement(node);

    Assert.assertEquals(expectedResult, actualResult);
  }

  @Test
  public void testIsStatement_Block() {
    Node node = new Node(Token.BLOCK);

    boolean expectedResult = true;
    boolean actualResult = NodeUtil.isStatement(node);

    Assert.assertEquals(expectedResult, actualResult);
  }

  @Test
  public void testIsStatement_Label() {
    Node node = new Node(Token.LABEL);

    boolean expectedResult = true;
    boolean actualResult = NodeUtil.isStatement(node);

    Assert.assertEquals(expectedResult, actualResult);
  }
  
  @Test
  public void testIsStatement_Other() {
    Node node = new Node(Token.ADD);

    boolean expectedResult = false;
    boolean actualResult = NodeUtil.isStatement(node);

    Assert.assertEquals(expectedResult, actualResult);
  }

  @Test
  public void testIsFunctionDeclaration_Typical() {
    Node node = new Node(Token.FUNCTION);
    node.addChildToFront(new Node(Token.NAME));

    boolean expectedResult = true;
    boolean actualResult = NodeUtil.isFunctionDeclaration(node);

    Assert.assertEquals(expectedResult, actualResult);
  }

  @Test
  public void testIsFunctionDeclaration_NotStatement() {
    Node node = new Node(Token.FUNCTION);
    node.addChildToFront(new Node(Token.NAME));
    
    Node parent = new Node(Token.ADD);
    parent.addChildToFront(node);
    node.setParent(parent);

    boolean expectedResult = false;
    boolean actualResult = NodeUtil.isFunctionDeclaration(node);

    Assert.assertEquals(expectedResult, actualResult);
  }

  @Test
  public void testIsHoistedFunctionDeclaration_Typical() {
    Node node = new Node(Token.FUNCTION);
    node.addChildToFront(new Node(Token.NAME));
    
    Node scriptParent = new Node(Token.SCRIPT);
    scriptParent.addChildToFront(node);
    node.setParent(scriptParent);

    boolean expectedResult = true;
    boolean actualResult = NodeUtil.isHoistedFunctionDeclaration(node);

    Assert.assertEquals(expectedResult, actualResult);
  }

  @Test
  public void testIsHoistedFunctionDeclaration_NotScopeRoot() {
    Node node = new Node(Token.FUNCTION);
    node.addChildToFront(new Node(Token.NAME));
    
    Node scriptParent = new Node(Token.SCRIPT);
    scriptParent.addChildToBack(new Node(Token.FUNCTION));
    scriptParent.addChildToBack(node);
    node.setParent(scriptParent);

    boolean expectedResult = false;
    boolean actualResult = NodeUtil.isHoistedFunctionDeclaration(node);

    Assert.assertEquals(expectedResult, actualResult);
  }

  @Test
  public void testIsHoistedFunctionDeclaration_NotFunctionParent() {
    Node node = new Node(Token.FUNCTION);
    node.addChildToFront(new Node(Token.NAME));
    
    Node scriptParent = new Node(Token.SCRIPT);
    scriptParent.addChildToFront(node);
    
    Node functionParent = new Node(Token.FUNCTION);
    functionParent.addChildToBack(scriptParent);
    scriptParent.setParent(functionParent);
    node.setParent(scriptParent);

    boolean expectedResult = false;
    boolean actualResult = NodeUtil.isHoistedFunctionDeclaration(node);

    Assert.assertEquals(expectedResult, actualResult);
  }
}