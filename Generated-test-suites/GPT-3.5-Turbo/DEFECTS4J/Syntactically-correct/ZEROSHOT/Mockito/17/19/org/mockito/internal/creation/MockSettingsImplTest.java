package org.mockito.internal.creation;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockSettings;
import org.mockito.exceptions.Reporter;
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
        MockSettings result = mockSettings.serializable();
        Assert.assertEquals(mockSettings, result);
        Assert.assertTrue(mockSettings.isSerializable());
    }

    @Test
    public void testExtraInterfacesWithNullParameter() {
        try {
            mockSettings.extraInterfaces((Class<?>[]) null);
            Assert.fail("Expected Exception Reporter.extraInterfacesDoesNotAcceptNullParameters");
        } catch (Exception e) {
            Assert.assertEquals(Reporter.class, e.getClass());
            Assert.assertEquals("extraInterfacesDoesNotAcceptNullParameters", e.getMessage());
        }
    }

    @Test
    public void testExtraInterfacesWithEmptyArray() {
        try {
            mockSettings.extraInterfaces(new Class<?>[0]);
            Assert.fail("Expected Exception Reporter.extraInterfacesRequiresAtLeastOneInterface");
        } catch (Exception e) {
            Assert.assertEquals(Reporter.class, e.getClass());
            Assert.assertEquals("extraInterfacesRequiresAtLeastOneInterface", e.getMessage());
        }
    }

    @Test
    public void testExtraInterfacesWithNonInterfaceClass() {
        try {
            Class<?>[] extraInterfaces = {String.class};
            mockSettings.extraInterfaces(extraInterfaces);
            Assert.fail("Expected Exception Reporter.extraInterfacesAcceptsOnlyInterfaces");
        } catch (Exception e) {
            Assert.assertEquals(Reporter.class, e.getClass());
            Assert.assertEquals("extraInterfacesAcceptsOnlyInterfaces", e.getMessage());
        }
    }

    @Test
    public void testExtraInterfacesWithValidParameters() {
        Class<?>[] extraInterfaces = {Runnable.class, Comparable.class};
        MockSettings result = mockSettings.extraInterfaces(extraInterfaces);
        Assert.assertEquals(mockSettings, result);
        Assert.assertArrayEquals(extraInterfaces, mockSettings.getExtraInterfaces());
    }

    @Test
    public void testGetMockName() {
        MockName mockName = mockSettings.getMockName();
        Assert.assertNull(mockName);
    }

    @Test
    public void testGetName() {
        String name = "mock";
        MockSettings result = mockSettings.name(name);
        Assert.assertEquals(mockSettings, result);
        Assert.assertEquals(name, mockSettings.getMockName().toString());
    }

    @Test
    public void testGetSpiedInstance() {
        Object spiedInstance = new Object();
        MockSettings result = mockSettings.spiedInstance(spiedInstance);
        Assert.assertEquals(mockSettings, result);
        Assert.assertEquals(spiedInstance, mockSettings.getSpiedInstance());
    }

    @Test
    public void testDefaultAnswer() {
        Answer<Object> defaultAnswer = (invocation) -> null;
        MockSettings result = mockSettings.defaultAnswer(defaultAnswer);
        Assert.assertEquals(mockSettings, result);
        Assert.assertEquals(defaultAnswer, mockSettings.getDefaultAnswer());
    }

    @Test
    public void testIsSerializableWhenExtraInterfacesIsNull() {
        boolean result = mockSettings.isSerializable();
        Assert.assertFalse(result);
    }

    @Test
    public void testIsSerializableWhenExtraInterfacesDoesNotContainSerializable() {
        mockSettings.extraInterfaces(Cloneable.class, Runnable.class);
        boolean result = mockSettings.isSerializable();
        Assert.assertFalse(result);
    }

    @Test
    public void testIsSerializableWhenExtraInterfacesContainsSerializable() {
        mockSettings.extraInterfaces(Cloneable.class, Runnable.class, java.io.Serializable.class);
        boolean result = mockSettings.isSerializable();
        Assert.assertTrue(result);
    }

    @Test
    public void testInitiateMockName() {
        Class<?> classToMock = Object.class;
        mockSettings.initiateMockName(classToMock);
        MockName mockName = mockSettings.getMockName();
        Assert.assertNotNull(mockName);
        Assert.assertEquals(classToMock, mockName.getTypeToMock());
    }
}