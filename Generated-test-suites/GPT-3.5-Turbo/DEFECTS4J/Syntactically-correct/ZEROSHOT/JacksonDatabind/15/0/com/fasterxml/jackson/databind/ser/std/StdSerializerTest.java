import org.junit.Test;

public class StdSerializerTest {

    @Test
    public void testHandledType() {
        StdSerializer serializer = new StdSerializer(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            }
        };
        assertEquals(Object.class, serializer.handledType());
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        StdSerializer serializer = new StdSerializer(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            }
        };
        assertNull(serializer.getSchema(null, null));
        assertNull(serializer.getSchema(null, null, false));
    }

    @Test
    public void testCreateObjectNode() {
        StdSerializer serializer = new StdSerializer(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            }
        };
        assertNotNull(serializer.createObjectNode());
    }

    @Test
    public void testCreateSchemaNode() {
        StdSerializer serializer = new StdSerializer(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            }
        };
        assertNotNull(serializer.createSchemaNode("string"));
        assertNotNull(serializer.createSchemaNode("string", false));
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        StdSerializer serializer = new StdSerializer(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            }
        };
        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
    }

    @Test
    public void testIsDefaultSerializer() {
        StdSerializer serializer = new StdSerializer(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            }
        };
        JsonSerializer<?> customSerializer = new JsonSerializer<>() {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }
        };
        assertFalse(serializer.isDefaultSerializer(customSerializer));
    }

    @Test
    public void testFindConvertingContentSerializer() throws JsonMappingException {
        StdSerializer serializer = new StdSerializer(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {

            }
        };
        SerializerProvider provider = null;
        BeanProperty prop = null;
        JsonSerializer<?> existingSerializer = null;
        assertNull(serializer.findConvertingContentSerializer(provider, prop, existingSerializer));
    }

    @Test
    public void testFindPropertyFilter() throws JsonMappingException {
        StdSerializer serializer = new StdSerializer(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {

            }
        };
        SerializerProvider provider = null;
        Object filterId = null;
        Object valueToFilter = null;
        assertNull(serializer.findPropertyFilter(provider, filterId, valueToFilter));
    }
}