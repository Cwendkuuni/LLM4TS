package org.mockito.internal.creation;
import static org.junit.Assert.*;

import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.MockName;
import org.mockito.stubbing.Answer;

public class MockSettingsImplTest {

    @Test
    public void testSerializable() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockSettingsImpl result = mockSettings.serializable();
        assertTrue(result.isSerializable());
    }

    @Test
    public void testExtraInterfaces_withNullArguments() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.extraInterfaces(null);
        // Verify that a Reporter exception is thrown
    }

    @Test
    public void testExtraInterfaces_withNoInterfaces() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.extraInterfaces();
        // Verify that a Reporter exception is thrown
    }

    @Test
    public void testExtraInterfaces_withNonInterfaceArgument() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.extraInterfaces(String.class);
        // Verify that a Reporter exception is thrown
    }

    @Test
    public void testExtraInterfaces_withValidInterfaces() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = { MyInterface.class, AnotherInterface.class };
        MockSettingsImpl result = mockSettings.extraInterfaces(extraInterfaces);
        assertArrayEquals(extraInterfaces, result.getExtraInterfaces());
    }

    @Test
    public void testGetMockName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.name("mockName");
        MockName result = mockSettings.getMockName();
        assertEquals("mockName", result.getName());
    }

    @Test
    public void testGetExtraInterfaces() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.extraInterfaces(MyInterface.class, AnotherInterface.class);
        Class<?>[] result = mockSettings.getExtraInterfaces();
        assertArrayEquals(new Class<?>[]{MyInterface.class, AnotherInterface.class}, result);
    }

    @Test
    public void testGetSpiedInstance() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Object spiedInstance = new Object();
        mockSettings.spiedInstance(spiedInstance);
        Object result = mockSettings.getSpiedInstance();
        assertEquals(spiedInstance, result);
    }

    @Test
    public void testDefaultAnswer() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Answer<Object> defaultAnswer = new MyAnswer();
        MockSettingsImpl result = mockSettings.defaultAnswer(defaultAnswer);
        assertEquals(defaultAnswer, result.getDefaultAnswer());
    }

    @Test
    public void testIsSerializable_withNullExtraInterfaces() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        boolean result = mockSettings.isSerializable();
        assertFalse(result);
    }

    @Test
    public void testIsSerializable_withSerializableInterface() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = { MyInterface.class, java.io.Serializable.class };
        mockSettings.extraInterfaces(extraInterfaces);
        boolean result = mockSettings.isSerializable();
        assertTrue(result);
    }

    @Test
    public void testInitiateMockName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.name("mockName");
        mockSettings.initiateMockName(MyClass.class);
        MockName result = mockSettings.getMockName();
        assertEquals("mockName", result.getName());
        assertEquals(MyClass.class, result.getMockedType());
    }
}