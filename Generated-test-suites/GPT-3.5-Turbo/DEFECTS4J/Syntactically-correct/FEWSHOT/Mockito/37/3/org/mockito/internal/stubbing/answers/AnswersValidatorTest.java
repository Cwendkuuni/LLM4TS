
package org.mockito.internal.stubbing.answers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;

public class AnswersValidatorTest {
    
    private static final Invocation VOID_INVOCATION = new Invocation(void.class, "voidMethod", null, null);
    private static final Invocation PRIMITIVE_INVOCATION = new Invocation(int.class, "primitiveMethod", null, null);
    private static final Invocation RETURN_INVOCATION = new Invocation(String.class, "returnMethod", null, null);
    private static final Invocation EXCEPTION_INVOCATION = new Invocation(void.class, "exceptionMethod", null, null);
    
    private AnswersValidator answersValidator = new AnswersValidator();
    private Reporter reporter = new Reporter();
    
    @Test
    public void testValidateDoNothing_onlyVoidMethods() {
        answersValidator.validate(new DoesNothing(), VOID_INVOCATION);
        assertNull(reporter.getErrorMessage());
    }
    
    @Test
    public void testValidateDoNothing_nonVoidMethod() {
        answersValidator.validate(new DoesNothing(), PRIMITIVE_INVOCATION);
        assertEquals("Only void methods can be set to doNothing()", reporter.getErrorMessage());
    }
    
    @Test
    public void testValidateReturnValue_voidMethod() {
        answersValidator.validate(new Returns("returnValue"), VOID_INVOCATION);
        assertEquals("Cannot stub void method with a return value", reporter.getErrorMessage());
    }
    
    @Test
    public void testValidateReturnValue_primitiveMethod_nullReturnValue() {
        answersValidator.validate(new Returns(null), PRIMITIVE_INVOCATION);
        assertEquals("Wrong type of return value: int expected, null returned from primitiveMethod", reporter.getErrorMessage());
    }
    
    @Test
    public void testValidateReturnValue_invalidReturnType() {
        answersValidator.validate(new Returns(10), RETURN_INVOCATION);
        assertEquals("Wrong type of return value: String expected, Integer returned from returnMethod", reporter.getErrorMessage());
    }
    
    @Test
    public void testValidateReturnValue_validReturnType() {
        answersValidator.validate(new Returns("returnValue"), RETURN_INVOCATION);
        assertNull(reporter.getErrorMessage());
    }
    
    @Test
    public void testValidateException_nullThrowable() {
        answersValidator.validate(new ThrowsException(null), EXCEPTION_INVOCATION);
        assertEquals("Cannot stub with null throwable", reporter.getErrorMessage());
    }
    
    @Test
    public void testValidateException_runtimeException() {
        answersValidator.validate(new ThrowsException(new RuntimeException()), EXCEPTION_INVOCATION);
        assertNull(reporter.getErrorMessage());
    }
    
    @Test
    public void testValidateException_error() {
        answersValidator.validate(new ThrowsException(new Error()), EXCEPTION_INVOCATION);
        assertNull(reporter.getErrorMessage());
    }
    
    @Test
    public void testValidateException_invalidException() {
        answersValidator.validate(new ThrowsException(new Exception()), EXCEPTION_INVOCATION);
        assertEquals("Checked exception is invalid: java.lang.Exception", reporter.getErrorMessage());
    }
}
