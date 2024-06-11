package org.mockito.internal.creation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import org.junit.Test;
import org.mockito.internal.util.MockName;
import org.mockito.stubbing.Answer;

public class MockSettingsImplTest {
    
    @Test
    public void testSerializable() {
        MockSettingsImpl settings = new MockSettingsImpl();
        MockSettings result = settings.serializable();
        assertEquals(settings, result);
    }

    @Test
    public void testExtraInterfaces() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {Runnable.class, AutoCloseable.class};
        MockSettings result = settings.extraInterfaces(extraInterfaces);
        assertEquals(settings, result);
        assertEquals(extraInterfaces, result.getExtraInterfaces());
    }

    @Test(expected = Reporter.class)
    public void testExtraInterfacesWithNull() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {Runnable.class, null, AutoCloseable.class};
        settings.extraInterfaces(extraInterfaces);
    }

    @Test(expected = Reporter.class)
    public void testExtraInterfacesWithNonInterface() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {Runnable.class, String.class};
        settings.extraInterfaces(extraInterfaces);
    }

    @Test
    public void testGetMockName() {
        MockSettingsImpl settings = new MockSettingsImpl();
        MockName mockName = mock(MockName.class);
        settings.mockName = mockName;
        assertEquals(mockName, settings.getMockName());
    }

    @Test
    public void testGetExtraInterfaces() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {Runnable.class, AutoCloseable.class};
        settings.extraInterfaces = extraInterfaces;
        assertEquals(extraInterfaces, settings.getExtraInterfaces());
    }

    @Test
    public void testGetSpiedInstance() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Object spiedInstance = new Object();
        settings.spiedInstance = spiedInstance;
        assertEquals(spiedInstance, settings.getSpiedInstance());
    }

    @Test
    public void testName() {
        MockSettingsImpl settings = new MockSettingsImpl();
        String name = "mock";
        MockSettings result = settings.name(name);
        assertEquals(settings, result);
        assertEquals(name, settings.name);
    }

    @Test
    public void testSpiedInstance() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Object spiedInstance = new Object();
        MockSettings result = settings.spiedInstance(spiedInstance);
        assertEquals(settings, result);
        assertEquals(spiedInstance, settings.spiedInstance);
    }

    @Test
    public void testDefaultAnswer() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Answer<Object> defaultAnswer = mock(Answer.class);
        MockSettings result = settings.defaultAnswer(defaultAnswer);
        assertEquals(settings, result);
        assertEquals(defaultAnswer, settings.defaultAnswer);
    }

    @Test
    public void testGetDefaultAnswer() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Answer<Object> defaultAnswer = mock(Answer.class);
        settings.defaultAnswer = defaultAnswer;
        assertEquals(defaultAnswer, settings.getDefaultAnswer());
    }

    @Test
    public void testIsSerializable() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {Runnable.class, AutoCloseable.class};
        settings.extraInterfaces = extraInterfaces;
        boolean result = settings.isSerializable();
        assertEquals(true, result);
    }

    @Test
    public void testIsNotSerializable() {
        MockSettingsImpl settings = new MockSettingsImpl();
        boolean result = settings.isSerializable();
        assertEquals(false, result);
    }

    @Test
    public void testInitiateMockName() {
        MockSettingsImpl settings = new MockSettingsImpl();
        String name = "mock";
        Class<?> classToMock = Object.class;
        settings.initiateMockName(classToMock);
        assertEquals(new MockName(name, classToMock), settings.getMockName());
    }
}