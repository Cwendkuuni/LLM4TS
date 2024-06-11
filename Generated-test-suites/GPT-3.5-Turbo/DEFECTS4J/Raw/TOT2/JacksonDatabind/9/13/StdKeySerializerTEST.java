import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.util.Date;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

public class StdKeySerializerTest {

    @Test
    public void testSerializeDateKey() {
        StdKeySerializer serializer = new StdKeySerializer();
        Date date = new Date();
        JsonGenerator generator = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();

        try {
            serializer.serialize(date, generator, provider);
        } catch (IOException e) {
            fail("Serialization of Date key failed");
        }
    }

    @Test
    public void testSerializeStringKey() {
        StdKeySerializer serializer = new StdKeySerializer();
        String str = "testKey";
        JsonGenerator generator = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();

        try {
            serializer.serialize(str, generator, provider);
        } catch (IOException e) {
            fail("Serialization of String key failed");
        }
    }

    @Test
    public void testGetSchema() {
        StdKeySerializer serializer = new StdKeySerializer();
        SerializerProvider provider = new SerializerProvider();
        Type typeHint = null;

        try {
            JsonNode schema = serializer.getSchema(provider, typeHint);
            assertNotNull(schema);
            assertEquals("string", schema.asText());
        } catch (JsonMappingException e) {
            fail("Get schema failed");
        }
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        StdKeySerializer serializer = new StdKeySerializer();
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper();
        JavaType typeHint = new JavaType();

        try {
            serializer.acceptJsonFormatVisitor(visitor, typeHint);
        } catch (JsonMappingException e) {
            fail("Accepting Json format visitor failed");
        }
    }
}