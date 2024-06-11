
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class NumberSerializerTest {

    @Test
    public void testSerializeBigDecimal() throws IOException {
        // Arrange
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        BigDecimal value = new BigDecimal("123.45");

        // Act
        numberSerializer.serialize(value, generator, provider);

        // Assert
        verify(generator).writeNumber(value);
    }
  
    @Test
    public void testSerializeBigInteger() throws IOException {
        // Arrange
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        BigInteger value = new BigInteger("12345");

        // Act
        numberSerializer.serialize(value, generator, provider);

        // Assert
        verify(generator).writeNumber(value);
    }

    @Test
    public void testSerializeLong() throws IOException {
        // Arrange
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        Long value = 12345L;

        // Act
        numberSerializer.serialize(value, generator, provider);

        // Assert
        verify(generator).writeNumber(value);
    }

    @Test
    public void testSerializeDouble() throws IOException {
        // Arrange
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        Double value = 123.45;

        // Act
        numberSerializer.serialize(value, generator, provider);

        // Assert
        verify(generator).writeNumber(value);
    }

    @Test
    public void testSerializeFloat() throws IOException {
        // Arrange
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        Float value = 12.34f;

        // Act
        numberSerializer.serialize(value, generator, provider);

        // Assert
        verify(generator).writeNumber(value);
    }

    @Test
    public void testSerializeInteger() throws IOException {
        // Arrange
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        Integer value = 123;

        // Act
        numberSerializer.serialize(value, generator, provider);

        // Assert
        verify(generator).writeNumber(value);
    }

    @Test
    public void testSerializeByte() throws IOException {
        // Arrange
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        Byte value = 1;

        // Act
        numberSerializer.serialize(value, generator, provider);

        // Assert
        verify(generator).writeNumber(value);
    }

    @Test
    public void testSerializeShort() throws IOException {
        // Arrange
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        Short value = 12;

        // Act
        numberSerializer.serialize(value, generator, provider);

        // Assert
        verify(generator).writeNumber(value);
    }

    @Test
    public void testSerializeUnknownNumberType() throws IOException {
        // Arrange
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        Number value = Mockito.mock(Number.class);
        String expectedValue = value.toString();

        // Act
        numberSerializer.serialize(value, generator, provider);

        // Assert
        verify(generator).writeNumber(expectedValue);
    }

    @Test
    public void testCreateSchemaNodeForInteger() {
        // Arrange
        SerializerProvider provider = mock(SerializerProvider.class);
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        ArgumentCaptor<JsonNode> argumentCaptor = ArgumentCaptor.forClass(JsonNode.class);

        // Act
        JsonNode schemaNode = numberSerializer.getSchema(provider, BigInteger.class);

        // Assert
        verify(numberSerializer).createSchemaNode("integer", true);
        verify(schemaNode).isValueNode();
        verify(schemaNode).isNull();
        verify(schemaNode).isObject();
        verify(schemaNode).isTextual();
        verify(schemaNode).isArray();
        verify(schemaNode).isBinary();
        verify(schemaNode).isBoolean();
        verify(schemaNode).isContainerNode();
        verify(schemaNode).isFloatingPointNumber();
        verify(schemaNode).isIntegralNumber();
        verify(schemaNode).isNumber();
        verify(schemaNode).isPOJO();
        verify(schemaNode).isValueNode();
        verify(schemaNode).isWrapped();
    }

    @Test
    public void testCreateSchemaNodeForNumber() {
        // Arrange
        SerializerProvider provider = mock(SerializerProvider.class);
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        ArgumentCaptor<JsonNode> argumentCaptor = ArgumentCaptor.forClass(JsonNode.class);

        // Act
        JsonNode schemaNode = numberSerializer.getSchema(provider, Long.class);

        // Assert
        verify(numberSerializer).createSchemaNode("number", true);
        verify(schemaNode).isValueNode();
        verify(schemaNode).isNull();
        verify(schemaNode).isObject();
        verify(schemaNode).isTextual();
        verify(schemaNode).isArray();
        verify(schemaNode).isBinary();
        verify(schemaNode).isBoolean();
        verify(schemaNode).isContainerNode();
        verify(schemaNode).isFloatingPointNumber();
        verify(schemaNode).isIntegralNumber();
        verify(schemaNode).isNumber();
        verify(schemaNode).isPOJO();
        verify(schemaNode).isValueNode();
        verify(schemaNode).isWrapped();
    }
}
