package org.mockito.internal.creation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class MockSettingsImplTest {
    private MockSettingsImpl mockSettings;

    @Before
    public void setUp() {
        mockSettings = new MockSettingsImpl();
    }

    @Test
    public void testSerializable() {
        MockSettingsImpl result = mockSettings.serializable();
        assertEquals(mockSettings, result);
        // Verify that extraInterfaces contains java.io.Serializable.class
        Class<?>[] extraInterfaces = result.getExtraInterfaces();
        assertNotNull(extraInterfaces);
        assertTrue(Arrays.asList(extraInterfaces).contains(java.io.Serializable.class));
    }

    @Test
    public void testExtraInterfaces_withNullExtraInterfaces() {
        Reporter reporter = mock(Reporter.class);
        doReturn(reporter).when(mockSettings).createReporter();

        MockSettingsImpl result = mockSettings.extraInterfaces((Class<?>[]) null);
        assertEquals(mockSettings, result);

        verify(reporter).extraInterfacesRequiresAtLeastOneInterface();
    }

    @Test
    public void testExtraInterfaces_withNullInterface() {
        Class<?>[] extraInterfaces = { null };
        Reporter reporter = mock(Reporter.class);
        doReturn(reporter).when(mockSettings).createReporter();

        MockSettingsImpl result = mockSettings.extraInterfaces(extraInterfaces);
        assertEquals(mockSettings, result);

        verify(reporter).extraInterfacesDoesNotAcceptNullParameters();
    }

    @Test
    public void testExtraInterfaces_withNonInterfaceClass() {
        Class<?>[] extraInterfaces = { Object.class };
        Reporter reporter = mock(Reporter.class);
        doReturn(reporter).when(mockSettings).createReporter();

        MockSettingsImpl result = mockSettings.extraInterfaces(extraInterfaces);
        assertEquals(mockSettings, result);

        verify(reporter).extraInterfacesAcceptsOnlyInterfaces(Object.class);
    }

    @Test
    public void testExtraInterfaces_withValidInterfaces() {
        Class<?>[] extraInterfaces = { Runnable.class, Serializable.class };
        MockSettingsImpl result = mockSettings.extraInterfaces(extraInterfaces);
        assertEquals(mockSettings, result);
        assertArrayEquals(extraInterfaces, result.getExtraInterfaces());
    }

    @Test
    public void testGetMockName() {
        MockName mockName = mock(MockName.class);
        mockSettings.mockName = mockName;

        MockName result = mockSettings.getMockName();

        assertEquals(mockName, result);
    }

    @Test
    public void testGetExtraInterfaces() {
        Class<?>[] extraInterfaces = { Runnable.class, Serializable.class };
        mockSettings.extraInterfaces = extraInterfaces;

        Class<?>[] result = mockSettings.getExtraInterfaces();

        assertArrayEquals(extraInterfaces, result);
    }

    @Test
    public void testGetSpiedInstance() {
        Object spiedInstance = new Object();
        mockSettings.spiedInstance = spiedInstance;

        Object result = mockSettings.getSpiedInstance();

        assertEquals(spiedInstance, result);
    }

    @Test
    public void testName() {
        String name = "mockName";
        MockSettingsImpl result = mockSettings.name(name);
        assertEquals(mockSettings, result);
        assertEquals(name, result.name);
    }

    @Test
    public void testSpiedInstance() {
        Object spiedInstance = new Object();
        MockSettingsImpl result = mockSettings.spiedInstance(spiedInstance);
        assertEquals(mockSettings, result);
        assertEquals(spiedInstance, result.spiedInstance);
    }

    @Test
    public void testDefaultAnswer() {
        Answer<Object> defaultAnswer = mock(Answer.class);
        MockSettingsImpl result = mockSettings.defaultAnswer(defaultAnswer);
        assertEquals(mockSettings, result);
        assertEquals(defaultAnswer, result.defaultAnswer);
    }

    @Test
    public void testGetDefaultAnswer() {
        Answer<Object> defaultAnswer = mock(Answer.class);
        mockSettings.defaultAnswer = defaultAnswer;

        Answer<Object> result = mockSettings.getDefaultAnswer();

        assertEquals(defaultAnswer, result);
    }

    @Test
    public void testIsSerializable_withNullExtraInterfaces() {
        mockSettings.extraInterfaces = null;

        boolean result = mockSettings.isSerializable();

        assertFalse(result);
    }

    @Test
    public void testIsSerializable_withSerializableExtraInterface() {
        Class<?>[] extraInterfaces = { Serializable.class };
        mockSettings.extraInterfaces = extraInterfaces;

        boolean result = mockSettings.isSerializable();

        assertTrue(result);
    }

    @Test
    public void testIsSerializable_withoutSerializableExtraInterface() {
        Class<?>[] extraInterfaces = { Runnable.class };
        mockSettings.extraInterfaces = extraInterfaces;

        boolean result = mockSettings.isSerializable();

        assertFalse(result);
    }

    @Test
    public void testInitiateMockName() {
        String name = "mockName";
        Class<?> classToMock = Object.class;
        MockName mockName = mock(MockName.class);
        doReturn(mockName).when(mockSettings).createMockName(name, classToMock);

        mockSettings.initiateMockName(classToMock);

        assertEquals(mockName, mockSettings.mockName);
        verify(mockSettings).createMockName(name, classToMock);
    }
}