package org.mockito.internal.test.mockhandler;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MockHandlerTest {

 private MockHandler<MockitoInvocationHandler> mockHandler;
 private MockSettingsImpl mockSettings;
 private Object target;

 @Before
 public void setup() {
 mockSettings = new MockSettingsImpl();
 mockHandler = new MockHandler<>(mockSettings);
 target = mock(MockitoInvocationHandler.class);
 }

 @Test
 public void testHandle_voidMethod() {
 // given
 doAnswer(invocation -> System.out.println("Stubbed void method")).when(target).voidMethod();

 // when
 mockHandler.handle(new InvocationOnMock(target, "voidMethod"));

 // then
 // No return value in this case, but we can verify that the void method was stubbed
 verify(target).voidMethod();
 }

 @Test
 public void testHandle_answer() {
 // given
 final String answer = "Answer";
 doAnswer(invocation -> answer).when(target).methodWithStringArgument(ArgumentMatchers.eq("Argument"));

 // when
 String result = (String) mockHandler.handle(new InvocationOnMock(target, "methodWithStringArgument", new Object[]{"Argument"}));

 // then
 assertThat(result).isEqualTo(answer);
 }

 @Test
 public void testHandle_verification() {
 // given
 doAnswer(invocation -> {
 // Method under test does not return anything, so we can't check return value here
 }).when(target).methodWithNoReturn();

 // when
 // Perform verification when the method is called
 mockHandler.handle(new InvocationOnMock(target, "methodWithNoReturn"));

 // then
 // Verify that the method was called
 verify(target).methodWithNoReturn();
 }

 @Test
 public void testHandle_multipleAnswers() {
 // given
 List<Answer> answers = new ArrayList<>();
 answers.add(invocation -> "Answer 1");
 answers.add(invocation -> "Answer 2");

 mockHandler.setAnswersForStubbing(answers);

 // when
 String result1 = (String) mockHandler.handle(new InvocationOnMock(target, "methodWithStringArgument", new Object[]{"Argument1"}));
 String result2 = (String) mockHandler.handle(new InvocationOnMock(target, "methodWithStringArgument", new Object[]{"Argument2"}));

 // then
 assertThat(result1).isEqualTo("Answer 1");
 assertThat(result2).isEqualTo("Answer 2");
 }

 @Test(expected = IllegalStateException.class)
 public void testHandle_verificationAndAnswer() {
 // given
 doAnswer(invocation -> {
 // This test case should fail as verification mode and answer cannot be used together
 throw new IllegalStateException("Verification and answer cannot be used together.");
 }).when(target).methodWithStringArgument(ArgumentMatchers.anyString());

 // when & then
 // The test should fail with the expected exception
 }
}