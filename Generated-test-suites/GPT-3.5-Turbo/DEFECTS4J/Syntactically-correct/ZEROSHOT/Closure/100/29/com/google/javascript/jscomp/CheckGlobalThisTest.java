import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.*;
import com.google.javascript.rhino.*;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckGlobalThis checkGlobalThis;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
  }

  @Test
  public void testShouldTraverse_DoesNotTraverseConstructorFunction() {
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(true);
    n.setJSDocInfo(jsDoc);
    assertFalse(checkGlobalThis.shouldTraverse(t, n, null));
  }

  @Test
  public void testShouldTraverse_DoesNotTraverseFunctionWithThisAnnotation() {
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordThisType();
    n.setJSDocInfo(jsDoc);
    assertFalse(checkGlobalThis.shouldTraverse(t, n, null));
  }

  @Test
  public void testShouldTraverse_TraversesFunctionWithoutConstructorOrThisAnnotation() {
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = new Node(Token.FUNCTION);
    assertTrue(checkGlobalThis.shouldTraverse(t, n, null));
  }

  @Test
  public void testShouldTraverse_TraversesAssignmentLeftSide() {
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToBack(n);
    assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
  }

  @Test
  public void testShouldTraverse_DoesNotTraverseAssignmentRightSideIfPrototypeProperty() {
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = new Node(Token.NAME, new Node(Token.GETPROP, new Node(Token.NAME, "foo"), new Node(Token.STRING, "prototype")));
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToBack(new Node(Token.NAME));
    parent.addChildToBack(n);
    assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
  }

  @Test
  public void testShouldTraverse_DoesNotTraverseAssignmentRightSideIfPrototypeSubproperty() {
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = new Node(Token.NAME, new Node(Token.GETPROP, new Node(Token.GETPROP, new Node(Token.NAME, "Foo"), new Node(Token.STRING, "prototype")), new Node(Token.STRING, "bar")));
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToBack(new Node(Token.NAME));
    parent.addChildToBack(n);
    assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
  }

  @Test
  public void testShouldTraverse_TraversesAssignmentRightSideWithoutPrototypePropertyOrSubproperty() {
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = new Node(Token.NAME, new Node(Token.GETPROP, new Node(Token.NAME, "foo"), new Node(Token.STRING, "bar")));
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToBack(new Node(Token.NAME));
    parent.addChildToBack(n);
    assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
  }

  @Test
  public void testVisit_DoesNotReportThisIfNotOnLeftSideOfAssignment() {
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToBack(new Node(Token.NAME));
    parent.addChildToBack(new Node());
    checkGlobalThis.visit(t, n, parent);
    assertEquals(0, compiler.getErrorCount());
  }

  @Test
  public void testVisit_ReportsThisIfOnLeftSideOfAssignment() {
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToBack(n);
    parent.addChildToBack(new Node());
    checkGlobalThis.visit(t, n, parent);
    assertEquals(1, compiler.getErrorCount());
  }

  @Test
  public void testVisit_ClearsAssignLhsChildAfterVisiting() {
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToBack(n);
    parent.addChildToBack(new Node());
    checkGlobalThis.visit(t, n, parent);
    assertNull(checkGlobalThis.getAssignLhsChild());
  }

  @Test
  public void testShouldReportThis_ReturnsFalseIfAssignLhsChildNotNull() {
    checkGlobalThis.setAssignLhsChild(new Node(Token.NAME));
    assertFalse(checkGlobalThis.shouldReportThis(new Node(Token.THIS), new Node()));
  }
  
  @Test
  public void testShouldReportThis_ReturnsFalseIfNotOnLeftSideOfAssignment() {
    assertFalse(checkGlobalThis.shouldReportThis(new Node(Token.THIS), new Node()));
  }

  @Test
  public void testShouldReportThis_ReturnsTrueIfOnLeftSideOfAssignment() {
    assertTrue(checkGlobalThis.shouldReportThis(new Node(Token.THIS), new Node(Token.ASSIGN)));
  }

  @Test
  public void testShouldReportThis_ReturnsFalseIfNoAssignLhsChild() {
    assertFalse(checkGlobalThis.shouldReportThis(new Node(Token.THIS), null));
  }
}