import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

@RunWith(JUnit4.class)
public class MinimizeExitPointsTest {

  @Test
  public void testMinimizeExitPoints() {
    MinimizeExitPoints minimizer = new MinimizeExitPoints(null);

    // Test label case
    Node labelNode = newNode("Label_1");
    Node breakNode = newNode(Token.BREAK);
    Node block = newNode(Token.BLOCK, new Node[]{labelNode, breakNode});
    Node root = newNode(Token.BLOCK, new Node[]{block});
    minimizer.process(null, root);
    assertNull(findNodeWithName(breakNode, root));

    // Test for case of FOR loop
    Node forNode = newNode(Token.FOR);
    Node init = newNode(Token.ASSIGN, new Node[]{newNode(Token.IDENTIFIER, "i"), newNode(Token.LITERAL, 0)});
    Node condition = newNode(Token.LITERAL, 10);
    Node increment = newNode(Token.ASSIGN, new Node[]{newNode(Token.IDENTIFIER, "i"), newNode(Token.LITERAL, 1)});
    Node loopBlock = newNode(Token.BLOCK);
    Node breakNodeInLoop = newNode(Token.BREAK);
    Node continueNodeInLoop = newNode(Token.CONTINUE);

    Node forStatement = newNode(forNode, new Node[]{init, condition, increment, loopBlock});
    Node exitPointsBefore = loopBlock;

    minimizer.process(null, root);
    Node exitPointsAfter = findLastChild(loopBlock);

    assertNull(findNodeWithName(breakNodeInLoop, exitPointsBefore));
    assertNull(findNodeWithName(continueNodeInLoop, exitPointsBefore));
    assertNull(findNodeWithName(breakNodeInLoop, exitPointsAfter));
    assertNull(findNodeWithName(continueNodeInLoop, exitPointsAfter));

    // Test for case of DO-WHILE loop
    Node doNode = newNode(Token.DO);
    Node conditionDo = newNode(Token.WHILE);
    Node loopBlockDo = newNode(Token.BLOCK);
    Node exitPointsBeforeDo = loopBlockDo;

    Node doStatement = newNode(doNode, new Node[]{conditionDo, loopBlockDo});
    Node breakNodeInDoWhile = newNode(Token.BREAK);
    Node continueNodeInDoWhile = newNode(Token.CONTINUE);

    loopBlockDo.addChildToBack(breakNodeInDoWhile);
    loopBlockDo.addChildToBack(continueNodeInDoWhile);

    root.replaceChild(exitPointsBefore, doStatement);

    minimizer.process(null, root);
    exitPointsAfter = findLastChild(loopBlockDo);

    assertNull(findNodeWithName(breakNodeInDoWhile, exitPointsBefore));
    assertNull(findNodeWithName(continueNodeInDoWhile, exitPointsBefore));
    assertNull(findNodeWithName(breakNodeInDoWhile, exitPointsAfter));
    assertNull(findNodeWithName(continueNodeInDoWhile, exitPointsAfter));

    // Test for case of IF statement
    Node ifNode = newNode(Token.IF);
    Node conditionIf = newNode(Token.LITERAL, true);
    Node trueBlockIf = newNode(Token.BLOCK);
    Node falseBlockIf = newNode(Token.BLOCK);
    Node breakNodeInIf = newNode(Token.BREAK);
    Node continueNodeInIf = newNode(Token.CONTINUE);

    Node ifStatement = newNode(ifNode, new Node[]{conditionIf, trueBlockIf, falseBlockIf});
    exitPointsBefore = trueBlockIf;

    root.replaceChild(exitPointsBefore, ifStatement);

    minimizer.process(null, root);
    exitPointsAfter = findLastChild(trueBlockIf);

    assertNull(findNodeWithName(breakNodeInIf, exitPointsBefore));
    assertNull(findNodeWithName(continueNodeInIf, exitPointsBefore));
    assertNull(findNodeWithName(breakNodeInIf, exitPointsAfter));
    assertNull(findNodeWithName(continueNodeInIf, exitPointsAfter));

    // Test for case of Function
    Node functionNode = newNode(Token.FUNCTION, new Node[]{newNode(Token.IDENTIFIER, "functionName"), newNode(Token.BLOCK, new Node[0])});
    Node exitPointsBeforeFunction = findLastChild(functionNode.getLastChild());

    root.addChildToBack(functionNode);
    minimizer.process(null, root);
    exitPointsAfter = findLastChild(functionNode.getLastChild());

    assertNull(exitPointsBeforeFunction);
    assertNull(exitPointsAfter);
  }

  private Node findLastChild(Node node) {
    if (node.getLastChild() == null) {
      return null;
    }
    return findLastChild(node.getLastChild());
  }

  private Node findNodeWithName(Node nodeToFind, Node parentNode) {
    for (Node child : parentNode.children()) {
      if (child.isLabel() && child.getString().equals(nodeToFind.getString())) {
        return child;
      }
      if (child.isBlock() && findNodeWithName(nodeToFind, child) != null) {
        return child;
      }
      if (child.equals(nodeToFind)) {
        return child;
      }
      if (child != null && findNodeWithName(nodeToFind, child) != null) {
        return child;
      }
    }
    return null;
  }
}