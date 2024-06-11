import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdDelegatingSerializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class StdDelegatingSerializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testSerialize_NullValue_DelegatesToDefaultSerializeNull() throws IOException {
        // Arrange
        Converter<Object, Object> converter = mock(Converter.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        // Act
        serializer.serialize(null, generator, provider);

        // Assert
        verify(provider, times(1)).defaultSerializeNull(generator);
    }

    @Test
    public void testSerialize_WithValue_DelegatesToDelegateSerializer() throws IOException {
        // Arrange
        Converter<Object, Object> converter = mock(Converter.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Object value = new Object();
        JsonSerializer<Object> delegateSerializer = mock(JsonSerializer.class);
        serializer._delegateSerializer = delegateSerializer;

        // Act
        serializer.serialize(value, generator, provider);

        // Assert
        verify(delegateSerializer, times(1)).serialize(value, generator, provider);
    }

    @Test
    public void testSerialize_WithConvertedValue_Null_DelegatesToDefaultSerializeNull() throws IOException {
        // Arrange
        Converter<Object, Object> converter = mock(Converter.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Object value = new Object();
        when(converter.convert(value)).thenReturn(null);

        // Act
        serializer.serialize(value, generator, provider);

        // Assert
        verify(provider, times(1)).defaultSerializeNull(generator);
    }

    @Test
    public void testSerializeWithType_DelegatesToDelegateSerializer() throws IOException {
        // Arrange
        Converter<Object, Object> converter = mock(Converter.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        TypeSerializer typeSerializer = mock(TypeSerializer.class);
        Object value = new Object();
        JsonSerializer<Object> delegateSerializer = mock(JsonSerializer.class);
        serializer._delegateSerializer = delegateSerializer;

        // Act
        serializer.serializeWithType(value, generator, provider, typeSerializer);

        // Assert
        verify(delegateSerializer, times(1)).serializeWithType(value, generator, provider, typeSerializer);
    }

    @Test
    public void testIsEmpty_WithValue_DelegatesToDelegateSerializer() {
        // Arrange
        Converter<Object, Object> converter = mock(Converter.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        Object value = new Object();
        JsonSerializer<Object> delegateSerializer = mock(JsonSerializer.class);
        serializer._delegateSerializer = delegateSerializer;

        // Act
        boolean result = serializer.isEmpty(value);

        // Assert
        verify(delegateSerializer, times(1)).isEmpty(value);
    }

    @Test
    public void testIsEmpty_WithConvertedValue_Null_DelegatesToDelegateSerializer() {
        // Arrange
        Converter<Object, Object> converter = mock(Converter.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        Object value = new Object();
        when(converter.convert(value)).thenReturn(null);
        JsonSerializer<Object> delegateSerializer = mock(JsonSerializer.class);
        serializer._delegateSerializer = delegateSerializer;

        // Act
        boolean result = serializer.isEmpty(value);

        // Assert
        verify(delegateSerializer, times(1)).isEmpty(null);
    }

    @Test
    public void testIsEmpty_WithSerializerProvider_DelegatesToDelegateSerializer() {
        // Arrange
        Converter<Object, Object> converter = mock(Converter.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        Object value = new Object();
        SerializerProvider prov = mock(SerializerProvider.class);
        JsonSerializer<Object> delegateSerializer = mock(JsonSerializer.class);
        serializer._delegateSerializer = delegateSerializer;

        // Act
        boolean result = serializer.isEmpty(prov, value);

        // Assert
        verify(delegateSerializer, times(1)).isEmpty(prov, value);
    }

    @Test
    public void testGetSchema_WithSerializerProviderAndType_DelegatesToDelegateSerializer() throws JsonMappingException {
        // Arrange
        Converter<Object, Object> converter = mock(Converter.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        SerializerProvider provider = mock(SerializerProvider.class);
        TypeFactory typeFactory = mock(TypeFactory.class);
        Type type = String.class;
        JsonSerializer<Object> delegateSerializer = mock(JsonSerializer.class);
        serializer._delegateSerializer = delegateSerializer;
        when(provider.getTypeFactory()).thenReturn(typeFactory);
        when(typeFactory.constructType(type)).thenReturn(TypeFactory.defaultInstance().constructType(type));
        when(delegateSerializer.getSchema(provider, type)).thenReturn(mock(JsonNode.class));

        // Act
        JsonNode result = serializer.getSchema(provider, type);

        // Assert
        verify(delegateSerializer, times(1)).getSchema(provider, type);
    }

    @Test
    public void testGetSchema_WithSerializerProviderAndTypeAndIsOptional_DelegatesToDelegateSerializer() throws JsonMappingException {
        // Arrange
        Converter<Object, Object> converter = mock(Converter.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        SerializerProvider provider = mock(SerializerProvider.class);
        TypeFactory typeFactory = mock(TypeFactory.class);
        Type type = String.class;
        boolean isOptional = true;
        JsonSerializer<Object> delegateSerializer = mock(JsonSerializer.class);
        schema._delegateSerializer = delegateSerializer;
        when(provider.getTypeFactory()).thenReturn(typeFactory);
        when(typeFactory.constructType(type)).thenReturn(TypeFactory.defaultInstance().constructType(type));
        when(delegateSerializer.getSchema(provider, type, isOptional)).thenReturn(mock(JsonNode.class));

        // Act
        JsonNode result = serializer.getSchema(provider, type, isOptional);

        // Assert
        verify(delegateSerializer, times(1)).getSchema(provider, type, isOptional);
    }

    @Test
    public void testAcceptJsonFormatVisitor_WithJsonFormatVisitorWrapperAndType_DelegatesToDelegateSerializer() throws JsonMappingException {
        // Arrange
        Converter<Object, Object> converter = mock(Converter.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        TypeFactory typeFactory = mock(TypeFactory.class);
        Type type = String.class;
        JsonSerializer<Object> delegateSerializer = mock(JsonSerializer.class);
        schema._delegateSerializer = delegateSerializer;
        when(delegateSerializer.acceptJsonFormatVisitor(visitor, type)).thenReturn(true);

        // Act
        JsonNode result = serializer.acceptJsonFormatVisitor(visitor, type);

        // Assert
        verify(delegateSerializer, times(1)).acceptJsonFormatVisitor(visitor, type);
    }

    @Test(expected = IllegalStateException.class)
    public void withDelegate_WithNonSubClass_ThrowsIllegalStateException() {
        // Arrange
        Converter<Object, Object> converter = mock(Converter.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);

        // Act
        serializer.withDelegate(converter, null, null);
    }
}