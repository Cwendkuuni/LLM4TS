package org.mockito.internal;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.Mockito;

public class MockHandlerTest {
    @Test
    public void testHandle_StubbingVoidsWithStubVoid() throws Throwable {
        MockHandler mockHandler = new MockHandler();
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = Mockito.mock(InvocationMatcher.class);
        Mockito.when(mockHandler.getMockSettings().getDefaultAnswer().answer(invocation)).thenReturn("return value");
        Mockito.when(mockHandler.getMockSettings().getDefaultAnswer().answer(invocationMatcher)).thenReturn("return value");

        Object result = mockHandler.handle(invocation);

        assertEquals("return value", result);
    }

    @Test
    public void testHandle_StubbingVoidsWithDoAnswer() throws Throwable {
        MockHandler mockHandler = new MockHandler();
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = Mockito.mock(InvocationMatcher.class);
        Mockito.when(mockHandler.getMockSettings().getDefaultAnswer().answer(invocation)).thenReturn("return value");
        Mockito.when(mockHandler.getMockSettings().getDefaultAnswer().answer(invocationMatcher)).thenReturn("return value");

        Object result = mockHandler.handle(invocation);

        assertEquals("return value", result);
    }

    @Test
    public void testHandle_StubsInvocationWithStubbedInvocationMatcher() throws Throwable {
        MockHandler mockHandler = new MockHandler();
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = Mockito.mock(InvocationMatcher.class);
        Mockito.when(mockHandler.getMockSettings().getDefaultAnswer().answer(invocation)).thenReturn("return value");
        Mockito.when(mockHandler.getMockSettings().getDefaultAnswer().answer(invocationMatcher)).thenReturn("return value");
        mockHandler.invocationContainerImpl.setMethodForStubbing(invocationMatcher);

        Object result = mockHandler.handle(invocation);

        assertEquals("return value", result);
    }

    @Test
    public void testHandle_NoStubbedInvocationReturnsDefaultAnswer() throws Throwable {
        MockHandler mockHandler = new MockHandler();
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = Mockito.mock(InvocationMatcher.class);
        Mockito.when(mockHandler.getMockSettings().getDefaultAnswer().answer(invocation)).thenReturn("return value");
        Mockito.when(mockHandler.getMockSettings().getDefaultAnswer().answer(invocationMatcher)).thenReturn("return value");

        Object result = mockHandler.handle(invocation);

        assertEquals("return value", result);
    }

    @Test
    public void testHandle_StubbedInvocationReturnsStubbedAnswer() throws Throwable {
        MockHandler mockHandler = new MockHandler();
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = Mockito.mock(InvocationMatcher.class);
        StubbedInvocationMatcher stubbedInvocation = Mockito.mock(StubbedInvocationMatcher.class);
        Mockito.when(mockHandler.getMockSettings().getDefaultAnswer().answer(invocation)).thenReturn("default answer");
        Mockito.when(mockHandler.getMockSettings().getDefaultAnswer().answer(invocationMatcher)).thenReturn("default answer");
        Mockito.when(mockHandler.invocationContainerImpl.findAnswerFor(invocation)).thenReturn(stubbedInvocation);
        Mockito.when(stubbedInvocation.answer(invocation)).thenReturn("stubbed answer");

        Object result = mockHandler.handle(invocation);

        assertEquals("stubbed answer", result);
    }

    @Test
    public void testVoidMethodStubbable() {
        MockHandler mockHandler = new MockHandler();
        Object mock = new Object();

        VoidMethodStubbable stabbable = mockHandler.voidMethodStubbable(mock);

        assertEquals(mockHandler.invocationContainerImpl, stabbable.invocationContainer);
        assertEquals(mock, stabbable.mock);
    }

    @Test
    public void testGetMockSettings() {
        MockSettingsImpl settings = new MockSettingsImpl();
        MockHandler mockHandler = new MockHandler(settings);

        MockSettingsImpl result = mockHandler.getMockSettings();

        assertEquals(settings, result);
    }

    @Test
    public void testSetAnswersForStubbing() {
        MockHandler mockHandler = new MockHandler();
        List<Answer> answers = Mockito.mock(List.class);

        mockHandler.setAnswersForStubbing(answers);

        assertEquals(answers, mockHandler.invocationContainerImpl.getAnswersForStubbing());
    }

    @Test
    public void testGetInvocationContainer() {
        MockHandler mockHandler = new MockHandler();

        InvocationContainer result = mockHandler.getInvocationContainer();

        assertEquals(mockHandler.invocationContainerImpl, result);
    }
}