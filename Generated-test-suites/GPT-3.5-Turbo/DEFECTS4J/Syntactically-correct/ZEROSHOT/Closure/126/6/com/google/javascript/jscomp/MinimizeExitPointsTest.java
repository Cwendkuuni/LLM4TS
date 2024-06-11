import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MinimizeExitPointsTest {

  private MinimizeExitPoints minimizeExitPoints;
  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    minimizeExitPoints = new MinimizeExitPoints(compiler);
  }

  @After
  public void tearDown() {
    compiler = null;
    minimizeExitPoints = null;
  }

  @Test
  public void testProcess() {
    Node externs = IR.root();
    Node root = IR.root();
    minimizeExitPoints.process(externs, root);
    // Add your assertions here
  }

  @Test
  public void testVisit_LABEL() {
    NodeTraversal t = null;
    Node n = IR.root();
    Node parent = IR.root();
    minimizeExitPoints.visit(t, n, parent);
    // Add your assertions here
  }

  @Test
  public void testVisit_FOR() {
    NodeTraversal t = null;
    Node n = IR.root();
    Node parent = IR.root();
    minimizeExitPoints.visit(t, n, parent);
    // Add your assertions here
  }

  @Test
  public void testVisit_WHILE() {
    NodeTraversal t = null;
    Node n = IR.root();
    Node parent = IR.root();
    minimizeExitPoints.visit(t, n, parent);
    // Add your assertions here
  }

  @Test
  public void testVisit_DO() {
    NodeTraversal t = null;
    Node n = IR.root();
    Node parent = IR.root();
    minimizeExitPoints.visit(t, n, parent);
    // Add your assertions here
  }

  @Test
  public void testVisit_FUNCTION() {
    NodeTraversal t = null;
    Node n = IR.root();
    Node parent = IR.root();
    minimizeExitPoints.visit(t, n, parent);
    // Add your assertions here
  }

  @Test
  public void testTryMinimizeExits() {
    Node n = IR.root();
    int exitType = 0;
    String labelName = null;
    minimizeExitPoints.tryMinimizeExits(n, exitType, labelName);
    // Add your assertions here
  }

  @Test
  public void testTryMinimizeIfBlockExits() {
    Node srcBlock = IR.root();
    Node destBlock = IR.root();
    Node ifNode = IR.root();
    int exitType = 0;
    String labelName = null;
    minimizeExitPoints.tryMinimizeIfBlockExits(srcBlock, destBlock, ifNode, exitType, labelName);
    // Add your assertions here
  }

  @Test
  public void testMatchingExitNode() {
    Node n = IR.root();
    int type = 0;
    String labelName = null;
    boolean result = minimizeExitPoints.matchingExitNode(n, type, labelName);
    // Add your assertions here
  }

  @Test
  public void testMoveAllFollowing() {
    Node start = IR.root();
    Node srcParent = IR.root();
    Node destParent = IR.root();
    minimizeExitPoints.moveAllFollowing(start, srcParent, destParent);
    // Add your assertions here
  }
}