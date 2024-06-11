import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isForIn;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isFunctionDeclaration;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isHoistedFunctionDeclaration;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isStatement;

import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, new int[] {1, 2, 3});
    Node nodeNotForIn = Node.newNode(Token.IF, new int[] {1, 2});

    assertTrue("isForIn should return true for FOR-IN statement", isForIn(nodeForIn));
    assertTrue("isForIn should return false for other statements", !isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = Node.newNode(Token.SCRIPT, new int[] {});
    Node nodeFunction = Node.newNode(Token.FUNCTION, new int[] {1, 2});
    Node nodeExpression = Node.newNode(Token.IDENT, new int[] {1});

    assertTrue("isStatement should return true for script and block statements",
        isStatement(nodeScript) && isStatement(nodeScript.getFirstChild()));
    assertTrue("isStatement should return true for function declarations",
        isStatement(nodeFunction) && isFunctionDeclaration(nodeFunction));
    assertTrue("isStatement should return false for expressions", !isStatement(nodeExpression));

    Preconditions.checkArgument(nodeScript.getParent() == null);
    Preconditions.checkArgument(nodeFunction.getParent() != null);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[] {1, 2, 3});
    Node nodeFunctionExpression = Node.newNode(Token.FUNCTION, new int[] {1, 2, 3, 4});

    assertTrue("isFunctionDeclaration should return true for function declarations",
        isFunctionDeclaration(nodeFunctionDeclaration));
    assertTrue("isFunctionDeclaration should return false for function expressions",
        !isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = Node.newNode(Token.SCRIPT, new int[] {});
    Node nodeFunctionDeclarationInScript = Node.newNode(Token.FUNCTION, new int[] {1, 2, 3});
    Node nodeFunctionDeclarationInFunction = Node.newNode(Token.FUNCTION, new int[] {1, 2});
    Node nodeFunctionDeclarationInBlock = Node.newNode(Token.BLOCK, new int[] {});
    Node nodeFunctionDeclarationInBlockChild = Node.newNode(Token.FUNCTION, new int[] {1, 2, 3});

    assertTrue("isHoistedFunctionDeclaration should return true for function declarations in script",
        isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));
    assertTrue("isHoistedFunctionDeclaration should return false for function declarations in function",
        !isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
    assertTrue("isHoistedFunctionDeclaration should return false for function declarations in block",
        !isHoistedFunctionDeclaration(nodeFunctionDeclarationInBlock));
    assertTrue("isHoistedFunctionDeclaration should return false for function declarations in block child",
        !isHoistedFunctionDeclaration(nodeFunctionDeclarationInBlockChild));
  }
}