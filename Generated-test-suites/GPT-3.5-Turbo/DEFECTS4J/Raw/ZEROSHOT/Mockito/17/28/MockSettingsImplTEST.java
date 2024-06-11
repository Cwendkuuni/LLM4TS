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
        Class<?>[] extraInterfaces = new Class<?>[]{MockSettings.class};
        MockSettings result = mockSettings.extraInterfaces(extraInterfaces);
        assertArrayEquals(extraInterfaces, result.getExtraInterfaces());
    }

    @Test
    public void testExtraInterfacesWithNull() {
        try {
            mockSettings.extraInterfaces(null);
            fail("Expected exception to be thrown");
        } catch (Exception e) {
            // Expected exception
        }
    }

    @Test
    public void testExtraInterfacesWithNonInterface() {
        try {
            Class<?>[] extraInterfaces = new Class<?>[]{String.class};
            mockSettings.extraInterfaces(extraInterfaces);
            fail("Expected exception to be thrown");
        } catch (Exception e) {
            // Expected exception
        }
    }

    @Test
    public void testGetMockName() {
        assertNull(mockSettings.getMockName());
    }

    @Test
    public void testGetName() {
        String name = "testName";
        MockSettings result = mockSettings.name(name);
        assertEquals(name, result.getMockName());
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
    public void testSpiedInstance() {
        Object spiedInstance = new Object();
        MockSettings result = mockSettings.spiedInstance(spiedInstance);
        assertEquals(spiedInstance, result.getSpiedInstance());
    }

    @Test
    public void testDefaultAnswer() {
        Answer<Object> defaultAnswer = (invocation) -> null;
        MockSettings result = mockSettings.defaultAnswer(defaultAnswer);
        assertEquals(defaultAnswer, result.getDefaultAnswer());
    }

    @Test
    public void testIsSerializableWithoutExtraInterfaces() {
        assertFalse(mockSettings.isSerializable());
    }

    @Test
    public void testIsSerializableWithExtraInterfaces() {
        Class<?>[] extraInterfaces = new Class<?>[]{java.io.Serializable.class};
        MockSettings result = mockSettings.extraInterfaces(extraInterfaces);
        assertTrue(result.isSerializable());
    }

    @Test
    public void testInitiateMockName() {
        Class<?> classToMock = Object.class;
        mockSettings.initiateMockName(classToMock);
        MockName mockName = mockSettings.getMockName();
        assertNotNull(mockName);
        assertEquals(Object.class, mockName.getClassToMock());
    }
}