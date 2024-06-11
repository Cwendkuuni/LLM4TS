package org.mockito.internal.stubbing.answers;

import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;

import static org.mockito.internal.stubbing.answers.AnswersValidator.reporter;
import static org.mockito.internal.stubbing.answers.AnswersValidator.validateDoNothing;
import static org.mockito.internal.stubbing.answers.AnswersValidator.validateException;
import static org.mockito.internal.stubbing.answers.AnswersValidator.validateReturnValue;
import static org.mockito.internal.util.reflection.Whitebox.*;
import static org.mockito.internal.util.reflection.Whitebox.setInternalValue;

public class AnswersValidatorTest {

 Reporter reporterSpy = spy(Reporter.class);
 Invocation invocation = mock(Invocation.class);
 ThrowsException throwsAnswer = new ThrowsException(new RuntimeException("Test Error"));
 Returns returnAnswer = new Returns("returned value");
 DoesNothing doNothingAnswer = new DoesNothing();

 @Test
 public void testValidateDoNothing() {
     setReturnValue(AnswersValidator.class, "reporter", reporterSpy);
     AnswersValidator validator = new AnswersValidator();
     validateDoNothing(doNothingAnswer, invocation);
     verify(reporterSpy, never()).onlyVoidMethodsCanBeSetToDoNothing();

     Invocation voidInvocation = createVoidInvocation();
     setReturnValue(AnswersValidator.class, "reporter", reporterSpy);
     validator.validate(doNothingAnswer, voidInvocation);
     verify(reporterSpy).onlyVoidMethodsCanBeSetToDoNothing();
 }

 @Test
 public void testValidateReturnValueForNullReturnAndPrimitiveInvocation() {
     setReturnValue(AnswersValidator.class, "reporter", reporterSpy);
     AnswersValidator validator = new AnswersValidator();
     validateReturnValue(returnAnswer, invocation);
     verify(reporterSpy, never()).wrongTypeOfReturnValue(anyString(), "null", anyString());

     Invocation primitiveInvocation = createPrimitiveInvocation();
     setReturnValue(AnswersValidator.class, "reporter", reporterSpy);
     validator.validate(returnAnswer, primitiveInvocation);
     verify(reporterSpy).wrongTypeOfReturnValue(eq("java.lang.String"), "null", anyString());
 }

 @Test
 public void testValidateReturnValueForNonNullReturnAndValidInvocationType() {
     setReturnValue(AnswersValidator.class, "reporter", reporterSpy);
     AnswersValidator validator = new AnswersValidator();
     validateReturnValue(returnAnswer, invocation);
     verify(reporterSpy, never()).wrongTypeOfReturnValue(anyString(), anyString(), anyString());

     Invocation objectInvocation = createObjectInvocation();
     setReturnValue(AnswersValidator.class, "reporter", reporterSpy);
     validator.validate(returnAnswer, objectInvocation);
     verify(reporterSpy, never()).wrongTypeOfReturnValue(anyString(), anyString(), anyString());
 }

 @Test
 public void testValidateReturnValueForNonNullReturnAndInvalidInvocationType() {
     setReturnValue(AnswersValidator.class, "reporter", reporterSpy);
     AnswersValidator validator = new AnswersValidator();
     validateReturnValue(returnAnswer, invocation);
     verify(reporterSpy, never()).wrongTypeOfReturnValue(anyString(), anyString(), anyString());

     Invocation primitiveInvocation = createPrimitiveInvocation();
     setReturnValue(AnswersValidator.class, "reporter", reporterSpy);
     validator.validate(returnAnswer, primitiveInvocation);
     verify(reporterSpy).wrongTypeOfReturnValue(eq("java.lang.String"), "java.lang.String", anyString());
 }

 @Test
 public void testValidateExceptionForNullThrowable() {
     setReturnValue(AnswersValidator.class, "reporter", reporterSpy);
     AnswersValidator validator = new AnswersValidator();
     validateException(throwsAnswer, invocation);
     verify(reporterSpy, never()).cannotStubWithNullThrowable();

     Invocation voidInvocation = createVoidInvocation();
     setReturnValue(AnswersValidator.class, "reporter", reporterSpy);
     validator.validate(throwsAnswer, voidInvocation);
     verify(reporterSpy).cannotStubWithNullThrowable();
 }

 @Test
 public void testValidateExceptionForRuntimeExceptionOrError() {
     setReturnValue(AnswersValidator.class, "reporter", reporterSpy);
     AnswersValidator validator = new AnswersValidator();
     validateException(throwsAnswer, invocation);
     verify(reporterSpy).onlyRuntimeExceptionsAndErrorsCanBeThrown();
     verifyNoMoreInteractions(reporterSpy);

     validateException(throwsAnswer, invocation);
     verifyNoMoreInteractions(reporterSpy);
 }

 @Test
 public void testValidateExceptionForInvalidThrowableType() {
     setReturnValue(AnswersValidator.class, "reporter", reporterSpy);
     AnswersValidator validator = new AnswersValidator();
     validateException(throwsAnswer, invocation);
     verify(reporterSpy, never()).checkedExceptionInvalid(any(Throwable.class));

     Invocation invalidInvocation = createInvalidInvocation();
     setReturnValue(AnswersValidator.class, "reporter", reporterSpy);
     validator.validate(throwsAnswer, invalidInvocation);
     verify(reporterSpy).checkedExceptionInvalid(eq(new ClassCastException("Test ClassCastException")));
 }

 private Invocation createVoidInvocation() {
     return mock(Invocation.class);
 }

 private Invocation createPrimitiveInvocation() {
     return new Invocation("java.lang.String", "testMethod", new Object[0]) {
         @Override
         public Object getReturnValue() {
             return null;
         }

         @Override
         public boolean isVoid() {
             return true;
         }
     };
 }

 private Invocation createObjectInvocation() {
     return new Invocation("java.lang.String", "testMethod", new Object[0]) {
         @Override
         public Object getReturnValue() {
             return new Object();
         }

         @Override
         public boolean isVoid() {
             return false;
         }
     };
 }

 private Invocation createInvalidInvocation() {
     return new Invocation("java.lang.String", "testMethod", new Object[0]) {
         @Override
         public Object getReturnValue() {
             return null;
         }

         @Override
         public boolean isVoid() {
             return false;
         }

         @Override
         public Class<?>[] getParameterTypes() {
             return new Class[0];
         }
     };
 }
}