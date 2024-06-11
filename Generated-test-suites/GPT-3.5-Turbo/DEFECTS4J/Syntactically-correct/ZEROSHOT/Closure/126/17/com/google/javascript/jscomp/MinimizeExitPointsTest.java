import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.TernaryValue;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MinimizeExitPointsTest {

  private MinimizeExitPoints minimizeExitPoints;

  @Before
  public void setUp() {
    minimizeExitPoints = new MinimizeExitPoints(new AbstractCompiler());
  }

  @Test
  public void testProcess() {
    Node externs = IR.empty();
    Node root = IR.empty();
    minimizeExitPoints.process(externs, root);
    // add assertion here
  }

  @Test
  public void testVisit() {
    // add test cases for different types in visit method
  }

  @Test
  public void testTryMinimizeExits() {
    // add test cases for tryMinimizeExits method
  }

  @Test
  public void testTryMinimizeIfBlockExits() {
    // add test cases for tryMinimizeIfBlockExits method
  }

  @Test
  public void testMatchingExitNode() {
    // add test cases for matchingExitNode method
  }

  @Test
  public void testMoveAllFollowing() {
    // add test cases for moveAllFollowing method
  }

}