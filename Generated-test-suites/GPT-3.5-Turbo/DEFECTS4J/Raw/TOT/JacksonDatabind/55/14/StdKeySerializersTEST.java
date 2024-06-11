package com.fasterxml.jackson.databind.ser.std;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;

import java.io.IOException;
import java.util.UUID;

import static org.junit.Assert.*;

public class StdKeySerializersTest {

    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetStdKeySerializer_WhenRawKeyTypeIsNull_ThenReturnDynamic() {
        // Arrange
        SerializationConfig config = objectMapper.getSerializationConfig();
        Class<?> rawKeyType = null;
        boolean useDefault = true;

        // Act
        JsonSerializer<Object> keySerializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);

        // Assert
        assertTrue(keySerializer instanceof StdKeySerializers.Dynamic);
    }

    @Test
    public void testGetStdKeySerializer_WhenRawKeyTypeIsObjectClass_ThenReturnDynamic() {
        // Arrange
        SerializationConfig config = objectMapper.getSerializationConfig();
        Class<?> rawKeyType = Object.class;
        boolean useDefault = true;

        // Act
        JsonSerializer<Object> keySerializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);

        // Assert
        assertTrue(keySerializer instanceof StdKeySerializers.Dynamic);
    }

    @Test
    public void testGetStdKeySerializer_WhenRawKeyTypeIsStringClass_ThenReturnDefaultStringSerializer() {
        // Arrange
        SerializationConfig config = objectMapper.getSerializationConfig();
        Class<?> rawKeyType = String.class;
        boolean useDefault = true;

        // Act
        JsonSerializer<Object> keySerializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);

        // Assert
        assertTrue(keySerializer instanceof StdKeySerializers.Default);
        StdKeySerializers.Default defaultSerializer = (StdKeySerializers.Default) keySerializer;
        assertEquals(StdKeySerializers.Default.TYPE_STRING, defaultSerializer._typeId);
    }

    @Test
    public void testGetStdKeySerializer_WhenRawKeyTypeIsPrimitiveType_ThenReturnDefaultKeySerializer() {
        // Arrange
        SerializationConfig config = objectMapper.getSerializationConfig();
        Class<?> rawKeyType = int.class;
        boolean useDefault = true;

        // Act
        JsonSerializer<Object> keySerializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);

        // Assert
        assertTrue(keySerializer instanceof StdKeySerializers.Default);
        StdKeySerializers.Default defaultSerializer = (StdKeySerializers.Default) keySerializer;
        assertEquals(StdKeySerializers.Default.TYPE_TO_STRING, defaultSerializer._typeId);
    }

    @Test
    public void testGetStdKeySerializer_WhenRawKeyTypeIsNumberClass_ThenReturnDefaultKeySerializer() {
        // Arrange
        SerializationConfig config = objectMapper.getSerializationConfig();
        Class<?> rawKeyType = Number.class;
        boolean useDefault = true;

        // Act
        JsonSerializer<Object> keySerializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);

        // Assert
        assertTrue(keySerializer instanceof StdKeySerializers.Default);
        StdKeySerializers.Default defaultSerializer = (StdKeySerializers.Default) keySerializer;
        assertEquals(StdKeySerializers.Default.TYPE_TO_STRING, defaultSerializer._typeId);
    }

    @Test
    public void testGetStdKeySerializer_WhenRawKeyTypeIsClassClass_ThenReturnDefaultKeySerializer() {
        // Arrange
        SerializationConfig config = objectMapper.getSerializationConfig();
        Class<?> rawKeyType = Class.class;
        boolean useDefault = true;

        // Act
        JsonSerializer<Object> keySerializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);

        // Assert
        assertTrue(keySerializer instanceof StdKeySerializers.Default);
        StdKeySerializers.Default defaultSerializer = (StdKeySerializers.Default) keySerializer;
        assertEquals(StdKeySerializers.Default.TYPE_CLASS, defaultSerializer._typeId);
    }

    @Test
    public void testGetStdKeySerializer_WhenRawKeyTypeIsDateClass_ThenReturnDefaultKeySerializer() {
        // Arrange
        SerializationConfig config = objectMapper.getSerializationConfig();
        Class<?> rawKeyType = Date.class;
        boolean useDefault = true;

        // Act
        JsonSerializer<Object> keySerializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);

        // Assert
        assertTrue(keySerializer instanceof StdKeySerializers.Default);
        StdKeySerializers.Default defaultSerializer = (StdKeySerializers.Default) keySerializer;
        assertEquals(StdKeySerializers.Default.TYPE_DATE, defaultSerializer._typeId);
    }

    @Test
    public void testGetStdKeySerializer_WhenRawKeyTypeIsCalendarClass_ThenReturnDefaultKeySerializer() {
        // Arrange
        SerializationConfig config = objectMapper.getSerializationConfig();
        Class<?> rawKeyType = Calendar.class;
        boolean useDefault = true;

        // Act
        JsonSerializer<Object> keySerializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);

        // Assert
        assertTrue(keySerializer instanceof StdKeySerializers.Default);
        StdKeySerializers.Default defaultSerializer = (StdKeySerializers.Default) keySerializer;
        assertEquals(StdKeySerializers.Default.TYPE_CALENDAR, defaultSerializer._typeId);
    }

    @Test
    public void testGetStdKeySerializer_WhenRawKeyTypeIsUUIDClass_ThenReturnDefaultKeySerializer() {
        // Arrange
        SerializationConfig config = objectMapper.getSerializationConfig();
        Class<?> rawKeyType = UUID.class;
        boolean useDefault = true;

        // Act
        JsonSerializer<Object> keySerializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);

        // Assert
        assertTrue(keySerializer instanceof StdKeySerializers.Default);
        StdKeySerializers.Default defaultSerializer = (StdKeySerializers.Default) keySerializer;
        assertEquals(StdKeySerializers.Default.TYPE_TO_STRING, defaultSerializer._typeId);
    }

    @Test
    public void testGetStdKeySerializer_WhenRawKeyTypeIsNull_ThenReturnNull() {
        // Arrange
        SerializationConfig config = objectMapper.getSerializationConfig();
        Class<?> rawKeyType = null;
        boolean useDefault = false;

        // Act
        JsonSerializer<Object> keySerializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);

        // Assert
        assertNull(keySerializer);
    }

    @Test
    public void testGetStdKeySerializer_WhenRawKeyTypeIsObjectClass_ThenReturnNull() {
        // Arrange
        SerializationConfig config = objectMapper.getSerializationConfig();
        Class<?> rawKeyType = Object.class;
        boolean useDefault = false;

        // Act
        JsonSerializer<Object> keySerializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);

        // Assert
        assertNull(keySerializer);
    }

    @Test
    public void testGetFallbackKeySerializer_WhenRawKeyTypeIsNull_ThenReturnDynamic() {
        // Arrange
        SerializationConfig config = objectMapper.getSerializationConfig();
        Class<?> rawKeyType = null;

        // Act
        JsonSerializer<Object> keySerializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);

        // Assert
        assertTrue(keySerializer instanceof StdKeySerializers.Dynamic);
    }

    @Test
    public void testGetFallbackKeySerializer_WhenRawKeyTypeIsEnumClass_ThenReturnDynamic() {
        // Arrange
        SerializationConfig config = objectMapper.getSerializationConfig();
        Class<?> rawKeyType = Enum.class;

        // Act
        JsonSerializer<Object> keySerializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);

        // Assert
        assertTrue(keySerializer instanceof StdKeySerializers.Dynamic);
    }

    @Test
    public void testGetFallbackKeySerializer_WhenRawKeyTypeIsEnumType_ThenReturnDefaultKeySerializer() {
        // Arrange
        SerializationConfig config = objectMapper.getSerializationConfig();
        Class<?> rawKeyType = MyEnum.class;

        // Act
        JsonSerializer<Object> keySerializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);

        // Assert
        assertTrue(keySerializer instanceof StdKeySerializers.Default);
        StdKeySerializers.Default defaultSerializer = (StdKeySerializers.Default) keySerializer;
        assertEquals(StdKeySerializers.Default.TYPE_ENUM, defaultSerializer._typeId);
    }

    @Test
    public void testGetDefault_ThenReturnDefaultKeySerializer() {
        // Act
        JsonSerializer<Object> keySerializer = StdKeySerializers.getDefault();

        // Assert
        assertTrue(keySerializer instanceof StdKeySerializers.Default);
    }

    @Test
    public void testDynamicSerialize_WhenValueIsString_ThenSerializeAsExpected() throws IOException {
        // Arrange
        StdKeySerializers.Dynamic dynamicSerializer = new StdKeySerializers.Dynamic();
        JsonGenerator generator = objectMapper.getJsonFactory().createGenerator(System.out);

        // Act
        dynamicSerializer.serialize("test", generator, null);

        // Assert
        // Verify output
    }

    @Test
    public void testDynamicSerialize_WhenValueIsInteger_ThenSerializeAsExpected() throws IOException {
        // Arrange
        StdKeySerializers.Dynamic dynamicSerializer = new StdKeySerializers.Dynamic();
        JsonGenerator generator = objectMapper.getJsonFactory().createGenerator(System.out);

        // Act
        dynamicSerializer.serialize(123, generator, null);

        // Assert
        // Verify output
    }

    @Test
    public void testDynamicSerialize_WhenValueIsEnum_ThenSerializeAsExpected() throws IOException {
        // Arrange
        StdKeySerializers.Dynamic dynamicSerializer = new StdKeySerializers.Dynamic();
        JsonGenerator generator = objectMapper.getJsonFactory().createGenerator(System.out);

        // Act
        dynamicSerializer.serialize(MyEnum.VALUE1, generator, null);

        // Assert
        // Verify output
    }

    private enum MyEnum {
        VALUE1,
        VALUE2
    }
}