
import org.junit.Test;

import static org.junit.Assert.*;

public class StdKeySerializersTest {

    @Test
    public void testGetStdKeySerializer_NullKeyType_ReturnsDynamicSerializer() {
        // Arrange
        SerializationConfig config = null;
        Class<?> rawKeyType = null;
        boolean useDefault = false;

        // Act
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);

        // Assert
        assertNotNull(serializer);
        assertTrue(serializer instanceof StdKeySerializers.Dynamic);
    }

    @Test
    public void testGetStdKeySerializer_ObjectKeyType_ReturnsDynamicSerializer() {
        // Arrange
        SerializationConfig config = null;
        Class<?> rawKeyType = Object.class;
        boolean useDefault = false;

        // Act
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);

        // Assert
        assertNotNull(serializer);
        assertTrue(serializer instanceof StdKeySerializers.Dynamic);
    }

    @Test
    public void testGetStdKeySerializer_StringKeyType_ReturnsDefaultStringSerializer() {
        // Arrange
        SerializationConfig config = null;
        Class<?> rawKeyType = String.class;
        boolean useDefault = false;

        // Act
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);

        // Assert
        assertNotNull(serializer);
        assertTrue(serializer instanceof StdKeySerializers.StringKeySerializer);
    }

    @Test
    public void testGetFallbackKeySerializer_ObjectKeyType_ReturnsDefaultKeySerializer() {
        // Arrange
        SerializationConfig config = null;
        Class<?> rawKeyType = Object.class;

        // Act
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);

        // Assert
        assertNotNull(serializer);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);
    }

    @Test
    public void testGetDefault_ReturnsDefaultKeySerializer() {
        // Arrange

        // Act
        JsonSerializer<Object> serializer = StdKeySerializers.getDefault();

        // Assert
        assertNotNull(serializer);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);
    }
}