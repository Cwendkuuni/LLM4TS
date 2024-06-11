import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class NumberSerializerTest {

    @Test
    public void testSerializeBigDecimal() throws IOException {
        // Arrange
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        BigDecimal value = new BigDecimal("123.456");

        // Act
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        // Assert
        verify(jsonGenerator).writeNumber(value);
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        // Arrange
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        BigInteger value = BigInteger.TEN;

        // Act
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        // Assert
        verify(jsonGenerator).writeNumber(value);
    }

    @Test
    public void testSerializeInteger() throws IOException {
        // Arrange
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        Integer value = 123;

        // Act
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        // Assert
        verify(jsonGenerator).writeNumber(value);
    }

    @Test
    public void testSerializeLong() throws IOException {
        // Arrange
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        Long value = 123L;

        // Act
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        // Assert
        verify(jsonGenerator).writeNumber(value);
    }

    @Test
    public void testSerializeDouble() throws IOException {
        // Arrange
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        Double value = 123.456;

        // Act
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        // Assert
        verify(jsonGenerator).writeNumber(value);
    }

    @Test
    public void testSerializeFloat() throws IOException {
        // Arrange
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        Float value = 123.456f;

        // Act
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        // Assert
        verify(jsonGenerator).writeNumber(value);
    }

    @Test
    public void testSerializeByte() throws IOException {
        // Arrange
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        Byte value = 123;

        // Act
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        // Assert
        verify(jsonGenerator).writeNumber(value.intValue());
    }

    @Test
    public void testSerializeShort() throws IOException {
        // Arrange
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        Short value = 123;

        // Act
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        // Assert
        verify(jsonGenerator).writeNumber(value.intValue());
    }

    @Test
    public void testSerializeFallback() throws IOException {
        // Arrange
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        Number value = 123.456789;

        // Act
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        // Assert
        verify(jsonGenerator).writeNumber(value.toString());
    }

    @Test
    public void testGetSchemaIsInt() {
        // Arrange
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        NumberSerializer numberSerializer = new NumberSerializer(BigInteger.class);

        // Act
        JsonNode schema = numberSerializer.getSchema(serializerProvider, null);

        // Assert
        assertEquals("integer", schema.asText());
    }

    @Test
    public void testGetSchemaIsNotInt() {
        // Arrange
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        NumberSerializer numberSerializer = new NumberSerializer(BigDecimal.class);

        // Act
        JsonNode schema = numberSerializer.getSchema(serializerProvider, null);

        // Assert
        assertEquals("number", schema.asText());
    }

    @Test
    public void testAcceptJsonFormatVisitorIsInt() throws JsonMappingException {
        // Arrange
        JsonFormatVisitorWrapper jsonFormatVisitorWrapper = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        NumberSerializer numberSerializer = new NumberSerializer(BigInteger.class);

        // Act
        numberSerializer.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, typeHint);

        // Assert
        verify(jsonFormatVisitorWrapper).visitIntFormat(typeHint, JsonParser.NumberType.BIG_INTEGER);
    }

    @Test
    public void testAcceptJsonFormatVisitorIsBigDecimal() throws JsonMappingException {
        // Arrange
        JsonFormatVisitorWrapper jsonFormatVisitorWrapper = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        NumberSerializer numberSerializer = new NumberSerializer(BigDecimal.class);

        // Act
        numberSerializer.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, typeHint);

        // Assert
        verify(jsonFormatVisitorWrapper).visitFloatFormat(typeHint, JsonParser.NumberType.BIG_INTEGER);
    }

    @Test
    public void testAcceptJsonFormatVisitorIsNotIntOrBigDecimal() throws JsonMappingException {
        // Arrange
        JsonFormatVisitorWrapper jsonFormatVisitorWrapper = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);

        // Act
        numberSerializer.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, typeHint);

        // Assert
        verify(jsonFormatVisitorWrapper).expectNumberFormat(typeHint);
    }
}