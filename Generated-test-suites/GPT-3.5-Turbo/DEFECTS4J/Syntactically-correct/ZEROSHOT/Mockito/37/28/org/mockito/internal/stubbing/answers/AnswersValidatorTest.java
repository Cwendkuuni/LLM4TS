
package org.mockito.internal.stubbing.answers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class AnswersValidatorTest {

    private AnswersValidator answersValidator;
    private Reporter reporterMock;
    
    @Before
    public void setUp() {
        answersValidator = new AnswersValidator();
        reporterMock = mock(Reporter.class);
        answersValidator.reporter = reporterMock;
    }
    
    @Test
    public void testValidate_ThrowsException() {
        Answer<?> answerMock = mock(ThrowsException.class);
        Invocation invocationMock = mock(Invocation.class);
        
        answersValidator.validate(answerMock, invocationMock);
        
        verifyNoInteractions(reporterMock);
    }
    
    @Test
    public void testValidate_Returns() {
        Answer<?> answerMock = mock(Returns.class);
        Invocation invocationMock = mock(Invocation.class);
        
        when(invocationMock.isVoid()).thenReturn(false);
        when(invocationMock.returnsPrimitive()).thenReturn(false);
        when(answerMock.returnsNull()).thenReturn(false);
        when(invocationMock.isValidReturnType(eq(answerMock.getReturnType()))).thenReturn(true);
        
        answersValidator.validate(answerMock, invocationMock);
        
        verifyNoInteractions(reporterMock);
    }
    
    @Test
    public void testValidate_Returns_VoidMethod() {
        Answer<?> answerMock = mock(Returns.class);
        Invocation invocationMock = mock(Invocation.class);
        
        when(invocationMock.isVoid()).thenReturn(true);
        
        answersValidator.validate(answerMock, invocationMock);
        
        verify(reporterMock).cannotStubVoidMethodWithAReturnValue();
    }
    
    @Test
    public void testValidate_Returns_ReturnsNullWithPrimitiveInvocation() {
        Answer<?> answerMock = mock(Returns.class);
        Invocation invocationMock = mock(Invocation.class);
        
        when(invocationMock.isVoid()).thenReturn(false);
        when(invocationMock.returnsPrimitive()).thenReturn(true);
        when(answerMock.returnsNull()).thenReturn(true);
        when(invocationMock.printMethodReturnType()).thenReturn("int");
        when(invocationMock.getMethodName()).thenReturn("testMethod");
        
        answersValidator.validate(answerMock, invocationMock);
        
        verify(reporterMock).wrongTypeOfReturnValue("int", "null", "testMethod");
    }
    
    @Test
    public void testValidate_Returns_WrongTypeOfReturnValue() {
        Answer<?> answerMock = mock(Returns.class);
        Invocation invocationMock = mock(Invocation.class);
        
        when(invocationMock.isVoid()).thenReturn(false);
        when(invocationMock.returnsPrimitive()).thenReturn(false);
        when(answerMock.returnsNull()).thenReturn(false);
        when(invocationMock.printMethodReturnType()).thenReturn("String");
        when(answerMock.printReturnType()).thenReturn("Integer");
        when(invocationMock.getMethodName()).thenReturn("testMethod");
        
        answersValidator.validate(answerMock, invocationMock);
        
        verify(reporterMock).wrongTypeOfReturnValue("String", "Integer", "testMethod");
    }
    
    @Test
    public void testValidate_DoesNothing() {
        Answer<?> answerMock = mock(DoesNothing.class);
        Invocation invocationMock = mock(Invocation.class);
        
        when(invocationMock.isVoid()).thenReturn(true);
        
        answersValidator.validate(answerMock, invocationMock);
        
        verifyNoInteractions(reporterMock);
    }
    
    @Test
    public void testValidate_DoesNothing_NonVoidMethod() {
        Answer<?> answerMock = mock(DoesNothing.class);
        Invocation invocationMock = mock(Invocation.class);
        
        when(invocationMock.isVoid()).thenReturn(false);
        
        answersValidator.validate(answerMock, invocationMock);
        
        verify(reporterMock).onlyVoidMethodsCanBeSetToDoNothing();
    }
    
    @Test
    public void testValidateException_NullThrowable() {
        Answer<?> answerMock = mock(ThrowsException.class);
        Invocation invocationMock = mock(Invocation.class);
        
        when(answerMock.getThrowable()).thenReturn(null);
        
        answersValidator.validate(answerMock, invocationMock);
        
        verify(reporterMock).cannotStubWithNullThrowable();
    }
    
    @Test
    public void testValidateException_RuntimeException() {
        Answer<?> answerMock = mock(ThrowsException.class);
        Invocation invocationMock = mock(Invocation.class);
        
        Throwable throwableMock = mock(RuntimeException.class);
        
        when(answerMock.getThrowable()).thenReturn(throwableMock);
        
        answersValidator.validate(answerMock, invocationMock);
        
        verifyNoInteractions(reporterMock);
    }
    
    @Test
    public void testValidateException_Error() {
        Answer<?> answerMock = mock(ThrowsException.class);
        Invocation invocationMock = mock(Invocation.class);
        
        Throwable throwableMock = mock(Error.class);
        
        when(answerMock.getThrowable()).thenReturn(throwableMock);
        
        answersValidator.validate(answerMock, invocationMock);
        
        verifyNoInteractions(reporterMock);
    }
    
    @Test
    public void testValidateException_CheckedExceptionInvalid() {
        Answer<?> answerMock = mock(ThrowsException.class);
        Invocation invocationMock = mock(Invocation.class);
        
        Throwable throwableMock = mock(Exception.class);
        
        when(answerMock.getThrowable()).thenReturn(throwableMock);
        when(invocationMock.isValidException(eq(throwableMock))).thenReturn(false);
        
        answersValidator.validate(answerMock, invocationMock);
        
        verify(reporterMock).checkedExceptionInvalid(eq(throwableMock));
    }

}
