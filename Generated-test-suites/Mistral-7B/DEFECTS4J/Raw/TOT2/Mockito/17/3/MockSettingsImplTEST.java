import org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.creation.MockSettingsImpl;

public class MockSettingsImplTest {

    @Test
    public void testSerializable() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.serializable();
        // Add assertions here
    }

    @Test
    public void testExtraInterfaces() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.extraInterfaces(java.io.Serializable.class);
        // Add assertions here
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExtraInterfacesNull() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.extraInterfaces(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExtraInterfacesEmpty() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.extraInterfaces(new Class[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExtraInterfacesNotAnInterface() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.extraInterfaces(String.class);
    }

    @Test
    public void testGetMockName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.initiateMockName(Object.class);
        String mockName = mockSettings.getMockName();
        // Add assertions here
    }

    // Repeat for the remaining methods

    @Test
    public void testIsSerializable() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.extraInterfaces(java.io.Serializable.class);
        boolean isSerializable = mockSettings.isSerializable();
        // Add assertions here
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsSerializableNoSerializableInterface() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.extraInterfaces(String.class);
        boolean isSerializable = mockSettings.isSerializable();
    }

    // Repeat for the remaining edge cases and exceptions

}