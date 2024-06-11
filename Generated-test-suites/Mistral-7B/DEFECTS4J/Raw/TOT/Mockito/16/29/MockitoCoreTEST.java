package org.mockito.internal.tests;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class MockitoCoreTest {

 private Reporter reporter;
 private MockUtil mockUtil;
 private MockingProgress mockingProgress;
 private IOngoingStubbing stubbing;
 private Object mock;
 private List<Invocation> invocations;

 @Before
 public void setup() {
 reporter = MockitoCore.newReporter();
 mockUtil = MockitoCore.newMockUtil();
 mockingProgress = MockitoCore.newThreadSafeMockingProgress();
 }

 @Test
 public void testMockCreation() {
 mock = Mockito.mock(String.class);
 verify(mockUtil).createMock(String.class, (MockSettingsImpl) Mockito.mockSettings());
 }

 @Test
 public void testMockReset() {
 mock = Mockito.mock(String.class);
 when(mock).thenReturn("test");
 verify(mockUtil).getMockHandler(mock).reset();
 verify(mock).toString(); // should return a default string representation
 }

 @Test
 public void testStubbing() {
 mock = Mockito.mock(String.class);
 stubbing = Mockito.stub(mock);
 stubbing.thenReturn("test");
 verify(mockUtil).getMockHandler(mock).stub(ArgumentMatchers.any()).thenReturn("test");
 }

 @Test
 public void testVerify() {
 mock = Mockito.mock(String.class);
 when(mock).thenReturn("test");
 verify(mock);
 }

 @Test(expected = NullPointerException.class)
 public void testVerifyNullMock() {
 verify(null);
 }

 @Test(expected = org.mockito.exceptions.misusing.NotAMockException.class)
 public void testVerifyNotAMock() {
 new Object(); // create a new object to mock it at runtime
 verify(new Object());
 }

 @Test
 public void testVerifyNoMoreInteractions() {
 mock = Mockito.mock(String.class);
 when(mock).thenReturn("test");
 verifyNoMoreInteractions(mock);
 }

 @Test(expected = NullPointerException.class)
 public void testVerifyNoMoreInteractionsNullMock() {
 verifyNoMoreInteractions(null);
 }

 @Test(expected = org.mockito.exceptions.misusing.NotAMockException.class)
 public void testVerifyNoMoreInteractionsNotAMock() {
 new Object(); // create a new object to mock it at runtime
 verifyNoMoreInteractions(new Object());
 }

 @Test
 public void testInOrder() {
 mock = Mockito.mock(String.class);
 String order = "test";
 InOrder inOrder = Mockito.inOrder(mock);
 when(mock.toString()).thenReturn(order);
 inOrder.verify(mock).toString();
 }

 @Test
 public void testAnswer() {
 mock = Mockito.mock(String.class);
 Answer<String> answer = invocation -> "test";
 Stubber answerStubber = Mockito.doAnswer(answer);
 when(mock).then(answerStubber);
 verify(mock).toString();
 }

 @Test
 public void testVoidMethodStubbable() {
 mock = Mockito.mock(String.class);
 Mockito.doNothing().when(mock);
 mock.toString();
 }

 @Test
 public void testGetLastInvocation() {
 mock = Mockito.mock(String.class);
 Invocation invocation = Mockito.doReturn("test").when(mock).toString();
 Invocation lastInvocation = MockitoCore.getLastInvocation();
 Mockito.verify(lastInvocation).getTarget();
 Mockito.verify(lastInvocation).getMethod();
 Mockito.verify(lastInvocation).getArguments();
 }

 @Test
 public void testValidateMockitoUsage() {
 MockitoCore.validateMockitoUsage();
 }

 @Test
 public void testResetMocks() {
 mock = Mockito.mock(String.class);
 mock = Mockito.mock(String.class);
 mock = Mockito.mock(String.class);
 List<Object> mocks = Arrays.asList(mock, mock, mock);
 MockitoCore.reset(mocks.toArray(new Object[0]));
 verify(mockUtil).resetMock(mock);
 verify(mockUtil).resetMock(mock);
 verify(mockUtil).resetMock(mock);
 }

 @Test
 public void testVerifyNoInteractions() {
 mock = Mockito.mock(String.class);
 verifyNoMoreInteractions(mock);
 }

 @Test
 public void testStubbingWithAnswer() {
 mock = Mockito.mock(String.class);
 Answer<String> answer = invocation -> "test";
 stub(mock).thenAnswer(answer);
 verify(mock).toString();
 }

 @Test
 public void testStubbingWithAnswerAndArgumentMatchers() {
 mock = Mockito.mock(String.class);
 Answer<String> answer = invocation -> "test " + invocation.getArgument(0);
 stub(mock).thenAnswer(answer);
 verify(mock).toString("test");
 }

 @Test
 public void testStubbingWithAnswerAndReturnValue() {
 mock = Mockito.mock(String.class);
 Answer<String> answer = invocation -> "test";
 stub(mock).thenAnswer(answer).thenReturn("otherTest");
 String result = mock.toString();
 verify(mock).toString();
 verify(mock).toString(); // should be the same as the previous call
 assertEquals("otherTest", result);
 }

 @Test
 public void testStubbingWithAnswerAndMultipleArguments() {
 mock = Mockito.mock(String[].class);
 Answer<String> answer = invocation -> "test " + Arrays.toString((String[]) invocation.getArgument(0));
 stub(mock).thenAnswer(answer);
 String[] arguments = new String[]{"arg1", "arg2"};
 String result = mock.toString(arguments);
 verify(mock).toString(arguments);
 assertEquals("test [arg1, arg2]", result);
 }

 @Test
 public void testStubbingWithAnswerAndReturnValueAndArgumentMatchers() {
 mock = Mockito.mock(String.class);
 Answer<String> answer = invocation -> "test " + invocation.getArgument(0).toString();
 stub(mock).thenAnswer(answer).thenReturn("otherTest");
 String argument = new String("arg");
 String result = mock.toString(argument);
 verify(mock).toString(argument);
 assertEquals("otherTest", result);
 }

 @Test
 public void testStubbingWithAnswerAndArgumentMatchersAndArgumentCapture() {
 mock = Mockito.mock(String.class);
 Answer<String> answer = invocation -> invocation.getArgument(0).toCharArray()[0] + "test";
 stub(mock).thenAnswer(answer).thenReturn("otherTest");
 String argument = "arg";
 String result = mock.toString(argument);
 char firstCharacter = argument.charAt(0);
 verify(mock).toString(ArgumentMatchers.eq(firstCharacter + "arg"));
 assertEquals("otherTest", result);
 }

 @Test
 public void testStubbingWithAnswerAndArgumentMatcher() {
 mock = Mockito.mock(String.class);
 Answer<String> answer = invocation -> "test " + invocation.getArgument(0);
 stub(mock).thenAnswer(answer).when(Mockito.argThat(s -> s.startsWith("arg")));
 String argument = "argTest";
 String result = mock.toString(argument);
 verify(mock).toString(argument);
 assertEquals("test argTest", result);
 }

 @Test
 public void testStubbingWithAnswerAndArgumentMatcherAndReturnValue() {
 mock = Mockito.mock(String.class);
 Answer<String> answer = invocation -> "test " + invocation.getArgument(0).toCharArray()[0] + "test";
 stub(mock).thenAnswer(answer).when(Mockito.argThat(s -> s.startsWith("arg"))).thenReturn("otherTest");
 String argument = "argTest";
 String result = mock.toString(argument);
 char firstCharacter = argument.charAt(0);
 verify(mock).toString(ArgumentMatchers.eq(firstCharacter + "argTest"));
 verify(mock, Mockito.times(2)).toString(ArgumentMatchers.eq(firstCharacter + "argTest"));
 assertEquals("otherTest", result);
 }

 @Test
 public void testStubbingWithAnswerAndArgumentMatcherAndReturnValueAndArgumentCapture() {
 mock = Mockito.mock(String.class);
 Answer<String> answer = invocation -> invocation.getArgument(0).toCharArray()[0] + "test";
 stub(mock).thenAnswer(answer).when(Mockito.argThat(s -> s.startsWith("arg"))).thenReturn("otherTest");
 String argument = "argTest";
 String result = mock.toString(argument);
 char firstCharacter = argument.charAt(0);
 verify(mock).toString(ArgumentMatchers.eq(firstCharacter + "argTest"));
 assertEquals("otherTest", result);
 }

 @Test
 public void testStubbingWithAnswerAndArgumentMatcherAndReturnValueAndReturnValueCapture() {
 mock = Mockito.mock(String.class);
 Answer<String> answer = invocation -> invocation.getArgument(0).toCharArray()[0] + "test";
 Answer<String> returnAnswer = invocation -> "otherTest";
 stub(mock).thenAnswer(answer).when(Mockito.argThat(s -> s.startsWith("arg"))).thenAnswer(returnAnswer);
 String argument = "argTest";
 String result = mock.toString(argument);
 char firstCharacter = argument.charAt(0);
 verify(mock).toString(ArgumentMatchers.eq(firstCharacter + "argTest"));
 verify(mock, Mockito.times(2)).toString(ArgumentMatchers.eq(firstCharacter + "argTest"));
 assertEquals("otherTest", result);
 }

 @Test
 public void testStubbingWithAnswerAndArgumentMatcherAndArgumentCaptureAndReturnValueCapture() {
 mock = Mockito.mock(String.class);
 Answer<String> answer = invocation -> invocation.getArgument(0).toCharArray()[0] + "test";
 Answer<String> returnAnswer = invocation -> "otherTest";
 stub(mock).thenAnswer(answer).when(Mockito.argThat(s -> s.startsWith("arg"))).thenAnswer(returnAnswer);
 String argument = "argTest";
 String result = mock.toString(argument);
 char firstCharacter = argument.charAt(0);
 verify(mock).toString(ArgumentMatchers.eq(firstCharacter + "argTest"));
 verify(mock).toString(ArgumentMatchers.eq(firstCharacter + "argTest"));
 assertEquals("otherTest", result);
 }
}