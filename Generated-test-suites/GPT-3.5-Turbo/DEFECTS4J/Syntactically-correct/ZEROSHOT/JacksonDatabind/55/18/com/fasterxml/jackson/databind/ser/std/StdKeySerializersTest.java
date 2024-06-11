package com.fasterxml.jackson.databind.ser.std;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;

@RunWith(MockitoJUnitRunner.class)
public class StdKeySerializersTest {

    private StdKeySerializers stdKeySerializers;

    @Before
    public void setup() {
        stdKeySerializers = new StdKeySerializers();
    }

    @Test
    public void testGetStdKeySerializer_NullRawKeyType_ReturnsDynamicSerializer() {
    
        // Arrange
        SerializationConfig config = Mockito.mock(SerializationConfig.class);
        Class<?> rawKeyType = null;
        boolean useDefault = true;

        // Act
        JsonSerializer<Object> result = stdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);

        // Assert
        assertTrue(result instanceof Dynamic);

    }

    @Test
    public void testGetStdKeySerializer_RawKeyTypeIsNull_ReturnsDynamicSerializer() {
    
        // Arrange
        SerializationConfig config = Mockito.mock(SerializationConfig.class);
        Class<?> rawKeyType = null;
        boolean useDefault = true;

        // Act
        JsonSerializer<Object> result = stdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);

        // Assert
        assertTrue(result instanceof Dynamic);

    }

    @Test
    public void testGetStdKeySerializer_RawKeyTypeIsObjectClass_ReturnsDynamicSerializer() {
    
        // Arrange
        SerializationConfig config = Mockito.mock(SerializationConfig.class);
        Class<?> rawKeyType = Object.class;
        boolean useDefault = true;

        // Act
        JsonSerializer<Object> result = stdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);

        // Assert
        assertTrue(result instanceof Dynamic);

    }

    @Test
    public void testGetStdKeySerializer_RawKeyTypeIsStringClass_ReturnsDefaultStringSerializer() {

        // Arrange
        SerializationConfig config = Mockito.mock(SerializationConfig.class);
        Class<?> rawKeyType = String.class;
        boolean useDefault = true;

        // Act
        JsonSerializer<Object> result = stdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);

        // Assert
        assertTrue(result instanceof StringKeySerializer);

    }

    @Test
    public void testGetStdKeySerializer_RawKeyTypeIsPrimitive_ReturnsDefaultKeySerializer() {

        // Arrange
        SerializationConfig config = Mockito.mock(SerializationConfig.class);
        Class<?> rawKeyType = boolean.class;
        boolean useDefault = true;

        // Act
        JsonSerializer<Object> result = stdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);

        // Assert
        assertTrue(result instanceof StdKeySerializer);

    }

    @Test
    public void testGetStdKeySerializer_RawKeyTypeIsNumberClass_ReturnsDefaultKeySerializer() {

        // Arrange
        SerializationConfig config = Mockito.mock(SerializationConfig.class);
        Class<?> rawKeyType = Number.class;
        boolean useDefault = true;

        // Act
        JsonSerializer<Object> result = stdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);

        // Assert
        assertTrue(result instanceof StdKeySerializer);

    }

    @Test
    public void testGetStdKeySerializer_RawKeyTypeIsClassClass_ReturnsDefaultDynamic() {

        // Arrange
        SerializationConfig config = Mockito.mock(SerializationConfig.class);
        Class<?> rawKeyType = Class.class;
        boolean useDefault = true;

        // Act
        JsonSerializer<Object> result = stdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);

        // Assert
        assertTrue(result instanceof Default);

    }

    @Test
    public void testGetStdKeySerializer_RawKeyTypeIsDateClass_ReturnsDefaultDynamic() {

        // Arrange
        SerializationConfig config = Mockito.mock(SerializationConfig.class);
        Class<?> rawKeyType = Date.class;
        boolean useDefault = true;

        // Act
        JsonSerializer<Object> result = stdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);

        // Assert
        assertTrue(result instanceof Default);

    }

    @Test
    public void testGetStdKeySerializer_RawKeyTypeIsCalendarClass_ReturnsDefaultDynamic() {

        // Arrange
        SerializationConfig config = Mockito.mock(SerializationConfig.class);
        Class<?> rawKeyType = Calendar.class;
        boolean useDefault = true;

        // Act
        JsonSerializer<Object> result = stdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);

        // Assert
        assertTrue(result instanceof Default);

    }

    @Test
    public void testGetStdKeySerializer_RawKeyTypeIsUUIDClass_ReturnsDefaultDynamic() {

        // Arrange
        SerializationConfig config = Mockito.mock(SerializationConfig.class);
        Class<?> rawKeyType = java.util.UUID.class;
        boolean useDefault = true;

        // Act
        JsonSerializer<Object> result = stdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);

        // Assert
        assertTrue(result instanceof Default);

    }

    @Test
    public void testGetStdKeySerializer_RawKeyTypeIsDifferent_ReturnsNull() {

        // Arrange
        SerializationConfig config = Mockito.mock(SerializationConfig.class);
        Class<?> rawKeyType = StdKeySerializersTest.class;
        boolean useDefault = true;

        // Act
        JsonSerializer<Object> result = stdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);

        // Assert
        assertNull(result);

    }

    @Test
    public void testGetStdKeySerializer_RawKeyTypeIsDifferentAndUseDefaultIsFalse_ReturnsNull() {

        // Arrange
        SerializationConfig config = Mockito.mock(SerializationConfig.class);
        Class<?> rawKeyType = StdKeySerializersTest.class;
        boolean useDefault = false;

        // Act
        JsonSerializer<Object> result = stdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);

        // Assert
        assertNull(result);

    }

    @Test
    public void testGetFallbackKeySerializer_RawKeyTypeIsNull_ReturnsDefaultKeySerializer() {

        // Arrange
        SerializationConfig config = Mockito.mock(SerializationConfig.class);
        Class<?> rawKeyType = null;

        // Act
        JsonSerializer<Object> result = stdKeySerializers.getFallbackKeySerializer(config, rawKeyType);

        // Assert
        assertTrue(result instanceof StdKeySerializer);

    }

    @Test
    public void testGetFallbackKeySerializer_RawKeyTypeIsEnumClass_ReturnsDynamic() {

        // Arrange
        SerializationConfig config = Mockito.mock(SerializationConfig.class);
        Class<?> rawKeyType = Enum.class;

        // Act
        JsonSerializer<Object> result = stdKeySerializers.getFallbackKeySerializer(config, rawKeyType);

        // Assert
        assertTrue(result instanceof Dynamic);

    }

    @Test
    public void testGetFallbackKeySerializer_RawKeyTypeIsEnum_ReturnsDefault() {

        // Arrange
        SerializationConfig config = Mockito.mock(SerializationConfig.class);
        Class<?> rawKeyType = SampleEnum.class;

        // Act
        JsonSerializer<Object> result = stdKeySerializers.getFallbackKeySerializer(config, rawKeyType);

        // Assert
        assertTrue(result instanceof Default);

    }

    @Test
    public void testDefaultGetDefault_ReturnsDefaultKeySerializer() {

        // Act
        JsonSerializer<Object> result = StdKeySerializers.getDefault();

        // Assert
        assertTrue(result instanceof StdKeySerializer);

    }

    private enum SampleEnum {
        ENUM_VALUE
    }

}