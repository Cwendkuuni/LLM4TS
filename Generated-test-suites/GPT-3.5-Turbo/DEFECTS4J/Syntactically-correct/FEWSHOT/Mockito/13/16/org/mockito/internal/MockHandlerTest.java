
package org.mockito.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationMode;

public class MockHandlerTest {
    
    private MockingProgress mockingProgress = new ThreadSafeMockingProgress();
    private InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
    private MatchersBinder matchersBinder = mock(MatchersBinder.class);
    private MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
    private Invocation invocation = mock(Invocation.class);
    private InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
    private VerificationMode verificationMode = mock(VerificationMode.class);
    private MockAwareVerificationMode mockAwareVerificationMode = mock(MockAwareVerificationMode.class);
    private StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);
    private Answer<Object> defaultAnswer = mock(Answer.class);
    private List<Answer> answers = Arrays.asList(defaultAnswer);
    private OngoingStubbingImpl ongoingStubbing = mock(OngoingStubbingImpl.class);

    @Test
    public void testHandle_stubbingVoidsWithStubVoidOrDoAnswerStyle_returnsNull() throws Throwable {
        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(true);
        when(matchersBinder.bindMatchers(mockingProgress.getArgumentMatcherStorage(), invocation)).thenReturn(invocationMatcher);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        
        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);
        Object result = mockHandler.handle(invocation);
        
        assertNull(result);
    }

    @Test
    public void testHandle_nullVerificationMode_returnsNull() throws Throwable {
        when(mockingProgress.pullVerificationMode()).thenReturn(null);
        when(matchersBinder.bindMatchers(mockingProgress.getArgumentMatcherStorage(), invocation)).thenReturn(invocationMatcher);

        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);
        Object result = mockHandler.handle(invocation);

        assertNull(result);
    }

    @Test
    public void testHandle_verificationModeNotInstanceOfMockAwareVerificationMode_returnsNull() throws Throwable {
        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        when(verificationMode instanceof MockAwareVerificationMode).thenReturn(false);
        when(matchersBinder.bindMatchers(mockingProgress.getArgumentMatcherStorage(), invocation)).thenReturn(invocationMatcher);

        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);
        Object result = mockHandler.handle(invocation);

        assertNull(result);
    }

    @Test
    public void testHandle_verificationModeInstanceOfMockAwareVerificationModeAndMockMatchesInvocation_returnsNull() throws Throwable {
        when(mockingProgress.pullVerificationMode()).thenReturn(mockAwareVerificationMode);
        when(mockAwareVerificationMode.getMock()).thenReturn(invocation.getMock());
        when(matchersBinder.bindMatchers(mockingProgress.getArgumentMatcherStorage(), invocation)).thenReturn(invocationMatcher);

        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);
        Object result = mockHandler.handle(invocation);

        assertNull(result);
    }

    @Test
    public void testHandle_stubbedInvocationNotNull_returnsAnswerResult() throws Throwable {
        when(mockingProgress.pullVerificationMode()).thenReturn(null);
        when(matchersBinder.bindMatchers(mockingProgress.getArgumentMatcherStorage(), invocation)).thenReturn(invocationMatcher);
        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(stubbedInvocation);
        when(stubbedInvocation.answer(invocation)).thenReturn(new Object());

        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);
        Object result = mockHandler.handle(invocation);

        assertEquals(Object.class, result.getClass());
    }

    @Test
    public void testHandle_stubbedInvocationNull_returnsDefaultAnswerResult() throws Throwable {
        when(mockSettings.getDefaultAnswer()).thenReturn(defaultAnswer);
        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(null);
        when(defaultAnswer.answer(invocation)).thenReturn(new Object());

        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);
        Object result = mockHandler.handle(invocation);

        assertEquals(Object.class, result.getClass());
    }

    @Test
    public void testVoidMethodStubbable_mockIsNull_returnsNull() {
        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);
        VoidMethodStubbable stubbable = mockHandler.voidMethodStubbable(null);

        assertNull(stubbable);
    }

    @Test
    public void testVoidMethodStubbable_mockIsNotNull_returnsVoidMethodStubbableImpl() {
        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);
        Object mock = mock(Object.class);
        VoidMethodStubbable stubbable = mockHandler.voidMethodStubbable(mock);

        assertEquals(VoidMethodStubbableImpl.class, stubbable.getClass());
    }

    @Test
    public void testGetMockSettings_returnsMockSettings() {
        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);
        MockSettingsImpl result = mockHandler.getMockSettings();

        assertEquals(MockSettingsImpl.class, result.getClass());
    }

    @Test
    public void testSetAnswersForStubbing_setsAnswersForStubbing() {
        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);
        mockHandler.setAnswersForStubbing(answers);

        assertEquals(answers, invocationContainerImpl.getAnswersForStubbing());
    }

    @Test
    public void testGetInvocationContainer_returnsInvocationContainer() {
        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);
        InvocationContainer result = mockHandler.getInvocationContainer();

        assertEquals(InvocationContainerImpl.class, result.getClass());
    }
}
