import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  private Node sampleNode;

  @Before
  public void setUp() {
    // Initialize sampleNode for testing
    sampleNode = new Node(Token.FOR, new Node(Token.BLOCK), new Node(Token.EMPTY), new Node(Token.EMPTY));
  }

  @Test
  public void testIsForIn() {
    assertTrue(NodeUtil.isForIn(sampleNode));
  }

  @Test
  public void testIsStatement() {
    Node stmtNode = new Node(Token.SCRIPT, new Node(Token.BLOCK));
    assertTrue(NodeUtil.isStatement(stmtNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node funcDeclNode = new Node(Token.FUNCTION, new Node(Token.NAME));
    assertTrue(NodeUtil.isFunctionDeclaration(funcDeclNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFuncDeclNode = new Node(Token.FUNCTION, new Node(Token.NAME));
    Node parentFuncNode = new Node(Token.FUNCTION, hoistedFuncDeclNode);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFuncDeclNode));
  }
}