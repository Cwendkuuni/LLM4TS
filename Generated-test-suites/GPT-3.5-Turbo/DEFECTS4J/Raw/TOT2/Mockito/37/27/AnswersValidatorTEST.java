import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;

public class AnswersValidatorTest {

    @Test
    public void testValidateDoNothing() {
        AnswersValidator av = new AnswersValidator();
        Reporter reporterMock = mock(Reporter.class);
        doThrow(new RuntimeException()).when(reporterMock).onlyVoidMethodsCanBeSetToDoNothing();
        Invocation invocationMock = mock(Invocation.class);
        when(invocationMock.isVoid()).thenReturn(false);

        av.validateDoNothing(new DoesNothing(), invocationMock);

        verify(reporterMock).onlyVoidMethodsCanBeSetToDoNothing();
    }

    @Test
    public void testValidateReturnValue() {
        AnswersValidator av = new AnswersValidator();
        Reporter reporterMock = mock(Reporter.class);
        doThrow(new RuntimeException()).when(reporterMock).cannotStubVoidMethodWithAReturnValue();
        Invocation invocationMock = mock(Invocation.class);
        when(invocationMock.isVoid()).thenReturn(true);
        Returns returnsMock = mock(Returns.class);
        when(returnsMock.returnsNull()).thenReturn(true);
        when(invocationMock.returnsPrimitive()).thenReturn(false);

        av.validateReturnValue(returnsMock, invocationMock);

        verify(reporterMock).cannotStubVoidMethodWithAReturnValue();
    }

    @Test
    public void testValidateException() {
        AnswersValidator av = new AnswersValidator();
        Reporter reporterMock = mock(Reporter.class);
        doThrow(new RuntimeException()).when(reporterMock).checkedExceptionInvalid(RuntimeException.class);
        Invocation invocationMock = mock(Invocation.class);
        when(invocationMock.isValidException(RuntimeException.class)).thenReturn(false);
        ThrowsException throwsExceptionMock = mock(ThrowsException.class);
        when(throwsExceptionMock.getThrowable()).thenReturn(new RuntimeException());

        av.validateException(throwsExceptionMock, invocationMock);

        verify(reporterMock).checkedExceptionInvalid(RuntimeException.class);
    }
}