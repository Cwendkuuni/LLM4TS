package org.mockito.internal.creation.test;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.creation.MockSettings;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.MockName;

import java.io.Serializable;
import java.util.Arrays;

public class MockSettingsImplTest {

    @Test
    public void testSerializable() {
        MockSettings mockSettings = new MockSettingsImpl();
        MockSettings serializableSettings = mockSettings.serializable();
        Assert.assertTrue(serializableSettings.isSerializable());
    }

    @Test
    public void testExtraInterfacesWithNullArguments() {
        MockSettings mockSettings = new MockSettingsImpl();
        try {
            MockSettings extraInterfacesSettings = mockSettings.extraInterfaces(null);
        } catch (AssertionError e) {
            Assert.assertEquals("Extra interfaces requires at least one interface", e.getMessage());
        }
    }

    @Test
    public void testExtraInterfacesWithNullInterface() {
        MockSettings mockSettings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = new Class<?>[1];
        extraInterfaces[0] = null;
        try {
            MockSettings extraInterfacesSettings = mockSettings.extraInterfaces(extraInterfaces);
        } catch (AssertionError e) {
            Assert.assertEquals("Extra interfaces accepts only interfaces", e.getMessage());
        }
    }

    @Test
    public void testExtraInterfacesWithNonInterfaceClass() {
        MockSettings mockSettings = new MockSettingsImpl();
        Class<?> nonInterfaceClass = new Object() {}.getClass();
        try {
            MockSettings extraInterfacesSettings = mockSettings.extraInterfaces(nonInterfaceClass);
        } catch (AssertionError e) {
            Assert.assertEquals("Extra interfaces accepts only interfaces", e.getMessage());
        }
    }

    @Test
    public void testExtraInterfacesWithEmptyArray() {
        MockSettings mockSettings = new MockSettingsImpl();
        try {
            MockSettings extraInterfacesSettings = mockSettings.extraInterfaces(new Class<?>[0]);
        } catch (AssertionError e) {
            Assert.assertEquals("Extra interfaces requires at least one interface", e.getMessage());
        }
    }

    @Test
    public void testExtraInterfacesWithValidInterfaces() {
        MockSettings mockSettings = new MockSettingsImpl();
        Class<?>[] validInterfaces = new Class<?>[2];
        validInterfaces[0] = Serializable.class;
        validInterfaces[1] = String.class;
        MockSettings extraInterfacesSettings = mockSettings.extraInterfaces(validInterfaces);
        Assert.assertNotNull(extraInterfacesSettings.getExtraInterfaces());
        Assert.assertArrayEquals(validInterfaces, extraInterfacesSettings.getExtraInterfaces());
    }

    @Test
    public void testGetMockName() {
        MockSettings mockSettings = new MockSettingsImpl();
        MockName mockName = mockSettings.getMockName();
        Assert.assertNotNull(mockName);
    }

    @Test
    public void testGetExtraInterfaces() {
        MockSettings mockSettings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = new Class<?>[2];
        MockSettings settingsWithExtraInterfaces = mockSettings.extraInterfaces(extraInterfaces);
        Assert.assertNotNull(settingsWithExtraInterfaces.getExtraInterfaces());
        Assert.assertArrayEquals(extraInterfaces, settingsWithExtraInterfaces.getExtraInterfaces());
    }

    @Test
    public void testGetSpiedInstance() {
        Object spiedInstance = new Object();
        MockSettings mockSettings = new MockSettingsImpl();
        MockSettings settingsWithSpiedInstance = mockSettings.spiedInstance(spiedInstance);
        Assert.assertEquals(spiedInstance, settingsWithSpiedInstance.getSpiedInstance());
    }

    @Test
    public void testName() {
        String name = "MockName";
        MockSettings mockSettings = new MockSettingsImpl();
        MockSettings settingsWithName = mockSettings.name(name);
        Assert.assertEquals(name, settingsWithName.getName());
    }

    @Test
    public void testDefaultAnswer() {
        Answer<Object> defaultAnswer = new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return "Mock Answer";
            }
        };
        MockSettings mockSettings = new MockSettingsImpl();
        MockSettings settingsWithDefaultAnswer = mockSettings.defaultAnswer(defaultAnswer);
        Assert.assertEquals(defaultAnswer, settingsWithDefaultAnswer.getDefaultAnswer());
    }

    @Test(expected = IllegalStateException.class)
    public void testInitiateMockNameWithNullClass() {
        MockSettings mockSettings = new MockSettingsImpl();
        mockSettings.initiateMockName(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitiateMockNameWithInvalidClass() {
        MockSettings mockSettings = new MockSettingsImpl();
        mockSettings.initiateMockName(int.class);
    }
}