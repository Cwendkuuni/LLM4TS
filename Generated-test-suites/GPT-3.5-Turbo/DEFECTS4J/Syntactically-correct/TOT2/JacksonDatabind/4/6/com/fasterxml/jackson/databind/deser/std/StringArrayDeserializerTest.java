import static org.junit.Assert.*;
import org.junit.Test;

public class StringArrayDeserializerTest {

    @Test
    public void testDeserialize() {
        // Test deserialize method with a valid JSON array
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] expected = {"value1", "value2", "value3"};
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        String[] result = deserializer.deserialize(jp, ctxt);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testDeserializeWithCustom() {
        // Test deserialize method when using the custom deserializer
        CustomStringDeserializer customDeserializer = new CustomStringDeserializer();
        StringArrayDeserializer deserializer = new StringArrayDeserializer(customDeserializer);
        String[] expected = {"custom1", "custom2", "custom3"};
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        String[] result = deserializer.deserialize(jp, ctxt);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testDeserializeWithType() {
        // Test deserializeWithType method
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        TypeDeserializer typeDeserializer = new TypeDeserializer();
        Object result = deserializer.deserializeWithType(jp, ctxt, typeDeserializer);
        assertNotNull(result);
    }

    @Test
    public void testHandleNonArray() {
        // Test handleNonArray method with different scenarios
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        
        // Test for single value as array
        String[] resultSingle = deserializer.handleNonArray(jp, ctxt);
        String[] expectedSingle = {"single"};
        assertArrayEquals(expectedSingle, resultSingle);
        
        // Test for empty string as null object
        jp.setCurrentToken(JsonToken.VALUE_STRING);
        String[] resultEmpty = deserializer.handleNonArray(jp, ctxt);
        assertNull(resultEmpty[0]);
    }

    @Test
    public void testCreateContextual() {
        // Test createContextual method
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        DeserializationContext ctxt = new DeserializationContext();
        BeanProperty property = new BeanProperty();

        // Test contextualization with default deserializer
        JsonDeserializer<?> resultDefault = deserializer.createContextual(ctxt, property);
        assertNull(resultDefault);

        // Test contextualization with custom deserializer
        CustomStringDeserializer customDeserializer = new CustomStringDeserializer();
        JsonDeserializer<?> resultCustom = deserializer.createContextual(ctxt, property);
        assertNotNull(resultCustom);
    }
}