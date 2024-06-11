import static org.junit.Assert.*;
import org.junit.Test;

public class DateTimeSerializerBaseTest {

    @Test
    public void testWithFormat() {
        // Test with valid input
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, true, DateFormat.getDateInstance());
        DateTimeSerializerBase<String> updatedSerializer = serializer.withFormat(false, DateFormat.getTimeInstance());
        assertEquals(false, updatedSerializer._useTimestamp);
        assertEquals(DateFormat.getTimeInstance(), updatedSerializer._customFormat);
        
        // Test with invalid input
        DateTimeSerializerBase<String> invalidSerializer = new DateTimeSerializerBase<String>(String.class, true, null);
        DateTimeSerializerBase<String> updatedInvalidSerializer = invalidSerializer.withFormat(true, null);
        assertEquals(true, updatedInvalidSerializer._useTimestamp);
        assertNull(updatedInvalidSerializer._customFormat);
    }

    @Test
    public void testCreateSchemaNode() {
        DateTimeSerializerBase<Boolean> booleanSerializer = new DateTimeSerializerBase<Boolean>(Boolean.class, false, null);
        JsonNode schemaNode = booleanSerializer.getSchema(null, Boolean.TYPE);
        assertNotNull(schemaNode);
        assertEquals("boolean", schemaNode.get("type").asText());
        assertTrue(schemaNode.get("required").asBoolean());
    }

    @Test
    public void testSerialize() {
        // Test serialization with valid input
        DateTimeSerializerBase<Integer> serializer = new DateTimeSerializerBase<Integer>(Integer.class, false, null) {
            @Override
            public void serialize(Integer value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeNumber(value);
            }

            @Override
            protected long _timestamp(Integer value) {
                return value;
            }
        };

        StringWriter writer = new StringWriter();
        JsonGenerator gen = new JsonFactory().createGenerator(writer);
        serializer.serialize(123, gen, null);
        gen.close();
        assertEquals("123", writer.toString());

        // Test serialization with invalid input
        StringWriter errorWriter = new StringWriter();
        JsonGenerator errorGen = new JsonFactory().createGenerator(errorWriter);
        assertThrows(IOException.class, () -> serializer.serialize(null, errorGen, null));
    }
}