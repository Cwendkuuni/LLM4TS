import static org.junit.Assert.*;
import org.junit.Test;

public class StdKeySerializersTest {

    @Test
    public void testGetStdKeySerializer() {
        // Typical use case
        assertNotNull(StdKeySerializers.getStdKeySerializer(null, String.class, true));

        // Edge case
        assertNull(StdKeySerializers.getStdKeySerializer(null, Enum.class, true));

        // Exception handling
        assertThrows(Exception.class, () -> {
            StdKeySerializers.getStdKeySerializer(null, null, true);
        });
    }

    @Test
    public void testGetFallbackKeySerializer() {
        // Typical use case
        assertNotNull(StdKeySerializers.getFallbackKeySerializer(null, Enum.class));

        // Edge case
        assertNull(StdKeySerializers.getFallbackKeySerializer(null, null));

        // Exception handling
        assertThrows(Exception.class, () -> {
            StdKeySerializers.getFallbackKeySerializer(null, String.class);
        });
    }

    @Test
    public void testGetDefault() {
        // Typical use case
        assertNotNull(StdKeySerializers.getDefault());
    }

    @Test
    public void testDefaultSerialize() {
        // Typical use case
        StdKeySerializers.Default defaultSerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_DATE, Date.class);
        assertNotNull(defaultSerializer);
        
        // Edge case
        assertDoesNotThrow(() -> {
            defaultSerializer.serialize(new Date(), null, null);
        });
        
        // Exception handling
        assertThrows(Exception.class, () -> {
            defaultSerializer.serialize(null, null, null);
        });
    }

    @Test
    public void testDynamicSerialize() {
        // Typical use case
        StdKeySerializers.Dynamic dynamicSerializer = new StdKeySerializers.Dynamic();
        assertNotNull(dynamicSerializer);

        // Edge case
        assertDoesNotThrow(() -> {
            dynamicSerializer.serialize("test", null, null);
        });

        // Exception handling
        assertThrows(Exception.class, () -> {
            dynamicSerializer.serialize(null, null, null);
        });
    }

    @Test
    public void testStringKeySerialize() {
        // Typical use case
        StdKeySerializers.StringKeySerializer stringKeySerializer = new StdKeySerializers.StringKeySerializer();
        assertNotNull(stringKeySerializer);

        // Edge case
        assertDoesNotThrow(() -> {
            stringKeySerializer.serialize("test", null, null);
        });

        // Exception handling
        assertThrows(Exception.class, () -> {
            stringKeySerializer.serialize(null, null, null);
        });
    }
}