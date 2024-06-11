package org.mockito.internal.stubbing.answers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.stubbing.Answer;
import org.junit.Test;
import org.mockito.exceptions.Reporter;

public class AnswersValidatorTest {
    
    @Test
    public void testValidate() {
        // Initialize necessary objects
        Answer<?> answer = mock(Answer.class);
        Invocation invocation = mock(Invocation.class);
        Reporter reporter = mock(Reporter.class);
        
        // Stub necessary methods
        when(invocation.isVoid()).thenReturn(false);
        when(invocation.returnsPrimitive()).thenReturn(true);
        when(answer.returnsNull()).thenReturn(true);
        when(invocation.printMethodReturnType()).thenReturn("void");
        when(invocation.getMethodName()).thenReturn("testMethod");
        
        // Create instance of AnswersValidator
        AnswersValidator validator = new AnswersValidator();
        validator.reporter = reporter;
        
        // Call the method to test
        validator.validate(answer, invocation);
        
        // Verify the expected behavior
        verify(reporter, times(1)).wrongTypeOfReturnValue("void", "null", "testMethod");
    }
    
    @Test
    public void testValidateDoNothing() {
        // Initialize necessary objects
        DoesNothing answer = mock(DoesNothing.class);
        Invocation invocation = mock(Invocation.class);
        Reporter reporter = mock(Reporter.class);
        
        // Stub necessary methods
        when(invocation.isVoid()).thenReturn(false);
        when(reporter.onlyVoidMethodsCanBeSetToDoNothing()).thenReturn(null);
        
        // Create instance of AnswersValidator
        AnswersValidator validator = new AnswersValidator();
        validator.reporter = reporter;
        
        // Call the method to test
        validator.validateDoNothing(answer, invocation);
        
        // Verify the expected behavior
        verify(reporter, times(1)).onlyVoidMethodsCanBeSetToDoNothing();
    }
    
    @Test
    public void testValidateReturnValue() {
        // Initialize necessary objects
        Returns answer = mock(Returns.class);
        Invocation invocation = mock(Invocation.class);
        Reporter reporter = mock(Reporter.class);
        
        // Stub necessary methods
        when(invocation.isVoid()).thenReturn(true);
        when(reporter.cannotStubVoidMethodWithAReturnValue()).thenReturn(null);
        when(answer.returnsNull()).thenReturn(true);
        when(invocation.returnsPrimitive()).thenReturn(true);
        when(invocation.printMethodReturnType()).thenReturn("void");
        when(answer.printReturnType()).thenReturn("null");
        when(invocation.getMethodName()).thenReturn("testMethod");
        
        // Create instance of AnswersValidator
        AnswersValidator validator = new AnswersValidator();
        validator.reporter = reporter;
        
        // Call the method to test
        validator.validateReturnValue(answer, invocation);
        
        // Verify the expected behavior
        verify(reporter, times(1)).cannotStubVoidMethodWithAReturnValue();
    }
    
    @Test
    public void testValidateException() {
        // Initialize necessary objects
        ThrowsException answer = mock(ThrowsException.class);
        Invocation invocation = mock(Invocation.class);
        Reporter reporter = mock(Reporter.class);
        
        // Stub necessary methods
        when(answer.getThrowable()).thenReturn(null);
        when(reporter.cannotStubWithNullThrowable()).thenReturn(null);
        when(invocation.isValidException(null)).thenReturn(false);
        when(reporter.checkedExceptionInvalid(null)).thenReturn(null);
        
        // Create instance of AnswersValidator
        AnswersValidator validator = new AnswersValidator();
        validator.reporter = reporter;
        
        // Call the method to test
        validator.validateException(answer, invocation);
        
        // Verify the expected behavior
        verify(reporter, times(1)).cannotStubWithNullThrowable();
        verify(reporter, times(1)).checkedExceptionInvalid(null);
    }
}