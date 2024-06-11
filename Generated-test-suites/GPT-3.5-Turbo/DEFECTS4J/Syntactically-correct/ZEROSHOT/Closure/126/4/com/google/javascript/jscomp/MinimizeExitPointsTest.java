import static org.junit.Assert.*;
import org.junit.Test;

public class MinimizeExitPointsTest {

  @Test
  public void testTryMinimizeExits() {
    MinimizeExitPoints tester = new MinimizeExitPoints();
    // Test case 1
    tester.tryMinimizeExits(n, Token.BREAK, "label");
    // Test case 2
    tester.tryMinimizeExits(n, Token.CONTINUE, null);
    // Test case 3
    tester.tryMinimizeExits(n, Token.RETURN, null);
    // Test case 4
    tester.tryMinimizeExits(n, Token.RETURN, "label");
  }

  @Test
  public void testTryMinimizeIfBlockExits() {
    MinimizeExitPoints tester = new MinimizeExitPoints();
    Node srcBlock = new Node();
    Node destBlock = new Node();
    Node ifNode = new Node();
    int exitType = Token.BREAK;
    String labelName = "label";
    // Test case 1
    tester.tryMinimizeIfBlockExits(srcBlock, destBlock, ifNode, exitType, labelName);
    // Test case 2
    exitType = Token.CONTINUE;
    tester.tryMinimizeIfBlockExits(srcBlock, destBlock, ifNode, exitType, labelName);
    // Test case 3
    exitType = Token.RETURN;
    labelName = null;
    tester.tryMinimizeIfBlockExits(srcBlock, destBlock, ifNode, exitType, labelName);
  }

  @Test
  public void testMatchingExitNode() {
    Node n = new Node();
    int type = Token.BREAK;
    String labelName = "label";
    boolean expected = true;
    boolean result = MinimizeExitPoints.matchingExitNode(n, type, labelName);
    assertEquals(expected, result);
  }

  @Test
  public void testMoveAllFollowing() {
    Node start = new Node();
    Node srcParent = new Node();
    Node destParent = new Node();
    // Test case 1
    MinimizeExitPoints.moveAllFollowing(start, srcParent, destParent);
    // Test case 2
    destParent.addChildToFront(srcParent);
    MinimizeExitPoints.moveAllFollowing(start, srcParent, destParent);
    // Test case 3
    destParent.addChildToBack(start);
    MinimizeExitPoints.moveAllFollowing(start, srcParent, destParent);
  }
}