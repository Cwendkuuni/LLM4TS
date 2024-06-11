import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

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
    public void testExtraInterfaces() {
        Class<?>[] extraInterfaces = {Comparable.class, Cloneable.class};
        MockSettings result = mockSettings.extraInterfaces(extraInterfaces);
        assertArrayEquals(extraInterfaces, result.getExtraInterfaces());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExtraInterfacesWithNullInput() {
        Class<?>[] extraInterfaces = null;
        mockSettings.extraInterfaces(extraInterfaces);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExtraInterfacesWithNoInterfaces() {
        Class<?>[] extraInterfaces = {};
        mockSettings.extraInterfaces(extraInterfaces);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExtraInterfacesWithNonInterfaceParameter() {
        Class<?>[] extraInterfaces = {String.class};
        mockSettings.extraInterfaces(extraInterfaces);
    }

    @Test
    public void testGetMockName() {
        assertNull(mockSettings.getMockName());
    }

    @Test
    public void testGetName() {
        String name = "testMock";
        MockSettings result = mockSettings.name(name);
        assertEquals(name, result.getMockName().getMockName());
    }

    @Test
    public void testGetSpiedInstance() {
        assertNull(mockSettings.getSpiedInstance());
    }

    @Test
    public void testSpiedInstance() {
        Object spiedInstance = new Object();
        MockSettings result = mockSettings.spiedInstance(spiedInstance);
        assertEquals(spiedInstance, result.getSpiedInstance());
    }

    @Test
    public void testDefaultAnswer() {
        Answer<Object> defaultAnswer = (invocation) -> "Default answer";
        MockSettings result = mockSettings.defaultAnswer(defaultAnswer);
        assertEquals(defaultAnswer, result.getDefaultAnswer());
    }

    @Test
    public void testIsSerializableWithSerializableInterface() {
        Class<?>[] extraInterfaces = {java.io.Serializable.class};
        MockSettings result = mockSettings.extraInterfaces(extraInterfaces);
        assertTrue(result.isSerializable());
    }

    @Test
    public void testIsSerializableWithoutSerializableInterface() {
        Class<?>[] extraInterfaces = {Comparable.class};
        MockSettings result = mockSettings.extraInterfaces(extraInterfaces);
        assertFalse(result.isSerializable());
    }

    @Test
    public void testInitiateMockName() {
        Class<?> classToMock = Object.class;
        mockSettings.initiateMockName(classToMock);
        assertNotNull(mockSettings.getMockName());
    }
}