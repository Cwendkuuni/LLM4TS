import static org.junit.Assert.*;
import org.junit.Test;

public class StdKeySerializersTest {

    @Test
    public void testGetStdKeySerializer() {
        SerializationConfig config = null;
        Class<?> rawKeyType = String.class;

        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, true);

        assertNotNull(serializer);
        assertTrue(serializer instanceof StdKeySerializers.Default);
    }

    @Test
    public void testGetFallbackKeySerializer() {
        SerializationConfig config = null;
        Class<?> rawKeyType = Enum.class;

        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);

        assertNotNull(serializer);
        assertTrue(serializer instanceof StdKeySerializers.Dynamic);
    }

    @Test
    public void testGetDefault() {
        JsonSerializer<Object> defaultSerializer = StdKeySerializers.getDefault();

        assertNotNull(defaultSerializer);
        assertTrue(defaultSerializer instanceof StdKeySerializers.Default);
    }

    @Test
    public void testDefaultSerialize() {
        StdKeySerializers.Default defaultSerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_DATE, Date.class);

        JsonGenerator g = null; // Mock JsonGenerator
        SerializerProvider provider = null; // Mock SerializerProvider

        try {
            defaultSerializer.serialize(new Date(), g, provider);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testDynamicSerialize() {
        StdKeySerializers.Dynamic dynamicSerializer = new StdKeySerializers.Dynamic();

        JsonGenerator g = null; // Mock JsonGenerator
        SerializerProvider provider = null; // Mock SerializerProvider

        try {
            dynamicSerializer.serialize("test", g, provider);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testStringKeySerialize() {
        StdKeySerializers.StringKeySerializer stringKeySerializer = new StdKeySerializers.StringKeySerializer();

        JsonGenerator g = null; // Mock JsonGenerator
        SerializerProvider provider = null; // Mock SerializerProvider

        try {
            stringKeySerializer.serialize("test", g, provider);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
}