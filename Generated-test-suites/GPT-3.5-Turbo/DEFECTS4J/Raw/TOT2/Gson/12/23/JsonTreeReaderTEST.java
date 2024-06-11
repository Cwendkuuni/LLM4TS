import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.junit.Test;

public class JsonTreeReaderTest {

    @Test
    public void testBeginArray() {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add("element1");
        jsonArray.add("element2");
        
        JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonArray);
        
        try {
            jsonTreeReader.beginArray();
            assertEquals(jsonTreeReader.nextString(), "element1");
            assertEquals(jsonTreeReader.nextString(), "element2");
            assertFalse(jsonTreeReader.hasNext());
        } catch (Exception e) {
            fail("Exception thrown while testing beginArray: " + e.getMessage());
        }
    }

    @Test
    public void testEndArray() {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add("element1");
        jsonArray.add("element2");
        
        JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonArray);
        
        try {
            jsonTreeReader.beginArray();
            jsonTreeReader.endArray();
            assertFalse(jsonTreeReader.hasNext());
        } catch (Exception e) {
            fail("Exception thrown while testing endArray: " + e.getMessage());
        }
    }

    // Add more test cases for other methods as needed

}