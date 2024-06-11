import static org.junit.Assert.*;
import org.junit.Test;

public class StdKeySerializerTest{
    
    @Test
    public void testSerialize_DateValue() {
        StdKeySerializer serializer = new StdKeySerializer();
        Date date = new Date();
        String expected = date.toString();
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createJsonGenerator(writer);

        try {
            serializer.serialize(date, generator, new DefaultSerializerProvider.Impl());
            generator.close();
            String jsonOutput = writer.toString();
            assertEquals("\"" + expected + "\"", jsonOutput);
        } catch (IOException e) {
            fail("Exception thrown during serialization");
        }
    }
    
    @Test
    public void testSerialize_NonDateValue() {
        StdKeySerializer serializer = new StdKeySerializer();
        String value = "test";
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createJsonGenerator(writer);

        try {
            serializer.serialize(value, generator, new DefaultSerializerProvider.Impl());
            generator.close();
            assertEquals("\"" + value + "\"", writer.toString());
        } catch (IOException e) {
            fail("Exception thrown during serialization");
        }
    }
    
    @Test
    public void testGetSchema() {
        StdKeySerializer serializer = new StdKeySerializer();
        SerializerProvider provider = new DefaultSerializerProvider.Impl();
        
        try {
            JsonNode schema = serializer.getSchema(provider, null);
            assertNotNull(schema);
            assertEquals("string", schema.asText());
        } catch (JsonMappingException e) {
            fail("Exception thrown during schema retrieval");
        }
    }
    
    @Test
    public void testAcceptJsonFormatVisitor() {
        StdKeySerializer serializer = new StdKeySerializer();
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper.Base();
        JavaType typeHint = SimpleType.constructUnsafe(Object.class);
        
        try {
            serializer.acceptJsonFormatVisitor(visitor, typeHint);
            assertTrue(true);
        } catch (JsonMappingException e) {
            fail("Exception thrown during visitor acceptance");
        }
    }
}