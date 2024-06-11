import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.ser.std.JsonValueSerializer;

public class JsonValueSerializerTest {

    @Test
    public void testConstructor() {
        AnnotatedMethod valueMethod = new AnnotatedMethod();
        JsonSerializer<?> ser = new JsonSerializer<>();
        
        JsonValueSerializer serializer = new JsonValueSerializer(valueMethod, ser);
        
        assertNotNull(serializer);
        assertEquals(valueMethod, serializer._accessorMethod);
        assertEquals(ser, serializer._valueSerializer);
        assertNull(serializer._property);
        assertTrue(serializer._forceTypeInformation);
    }

    @Test
    public void testSerialization() {
        AnnotatedMethod valueMethod = new AnnotatedMethod();
        JsonSerializer<?> ser = new JsonSerializer<>();

        JsonValueSerializer serializer = new JsonValueSerializer(valueMethod, ser);

        JsonGenerator gen = new JsonGenerator();
        SerializerProvider prov = new SerializerProvider();

        Object bean = new Object();

        try {
            serializer.serialize(bean, gen, prov);
        } catch (IOException e) {
            fail("Serialization failed with IOException");
        }
    }

    @Test
    public void testSerializationWithType() {
        AnnotatedMethod valueMethod = new AnnotatedMethod();
        JsonSerializer<?> ser = new JsonSerializer<>();

        JsonValueSerializer serializer = new JsonValueSerializer(valueMethod, ser);

        JsonGenerator gen = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();

        Object bean = new Object();

        TypeSerializer typeSer0 = new TypeSerializer();

        try {
            serializer.serializeWithType(bean, gen, provider, typeSer0);
        } catch (IOException e) {
            fail("Serialization with type information failed with IOException");
        }
    }

    @Test
    public void testGetSchema() {
        AnnotatedMethod valueMethod = new AnnotatedMethod();
        JsonSerializer<?> ser = new JsonSerializer<>();

        JsonValueSerializer serializer = new JsonValueSerializer(valueMethod, ser);

        SerializerProvider provider = new SerializerProvider();
        Type typeHint = new Type();

        try {
            JsonNode schema = serializer.getSchema(provider, typeHint);
            assertNotNull(schema);
        } catch (JsonMappingException e) {
            fail("Failed to get schema with JsonMappingException");
        }
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        AnnotatedMethod valueMethod = new AnnotatedMethod();
        JsonSerializer<?> ser = new JsonSerializer<>();

        JsonValueSerializer serializer = new JsonValueSerializer(valueMethod, ser);

        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper();
        JavaType typeHint = new JavaType();

        try {
            serializer.acceptJsonFormatVisitor(visitor, typeHint);
        } catch (JsonMappingException e) {
            fail("Failed to accept JSON format visitor with JsonMappingException");
        }
    }

}