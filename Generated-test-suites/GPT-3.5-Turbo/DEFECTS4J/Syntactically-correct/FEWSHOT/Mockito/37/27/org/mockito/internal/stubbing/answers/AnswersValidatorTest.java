package org.mockito.internal.stubbing.answers;

import static org.mockito.Mockito.*;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;

public class AnswersValidatorTest {
    
    @Test
    public void testValidateDoNothing_onlyVoidMethodsCanBeSetToDoNothing() {
        // Arrange
        AnswersValidator answersValidator = new AnswersValidator();
        DoesNothing answer = mock(DoesNothing.class);
        Invocation invocation = mock(Invocation.class);
        Reporter reporter = mock(Reporter.class);
        doReturn(false).when(invocation).isVoid();
        doReturn(reporter).when(answersValidator).getReporter();
        
        // Act
        answersValidator.validateDoNothing(answer, invocation);
        
        // Assert
        verify(reporter).onlyVoidMethodsCanBeSetToDoNothing();
    }
    
    @Test
    public void testValidateDoNothing_voidMethodsCanBeSetToDoNothing() {
        // Arrange
        AnswersValidator answersValidator = new AnswersValidator();
        DoesNothing answer = mock(DoesNothing.class);
        Invocation invocation = mock(Invocation.class);
        doReturn(true).when(invocation).isVoid();
        doNothing().when(answersValidator).getReporter();
        
        // Act
        answersValidator.validateDoNothing(answer, invocation);
        
        // Assert
        verifyZeroInteractions(mock(Reporter.class));
    }
    
    @Test
    public void testValidateReturnValue_cannotStubVoidMethodWithAReturnValue() {
        // Arrange
        AnswersValidator answersValidator = new AnswersValidator();
        Returns answer = mock(Returns.class);
        Invocation invocation = mock(Invocation.class);
        Reporter reporter = mock(Reporter.class);
        doReturn(true).when(invocation).isVoid();
        doReturn(reporter).when(answersValidator).getReporter();
        
        // Act
        answersValidator.validateReturnValue(answer, invocation);
        
        // Assert
        verify(reporter).cannotStubVoidMethodWithAReturnValue();
    }
    
    @Test
    public void testValidateReturnValue_validReturnType() {
        // Arrange
        AnswersValidator answersValidator = new AnswersValidator();
        Returns answer = mock(Returns.class);
        Invocation invocation = mock(Invocation.class);
        Reporter reporter = mock(Reporter.class);
        doReturn(false).when(invocation).isVoid();
        doReturn(false).when(answer).returnsNull();
        doReturn(true).when(invocation).isValidReturnType(answer.getReturnType());
        doReturn(reporter).when(answersValidator).getReporter();
        
        // Act
        answersValidator.validateReturnValue(answer, invocation);
        
        // Assert
        verifyZeroInteractions(mock(Reporter.class));
    }
    
    @Test
    public void testValidateReturnValue_wrongTypeOfReturnValue() {
        // Arrange
        AnswersValidator answersValidator = new AnswersValidator();
        Returns answer = mock(Returns.class);
        Invocation invocation = mock(Invocation.class);
        Reporter reporter = mock(Reporter.class);
        doReturn(false).when(invocation).isVoid();
        doReturn(false).when(answer).returnsNull();
        doReturn(false).when(invocation).isValidReturnType(answer.getReturnType());
        doReturn(reporter).when(answersValidator).getReporter();
        
        // Act
        answersValidator.validateReturnValue(answer, invocation);
        
        // Assert
        verify(reporter).wrongTypeOfReturnValue(invocation.printMethodReturnType(), answer.printReturnType(), invocation.getMethodName());
    }
    
    @Test
    public void testValidateReturnValue_nullReturnValueWithPrimitiveReturnType() {
        // Arrange
        AnswersValidator answersValidator = new AnswersValidator();
        Returns answer = mock(Returns.class);
        Invocation invocation = mock(Invocation.class);
        Reporter reporter = mock(Reporter.class);
        doReturn(false).when(invocation).isVoid();
        doReturn(true).when(answer).returnsNull();
        doReturn(true).when(invocation).returnsPrimitive();
        doReturn(reporter).when(answersValidator).getReporter();
        
        // Act
        answersValidator.validateReturnValue(answer, invocation);
        
        // Assert
        verify(reporter).wrongTypeOfReturnValue(invocation.printMethodReturnType(), "null", invocation.getMethodName());
    }
    
    @Test
    public void testValidateReturnValue_nonNullReturnValueWithPrimitiveReturnType() {
        // Arrange
        AnswersValidator answersValidator = new AnswersValidator();
        Returns answer = mock(Returns.class);
        Invocation invocation = mock(Invocation.class);
        Reporter reporter = mock(Reporter.class);
        doReturn(false).when(invocation).isVoid();
        doReturn(false).when(answer).returnsNull();
        doReturn(true).when(invocation).returnsPrimitive();
        doReturn(reporter).when(answersValidator).getReporter();
        
        // Act
        answersValidator.validateReturnValue(answer, invocation);
        
        // Assert
        verifyZeroInteractions(mock(Reporter.class));
    }
    
    @Test
    public void testValidateException_cannotStubWithNullThrowable() {
        // Arrange
        AnswersValidator answersValidator = new AnswersValidator();
        ThrowsException answer = mock(ThrowsException.class);
        Invocation invocation = mock(Invocation.class);
        Reporter reporter = mock(Reporter.class);
        doReturn(null).when(answer).getThrowable();
        doReturn(reporter).when(answersValidator).getReporter();
        
        // Act
        answersValidator.validateException(answer, invocation);
        
        // Assert
        verify(reporter).cannotStubWithNullThrowable();
    }
    
    @Test
    public void testValidateException_checkedExceptionInvalid() {
        // Arrange
        AnswersValidator answersValidator = new AnswersValidator();
        ThrowsException answer = mock(ThrowsException.class);
        Invocation invocation = mock(Invocation.class);
        Throwable throwable = mock(Throwable.class);
        Reporter reporter = mock(Reporter.class);
        doReturn(throwable).when(answer).getThrowable();
        doReturn(false).when(throwable).isInstance(invocation.getMock());
        doReturn(reporter).when(answersValidator).getReporter();
        
        // Act
        answersValidator.validateException(answer, invocation);
        
        // Assert
        verify(reporter).checkedExceptionInvalid(throwable);
    }
    
    @Test
    public void testValidateException_noCheckedExceptionInvalid() {
        // Arrange
        AnswersValidator answersValidator = new AnswersValidator();
        ThrowsException answer = mock(ThrowsException.class);
        Invocation invocation = mock(Invocation.class);
        Throwable throwable = mock(Throwable.class);
        Reporter reporter = mock(Reporter.class);
        doReturn(throwable).when(answer).getThrowable();
        doReturn(true).when(throwable).isInstance(invocation.getMock());
        doReturn(reporter).when(answersValidator).getReporter();
        
        // Act
        answersValidator.validateException(answer, invocation);
        
        // Assert
        verifyZeroInteractions(mock(Reporter.class));
    }
}