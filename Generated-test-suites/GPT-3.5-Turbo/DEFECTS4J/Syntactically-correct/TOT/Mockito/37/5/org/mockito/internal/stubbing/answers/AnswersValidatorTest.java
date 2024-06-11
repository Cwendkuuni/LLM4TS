import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.stubbing.Answer;
import static org.mockito.Mockito.*;

public class AnswersValidatorTest {

    private AnswersValidator answersValidator;
    private Reporter reporter;

    @Before
    public void setup() {
        answersValidator = new AnswersValidator();
        reporter = mock(Reporter.class);
        answersValidator.setReporter(reporter);
    }

    @Test
    public void validate_ThrowsException_ValidInvocation_Success() {
        // Setup
        Answer<?> answer = mock(ThrowsException.class);
        Invocation invocation = mock(Invocation.class);
        when(answer instanceof ThrowsException).thenReturn(true);

        // Exercise
        answersValidator.validate(answer, invocation);

        // Verify
        verifyNoInteractions(reporter);
    }

    @Test
    public void validate_ThrowsException_NullThrowable_ThrowsException() {
        // Setup
        Answer<?> answer = mock(ThrowsException.class);
        Invocation invocation = mock(Invocation.class);
        when(answer instanceof ThrowsException).thenReturn(true);
        when(((ThrowsException) answer).getThrowable()).thenReturn(null);

        // Exercise
        answersValidator.validate(answer, invocation);

        // Verify
        verify(reporter).cannotStubWithNullThrowable();
    }

    @Test
    public void validate_ThrowsException_RuntimeExceptionOrError_Success() {
        // Setup
        Answer<?> answer = mock(ThrowsException.class);
        Invocation invocation = mock(Invocation.class);
        when(answer instanceof ThrowsException).thenReturn(true);
        when(((ThrowsException) answer).getThrowable()).thenReturn(new RuntimeException());

        // Exercise
        answersValidator.validate(answer, invocation);

        // Verify
        verifyNoInteractions(reporter);
    }

    @Test
    public void validate_ThrowsException_CheckedInvalidException_ThrowsException() {
        // Setup
        Answer<?> answer = mock(ThrowsException.class);
        Invocation invocation = mock(Invocation.class);
        when(answer instanceof ThrowsException).thenReturn(true);
        when(((ThrowsException) answer).getThrowable()).thenReturn(new IllegalArgumentException());

        // Exercise
        answersValidator.validate(answer, invocation);

        // Verify
        verify(reporter).checkedExceptionInvalid(any(Throwable.class));
    }

    @Test
    public void validate_Returns_ValidInvocation_Success() {
        // Setup
        Answer<?> answer = mock(Returns.class);
        Invocation invocation = mock(Invocation.class);
        when(answer instanceof Returns).thenReturn(true);
        when(invocation.isVoid()).thenReturn(false);

        // Exercise
        answersValidator.validate(answer, invocation);

        // Verify
        verifyNoInteractions(reporter);
    }

    @Test
    public void validate_Returns_VoidMethodWithReturnValue_ThrowsException() {
        // Setup
        Answer<?> answer = mock(Returns.class);
        Invocation invocation = mock(Invocation.class);
        when(answer instanceof Returns).thenReturn(true);
        when(invocation.isVoid()).thenReturn(true);

        // Exercise
        answersValidator.validate(answer, invocation);

        // Verify
        verify(reporter).cannotStubVoidMethodWithAReturnValue();
    }

    @Test
    public void validate_Returns_NullReturnValueForPrimitiveType_ThrowsException() {
        // Setup
        Answer<?> answer = mock(Returns.class);
        Invocation invocation = mock(Invocation.class);
        when(answer instanceof Returns).thenReturn(true);
        when(invocation.isVoid()).thenReturn(false);
        when(invocation.returnsPrimitive()).thenReturn(true);
        when(((Returns) answer).returnsNull()).thenReturn(true);
        when(invocation.printMethodReturnType()).thenReturn("int");
        when(invocation.getMethodName()).thenReturn("testMethod");

        // Exercise
        answersValidator.validate(answer, invocation);

        // Verify
        verify(reporter).wrongTypeOfReturnValue(eq("int"), eq("null"), eq("testMethod"));
    }

    @Test
    public void validate_Returns_InvalidReturnType_ThrowsException() {
        // Setup
        Answer<?> answer = mock(Returns.class);
        Invocation invocation = mock(Invocation.class);
        when(answer instanceof Returns).thenReturn(true);
        when(invocation.isVoid()).thenReturn(false);
        when(invocation.isValidReturnType(any(Class.class))).thenReturn(false);
        when(invocation.printMethodReturnType()).thenReturn("String");
        when(((Returns) answer).printReturnType()).thenReturn("Integer");
        when(invocation.getMethodName()).thenReturn("testMethod");

        // Exercise
        answersValidator.validate(answer, invocation);

        // Verify
        verify(reporter).wrongTypeOfReturnValue(eq("String"), eq("Integer"), eq("testMethod"));
    }

    @Test
    public void validate_DoNothing_ValidInvocation_Success() {
        // Setup
        Answer<?> answer = mock(DoesNothing.class);
        Invocation invocation = mock(Invocation.class);
        when(answer instanceof DoesNothing).thenReturn(true);
        when(invocation.isVoid()).thenReturn(true);

        // Exercise
        answersValidator.validate(answer, invocation);

        // Verify
        verifyNoInteractions(reporter);
    }

    @Test
    public void validate_DoNothing_NonVoidMethod_ThrowsException() {
        // Setup
        Answer<?> answer = mock(DoesNothing.class);
        Invocation invocation = mock(Invocation.class);
        when(answer instanceof DoesNothing).thenReturn(true);
        when(invocation.isVoid()).thenReturn(false);

        // Exercise
        answersValidator.validate(answer, invocation);

        // Verify
        verify(reporter).onlyVoidMethodsCanBeSetToDoNothing();
    }
}