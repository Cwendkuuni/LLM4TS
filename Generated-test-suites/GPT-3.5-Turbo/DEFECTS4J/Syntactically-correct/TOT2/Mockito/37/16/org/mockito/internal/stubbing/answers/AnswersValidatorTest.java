import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;

public class AnswersValidatorTest {
  
  private AnswersValidator answersValidator;
  
  @Before
  public void setUp() {
    answersValidator = new AnswersValidator();
  }
  
  @Test
  public void testValidateDoNothing() {
    // Edge case: Validate that only void methods can be set to do nothing
    DoesNothing answer = new DoesNothing();
    Invocation invocation = new Invocation();
    
    answersValidator.validateDoNothing(answer, invocation);
    
    // Assert that the correct message is reported by the Reporter
    // (This assumes the implementation of Reporter)
    assertEquals("Only void methods can be set to do nothing", Reporter.getMessage());
  }
  
  @Test
  public void testValidateReturnValue() {
    // Typical use case: Validate the return value for a valid method
    Returns answer = new Returns();
    Invocation invocation = new Invocation();
    
    answersValidator.validateReturnValue(answer, invocation);
    
    // Assert that no error message is reported by the Reporter
    assertNull(Reporter.getMessage());
  }
  
  @Test
  public void testValidateException() {
    // Error scenario: Validate that stubbing with a null throwable is not allowed
    ThrowsException answer = new ThrowsException();
    Invocation invocation = new Invocation();
    
    answersValidator.validateException(answer, invocation);
    
    // Assert that the correct message is reported by the Reporter
    assertEquals("Cannot stub with null throwable", Reporter.getMessage());
  }
}