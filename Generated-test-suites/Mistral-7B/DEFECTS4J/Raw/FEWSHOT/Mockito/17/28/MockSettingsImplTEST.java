package org.mockito.internal.creation;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.exceptions.base.MockitoException.classIsNotAnInterface;
import static org.mockito.exceptions.base.MockitoException.nullArgument;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockSettings;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.MockName;
import org.mockito.stubbing.Answer;

public class MockSettingsImplTest {

    private MockSettingsImpl mockSettings;

    @Before
    public void setUp() {
        mockSettings = new MockSettingsImpl();
    }

    @Test
    public void testSerializable() {
        MockSettings serializableMockSettings = mockSettings.serializable();
        assertTrue(serializableMockSettings.isSerializable());
    }

    @Test(expected = MockitoException.class)
    public void testSerializableNullExtraInterfaces() {
        MockSettings serializableMockSettings = mockSettings.serializable((Class<?>[]) null);
    }

    @Test(expected = MockitoException.class)
    public void testSerializableNoInterfaceExtraInterface() {
        MockSettings serializableMockSettings = mockSettings.serializable(String.class);
    }

    @Test
    public void testSerializableMultipleInterfaces() {
        Class<?>[] extraInterfaces = new Class[]{String.class, java.io.Serializable.class};
        MockSettings serializableMockSettings = mockSettings.serializable(extraInterfaces);
        assertTrue(serializableMockSettings.isSerializable());
    }

    @Test
    public void testGetMockName() {
        assertNull(mockSettings.getMockName());
        String name = "mockName";
        MockSettings namedMockSettings = mockSettings.name(name);
        MockName mockName = namedMockSettings.getMockName();
        assertSame(name, mockName.getName());
    }

    @Test
    public void testGetExtraInterfaces() {
        Class<?>[] extraInterfaces = new Class[]{String.class, java.io.Serializable.class};
        MockSettings extraInterfacesMockSettings = mockSettings.extraInterfaces(extraInterfaces);
        assertArrayEquals(extraInterfaces, extraInterfacesMockSettings.getExtraInterfaces());
    }

    @Test(expected = MockitoException.class)
    public void testGetExtraInterfacesNull() {
        mockSettings.getExtraInterfaces();
    }

    @Test(expected = MockitoException.class)
    public void testGetExtraInterfacesEmptyArray() {
        mockSettings.extraInterfaces(new Class<?>[0]);
        mockSettings.getExtraInterfaces();
    }

    @Test(expected = MockitoException.class)
    public void testGetExtraInterfacesNullInterface() {
        mockSettings.extraInterfaces(new Class<?>[]{null});
    }

    @Test(expected = MockitoException.class)
    public void testGetExtraInterfacesNonInterface() {
        mockSettings.extraInterfaces(String[].class);
    }

    @Test(expected = MockitoException.class)
    public void testGetExtraInterfacesInterfaceNotImplemented() {
        mockSettings.extraInterfaces(MockSettingsImpl.class);
    }

    @Test
    public void testSpiedInstance() {
        Object spiedInstance = new Object();
        MockSettings spiedInstanceMockSettings = mockSettings.spiedInstance(spiedInstance);
        assertSame(spiedInstance, spiedInstanceMockSettings.getSpiedInstance());
    }

    @Test(expected = NullPointerException.class)
    public void testSpiedInstanceNull() {
        mockSettings.spiedInstance(null);
    }

    @Test
    public void testDefaultAnswer() {
        Answer<Object> answer = org.mockito.internal.creation.AnswerUtils.returnMock();
        MockSettings defaultAnswerMockSettings = mockSettings.defaultAnswer(answer);
        Answer<Object> getDefaultAnswer = defaultAnswerMockSettings.getDefaultAnswer();
        assertSame(answer, getDefaultAnswer);
    }

    @Test(expected = NullPointerException.class)
    public void testDefaultAnswerNull() {
        mockSettings.defaultAnswer(null);
    }

    @Test
    public void testIsSerializable() {
        assertFalse(mockSettings.isSerializable());
        Class<?>[] extraInterfaces = new Class[]{java.io.Serializable.class};
        MockSettings serializableMockSettings = mockSettings.extraInterfaces(extraInterfaces);
        assertTrue(serializableMockSettings.isSerializable());
    }

    @Test
    public void testInitiateMockNameNullClassToMock() {
        mockSettings.initiateMockName(null);
    }

    @Test(expected = ClassCastException.class)
    public void testInitiateMockNameClassToMockNotMatch() {
        mockSettings.initiateMockName(String.class);
        try {
            mockSettings.initiateMockName((Class<?>) int.class);
        } catch (MockitoException e) {
            throw new AssertionError(classIsNotAnInterface.getMessage(), e);
        }
    }

    @Test
    public void testInitiateMockName() {
        String name = "mockName";
        Class<?> classToMock = String.class;
        mockSettings.initiateMockName(classToMock);
        MockName mockName = mockSettings.getMockName();
        assertSame(name, mockName.getName());
        assertSame(classToMock, mockName.getMockType());
    }
}