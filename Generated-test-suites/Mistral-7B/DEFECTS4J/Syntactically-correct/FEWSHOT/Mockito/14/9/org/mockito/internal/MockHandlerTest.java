package org.mockito.internal;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.verifyZeroInteractionsInOrder;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import org.mockito.internal.MatchersBinder;
import org.mockito.internal.MockingProgress;
import org.mockito.internal.invocation.InvocationContainer;
import org.mockito.internal.invocation.InvocationContainerImpl;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.stubbing.InvocationMatcherWithAnswer;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.stubbing.AnswerImpl;
import org.mockito.stubbing.AnswerWithArgs;
import org.mockito.stubbing.AnswerWithReturnValue;
import org.mockito.stubbing.Stubber;
import org.mockito.verification.VerificationMode;

public class MockHandlerTest {

    @Mock
    private InvocationContainer invocationContainer;

    @Mock
    private MatchersBinder matchersBinder;

    @Mock
    private MockingProgress mockingProgress;

    @Mock
    private VerificationDataImpl verificationData;

    @Mock
    private Stubber stubber;

    @Mock
    private InvocationMatcher invocationMatcher;

    @Mock
    private Invocation invocation;

    @Mock
    private OngoingStubbingImpl<Object> ongoingStubbing;

    @InjectMocks
    private MockHandler<Object> mockHandler;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockStatic(Mockito.class);
    }

    @Test
    public void testHandle_noAnswersForStubbing() throws Throwable {
        when(matchersBinder.bindMatchers(ArgumentMatchers.any(MockingProgress.class), ArgumentMatchers.eq(invocation)))
                .thenReturn(invocationMatcher);

        when(invocationContainer.findAnswerFor(ArgumentMatchers.eq(invocation)))
                .thenReturn(null);

        when(mockingProgress.pullVerificationMode()).thenReturn(null);

        Object result = mockHandler.handle(invocation);

        verify(invocationContainer).setInvocationForPotentialStubbing(invocationMatcher);
        verify(invocationContainer).resetInvocationForPotentialStubbing(invocationMatcher);
        verifyNoInteractions(verificationData);

        assertNull(result);
    }

    @Test
    public void testHandle_verificationModeNotNull() throws Throwable {
        when(matchersBinder.bindMatchers(ArgumentMatchers.any(MockingProgress.class), ArgumentMatchers.eq(invocation)))
                .thenReturn(invocationMatcher);

        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode());

        mockHandler.handle(invocation);

        verify(verificationData).verify(ArgumentMatchers.eq(invocationContainerImpl.getInvocations()), ArgumentMatchers.eq(invocationMatcher));
        verifyZeroInteractions(invocationContainerImpl);
    }

    @Test
    public void testHandle_answersForStubbing() throws Throwable {
        when(matchersBinder.bindMatchers(ArgumentMatchers.any(MockingProgress.class), ArgumentMatchers.eq(invocation)))
                .thenReturn(invocationMatcher);

        List<Answer> answers = new ArrayList<>();
        answers.add(AnswerWithReturnValue.answer(1));
        when(invocationContainer.getAnswersForStubbing()).thenReturn(answers);

        Object result = mockHandler.handle(invocation);

        verify(invocationContainer).setMethodForStubbing(invocationMatcher);
        verifyZeroInteractions(invocationMatcher);
        verifyZeroInteractions(verificationData);

        assertTrue(result == null);
    }

    @Test
    public void testHandle_answersForStubbingWithArgs() throws Throwable {
        when(matchersBinder.bindMatchers(ArgumentMatchers.any(MockingProgress.class), ArgumentMatchers.eq(invocation)))
                .thenReturn(invocationMatcher);

        Object[] arguments = new Object[]{ "arg1", 1, mock(Object.class) };
        InvocationOnMock invocationOnMock = Mockito.mock(InvocationOnMock.class);
        when(invocationOnMock.getArguments()).thenReturn(arguments);
        when(invocationContainer.getAnswersForStubbing()).thenReturn(answersWithArgs());

        Object result = mockHandler.handle(invocationOnMock);

        verify(invocationContainer).setMethodForStubbing(invocationMatcher);
        verify(invocationMatcher).withArguments(arguments);
        verifyZeroInteractions(verificationData);

        assertTrue(result == null);
    }

    @Test
    public void testHandle_answersForStubbingWithAnswer() throws Throwable {
        when(matchersBinder.bindMatchers(ArgumentMatchers.any(MockingProgress.class), ArgumentMatchers.eq(invocation)))
                .thenReturn(invocationMatcher);

        Answer answer = AnswerWithArgs.answer(1, 2, 3);
        when(invocationContainer.getAnswersForStubbing()).thenReturn(answersWithAnswer(answer));

        Object result = mockHandler.handle(invocation);

        verify(invocationContainer).setMethodForStubbing(invocationMatcher);
        verifyZeroInteractions(invocationMatcher);
        verifyZeroInteractions(verificationData);

        assertTrue(result == null);
    }

    @Test
    public void testHandle_answersForStubbingWithAnswerWithReturnValue() throws Throwable {
        when(matchersBinder.bindMatchers(ArgumentMatchers.any(MockingProgress.class), ArgumentMatchers.eq(invocation)))
                .thenReturn(invocationMatcher);

        Answer answer = AnswerWithReturnValue.returning(42);
        when(invocationContainer.getAnswersForStubbing()).thenReturn(answersWithAnswer(answer));

        Object result = mockHandler.handle(invocation);

        verify(invocationContainer).setMethodForStubbing(invocationMatcher);
        verifyZeroInteractions(invocationMatcher);
        verifyZeroInteractions(verificationData);

        assertTrue(result == null);
        assertTrue(result instanceof Integer);
        assertTrue(((Integer) result).intValue() == 42);
    }

    @Test
    public void testHandle_answersForStubbingWithAnswerWithArgsAndReturnValue() throws Throwable {
        when(matchersBinder.bindMatchers(ArgumentMatchers.any(MockingProgress.class), ArgumentMatchers.eq(invocation)))
                .thenReturn(invocationMatcher);

        Object[] arguments = new Object[]{ "arg1", 1, mock(Object.class) };
        InvocationOnMock invocationOnMock = Mockito.mock(InvocationOnMock.class);
        when(invocationOnMock.getArguments()).thenReturn(arguments);

        Answer answer = AnswerWithArgsAndReturnValue.answer(1, 2, 3, 4);
        when(invocationContainer.getAnswersForStubbing()).thenReturn(answersWithAnswer(answer));

        Object result = mockHandler.handle(invocationOnMock);

        verify(invocationContainer).setMethodForStubbing(invocationMatcher);
        verify(invocationMatcher).withArguments(arguments);
        verifyZeroInteractions(verificationData);

        assertTrue(result == null);
        assertTrue(result instanceof Integer);
        assertTrue(((Integer) result).intValue() == 4);
    }

    @Test
    public void testVoidMethodStubbable() {
        VoidMethodStubbable<Object> voidMethodStubbable = mockHandler.voidMethodStubbable(mock(Object.class));

        verify(invocationContainerImpl).setAnswersForStubbing(ArgumentMatchers.isA(List.class));
        verifyZeroInteractions(matchersBinder);
        verifyZeroInteractions(mockingProgress);

        assertEquals(mockHandler, voidMethodStubbable.getMockHandler());
        assertEquals(mock(Object.class), voidMethodStubbable.getMock());
    }

    @Test
    public void testGetMockSettings() {
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);

        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);

        assertEquals(mockSettings, mockHandler.getMockSettings());
    }

    @Test
    public void testSetAnswersForStubbing() {
        List<Answer> answers = new ArrayList<>();
        answers.add(AnswerWithReturnValue.returning(42));

        mockHandler.setAnswersForStubbing(answers);

        verify(invocationContainerImpl).setAnswersForStubbing(answers);
    }

    @Test
    public void testGetInvocationContainer() {
        InvocationContainer invocationContainer = mock(InvocationContainer.class);

        MockHandler<Object> mockHandler = new MockHandler<>(invocationContainer);

        assertEquals(invocationContainer, mockHandler.getInvocationContainer());
    }

    private VerificationMode verificationMode() {
        return Mockito.verifyNoMoreInteractions(mockingProgress, verificationData);
    }

    private InvocationMatcher WithAnswer(Answer answer, Invocation invocation) {
        return new InvocationMatcherWithAnswer(answer, invocation);
    }

    private InvocationMatcher WithAnswerAndArguments(Answer answer, Object[] arguments) {
        return new InvocationMatcherWithAnswer(answer, new InvocationOnMock() {
            @Override
            public Object[] getArguments() {
                return arguments;
            }
        });
    }

    private InvocationMatcher WithArgs(Object[] arguments) {
        return ArgumentMatchers.argThat(invocation -> Arrays.equals(arguments, invocation.getArguments()));
    }

    private List<Answer> answersWithArgs() {
        List<Answer> answers = new ArrayList<>();
        answers.add(AnswerWithArgs.answer(ArgumentMatchers.anyArray(), ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt(),
                ArgumentMatchers.any(Object.class)));
        return answers;
    }

    private List<Answer> answersWithAnswer(Answer answer) {
        List<Answer> answers = new ArrayList<>();
        answers.add(answer);
        return answers;
    }
}