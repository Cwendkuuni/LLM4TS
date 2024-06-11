package org.mockito.internal.stubbing.answers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;

public class AnswersValidatorTest {
    @Test
    public void testValidate_ThrowsException() {
        ThrowsException answer = mock(ThrowsException.class);
        Invocation invocation = mock(Invocation.class);
        
        when(answer instanceof ThrowsException).thenReturn(true);
        
        // Test case 1
        when(invocation.isValidException(null)).thenReturn(false);
        Reporter reporter = mock(Reporter.class);
        doNothing().when(reporter).cannotStubWithNullThrowable();
        assertEquals(reporter, validateException(answer, invocation));
        
        // Test case 2
        when(throwable instanceof RuntimeException || throwable instanceof Error).thenReturn(true);
        assertEquals(reporter, validateException(answer, invocation));
        
        // Test case 3
        when(invocation.isValidException(throwable)).thenReturn(false);
        doNothing().when(reporter).checkedExceptionInvalid(throwable);
        assertEquals(reporter, validateException(answer, invocation));
    }
    
    @Test
    public void testValidate_Returns() {
        Returns answer = mock(Returns.class);
        Invocation invocation = mock(Invocation.class);
        
        when(answer instanceof Returns).thenReturn(true);
        when(invocation.isVoid()).thenReturn(false);
        
        // Test case 1
        when(invocation.isVoid()).thenReturn(true);
        Reporter reporter = mock(Reporter.class);
        doNothing().when(reporter).cannotStubVoidMethodWithAReturnValue();
        assertEquals(reporter, validateReturnValue(answer, invocation));
        
        // Test case 2
        when(invocation.returnsPrimitive()).thenReturn(true);
        when(answer.returnsNull()).thenReturn(true);
        String returnType = invocation.printMethodReturnType();
        String nullValue = "null";
        String methodName = invocation.getMethodName();
        doNothing().when(reporter).wrongTypeOfReturnValue(returnType, nullValue, methodName);
        assertEquals(reporter, validateReturnValue(answer, invocation));
        
        // Test case 3
        when(answer.returnsNull()).thenReturn(false);
        when(invocation.isValidReturnType(answer.getReturnType())).thenReturn(false);
        String answerReturnType = answer.printReturnType();
        doNothing().when(reporter).wrongTypeOfReturnValue(returnType, answerReturnType, methodName);
        assertEquals(reporter, validateReturnValue(answer, invocation));
    }
    
    @Test
    public void testValidate_DoNothing() {
        DoesNothing answer = mock(DoesNothing.class);
        Invocation invocation = mock(Invocation.class);
        
        when(answer instanceof DoesNothing).thenReturn(true);
        when(invocation.isVoid()).thenReturn(false);
        
        // Test case
        Reporter reporter = mock(Reporter.class);
        doNothing().when(reporter).onlyVoidMethodsCanBeSetToDoNothing();
        assertEquals(reporter, validateDoNothing(answer, invocation));
    }
}