package org.mockito.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.Invocation;

public class MockHandlerTest {

    private MockHandler<Object> mockHandler;
    private InvocationContainerImpl invocationContainerImpl;

    @Before
    public void setUp() {
        mockHandler = new MockHandler<Object>();
        invocationContainerImpl = mock(InvocationContainerImpl.class);
        mockHandler.invocationContainerImpl = invocationContainerImpl;
    }

    @Test
    public void testHandle_WhenInvocationContainerHasAnswersForStubbing_ReturnsNull() throws Throwable {
        Invocation invocation = mock(Invocation.class);

        assertEquals(null, mockHandler.handle(invocation));

        verify(invocationContainerImpl).hasAnswersForStubbing();
    }

    @Test
    public void testHandle_WhenInvocationContainerHasNoAnswersForStubbing_ReturnsAnswerFromDefaultAnswer() throws Throwable {
        Invocation invocation = mock(Invocation.class);

        assertNull(mockHandler.handle(invocation));

        verify(invocationContainerImpl).hasAnswersForStubbing();
        verify(invocationContainerImpl).findAnswerFor(invocation);
        verify(invocationContainerImpl).resetInvocationForPotentialStubbing(invocationMatcher);
    }

    @Test
    public void testVoidMethodStubbable_ReturnsVoidMethodStubbableImpl() {
        Object mock = new Object();
        VoidMethodStubbableImpl stubbableImpl = mock(VoidMethodStubbableImpl.class);

        assertNotNull(mockHandler.voidMethodStubbable(mock));

        verify(stubbableImpl).setInvocationContainer(invocationContainerImpl);
    }

    @Test
    public void testSetAnswersForStubbing_SetsAnswersForStubbingInInvocationContainer() {
        List<Answer> answers = mock(List.class);

        mockHandler.setAnswersForStubbing(answers);

        verify(invocationContainerImpl).setAnswersForStubbing(answers);
    }

    @Test
    public void testGetInvocationContainer_ReturnsInvocationContainerImpl() {
        assertEquals(invocationContainerImpl, mockHandler.getInvocationContainer());
    }
}