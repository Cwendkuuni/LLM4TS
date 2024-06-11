import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;

public class AnswersValidatorTest {

    private AnswersValidator answersValidator;
    private Reporter mockReporter;
    
    @Before
    public void setup() {
        answersValidator = new AnswersValidator();
        mockReporter = Mockito.mock(Reporter.class);
        Mockito.when(mockReporter.onlyVoidMethodsCanBeSetToDoNothing()).thenReturn("");
        Mockito.when(mockReporter.cannotStubVoidMethodWithAReturnValue()).thenReturn("");
        Mockito.when(mockReporter.wrongTypeOfReturnValue("", "", "")).thenReturn("");
        Mockito.when(mockReporter.cannotStubWithNullThrowable()).thenReturn("");
        Mockito.when(mockReporter.checkedExceptionInvalid(null)).thenReturn("");
    }
    
    @Test
    public void testValidate_DoNothing_VoidMethod_VoidInvocation_ReportError() {
        Invocation mockInvocation = Mockito.mock(Invocation.class);
        Mockito.when(mockInvocation.isVoid()).thenReturn(true);
        
        answersValidator.setReporter(mockReporter);
        answersValidator.validate(new DoesNothing(), mockInvocation);
        
        Mockito.verify(mockReporter).onlyVoidMethodsCanBeSetToDoNothing();
    }
    
    @Test
    public void testValidate_DoNothing_NonVoidMethod_VoidInvocation_DoNothing() {
        Invocation mockInvocation = Mockito.mock(Invocation.class);
        Mockito.when(mockInvocation.isVoid()).thenReturn(true);
        
        answersValidator.setReporter(mockReporter);
        answersValidator.validate(new DoesNothing(), mockInvocation);
        
        Mockito.verify(mockReporter, Mockito.never()).onlyVoidMethodsCanBeSetToDoNothing();
    }
    
    @Test
    public void testValidate_Returns_VoidMethod_VoidInvocation_ReportError() {
        Invocation mockInvocation = Mockito.mock(Invocation.class);
        Mockito.when(mockInvocation.isVoid()).thenReturn(true);
        
        answersValidator.setReporter(mockReporter);
        answersValidator.validate(new Returns(), mockInvocation);
        
        Mockito.verify(mockReporter).cannotStubVoidMethodWithAReturnValue();
    }
    
    @Test
    public void testValidate_Returns_NonVoidMethod_NonNullReturnValue_ValidReturnType_DoNothing() {
        Invocation mockInvocation = Mockito.mock(Invocation.class);
        Mockito.when(mockInvocation.isVoid()).thenReturn(false);
        Mockito.when(mockInvocation.isValidReturnType(Mockito.any())).thenReturn(true);
        
        answersValidator.setReporter(mockReporter);
        answersValidator.validate(new Returns(), mockInvocation);
        
        Mockito.verify(mockReporter, Mockito.never()).cannotStubVoidMethodWithAReturnValue();
        Mockito.verify(mockReporter, Mockito.never()).wrongTypeOfReturnValue(Mockito.any(), Mockito.any(), Mockito.any());
    }
    
    @Test
    public void testValidate_Returns_NonVoidMethod_NonNullReturnValue_InvalidReturnType_ReportError() {
        Invocation mockInvocation = Mockito.mock(Invocation.class);
        Mockito.when(mockInvocation.isVoid()).thenReturn(false);
        Mockito.when(mockInvocation.isValidReturnType(Mockito.any())).thenReturn(false);
        
        answersValidator.setReporter(mockReporter);
        answersValidator.validate(new Returns(), mockInvocation);
        
        Mockito.verify(mockReporter, Mockito.never()).cannotStubVoidMethodWithAReturnValue();
        Mockito.verify(mockReporter).wrongTypeOfReturnValue(Mockito.any(), Mockito.any(), Mockito.any());
    }
    
    @Test
    public void testValidate_Returns_NonVoidMethod_NullReturnValue_InvalidReturnType_ReportError() {
        Invocation mockInvocation = Mockito.mock(Invocation.class);
        Mockito.when(mockInvocation.isVoid()).thenReturn(false);
        Mockito.when(mockInvocation.returnsPrimitive()).thenReturn(true);
        Mockito.when(mockInvocation.printMethodReturnType()).thenReturn("");
        Mockito.when(mockInvocation.getMethodName()).thenReturn("");
        
        answersValidator.setReporter(mockReporter);
        answersValidator.validate(new Returns(), mockInvocation);
        
        Mockito.verify(mockReporter, Mockito.never()).cannotStubVoidMethodWithAReturnValue();
        Mockito.verify(mockReporter, Mockito.never()).wrongTypeOfReturnValue(Mockito.any(), Mockito.any(), Mockito.any());
    }
    
    @Test
    public void testValidate_Returns_NonVoidMethod_NullReturnValue_ValidReturnType_DoNothing() {
        Invocation mockInvocation = Mockito.mock(Invocation.class);
        Mockito.when(mockInvocation.isVoid()).thenReturn(false);
        Mockito.when(mockInvocation.returnsPrimitive()).thenReturn(true);
        Mockito.when(mockInvocation.printMethodReturnType()).thenReturn("");
        Mockito.when(mockInvocation.getMethodName()).thenReturn("");
        
        answersValidator.setReporter(mockReporter);
        answersValidator.validate(new Returns(), mockInvocation);
        
        Mockito.verify(mockReporter, Mockito.never()).cannotStubVoidMethodWithAReturnValue();
        Mockito.verify(mockReporter, Mockito.never()).wrongTypeOfReturnValue(Mockito.any(), Mockito.any(), Mockito.any());
    }
    
    @Test
    public void testValidate_Exception_ThrowableIsNull_ReportError() {
        Invocation mockInvocation = Mockito.mock(Invocation.class);
        
        answersValidator.setReporter(mockReporter);
        answersValidator.validate(new ThrowsException(), mockInvocation);
        
        Mockito.verify(mockReporter).cannotStubWithNullThrowable();
    }
    
    @Test
    public void testValidate_Exception_ThrowableIsRuntimeExceptionOrError_DoNothing() {
        Invocation mockInvocation = Mockito.mock(Invocation.class);
        
        answersValidator.setReporter(mockReporter);
        answersValidator.validate(new ThrowsException(), mockInvocation);
        
        Mockito.verify(mockReporter, Mockito.never()).cannotStubWithNullThrowable();
        Mockito.verify(mockReporter, Mockito.never()).checkedExceptionInvalid(Mockito.any());
    }
    
    @Test
    public void testValidate_Exception_ValidCheckedException_DoNothing() {
        Invocation mockInvocation = Mockito.mock(Invocation.class);
        Mockito.when(mockInvocation.isValidException(Mockito.any())).thenReturn(true);
        
        answersValidator.setReporter(mockReporter);
        answersValidator.validate(new ThrowsException(), mockInvocation);
        
        Mockito.verify(mockReporter, Mockito.never()).cannotStubWithNullThrowable();
        Mockito.verify(mockReporter, Mockito.never()).checkedExceptionInvalid(Mockito.any());
    }
    
    @Test
    public void testValidate_Exception_InvalidCheckedException_ReportError() {
        Invocation mockInvocation = Mockito.mock(Invocation.class);
        Mockito.when(mockInvocation.isValidException(Mockito.any())).thenReturn(false);
        
        answersValidator.setReporter(mockReporter);
        answersValidator.validate(new ThrowsException(), mockInvocation);
        
        Mockito.verify(mockReporter, Mockito.never()).cannotStubWithNullThrowable();
        Mockito.verify(mockReporter).checkedExceptionInvalid(Mockito.any());
    }
}