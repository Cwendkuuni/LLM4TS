import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.Assert.assertEquals;

public class NumberSerializerTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testSerialize_WhenNumberIsBigDecimal_ShouldWriteNumberAsBigDecimal() throws IOException {
        // Arrange
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JsonGenerator generator = mapper.getFactory().createGenerator(outputStream);

        // Act
        serializer.serialize(new BigDecimal("100.50"), generator, null);
        generator.close();

        // Assert
        assertEquals("\"100.50\"", outputStream.toString());
    }

    @Test
    public void testSerialize_WhenNumberIsBigInteger_ShouldWriteNumberAsBigInteger() throws IOException {
        // Arrange
        NumberSerializer serializer = new NumberSerializer(BigInteger.class);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JsonGenerator generator = mapper.getFactory().createGenerator(outputStream);

        // Act
        serializer.serialize(new BigInteger("100"), generator, null);
        generator.close();

        // Assert
        assertEquals("100", outputStream.toString());
    }

    @Test
    public void testSerialize_WhenNumberIsLong_ShouldWriteNumberAsLong() throws IOException {
        // Arrange
        NumberSerializer serializer = new NumberSerializer(Long.class);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JsonGenerator generator = mapper.getFactory().createGenerator(outputStream);

        // Act
        serializer.serialize(100L, generator, null);
        generator.close();

        // Assert
        assertEquals("100", outputStream.toString());
    }

    @Test
    public void testSerialize_WhenNumberIsDouble_ShouldWriteNumberAsDouble() throws IOException {
        // Arrange
        NumberSerializer serializer = new NumberSerializer(Double.class);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JsonGenerator generator = mapper.getFactory().createGenerator(outputStream);

        // Act
        serializer.serialize(100.50, generator, null);
        generator.close();

        // Assert
        assertEquals("100.5", outputStream.toString());
    }

    @Test
    public void testSerialize_WhenNumberIsFloat_ShouldWriteNumberAsFloat() throws IOException {
        // Arrange
        NumberSerializer serializer = new NumberSerializer(Float.class);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JsonGenerator generator = mapper.getFactory().createGenerator(outputStream);

        // Act
        serializer.serialize(100.50f, generator, null);
        generator.close();

        // Assert
        assertEquals("100.5", outputStream.toString());
    }

    @Test
    public void testSerialize_WhenNumberIsInteger_ShouldWriteNumberAsInteger() throws IOException {
        // Arrange
        NumberSerializer serializer = new NumberSerializer(Integer.class);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JsonGenerator generator = mapper.getFactory().createGenerator(outputStream);

        // Act
        serializer.serialize(100, generator, null);
        generator.close();

        // Assert
        assertEquals("100", outputStream.toString());
    }

    @Test
    public void testSerialize_WhenNumberIsByte_ShouldWriteNumberAsByte() throws IOException {
        // Arrange
        NumberSerializer serializer = new NumberSerializer(Byte.class);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JsonGenerator generator = mapper.getFactory().createGenerator(outputStream);

        // Act
        serializer.serialize((byte) 100, generator, null);
        generator.close();

        // Assert
        assertEquals("100", outputStream.toString());
    }

    @Test
    public void testSerialize_WhenNumberIsShort_ShouldWriteNumberAsShort() throws IOException {
        // Arrange
        NumberSerializer serializer = new NumberSerializer(Short.class);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JsonGenerator generator = mapper.getFactory().createGenerator(outputStream);

        // Act
        serializer.serialize((short) 100, generator, null);
        generator.close();

        // Assert
        assertEquals("100", outputStream.toString());
    }

    @Test
    public void testSerialize_WhenNumberIsRandomClass_ShouldWriteNumberAsString() throws IOException {
        // Arrange
        NumberSerializer serializer = new NumberSerializer(Number.class);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JsonGenerator generator = mapper.getFactory().createGenerator(outputStream);

        // Act
        serializer.serialize(new CustomNumber(), generator, null);
        generator.close();

        // Assert
        assertEquals("\"100\"", outputStream.toString());
    }

    private class CustomNumber extends Number {
        @Override
        public int intValue() {
            return 100;
        }

        @Override
        public long longValue() {
            return 100;
        }

        @Override
        public float floatValue() {
            return 100.0f;
        }

        @Override
        public double doubleValue() {
            return 100.0;
        }
    }

    @Test
    public void testCreateSchemaNode_WhenNumberIsBigInteger_ShouldReturnIntegerSchemaNode() {
        // Arrange
        NumberSerializer serializer = new NumberSerializer(BigInteger.class);

        // Act
        JsonNode schemaNode = serializer.getSchema(null, null);

        // Assert
        assertEquals("integer", schemaNode.get("type").asText());
        assertEquals(true, schemaNode.get("isInt").asBoolean());
    }

    @Test
    public void testCreateSchemaNode_WhenNumberIsNotBigInteger_ShouldReturnNumberSchemaNode() {
        // Arrange
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);

        // Act
        JsonNode schemaNode = serializer.getSchema(null, null);

        // Assert
        assertEquals("number", schemaNode.get("type").asText());
        assertEquals(false, schemaNode.get("isInt").asBoolean());
    }

}