import static org.junit.Assert.*;
import org.junit.Test;

public class FromStringDeserializerTest {
    
    @Test
    public void testTypes() {
        Class<?>[] types = FromStringDeserializer.types();
        assertNotNull(types);
        assertEquals(13, types.length);
    }

    @Test
    public void testFindDeserializer() {
        Std deserializer = FromStringDeserializer.findDeserializer(File.class);
        assertNotNull(deserializer);
        assertEquals(Std.STD_FILE, deserializer._kind);
        
        deserializer = FromStringDeserializer.findDeserializer(StringBuilder.class);
        assertNotNull(deserializer);
        assertEquals(Std.STD_STRING_BUILDER, deserializer._kind);
        
        deserializer = FromStringDeserializer.findDeserializer(ClassUtil.class);
        assertNull(deserializer);
    }

    @Test
    public void testDeserialize() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(String.class, 0);
        DeserializationContext ctxt = new DeserializationContext(null);
        
        // Test typical use case
        try {
            String json = "\"test\"";
            JsonParser parser = new JsonFactory().createParser(json);
            String result = deserializer.deserialize(parser, ctxt);
            assertEquals("test", result);
        } catch (IOException e) {
            fail("IOException thrown while testing typical use case");
        }
        
        // Test empty string
        try {
            String json = "\"\"";
            JsonParser parser = new JsonFactory().createParser(json);
            String result = deserializer.deserialize(parser, ctxt);
            assertNull(result);
        } catch (IOException e) {
            fail("IOException thrown while testing empty string case");
        }
        
        // Test unexpected token
        try {
            String json = "10";
            JsonParser parser = new JsonFactory().createParser(json);
            String result = deserializer.deserialize(parser, ctxt);
            assertNull(result);
        } catch (IOException e) {
            assertTrue(e instanceof JsonMappingException);
        }
    }

    // Add more test cases here for other methods

}