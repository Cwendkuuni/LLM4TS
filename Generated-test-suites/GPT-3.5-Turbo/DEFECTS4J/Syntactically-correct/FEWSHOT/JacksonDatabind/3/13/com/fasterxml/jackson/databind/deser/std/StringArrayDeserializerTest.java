import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class StringArrayDeserializerTest {
    @Test
    public void testDeserialize() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] expected1 = new String[]{"foo", "bar"};
        String[] result1 = deserializer.deserialize(JsonParser.START_ARRAY, DeserializationContext);
        assertArrayEquals(expected1, result1);
        
        String[] expected2 = new String[]{};
        String[] result2 = deserializer.deserialize(JsonParser.END_ARRAY, DeserializationContext);
        assertArrayEquals(expected2, result2);
        
        String[] expected3 = new String[]{"foo", null, "bar"};
        String[] result3 = deserializer.deserialize(JsonParser.START_ARRAY, DeserializationContext);
        assertArrayEquals(expected3, result3);
        
        String[] expected4 = new String[]{"foo"};
        String[] result4 = deserializer.deserialize(JsonParser.START_ARRAY, DeserializationContext);
        assertArrayEquals(expected4, result4);
    }
    
    @Test
    public void testDeserializeWithType() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] expected1 = new String[]{"foo", "bar"};
        String[] result1 = deserializer.deserializeWithType(JsonParser.START_ARRAY, DeserializationContext, TypeDeserializer);
        assertArrayEquals(expected1, result1);
        
        String[] expected2 = new String[]{};
        String[] result2 = deserializer.deserializeWithType(JsonParser.END_ARRAY, DeserializationContext, TypeDeserializer);
        assertArrayEquals(expected2, result2);
        
        String[] expected3 = new String[]{"foo", null, "bar"};
        String[] result3 = deserializer.deserializeWithType(JsonParser.START_ARRAY, DeserializationContext, TypeDeserializer);
        assertArrayEquals(expected3, result3);
        
        String[] expected4 = new String[]{"foo"};
        String[] result4 = deserializer.deserializeWithType(JsonParser.START_ARRAY, DeserializationContext, TypeDeserializer);
        assertArrayEquals(expected4, result4);
    }
    
    @Test
    public void testHandleNonArray() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        assertNull(deserializer.handleNonArray(JsonParser.END_ARRAY, DeserializationContext));
        assertNull(deserializer.handleNonArray(JsonParser.VALUE_NULL, DeserializationContext));
    }
    
    @Test
    public void testContextual() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonDeserializer<?> expected = new CustomDeserializer();
        JsonDeserializer<?> result = deserializer.createContextual(DeserializationContext, BeanProperty);
        assertEquals(expected, result);
    }
}