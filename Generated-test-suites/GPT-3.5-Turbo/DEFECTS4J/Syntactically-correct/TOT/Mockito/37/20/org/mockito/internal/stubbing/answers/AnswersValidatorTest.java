import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.stubbing.answers.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AnswersValidatorTest {
  
  @Test
  public void testValidate_DoNothing() {
    // Typical use case
    Invocation invocation = mock(Invocation.class);
    when(invocation.isVoid()).thenReturn(true);
    
    DoesNothing answer = mock(DoesNothing.class);
    when(answer.getReturnType()).thenReturn(Void.TYPE);
    
    AnswersValidator validator = new AnswersValidator();
    validator.validate(answer, invocation);
    
    // Error scenario
    Invocation invocation2 = mock(Invocation.class);
    when(invocation2.isVoid()).thenReturn(false);
    
    Reporter reporter = mock(Reporter.class);
    doNothing().when(reporter).onlyVoidMethodsCanBeSetToDoNothing();
    
    AnswersValidator validator2 = new AnswersValidator();
    validator2.validate(answer, invocation2);
    
    verify(reporter).onlyVoidMethodsCanBeSetToDoNothing();
  }
  
  @Test
  public void testValidate_ReturnValue() {
    // Typical use case
    Invocation invocation = mock(Invocation.class);
    when(invocation.isVoid()).thenReturn(false);
    when(invocation.returnsPrimitive()).thenReturn(false);
    when(invocation.printMethodReturnType()).thenReturn("java.lang.String");
    when(invocation.getMethodName()).thenReturn("getString");
    when(invocation.isValidReturnType(String.class)).thenReturn(true);
    
    Returns answer = mock(Returns.class);
    when(answer.getReturnType()).thenReturn(String.class);
    when(answer.printReturnType()).thenReturn("java.lang.String");
    when(answer.returnsNull()).thenReturn(false);
    
    AnswersValidator validator = new AnswersValidator();
    validator.validate(answer, invocation);
    
    // Edge case
    Invocation invocation2 = mock(Invocation.class);
    when(invocation2.isVoid()).thenReturn(true);
    when(invocation2.returnsPrimitive()).thenReturn(true);
    when(invocation2.printMethodReturnType()).thenReturn("int");
    when(invocation2.getMethodName()).thenReturn("getNumber");
    
    Reporter reporter = mock(Reporter.class);
    doNothing().when(reporter).cannotStubVoidMethodWithAReturnValue();
    
    AnswersValidator validator2 = new AnswersValidator();
    validator2.validate(answer, invocation2);
    
    verify(reporter).cannotStubVoidMethodWithAReturnValue();
    
    // Error scenario
    Invocation invocation3 = mock(Invocation.class);
    when(invocation3.isVoid()).thenReturn(false);
    when(invocation3.returnsPrimitive()).thenReturn(true);
    when(invocation3.printMethodReturnType()).thenReturn("int");
    when(invocation3.getMethodName()).thenReturn("getNumber");
    
    Reporter reporter2 = mock(Reporter.class);
    doNothing().when(reporter2).wrongTypeOfReturnValue("int", "null", "getNumber");
    
    Returns answer2 = mock(Returns.class);
    when(answer2.getReturnType()).thenReturn(Integer.TYPE);
    when(answer2.printReturnType()).thenReturn("int");
    when(answer2.returnsNull()).thenReturn(true);
    
    AnswersValidator validator3 = new AnswersValidator();
    validator3.validate(answer2, invocation3);
    
    verify(reporter2).wrongTypeOfReturnValue("int", "null", "getNumber");
  }
  
  @Test
  public void testValidate_Exception() {
    // Typical use case
    Invocation invocation = mock(Invocation.class);
    Throwable throwable = mock(Throwable.class);
    when(throwable instanceof RuntimeException || throwable instanceof Error).thenReturn(false);
    when(invocation.isValidException(throwable)).thenReturn(true);
    
    ThrowsException answer = mock(ThrowsException.class);
    when(answer.getThrowable()).thenReturn(throwable);
    
    AnswersValidator validator = new AnswersValidator();
    validator.validate(answer, invocation);
    
    // Error scenario
    Invocation invocation2 = mock(Invocation.class);
    Throwable throwable2 = null;
    
    Reporter reporter = mock(Reporter.class);
    doNothing().when(reporter).cannotStubWithNullThrowable();
    
    ThrowsException answer2 = mock(ThrowsException.class);
    when(answer2.getThrowable()).thenReturn(throwable2);
    
    AnswersValidator validator2 = new AnswersValidator();
    validator2.validate(answer2, invocation2);
    
    verify(reporter).cannotStubWithNullThrowable();
    
    // Edge case
    Invocation invocation3 = mock(Invocation.class);
    Throwable throwable3 = mock(RuntimeException.class);
    
    ThrowsException answer3 = mock(ThrowsException.class);
    when(answer3.getThrowable()).thenReturn(throwable3);
    
    AnswersValidator validator3 = new AnswersValidator();
    validator3.validate(answer3, invocation3);
    
    verifyZeroInteractions(reporter);
  }
  
}