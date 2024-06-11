package org.mockito.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.stubbing.Answer;
import org.mockito.verification.VerificationData;

public class MockHandlerTest {

    private MockHandler<Object> mockHandler;

    @Before
    public void setUp() {
        mockHandler = new MockHandler<>();
    }

    @Test
    public void testHandle() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        VerificationData verificationData = mock(VerificationData.class);

        when(mockHandler.getInvocationContainer().hasAnswersForStubbing()).thenReturn(true);
        when(mockHandler.getInvocationContainer().getMethodForStubbing(invocationMatcher)).thenReturn("");
        when(mockHandler.getMockSettings().getDefaultAnswer().answer(invocation)).thenReturn("");

        assertNull(mockHandler.handle(invocation));

        verify(mockHandler.getInvocationContainer()).hasAnswersForStubbing();
        verify(mockHandler.getInvocationContainer()).getMethodForStubbing(invocationMatcher);
        verify(mockHandler.getMockSettings().getDefaultAnswer()).answer(invocation);
        verify(mockHandler.getInvocationContainer()).resetInvocationForPotentialStubbing(invocationMatcher);

        when(mockHandler.getInvocationContainer().hasAnswersForStubbing()).thenReturn(false);
        when(mockHandler.getMockSettings().getDefaultAnswer().answer(invocation)).thenReturn("");

        assertNull(mockHandler.handle(invocation));

        verify(mockHandler.getInvocationContainer()).hasAnswersForStubbing();
        verify(mockHandler.getMockSettings().getDefaultAnswer()).answer(invocation);
        verify(mockHandler.getInvocationContainer()).resetInvocationForPotentialStubbing(invocationMatcher);

        when(mockHandler.getMockSettings().getDefaultAnswer().answer(invocation)).thenReturn("");
        when(mockHandler.getInvocationContainer().findAnswerFor(invocation)).thenReturn(null);

        assertNull(mockHandler.handle(invocation));

        verify(mockHandler.getMockSettings().getDefaultAnswer()).answer(invocation);
        verify(mockHandler.getInvocationContainer()).resetInvocationForPotentialStubbing(invocationMatcher);
    }

    @Test
    public void testVoidMethodStubbable() {
        Object mock = new Object();

        VoidMethodStubbable<Object> voidMethodStubbable = mockHandler.voidMethodStubbable(mock);

        assertEquals(mockHandler.getInvocationContainer(), voidMethodStubbable.getInvocationContainer());
    }

    @Test
    public void testSetAnswersForStubbing() {
        List<Answer<?>> answers = mock(List.class);

        mockHandler.setAnswersForStubbing(answers);

        verify(mockHandler.getInvocationContainer()).setAnswersForStubbing(answers);
    }
}