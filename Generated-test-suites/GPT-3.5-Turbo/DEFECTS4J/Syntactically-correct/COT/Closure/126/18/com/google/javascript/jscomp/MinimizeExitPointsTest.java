
import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.rhino.Node;

public class MinimizeExitPointsTest {
  
  @Test
  public void testProcess() {
    AbstractCompiler compiler = new AbstractCompiler();
    MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(compiler);
    Node externs = null; // replace with proper externs node
    Node root = null; // replace with proper root node
    minimizeExitPoints.process(externs, root);
    // add assertions here
  }
  
  @Test
  public void testVisit() {
    AbstractCompiler compiler = new AbstractCompiler();
    MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(compiler);
    NodeTraversal t = null; // replace with proper traversal node
    Node n = null; // replace with proper node
    Node parent = null; // replace with proper parent node
    minimizeExitPoints.visit(t, n, parent);
    // add assertions here
  }
  
  @Test
  public void testTryMinimizeExits() {
    AbstractCompiler compiler = new AbstractCompiler();
    MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(compiler);
    Node n = null; // replace with proper node
    int exitType = 0; // replace with proper exit type
    String labelName = null; // replace with proper label name
    minimizeExitPoints.tryMinimizeExits(n, exitType, labelName);
    // add assertions here
  }
  
  @Test
  public void testMatchingExitNode() {
    Node n = null; // replace with proper node
    int type = 0; // replace with proper type
    String labelName = null; // replace with proper label name
    boolean result = MinimizeExitPoints.matchingExitNode(n, type, labelName);
    // add assertions here
  }
  
  @Test
  public void testMoveAllFollowing() {
    Node start = null; // replace with proper start node
    Node srcParent = null; // replace with proper source parent node
    Node destParent = null; // replace with proper destination parent node
    MinimizeExitPoints.moveAllFollowing(start, srcParent, destParent);
    // add assertions here
  }
  
}
