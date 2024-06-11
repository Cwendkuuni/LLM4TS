import org.junit.Test;
import static org.junit.Assert.*;

public class MinimizeExitPointsTest {

  @Test
  public void testProcess() {
    Compiler compiler = new Compiler();
    MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(compiler);
    Node externs = new Node(Token.EXTERN_NAME);
    Node root = new Node(Token.ROOT);
    minimizeExitPoints.process(externs, root);
  }

  @Test
  public void testTryMinimizeExits() {
    Compiler compiler = new Compiler();
    MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(compiler);
    Node n = new Node(Token.EXPR_RESULT);
    int exitType = Token.BREAK;
    String labelName = "label";
    minimizeExitPoints.tryMinimizeExits(n, exitType, labelName);
  }

  @Test
  public void testMatchingExitNode() {
    Node n = new Node(Token.RETURN);
    int type = Token.RETURN;
    String labelName = null;
    assertTrue(MinimizeExitPoints.matchingExitNode(n, type, labelName));
  }

  @Test
  public void testMoveAllFollowing() {
    Node start = new Node(Token.EXPR_RESULT);
    Node srcParent = new Node(Token.BLOCK);
    Node destParent = new Node(Token.BLOCK);
    MinimizeExitPoints.moveAllFollowing(start, srcParent, destParent);
  }

}