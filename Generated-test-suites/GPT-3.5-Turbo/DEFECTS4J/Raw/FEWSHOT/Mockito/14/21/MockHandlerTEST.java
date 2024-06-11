
package org.mockito.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class MockHandlerTest {
    @Test
    public void testHandle() throws Throwable {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);
        
        // test invocation without stubbing answers
        Invocation invocation1 = new Invocation(null, null, null, null, null);
        assertNull(mockHandler.handle(invocation1));
        
        // test invocation with stubbing voids
        Invocation invocation2 = new Invocation(null, null, null, null, null);
        mockHandler.getInvocationContainer().setAnswersForStubbing(null);
        assertNull(mockHandler.handle(invocation2));
        
        // test invocation with stubbing answers
        Invocation invocation3 = new Invocation(null, null, null, null, null);
        mockHandler.getInvocationContainer().setAnswersForStubbing(null);
        assertNull(mockHandler.handle(invocation3));
        
        // test invocation without stubbed invocation
        Invocation invocation4 = new Invocation(null, null, null, null, null);
        mockHandler.getInvocationContainer().setMethodForStubbing(null);
        assertNull(mockHandler.handle(invocation4));
        
        // test invocation with stubbed invocation
        Invocation invocation5 = new Invocation(null, null, null, null, null);
        mockHandler.getInvocationContainer().setMethodForStubbing(null);
        assertNull(mockHandler.handle(invocation5));
    }
    
    @Test
    public void testVoidMethodStubbable() {
        MockHandler<Object> mockHandler = new MockHandler<>();
        Object mock = new Object();
        VoidMethodStubbable<Object> voidMethodStubbable = mockHandler.voidMethodStubbable(mock);
        assertEquals(mock, voidMethodStubbable.getMock());
        assertEquals(mockHandler.getInvocationContainer(), voidMethodStubbable.getInvocationContainer());
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
        List<Answer> answers = null;
        mockHandler.setAnswersForStubbing(answers);
        assertEquals(answers, mockHandler.getInvocationContainer().getAnswersForStubbing());
    }
    
    @Test
    public void testGetInvocationContainer() {
        MockHandler<Object> mockHandler = new MockHandler<>();
        InvocationContainerImpl invocationContainer = new InvocationContainerImpl(null);
        mockHandler.invocationContainerImpl = invocationContainer;
        assertEquals(invocationContainer, mockHandler.getInvocationContainer());
    }
}
