import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.junit.Test;

public class JsonTreeReaderTest {

    @Test
    public void testBeginArray() {
        // Test typical use case
        JsonElement element = JsonParser.parseString("[1,2,3]");
        JsonTreeReader reader = new JsonTreeReader(element);

        try {
            reader.beginArray();
            assertTrue(reader.hasNext());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testEndArray() {
        // Test typical use case
        JsonElement element = JsonParser.parseString("[1,2,3]");
        JsonTreeReader reader = new JsonTreeReader(element);

        try {
            reader.beginArray();
            reader.endArray();
            assertFalse(reader.hasNext());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    // Add more test cases for other methods

}