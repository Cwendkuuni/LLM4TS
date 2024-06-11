package org.mockito.internal;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.MockSettings;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.verification.VerificationMode;

public class MockitoCoreTest {

 private Reporter reporter;
 private MockUtil mockUtil;
 private MockingProgress mockingProgress;
 private ThreadSafeMockingProgress threadSafeMockingProgress;
 private OngoingStubbingImpl ongoingStubbing;
 private StubberImpl stubberImpl;
 private OngoingStubbing<Object> ongoingStubbingObject;
 private OngoingStubbing<String> ongoingStubbingString;
 private InOrder inOrder;
 private Object mockObject;
 private Object mockObject2;
 private Object mockObject3;
 private Answer<Void> answer;
 private VoidMethodStubbable<Object> voidMethodStubbable;

 @Before
 public void setUp() {
 reporter = new Reporter();
 mockUtil = new MockUtil();
 mockingProgress = new MockitoCore().new ThreadSafeMockingProgress();
 threadSafeMockingProgress = new ThreadSafeMockingProgress();
 ongoingStubbing = new OngoingStubbingImpl();
 ongoingStubbingObject = mockingProgress.stubbingStarted();
 ongoingStubbingString = mockingProgress.stubbingStarted();
 stubberImpl = new StubberImpl();
 answer = new Answer<Void>() {
 @Override
 public Void answer(InvocationOnMock invocation) throws Throwable {
 return null;
 }
 };
 voidMethodStubbable = mockingProgress.stubbingStarted().voidMethodStubbable(mockObject);
 mockObject = mock(Object.class, new MockSettings());
 mockObject2 = mock(Object.class, new MockSettings());
 mockObject3 = mock(Object.class, new MockSettings());
 }

 @Test
 public void testMock() {
 Object mock = new MockitoCore().mock(Object.class, new MockSettings());
 assertEquals(mockObject, mock);
 }

 @Test
 public void testMockWithSettings() {
 Object mock = new MockitoCore().mock(Object.class, new MockSettings().serializable());
 assertEquals(mockObject, mock);
 }

 @Test
 public void testStub() {
 OngoingStubbing<Object> stubbing = new MockitoCore().stub();
 assertSame(ongoingStubbingObject, stubbing);
 }

 @Test
 public void testStubDeprecated() {
 DeprecatedOngoingStubbing<Object> deprecatedStubbing = new MockitoCore().stub((Object) null);
 assertSame(ongoingStubbingObject, deprecatedStubbing);
 }

 @Test
 public void testWhen() {
 OngoingStubbing<Object> whenStubbing = new MockitoCore().when((Object) null);
 assertSame(ongoingStubbingObject, whenStubbing);
 }

 @Test
 public void testVerify() {
 new MockitoCore().verify(mockObject, VerificationMode.RETURNING);
 }

 @Test(expected = RuntimeException.class)
 public void testVerifyNull() {
 new MockitoCore().verify(null, VerificationMode.RETURNING);
 }

 @Test
 public void testVerifyNotAMock() {
 new MockitoCore().verify(new Object(), VerificationMode.RETURNING);
 }

 @Test
 public void testReset() {
 new MockitoCore().reset(mockObject, mockObject2, mockObject3);
 mockingProgress.reset();
 mockingProgress.resetOngoingStubbing();
 mockUtil.resetMock(mockObject);
 mockUtil.resetMock(mockObject2);
 mockUtil.resetMock(mockObject3);
 }

 @Test
 public void testVerifyNoMoreInteractions() {
 new MockitoCore().verifyNoMoreInteractions(mockObject);
 }

 @Test
 public void testVerifyNoMoreInteractionsWithNulls() {
 new MockitoCore().verifyNoMoreInteractions(mockObject, null);
 }

 @Test
 public void testVerifyNoMoreInteractionsWithEmptyList() {
 List<Object> emptyList = Arrays.asList();
 new MockitoCore().verifyNoMoreInteractions(emptyList.toArray(new Object[emptyList.size()]));
 }

 @Test
 public void testVerifyNoMoreInteractionsInOrder() {
 Object mock1 = mock(Object.class, new MockSettings());
 Object mock2 = mock(Object.class, new MockSettings());
 InOrder inOrder = new MockitoCore().inOrder(mock1, mock2);
 when(mock1.hashCode()).thenReturn(1);
 when(mock1.toString()).thenReturn("mock1");
 when(mock2.hashCode()).thenReturn(2);
 when(mock2.toString()).thenReturn("mock2");
 inOrder.verify(mock1).hashCode();
 inOrder.verify(mock1).toString();
 inOrder.verify(mock2).hashCode();
 inOrder.verify(mock2).toString();
 }

 @Test
 public void testVerifyNoInteractions() {
 verifyNoInteractions(mockObject);
 }

 @Test
 public void testVerifyZeroInteractions() {
 verifyZeroInteractions(mockObject);
 }

 @Test
 public void testInOrder() {
 Object mock1 = mock(Object.class, new MockSettings());
 Object mock2 = mock(Object.class, new MockSettings());
 InOrder inOrder = new MockitoCore().inOrder(mock1, mock2);
 ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
 when(mock1.toString()).thenReturn("mock1");
 when(mock1.hashCode()).thenReturn(1L);
 when(mock2.toString()).thenReturn("mock2");
 when(mock2.hashCode()).thenReturn(2L);
 inOrder.verify(mock1).toString();
 inOrder.verify(mock1).hashCode();
 inOrder.verify(mock2).toString();
 inOrder.verify(mock2).hashCode();
 }

 @Test
 public void testInOrderWithNulls() {
 Object mock1 = mock(Object.class, new MockSettings());
 Object mock2 = mock(Object.class, new MockSettings());
 InOrder inOrder = new MockitoCore().inOrder(mock1, null);
 ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
 when(mock1.toString()).thenReturn("mock1");
 when(mock1.hashCode()).thenReturn(1L);
 inOrder.verify(mock1).toString();
 inOrder.verify(mock1).hashCode();
 }

 @Test
 public void testVerifyOrder() {
 Object mock1 = mock(Object.class, new MockSettings());
 Object mock2 = mock(Object.class, new MockSettings());
 InOrder inOrder = new MockitoCore().inOrder(mock1, mock2);
 ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
 when(mock1.toString()).thenReturn("mock1");
 when(mock1.hashCode()).thenReturn(1L);
 when(mock2.toString()).thenReturn("mock2");
 when(mock2.hashCode()).thenReturn(2L);
 inOrder.verify(mock1).toString();
 inOrder.verify(mock1).hashCode();
 inOrder.verify(mock2).toString();
 inOrder.verify(mock2).hashCode();
 inOrder.verify(mock1, Mockito.eq("mock1")).toString();
 inOrder.verify(mock1, Mockito.eq(1L)).hashCode();
 inOrder.verify(mock2, Mockito.eq("mock2")).toString();
 inOrder.verify(mock2, Mockito.eq(2L)).hashCode();
 }

 @Test
 public void testDoAnswer() {
 Answer<Integer> answer = arg -> arg.hashCode();
 OngoingStubbing<Object> stubbing = new MockitoCore().doAnswer(answer);
 Object mock = mock(Object.class, new MockSettings());
 stubbing.then(mock).hashCode();
 assertEquals(1, stubbing.getInvocation().getArgument(0).hashCode());
 verify(mock).hashCode();
 }

 @Test
 public void testAnswer() {
 Answer<Boolean> answer = arg -> arg.hashCode() % 2 == 0;
 when(mockObject.isEven()).thenAnswer(answer);
 assertTrue(mockObject.isEven().hashCode() % 2 == 0);
 }

 @Test
 public void testAnswerWithArguments() {
 Answer<String> answer = arg -> arg.toString() + "world";
 when(mockObject.someMethod()).thenAnswer(answer);
 assertEquals("mockObjectsomeMethodworld", mockObject.someMethod());
 }

 @Test
 public void testAnswerWithReturnValue() {
 Answer<Integer> answer = new Answer<Integer>() {
 @Override
 public Integer answer(InvocationOnMock invocation) throws Throwable {
 return (Integer) invocation.getArgument(0);
 }
 };
 when(mockObject.getNumber()).thenAnswer(answer);
 assertEquals(1, mockObject.getNumber().hashCode());
 }

 @Test
 public void testAnswerWithArgumentAndReturnValue() {
 Answer<Integer> answer = new Answer<Integer>() {
 @Override
 public Integer answer(InvocationOnMock invocation) throws Throwable {
 Integer arg = (Integer) invocation.getArgument(0);
 return arg + 1;
 }
 };
 when(mockObject.addOne()).thenAnswer(answer);
 assertEquals(2, mockObject.addOne().hashCode());
 }

 @Test
 public void testStubVoid() {
 new MockitoCore().stubVoid(mockObject);
 voidMethodStubbable.stub();
 }

 @Test
 public void testVerifyMockitoUsage() {
 new MockitoCore().validateMockitoUsage();
 }

 @Test
 public void testGetLastInvocation() {
 Invocation invocation = new Object() {
 @Override
 public String toString() {
 return "invocation";
 }
 };
 when(mockObject.toString()).thenReturn(invocation);
 OngoingStubbing<Object> stubbing = new MockitoCore().when(mockObject).hashCode();
 assertEquals(invocation, new MockitoCore().getLastInvocation());
 }

}