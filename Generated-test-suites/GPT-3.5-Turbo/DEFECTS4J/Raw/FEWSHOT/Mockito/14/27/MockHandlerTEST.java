package org.mockito.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.mockito.Mockito;
import org.mockito.internal.stubbing.VoidMethodStubbable;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.stubbing.Answer;
import org.mockito.verification.VerificationMode;
import org.junit.Before;
import org.junit.Test;

public class MockHandlerTest {
    private MockHandler mockHandler;

    @Before
    public void setup() {
        mockHandler = Mockito.mock(MockHandler.class);
    }

    @Test
    public void testHandle() throws Throwable {
        Invocation invocation = Mockito.mock(Invocation.class);

        Mockito.when(mockHandler.handle(invocation)).thenCallRealMethod();
        assertNull(mockHandler.handle(invocation));
    }

    @Test
    public void testVoidMethodStubbable() {
        Object mock = new Object();
        VoidMethodStubbable stubbable = mockHandler.voidMethodStubbable(mock);

        assertEquals(mock, stubbable.getMock());
        assertEquals(mockHandler.getInvocationContainer(), stubbable.getInvocationContainer());
    }

    @Test
    public void testGetMockSettings() {
        MockSettingsImpl settings = Mockito.mock(MockSettingsImpl.class);
        mockHandler = new MockHandler(settings);

        assertEquals(settings, mockHandler.getMockSettings());
    }

    @Test
    public void testSetAnswersForStubbing() {
        List<Answer> answers = Mockito.mock(List.class);

        mockHandler.setAnswersForStubbing(answers);

        assertEquals(answers, mockHandler.getInvocationContainer().getAnswersForStubbing());
    }

    @Test
    public void testGetInvocationContainer() {
        InvocationContainer container = new InvocationContainerImpl();

        mockHandler.invocationContainerImpl = container;

        assertEquals(container, mockHandler.getInvocationContainer());
    }

}