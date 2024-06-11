import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse_NodeIsFunctionWithConstructorAnnotation_ReturnsFalse() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    // Assert
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_NodeIsFunctionWithThisAnnotation_ReturnsFalse() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordThisType();
    n.setJSDocInfo(jsDoc);
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    // Assert
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_NodeIsFunctionWithOverrideAnnotation_ReturnsFalse() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordOverride();
    n.setJSDocInfo(jsDoc);
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    // Assert
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_NodeIsFunctionWithinNonGlobalContext_ReturnsFalse() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    Node grandparent = new Node(Token.GETPROP);
    grandparent.addChildToFront(new Node(Token.NAME, "prototype"));
    parent.addChildToFront(grandparent);
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    // Assert
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_NodeIsFunctionWithinGlobalContext_ReturnsTrue() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    Node grandparent = new Node(Token.SCRIPT);
    grandparent.addChildToBack(parent);
    parent.addChildToBack(n);
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    // Assert
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_NodeIsAssignmentWithPrototypeProperty_ReturnsFalse() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.NAME);
    n.setString("prototype");
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToBack(n);
    Node lhs = new Node(Token.NAME);
    parent.addChildToFront(lhs);
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    // Assert
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_NodeIsAssignmentWithPrototypeSubproperty_ReturnsFalse() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.NAME);
    n.setString("prototype.property");
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToBack(n);
    Node lhs = new Node(Token.NAME);
    parent.addChildToFront(lhs);
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    // Assert
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_NodeIsAssignmentToGlobalThis_ReturnsTrue() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.NAME);
    n.setString("this");
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToFront(n);
    Node lhs = new Node(Token.NAME);
    parent.addChildToBack(lhs);
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    // Assert
    assertTrue(result);
  }
  
  @Test
  public void testVisit_NodeIsThisAndShouldReportThis_CallsCompilerReport() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    parent.addChildToFront(new Node(Token.NAME));
    compiler.reportCallback = new ReportCallback() {
      @Override
      public void report(DiagnosticType diagnosticType, Node node, CheckLevel level, String message) {
        // Assert
        assertEquals(CheckGlobalThis.GLOBAL_THIS, diagnosticType);
        assertEquals(n, node);
        assertEquals(CheckLevel.WARNING, level);
        assertNotNull(message);
      }
    };
    
    // Act
    checkGlobalThis.visit(t, n, parent);
  }
  
  @Test
  public void testVisit_NodeIsNotThisAndAssignLhsChildIsSet_SetsAssignLhsChildToNull() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.GETPROP);
    parent.addChildToFront(new Node(Token.THIS));
    checkGlobalThis.assignLhsChild = n;
    
    // Act
    checkGlobalThis.visit(t, n, parent);
    
    // Assert
    assertNull(checkGlobalThis.assignLhsChild);
  }

  interface ReportCallback {
    void report(DiagnosticType diagnosticType, Node node, CheckLevel level, String message);
  }
}