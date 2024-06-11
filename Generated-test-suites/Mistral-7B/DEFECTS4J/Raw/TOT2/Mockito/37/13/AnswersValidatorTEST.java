import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.answers.AnswersValidator;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

public class AnswersValidatorTest {
    private AnswersValidator validator;
    private Reporter reporter;

    @Before
    public void setup() {
        reporter = mock(Reporter.class);
        validator = new AnswersValidator(reporter);
    }

    @Test
    public void testValidateDoNothing() {
        Answer<Void> answer = Mockito.mock(DoesNothing.class);
        Invocation invocation = new Invocation("testMethod", new Object[]{});
        validator.validate(answer, invocation);
        verifyNoMoreInteractions(reporter);
    }

    @Test
    public void testValidateReturnValue_Valid() {
        Answer<String> answer = Mockito.mock(Returns.class);
        answer.returns("testString");
        Invocation invocation = new Invocation("testMethod", new Object[]{});
        validator.validate(answer, invocation);
        verifyNoMoreInteractions(reporter);
    }

    @Test
    public void testValidateReturnValue_Null_Primitive() {
        Returns answer = Mockito.mock(Returns.class);
        answer.returns(null);
        Invocation invocation = new Invocation("testMethod", new Object[]{"int"});
        validator.validate(answer, invocation);
        Mockito.verify(reporter).wrongTypeOfReturnValue("int", "null", "testMethod");
    }

    @Test
    public void testValidateReturnValue_Null_NonPrimitive() {
        Returns answer = Mockito.mock(Returns.class);
        answer.returns(null);
        Invocation invocation = new Invocation("testMethod", new Object[]{"String"});
        validator.validate(answer, invocation);
        Mockito.verifyNoInteractions(reporter);
    }

    @Test
    public void testValidateException_Null_Throwable() {
        ThrowsException answer = Mockito.mock(ThrowsException.class);
        Invocation invocation = new Invocation("testMethod", new Object[]{});
        validator.validate(answer, invocation);
        Mockito.verify(reporter).cannotStubWithNullThrowable();
    }

    @Test
    public void testValidateException_InvalidException() {
        ThrowsException answer = Mockito.mock(ThrowsException.class);
        Throwable throwable = new IllegalArgumentException();
        answer.getThrowable(throwable);
        Invocation invocation = new Invocation("testMethod", new Object[]{});
        validator.validate(answer, invocation);
        Mockito.verify(reporter).checkedExceptionInvalid(throwable);
    }
}