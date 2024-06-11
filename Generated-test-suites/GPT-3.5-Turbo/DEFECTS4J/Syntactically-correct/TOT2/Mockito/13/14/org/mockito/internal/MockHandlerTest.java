package org.mockito.internal;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.stubbing.Answer;
import org.mockito.verification.VerificationMode;

public class MockHandlerTest {

    private MockHandler mockHandler;
    private MockSettingsImpl mockSettings;

    @Before
    public void setup() {
        mockSettings = new MockSettingsImpl();
        mockHandler = new MockHandler(mockSettings);
    }

    @Test
    public void testHandle() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        Object result = mockHandler.handle(invocation);
        assertEquals(null, result);
    }

    @Test
    public void testVoidMethodStubbable() {
        Object mock = new Object();
        VoidMethodStubbable stubbable = mockHandler.voidMethodStubbable(mock);
        assertEquals(mock, stubbable.getMock());
    }

    @Test
    public void testGetMockSettings() {
        assertEquals(mockSettings, mockHandler.getMockSettings());
    }

    @Test
    public void testSetAnswersForStubbing() {
        List<Answer> answers = new ArrayList<>();
        mockHandler.setAnswersForStubbing(answers);
        assertEquals(answers, mockHandler.getInvocationContainer().getAnswersForStubbing());
    }

    @Test
    public void testHandleWithStubbing() throws Throwable {
        Invocation invocation = mock(Invocation.class);

        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        Mockito.when(container.hasAnswersForStubbing()).thenReturn(true);

        MatchersBinder binder = mock(MatchersBinder.class);
        Mockito.when(binder.bindMatchers(ArgumentMatchers.any(), invocation)).thenReturn(new InvocationMatcher());
        mockHandler.invocationContainerImpl = container;
        mockHandler.matchersBinder = binder;

        Object result = mockHandler.handle(invocation);
        assertEquals(null, result);
    }

    @Test
    public void testHandleWithVerificationMode() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        VerificationMode verificationMode = mock(MockAwareVerificationMode.class);

        MatchersBinder binder = mock(MatchersBinder.class);
        Mockito.when(binder.bindMatchers(ArgumentMatchers.any(), invocation)).thenReturn(new InvocationMatcher());
        mockHandler.matchersBinder = binder;
        mockHandler.mockingProgress = mock(MockingProgress.class);
        Mockito.when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);

        Object result = mockHandler.handle(invocation);
        assertEquals(null, result);
    }

    @Test
    public void testHandleWithStubbedInvocation() throws Throwable {
        Invocation invocation = mock(Invocation.class);

        StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);
        Mockito.when(container.findAnswerFor(invocation)).thenReturn(stubbedInvocation);

        Object result = mockHandler.handle(invocation);
        assertEquals(null, result);
    }

    @Test(expected = Exception.class)
    public void testHandleWithException() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        Mockito.when(mockSettings.getDefaultAnswer().answer(invocation)).thenThrow(new Exception());

        Object result = mockHandler.handle(invocation);
    }
}