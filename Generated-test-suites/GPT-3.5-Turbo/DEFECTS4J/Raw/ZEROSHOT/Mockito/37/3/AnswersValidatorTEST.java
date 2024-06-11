import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;
import org.mockito.internal.stubbing.answers.AnswersValidator;
import org.mockito.internal.stubbing.answers.Returns;
import org.mockito.internal.stubbing.answers.ThrowsException;
import org.mockito.internal.stubbing.answers.DoesNothing;

public class AnswersValidatorTest {

    private AnswersValidator answersValidator;
    private Reporter reporter;

    @Before
    public void setup() {
        answersValidator = new AnswersValidator();
        reporter = new Reporter();
    }

    @Test
    public void testValidateDoNothing() {
        DoesNothing answer = new DoesNothing();
        Invocation invocation = new Invocation();

        answersValidator.validateDoNothing(answer, invocation);

        Assert.assertEquals(reporter.onlyVoidMethodsCanBeSetToDoNothing(), reporter.getMessage());
    }

    @Test
    public void testValidateReturnValueVoidMethod() {
        Returns answer = new Returns();
        answer.setReturnValue("return value");
        Invocation invocation = new Invocation();
        invocation.setVoid(true);

        answersValidator.validateReturnValue(answer, invocation);

        Assert.assertEquals(reporter.cannotStubVoidMethodWithAReturnValue(), reporter.getMessage());
    }

    @Test
    public void testValidateReturnValueNullReturnType() {
        Returns answer = new Returns();
        answer.setReturnsNull(true);
        Invocation invocation = new Invocation();
        invocation.setReturnType(int.class);

        answersValidator.validateReturnValue(answer, invocation);

        Assert.assertEquals(reporter.wrongTypeOfReturnValue("int", "null", invocation.getMethodName()), reporter.getMessage());
    }

    @Test
    public void testValidateReturnValueInvalidReturnType() {
        Returns answer = new Returns();
        answer.setReturnType(String.class);
        Invocation invocation = new Invocation();
        invocation.setReturnType(int.class);

        answersValidator.validateReturnValue(answer, invocation);

        Assert.assertEquals(reporter.wrongTypeOfReturnValue("int", "String", invocation.getMethodName()), reporter.getMessage());
    }

    @Test
    public void testValidateExceptionNullThrowable() {
        ThrowsException answer = new ThrowsException();
        answer.setThrowable(null);
        Invocation invocation = new Invocation();

        answersValidator.validateException(answer, invocation);

        Assert.assertEquals(reporter.cannotStubWithNullThrowable(), reporter.getMessage());
    }

    @Test
    public void testValidateExceptionCheckedExceptionInvalid() {
        ThrowsException answer = new ThrowsException();
        answer.setThrowable(new Exception());
        Invocation invocation = new Invocation();

        answersValidator.validateException(answer, invocation);

        Assert.assertEquals(reporter.checkedExceptionInvalid(throwable), reporter.getMessage());
    }
}