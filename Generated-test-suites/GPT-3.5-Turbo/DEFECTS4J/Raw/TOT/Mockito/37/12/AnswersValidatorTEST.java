import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;
import org.mockito.internal.stubbing.answers.AnswersValidator;
import org.mockito.internal.stubbing.answers.DoesNothing;
import org.mockito.internal.stubbing.answers.Returns;
import org.mockito.internal.stubbing.answers.ThrowsException;

import static org.mockito.Mockito.*;

public class AnswersValidatorTest {

    @Test
    public void testValidateDoNothingWithVoidMethod() {
        // Create mock objects
        Answer<?> answer = mock(DoesNothing.class);
        Invocation invocation = mock(Invocation.class);
        Reporter reporter = mock(Reporter.class);

        // Set up mock behavior
        when(invocation.isVoid()).thenReturn(true);

        // Create instance of class under test
        AnswersValidator validator = new AnswersValidator();
        validator.setReporter(reporter);

        // Call method under test
        validator.validateDoNothing((DoesNothing) answer, invocation);

        // Verify that correct method was called on reporter
        verify(reporter).onlyVoidMethodsCanBeSetToDoNothing();
    }

    @Test
    public void testValidateDoNothingWithNonVoidMethod() {
        // Create mock objects
        Answer<?> answer = mock(DoesNothing.class);
        Invocation invocation = mock(Invocation.class);
        Reporter reporter = mock(Reporter.class);

        // Set up mock behavior
        when(invocation.isVoid()).thenReturn(false);

        // Create instance of class under test
        AnswersValidator validator = new AnswersValidator();
        validator.setReporter(reporter);

        // Call method under test
        validator.validateDoNothing((DoesNothing) answer, invocation);

        // Verify that correct method was NOT called on reporter
        verify(reporter, never()).onlyVoidMethodsCanBeSetToDoNothing();
    }

    @Test
    public void testValidateReturnValueWithVoidMethod() {
        // Create mock objects
        Answer<?> answer = mock(Returns.class);
        Invocation invocation = mock(Invocation.class);
        Reporter reporter = mock(Reporter.class);

        // Set up mock behavior
        when(invocation.isVoid()).thenReturn(true);

        // Create instance of class under test
        AnswersValidator validator = new AnswersValidator();
        validator.setReporter(reporter);

        // Call method under test
        validator.validateReturnValue((Returns) answer, invocation);

        // Verify that correct method was called on reporter
        verify(reporter).cannotStubVoidMethodWithAReturnValue();
    }

    @Test
    public void testValidateReturnValueWithNullReturnAndPrimitiveReturnType() {
        // Create mock objects
        Answer<?> answer = mock(Returns.class);
        Invocation invocation = mock(Invocation.class);
        Reporter reporter = mock(Reporter.class);

        // Set up mock behavior
        when(invocation.isVoid()).thenReturn(false);
        when(invocation.returnsPrimitive()).thenReturn(true);
        when(answer.returnsNull()).thenReturn(true);
        when(invocation.printMethodReturnType()).thenReturn("int");

        // Create instance of class under test
        AnswersValidator validator = new AnswersValidator();
        validator.setReporter(reporter);

        // Call method under test
        validator.validateReturnValue((Returns) answer, invocation);

        // Verify that correct method was called on reporter
        verify(reporter).wrongTypeOfReturnValue("int", "null", invocation.getMethodName());
    }

    @Test
    public void testValidateReturnValueWithNonVoidMethodAndValidReturnType() {
        // Create mock objects
        Answer<?> answer = mock(Returns.class);
        Invocation invocation = mock(Invocation.class);
        Reporter reporter = mock(Reporter.class);

        // Set up mock behavior
        when(invocation.isVoid()).thenReturn(false);
        when(invocation.returnsPrimitive()).thenReturn(false);
        when(answer.returnsNull()).thenReturn(false);
        when(invocation.isValidReturnType(any(Class.class))).thenReturn(true);

        // Create instance of class under test
        AnswersValidator validator = new AnswersValidator();
        validator.setReporter(reporter);

        // Call method under test
        validator.validateReturnValue((Returns) answer, invocation);

        // Verify that correct method was NOT called on reporter
        verify(reporter, never()).wrongTypeOfReturnValue(any(String.class), any(String.class), any(String.class));
    }

    @Test
    public void testValidateReturnValueWithNonVoidMethodAndInvalidReturnType() {
        // Create mock objects
        Answer<?> answer = mock(Returns.class);
        Invocation invocation = mock(Invocation.class);
        Reporter reporter = mock(Reporter.class);

        // Set up mock behavior
        when(invocation.isVoid()).thenReturn(false);
        when(invocation.returnsPrimitive()).thenReturn(false);
        when(answer.returnsNull()).thenReturn(false);
        when(invocation.isValidReturnType(any(Class.class))).thenReturn(false);
        when(invocation.printMethodReturnType()).thenReturn("String");
        when(answer.printReturnType()).thenReturn("Integer");

        // Create instance of class under test
        AnswersValidator validator = new AnswersValidator();
        validator.setReporter(reporter);

        // Call method under test
        validator.validateReturnValue((Returns) answer, invocation);

        // Verify that correct method was called on reporter
        verify(reporter).wrongTypeOfReturnValue("String", "Integer", invocation.getMethodName());
    }

    @Test
    public void testValidateExceptionWithNullThrowable() {
        // Create mock objects
        Answer<?> answer = mock(ThrowsException.class);
        Invocation invocation = mock(Invocation.class);
        Reporter reporter = mock(Reporter.class);

        // Set up mock behavior
        when(((ThrowsException) answer).getThrowable()).thenReturn(null);

        // Create instance of class under test
        AnswersValidator validator = new AnswersValidator();
        validator.setReporter(reporter);

        // Call method under test
        validator.validateException((ThrowsException) answer, invocation);

        // Verify that correct method was called on reporter
        verify(reporter).cannotStubWithNullThrowable();
    }

    @Test
    public void testValidateExceptionWithRuntimeException() {
        // Create mock objects
        Answer<?> answer = mock(ThrowsException.class);
        Invocation invocation = mock(Invocation.class);
        Reporter reporter = mock(Reporter.class);
        RuntimeException exception = new RuntimeException();

        // Set up mock behavior
        when(((ThrowsException) answer).getThrowable()).thenReturn(exception);

        // Create instance of class under test
        AnswersValidator validator = new AnswersValidator();
        validator.setReporter(reporter);

        // Call method under test
        validator.validateException((ThrowsException) answer, invocation);

        // Verify that correct method was NOT called on reporter
        verify(reporter, never()).checkedExceptionInvalid(exception);
    }

    @Test
    public void testValidateExceptionWithError() {
        // Create mock objects
        Answer<?> answer = mock(ThrowsException.class);
        Invocation invocation = mock(Invocation.class);
        Reporter reporter = mock(Reporter.class);
        Error error = new Error();

        // Set up mock behavior
        when(((ThrowsException) answer).getThrowable()).thenReturn(error);

        // Create instance of class under test
        AnswersValidator validator = new AnswersValidator();
        validator.setReporter(reporter);

        // Call method under test
        validator.validateException((ThrowsException) answer, invocation);

        // Verify that correct method was NOT called on reporter
        verify(reporter, never()).checkedExceptionInvalid(error);
    }

    @Test
    public void testValidateExceptionWithCheckedException() {
        // Create mock objects
        Answer<?> answer = mock(ThrowsException.class);
        Invocation invocation = mock(Invocation.class);
        Reporter reporter = mock(Reporter.class);
        Exception exception = new Exception();

        // Set up mock behavior
        when(((ThrowsException) answer).getThrowable()).thenReturn(exception);
        when(invocation.isValidException(exception)).thenReturn(true);

        // Create instance of class under test
        AnswersValidator validator = new AnswersValidator();
        validator.setReporter(reporter);

        // Call method under test
        validator.validateException((ThrowsException) answer, invocation);

        // Verify that correct method was NOT called on reporter
        verify(reporter, never()).checkedExceptionInvalid(exception);
    }

    @Test
    public void testValidateExceptionWithInvalidCheckedException() {
        // Create mock objects
        Answer<?> answer = mock(ThrowsException.class);
        Invocation invocation = mock(Invocation.class);
        Reporter reporter = mock(Reporter.class);
        Exception exception = new Exception();

        // Set up mock behavior
        when(((ThrowsException) answer).getThrowable()).thenReturn(exception);
        when(invocation.isValidException(exception)).thenReturn(false);

        // Create instance of class under test
        AnswersValidator validator = new AnswersValidator();
        validator.setReporter(reporter);

        // Call method under test
        validator.validateException((ThrowsException) answer, invocation);

        // Verify that correct method was called on reporter
        verify(reporter).checkedExceptionInvalid(exception);
    }
}