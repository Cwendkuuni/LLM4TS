import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.Assert.assertEquals;

public class NumberSerializerTest {
    
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }
    
    @Test
    public void testSerializeBigDecimal() throws IOException {
        // Arrange
        NumberSerializer numberSerializer = NumberSerializer.instance;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(outputStream);

        // Act
        numberSerializer.serialize(new BigDecimal("123.45"), jsonGenerator, null);
        jsonGenerator.flush();
        String json = outputStream.toString();

        // Assert
        assertEquals("123.45", json);
    }
    
    @Test
    public void testSerializeBigInteger() throws IOException {
        // Arrange
        NumberSerializer numberSerializer = NumberSerializer.instance;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(outputStream);

        // Act
        numberSerializer.serialize(new BigInteger("10000"), jsonGenerator, null);
        jsonGenerator.flush();
        String json = outputStream.toString();

        // Assert
        assertEquals("10000", json);
    }
    
    @Test
    public void testSerializeLong() throws IOException {
        // Arrange
        NumberSerializer numberSerializer = NumberSerializer.instance;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(outputStream);

        // Act
        numberSerializer.serialize(100L, jsonGenerator, null);
        jsonGenerator.flush();
        String json = outputStream.toString();

        // Assert
        assertEquals("100", json);
    }
    
    @Test
    public void testSerializeDouble() throws IOException {
        // Arrange
        NumberSerializer numberSerializer = NumberSerializer.instance;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(outputStream);

        // Act
        numberSerializer.serialize(10.5, jsonGenerator, null);
        jsonGenerator.flush();
        String json = outputStream.toString();

        // Assert
        assertEquals("10.5", json);
    }
    
    @Test
    public void testSerializeFloat() throws IOException {
        // Arrange
        NumberSerializer numberSerializer = NumberSerializer.instance;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(outputStream);

        // Act
        numberSerializer.serialize(5.5f, jsonGenerator, null);
        jsonGenerator.flush();
        String json = outputStream.toString();

        // Assert
        assertEquals("5.5", json);
    }
    
    @Test
    public void testSerializeInteger() throws IOException {
        // Arrange
        NumberSerializer numberSerializer = NumberSerializer.instance;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(outputStream);

        // Act
        numberSerializer.serialize(100, jsonGenerator, null);
        jsonGenerator.flush();
        String json = outputStream.toString();

        // Assert
        assertEquals("100", json);
    }
    
    @Test
    public void testSerializeByte() throws IOException {
        // Arrange
        NumberSerializer numberSerializer = NumberSerializer.instance;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(outputStream);

        // Act
        numberSerializer.serialize(new Byte("7"), jsonGenerator, null);
        jsonGenerator.flush();
        String json = outputStream.toString();

        // Assert
        assertEquals("7", json);
    }
    
    @Test
    public void testSerializeShort() throws IOException {
        // Arrange
        NumberSerializer numberSerializer = NumberSerializer.instance;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(outputStream);

        // Act
        numberSerializer.serialize((short) 42, jsonGenerator, null);
        jsonGenerator.flush();
        String json = outputStream.toString();

        // Assert
        assertEquals("42", json);
    }
    
    @Test
    public void testSerializeUnknownNumberType() throws IOException {
        // Arrange
        NumberSerializer numberSerializer = NumberSerializer.instance;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(outputStream);

        // Act
        numberSerializer.serialize(1234567890.123, jsonGenerator, null);
        jsonGenerator.flush();
        String json = outputStream.toString();

        // Assert
        assertEquals("1234567890.123", json);
    }
    
    @Test
    public void testGetSchemaForBigInteger() throws IOException {
        // Arrange
        NumberSerializer numberSerializer = NumberSerializer.instance;

        // Act
        JsonNode schema = numberSerializer.getSchema(null, BigInteger.class);

        // Assert
        assertEquals("integer", schema.get("type").asText());
    }
    
    @Test
    public void testGetSchemaForLong() throws IOException {
        // Arrange
        NumberSerializer numberSerializer = NumberSerializer.instance;

        // Act
        JsonNode schema = numberSerializer.getSchema(null, Long.class);

        // Assert
        assertEquals("integer", schema.get("type").asText());
    }
    
    @Test
    public void testGetSchemaForDouble() throws IOException {
        // Arrange
        NumberSerializer numberSerializer = NumberSerializer.instance;

        // Act
        JsonNode schema = numberSerializer.getSchema(null, Double.class);

        // Assert
        assertEquals("number", schema.get("type").asText());
    }
    
    @Test
    public void testAcceptJsonFormatVisitorForBigInteger() throws IOException {
        // Arrange
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = TypeFactory.defaultInstance().constructType(BigInteger.class);

        // Act
        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);

        // Assert
        Mockito.verify(visitor).expectIntegerFormat(typeHint);
    }
    
    @Test
    public void testAcceptJsonFormatVisitorForBigDecimal() throws IOException {
        // Arrange
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = TypeFactory.defaultInstance().constructType(BigDecimal.class);

        // Act
        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);

        // Assert
        Mockito.verify(visitor).expectFloatFormat(typeHint);
    }
    
    @Test
    public void testAcceptJsonFormatVisitorForUnknownNumberType() throws IOException {
        // Arrange
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = TypeFactory.defaultInstance().constructType(Number.class);

        // Act
        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);

        // Assert
        Mockito.verify(visitor).expectNumberFormat(typeHint);
    }
    
}