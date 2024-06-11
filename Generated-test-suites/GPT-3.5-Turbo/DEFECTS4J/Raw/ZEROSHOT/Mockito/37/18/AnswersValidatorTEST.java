
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AnswersValidatorTest {

    private AnswersValidator answersValidator;
    private Reporter reporterMock;

    @Before
    public void setup() {
        answersValidator = new AnswersValidator();
        reporterMock = mock(Reporter.class);
        answersValidator.setReporter(reporterMock);
    }

    @Test
    public void testValidate_ThrowsException() {
        Answer<Throwable> answerMock = mock(ThrowsException.class);
        Invocation invocationMock = mock(Invocation.class);

        answersValidator.validate(answerMock, invocationMock);

        verify(answerMock, times(1)).getThrowable();
        verify(reporterMock, times(1)).cannotStubWithNullThrowable();
        verifyNoMoreInteractions(answerMock, invocationMock, reporterMock);
    }

    @Test
    public void testValidate_Returns() {
        Answer<Object> answerMock = mock(Returns.class);
        Invocation invocationMock = mock(Invocation.class);

        answersValidator.validate(answerMock, invocationMock);

        verify(answerMock, times(1)).returnsNull();
        verify(answerMock, times(1)).getReturnType();
        verify(invocationMock, times(1)).isVoid();
        verify(invocationMock, times(1)).printMethodReturnType();
        verify(reporterMock, times(1)).cannotStubVoidMethodWithAReturnValue();
        verify(reporterMock, times(1)).wrongTypeOfReturnValue(anyString(), anyString(), anyString());
        verifyNoMoreInteractions(answerMock, invocationMock, reporterMock);
    }

    @Test
    public void testValidate_DoesNothing() {
        Answer<Void> answerMock = mock(DoesNothing.class);
        Invocation invocationMock = mock(Invocation.class);

        answersValidator.validate(answerMock, invocationMock);

        verify(invocationMock, times(1)).isVoid();
        verify(reporterMock, times(1)).onlyVoidMethodsCanBeSetToDoNothing();
        verifyNoMoreInteractions(answerMock, invocationMock, reporterMock);
    }
}
