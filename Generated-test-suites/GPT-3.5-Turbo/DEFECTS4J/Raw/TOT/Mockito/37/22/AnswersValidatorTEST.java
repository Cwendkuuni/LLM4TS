import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class AnswersValidatorTest {

    private AnswersValidator answersValidator;
    private Reporter mockedReporter;
    
    @Before
    public void setUp() {
        answersValidator = new AnswersValidator();
        mockedReporter = mock(Reporter.class);
        answersValidator.setReporter(mockedReporter);
    }
    
    @Test
    public void testValidate_DoNothing_WithVoidMethod_ShouldCallReporterOnlyVoidMethodsCanBeSetToDoNothing() {
        // Arrange
        DoesNothing doNothing = new DoesNothing();
        Invocation voidMethodInvocation = mock(Invocation.class);
        when(voidMethodInvocation.isVoid()).thenReturn(true);
        
        // Act
        answersValidator.validate(doNothing, voidMethodInvocation);
        
        // Assert
        verify(mockedReporter).onlyVoidMethodsCanBeSetToDoNothing();
    }
    
    @Test
    public void testValidate_Returns_WithVoidMethod_ShouldCallReporterCannotStubVoidMethodWithAReturnValue() {
        // Arrange
        Returns returns = new Returns();
        Invocation voidMethodInvocation = mock(Invocation.class);
        when(voidMethodInvocation.isVoid()).thenReturn(true);
        
        // Act
        answersValidator.validate(returns, voidMethodInvocation);
        
        // Assert
        verify(mockedReporter).cannotStubVoidMethodWithAReturnValue();
    }
    
    @Test
    public void testValidate_Returns_WithNullReturnValueAndPrimitiveReturnType_ShouldCallReporterWrongTypeOfReturnValue() {
        // Arrange
        Returns returns = new Returns();
        Invocation primitiveMethodInvocation = mock(Invocation.class);
        when(primitiveMethodInvocation.isVoid()).thenReturn(false);
        when(primitiveMethodInvocation.returnsPrimitive()).thenReturn(true);
        when(primitiveMethodInvocation.printMethodReturnType()).thenReturn("int");
        returns.setReturnsNull(true);
        
        // Act
        answersValidator.validate(returns, primitiveMethodInvocation);
        
        // Assert
        verify(mockedReporter).wrongTypeOfReturnValue("int", "null", primitiveMethodInvocation.getMethodName());
    }
    
    @Test
    public void testValidate_Returns_WithNonVoidMethodAndInvalidReturnType_ShouldCallReporterWrongTypeOfReturnValue() {
        // Arrange
        Returns returns = new Returns();
        Invocation nonVoidMethodInvocation = mock(Invocation.class);
        when(nonVoidMethodInvocation.isVoid()).thenReturn(false);
        when(nonVoidMethodInvocation.isValidReturnType(any(Class.class))).thenReturn(false);
        when(nonVoidMethodInvocation.printMethodReturnType()).thenReturn("String");
        returns.setReturnType(String.class);
        
        // Act
        answersValidator.validate(returns, nonVoidMethodInvocation);
        
        // Assert
        verify(mockedReporter).wrongTypeOfReturnValue("String", "String", nonVoidMethodInvocation.getMethodName());
    }
    
    @Test
    public void testValidate_Exception_WithNullThrowable_ShouldCallReporterCannotStubWithNullThrowable() {
        // Arrange
        ThrowsException throwsException = new ThrowsException();
        Invocation methodInvocation = mock(Invocation.class);
        when(methodInvocation.getMethodName()).thenReturn("methodName");
        
        // Act
        answersValidator.validate(throwsException, methodInvocation);
        
        // Assert
        verify(mockedReporter).cannotStubWithNullThrowable();
    }
    
    @Test
    public void testValidate_Exception_WithCheckedException_ShouldNotCallReporterCheckedExceptionInvalid() {
        // Arrange
        ThrowsException throwsException = new ThrowsException();
        Throwable checkedException = mock(Exception.class);
        Invocation methodInvocation = mock(Invocation.class);
        when(methodInvocation.isValidException(checkedException)).thenReturn(true);
        throwsException.setThrowable(checkedException);
        
        // Act
        answersValidator.validate(throwsException, methodInvocation);
        
        // Assert
        verify(mockedReporter, never()).checkedExceptionInvalid(checkedException);
    }
    
    @Test
    public void testValidate_Exception_WithRuntimeException_ShouldNotCallReporterCheckedExceptionInvalid() {
        // Arrange
        ThrowsException throwsException = new ThrowsException();
        Throwable runtimeException = mock(RuntimeException.class);
        Invocation methodInvocation = mock(Invocation.class);
        throwsException.setThrowable(runtimeException);
        
        // Act
        answersValidator.validate(throwsException, methodInvocation);
        
        // Assert
        verify(mockedReporter, never()).checkedExceptionInvalid(runtimeException);
    }
    
    @Test
    public void testValidate_Exception_WithError_ShouldNotCallReporterCheckedExceptionInvalid() {
        // Arrange
        ThrowsException throwsException = new ThrowsException();
        Throwable error = mock(Error.class);
        Invocation methodInvocation = mock(Invocation.class);
        throwsException.setThrowable(error);
        
        // Act
        answersValidator.validate(throwsException, methodInvocation);
        
        // Assert
        verify(mockedReporter, never()).checkedExceptionInvalid(error);
    }
}