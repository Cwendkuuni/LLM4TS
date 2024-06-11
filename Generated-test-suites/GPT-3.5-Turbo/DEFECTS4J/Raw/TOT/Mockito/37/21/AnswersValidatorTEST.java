import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.stubbing.Answer;
import static org.mockito.Mockito.*;

public class AnswersValidatorTest {

    AnswersValidator answersValidator = new AnswersValidator();

    @Test
    public void testValidate_ThrowsException_withValidThrowable_ShouldNotThrowException() {
        Answer<?> answer = mock(ThrowsException.class);
        Invocation invocation = mock(Invocation.class);
        Throwable throwable = new RuntimeException("Test Exception");

        when(answer.getThrowable()).thenReturn(throwable);
        when(invocation.isValidException(throwable)).thenReturn(true);

        answersValidator.validate(answer, invocation);
    }

    @Test
    public void testValidate_ThrowsException_withNullThrowable_ShouldThrowException() {
        Answer<?> answer = mock(ThrowsException.class);
        Invocation invocation = mock(Invocation.class);

        when(answer.getThrowable()).thenReturn(null);
        doThrow(new Reporter().cannotStubWithNullThrowable()).when(reporter).cannotStubWithNullThrowable();

        answersValidator.validate(answer, invocation);

        verify(answer).getThrowable();
        verify(reporter).cannotStubWithNullThrowable();
    }

    @Test
    public void testValidate_Returns_withVoidMethodAndReturnValue_ShouldThrowException() {
        Answer<?> answer = mock(Returns.class);
        Invocation invocation = mock(Invocation.class);

        when(invocation.isVoid()).thenReturn(true);
        doThrow(new Reporter().cannotStubVoidMethodWithAReturnValue()).when(reporter).cannotStubVoidMethodWithAReturnValue();

        answersValidator.validate(answer, invocation);

        verify(invocation).isVoid();
        verify(reporter).cannotStubVoidMethodWithAReturnValue();
    }

    @Test
    public void testValidate_Returns_withPrimitiveReturnValueAndNull_ShouldThrowException() {
        Answer<?> answer = mock(Returns.class);
        Invocation invocation = mock(Invocation.class);

        when(invocation.returnsPrimitive()).thenReturn(true);
        when(answer.returnsNull()).thenReturn(true);
        when(invocation.printMethodReturnType()).thenReturn("int");
        when(invocation.getMethodName()).thenReturn("testMethod");

        doThrow(new Reporter().wrongTypeOfReturnValue("int", "null", "testMethod")).when(reporter).wrongTypeOfReturnValue("int", "null", "testMethod");

        answersValidator.validate(answer, invocation);

        verify(invocation).returnsPrimitive();
        verify(answer).returnsNull();
        verify(invocation).printMethodReturnType();
        verify(invocation).getMethodName();
        verify(reporter).wrongTypeOfReturnValue("int", "null", "testMethod");
    }

    @Test
    public void testValidate_Returns_withInvalidReturnTypeAndNonNullValue_ShouldThrowException() {
        Answer<?> answer = mock(Returns.class);
        Invocation invocation = mock(Invocation.class);

        when(answer.returnsNull()).thenReturn(false);
        when(invocation.isValidReturnType(any())).thenReturn(false);
        when(invocation.printMethodReturnType()).thenReturn("String");
        when(answer.printReturnType()).thenReturn("Integer");
        when(invocation.getMethodName()).thenReturn("testMethod");

        doThrow(new Reporter().wrongTypeOfReturnValue("String", "Integer", "testMethod")).when(reporter).wrongTypeOfReturnValue("String", "Integer", "testMethod");

        answersValidator.validate(answer, invocation);

        verify(answer).returnsNull();
        verify(invocation).isValidReturnType(any());
        verify(invocation).printMethodReturnType();
        verify(answer).printReturnType();
        verify(invocation).getMethodName();
        verify(reporter).wrongTypeOfReturnValue("String", "Integer", "testMethod");
    }

    @Test
    public void testValidate_DoNothing_withNonVoidMethod_ShouldThrowException() {
        Answer<?> answer = mock(DoesNothing.class);
        Invocation invocation = mock(Invocation.class);

        when(invocation.isVoid()).thenReturn(false);
        doThrow(new Reporter().onlyVoidMethodsCanBeSetToDoNothing()).when(reporter).onlyVoidMethodsCanBeSetToDoNothing();

        answersValidator.validate(answer, invocation);

        verify(invocation).isVoid();
        verify(reporter).onlyVoidMethodsCanBeSetToDoNothing();
    }

}