package org.mockito.internal.creation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class MockSettingsImplTest {
    
    @Test
    public void testSerializable() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockSettingsImpl serializableSettings = mockSettings.serializable();
        
        assertEquals(java.io.Serializable.class, serializableSettings.getExtraInterfaces()[0]);
    }
    
    @Test
    public void testExtraInterfaces() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {java.util.List.class, java.util.Set.class};
        MockSettingsImpl settingsWithExtraInterfaces = mockSettings.extraInterfaces(extraInterfaces);
        
        assertEquals(extraInterfaces, settingsWithExtraInterfaces.getExtraInterfaces());
    }
    
    @Test
    public void testExtraInterfacesWithNull() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = null;
        MockSettingsImpl settingsWithExtraInterfaces = mockSettings.extraInterfaces(extraInterfaces);
        
        assertNull(settingsWithExtraInterfaces.getExtraInterfaces());
    }
    
    @Test
    public void testExtraInterfacesWithEmptyArray() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {};
        MockSettingsImpl settingsWithExtraInterfaces = mockSettings.extraInterfaces(extraInterfaces);
        
        assertNull(settingsWithExtraInterfaces.getExtraInterfaces());
    }
    
    @Test(expected=Reporter.extraInterfacesRequiresAtLeastOneInterface)
    public void testExtraInterfacesRequiresAtLeastOneInterface() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.extraInterfaces();
    }
    
    @Test
    public void testExtraInterfacesWithNullParameter() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {null};
        MockSettingsImpl settingsWithExtraInterfaces = mockSettings.extraInterfaces(extraInterfaces);
        
        assertNull(settingsWithExtraInterfaces.getExtraInterfaces());
    }
    
    @Test(expected=Reporter.extraInterfacesDoesNotAcceptNullParameters)
    public void testExtraInterfacesDoesNotAcceptNullParameters() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {java.util.List.class, null};
        mockSettings.extraInterfaces(extraInterfaces);
        assertNull(settingsWithExtraInterfaces.getExtraInterfaces());
    }
    
    @Test(expected=Reporter.extraInterfacesAcceptsOnlyInterfaces)
    public void testExtraInterfacesAcceptsOnlyInterfaces() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {java.util.ArrayList.class};
        mockSettings.extraInterfaces(extraInterfaces);
        assertNull(settingsWithExtraInterfaces.getExtraInterfaces());
    }
    
    @Test
    public void testGetMockName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.name("mockName");
        
        assertEquals("mockName", mockSettings.getMockName());
    }
    
    @Test
    public void testGetExtraInterfaces() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {java.util.List.class};
        mockSettings.extraInterfaces(extraInterfaces);
        
        assertEquals(extraInterfaces, mockSettings.getExtraInterfaces());
    }
    
    @Test
    public void testGetSpiedInstance() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Object spiedInstance = new Object();
        mockSettings.spiedInstance(spiedInstance);
        
        assertEquals(spiedInstance, mockSettings.getSpiedInstance());
    }
    
    @Test
    public void testDefaultAnswer() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Answer defaultAnswer = new Answer() {
            public Object answer(InvocationOnMock invocation) {
                return null;
            }
        };
        mockSettings.defaultAnswer(defaultAnswer);
        
        assertEquals(defaultAnswer, mockSettings.getDefaultAnswer());
    }
    
    @Test
    public void testIsSerializableWithExtraInterfaces() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {java.util.List.class, java.io.Serializable.class};
        mockSettings.extraInterfaces(extraInterfaces);
        
        assertTrue(mockSettings.isSerializable());
    }
    
    @Test
    public void testIsSerializableWithoutExtraInterfaces() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        
        assertFalse(mockSettings.isSerializable());
    }
    
    @Test
    public void testInitiateMockName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class classToMock = java.util.ArrayList.class;
        mockSettings.initiateMockName(classToMock);
        
        assertEquals(new MockName(null, classToMock), mockSettings.getMockName());
    }
}