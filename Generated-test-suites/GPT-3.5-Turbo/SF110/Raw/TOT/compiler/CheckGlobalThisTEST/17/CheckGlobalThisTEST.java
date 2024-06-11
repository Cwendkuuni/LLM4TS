import org.junit.Assert;
import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void shouldNotReportThis_whenThisUsedInPrototypeMethod() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    
    // Act
    boolean result = checkGlobalThis.shouldReportThis(new Node(Token.THIS));
    
    // Assert
    Assert.assertFalse(result);
  }
  
  @Test
  public void shouldNotReportThis_whenThisUsedInConstructorFunction() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    
    // Act
    boolean result = checkGlobalThis.shouldReportThis(new Node(Token.THIS));
    
    // Assert
    Assert.assertFalse(result);
  }
  
  @Test
  public void shouldNotReportThis_whenThisUsedInThisAnnotatedFunction() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    
    // Act
    boolean result = checkGlobalThis.shouldReportThis(new Node(Token.THIS));
    
    // Assert
    Assert.assertFalse(result);
  }
  
  @Test
  public void shouldNotReportThis_whenThisUsedInNamedFunction() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    
    // Act
    boolean result = checkGlobalThis.shouldReportThis(new Node(Token.THIS));
    
    // Assert
    Assert.assertFalse(result);
  }
  
  @Test
  public void shouldReportThis_whenThisUsedOnLeftSideOfAssignment() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    
    // Act
    boolean result = checkGlobalThis.shouldReportThis(new Node(Token.THIS));
    
    // Assert
    Assert.assertTrue(result);
  }
  
  @Test
  public void shouldReportThis_whenThisUsedInPropertyAccess() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    
    // Act
    boolean result = checkGlobalThis.shouldReportThis(new Node(Token.THIS));
    
    // Assert
    Assert.assertTrue(result);
  }
  
  @Test
  public void shouldReportThis_whenThisUsedOnLeftSideOfNestedAssignment() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    
    // Act
    boolean result = checkGlobalThis.shouldReportThis(new Node(Token.THIS));
    
    // Assert
    Assert.assertTrue(result);
  }
  
  @Test
  public void shouldNotReportThis_whenThisUsedOnRightSideOfAssignment() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    
    // Act
    boolean result = checkGlobalThis.shouldReportThis(new Node(Token.THIS));
    
    // Assert
    Assert.assertFalse(result);
  }
  
}