import static org.junit.Assert.*;
import org.junit.Test;

public class StdKeySerializerTest {

    @Test
    public void testSerializeForDate() {
        StdKeySerializer serializer = new StdKeySerializer();
        Date date = new Date();
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new DefaultSerializerProvider.Impl();
        try {
            serializer.serialize(date, generator, provider);
            // Verify if the date is serialized correctly
            assertEquals("\"" + date.toString() + "\"", generator.getCurrentValue());
        } catch (IOException e) {
            fail("Exception thrown during serialization");
        }
    }

    @Test
    public void testSerializeForNonDate() {
        StdKeySerializer serializer = new StdKeySerializer();
        Integer number = 123;
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new DefaultSerializerProvider.Impl();
        try {
            serializer.serialize(number, generator, provider);
            // Verify if the number is serialized correctly
            assertEquals("\"" + number.toString() + "\"", generator.getCurrentValue());
        } catch (IOException e) {
            fail("Exception thrown during serialization");
        }
    }

    @Test
    public void testGetSchema() {
        StdKeySerializer serializer = new StdKeySerializer();
        SerializerProvider provider = new DefaultSerializerProvider.Impl();
        try {
            JsonNode schema = serializer.getSchema(provider, Object.class);
            // Verify if the schema is of type string
            assertEquals("string", schema.asText());
        } catch (JsonMappingException e) {
            fail("Exception thrown while getting schema");
        }
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        StdKeySerializer serializer = new StdKeySerializer();
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper.Base();
        JavaType typeHint = TypeFactory.defaultInstance().constructType(Date.class);
        try {
            serializer.acceptJsonFormatVisitor(visitor, typeHint);
            // No assertion is required for this method
        } catch (JsonMappingException e) {
            fail("Exception thrown while accepting JSON format visitor");
        }
    }
}