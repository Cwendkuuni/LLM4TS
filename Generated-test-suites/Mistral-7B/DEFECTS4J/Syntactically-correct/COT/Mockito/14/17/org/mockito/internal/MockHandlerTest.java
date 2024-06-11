import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;

import org.mockito.internal.MockitoInvocationHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.MockSettingsImpl;
import org.mockito.internal.creation.MockSettingsImpl_;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.stubbing.VoidMethodStubbableImpl;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.stubbing.Answer_;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationMode;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(org.mockito.runners.JUnit4.class)
public class MockHandlerTest {

 private MockHandler<MockitoInvocationHandler> mockHandler;
 private MockSettingsImpl mockSettings;
 private InvocationContainer invocationContainer;
 private InvocationMatcher invocationMatcher;
 private Invocation invocation;
 private OngoingStubbing<MockitoInvocationHandler> ongoingStubbing;
 private StubbedInvocationMatcher stubbedInvocation;

 @Before
 public void setup() {
 mockSettings = new MockSettingsImpl_();
 invocationContainer = new InvocationContainerImpl(new ThreadSafeMockingProgress());
 mockHandler = new MockHandler<>(mockSettings);
 mockHandler.setInvocationContainer(invocationContainer);
 }

 @Test
 public void testHandle_stubbing() throws Throwable {
 invocation = Mockito.mock(InvocationOnMock.class);
 invocationMatcher = ArgumentMatchers.eq(invocationMatcher);

 when(mockSettings.getDefaultAnswer()).thenReturn(new Answer() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return "default answer";
 }
 });

 ongoingStubbing = mockHandler.voidMethodStubbable(mockHandler);
 ongoingStubbing.when(invocationMatcher).thenReturn(new Answer_() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return "stubbed answer";
 }
 });

 invocationMatcher = mockHandler.matchersBinder.bindMatchers(invocationContainer.getArgumentMatcherStorage(), invocation);

 Object result = mockHandler.handle(invocation);

 assertNull(result);
 verifyZeroInteractions(invocation);
 }

 @Test
 public void testHandle_verification() throws Throwable {
 invocation = Mockito.mock(InvocationOnMock.class);
 invocationMatcher = ArgumentMatchers.eq(invocationMatcher);

 mockHandler.mockingProgress.pushVerificationMode(VerificationMode.VERIFY_NONE);
 mockHandler.mockingProgress.pushVerificationMode(VerificationMode.verify(invocationMatcher));

 mockHandler.handle(invocation);

 verifyNoMoreInteractions(invocation);
 }

 @Test(expected = Throwable.class)
 public void testHandle_verify_different_mock() throws Throwable {
 MockitoInvocationHandler mockHandler2 = mock(MockitoInvocationHandler.class);
 invocation = Mockito.mock(InvocationOnMock.class);
 invocationMatcher = ArgumentMatchers.eq(invocationMatcher);

 mockHandler.mockingProgress.pushVerificationMode(VerificationMode.verify(invocationMatcher));
 mockHandler.handle(invocation);
 mockHandler2.handle(invocation);
 }

 @Test
 public void testHandle_answers_for_stubbing() throws Throwable {
 List<Answer> answers = new ArrayList<>();
 answers.add(new Answer() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return "answer 1";
 }
 });
 answers.add(new Answer() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return "answer 2";
 }
 });

 mockHandler.setAnswersForStubbing(answers);
 invocation = Mockito.mock(InvocationOnMock.class);
 invocationMatcher = ArgumentMatchers.eq(invocationMatcher);

 Object result = mockHandler.handle(invocation);

 assertNull(result);
 verifyZeroInteractions(invocation);

 ongoingStubbing = mockHandler.voidMethodStubbable(mockHandler);
 ongoingStubbing.when(invocationMatcher).thenAnswer(answers.get(0));
 result = mockHandler.handle(invocation);
 assertEquals("answer 1", result);
 }

 @Test
 public void testHandle_answers_for_stubbing_with_null() throws Throwable {
 List<Answer> answers = new ArrayList<>();
 answers.add(null);

 mockHandler.setAnswersForStubbing(answers);
 invocation = Mockito.mock(InvocationOnMock.class);
 invocationMatcher = ArgumentMatchers.eq(invocationMatcher);

 Object result = mockHandler.handle(invocation);

 assertNull(result);
 verifyZeroInteractions(invocation);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testHandle_answers_for_stubbing_with_empty_list() {
 List<Answer> answers = new ArrayList<>();
 mockHandler.setAnswersForStubbing(answers);
 }

 @Test
 public void testHandle_answers_for_stubbing_with_multiple_answers() throws Throwable {
 List<Answer> answers = new ArrayList<>();
 answers.add(new Answer() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return "answer 1";
 }
 });
 answers.add(new Answer() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 throw new RuntimeException("exception");
 }
 });

 mockHandler.setAnswersForStubbing(answers);
 invocation = Mockito.mock(InvocationOnMock.class);
 invocationMatcher = ArgumentMatchers.eq(invocationMatcher);

 try {
 mockHandler.handle(invocation);
 fail("Expected RuntimeException to be thrown");
 } catch (RuntimeException e) {
 // expected
 }
 }

 @Test
 public void testHandle_null_invocation() throws Throwable {
 invocation = null;
 invocationMatcher = ArgumentMatchers.eq(invocationMatcher);

 try {
 mockHandler.handle(invocation);
 fail("Expected IllegalStateException to be thrown");
 } catch (IllegalStateException e) {
 // expected
 }
 }

 @Test
 public void testHandle_void_method_stubbable() throws Throwable {
 VoidMethodStubbable<MockitoInvocationHandler> voidMethodStubbable = mockHandler.voidMethodStubbable(mockHandler);
 voidMethodStubbable.stubVoid();
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testHandle_void_method_stubbable_with_arguments() throws Throwable {
 VoidMethodStubbable<MockitoInvocationHandler> voidMethodStubbable = mockHandler.voidMethodStubbable(mockHandler);
 voidMethodStubbable.stub(ArgumentMatchers.any());
 }
}