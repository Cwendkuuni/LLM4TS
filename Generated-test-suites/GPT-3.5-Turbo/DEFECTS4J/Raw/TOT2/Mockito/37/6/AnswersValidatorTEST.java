import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.stubbing.answers.AnswersValidator;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;
import org.mockito.internal.stubbing.answers.DoesNothing;
import org.mockito.internal.stubbing.answers.Returns;
import org.mockito.internal.stubbing.answers.ThrowsException;

public class AnswersValidatorTest {

    private AnswersValidator answersValidator = new AnswersValidator();
    private Reporter reporter = new Reporter();

    @Test
    public void testValidateDoNothingWithNonVoidMethod() {
        Invocation invocation = new Invocation("methodName", "methodReturnType", false);
        DoesNothing answer = new DoesNothing();
        reporter.onlyVoidMethodsCanBeSetToDoNothing();
        answersValidator.validateDoNothing(answer, invocation);
    }

    @Test
    public void testValidateReturnValueWithVoidMethod() {
        Invocation invocation = new Invocation("methodName", "void", true);
        Returns answer = new Returns();
        reporter.cannotStubVoidMethodWithAReturnValue();
        answersValidator.validateReturnValue(answer, invocation);
    }

    @Test
    public void testValidateReturnValueWithNullReturnAndPrimitiveType() {
        Invocation invocation = new Invocation("methodName", "int", false);
        Returns answer = new Returns();
        answer.setReturnsNull(true);
        reporter.wrongTypeOfReturnValue("int", "null", "methodName");
        answersValidator.validateReturnValue(answer, invocation);
    }

    @Test
    public void testValidateReturnValueWithDifferentReturnType() {
        Invocation invocation = new Invocation("methodName", "String", false);
        Returns answer = new Returns();
        answer.setReturnType(String.class);
        reporter.wrongTypeOfReturnValue("String", "Integer", "methodName");
        answersValidator.validateReturnValue(answer, invocation);
    }

    @Test
    public void testValidateExceptionWithNullThrowable() {
        Invocation invocation = new Invocation("methodName", "methodReturnType", false);
        ThrowsException answer = new ThrowsException();
        reporter.cannotStubWithNullThrowable();
        answersValidator.validateException(answer, invocation);
    }

    @Test
    public void testValidateExceptionWithCheckedException() {
        Invocation invocation = new Invocation("methodName", "methodReturnType", false);
        ThrowsException answer = new ThrowsException();
        answer.setThrowable(new Exception());
        reporter.checkedExceptionInvalid(new Exception());
        answersValidator.validateException(answer, invocation);
    }
}