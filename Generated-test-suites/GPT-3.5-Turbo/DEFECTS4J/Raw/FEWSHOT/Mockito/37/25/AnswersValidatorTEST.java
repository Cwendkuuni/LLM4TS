package org.mockito.internal.stubbing.answers;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;

public class AnswersValidatorTest {
    private AnswersValidator answersValidator = new AnswersValidator();
    private Reporter reporter = new Reporter();

    @Test
    public void testValidate_ThrowsException() {
        ThrowsException answer = new ThrowsException(null);
        Invocation invocation = new Invocation(null, null);
        
        // Test case 1
        answersValidator.validate(answer, invocation);
        Assert.assertTrue(true); // No exception should be thrown
        
        // Test case 2
        answer = new ThrowsException(new RuntimeException());
        answersValidator.validate(answer, invocation);
        Assert.assertTrue(true); // No exception should be thrown
        
        // Test case 3
        answer = new ThrowsException(new Error());
        answersValidator.validate(answer, invocation);
        Assert.assertTrue(true); // No exception should be thrown
    }

    @Test
    public void testValidate_Returns() {
        Returns answer = new Returns(null);
        Invocation invocation = new Invocation(null, null);
        
        // Test case 1
        answer.returnsNull();
        invocation.setReturnType(void.class);
        answer.setReturnType(String.class);
        answersValidator.validate(answer, invocation);
        Assert.assertTrue(true); // No exception should be thrown
        
        // Test case 2
        answer.returnsNull();
        invocation.setReturnType(int.class);
        answer.setReturnType(String.class);
        try {
            answersValidator.validate(answer, invocation);
            Assert.fail("Reporter.wrongTypeOfReturnValue should be thrown");
        } catch (Exception e) {
            Assert.assertEquals("WrongTypeOfReturnValue", e.getMessage());
        }
        
        // Test case 3
        answer.returnsNull();
        invocation.setReturnType(int.class);
        answer.setReturnType(null);
        try {
            answersValidator.validate(answer, invocation);
            Assert.fail("Reporter.wrongTypeOfReturnValue should be thrown");
        } catch (Exception e) {
            Assert.assertEquals("WrongTypeOfReturnValue", e.getMessage());
        }
    }

    @Test
    public void testValidate_DoesNoting() {
        DoesNothing answer = new DoesNothing();
        Invocation invocation = new Invocation(null, null);
        
        // Test case 1
        invocation.setVoid(true);
        answersValidator.validate(answer, invocation);
        Assert.assertTrue(true); // No exception should be thrown
        
        // Test case 2
        invocation.setVoid(false);
        try {
            answersValidator.validate(answer, invocation);
            Assert.fail("Reporter.onlyVoidMethodsCanBeSetToDoNothing should be thrown");
        } catch (Exception e) {
            Assert.assertEquals("OnlyVoidMethodsCanBeSetToDoNothing", e.getMessage());
        }
    }
}