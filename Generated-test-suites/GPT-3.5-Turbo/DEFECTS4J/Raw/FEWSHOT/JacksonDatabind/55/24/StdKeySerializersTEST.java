package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;

@SuppressWarnings("serial")
public class StdKeySerializersTest {

    @Test
    public void testGetStdKeySerializer_WithRawKeyTypeNullAndUseDefaultTrue_ExpectDynamicTypeKeySerializer() {
        // Arrange
        SerializationConfig config = mock(SerializationConfig.class);
        Class<?> rawKeyType = null;
        boolean useDefault = true;

        // Act
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        
        // Assert
        assertEquals(StdKeySerializers.Dynamic.class, result.getClass());
    }

    @Test
    public void testGetStdKeySerializer_WithRawKeyTypeNullAndUseDefaultFalse_ExpectNullValue() {
        // Arrange
        SerializationConfig config = mock(SerializationConfig.class);
        Class<?> rawKeyType = null;
        boolean useDefault = false;

        // Act
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        
        // Assert
        assertNull(result);
    }

    // Additional test cases for other methods in StdKeySerializers

}