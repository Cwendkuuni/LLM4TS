import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class StdSerializerTest {

    private StdSerializer<Integer> serializer;

    @Before
    public void setUp() {
        serializer = new StdSerializer<Integer>(Integer.class) {
            @Override
            public void serialize(Integer value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {

            }
        };
    }

    @Test
    public void handledType() {
        Class<Integer> expected = Integer.class;
        Class<Integer> actual = serializer.handledType();
        assertEquals(expected, actual);
    }

    @Test
    public void getSchema() throws JsonMappingException {
        SerializerProvider provider = null;
        Type typeHint = null;
        JsonNode expected = null;
        JsonNode actual = serializer.getSchema(provider, typeHint);
        assertEquals(expected, actual);
    }

    @Test
    public void getSchema1() throws JsonMappingException {
        SerializerProvider provider = null;
        Type typeHint = null;
        boolean isOptional = false;
        JsonNode expected = null;
        JsonNode actual = serializer.getSchema(provider, typeHint, isOptional);
        assertEquals(expected, actual);
    }

    @Test
    public void createObjectNode() {
        ObjectNode expected = null;
        ObjectNode actual = serializer.createObjectNode();
        assertEquals(expected, actual);
    }

    @Test
    public void createSchemaNode() {
        String type = "";
        ObjectNode expected = null;
        ObjectNode actual = serializer.createSchemaNode(type);
        assertEquals(expected, actual);
    }

    @Test
    public void createSchemaNode1() {
        String type = "";
        boolean isOptional = false;
        ObjectNode expected = null;
        ObjectNode actual = serializer.createSchemaNode(type, isOptional);
        assertEquals(expected, actual);
    }

    @Test
    public void acceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
    }

    @Test
    public void isDefaultSerializer() {
        JsonSerializer<?> serializer = null;
        boolean expected = false;
        boolean actual = this.serializer.isDefaultSerializer(serializer);
        assertEquals(expected, actual);
    }

    @Test
    public void findConvertingContentSerializer() throws JsonMappingException {
        SerializerProvider provider = null;
        BeanProperty prop = null;
        JsonSerializer<?> existingSerializer = null;
        JsonSerializer<?> expected = null;
        JsonSerializer<?> actual = this.serializer.findConvertingContentSerializer(provider, prop, existingSerializer);
        assertEquals(expected, actual);
    }

    @Test
    public void findPropertyFilter() throws JsonMappingException {
        SerializerProvider provider = null;
        Object filterId = null;
        Object valueToFilter = null;
        PropertyFilter expected = null;
        PropertyFilter actual = this.serializer.findPropertyFilter(provider, filterId, valueToFilter);
        assertEquals(expected, actual);
    }
}