import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.List;
import java.util.concurrent.Callable;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 private Object mockObject;
 private Object mockObject2;
 private Object mockList;

 @Before
 public void setup() {
 mockitoCore = new MockitoCore();
 mockObject = new Object() {};
 mockObject2 = new Object() {};
 mockList = new Object[]{mockObject, mockObject2};
 }

 @Test
 public void testMockCreation() {
 T mock = mockitoCore.mock(Object.class);
 assertNotNull(mock);
 verify(mockitoCore).mock(ArgumentMatchers.eq(Object.class), any(MockSettings.class), anyBoolean());
 }

 @Test
 public void testMockCreationWithSettings() {
 MockSettings settings = new MockSettingsImpl();
 T mock = mockitoCore.mock(Object.class, settings, true);
 assertNotNull(mock);
 verify(mockitoCore).mock(ArgumentMatchers.eq(Object.class), ArgumentMatchers.eq(settings), ArgumentMatchers.eq(true));
 }

 @Test
 public void testStubbing() {
 OngoingStubbing stubbing = mockitoCore.when(mockObject).thenReturn("testValue");
 assertEquals("testValue", mockitoCore.verify(mockObject).someMethod());
 verify(mockObject).someMethod();
 }

 @Test
 public void testStubbingVoidMethod() {
 Stubber stubber = mockitoCore.doAnswer(new Answer() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return null;
 }
 }).when(mockObject).someVoidMethod();
 stubber.doCall();
 verify(mockObject).someVoidMethod();
 }

 @Test(expected = NotAMockException.class)
 public void testVerifyNullObject() {
 mockitoCore.verify(null);
 }

 @Test(expected = NotAMockException.class)
 public void testVerifyNotAMock() {
 Object notAMock = new Object() {};
 mockitoCore.verify(notAMock);
 }

 @Test
 public void testVerifyNoMoreInteractions() {
 mockitoCore.verifyNoMoreInteractions(mockList);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testVerifyNoMoreInteractionsNull() {
 mockitoCore.verifyNoMoreInteractions(null);
 }

 @Test
 public void testInOrder() {
 InOrder inOrder = mockitoCore.inOrder(mockObject, mockObject2);
 verify(mockObject).someMethod();
 verify(mockObject2).someMethod();
 inOrder.verify(mockObject).someMethod();
 inOrder.verify(mockObject2).someMethod();
 }

 @Test(expected = NullPointerException.class)
 public void testInOrderNull() {
 mockitoCore.inOrder(null);
 }

 @Test(expected = NotAMockException.class)
 public void testInOrderNotAMock() {
 Object notAMock = new Object() {};
 mockitoCore.inOrder(notAMock);
 }

 @Test
 public void testVerifyException() {
 try {
 mockitoCore.verify(mockObject).someExceptionMethod();
 fail();
 } catch (AssertionError e) {
 // expected
 }
 }

 @Test
 public void testVerifyExceptionWithMessage() {
 try {
 mockitoCore.verify(mockObject).someExceptionMethodWithMessage("testMessage");
 fail();
 } catch (AssertionError e) {
 assertEquals("testMessage", e.getMessage());
 }
 }

 @Test
 public void testVerifyNoInteractions() {
 mockitoCore.verifyNoInteractions(mockObject);
 }

 @Test
 public void testResetMocks() {
 mockitoCore.reset(mockObject, mockObject2);
 verify(mockObject).someMethod();
 verifyZeroInteractions(mockObject2);
 }

 @Test
 public void testStubbingWithAnswer() {
 final List<Integer> ints = new java.util.ArrayList<>();
 Answer<Callable<Integer>> answer = new Answer<Callable<Integer>>() {
 @Override
 public Callable<Integer> answer(InvocationOnMock invocation) throws Throwable {
 ints.add((Integer) invocation.getArgumentAt(0, 0));
 return new Callable<Integer>() {
 @Override
 public Integer call() throws Exception {
 return 42;
 }
 };
 }
 };
 mockitoCore.stub(mockObject).someMethod(Arg.anyInt()).thenAnswer(answer);
 assertEquals(42, mockitoCore.verify(mockObject).someMethod(1));
 assertEquals(1, ints.size());
 assertEquals(1, ints.get(0).intValue());
 }
}