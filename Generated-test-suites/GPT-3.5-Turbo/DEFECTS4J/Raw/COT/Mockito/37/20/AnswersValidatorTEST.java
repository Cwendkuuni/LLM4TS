import static org.mockito.Mockito.*;

import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;
import org.junit.Test;

public class AnswersValidatorTest {

    private AnswersValidator answersValidator = new AnswersValidator();
    private Reporter reporter = mock(Reporter.class);
    private Answer<?> answer = mock(Answer.class);
    private Invocation invocation = mock(Invocation.class);

    @Test
    public void testValidate_DoNothing_onlyVoidMethodsCanBeSetToDoNothing() {
        DoesNothing doesNothingAnswer = mock(DoesNothing.class);
        when(invocation.isVoid()).thenReturn(false);
        
        answersValidator.validateDoNothing(doesNothingAnswer, invocation);
        
        verify(reporter).onlyVoidMethodsCanBeSetToDoNothing();
    }

    @Test
    public void testValidate_ReturnValue_cannotStubVoidMethodWithAReturnValue() {
        Returns returnsAnswer = mock(Returns.class);
        when(invocation.isVoid()).thenReturn(true);
        
        answersValidator.validateReturnValue(returnsAnswer, invocation);
        
        verify(reporter).cannotStubVoidMethodWithAReturnValue();
    }
    
    @Test
    public void testValidate_ReturnValue_wrongTypeOfReturnValue_null() {
        Returns returnsAnswer = mock(Returns.class);
        when(invocation.isVoid()).thenReturn(false);
        when(invocation.returnsPrimitive()).thenReturn(true);
        when(invocation.printMethodReturnType()).thenReturn("int");
        when(returnsAnswer.returnsNull()).thenReturn(true);
        when(invocation.getMethodName()).thenReturn("methodName");
        
        answersValidator.validateReturnValue(returnsAnswer, invocation);
        
        verify(reporter).wrongTypeOfReturnValue("int", "null", "methodName");
    }
    
    @Test
    public void testValidate_ReturnValue_wrongTypeOfReturnValue() {
        Returns returnsAnswer = mock(Returns.class);
        when(invocation.isVoid()).thenReturn(false);
        when(invocation.printMethodReturnType()).thenReturn("int");
        when(returnsAnswer.returnsNull()).thenReturn(false);
        when(returnsAnswer.getReturnType()).thenReturn(String.class);
        when(invocation.isValidReturnType(String.class)).thenReturn(false);
        when(invocation.getMethodName()).thenReturn("methodName");
        
        answersValidator.validateReturnValue(returnsAnswer, invocation);
        
        verify(reporter).wrongTypeOfReturnValue("int", "String", "methodName");
    }
    
    @Test
    public void testValidate_Exception_cannotStubWithNullThrowable() {
        ThrowsException throwsExceptionAnswer = mock(ThrowsException.class);
        when(throwsExceptionAnswer.getThrowable()).thenReturn(null);
        
        answersValidator.validateException(throwsExceptionAnswer, invocation);
        
        verify(reporter).cannotStubWithNullThrowable();
    }
    
    @Test
    public void testValidate_Exception_checkedExceptionInvalid() {
        ThrowsException throwsExceptionAnswer = mock(ThrowsException.class);
        Throwable throwable = mock(Throwable.class);
        when(throwsExceptionAnswer.getThrowable()).thenReturn(throwable);
        when(throwable instanceof RuntimeException).thenReturn(false);
        when(throwable instanceof Error).thenReturn(false);
        when(invocation.isValidException(throwable)).thenReturn(false);
        
        answersValidator.validateException(throwsExceptionAnswer, invocation);
        
        verify(reporter).checkedExceptionInvalid(throwable);
    }

    @Test
    public void testValidate_Exception_noValidationNeededForRuntimeExceptionOrError() {
        ThrowsException throwsExceptionAnswer = mock(ThrowsException.class);
        Throwable throwable = mock(Throwable.class);
        when(throwsExceptionAnswer.getThrowable()).thenReturn(throwable);
        when(throwable instanceof RuntimeException).thenReturn(true);
        when(throwable instanceof Error).thenReturn(true);
        
        answersValidator.validateException(throwsExceptionAnswer, invocation);
        
        verifyZeroInteractions(reporter);
    }
    
    @Test
    public void testValidate_callsValidateExceptionForThrowsException() {
        ThrowsException throwsExceptionAnswer = mock(ThrowsException.class);
        when(answer instanceof ThrowsException).thenReturn(true);
        
        answersValidator.validate(answer, invocation);
        
        verify(answersValidator).validateException(throwsExceptionAnswer, invocation);
    }

    @Test
    public void testValidate_callsValidateReturnValueForReturns() {
        Returns returnsAnswer = mock(Returns.class);
        when(answer instanceof Returns).thenReturn(true);
        
        answersValidator.validate(answer, invocation);
        
        verify(answersValidator).validateReturnValue(returnsAnswer, invocation);
    }
    
    @Test
    public void testValidate_callsValidateDoNothingForDoesNothing() {
        DoesNothing doesNothingAnswer = mock(DoesNothing.class);
        when(answer instanceof DoesNothing).thenReturn(true);
        
        answersValidator.validate(answer, invocation);
        
        verify(answersValidator).validateDoNothing(doesNothingAnswer, invocation);
    }

    @Test
    public void testValidate_callsValidateMethodsInCorrectOrder() {
        when(answer instanceof ThrowsException).thenReturn(true);
        when(answer instanceof Returns).thenReturn(true);
        when(answer instanceof DoesNothing).thenReturn(true);
        
        answersValidator.validate(answer, invocation);
        
        InOrder inOrder = inOrder(answersValidator);
        inOrder.verify(answersValidator).validateException(any(ThrowsException.class), eq(invocation));
        inOrder.verify(answersValidator).validateReturnValue(any(Returns.class), eq(invocation));
        inOrder.verify(answersValidator).validateDoNothing(any(DoesNothing.class), eq(invocation));
    }    

}