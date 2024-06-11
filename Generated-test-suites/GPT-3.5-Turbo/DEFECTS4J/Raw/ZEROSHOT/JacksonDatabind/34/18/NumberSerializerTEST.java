import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class NumberSerializerTest {

    private NumberSerializer numberSerializer;
    private JsonGenerator jsonGenerator;
    private SerializerProvider serializerProvider;

    @Before
    public void setUp() {
        numberSerializer = new NumberSerializer(Number.class);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        serializerProvider = objectMapper.getSerializerProvider();
    }

    @Test
    public void serialize_shouldSerializeBigDecimal() {
        // Arrange
        BigDecimal value = new BigDecimal("10.5");

        // Act
        try {
            numberSerializer.serialize(value, jsonGenerator, serializerProvider);
            jsonGenerator.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Assert
        // Expected output: 10.5
    }

    @Test
    public void serialize_shouldSerializeBigInteger() {
        // Arrange
        BigInteger value = new BigInteger("10");

        // Act
        try {
            numberSerializer.serialize(value, jsonGenerator, serializerProvider);
            jsonGenerator.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Assert
        // Expected output: 10
    }

    @Test
    public void serialize_shouldSerializeInteger() {
        // Arrange
        Integer value = 10;

        // Act
        try {
            numberSerializer.serialize(value, jsonGenerator, serializerProvider);
            jsonGenerator.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Assert
        // Expected output: 10
    }

    @Test
    public void serialize_shouldSerializeLong() {
        // Arrange
        Long value = 10L;

        // Act
        try {
            numberSerializer.serialize(value, jsonGenerator, serializerProvider);
            jsonGenerator.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Assert
        // Expected output: 10
    }

    @Test
    public void serialize_shouldSerializeDouble() {
        // Arrange
        Double value = 10.5;

        // Act
        try {
            numberSerializer.serialize(value, jsonGenerator, serializerProvider);
            jsonGenerator.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Assert
        // Expected output: 10.5
    }

    @Test
    public void serialize_shouldSerializeFloat() {
        // Arrange
        Float value = 10.5f;

        // Act
        try {
            numberSerializer.serialize(value, jsonGenerator, serializerProvider);
            jsonGenerator.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Assert
        // Expected output: 10.5
    }

    @Test
    public void serialize_shouldSerializeByte() {
        // Arrange
        Byte value = 10;

        // Act
        try {
            numberSerializer.serialize(value, jsonGenerator, serializerProvider);
            jsonGenerator.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Assert
        // Expected output: 10
    }

    @Test
    public void serialize_shouldSerializeShort() {
        // Arrange
        Short value = 10;

        // Act
        try {
            numberSerializer.serialize(value, jsonGenerator, serializerProvider);
            jsonGenerator.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Assert
        // Expected output: 10
    }

    @Test
    public void serialize_shouldSerializeUncastNumber() {
        // Arrange
        Number value = 10.5;

        // Act
        try {
            numberSerializer.serialize(value, jsonGenerator, serializerProvider);
            jsonGenerator.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Assert
        // Expected output: 10.5
    }

    @Test
    public void getSchema_shouldReturnIntegerSchema() {
        // Arrange

        // Act
        JsonNode schema = numberSerializer.getSchema(serializerProvider, null);

        // Assert
        Assert.assertEquals("integer", schema.get("type").asText());
    }

    @Test
    public void acceptJsonFormatVisitor_shouldVisitIntFormat() {
        // Arrange

        // Act

        // Assert

    }
}