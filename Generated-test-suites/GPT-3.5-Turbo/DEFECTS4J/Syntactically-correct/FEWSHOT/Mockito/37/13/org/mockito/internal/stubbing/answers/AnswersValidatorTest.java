package org.mockito.internal.stubbing.answers;

import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.internal.invocation.Invocation;

public class AnswersValidatorTest {

    @Test
    public void testValidate_DoNothingAndVoidMethod_ThrowsException() {
        AnswersValidator answersValidator = new AnswersValidator();
        DoesNothing answer = mock(DoesNothing.class);
        Invocation invocation = mock(Invocation.class);
        
        when(invocation.isVoid()).thenReturn(false);
        
        try {
            answersValidator.validateDoNothing(answer, invocation);
            fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertEquals("Reporter: onlyVoidMethodsCanBeSetToDoNothing", e.getMessage());
        }
    }
    
    @Test
    public void testValidate_ReturnValue_VoidMethod_ThrowsException() {
        AnswersValidator answersValidator = new AnswersValidator();
        Returns answer = mock(Returns.class);
        Invocation invocation = mock(Invocation.class);
        
        when(invocation.isVoid()).thenReturn(true);
        
        try {
            answersValidator.validateReturnValue(answer, invocation);
            fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertEquals("Reporter: cannotStubVoidMethodWithAReturnValue", e.getMessage());
        }
    }
    
    @Test
    public void testValidate_ReturnValue_NullReturnValueAndPrimitiveReturnType_ThrowsException() {
        AnswersValidator answersValidator = new AnswersValidator();
        Returns answer = mock(Returns.class);
        Invocation invocation = mock(Invocation.class);
        
        when(invocation.isVoid()).thenReturn(false);
        when(invocation.returnsPrimitive()).thenReturn(true);
        when(answer.returnsNull()).thenReturn(true);
        when(invocation.printMethodReturnType()).thenReturn("int");
        when(invocation.getMethodName()).thenReturn("method");
        
        try {
            answersValidator.validateReturnValue(answer, invocation);
            fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertEquals("Reporter: wrongTypeOfReturnValue(int, null, method)", e.getMessage());
        }
    }
    
    @Test
    public void testValidate_ReturnValue_NonNullReturnValueAndInvalidReturnType_ThrowsException() {
        AnswersValidator answersValidator = new AnswersValidator();
        Returns answer = mock(Returns.class);
        Invocation invocation = mock(Invocation.class);
        
        when(invocation.isVoid()).thenReturn(false);
        when(invocation.returnsPrimitive()).thenReturn(false);
        when(answer.returnsNull()).thenReturn(false);
        when(invocation.printMethodReturnType()).thenReturn("String");
        when(answer.printReturnType()).thenReturn("int");
        when(invocation.getMethodName()).thenReturn("method");
        
        try {
            answersValidator.validateReturnValue(answer, invocation);
            fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertEquals("Reporter: wrongTypeOfReturnValue(String, int, method)", e.getMessage());
        }
    }
    
    @Test
    public void testValidate_Exception_NullThrowable_ThrowsException() {
        AnswersValidator answersValidator = new AnswersValidator();
        ThrowsException answer = mock(ThrowsException.class);
        Invocation invocation = mock(Invocation.class);
        
        when(answer.getThrowable()).thenReturn(null);
        
        try {
            answersValidator.validateException(answer, invocation);
            fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertEquals("Reporter: cannotStubWithNullThrowable", e.getMessage());
        }
    }
    
    @Test
    public void testValidate_Exception_CheckedExceptionInvalid_ThrowsException() {
        AnswersValidator answersValidator = new AnswersValidator();
        ThrowsException answer = mock(ThrowsException.class);
        Invocation invocation = mock(Invocation.class);
        Throwable throwable = mock(Throwable.class);
        
        when(answer.getThrowable()).thenReturn(throwable);
        when(invocation.isValidException(throwable)).thenReturn(false);
        
        try {
            answersValidator.validateException(answer, invocation);
            fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertEquals("Reporter: checkedExceptionInvalid", e.getMessage());
        }
    }
}