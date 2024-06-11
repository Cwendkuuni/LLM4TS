import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.rhino.NodeType.ASSIGN;
import static com.google.javascript.rhino.NodeType.FUNCTION;
import static com.google.javascript.rhino.NodeType.GETPROP;
import static com.google.javascript.rhino.NodeType.THIS;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.JSDocInfo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckGlobalThis checker;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler() {};
    checker = new CheckGlobalThis(compiler, CheckLevel.QUIRKS_MODE);
  }

  @Test
  public void testFunctionNoJsDocInfo() {
    Node node = new Node(FUNCTION, null);
    boolean result = checker.shouldTraverse(NodeTraversal.emptyTraversal(), node, null);
    assertTrue(result);
  }

  @Test
  public void testFunctionWithJsDocConstructor() {
    Node node = new Node(FUNCTION, null);
    Node jsDoc = new Node(JSDocInfo.constructorNode(null, "constructor"), null);
    node.setFirstChild(jsDoc);
    boolean result = checker.shouldTraverse(NodeTraversal.emptyTraversal(), node, null);
    assertFalse(result);
  }

  @Test
  public void testFunctionWithJsDocThis() {
    Node node = new Node(FUNCTION, null);
    Node jsDoc = new Node(JSDocInfo.thisNode(null), null);
    node.setFirstChild(jsDoc);
    boolean result = checker.shouldTraverse(NodeTraversal.emptyTraversal(), node, null);
    assertFalse(result);
  }

  @Test
  public void testFunctionWithNoValidPlacesForThisAnnotation() {
    Node node = new Node(FUNCTION, null);
    boolean result = checker.shouldTraverse(NodeTraversal.emptyTraversal(), node, null);
    assertTrue(result);
  }

  @Test
  public void testAssignLeftSide() {
    Node parentAssign = new Node(ASSIGN, null);
    Node leftSide = new Node(THIS, null);
    Node rightSide = new Node(FUNCTION, null);
    parentAssign.setFirstChild(leftSide);
    leftSide.setNext(rightSide);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.QUIRKS_MODE);
    boolean result = checker.shouldReportThis(leftSide, parentAssign);
    assertTrue(result);
  }

  @Test
  public void testAssignRightSide() {
    Node parentAssign = new Node(ASSIGN, null);
    Node leftSide = new Node(FUNCTION, null);
    Node rightSide = new Node(THIS, null);
    parentAssign.setFirstChild(leftSide);
    leftSide.setNext(rightSide);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.QUIRKS_MODE);
    boolean result = checker.shouldReportThis(rightSide, parentAssign);
    assertFalse(result);
  }

  @Test
  public void testGetPropertyNotOnPrototype() {
    Node parentAssign = new Node(ASSIGN, null);
    Node leftSideAssign = new Node(ASSIGN, null);
    Node leftSideThis = new Node(THIS, null);
    Node leftSideGetProp = new Node(GETPROP, null, leftSideThis);
    Node rightSideFunction = new Node(FUNCTION, null);
    leftSideAssign.setFirstChild(leftSideThis);
    leftSideAssign.setLastChild(leftSideGetProp);
    parentAssign.setFirstChild(leftSideAssign);
    parentAssign.setNext(rightSideFunction);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.QUIRKS_MODE);
    boolean result = checker.shouldReportThis(leftSideGetProp, parentAssign);
    assertFalse(result);
  }

  @Test
  public void testGetPropertyOnPrototype() {
    Node parentAssign = new Node(ASSIGN, null);
    Node leftSideAssign = new Node(ASSIGN, null);
    Node leftSideThis = new Node(THIS, null);
    Node leftSideGetPropPrototype = new Node(GETPROP, null, leftSideThis);
    leftSideGetPropPrototype.getLastChild().setString("prototype");
    leftSideAssign.setFirstChild(leftSideThis);
    leftSideAssign.setLastChild(leftSideGetPropPrototype);
    parentAssign.setFirstChild(leftSideAssign);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.QUIRKS_MODE);
    boolean result = checker.shouldReportThis(leftSideGetPropPrototype, parentAssign);
    assertTrue(result);
  }

  @Test(expected = AssertionError.class)
  public void testNullParent() {
    Node node = new Node(FUNCTION, null);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.QUIRKS_MODE);
    checker.shouldTraverse(NodeTraversal.emptyTraversal(), node, null);
  }
}