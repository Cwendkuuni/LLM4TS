import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.MockitoCore;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.verify;
import static org.mockito.BDDMockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 private Object mock;
 private Object mock2;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 mockitoCore = new MockitoCore();
 mock = mockitoCore.mock(Object.class);
 mock2 = mockitoCore.mock(Object.class);
 }

 @Test
 public void testMockCreation() {
 Object returnedMock = mockitoCore.mock(Object.class);
 assert(returnedMock instanceof Object);
 }

 @Test
 public void testStubbing() {
 Object returnValue = new Object();
 when(mock).thenReturn(returnValue);
 Object result = mockitoCore.verify(mock);
 assert(result == mock);
 assert(mockitoCore.getLastInvocation().getMethod().getName().equals("thenReturn"));
 assert(mockitoCore.getLastInvocation().getTarget().equals(mock));
 assert(mockitoCore.getLastInvocation().getArguments()[0].equals(returnValue));
 }

 @Test
 public void testOngoingStubbing() {
 IOngoingStubbing stubbing = mockitoCore.stub();
 stubbing.thenReturn("test");
 Object result = mockitoCore.verify(mock).someMethod();
 assert(result.equals("test"));
 }

 @Test(expected = NullPointerException.class)
 public void testNullPassedToVerify() {
 mockitoCore.verify(null);
 }

 @Test(expected = NotAMockException.class)
 public void testNotAMockPassedToVerify() {
 Object notAMock = new Object();
 mockitoCore.verify(notAMock);
 }

 @Test
 public void testReset() {
 Object returnValue = new Object();
 when(mock).thenReturn(returnValue);
 Object result = mockitoCore.verify(mock).someMethod();
 assert(result.equals(returnValue));
 reset(mock);
 assert(mockitoCore.getLastInvocation() == null);
 }

 @Test
 public void testVerifyNoMoreInteractions() {
 given(mock.someMethod()).willReturn(1);
 verifyNoMoreInteractions(mock);
 }

 @Test(expected = NullPointerException.class)
 public void testNullPassedToVerifyNoMoreInteractions() {
 mockitoCore.verifyNoMoreInteractions(null);
 }

 @Test(expected = NotAMockException.class)
 public void testNotAMockPassedToVerifyNoMoreInteractions() {
 mockitoCore.verifyNoMoreInteractions(new Object());
 }

 @Test
 public void testInOrder() {
 Object mock1 = mockitoCore.mock(Object.class);
 Object mock2 = mockitoCore.mock(Object.class);
 InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
 given(mock1.someMethod()).willReturn(1);
 given(mock2.someMethod()).willReturn(2);
 inOrder.verify(mock1).someMethod();
 inOrder.verify(mock2).someMethod();
 }

 @Test
 public void testAnswer() {
 Object answer = new Object();
 Answer answerAnswer = invocation -> answer;
 when(mock.someMethod()).thenAnswer(answerAnswer);
 Object result = mockitoCore.verify(mock).someMethod();
 assert(result.equals(answer));
 }

 @Test
 public void testVoidStubbing() {
 mockitoCore.stubVoid(mock).someVoidMethod();
 then(mock).someVoidMethod();
 verify(mock).someVoidMethod();
 }

 @Test
 public void testVerifyUsage() {
 mockitoCore.validateMockitoUsage();
 }

 @Test
 public void testNullPassedToVerifyUsage() {
 mockitoCore.validateMockitoUsage(null);
 }

 @Test
 public void testVerifyUsageWithNotAMock() {
 Object notAMock = new Object();
 mockitoCore.validateMockitoUsage(notAMock);
 }

 @Test
 public void testVerifyUsageWithMocks() {
 Object mock1 = mockitoCore.mock(Object.class);
 Object mock2 = mockitoCore.mock(Object.class);
 mockitoCore.validateMockitoUsage(mock1, mock2);
 }

 @Test
 public void testVerifyUsageWithMocksAndNotAMocks() {
 Object mock1 = mockitoCore.mock(Object.class);
 Object notAMock1 = new Object();
 Object notAMock2 = new Object();
 mockitoCore.validateMockitoUsage(mock1, notAMock1, notAMock2);
 }

 @Test
 public void testVerificationMode() {
 Object mock = mockitoCore.mock(Object.class);
 List<Object> mocks = Arrays.asList(mock);
 mockitoCore.verify(mock, Mockito.times(1)).someMethod();
 then(mock).someMethod().thenReturn(1);
 mockitoCore.verify(mock, Mockito.never()).someMethod();
 mockitoCore.verify(mock, Mockito.atLeastOnce()).someMethod();
 mockitoCore.verify(mock, Mockito.atLeast(2)).someMethod();
 mockitoCore.verify(mock, Mockito.atMost(2)).someMethod();
 mockitoCore.verify(mock, Mockito.atMostOnce()).someMethod();
 mockitoCore.verify(mock, Mockito.times(5)).someMethod();
 mockitoCore.verify(mock, Mockito.times(5)).someMethod(); // should be ignored
 mockitoCore.verify(mock, Mockito.times(5)).someMethod(); // should be ignored
 mockitoCore.verify(mock, Mockito.times(1)).someOtherMethod();
 mockitoCore.verify(mock2).someMethod();
 verifyNoMoreInteractions(mocks);
 }

 @Test
 public void testVerifyUsageWithEmptyMocks() {
 mockitoCore.validateMockitoUsage((Object[]) null);
 }

 @Test
 public void testVerifyUsageWithEmptyMocksAndNotAMocks() {
 mockitoCore.validateMockitoUsage((Object[]) null, new Object());
 }

 @Test
 public void testVerifyUsageWithEmptyMocksAndMocks() {
 mockitoCore.validateMockitoUsage((Object[]) null, mockitoCore.mock(Object.class));
 }

 @Test
 public void testVerifyUsageWithMocksAndNotAMocksAndEmptyMocks() {
 mockitoCore.validateMockitoUsage(mockitoCore.mock(Object.class), new Object(), (Object[]) null);
 }

 @Test
 public void testVerifyUsageWithMocksAndNotAMocksAndMocks() {
 mockitoCore.validateMockitoUsage(mockitoCore.mock(Object.class), new Object(), mockitoCore.mock(Object.class));
 }
}