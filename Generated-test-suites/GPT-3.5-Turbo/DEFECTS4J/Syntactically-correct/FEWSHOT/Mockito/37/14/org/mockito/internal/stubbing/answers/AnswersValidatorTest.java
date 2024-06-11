import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.stubbing.answers.AnswersValidator;
import org.mockito.internal.stubbing.answers.DoesNothing;
import org.mockito.internal.stubbing.answers.Returns;
import org.mockito.internal.stubbing.answers.ThrowsException;
import org.mockito.stubbing.Answer;

public class AnswersValidatorTest {
    
    private AnswersValidator answersValidator;
    private Reporter reporter;
    
    @Before
    public void setUp() {
        answersValidator = new AnswersValidator();
        reporter = mock(Reporter.class);
        answersValidator.reporter = reporter;
    }

    @Test
    public void testValidate_DoNothing_ValidInvocation() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.isVoid()).thenReturn(true);
        answersValidator.validate(new DoesNothing(), invocation);
        verifyZeroInteractions(reporter);
    }

    @Test
    public void testValidate_DoNothing_InvalidInvocation() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.isVoid()).thenReturn(false);
        answersValidator.validate(new DoesNothing(), invocation);
        verify(reporter).onlyVoidMethodsCanBeSetToDoNothing();
    }

    @Test
    public void testValidate_ReturnValue_ValidInvocation() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.isVoid()).thenReturn(false);
        Returns answer = new Returns();
        answer.setReturnType(String.class);
        when(invocation.printMethodReturnType()).thenReturn(String.class.getName());
        when(invocation.getMethodName()).thenReturn("methodName");
        answersValidator.validate(answer, invocation);
        verifyZeroInteractions(reporter);
    }

    @Test
    public void testValidate_ReturnValue_VoidInvocation() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.isVoid()).thenReturn(true);
        Returns answer = new Returns();
        answer.setReturnType(String.class);
        answersValidator.validate(answer, invocation);
        verify(reporter).cannotStubVoidMethodWithAReturnValue();
    }

    @Test
    public void testValidate_ReturnValue_ReturnsNullForPrimitiveInvocation() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.isVoid()).thenReturn(false);
        when(invocation.returnsPrimitive()).thenReturn(true);
        Returns answer = new Returns();
        answer.returnsNull();
        when(invocation.printMethodReturnType()).thenReturn(int.class.getName());
        when(invocation.getMethodName()).thenReturn("methodName");
        answersValidator.validate(answer, invocation);
        verify(reporter).wrongTypeOfReturnValue(int.class.getName(), "null", "methodName");
    }

    @Test
    public void testValidate_ReturnValue_InvalidReturnTypeInvocation() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.isVoid()).thenReturn(false);
        when(invocation.isValidReturnType(any())).thenReturn(false);
        Returns answer = new Returns();
        answer.setReturnType(String.class);
        when(invocation.printMethodReturnType()).thenReturn(boolean.class.getName());
        when(answer.printReturnType()).thenReturn(String.class.getName());
        when(invocation.getMethodName()).thenReturn("methodName");
        answersValidator.validate(answer, invocation);
        verify(reporter).wrongTypeOfReturnValue(boolean.class.getName(), String.class.getName(), "methodName");
    }

    @Test
    public void testValidate_Exception_ValidInvocation() {
        Invocation invocation = mock(Invocation.class);
        ThrowsException answer = new ThrowsException();
        answer.setThrowable(new RuntimeException());
        answersValidator.validate(answer, invocation);
        verifyZeroInteractions(reporter);
    }

    @Test
    public void testValidate_Exception_NullThrowableInvocation() {
        Invocation invocation = mock(Invocation.class);
        ThrowsException answer = new ThrowsException();
        answer.setThrowable(null);
        answersValidator.validate(answer, invocation);
        verify(reporter).cannotStubWithNullThrowable();
    }

    @Test
    public void testValidate_Exception_CheckedExceptionInvalidInvocation() {
        Invocation invocation = mock(Invocation.class);
        ThrowsException answer = new ThrowsException();
        answer.setThrowable(new Exception());
        when(invocation.isValidException(any())).thenReturn(false);
        answersValidator.validate(answer, invocation);
        verify(reporter).checkedExceptionInvalid(any());
    }

    @Test
    public void testValidate_Exception_RuntimeExceptionInvocation() {
        Invocation invocation = mock(Invocation.class);
        ThrowsException answer = new ThrowsException();
        answer.setThrowable(new RuntimeException());
        answersValidator.validate(answer, invocation);
        verifyZeroInteractions(reporter);
    }

    @Test
    public void testValidate_Exception_ErrorInvocation() {
        Invocation invocation = mock(Invocation.class);
        ThrowsException answer = new ThrowsException();
        answer.setThrowable(new Error());
        answersValidator.validate(answer, invocation);
        verifyZeroInteractions(reporter);
    }
}