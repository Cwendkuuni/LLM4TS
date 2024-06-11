import static org.junit.Assert.*;
import org.junit.Test;

public class StdKeySerializersTest {

    @Test
    public void testGetStdKeySerializer() {
        // Test typical use case
        assertNotNull(StdKeySerializers.getStdKeySerializer(null, String.class, true));

        // Test edge case
        assertNull(StdKeySerializers.getStdKeySerializer(null, Object.class, false));
    }

    @Test
    public void testGetFallbackKeySerializer() {
        // Test typical use case
        assertNotNull(StdKeySerializers.getFallbackKeySerializer(null, Enum.class));

        // Test edge case
        assertNotNull(StdKeySerializers.getFallbackKeySerializer(null, Enum.class));
    }

    @Test
    public void testGetDefault() {
        // Test typical use case
        assertNotNull(StdKeySerializers.getDefault());
    }

    @Test
    public void testDefaultSerialize() {
        // Test typical use case
        StdKeySerializers.Default defaultSerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_DATE, Date.class);
        assertNotNull(defaultSerializer);
        
        // Test edge case
        try {
            defaultSerializer.serialize(new Date(), null, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void testDynamicSerialize() {
        // Test typical use case
        StdKeySerializers.Dynamic dynamicSerializer = new StdKeySerializers.Dynamic();
        assertNotNull(dynamicSerializer);
        
        // Test edge case
        try {
            dynamicSerializer.serialize(null, null, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void testStringKeySerialize() {
        // Test typical use case
        StdKeySerializers.StringKeySerializer stringSerializer = new StdKeySerializers.StringKeySerializer();
        assertNotNull(stringSerializer);

        // Test edge case
        try {
            stringSerializer.serialize(null, null, null);
            fail("Expected ClassCastException");
        } catch (ClassCastException e) {
            // Expected exception
        }
    }
}