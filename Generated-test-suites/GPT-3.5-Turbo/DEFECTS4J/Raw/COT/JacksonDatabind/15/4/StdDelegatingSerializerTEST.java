
package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class StdDelegatingSerializerTest {

    @Mock
    Converter converter;

    @Mock
    JavaType delegateType;

    @Mock
    JsonSerializer delegateSerializer;

    @Mock
    JsonGenerator generator;

    @Mock
    SerializerProvider provider;

    private StdDelegatingSerializer serializer;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);
    }

    @Test
    public void testSerialize_NullValue_SerializesNullUsingProvider() throws Exception {
        Object value = null;

        // Act
        serializer.serialize(value, generator, provider);

        // Assert
        verify(provider, times(1)).defaultSerializeNull(generator);
        verify(delegateSerializer, never()).serialize(any(), eq(generator), eq(provider));
    }

    @Test
    public void testSerialize_NonNullValue_SerializesConvertedDelegateValueUsingSerializer() throws Exception {
        Object value = new Object();
        Object convertedValue = new Object();

        // Arrange
        when(converter.convert(value)).thenReturn(convertedValue);

        // Act
        serializer.serialize(value, generator, provider);

        // Assert
        verify(delegateSerializer, times(1)).serialize(convertedValue, generator, provider);
        verify(provider, never()).defaultSerializeNull(generator);
    }

    @Test
    public void testSerializeWithType_SerializesDelegateValueWithType() throws Exception {
        Object value = new Object();
        Object convertedValue = new Object();

        // Arrange
        when(converter.convert(value)).thenReturn(convertedValue);

        // Act
        serializer.serializeWithType(value, generator, provider, null);

        // Assert
        verify(delegateSerializer, times(1)).serializeWithType(convertedValue, generator, provider, null);
    }

    @Test
    public void testIsEmpty_NullValue_DelegatesToDelegateSerializer() throws Exception {
        Object value = null;
        Object convertedValue = null;

        // Arrange
        when(converter.convert(value)).thenReturn(convertedValue);
        when(delegateSerializer.isEmpty(convertedValue)).thenReturn(true);

        // Act
        boolean result = serializer.isEmpty(provider, value);

        // Assert
        verify(delegateSerializer, times(1)).isEmpty(convertedValue);
        verify(provider, never()).defaultSerializeNull(generator);
        assert result;
    }

    // Remaining test cases for other methods

}
