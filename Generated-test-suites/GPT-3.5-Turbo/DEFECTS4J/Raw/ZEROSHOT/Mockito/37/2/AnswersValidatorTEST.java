import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;

public class AnswersValidatorTest {
    
    private AnswersValidator answersValidator;
    private Reporter reporter;
    private Answer<Object> answer;
    private Invocation invocation;

    @Before
    public void setup() {
        answersValidator = new AnswersValidator();
        reporter = mock(Reporter.class);
        answer = mock(Answer.class);
        invocation = mock(Invocation.class);
    }
    
    @Test
    public void testValidate_ThrowsException() {
        ThrowsException throwsExceptionAnswer = mock(ThrowsException.class);
        when(answer instanceof ThrowsException).thenReturn(true);
        when((ThrowsException) answer).thenReturn(throwsExceptionAnswer);
        
        answersValidator.validate(answer, invocation);
        
        verify(answersValidator).validateException(throwsExceptionAnswer, invocation);
        verifyNoMoreInteractions(answersValidator);
    }
    
    @Test
    public void testValidate_Returns() {
        Returns returnsAnswer = mock(Returns.class);
        when(answer instanceof Returns).thenReturn(true);
        when((Returns) answer).thenReturn(returnsAnswer);
        
        answersValidator.validate(answer, invocation);
        
        verify(answersValidator).validateReturnValue(returnsAnswer, invocation);
        verifyNoMoreInteractions(answersValidator);
    }
    
    @Test
    public void testValidate_DoesNothing() {
        DoesNothing doesNothingAnswer = mock(DoesNothing.class);
        when(answer instanceof DoesNothing).thenReturn(true);
        when((DoesNothing) answer).thenReturn(doesNothingAnswer);
        
        answersValidator.validate(answer, invocation);
        
        verify(answersValidator).validateDoNothing(doesNothingAnswer, invocation);
        verifyNoMoreInteractions(answersValidator);
    }
    
    @Test
    public void testValidateDoNothing_ValidInvocation() {
        DoesNothing doesNothingAnswer = mock(DoesNothing.class);
        when(invocation.isVoid()).thenReturn(true);
        when(answer instanceof DoesNothing).thenReturn(true);
        when((DoesNothing) answer).thenReturn(doesNothingAnswer);
        
        answersValidator.validateDoNothing(doesNothingAnswer, invocation);
        
        verifyNoInteractions(reporter);
    }
    
    @Test
    public void testValidateDoNothing_InvalidInvocation() {
        DoesNothing doesNothingAnswer = mock(DoesNothing.class);
        when(invocation.isVoid()).thenReturn(false);
        when(answer instanceof DoesNothing).thenReturn(true);
        when((DoesNothing) answer).thenReturn(doesNothingAnswer);
        
        answersValidator.validateDoNothing(doesNothingAnswer, invocation);
        
        verify(reporter).onlyVoidMethodsCanBeSetToDoNothing();
    }
    
    @Test
    public void testValidateReturnValue_VoidInvocation() {
        Returns returnsAnswer = mock(Returns.class);
        when(invocation.isVoid()).thenReturn(true);
        when(answer instanceof Returns).thenReturn(true);
        when((Returns) answer).thenReturn(returnsAnswer);
        
        answersValidator.validateReturnValue(returnsAnswer, invocation);
        
        verify(reporter).cannotStubVoidMethodWithAReturnValue();
    }
    
    @Test
    public void testValidateReturnValue_ReturnsNull() {
        Returns returnsAnswer = mock(Returns.class);
        when(invocation.isVoid()).thenReturn(false);
        when(returnsAnswer.returnsNull()).thenReturn(true);
        when(invocation.returnsPrimitive()).thenReturn(true);
        when(answer instanceof Returns).thenReturn(true);
        when((Returns) answer).thenReturn(returnsAnswer);
        
        answersValidator.validateReturnValue(returnsAnswer, invocation);
        
        verify(reporter).wrongTypeOfReturnValue(invocation.printMethodReturnType(), "null", invocation.getMethodName());
    }
    
    @Test
    public void testValidateReturnValue_ValidReturnType() {
        Returns returnsAnswer = mock(Returns.class);
        when(invocation.isVoid()).thenReturn(false);
        when(returnsAnswer.returnsNull()).thenReturn(false);
        when(invocation.isValidReturnType(returnsAnswer.getReturnType())).thenReturn(true);
        when(answer instanceof Returns).thenReturn(true);
        when((Returns) answer).thenReturn(returnsAnswer);
        
        answersValidator.validateReturnValue(returnsAnswer, invocation);
        
        verifyNoInteractions(reporter);
    }
    
    @Test
    public void testValidateReturnValue_InvalidReturnType() {
        Returns returnsAnswer = mock(Returns.class);
        when(invocation.isVoid()).thenReturn(false);
        when(returnsAnswer.returnsNull()).thenReturn(false);
        when(invocation.isValidReturnType(returnsAnswer.getReturnType())).thenReturn(false);
        when(invocation.printMethodReturnType()).thenReturn("ReturnType");
        when(returnsAnswer.printReturnType()).thenReturn("ReturnType");
        when(invocation.getMethodName()).thenReturn("methodName");
        when(answer instanceof Returns).thenReturn(true);
        when((Returns) answer).thenReturn(returnsAnswer);
        
        answersValidator.validateReturnValue(returnsAnswer, invocation);
        
        verify(reporter).wrongTypeOfReturnValue("ReturnType", "ReturnType", "methodName");
    }
    
    @Test
    public void testValidateException_NullThrowable() {
        ThrowsException throwsExceptionAnswer = mock(ThrowsException.class);
        when(throwsExceptionAnswer.getThrowable()).thenReturn(null);
        when(answer instanceof ThrowsException).thenReturn(true);
        when((ThrowsException) answer).thenReturn(throwsExceptionAnswer);
        
        answersValidator.validateException(throwsExceptionAnswer, invocation);
        
        verify(reporter).cannotStubWithNullThrowable();
    }
    
    @Test
    public void testValidateException_RuntimeException() {
        ThrowsException throwsExceptionAnswer = mock(ThrowsException.class);
        when(throwsExceptionAnswer.getThrowable()).thenReturn(new RuntimeException());
        when(answer instanceof ThrowsException).thenReturn(true);
        when((ThrowsException) answer).thenReturn(throwsExceptionAnswer);
        
        answersValidator.validateException(throwsExceptionAnswer, invocation);
        
        verifyNoInteractions(reporter);
    }
    
    @Test
    public void testValidateException_Error() {
        ThrowsException throwsExceptionAnswer = mock(ThrowsException.class);
        when(throwsExceptionAnswer.getThrowable()).thenReturn(new Error());
        when(answer instanceof ThrowsException).thenReturn(true);
        when((ThrowsException) answer).thenReturn(throwsExceptionAnswer);
        
        answersValidator.validateException(throwsExceptionAnswer, invocation);
        
        verifyNoInteractions(reporter);
    }
    
    @Test
    public void testValidateException_CheckedExceptionValid() {
        ThrowsException throwsExceptionAnswer = mock(ThrowsException.class);
        Throwable throwable = mock(Throwable.class);
        when(throwsExceptionAnswer.getThrowable()).thenReturn(throwable);
        when(invocation.isValidException(throwable)).thenReturn(true);
        when(answer instanceof ThrowsException).thenReturn(true);
        when((ThrowsException) answer).thenReturn(throwsExceptionAnswer);
        
        answersValidator.validateException(throwsExceptionAnswer, invocation);
        
        verifyNoInteractions(reporter);
    }
    
    @Test
    public void testValidateException_CheckedExceptionInvalid() {
        ThrowsException throwsExceptionAnswer = mock(ThrowsException.class);
        Throwable throwable = mock(Throwable.class);
        when(throwsExceptionAnswer.getThrowable()).thenReturn(throwable);
        when(invocation.isValidException(throwable)).thenReturn(false);
        when(answer instanceof ThrowsException).thenReturn(true);
        when((ThrowsException) answer).thenReturn(throwsExceptionAnswer);
        
        answersValidator.validateException(throwsExceptionAnswer, invocation);
        
        verify(reporter).checkedExceptionInvalid(throwable);
    }
}