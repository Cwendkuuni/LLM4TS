
package org.mockito.internal.creation;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.stubbing.Answer;

public class MockSettingsImplTest {

    @Test
    public void testSerializable() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();

        MockSettingsImpl result = mockSettings.serializable();

        assertSame(mockSettings, result);
        assertNotNull(result.getExtraInterfaces());
        assertArrayEquals(new Class<?>[] { java.io.Serializable.class }, result.getExtraInterfaces());
    }

    @Test(expected = Reporter.class)
    public void testExtraInterfacesWithNull() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();

        mockSettings.extraInterfaces(null);
    }

    @Test(expected = Reporter.class)
    public void testExtraInterfacesWithEmptyArray() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();

        mockSettings.extraInterfaces(new Class<?>[] {});
    }

    @Test(expected = Reporter.class)
    public void testExtraInterfacesWithInvalidClass() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();

        mockSettings.extraInterfaces(String.class);
    }

    @Test
    public void testExtraInterfacesWithValidClasses() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();

        Class<?>[] extraInterfaces = { Runnable.class, Comparable.class };
        MockSettingsImpl result = mockSettings.extraInterfaces(extraInterfaces);

        assertSame(mockSettings, result);
        assertArrayEquals(extraInterfaces, result.getExtraInterfaces());
    }

    @Test
    public void testGetMockName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();

        MockName result = mockSettings.getMockName();

        assertNull(result);
    }

    @Test
    public void testGetName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        String name = "mockName";
        mockSettings.name(name);

        String result = mockSettings.getMockName();

        assertEquals(name, result);
    }

    @Test
    public void testGetExtraInterfaces() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();

        Class<?>[] result = mockSettings.getExtraInterfaces();

        assertNull(result);
    }

    @Test
    public void testGetSpiedInstance() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();

        Object result = mockSettings.getSpiedInstance();

        assertNull(result);
    }

    @Test
    public void testSpiedInstance() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();

        Object spiedInstance = new Object();
        MockSettingsImpl result = mockSettings.spiedInstance(spiedInstance);

        assertSame(mockSettings, result);
        assertSame(spiedInstance, result.getSpiedInstance());
    }

    @Test
    public void testDefaultAnswer() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();

        Answer<Object> defaultAnswer = new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        };

        MockSettingsImpl result = mockSettings.defaultAnswer(defaultAnswer);

        assertSame(mockSettings, result);
        assertSame(defaultAnswer, result.getDefaultAnswer());
    }

    @Test
    public void testGetDefaultAnswer() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Answer<Object> defaultAnswer = new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        };
        mockSettings.defaultAnswer(defaultAnswer);

        Answer<Object> result = mockSettings.getDefaultAnswer();

        assertEquals(defaultAnswer, result);
    }

    @Test
    public void testIsSerializableWithNullExtraInterfaces() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();

        boolean result = mockSettings.isSerializable();

        assertFalse(result);
    }

    @Test
    public void testIsSerializableWithSerializableExtraInterface() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.extraInterfaces(java.io.Serializable.class);

        boolean result = mockSettings.isSerializable();

        assertTrue(result);
    }

    @Test
    public void testIsSerializableWithoutSerializableExtraInterface() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.extraInterfaces(Runnable.class);

        boolean result = mockSettings.isSerializable();

        assertFalse(result);
    }

    @Test
    public void testInitiateMockName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        String name = "mockName";
        mockSettings.name(name);
        Class<?> classToMock = Object.class;

        mockSettings.initiateMockName(classToMock);

        assertNotNull(mockSettings.getMockName());
        assertEquals(name, mockSettings.getMockName().getMockName());
        assertSame(classToMock, mockSettings.getMockName().getMockedType());
    }
}
