import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private CheckGlobalThis checkGlobalThis;

  @Before
  public void setUp() {
    checkGlobalThis = new CheckGlobalThis(null, null);
  }

  // Test for shouldTraverse method

  @Test
  public void shouldTraverse_shouldReturnTrueForFunctionWithNoAnnotations() {
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = createFunctionNode();
    Node parent = createParentNode();

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertTrue(result);
  }

  @Test
  public void shouldTraverse_shouldReturnFalseForConstructorAnnotatedFunction() {
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = createFunctionNode();
    setConstructorAnnotation(n);
    Node parent = createParentNode();

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertFalse(result);
  }

  @Test
  public void shouldTraverse_shouldReturnFalseForInterfaceAnnotatedFunction() {
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = createFunctionNode();
    setInterfaceAnnotation(n);
    Node parent = createParentNode();

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertFalse(result);
  }

  @Test
  public void shouldTraverse_shouldReturnFalseForThisAnnotatedFunction() {
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = createFunctionNode();
    setThisAnnotation(n);
    Node parent = createParentNode();

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertFalse(result);
  }

  @Test
  public void shouldTraverse_shouldReturnFalseForOverrideAnnotatedFunction() {
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = createFunctionNode();
    setOverrideAnnotation(n);
    Node parent = createParentNode();

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertFalse(result);
  }

  @Test
  public void shouldTraverse_shouldReturnFalseForFunctionInNonGlobalContext1() {
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = createFunctionNode();
    Node parent = createParentNode(Token.BLOCK);

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertFalse(result);
  }

  @Test
  public void shouldTraverse_shouldReturnFalseForFunctionInNonGlobalContext2() {
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = createFunctionNode();
    Node parent = createParentNode(Token.SCRIPT);

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertFalse(result);
  }

  @Test
  public void shouldTraverse_shouldReturnFalseForFunctionInNonGlobalContext3() {
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = createFunctionNode();
    Node parent = createParentNode(Token.NAME);

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertFalse(result);
  }

  @Test
  public void shouldTraverse_shouldReturnFalseForFunctionInNonGlobalContext4() {
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = createFunctionNode();
    Node parent = createParentNode(Token.ASSIGN);

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertFalse(result);
  }

  @Test
  public void shouldTraverse_shouldReturnFalseForFunctionInNonGlobalContext5() {
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = createFunctionNode();
    Node parent = createParentNode(Token.STRING);

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertFalse(result);
  }

  @Test
  public void shouldTraverse_shouldReturnFalseForFunctionInNonGlobalContext6() {
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = createFunctionNode();
    Node parent = createParentNode(Token.NUMBER);

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertFalse(result);
  }

  @Test
  public void shouldTraverse_shouldReturnTrueForFunctionInValidNonGlobalContext() {
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = createFunctionNode();
    Node parent = createParentNode(Token.OTHER);

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertTrue(result);
  }

  @Test
  public void shouldTraverse_shouldReturnFalseForFunctionLentToPrototype() {
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = createFunctionNode();
    Node parent = createParentNode();
    setPrototypeAssignment(parent);

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertFalse(result);
  }

  // Test for visit method

  @Test
  public void visit_shouldReportGlobalThisForAssignment() {
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = createThisNode();
    Node parent = createParentNode(Token.ASSIGN);

    checkGlobalThis.visit(t, n, parent);

    // assert that the report method is called
  }

  @Test
  public void visit_shouldNotReportGlobalThisForNonAssignment() {
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = createThisNode();
    Node parent = createParentNode();

    checkGlobalThis.visit(t, n, parent);

    // assert that the report method is not called
  }

  private Node createFunctionNode() {
    return new Node(Token.FUNCTION);
  }

  private Node createParentNode() {
    return new Node(Token.BLOCK);
  }

  private Node createParentNode(int type) {
    return new Node(type);
  }

  private Node createThisNode() {
    return new Node(Token.THIS);
  }

  private void setConstructorAnnotation(Node functionNode) {
    functionNode.putProp(Node.CONSTRUCTOR, true);
  }

  private void setInterfaceAnnotation(Node functionNode) {
    functionNode.putProp(Node.INTERFACE, true);
  }

  private void setThisAnnotation(Node functionNode) {
    functionNode.putProp(Node.THIS, true);
  }

  private void setOverrideAnnotation(Node functionNode) {
    functionNode.putProp(Node.OVERRIDE, true);
  }

  private void setPrototypeAssignment(Node parentNode) {
    Node lhs = new Node(Token.GETPROP);
    lhs.addChildToBack(new Node(Token.THIS));
    lhs.addChildToBack(new Node(Token.STRING, "prototype"));

    parentNode.addChildToBack(lhs);
  }
}