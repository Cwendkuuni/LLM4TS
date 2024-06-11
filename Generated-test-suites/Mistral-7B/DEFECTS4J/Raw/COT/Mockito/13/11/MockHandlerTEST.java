import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.AnswerWithMockito;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.verification.VerificationMode;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.mockito.internal.verification.VerificationModeFactory.times;

public class MockHandlerTest {

 private MockHandler<DummyClass> mockHandler;
 private DummyClass mock;

 @Before
 public void setUp() {
 mock = mock(DummyClass.class);
 mockHandler = new MockHandler<>(mock.getClass().getConstructors()[0].newInstance(new MockSettingsImpl()));
 }

 @Test
 public void testHandle_stubbing() throws Throwable {
 // given
 final int expectedValue = 42;
 final Method methodToStub = DummyClass.class.getMethod("methodToStub", int.class);
 final Answer<Integer> answer = new AnswerWithMockito<Integer>() {
 @Override
 public Integer answer(InvocationOnMock invocation) throws Throwable {
 return expectedValue;
 }
 };
 when(mockHandler.getInvocationContainer()).thenReturn(new InvocationContainerImpl(new ThreadSafeMockingProgress()));
 when(mockHandler.getMockSettings()).thenReturn(new MockSettingsImpl());
 when(mockHandler.matchersBinder.bindMatchers(ArgumentMatchers.any(ThreadSafeMockingProgress.class), ArgumentMatchers.eq(methodToStub))).thenReturn(new StubbedInvocationMatcher(methodToStub, answer));

 // when
 Object result = mockHandler.handle(new InvocationOnMock(mock, methodToStub, new Object[]{1}, new Object[0]));

 // then
 verify(mockHandler.getInvocationContainer()).setAnswersForStubbing(ArgumentMatchers.eq(new ArrayList<Answer<?>>(){{add(answer);}}));
 verify(mockHandler.getInvocationContainer(), never()).setInvocationForPotentialStubbing(ArgumentMatchers.any(StubbedInvocationMatcher.class));
 verify(mockHandler.matchersBinder, never()).bindMatchers(ArgumentMatchers.any(ThreadSafeMockingProgress.class), ArgumentMatchers.isA(Invocation.class));
 verify(mock, times(1)).methodToStub(expectedValue);
 assertNull(result);
 }

 @Test(expected = AssertionError.class)
 public void testHandle_verification_on_different_mock() throws Throwable {
 final MockHandler<DummyClass> otherMockHandler = new MockHandler<>(mock.getClass().getConstructors()[0].newInstance(new MockSettingsImpl()));
 final VerificationMode verificationMode = times(1);
 when(mockHandler.getMockingProgress()).thenReturn(new ThreadSafeMockingProgress());
 when(mockHandler.getMockingProgress().pullVerificationMode()).thenReturn(verificationMode);
 when(otherMockHandler.getMockingProgress()).thenReturn(new ThreadSafeMockingProgress());
 when(otherMockHandler.getMockingProgress().pullVerificationMode()).thenReturn(verificationMode);

 // when and then
 mockHandler.handle(new InvocationOnMock(mock, new Method(null, null, new Class[0]), new Object[0], new Object[0]));
 }

 @Test
 public void testHandle_no_verification_mode() throws Throwable {
 final MockHandler<DummyClass> mockHandler = new MockHandler<>(mock.getClass().getConstructors()[0].newInstance(new MockSettingsImpl()));
 final InvocationOnMock invocation = new InvocationOnMock(mock, new Method(null, null, new Class[0]), new Object[0], new Object[0]);
 when(mockHandler.getMockingProgress()).thenReturn(new ThreadSafeMockingProgress());
 when(mockHandler.handle(invocation)).thenCallRealMethod();

 // when
 mockHandler.handle(invocation);

 // then
 verify(mock, never()).methodCall(); // replace 'methodCall' with the actual method name you want to test
 }

 @Test
 public void testHandle_delegation() throws Throwable {
 final MockHandler<DummyClass> mockHandler = new MockHandler<>(mock.getClass().getConstructors()[0].newInstance(new MockSettingsImpl()));
 final DummyClass spy = mock(DummyClass.class, new Answers().withAnswers(new Answer<Object>() {
 @Override public Object answer(InvocationOnMock invocation) throws Throwable {
 return mockHandler.handle(invocation);
 }
 }));
 final Method methodToDelegate = DummyClass.class.getMethod("methodToDelegate");
 when(mockHandler.getMockingProgress()).thenReturn(new ThreadSafeMockingProgress());
 when(mockHandler.getInvocationContainer()).thenReturn(new InvocationContainerImpl(new ThreadSafeMockingProgress()));
 when(mockHandler.matchersBinder.bindMatchers(ArgumentMatchers.any(ThreadSafeMockingProgress.class), ArgumentMatchers.eq(methodToDelegate))).thenReturn(new StubbedInvocationMatcher(methodToDelegate, Mockito.doNothing()));
 when(mockHandler.handle(ArgumentMatchers.eq(methodToDelegate))).thenCallRealMethod();

 // when
 Object result = spy.methodToDelegate();

 // then
 verify(mockHandler.getInvocationContainer(), never()).setInvocationForPotentialStubbing(ArgumentMatchers.any(StubbedInvocationMatcher.class));
 verify(mockHandler.matchersBinder, never()).bindMatchers(ArgumentMatchers.any(ThreadSafeMockingProgress.class), ArgumentMatchers.isA(Invocation.class));
 verify(mock, times(1)).methodToDelegate();
 assertNotNull(result);
 }

 @Test
 public void testHandle_void_method() throws Throwable {
 final MockHandler<DummyClass> mockHandler = new MockHandler<>(mock.getClass().getConstructors()[0].newInstance(new MockSettingsImpl()));
 final VoidMethodStubbable<DummyClass> voidMethodStubbable = mockHandler.voidMethodStubbable(mock);
 final Method methodToVoidStub = DummyClass.class.getMethod("voidMethodToStub");
 when(mockHandler.getMockingProgress()).thenReturn(new ThreadSafeMockingProgress());
 when(mockHandler.getInvocationContainer()).thenReturn(new InvocationContainerImpl(new ThreadSafeMockingProgress()));
 when(mockHandler.matchersBinder.bindMatchers(ArgumentMatchers.any(ThreadSafeMockingProgress.class), ArgumentMatchers.eq(methodToVoidStub))).thenReturn(new StubbedInvocationMatcher(methodToVoidStub, Mockito.doNothing()));
 when(voidMethodStubbable.stubVoid()).thenCallRealMethod();

 // when
 mockHandler.voidMethodStubbable(mock).stubVoid();

 // then
 verify(mockHandler.getInvocationContainer()).setAnswersForStubbing(ArgumentMatchers.eq(new ArrayList<Answer<?>>()));
 verify(mockHandler.getInvocationContainer(), never()).setInvocationForPotentialStubbing(ArgumentMatchers.any(StubbedInvocationMatcher.class));
 verify(mockHandler.matchersBinder, never()).bindMatchers(ArgumentMatchers.any(ThreadSafeMockingProgress.class), ArgumentMatchers.isA(Invocation.class));
 verify(mock, never()).voidMethodToStub();
 }
}

class DummyClass {
 void voidMethodToStub() {}
 int methodToStub(int i) { return i; }
 int methodCall() { return 0; }
}