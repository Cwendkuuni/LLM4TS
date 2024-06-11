
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class MockSettingsImplTest {

    private MockSettingsImpl mockSettings;

    @Before
    public void setUp() {
        mockSettings = new MockSettingsImpl();
    }

    @Test
    public void testSerializable_Success() {
        MockSettingsImpl updatedMockSettings = mockSettings.serializable();

        assertTrue(updatedMockSettings.isSerializable());
    }

    @Test
    public void testExtraInterfaces_NullInput_Error() {
        try {
            mockSettings.extraInterfaces(null);
            fail("Expected Reporter.extraInterfacesRequiresAtLeastOneInterface() to be thrown");
        } catch (Exception e) {
            assertEquals("Expected exception message", "Exception message", e.getMessage());
        }
    }

    @Test
    public void testExtraInterfaces_EmptyInput_Error() {
        try {
            mockSettings.extraInterfaces();
            fail("Expected Reporter.extraInterfacesRequiresAtLeastOneInterface() to be thrown");
        } catch (Exception e) {
            assertEquals("Expected exception message", "Exception message", e.getMessage());
        }
    }

    @Test
    public void testExtraInterfaces_WithInterfaces_Success() {
        Class<?>[] interfaces = {Comparable.class, Cloneable.class};
        MockSettingsImpl updatedMockSettings = mockSettings.extraInterfaces(interfaces);

        assertArrayEquals("Expected extra interfaces match", interfaces, updatedMockSettings.getExtraInterfaces());
    }

    @Test
    public void testExtraInterfaces_WithNonInterfaces_Error() {
        Class<?>[] interfaces = {String.class, Integer.class};
        
        try {
            mockSettings.extraInterfaces(interfaces);
            fail("Expected Reporter.extraInterfacesAcceptsOnlyInterfaces() to be thrown");
        } catch (Exception e) {
            assertEquals("Expected exception message", "Exception message", e.getMessage());
        }
    }

    @Test
    public void testGetMockName_Success() {
        MockName mockName = new MockName("mockName", Object.class);
        mockSettings.initiateMockName(Object.class);

        assertEquals("Expected mockName to be returned", mockName, mockSettings.getMockName());
    }

    @Test
    public void testGetExtraInterfaces_Success() {
        Class<?>[] interfaces = {Comparable.class, Cloneable.class};
        mockSettings.extraInterfaces(interfaces);

        assertArrayEquals("Expected extra interfaces match", interfaces, mockSettings.getExtraInterfaces());
    }

    @Test
    public void testGetSpiedInstance_Success() {
        Object spiedInstance = new Object();
        mockSettings.spiedInstance(spiedInstance);

        assertEquals("Expected spiedInstance to be returned", spiedInstance, mockSettings.getSpiedInstance());
    }

    @Test
    public void testSetName_Success() {
        String name = "mockName";
        MockSettingsImpl updatedMockSettings = mockSettings.name(name);

        assertEquals("Expected name to be set", name, updatedMockSettings.getMockName().toString());
    }

    @Test
    public void testSetSpiedInstance_Success() {
        Object spiedInstance = new Object();
        MockSettingsImpl updatedMockSettings = mockSettings.spiedInstance(spiedInstance);

        assertEquals("Expected spiedInstance to be set", spiedInstance, updatedMockSettings.getSpiedInstance());
    }

    @Test
    public void testSetDefaultAnswer_Success() {
        Answer<Object> defaultAnswer = new Answer<>();
        MockSettingsImpl updatedMockSettings = mockSettings.defaultAnswer(defaultAnswer);

        assertEquals("Expected defaultAnswer to be set", defaultAnswer, updatedMockSettings.getDefaultAnswer());
    }

    @Test
    public void testIsSerializable_WithoutExtraInterfaces_False() {
        assertFalse(mockSettings.isSerializable());
    }

    @Test
    public void testIsSerializable_WithExtraInterfaces_False() {
        Class<?>[] interfaces = {Comparable.class, Cloneable.class};
        mockSettings.extraInterfaces(interfaces);

        assertFalse(mockSettings.isSerializable());
    }

    @Test
    public void testIsSerializable_WithSerializableInterface_True() {
        Class<?>[] interfaces = {Comparable.class, Cloneable.class, java.io.Serializable.class};
        mockSettings.extraInterfaces(interfaces);

        assertTrue(mockSettings.isSerializable());
    }

    @Test
    public void testInitiateMockName_Success() {
        MockName mockName = new MockName("mockName", Object.class);
        mockSettings.initiateMockName(Object.class);

        assertEquals("Expected mockName to be set", mockName, mockSettings.getMockName());
    }
}

