import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.internal.creation.MockSettingsImpl;

public class MockSettingsImplTest {

    @Test
    public void testSerializable() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockSettingsImpl serializableSettings = (MockSettingsImpl) mockSettings.serializable();
        
        assertTrue(serializableSettings.isSerializable());
    }

    @Test
    public void testExtraInterfaces() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] interfaces = new Class[]{Runnable.class, Comparable.class};
        mockSettings.extraInterfaces(interfaces);
        
        assertArrayEquals(interfaces, mockSettings.getExtraInterfaces());
    }

    @Test
    public void testExtraInterfaces_NullArray() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        
        try {
            mockSettings.extraInterfaces(null);
            fail("Expected exception");
        } catch (Exception e) {
            // Pass if exception is thrown
        }
    }

    @Test
    public void testExtraInterfaces_NullInterface() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] interfaces = new Class[]{Runnable.class, null};
        
        try {
            mockSettings.extraInterfaces(interfaces);
            fail("Expected exception");
        } catch (Exception e) {
            // Pass if exception is thrown
        }
    }

    // Continue with test cases for other methods...

}