package org.junit.platform.commons.internal.test.annotation;

import org.junit.*;
import org.mockito.*;
import org.mockito.annotations.*;
import org.mockito.exceptions.base.*;
import org.mockito.invocation.*;
import org.mockito.stubbing.*;

import static org.junit.Assert.*;

public class AnswersValidatorTest {

    @Mock
    private Reporter reporter;

    private AnswersValidator validator;

    @Before
    public void setup() {
        validator = new AnswersValidator();
        validator.setReporter(reporter);
    }

    @Test
    public void testValidate() {
        Answer<String> returnsAnswer = mock(Returns.class);
        Invocation<String, String> invocation = mock(Invocation.class);

        validator.validate(returnsAnswer, invocation);

        verify(returnsAnswer).answer(any());
        verify(invocation).getMethod();
    }

    @Test
    public void testValidateDoNothingVoid() {
        DoesNothing doesNothing = mock(DoesNothing.class);
        Invocation invocation = mock(Invocation.class);
        invocation.markVoid();

        validator.validateDoNothing(doesNothing, invocation);
    }

    @Test(expected = AssertionError.class)
    public void testValidateDoNothingNonVoid() {
        DoesNothing doesNothing = mock(DoesNothing.class);
        Invocation invocation = mock(Invocation.class);

        validator.validateDoNothing(doesNothing, invocation);
    }

    @Test
    public void testValidateReturnValueVoidNull() {
        Returns returns = mock(Returns.class);
        Invocation invocation = mock(Invocation.class);
        invocation.markVoid();

        validator.validateReturnValue(returns, invocation);
    }

    @Test(expected = AssertionError.class)
    public void testValidateReturnValueNonVoidNull() {
        Returns returns = mock(Returns.class);
        Invocation invocation = mock(Invocation.class);

        validator.validateReturnValue(returns, invocation);
    }

    @Test(expected = AssertionError.class)
    public void testValidateReturnValueNonVoidIncorrectType() {
        Returns returns = mock(Returns.class);
        Returns expectedReturnType = mock(Returns.class);
        Invocation invocation = mock(Invocation.class);
        invocation.returns(1L);

        validator.validateReturnValue(returns, invocation);

        verify(validator).wrongTypeOfReturnValue(invocation.printMethodReturnType(), expectedReturnType.printReturnType(), invocation.getMethodName());
    }

    @Test
    public void testValidateReturnValueNonVoidCorrectType() {
        Returns returns = mock(Returns.class);
        Returns expectedReturnType = mock(Returns.class);
        Invocation invocation = mock(Invocation.class);
        invocation.returns("Hello World");

        validator.validateReturnValue(returns, invocation);
    }

    @Test
    public void testValidateExceptionNoException() {
        ThrowsException throwsException = mock(ThrowsException.class);
        Invocation invocation = mock(Invocation.class);

        validator.validateException(throwsException, invocation);
    }

    @Test
    public void testValidateExceptionRuntimeException() {
        ThrowsException throwsException = mock(ThrowsException.class);
        Invocation invocation = mock(Invocation.class);
        Throwable runtimeException = new RuntimeException("Test exception");

        throwsException.setThrowable(runtimeException);

        validator.validateException(throwsException, invocation);
    }

    @Test(expected = AssertionError.class)
    public void testValidateExceptionDifferentException() {
        ThrowsException throwsException = mock(ThrowsException.class);
        Invocation invocation = mock(Invocation.class);
        Throwable expectedException = new IllegalStateException("Test exception");

        throwsException.setThrowable(expectedException);

        validator.validateException(throwsException, invocation);
    }

    @Test(expected = AssertionError.class)
    public void testValidateExceptionDifferentExceptionType() {
        ThrowsException throwsException = mock(ThrowsException.class);
        Invocation invocation = mock(Invocation.class);
        Throwable expectedException = new IllegalArgumentException("Test exception");

        throwsException.setThrowable(expectedException);

        validator.validateException(throwsException, invocation);
    }
}