package org.mockito.internal.stubbing.answers;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;

public class AnswersValidatorTest {
    
    private Reporter reporter = Mockito.mock(Reporter.class);
    
    @Test
    public void testValidateDoNothing_withVoidInvocation() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Answer<?> answer = Mockito.mock(DoesNothing.class);
        when(invocation.isVoid()).thenReturn(true);
        
        AnswersValidator validator = new AnswersValidator();
        validator.validate(answer, invocation);
        
        verifyZeroInteractions(reporter);
    }
    
    @Test
    public void testValidateDoNothing_withNonVoidInvocation() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Answer<?> answer = Mockito.mock(DoesNothing.class);
        when(invocation.isVoid()).thenReturn(false);
        
        AnswersValidator validator = new AnswersValidator();
        validator.validate(answer, invocation);
        
        verify(reporter).onlyVoidMethodsCanBeSetToDoNothing();
    }
    
    @Test
    public void testValidateReturnValue_withVoidInvocation() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Answer<?> answer = Mockito.mock(Returns.class);
        when(invocation.isVoid()).thenReturn(true);
        
        AnswersValidator validator = new AnswersValidator();
        validator.validate(answer, invocation);
        
        InOrder inOrder = Mockito.inOrder(reporter);
        inOrder.verify(reporter).cannotStubVoidMethodWithAReturnValue();
        inOrder.verifyNoMoreInteractions();
    }
    
    @Test
    public void testValidateReturnValue_withNullReturnValue() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Answer<?> answer = Mockito.mock(Returns.class);
        when(answer.returnsNull()).thenReturn(true);
        when(invocation.returnsPrimitive()).thenReturn(false);
        when(invocation.isValidReturnType(any())).thenReturn(true);
        
        AnswersValidator validator = new AnswersValidator();
        validator.validate(answer, invocation);
        
        verify(reporter, never()).wrongTypeOfReturnValue(anyString(), anyString(), anyString());
    }
    
    @Test
    public void testValidateReturnValue_withNonNullReturnValue_validReturnType() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Answer<?> answer = Mockito.mock(Returns.class);
        when(answer.returnsNull()).thenReturn(false);
        when(invocation.isValidReturnType(any())).thenReturn(true);
        
        AnswersValidator validator = new AnswersValidator();
        validator.validate(answer, invocation);
        
        verify(reporter, never()).wrongTypeOfReturnValue(anyString(), anyString(), anyString());
    }
    
    @Test
    public void testValidateReturnValue_withNonNullReturnValue_invalidReturnType() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Answer<?> answer = Mockito.mock(Returns.class);
        when(answer.returnsNull()).thenReturn(false);
        when(invocation.isValidReturnType(any())).thenReturn(false);
        when(invocation.printMethodReturnType()).thenReturn("String");
        when(answer.printReturnType()).thenReturn("Integer");
        
        AnswersValidator validator = new AnswersValidator();
        validator.validate(answer, invocation);
        
        verify(reporter).wrongTypeOfReturnValue("String", "Integer", anyString());
    }
    
    @Test
    public void testValidateException_withNullThrowable() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Answer<?> answer = Mockito.mock(ThrowsException.class);
        when(answer.getThrowable()).thenReturn(null);
        
        AnswersValidator validator = new AnswersValidator();
        validator.validate(answer, invocation);
        
        verify(reporter).cannotStubWithNullThrowable();
    }
    
    @Test
    public void testValidateException_withRuntimeException() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Answer<?> answer = Mockito.mock(ThrowsException.class);
        when(answer.getThrowable()).thenReturn(new RuntimeException());
        
        AnswersValidator validator = new AnswersValidator();
        validator.validate(answer, invocation);
        
        verifyZeroInteractions(reporter);
    }
    
    @Test
    public void testValidateException_withError() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Answer<?> answer = Mockito.mock(ThrowsException.class);
        when(answer.getThrowable()).thenReturn(new Error());
        
        AnswersValidator validator = new AnswersValidator();
        validator.validate(answer, invocation);
        
        verifyZeroInteractions(reporter);
    }
    
    @Test
    public void testValidateException_withCheckedException_invalidException() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Answer<?> answer = Mockito.mock(ThrowsException.class);
        Throwable throwable = Mockito.mock(Throwable.class);
        when(answer.getThrowable()).thenReturn(throwable);
        when(invocation.isValidException(any())).thenReturn(false);
        
        AnswersValidator validator = new AnswersValidator();
        validator.validate(answer, invocation);
        
        verify(reporter).checkedExceptionInvalid(throwable);
    }
    
    @Test
    public void testValidateException_withCheckedException_validException() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Answer<?> answer = Mockito.mock(ThrowsException.class);
        Throwable throwable = Mockito.mock(Throwable.class);
        when(answer.getThrowable()).thenReturn(throwable);
        when(invocation.isValidException(any())).thenReturn(true);
        
        AnswersValidator validator = new AnswersValidator();
        validator.validate(answer, invocation);
        
        verifyZeroInteractions(reporter);
    }
}