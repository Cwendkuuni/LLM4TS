import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;

public class CheckGlobalThisTest {

  private CheckGlobalThis checkGlobalThis;
  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    checkGlobalThis = new CheckGlobalThis(compiler, level);
  }

  @Test
  public void shouldTraverse_shouldReturnTrueWhenNodeTypeIsFunctionAndNoAnnotations() {
    NodeTraversal t = mock(NodeTraversal.class);
    Node n = mock(Node.class);
    Node parent = mock(Node.class);

    when(n.getType()).thenReturn(Token.FUNCTION);
    when(parent.getType()).thenReturn(Token.SCRIPT);

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertEquals(true, result);
  }

  @Test
  public void shouldTraverse_shouldReturnFalseWhenNodeTypeIsFunctionWithConstructorAnnotation() {
    NodeTraversal t = mock(NodeTraversal.class);
    Node n = mock(Node.class);
    Node parent = mock(Node.class);
    JSDocInfo jsDoc = mock(JSDocInfo.class);

    when(n.getType()).thenReturn(Token.FUNCTION);
    when(parent.getType()).thenReturn(Token.SCRIPT);
    when(n.getJSDocInfo()).thenReturn(jsDoc);
    when(jsDoc.isConstructor()).thenReturn(true);

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertEquals(false, result);
  }

  @Test
  public void shouldTraverse_shouldReturnFalseWhenNodeTypeIsFunctionWithThisAnnotation() {
    NodeTraversal t = mock(NodeTraversal.class);
    Node n = mock(Node.class);
    Node parent = mock(Node.class);
    JSDocInfo jsDoc = mock(JSDocInfo.class);

    when(n.getType()).thenReturn(Token.FUNCTION);
    when(parent.getType()).thenReturn(Token.SCRIPT);
    when(n.getJSDocInfo()).thenReturn(jsDoc);
    when(jsDoc.hasThisType()).thenReturn(true);

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertEquals(false, result);
  }

  @Test
  public void shouldTraverse_shouldReturnFalseWhenNodeTypeIsFunctionWithOverrideAnnotation() {
    NodeTraversal t = mock(NodeTraversal.class);
    Node n = mock(Node.class);
    Node parent = mock(Node.class);
    JSDocInfo jsDoc = mock(JSDocInfo.class);

    when(n.getType()).thenReturn(Token.FUNCTION);
    when(parent.getType()).thenReturn(Token.SCRIPT);
    when(n.getJSDocInfo()).thenReturn(jsDoc);
    when(jsDoc.isOverride()).thenReturn(true);

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertEquals(false, result);
  }

  @Test
  public void shouldTraverse_shouldReturnFalseWhenNodeTypeIsFunctionAndParentTypeIsNotBlockScriptNameAssign() {
    NodeTraversal t = mock(NodeTraversal.class);
    Node n = mock(Node.class);
    Node parent = mock(Node.class);

    when(n.getType()).thenReturn(Token.FUNCTION);
    when(parent.getType()).thenReturn(Token.EMPTY);

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertEquals(false, result);
  }

  @Test
  public void shouldTraverse_shouldReturnTrueWhenParentTypeIsAssignAndNodeIsLeftSideOfAssignment() {
    NodeTraversal t = mock(NodeTraversal.class);
    Node n = mock(Node.class);
    Node parent = mock(Node.class);
    Node lhs = mock(Node.class);

    when(n.getType()).thenReturn(Token.THIS);
    when(parent.getType()).thenReturn(Token.ASSIGN);
    when(parent.getFirstChild()).thenReturn(lhs);
    when(lhs.getNext()).thenReturn(n);
    checkGlobalThis.shouldTraverse(t, n, parent);

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertEquals(true, result);
  }

  @Test
  public void shouldTraverse_shouldReturnFalseWhenParentTypeIsAssignAndNodeIsRightSideOfAssignmentWithPrototypePropertyAssignment() {
    NodeTraversal t = mock(NodeTraversal.class);
    Node n = mock(Node.class);
    Node parent = mock(Node.class);
    Node lhs = mock(Node.class);

    when(n.getType()).thenReturn(Token.THIS);
    when(parent.getType()).thenReturn(Token.ASSIGN);
    when(parent.getFirstChild()).thenReturn(lhs);
    when(lhs.getType()).thenReturn(Token.GETPROP);
    when(lhs.getLastChild()).thenReturn(mock(Node.class));
    when(lhs.getLastChild().getString()).thenReturn("prototype");
    checkGlobalThis.shouldTraverse(t, n, parent);

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertEquals(false, result);
  }

  @Test
  public void shouldTraverse_shouldReturnFalseWhenParentTypeIsAssignAndNodeIsRightSideOfAssignmentWithPrototypeSubPropertyAssignment() {
    NodeTraversal t = mock(NodeTraversal.class);
    Node n = mock(Node.class);
    Node parent = mock(Node.class);
    Node lhs = mock(Node.class);

    when(n.getType()).thenReturn(Token.THIS);
    when(parent.getType()).thenReturn(Token.ASSIGN);
    when(parent.getFirstChild()).thenReturn(lhs);
    when(lhs.getType()).thenReturn(Token.GETPROP);
    when(lhs.getLastChild()).thenReturn(mock(Node.class));
    when(lhs.getLastChild().getString()).thenReturn("property.prototype.subproperty");
    checkGlobalThis.shouldTraverse(t, n, parent);

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertEquals(false, result);
  }

  @Test
  public void visit_shouldReportErrorWhenNodeTypeIsThisAndShouldReportThisReturnsTrue() {
    NodeTraversal t = mock(NodeTraversal.class);
    Node n = mock(Node.class);
    Node parent = mock(Node.class);
    DiagnosticType diagnosticType = mock(DiagnosticType.class);

    when(n.getType()).thenReturn(Token.THIS);
    when(checkGlobalThis.shouldReportThis(n, parent)).thenReturn(true);
    when(compiler.report(t.makeError(n, CheckLevel.WARNING, diagnosticType))).thenReturn(true);

    checkGlobalThis.visit(t, n, parent);

    // verify that the compiler's report method is called with the expected parameters
    verify(compiler).report(t.makeError(n, CheckLevel.WARNING, diagnosticType));
  }

  @Test
  public void visit_shouldNotReportErrorWhenNodeTypeIsThisAndShouldReportThisReturnsFalse() {
    NodeTraversal t = mock(NodeTraversal.class);
    Node n = mock(Node.class);
    Node parent = mock(Node.class);

    when(n.getType()).thenReturn(Token.THIS);
    when(checkGlobalThis.shouldReportThis(n, parent)).thenReturn(false);

    checkGlobalThis.visit(t, n, parent);

    // verify that the compiler's report method is not called
    verify(compiler, never()).report(any());
  }

  @Test
  public void visit_shouldSetAssignLhsChildToNullWhenNodeIsAssignLhsChild() {
    NodeTraversal t = mock(NodeTraversal.class);
    Node n = mock(Node.class);
    Node parent = mock(Node.class);
    Node assignLhsChild = mock(Node.class);

    when(n.getType()).thenReturn(Token.THIS);
    when(checkGlobalThis.shouldReportThis(n, parent)).thenReturn(false);
    when(n == assignLhsChild).thenReturn(true);
    checkGlobalThis.visit(t, n, parent);

    FieldUtils.writeField(checkGlobalThis, "assignLhsChild", assignLhsChild, true);

    checkGlobalThis.visit(t, n, parent);

    // verify that assignLhsChild is set to null
    assertEquals(null, FieldUtils.readField(checkGlobalThis, "assignLhsChild"));
  }

}