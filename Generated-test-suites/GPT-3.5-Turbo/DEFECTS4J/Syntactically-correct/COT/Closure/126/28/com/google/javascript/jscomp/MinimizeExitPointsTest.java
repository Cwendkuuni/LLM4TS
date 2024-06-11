
import static org.junit.Assert.*;
import org.junit.Test;
import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.*;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.TernaryValue;

public class MinimizeExitPointsTest {

  @Test
  public void testTryMinimizeExits_ExitNode() {
    Node n = IR.returnNode();
    int exitType = Token.RETURN;
    String labelName = null;
    MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(null);

    minimizeExitPoints.tryMinimizeExits(n, exitType, labelName);

    assertEquals("Exit node should be removed", null, n.getParent());
  }

  @Test
  public void testTryMinimizeExits_IfNode() {
    Node n = new Node(Token.IF);
    Node trueBlock = IR.block();
    Node falseBlock = IR.block();
    n.addChildToBack(IR.bool(true));
    n.addChildToBack(trueBlock);
    n.addChildToBack(falseBlock);
    int exitType = Token.BREAK;
    String labelName = null;
    MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(null);

    minimizeExitPoints.tryMinimizeExits(n, exitType, labelName);

    assertEquals("If node should have no changes", trueBlock, n.getFirstChild().getNext());
    assertEquals("True block should have no changes", trueBlock, n.getFirstChild());
    assertEquals("False block should have no changes", falseBlock, n.getLastChild());
  }

  @Test
  public void testTryMinimizeExits_TryNode() {
    Node n = new Node(Token.TRY);
    Node tryBlock = IR.block();
    Node catchNodes = IR.catchNode(IR.name("e"), IR.block());
    Node finallyBlock = IR.block();
    n.addChildToBack(tryBlock);
    n.addChildToBack(catchNodes);
    n.addChildToBack(finallyBlock);
    int exitType = Token.BREAK;
    String labelName = null;
    MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(null);

    minimizeExitPoints.tryMinimizeExits(n, exitType, labelName);

    assertEquals("Try node should have no changes", tryBlock, n.getFirstChild());
    assertEquals("Catch nodes should have no changes", catchNodes, n.getSecondChild());
    assertEquals("Finally block should have no changes", finallyBlock, n.getLastChild());
  }

  @Test
  public void testTryMinimizeExits_LabelNode() {
    Node n = new Node(Token.LABEL);
    Node labelBlock = IR.block();
    n.addChildToBack(IR.string("label"));
    n.addChildToBack(labelBlock);
    int exitType = Token.CONTINUE;
    String labelName = "label";
    MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(null);

    minimizeExitPoints.tryMinimizeExits(n, exitType, labelName);

    assertEquals("Label node should have no changes", labelBlock, n.getLastChild());
  }
}
