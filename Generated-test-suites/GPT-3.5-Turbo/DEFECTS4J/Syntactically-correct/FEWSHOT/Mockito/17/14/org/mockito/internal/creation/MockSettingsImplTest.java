package org.mockito.internal.creation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.Test;
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
        Class<?>[] interfaces = {Runnable.class, AutoCloseable.class};
        MockSettingsImpl result = mockSettings.extraInterfaces(interfaces);

        assertEquals(interfaces[0], result.getExtraInterfaces()[0]);
        assertEquals(interfaces[1], result.getExtraInterfaces()[1]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExtraInterfacesNull() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.extraInterfaces(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExtraInterfacesEmpty() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.extraInterfaces();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExtraInterfacesNonInterface() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.extraInterfaces(MockSettingsImpl.class);
    }

    @Test
    public void testGetMockName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        assertNull(mockSettings.getMockName());

        String name = "mockName";
        mockSettings.name(name);
        assertEquals(name, mockSettings.getMockName().toString());
    }

    @Test
    public void testGetExtraInterfaces() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] interfaces = {Runnable.class, AutoCloseable.class};
        mockSettings.extraInterfaces(interfaces);

        assertEquals(interfaces[0], mockSettings.getExtraInterfaces()[0]);
        assertEquals(interfaces[1], mockSettings.getExtraInterfaces()[1]);
    }

    @Test
    public void testGetSpiedInstance() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        assertNull(mockSettings.getSpiedInstance());

        Object spiedInstance = new Object();
        mockSettings.spiedInstance(spiedInstance);
        assertEquals(spiedInstance, mockSettings.getSpiedInstance());
    }

    @Test
    public void testGetName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        assertNull(mockSettings.getMockName());

        String name = "mockName";
        mockSettings.name(name);
        assertEquals(name, mockSettings.getMockName().toString());
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
        Class<?>[] interfaces = {Runnable.class, java.io.Serializable.class};
        mockSettings.extraInterfaces(interfaces);

        assertEquals(true, mockSettings.isSerializable());
    }

    @Test
    public void testIsSerializableFalse() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        assertEquals(false, mockSettings.isSerializable());
    }

    @Test
    public void testInitiateMockName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?> classToMock = Object.class;
        mockSettings.initiateMockName(classToMock);

        assertEquals(classToMock.getName(), mockSettings.getMockName().toString());
    }
}