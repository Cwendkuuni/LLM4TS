import org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.MockUtil;
import org.mockito.internal.stubbing.Answer;
import org.mockito.internal.stubbing.OngoingStubbing;
import org.mockito.internal.verification.api.VerificationMode;
import org.mockito.stubbing.AnswerStub;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 private Object mock;
 private Object stub;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 mockitoCore = new MockitoCore();
 }

 @Test
 public void testMockWithNullClass() {
 assertThrows(NullPointerException.class, () -> mockitoCore.mock(null, new MockSettingsImpl(), false));
 }

 @Test
 public void testMockWithNullMockSettings() {
 assertThrows(NullPointerException.class, () -> mockitoCore.mock(Object.class, null, false));
 }

 @Test
 public void testMockWithValidArguments() {
 mock = mockitoCore.mock(String.class, new MockSettingsImpl(), false);
 assertNotNull(mock);
 }

 @Test
 public void testStubWithNullArgument() {
 assertThrows(NullPointerException.class, () -> mockitoCore.stub((Object) null));
 }

 @Test
 public void testStubWithValidArgument() {
 stub = mockitoCore.stub();
 assertNotNull(stub);
 }

 @Test
 public void testWhenWithNullArgument() {
 assertThrows(NullPointerException.class, () -> mockitoCore.when((Object) null));
 }

 @Test
 public void testWhenWithValidArgument() {
 mock = mockitoCore.mock(String.class, new MockSettingsImpl(), false);
 OngoingStubbing<String> ongoingStubbing = mockitoCore.when(mock);
 ongoingStubbing.thenReturn("test");
 assertEquals("test", mockitoCore.verify(mock).toString());
 }

 @Test(expected = NotAMockException.class)
 public void testVerifyWithNullArgument() {
 mockitoCore.verify(null);
 }

 @Test(expected = NotAMockException.class)
 public void testVerifyWithNonMockObject() {
 Object nonMockObject = new Object();
 mockitoCore.verify(nonMockObject);
 }

 @Test
 public void testResetWithNullArgument() {
 mockitoCore.reset(null);
 }

 @Test
 public void testResetWithEmptyArray() {
 mockitoCore.reset();
 }

 @Test
 public void testResetWithValidArguments() {
 List<Object> mocks = Arrays.asList(mock, new Object());
 mockitoCore.reset(mocks.toArray(new Object[0]));
 MockUtil.verifyReset(mock);
 MockUtil.verifyReset(mockitoCore.mock(Object.class, new MockSettingsImpl(), false));
 }

 @Test
 public void testVerifyNoMoreInteractionsWithNullArgument() {
 mockitoCore.verifyNoMoreInteractions(null);
 }

 @Test
 public void testVerifyNoMoreInteractionsWithEmptyArray() {
 mockitoCore.verifyNoMoreInteractions();
 }

 @Test
 public void testVerifyNoMoreInteractionsWithValidArguments() {
 mock = mockitoCore.mock(String.class, new MockSettingsImpl(), false);
 mockitoCore.verify(mock).toString();
 mockitoCore.verifyNoMoreInteractions(mock);
 }

 @Test
 public void testVerifyNoMoreInteractionsWithNonMockObject() {
 Object nonMockObject = new Object();
 mockitoCore.verifyNoMoreInteractions(nonMockObject);
 }

 @Test
 public void testInOrderWithNullArgument() {
 assertThrows(NullPointerException.class, () -> mockitoCore.inOrder((Object) null));
 }

 @Test
 public void testInOrderWithEmptyArray() {
 InOrder inOrder = mockitoCore.inOrder();
 inOrder.verify(mock).toString();
 inOrder.verify(mockitoCore.mock(Object.class, new MockSettingsImpl(), false)).toString();
 }

 @Test
 public void testStubVoidWithNullMock() {
 assertThrows(NullPointerException.class, () -> mockitoCore.stubVoid(null));
 }

 @Test
 public void testStubVoidWithValidMock() {
 mock = mockitoCore.mock(String.class, new MockSettingsImpl(), false);
 mockitoCore.stubVoid(mock).andReturn("test");
 assertEquals("test", mockitoCore.verify(mock).toString());
 }

 @Test
 public void testValidateMockitoUsage() {
 mockitoCore.validateMockitoUsage();
 }

 @Test
 public void testGetLastInvocationWithNullArgument() {
 assertThrows(NullPointerException.class, () -> mockitoCore.getLastInvocation());
 }

 @Test
 public void testGetLastInvocationWithNoInvocation() {
 mockitoCore = new MockitoCore();
 mockitoCore.stub();
 assertNull(mockitoCore.getLastInvocation());
 }

 @Test
 public void testGetLastInvocationWithInvocation() {
 mock = mockitoCore.mock(String.class, new MockSettingsImpl(), false);
 OngoingStubbing<String> ongoingStubbing = mockitoCore.when(mock);
 ongoingStubbing.thenReturn("test");
 assertEquals("test", mockitoCore.getLastInvocation().getMethod().getName());
 }

 @Test
 public void testAnswerStubbing() {
 Answer answer = ArgumentMatchers.anyString().answers(new AnswerStub() {
 @Override public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
 return "test";
 }
 });
 mock = mockitoCore.mock(String.class, new MockSettingsImpl(), false);
 OngoingStubbing<String> ongoingStubbing = mockitoCore.when(mock);
 ongoingStubbing.thenAnswer(answer);
 assertEquals("test", mockitoCore.verify(mock).toString());
 }
}