import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Test;

import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR, new Node(), new Node(), new Node());
    assertTrue(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsForIn_notForIn() {
    Node node = new Node(Token.IF, new Node(), new Node());
    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node scriptNode = new Node(Token.SCRIPT);
    Node blockNode = new Node(Token.BLOCK);
    Node labelNode = new Node(Token.LABEL);

    assertTrue(NodeUtil.isStatement(scriptNode));
    assertTrue(NodeUtil.isStatement(blockNode));
    assertTrue(NodeUtil.isStatement(labelNode));
  }

  @Test
  public void testIsStatement_notStatement() {
    Node node = new Node(Token.IF, new Node(), new Node());
    assertFalse(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION, new Node(), new Node(), new Node());
    assertTrue(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsFunctionDeclaration_notFunctionDeclaration() {
    Node node = new Node(Token.IF, new Node(), new Node());
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = new Node(Token.SCRIPT);
    Node functionNode = new Node(Token.FUNCTION, new Node(), new Node(), new Node());

    scriptNode.addChildToFront(functionNode);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));

    Node parentFunctionNode = new Node(Token.FUNCTION, new Node(), new Node(), new Node());
    parentFunctionNode.addChildToFront(functionNode);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_notHoistedFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION, new Node(), new Node(), new Node());
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }
}