
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsonTreeReaderTest {

    @Test
    public void testBeginArray() {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(new JsonPrimitive("value1"));
        jsonArray.add(new JsonPrimitive("value2"));

        JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonArray);
        try {
            jsonTreeReader.beginArray();
            assertTrue(jsonTreeReader.hasNext());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testEndArray() {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(new JsonPrimitive("value1"));

        JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonArray);
        try {
            jsonTreeReader.beginArray();
            jsonTreeReader.endArray();
            assertFalse(jsonTreeReader.hasNext());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    // Add more test cases for other methods

}

