import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MockHandlerTest {

 private MockHandler<MockHandlerTestClass> mockHandler;
 private MockHandler<MockHandlerTestClass> mockHandlerSpy;
 private MockSettingsImpl mockSettings;
 private InvocationOnMock invocation;
 private List<Answer> answers;

 @Before
 public void setup() {
 mockSettings = new MockSettingsImpl();
 mockHandler = new MockHandler<>(mockSettings);
 mockHandlerSpy = Mockito.spy(new MockHandler<>(mockSettings));
 answers = new ArrayList<>();
 }

 @Test
 public void testHandle_InvocationWithStubbing() throws Throwable {
 // Given
 when(mockHandler.getInvocationContainer()).thenReturn(new InvocationContainerImpl(mockingProgress));
 when(mockHandler.matchersBinder.bindMatchers(Mockito.mock(ArgumentMatcher.class), ArgumentMatchers.any()))
 .thenReturn(Mockito.mock(InvocationMatcher.class));
 when(mockHandler.mockingProgress.pullVerificationMode()).thenReturn(null);

 // When
 Invocation invocation = Mockito.mock(Invocation.class);
 Object result = mockHandler.handle(invocation);

 // Then
 verify(mockHandler.invocationContainerImpl).setMethodForStubbing(Mockito.eq(Mockito.any(InvocationMatcher.class)));
 verify(mockHandler.invocationContainerImpl).setAnswersForStubbing(answers);
 verify(mockHandler.mockingProgress).reportOngoingStubbing(Mockito.any());
 assertNull(result);
 }

 @Test
 public void testHandle_InvocationWithoutStubbing() throws Throwable {
 // Given
 when(mockHandler.getInvocationContainer()).thenReturn(new InvocationContainerImpl(mockingProgress));
 when(mockHandler.mockingProgress.pullVerificationMode()).thenReturn(null);
 when(mockHandler.matchersBinder.bindMatchers(Mockito.mock(ArgumentMatcher.class), ArgumentMatchers.any()))
 .thenReturn(null);

 // When
 Invocation invocation = Mockito.mock(Invocation.class);
 Object result = mockHandler.handle(invocation);

 // Then
 verifyNoInteractions(mockHandler.invocationContainerImpl);
 verifyNoInteractions(mockHandler.mockingProgress);
 assertEquals(mockSettings.getDefaultAnswer().answer(invocation), result);
 }

 @Test(expected = Throwable.class)
 public void testHandle_InvocationWithVerificationMode() throws Throwable {
 // Given
 when(mockHandler.getInvocationContainer()).thenReturn(new InvocationContainerImpl(mockingProgress));
 when(mockHandler.mockingProgress.pullVerificationMode()).thenReturn(new VerificationMode() {});

 // When
 Invocation invocation = Mockito.mock(Invocation.class);
 mockHandler.handle(invocation);

 // Then
 verify(mockHandler.mockingProgress).validateState();
 verify(mockHandler.invocationContainerImpl).getInvocations();
 throw new Throwable();
 }

 @Test
 public void testHandle_InvocationWithAnswer() throws Throwable {
 // Given
 when(mockHandler.getInvocationContainer()).thenReturn(new InvocationContainerImpl(mockingProgress));
 when(mockHandler.mockingProgress.pullVerificationMode()).thenReturn(null);
 when(mockHandler.matchersBinder.bindMatchers(Mockito.mock(ArgumentMatcher.class), ArgumentMatchers.any()))
 .thenReturn(Mockito.mock(InvocationMatcher.class));
 when(mockHandler.invocationContainerImpl.findAnswerFor(Mockito.any()))
 .thenReturn(Mockito.mock(StubbedInvocationMatcher.class));
 when(mockHandler.stubbedInvocation.answer(Mockito.any()))
 .thenReturn("Answer");

 // When
 Invocation invocation = Mockito.mock(Invocation.class);
 Object result = mockHandler.handle(invocation);

 // Then
 verify(mockHandler.invocationContainerImpl).setMethodForStubbing(Mockito.eq(Mockito.any(InvocationMatcher.class)));
 verify(mockHandler.invocationContainerImpl).setInvocationForPotentialStubbing(Mockito.eq(Mockito.any(InvocationMatcher.class)));
 verify(mockHandler.invocationContainerImpl).setAnswersForStubbing(answers);
 verify(mockHandler.mockingProgress).reportOngoingStubbing(Mockito.any());
 verify(mockHandler.stubbedInvocation).captureArgumentsFrom(invocation);
 assertEquals("Answer", result);
 }

 @Test
 public void testHandle_InvocationWithNoAnswer() throws Throwable {
 // Given
 when(mockHandler.getInvocationContainer()).thenReturn(new InvocationContainerImpl(mockingProgress));
 when(mockHandler.mockingProgress.pullVerificationMode()).thenReturn(null);
 when(mockHandler.matchersBinder.bindMatchers(Mockito.mock(ArgumentMatcher.class), ArgumentMatchers.any()))
 .thenReturn(null);
 when(mockHandler.invocationContainerImpl.findAnswerFor(Mockito.any()))
 .thenReturn(null);

 // When
 Invocation invocation = Mockito.mock(Invocation.class);
 Object result = mockHandler.handle(invocation);

 // Then
 verify(mockHandler.invocationContainerImpl).resetInvocationForPotentialStubbing(Mockito.eq(Mockito.any(InvocationMatcher.class)));
 verify(mockHandler.invocationContainerImpl).setInvocationForPotentialStubbing(Mockito.eq(Mockito.any(InvocationMatcher.class)));
 verifyNoInteractions(mockHandler.stubbedInvocation);
 assertEquals(mockSettings.getDefaultAnswer().answer(invocation), result);
 }

 @Test
 public void testVoidMethodStubbable() {
 Answer answer = Mockito.mock(Answer.class);
 VoidMethodStubbable<MockHandlerTestClass> voidMethodStubbable = mockHandler.voidMethodStubbable(Mockito.mock(MockHandlerTestClass.class));
 voidMethodStubbable.thenAnswer(answer);
 verify(mockHandler.invocationContainerImpl).setAnswersForStubbing(answers);
 verify(answer).answer(Mockito.any());
 }

}

class MockHandlerTestClass {
}