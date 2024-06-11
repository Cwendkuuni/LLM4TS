import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {
  
  private NodeUtil nodeUtil;
  
  @Before
  public void setUp() {
    nodeUtil = new NodeUtil();
  }
  
  @Test
  public void testIsForIn() {
    Node n1 = new Node(Token.FOR, new Node(), new Node(), new Node());
    Node n2 = new Node(Token.WHILE, new Node(), new Node());
    assertTrue(nodeUtil.isForIn(n1));
    assertFalse(nodeUtil.isForIn(n2));
  }
  
  @Test
  public void testIsStatement() {
    Node scriptNode = new Node(Token.SCRIPT, new Node());
    Node blockNode = new Node(Token.BLOCK, new Node());
    Node labelNode = new Node(Token.LABEL, new Node());
    Node otherNode = new Node(Token.PLUS, new Node(), new Node());
    assertTrue(nodeUtil.isStatement(scriptNode));
    assertTrue(nodeUtil.isStatement(blockNode));
    assertTrue(nodeUtil.isStatement(labelNode));
    assertFalse(nodeUtil.isStatement(otherNode));
  }
  
  @Test
  public void testIsFunctionDeclaration() {
    Node functionNode = new Node(Token.FUNCTION, new Node(), new Node());
    Node otherNode = new Node(Token.PLUS, new Node(), new Node());
    assertTrue(nodeUtil.isFunctionDeclaration(functionNode));
    assertFalse(nodeUtil.isFunctionDeclaration(otherNode));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = new Node(Token.SCRIPT, new Node());
    Node functionNode = new Node(Token.FUNCTION, new Node(), new Node());
    Node functionDeclarationNode = new Node(Token.FUNCTION, new Node(), new Node(), scriptNode);
    Node otherNode = new Node(Token.PLUS, new Node(), new Node());
    assertFalse(nodeUtil.isHoistedFunctionDeclaration(functionNode));
    assertTrue(nodeUtil.isHoistedFunctionDeclaration(functionDeclarationNode));
    assertFalse(nodeUtil.isHoistedFunctionDeclaration(otherNode));
  }
}