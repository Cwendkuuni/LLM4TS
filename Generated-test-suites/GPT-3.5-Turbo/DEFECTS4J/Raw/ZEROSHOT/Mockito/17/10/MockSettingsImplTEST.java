package org.mockito.internal.creation;

import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.MockName;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;

public class MockSettingsImplTest {

    @Test
    public void testSerializable() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockSettings result = mockSettings.serializable();
        assertTrue(result.isSerializable());
    }

    @Test
    public void testExtraInterfaces_withNullParameter() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Reporter reporterMock = mock(Reporter.class);

        mockSettings.extraInterfaces(null);

        verify(reporterMock, times(1)).extraInterfacesRequiresAtLeastOneInterface();
        assertNull(mockSettings.getExtraInterfaces());
    }

    @Test
    public void testExtraInterfaces_withNoInterfaces() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Reporter reporterMock = mock(Reporter.class);

        mockSettings.extraInterfaces();

        verify(reporterMock, times(1)).extraInterfacesRequiresAtLeastOneInterface();
        assertNull(mockSettings.getExtraInterfaces());
    }

    @Test
    public void testExtraInterfaces_withNonInterfaceParameter() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Reporter reporterMock = mock(Reporter.class);

        mockSettings.extraInterfaces(String.class);

        verify(reporterMock, times(1)).extraInterfacesAcceptsOnlyInterfaces(String.class);
        assertNull(mockSettings.getExtraInterfaces());
    }

    @Test
    public void testExtraInterfaces_withValidInterface() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = new Class[]{Runnable.class};

        mockSettings.extraInterfaces(extraInterfaces);

        assertArrayEquals(extraInterfaces, mockSettings.getExtraInterfaces());
    }

    @Test
    public void testGetMockName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockName mockNameMock = mock(MockName.class);

        mockSettings.setMockName(mockNameMock);

        assertEquals(mockNameMock, mockSettings.getMockName());
    }

    @Test
    public void testGetExtraInterfaces() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = new Class[]{Runnable.class};

        mockSettings.setExtraInterfaces(extraInterfaces);

        assertArrayEquals(extraInterfaces, mockSettings.getExtraInterfaces());
    }

    @Test
    public void testGetSpiedInstance() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Object spiedInstance = new Object();

        mockSettings.setSpiedInstance(spiedInstance);

        assertEquals(spiedInstance, mockSettings.getSpiedInstance());
    }

    @Test
    public void testName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        String name = "mockName";

        mockSettings.name(name);

        assertEquals(name, mockSettings.getName());
    }

    @Test
    public void testSpiedInstance() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Object spiedInstance = new Object();

        mockSettings.spiedInstance(spiedInstance);

        assertEquals(spiedInstance, mockSettings.getSpiedInstance());
    }

    @Test
    public void testDefaultAnswer() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Answer<Object> defaultAnswer = mock(Answer.class);

        mockSettings.defaultAnswer(defaultAnswer);

        assertEquals(defaultAnswer, mockSettings.getDefaultAnswer());
    }

    @Test
    public void testIsSerializable_withExtraInterfaces() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = new Class[]{Runnable.class, java.io.Serializable.class};
        mockSettings.setExtraInterfaces(extraInterfaces);

        assertTrue(mockSettings.isSerializable());
    }

    @Test
    public void testIsSerializable_withoutExtraInterfaces() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();

        assertFalse(mockSettings.isSerializable());
    }

    @Test
    public void testInitiateMockName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?> classToMock = String.class;
        MockName mockNameMock = mock(MockName.class);
        mockSettings.setMockName(mockNameMock);

        mockSettings.initiateMockName(classToMock);

        verify(mockNameMock).setTestClass(classToMock);
    }
}