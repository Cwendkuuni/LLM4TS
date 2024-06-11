import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private CheckGlobalThis checkGlobalThis;

  @Before
  public void setUp() {
    checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
  }

  @Test
  public void shouldNotTraverseNonGlobalContext() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);

    boolean shouldTraverse = checkGlobalThis.shouldTraverse(t, n, parent);

    Assert.assertFalse(shouldTraverse);
  }

  @Test
  public void shouldNotReportSafeUseOfThis() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);

    boolean shouldReportThis = checkGlobalThis.shouldReportThis(n, parent);

    Assert.assertFalse(shouldReportThis);
  }

  @Test
  public void shouldReportThisInAssignLhsChild() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);

    boolean shouldReportThis = checkGlobalThis.shouldReportThis(n, parent);

    Assert.assertTrue(shouldReportThis);
  }

  @Test
  public void shouldReportThisWithPropertyAccess() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);

    boolean shouldReportThis = checkGlobalThis.shouldReportThis(n, parent);

    Assert.assertTrue(shouldReportThis);
  }

  @Test
  public void shouldTraverseFunctionWithoutAnnotations() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);

    boolean shouldTraverse = checkGlobalThis.shouldTraverse(t, n, parent);

    Assert.assertTrue(shouldTraverse);
  }

  @Test
  public void shouldNotTraverseFunctionWithConstructorAnnotation() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(true);
    n.setJSDocInfo(jsDoc);

    boolean shouldTraverse = checkGlobalThis.shouldTraverse(t, n, parent);

    Assert.assertFalse(shouldTraverse);
  }

  @Test
  public void shouldNotTraverseFunctionWithThisAnnotation() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordThisType();
    n.setJSDocInfo(jsDoc);

    boolean shouldTraverse = checkGlobalThis.shouldTraverse(t, n, parent);

    Assert.assertFalse(shouldTraverse);
  }

  @Test
  public void shouldNotTraverseFunctionWithNoLogicalPlaceForThisAnnotation() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setOverride(true);
    n.setJSDocInfo(jsDoc);

    boolean shouldTraverse = checkGlobalThis.shouldTraverse(t, n, parent);

    Assert.assertFalse(shouldTraverse);
  }

  @Test
  public void shouldNotTraverseFunctionWithInterfaceAnnotation() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setInterface(true);
    n.setJSDocInfo(jsDoc);

    boolean shouldTraverse = checkGlobalThis.shouldTraverse(t, n, parent);

    Assert.assertFalse(shouldTraverse);
  }

  @Test
  public void shouldTraverseFunctionWithValidParentTypes() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    Node grandParent = new Node(Token.SCRIPT);
    parent.addChildToFront(grandParent);

    boolean shouldTraverse = checkGlobalThis.shouldTraverse(t, n, parent);

    Assert.assertTrue(shouldTraverse);
  }

  @Test
  public void shouldNotTraverseFunctionWithInvalidParentTypes() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    Node grandParent = new Node(Token.SCRIPT);
    grandParent.setString("x");
    parent.addChildToFront(grandParent);

    boolean shouldTraverse = checkGlobalThis.shouldTraverse(t, n, parent);

    Assert.assertFalse(shouldTraverse);
  }

  @Test
  public void shouldNotTraverseFunctionLendingToPrototype() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);

    boolean shouldTraverse = checkGlobalThis.shouldTraverse(t, n, parent);

    Assert.assertFalse(shouldTraverse);
  }

  @Test
  public void shouldNotReportThisInAssignLhsChildWhenOverrideParent() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    checkGlobalThis.shouldReportThis(n, parent); // Pre-set assignLhsChild
    Node grandParent = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setOverride(true);
    grandParent.setJSDocInfo(jsDoc);
    parent.addChildToFront(grandParent);

    boolean shouldReportThis = checkGlobalThis.shouldReportThis(n, parent);

    Assert.assertFalse(shouldReportThis);
  }

  @Test
  public void shouldReportThisInAssignLhsChildWhenNestedAssignments() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    checkGlobalThis.shouldReportThis(n, parent); // Pre-set assignLhsChild
    Node grandParent = new Node(Token.FUNCTION);
    Node ancestor = new Node(Token.ASSIGN,
        new Node(Token.GETPROP, new Node(Token.THIS), new Node(Token.STRING, "property")),
        new Node(Token.NAME, "c"));
    grandParent.addChildToFront(ancestor);
    parent.addChildToFront(grandParent);

    boolean shouldReportThis = checkGlobalThis.shouldReportThis(n, parent);

    Assert.assertTrue(shouldReportThis);
  }

  @Test
  public void shouldReportThisWithPropertyAccessInAssignmentToPrototype() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    Node grandParent = new Node(Token.ASSIGN,
        new Node(Token.GETPROP, new Node(Token.THIS), new Node(Token.STRING, "prototype")),
        new Node(Token.NAME, "property"));
    parent.addChildToFront(grandParent);

    boolean shouldReportThis = checkGlobalThis.shouldReportThis(n, parent);

    Assert.assertTrue(shouldReportThis);
  }
}