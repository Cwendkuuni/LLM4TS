package org.mockito.internal.creation;

import static org.junit.Assert.*;

import org.mockito.Answer;
import org.mockito.MockSettings;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.MockName;

public class MockSettingsImplTest {

    @Test
    public void testSerializable() {
        MockSettings settings = new MockSettingsImpl();
        MockSettings serializableSettings = settings.serializable();
        assertTrue(serializableSettings instanceof MockSettingsImpl);
        assertTrue(serializableSettings.isSerializable());
    }

    @Test(expected = IllegalStateException.class)
    public void testSerializableThrowsExceptionWhenExtraInterfacesIsNotSet() {
        MockSettings settings = new MockSettingsImpl();
        settings.serializable();
    }

    @Test
    public void testExtraInterfacesNull() {
        MockSettings settings = new MockSettingsImpl();
        MockSettings settingsWithNullExtraInterfaces = settings.extraInterfaces(null);
        assertEquals(settings, settingsWithNullExtraInterfaces);
        assertNull(settingsWithNullExtraInterfaces.getExtraInterfaces());
    }

    @Test(expected = org.mockito.exceptions.Reporter.ExtraInterfacesRequiresAtLeastOneInterfaceException.class)
    public void testExtraInterfacesEmptyArray() {
        MockSettings settings = new MockSettingsImpl();
        settings.extraInterfaces(new Class[0]);
    }

    @Test(expected = org.mockito.exceptions.Reporter.ExtraInterfacesAcceptsOnlyInterfacesException.class)
    public void testExtraInterfacesWithNonInterfaceClass() {
        MockSettings settings = new MockSettingsImpl();
        settings.extraInterfaces(String.class);
    }

    @Test
    public void testExtraInterfacesMultipleInterfaces() {
        MockSettings settings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = { java.io.Serializable.class, java.util.List.class };
        MockSettings settingsWithExtraInterfaces = settings.extraInterfaces(extraInterfaces);
        assertEquals(settings, settingsWithExtraInterfaces);
        assertArrayEquals(extraInterfaces, settingsWithExtraInterfaces.getExtraInterfaces());
    }

    @Test
    public void testName() {
        MockSettings settings = new MockSettingsImpl();
        String name = "mockName";
        MockSettings settingsWithName = settings.name(name);
        assertEquals(settings, settingsWithName);
        assertEquals(name, settingsWithName.getName());
    }

    @Test
    public void testSpiedInstance() {
        MockSettings settings = new MockSettingsImpl();
        Object spiedInstance = new Object();
        MockSettings settingsWithSpiedInstance = settings.spiedInstance(spiedInstance);
        assertEquals(settings, settingsWithSpiedInstance);
        assertEquals(spiedInstance, settingsWithSpiedInstance.getSpiedInstance());
    }

    @Test
    public void testDefaultAnswer() {
        MockSettings settings = new MockSettingsImpl();
        Answer<Object> defaultAnswer = mock(Answer.class);
        MockSettings settingsWithDefaultAnswer = settings.defaultAnswer(defaultAnswer);
        assertEquals(settings, settingsWithDefaultAnswer);
        assertEquals(defaultAnswer, settingsWithDefaultAnswer.getDefaultAnswer());
    }

    @Test
    public void testIsSerializable() {
        MockSettings settings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = { java.io.Serializable.class };
        MockSettings settingsWithExtraInterfaces = settings.extraInterfaces(extraInterfaces);
        assertTrue(settingsWithExtraInterfaces.isSerializable());

        settings = new MockSettingsImpl();
        MockSettings settingsWithoutExtraInterfaces = settings;
        assertFalse(settingsWithoutExtraInterfaces.isSerializable());
    }

    @Test
    public void testInitiateMockName() {
        MockSettings settings = new MockSettingsImpl();
        Class<?> classToMock = MockitoTest.class;
        settings.initiateMockName(classToMock);
        assertNotNull(settings.getMockName());
    }
}