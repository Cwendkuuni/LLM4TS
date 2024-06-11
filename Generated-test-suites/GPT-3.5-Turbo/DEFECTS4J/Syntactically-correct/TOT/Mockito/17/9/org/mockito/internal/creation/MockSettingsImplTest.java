import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;

public class MockSettingsImplTest {

    @Test
    public void testSerializable() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockSettingsImpl newMockSettings = mockSettings.serializable();
        assertTrue(newMockSettings.isSerializable());
    }

    @Test
    public void testExtraInterfaces() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = new Class[] {Comparable.class, Cloneable.class};
        MockSettingsImpl newMockSettings = mockSettings.extraInterfaces(extraInterfaces);
        assertArrayEquals(extraInterfaces, newMockSettings.getExtraInterfaces());
    }

    @Test
    public void testExtraInterfacesNull() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        try {
            mockSettings.extraInterfaces(null);
            fail("Expected Reporter to throw exception for null extraInterfaces");
        } catch (Reporter e) {
            // Exception expected
        }
    }

    @Test
    public void testExtraInterfacesEmpty() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        try {
            mockSettings.extraInterfaces();
            fail("Expected Reporter to throw exception for empty extraInterfaces");
        } catch (Reporter e) {
            // Exception expected
        }
    }

    @Test
    public void testExtraInterfacesNotInterface() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        try {
            mockSettings.extraInterfaces(String.class);
            fail("Expected Reporter to throw exception for non-interface extraInterfaces");
        } catch (Reporter e) {
            // Exception expected
        }
    }

    @Test
    public void testGetMockName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        assertNull(mockSettings.getMockName());
    }

    @Test
    public void testGetName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        String name = "mockName";
        MockSettingsImpl newMockSettings = mockSettings.name(name);
        assertEquals(name, newMockSettings.getMockName().getMockName());
    }

    @Test
    public void testGetExtraInterfaces() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = new Class[] {Comparable.class, Cloneable.class};
        mockSettings.extraInterfaces(extraInterfaces);
        assertArrayEquals(extraInterfaces, mockSettings.getExtraInterfaces());
    }

    @Test
    public void testGetSpiedInstance() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        assertNull(mockSettings.getSpiedInstance());
    }

    @Test
    public void testSpiedInstance() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Object spiedInstance = new Object();
        MockSettingsImpl newMockSettings = mockSettings.spiedInstance(spiedInstance);
        assertEquals(spiedInstance, newMockSettings.getSpiedInstance());
    }

    @Test
    public void testDefaultAnswer() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Answer<Object> defaultAnswer = (Answer<Object>) invocationOnMock -> null;
        MockSettingsImpl newMockSettings = mockSettings.defaultAnswer(defaultAnswer);
        assertEquals(defaultAnswer, newMockSettings.getDefaultAnswer());
    }

    @Test
    public void testIsSerializable() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        assertFalse(mockSettings.isSerializable());
        mockSettings.extraInterfaces(java.io.Serializable.class);
        assertTrue(mockSettings.isSerializable());
    }

    @Test
    public void testInitiateMockName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        String name = "mockName";
        mockSettings.name(name);
        Class<?> classToMock = Object.class;
        mockSettings.initiateMockName(classToMock);
        assertEquals(name, mockSettings.getMockName().getMockName());
        assertEquals(classToMock, mockSettings.getMockName().getMockedType());
    }
}