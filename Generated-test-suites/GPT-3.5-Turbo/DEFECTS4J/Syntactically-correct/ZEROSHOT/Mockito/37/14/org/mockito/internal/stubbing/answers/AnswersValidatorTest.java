import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;
import org.mockito.internal.stubbing.answers.AnswersValidator;
import org.mockito.internal.stubbing.answers.DoesNothing;
import org.mockito.internal.stubbing.answers.Returns;
import org.mockito.internal.stubbing.answers.ThrowsException;

public class AnswersValidatorTest {

    private AnswersValidator answersValidator = new AnswersValidator();
    private Reporter reporter = new Reporter();

    @Test
    public void testValidate() {
        Answer<?> answer = new Returns();
        Invocation invocation = new Invocation();

        answersValidator.validate(answer, invocation);

        // Assert that no exception is thrown
    }

    @Test
    public void testValidate_ThrowsException() {
        Answer<?> answer = new ThrowsException();
        Invocation invocation = new Invocation();

        answersValidator.validate(answer, invocation);

        // Assert that no exception is thrown
    }

    @Test
    public void testValidate_DoesNothing() {
        Answer<?> answer = new DoesNothing();
        Invocation invocation = new Invocation();

        answersValidator.validate(answer, invocation);

        // Assert that no exception is thrown
    }

    @Test
    public void testValidateReturnValue_VoidMethod() {
        Answer<?> answer = new Returns();
        Invocation invocation = new Invocation();
        invocation.setVoid(true);

        answersValidator.validate(answer, invocation);

        String expectedErrorMessage = "Cannot stub void method with a return value";
        Assert.assertEquals(expectedErrorMessage, reporter.getErrorMessage());
    }

    @Test
    public void testValidateReturnValue_ReturnsNullWithPrimitiveReturnType() {
        Answer<?> answer = new Returns();
        Invocation invocation = new Invocation();
        invocation.setReturnsPrimitive(true);

        answersValidator.validate(answer, invocation);

        String expectedErrorMessage = "Wrong type of return value: null, expected " + invocation.printMethodReturnType() + " for " + invocation.getMethodName();
        Assert.assertEquals(expectedErrorMessage, reporter.getErrorMessage());
    }

    @Test
    public void testValidateReturnValue_WrongReturnType() {
        Answer<?> answer = new Returns();
        Invocation invocation = new Invocation();
        invocation.setReturnType("int");
        answer.setReturnType("String");

        answersValidator.validate(answer, invocation);

        String expectedErrorMessage = "Wrong type of return value: String, expected int for " + invocation.getMethodName();
        Assert.assertEquals(expectedErrorMessage, reporter.getErrorMessage());
    }

    @Test
    public void testValidateException_NullThrowable() {
        Answer<?> answer = new ThrowsException();
        Invocation invocation = new Invocation();

        answersValidator.validate(answer, invocation);

        String expectedErrorMessage = "Cannot stub with null throwable";
        Assert.assertEquals(expectedErrorMessage, reporter.getErrorMessage());
    }

    @Test
    public void testValidateException_RuntimeException() {
        Answer<?> answer = new ThrowsException();
        Invocation invocation = new Invocation();
        Throwable throwable = new RuntimeException();
        answer.setThrowable(throwable);

        answersValidator.validate(answer, invocation);

        // Assert that no exception is thrown
    }

    @Test
    public void testValidateException_Error() {
        Answer<?> answer = new ThrowsException();
        Invocation invocation = new Invocation();
        Throwable throwable = new Error();
        answer.setThrowable(throwable);

        answersValidator.validate(answer, invocation);

        // Assert that no exception is thrown
    }

    @Test
    public void testValidateException_CheckedExceptionInvalid() {
        Answer<?> answer = new ThrowsException();
        Invocation invocation = new Invocation();
        Throwable throwable = new Exception();
        answer.setThrowable(throwable);

        answersValidator.validate(answer, invocation);

        String expectedErrorMessage = "Checked exception is invalid: " + throwable.toString();
        Assert.assertEquals(expectedErrorMessage, reporter.getErrorMessage());
    }
}