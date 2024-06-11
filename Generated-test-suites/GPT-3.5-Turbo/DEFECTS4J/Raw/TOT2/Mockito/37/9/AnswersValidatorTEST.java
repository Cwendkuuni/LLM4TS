
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;
import org.mockito.internal.stubbing.answers.AnswersValidator;
import org.mockito.internal.stubbing.answers.DoesNothing;
import org.mockito.internal.stubbing.answers.Returns;
import org.mockito.internal.stubbing.answers.ThrowsException;
import org.mockito.exceptions.Reporter;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AnswersValidatorTest {

    @Test
    public void testValidateDoNothing() {
        AnswersValidator validator = new AnswersValidator();
        Reporter reporterMock = mock(Reporter.class);
        validator.reporter = reporterMock;
        
        Invocation invocation = mock(Invocation.class);
        when(invocation.isVoid()).thenReturn(false);
        
        DoesNothing answer = mock(DoesNothing.class);
        
        validator.validateDoNothing(answer, invocation);
        
        verify(reporterMock).onlyVoidMethodsCanBeSetToDoNothing();
    }

    @Test
    public void testValidateReturnValue() {
        AnswersValidator validator = new AnswersValidator();
        Reporter reporterMock = mock(Reporter.class);
        validator.reporter = reporterMock;
        
        Invocation invocation = mock(Invocation.class);
        when(invocation.isVoid()).thenReturn(true);
        when(invocation.returnsPrimitive()).thenReturn(true);
        when(invocation.printMethodReturnType()).thenReturn("void");
        when(invocation.getMethodName()).thenReturn("mockMethod");
        
        Returns answer = mock(Returns.class);
        when(answer.returnsNull()).thenReturn(true);
        
        validator.validateReturnValue(answer, invocation);
        
        verify(reporterMock).cannotStubVoidMethodWithAReturnValue();
        
        when(invocation.isVoid()).thenReturn(false);
        when(answer.returnsNull()).thenReturn(false);
        when(answer.getReturnType()).thenReturn(String.class);
        when(invocation.isValidReturnType(any())).thenReturn(false);
        
        validator.validateReturnValue(answer, invocation);
        
        verify(reporterMock).wrongTypeOfReturnValue("void", "class java.lang.String", "mockMethod");
    }

    @Test
    public void testValidateException() {
        AnswersValidator validator = new AnswersValidator();
        Reporter reporterMock = mock(Reporter.class);
        validator.reporter = reporterMock;
        
        Invocation invocation = mock(Invocation.class);
        
        ThrowsException answer = mock(ThrowsException.class);
        when(answer.getThrowable()).thenReturn(null);
        
        validator.validateException(answer, invocation);
        
        verify(reporterMock).cannotStubWithNullThrowable();
        
        Throwable checkedException = new Exception();
        
        when(answer.getThrowable()).thenReturn(checkedException);
        when(invocation.isValidException(checkedException)).thenReturn(false);
        
        validator.validateException(answer, invocation);
        
        verify(reporterMock).checkedExceptionInvalid(checkedException);
    }
}
