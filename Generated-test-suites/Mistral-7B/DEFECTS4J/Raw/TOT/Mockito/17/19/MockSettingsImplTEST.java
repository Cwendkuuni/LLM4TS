package org.mockito.internal.creation;

import org.junit.Test;
import org.mockito.Answer;
import org.mockito.ArgumentMatchers;
import org.mockito.MockSettings;
import org.mockito.exceptions.Reporter;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockSettingsImplTest {

    @Test
    public void testSerializableMockSettings() {
        // Given
        Class<?>[] extraInterfaces = new Class[]{java.io.Serializable.class};
        MockSettings mockSettings = mock(MockSettings.class, new MockSettingsImpl().serializable().extraInterfaces(extraInterfaces));

        // When
        boolean isSerializable = mockSettings.isSerializable();

        // Then
        verify(mockSettings).isSerializable();
        assertTrue(isSerializable);
    }

    @Test(expected = Reporter.class)
    public void testNullExtraInterfaces() {
        // Given
        MockSettingsImpl mockSettings = new MockSettingsImpl();

        // When & Then
        mockSettings.extraInterfaces(null);
    }

    @Test(expected = Reporter.class)
    public void testNonInterfaceExtraInterface() {
        // Given
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?> nonInterfaceClass = Object.class;

        // When & Then
        mockSettings.extraInterfaces(new Class[]{nonInterfaceClass});
    }

    @Test(expected = Reporter.class)
    public void testInvalidInterface() {
        // Given
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?> invalidInterface = new Object() {};

        // When & Then
        mockSettings.extraInterfaces(new Class[]{invalidInterface});
    }

    @Test
    public void testSettingsName() {
        // Given
        MockSettings mockSettings = new MockSettingsImpl().name("Mock name");

        // When
        String name = mockSettings.getMockName();

        // Then
        assertEquals("Mock name", name);
    }

    @Test(expected = Reporter.class)
    public void testNullName() {
        // Given
        MockSettingsImpl mockSettings = new MockSettingsImpl();

        // When
        mockSettings.name(null);
    }

    @Test
    public void testSettingsNoExtraInterface() {
        // Given
        MockSettings mockSettings = new MockSettingsImpl();

        // When
        Class<?>[] extraInterfaces = mockSettings.getExtraInterfaces();

        // Then
        assertNull(extraInterfaces);
    }

    @Test(expected = Reporter.class)
    public void testNullSpiedInstance() {
        // Given
        MockSettingsImpl mockSettings = new MockSettingsImpl();

        // When
        mockSettings.spiedInstance(null);
    }

    @Test
    public void testSettingsDefaultAnswer() {
        // Given
        Answer<Object> defaultAnswer = mock(Answer.class);
        MockSettingsImpl mockSettings = new MockSettingsImpl().defaultAnswer(defaultAnswer);

        // When
        Answer<Object> result = mockSettings.getDefaultAnswer();

        // Then
        assertEquals(defaultAnswer, result);
    }

    @Test(expected = Reporter.class)
    public void testNullDefaultAnswer() {
        // Given
        MockSettingsImpl mockSettings = new MockSettingsImpl();

        // When
        mockSettings.defaultAnswer(null);
    }

    @Test
    public void testInitiateMockName() {
        // Given
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class classToMock = Object.class;

        // When
        mockSettings.initiateMockName(classToMock);

        // Then
        MockName mockName = mockSettings.getMockName();
        verify(mockName).initiateMockName(classToMock);
    }

    @Test(expected = Reporter.class)
    public void testNullClassToMock() {
        // Given
        MockSettingsImpl mockSettings = new MockSettingsImpl();

        // When & Then
        mockSettings.initiateMockName(null);
    }
}