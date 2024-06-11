
import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;
import org.mockito.internal.stubbing.answers.*;

public class AnswersValidatorTest {

    @Test
    public void testValidate_DoNothing_WithVoidMethod() {
        DoesNothing answer = new DoesNothing();
        Invocation invocation = new Invocation();
        invocation.setVoid(true);
        Reporter reporter = new Reporter();

        AnswersValidator validator = new AnswersValidator();
        validator.validateDoNothing(answer, invocation);

        Assert.assertTrue(reporter.hasInvalidVoidMethodException());
    }

    @Test
    public void testValidate_Returns_WithVoidMethod() {
        Returns answer = new Returns();
        Invocation invocation = new Invocation();
        invocation.setVoid(true);
        Reporter reporter = new Reporter();

        AnswersValidator validator = new AnswersValidator();
        validator.validateReturnValue(answer, invocation);

        Assert.assertTrue(reporter.hasInvalidVoidMethodException());
    }

    @Test
    public void testValidate_Returns_WithNullReturnValueAndPrimitiveMethod() {
        Returns answer = new Returns();
        Invocation invocation = new Invocation();
        invocation.setReturnsPrimitive(true);
        answer.setReturnsNull(true);
        Reporter reporter = new Reporter();

        AnswersValidator validator = new AnswersValidator();
        validator.validateReturnValue(answer, invocation);

        Assert.assertTrue(reporter.hasWrongTypeOfReturnValueException());
    }

    @Test
    public void testValidate_Returns_WithInvalidReturnValue() {
        Returns answer = new Returns();
        Invocation invocation = new Invocation();
        answer.setReturnType(String.class);
        invocation.setMethodReturnType(Integer.class);
        Reporter reporter = new Reporter();

        AnswersValidator validator = new AnswersValidator();
        validator.validateReturnValue(answer, invocation);

        Assert.assertTrue(reporter.hasWrongTypeOfReturnValueException());
    }

    @Test
    public void testValidate_Exception_WithNullThrowable() {
        ThrowsException answer = new ThrowsException();
        Invocation invocation = new Invocation();
        Throwable throwable = null;
        answer.setThrowable(throwable);
        Reporter reporter = new Reporter();

        AnswersValidator validator = new AnswersValidator();
        validator.validateException(answer, invocation);

        Assert.assertTrue(reporter.hasCannotStubWithNullThrowableException());
    }

    @Test
    public void testValidate_Exception_WithCheckedException() {
        ThrowsException answer = new ThrowsException();
        Invocation invocation = new Invocation();
        Throwable throwable = new Exception();
        answer.setThrowable(throwable);
        Reporter reporter = new Reporter();

        AnswersValidator validator = new AnswersValidator();
        validator.validateException(answer, invocation);

        Assert.assertTrue(reporter.hasCheckedExceptionInvalidException());
    }

    @Test
    public void testValidate_Exception_WithRuntimeException() {
        ThrowsException answer = new ThrowsException();
        Invocation invocation = new Invocation();
        Throwable throwable = new RuntimeException();
        answer.setThrowable(throwable);
        Reporter reporter = new Reporter();

        AnswersValidator validator = new AnswersValidator();
        validator.validateException(answer, invocation);

        Assert.assertFalse(reporter.hasCheckedExceptionInvalidException());
    }

    @Test
    public void testValidate_Exception_WithError() {
        ThrowsException answer = new ThrowsException();
        Invocation invocation = new Invocation();
        Throwable throwable = new Error();
        answer.setThrowable(throwable);
        Reporter reporter = new Reporter();

        AnswersValidator validator = new AnswersValidator();
        validator.validateException(answer, invocation);

        Assert.assertFalse(reporter.hasCheckedExceptionInvalidException());
    }
}
