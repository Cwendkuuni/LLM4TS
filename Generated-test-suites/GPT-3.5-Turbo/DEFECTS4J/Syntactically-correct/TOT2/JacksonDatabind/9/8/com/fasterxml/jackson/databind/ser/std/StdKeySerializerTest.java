import static org.junit.Assert.*;
import org.junit.Test;

public class StdKeySerializerTest {

    @Test
    public void testSerialize_Date() {
        // test serializing a Date object
        StdKeySerializer serializer = new StdKeySerializer();
        Date date = new Date();
        String expected = date.toString();
        StringWriter writer = new StringWriter();
        JsonGenerator jgen = new JsonFactory().createGenerator(writer);
        SerializerProvider provider = new DefaultSerializerProvider.Impl();

        try {
            serializer.serialize(date, jgen, provider);
            jgen.flush();
            assertEquals("\"" + expected + "\"", writer.toString());
        } catch (IOException e) {
            fail("IOException was thrown unexpectedly");
        }
    }

    @Test
    public void testSerialize_String() {
        // test serializing a String object
        StdKeySerializer serializer = new StdKeySerializer();
        String str = "test";
        StringWriter writer = new StringWriter();
        JsonGenerator jgen = new JsonFactory().createGenerator(writer);
        SerializerProvider provider = new DefaultSerializerProvider.Impl();

        try {
            serializer.serialize(str, jgen, provider);
            jgen.flush();
            assertEquals("\"" + str + "\"", writer.toString());
        } catch (IOException e) {
            fail("IOException was thrown unexpectedly");
        }
    }

    @Test
    public void testGetSchema() {
        // test getting the schema for the serializer
        StdKeySerializer serializer = new StdKeySerializer();
        SerializerProvider provider = new DefaultSerializerProvider.Impl();
        Type typeHint = String.class;

        try {
            JsonNode schema = serializer.getSchema(provider, typeHint);
            assertEquals("string", schema.asText());
        } catch (JsonMappingException e) {
            fail("JsonMappingException was thrown unexpectedly");
        }
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        // test accepting a JSON format visitor
        StdKeySerializer serializer = new StdKeySerializer();
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper();
        JavaType typeHint = TypeFactory.defaultInstance().constructType(String.class);

        try {
            serializer.acceptJsonFormatVisitor(visitor, typeHint);
            assertTrue(true); // success if no exception is thrown
        } catch (JsonMappingException e) {
            fail("JsonMappingException was thrown unexpectedly");
        }
    }
}