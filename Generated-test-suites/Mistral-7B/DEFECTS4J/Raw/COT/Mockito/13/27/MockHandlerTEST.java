import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MockHandlerTest {

 private MockHandler<MockHandlerTestMock> mockHandler;
 private MockHandler<MockHandlerTestMock> oldMockHandler;
 private MockHandler<MockHandlerTestMock> newMockHandler;
 private MockHandler<MockHandlerTestMock> anotherMockHandler;
 private MockHandler<MockHandlerTestMock> anotherDifferentMockHandler;
 private MockitoInvocationHandler target;
 private MockHandlerInterface<MockHandlerTestMock> mock;
 private Invocation invocation;
 private InvocationOnMock invocationOnMock;
 private List<Answer> answers;

 @Before
 public void setup() {
 mock = mock(MockHandlerTestMock.class);
 mockHandler = new MockHandler<>(mockSettings());
 oldMockHandler = mockHandler;
 newMockHandler = new MockHandler(mockHandler);
 anotherMockHandler = mockHandler;
 anotherDifferentMockHandler = mockHandler;
 target = new MockHandler<MockHandlerTestMock>(mock) {
 @Override
 public Object handle(Invocation invocation) throws Throwable {
 return super.handle(invocation);
 }
 };
 answers = new ArrayList<>();
 }

 @Test
 public void testHandle_InvocationContainerHasAnswersForStubbing() throws Throwable {
 invocation = Mockito.mock(InvocationOnMock.class);
 when(mock.handle(ArgumentMatchers.any(InvocationOnMock.class))).thenReturn(42);
 when(mock.getMockSettings()).thenReturn(mockSettings());
 when(mockingProgress.pullVerificationMode()).thenReturn(null);

 when(matchersBinder.bindMatchers(ArgumentMatchers.any(ArgumentMatcher[].class), ArgumentMatchers.any(Invocation.class)))
 .thenReturn(new StubbedInvocationMatcher(null, null, null, null, null, null));

 when(invocationContainerImpl.findAnswerFor(ArgumentMatchers.any(InvocationMatcher.class))).thenReturn(new StubbedInvocationMatcher(null, answers, null, null, null, null));

 when(invocationContainerImpl.setAnswersForStubbing(ArgumentMatchers.any(List.class))).thenReturn(null);

 when(mockSettings.getDefaultAnswer()).thenReturn(answer(42));

 Object result = target.handle(invocation);

 assertEquals(42, result);
 verify(mock).handle(invocation);
 verify(mockingProgress, never()).pullVerificationMode();
 verify(mockingProgress, never()).validateState();
 verify(invocationContainerImpl, never()).setInvocationForPotentialStubbing(ArgumentMatchers.any(InvocationMatcher.class));
 verify(invocationContainerImpl).resetInvocationForPotentialStubbing(ArgumentMatchers.any(InvocationMatcher.class));
 verify(invocationContainerImpl, never()).setMethodForStubbing(ArgumentMatchers.any(InvocationMatcher.class));
 verify(invocationContainerImpl).setAnswersForStubbing(answers);
 }

 @Test
 public void testHandle_VerificationModeIsNotNull() throws Throwable {
 invocation = Mockito.mock(InvocationOnMock.class);
 when(mock.handle(ArgumentMatchers.any(InvocationOnMock.class))).thenReturn(42);
 when(mock.getMockSettings()).thenReturn(mockSettings());
 when(mockingProgress.pullVerificationMode()).thenReturn(new MockAwareVerificationMode(mock));

 when(invocationMatcher(anyInt(), eq(mock))).thenReturn(new StubbedInvocationMatcher(null, null, null, null, null, null));

 Object result = target.handle(invocation);

 assertEquals(42, result);
 verify(mock).handle(invocation);
 verify(mockingProgress).pullVerificationMode();
 verify(mockingProgress).validateState();
 }

 @Test(expected = IllegalStateException.class)
 public void testHandle_VerificationModeIsNotNullButNotForCurrentMock() throws Throwable {
 invocation = Mockito.mock(InvocationOnMock.class);
 when(mock.handle(ArgumentMatchers.any(InvocationOnMock.class))).thenReturn(42);
 when(mock.getMockSettings()).thenReturn(mockSettings());
 when(mockingProgress.pullVerificationMode()).thenReturn(new MockAwareVerificationMode(anotherMockHandler));

 when(invocationMatcher(anyInt(), eq(mock))).thenReturn(new StubbedInvocationMatcher(null, null, null, null, null, null));

 target.handle(invocation);
 }

 @Test
 public void testHandle_NoAnswerFound() throws Throwable {
 invocation = Mockito.mock(InvocationOnMock.class);
 when(mock.handle(ArgumentMatchers.any(InvocationOnMock.class))).thenReturn(42);
 when(mock.getMockSettings()).thenReturn(mockSettings());
 when(mockingProgress.pullVerificationMode()).thenReturn(null);

 when(invocationMatcher(anyInt(), eq(mock))).thenReturn(null);

 Object result = target.handle(invocation);

 assertEquals(null, result);
 }

 @Test
 public void testVoidMethodStubbable() throws Throwable {
 VoidMethodStubbable<MockHandlerTestMock> voidMethodStubbable = mockHandler.voidMethodStubbable(mock);
 voidMethodStubbable.stubVoid();

 verify(mock, never()).handle(ArgumentMatchers.any(InvocationOnMock.class));
 }

 @Test
 public void testGetMockSettings() throws Throwable {
 Object result = target.getMockSettings();

 assertEquals(mockSettings(), result);
 }

 @Test
 public void testSetAnswersForStubbing() throws Throwable {
 List<Answer> answers = new ArrayList<>();
 answers.add(answer(42));
 target.setAnswersForStubbing(answers);

 Object result = target.handle(invocation);

 assertEquals(42, result);
 }

 @Test
 public void testGetInvocationContainer() throws Throwable {
 Object result = target.getInvocationContainer();

 assertTrue(result instanceof InvocationContainer);
 }

 private Answer answer(int value) {
 return new Answer<Void>() {
 @Override
 public Void answer(InvocationOnMock invocation) throws Throwable {
 return null;
 }
 };
 }

 private InvocationMatcher invocationMatcher(int arg1, MockHandlerTestMock arg2) {
 return ArgumentMatchers.argThat(new ArgumentMatcher<InvocationOnMock>() {
 @Override
 public boolean matches(InvocationOnMock argument) {
 return ArgumentMatchers.eq(arg1).equals(argument.getArguments()[0])
 && ArgumentMatchers.eq(arg2).equals(argument.getMock());
 }
 });
}
}