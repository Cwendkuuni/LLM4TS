import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class CheckGlobalThisTest {
  
  private CheckGlobalThis checkGlobalThis;
  private AbstractCompiler compiler;
  private NodeTraversal.Callback callback;
  
  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    callback = checkGlobalThis;
  }

  @Test
  public void testShouldTraverse_withFunctionConstructorAnnotation() {
    NodeTraversal t = new NodeTraversal(compiler, callback);
    Node n = new Node(Token.FUNCTION);
    n.setJSDocInfo(createJsDocInfoBuilder().makeConstructor().build());
    boolean result = checkGlobalThis.shouldTraverse(t, n, null);
    assertEquals(false, result);
  }
  
  @Test
  public void testShouldTraverse_withFunctionThisAnnotation() {
    NodeTraversal t = new NodeTraversal(compiler, callback);
    Node n = new Node(Token.FUNCTION);
    n.setJSDocInfo(createJsDocInfoBuilder().recordThis().build());
    boolean result = checkGlobalThis.shouldTraverse(t, n, null);
    assertEquals(false, result);
  }
  
  @Test
  public void testShouldTraverse_withFunctionOverrideAnnotation() {
    NodeTraversal t = new NodeTraversal(compiler, callback);
    Node n = new Node(Token.FUNCTION);
    n.setJSDocInfo(createJsDocInfoBuilder().makeOverride().build());
    boolean result = checkGlobalThis.shouldTraverse(t, n, null);
    assertEquals(false, result);
  }
  
  @Test
  public void testShouldTraverse_withNestedAssign() {
    NodeTraversal t = new NodeTraversal(compiler, callback);
    Node n = new Node(Token.FUNCTION);
    Node assign = new Node(Token.ASSIGN);
    assign.addChildToFront(new Node(Token.NAME));
    assign.addChildToBack(new Node(Token.THIS));
    n.addChildToFront(assign);
    boolean result = checkGlobalThis.shouldTraverse(t, n, assign);
    assertEquals(true, result);
  }
  
  @Test
  public void testShouldTraverse_withGlobalFunction() {
    NodeTraversal t = new NodeTraversal(compiler, callback);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(new Node(Token.NAME));
    parent.addChildToBack(n);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertEquals(false, result);
  }
  
  @Test
  public void testShouldTraverse_withEmptyFunctionBlock() {
    NodeTraversal t = new NodeTraversal(compiler, callback);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToBack(n);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertEquals(false, result);
  }
  
  @Test
  public void testShouldTraverse_withFunctionName() {
    NodeTraversal t = new NodeTraversal(compiler, callback);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    parent.addChildToBack(n);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertEquals(false, result);
  }
  
  @Test
  public void testShouldTraverse_withAssignmentToPrototype() {
    NodeTraversal t = new NodeTraversal(compiler, callback);
    Node n = new Node(Token.SCRIPT);
    Node assign = new Node(Token.ASSIGN);
    assign.addChildToFront(new Node(Token.GETPROP));
    assign.addChildToBack(new Node(Token.THIS));
    assign.getFirstChild().addChildToFront(new Node(Token.NAME));
    assign.getFirstChild().addChildToBack(new Node(Token.STRING));
    n.addChildToBack(assign);
    boolean result = checkGlobalThis.shouldTraverse(t, assign.getFirstChild(), n);
    assertEquals(false, result);
  }
  
  @Test
  public void testShouldReportThis_withThisAssignment() {
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    boolean result = checkGlobalThis.shouldReportThis(n, parent);
    assertEquals(true, result);
  }
  
  @Test
  public void testShouldReportThis_withPropertyAccess() {
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    boolean result = checkGlobalThis.shouldReportThis(n, parent);
    assertEquals(true, result);
  }
  
  @Test
  public void testShouldReportThis_withNonGlobalThis() {
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.BLOCK);
    boolean result = checkGlobalThis.shouldReportThis(n, parent);
    assertEquals(false, result);
  }
  
  @Test
  public void testVisit_withGlobalThis() {
    NodeTraversal t = new NodeTraversal(compiler, callback);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    checkGlobalThis.visit(t, n, parent);
    // verify that a warning was reported
    assertEquals(1, compiler.getErrorCount());
  }
  
  @Test
  public void testVisit_withNonGlobalThis() {
    NodeTraversal t = new NodeTraversal(compiler, callback);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.BLOCK);
    checkGlobalThis.visit(t, n, parent);
    // verify that no warning was reported
    assertEquals(0, compiler.getErrorCount());
  }
  
  private JSDocInfo.JSDocInfoBuilder createJsDocInfoBuilder() {
    return new JSDocInfo.JSDocInfoBuilder(false);
  }
}