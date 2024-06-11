import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.stubbing.answers.*;

import static org.mockito.Mockito.*;

public class AnswersValidatorTest {

    private AnswersValidator answersValidator;
    private Reporter reporter;

    @Before
    public void setup() {
        answersValidator = new AnswersValidator();
        reporter = spy(new Reporter());
        answersValidator.reporter = reporter;
    }

    @Test
    public void testValidate_DoNothing_VoidMethod_NoException() {
        DoesNothing mockAnswer = mock(DoesNothing.class);

        Invocation invocation = mock(Invocation.class);
        when(invocation.isVoid()).thenReturn(true);

        answersValidator.validate(mockAnswer, invocation);

        verify(invocation, times(1)).isVoid();
        verifyNoMoreInteractions(invocation);
        verify(reporter, never()).onlyVoidMethodsCanBeSetToDoNothing();
    }

    @Test
    public void testValidate_DoNothing_NonVoidMethod_Exception() {
        DoesNothing mockAnswer = mock(DoesNothing.class);

        Invocation invocation = mock(Invocation.class);
        when(invocation.isVoid()).thenReturn(false);

        answersValidator.validate(mockAnswer, invocation);

        verify(invocation, times(1)).isVoid();
        verifyNoMoreInteractions(invocation);
        verify(reporter, times(1)).onlyVoidMethodsCanBeSetToDoNothing();
    }

    @Test
    public void testValidate_ReturnValue_VoidMethod_Exception() {
        Returns mockAnswer = mock(Returns.class);

        Invocation invocation = mock(Invocation.class);
        when(invocation.isVoid()).thenReturn(true);

        answersValidator.validate(mockAnswer, invocation);

        verify(invocation, times(1)).isVoid();
        verifyNoMoreInteractions(invocation);
        verify(reporter, times(1)).cannotStubVoidMethodWithAReturnValue();
    }

    @Test
    public void testValidate_ReturnValue_ReturnsNull_PrimitiveException() {
        Returns mockAnswer = mock(Returns.class);

        Invocation invocation = mock(Invocation.class);
        when(invocation.isVoid()).thenReturn(false);
        when(invocation.returnsPrimitive()).thenReturn(true);

        answersValidator.validate(mockAnswer, invocation);

        verify(invocation, times(1)).isVoid();
        verify(invocation, times(1)).returnsPrimitive();
        verifyNoMoreInteractions(invocation);
        verify(reporter, times(1)).wrongTypeOfReturnValue(anyString(), eq("null"), anyString());
    }

    @Test
    public void testValidate_ReturnValue_ValidReturnType_NoException() {
        Returns mockAnswer = mock(Returns.class);

        Invocation invocation = mock(Invocation.class);
        when(invocation.isVoid()).thenReturn(false);
        when(invocation.returnsPrimitive()).thenReturn(false);
        when(invocation.isValidReturnType(any(Class.class))).thenReturn(true);

        answersValidator.validate(mockAnswer, invocation);

        verify(invocation, times(1)).isVoid();
        verify(invocation, times(1)).returnsPrimitive();
        verify(invocation, times(1)).isValidReturnType(any(Class.class));
        verifyNoMoreInteractions(invocation);
        verify(reporter, never()).wrongTypeOfReturnValue(anyString(), anyString(), anyString());
    }

    @Test
    public void testValidate_ReturnValue_ValidReturnType_Exception() {
        Returns mockAnswer = mock(Returns.class);

        Invocation invocation = mock(Invocation.class);
        when(invocation.isVoid()).thenReturn(false);
        when(invocation.returnsPrimitive()).thenReturn(false);
        when(invocation.isValidReturnType(any(Class.class))).thenReturn(false);
        when(mockAnswer.getReturnType()).thenReturn(Object.class);

        answersValidator.validate(mockAnswer, invocation);

        verify(invocation, times(1)).isVoid();
        verify(invocation, times(1)).returnsPrimitive();
        verify(invocation, times(1)).isValidReturnType(any(Class.class));
        verify(mockAnswer, times(1)).getReturnType();
        verifyNoMoreInteractions(invocation);
        verify(reporter, times(1)).wrongTypeOfReturnValue(anyString(), anyString(), anyString());
    }

    @Test
    public void testValidate_ThrowsException_NullThrowable_Exception() {
        ThrowsException mockAnswer = mock(ThrowsException.class);

        Invocation invocation = mock(Invocation.class);
        when(mockAnswer.getThrowable()).thenReturn(null);

        answersValidator.validate(mockAnswer, invocation);

        verify(mockAnswer, times(1)).getThrowable();
        verifyNoMoreInteractions(invocation);
        verify(reporter, times(1)).cannotStubWithNullThrowable();
    }

    @Test
    public void testValidate_ThrowsException_RuntimeExceptionOrError_NoException() {
        ThrowsException mockAnswer = mock(ThrowsException.class);
        RuntimeException runtimeException = new RuntimeException();

        Invocation invocation = mock(Invocation.class);
        when(mockAnswer.getThrowable()).thenReturn(runtimeException);

        answersValidator.validate(mockAnswer, invocation);

        verify(mockAnswer, times(1)).getThrowable();
        verifyNoMoreInteractions(invocation);
        verify(reporter, never()).checkedExceptionInvalid(any(Throwable.class));
    }

    @Test
    public void testValidate_ThrowsException_CheckedException_Exception() {
        ThrowsException mockAnswer = mock(ThrowsException.class);
        Exception checkedException = new Exception();

        Invocation invocation = mock(Invocation.class);
        when(invocation.isValidException(checkedException)).thenReturn(false);
        when(mockAnswer.getThrowable()).thenReturn(checkedException);

        answersValidator.validate(mockAnswer, invocation);

        verify(invocation, times(1)).isValidException(any(Throwable.class));
        verify(mockAnswer, times(1)).getThrowable();
        verifyNoMoreInteractions(invocation);
        verify(reporter, times(1)).checkedExceptionInvalid(any(Throwable.class));
    }
}