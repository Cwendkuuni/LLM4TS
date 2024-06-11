package org.mockito.internal.creation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.mockito.MockSettings;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.MockName;
import org.mockito.stubbing.Answer;

public class MockSettingsImplTest {
    @Test
    public void testSerializable() {
        MockSettingsImpl settings = new MockSettingsImpl();
        MockSettings result = settings.serializable();
        assertTrue(result.isSerializable());
    }

    @Test
    public void testExtraInterfaces() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {MockSettings.class};
        MockSettings result = settings.extraInterfaces(extraInterfaces);
        assertArrayEquals(extraInterfaces, result.getExtraInterfaces());
    }

    @Test(expected = Reporter.class)
    public void testExtraInterfacesWithNull() {
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.extraInterfaces(null);
    }

    @Test(expected = Reporter.class)
    public void testExtraInterfacesWithEmptyArray() {
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.extraInterfaces(new Class<?>[]{});
    }

    @Test(expected = Reporter.class)
    public void testExtraInterfacesWithNonInterfaceClass() {
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.extraInterfaces(String.class);
    }

    @Test
    public void testGetMockName() {
        MockSettingsImpl settings = new MockSettingsImpl();
        MockName mockName = new MockName("testMock", MockSettingsImpl.class);
        settings.initiateMockName(MockSettingsImpl.class);
        assertEquals(mockName, settings.getMockName());
    }

    @Test
    public void testGetExtraInterfaces() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {MockSettings.class};
        settings.extraInterfaces(extraInterfaces);
        assertArrayEquals(extraInterfaces, settings.getExtraInterfaces());
    }

    @Test
    public void testGetSpiedInstance() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Object spiedInstance = mock(Object.class);
        settings.spiedInstance(spiedInstance);
        assertEquals(spiedInstance, settings.getSpiedInstance());
    }

    @Test
    public void testName() {
        MockSettingsImpl settings = new MockSettingsImpl();
        String name = "testMock";
        MockSettings result = settings.name(name);
        assertEquals(name, result.getMockName().toString());
    }

    @Test
    public void testDefaultAnswer() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Answer<Object> defaultAnswer = mock(Answer.class);
        MockSettings result = settings.defaultAnswer(defaultAnswer);
        assertEquals(defaultAnswer, result.getDefaultAnswer());
    }

    @Test
    public void testDefaultAnswerNull() {
        MockSettingsImpl settings = new MockSettingsImpl();
        MockSettings result = settings.defaultAnswer(null);
        assertNull(result.getDefaultAnswer());
    }

    @Test
    public void testisSerializable() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {java.io.Serializable.class};
        settings.extraInterfaces(extraInterfaces);
        assertTrue(settings.isSerializable());
    }

    @Test
    public void testisSerializableFalse() {
        MockSettingsImpl settings = new MockSettingsImpl();
        assertFalse(settings.isSerializable());
    }
}