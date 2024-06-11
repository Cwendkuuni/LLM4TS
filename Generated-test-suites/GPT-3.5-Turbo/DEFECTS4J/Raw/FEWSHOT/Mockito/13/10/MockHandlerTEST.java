
package org.mockito.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;
import org.junit.Test;

public class MockHandlerTest {
    
    @Test
    public void testHandle() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        MockHandler<MockitoInvocationHandler> handler = new MockHandler<>();
        assertNull(handler.handle(invocation));
    }
    
    @Test
    public void testVoidMethodStubbable() {
        MockHandler<MockitoInvocationHandler> handler = new MockHandler<>();
        VoidMethodStubbable<MockitoInvocationHandler> methodStubbable = handler.voidMethodStubbable(null);
        assertNull(methodStubbable);
    }
    
    @Test
    public void testGetMockSettings() {
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        MockHandler<MockitoInvocationHandler> handler = new MockHandler<>(mockSettings);
        assertEquals(mockSettings, handler.getMockSettings());
    }
    
    @Test
    public void testSetAnswersForStubbing() {
        List<Answer> answers = mock(List.class);
        MockHandler<MockitoInvocationHandler> handler = new MockHandler<>();
        handler.setAnswersForStubbing(answers);
        assertEquals(answers, handler.getInvocationContainer().getAnswersForStubbing());
    }
    
    @Test
    public void testGetInvocationContainer() {
        MockHandler<MockitoInvocationHandler> handler = new MockHandler<>();
        InvocationContainer container = handler.getInvocationContainer();
        assertNotNull(container);
    }
}
