package org.mockito.internal.creation;

import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;

public class MockSettingsImplTest {
    
    @Test
    public void testSerializable() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.serializable();
        assertTrue(mockSettings.isSerializable());
    }
    
    @Test
    public void testExtraInterfaces() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        
        // Typical use case
        Class<?>[] extraInterfaces = {Runnable.class, Comparable.class};
        mockSettings.extraInterfaces(extraInterfaces);
        assertArrayEquals(extraInterfaces, mockSettings.getExtraInterfaces());
        assertFalse(mockSettings.isSerializable());
        
        // Edge case: empty array of extra interfaces
        Class<?>[] emptyArray = {};
        mockSettings.extraInterfaces(emptyArray);
        assertArrayEquals(emptyArray, mockSettings.getExtraInterfaces());
        assertFalse(mockSettings.isSerializable());
        
        // Error scenario: null array of extra interfaces
        try {
            mockSettings.extraInterfaces(null);
            fail("Expected Reporter.extraInterfacesRequiresAtLeastOneInterface() to be thrown");
        } catch (Reporter extraInterfacesRequiresException) {
            assertNotNull(extraInterfacesRequiresException);
        }
        
        // Error scenario: null interface in extra interfaces array
        Class<?>[] interfacesWithNull = {Runnable.class, null};
        try {
            mockSettings.extraInterfaces(interfacesWithNull);
            fail("Expected Reporter.extraInterfacesDoesNotAcceptNullParameters() to be thrown");
        } catch (Reporter extraInterfacesParameterException) {
            assertNotNull(extraInterfacesParameterException);
        }
        
        // Error scenario: interface is not an interface
        Class<?>[] invalidInterfaces = {Runnable.class, String.class};
        try {
            mockSettings.extraInterfaces(invalidInterfaces);
            fail("Expected Reporter.extraInterfacesAcceptsOnlyInterfaces() to be thrown");
        } catch (Reporter extraInterfacesAcceptsException) {
            assertNotNull(extraInterfacesAcceptsException);
        }
    }
    
    @Test
    public void testGetSetMockName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        
        MockName mockName = new MockName("testMock", Object.class);
        mockSettings.initiateMockName(Object.class);
        
        assertEquals(mockName, mockSettings.getMockName());
    }
    
    @Test
    public void testGetSetSpiedInstance() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        
        Object spiedInstance = new Object();
        mockSettings.spiedInstance(spiedInstance);
        
        assertEquals(spiedInstance, mockSettings.getSpiedInstance());
    }
    
    @Test
    public void testGetSetDefaultAnswer() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        
        Answer<Object> defaultAnswer = invocation -> null;
        mockSettings.defaultAnswer(defaultAnswer);
        
        assertEquals(defaultAnswer, mockSettings.getDefaultAnswer());
    }
    
    @Test
    public void testIsSerializable() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        
        // Case when extraInterfaces is not null and contains Serializable interface
        Class<?>[] extraInterfaces = {Runnable.class, java.io.Serializable.class};
        mockSettings.extraInterfaces(extraInterfaces);
        assertTrue(mockSettings.isSerializable());
        
        // Case when extraInterfaces is null
        mockSettings.extraInterfaces(null);
        assertFalse(mockSettings.isSerializable());
        
        // Case when extraInterfaces does not contain Serializable interface
        Class<?>[] extraInterfacesWithoutSerializable = {Runnable.class, Comparable.class};
        mockSettings.extraInterfaces(extraInterfacesWithoutSerializable);
        assertFalse(mockSettings.isSerializable());
    }
    
    @Test
    public void testConstructor() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        assertNull(mockSettings.getMockName());
        assertNull(mockSettings.getExtraInterfaces());
        assertNull(mockSettings.getSpiedInstance());
        assertNull(mockSettings.getDefaultAnswer());
    }
}