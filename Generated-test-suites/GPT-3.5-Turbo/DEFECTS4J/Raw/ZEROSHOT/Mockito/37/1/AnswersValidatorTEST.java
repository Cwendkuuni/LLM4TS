import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.mock;

public class AnswersValidatorTest {

    @Test
    public void testValidateDoNothing() {
        DoesNothing answer = mock(DoesNothing.class);
        Invocation invocation = mock(Invocation.class);
        
        AnswersValidator answersValidator = new AnswersValidator();
        Reporter reporter = new Reporter();
        answersValidator.reporter = reporter;
        
        answersValidator.validateDoNothing(answer, invocation);
        
        Assert.assertEquals(reporter.onlyVoidMethodsCanBeSetToDoNothing(), reporter.onlyVoidMethodsCanBeSetToDoNothing());
    }

    @Test
    public void testValidateReturnValue() {
        Returns answer = mock(Returns.class);
        Invocation invocation = mock(Invocation.class);
        
        AnswersValidator answersValidator = new AnswersValidator();
        Reporter reporter = new Reporter();
        answersValidator.reporter = reporter;
        
        answersValidator.validateReturnValue(answer, invocation);
        
        Assert.assertEquals(reporter.cannotStubVoidMethodWithAReturnValue(), reporter.cannotStubVoidMethodWithAReturnValue());
        Assert.assertEquals(reporter.wrongTypeOfReturnValue(invocation.printMethodReturnType(), "null", invocation.getMethodName()), reporter.wrongTypeOfReturnValue(invocation.printMethodReturnType(), "null", invocation.getMethodName()));
        Assert.assertEquals(reporter.wrongTypeOfReturnValue(invocation.printMethodReturnType(), answer.printReturnType(), invocation.getMethodName()), reporter.wrongTypeOfReturnValue(invocation.printMethodReturnType(), answer.printReturnType(), invocation.getMethodName()));
    }

    @Test
    public void testValidateException() {
        ThrowsException answer = mock(ThrowsException.class);
        Invocation invocation = mock(Invocation.class);
        
        AnswersValidator answersValidator = new AnswersValidator();
        Reporter reporter = new Reporter();
        answersValidator.reporter = reporter;
        
        answersValidator.validateException(answer, invocation);
        
        Assert.assertEquals(reporter.cannotStubWithNullThrowable(), reporter.cannotStubWithNullThrowable());
        Assert.assertEquals(reporter.checkedExceptionInvalid(answer.getThrowable()), reporter.checkedExceptionInvalid(answer.getThrowable()));
    }
}