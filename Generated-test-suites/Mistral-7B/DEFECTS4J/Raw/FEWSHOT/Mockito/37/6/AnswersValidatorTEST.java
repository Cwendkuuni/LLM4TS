package org.mockito.internal.stubbing.answers;

import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;

import static org.mockito.internal.stubbing.answers.AnswersValidator.REPORTER;
import static org.mockito.internal.stubbing.answers.AnswersValidator.WRONG_TYPE_OF_RETURN_VALUE;
import static org.mockito.internal.stubbing.answers.AnswersValidator.CANNOT_STUB_WITH_NULL_THROWABLE;
import static org.mockito.internal.stubbing.answers.AnswersValidator.CANNOT_STUB_VOID_METHOD_WITH_AReturnValue;
import static org.mockito.internal.stubbing.answers.AnswersValidator.ONLY_VOID_METHODS_CAN_BE_SET_TO_DO_NOTHING;
import static org.mockito.internal.stubbing.answers.AnswersValidator.CHECKED_EXCEPTION_INVALID;

public class AnswersValidatorTest {

    @Test
    public void testValidate_WhenAnswerIsThrowsException_ThenValidateException() {
        Reporter reporter = new Reporter();
        AnswersValidator validator = new AnswersValidator(reporter);
        Invocation invocation = new Invocation("testMethod", new Object[0]);
        ThrowsException answer = new ThrowsException(new RuntimeException("Exception message"));

        validator.validate(answer, invocation);

        // No error expected, as RuntimeException is a subclass of Throwable
    }

    @Test
    public void testValidate_WhenAnswerIsDoesNothing_ThenValidateDoNothing() {
        Reporter reporter = new Reporter();
        AnswersValidator validator = new AnswersValidator(reporter);
        Invocation invocation = new Invocation("testMethod", new Object[0], "void");
        DoesNothing answer = new DoesNothing();

        validator.validate(answer, invocation);

        if (invocation.isVoid()) {
            // No error expected
        } else {
            reporter.onlyVoidMethodsCanBeSetToDoNothing();
            assertErrorMessage(ONLY_VOID_METHODS_CAN_BE_SET_TO_DO_NOTHING, validator.reporter);
        }
    }

    @Test
    public void testValidate_WhenAnswerIsReturnsNull_ThenValidateReturnValue() {
        Reporter reporter = new Reporter();
        AnswersValidator validator = new AnswersValidator(reporter);
        Invocation invocation = new Invocation("testMethod", null, "String");
        Returns answer = new Returns(null);

        validator.validate(answer, invocation);

        if (invocation.isVoid()) {
            assertErrorMessage(CANNOT_STUB_VOID_METHOD_WITH_AReturnValue, validator.reporter);
        } else if (answer.getReturnType().isPrimitive()) {
            assertErrorMessage(WRONG_TYPE_OF_RETURN_VALUE, validator.reporter);
        }
    }

    @Test
    public void testValidate_WhenAnswerIsReturnsValue_ThenValidateReturnValue() {
        Reporter reporter = new Reporter();
        AnswersValidator validator = new AnswersValidator(reporter);
        Invocation invocation = new Invocation("testMethod", null, "String");
        Returns answer = new Returns("answer");

        validator.validate(answer, invocation);

        if (invocation.isVoid()) {
            assertErrorMessage(CANNOT_STUB_VOID_METHOD_WITH_AReturnValue, validator.reporter);
        } else if (!invocation.isValidReturnType(answer.getReturnType())) {
            String expectedMessage = String.format(WRONG_TYPE_OF_RETURN_VALUE, invocation.printMethodReturnType(), answer.printReturnType());
            assertErrorMessage(expectedMessage, validator.reporter);
        }
    }

    @Test
    public void testValidate_WhenAnswerIsThrowsCheckException_ThenValidateException() {
        Reporter reporter = new Reporter();
        AnswersValidator validator = new AnswersValidator(reporter);
        Invocation invocation = new Invocation("testMethod", new Object[0]);
        ThrowsException answer = new ThrowsException(new CheckedException("Exception message"));

        validator.validate(answer, invocation);

        assertErrorMessage(CHECKED_EXCEPTION_INVALID, validator.reporter);
    }

    @Test
    public void testValidate_WhenAnswerIsNull_ThenNoError() {
        Reporter reporter = new Reporter();
        AnswersValidator validator = new AnswersValidator(reporter);
        Invocation invocation = new Invocation("testMethod", new Object[0]);

        validator.validate(null, invocation);

        // No error expected
    }

    @Test
    public void testValidate_WhenAnswerIsNotNullButNotAnInstanceOfAnswer_ThenNoError() {
        Reporter reporter = new Reporter();
        AnswersValidator validator = new AnswersValidator(reporter);
        Invocation invocation = new Invocation("testMethod", new Object[0]);

        validator.validate(new Object(), invocation);

        // No error expected
    }

    private void assertErrorMessage(String expectedMessage, Reporter reporter) {
        try {
            validator.validate(answer, invocation);
            fail("An error was expected: " + expectedMessage);
        } catch (AssertionError e) {
            assertEquals(expectedMessage, e.getMessage());
        }
    }

    private static class CheckedException extends Exception {
        CheckedException(String message) {
            super(message);
        }
    }
}