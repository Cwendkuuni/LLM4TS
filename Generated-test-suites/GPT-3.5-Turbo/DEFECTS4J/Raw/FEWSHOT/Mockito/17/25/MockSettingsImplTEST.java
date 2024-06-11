
package org.mockito.internal.creation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.MockName;
import org.mockito.stubbing.Answer;

public class MockSettingsImplTest {
    @Test
    public void testSerializable() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockSettingsImpl result = mockSettings.serializable();
        
        assertEquals(java.io.Serializable.class, result.getExtraInterfaces()[0]);
    }

    @Test
    public void testExtraInterfaces() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = { Object.class, Runnable.class };
        MockSettingsImpl result = mockSettings.extraInterfaces(extraInterfaces);
        
        assertEquals(extraInterfaces, result.getExtraInterfaces());
    }

    @Test
    public void testExtraInterfacesWithNull() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = { null };
        
        try {
            mockSettings.extraInterfaces(extraInterfaces);
        } catch (RuntimeException e) {
            assertEquals(Reporter.class, e.getClass());
            assertEquals("extraInterfacesDoesNotAcceptNullParameters", e.getMessage());
        }
    }
    
    @Test
    public void testExtraInterfacesWithNonInterface() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = { String.class };
        
        try {
            mockSettings.extraInterfaces(extraInterfaces);
        } catch (RuntimeException e) {
            assertEquals(Reporter.class, e.getClass());
            assertEquals("extraInterfacesAcceptsOnlyInterfaces", e.getMessage());
        }
    }

    @Test
    public void testMockName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockName mockName = mock(MockName.class);
        
        when(mockName.getName()).thenReturn("mockName");
        
        mockSettings.initiateMockName(String.class);
        mockSettings.name("mockName");
        
        assertEquals(mockName, mockSettings.getMockName());
    }

    @Test
    public void testSpiedInstance() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Object spiedInstance = new Object();
        MockSettingsImpl result = mockSettings.spiedInstance(spiedInstance);
        
        assertEquals(spiedInstance, result.getSpiedInstance());
    }

    @Test
    public void testDefaultAnswer() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Answer<Object> defaultAnswer = mock(Answer.class);
        MockSettingsImpl result = mockSettings.defaultAnswer(defaultAnswer);
        
        assertEquals(defaultAnswer, result.getDefaultAnswer());
    }

    @Test
    public void testIsSerializableTrue() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = { java.io.Serializable.class };
        mockSettings.extraInterfaces(extraInterfaces);
        
        assertTrue(mockSettings.isSerializable());
    }

    @Test
    public void testIsSerializableFalse() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = { Object.class };
        mockSettings.extraInterfaces(extraInterfaces);
        
        assertFalse(mockSettings.isSerializable());
    }
}
