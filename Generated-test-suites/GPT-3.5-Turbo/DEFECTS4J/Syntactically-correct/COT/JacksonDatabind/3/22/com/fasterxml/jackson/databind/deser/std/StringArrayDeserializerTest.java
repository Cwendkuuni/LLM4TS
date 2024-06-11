
import org.junit.Test;
import static org.junit.Assert.*;

public class StringArrayDeserializerTest {

    @Test
    public void testDeserialize() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();

        // Test with empty array
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        String[] result = deserializer.deserialize(jp, ctxt);
        assertArrayEquals(new String[]{}, result);

        // Test with array of one string
        jp = new JsonParser();
        ctxt = new DeserializationContext();
        jp.writeStartArray();
        jp.writeString("Hello");
        jp.writeEndArray();
        result = deserializer.deserialize(jp, ctxt);
        assertArrayEquals(new String[]{"Hello"}, result);

        // Test with array of multiple strings
        jp = new JsonParser();
        ctxt = new DeserializationContext();
        jp.writeStartArray();
        jp.writeString("Hello");
        jp.writeString("World");
        jp.writeEndArray();
        result = deserializer.deserialize(jp, ctxt);
        assertArrayEquals(new String[]{"Hello", "World"}, result);
    }

    @Test
    public void testDeserializeWithType() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();

        // Test with empty array
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        TypeDeserializer typeDeserializer = new TypeDeserializer();
        Object result = deserializer.deserializeWithType(jp, ctxt, typeDeserializer);
        assertNull(result);

        // Test with non-empty array
        jp = new JsonParser();
        ctxt = new DeserializationContext();
        jp.writeStartArray();
        jp.writeString("Hello");
        jp.writeString("World");
        jp.writeEndArray();
        result = deserializer.deserializeWithType(jp, ctxt, typeDeserializer);
        assertArrayEquals(new String[]{"Hello", "World"}, (String[])result);
    }
    
    @Test
    public void testHandleNonArray() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();

        // Test with single value and ACCEPT_SINGLE_VALUE_AS_ARRAY disabled
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        jp.writeString("Hello");
        try {
            deserializer.handleNonArray(jp, ctxt);
            fail("Exception not thrown");
        } catch (JsonMappingException ex) {
            // Exception expected
        }

        // Test with single value and ACCEPT_SINGLE_VALUE_AS_ARRAY enabled
        jp = new JsonParser();
        ctxt = new DeserializationContext();
        ctxt.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        jp.writeString("Hello");
        String[] result = deserializer.handleNonArray(jp, ctxt);
        assertArrayEquals(new String[]{"Hello"}, result);

        // Test with null value and ACCEPT_SINGLE_VALUE_AS_ARRAY enabled
        jp = new JsonParser();
        ctxt = new DeserializationContext();
        ctxt.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        jp.writeNull();
        result = deserializer.handleNonArray(jp, ctxt);
        assertArrayEquals(new String[]{null}, result);
    }

}
