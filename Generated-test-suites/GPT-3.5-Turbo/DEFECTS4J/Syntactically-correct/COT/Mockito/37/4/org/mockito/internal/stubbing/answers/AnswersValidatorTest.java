
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;
import org.mockito.internal.stubbing.answers.AnswersValidator;
import org.mockito.internal.stubbing.answers.Returns;
import org.mockito.internal.stubbing.answers.DoesNothing;
import org.mockito.internal.stubbing.answers.ThrowsException;

public class AnswersValidatorTest {

    @Test
    public void validate_DoNothing_Answer_With_NonVoid_Method_Should_Throw_Exception() {
        Answer<Void> answer = new DoesNothing();
        Invocation invocation = new Invocation("methodName", "ReturnType", new Object[] {}, false, false);
        Reporter reporter = new Reporter();

        try {
            reporter.onlyVoidMethodsCanBeSetToDoNothing();
            validate(answer, invocation, reporter);
        } catch (Exception e) {
            assert(e.getMessage().equals("onlyVoidMethodsCanBeSetToDoNothing"));
        }
    }

    @Test
    public void validate_Returns_Answer_With_Void_Method_Should_Throw_Exception() {
        Answer<?> answer = new Returns();
        Invocation invocation = new Invocation("methodName", "void", new Object[] {}, true, false);
        Reporter reporter = new Reporter();

        try {
            reporter.cannotStubVoidMethodWithAReturnValue();
            validate(answer, invocation, reporter);
        } catch (Exception e) {
            assert(e.getMessage().equals("cannotStubVoidMethodWithAReturnValue"));
        }
    }

    @Test
    public void validate_Returns_Answer_With_Null_Return_Value_And_Primitive_Method_Should_Throw_Exception() {
        Answer<?> answer = new Returns();
        Invocation invocation = new Invocation("methodName", "int", new Object[] {}, false, true);
        Reporter reporter = new Reporter();

        try {
            reporter.wrongTypeOfReturnValue("int", "null", "methodName");
            validate(answer, invocation, reporter);
        } catch (Exception e) {
            assert(e.getMessage().equals("wrongTypeOfReturnValue: expected int, but got null in methodName"));
        }
    }

    @Test
    public void validate_Returns_Answer_With_Invalid_Return_Type_Should_Throw_Exception() {
        Answer<?> answer = new Returns();
        Invocation invocation = new Invocation("methodName", "ReturnType", new Object[] {}, false, false);
        Reporter reporter = new Reporter();

        try {
            reporter.wrongTypeOfReturnValue("ReturnType", "InvalidType", "methodName");
            validate(answer, invocation, reporter);
        } catch (Exception e) {
            assert(e.getMessage().equals("wrongTypeOfReturnValue: expected ReturnType, but got InvalidType in methodName"));
        }
    }

    @Test
    public void validate_ThrowsException_Answer_With_Null_Throwable_Should_Throw_Exception() {
        ThrowsException answer = new ThrowsException();
        Invocation invocation = new Invocation("methodName", "ReturnType", new Object[] {}, false, false);
        Reporter reporter = new Reporter();

        try {
            reporter.cannotStubWithNullThrowable();
            validate(answer, invocation, reporter);
        } catch (Exception e) {
            assert(e.getMessage().equals("cannotStubWithNullThrowable"));
        }
    }

    @Test
    public void validate_ThrowsException_Answer_With_Checked_Exception_Should_Not_Throw_Exception() {
        ThrowsException answer = new ThrowsException(new Exception());
        Invocation invocation = new Invocation("methodName", "ReturnType", new Object[] {}, false, false);
        Reporter reporter = new Reporter();

        try {
            validate(answer, invocation, reporter);
        } catch (Exception e) {
            assert(false);
        }
    }

    @Test
    public void validate_ThrowsException_Answer_With_Runtime_Exception_Should_Not_Throw_Exception() {
        ThrowsException answer = new ThrowsException(new RuntimeException());
        Invocation invocation = new Invocation("methodName", "ReturnType", new Object[] {}, false, false);
        Reporter reporter = new Reporter();

        try {
            validate(answer, invocation, reporter);
        } catch (Exception e) {
            assert(false);
        }
    }

    private void validate(Answer<?> answer, Invocation invocation, Reporter reporter) {
        AnswersValidator answersValidator = new AnswersValidator();
        answersValidator.setReporter(reporter);
        answersValidator.validate(answer, invocation);
    }
}
