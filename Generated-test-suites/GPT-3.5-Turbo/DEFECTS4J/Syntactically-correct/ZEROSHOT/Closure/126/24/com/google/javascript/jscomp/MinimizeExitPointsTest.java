import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MinimizeExitPointsTest {
  
  private MinimizeExitPoints minimizeExitPoints;
  
  @Before
  public void setUp() {
    minimizeExitPoints = new MinimizeExitPoints(new AbstractCompiler());
  }
  
  @Test
  public void testProcess() {
    Node externs = new Node();
    Node root = new Node();
    minimizeExitPoints.process(externs, root);
    // Add assertion here
  }
  
  @Test
  public void testVisit_label() {
    NodeTraversal t = null;
    Node n = new Node();
    Node parent = new Node();
    minimizeExitPoints.visit(t, n, parent);
    // Add assertion here
  }
  
  @Test
  public void testVisit_for() {
    NodeTraversal t = null;
    Node n = new Node();
    Node parent = new Node();
    minimizeExitPoints.visit(t, n, parent);
    // Add assertion here
  }
  
  @Test
  public void testVisit_while() {
    NodeTraversal t = null;
    Node n = new Node();
    Node parent = new Node();
    minimizeExitPoints.visit(t, n, parent);
    // Add assertion here
  }
  
  @Test
  public void testVisit_do() {
    NodeTraversal t = null;
    Node n = new Node();
    Node parent = new Node();
    minimizeExitPoints.visit(t, n, parent);
    // Add assertion here
  }
  
  @Test
  public void testVisit_function() {
    NodeTraversal t = null;
    Node n = new Node();
    Node parent = new Node();
    minimizeExitPoints.visit(t, n, parent);
    // Add assertion here
  }
  
  @Test
  public void testTryMinimizeExits() {
    Node n = new Node();
    int exitType = Token.BREAK;
    String labelName = "label";
    minimizeExitPoints.tryMinimizeExits(n, exitType, labelName);
    // Add assertion here
  }
  
  @Test
  public void testMatchingExitNode() {
    Node n = new Node();
    int type = Token.BREAK;
    String labelName = "label";
    boolean result = minimizeExitPoints.matchingExitNode(n, type, labelName);
    // Add assertion here
  }
  
  @Test
  public void testMoveAllFollowing() {
    Node start = new Node();
    Node srcParent = new Node();
    Node destParent = new Node();
    minimizeExitPoints.moveAllFollowing(start, srcParent, destParent);
    // Add assertion here
  }
  
}