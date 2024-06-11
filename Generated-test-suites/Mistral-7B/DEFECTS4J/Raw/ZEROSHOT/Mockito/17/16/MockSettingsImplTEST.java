package com.example.junit4; // Replace with your package name

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answer;
import org.mockito.Mockito;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.creation.MockSettings;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.MockName;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

public class MockSettingsImplTest {

    private MockSettingsImpl mockSettings;

    @Before
    public void setUp() {
        mockSettings = new MockSettingsImpl();
    }

    @Test
    public void testSerializableMethod() {
        MockSettings serializableMockSettings = mockSettings.serializable();
        verify(mockSettings).extraInterfaces(Serializable.class);
        verify(serializableMockSettings).isSerializable();
    }

    @Test(expected = IllegalStateException.class)
    public void testNullExtraInterfaces() {
        mockSettings.extraInterfaces(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testEmptyExtraInterfaces() {
        mockSettings.extraInterfaces();
    }

    @Test(expected = IllegalStateException.class)
    public void testNonInterfaceExtraInterface() {
        Class<?> nonInterfaceClass = Mockito.class;
        mockSettings.extraInterfaces(nonInterfaceClass);
    }

    @Test
    public void testGetExtraInterfaces() {
        Class<?>[] extraInterfaces = new Class[]{Serializable.class, String.class};
        MockSettings mockSettings = mockSettings.extraInterfaces(extraInterfaces);
        List<Class<?>> actualExtraInterfaces = Arrays.asList(mockSettings.getExtraInterfaces());
        verify(mockSettings).extraInterfaces(extraInterfaces);
        verify(mockSettings).isSerializable();
        verify(mockSettings).getMockName();
        verifyZeroInteractions(mockSettings.getDefaultAnswer());
        verify(mockSettings.getSpiedInstance()).isInstanceOf(Object.class);
        verify(actualExtraInterfaces).containsAll(Arrays.asList(Serializable.class, String.class));
    }

    @Test
    public void testNameMethod() {
        String name = "TestMock";
        MockSettings nameMockSettings = mockSettings.name(name);
        verify(mockSettings).name(name);
        verify(nameMockSettings).getMockName();
        verifyZeroInteractions(mockSettings.getDefaultAnswer());
        verifyZeroInteractions(mockSettings.getExtraInterfaces());
        verifyZeroInteractions(mockSettings.getSpiedInstance());
    }

    @Test
    public void testSpiedInstanceMethod() {
        Object spiedInstance = mock(Object.class);
        MockSettings spiedInstanceMockSettings = mockSettings.spiedInstance(spiedInstance);
        verify(mockSettings).spiedInstance(spiedInstance);
        verifyZeroInteractions(mockSettings.getDefaultAnswer());
        verifyZeroInteractions(mockSettings.getExtraInterfaces());
        verifyZeroInteractions(mockSettings.getMockName());
        verify(spiedInstanceMockSettings).getSpiedInstance();
        verify(spiedInstanceMockSettings).getMockName();
        verifyZeroInteractions(spiedInstance);
    }

    @Test
    public void testDefaultAnswerMethod() {
        Answer<Object> answer = Mockito.answer(new Object());
        MockSettings defaultAnswerMockSettings = mockSettings.defaultAnswer(answer);
        verify(mockSettings).defaultAnswer(answer);
        verifyZeroInteractions(mockSettings.getExtraInterfaces());
        verifyZeroInteractions(mockSettings.getMockName());
        verifyZeroInteractions(mockSettings.getSpiedInstance());
        verify(defaultAnswerMockSettings).getDefaultAnswer();
        verify(defaultAnswerMockSettings).getMockName();
        verifyZeroInteractions(answer);
    }

    @Test
    public void testInitiateMockNameMethod() {
        Class<?> classToMock = Mockito.class;
        MockSettings mockSettings = mockSettings;
        mockSettings.initiateMockName(classToMock);
        verify(mockSettings).initiateMockName(classToMock);
        verify(mockSettings.getMockName()).isInstanceOf(MockName.class);
        verifyZeroInteractions(mockSettings.getDefaultAnswer());
        verifyZeroInteractions(mockSettings.getExtraInterfaces());
        verifyZeroInteractions(mockSettings.getSpiedInstance());
    }

    @Test(expected = IllegalStateException.class)
    public void testInitiateMockNameWithNullClass() {
        mockSettings.initiateMockName(null);
    }
}