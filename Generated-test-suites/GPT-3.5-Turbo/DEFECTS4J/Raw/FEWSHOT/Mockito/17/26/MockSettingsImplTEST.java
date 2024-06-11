
package org.mockito.internal.creation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockSettings;
import org.mockito.exceptions.Reporter;
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

        assertTrue(result.isSerializable());
    }

    @Test
    public void testExtraInterfacesWithNull() {
        Reporter reporter = new Reporter();

        try {
            mockSettings.extraInterfaces(null);
        } catch (Exception e) {
            assertTrue(e instanceof NullPointerException);
            assertEquals("Null extra interface", e.getMessage());
        }

        verify(reporter).extraInterfacesRequiresAtLeastOneInterface();
    }

    @Test
    public void testExtraInterfacesWithEmptyArray() {
        Reporter reporter = new Reporter();

        try {
            mockSettings.extraInterfaces(new Class[] {});
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
            assertEquals("The extraInterfaces method requires at least one interface", e.getMessage());
        }

        verify(reporter).extraInterfacesRequiresAtLeastOneInterface();
    }

    @Test
    public void testExtraInterfacesWithNonInterface() {
        Reporter reporter = new Reporter();
        Class<?> nonInterfaceClass = String.class;

        try {
            mockSettings.extraInterfaces(nonInterfaceClass);
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
            assertEquals("The extraInterfaces method accepts only interfaces, got: " + nonInterfaceClass.getName(), e.getMessage());
        }

        verify(reporter).extraInterfacesAcceptsOnlyInterfaces(nonInterfaceClass);
    }

    @Test
    public void testExtraInterfacesWithInterfaces() {
        Class<?>[] extraInterfaces = new Class[] { Comparable.class, Cloneable.class };
        mockSettings.extraInterfaces(extraInterfaces);

        assertArrayEquals(extraInterfaces, mockSettings.getExtraInterfaces());
    }

    @Test
    public void testMockName() {
        String name = "mockName";
        MockSettings result = mockSettings.name(name);

        assertEquals(name, result.getMockName().getMockName());
    }

    @Test
    public void testSpiedInstance() {
        Object spiedInstance = new Object();
        MockSettings result = mockSettings.spiedInstance(spiedInstance);

        assertEquals(spiedInstance, result.getSpiedInstance());
    }

    @Test
    public void testDefaultAnswer() {
        Answer<?> defaultAnswer = null;
        MockSettings result = mockSettings.defaultAnswer(defaultAnswer);

        assertEquals(defaultAnswer, result.getDefaultAnswer());
    }
    
    @Test
    public void testIsSerializableWithNullExtraInterfaces() {
        assertFalse(mockSettings.isSerializable());
    }

    @Test
    public void testIsSerializableWithSerializableInterface() {
        Class<?>[] extraInterfaces = new Class[] { java.io.Serializable.class };
        mockSettings.extraInterfaces(extraInterfaces);

        assertTrue(mockSettings.isSerializable());
    }

    @Test
    public void testInitiateMockName() {
        String name = "mockName";
        Class<?> classToMock = Object.class;
        mockSettings.name(name);

        mockSettings.initiateMockName(classToMock);

        assertEquals(classToMock, mockSettings.getMockName().getClassToMock());
        assertEquals(name, mockSettings.getMockName().getMockName());
    }
}
