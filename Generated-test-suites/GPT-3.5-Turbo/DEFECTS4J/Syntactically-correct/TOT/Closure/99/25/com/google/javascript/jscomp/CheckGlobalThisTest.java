import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class CheckGlobalThisTest {

  private CheckGlobalThis checker;
  private AbstractCompiler compiler;
  
  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
  }
  
  @Test
  public void testCheckGlobalThis_GlobalThisUsedInAssignment_ReportWarning() {
    NodeTraversal.Callback callback = checker;
    Node node = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    boolean shouldTraverse = checker.shouldTraverse(null, node, parent);
    assertFalse(shouldTraverse);
    checker.visit(null, node, parent);
    // verify that a warning is reported
    assertTrue(compiler.getErrors().size() == 1);
    assertEquals(CheckGlobalThis.GLOBAL_THIS, compiler.getErrors().get(0).getType());
  }
  
  @Test
  public void testCheckGlobalThis_GlobalThisUsedInPropertyAccess_ReportWarning() {
    NodeTraversal.Callback callback = checker;
    Node node = new Node(Token.THIS);
    Node parent = new Node(Token.GETELEM);
    boolean shouldTraverse = checker.shouldTraverse(null, node, parent);
    assertFalse(shouldTraverse);
    checker.visit(null, node, parent);
    // verify that a warning is reported
    assertTrue(compiler.getErrors().size() == 1);
    assertEquals(CheckGlobalThis.GLOBAL_THIS, compiler.getErrors().get(0).getType());
  }
  
  @Test
  public void testCheckGlobalThis_GlobalThisInNonGlobalContext_ShouldNotTraverse() {
    NodeTraversal.Callback callback = checker;
    Node node = new Node(Token.THIS);
    Node parent = new Node(Token.STRING);
    boolean shouldTraverse = checker.shouldTraverse(null, node, parent);
    assertFalse(shouldTraverse);
  }
  
  @Test
  public void testCheckGlobalThis_GlobalThisInConstructorFunction_ShouldNotTraverse() {
    NodeTraversal.Callback callback = checker;
    Node node = new Node(Token.THIS);
    Node parent = new Node(Token.FUNCTION);
    parent.setJSDocInfo(new JSDocInfo(true));
    boolean shouldTraverse = checker.shouldTraverse(null, node, parent);
    assertFalse(shouldTraverse);
  }
  
  @Test
  public void testCheckGlobalThis_GlobalThisInFunctionWithThisAnnotation_ShouldNotTraverse() {
    NodeTraversal.Callback callback = checker;
    Node node = new Node(Token.THIS);
    Node parent = new Node(Token.FUNCTION);
    parent.setJSDocInfo(new JSDocInfo());
    parent.getJSDocInfo().setThisType(new Node(Token.STRING, "example"));
    boolean shouldTraverse = checker.shouldTraverse(null, node, parent);
    assertFalse(shouldTraverse);
  }
  
  @Test
  public void testCheckGlobalThis_GlobalThisInConstructorFunctionWithOverrideAnnotation_ShouldNotTraverse() {
    NodeTraversal.Callback callback = checker;
    Node node = new Node(Token.THIS);
    Node parent = new Node(Token.FUNCTION);
    parent.setJSDocInfo(new JSDocInfo());
    parent.getJSDocInfo().setOverride(true);
    boolean shouldTraverse = checker.shouldTraverse(null, node, parent);
    assertFalse(shouldTraverse);
  }
  
  @Test
  public void testCheckGlobalThis_GlobalThisInFunctionWithNoLogicalPlaceForAnnotation_ShouldNotTraverse() {
    NodeTraversal.Callback callback = checker;
    Node node = new Node(Token.THIS);
    Node parent = new Node(Token.FUNCTION);
    parent.setJSDocInfo(null);
    Node grandparent = new Node(Token.FUNCTION);
    boolean shouldTraverse = checker.shouldTraverse(null, node, parent);
    assertFalse(shouldTraverse);
  }
  
  @Test
  public void testCheckGlobalThis_PropertyAccessToPrototype_ShouldNotTraverse() {
    NodeTraversal.Callback callback = checker;
    Node lhs = new Node(Token.GETPROP);
    lhs.addChildToFront(new Node(Token.THIS));
    lhs.addChildToBack(new Node(Token.STRING, "prototype"));
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToFront(lhs);
    Node rhs = new Node(Token.NAME);
    rhs.setString("example");
    parent.addChildToBack(rhs);
    Node node = rhs;
    boolean shouldTraverse = checker.shouldTraverse(null, node, parent);
    assertFalse(shouldTraverse);
  }
  
  @Test
  public void testCheckGlobalThis_PropertyAccessToSubpropertyOfPrototype_ShouldNotTraverse() {
    NodeTraversal.Callback callback = checker;
    Node lhs = new Node(Token.GETPROP);
    lhs.addChildToFront(new Node(Token.STRING, "someObject"));
    lhs.addChildToBack(new Node(Token.STRING, "prototype"));
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToFront(lhs);
    Node rhs = new Node(Token.NAME);
    rhs.setString("example");
    parent.addChildToBack(rhs);
    Node node = rhs;
    boolean shouldTraverse = checker.shouldTraverse(null, node, parent);
    assertFalse(shouldTraverse);
  }
  
  @Test
  public void testCheckGlobalThis_GlobalThisUsedInNonAssignment_ShouldNotReportWarning() {
    NodeTraversal.Callback callback = checker;
    Node node = new Node(Token.THIS);
    Node parent = new Node(Token.VAR);
    boolean shouldReportThis = checker.shouldReportThis(node, parent);
    assertFalse(shouldReportThis);
  }
  
  @Test
  public void testCheckGlobalThis_GlobalThisUsedInAssignment_ShouldReportWarning() {
    NodeTraversal.Callback callback = checker;
    Node node = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    boolean shouldReportThis = checker.shouldReportThis(node, parent);
    assertTrue(shouldReportThis);
  }
  
}