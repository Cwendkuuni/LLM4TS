import static org.junit.Assert.*;
import org.junit.Test;

public class StdKeySerializersTest {

    @Test
    public void testGetStdKeySerializer() {
        // Test typical case
        assertNotNull(StdKeySerializers.getStdKeySerializer(null, String.class, true));
        
        // Test edge case: rawKeyType is null
        assertNotNull(StdKeySerializers.getStdKeySerializer(null, null, true));
        
        // Test exception handling: rawKeyType is unsupported
        assertNull(StdKeySerializers.getStdKeySerializer(null, Thread.class, false));
    }

    @Test
    public void testGetFallbackKeySerializer() {
        // Test typical case
        assertNotNull(StdKeySerializers.getFallbackKeySerializer(null, Enum.class));
        
        // Test edge case: rawKeyType is null
        assertNotNull(StdKeySerializers.getFallbackKeySerializer(null, null));
        
        // Test exception handling: rawKeyType is not handled
        assertNotNull(StdKeySerializers.getFallbackKeySerializer(null, Thread.class));
    }

    @Test
    public void testGetDefault() {
        // Test typical case
        assertNotNull(StdKeySerializers.getDefault());
    }

    @Test
    public void testDefaultSerialize() {
        // Create Default instance for testing
        StdKeySerializers.Default defaultSerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_TO_STRING, String.class);

        // Test typical case
        assertNotNull(defaultSerializer);
        
        // Test edge case: TYPE_DATE
        assertEquals(StdKeySerializers.Default.TYPE_DATE, defaultSerializer._typeId);
        
        // Test exception handling: Unknown type
        try {
            defaultSerializer.serialize(new Object(), null, null);
            fail("Should throw IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testDynamicSerialize() {
        // Create Dynamic instance for testing
        StdKeySerializers.Dynamic dynamicSerializer = new StdKeySerializers.Dynamic();

        // Test typical case
        assertNotNull(dynamicSerializer);
        
        // Test edge case: _dynamicSerializers is empty
        assertNull(dynamicSerializer._dynamicSerializers.serializerFor(String.class));
        
        // Test exception handling: Unknown type
        try {
            dynamicSerializer.serialize(new Object(), null, null);
            fail("Should throw IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testStringKeySerializer() {
        // Create StringKeySerializer instance for testing
        StdKeySerializers.StringKeySerializer stringKeySerializer = new StdKeySerializers.StringKeySerializer();

        // Test typical case
        assertNotNull(stringKeySerializer);
        
        // Test edge case: value is a String
        try {
            stringKeySerializer.serialize("test", null, null);
        } catch (IOException e) {
            fail("Should not throw IOException");
        }
        
        // Test exception handling: value is not a String
        try {
            stringKeySerializer.serialize(123, null, null);
            fail("Should throw IOException");
        } catch (IOException e) {
            // Expected
        }
    }
}