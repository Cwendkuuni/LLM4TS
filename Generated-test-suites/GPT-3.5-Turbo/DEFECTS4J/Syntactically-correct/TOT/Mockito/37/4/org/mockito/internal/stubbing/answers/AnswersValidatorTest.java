import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.DefaultAnswer;
import org.mockito.stubbing.Returns;
import org.mockito.stubbing.ThrowsException;
import org.mockito.exceptions.Reporter;

import static org.mockito.Mockito.*;

public class AnswersValidatorTest {

    private AnswersValidator answersValidator;
    private Reporter reporter;

    @Before
    public void setUp() {
        answersValidator = new AnswersValidator();
        reporter = mock(Reporter.class);

        //inject mocked reporter
        answersValidator.setReporter(reporter);
    }

    @Test
    public void testValidate_DoNothing_ValidInvocation() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.isVoid()).thenReturn(true);

        DoesNothing answer = mock(DoesNothing.class);

        answersValidator.validate(answer, invocation);

        verifyNoInteractions(reporter);
    }

    @Test
    public void testValidate_DoNothing_InvalidInvocation() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.isVoid()).thenReturn(false);

        DoesNothing answer = mock(DoesNothing.class);

        answersValidator.validate(answer, invocation);

        verify(reporter).onlyVoidMethodsCanBeSetToDoNothing();
    }

    @Test
    public void testValidate_ReturnValue_ValidInvocation() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.isVoid()).thenReturn(false);
        when(invocation.returnsPrimitive()).thenReturn(false);
        when(invocation.printMethodReturnType()).thenReturn("String");
        when(invocation.isValidReturnType(any())).thenReturn(true);

        Returns answer = mock(Returns.class);
        when(answer.returnsNull()).thenReturn(false);
        when(answer.getReturnType()).thenReturn(String.class);
        when(answer.printReturnType()).thenReturn("String");

        answersValidator.validate(answer, invocation);

        verifyNoInteractions(reporter);
    }

    @Test
    public void testValidate_ReturnValue_InvalidInvocation_VoidMethod() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.isVoid()).thenReturn(true);

        Returns answer = mock(Returns.class);
        when(answer.returnsNull()).thenReturn(false);

        answersValidator.validate(answer, invocation);

        verify(reporter).cannotStubVoidMethodWithAReturnValue();
    }

    @Test
    public void testValidate_ReturnValue_InvalidInvocation_NullReturnValueForPrimitive() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.isVoid()).thenReturn(false);
        when(invocation.returnsPrimitive()).thenReturn(true);
        when(invocation.printMethodReturnType()).thenReturn("int");

        Returns answer = mock(Returns.class);
        when(answer.returnsNull()).thenReturn(true);

        answersValidator.validate(answer, invocation);

        verify(reporter).wrongTypeOfReturnValue("int", "null", invocation.getMethodName());
    }

    @Test
    public void testValidate_ReturnValue_InvalidInvocation_InvalidReturnType() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.isVoid()).thenReturn(false);
        when(invocation.returnsPrimitive()).thenReturn(false);
        when(invocation.printMethodReturnType()).thenReturn("String");
        when(invocation.isValidReturnType(any())).thenReturn(false);

        Returns answer = mock(Returns.class);
        when(answer.returnsNull()).thenReturn(false);
        when(answer.getReturnType()).thenReturn(Integer.class);
        when(answer.printReturnType()).thenReturn("Integer");

        answersValidator.validate(answer, invocation);

        verify(reporter).wrongTypeOfReturnValue("String", "Integer", invocation.getMethodName());
    }

    @Test
    public void testValidate_Exception_ValidInvocation_NullThrowable() {
        Invocation invocation = mock(Invocation.class);

        ThrowsException answer = mock(ThrowsException.class);
        when(answer.getThrowable()).thenReturn(null);

        answersValidator.validate(answer, invocation);

        verify(reporter).cannotStubWithNullThrowable();
    }

    @Test
    public void testValidate_Exception_ValidInvocation_RuntimeException() {
        Invocation invocation = mock(Invocation.class);

        ThrowsException answer = mock(ThrowsException.class);
        Throwable runtimeException = new RuntimeException();
        when(answer.getThrowable()).thenReturn(runtimeException);

        answersValidator.validate(answer, invocation);

        verifyNoInteractions(reporter);
    }

    @Test
    public void testValidate_Exception_ValidInvocation_Error() {
        Invocation invocation = mock(Invocation.class);

        ThrowsException answer = mock(ThrowsException.class);
        Throwable error = new Error();
        when(answer.getThrowable()).thenReturn(error);

        answersValidator.validate(answer, invocation);

        verifyNoInteractions(reporter);
    }

    @Test
    public void testValidate_Exception_InvalidInvocation_Checked() {
        Invocation invocation = mock(Invocation.class);

        ThrowsException answer = mock(ThrowsException.class);
        Throwable checkedException = new Exception();
        when(answer.getThrowable()).thenReturn(checkedException);
        when(invocation.isValidException(any())).thenReturn(false);

        answersValidator.validate(answer, invocation);

        verify(reporter).checkedExceptionInvalid(checkedException);
    }
}