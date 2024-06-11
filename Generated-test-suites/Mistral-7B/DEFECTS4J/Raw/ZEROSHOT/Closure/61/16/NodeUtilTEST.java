import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isForIn;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isFunctionDeclaration;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isHoistedFunctionDeclaration;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isStatement;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.jsonml.Node;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[]{}, new int[]{0, 1, 2});
    Node nodeNotForIn = new Node(Token.IF, new int[]{}, new int[]{0});

    assertTrue(isForIn(nodeForIn));
    assertFalse(isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = new Node(Token.SCRIPT, new int[]{}, new int[]{0});
    Node nodeFunction = new Node(Token.FUNCTION, new int[]{}, new int[]{0});
    Node nodeIf = new Node(Token.IF, new int[]{}, new int[]{0});
    Node nodeExpression = new Node(Token.ASSIGN, new int[]{}, new int[]{0, 1});

    assertTrue(isStatement(nodeScript));
    assertTrue(isStatement(nodeFunction));
    assertTrue(isStatement(nodeIf));
    assertFalse(isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[]{"name"}, new int[]{0, 1});
    Node nodeFunctionExpression = new Node(Token.ASSIGN, new int[]{"expr"}, new int[]{0, 1});
    Preconditions.checkState(nodeFunctionDeclaration.getChild(0).getText().equals("name"));
    Preconditions.checkState(nodeFunctionExpression.getChild(0).getType() == Token.FUNCTION);
    Node nodeIf = new Node(Token.IF, new int[]{}, new int[]{0});

    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
    assertFalse(isFunctionDeclaration(nodeIf));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = new Node(Token.SCRIPT, new int[]{}, new int[]{0});
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[]{"name"}, new int[]{0, 1});
    Node nodeFunctionDeclarationInFunction = new Node(Token.FUNCTION, new int[]{}, new int[]{0});
    Node nodeFunctionDeclarationInFunctionWithName = new Node(Token.FUNCTION, new int[]{"name"}, new int[]{0, 1});
    Preconditions.checkState(nodeFunctionDeclaration.getChild(0).getText().equals("name"));

    assertTrue(isHoistedFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
    assertTrue(isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunctionWithName));
  }
}