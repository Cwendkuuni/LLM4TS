package org.mockito.internal.creation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.stubbing.Answer;

public class MockSettingsImplTest {

    private MockSettingsImpl mockSettings;

    @Before
    public void setup() {
        mockSettings = new MockSettingsImpl();
    }

    @Test
    public void testSerializable() {
        MockSettingsImpl settings = mockSettings.serializable();
        Class<?>[] extraInterfaces = settings.getExtraInterfaces();
        assertNotNull(extraInterfaces);
        assertEquals(1, extraInterfaces.length);
        assertEquals(java.io.Serializable.class, extraInterfaces[0]);
    }

    @Test
    public void testExtraInterfaces() {
        Class<?>[] extraInterfaces = { Runnable.class, Comparable.class };
        MockSettingsImpl settings = mockSettings.extraInterfaces(extraInterfaces);
        Class<?>[] returnedInterfaces = settings.getExtraInterfaces();
        assertNotNull(returnedInterfaces);
        assertEquals(2, returnedInterfaces.length);
        assertEquals(Runnable.class, returnedInterfaces[0]);
        assertEquals(Comparable.class, returnedInterfaces[1]);
    }

    @Test(expected = Reporter.class)
    public void testExtraInterfacesWithNullArray() {
        mockSettings.extraInterfaces((Class<?>[]) null);
    }

    @Test(expected = Reporter.class)
    public void testExtraInterfacesWithNullInterface() {
        Class<?>[] extraInterfaces = { null, Comparable.class };
        mockSettings.extraInterfaces(extraInterfaces);
    }

    @Test(expected = Reporter.class)
    public void testExtraInterfacesWithNonInterface() {
        Class<?>[] extraInterfaces = { Runnable.class, String.class };
        mockSettings.extraInterfaces(extraInterfaces);
    }

    @Test
    public void testMockName() {
        String name = "mockName";
        MockSettingsImpl settings = mockSettings.name(name);
        MockName mockName = settings.getMockName();
        assertNotNull(mockName);
        assertEquals(name, mockName.toString());
    }

    @Test
    public void testSpiedInstance() {
        Object spiedInstance = mock(Object.class);
        MockSettingsImpl settings = mockSettings.spiedInstance(spiedInstance);
        Object returnedInstance = settings.getSpiedInstance();
        assertEquals(spiedInstance, returnedInstance);
    }

    @Test
    public void testDefaultAnswer() {
        Answer<Object> defaultAnswer = mock(Answer.class);
        MockSettingsImpl settings = mockSettings.defaultAnswer(defaultAnswer);
        Answer<Object> returnedAnswer = settings.getDefaultAnswer();
        assertEquals(defaultAnswer, returnedAnswer);
    }

    @Test
    public void testIsSerializable() {
        Class<?>[] extraInterfaces = { Runnable.class, java.io.Serializable.class, Comparable.class };
        MockSettingsImpl settings = mockSettings.extraInterfaces(extraInterfaces);
        assertTrue(settings.isSerializable());
    }

    @Test
    public void testIsSerializableFalse() {
        MockSettingsImpl settings = new MockSettingsImpl();
        assertFalse(settings.isSerializable());
    }

    @Test
    public void testInitiateMockName() {
        Class<?> classToMock = Object.class;
        String name = "mockName";
        MockSettingsImpl settings = mockSettings.name(name);
        settings.initiateMockName(classToMock);
        MockName mockName = settings.getMockName();
        assertNotNull(mockName);
        assertEquals(name, mockName.toString());
    }

}