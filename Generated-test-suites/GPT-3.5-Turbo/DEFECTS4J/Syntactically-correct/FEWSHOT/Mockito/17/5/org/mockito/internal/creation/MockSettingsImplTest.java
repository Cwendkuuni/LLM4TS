package org.mockito.internal.creation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.Answer;

public class MockSettingsImplTest {
    private MockSettingsImpl mockSettings;

    @Before
    public void setup() {
        mockSettings = new MockSettingsImpl();
    }

    @Test
    public void testSerializable() {
        mockSettings.serializable();
        assertEquals(MockSettingsImpl.class, mockSettings.getClass());
    }

    @Test
    public void testExtraInterfaces() {
        Class<?>[] extraInterfaces = { TestInterface.class };
        mockSettings.extraInterfaces(extraInterfaces);
        assertEquals(extraInterfaces, mockSettings.getExtraInterfaces());
        
        Class<?>[] emptyInterfaces = {};
        mockSettings.extraInterfaces(emptyInterfaces);
        assertNull(mockSettings.getExtraInterfaces());
    }

    @Test
    public void testExtraInterfacesWithNull() {
        Class<?>[] extraInterfaces = { null };
        mockSettings.extraInterfaces(extraInterfaces);
        assertNull(mockSettings.getExtraInterfaces());
    }

    @Test
    public void testExtraInterfacesWithNonInterface() {
        Class<?>[] extraInterfaces = { TestInterfaceWithImplementation.class };
        mockSettings.extraInterfaces(extraInterfaces);
        assertNull(mockSettings.getExtraInterfaces());
    }

    @Test
    public void testName() {
        String name = "mock";
        mockSettings.name(name);
        assertEquals(name, mockSettings.getMockName());
    }

    @Test
    public void testSpiedInstance() {
        Object spiedInstance = new Object();
        mockSettings.spiedInstance(spiedInstance);
        assertEquals(spiedInstance, mockSettings.getSpiedInstance());
    }

    @Test
    public void testDefaultAnswer() {
        Answer<Object> defaultAnswer = (invocation) -> null;
        mockSettings.defaultAnswer(defaultAnswer);
        assertEquals(defaultAnswer, mockSettings.getDefaultAnswer());
    }

    @Test
    public void testDefaultAnswerWithNull() {
        mockSettings.defaultAnswer(null);
        assertNull(mockSettings.getDefaultAnswer());
    }

    @Test
    public void testIsSerializable() {
        Class<?>[] extraInterfaces = { TestInterface.class, java.io.Serializable.class };
        mockSettings.extraInterfaces(extraInterfaces);
        assertEquals(true, mockSettings.isSerializable());

        Class<?>[] extraInterfacesWithoutSerializable = { TestInterface.class };
        mockSettings.extraInterfaces(extraInterfacesWithoutSerializable);
        assertEquals(false, mockSettings.isSerializable());
    }

    @Test
    public void testInitiateMockName() {
        String name = "mock";
        mockSettings.initiateMockName(TestClass.class);
        assertEquals(name, mockSettings.getMockName().toString());
        assertNotEquals(null, mockSettings.getMockName().getMockType());
    }
    
    // Helper test interfaces and classes
    private interface TestInterface {}
    
    private interface TestInterfaceWithImplementation {
        void method();
    }
    
    private class TestClass {}
}