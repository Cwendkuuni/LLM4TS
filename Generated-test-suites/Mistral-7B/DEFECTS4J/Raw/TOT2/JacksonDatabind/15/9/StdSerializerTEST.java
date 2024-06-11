import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class StdSerializerTest {

    @Test
    public void testDefaultConstructor() {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class);
        assertNotNull(serializer);
        assertEquals(String.class, serializer.handledType());
    }

    @Test
    public void testTypeConstructor() throws JsonProcessingException {
        StdSerializer<ObjectNode> serializer = new StdSerializer<ObjectNode>(JsonType.construct(ObjectNode.class));
        assertNotNull(serializer);
        assertEquals(ObjectNode.class, serializer.handledType());
    }

    @Test
    public void testSerialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        StdSerializer<String> serializer = new StdSerializer<String>(String.class);
        String value = "test";
        ObjectNode node = mapper.createObjectNode();
        node.put("value", value);
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        serializer.serialize(value, generator, mapper.getSerializationConfig());
        generator.flush();
        assertEquals("\"test\"", mapper.readTree(System.in).get("value").asText());
    }

    @Test(expected = IOException.class)
    public void testSerializationWithException() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdSerializer<String> serializer = new StdSerializer<String>(String.class);
        throw serializer.wrapAndThrow(mapper.getSerializationConfig(), new IOException(), null, "test");
    }

    @Test
    public void testGetSchema() {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class);
        assertEquals(createSchemaNode("string"), serializer.getSchema(null, String.class));
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class);
        JsonFormatVisitorWrapper visitor = null;
        serializer.acceptJsonFormatVisitor(visitor, String.class);
    }

    @Test
    public void testIsDefaultSerializer() {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class);
        JsonSerializer<Object> anotherSerializer = new JsonStringSerializer();
        assertTrue(StdSerializer.class.isInstance(serializer));
        assertFalse(StdSerializer.class.isInstance(anotherSerializer));
        assertTrue(StdSerializer.class.isInstance(serializer));
        assertTrue(StdSerializer.class.isInstance(serializer));
    }

    @Test
    public void testFindConvertingContentSerializer() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        StdSerializer<ObjectNode> serializer = new StdSerializer<ObjectNode>(ObjectNode.class);
        ObjectNode value = mapper.createObjectNode();
        ObjectNode expectedValue = mapper.createObjectNode();
        expectedValue.put("test", "test");
        PropertyFilter propertyFilter = mapper.getSerializers().findPropertyFilter(null, "test");
        JsonSerializer<ObjectNode> existingSerializer = mapper.findValueSerializer(JsonType.construct(ObjectNode.class));
        JsonSerializer<ObjectNode> serializerWithFilter = serializer.findConvertingContentSerializer(mapper.getSerializationConfig(), null, value);
        ObjectNode result = mapper.writerWithRootValueSerializer(serializerWithFilter).writeValueAsTree(value);
        assertEquals(expectedValue, mapper.readerForTree(result));
    }

    @Test
    public void testFindPropertyFilter() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        String filterId = "test";
        Object valueToFilter = new Object();
        PropertyFilter filter = mapper.getSerializers().findPropertyFilter(filterId, valueToFilter);
        assertNull(filter);
    }

}