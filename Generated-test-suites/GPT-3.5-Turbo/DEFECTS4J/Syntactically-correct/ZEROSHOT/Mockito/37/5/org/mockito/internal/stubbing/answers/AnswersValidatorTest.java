import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class AnswersValidatorTest {

  private AnswersValidator answersValidator;
  private Reporter reporter;

  @Before
  public void setup() {
    reporter = mock(Reporter.class);
    answersValidator = new AnswersValidator(reporter);
  }

  @Test
  public void testValidate_DoNothingWithNonVoidMethod_ShouldReportError() {
    // Arrange
    DoesNothing answer = mock(DoesNothing.class);
    Invocation invocation = mock(Invocation.class);
    when(invocation.isVoid()).thenReturn(false);

    // Act
    answersValidator.validate(answer, invocation);

    // Assert
    verify(reporter).onlyVoidMethodsCanBeSetToDoNothing();
  }

  @Test
  public void testValidate_ReturnValueWithVoidMethod_ShouldReportError() {
    // Arrange
    Returns answer = mock(Returns.class);
    Invocation invocation = mock(Invocation.class);
    when(invocation.isVoid()).thenReturn(true);

    // Act
    answersValidator.validate(answer, invocation);

    // Assert
    verify(reporter).cannotStubVoidMethodWithAReturnValue();
  }

  @Test
  public void testValidate_ReturnValueWithNullReturnType_ShouldReportError() {
    // Arrange
    Returns answer = mock(Returns.class);
    Invocation invocation = mock(Invocation.class);
    when(invocation.isVoid()).thenReturn(false);
    when(invocation.returnsPrimitive()).thenReturn(true);
    when(invocation.printMethodReturnType()).thenReturn("int");
    when(answer.returnsNull()).thenReturn(true);
    when(invocation.getMethodName()).thenReturn("someMethod");

    // Act
    answersValidator.validate(answer, invocation);

    // Assert
    verify(reporter).wrongTypeOfReturnValue("int", "null", "someMethod");
  }

  @Test
  public void testValidate_ReturnValueWithInvalidReturnType_ShouldReportError() {
    // Arrange
    Returns answer = mock(Returns.class);
    Invocation invocation = mock(Invocation.class);
    when(invocation.isVoid()).thenReturn(false);
    when(invocation.returnsPrimitive()).thenReturn(false);
    when(invocation.printMethodReturnType()).thenReturn("String");
    when(answer.returnsNull()).thenReturn(false);
    when(answer.getReturnType()).thenReturn(Integer.class);
    when(invocation.getMethodName()).thenReturn("someMethod");

    // Act
    answersValidator.validate(answer, invocation);

    // Assert
    verify(reporter).wrongTypeOfReturnValue("String", "Integer", "someMethod");
  }

  @Test
  public void testValidate_ExceptionWithNullThrowable_ShouldReportError() {
    // Arrange
    ThrowsException answer = mock(ThrowsException.class);
    Invocation invocation = mock(Invocation.class);
    when(answer.getThrowable()).thenReturn(null);

    // Act
    answersValidator.validate(answer, invocation);

    // Assert
    verify(reporter).cannotStubWithNullThrowable();
  }

  @Test
  public void testValidate_ExceptionWithCheckedException_ShouldReportError() {
    // Arrange
    ThrowsException answer = mock(ThrowsException.class);
    Invocation invocation = mock(Invocation.class);
    Throwable throwable = mock(Exception.class);
    when(answer.getThrowable()).thenReturn(throwable);
    when(invocation.isValidException(throwable)).thenReturn(false);

    // Act
    answersValidator.validate(answer, invocation);

    // Assert
    verify(reporter).checkedExceptionInvalid(throwable);
  }
}