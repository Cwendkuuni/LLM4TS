
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializerTest {
    
    @Test
    public void testSerialize_BigDecimal() {
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        BigDecimal value = new BigDecimal("123.45");
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonGenerator(writer);
        SerializerProvider provider = new SerializerProvider();
        
        try {
            serializer.serialize(value, generator, provider);
            generator.flush();
            String json = writer.toString();
            assertEquals("123.45", json);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
    
    @Test
    public void testSerialize_BigInteger() {
        NumberSerializer serializer = new NumberSerializer(BigInteger.class);
        BigInteger value = new BigInteger("1234567890");
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonGenerator(writer);
        SerializerProvider provider = new SerializerProvider();
        
        try {
            serializer.serialize(value, generator, provider);
            generator.flush();
            String json = writer.toString();
            assertEquals("1234567890", json);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
    
    @Test
    public void testSerialize_Long() {
        NumberSerializer serializer = new NumberSerializer(Long.class);
        Long value = 12345L;
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonGenerator(writer);
        SerializerProvider provider = new SerializerProvider();
        
        try {
            serializer.serialize(value, generator, provider);
            generator.flush();
            String json = writer.toString();
            assertEquals("12345", json);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
    
    @Test
    public void testSerialize_Double() {
        NumberSerializer serializer = new NumberSerializer(Double.class);
        Double value = 123.45;
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonGenerator(writer);
        SerializerProvider provider = new SerializerProvider();
        
        try {
            serializer.serialize(value, generator, provider);
            generator.flush();
            String json = writer.toString();
            assertEquals("123.45", json);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
    
    @Test
    public void testSerialize_Float() {
        NumberSerializer serializer = new NumberSerializer(Float.class);
        Float value = 123.45f;
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonGenerator(writer);
        SerializerProvider provider = new SerializerProvider();
        
        try {
            serializer.serialize(value, generator, provider);
            generator.flush();
            String json = writer.toString();
            assertEquals("123.45", json);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
    
    @Test
    public void testSerialize_Integer() {
        NumberSerializer serializer = new NumberSerializer(Integer.class);
        Integer value = 12345;
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonGenerator(writer);
        SerializerProvider provider = new SerializerProvider();
        
        try {
            serializer.serialize(value, generator, provider);
            generator.flush();
            String json = writer.toString();
            assertEquals("12345", json);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
    
    @Test
    public void testSerialize_Byte() {
        NumberSerializer serializer = new NumberSerializer(Byte.class);
        Byte value = 123;
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonGenerator(writer);
        SerializerProvider provider = new SerializerProvider();
        
        try {
            serializer.serialize(value, generator, provider);
            generator.flush();
            String json = writer.toString();
            assertEquals("123", json);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
    
    @Test
    public void testSerialize_Short() {
        NumberSerializer serializer = new NumberSerializer(Short.class);
        Short value = 12345;
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonGenerator(writer);
        SerializerProvider provider = new SerializerProvider();
        
        try {
            serializer.serialize(value, generator, provider);
            generator.flush();
            String json = writer.toString();
            assertEquals("12345", json);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
    
    @Test
    public void testSerialize_OtherNumber() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        Number value = new AtomicLong(12345);
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonGenerator(writer);
        SerializerProvider provider = new SerializerProvider();
        
        try {
            serializer.serialize(value, generator, provider);
            generator.flush();
            String json = writer.toString();
            assertEquals("12345", json);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
    
    @Test
    public void testGetSchema() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        SerializerProvider provider = new SerializerProvider();
        Type typeHint = Number.class;
        
        JsonNode schema = serializer.getSchema(provider, typeHint);
        
        assertEquals("number", schema.get("type").asText());
        assertTrue(schema.get("required").asBoolean());
    }
    
    @Test
    public void testAcceptJsonFormatVisitor() {
        NumberSerializer serializer1 = new NumberSerializer(BigInteger.class);
        NumberSerializer serializer2 = new NumberSerializer(BigDecimal.class);
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper();
        JavaType typeHint = JavaType.fromClass(BigInteger.class);
        
        try {
            serializer1.acceptJsonFormatVisitor(visitor, typeHint);
        } catch (JsonMappingException e) {
            fail("JsonMappingException should not be thrown");
        }
        
        typeHint = JavaType.fromClass(BigDecimal.class);
        
        try {
            serializer2.acceptJsonFormatVisitor(visitor, typeHint);
        } catch (JsonMappingException e) {
            fail("JsonMappingException should not be thrown");
        }
    }
}
