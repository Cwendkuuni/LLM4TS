import static org.junit.Assert.*;
import org.junit.Test;

public class StdKeySerializersTest {

    @Test
    public void testGetStdKeySerializer() {
        // Typical use case
        assertNotNull(StdKeySerializers.getStdKeySerializer(null, String.class, true));

        // Edge cases
        assertNull(StdKeySerializers.getStdKeySerializer(null, Object.class, false));
        assertNotNull(StdKeySerializers.getStdKeySerializer(null, java.util.UUID.class, true));
    }

    @Test
    public void testGetFallbackKeySerializer() {
        // Typical use case
        assertNotNull(StdKeySerializers.getFallbackKeySerializer(null, Enum.class));

        // Edge cases
        assertNotNull(StdKeySerializers.getFallbackKeySerializer(null, String.class));
        assertNotNull(StdKeySerializers.getFallbackKeySerializer(null, Enum.class));
    }

    @Test
    public void testGetDefault() {
        // Typical use case
        assertNotNull(StdKeySerializers.getDefault());
    }

    @Test
    public void testDefaultSerialize() {
        // Typical use case
        StdKeySerializers.Default defaultSerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_CLASS, String.class);
        assertNotNull(defaultSerializer);
    }

    @Test
    public void testDynamicSerialize() {
        // Typical use case
        StdKeySerializers.Dynamic dynamicSerializer = new StdKeySerializers.Dynamic();
        assertNotNull(dynamicSerializer);
    }

    @Test
    public void testStringKeySerialize() {
        // Typical use case
        StdKeySerializers.StringKeySerializer stringKeySerializer = new StdKeySerializers.StringKeySerializer();
        assertNotNull(stringKeySerializer);
    }
}