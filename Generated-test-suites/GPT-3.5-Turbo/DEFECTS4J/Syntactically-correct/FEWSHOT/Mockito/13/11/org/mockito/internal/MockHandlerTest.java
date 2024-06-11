package org.mockito.internal;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.internal.creation.MockSettingsImpl;
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
import org.mockito.internal.verification.MockAwareVerificationMode;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationMode;

public class MockHandlerTest {

    @Test
    public void testHandle() throws Throwable {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);
        Invocation invocation = new Invocation();
        assertNull(mockHandler.handle(invocation));
    }

    @Test
    public void testVoidMethodStubbable() {
        MockHandler<Object> mockHandler = new MockHandler<>();
        Object mock = new Object();
        assertTrue(mockHandler.voidMethodStubbable(mock) instanceof VoidMethodStubbableImpl);
    }

    @Test
    public void testGetMockSettings() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);
        assertEquals(mockSettings, mockHandler.getMockSettings());
    }

    @Test
    public void testSetAnswersForStubbing() {
        MockHandler<Object> mockHandler = new MockHandler<>();
        List<Answer> answers = new ArrayList<>();
        mockHandler.setAnswersForStubbing(answers);
        assertEquals(answers, mockHandler.getInvocationContainer().getAnswersForStubbing());
    }

    @Test
    public void testGetInvocationContainer() {
        MockHandler<Object> mockHandler = new MockHandler<>();
        assertTrue(mockHandler.getInvocationContainer() instanceof InvocationContainerImpl);
    }
}