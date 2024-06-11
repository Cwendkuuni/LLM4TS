import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class JsonTreeReaderTest {

    private JsonTreeReader jsonTreeReader;

    @Before
    public void setUp() {
        // Initialize test data
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(new JsonPrimitive("value1"));
        jsonArray.add(new JsonPrimitive("value2"));
        jsonArray.add(new JsonPrimitive("value3"));

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("key1", "value1");
        jsonObject.addProperty("key2", "value2");
        jsonObject.addProperty("key3", "value3");

        jsonTreeReader = new JsonTreeReader(jsonArray);

        // Push additional elements to the stack for testing
        jsonTreeReader.push(jsonArray);
        jsonTreeReader.push(jsonObject);
        jsonTreeReader.push(new JsonPrimitive("value1"));
    }

    @Test
    public void testBeginArray() throws IOException {
        jsonTreeReader.beginArray();

        assertTrue(jsonTreeReader.peek().equals(JsonToken.BEGIN_ARRAY));
        assertEquals(0, jsonTreeReader.getPathIndices()[jsonTreeReader.getStackSize() - 1]);
    }

    @Test(expected = IllegalStateException.class)
    public void testBeginArrayInvalid() throws IOException {
        jsonTreeReader.endArray();

        jsonTreeReader.beginArray();
    }

    @Test
    public void testEndArray() throws IOException {
        jsonTreeReader.endArray();

        assertTrue(jsonTreeReader.peek().equals(JsonToken.END_ARRAY));
        assertEquals(1, jsonTreeReader.getPathIndices()[jsonTreeReader.getStackSize() - 1]);
    }

    @Test(expected = IllegalStateException.class)
    public void testEndArrayInvalid() throws IOException {
        jsonTreeReader.endObject();

        jsonTreeReader.endArray();
    }

    // Add test cases for other methods following the same pattern as above

}



// Test cases go here

