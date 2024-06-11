import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.*;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckGlobalThis checkGlobalThis;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
  }

  @After
  public void tearDown() {
    compiler = null;
    checkGlobalThis = null;
  }

  @Test
  public void testVisit_ThisWithPropertyAccessShouldReportThis() {
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node thisNode = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    parent.addChildToBack(thisNode);

    checkGlobalThis.visit(t, thisNode, parent);

    Assert.assertTrue(compiler.getErrorCount() > 0);
  }

  @Test
  public void testVisit_ThisOnLeftOfAssignmentShouldReportThis() {
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node thisNode = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.NAME);
    lhs.addChildToBack(thisNode);
    parent.addChildToBack(lhs);

    checkGlobalThis.visit(t, thisNode, parent);

    Assert.assertTrue(compiler.getErrorCount() > 0);
  }

  @Test
  public void testVisit_ThisOnRightOfAssignmentShouldNotReportThis() {
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node thisNode = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.NAME, new Node(Token.STRING, "prototype"));
    parent.addChildToBack(lhs);
    parent.addChildToBack(thisNode);

    checkGlobalThis.visit(t, thisNode, parent);

    Assert.assertEquals(compiler.getErrorCount(), 0);
  }
}