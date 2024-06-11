import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.internal.creation.MockSettingsImpl;

public class MockSettingsImplTest {

    @Test
    public void testSerializable() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockSettingsImpl result = (MockSettingsImpl) mockSettings.serializable();
        assertNotNull(result.getExtraInterfaces());
        assertEquals(1, result.getExtraInterfaces().length);
        assertEquals(java.io.Serializable.class, result.getExtraInterfaces()[0]);
    }

    @Test
    public void testExtraInterfaces() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.extraInterfaces(String.class);
        assertNotNull(mockSettings.getExtraInterfaces());
        assertEquals(1, mockSettings.getExtraInterfaces().length);
        assertEquals(String.class, mockSettings.getExtraInterfaces()[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExtraInterfacesNullInput() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.extraInterfaces(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExtraInterfacesNonInterfaceInput() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.extraInterfaces(Integer.class);
    }

    @Test
    public void testGetName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        String name = "testName";
        mockSettings.name(name);
        assertEquals(name, mockSettings.getMockName().getMockName());
    }

    @Test
    public void testSpiedInstance() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Object spiedInstance = new Object();
        mockSettings.spiedInstance(spiedInstance);
        assertEquals(spiedInstance, mockSettings.getSpiedInstance());
    }

    @Test
    public void testDefaultAnswer() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Answer<Object> defaultAnswer = invocation -> null;
        mockSettings.defaultAnswer(defaultAnswer);
        assertEquals(defaultAnswer, mockSettings.getDefaultAnswer());
    }

    @Test
    public void testIsSerializable() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.extraInterfaces(java.io.Serializable.class);
        assertTrue(mockSettings.isSerializable());
    }

    @Test
    public void testInitiateMockName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?> classToMock = String.class;
        String name = "testName";
        mockSettings.name(name);
        mockSettings.initiateMockName(classToMock);
        assertNotNull(mockSettings.getMockName());
        assertEquals(name, mockSettings.getMockName().getMockName());
        assertEquals(classToMock, mockSettings.getMockName().getMockType());
    }
}