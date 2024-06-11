package org.mockito.internal.creation;

import org.junit.Test;
import org.mockito.MockSettings;
import org.mockito.internal.util.MockName;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockSettingsImplTest {

    @Test
    public void testSerializable() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockSettings result = mockSettings.serializable();

        assertTrue(result instanceof MockSettingsImpl);
        assertArrayEquals(new Class[]{java.io.Serializable.class}, mockSettings.getExtraInterfaces());
        assertTrue(mockSettings.isSerializable());
    }

    @Test
    public void testExtraInterfaces() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockSettings result = mockSettings.extraInterfaces(Runnable.class, Comparable.class);

        assertTrue(result instanceof MockSettingsImpl);
        assertArrayEquals(new Class[]{Runnable.class, Comparable.class}, mockSettings.getExtraInterfaces());
        assertFalse(mockSettings.isSerializable());
    }

    @Test(expected = NullPointerException.class)
    public void testExtraInterfacesNull() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.extraInterfaces((Class[]) null);
    }

    @Test(expected = Reporter.class)
    public void testExtraInterfacesNoInterface() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.extraInterfaces(String.class);
    }

    @Test
    public void testGetMockName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockName mockName = new MockName("test", Object.class);
        mockSettings.initiateMockName(Object.class);

        assertEquals(mockName, mockSettings.getMockName());
    }

    @Test
    public void testGetExtraInterfaces() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {Runnable.class, Comparable.class};
        mockSettings.extraInterfaces(extraInterfaces);

        assertArrayEquals(extraInterfaces, mockSettings.getExtraInterfaces());
    }

    @Test
    public void testGetSpiedInstance() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Object spiedInstance = new Object();
        mockSettings.spiedInstance(spiedInstance);

        assertEquals(spiedInstance, mockSettings.getSpiedInstance());
    }

    @Test
    public void testName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockSettings result = mockSettings.name("test");

        assertTrue(result instanceof MockSettingsImpl);
        assertEquals("test", mockSettings.getMockName().toString());
    }

    @Test
    public void testSpiedInstance() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Object spiedInstance = new Object();
        MockSettings result = mockSettings.spiedInstance(spiedInstance);

        assertTrue(result instanceof MockSettingsImpl);
        assertEquals(spiedInstance, mockSettings.getSpiedInstance());
    }

    @Test
    public void testDefaultAnswer() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Answer<Object> defaultAnswer = mock(Answer.class);
        MockSettings result = mockSettings.defaultAnswer(defaultAnswer);

        assertTrue(result instanceof MockSettingsImpl);
        assertEquals(defaultAnswer, mockSettings.getDefaultAnswer());
    }

    @Test
    public void testIsSerializable() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.extraInterfaces(java.io.Serializable.class);

        assertTrue(mockSettings.isSerializable());
    }
}