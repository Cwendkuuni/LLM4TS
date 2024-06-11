
import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

public class NumberSerializerTest {
    
    private NumberSerializer numberSerializer = new NumberSerializer(Number.class);
    private ObjectMapper mapper = new ObjectMapper();
    
    private JsonNode serializeAndDeserialize(Number value) throws IOException {
        // Serialize the value
        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator generator = jsonFactory.createGenerator(System.out);
        numberSerializer.serialize(value, generator, null);
        generator.close();
        
        // Deserialize the value
        JsonParser parser = jsonFactory.createParser(System.out);
        JsonNode jsonNode = mapper.readTree(parser);
        
        return jsonNode;
    }
    
    @Test
    public void testSerialize_Integer() throws IOException {
        JsonNode jsonNode = serializeAndDeserialize(42);
        
        assertTrue(jsonNode.isInt());
        assertEquals(42, jsonNode.intValue());
    }
    
    @Test
    public void testSerialize_Long() throws IOException {
        JsonNode jsonNode = serializeAndDeserialize(123456789L);
        
        assertTrue(jsonNode.isLong());
        assertEquals(123456789L, jsonNode.longValue());
    }
    
    @Test
    public void testSerialize_Double() throws IOException {
        JsonNode jsonNode = serializeAndDeserialize(3.14);
        
        assertTrue(jsonNode.isDouble());
        assertEquals(3.14, jsonNode.doubleValue(), 0.0001);
    }
    
    @Test
    public void testSerialize_Float() throws IOException {
        JsonNode jsonNode = serializeAndDeserialize(2.718f);
        
        assertTrue(jsonNode.isFloat());
        assertEquals(2.718f, jsonNode.floatValue(), 0.0001);
    }
    
    @Test
    public void testSerialize_Byte() throws IOException {
        JsonNode jsonNode = serializeAndDeserialize((byte) 255);
        
        assertTrue(jsonNode.isInt());
        assertEquals(255, jsonNode.intValue());
    }
    
    @Test
    public void testSerialize_Short() throws IOException {
        JsonNode jsonNode = serializeAndDeserialize((short) 32767);
        
        assertTrue(jsonNode.isInt());
        assertEquals(32767, jsonNode.intValue());
    }
    
    @Test
    public void testSerialize_BigDecimal() throws IOException {
        BigDecimal bd = new BigDecimal("123.456");
        JsonNode jsonNode = serializeAndDeserialize(bd);
        
        assertTrue(jsonNode.isBigDecimal());
        assertEquals(bd, jsonNode.decimalValue());
    }
    
    @Test
    public void testSerialize_BigInteger() throws IOException {
        BigInteger bi = new BigInteger("123456789");
        JsonNode jsonNode = serializeAndDeserialize(bi);
        
        assertTrue(jsonNode.isBigInteger());
        assertEquals(bi, jsonNode.bigIntegerValue());
    }
    
    @Test
    public void testSerialize_UnsupportedType() throws IOException {
        // Custom number type not supported, fallback to untyped serialization
        Number customNumber = new CustomNumber();
        JsonNode jsonNode = serializeAndDeserialize(customNumber);
        
        assertTrue(jsonNode.isTextual());
        assertEquals(customNumber.toString(), jsonNode.textValue());
    }
    
    @Test
    public void testGetSchema_Integer() {
        SerializerProvider provider = null;
        JsonNode schema = numberSerializer.getSchema(provider, Integer.class);
        
        assertTrue(schema.isTextual());
        assertEquals("integer", schema.textValue());
    }
    
    @Test
    public void testGetSchema_Long() {
        SerializerProvider provider = null;
        JsonNode schema = numberSerializer.getSchema(provider, Long.class);
        
        assertTrue(schema.isTextual());
        assertEquals("number", schema.textValue());
    }
    
    @Test
    public void testGetSchema_BigDecimal() {
        SerializerProvider provider = null;
        JsonNode schema = numberSerializer.getSchema(provider, BigDecimal.class);
        
        assertTrue(schema.isTextual());
        assertEquals("number", schema.textValue());
    }
    
    @Test
    public void testAcceptJsonFormatVisitor_Integer() throws IOException {
        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;
        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        // Expecting int format visitor with BIG_INTEGER number type
    }
    
    @Test
    public void testAcceptJsonFormatVisitor_BigDecimal() throws IOException {
        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;
        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        // Expecting float format visitor with BIG_INTEGER number type
    }
    
    private static class CustomNumber extends Number {
        @Override
        public int intValue() {
            return 0;
        }
        
        @Override
        public long longValue() {
            return 0;
        }
        
        @Override
        public float floatValue() {
            return 0;
        }
        
        @Override
        public double doubleValue() {
            return 0;
        }
    }
}
