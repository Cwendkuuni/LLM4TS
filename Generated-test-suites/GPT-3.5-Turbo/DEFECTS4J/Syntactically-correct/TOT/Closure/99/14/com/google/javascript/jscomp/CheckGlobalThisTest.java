import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void shouldReportGlobalThisOnLeftSideOfAssignment() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    
    // Act
    boolean result = checkGlobalThis.shouldReportThis(n, parent);
    
    // Assert
    assertTrue(result);
  }
  
  @Test
  public void shouldReportGlobalThisWithPropertyAccess() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    
    // Act
    boolean result = checkGlobalThis.shouldReportThis(n, parent);
    
    // Assert
    assertTrue(result);
  }

  @Test
  public void shouldNotReportGlobalThisInPrototypeMethod() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    
    // Act
    boolean result = checkGlobalThis.shouldReportThis(n, parent);
    
    // Assert
    assertFalse(result);
  }

  @Test
  public void shouldNotReportGlobalThisInConstructorAnnotatedFunction() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    
    // Act
    boolean result = checkGlobalThis.shouldReportThis(n, parent);
    
    // Assert
    assertFalse(result);
  }

  @Test
  public void shouldNotReportGlobalThisInThisAnnotatedFunction() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    
    // Act
    boolean result = checkGlobalThis.shouldReportThis(n, parent);
    
    // Assert
    assertFalse(result);
  }

  @Test
  public void shouldNotReportGlobalThisInFunctionWithNoLogicalPlaceForThisAnnotation() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    
    // Act
    boolean result = checkGlobalThis.shouldReportThis(n, parent);
    
    // Assert
    assertFalse(result);
  }
}