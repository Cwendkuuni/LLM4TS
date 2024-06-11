import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class CheckGlobalThisTest {

  private CheckGlobalThis checkGlobalThis;
  
  @Before
  public void setUp() {
    checkGlobalThis = new CheckGlobalThis();
  }
  
  @Test
  public void shouldNotReportThisForSafeAssignments() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    
    assertFalse(checkGlobalThis.shouldReportThis(n, parent));
  }

  @Test
  public void shouldReportThisForUnsafeAssignments() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);  
    
    assertTrue(checkGlobalThis.shouldReportThis(n, parent));
  }

  @Test
  public void shouldTraverseFunctionsWithConstructorAnnotation() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);  
    parent.putProp(Node.JSDOC_INFO_PROP, JSDocInfo.CONSTRUCTOR_ANNOTATION);
    
    assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
  }
  
  @Test
  public void shouldTraverseFunctionsWithThisAnnotation() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);  
    parent.putProp(Node.JSDOC_INFO_PROP, JSDocInfo.THIS_ANNOTATION);
    
    assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
  }
  
  @Test
  public void shouldNotTraverseFunctionsWithOverrideAnnotation() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);  
    parent.putProp(Node.JSDOC_INFO_PROP, JSDocInfo.OVERRIDE_ANNOTATION);
    
    assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
  }
  
  @Test
  public void shouldTraverseFunctionsWithoutAnnotations() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);  
    
    assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
  }
  
  @Test
  public void shouldNotTraverseNonFunctions() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.VAR);
    Node parent = new Node(Token.NAME);  
    
    assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
  }
  
  @Test
  public void shouldTraverseNonConstructorContextFunctions() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);  
    
    assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
  }
  
  @Test
  public void shouldTraversePrototypeMethods() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.GETPROP);
    parent.getLastChild().setString("prototype");
    
    assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
  }
  
  @Test
  public void shouldNotTraversePrototypeProperties() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    parent.getLastChild().setString("prototype");
    
    assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
  }
  
  @Test
  public void shouldNotTraverseNestedAssignmentToLeftSide() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.NAME);
    Node grandParent = new Node(Token.SUB, NodeUtil.createName("a"), NodeUtil.createName("this"));
    parent.setParent(grandParent);
    
    assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
  }
  
  @Test
  public void shouldTraverseNestedAssignmentToRightSide() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ADD, NodeUtil.createName("this"), NodeUtil.createName("property"));
    Node grandParent = new Node(Token.ASSIGN, NodeUtil.createName("a"), parent);
    parent.setParent(grandParent);
    
    assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
  }
  
  @Test
  public void shouldReportThisWhenEncountered() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    parent.getParent().putProp(Node.JSDOC_INFO_PROP, JSDocInfo.CONSTRUCTOR_ANNOTATION);
    
    assertNotNull(checkGlobalThis.visit(t, n, parent));
  }

  @Test
  public void shouldNotReportThisWhenNotEncountered() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.ADD, NodeUtil.createName("a"), NodeUtil.createName("property"));
    Node parent = new Node(Token.GETPROP);
    parent.getParent().putProp(Node.JSDOC_INFO_PROP, JSDocInfo.CONSTRUCTOR_ANNOTATION);
    
    assertNull(checkGlobalThis.visit(t, n, parent));
  }

  @Test
  public void shouldResetAssignLhsChildAfterVisit() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    Node grandParent = new Node(Token.SUB, NodeUtil.createName("a"), n);
    parent.setParent(grandParent);
    checkGlobalThis.visit(t, n, parent);
    
    assertNull(checkGlobalThis.assignLhsChild);
  }

  @Test
  public void shouldSetAssignLhsChildInShouldTraverse() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    
    assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
    assertNotNull(checkGlobalThis.assignLhsChild);
  }

  @Test
  public void shouldNotSetAssignLhsChildInShouldTraverse() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    
    assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
    assertNull(checkGlobalThis.assignLhsChild);
  }

  @Test
  public void shouldReportGlobalThisWarning() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    
    DiagnosticType globalThisWarning = DiagnosticType.warning(
      "JSC_USED_GLOBAL_THIS",
      "dangerous use of the global 'this' object"
    );
    assertEquals(
      globalThisWarning,
      checkGlobalThis.visit(t, n, parent)
    );
  }
}