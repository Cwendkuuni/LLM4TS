
package org.mockito.internal.creation;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.MockSettings;
import org.mockito.Mockito;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.MockName;
import org.mockito.stubbing.Answer;

public class MockSettingsImplTest {

    @Test
    public void testSerializable() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockSettings result = mockSettings.serializable();

        Assert.assertEquals(mockSettings, result);
        Assert.assertTrue(mockSettings.isSerializable());
    }

    @Test
    public void testExtraInterfaces() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockSettings result = mockSettings.extraInterfaces(Runnable.class, Cloneable.class);

        Assert.assertEquals(mockSettings, result);
        Assert.assertArrayEquals(new Class<?>[]{Runnable.class, Cloneable.class}, mockSettings.getExtraInterfaces());
    }

    @Test
    public void testExtraInterfacesWithNullInterface() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();

        try {
            mockSettings.extraInterfaces((Class<?>) null);
            Assert.fail("Expected an exception.");
        } catch (Exception e) {
            Assert.assertEquals(Reporter.class, e.getClass());
            Assert.assertEquals("extraInterfacesDoesNotAcceptNullParameters", e.getMessage());
        }
    }

    @Test
    public void testExtraInterfacesWithNonInterfaceClass() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();

        try {
            mockSettings.extraInterfaces(String.class);
            Assert.fail("Expected an exception.");
        } catch (Exception e) {
            Assert.assertEquals(Reporter.class, e.getClass());
            Assert.assertEquals("extraInterfacesAcceptsOnlyInterfaces", e.getMessage());
        }
    }

    @Test
    public void testGetMockName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockName mockName = new MockName("TestName", String.class);
        mockSettings.initiateMockName(String.class);

        Assert.assertEquals(mockName, mockSettings.getMockName());
    }

    @Test
    public void testGetName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        String name = "TestName";
        MockSettings result = mockSettings.name(name);

        Assert.assertEquals(mockSettings, result);
        Assert.assertEquals(name, mockSettings.getMockName().getMockName());
    }

    @Test
    public void testGetSpiedInstance() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Object spiedInstance = new Object();
        MockSettings result = mockSettings.spiedInstance(spiedInstance);

        Assert.assertEquals(mockSettings, result);
        Assert.assertEquals(spiedInstance, mockSettings.getSpiedInstance());
    }

    @Test
    public void testDefaultAnswer() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Answer<Object> defaultAnswer = Mockito.mock(Answer.class);
        MockSettings result = mockSettings.defaultAnswer(defaultAnswer);

        Assert.assertEquals(mockSettings, result);
        Assert.assertEquals(defaultAnswer, mockSettings.getDefaultAnswer());
    }

    @Test
    public void testIsSerializable() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Object[] extraInterfaces = new Object[] { java.io.Serializable.class };
        mockSettings.extraInterfaces(extraInterfaces);

        Assert.assertTrue(mockSettings.isSerializable());
    }

}
