package org.mockito.internal.creation;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.exceptions.base.MockitoException.classNotFound;

import org.junit.Test;
import org.mockito.MockSettings;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.MockName;
import org.mockito.stubbing.Answer;

public class MockSettingsImplTest {

    @Test
    public void testExtraInterfaces_null() {
        MockSettings settings = new MockSettingsImpl();
        assertThrows(Reporter.class, () -> settings.extraInterfaces(null));
    }

    @Test
    public void testExtraInterfaces_empty() {
        MockSettings settings = new MockSettingsImpl();
        assertThrows(Reporter.class, () -> settings.extraInterfaces(new Class[0]));
    }

    @Test
    public void testExtraInterfaces_nullInterface() {
        MockSettings settings = new MockSettingsImpl();
        assertThrows(Reporter.class, () -> settings.extraInterfaces(null));
    }

    @Test
    public void testExtraInterfaces_nonInterface() {
        MockSettings settings = new MockSettingsImpl();
        assertThrows(Reporter.class, () -> settings.extraInterfaces(String.class));
    }

    @Test
    public void testExtraInterfaces_multiple() {
        MockSettings settings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {String.class, Integer.class};
        MockSettings settingsWithExtraInterfaces = settings.extraInterfaces(extraInterfaces);
        assertEquals(settingsWithExtraInterfaces, settings.extraInterfaces(extraInterfaces));
        assertArrayEquals(extraInterfaces, settingsWithExtraInterfaces.getExtraInterfaces());
    }

    @Test
    public void testGetExtraInterfaces() {
        MockSettings settings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {String.class, Integer.class};
        MockSettings settingsWithExtraInterfaces = settings.extraInterfaces(extraInterfaces);
        assertEquals(settingsWithExtraInterfaces.getExtraInterfaces(), extraInterfaces);
    }

    @Test
    public void testIsSerializable_noExtraInterfaces() {
        MockSettings settings = new MockSettingsImpl();
        assertFalse(settings.isSerializable());
    }

    @Test
    public void testIsSerializable_serializableExtraInterface() {
        MockSettings settings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {java.io.Serializable.class};
        MockSettings settingsWithSerializableExtraInterface = settings.extraInterfaces(extraInterfaces);
        assertTrue(settingsWithSerializableExtraInterface.isSerializable());
    }

    @Test
    public void testIsSerializable_nonSerializableExtraInterface() {
        MockSettings settings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {String.class};
        MockSettings settingsWithNonSerializableExtraInterface = settings.extraInterfaces(extraInterfaces);
        assertFalse(settingsWithNonSerializableExtraInterface.isSerializable());
    }

    @Test
    public void testIsSerializable_mixedExtraInterfaces() {
        MockSettings settings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {java.io.Serializable.class, String.class};
        MockSettings settingsWithMixedExtraInterfaces = settings.extraInterfaces(extraInterfaces);
        assertTrue(settingsWithMixedExtraInterfaces.isSerializable());
    }

    @Test
    public void testName_null() {
        MockSettings settings = new MockSettingsImpl();
        assertThrows(Reporter.class, () -> settings.name(null));
    }

    @Test
    public void testName_emptyString() {
        MockSettings settings = new MockSettingsImpl();
        settings.name("");
        assertNull(settings.name);
    }

    @Test
    public void testName_nonEmptyString() {
        MockSettings settings = new MockSettingsImpl();
        String name = "mockName";
        settings.name(name);
        assertEquals(name, settings.name);
    }

    @Test
    public void testGetName() {
        MockSettings settings = new MockSettingsImpl();
        String name = "mockName";
        MockSettings settingsWithName = settings.name(name);
        assertEquals(settingsWithName.name, name);
    }

    @Test
    public void testSpiedInstance_null() {
        MockSettings settings = new MockSettingsImpl();
        assertThrows(Reporter.class, () -> settings.spiedInstance(null));
    }

    @Test
    public void testSpiedInstance_notMockableType() {
        MockSettings settings = new MockSettingsImpl();
        assertThrows(Reporter.class, () -> settings.spiedInstance(new Object() {}));
    }

    @Test
    public void testSpiedInstance_mockableType() {
        MockSettings settings = new MockSettingsImpl();
        Object spiedInstance = new Object() {};
        MockSettings settingsWithSpiedInstance = settings.spiedInstance(spiedInstance);
        assertEquals(settingsWithSpiedInstance.getSpiedInstance(), spiedInstance);
    }

    @Test
    public void testGetSpiedInstance() {
        MockSettings settings = new MockSettingsImpl();
        Object spiedInstance = new Object() {};
        MockSettings settingsWithSpiedInstance = settings.spiedInstance(spiedInstance);
        assertEquals(settingsWithSpiedInstance.getSpiedInstance(), spiedInstance);
    }

    @Test
    public void testDefaultAnswer_null() {
        MockSettings settings = new MockSettingsImpl();
        assertThrows(Reporter.class, () -> settings.defaultAnswer(null));
    }

    @Test
    public void testDefaultAnswer() {
        MockSettings settings = new MockSettingsImpl();
        Answer<Object> defaultAnswer = mock(Answer.class);
        MockSettings settingsWithDefaultAnswer = settings.defaultAnswer(defaultAnswer);
        assertEquals(settingsWithDefaultAnswer.getDefaultAnswer(), defaultAnswer);
    }

    @Test
    public void testGetDefaultAnswer() {
        MockSettings settings = new MockSettingsImpl();
        Answer<Object> defaultAnswer = mock(Answer.class);
        settings.defaultAnswer(defaultAnswer);
        assertEquals(settings.getDefaultAnswer(), defaultAnswer);
    }

    @Test
    public void testInitiateMockName_nullClassToMock() {
        MockSettings settings = new MockSettingsImpl();
        assertThrows(ClassNotFoundException.class, () -> settings.initiateMockName(null));
    }

    @Test
    public void testInitiateMockName() {
        MockSettings settings = new MockSettingsImpl();
        Class<?> classToMock = String.class;
        settings.initiateMockName(classToMock);
        assertNotNull(settings.mockName);
    }

    @Test
    public void testGetMockName() {
        MockSettings settings = new MockSettingsImpl();
        Class<?> classToMock = String.class;
        settings.initiateMockName(classToMock);
        assertEquals(settings.getMockName(), settings.mockName);
    }
}