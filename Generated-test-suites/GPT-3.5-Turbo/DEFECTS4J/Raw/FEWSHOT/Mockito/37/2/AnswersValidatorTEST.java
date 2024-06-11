import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;

public class AnswersValidatorTest {
    
    private AnswersValidator answersValidator;
    private Reporter reporter;
    private Answer<?> answer;
    private Invocation invocation;

    @Before
    public void setUp() {
        answersValidator = new AnswersValidator();
        reporter = mock(Reporter.class);
        answer = mock(Answer.class);
        invocation = mock(Invocation.class);
        answersValidator.reporter = reporter;
    }

    @Test
    public void testValidate_DoNothing() {
        DoesNothing doesNothingAnswer = mock(DoesNothing.class);
        when(invocation.isVoid()).thenReturn(false);
        answersValidator.validate(doesNothingAnswer, invocation);
        verify(reporter).onlyVoidMethodsCanBeSetToDoNothing();
        reset(reporter);
        
        when(invocation.isVoid()).thenReturn(true);
        answersValidator.validate(doesNothingAnswer, invocation);
        verifyNoInteractions(reporter);
    }
    
    @Test
    public void testValidate_Returns() {
        Returns returnsAnswer = mock(Returns.class);
        when(invocation.isVoid()).thenReturn(true);
        answersValidator.validate(returnsAnswer, invocation);
        verify(reporter).cannotStubVoidMethodWithAReturnValue();
        reset(reporter);
        
        when(invocation.isVoid()).thenReturn(false);
        when(invocation.returnsPrimitive()).thenReturn(true);
        when(returnsAnswer.returnsNull()).thenReturn(true);
        answersValidator.validate(returnsAnswer, invocation);
        verify(reporter).wrongTypeOfReturnValue(anyString(), eq("null"), anyString());
        
        reset(reporter);
        when(invocation.isVoid()).thenReturn(false);
        when(invocation.returnsPrimitive()).thenReturn(false);
        when(invocation.isValidReturnType(any(Class.class))).thenReturn(false);
        when(returnsAnswer.returnsNull()).thenReturn(false);
        when(returnsAnswer.getReturnType()).thenReturn(String.class);
        answersValidator.validate(returnsAnswer, invocation);
        verify(reporter).wrongTypeOfReturnValue(anyString(), anyString(), anyString());
        reset(reporter);
        
        when(invocation.isValidReturnType(eq(String.class))).thenReturn(true);
        answersValidator.validate(returnsAnswer, invocation);
        verifyNoInteractions(reporter);
    }
    
    @Test
    public void testValidate_ThrowsException() {
        ThrowsException throwsExceptionAnswer = mock(ThrowsException.class);
        when(throwsExceptionAnswer.getThrowable()).thenReturn(null);
        answersValidator.validate(throwsExceptionAnswer, invocation);
        verify(reporter).cannotStubWithNullThrowable();
        reset(reporter);
        
        Throwable throwable = mock(Throwable.class);
        when(throwsExceptionAnswer.getThrowable()).thenReturn(throwable);
        when(invocation.isValidException(eq(throwable))).thenReturn(false);
        when(throwable instanceof RuntimeException).thenReturn(false);
        when(throwable instanceof Error).thenReturn(false);
        answersValidator.validate(throwsExceptionAnswer, invocation);
        verify(reporter).checkedExceptionInvalid(eq(throwable));
        reset(reporter);
        
        when(invocation.isValidException(eq(throwable))).thenReturn(true);
        when(throwable instanceof RuntimeException).thenReturn(true);
        answersValidator.validate(throwsExceptionAnswer, invocation);
        verifyNoInteractions(reporter);
        
        reset(reporter);
        when(throwable instanceof RuntimeException).thenReturn(false);
        when(throwable instanceof Error).thenReturn(true);
        answersValidator.validate(throwsExceptionAnswer, invocation);
        verifyNoInteractions(reporter);
    }
}