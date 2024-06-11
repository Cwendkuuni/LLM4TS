package org.mockito.internal.creation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.MockName;
import org.mockito.stubbing.Answer;

public class MockSettingsImplTest {
    @Test
    public void testSerializable() {
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.serializable();
        assertEquals(true, settings.isSerializable());
    }
    
    @Test
    public void testExtraInterfaces() {
        MockSettingsImpl settings = new MockSettingsImpl();
        
        // Test with null extraInterfaces
        try {
            settings.extraInterfaces(null);
        } catch (Reporter e) {
            assertEquals("Extra interfaces requires at least one interface", e.getMessage());
        }
        
        // Test with empty extraInterfaces
        try {
            settings.extraInterfaces(new Class<?>[0]);
        } catch (Reporter e) {
            assertEquals("Extra interfaces requires at least one interface", e.getMessage());
        }
        
        // Test with null interface
        try {
            settings.extraInterfaces(String.class, null);
        } catch (Reporter e) {
            assertEquals("Extra interfaces does not accept null parameters", e.getMessage());
        }
        
        // Test with non-interface class
        try {
            settings.extraInterfaces(String.class);
        } catch (Reporter e) {
            assertEquals("Extra interfaces accepts only interfaces", e.getMessage());
        }
    }
    
    @Test
    public void testGetMockName() {
        MockSettingsImpl settings = new MockSettingsImpl();
        MockName mockName = new MockName();
        settings.initiateMockName(MockSettingsImpl.class);
        assertEquals(mockName, settings.getMockName());
    }
    
    @Test
    public void testGetExtraInterfaces() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {Serializable.class, Cloneable.class};
        settings.extraInterfaces(extraInterfaces);
        assertEquals(extraInterfaces, settings.getExtraInterfaces());
    }
    
    @Test
    public void testGetSpiedInstance() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Object spiedInstance = new Object();
        settings.spiedInstance(spiedInstance);
        assertEquals(spiedInstance, settings.getSpiedInstance());
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testDefaultAnswer() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Answer<Object> defaultAnswer = (Answer<Object>) Mockito.mock(Answer.class);
        settings.defaultAnswer(defaultAnswer);
        assertEquals(defaultAnswer, settings.getDefaultAnswer());
    }
    
}