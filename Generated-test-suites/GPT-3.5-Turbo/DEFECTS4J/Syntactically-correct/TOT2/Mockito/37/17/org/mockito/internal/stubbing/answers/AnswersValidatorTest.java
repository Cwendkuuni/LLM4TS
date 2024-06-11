import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.stubbing.answers.*;

public class AnswersValidatorTest {

    private AnswersValidator answersValidator;
    
    @Before
    public void setUp() {
        answersValidator = new AnswersValidator();
    }
    
    @Test
    public void testValidateDoNothing() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.isVoid()).thenReturn(true);
        DoesNothing answer = mock(DoesNothing.class);
        
        answersValidator.validateDoNothing(answer, invocation);
        
        verifyNoMoreInteractions(answer);
    }
    
    @Test
    public void testValidateReturnValue() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.isVoid()).thenReturn(false);
        when(invocation.returnsPrimitive()).thenReturn(true);
        Returns answer = mock(Returns.class);
        when(answer.returnsNull()).thenReturn(true);
        when(invocation.printMethodReturnType()).thenReturn("int");
        when(invocation.getMethodName()).thenReturn("mockMethod");
        
        answersValidator.validateReturnValue(answer, invocation);
        
        verifyNoMoreInteractions(answer);
    }
    
    @Test
    public void testValidateException() {
        Invocation invocation = mock(Invocation.class);
        ThrowsException answer = mock(ThrowsException.class);
        when(answer.getThrowable()).thenReturn(new MockitoException("Test Exception"));
        when(invocation.isValidException(any())).thenReturn(true);
        
        answersValidator.validateException(answer, invocation);
        
        verifyNoMoreInteractions(answer);
    }

}