import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[]{}, new int[]{0, 1, 2});
    assertTrue(isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.IF, new int[]{}, new int[]{0, 1});
    assertFalse(isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.IF, new int[]{}, new int[]{0, 1});
    assertTrue(isStatement(nodeStatement));

    Node nodeNotStatement = new Node(Token.IF, new int[]{Token.IF}, new int[]{0, 1});
    assertFalse(isStatement(nodeNotStatement));

    Node nodeFunction = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 1});
    assertFalse(isStatement(nodeFunction));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[]{"foo"}, new int[]{0, 1, 2});
    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 1, 2});
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));

    Node nodeIf = new Node(Token.IF, new int[]{}, new int[]{0, 1});
    assertFalse(isFunctionDeclaration(nodeIf));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[]{"foo"}, new int[]{0, 1, 2});
    Node scriptNode = new Node(Token.SCRIPT, new int[]{}, new int[]{0});
    scriptNode.addChild(nodeFunctionDeclaration);
    assertTrue(isHoistedFunctionDeclaration(nodeFunctionDeclaration));

    Node functionNode = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 1, 2});
    Node functionDeclarationNode = new Node(Token.FUNCTION, new int[]{"foo"}, new int[]{0, 1, 2});
    Node blockNode = new Node(Token.BLOCK, new int[]{}, new int[]{0, 1});
    blockNode.addChild(functionDeclarationNode);
    functionNode.addChild(blockNode);
    assertFalse(isHoistedFunctionDeclaration(functionNode));
  }
}