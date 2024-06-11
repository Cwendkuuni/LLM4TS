import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  private NodeUtil nodeUtil;

  @Before
  public void setUp() {
    nodeUtil = new NodeUtil();
  }

  @Test
  public void testIsForIn_True() {
    Node forNode = new Node(Token.FOR);
    Node child1 = new Node(Token.NAME);
    Node child2 = new Node(Token.NAME);
    Node child3 = new Node(Token.NAME);
    forNode.addChildToBack(child1);
    forNode.addChildToBack(child2);
    forNode.addChildToBack(child3);

    assertTrue(nodeUtil.isForIn(forNode));
  }

  @Test
  public void testIsForIn_False() {
    Node notForNode = new Node(Token.WHILE);

    assertFalse(nodeUtil.isForIn(notForNode));
  }

  @Test
  public void testIsStatement_True() {
    Node scriptNode = new Node(Token.SCRIPT);
    Node labelNode = new Node(Token.LABEL);
    scriptNode.addChildToBack(labelNode);

    assertTrue(nodeUtil.isStatement(labelNode));
  }

  @Test
  public void testIsStatement_False() {
    Node notStatementNode = new Node(Token.EXPR_RESULT);

    assertFalse(nodeUtil.isStatement(notStatementNode));
  }

  @Test
  public void testIsFunctionDeclaration_True() {
    Node functionNode = new Node(Token.FUNCTION);
    Node scriptNode = new Node(Token.SCRIPT);
    scriptNode.addChildToBack(functionNode);

    assertTrue(nodeUtil.isFunctionDeclaration(functionNode));
  }

  @Test
  public void testIsFunctionDeclaration_False() {
    Node notFunctionNode = new Node(Token.EXPR_RESULT);

    assertFalse(nodeUtil.isFunctionDeclaration(notFunctionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_True() {
    Node functionNode = new Node(Token.FUNCTION);
    Node scriptNode = new Node(Token.SCRIPT);
    scriptNode.addChildToBack(functionNode);

    assertTrue(nodeUtil.isHoistedFunctionDeclaration(functionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_False() {
    Node notHoistedFunctionNode = new Node(Token.EXPR_RESULT);

    assertFalse(nodeUtil.isHoistedFunctionDeclaration(notHoistedFunctionNode));
  }

}