import static org.junit.Assert.*;

import org.junit.Test;

public class StringArrayDeserializerTest {
    
    private StringArrayDeserializer stringArrayDeserializer = StringArrayDeserializer.instance;

    @Test
    public void testDeserialize() {
        // Typical use case
        String json = "[\"value1\", \"value2\", \"value3\"]";
        String[] expected = {"value1", "value2", "value3"};
        assertEquals(expected, stringArrayDeserializer.deserialize(new JsonFactory().createParser(json), DeserializationContext.emptyContext()));
        
        // Edge case: single value array
        json = "\"singleValue\"";
        expected = new String[] {"singleValue"};
        assertEquals(expected, stringArrayDeserializer.deserialize(new JsonFactory().createParser(json), DeserializationContext.emptyContext()));
        
        // Edge case: empty array
        json = "[]";
        expected = new String[] {};
        assertEquals(expected, stringArrayDeserializer.deserialize(new JsonFactory().createParser(json), DeserializationContext.emptyContext()));
        
        // Error scenario: invalid array format
        json = "{invalidJson}";
        assertThrows(IOException.class, () -> stringArrayDeserializer.deserialize(new JsonFactory().createParser(json), DeserializationContext.emptyContext()));
    }

    @Test
    public void testDeserializeWithType() {
        // Typical use case
        String json = "[\"value1\", \"value2\", \"value3\"]";
        String[] expected = {"value1", "value2", "value3"};
        TypeDeserializer typeDeserializer = new StdTypeResolverBuilder().buildTypeDeserializer(new DeserializationConfig(), new ArrayTypeResolverBuilder(), null);
        assertEquals(expected, stringArrayDeserializer.deserializeWithType(new JsonFactory().createParser(json), DeserializationContext.emptyContext(), typeDeserializer));
        
        // Edge case: single value array
        json = "\"singleValue\"";
        expected = new String[] {"singleValue"};
        assertEquals(expected, stringArrayDeserializer.deserializeWithType(new JsonFactory().createParser(json), DeserializationContext.emptyContext(), typeDeserializer));
        
        // Error scenario: invalid array with type information
        json = "{invalidJson}";
        assertThrows(IOException.class, () -> stringArrayDeserializer.deserializeWithType(new JsonFactory().createParser(json), DeserializationContext.emptyContext(), typeDeserializer));
    }

    @Test
    public void testHandleNonArray() {
        // Typical use case
        String json = "\"singleValue\"";
        String[] expected = {"singleValue"};
        assertEquals(expected, stringArrayDeserializer.handleNonArray(new JsonFactory().createParser(json), DeserializationContext.emptyContext()));
        
        // Edge case: empty string as null object
        json = "\"\"";
        expected = null;
        assertEquals(expected, stringArrayDeserializer.handleNonArray(new JsonFactory().createParser(json), DeserializationContext.emptyContext()));
        
        // Error scenario: single value not accepted as array
        json = "[\"value1\", \"value2\"]";
        DeserializationContext ctxt = DeserializationContext.emptyContext();
        ctxt.disable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        assertThrows(JsonMappingException.class, () -> stringArrayDeserializer.handleNonArray(new JsonFactory().createParser(json), ctxt));
    }

}