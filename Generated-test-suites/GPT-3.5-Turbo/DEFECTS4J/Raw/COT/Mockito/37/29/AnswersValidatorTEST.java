
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;
import org.mockito.internal.stubbing.answers.AnswersValidator;
import org.mockito.internal.stubbing.answers.DoesNothing;
import org.mockito.internal.stubbing.answers.Returns;
import org.mockito.internal.stubbing.answers.ThrowsException;

import static org.mockito.Mockito.*;

public class AnswersValidatorTest {

    @Test
    public void testValidate_DoNothing() {
        // Setup
        DoesNothing answer = mock(DoesNothing.class);
        Invocation invocation = mock(Invocation.class);
        
        // Exercise
        AnswersValidator validator = new AnswersValidator();
        validator.validate(answer, invocation);
        
        // Verify
        verify(answer).isVoid();
        verifyNoMoreInteractions(answer, invocation);
    }

    @Test
    public void testValidate_Returns_VoidMethod() {
        // Setup
        Returns answer = mock(Returns.class);
        Invocation invocation = mock(Invocation.class);
        when(invocation.isVoid()).thenReturn(true);
        
        // Exercise
        AnswersValidator validator = new AnswersValidator();
        validator.validate(answer, invocation);
        
        // Verify
        verify(reporter).cannotStubVoidMethodWithAReturnValue();
        verifyNoMoreInteractions(reporter, answer, invocation);
    }

    @Test
    public void testValidate_Returns_PrimitiveNullReturnValue() {
        // Setup
        Returns answer = mock(Returns.class);
        Invocation invocation = mock(Invocation.class);
        when(invocation.returnsPrimitive()).thenReturn(true);
        when(answer.returnsNull()).thenReturn(true);
        when(invocation.printMethodReturnType()).thenReturn("int");
        when(invocation.getMethodName()).thenReturn("methodName");
        
        // Exercise
        AnswersValidator validator = new AnswersValidator();
        validator.validate(answer, invocation);
        
        // Verify
        verify(reporter).wrongTypeOfReturnValue("int", "null", "methodName");
        verifyNoMoreInteractions(reporter, answer, invocation);
    }

    @Test
    public void testValidate_Returns_InvalidReturnType() {
        // Setup
        Returns answer = mock(Returns.class);
        Invocation invocation = mock(Invocation.class);
        when(answer.returnsNull()).thenReturn(false);
        when(invocation.isValidReturnType(any())).thenReturn(false);
        when(invocation.printMethodReturnType()).thenReturn("int");
        when(answer.printReturnType()).thenReturn("String");
        when(invocation.getMethodName()).thenReturn("methodName");
        
        // Exercise
        AnswersValidator validator = new AnswersValidator();
        validator.validate(answer, invocation);
        
        // Verify
        verify(reporter).wrongTypeOfReturnValue("int", "String", "methodName");
        verifyNoMoreInteractions(reporter, answer, invocation);
    }

    @Test
    public void testValidate_ThrowsException_NullThrowable() {
        // Setup
        ThrowsException answer = mock(ThrowsException.class);
        Invocation invocation = mock(Invocation.class);
        when(answer.getThrowable()).thenReturn(null);
        
        // Exercise
        AnswersValidator validator = new AnswersValidator();
        validator.validate(answer, invocation);
        
        // Verify
        verify(reporter).cannotStubWithNullThrowable();
        verifyNoMoreInteractions(reporter, answer, invocation);
    }

    @Test
    public void testValidate_ThrowsException_CheckedExceptionInvalid() {
        // Setup
        ThrowsException answer = mock(ThrowsException.class);
        Invocation invocation = mock(Invocation.class);
        Throwable throwable = mock(Throwable.class);
        when(answer.getThrowable()).thenReturn(throwable);
        when(invocation.isValidException(throwable)).thenReturn(false);
        
        // Exercise
        AnswersValidator validator = new AnswersValidator();
        validator.validate(answer, invocation);
        
        // Verify
        verify(reporter).checkedExceptionInvalid(throwable);
        verifyNoMoreInteractions(reporter, answer, invocation);
    }
    
    private Reporter reporter = mock(Reporter.class);
}
