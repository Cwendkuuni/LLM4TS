import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdDelegatingSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Type;

public class StdDelegatingSerializerTest {

    private StdDelegatingSerializer serializer;

    @Before
    public void setup() {
        Converter<Object, String> converter = new Converter<Object, String>() {
            @Override
            public String convert(Object value) {
                return value.toString();
            }

            @Override
            public JavaType getInputType(SerializationConfig config) {
                return null;
            }

            @Override
            public JavaType getOutputType(SerializationConfig config) {
                return null;
            }
        };

        serializer = new StdDelegatingSerializer(converter);
    }

    @Test
    public void testSerialize_WithNonNullValue_ShouldCallDelegateSerializer() throws IOException {
        // Arrange
        JsonSerializer<Object> delegateSerializer = Mockito.mock(JsonSerializer.class);
        JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);

        serializer = serializer.withDelegate(serializer.getConverter(), Mockito.mock(JavaType.class), delegateSerializer);

        // Act
        serializer.serialize("test", jsonGenerator, serializerProvider);

        // Assert
        Mockito.verify(delegateSerializer, Mockito.times(1)).serialize(Matchers.anyString(), Matchers.any(JsonGenerator.class), Matchers.any(SerializerProvider.class));
    }

    @Test
    public void testSerialize_WithNullValue_ShouldCallDefaultSerializeNull() throws IOException {
        // Arrange
        JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);

        // Act
        serializer.serialize(null, jsonGenerator, serializerProvider);

        // Assert
        Mockito.verify(serializerProvider, Mockito.times(1)).defaultSerializeNull(jsonGenerator);
    }

    @Test
    public void testSerializeWithType_WithNonNullValue_ShouldCallDelegateSerializer() throws IOException {
        // Arrange
        JsonSerializer<Object> delegateSerializer = Mockito.mock(JsonSerializer.class);
        JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);
        TypeSerializer typeSerializer = Mockito.mock(TypeSerializer.class);

        serializer = serializer.withDelegate(serializer.getConverter(), Mockito.mock(JavaType.class), delegateSerializer);

        // Act
        serializer.serializeWithType("test", jsonGenerator, serializerProvider, typeSerializer);

        // Assert
        Mockito.verify(delegateSerializer, Mockito.times(1)).serializeWithType(Matchers.anyString(), Matchers.any(JsonGenerator.class), Matchers.any(SerializerProvider.class), Matchers.any(TypeSerializer.class));
    }

    @Test
    public void testIsEmpty_WithNonNullValue_ShouldCallDelegateSerializer() {
        // Arrange
        JsonSerializer<Object> delegateSerializer = Mockito.mock(JsonSerializer.class);
        Object value = new Object();

        serializer = serializer.withDelegate(serializer.getConverter(), Mockito.mock(JavaType.class), delegateSerializer);

        // Act
        serializer.isEmpty(value);

        // Assert
        Mockito.verify(delegateSerializer, Mockito.times(1)).isEmpty(Matchers.any(Object.class));
    }

    @Test
    public void testIsEmpty_WithNullValue_ShouldCallDelegateSerializer() {
        // Arrange
        JsonSerializer<Object> delegateSerializer = Mockito.mock(JsonSerializer.class);
        Object value = null;

        serializer = serializer.withDelegate(serializer.getConverter(), Mockito.mock(JavaType.class), delegateSerializer);

        // Act
        serializer.isEmpty(value);

        // Assert
        Mockito.verify(delegateSerializer, Mockito.times(1)).isEmpty(Matchers.any(Object.class));
    }

    @Test
    public void testIsEmptyWithSerializerProvider_WithNonNullValue_ShouldCallDelegateSerializer() {
        // Arrange
        JsonSerializer<Object> delegateSerializer = Mockito.mock(JsonSerializer.class);
        Object value = new Object();
        SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);

        serializer = serializer.withDelegate(serializer.getConverter(), Mockito.mock(JavaType.class), delegateSerializer);

        // Act
        serializer.isEmpty(serializerProvider, value);

        // Assert
        Mockito.verify(delegateSerializer, Mockito.times(1)).isEmpty(serializerProvider, value);
    }

    @Test
    public void testIsEmptyWithSerializerProvider_WithNullValue_ShouldCallDelegateSerializer() {
        // Arrange
        JsonSerializer<Object> delegateSerializer = Mockito.mock(JsonSerializer.class);
        Object value = null;
        SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);

        serializer = serializer.withDelegate(serializer.getConverter(), Mockito.mock(JavaType.class), delegateSerializer);

        // Act
        serializer.isEmpty(serializerProvider, value);

        // Assert
        Mockito.verify(delegateSerializer, Mockito.times(1)).isEmpty(serializerProvider, value);
    }

    @Test
    public void testGetSchema_WithSchemaAwareDelegateSerializer_ShouldCallDelegateSerializerGetSchema() throws JsonMappingException {
        // Arrange
        JsonSerializer<Object> delegateSerializer = Mockito.mock(JsonSerializer.class);
        Object value = new Object();
        SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);
        Type typeHint = String.class;

        serializer = serializer.withDelegate(serializer.getConverter(), Mockito.mock(JavaType.class), delegateSerializer);

        Mockito.when(delegateSerializer.getSchema(serializerProvider, typeHint)).thenReturn(Mockito.mock(JsonNode.class));

        // Act
        JsonNode actualResult = serializer.getSchema(serializerProvider, typeHint);

        // Assert
        Mockito.verify(delegateSerializer, Mockito.times(1)).getSchema(serializerProvider, typeHint);
        Assert.assertNotNull(actualResult);
    }

    @Test
    public void testGetSchema_WithNonSchemaAwareDelegateSerializer_ShouldReturnSuperGetSchema() throws JsonMappingException {
        // Arrange
        JsonSerializer<Object> delegateSerializer = Mockito.mock(JsonSerializer.class);
        Object value = new Object();
        SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);
        Type typeHint = String.class;

        serializer = serializer.withDelegate(serializer.getConverter(), Mockito.mock(JavaType.class), delegateSerializer);

        // Act
        JsonNode actualResult = serializer.getSchema(serializerProvider, typeHint);

        // Assert
        Mockito.verify(delegateSerializer, Mockito.times(0)).getSchema(serializerProvider, typeHint);
        Assert.assertNotNull(actualResult);
    }

    @Test
    public void testGetSchema_WithSchemaAwareDelegateSerializer_ShouldCallDelegateSerializerGetSchemaWithOptional() throws JsonMappingException {
        // Arrange
        JsonSerializer<Object> delegateSerializer = Mockito.mock(JsonSerializer.class);
        Object value = new Object();
        SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);
        Type typeHint = String.class;
        boolean isOptional = true;

        serializer = serializer.withDelegate(serializer.getConverter(), Mockito.mock(JavaType.class), delegateSerializer);

        Mockito.when(delegateSerializer.getSchema(serializerProvider, typeHint, isOptional)).thenReturn(Mockito.mock(JsonNode.class));

        // Act
        JsonNode actualResult = serializer.getSchema(serializerProvider, typeHint, isOptional);

        // Assert
        Mockito.verify(delegateSerializer, Mockito.times(1)).getSchema(serializerProvider, typeHint, isOptional);
        Assert.assertNotNull(actualResult);
    }

    @Test
    public void testGetSchema_WithNonSchemaAwareDelegateSerializer_ShouldReturnSuperGetSchemaWithOptional() throws JsonMappingException {
        // Arrange
        JsonSerializer<Object> delegateSerializer = Mockito.mock(JsonSerializer.class);
        Object value = new Object();
        SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);
        Type typeHint = String.class;
        boolean isOptional = true;

        serializer = serializer.withDelegate(serializer.getConverter(), Mockito.mock(JavaType.class), delegateSerializer);

        // Act
        JsonNode actualResult = serializer.getSchema(serializerProvider, typeHint, isOptional);

        // Assert
        Mockito.verify(delegateSerializer, Mockito.times(0)).getSchema(serializerProvider, typeHint, isOptional);
        Assert.assertNotNull(actualResult);
    }

    @Test
    public void testAcceptJsonFormatVisitor_WithSchemaAwareDelegateSerializer_ShouldCallDelegateSerializerAcceptJsonFormatVisitor() throws JsonMappingException {
        // Arrange
        JsonSerializer<Object> delegateSerializer = Mockito.mock(JsonSerializer.class);
        Object value = new Object();
        SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);
        JavaType typeHint = Mockito.mock(JavaType.class);

        serializer = serializer.withDelegate(serializer.getConverter(), Mockito.mock(JavaType.class), delegateSerializer);

        // Act
        serializer.acceptJsonFormatVisitor(Mockito.mock(JsonFormatVisitorWrapper.class), typeHint);

        // Assert
        Mockito.verify(delegateSerializer, Mockito.times(1)).acceptJsonFormatVisitor(Matchers.any(JsonFormatVisitorWrapper.class), Matchers.any(JavaType.class));
    }

    @Test
    public void testAcceptJsonFormatVisitor_WithNonSchemaAwareDelegateSerializer_ShouldNotCallDelegateSerializerAcceptJsonFormatVisitor() throws JsonMappingException {
        // Arrange
        JsonSerializer<Object> delegateSerializer = Mockito.mock(JsonSerializer.class);
        Object value = new Object();
        SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);
        JavaType typeHint = Mockito.mock(JavaType.class);

        serializer = serializer.withDelegate(serializer.getConverter(), Mockito.mock(JavaType.class), delegateSerializer);

        // Act
        serializer.acceptJsonFormatVisitor(Mockito.mock(JsonFormatVisitorWrapper.class), typeHint);

        // Assert
        Mockito.verify(delegateSerializer, Mockito.times(0)).acceptJsonFormatVisitor(Matchers.any(JsonFormatVisitorWrapper.class), Matchers.any(JavaType.class));
    }
}