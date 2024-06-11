import static org.junit.Assert.*;
import org.junit.Test;

public class StdKeySerializerTest {

    @Test
    public void testSerializeDate() {
        StdKeySerializer serializer = new StdKeySerializer();
        Date date = new Date();
        JsonGenerator jgen = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new DefaultSerializerProvider.Impl();

        try {
            serializer.serialize(date, jgen, provider);
        } catch (IOException e) {
            fail("Failed to serialize date key.");
        }
    }

    @Test
    public void testSerializeNonDate() {
        StdKeySerializer serializer = new StdKeySerializer();
        Object obj = "test";
        JsonGenerator jgen = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new DefaultSerializerProvider.Impl();

        try {
            serializer.serialize(obj, jgen, provider);
        } catch (IOException e) {
            fail("Failed to serialize non-date key.");
        }
    }

    @Test
    public void testGetSchema() {
        StdKeySerializer serializer = new StdKeySerializer();
        SerializerProvider provider = new DefaultSerializerProvider.Impl();
        Type typeHint = String.class;

        try {
            JsonNode schema = serializer.getSchema(provider, typeHint);
            assertNotNull(schema);
            assertEquals("string", schema.asText());
        } catch (JsonMappingException e) {
            fail("Failed to get schema.");
        }
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        StdKeySerializer serializer = new StdKeySerializer();
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper.Base();
        JavaType typeHint = TypeFactory.defaultInstance().constructSimpleType(String.class, null);

        try {
            serializer.acceptJsonFormatVisitor(visitor, typeHint);
        } catch (JsonMappingException e) {
            fail("Failed to accept Json format visitor.");
        }
    }
}