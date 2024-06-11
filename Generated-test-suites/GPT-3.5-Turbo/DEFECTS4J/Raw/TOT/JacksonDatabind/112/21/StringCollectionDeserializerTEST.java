import java.util.ArrayList;
import java.util.Collection;
import static org.junit.Assert.*;
import org.junit.Test;

public class StringCollectionDeserializerTest {

    @Test
    public void testDeserialize() {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null);
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        // Test with empty array
        when(p.isExpectedStartArrayToken()).thenReturn(true);
        when(p.getCurrentToken()).thenReturn(JsonToken.END_ARRAY);
        Collection<String> result = new ArrayList<>();
        deserializer.deserialize(p, ctxt, result);
        assertTrue(result.isEmpty());

        // Test with null value
        when(p.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);
        result = new ArrayList<>();
        deserializer.deserialize(p, ctxt, result);
        assertTrue(result.isEmpty());

        // Test with non-array token
        when(p.isExpectedStartArrayToken()).thenReturn(false);
        when(ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(true);

        // Test with null value and accept single value as array enabled
        when(p.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);
        result = new ArrayList<>();
        deserializer.deserialize(p, ctxt, result);
        assertEquals(1, result.size());
        assertEquals(null, result.iterator().next());

        // Test with single string value
        when(p.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(p.getText()).thenReturn("testValue");
        result = new ArrayList<>();
        deserializer.deserialize(p, ctxt, result);
        assertEquals(1, result.size());
        assertEquals("testValue", result.iterator().next());

        // Test with multiple string values
        when(p.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.END_ARRAY);
        when(p.nextTextValue()).thenReturn("value1", "value2", null);
        result = new ArrayList<>();
        deserializer.deserialize(p, ctxt, result);
        assertEquals(2, result.size());
        assertTrue(result.contains("value1"));
        assertTrue(result.contains("value2"));

        // Test with custom deserializer
        JsonDeserializer<String> customDeserializer = mock(JsonDeserializer.class);
        when(p.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.END_ARRAY);
        when(p.nextTextValue()).thenReturn("value1", "value2", null);
        when(customDeserializer.deserialize(p, ctxt)).thenReturn("customValue");
        deserializer = new StringCollectionDeserializer(null, null, customDeserializer);
        result = new ArrayList<>();
        deserializer.deserialize(p, ctxt, result);
        assertEquals(2, result.size());
        assertTrue(result.contains("customValue"));
    }
}