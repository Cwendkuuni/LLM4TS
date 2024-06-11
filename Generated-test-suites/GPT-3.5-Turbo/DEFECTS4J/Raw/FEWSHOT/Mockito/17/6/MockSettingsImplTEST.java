package org.mockito.internal.creation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

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
    public void testExtraInterfaces() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {java.io.Serializable.class};
        MockSettingsImpl result = mockSettings.extraInterfaces(extraInterfaces);
        assertEquals(extraInterfaces, result.getExtraInterfaces());
    }

    @Test
    public void testExtraInterfaces_null() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        try {
            mockSettings.extraInterfaces(null);
        } catch (Exception e) {
            assertTrue(e instanceof Reporter);
        }
    }

    @Test
    public void testExtraInterfaces_emptyArray() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        try {
            mockSettings.extraInterfaces(new Class<?>[]{});
        } catch (Exception e) {
            assertTrue(e instanceof Reporter);
        }
    }

    @Test
    public void testExtraInterfaces_notInterface() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {String.class};
        try {
            mockSettings.extraInterfaces(extraInterfaces);
        } catch (Exception e) {
            assertTrue(e instanceof Reporter);
        }
    }

    @Test
    public void testGetName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        String name = "mock";
        MockSettingsImpl result = mockSettings.name(name);
        assertEquals(name, result.getMockName().toString());
    }

    @Test
    public void testGetSpiedInstance() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Object spiedInstance = new Object();
        MockSettingsImpl result = mockSettings.spiedInstance(spiedInstance);
        assertEquals(spiedInstance, result.getSpiedInstance());
    }

    @Test
    public void testDefaultAnswer() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Answer<Object> defaultAnswer = null;
        MockSettingsImpl result = mockSettings.defaultAnswer(defaultAnswer);
        assertEquals(defaultAnswer, result.getDefaultAnswer());
    }

    @Test
    public void testIsSerializable_true() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {java.io.Serializable.class};
        mockSettings.extraInterfaces(extraInterfaces);
        assertTrue(mockSettings.isSerializable());
    }

    @Test
    public void testIsSerializable_false() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        assertFalse(mockSettings.isSerializable());
    }

    @Test
    public void testInitiateMockName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        String name = "MockName";
        Class<?> classToMock = Object.class;
        mockSettings.name(name);
        mockSettings.initiateMockName(classToMock);
        assertEquals(name, mockSettings.getMockName().toString());
    }
}