import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import static org.junit.Assert.assertEquals;

public class NumberSerializerTest {
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper().enable(SerializationFeature.WRITE_ENUMS_USING_INDEX);
    }

    @Test
    public void testSerializeBigDecimal() throws IOException {
        // Arrange
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);

        // Act
        serializer.serialize(new BigDecimal("1234567890.1234567890"), generator, null);
        generator.flush();
        String jsonOutput = writer.toString();

        // Assert
        assertEquals("1234567890.1234567890", jsonOutput);
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        // Arrange
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        NumberSerializer serializer = new NumberSerializer(BigInteger.class);

        // Act
        serializer.serialize(new BigInteger("12345678901234567890"), generator, null);
        generator.flush();
        String jsonOutput = writer.toString();

        // Assert
        assertEquals("12345678901234567890", jsonOutput);
    }

    @Test
    public void testSerializeInteger() throws IOException {
        // Arrange
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        NumberSerializer serializer = new NumberSerializer(Integer.class);

        // Act
        serializer.serialize(123, generator, null);
        generator.flush();
        String jsonOutput = writer.toString();

        // Assert
        assertEquals("123", jsonOutput);
    }

    @Test
    public void testSerializeLong() throws IOException {
        // Arrange
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        NumberSerializer serializer = new NumberSerializer(Long.class);

        // Act
        serializer.serialize(1234567890L, generator, null);
        generator.flush();
        String jsonOutput = writer.toString();

        // Assert
        assertEquals("1234567890", jsonOutput);
    }

    @Test
    public void testSerializeDouble() throws IOException {
        // Arrange
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        NumberSerializer serializer = new NumberSerializer(Double.class);

        // Act
        serializer.serialize(123.456, generator, null);
        generator.flush();
        String jsonOutput = writer.toString();

        // Assert
        assertEquals("123.456", jsonOutput);
    }

    @Test
    public void testSerializeFloat() throws IOException {
        // Arrange
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        NumberSerializer serializer = new NumberSerializer(Float.class);

        // Act
        serializer.serialize(123.456f, generator, null);
        generator.flush();
        String jsonOutput = writer.toString();

        // Assert
        assertEquals("123.456", jsonOutput);
    }

    @Test
    public void testSerializeByte() throws IOException {
        // Arrange
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        NumberSerializer serializer = new NumberSerializer(Byte.class);

        // Act
        serializer.serialize((byte) 123, generator, null);
        generator.flush();
        String jsonOutput = writer.toString();

        // Assert
        assertEquals("123", jsonOutput);
    }

    @Test
    public void testSerializeShort() throws IOException {
        // Arrange
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        NumberSerializer serializer = new NumberSerializer(Short.class);

        // Act
        serializer.serialize((short) 123, generator, null);
        generator.flush();
        String jsonOutput = writer.toString();

        // Assert
        assertEquals("123", jsonOutput);
    }

    @Test
    public void testSerializeUnknownNumberType() throws IOException {
        // Arrange
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        NumberSerializer serializer = new NumberSerializer(Number.class);

        // Act
        serializer.serialize(1234567890, generator, null);
        generator.flush();
        String jsonOutput = writer.toString();

        // Assert
        assertEquals("1234567890", jsonOutput);
    }

    @Test(expected = JsonParseException.class)
    public void testSerializeIOException() throws IOException {
        // Arrange
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        NumberSerializer serializer = new NumberSerializer(Number.class);

        // Act
        serializer.serialize(null, generator, null);
    }
}