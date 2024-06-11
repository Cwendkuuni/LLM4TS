
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;

public class NumberSerializerTest {
    
    private NumberSerializer serializer;
    private SerializerProvider provider;
    private StringWriter writer;
    private JsonGenerator generator;
    
    @Before
    public void setUp() throws IOException {
        serializer = new NumberSerializer(Number.class);
        provider = new ObjectMapper().getSerializerProvider();
        writer = new StringWriter();
        generator = new ObjectMapper().getFactory().createGenerator(writer);
    }
    
    @Test
    public void testSerialize_withBigDecimal_shouldWriteNumber() throws IOException {
        // Arrange
        BigDecimal value = new BigDecimal("123.45");
        
        // Act
        serializer.serialize(value, generator, provider);
        generator.flush();
        String result = writer.toString();
        
        // Assert
        Assert.assertEquals(value.toString(), result);
    }
    
    @Test
    public void testSerialize_withBigInteger_shouldWriteNumber() throws IOException {
        // Arrange
        BigInteger value = new BigInteger("1234567890");
        
        // Act
        serializer.serialize(value, generator, provider);
        generator.flush();
        String result = writer.toString();
        
        // Assert
        Assert.assertEquals(value.toString(), result);
    }
    
    @Test
    public void testSerialize_withLong_shouldWriteNumber() throws IOException {
        // Arrange
        Long value = 123456789L;
        
        // Act
        serializer.serialize(value, generator, provider);
        generator.flush();
        String result = writer.toString();
        
        // Assert
        Assert.assertEquals(value.toString(), result);
    }
    
    @Test
    public void testSerialize_withDouble_shouldWriteNumber() throws IOException {
        // Arrange
        Double value = 123.45;
        
        // Act
        serializer.serialize(value, generator, provider);
        generator.flush();
        String result = writer.toString();
        
        // Assert
        Assert.assertEquals(value.toString(), result);
    }
    
    @Test
    public void testSerialize_withFloat_shouldWriteNumber() throws IOException {
        // Arrange
        Float value = 123.45f;
        
        // Act
        serializer.serialize(value, generator, provider);
        generator.flush();
        String result = writer.toString();
        
        // Assert
        Assert.assertEquals(value.toString(), result);
    }
    
    @Test
    public void testSerialize_withInteger_shouldWriteNumber() throws IOException {
        // Arrange
        Integer value = 123;
        
        // Act
        serializer.serialize(value, generator, provider);
        generator.flush();
        String result = writer.toString();
        
        // Assert
        Assert.assertEquals(value.toString(), result);
    }
    
    @Test
    public void testSerialize_withByte_shouldWriteNumber() throws IOException {
        // Arrange
        Byte value = 123;
        
        // Act
        serializer.serialize(value, generator, provider);
        generator.flush();
        String result = writer.toString();
        
        // Assert
        Assert.assertEquals(value.toString(), result);
    }
    
    @Test
    public void testSerialize_withShort_shouldWriteNumber() throws IOException {
        // Arrange
        Short value = 123;
        
        // Act
        serializer.serialize(value, generator, provider);
        generator.flush();
        String result = writer.toString();
        
        // Assert
        Assert.assertEquals(value.toString(), result);
    }
    
    @Test
    public void testSerialize_withOtherNumber_shouldWriteString() throws IOException {
        // Arrange
        Number value = new CustomNumber(123);
        
        // Act
        serializer.serialize(value, generator, provider);
        generator.flush();
        String result = writer.toString();
        
        // Assert
        Assert.assertEquals(value.toString(), result);
    }
    
    @Test
    public void testGetSchema_withInteger_shouldReturnIntegerSchemaNode() {
        // Act
        JsonNode schema = serializer.getSchema(provider, Integer.class);
        
        // Assert
        Assert.assertEquals("integer", schema.get("type").textValue());
    }
    
    @Test
    public void testGetSchema_withBigDecimal_shouldReturnNumberSchemaNode() {
        // Arrange
        serializer = new NumberSerializer(BigDecimal.class);
        
        // Act
        JsonNode schema = serializer.getSchema(provider, BigDecimal.class);
        
        // Assert
        Assert.assertEquals("number", schema.get("type").textValue());
    }
    
    @Test
    public void testAcceptJsonFormatVisitor_withBigInteger_shouldVisitIntFormat() throws IOException {
        // Arrange
        JsonFormatVisitorWrapper visitor = new ObjectMapper().getSerializerProvider().visitorWrapper();
        
        // Act
        serializer.acceptJsonFormatVisitor(visitor, BigInteger.class);
        
        // Assert
        String expected = "integer";
        Assert.assertEquals(expected, visitor.getProvider().getJsonValueDeserializer().handledType().getName());
    }
    
    private static class CustomNumber extends Number {
        
        private final int value;
        
        public CustomNumber(int value) {
            this.value = value;
        }
        
        @Override
        public int intValue() {
            return value;
        }
        
        @Override
        public long longValue() {
            return value;
        }
        
        @Override
        public float floatValue() {
            return value;
        }
        
        @Override
        public double doubleValue() {
            return value;
        }
        
        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }
}
