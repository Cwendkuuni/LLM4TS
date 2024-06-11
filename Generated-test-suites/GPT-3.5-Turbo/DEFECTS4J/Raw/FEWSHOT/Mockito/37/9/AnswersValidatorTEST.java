
package org.mockito.internal.stubbing.answers;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class AnswersValidatorTest {
    
    private AnswersValidator answersValidator;
    private Invocation invocation;
    
    @Before
    public void setUp() {
        answersValidator = new AnswersValidator();
        invocation = Mockito.mock(Invocation.class);
    }
    
    @Test
    public void testValidateDoNothing_withVoidMethod_shouldNotThrowException() {
        DoesNothing answer = Mockito.mock(DoesNothing.class);
        Mockito.when(invocation.isVoid()).thenReturn(true);
        
        try {
            answersValidator.validateDoNothing(answer, invocation);
        } catch (Exception e) {
            fail("Should not throw exception");
        }
    }
    
    @Test
    public void testValidateDoNothing_withNonVoidMethod_shouldThrowException() {
        DoesNothing answer = Mockito.mock(DoesNothing.class);
        Mockito.when(invocation.isVoid()).thenReturn(false);
        
        try {
            answersValidator.validateDoNothing(answer, invocation);
            fail("Should throw exception");
        } catch (Exception e) {
            // Success
        }
    }
    
    @Test
    public void testValidateReturnValue_withVoidMethod_shouldThrowException() {
        Returns answer = Mockito.mock(Returns.class);
        Mockito.when(invocation.isVoid()).thenReturn(true);
        
        try {
            answersValidator.validateReturnValue(answer, invocation);
            fail("Should throw exception");
        } catch (Exception e) {
            // Success
        }
    }
    
    @Test
    public void testValidateReturnValue_withNullReturnValueAndPrimitiveMethod_shouldThrowException() {
        Returns answer = Mockito.mock(Returns.class);
        Mockito.when(invocation.isVoid()).thenReturn(false);
        Mockito.when(invocation.returnsPrimitive()).thenReturn(true);
        Mockito.when(answer.returnsNull()).thenReturn(true);
        Mockito.when(invocation.printMethodReturnType()).thenReturn("int");
        Mockito.when(invocation.getMethodName()).thenReturn("testMethod");
        
        try {
            answersValidator.validateReturnValue(answer, invocation);
            fail("Should throw exception");
        } catch (Exception e) {
            // Success
        }
    }
    
    @Test
    public void testValidateReturnValue_withNonVoidMethodAndInvalidReturnType_shouldThrowException() {
        Returns answer = Mockito.mock(Returns.class);
        Mockito.when(invocation.isVoid()).thenReturn(false);
        Mockito.when(invocation.returnsPrimitive()).thenReturn(false);
        Mockito.when(answer.returnsNull()).thenReturn(false);
        Mockito.when(invocation.isValidReturnType(Mockito.any())).thenReturn(false);
        Mockito.when(invocation.printMethodReturnType()).thenReturn("String");
        Mockito.when(answer.printReturnType()).thenReturn("Integer");
        Mockito.when(invocation.getMethodName()).thenReturn("testMethod");
        
        try {
            answersValidator.validateReturnValue(answer, invocation);
            fail("Should throw exception");
        } catch (Exception e) {
            // Success
        }
    }
    
    @Test
    public void testValidateReturnValue_withNonVoidMethodAndValidReturnType_shouldNotThrowException() {
        Returns answer = Mockito.mock(Returns.class);
        Mockito.when(invocation.isVoid()).thenReturn(false);
        Mockito.when(invocation.returnsPrimitive()).thenReturn(false);
        Mockito.when(answer.returnsNull()).thenReturn(false);
        Mockito.when(invocation.isValidReturnType(Mockito.any())).thenReturn(true);
        
        try {
            answersValidator.validateReturnValue(answer, invocation);
        } catch (Exception e) {
            fail("Should not throw exception");
        }
    }
    
    @Test
    public void testValidateException_withNullThrowable_shouldThrowException() {
        ThrowsException answer = Mockito.mock(ThrowsException.class);
        Mockito.when(answer.getThrowable()).thenReturn(null);
        
        try {
            answersValidator.validateException(answer, invocation);
            fail("Should throw exception");
        } catch (Exception e) {
            // Success
        }
    }
    
    @Test
    public void testValidateException_withCheckedExceptionInvalid_shouldThrowException() {
        ThrowsException answer = Mockito.mock(ThrowsException.class);
        Throwable throwable = new Exception();
        Mockito.when(answer.getThrowable()).thenReturn(throwable);
        Mockito.when(invocation.isValidException(throwable)).thenReturn(false);
        
        try {
            answersValidator.validateException(answer, invocation);
            fail("Should throw exception");
        } catch (Exception e) {
            // Success
        }
    }
    
    @Test
    public void testValidateException_withRuntimeExceptionOrError_shouldNotThrowException() {
        ThrowsException answer = Mockito.mock(ThrowsException.class);
        Throwable throwable = new RuntimeException();
        Mockito.when(answer.getThrowable()).thenReturn(throwable);
        
        try {
            answersValidator.validateException(answer, invocation); // No exception should be thrown
        } catch (Exception e) {
            fail("Should not throw exception");
        }
        
        throwable = new Error();
        Mockito.when(answer.getThrowable()).thenReturn(throwable);
        
        try {
            answersValidator.validateException(answer, invocation); // No exception should be thrown
        } catch (Exception e) {
            fail("Should not throw exception");
        }
    }
    
}
