import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;
import org.mockito.internal.stubbing.answers.AnswersValidator;

import static org.mockito.Mockito.*;

public class AnswersValidatorTest {

    private AnswersValidator answersValidator;
    private Reporter reporter;

    @Before
    public void setUp() {
        answersValidator = new AnswersValidator();
        reporter = mock(Reporter.class);
        answersValidator.setReporter(reporter);
    }

    @Test
    public void testValidate_DoNothing_WithVoidInvocation() {
        Invocation invocation = new Invocation(Void.class, "voidMethod", null);
        DoesNothing answer = mock(DoesNothing.class);

        answersValidator.validate(answer, invocation);

        verify(reporter, times(1)).onlyVoidMethodsCanBeSetToDoNothing();
    }

    @Test
    public void testValidate_ReturnValue_WithVoidInvocation() {
        Invocation invocation = new Invocation(Void.class, "voidMethod", null);
        Returns answer = mock(Returns.class);

        answersValidator.validate(answer, invocation);

        verify(reporter, times(1)).cannotStubVoidMethodWithAReturnValue();
    }

    @Test
    public void testValidate_ReturnValue_WithNullReturnAndPrimitiveReturnType() {
        Invocation invocation = new Invocation(Integer.class, "getInt", null);
        Returns answer = mock(Returns.class);

        when(answer.returnsNull()).thenReturn(true);
        when(invocation.returnsPrimitive()).thenReturn(true);

        answersValidator.validate(answer, invocation);

        verify(reporter, times(1)).wrongTypeOfReturnValue(
                invocation.printMethodReturnType(),
                "null",
                invocation.getMethodName()
        );
    }

    @Test
    public void testValidate_ReturnValue_WithIncompatibleReturnType() {
        Invocation invocation = new Invocation(Integer.class, "getInt", null);
        Returns answer = mock(Returns.class);

        when(answer.returnsNull()).thenReturn(false);
        when(answer.getReturnType()).thenReturn(String.class);
        when(invocation.isValidReturnType(any(Class.class))).thenReturn(false);

        answersValidator.validate(answer, invocation);

        verify(reporter, times(1)).wrongTypeOfReturnValue(
                invocation.printMethodReturnType(),
                answer.printReturnType(),
                invocation.getMethodName()
        );
    }

    @Test
    public void testValidate_Exception_WithNullThrowable() {
        Invocation invocation = new Invocation(Integer.class, "getInt", null);
        ThrowsException answer = mock(ThrowsException.class);

        when(answer.getThrowable()).thenReturn(null);

        answersValidator.validate(answer, invocation);

        verify(reporter, times(1)).cannotStubWithNullThrowable();
    }

    @Test
    public void testValidate_Exception_WithCheckedInvalidException() {
        Invocation invocation = new Invocation(Integer.class, "getInt", null);
        ThrowsException answer = mock(ThrowsException.class);
        Throwable throwable = mock(Throwable.class);

        when(answer.getThrowable()).thenReturn(throwable);
        when(invocation.isValidException(any(Throwable.class))).thenReturn(false);

        answersValidator.validate(answer, invocation);

        verify(reporter, times(1)).checkedExceptionInvalid(throwable);
    }

    // Add more test cases here...

}