import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Calendar;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers;

@RunWith(JUnit4.class)
public class StdKeySerializersTest {

    @Test
    public void testGetStdKeySerializer_NullRawKeyType_UseDefault_ReturnsDynamic() {
        // Arrange
        SerializationConfig config = null;
        Class<?> rawKeyType = null;
        boolean useDefault = true;

        // Act
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);

        // Assert
        assertNotNull(serializer);
        assertTrue(serializer instanceof StdKeySerializers.Dynamic);
    }

    @Test
    public void testGetStdKeySerializer_RawKeyTypeString_ReturnsDefaultStringSerializer() {
        // Arrange
        SerializationConfig config = null;
        Class<?> rawKeyType = String.class;
        boolean useDefault = true;

        // Act
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);

        // Assert
        assertNotNull(serializer);
        assertTrue(serializer instanceof StdKeySerializers.Default);
    }

    @Test
    public void testGetStdKeySerializer_RawKeyTypePrimitive_ReturnsDefaultKeySerializer() {
        // Arrange
        SerializationConfig config = null;
        Class<?> rawKeyType = int.class;
        boolean useDefault = true;

        // Act
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);

        // Assert
        assertNotNull(serializer);
        assertTrue(serializer instanceof StdKeySerializers.Default);
    }

    @Test
    public void testGetStdKeySerializer_RawKeyTypeNumber_ReturnsDefaultKeySerializer() {
        // Arrange
        SerializationConfig config = null;
        Class<?> rawKeyType = Double.class;
        boolean useDefault = true;

        // Act
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);

        // Assert
        assertNotNull(serializer);
        assertTrue(serializer instanceof StdKeySerializers.Default);
    }

    @Test
    public void testGetStdKeySerializer_RawKeyTypeClass_ReturnsDefaultClassSerializer() {
        // Arrange
        SerializationConfig config = null;
        Class<?> rawKeyType = Class.class;
        boolean useDefault = true;

        // Act
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);

        // Assert
        assertNotNull(serializer);
        assertTrue(serializer instanceof StdKeySerializers.Default);
    }

    @Test
    public void testGetStdKeySerializer_RawKeyTypeDate_ReturnsDefaultDateSerializer() {
        // Arrange
        SerializationConfig config = null;
        Class<?> rawKeyType = Date.class;
        boolean useDefault = true;

        // Act
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);

        // Assert
        assertNotNull(serializer);
        assertTrue(serializer instanceof StdKeySerializers.Default);
    }

    @Test
    public void testGetStdKeySerializer_RawKeyTypeCalendar_ReturnsDefaultCalendarSerializer() {
        // Arrange
        SerializationConfig config = null;
        Class<?> rawKeyType = Calendar.class;
        boolean useDefault = true;

        // Act
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);

        // Assert
        assertNotNull(serializer);
        assertTrue(serializer instanceof StdKeySerializers.Default);
    }

    @Test
    public void testGetStdKeySerializer_RawKeyTypeUUID_ReturnsDefaultToStringSerializer() {
        // Arrange
        SerializationConfig config = null;
        Class<?> rawKeyType = java.util.UUID.class;
        boolean useDefault = true;

        // Act
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);

        // Assert
        assertNotNull(serializer);
        assertTrue(serializer instanceof StdKeySerializers.Default);
    }

    @Test
    public void testGetStdKeySerializer_RawKeyTypeEnum_ReturnsDefaultEnumSerializer() {
        // Arrange
        SerializationConfig config = null;
        Class<?> rawKeyType = MyEnum.class;
        boolean useDefault = true;

        // Act
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);

        // Assert
        assertNotNull(serializer);
        assertTrue(serializer instanceof StdKeySerializers.Default);
    }

    @Test
    public void testGetStdKeySerializer_RawKeyTypeUnknown_ReturnsNull() {
        // Arrange
        SerializationConfig config = null;
        Class<?> rawKeyType = UnknownType.class;
        boolean useDefault = true;

        // Act
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);

        // Assert
        assertNull(serializer);
    }

    @Test
    public void testGetFallbackKeySerializer_RawKeyTypeNullOrEnum_ReturnsDynamic() {
        // Arrange
        SerializationConfig config = null;
        Class<?> rawKeyType1 = null;
        Class<?> rawKeyType2 = MyEnum.class;

        // Act
        JsonSerializer<Object> serializer1 = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType1);
        JsonSerializer<Object> serializer2 = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType2);

        // Assert
        assertNotNull(serializer1);
        assertTrue(serializer1 instanceof StdKeySerializers.Dynamic);
        assertNotNull(serializer2);
        assertTrue(serializer2 instanceof StdKeySerializers.Dynamic);
    }

    @Test
    public void testGetFallbackKeySerializer_RawKeyTypeEnum_ReturnsDefaultEnumSerializer() {
        // Arrange
        SerializationConfig config = null;
        Class<?> rawKeyType = MyEnum.class;

        // Act
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);

        // Assert
        assertNotNull(serializer);
        assertTrue(serializer instanceof StdKeySerializers.Default);
    }

    @Test
    public void testGetFallbackKeySerializer_RawKeyTypeUnknown_ReturnsDefaultKeySerializer() {
        // Arrange
        SerializationConfig config = null;
        Class<?> rawKeyType = UnknownType.class;

        // Act
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);

        // Assert
        assertNotNull(serializer);
        assertTrue(serializer instanceof StdKeySerializers.Default);
    }

    /**
     * Helper enum class for testing purposes only.
     */
    private enum MyEnum {
        VALUE1, VALUE2, VALUE3
    }

    /**
     * Helper class for testing purposes only.
     */
    private static class UnknownType {
    }
}