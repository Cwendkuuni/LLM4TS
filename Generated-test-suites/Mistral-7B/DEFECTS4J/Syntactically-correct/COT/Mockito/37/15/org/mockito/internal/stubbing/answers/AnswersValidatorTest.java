package org.mockito.internal.stubbing.answers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

public class AnswersValidatorTest {

 private AnswersValidator validator;

 @Before
 public void setup() {
 validator = new AnswersValidator();
 }

 @Test
 public void testValidate_ReturnsAnswer() {
 Answer<String> answer = mock(Answer.class);
 InvocationOnMock invocation = mock(InvocationOnMock.class);

 validator.validate(answer, invocation);

 // No exception should be thrown
 }

 @Test(expected = MockitoException.class)
 public void testValidate_ThrowsExceptionAnswer_InvalidThrowable() {
 ThrowsException answer = mock(ThrowsException.class);
 answer.throwsNew(IllegalArgumentException.class);
 InvocationOnMock invocation = mock(InvocationOnMock.class);

 validator.validate(answer, invocation);

 // MockitoException should be thrown
 }

 @Test
 public void testValidate_ReturnsAnswer_ValidReturnValue() {
 Returns answer = mock(Returns.class);
 answer.returns("test");
 InvocationOnMock invocation = mock(InvocationOnMock.class);

 validator.validate(answer, invocation);

 // No exception should be thrown
 }

 @Test(expected = MockitoException.class)
 public void testValidate_ReturnsAnswer_InvalidReturnValue_VoidMethod() {
 Returns answer = mock(Returns.class);
 answer.returns(1);
 InvocationOnMock invocation = mock(InvocationOnMock.class).withMethodType(ArgumentMatchers.isA(Void.class));

 validator.validate(answer, invocation);

 // MockitoException should be thrown
 }

 @Test(expected = MockitoException.class)
 public void testValidate_ReturnsAnswer_InvalidReturnValue_InvalidType() {
 Returns answer = mock(Returns.class);
 answer.returns(new Object());
 InvocationOnMock invocation = mock(InvocationOnMock.class).withMethodType(ArgumentMatchers.isA(String.class));

 validator.validate(answer, invocation);

 // MockitoException should be thrown
 }

 @Test(expected = MockitoException.class)
 public void testValidate_ReturnsAnswer_NullReturnValue_PrimitiveType() {
 Returns answer = mock(Returns.class);
 answer.returns(null);
 InvocationOnMock invocation = mock(InvocationOnMock.class).withMethodType(ArgumentMatchers.isA(int.class));

 validator.validate(answer, invocation);

 // MockitoException should be thrown
 }

 @Test
 public void testValidate_ReturnsAnswer_ValidDoNothing() {
 DoesNothing answer = mock(DoesNothing.class);
 InvocationOnMock invocation = mock(InvocationOnMock.class);

 validator.validate(answer, invocation);

 // No exception should be thrown
 }

 @Test(expected = MockitoException.class)
 public void testValidate_ReturnsAnswer_InvalidDoNothing_NonVoidMethod() {
 DoesNothing answer = mock(DoesNothing.class);
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 invocation.expects(ArgumentMatchers.isA(InvocationOnMock.class)).probesBehavior();

 validator.validate(answer, invocation);

 // MockitoException should be thrown
 }

 @Test
 public void testValidate_ThrowsException_NullAnswer() {
 validator.validate(null, mock(InvocationOnMock.class));

 // No exception should be thrown (as it is not checked in the code)
 }

 @Test
 public void testValidate_ThrowsException_NullInvocation() {
 validator.validate(mock(Answer.class), null);

 // No exception should be thrown (as it is not checked in the code)
 }
}