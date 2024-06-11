package org.mockito.internal.test.MockHandlerTest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.MockSettingsImpl;
import org.mockito.internal.MockitoInvocationHandler;
import org.mockito.internal.MockitoInvocationHandlerMock;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.Answer;
import org.mockito.internal.stubbing.Stub;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.stubbing.VoidMethodStubbable;
import org.mockito.internal.verification.VerificationMode;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MockHandlerTest {

 private MockitoInvocationHandler handler;
 private MockSettingsImpl mockSettings;
 private ThreadSafeMockingProgress mockingProgress;
 private InvocationContainer invocationContainer;
 private VoidMethodStubbable<Object> voidMethodStubbable;

 @Before
 public void setUp() {
 mockSettings = new MockSettingsImpl();
 mockingProgress = new ThreadSafeMockingProgress();
 invocationContainer = new InvocationContainerImpl(mockingProgress);
 handler = new MockHandler<>(mockSettings);
 }

 @Test
 public void testHandle_InvocationWithStubbedMethod_ReturnsNull() throws Throwable {
 Object mock = mock(Object.class);
 when(mock.someMethod(anyInt())).thenReturn("stubbed value");

 StubbedInvocationMatcher invocationMatcher = new StubbedInvocationMatcher(anyInt());
 Stub stub = handler.stub(mock, invocationMatcher, "returnValue");

 Object result = handler.handle(new InvocationOnMock(mock, "someMethod", new Object[]{1}, new Class[]{int.class}));

 verify(mock).someMethod(1);

 Assert.assertEquals("stubbed value", result);
 }

 @Test
 public void testHandle_InvocationWithVerificationMode_DoesVerification() throws Throwable {
 Object mock = mock(Object.class);
 Object[] arguments = new Object[]{1, 2, 3};

 MockitoInvocationHandlerMock handlerMock = new MockitoInvocationHandlerMock();
 handlerMock.setVerificationMode(VerificationMode.RETURN_VALUE);
 handlerMock.setInvocation(new InvocationOnMock(mock, "someMethod", arguments, new Class[]{int[].class}));
 handlerMock.setVerificationData(new VerificationDataImpl(new ArrayList<>(), new StubbedInvocationMatcher(anyInt())));

 handler.handle(handlerMock.getInvocation());

 verify(mock).someMethod(arguments);
 }

 @Test
 public void testHandle_InvocationWithoutStubbingOrVerification_ReturnsDefaultAnswer() throws Throwable {
 Object mock = mock(Object.class);
 Object result = handler.handle(new InvocationOnMock(mock, "someMethod", new Object[]{1}, new Class[]{int.class}));

 Assert.assertNull(result);
 }

 @Test
 public void testVoidMethodStubbable() {
 voidMethodStubbable = handler.voidMethodStubbable(mock(Object.class));
 }

 @Test
 public void testVoidMethodStubbing_InvocationWithStubbing_ReturnsNull() throws Throwable {
 Object mock = mock(Object.class);

 voidMethodStubbable.stubVoid(mock, anyInt());

 handler.handle(new InvocationOnMock(mock, "someMethod", new Object[]{1}, new Class[]{int.class}));

 verify(mock).someMethod(1);
 }

 @Test(expected = IllegalStateException.class)
 public void testHandle_InvocationWithMultipleStubs_ThrowsException() throws Throwable {
 Object mock = mock(Object.class);

 StubbedInvocationMatcher invocationMatcher1 = new StubbedInvocationMatcher(anyInt());
 Stub stub1 = handler.stub(mock, invocationMatcher1, "returnValue1");

 StubbedInvocationMatcher invocationMatcher2 = new StubbedInvocationMatcher(anyInt());
 Stub stub2 = handler.stub(mock, invocationMatcher2, "returnValue2");

 handler.handle(new InvocationOnMock(mock, "someMethod", new Object[]{1}, new Class[]{int.class}));
 }

 @Test
 public void testSetAnswersForStubbing() {
 List<Answer> answers = new ArrayList<>();
 answers.add((InvocationOnMock invocation) -> "answer1");
 answers.add((InvocationOnMock invocation) -> "answer2");
 handler.setAnswersForStubbing(answers);

 Object mock = mock(Object.class);
 Object result = handler.handle(new InvocationOnMock(mock, "someMethod", new Object[]{1}, new Class[]{int.class}));

 Assert.assertEquals("answer1", result);
 }

 @Test(expected = IllegalStateException.class)
 public void testHandle_InvocationWithStubbingAndVerification_ThrowsException() throws Throwable {
 Object mock = mock(Object.class);
 when(mock.someMethod(anyInt())).thenReturn("expectedValue");

 StubbedInvocationMatcher invocationMatcher = new StubbedInvocationMatcher(anyInt());
 Stub stub = handler.stub(mock, invocationMatcher, "returnValue");

 handler.handle(new InvocationOnMock(mock, "someMethod", new Object[]{1}, new Class[]{int.class}));
 }

 @Test(expected = IllegalStateException.class)
 public void testHandle_InvocationWithStubbingAndDifferentMock_ThrowsException() throws Throwable {
 Object mock1 = mock(Object.class);
 Object mock2 = mock(Object.class);

 StubbedInvocationMatcher invocationMatcher = new StubbedInvocationMatcher(anyInt());
 Stub stub = handler.stub(mock1, invocationMatcher, "returnValue");

 handler.handle(new InvocationOnMock(mock2, "someMethod", new Object[]{1}, new Class[]{int.class}));
 }

 @Test
 public void testHandle_InvocationWithNoMatchingStubOrVerification_ThrowsInvocationTargetException() throws Throwable {
 Object mock = mock(Object.class);
 handler.handle(new InvocationOnMock(mock, "someMethod", new Object[]{1}, new Class[]{int.class}));

 Assert.assertThrows(InvocationTargetException.class, () -> handler.handle(new InvocationOnMock(mock, "someOtherMethod", new Object[]{1}, new Class[]{int.class})));
 }
}