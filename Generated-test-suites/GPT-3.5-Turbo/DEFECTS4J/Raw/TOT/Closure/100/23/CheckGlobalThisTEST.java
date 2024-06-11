import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.jscomp.*;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse_shouldReturnValueTrueForGlobalContext() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

    Node n = new Node(Token.FUNCTION);
    Node parent = null;
    boolean result = checkGlobalThis.shouldTraverse(null, n, parent);

    assertTrue(result);
  }

  @Test
  public void testShouldTraverse_shouldReturnValueFalseForConstructor() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

    Node n = new Node(Token.FUNCTION);
    Node parent = null;

    // Simulate constructor annotation
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(true);

    // Set function JSDocInfo to constructor JSDocInfo
    n.setJSDocInfo(jsDoc);

    boolean result = checkGlobalThis.shouldTraverse(null, n, parent);

    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_shouldReturnValueFalseForThisAnnotation() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

    Node n = new Node(Token.FUNCTION);
    Node parent = null;

    // Simulate @this annotation
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordThisType();

    // Set function JSDocInfo to @this JSDocInfo
    n.setJSDocInfo(jsDoc);

    boolean result = checkGlobalThis.shouldTraverse(null, n, parent);

    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_shouldReturnValueTrueForLeftSideAssignment() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    checkGlobalThis.visit(null, null, null);

    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToFront(new Node(Token.NAME, new Node(Token.THIS)));

    boolean result = checkGlobalThis.shouldTraverse(null, n, parent);

    assertTrue(result);
  }

  @Test
  public void testShouldTraverse_shouldReturnValueFalseForRightSideAssignmentPrototypeProperty() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);

    // Simulate assignment to prototype property
    Node lhs = new Node(Token.GETPROP, new Node(Token.NAME, new Node(Token.THIS)), new Node(Token.STRING, "prototype"));
    parent.addChildToFront(lhs);

    boolean result = checkGlobalThis.shouldTraverse(null, n, parent);

    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_shouldReturnValueFalseForRightSideAssignmentPrototypeSubproperty() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);

    // Simulate assignment to prototype subproperty
    Node lhs = new Node(Token.GETPROP, new Node(Token.NAME, new Node(Token.THIS)), new Node(Token.STRING, "prototype.subproperty"));
    parent.addChildToFront(lhs);

    boolean result = checkGlobalThis.shouldTraverse(null, n, parent);

    assertFalse(result);
  }

  @Test
  public void testVisit_shouldReportWarningForGlobalThisKeyword() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    checkGlobalThis.visit(null, new Node(Token.THIS), null);

    // Assert that a warning was reported
    assertEquals(1, compiler.getWarnings().size());
    assertEquals(DiagnosticType.warning("JSC_USED_GLOBAL_THIS"), compiler.getWarnings().get(0).getType());
  }

  @Test
  public void testVisit_shouldClearAssignLhsChildAfterVisiting() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    Node assignLhsChild = new Node(Token.NAME, new Node(Token.THIS));
    checkGlobalThis.visit(null, null, assignLhsChild);

    // Assert that assignLhsChild is cleared
    assertNull(checkGlobalThis.assignLhsChild);
  }

  @Test
  public void testShouldReportThis_shouldReturnValueTrueForLeftSideAssignment() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    checkGlobalThis.assignLhsChild = new Node(Token.NAME, new Node(Token.THIS));

    Node n = new Node(Token.THIS);
    Node parent = null;
    boolean result = checkGlobalThis.shouldReportThis(n, parent);

    assertTrue(result);
  }

  @Test
  public void testShouldReportThis_shouldReturnValueFalseForPropertyAccess() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP, new Node(Token.NAME, new Node(Token.THIS)), new Node(Token.STRING, "property"));
    boolean result = checkGlobalThis.shouldReportThis(n, parent);

    assertFalse(result);
  }

}