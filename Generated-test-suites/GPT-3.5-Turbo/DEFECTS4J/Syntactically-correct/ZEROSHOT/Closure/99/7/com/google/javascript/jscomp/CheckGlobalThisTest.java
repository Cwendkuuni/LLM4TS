import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void shouldTraverseShouldReturnTrueWhenParentIsNull() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    boolean result = checkGlobalThis.shouldTraverse(null, null, null);
    assertTrue(result);
  }

  @Test
  public void shouldTraverseShouldReturnFalseWhenNodeIsFunctionConstructor() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal(null);
    Node n = new Node(Token.FUNCTION);
    n.putProp(Node.JSDOC_INFO_PROP, new JSDocInfo().setConstructor(true));
    boolean result = checkGlobalThis.shouldTraverse(t, n, null);
    assertFalse(result);
  }

  @Test
  public void shouldTraverseShouldReturnFalseWhenNodeHasThisTypeAnnotation() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal(null);
    Node n = new Node(Token.FUNCTION);
    n.putProp(Node.JSDOC_INFO_PROP, new JSDocInfo().setThisType("type"));
    boolean result = checkGlobalThis.shouldTraverse(t, n, null);
    assertFalse(result);
  }

  @Test
  public void shouldTraverseShouldReturnFalseWhenNodeHasOverrideAnnotation() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal(null);
    Node n = new Node(Token.FUNCTION);
    n.putProp(Node.JSDOC_INFO_PROP, new JSDocInfo().setOverride(true));
    boolean result = checkGlobalThis.shouldTraverse(t, n, null);
    assertFalse(result);
  }

  @Test
  public void shouldTraverseShouldReturnFalseWhenParentTypeIsNotBlockScriptNameOrAssign() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal(null);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.TYPEOFNAME);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertFalse(result);
  }

  @Test
  public void shouldTraverseShouldReturnTrueWhenParentTypeIsBlock() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal(null);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertTrue(result);
  }

  @Test
  public void shouldTraverseShouldReturnTrueWhenParentTypeIsScript() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal(null);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertTrue(result);
  }

  @Test
  public void shouldTraverseShouldReturnTrueWhenParentTypeIsName() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal(null);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertTrue(result);
  }

  @Test
  public void shouldTraverseShouldReturnTrueWhenParentTypeIsAssign() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal(null);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertTrue(result);
  }

  @Test
  public void visitShouldReportErrorWhenNodeTypeIsThisAndShouldReportThisReturnsTrue() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal(null);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    parent.addChildToFront(n);
    boolean result = checkGlobalThis.shouldReportThis(n, parent);
    assertTrue(result);
  }

  @Test
  public void visitShouldNotReportErrorWhenNodeTypeIsThisAndShouldReportThisReturnsFalse() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal(null);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    parent.addChildToBack(n);
    boolean result = checkGlobalThis.shouldReportThis(n, parent);
    assertFalse(result);
  }

  @Test
  public void visitShouldResetAssignLhsChildToNullWhenNodeIsAssignLhsChild() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal(null);
    Node assignLhsChild = new Node(Token.THIS);
    checkGlobalThis.assignLhsChild = assignLhsChild;
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    checkGlobalThis.visit(t, n, parent);
    assertNull(checkGlobalThis.assignLhsChild);
  }
}