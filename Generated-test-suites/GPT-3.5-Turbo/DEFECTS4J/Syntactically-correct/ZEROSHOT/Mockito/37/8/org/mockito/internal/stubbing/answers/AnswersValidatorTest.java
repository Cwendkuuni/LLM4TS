import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;

public class AnswersValidatorTest {

    private AnswersValidator answersValidator;
    private Reporter reporter;
    
    @Before
    public void setup() {
        answersValidator = new AnswersValidator();
        reporter = mock(Reporter.class);
        answersValidator.reporter = reporter;
    }
    
    @Test
    public void testValidate_ThrowsException() {
        ThrowsException answer = mock(ThrowsException.class);
        Invocation invocation = mock(Invocation.class);
        
        answersValidator.validate(answer, invocation);
        
        verify(reporter, never()).onlyVoidMethodsCanBeSetToDoNothing();
        verify(reporter, never()).cannotStubVoidMethodWithAReturnValue();
        verify(reporter, never()).wrongTypeOfReturnValue(any(), any(), any());
        verify(reporter, never()).cannotStubWithNullThrowable();
        verify(reporter, never()).checkedExceptionInvalid(any());
    }
    
    @Test
    public void testValidate_Returns() {
        Returns answer = mock(Returns.class);
        Invocation invocation = mock(Invocation.class);
        
        // Test void invocation
        when(invocation.isVoid()).thenReturn(true);
        
        answersValidator.validate(answer, invocation);
        
        verify(reporter).cannotStubVoidMethodWithAReturnValue();
        verify(reporter, never()).onlyVoidMethodsCanBeSetToDoNothing();
        verify(reporter, never()).wrongTypeOfReturnValue(any(), any(), any());
        verify(reporter, never()).cannotStubWithNullThrowable();
        verify(reporter, never()).checkedExceptionInvalid(any());
        
        reset(reporter);
        
        // Test non-void invocation with returnsNull true
        when(invocation.isVoid()).thenReturn(false);
        when(answer.returnsNull()).thenReturn(true);
        when(invocation.returnsPrimitive()).thenReturn(true);
        
        answersValidator.validate(answer, invocation);
        
        verify(reporter).wrongTypeOfReturnValue(any(), eq("null"), any());
        verify(reporter, never()).onlyVoidMethodsCanBeSetToDoNothing();
        verify(reporter, never()).cannotStubVoidMethodWithAReturnValue();
        verify(reporter, never()).cannotStubWithNullThrowable();
        verify(reporter, never()).checkedExceptionInvalid(any());
        
        reset(reporter);
        
        // Test non-void invocation with returnsNull false and valid return type
        when(answer.returnsNull()).thenReturn(false);
        when(invocation.isValidReturnType(any())).thenReturn(true);
        
        answersValidator.validate(answer, invocation);
        
        verify(reporter, never()).wrongTypeOfReturnValue(any(), any(), any());
        verify(reporter, never()).onlyVoidMethodsCanBeSetToDoNothing();
        verify(reporter, never()).cannotStubVoidMethodWithAReturnValue();
        verify(reporter, never()).cannotStubWithNullThrowable();
        verify(reporter, never()).checkedExceptionInvalid(any());
        
        reset(reporter);
        
        // Test non-void invocation with returnsNull false and invalid return type
        when(invocation.isValidReturnType(any())).thenReturn(false);
        
        answersValidator.validate(answer, invocation);
        
        verify(reporter).wrongTypeOfReturnValue(any(), any(), any());
        verify(reporter, never()).onlyVoidMethodsCanBeSetToDoNothing();
        verify(reporter, never()).cannotStubVoidMethodWithAReturnValue();
        verify(reporter, never()).cannotStubWithNullThrowable();
        verify(reporter, never()).checkedExceptionInvalid(any());
        
    }
    
    @Test
    public void testValidate_DoesNothing() {
        DoesNothing answer = mock(DoesNothing.class);
        Invocation invocation = mock(Invocation.class);
        
        // Test non-void invocation
        when(invocation.isVoid()).thenReturn(false);
        
        answersValidator.validate(answer, invocation);
        
        verify(reporter).onlyVoidMethodsCanBeSetToDoNothing();
        verify(reporter, never()).wrongTypeOfReturnValue(any(), any(), any());
        verify(reporter, never()).cannotStubVoidMethodWithAReturnValue();
        verify(reporter, never()).cannotStubWithNullThrowable();
        verify(reporter, never()).checkedExceptionInvalid(any());
        
        reset(reporter);
        
        // Test void invocation
        when(invocation.isVoid()).thenReturn(true);
        
        answersValidator.validate(answer, invocation);
        
        verify(reporter, never()).onlyVoidMethodsCanBeSetToDoNothing();
        verify(reporter, never()).wrongTypeOfReturnValue(any(), any(), any());
        verify(reporter, never()).cannotStubVoidMethodWithAReturnValue();
        verify(reporter, never()).cannotStubWithNullThrowable();
        verify(reporter, never()).checkedExceptionInvalid(any());
    }

}