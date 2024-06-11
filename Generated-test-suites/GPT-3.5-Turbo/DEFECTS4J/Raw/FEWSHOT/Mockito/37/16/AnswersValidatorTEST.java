import static org.mockito.Mockito.*;

import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;
import org.mockito.internal.stubbing.answers.AnswersValidator;
import org.mockito.internal.stubbing.answers.Returns;
import org.mockito.internal.stubbing.answers.ThrowsException;
import org.mockito.internal.stubbing.answers.DoesNothing;

import org.junit.Before;
import org.junit.Test;

public class AnswersValidatorTest {

    private Reporter mockReporter;
    private Answer<?> mockAnswer;
    private Invocation mockInvocation;
    private AnswersValidator answersValidator;

    @Before
    public void setup() {
        mockReporter = mock(Reporter.class);
        mockAnswer = mock(Answer.class);
        mockInvocation = mock(Invocation.class);
        answersValidator = new AnswersValidator();
        answersValidator.reporter = mockReporter;
    }

    @Test
    public void testValidate_DoNothing_VoidInvocation() {
        when(mockInvocation.isVoid()).thenReturn(true);
        answersValidator.validate(mockAnswer, mockInvocation);
        verify(mockReporter).onlyVoidMethodsCanBeSetToDoNothing();
    }

    @Test
    public void testValidate_DoNothing_NonVoidInvocation() {
        when(mockInvocation.isVoid()).thenReturn(false);
        answersValidator.validate(mockAnswer, mockInvocation);
        verify(mockReporter, never()).onlyVoidMethodsCanBeSetToDoNothing();
    }

    @Test
    public void testValidate_ReturnValue_VoidInvocation() {
        when(mockInvocation.isVoid()).thenReturn(true);
        answersValidator.validate(mockAnswer, mockInvocation);
        verify(mockReporter).cannotStubVoidMethodWithAReturnValue();
    }

    @Test
    public void testValidate_ReturnValue_NonVoidInvocation_NullReturn() {
        when(mockInvocation.isVoid()).thenReturn(false);
        when(mockAnswer.returnsNull()).thenReturn(true);
        when(mockInvocation.returnsPrimitive()).thenReturn(false);
        answersValidator.validate(mockAnswer, mockInvocation);
        verify(mockReporter, never()).cannotStubVoidMethodWithAReturnValue();
        verify(mockReporter).wrongTypeOfReturnValue(anyString(), eq("null"), anyString());
    }

    @Test
    public void testValidate_ReturnValue_NonVoidInvocation_InvalidReturnNull() {
        when(mockInvocation.isVoid()).thenReturn(false);
        when(mockAnswer.returnsNull()).thenReturn(false);
        when(mockInvocation.returnsPrimitive()).thenReturn(false);
        when(mockInvocation.isValidReturnType(any())).thenReturn(false);
        answersValidator.validate(mockAnswer, mockInvocation);
        verify(mockReporter, never()).cannotStubVoidMethodWithAReturnValue();
        verify(mockReporter).wrongTypeOfReturnValue(anyString(), anyString(), anyString());
    }

    @Test
    public void testValidate_ReturnValue_NonVoidInvocation_ValidReturnNull() {
        when(mockInvocation.isVoid()).thenReturn(false);
        when(mockAnswer.returnsNull()).thenReturn(false);
        when(mockInvocation.returnsPrimitive()).thenReturn(false);
        when(mockInvocation.isValidReturnType(any())).thenReturn(true);
        answersValidator.validate(mockAnswer, mockInvocation);
        verify(mockReporter, never()).cannotStubVoidMethodWithAReturnValue();
        verify(mockReporter, never()).wrongTypeOfReturnValue(anyString(), anyString(), anyString());
    }
    
    @Test
    public void testValidate_Exception_NullThrowable() {
        when(mockAnswer instanceof ThrowsException).thenReturn(true);
        when(((ThrowsException) mockAnswer).getThrowable()).thenReturn(null);
        answersValidator.validate(mockAnswer, mockInvocation);
        verify(mockReporter).cannotStubWithNullThrowable();
    }

    @Test
    public void testValidate_Exception_RuntimeException() {
        when(mockAnswer instanceof ThrowsException).thenReturn(true);
        when(((ThrowsException) mockAnswer).getThrowable()).thenReturn(new RuntimeException());
        answersValidator.validate(mockAnswer, mockInvocation);
        verify(mockReporter, never()).cannotStubWithNullThrowable();
        verify(mockReporter, never()).checkedExceptionInvalid(any());
    }

    @Test
    public void testValidate_Exception_Error() {
        when(mockAnswer instanceof ThrowsException).thenReturn(true);
        when(((ThrowsException) mockAnswer).getThrowable()).thenReturn(new Error());
        answersValidator.validate(mockAnswer, mockInvocation);
        verify(mockReporter, never()).cannotStubWithNullThrowable();
        verify(mockReporter, never()).checkedExceptionInvalid(any());
    }

    @Test
    public void testValidate_Exception_CheckedInvalid() {
        when(mockAnswer instanceof ThrowsException).thenReturn(true);
        when(((ThrowsException) mockAnswer).getThrowable()).thenReturn(new Exception());
        when(mockInvocation.isValidException(any())).thenReturn(false);
        answersValidator.validate(mockAnswer, mockInvocation);
        verify(mockReporter, never()).cannotStubWithNullThrowable();
        verify(mockReporter).checkedExceptionInvalid(any());
    }

    @Test
    public void testValidate_Exception_CheckedValid() {
        when(mockAnswer instanceof ThrowsException).thenReturn(true);
        when(((ThrowsException) mockAnswer).getThrowable()).thenReturn(new Exception());
        when(mockInvocation.isValidException(any())).thenReturn(true);
        answersValidator.validate(mockAnswer, mockInvocation);
        verify(mockReporter, never()).cannotStubWithNullThrowable();
        verify(mockReporter, never()).checkedExceptionInvalid(any());
    }
}