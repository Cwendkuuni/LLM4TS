import com.google.javascript.jscomp.NodeUtil;
import com.google.common.base.Preconditions;
import org.junit.Test;
import org.junit.Assert;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, new int[]{-1, -1, -1}, new Node[]{
      Node.newIdent("i"),
      Node.newNode(Token.KEYWORD, "in"),
      Node.newIdent("arr")
    });
    Assert.assertTrue("expected FOR-IN loop", NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newNode(Token.FOR, new int[]{-1, -1, -1}, new Node[]{
      Node.newIdent("i"),
      Node.newNode(Token.ASSIGN, new int[]{-1, -1}, new Node[]{
        Node.newIdent("i"),
        Node.newNumber(-1)
      }),
      Node.newIdent("arr")
    });
    Assert.assertFalse("expected not FOR-IN loop", NodeUtil.isForIn(nodeNotForIn));

    Node nodeFor = Node.newNode(Token.FOR, new int[]{-1, -1, -1}, new Node[]{
      Node.newIdent("i"),
      Node.newNode(Token.KEYWORD, "for"),
      Node.newIdent("j")
    });
    Assert.assertFalse("expected not FOR-IN loop", NodeUtil.isForIn(nodeFor));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newNode(Token.PRINT, new int[]{-1, -1}, new Node[]{
      Node.newString("Hello World")
    });
    Assert.assertTrue("expected statement", NodeUtil.isStatement(nodeStatement));

    Node nodeExpression = Node.newNode(Token.ADD, new int[]{-1, -1}, new Node[]{
      Node.newNumber(1),
      Node.newNumber(2)
    });
    Assert.assertFalse("expected not statement", NodeUtil.isStatement(nodeExpression));

    Node nodeFunction = Node.newNode(Token.FUNCTION, new int[]{-1, -1}, new Node[]{
      Node.newIdent("funcName"),
      Node.newBlock(new Node[]{})
    });
    Assert.assertTrue("expected statement", NodeUtil.isStatement(nodeFunction));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[]{-1, -1}, new Node[]{
      Node.newIdent("funcName"),
      Node.newBlock(new Node[]{})
    });
    Assert.assertTrue("expected function declaration", NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newNode(Token.FUNCTION, new int[]{-1, -1}, new Node[]{
      Node.newIdent("funcName"),
      Node.newBlock(new Node[]{})
    });
    Node parent = Node.newNode(Token.ASSIGN, new int[]{-1, -1}, new Node[]{
      Node.newIdent("variable"),
      nodeFunctionExpression
    });
    Assert.assertFalse("expected not function declaration", NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
    Preconditions.checkState(parent != null);
    Assert.assertFalse("expected not function declaration", NodeUtil.isFunctionDeclaration(nodeFunctionExpression.getParent()));

    Node nodeVariable = Node.newIdent("funcName");
    Assert.assertFalse("expected not function declaration", NodeUtil.isFunctionDeclaration(nodeVariable));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[]{-1, -1}, new Node[]{
      Node.newIdent("funcName"),
      Node.newBlock(new Node[]{})
    });
    Node parentScript = Node.newNode(Token.SCRIPT, new int[]{-1, -1}, new Node[]{nodeFunctionDeclaration});
    Assert.assertTrue("expected hoisted function declaration", NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionDeclarationOutsideScript = Node.newNode(Token.FUNCTION, new int[]{-1, -1}, new Node[]{
      Node.newIdent("funcName"),
      Node.newBlock(new Node[]{})
    });
    Node parentFunction = Node.newNode(Token.FUNCTION, new int[]{-1, -1}, new Node[]{
      Node.newIdent("funcNameParent"),
      Node.newBlock(new Node[]{nodeFunctionDeclarationOutsideScript})
    });
    Assert.assertFalse("expected not hoisted function declaration", NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationOutsideScript));
    Preconditions.checkState(parentFunction != null);
    Assert.assertFalse("expected not hoisted function declaration", NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationOutsideScript.getParent()));

    Node nodeVariable = Node.newIdent("funcName");
    Assert.assertFalse("expected not hoisted function declaration", NodeUtil.isHoistedFunctionDeclaration(nodeVariable));
  }
}