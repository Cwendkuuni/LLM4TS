import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;
import org.mockito.internal.stubbing.answers.AnswersValidator;
import org.mockito.internal.stubbing.answers.DoesNothing;
import org.mockito.internal.stubbing.answers.Returns;
import org.mockito.internal.stubbing.answers.ThrowsException;

public class AnswersValidatorTest {

    private AnswersValidator answersValidator;
    private Reporter reporterMock;

    @Before
    public void setUp() {
        answersValidator = new AnswersValidator();
        reporterMock = mock(Reporter.class);
        answersValidator.reporter = reporterMock;
    }

    @Test
    public void testValidate_DoNothing_VoidMethod_ReturnsCorrectly() {
        //Arrange
        DoesNothing answer = mock(DoesNothing.class);
        when(answer.isDoNothing()).thenReturn(true);
        Invocation invocation = mock(Invocation.class);
        when(invocation.isVoid()).thenReturn(true);

        //Act
        answersValidator.validate(answer, invocation);

        //Assert
        verifyNoInteractions(reporterMock);
    }

    @Test
    public void testValidate_DoNothing_NonVoidMethod_ThrowsException() {
        //Arrange
        DoesNothing answer = mock(DoesNothing.class);
        when(answer.isDoNothing()).thenReturn(true);
        Invocation invocation = mock(Invocation.class);
        when(invocation.isVoid()).thenReturn(false);

        //Act
        answersValidator.validate(answer, invocation);

        //Assert
        verify(reporterMock).onlyVoidMethodsCanBeSetToDoNothing();
    }

    @Test
    public void testValidate_ReturnValue_VoidMethod_ThrowsException() {
        //Arrange
        Returns answer = mock(Returns.class);
        when(answer.returnsNull()).thenReturn(false);
        Invocation invocation = mock(Invocation.class);
        when(invocation.isVoid()).thenReturn(true);

        //Act
        answersValidator.validate(answer, invocation);

        //Assert
        verify(reporterMock).cannotStubVoidMethodWithAReturnValue();
    }

    @Test
    public void testValidate_ReturnValue_NullReturn_PrimitiveMethod_ThrowsException() {
        //Arrange
        Returns answer = mock(Returns.class);
        when(answer.returnsNull()).thenReturn(true);
        Invocation invocation = mock(Invocation.class);
        when(invocation.isVoid()).thenReturn(false);
        when(invocation.returnsPrimitive()).thenReturn(true);
        when(invocation.printMethodReturnType()).thenReturn("int");
        when(invocation.getMethodName()).thenReturn("methodName");

        //Act
        answersValidator.validate(answer, invocation);

        //Assert
        verify(reporterMock).wrongTypeOfReturnValue("int", "null", "methodName");
    }

    @Test
    public void testValidate_ReturnValue_NonNullReturn_InvalidReturnType_ThrowsException() {
        //Arrange
        Returns answer = mock(Returns.class);
        when(answer.returnsNull()).thenReturn(false);
        Invocation invocation = mock(Invocation.class);
        when(invocation.isVoid()).thenReturn(false);
        when(invocation.returnsPrimitive()).thenReturn(false);
        when(invocation.printMethodReturnType()).thenReturn("String");
        when(answer.printReturnType()).thenReturn("Integer");
        when(invocation.getMethodName()).thenReturn("methodName");

        //Act
        answersValidator.validate(answer, invocation);

        //Assert
        verify(reporterMock).wrongTypeOfReturnValue("String", "Integer", "methodName");
    }

    @Test
    public void testValidate_ReturnValue_NonNullReturn_ValidReturnType_DoesNothing() {
        //Arrange
        Returns answer = mock(Returns.class);
        when(answer.returnsNull()).thenReturn(false);
        Invocation invocation = mock(Invocation.class);
        when(invocation.isVoid()).thenReturn(false);
        when(invocation.returnsPrimitive()).thenReturn(false);
        when(invocation.printMethodReturnType()).thenReturn("String");
        when(answer.isValidReturnType(any())).thenReturn(true);

        //Act
        answersValidator.validate(answer, invocation);

        //Assert
        verifyNoInteractions(reporterMock);
    }

    @Test
    public void testValidate_Exception_NullThrowable_ThrowsException() {
        //Arrange
        ThrowsException answer = mock(ThrowsException.class);
        when(answer.getThrowable()).thenReturn(null);
        Invocation invocation = mock(Invocation.class);

        //Act
        answersValidator.validate(answer, invocation);

        //Assert
        verify(reporterMock).cannotStubWithNullThrowable();
    }

    @Test
    public void testValidate_Exception_RuntimeException_DoesNothing() {
        //Arrange
        ThrowsException answer = mock(ThrowsException.class);
        Throwable throwable = new RuntimeException();
        when(answer.getThrowable()).thenReturn(throwable);
        Invocation invocation = mock(Invocation.class);

        //Act
        answersValidator.validate(answer, invocation);

        //Assert
        verifyNoInteractions(reporterMock);
    }

    @Test
    public void testValidate_Exception_Error_DoesNothing() {
        //Arrange
        ThrowsException answer = mock(ThrowsException.class);
        Throwable throwable = new Error();
        when(answer.getThrowable()).thenReturn(throwable);
        Invocation invocation = mock(Invocation.class);

        //Act
        answersValidator.validate(answer, invocation);

        //Assert
        verifyNoInteractions(reporterMock);
    }

    @Test
    public void testValidate_Exception_CheckedException_Invalid_ThrowsException() {
        //Arrange
        ThrowsException answer = mock(ThrowsException.class);
        Throwable throwable = new Exception();
        when(answer.getThrowable()).thenReturn(throwable);
        Invocation invocation = mock(Invocation.class);
        when(invocation.isValidException(any())).thenReturn(false);

        //Act
        answersValidator.validate(answer, invocation);

        //Assert
        verify(reporterMock).checkedExceptionInvalid(throwable);
    }

    @Test
    public void testValidate_Exception_CheckedException_Valid_DoesNothing() {
        //Arrange
        ThrowsException answer = mock(ThrowsException.class);
        Throwable throwable = new Exception();
        when(answer.getThrowable()).thenReturn(throwable);
        Invocation invocation = mock(Invocation.class);
        when(invocation.isValidException(any())).thenReturn(true);

        //Act
        answersValidator.validate(answer, invocation);

        //Assert
        verifyNoInteractions(reporterMock);
    }
}