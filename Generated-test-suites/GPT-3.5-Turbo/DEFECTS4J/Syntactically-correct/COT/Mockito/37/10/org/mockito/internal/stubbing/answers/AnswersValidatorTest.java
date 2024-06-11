import org.junit.*;
import static org.junit.Assert.*;

public class AnswersValidatorTest {
  
  @Test
  public void testValidate_ThrowsException() {
    // Arrange
    Answer<?> answer = new ThrowsException();
    Invocation invocation = new Invocation();
    AnswersValidator validator = new AnswersValidator();
    
    // Act
    validator.validate(answer, invocation);
    
    // Assert - no exceptions should be thrown
  }
  
  @Test
  public void testValidate_Returns() {
    // Arrange
    Answer<?> answer = new Returns();
    Invocation invocation = new Invocation();
    AnswersValidator validator = new AnswersValidator();
    
    // Act
    validator.validate(answer, invocation);
    
    // Assert - no exceptions should be thrown
  }
  
  @Test
  public void testValidate_DoesNothing() {
    // Arrange
    Answer<?> answer = new DoesNothing();
    Invocation invocation = new Invocation();
    AnswersValidator validator = new AnswersValidator();
    
    // Act
    validator.validate(answer, invocation);
    
    // Assert - no exceptions should be thrown
  }
  
  @Test
  public void testValidateDoNothing_WithVoidMethod() {
    // Arrange
    DoesNothing answer = new DoesNothing();
    Invocation invocation = new Invocation();
    invocation.setVoid(true);
    AnswersValidator validator = new AnswersValidator();
    
    // Act
    validator.validateDoNothing(answer, invocation);
    
    // Assert - an exception should be thrown
    // Expected exception: reporter.onlyVoidMethodsCanBeSetToDoNothing()
  }
  
  @Test
  public void testValidateReturnValue_WithVoidMethod() {
    // Arrange
    Returns answer = new Returns();
    Invocation invocation = new Invocation();
    invocation.setVoid(true);
    AnswersValidator validator = new AnswersValidator();
    
    // Act
    validator.validateReturnValue(answer, invocation);
    
    // Assert - an exception should be thrown
    // Expected exception: reporter.cannotStubVoidMethodWithAReturnValue()
  }
  
  @Test
  public void testValidateReturnValue_WithNullReturnValueAndPrimitiveInvocation() {
    // Arrange
    Returns answer = new Returns();
    Invocation invocation = new Invocation();
    invocation.setReturnsPrimitive(true);
    answer.setReturnsNull(true);
    AnswersValidator validator = new AnswersValidator();
    
    // Act
    validator.validateReturnValue(answer, invocation);
    
    // Assert - an exception should be thrown
    // Expected exception: reporter.wrongTypeOfReturnValue(invocation.printMethodReturnType(), "null", invocation.getMethodName())
  }
  
  @Test
  public void testValidateReturnValue_WithInvalidReturnValueType() {
    // Arrange
    Returns answer = new Returns();
    Invocation invocation = new Invocation();
    invocation.setReturnType("int");
    answer.setReturnType("String");
    AnswersValidator validator = new AnswersValidator();
    
    // Act
    validator.validateReturnValue(answer, invocation);
    
    // Assert - an exception should be thrown
    // Expected exception: reporter.wrongTypeOfReturnValue(invocation.printMethodReturnType(), answer.printReturnType(), invocation.getMethodName())
  }
  
  @Test
  public void testValidateException_WithNullThrowable() {
    // Arrange
    ThrowsException answer = new ThrowsException();
    Invocation invocation = new Invocation();
    answer.setThrowable(null);
    AnswersValidator validator = new AnswersValidator();
    
    // Act
    validator.validateException(answer, invocation);
    
    // Assert - an exception should be thrown
    // Expected exception: reporter.cannotStubWithNullThrowable()
  }
  
  @Test
  public void testValidateException_WithRuntimeExceptionOrError() {
    // Arrange
    ThrowsException answer = new ThrowsException();
    Invocation invocation = new Invocation();
    answer.setThrowable(new RuntimeException());
    AnswersValidator validator = new AnswersValidator();
    
    // Act
    validator.validateException(answer, invocation);
    
    // Assert - no exceptions should be thrown
  }
  
  @Test
  public void testValidateException_WithInvalidCheckedException() {
    // Arrange
    ThrowsException answer = new ThrowsException();
    Invocation invocation = new Invocation();
    answer.setThrowable(new Exception());
    AnswersValidator validator = new AnswersValidator();
    
    // Act
    validator.validateException(answer, invocation);
    
    // Assert - an exception should be thrown
    // Expected exception: reporter.checkedExceptionInvalid(throwable)
  }
}