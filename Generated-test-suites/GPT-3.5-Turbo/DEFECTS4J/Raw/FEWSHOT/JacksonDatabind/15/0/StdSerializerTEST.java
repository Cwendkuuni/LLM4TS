import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.cli.Util;
import org.junit.Test;

public class StdSerializerTest {
    @Test
    public void testCreateObjectNode() {
        ObjectNode objectNode = Util.createObjectNode();
        assertNotNull(objectNode);
    }
    
    @Test
    public void testCreateSchemaNodeWithStringType() {
        ObjectNode schemaNode = Util.createSchemaNode("string");
        assertEquals("string", schemaNode.get("type").asText());
        assertFalse(schemaNode.has("required"));
    }
    
    @Test
    public void testCreateSchemaNodeWithStringTypeAndIsOptional() {
        ObjectNode schemaNode = Util.createSchemaNode("string", true);
        assertEquals("string", schemaNode.get("type").asText());
        assertFalse(schemaNode.get("required").asBoolean(true));
    }
    
    @Test
    public void testCreateSchemaNodeWithStringTypeAndIsNotOptional() {
        ObjectNode schemaNode = Util.createSchemaNode("string", false);
        assertEquals("string", schemaNode.get("type").asText());
        assertTrue(schemaNode.get("required").asBoolean(false));
    }
    
    @Test
    public void testWrapAndThrowWithNullProvider() {
        try {
            Util.wrapAndThrow(null, new IOException(), new Object(), "field");
            fail("Should throw IOException");
        } catch (IOException e) {
            // Expected
        }
    }
    
    @Test
    public void testWrapAndThrowWithNoWrapEnabled() {
        SerializerProvider provider = new SerializerProvider();
        provider.configure(SerializationFeature.WRAP_EXCEPTIONS, false);
        try {
            Util.wrapAndThrow(provider, new IOException(), new Object(), "field");
            fail("Should throw IOException");
        } catch (IOException e) {
            // Expected
        }
    }
    
    @Test(expected = JsonMappingException.class)
    public void testWrapAndThrowWithJsonMappingException() throws IOException {
        SerializerProvider provider = new SerializerProvider();
        Util.wrapAndThrow(provider, new JsonMappingException(""), new Object(), "field");
    }
    
    @Test
    public void testIsDefaultSerializerWithDefaultSerializer() {
        assertTrue(Util.isDefaultSerializer(new StdSerializer<>(StdSerializer.class) {}));
    }
    
    @Test
    public void testIsDefaultSerializerWithCustomSerializer() {
        assertFalse(Util.isDefaultSerializer(new CustomSerializer<>(StdSerializer.class) {}));
    }
}