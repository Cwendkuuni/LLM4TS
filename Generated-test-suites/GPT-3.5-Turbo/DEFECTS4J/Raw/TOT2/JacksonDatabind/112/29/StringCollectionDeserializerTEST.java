import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class StringCollectionDeserializerTest {

    @Test
    public void testDeserialize() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null);
        List<String> result = new ArrayList<>();
        // Typical case
        deserializer.deserialize(createJsonParser("[\"abc\",\"def\"]"), null, result);
        assertEquals(2, result.size());
        assertEquals("abc", result.get(0));
        assertEquals("def", result.get(1));
        // Edge case: empty array
        result.clear();
        deserializer.deserialize(createJsonParser("[]"), null, result);
        assertTrue(result.isEmpty());
        // Error scenario: invalid input
        result.clear();
        boolean exceptionThrown = false;
        try {
            deserializer.deserialize(createJsonParser("[\"ghi\""), null, result);
        } catch (IOException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null);
        // Typical case
        Object result = deserializer.deserializeWithType(createJsonParser("[\"abc\",\"def\"]"), null, null);
        assertNotNull(result);
        assertTrue(result instanceof List);
        List<String> resultList = (List<String>) result;
        assertEquals(2, resultList.size());
        assertEquals("abc", resultList.get(0));
        assertEquals("def", resultList.get(1));
    }

    // Helper method to create a JsonParser for testing
    private JsonParser createJsonParser(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.getFactory().createParser(json);
    }
}