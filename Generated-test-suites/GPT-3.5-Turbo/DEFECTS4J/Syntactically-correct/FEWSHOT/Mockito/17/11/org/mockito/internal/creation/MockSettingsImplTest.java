
package org.mockito.internal.creation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockSettings;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.MockName;
import org.mockito.stubbing.Answer;

public class MockSettingsImplTest {

    private MockSettingsImpl mockSettings;

    @Before
    public void setup() {
        mockSettings = new MockSettingsImpl();
    }

    @Test
    public void testSerializable() {
        mockSettings.serializable();
        Class<?>[] extraInterfaces = mockSettings.getExtraInterfaces();
        assertEquals(1, extraInterfaces.length);
        assertEquals(java.io.Serializable.class, extraInterfaces[0]);
    }

    @Test
    public void testExtraInterfaces() {
        Class<?>[] extraInterfaces = { Comparable.class };
        mockSettings.extraInterfaces(extraInterfaces);
        assertEquals(extraInterfaces, mockSettings.getExtraInterfaces());
    }

    @Test(expected = Reporter.class)
    public void testExtraInterfacesWithNullInput() {
        mockSettings.extraInterfaces(null);
    }

    @Test(expected = Reporter.class)
    public void testExtraInterfacesWithEmptyArray() {
        Class<?>[] extraInterfaces = {};
        mockSettings.extraInterfaces(extraInterfaces);
    }

    @Test(expected = Reporter.class)
    public void testExtraInterfacesWithNonInterfaceClass() {
        Class<?>[] extraInterfaces = { String.class };
        mockSettings.extraInterfaces(extraInterfaces);
    }

    @Test
    public void testGetName() {
        String name = "testMock";
        mockSettings.name(name);
        assertEquals(name, mockSettings.getMockName());
    }

    @Test
    public void testGetExtraInterfaces() {
        Class<?>[] extraInterfaces = { Comparable.class };
        mockSettings.extraInterfaces(extraInterfaces);
        assertEquals(extraInterfaces, mockSettings.getExtraInterfaces());
    }

    @Test
    public void testGetSpiedInstance() {
        Object spiedInstance = mock(Object.class);
        mockSettings.spiedInstance(spiedInstance);
        assertEquals(spiedInstance, mockSettings.getSpiedInstance());
    }

    @Test
    public void testDefaultAnswer() {
        Answer<Object> defaultAnswer = mock(Answer.class);
        mockSettings.defaultAnswer(defaultAnswer);
        assertEquals(defaultAnswer, mockSettings.getDefaultAnswer());
    }

    @Test
    public void testIsSerializable() {
        Class<?>[] extraInterfaces = { java.io.Serializable.class };
        mockSettings.extraInterfaces(extraInterfaces);
        assertEquals(true, mockSettings.isSerializable());
    }

    @Test
    public void testInitiateMockName() {
        String name = "testMock";
        Class<?> classToMock = Object.class;
        mockSettings.name(name);
        mockSettings.initiateMockName(classToMock);
        MockName mockName = mockSettings.getMockName();
        assertEquals(name, mockName.getMockName());
        assertEquals(classToMock, mockName.getTypeToMock());
    }
}
