import static org.junit.Assert.*;
import org.junit.Test;

public class StringArrayDeserializerTest {

    @Test
    public void deserialize_withStrings_only() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] expected = {"apple", "banana", "cherry"};
        String[] result = deserializer.deserialize(mockJsonParserForStringsOnly(), mockDeserializationContext());
        
        assertArrayEquals(expected, result);
    }

    @Test
    public void deserialize_withNulls() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] expected = {null, "apple", null};
        String[] result = deserializer.deserialize(mockJsonParserForNulls(), mockDeserializationContext());
        
        assertArrayEquals(expected, result);
    }

    @Test
    public void deserialize_withMixedDataTypes() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] expected = {"apple", null, "123"};
        String[] result = deserializer.deserialize(mockJsonParserForMixedDataTypes(), mockDeserializationContext());
        
        assertArrayEquals(expected, result);
    }

    @Test
    public void handleNonArray_emptyStringAsNull() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] expected = null;
        String[] result = deserializer.handleNonArray(mockJsonParserForEmptyString(), mockDeserializationContext());
        
        assertArrayEquals(expected, result);
    }

    private JsonParser mockJsonParserForStringsOnly() {
        // Mocking logic to simulate a JsonParser with only strings
    }

    private JsonParser mockJsonParserForNulls() {
        // Mocking logic to simulate a JsonParser with nulls
    }

    private JsonParser mockJsonParserForMixedDataTypes() {
        // Mocking logic to simulate a JsonParser with mixed data types
    }

    private JsonParser mockJsonParserForEmptyString() {
        // Mocking logic to simulate a JsonParser with empty string
    }

    private DeserializationContext mockDeserializationContext() {
        // Mocking logic to simulate a DeserializationContext
    }
}