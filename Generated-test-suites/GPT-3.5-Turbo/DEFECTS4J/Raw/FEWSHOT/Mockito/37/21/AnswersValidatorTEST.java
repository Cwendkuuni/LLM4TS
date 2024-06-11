package org.mockito.internal.stubbing.answers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;

public class AnswersValidatorTest {

    @Test
    public void testValidate() {
        Reporter reporter = new Reporter();
        
        ThrowsException throwsException = new ThrowsException();
        Returns returns = new Returns();
        DoesNothing doesNothing = new DoesNothing();
        
        Invocation invocation = new Invocation();
        Throwable throwable = new Throwable();
        
        AnswersValidator answersValidator = new AnswersValidator();
        
        answersValidator.validate(throwsException, invocation);
        answersValidator.validate(returns, invocation);
        answersValidator.validate(doesNothing, invocation);
        
        // Verify that the correct error message is reported for ThrowsException validation
        
        assertNull(throwsException.getThrowable());
        reporter.cannotStubWithNullThrowable();
        
        // Verify that the correct error message is reported for Returns validation
        
        assertFalse(invocation.isVoid());
        reporter.cannotStubVoidMethodWithAReturnValue();
        
        assertTrue(returns.returnsNull());
        assertTrue(invocation.returnsPrimitive());
        reporter.wrongTypeOfReturnValue(invocation.printMethodReturnType(), "null", invocation.getMethodName());
        
        assertFalse(answer.returnsNull());
        assertFalse(invocation.isValidReturnType(answer.getReturnType()));
        reporter.wrongTypeOfReturnValue(invocation.printMethodReturnType(), answer.printReturnType(), invocation.getMethodName());
        
        // Verify that the correct error message is reported for DoesNothing validation
        
        assertFalse(invocation.isVoid());
        reporter.onlyVoidMethodsCanBeSetToDoNothing();
    }
}