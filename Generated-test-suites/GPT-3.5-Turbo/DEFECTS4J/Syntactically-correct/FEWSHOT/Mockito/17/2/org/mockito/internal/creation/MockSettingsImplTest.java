package org.mockito.internal.creation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

public class MockSettingsImplTest {
    
    @Test
    public void testMockSettingsImpl_serializable() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.serializable();
        
        assertEquals(true, mockSettings.isSerializable());
    }
    
    @Test
    public void testMockSettingsImpl_extraInterfaces() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {Runnable.class, Comparable.class};
        mockSettings.extraInterfaces(extraInterfaces);
        
        assertEquals(extraInterfaces, mockSettings.getExtraInterfaces());
    }
    
    @Test
    public void testMockSettingsImpl_extraInterfaces_withNullParameters() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = null;
        mockSettings.extraInterfaces(extraInterfaces);
        
        assertEquals(null, mockSettings.getExtraInterfaces());
    }
    
    @Test
    public void testMockSettingsImpl_extraInterfaces_onlyInterfaces() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {Runnable.class, Integer.class};
        mockSettings.extraInterfaces(extraInterfaces);
        
        assertEquals(null, mockSettings.getExtraInterfaces());
    }
    
    @Test
    public void testMockSettingsImpl_getMockName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockName mockName = mock(MockName.class);
        mockSettings.initiateMockName(MockSettingsImpl.class);
        
        assertEquals(mockName, mockSettings.getMockName());
    }
    
    @Test
    public void testMockSettingsImpl_getSpiedInstance() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Object spiedInstance = new Object();
        mockSettings.spiedInstance(spiedInstance);
        
        assertEquals(spiedInstance, mockSettings.getSpiedInstance());
    }
    
    @Test
    public void testMockSettingsImpl_name() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        String name = "mockName";
        mockSettings.name(name);
        
        assertEquals(name, mockSettings.getMockName().toString());
    }
    
    @Test
    public void testMockSettingsImpl_defaultAnswer() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Answer<Object> defaultAnswer = Mockito.mock(Answer.class);
        mockSettings.defaultAnswer(defaultAnswer);
        
        assertEquals(defaultAnswer, mockSettings.getDefaultAnswer());
    }
    
}