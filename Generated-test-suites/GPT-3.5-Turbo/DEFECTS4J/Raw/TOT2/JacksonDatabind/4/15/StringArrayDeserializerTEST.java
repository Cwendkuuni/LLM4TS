import static org.junit.Assert.*;
import org.junit.Test;

public class StringArrayDeserializerTest {

    @Test
    public void testDeserialize() {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonParser jp = new JsonFactory().createParser("[\"String1\", \"String2\", \"String3\"]");
        DeserializationContext ctxt = new DeserializationContext(null, null, null); // create DeserializationContext object
        try {
            String[] result = deserializer.deserialize(jp, ctxt); // test deserialize method
            assertNotNull(result);
            assertEquals(3, result.length);
            assertEquals("String1", result[0]);
            assertEquals("String2", result[1]);
            assertEquals("String3", result[2]);
        } catch (IOException e) {
            fail("Exception thrown while deserializing: " + e.getMessage());
        }
    }

    @Test
    public void testDeserializeCustom() {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonParser jp = new JsonFactory().createParser("[\"String1\", \"String2\", \"String3\"]");
        DeserializationContext ctxt = new DeserializationContext(null, null, null); // create DeserializationContext object
        try {
            String[] result = deserializer._deserializeCustom(jp, ctxt); // test _deserializeCustom method
            assertNotNull(result);
            assertEquals(3, result.length);
            assertEquals("String1", result[0]);
            assertEquals("String2", result[1]);
            assertEquals("String3", result[2]);
        } catch (IOException e) {
            fail("Exception thrown while deserializing: " + e.getMessage());
        }
    }

    @Test
    public void testHandleNonArray() {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonParser jp = new JsonFactory().createParser("\"SingleString\"");
        DeserializationContext ctxt = new DeserializationContext(null, null, null); // create DeserializationContext object
        try {
            String[] result = deserializer.handleNonArray(jp, ctxt); // test handleNonArray method for single value scenario
            assertNotNull(result);
            assertEquals(1, result.length);
            assertEquals("SingleString", result[0]);
        } catch (IOException e) {
            fail("Exception thrown while handling non-array: " + e.getMessage());
        }
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        DeserializationContext ctxt = new DeserializationContext(null, null, null); // create DeserializationContext object
        BeanProperty property = null; // create BeanProperty object
        try {
            JsonDeserializer<?> result = deserializer.createContextual(ctxt, property); // test createContextual method
            assertNotNull(result);
        } catch (JsonMappingException e) {
            fail("Exception thrown while creating contextual deserializer: " + e.getMessage());
        }
    }
}