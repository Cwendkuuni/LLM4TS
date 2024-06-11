import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class CheckGlobalThisTest {
  
  @Test
  public void shouldReportGlobalThis() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
    
    // Act
    boolean result = checkGlobalThis.shouldReportThis();
    
    // Assert
    assertEquals(true, result);
  }
  
  @Test
  public void shouldNotReportGlobalThis() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
    
    // Act
    boolean result = checkGlobalThis.shouldReportThis();
    
    // Assert
    assertEquals(false, result);
  }
  
  @Test
  public void shouldTraverseFunction() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
    
    // Act
    boolean result = checkGlobalThis.shouldTraverseFunction();
    
    // Assert
    assertEquals(true, result);
  }
  
  @Test
  public void shouldNotTraverseFunction() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
    
    // Act
    boolean result = checkGlobalThis.shouldTraverseFunction();
    
    // Assert
    assertEquals(false, result);
  }
  
  @Test
  public void visitShouldReportGlobalThis() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node();
    
    // Act
    checkGlobalThis.visit(t, n, parent);
    
    // Assert
    // Add assertion
  }
  
  @Test
  public void visitShouldNotReportGlobalThis() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node();
    
    // Act
    checkGlobalThis.visit(t, n, parent);
    
    // Assert
    // Add assertion
  }
  
  @Test
  public void getFunctionJsDocInfoShouldReturnJsDocInfo() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
    Node n = new Node();
    
    // Act
    JSDocInfo jsDocInfo = checkGlobalThis.getFunctionJsDocInfo(n);
    
    // Assert
    // Add assertion
  }
  
  @Test
  public void getFunctionJsDocInfoShouldReturnNull() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
    Node n = new Node();
    
    // Act
    JSDocInfo jsDocInfo = checkGlobalThis.getFunctionJsDocInfo(n);
    
    // Assert
    // Add assertion
  }
}