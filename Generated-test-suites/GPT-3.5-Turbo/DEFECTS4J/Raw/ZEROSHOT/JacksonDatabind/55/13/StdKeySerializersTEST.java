import org.junit.Test;

public class StdKeySerializersTest {
    
    @Test
    public void testGetStdKeySerializer() {
        // Test with rawKeyType as null
        assertNull(StdKeySerializers.getStdKeySerializer(null, null, true));
        // Test with rawKeyType as Object.class
        assertNotNull(StdKeySerializers.getStdKeySerializer(null, Object.class, true));
        // Test with rawKeyType as String.class
        assertNotNull(StdKeySerializers.getStdKeySerializer(null, String.class, true));
        // Test with rawKeyType as int.class
        assertNotNull(StdKeySerializers.getStdKeySerializer(null, int.class, true));
        // Test with rawKeyType as Integer.class
        assertNotNull(StdKeySerializers.getStdKeySerializer(null, Integer.class, true));
        // Test with rawKeyType as Class.class
        assertNotNull(StdKeySerializers.getStdKeySerializer(null, Class.class, true));
        // Test with rawKeyType as Date.class
        assertNotNull(StdKeySerializers.getStdKeySerializer(null, Date.class, true));
        // Test with rawKeyType as Calendar.class
        assertNotNull(StdKeySerializers.getStdKeySerializer(null, Calendar.class, true));
        // Test with rawKeyType as java.util.UUID.class
        assertNotNull(StdKeySerializers.getStdKeySerializer(null, java.util.UUID.class, true));
    }
    
    @Test
    public void testGetFallbackKeySerializer() {
        // Test with rawKeyType as null
        assertNotNull(StdKeySerializers.getFallbackKeySerializer(null, null));
        // Test with rawKeyType as Enum.class
        assertNotNull(StdKeySerializers.getFallbackKeySerializer(null, Enum.class));
    }
    
    @Test
    public void testGetDefault() {
        assertNotNull(StdKeySerializers.getDefault());
    }
}