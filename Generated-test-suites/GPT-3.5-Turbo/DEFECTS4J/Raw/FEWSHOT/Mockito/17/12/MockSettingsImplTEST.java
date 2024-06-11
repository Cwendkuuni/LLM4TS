package org.mockito.internal.creation;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
        assertEquals(mockSettings, result);
    }

    @Test
    public void testExtraInterfaces() {
        Class<?>[] extraInterfaces = {};
        MockSettings result = mockSettings.extraInterfaces(extraInterfaces);
        assertArrayEquals(extraInterfaces, result.getExtraInterfaces());

        extraInterfaces = new Class<?>[]{Object.class, Cloneable.class};
        result = mockSettings.extraInterfaces(extraInterfaces);
        assertArrayEquals(extraInterfaces, result.getExtraInterfaces());

        extraInterfaces = new Class<?>[]{null};
        try {
            mockSettings.extraInterfaces(extraInterfaces);
            assertFalse(true);
        } catch (Reporter e) {
            assertFalse(false);
        }

        extraInterfaces = new Class<?>[]{MockSettings.class};
        try {
            mockSettings.extraInterfaces(extraInterfaces);
            assertFalse(true);
        } catch (Reporter e) {
            assertFalse(false);
        }
    }

    @Test
    public void testGetMockName() {
        assertNull(mockSettings.getMockName());
    }

    @Test
    public void testGetExtraInterfaces() {
        assertNull(mockSettings.getExtraInterfaces());
    }

    @Test
    public void testGetSpiedInstance() {
        assertNull(mockSettings.getSpiedInstance());
    }

    @Test
    public void testName() {
        String name = "test";
        MockSettings result = mockSettings.name(name);
        assertEquals(name, result.getMockName().toString());
    }

    @Test
    public void testSpiedInstance() {
        Object spiedInstance = new Object();
        MockSettings result = mockSettings.spiedInstance(spiedInstance);
        assertEquals(spiedInstance, result.getSpiedInstance());
    }

    @Test
    public void testDefaultAnswer() {
        Answer<Object> defaultAnswer = null;
        MockSettings result = mockSettings.defaultAnswer(defaultAnswer);
        assertEquals(defaultAnswer, result.getDefaultAnswer());
    }

    @Test
    public void testIsSerializable() {
        assertFalse(mockSettings.isSerializable());

        Class<?>[] extraInterfaces = {java.io.Serializable.class};
        mockSettings.extraInterfaces(extraInterfaces);
        assertTrue(mockSettings.isSerializable());
        
        extraInterfaces = new Class<?>[]{Object.class, Cloneable.class};
        mockSettings.extraInterfaces(extraInterfaces);
        assertFalse(mockSettings.isSerializable());
    }

    @Test
    public void testInitiateMockName() {
        Class classToMock = Object.class;
        mockSettings.initiateMockName(classToMock);
        assertEquals(classToMock.getName(), mockSettings.getMockName().toString());
    }
}