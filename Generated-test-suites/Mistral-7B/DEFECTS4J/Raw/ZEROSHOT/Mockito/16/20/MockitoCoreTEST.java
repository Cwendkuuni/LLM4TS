package org.mockito.internal.tests;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.progress.IOngoingStubbing;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubberImpl;
import org.mockito.internal.verification.api.VerificationMode;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.AnswerWithInfo;
import org.mockito.stubbing.Stub;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.VerificationModeFactory.times;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 private MockSettings mockSettings;
 private IOngoingStubbing ongoingStubbing;
 private MockingProgress mockingProgress;

 @Before
 public void setup() {
 mockitoCore = new MockitoCore();
 mockSettings = new MockSettingsImpl();
 mockingProgress = new ThreadSafeMockingProgress();
 }

 @Test
 public void testMock() {
 // Test mock creation with no arguments
 Mockito.openMocks(this); // initializing open mocks to have the same progress and settings
 Mockito.mock(Mockito.class); // this will not be mocked as it's the class of the test class
 Object mockedMock = mockitoCore.mock(String.class, mockSettings);
 assertEquals(String.class, mockedMock.getClass());

 // Test mock creation with custom settings
 mockSettings.setDeprecatedStubbing(true);
 mockedMock = mockitoCore.mock(String.class, mockSettings);
 assertEquals(String.class, mockedMock.getClass());

 // Test resetting ongoing stubbing
 ongoingStubbing = mockitoCore.stub();
 IOngoingStubbing stubbing = mockitoCore.stub();
 assertEquals(stubbing, ongoingStubbing);
 mockitoCore.resetOngoingStubbing();
 assertNull(ongoingStubbing);
 }

 @Test
 public void testStub() {
 // Test getting ongoing stubbing
 ongoingStubbing = mockitoCore.stub();
 assertSame(ongoingStubbing, mockitoCore.stub());

 // Test stubbing an object
 Stub<String> stringStub = mockitoCore.stub();
 stringStub.returns("test");
 String result = mockitoCore.when(any(String.class)).thenReturn("test").getMock().toString();
 assertEquals("test", result);

 // Test stubbing a method call
 Stub<String> methodStub = mockitoCore.stub();
 methodStub.answers(new Answer<String>() {
 @Override
 public String answer(InvocationOnMock invocation) {
 return "test";
 }
 });
 String result2 = mockitoCore.when(any(String.class)).thenAnswer(methodStub).getMock().toString();
 assertEquals("test", result2);
 }

 @Test
 public void testWhen() {
 // Test when method with no arguments
 when(any(String.class)).thenReturn("test");
 String result = mockitoCore.verify(any(String.class)).toString();
 assertEquals("test", result);

 // Test when method with mock argument
 String mockedMock = mockitoCore.mock(String.class);
 when(mockedMock).thenReturn("test");
 String result3 = mockitoCore.verify(mockedMock).toString();
 assertEquals("test", result3);

 // Test when method with a stubbed method
 String mockedMock2 = mockitoCore.mock(String.class);
 Stub<String> stubbedMethod = mockitoCore.stub();
 when(mockedMock2).thenCallRealMethod();
 when(mockedMock2.toString()).thenReturn(stubbedMethod);
 String result4 = mockitoCore.verify(mockedMock2).toString();
 assertEquals("", result4); // should not call real method as we stubbed it
 }

 @Test
 public void testVerify() {
 // Test verify method with null argument
 mockitoCore.verify(null);

 // Test verify method with empty argument list
 mockitoCore.verify(new Object[0]);

 // Test verify method with a mocked object
 String mockedMock = mockitoCore.mock(String.class);
 mockitoCore.verify(mockedMock);

 // Test verify method with a not a mock object
 String notAMock = new String("test");
 mockitoCore.verify(notAMock);

 // Test verify method with a mocked object and verification mode
 String mockedMock2 = mockitoCore.mock(String.class);
 mockitoCore.verify(mockedMock2, times(2)).toString();
 }

 @Test
 public void testReset() {
 // Test reset method with no arguments
 mockitoCore.reset();

 // Test reset method with mocks
 String mockedMock3 = mockitoCore.mock(String.class);
 String mockedMock4 = mockitoCore.mock(String.class);
 mockitoCore.reset(mockedMock3, mockedMock4);
 assertTrue(mockingDetails(mockedMock3).wasReset());
 assertTrue(mockingDetails(mockedMock4).wasReset());
 }

 @Test
 public void testVerifyNoMoreInteractions() {
 // Test verify no more interactions with null argument
 mockitoCore.verifyNoMoreInteractions(null);

 // Test verify no more interactions with empty argument list
 mockitoCore.verifyNoMoreInteractions(new Object[0]);

 // Test verify no more interactions with a mocked object
 String mockedMock5 = mockitoCore.mock(String.class);
 String mockedMock6 = mockitoCore.mock(String.class);
 mockitoCore.verifyNoMoreInteractions(mockedMock5);
 mockitoCore.verifyNoMoreInteractions(mockedMock6);

 // Test verify no more interactions with a not a mock object
 String notAMock2 = new String("test");
 mockitoCore.verifyNoMoreInteractions(notAMock2);
 }

 @Test
 public void testInOrder() {
 // Test creating an InOrder object
 InOrder inOrder = mockitoCore.inOrder(new String[]{"test1", "test2"});

 // Test verifying that a method was called in the correct order
 String mockedMock7 = mockitoCore.mock(String.class);
 String mockedMock8 = mockitoCore.mock(String.class);
 when(mockedMock7.toString()).thenReturn("test1");
 when(mockedMock8.toString()).thenReturn("test2");
 inOrder.verify(mockedMock7).toString();
 inOrder.verify(mockedMock8).toString();
 }

 @Test
 public void testDoAnswer() {
 // Test doAnswer method with a simple answer
 Answer<String> answer = mockitoCore.stub().returns("test");
 AnswerWithInfo answerWithInfo = mockitoCore.doAnswer(answer);
 String result5 = mockitoCore.when(any(String.class)).then(answerWithInfo).getMock().toString();
 assertEquals("test", result5);

 // Test doAnswer method with an answer that captures an argument
 Answer<String> answer2 = mockitoCore.stub().answers(new Answer<String>() {
 @Override
 public String answer(InvocationOnMock invocation) {
 return invocation.getArgument(0).toString();
 }
 });
 AnswerWithInfo answerWithInfo2 = mockitoCore.doAnswer(answer2);
 String result6 = mockitoCore.when(any(String.class)).then(answerWithInfo2).getMock().toString();
 assertEquals("test", result6); // assuming "test" is the argument passed to when
 }

 @Test(expected = NotAMockException.class)
 public void testNotAMockException() {
 mockitoCore.verify(new String("test")).toString();
 }
}