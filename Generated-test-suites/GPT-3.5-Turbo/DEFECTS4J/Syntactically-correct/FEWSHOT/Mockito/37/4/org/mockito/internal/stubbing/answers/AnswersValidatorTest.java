
package org.mockito.internal.stubbing.answers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class AnswersValidatorTest {

    private AnswersValidator answersValidator;
    private Answer<Object> answer;
    private Invocation invocation;

    @Before
    public void setUp() {
        answersValidator = new AnswersValidator();
        answer = mock(Answer.class);
        invocation = mock(Invocation.class);
    }

    @Test
    public void testValidate_ThrowsException() {
        ThrowsException throwsExceptionAnswer = mock(ThrowsException.class);
        when(answer instanceof ThrowsException).thenReturn(true);
        when(answer).thenReturn(throwsExceptionAnswer);
        when(invocation.isValidException(any())).thenReturn(true);

        answersValidator.validate(answer, invocation);

        verify(invocation).isValidException(any());
        verifyNoMoreInteractions(invocation);
    }

    @Test
    public void testValidate_ThrowsException_NullThrowable() {
        ThrowsException throwsExceptionAnswer = mock(ThrowsException.class);
        when(answer instanceof ThrowsException).thenReturn(true);
        when(answer).thenReturn(throwsExceptionAnswer);
        when(throwsExceptionAnswer.getThrowable()).thenReturn(null);
        when(invocation.isValidException(any())).thenReturn(true);

        answersValidator.validate(answer, invocation);

        verify(throwsExceptionAnswer).getThrowable();
        verifyNoMoreInteractions(invocation);
    }

    @Test
    public void testValidate_ThrowsException_RuntimeException() {
        ThrowsException throwsExceptionAnswer = mock(ThrowsException.class);
        when(answer instanceof ThrowsException).thenReturn(true);
        when(answer).thenReturn(throwsExceptionAnswer);
        when(throwsExceptionAnswer.getThrowable()).thenReturn(new RuntimeException());
        when(invocation.isValidException(any())).thenReturn(true);

        answersValidator.validate(answer, invocation);

        verify(throwsExceptionAnswer).getThrowable();
        verifyNoMoreInteractions(invocation);
    }

    @Test
    public void testValidate_ThrowsException_Error() {
        ThrowsException throwsExceptionAnswer = mock(ThrowsException.class);
        when(answer instanceof ThrowsException).thenReturn(true);
        when(answer).thenReturn(throwsExceptionAnswer);
        when(throwsExceptionAnswer.getThrowable()).thenReturn(new Error());
        when(invocation.isValidException(any())).thenReturn(true);

        answersValidator.validate(answer, invocation);

        verify(throwsExceptionAnswer).getThrowable();
        verifyNoMoreInteractions(invocation);
    }

    @Test
    public void testValidate_ThrowsException_InvalidCheckedException() {
        ThrowsException throwsExceptionAnswer = mock(ThrowsException.class);
        when(answer instanceof ThrowsException).thenReturn(true);
        when(answer).thenReturn(throwsExceptionAnswer);
        when(throwsExceptionAnswer.getThrowable()).thenReturn(new Exception());
        when(invocation.isValidException(any())).thenReturn(false);
        Reporter reporter = spy(new Reporter());
        answersValidator.setReporter(reporter);

        answersValidator.validate(answer, invocation);

        verify(throwsExceptionAnswer).getThrowable();
        verify(invocation).isValidException(any());
        verify(reporter).checkedExceptionInvalid(any());
        verifyNoMoreInteractions(invocation);
        verifyNoMoreInteractions(reporter);
    }

    @Test
    public void testValidate_Returns() {
        Returns returnsAnswer = mock(Returns.class);
        when(answer instanceof Returns).thenReturn(true);
        when(answer).thenReturn(returnsAnswer);
        when(invocation.isVoid()).thenReturn(false);
        when(invocation.returnsPrimitive()).thenReturn(false);

        answersValidator.validate(answer, invocation);

        verify(invocation).isVoid();
        verify(invocation).returnsPrimitive();
        verifyNoMoreInteractions(invocation);
    }

    @Test
    public void testValidate_Returns_VoidMethodWithReturnValue() {
        Returns returnsAnswer = mock(Returns.class);
        when(answer instanceof Returns).thenReturn(true);
        when(answer).thenReturn(returnsAnswer);
        when(invocation.isVoid()).thenReturn(true);
        Reporter reporter = spy(new Reporter());
        answersValidator.setReporter(reporter);

        answersValidator.validate(answer, invocation);

        verify(invocation).isVoid();
        verify(reporter).cannotStubVoidMethodWithAReturnValue();
        verifyNoMoreInteractions(invocation);
        verifyNoMoreInteractions(reporter);
    }

    @Test
    public void testValidate_Returns_NullReturnValuePrimitiveMethod() {
        Returns returnsAnswer = mock(Returns.class);
        when(answer instanceof Returns).thenReturn(true);
        when(answer).thenReturn(returnsAnswer);
        when(invocation.isVoid()).thenReturn(false);
        when(invocation.returnsPrimitive()).thenReturn(true);
        when(returnsAnswer.returnsNull()).thenReturn(true);
        when(invocation.printMethodReturnType()).thenReturn("int");
        Reporter reporter = spy(new Reporter());
        answersValidator.setReporter(reporter);

        answersValidator.validate(answer, invocation);

        verify(invocation).isVoid();
        verify(invocation).returnsPrimitive();
        verify(returnsAnswer).returnsNull();
        verify(invocation).printMethodReturnType();
        verify(reporter).wrongTypeOfReturnValue("int", "null", invocation.getMethodName());
        verifyNoMoreInteractions(invocation);
        verifyNoMoreInteractions(reporter);
        verifyNoMoreInteractions(returnsAnswer);
    }

    @Test
    public void testValidate_Returns_InvalidReturnType() {
        Returns returnsAnswer = mock(Returns.class);
        when(answer instanceof Returns).thenReturn(true);
        when(answer).thenReturn(returnsAnswer);
        when(invocation.isVoid()).thenReturn(false);
        when(invocation.returnsPrimitive()).thenReturn(false);
        when(returnsAnswer.returnsNull()).thenReturn(false);
        when(returnsAnswer.getReturnType()).thenReturn(String.class);
        when(invocation.isValidReturnType(any())).thenReturn(false);
        when(invocation.printMethodReturnType()).thenReturn("int");
        Reporter reporter = spy(new Reporter());
        answersValidator.setReporter(reporter);

        answersValidator.validate(answer, invocation);

        verify(invocation).isVoid();
        verify(invocation).returnsPrimitive();
        verify(returnsAnswer).returnsNull();
        verify(returnsAnswer).getReturnType();
        verify(invocation).isValidReturnType(any());
        verify(invocation).printMethodReturnType();
        verify(reporter).wrongTypeOfReturnValue("int", "java.lang.String", invocation.getMethodName());
        verifyNoMoreInteractions(invocation);
        verifyNoMoreInteractions(reporter);
        verifyNoMoreInteractions(returnsAnswer);
    }

    @Test
    public void testValidate_DoesNothing() {
        DoesNothing doesNothingAnswer = mock(DoesNothing.class);
        when(answer instanceof DoesNothing).thenReturn(true);
        when(answer).thenReturn(doesNothingAnswer);
        when(invocation.isVoid()).thenReturn(true);

        answersValidator.validate(answer, invocation);

        verify(invocation).isVoid();
        verifyNoMoreInteractions(invocation);
    }

    @Test
    public void testValidate_DoesNothing_NonVoidMethod() {
        DoesNothing doesNothingAnswer = mock(DoesNothing.class);
        when(answer instanceof DoesNothing).thenReturn(true);
        when(answer).thenReturn(doesNothingAnswer);
        when(invocation.isVoid()).thenReturn(false);
        Reporter reporter = spy(new Reporter());
        answersValidator.setReporter(reporter);

        answersValidator.validate(answer, invocation);

        verify(invocation).isVoid();
        verify(reporter).onlyVoidMethodsCanBeSetToDoNothing();
        verifyNoMoreInteractions(invocation);
        verifyNoMoreInteractions(reporter);
    }

    public void setReporter(Reporter reporter) {
        this.reporter = reporter;
    }

}
