import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {
  
  private CheckGlobalThis checkGlobalThis;
  
  @Before
  public void setup() {
    AbstractCompiler compiler = new AbstractCompiler();
    checkGlobalThis = new CheckGlobalThis(compiler);
  }
  
  @Test
  public void testShouldTraverse_withFunctionConstructorAndThisAnnotation_shouldReturnFalse() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    n.putBooleanProp(Node.CONSTRUCT_DEFINITION, true);
    n.setJSDocInfo(new JSDocInfo().setConstructor(true));
    Node parent = new Node(Token.SCRIPT);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_withFunctionThisAnnotation_shouldReturnFalse() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    n.setJSDocInfo(new JSDocInfo().setThisType(new JSTypeExpression()));
    Node parent = new Node(Token.SCRIPT);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_withFunctionOverrideAnnotation_shouldReturnFalse() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    n.setJSDocInfo(new JSDocInfo().setOverride(true));
    Node parent = new Node(Token.SCRIPT);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_withFunctionNotableForTypeCheck_shouldReturnTrue() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    n.setJSDocInfo(new JSDocInfo().setNotableForTypeCheck(true));
    Node parent = new Node(Token.SCRIPT);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_withFunctionLendsToPrototype_shouldReturnFalse() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    n.setJSDocInfo(new JSDocInfo().setLendsName("test.prototype"));
    Node parent = new Node(Token.SCRIPT);
    parent.setJSDocInfo(new JSDocInfo().setLendsName("test.prototype"));
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_withNestedAssignment_shouldReturnTrue() {
    NodeTraversal t = new NodeTraversal();
    Node assignNode = new Node(Token.ASSIGN);
    Node lhsNode = new Node(Token.NAME, new Node(Token.THIS));
    assignNode.addChildToBack(lhsNode);
    Node rhsNode = new Node(Token.NAME, new Node(Token.THIS));
    assignNode.addChildToBack(rhsNode);
    Node n = new Node(Token.GETPROP);
    n.setParent(assignNode);
    n.addChildToBack(new Node(Token.NAME, "prototype"));
    n.addChildToBack(new Node(Token.STRING, "property"));
    Node parent = new Node(Token.SCRIPT);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_withNestedAssignmentAndPrototypeProperty_shouldReturnFalse() {
    NodeTraversal t = new NodeTraversal();
    Node assignNode = new Node(Token.ASSIGN);
    Node lhsNode = new Node(Token.NAME, new Node(Token.THIS));
    assignNode.addChildToBack(lhsNode);
    Node rhsNode = new Node(Token.NAME, new Node(Token.THIS));
    assignNode.addChildToBack(rhsNode);
    Node n = new Node(Token.GETPROP);
    n.setParent(assignNode);
    n.addChildToBack(new Node(Token.GETPROP));
    n.getFirstChild().addChildToBack(new Node(Token.NAME, "prototype"));
    n.getFirstChild().addChildToBack(new Node(Token.STRING, "property"));
    Node parent = new Node(Token.SCRIPT);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_withFunctionChildOfBlock_shouldReturnTrue() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_withFunctionChildOfScript_shouldReturnTrue() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_withFunctionChildOfName_shouldReturnTrue() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_withFunctionChildOfAssign_shouldReturnTrue() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_withFunctionChildOfStringKey_shouldReturnTrue() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.STRING_KEY);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_withFunctionChildNotInAllowedParents_shouldReturnFalse() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ADD);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_withNonAssignmentNode_shouldReturnTrue() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.SCRIPT);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_withParentAssignAndChildNotAssignment_shouldReturnTrue() {
    NodeTraversal t = new NodeTraversal();
    Node assignNode = new Node(Token.ASSIGN);
    Node lhsNode = new Node(Token.THIS);
    assignNode.addChildToBack(lhsNode);
    Node rhsNode = new Node(Token.NAME, "x");
    assignNode.addChildToBack(rhsNode);
    Node n = new Node(Token.GETPROP);
    n.setParent(assignNode);
    n.addChildToBack(new Node(Token.NAME, "prototype"));
    n.addChildToBack(new Node(Token.STRING, "property"));
    Node parent = new Node(Token.SCRIPT);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertTrue(result);
  }
  
  @Test
  public void testVisit_withThisNodeAndShouldReportThis_shouldReportError() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    n.setParent(new Node(Token.GETPROP));
    AbstractCompiler compiler = new AbstractCompiler();
    compiler.report = new DiagnosticType();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    checkGlobalThis.assignLhsChild = null;
    checkGlobalThis.visit(t, n, n.getParent());
    assertTrue(compiler.reportCallCount > 0);
  }
  
  @Test
  public void testVisit_withAssignLhsChildAndAssignLhsChildIsNull_shouldNotResetAssignLhsChild() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.NAME);
    n.setString("test");
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    checkGlobalThis.assignLhsChild = n;
    checkGlobalThis.visit(t, n, new Node(Token.SCRIPT));
    assertNotNull(checkGlobalThis.assignLhsChild);
  }
  
  @Test
  public void testVisit_withAssignLhsChildAndAssignLhsChildIsNotNull_shouldResetAssignLhsChild() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    checkGlobalThis.assignLhsChild = n;
    checkGlobalThis.visit(t, n, new Node(Token.ASSIGN));
    assertNull(checkGlobalThis.assignLhsChild);
  }
  
  @Test
  public void testShouldReportThis_withAssignLhsChildIsNull_shouldReturnFalse() {
    Node n = new Node(Token.THIS);
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    checkGlobalThis.assignLhsChild = null;
    boolean result = checkGlobalThis.shouldReportThis(n);
    assertFalse(result);
  }
  
  @Test
  public void testShouldReportThis_withAssignLhsChildNotNull_shouldReturnTrue() {
    Node n = new Node(Token.THIS);
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    checkGlobalThis.assignLhsChild = n;
    boolean result = checkGlobalThis.shouldReportThis(n);
    assertTrue(result);
  }
}