// Import necessary libraries
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StringCollectionDeserializerTest {

    @InjectMocks
    private StringCollectionDeserializer stringCollectionDeserializer;

    @Mock
    private JsonParser jsonParser;

    @Mock
    private DeserializationContext deserializationContext;

    @Test
    public void testDeserialize_DefaultDeserializer_ValidInput() throws IOException {
        // Create a mock Collection<String>
        Collection<String> result = new ArrayList<>();
        
        // Setup mock behavior
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.nextTextValue()).thenReturn("string1", "string2", null);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.VALUE_STRING,
                JsonToken.VALUE_NULL);

        // Call the method under test
        Collection<String> deserializedCollection = stringCollectionDeserializer.deserialize(jsonParser, deserializationContext, result);

        // Assert the result
        assertEquals(2, deserializedCollection.size());
        assertTrue(deserializedCollection.contains("string1"));
        assertTrue(deserializedCollection.contains("string2"));
    }

    @Test
    public void testDeserialize_DefaultDeserializer_EmptyInput() throws IOException {
        // Create a mock Collection<String>
        Collection<String> result = new ArrayList<>();
        
        // Setup mock behavior
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(false);

        // Call the method under test
        Collection<String> deserializedCollection = stringCollectionDeserializer.deserialize(jsonParser, deserializationContext, result);

        // Assert the result
        assertEquals(0, deserializedCollection.size());
    }
    
    @Test
    public void testDeserialize_WithCustomDeserializer_ValidInput() throws IOException {
        // Create a mock Collection<String>
        Collection<String> result = new ArrayList<>();
        
        // Create a mock JsonDeserializer<String>
        JsonDeserializer<String> customDeserializer = mock(JsonDeserializer.class);
        
        // Setup mock behavior
        when(jsonParser.nextTextValue()).thenReturn("string1", "string2", null);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.VALUE_STRING,
                JsonToken.VALUE_NULL);
        when(customDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn("customString");

        // Call the method under test
        Collection<String> deserializedCollection = stringCollectionDeserializer.deserializeUsingCustom(jsonParser, deserializationContext, result, customDeserializer);

        // Assert the result
        assertEquals(3, deserializedCollection.size());
        assertTrue(deserializedCollection.contains("string1"));
        assertTrue(deserializedCollection.contains("string2"));
        assertTrue(deserializedCollection.contains("customString"));
    }
}