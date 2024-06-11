
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdDelegatingSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Type;

public class StdDelegatingSerializerTest {
    private StdDelegatingSerializer serializer;
    private Converter<Object, ?> converter;
    private JavaType delegateType;
    private JsonSerializer<Object> delegateSerializer;

    @Before
    public void setup() {
        converter = Mockito.mock(Converter.class);
        delegateType = Mockito.mock(JavaType.class);
        delegateSerializer = Mockito.mock(JsonSerializer.class);

        serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);
    }

    @Test
    public void testSerialize_NullValue_ShouldCallDefaultSerializeNull() throws IOException {
        // Arrange
        Object value = null;
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);

        // Act
        serializer.serialize(value, gen, provider);

        // Assert
        Mockito.verify(provider, Mockito.times(1)).defaultSerializeNull(gen);
    }

    @Test
    public void testSerialize_NonNullValue_ShouldCallSerializeOnDelegateSerializer() throws IOException {
        // Arrange
        Object value = new Object();
        String expectedSerializedValue = "serializedValue";
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        Mockito.when(converter.convert(value)).thenReturn("convertedValue");
        Mockito.when(delegateSerializer.serialize("convertedValue", gen, provider)).thenReturn(expectedSerializedValue);

        // Act
        serializer.serialize(value, gen, provider);

        // Assert
        Mockito.verify(delegateSerializer, Mockito.times(1)).serialize("convertedValue", gen, provider);
        Mockito.verify(gen, Mockito.times(0)).writeNull();
        Mockito.verify(gen, Mockito.times(0)).writeString(expectedSerializedValue);
    }

    @Test
    public void testSerialize_WithNullDelegateValue_ShouldCallDefaultSerializeNull() throws IOException {
        // Arrange
        Object value = new Object();
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        Mockito.when(converter.convert(value)).thenReturn(null);

        // Act
        serializer.serialize(value, gen, provider);

        // Assert
        Mockito.verify(provider, Mockito.times(1)).defaultSerializeNull(gen);
    }

    @Test
    public void testSerializeWithType_ShouldCallSerializeWithTypeOnDelegateSerializer() throws IOException {
        // Arrange
        Object value = new Object();
        String expectedSerializedValue = "serializedValue";
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        TypeSerializer typeSerializer = Mockito.mock(TypeSerializer.class);
        Mockito.when(converter.convert(value)).thenReturn("convertedValue");
        Mockito.when(delegateSerializer.serializeWithType("convertedValue", gen, provider, typeSerializer)).thenReturn(expectedSerializedValue);

        // Act
        serializer.serializeWithType(value, gen, provider, typeSerializer);

        // Assert
        Mockito.verify(delegateSerializer, Mockito.times(1)).serializeWithType("convertedValue", gen, provider, typeSerializer);
        Mockito.verify(gen, Mockito.times(0)).writeNull();
        Mockito.verify(gen, Mockito.times(0)).writeString(expectedSerializedValue);
    }

    @Test
    public void testIsEmpty_ShouldCallIsEmptyOnDelegateSerializer() {
        // Arrange
        Object value = new Object();
        Object delegateValue = new Object();
        Mockito.when(converter.convert(value)).thenReturn(delegateValue);
        Mockito.when(delegateSerializer.isEmpty(delegateValue)).thenReturn(true);

        // Act
        boolean result = serializer.isEmpty(value);

        // Assert
        Assert.assertTrue(result);
        Mockito.verify(delegateSerializer, Mockito.times(1)).isEmpty(delegateValue);
    }

    @Test
    public void testIsEmpty_WithNullDelegateValue_ShouldReturnFalse() {
        // Arrange
        Object value = new Object();
        Mockito.when(converter.convert(value)).thenReturn(null);

        // Act
        boolean result = serializer.isEmpty(value);

        // Assert
        Assert.assertFalse(result);
        Mockito.verify(delegateSerializer, Mockito.times(0)).isEmpty(Mockito.any());
    }

    @Test
    public void testIsEmpty_Provider_ShouldCallIsEmptyOnDelegateSerializer() {
        // Arrange
        Object value = new Object();
        Object delegateValue = new Object();
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        Mockito.when(converter.convert(value)).thenReturn(delegateValue);
        Mockito.when(delegateSerializer.isEmpty(provider, delegateValue)).thenReturn(true);

        // Act
        boolean result = serializer.isEmpty(provider, value);

        // Assert
        Assert.assertTrue(result);
        Mockito.verify(delegateSerializer, Mockito.times(1)).isEmpty(provider, delegateValue);
    }

    @Test
    public void testIsEmpty_Provider_WithNullDelegateValue_ShouldReturnFalse() {
        // Arrange
        Object value = new Object();
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        Mockito.when(converter.convert(value)).thenReturn(null);

        // Act
        boolean result = serializer.isEmpty(provider, value);

        // Assert
        Assert.assertFalse(result);
        Mockito.verify(delegateSerializer, Mockito.times(0)).isEmpty(Mockito.any(SerializerProvider.class), Mockito.any());
    }

    @Test
    public void testGetSchema_WithSchemaAwareDelegateSerializer_ShouldCallGetSchemaOnDelegateSerializer() throws JsonMappingException {
        // Arrange
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        Type typeHint = Mockito.mock(Type.class);
        JsonNode expectedSchema = Mockito.mock(JsonNode.class);
        Mockito.when(delegateSerializer instanceof SchemaAware).thenReturn(true);
        Mockito.when(((SchemaAware) delegateSerializer).getSchema(provider, typeHint)).thenReturn(expectedSchema);

        // Act
        JsonNode result = serializer.getSchema(provider, typeHint);

        // Assert
        Assert.assertEquals(expectedSchema, result);
        Mockito.verify(((SchemaAware) delegateSerializer), Mockito.times(1)).getSchema(provider, typeHint);
    }

    @Test
    public void testGetSchema_WithNonSchemaAwareDelegateSerializer_ShouldCallSuperGetSchema() throws JsonMappingException {
        // Arrange
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        Type typeHint = Mockito.mock(Type.class);
        JsonNode expectedSchema = Mockito.mock(JsonNode.class);
        Mockito.when(delegateSerializer instanceof SchemaAware).thenReturn(false);
        Mockito.when(serializer.getSchema(provider, typeHint)).thenReturn(expectedSchema);

        // Act
        JsonNode result = serializer.getSchema(provider, typeHint);

        // Assert
        Assert.assertEquals(expectedSchema, result);
        Mockito.verify(serializer, Mockito.times(1)).getSchema(provider, typeHint);
    }

    @Test
    public void testGetSchema_WithSchemaAwareDelegateSerializer_ShouldCallGetSchemaOnDelegateSerializerWithIsOptional() throws JsonMappingException {
        // Arrange
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        Type typeHint = Mockito.mock(Type.class);
        JsonNode expectedSchema = Mockito.mock(JsonNode.class);
        Mockito.when(delegateSerializer instanceof SchemaAware).thenReturn(true);
        Mockito.when(((SchemaAware) delegateSerializer).getSchema(provider, typeHint, true)).thenReturn(expectedSchema);

        // Act
        JsonNode result = serializer.getSchema(provider, typeHint, true);

        // Assert
        Assert.assertEquals(expectedSchema, result);
        Mockito.verify(((SchemaAware) delegateSerializer), Mockito.times(1)).getSchema(provider, typeHint, true);
    }

    @Test
    public void testGetSchema_WithNonSchemaAwareDelegateSerializer_ShouldCallSuperGetSchema() throws JsonMappingException {
        // Arrange
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        Type typeHint = Mockito.mock(Type.class);
        JsonNode expectedSchema = Mockito.mock(JsonNode.class);
        Mockito.when(delegateSerializer instanceof SchemaAware).thenReturn(false);
        Mockito.when(serializer.getSchema(provider, typeHint, true)).thenReturn(expectedSchema);

        // Act
        JsonNode result = serializer.getSchema(provider, typeHint, true);

        // Assert
        Assert.assertEquals(expectedSchema, result);
        Mockito.verify(serializer, Mockito.times(1)).getSchema(provider, typeHint, true);
    }

    @Test
    public void testAcceptJsonFormatVisitor_ShouldCallAcceptJsonFormatVisitorOnDelegateSerializer() throws JsonMappingException {
        // Arrange
        JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = Mockito.mock(JavaType.class);

        // Act
        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        // Assert
        Mockito.verify(delegateSerializer, Mockito.times(1)).acceptJsonFormatVisitor(visitor, typeHint);
    }

    @Test
    public void testConvertValue_ShouldCallConvertOnConverter() {
        // Arrange
        Object value = new Object();
        Object expectedConvertedValue = new Object();
        Mockito.when(converter.convert(value)).thenReturn(expectedConvertedValue);

        // Act
        Object result = serializer.convertValue(value);

        // Assert
        Assert.assertEquals(expectedConvertedValue, result);
        Mockito.verify(converter, Mockito.times(1)).convert(value);
    }

    @Test
    public void testWithDelegate_ShouldReturnNewInstanceWithDelegateSerializer() {
        // Arrange
        Converter<Object, ?> newConverter = Mockito.mock(Converter.class);
        JavaType newDelegateType = Mockito.mock(JavaType.class);
        JsonSerializer<Object> newDelegateSerializer = Mockito.mock(JsonSerializer.class);

        // Act
        StdDelegatingSerializer newSerializer = serializer.withDelegate(newConverter, newDelegateType, newDelegateSerializer);

        // Assert
        Assert.assertNotEquals(serializer, newSerializer);
        Assert.assertEquals(serializer.getConverter(), newConverter);
        Assert.assertEquals(serializer._delegateType, newDelegateType);
        Assert.assertEquals(serializer._delegateSerializer, newDelegateSerializer);
    }
}
