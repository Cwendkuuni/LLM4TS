package org.mockito.internal.stubbing.answers;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;

public class AnswersValidatorTest {
    private Reporter reporter = new Reporter();

    @Test
    public void testValidate_ThrowsException_Valid() {
        Answer<String> answer = new ThrowsException(new RuntimeException("Expected Exception"));
        Invocation invocation = new Invocation(null, null, null, null, null, null, null);

        new AnswersValidator().validate(answer, invocation);

        // No exception should be thrown
    }

    @Test(expected = IllegalStateException.class)
    public void testValidate_ThrowsException_Invalid() {
        Answer<String> answer = new ThrowsException(null);
        Invocation invocation = new Invocation(null, null, null, null, null, null, null);

        new AnswersValidator().validate(answer, invocation);

        // IllegalStateException should be thrown
    }

    @Test
    public void testValidate_Returns_Valid() {
        Answer<String> answer = new Returns<>("Expected Value");
        Invocation invocation = new Invocation(null, null, null, null, null, null, String.class);

        new AnswersValidator().validate(answer, invocation);

        // No exception should be thrown
    }

    @Test(expected = IllegalStateException.class)
    public void testValidate_Returns_VoidMethod() {
        Answer<String> answer = new Returns<>("Expected Value");
        Invocation invocation = new Invocation(null, null, null, null, null, null, Void.TYPE);

        new AnswersValidator().validate(answer, invocation);

        // IllegalStateException should be thrown
    }

    @Test
    public void testValidate_Returns_NullAndPrimitiveType() {
        Answer<Integer> answer = new Returns<>(null);
        Invocation invocation = new Invocation(null, null, null, null, null, null, Integer.TYPE);

        new AnswersValidator().validate(answer, invocation);

        // No exception should be thrown
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidate_Returns_NullAndReferenceType() {
        Answer<String> answer = new Returns<>(null);
        Invocation invocation = new Invocation(null, null, null, null, null, null, String.class);

        new AnswersValidator().validate(answer, invocation);

        // IllegalArgumentException should be thrown
    }

    @Test(expected = IllegalStateException.class)
    public void testValidate_Returns_WrongType() {
        Answer<String> answer = new Returns("Expected Value");
        Invocation invocation = new Invocation(null, null, null, null, null, null, Integer.TYPE);

        new AnswersValidator().validate(answer, invocation);

        // IllegalStateException should be thrown
    }

    @Test
    public void testValidate_DoesNothing_Valid() {
        DoesNothing answer = new DoesNothing();
        Invocation invocation = new Invocation(null, null, null, null, null, null, Void.TYPE);

        new AnswersValidator().validate(answer, invocation);

        // No exception should be thrown
    }

    @Test(expected = IllegalStateException.class)
    public void testValidate_DoesNothing_NonVoidMethod() {
        DoesNothing answer = new DoesNothing();
        Invocation invocation = new Invocation(null, null, null, null, null, null, String.class);

        new AnswersValidator().validate(answer, invocation);

        // IllegalStateException should be thrown
    }
}