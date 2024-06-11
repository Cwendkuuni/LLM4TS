package org.mockito.internal;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class MockHandlerTest {
    @Test
    public void testHandle() throws Throwable {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);
        Invocation invocation = mock(Invocation.class);
        assertNull(mockHandler.handle(invocation));
        
        MockHandlerInterface<Object> oldMockHandler = mock(MockHandlerInterface.class);
        mockHandler = new MockHandler<>(oldMockHandler);
        assertNull(mockHandler.handle(invocation));
    }
    
    @Test
    public void testVoidMethodStubbable() {
        MockHandler<Object> mockHandler = new MockHandler<>();
        Object mock = new Object();
        assertNotNull(mockHandler.voidMethodStubbable(mock));
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
        List<Answer> answers = mock(List.class);
        mockHandler.setAnswersForStubbing(answers);
        assertEquals(answers, mockHandler.getInvocationContainer().getAnswersForStubbing());
    }
    
    @Test
    public void testGetInvocationContainer() {
        MockHandler<Object> mockHandler = new MockHandler<>();
        assertNotNull(mockHandler.getInvocationContainer());
    }
}