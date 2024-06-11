import static org.junit.Assert.*;
import org.junit.Test;

public class StdKeySerializerTest {

    @Test
    public void testSerialize_Date() throws IOException {
        StdKeySerializer serializer = new StdKeySerializer();
        Date date = new Date();
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        SerializerProvider provider = new DefaultSerializerProvider.Impl();

        serializer.serialize(date, generator, provider);

        generator.close();
        String json = writer.toString();

        assertEquals("\"" + date.toString() + "\"", json);
    }

    @Test
    public void testSerialize_String() throws IOException {
        StdKeySerializer serializer = new StdKeySerializer();
        String input = "test";
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        SerializerProvider provider = new DefaultSerializerProvider.Impl();

        serializer.serialize(input, generator, provider);

        generator.close();
        String json = writer.toString();

        assertEquals("\"" + input + "\"", json);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        StdKeySerializer serializer = new StdKeySerializer();
        SerializerProvider provider = new DefaultSerializerProvider.Impl();

        JsonNode schema = serializer.getSchema(provider, null);

        assertEquals("string", schema.asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        StdKeySerializer serializer = new StdKeySerializer();
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper.Base();
        JavaType typeHint = TypeFactory.defaultInstance().constructType(Object.class);

        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        assertTrue(true); // No exception means test passed
    }
}