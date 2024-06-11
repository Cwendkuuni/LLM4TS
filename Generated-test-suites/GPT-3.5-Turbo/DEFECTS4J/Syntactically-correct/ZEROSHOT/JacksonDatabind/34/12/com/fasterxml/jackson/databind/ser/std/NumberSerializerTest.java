import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

public class NumberSerializerTest {
    
    @Test
    public void testSerializeBigDecimal() throws Exception {
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        BigDecimal value = new BigDecimal("123.45");
        
        serializer.serialize(value, generator, provider);
        
        Mockito.verify(generator).writeNumber(value);
    }
    
    @Test
    public void testSerializeBigInteger() throws Exception {
        NumberSerializer serializer = new NumberSerializer(BigInteger.class);
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        BigInteger value = new BigInteger("1234567890");
        
        serializer.serialize(value, generator, provider);
        
        Mockito.verify(generator).writeNumber(value);
    }
    
    @Test
    public void testSerializeInteger() throws Exception {
        NumberSerializer serializer = new NumberSerializer(Integer.class);
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        Integer value = 123;
        
        serializer.serialize(value, generator, provider);
        
        Mockito.verify(generator).writeNumber(value.intValue());
    }
    
    @Test
    public void testSerializeLong() throws Exception {
        NumberSerializer serializer = new NumberSerializer(Long.class);
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        Long value = 123L;
        
        serializer.serialize(value, generator, provider);
        
        Mockito.verify(generator).writeNumber(value.longValue());
    }
    
    @Test
    public void testSerializeDouble() throws Exception {
        NumberSerializer serializer = new NumberSerializer(Double.class);
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        Double value = 123.45;
        
        serializer.serialize(value, generator, provider);
        
        Mockito.verify(generator).writeNumber(value.doubleValue());
    }
    
    @Test
    public void testSerializeFloat() throws Exception {
        NumberSerializer serializer = new NumberSerializer(Float.class);
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        Float value = 123.45f;
        
        serializer.serialize(value, generator, provider);
        
        Mockito.verify(generator).writeNumber(value.floatValue());
    }
    
    @Test
    public void testSerializeByte() throws Exception {
        NumberSerializer serializer = new NumberSerializer(Byte.class);
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        Byte value = 123;
        
        serializer.serialize(value, generator, provider);
        
        Mockito.verify(generator).writeNumber(value.intValue());
    }
    
    @Test
    public void testSerializeShort() throws Exception {
        NumberSerializer serializer = new NumberSerializer(Short.class);
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        Short value = 123;
        
        serializer.serialize(value, generator, provider);
        
        Mockito.verify(generator).writeNumber(value.intValue());
    }
    
    @Test
    public void testSerializeOtherNumber() throws Exception {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        Number value = 12345;
        
        serializer.serialize(value, generator, provider);
        
        Mockito.verify(generator).writeNumber(value.toString());
    }
    
    @Test
    public void testGetSchema() throws Exception {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        Type typeHint = Number.class;
        
        JsonNode schema = serializer.getSchema(provider, typeHint);
        
        assertNotNull(schema);
        assertEquals("number", schema.get("type").asText());
        assertTrue(schema.get("required").asBoolean());
    }
    
    @Test
    public void testAcceptJsonFormatVisitorInt() throws Exception {
        NumberSerializer serializer = new NumberSerializer(BigInteger.class);
        JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = Mockito.mock(JavaType.class);
        
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        Mockito.verify(visitor).visitIntFormat(serializer, typeHint, JsonParser.NumberType.BIG_INTEGER);
    }
    
    @Test
    public void testAcceptJsonFormatVisitorFloat() throws Exception {
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = Mockito.mock(JavaType.class);
        
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        Mockito.verify(visitor).visitFloatFormat(serializer, typeHint, JsonParser.NumberType.BIG_INTEGER);
    }
    
    @Test
    public void testAcceptJsonFormatVisitorOther() throws Exception {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = Mockito.mock(JavaType.class);
        
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        Mockito.verify(visitor).expectNumberFormat(typeHint);
    }
}