import org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.List;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 private Object mock;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 mockitoCore = new MockitoCore();
 }

 @Test
 public void testMock() {
 Class<Object> classToMock = Object.class;
 MockSettings mockSettings = MockSettings.none();
 Object returnedMock = mockitoCore.mock(classToMock, mockSettings, false);
 assertTrue(returnedMock instanceof Object);
 }

 @Test
 public void testMock_withSettings() {
 Class<Object> classToMock = Object.class;
 MockSettings mockSettings = MockSettings.builder().build();
 Object returnedMock = mockitoCore.mock(classToMock, mockSettings, false);
 assertTrue(returnedMock instanceof Object);
 }

 @Test
 public void testStub() {
 Object mockCall = Mockito.mock(Object.class);
 IOngoingStubbing stub = mockitoCore.stub();
 stub.thenReturn(true);
 Object result = mockitoCore.when(mockCall).thenReturn(true).getMock();
 assertEquals(true, mockitoCore.verify(result).get(0).getValue());
 }

 @Test
 public void testWhen() {
 Object mockCall = Mockito.mock(Object.class);
 Object expectedValue = "test";
 OngoingStubbing<Object> whenResult = mockitoCore.when(mockCall);
 whenResult.thenReturn(expectedValue);
 Object result = mockitoCore.when(mockCall).thenReturn(expectedValue).getMock();
 assertEquals(expectedValue, mockitoCore.verify(result).get(0).getValue());
 }

 @Test
 public void testVerify() {
 Object mock = Mockito.mock(Object.class);
 mockitoCore.verify(mock, Mockito.any());
 }

 @Test(expected = IllegalStateException.class)
 public void testVerify_nullMock() {
 Object nullMock = null;
 mockitoCore.verify(nullMock, Mockito.any());
 }

 @Test(expected = AssertionError.class)
 public void testVerify_notAMock() {
 Object notAMock = new Object() {};
 mockitoCore.verify(notAMock, Mockito.any());
 }

 @Test
 public void testReset() {
 Object mock1 = Mockito.mock(Object.class);
 Object mock2 = Mockito.mock(Object.class);
 mockitoCore.reset(mock1, mock2);
 Mockito.verifyNoMoreInteractions(mock1, mock2);
 }

 @Test
 public void testVerifyNoMoreInteractions() {
 Object mock = Mockito.mock(Object.class);
 Object mock2 = Mockito.mock(Object.class);
 mockitoCore.verifyNoMoreInteractions(mock, mock2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testVerifyNoMoreInteractions_empty() {
 mockitoCore.verifyNoMoreInteractions();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testVerifyNoMoreInteractions_null() {
 Object[] nullMocks = new Object[] { null };
 mockitoCore.verifyNoMoreInteractions(nullMocks);
 }

 @Test
 public void testVerifyNoMoreInteractions_notAMock() {
 Object notAMock = new Object() {};
 mockitoCore.verifyNoMoreInteractions(notAMock);
 }

 @Test
 public void testInOrder() {
 Object mock1 = Mockito.mock(Object.class);
 Object mock2 = Mockito.mock(Object.class);
 InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
 }

 @Test
 public void testDoAnswer() {
 Object mock = Mockito.mock(Object.class);
 Answer<Integer> answer = invocation -> 42;
 Stubber stub = mockitoCore.doAnswer(answer);
 stub.doAnswer(answer).thenReturn(42).getMock();
 assertEquals(42, mockitoCore.verify(mock).get(0).getValue());
 }

 @Test
 public void testStubVoid() {
 Object mock = Mockito.mock(Object.class);
 Stubber stub = mockitoCore.stubVoid(mock);
 stub.then(new Answer<Void>() {
 @Override public Void answer(InvocationOnMock invocation) throws Throwable {
 throw new RuntimeException("Test Exception");
 }
 });
 try {
 mockitoCore.verify(mock).get(0).getValue();
 fail();
 } catch (AssertionError e) {
 // expected
 }
 }

 @Test
 public void testValidateMockitoUsage() {
 mockitoCore.validateMockitoUsage();
 }

 @Test(expected = IllegalStateException.class)
 public void testGetLastInvocation_noInvocation() {
 mockitoCore.validateMockitoUsage();
 mockitoCore.getLastInvocation();
 }
}