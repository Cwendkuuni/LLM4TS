import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.MockUtil;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 private MockUtil mockUtil;
 private MockingProgress mockingProgress;
 private Object mock;

 @Before
 public void setup() {
 mockitoCore = new MockitoCore();
 mockUtil = mockitoCore.new MockUtil();
 mockingProgress = mockitoCore.new ThreadSafeMockingProgress();
 }

 @Test
 public void testMockCreation() {
 mock = mockitoCore.mock(String.class, new MockSettingsImpl());
 assertNotNull(mock);
 }

 @Test
 public void testStubbing() {
 mock = mock(String.class, new MockSettingsImpl());
 OngoingStubbing stubbing = mockitoCore.stub(mock);
 stubbing.thenReturn("Hello, World!");
 String result = (String) mockitoCore.verify(mock).toString();
 assertEquals("Hello, World!", result);
 }

 @Test(expected = NullPointerException.class)
 public void testNullPassedToVerify() {
 mockitoCore.verify(null, Mockito.verifyNoMoreInteractions());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotAMockPassedToVerify() {
 mockitoCore.verify(new Object(), Mockito.verifyNoMoreInteractions());
 }

 @Test
 public void testVerification() {
 mock = mock(String.class, new MockSettingsImpl());
 when(mock.toString()).thenReturn("Hello, World!");
 String result = (String) mockitoCore.verify(mock, Mockito.eq("Hello, World!")).toString();
 assertEquals("Hello, World!", result);
 }

 @Test
 public void testReset() {
 mock = mock(String.class, new MockSettingsImpl());
 when(mock.toString()).thenReturn("Hello, World!");
 String result = (String) mockitoCore.verify(mock).toString();
 assertEquals("Hello, World!", result);
 mockitoCore.reset(mock);
 result = (String) mockitoCore.verify(mock).toString();
 assertNull(result);
 }

 @Test(expected = NullPointerException.class)
 public void testNullPassedToVerifyNoMoreInteractions() {
 mockitoCore.verifyNoMoreInteractions(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotAMockPassedToVerifyNoMoreInteractions() {
 mockitoCore.verifyNoMoreInteractions(new Object());
 }

 @Test
 public void testVerifyNoMoreInteractions() {
 mock = mock(String.class, new MockSettingsImpl());
 when(mock.toString()).thenReturn("Hello, World!");
 mockitoCore.verifyNoMoreInteractions(mock);
 }

 @Test
 public void testVerifyNoMoreInteractionsInOrder() {
 mock = mock(String[].class, new MockSettingsImpl());
 String[] mocks = new String[]{"Mock 1", "Mock 2"};
 InOrder inOrder = mockitoCore.inOrder(mocks);
 when(mocks[0].toString()).thenReturn("Mock 1: Hello, World!");
 when(mocks[1].toString()).thenReturn("Mock 2: Hello, Mockito!");
 mockitoCore.verifyNoMoreInteractionsInOrder(Arrays.asList(mocks), inOrder);
 }

 @Test
 public void testAnswer() {
 mock = mock(Counter.class, new MockSettingsImpl());
 Answer<Integer> answer = new Answer<Integer>() {
 @Override
 public Integer answer(InvocationOnMock invocation) throws Throwable {
 return 42;
 }
 };
 Stubber stubber = mockitoCore.doAnswer(answer);
 stubber.doAnswer(answer).when(mock).increment();
 Integer result = (Integer) mockitoCore.verify(mock).increment();
 assertEquals(42, result.intValue());
 }

 private static class Counter {
 public void increment() {
 // empty
 }
 }

 @Test
 public void testStubVoid() {
 mock = mock(Counter.class, new MockSettingsImpl());
 VoidMethodStubbable stubbable = mockitoCore.stubVoid(mock);
 stubbable.thenCallRealMethod();
 mockitoCore.verify(mock).increment();
 }

 @Test(expected = NullPointerException.class)
 public void testNullPassedToInOrder() {
 List<Object> mocks = null;
 InOrder inOrder = mockitoCore.inOrder(mocks);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotAMockPassedToInOrder() {
 Object notAMock = new Object();
 InOrder inOrder = mockitoCore.inOrder(notAMock);
 }

 @Test
 public void testInOrder() {
 mock = mock(String.class, new MockSettingsImpl());
 mockitoCore.when(mock.toString()).thenReturn("Mock 1: Hello, World!");
 mockitoCore.when(mock.toString()).thenReturn("Mock 2: Hello, Mockito!");
 InOrder inOrder = mockitoCore.inOrder(mock);
 }
}