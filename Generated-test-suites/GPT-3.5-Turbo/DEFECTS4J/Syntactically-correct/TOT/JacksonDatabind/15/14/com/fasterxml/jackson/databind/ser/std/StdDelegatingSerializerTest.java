import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class StdDelegatingSerializerTest {

    private StdDelegatingSerializer serializer;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        serializer = new StdDelegatingSerializer();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testSerialize_WithNullValue_ShouldSerializeNullValue() throws Exception {
        // Arrange
        Object value = null;
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        // Act
        serializer.serialize(value, gen, provider);

        // Assert
        verify(provider).defaultSerializeNull(gen);
    }

    @Test
    public void testSerialize_WithNonNullValue_ShouldSerializeConvertedValue() throws Exception {
        // Arrange
        Object value = new Object();
        Object convertedValue = new Object();
        Converter<Object, Object> converter = mock(Converter.class);
        when(converter.convert(value)).thenReturn(convertedValue);

        serializer = new StdDelegatingSerializer(converter);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        // Act
        serializer.serialize(value, gen, provider);

        // Assert
        verify(gen).writeObject(convertedValue);
    }

    @Test
    public void testSerializeWithType_WithNonNullValue_ShouldSerializeConvertedValueWithType() throws Exception {
        // Arrange
        Object value = new Object();
        Object convertedValue = new Object();
        Converter<Object, Object> converter = mock(Converter.class);
        when(converter.convert(value)).thenReturn(convertedValue);

        serializer = new StdDelegatingSerializer(converter);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        TypeSerializer typeSer = mock(TypeSerializer.class);

        // Act
        serializer.serializeWithType(value, gen, provider, typeSer);

        // Assert
        verify(typeSer).writeTypeSuffixForObject(eq(convertedValue), any(JsonGenerator.class));
    }

    @Test
    public void testIsEmpty_WithNullValue_ShouldReturnTrue() throws Exception {
        // Arrange
        Object value = null;
        Object convertedValue = new Object();
        Converter<Object, Object> converter = mock(Converter.class);
        when(converter.convert(value)).thenReturn(convertedValue);

        serializer = new StdDelegatingSerializer(converter);
        SerializerProvider prov = mock(SerializerProvider.class);

        // Act
        boolean result = serializer.isEmpty(prov, value);

        // Assert
        assertEquals(true, result);
    }

    @Test
    public void testIsEmpty_WithNonNullValue_ShouldCallDelegateIsEmpty() throws Exception {
        // Arrange
        Object value = new Object();
        Object convertedValue = new Object();
        Converter<Object, Object> converter = mock(Converter.class);
        when(converter.convert(value)).thenReturn(convertedValue);

        serializer = new StdDelegatingSerializer(converter);
        SerializerProvider prov = mock(SerializerProvider.class);

        JsonSerializer<Object> delegateSerializer = mock(JsonSerializer.class);
        serializer._delegateSerializer = delegateSerializer;

        // Act
        serializer.isEmpty(prov, value);

        // Assert
        verify(delegateSerializer).isEmpty(convertedValue);
    }

    @Test
    public void testGetSchema_WithSchemaAwareDelegateSerializer_ShouldCallDelegateGetSchema() throws Exception {
        // Arrange
        SchemaAware delegateSerializer = mock(SchemaAware.class);
        serializer._delegateSerializer = delegateSerializer;

        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = mock(Type.class);

        // Act
        serializer.getSchema(provider, typeHint);

        // Assert
        verify(delegateSerializer).getSchema(provider, typeHint);
    }

    @Test
    public void testGetSchema_WithNonSchemaAwareDelegateSerializer_ShouldCallSuperGetSchema() throws Exception {
        // Arrange
        JsonSerializer<Object> delegateSerializer = mock(JsonSerializer.class);
        serializer._delegateSerializer = delegateSerializer;

        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = mock(Type.class);

        // Act
        serializer.getSchema(provider, typeHint);

        // Assert
        verify(serializer).getSchema(provider, typeHint);
    }

    @Test
    public void testGetSchema_WithSchemaAwareDelegateSerializer_ShouldCallDelegateGetSchemaWithIsOptional() throws Exception {
        // Arrange
        SchemaAware delegateSerializer = mock(SchemaAware.class);
        serializer._delegateSerializer = delegateSerializer;

        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = mock(Type.class);
        boolean isOptional = true;

        // Act
        serializer.getSchema(provider, typeHint, isOptional);

        // Assert
        verify(delegateSerializer).getSchema(provider, typeHint, isOptional);
    }

    @Test
    public void testGetSchema_WithNonSchemaAwareDelegateSerializer_ShouldCallSuperGetSchemaWithIsOptional() throws Exception {
        // Arrange
        JsonSerializer<Object> delegateSerializer = mock(JsonSerializer.class);
        serializer._delegateSerializer = delegateSerializer;

        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = mock(Type.class);
        boolean isOptional = true;

        // Act
        serializer.getSchema(provider, typeHint, isOptional);

        // Assert
        verify(serializer).getSchema(provider, typeHint);
    }

    @Test
    public void testAcceptJsonFormatVisitor_WithJsonFormatVisitor_ShouldCallDelegateAcceptJsonFormatVisitor() throws Exception {
        // Arrange
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);

        JsonSerializer<Object> delegateSerializer = mock(JsonSerializer.class);
        serializer._delegateSerializer = delegateSerializer;

        // Act
        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        // Assert
        verify(delegateSerializer).acceptJsonFormatVisitor(visitor, typeHint);
    }

    @Test
    public void testConvertValue_WithConverter_ShouldReturnConvertedValue() throws Exception {
        // Arrange
        Object value = new Object();
        Object convertedValue = new Object();
        Converter<Object, Object> converter = mock(Converter.class);
        when(converter.convert(value)).thenReturn(convertedValue);

        // Act
        Object result = serializer.convertValue(value);

        // Assert
        assertEquals(convertedValue, result);
    }

}