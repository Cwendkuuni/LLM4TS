
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class StringArrayDeserializerTest {

    @Test
    public void testDeserialize() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        // Test case 1: Empty array
        String[] result1 = deserializer.deserialize(new JsonParser(), new DeserializationContext());
        assertNotNull(result1);
        assertEquals(0, result1.length);
        
        // Test case 2: Array with one element
        JsonParser jp2 = new JsonParser();
        jp2.nextToken();
        jp2.setValue("value");
        String[] result2 = deserializer.deserialize(jp2, new DeserializationContext());
        assertNotNull(result2);
        assertEquals(1, result2.length);
        assertEquals("value", result2[0]);
        
        // Test case 3: Array with multiple elements
        JsonParser jp3 = new JsonParser();
        jp3.nextToken();
        jp3.setValue("value1");
        jp3.nextToken();
        jp3.setValue("value2");
        jp3.nextToken();
        jp3.setValue("value3");
        String[] result3 = deserializer.deserialize(jp3, new DeserializationContext());
        assertNotNull(result3);
        assertEquals(3, result3.length);
        assertEquals("value1", result3[0]);
        assertEquals("value2", result3[1]);
        assertEquals("value3", result3[2]);
    }
    
    @Test(expected = JsonParseException.class)
    public void testDeserializeInvalidJson() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        // Test case: Invalid JSON
        JsonParser jp = new JsonParser();
        jp.nextToken();
        deserializer.deserialize(jp, new DeserializationContext());
    }
    
    @Test(expected = JsonMappingException.class)
    public void testDeserializeNonArray() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        // Test case: Non-array value
        JsonParser jp = new JsonParser();
        jp.nextToken();
        jp.setValue("value");
        deserializer.deserialize(jp, new DeserializationContext());
    }
    
    @Test(expected = JsonMappingException.class)
    public void testDeserializeNullValue() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        // Test case: Array with null value
        JsonParser jp = new JsonParser();
        jp.nextToken();
        jp.setValue(null);
        deserializer.deserialize(jp, new DeserializationContext());
    }
}
