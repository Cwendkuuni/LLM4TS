
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;

public class AnswersValidatorTest {

    private AnswersValidator answersValidator;
    private Reporter reporter;
    
    @Before
    public void setup() {
        answersValidator = new AnswersValidator();
        reporter = spy(new Reporter());
        answersValidator.reporter = reporter;
    }
    
    @Test
    public void testValidateDoNothing_withVoidInvocation() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.isVoid()).thenReturn(true);
        
        answersValidator.validate(mock(DoesNothing.class), invocation);
        
        verify(reporter).onlyVoidMethodsCanBeSetToDoNothing();
    }
    
    @Test
    public void testValidateDoNothing_withNonVoidInvocation() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.isVoid()).thenReturn(false);
        
        answersValidator.validate(mock(DoesNothing.class), invocation);
        
        verify(reporter, never()).onlyVoidMethodsCanBeSetToDoNothing();
    }
    
    @Test
    public void testValidateReturnValue_withVoidInvocation() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.isVoid()).thenReturn(true);
        
        answersValidator.validate(mock(Returns.class), invocation);
        
        verify(reporter).cannotStubVoidMethodWithAReturnValue();
    }
    
    @Test
    public void testValidateReturnValue_withNotNullReturnValue_andInvalidReturnType() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.isVoid()).thenReturn(false);
        when(invocation.isValidReturnType(any(Class.class))).thenReturn(false);
        
        answersValidator.validate(createMockReturns(false), invocation);
        
        verify(reporter).wrongTypeOfReturnValue(anyString(), anyString(), anyString());
    }
    
    @Test
    public void testValidateReturnValue_withNullReturnValue_andInvalidReturnType() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.isVoid()).thenReturn(false);
        when(invocation.isValidReturnType(any(Class.class))).thenReturn(false);
        
        answersValidator.validate(createMockReturns(true), invocation);
        
        verify(reporter).wrongTypeOfReturnValue(anyString(), anyString(), anyString());
    }
    
    @Test
    public void testValidateReturnValue_withNullReturnValue_andValidReturnType() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.isVoid()).thenReturn(false);
        when(invocation.isValidReturnType(any(Class.class))).thenReturn(true);
        
        answersValidator.validate(createMockReturns(true), invocation);
        
        verify(reporter, never()).wrongTypeOfReturnValue(anyString(), anyString(), anyString());
    }
    
    @Test
    public void testValidateException_withNullThrowable() {
        Invocation invocation = mock(Invocation.class);
        
        answersValidator.validate(createMockThrowsException(null), invocation);
        
        verify(reporter).cannotStubWithNullThrowable();
    }
    
    @Test
    public void testValidateException_withRuntimeException() {
        Invocation invocation = mock(Invocation.class);
        
        answersValidator.validate(createMockThrowsException(new RuntimeException()), invocation);
        
        verify(reporter, never()).checkedExceptionInvalid(any(Throwable.class));
    }
    
    @Test
    public void testValidateException_withError() {
        Invocation invocation = mock(Invocation.class);
        
        answersValidator.validate(createMockThrowsException(new Error()), invocation);
        
        verify(reporter, never()).checkedExceptionInvalid(any(Throwable.class));
    }
    
    @Test
    public void testValidateException_withCheckedException() {
        Invocation invocation = mock(Invocation.class);
        
        answersValidator.validate(createMockThrowsException(new Exception()), invocation);
        
        verify(reporter).checkedExceptionInvalid(any(Throwable.class));
    }
    
    private ThrowsException createMockThrowsException(Throwable throwable) {
        ThrowsException throwsException = mock(ThrowsException.class);
        when(throwsException.getThrowable()).thenReturn(throwable);
        return throwsException;
    }
    
    private Returns createMockReturns(boolean returnsNull) {
        Returns returns = mock(Returns.class);
        when(returns.returnsNull()).thenReturn(returnsNull);
        return returns;
    }
}
