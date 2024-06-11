import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

public class NumberSerializerTest {

    private final NumberSerializer numberSerializer = new NumberSerializer(Number.class);

    @Test
    public void testSerializeWithBigDecimal() throws IOException {
        BigDecimal value = new BigDecimal("10.25");
        StringWriter writer = new StringWriter();
        JsonGenerator g = new JsonFactory().createGenerator(writer);
        numberSerializer.serialize(value, g, null);
        g.flush();
        String json = writer.toString();
        Assert.assertEquals("10.25", json);
    }
    
    @Test
    public void testSerializeWithBigInteger() throws IOException {
        BigInteger value = new BigInteger("12345");
        StringWriter writer = new StringWriter();
        JsonGenerator g = new JsonFactory().createGenerator(writer);
        numberSerializer.serialize(value, g, null);
        g.flush();
        String json = writer.toString();
        Assert.assertEquals("12345", json);
    }
    
    @Test
    public void testSerializeWithLong() throws IOException {
        Long value = 100L;
        StringWriter writer = new StringWriter();
        JsonGenerator g = new JsonFactory().createGenerator(writer);
        numberSerializer.serialize(value, g, null);
        g.flush();
        String json = writer.toString();
        Assert.assertEquals("100", json);
    }
    
    @Test
    public void testSerializeWithDouble() throws IOException {
        Double value = 10.5;
        StringWriter writer = new StringWriter();
        JsonGenerator g = new JsonFactory().createGenerator(writer);
        numberSerializer.serialize(value, g, null);
        g.flush();
        String json = writer.toString();
        Assert.assertEquals("10.5", json);
    }
    
    @Test
    public void testSerializeWithFloat() throws IOException {
        Float value = 5.7f;
        StringWriter writer = new StringWriter();
        JsonGenerator g = new JsonFactory().createGenerator(writer);
        numberSerializer.serialize(value, g, null);
        g.flush();
        String json = writer.toString();
        Assert.assertEquals("5.7", json);
    }
    
    @Test
    public void testSerializeWithInteger() throws IOException {
        Integer value = 50;
        StringWriter writer = new StringWriter();
        JsonGenerator g = new JsonFactory().createGenerator(writer);
        numberSerializer.serialize(value, g, null);
        g.flush();
        String json = writer.toString();
        Assert.assertEquals("50", json);
    }
    
    @Test
    public void testSerializeWithByte() throws IOException {
        Byte value = 3;
        StringWriter writer = new StringWriter();
        JsonGenerator g = new JsonFactory().createGenerator(writer);
        numberSerializer.serialize(value, g, null);
        g.flush();
        String json = writer.toString();
        Assert.assertEquals("3", json);
    }
    
    @Test
    public void testSerializeWithShort() throws IOException {
        Short value = 7;
        StringWriter writer = new StringWriter();
        JsonGenerator g = new JsonFactory().createGenerator(writer);
        numberSerializer.serialize(value, g, null);
        g.flush();
        String json = writer.toString();
        Assert.assertEquals("7", json);
    }
    
    @Test
    public void testSerializeWithString() throws IOException {
        String value = "test";
        StringWriter writer = new StringWriter();
        JsonGenerator g = new JsonFactory().createGenerator(writer);
        numberSerializer.serialize(value, g, null);
        g.flush();
        String json = writer.toString();
        Assert.assertEquals("\"test\"", json);
    }

    @Test
    public void testGetSchema() {
        SerializerProvider provider = null;
        Type typeHint = null;
        JsonNode schema = numberSerializer.getSchema(provider, typeHint);
        Assert.assertEquals("number", schema.get("type").asText());
    }
    
    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;
        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        // Test passes if no exceptions are thrown
    }
}