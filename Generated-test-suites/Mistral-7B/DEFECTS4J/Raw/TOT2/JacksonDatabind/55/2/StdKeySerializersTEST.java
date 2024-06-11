import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class StdKeySerializersTest {
    @Test
    public void testGetStdKeySerializer() throws Exception {
        // Test with valid arguments
        SerializationConfig config = new SerializationConfig();
        Class<?> rawKeyType = String.class;
        boolean useDefault = false;
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertNotNull(serializer);

        // Test with null configuration
        config = null;
        serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertNull(serializer);

        // Test with null rawKeyType
        rawKeyType = null;
        serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertNull(serializer);

        // Test with null useDefault
        useDefault = null;
        serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertNull(serializer);

        // Test with different data types
        rawKeyType = Date.class;
        serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertNotNull(serializer);

        rawKeyType = Calendar.class;
        serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertNotNull(serializer);

        rawKeyType = Class.class;
        serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertNotNull(serializer);

        rawKeyType = Enum.class;
        serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertNotNull(serializer);

        rawKeyType = UUID.class;
        serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertNotNull(serializer);

        // Test configuration and rawKeyType being the same class
        config = new SerializationConfig();
        rawKeyType = config.getClass();
        serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertNotNull(serializer);

        config = new SerializationConfig();
        rawKeyType = config.getClass();
        useDefault = true;
        serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertNull(serializer);

        config = new SerializationConfig();
        rawKeyType = config.getClass();
        useDefault = false;
        serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertNotNull(serializer);
    }
}