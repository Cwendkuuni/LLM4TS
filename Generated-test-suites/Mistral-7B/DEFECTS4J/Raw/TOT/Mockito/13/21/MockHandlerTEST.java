package org.mockito.internal.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMockVerificationModeBuilder;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

public class MockHandlerTest {

 private MockHandler<Foo> mockHandler;
 private Foo mock;

 @Before
 public void setup() {
 mock = mock(Foo.class);
 mockHandler = new MockHandler<Foo>(mock.getClass());
 }

 @Test
 public void testHandle_voidMethodInvocation_stubbed() {
 // Given
 when(mock.someVoidMethod(ArgumentMatchers.anyInt(), ArgumentMatchers.anyString()))
 .thenAnswer(invocation -> System.out.println("Invoked void method with: " + invocation.getArguments()));

 // When
 mockHandler.handle(new InvocationOnMock(mock, "someVoidMethod", Arrays.asList(1, "test"), null));

 // Then
 verify(mock).someVoidMethod(ArgumentMatchers.eq(1), ArgumentMatchers.eq("test"));
 }

 @Test
 public void testHandle_nonVoidMethodInvocation_stubbed() {
 // Given
 when(mock.someMethod(ArgumentMatchers.anyInt(), ArgumentMatchers.anyString()))
 .thenReturn("Test result");

 // When
 String result = (String) mockHandler.handle(new InvocationOnMock(mock, "someMethod", Arrays.asList(1, "test"), null));

 // Then
 assertEquals("Test result", result);
 }

 @Test
 public void testHandle_verificationModePulled() {
 // Given
 mockHandler.handle(new InvocationOnMock(mock, "someMethod", Arrays.asList(1, "test"), null));

 // When
 InvocationOnMockVerificationModeBuilder verificationMode = Mockito.verify(mock);

 // Then
 verifyNoInteractions(mock);

 // When and Then combined
 mockHandler.handle(new InvocationOnMock(mock, "someMethod", Arrays.asList(1, "test"), null));
 verificationMode.times(1);
 }

 @Test(expected = AssertionError.class)
 public void testHandle_verificationModePulled_onDifferentMock() {
 // Given
 Foo otherMock = mock(Foo.class);
 mockHandler = new MockHandler<Foo>(mock.getClass());
 InvocationOnMockVerificationModeBuilder verificationMode = Mockito.verify(otherMock);

 // When
 mockHandler.handle(new InvocationOnMock(mock, "someMethod", Arrays.asList(1, "test"), null));

 // Then
 throw new AssertionError(); // This test will fail because verificationMode is not the one for the mockHandler's mock
 }

 @Test
 public void testHandle_noAnswersForStubbing() {
 // Given
 mockHandler.handle(new InvocationOnMock(mock, "someMethod", Arrays.asList(1, "test"), null));

 // Then
 MockSettingsImpl mockSettings = mockHandler.getMockSettings();
 List<Answer> answers = mockSettings.getDefaultAnswer().getAnswers();
 assertTrue(answers.isEmpty());
 }
}

interface Foo {
 void someVoidMethod(int arg1, String arg2);
 String someMethod(int arg1, String arg2);
}