package org.mockito.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationMatcher;
import org.mockito.progress.MockingProgress;
import org.mockito.progress.ThreadSafeMockingProgress;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.stubbing.StubbedInvocationMatcher;
import org.mockito.verification.VerificationData;
import org.mockito.verification.VerificationMode;

public class MockHandlerTest {
    @Test
    public void testHandle() throws Throwable {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);

        Invocation invocation = new Invocation(null, null, null, null);
        assertNull(mockHandler.handle(invocation));

        Answer<Object> defaultAnswer = new Answer<Object>() {
            public Object answer(Invocation invocation) throws Throwable {
                return null;
            }
        };
        mockSettings.setDefaultAnswer(defaultAnswer);

        mockHandler.setAnswersForStubbing(null);
        assertNull(mockHandler.handle(invocation));

        mockHandler.setAnswersForStubbing(null);
        mockHandler.getInvocationContainer().setMethodForStubbing(null);
        assertNull(mockHandler.handle(invocation));

        mockHandler.setAnswersForStubbing(null);
        mockHandler.getInvocationContainer().setMethodForStubbing(null);
        System.out.println(mockSettings.getDefaultAnswer());
        assertNotNull(mockHandler.handle(invocation));

        mockHandler.setAnswersForStubbing(null);
        mockHandler.getInvocationContainer().setMethodForStubbing(null);
        mockHandler.getMockSettings().setDefaultAnswer(null);
        assertNotNull(mockHandler.handle(invocation));
    }

    @Test
    public void testVoidMethodStubbable() {
        MockHandler<Object> mockHandler = new MockHandler<>();

        Object mock = new Object();
        VoidMethodStubbable<Object> stubbable = mockHandler.voidMethodStubbable(mock);
        assertNotNull(stubbable);

        Invocation invocation = new Invocation(null, null, null, null);
        assertNull(stubbable.addVoidMethodForStubbing(invocation));
        assertNull(stubbable.addVoidMethodForStubbing(null));
    }

    @Test
    public void testGetMockSettings() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);

        assertEquals(mockSettings, mockHandler.getMockSettings());

        mockHandler = new MockHandler<>();
        assertNotNull(mockHandler.getMockSettings());
    }

    @Test
    public void testSetAnswersForStubbing() {
        MockHandler<Object> mockHandler = new MockHandler<>();

        assertNull(mockHandler.getInvocationContainer().getAnswersForStubbing());

        Answer<Object> answer1 = new Answer<Object>() {
            public Object answer(Invocation invocation) throws Throwable {
                return null;
            }
        };
        Answer<Object> answer2 = new Answer<Object>() {
            public Object answer(Invocation invocation) throws Throwable {
                return null;
            }
        };
        mockHandler.setAnswersForStubbing(null);
        assertNull(mockHandler.getInvocationContainer().getAnswersForStubbing());

        mockHandler.setAnswersForStubbing(new ArrayList<Answer<Object>>());
        assertNotNull(mockHandler.getInvocationContainer().getAnswersForStubbing());

        mockHandler.setAnswersForStubbing(Arrays.asList(answer1, answer2));
        assertNotNull(mockHandler.getInvocationContainer().getAnswersForStubbing());
    }

    @Test
    public void testGetInvocationContainer() {
        MockHandler<Object> mockHandler = new MockHandler<>();

        InvocationContainerImpl invocationContainer = mockHandler.getInvocationContainer();
        assertNotNull(invocationContainer);

        mockHandler.invocationContainerImpl = null;
        assertNotNull(mockHandler.getInvocationContainer());
    }
}