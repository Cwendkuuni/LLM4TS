package org.mockito.internal.creation;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.exceptions.base.MockitoException.classIsNotAnInterface;
import static org.mockito.exceptions.base.MockitoException.nullArgument;

import org.junit.Test;
import org.mockito.MockSettings;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.MockName;
import org.mockito.stubbing.Answer;

public class MockSettingsImplTest {

    @Test
    public void testSerializableMockSettings_default() {
        MockSettings mockSettings = new MockSettingsImpl();
        assertNull(mockSettings.getExtraInterfaces());
        assertNull(mockSettings.getName());
        assertNull(mockSettings.getSpiedInstance());
        assertNull(mockSettings.getDefaultAnswer());
        assertNull(mockSettings.getMockName());
        assertFalse(mockSettings.isSerializable());
    }

    @Test
    public void testSerializableMockSettings_name() {
        String name = "MockName";
        MockSettings mockSettings = new MockSettingsImpl().name(name);
        assertEquals(name, mockSettings.getName());
        assertNull(mockSettings.getExtraInterfaces());
        assertNull(mockSettings.getSpiedInstance());
        assertNull(mockSettings.getDefaultAnswer());
        assertNull(mockSettings.getMockName());
        assertFalse(mockSettings.isSerializable());
    }

    @Test
    public void testSerializableMockSettings_spiedInstance() {
        Object spiedInstance = new Object();
        MockSettings mockSettings = new MockSettingsImpl().spiedInstance(spiedInstance);
        assertEquals(spiedInstance, mockSettings.getSpiedInstance());
        assertNull(mockSettings.getName());
        assertNull(mockSettings.getExtraInterfaces());
        assertNull(mockSettings.getDefaultAnswer());
        assertNull(mockSettings.getMockName());
        assertFalse(mockSettings.isSerializable());
    }

    @Test
    public void testSerializableMockSettings_defaultAnswer() {
        Answer<Object> defaultAnswer = arg -> arg.toString();
        MockSettings mockSettings = new MockSettingsImpl().defaultAnswer(defaultAnswer);
        assertEquals(defaultAnswer, mockSettings.getDefaultAnswer());
        assertNull(mockSettings.getName());
        assertNull(mockSettings.getSpiedInstance());
        assertNull(mockSettings.getExtraInterfaces());
        assertNull(mockSettings.getMockName());
        assertFalse(mockSettings.isSerializable());
    }

    @Test
    public void testSerializableMockSettings_name_spiedInstance() {
        String name = "MockName";
        Object spiedInstance = new Object();
        MockSettings mockSettings = new MockSettingsImpl().name(name).spiedInstance(spiedInstance);
        assertEquals(name, mockSettings.getName());
        assertEquals(spiedInstance, mockSettings.getSpiedInstance());
        assertNull(mockSettings.getExtraInterfaces());
        assertNull(mockSettings.getDefaultAnswer());
        assertNull(mockSettings.getMockName());
        assertFalse(mockSettings.isSerializable());
    }

    @Test
    public void testSerializableMockSettings_extraInterfaces() {
        Class<?>[] extraInterfaces = {Serializable.class};
        MockSettings mockSettings = new MockSettingsImpl().extraInterfaces(extraInterfaces);
        assertArrayEquals(extraInterfaces, mockSettings.getExtraInterfaces());
        assertNull(mockSettings.getName());
        assertNull(mockSettings.getSpiedInstance());
        assertNull(mockSettings.getDefaultAnswer());
        assertNull(mockSettings.getMockName());
        assertTrue(mockSettings.isSerializable());
    }

    @Test(expected = Reporter.class)
    public void testSerializableMockSettings_nullExtraInterfaces() {
        new MockSettingsImpl().extraInterfaces(null);
    }

    @Test(expected = Reporter.class)
    public void testSerializableMockSettings_emptyExtraInterfaces() {
        new MockSettingsImpl().extraInterfaces(new Class[0]);
    }

    @Test(expected = Reporter.class)
    public void testSerializableMockSettings_nonInterfaceExtraInterface() {
        Class nonInterfaceClass = String.class;
        new MockSettingsImpl().extraInterfaces(nonInterfaceClass);
    }

    @Test
    public void testSerializableMockSettings_extraInterfaces_spiedInstance() {
        Class<?>[] extraInterfaces = {Serializable.class};
        Object spiedInstance = new Object();
        MockSettings mockSettings = new MockSettingsImpl().extraInterfaces(extraInterfaces).spiedInstance(spiedInstance);
        assertArrayEquals(extraInterfaces, mockSettings.getExtraInterfaces());
        assertEquals(spiedInstance, mockSettings.getSpiedInstance());
        assertNull(mockSettings.getName());
        assertNull(mockSettings.getDefaultAnswer());
        assertNull(mockSettings.getMockName());
        assertTrue(mockSettings.isSerializable());
    }

    @Test
    public void testSerializableMockSettings_extraInterfaces_name() {
        Class<?>[] extraInterfaces = {Serializable.class};
        String name = "MockName";
        MockSettings mockSettings = new MockSettingsImpl().extraInterfaces(extraInterfaces).name(name);
        assertArrayEquals(extraInterfaces, mockSettings.getExtraInterfaces());
        assertEquals(name, mockSettings.getName());
        assertNull(mockSettings.getSpiedInstance());
        assertNull(mockSettings.getDefaultAnswer());
        assertNull(mockSettings.getMockName());
        assertTrue(mockSettings.isSerializable());
    }

    @Test
    public void testSerializableMockSettings_extraInterfaces_name_spiedInstance() {
        Class<?>[] extraInterfaces = {Serializable.class};
        String name = "MockName";
        Object spiedInstance = new Object();
        MockSettings mockSettings = new MockSettingsImpl().extraInterfaces(extraInterfaces).name(name).spiedInstance(spiedInstance);
        assertArrayEquals(extraInterfaces, mockSettings.getExtraInterfaces());
        assertEquals(name, mockSettings.getName());
        assertEquals(spiedInstance, mockSettings.getSpiedInstance());
        assertNull(mockSettings.getDefaultAnswer());
        assertNull(mockSettings.getMockName());
        assertTrue(mockSettings.isSerializable());
    }

    @Test
    public void testSerializableMockSettings_extraInterfaces_defaultAnswer() {
        Class<?>[] extraInterfaces = {Serializable.class};
        Answer<Object> defaultAnswer = arg -> arg.toString();
        MockSettings mockSettings = new MockSettingsImpl().extraInterfaces(extraInterfaces).defaultAnswer(defaultAnswer);
        assertArrayEquals(extraInterfaces, mockSettings.getExtraInterfaces());
        assertNull(mockSettings.getName());
        assertNull(mockSettings.getSpiedInstance());
        assertEquals(defaultAnswer, mockSettings.getDefaultAnswer());
        assertNull(mockSettings.getMockName());
        assertTrue(mockSettings.isSerializable());
    }

    @Test
    public void testSerializableMockSettings_extraInterfaces_name_defaultAnswer() {
        Class<?>[] extraInterfaces = {Serializable.class};
        String name = "MockName";
        Answer<Object> defaultAnswer = arg -> arg.toString();
        MockSettings mockSettings = new MockSettingsImpl().extraInterfaces(extraInterfaces).name(name).defaultAnswer(defaultAnswer);
        assertArrayEquals(extraInterfaces, mockSettings.getExtraInterfaces());
        assertEquals(name, mockSettings.getName());
        assertEquals(defaultAnswer, mockSettings.getDefaultAnswer());
        assertNull(mockSettings.getSpiedInstance());
        assertNull(mockSettings.getMockName());
        assertTrue(mockSettings.isSerializable());
    }

    @Test
    public void testSerializableMockSettings_extraInterfaces_name_spiedInstance_defaultAnswer() {
        Class<?>[] extraInterfaces = {Serializable.class};
        String name = "MockName";
        Object spiedInstance = new Object();
        Answer<Object> defaultAnswer = arg -> arg.toString();
        MockSettings mockSettings = new MockSettingsImpl().extraInterfaces(extraInterfaces).name(name).spiedInstance(spiedInstance).defaultAnswer(defaultAnswer);
        assertArrayEquals(extraInterfaces, mockSettings.getExtraInterfaces());
        assertEquals(name, mockSettings.getName());
        assertEquals(spiedInstance, mockSettings.getSpiedInstance());
        assertEquals(defaultAnswer, mockSettings.getDefaultAnswer());
        assertNull(mockSettings.getMockName());
        assertTrue(mockSettings.isSerializable());
    }

    @Test
    public void testMockSettings_initiateMockName() {
        Class classToMock = String.class;
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.initiateMockName(classToMock);
        assertNotNull(mockSettings.getMockName());
        assertEquals(classToMock.getName(), mockSettings.getMockName().getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMockSettings_initiateMockName_nullClassToMock() {
        new MockSettingsImpl().initiateMockName(null);
    }
}