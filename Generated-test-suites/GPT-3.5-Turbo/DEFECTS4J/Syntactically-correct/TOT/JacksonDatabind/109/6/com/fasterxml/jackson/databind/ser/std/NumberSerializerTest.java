
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class NumberSerializerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    @Test
    public void testSerializeBigDecimal() throws IOException {
        // Arrange
        BigDecimal input = BigDecimal.valueOf(10.5);
        NumberSerializer serializer = NumberSerializer.instance;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JsonGenerator generator = new JsonFactory().createGenerator(baos);

        // Act
        serializer.serialize(input, generator, null);
        generator.flush();

        // Assert
        assertEquals("10.5", new String(baos.toByteArray(), StandardCharsets.UTF_8));
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        // Arrange
        BigInteger input = BigInteger.valueOf(20);
        NumberSerializer serializer = NumberSerializer.instance;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JsonGenerator generator = new JsonFactory().createGenerator(baos);

        // Act
        serializer.serialize(input, generator, null);
        generator.flush();

        // Assert
        assertEquals("20", new String(baos.toByteArray(), StandardCharsets.UTF_8));
    }
    
    @Test
    public void testSerializeLong() throws IOException {
        // Arrange
        Long input = 30L;
        NumberSerializer serializer = NumberSerializer.instance;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JsonGenerator generator = new JsonFactory().createGenerator(baos);

        // Act
        serializer.serialize(input, generator, null);
        generator.flush();

        // Assert
        assertEquals("30", new String(baos.toByteArray(), StandardCharsets.UTF_8));
    }
    
    @Test
    public void testSerializeDouble() throws IOException {
        // Arrange
        Double input = 40.5;
        NumberSerializer serializer = NumberSerializer.instance;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JsonGenerator generator = new JsonFactory().createGenerator(baos);

        // Act
        serializer.serialize(input, generator, null);
        generator.flush();

        // Assert
        assertEquals("40.5", new String(baos.toByteArray(), StandardCharsets.UTF_8));
    }
    
    @Test
    public void testSerializeFloat() throws IOException {
        // Arrange
        Float input = 50.0f;
        NumberSerializer serializer = NumberSerializer.instance;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JsonGenerator generator = new JsonFactory().createGenerator(baos);

        // Act
        serializer.serialize(input, generator, null);
        generator.flush();

        // Assert
        assertEquals("50.0", new String(baos.toByteArray(), StandardCharsets.UTF_8));
    }
    
    @Test
    public void testSerializeInteger() throws IOException {
        // Arrange
        Integer input = 60;
        NumberSerializer serializer = NumberSerializer.instance;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JsonGenerator generator = new JsonFactory().createGenerator(baos);

        // Act
        serializer.serialize(input, generator, null);
        generator.flush();

        // Assert
        assertEquals("60", new String(baos.toByteArray(), StandardCharsets.UTF_8));
    }
    
    @Test
    public void testSerializeByte() throws IOException {
        // Arrange
        Byte input = 70;
        NumberSerializer serializer = NumberSerializer.instance;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JsonGenerator generator = new JsonFactory().createGenerator(baos);

        // Act
        serializer.serialize(input, generator, null);
        generator.flush();

        // Assert
        assertEquals("70", new String(baos.toByteArray(), StandardCharsets.UTF_8));
    }
    
    @Test
    public void testSerializeShort() throws IOException {
        // Arrange
        Short input = 80;
        NumberSerializer serializer = NumberSerializer.instance;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JsonGenerator generator = new JsonFactory().createGenerator(baos);

        // Act
        serializer.serialize(input, generator, null);
        generator.flush();

        // Assert
        assertEquals("80", new String(baos.toByteArray(), StandardCharsets.UTF_8));
    }
    
    @Test
    public void testFallbackSerialize() throws IOException {
        // Arrange
        Number input = new TestNumber();
        NumberSerializer serializer = NumberSerializer.instance;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JsonGenerator generator = new JsonFactory().createGenerator(baos);

        // Act
        serializer.serialize(input, generator, null);
        generator.flush();

        // Assert
        assertEquals(input.toString(), new String(baos.toByteArray(), StandardCharsets.UTF_8));
    }

    private static class TestNumber extends Number {
        private static final long serialVersionUID = 1L;

        @Override
        public int intValue() {
            return 90;
        }

        @Override
        public long longValue() {
            return 90L;
        }

        @Override
        public float floatValue() {
            return 90.5f;
        }

        @Override
        public double doubleValue() {
            return 90.5;
        }
    }
}

