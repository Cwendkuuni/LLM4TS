import org.mockito.internal.stubbing.answers.AnswersValidator;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;
import org.mockito.internal.stubbing.answers.ThrowsException;
import org.mockito.internal.stubbing.answers.Returns;
import org.mockito.internal.stubbing.answers.DoesNothing;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class AnswersValidatorTest {

    private AnswersValidator answersValidator;
    private Reporter reporterMock;
    private Answer answerMock;
    private Invocation invocationMock;

    @Before
    public void setUp() {
        answersValidator = new AnswersValidator();
        reporterMock = mock(Reporter.class);
        answerMock = mock(Answer.class);
        invocationMock = mock(Invocation.class);
        answersValidator.reporter = reporterMock;
    }

    @Test
    public void testValidateDoNothing() {
        DoesNothing answerMock = mock(DoesNothing.class);
        
        when(invocationMock.isVoid()).thenReturn(true);
        answersValidator.validateDoNothing(answerMock, invocationMock);
        verify(reporterMock, never()).onlyVoidMethodsCanBeSetToDoNothing();
        
        reset(invocationMock);
        when(invocationMock.isVoid()).thenReturn(false);
        answersValidator.validateDoNothing(answerMock, invocationMock);
        verify(reporterMock).onlyVoidMethodsCanBeSetToDoNothing();
    }

    @Test
    public void testValidateReturnValue() {
        Returns answerMock = mock(Returns.class);
        
        when(invocationMock.isVoid()).thenReturn(true);
        when(invocationMock.returnsPrimitive()).thenReturn(false);
        answersValidator.validateReturnValue(answerMock, invocationMock);
        verify(reporterMock).cannotStubVoidMethodWithAReturnValue();
        
        reset(invocationMock);
        when(invocationMock.isVoid()).thenReturn(false);
        when(invocationMock.returnsPrimitive()).thenReturn(true);
        when(invocationMock.printMethodReturnType()).thenReturn("int");
        answersValidator.validateReturnValue(answerMock, invocationMock);
        verify(reporterMock).wrongTypeOfReturnValue("int", "null", invocationMock.getMethodName());
        
        reset(invocationMock);
        when(invocationMock.isVoid()).thenReturn(false);
        when(invocationMock.returnsPrimitive()).thenReturn(false);
        when(answerMock.returnsNull()).thenReturn(true);
        answersValidator.validateReturnValue(answerMock, invocationMock);
        verify(reporterMock, never()).wrongTypeOfReturnValue(anyString(), anyString(), anyString());
        
        reset(invocationMock);
        when(invocationMock.isVoid()).thenReturn(false);
        when(invocationMock.returnsPrimitive()).thenReturn(false);
        when(invocationMock.printMethodReturnType()).thenReturn("String");
        when(answerMock.returnsNull()).thenReturn(false);
        when(answerMock.getReturnType()).thenReturn(String.class);
        when(invocationMock.isValidReturnType(String.class)).thenReturn(true);
        answersValidator.validateReturnValue(answerMock, invocationMock);
        verify(reporterMock, never()).wrongTypeOfReturnValue(anyString(), anyString(), anyString());
        
        reset(invocationMock);
        when(invocationMock.isVoid()).thenReturn(false);
        when(invocationMock.returnsPrimitive()).thenReturn(false);
        when(invocationMock.printMethodReturnType()).thenReturn("String");
        when(answerMock.returnsNull()).thenReturn(false);
        when(answerMock.getReturnType()).thenReturn(Integer.class);
        when(invocationMock.isValidReturnType(Integer.class)).thenReturn(false);
        answersValidator.validateReturnValue(answerMock, invocationMock);
        verify(reporterMock).wrongTypeOfReturnValue("String", "Integer", invocationMock.getMethodName());
    }

    @Test
    public void testValidateException() {
        ThrowsException answerMock = mock(ThrowsException.class);
        Throwable throwableMock = mock(Throwable.class);
        
        when(answerMock.getThrowable()).thenReturn(null);
        answersValidator.validateException(answerMock, invocationMock);
        verify(reporterMock).cannotStubWithNullThrowable();
        
        reset(answerMock);
        when(answerMock.getThrowable()).thenReturn(throwableMock);
        when(invocationMock.isValidException(throwableMock)).thenReturn(true);
        answersValidator.validateException(answerMock, invocationMock);
        verify(reporterMock, never()).checkedExceptionInvalid(throwableMock);
        
        reset(answerMock);
        when(answerMock.getThrowable()).thenReturn(throwableMock);
        when(invocationMock.isValidException(throwableMock)).thenReturn(false);
        answersValidator.validateException(answerMock, invocationMock);
        verify(reporterMock).checkedExceptionInvalid(throwableMock);
    }
}