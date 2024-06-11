
package org.mockito.internal;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationMode;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MockHandlerTest {

    private MockHandler<TestClass> mockHandler;
    private MockHandlerInterface<TestClass> oldMockHandler;
    private MockSettingsImpl mockSettings;
    private List<Answer> answers;
    private InvocationContainerImpl invocationContainerImpl;
    private MatchersBinder matchersBinder;
    private MockingProgress mockingProgress;

    @Before
    public void setup() {
        mockSettings = new MockSettingsImpl();
        answers = new ArrayList<>();
        invocationContainerImpl = new InvocationContainerImpl(mockingProgress);
        matchersBinder = new MatchersBinder();
        mockingProgress = new ThreadSafeMockingProgress();
        mockHandler = new MockHandler<>(mockSettings);
        oldMockHandler = new MockHandler<>(mockHandler);
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;
    }

    @Test
    public void testHandle_withAnswersForStubbing() throws Throwable {
        Invocation invocation = new Invocation();
        invocationContainerImpl.setAnswersForStubbing(answers);

        Object result = mockHandler.handle(invocation);

        assertEquals(null, result);
    }

    @Test
    public void testHandle_withVerificationMode() throws Throwable {
        Invocation invocation = new Invocation();
        matchersBinder.bindMatchers(mockingProgress.getArgumentMatcherStorage(), invocation);
        mockingProgress.pullVerificationMode();
        mockingProgress.validateState();

        Object result = mockHandler.handle(invocation);

        assertEquals(null, result);
    }

    @Test
    public void testHandle_withStubbedInvocation() throws Throwable {
        Invocation invocation = new Invocation();
        invocationContainerImpl.setInvocationForPotentialStubbing(invocation);

        Object result = mockHandler.handle(invocation);

        assertNotNull(result);
    }

    @Test
    public void testVoidMethodStubbable() {
        TestClass mock = new TestClass();
        VoidMethodStubbable<TestClass> voidMethodStubbable = mockHandler.voidMethodStubbable(mock);

        assertNotNull(voidMethodStubbable);
    }

    @Test
    public void testGetMockSettings() {
        MockSettingsImpl result = mockHandler.getMockSettings();

        assertEquals(mockSettings, result);
    }

    @Test
    public void testSetAnswersForStubbing() {
        List<Answer> answers = new ArrayList<>();

        mockHandler.setAnswersForStubbing(answers);

        assertEquals(answers, invocationContainerImpl.getAnswersForStubbing());
    }

    @Test
    public void testGetInvocationContainer() {
        InvocationContainer result = mockHandler.getInvocationContainer();

        assertEquals(invocationContainerImpl, result);
    }
}

class TestClass {}
