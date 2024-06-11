package org.mockito.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationData;
import org.mockito.verification.VerificationMode;
import org.mockito.verification.VerificationModeFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class MockHandlerTest {

    private MockHandler<MockHandlerInterface> mockHandler;

    @Before
    public void setUp() {
        mockHandler = new MockHandler<MockHandlerInterface>();
    }

    @Test
    public void testHandle() throws Throwable {
        Invocation invocation = new Invocation(null, null, null, null, null);
        Object result = mockHandler.handle(invocation);
        assertNull(result);
    }

    @Test
    public void testInvocationContainer() {
        InvocationContainer container = mockHandler.getInvocationContainer();
        assertNotNull(container);
    }

    @Test
    public void testVoidMethodStubbable() {
        VoidMethodStubbable stubbable = mockHandler.voidMethodStubbable(null);
        assertNotNull(stubbable);
    }

    @Test
    public void testSetAnswersForStubbing() {
        List<Answer> answers = new ArrayList<Answer>();
        mockHandler.setAnswersForStubbing(answers);
        InvocationContainer container = mockHandler.getInvocationContainer();
        List<Answer> storedAnswers = container.getAnswersForStubbing();
        assertEquals(answers, storedAnswers);
    }

    @Test
    public void testGetMockSettings() {
        MockSettingsImpl mockSettings = mockHandler.getMockSettings();
        assertNotNull(mockSettings);
    }

    @Test
    public void testHandleVerify() throws Throwable {
        Invocation invocation = new Invocation(null, null, null, null, null);
        mockHandler.voidMethodStubbable(null).doNothing();
        mockHandler.handle(invocation);
        VerificationMode verificationMode = VerificationModeFactory.times(1);
        VerificationData verificationData = Mockito.mock(VerificationData.class);
        when(verificationData.getVerificationMode()).thenReturn(verificationMode);
        mockHandler.handleVerify(invocation, verificationData);
        verify(verificationData, times(1)).getVerificationMode();
    }
}