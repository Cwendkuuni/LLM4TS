import static org.junit.Assert.*;
import org.junit.Test;

public class JsonValueSerializerTest {

    @Test
    public void testSerialize() {
        // Test when value is null
        JsonValueSerializer serializer = new JsonValueSerializer(null, null);
        assertEquals("null", serializer.serialize(null));
     
        // Test when value is not null
        serializer = new JsonValueSerializer(null, new JsonSerializer<Object>() {
            public void serialize(Object value, JsonGenerator gen, SerializerProvider prov) throws IOException {
                gen.writeString(value.toString());
            }
        });
        assertEquals("\"test\"", serializer.serialize("test"));
    }

    @Test
    public void testSerializeWithType() {
        // Test when value is null
        JsonValueSerializer serializer = new JsonValueSerializer(null, null);
        assertEquals("null", serializer.serializeWithType(null));
     
        // Test when value is not null
        serializer = new JsonValueSerializer(null, new JsonSerializer<Object>() {
            public void serialize(Object value, JsonGenerator gen, SerializerProvider prov) throws IOException {
                gen.writeNull();
            }

            public void serializeWithType(Object value, JsonGenerator gen, SerializerProvider prov, TypeSerializer typeSer) throws IOException {
                gen.writeString(value.toString());
            }
        });
        assertEquals("\"test\"", serializer.serializeWithType("test"));
    }

    @Test
    public void testGetSchema() {
        JsonValueSerializer serializer = new JsonValueSerializer(null, null);
        JsonNode node = serializer.getSchema(null);
        assertEquals(0, node.size());
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        JsonValueSerializer serializer = new JsonValueSerializer(null, null);
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(null);
        serializer.acceptJsonFormatVisitor(visitor, null);
    }

}