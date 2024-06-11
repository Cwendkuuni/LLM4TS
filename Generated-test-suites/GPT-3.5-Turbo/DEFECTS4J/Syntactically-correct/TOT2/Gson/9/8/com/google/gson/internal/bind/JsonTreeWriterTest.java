import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class JsonTreeWriterTest {

    @Test
    public void testGet() {
        JsonTreeWriter writer = new JsonTreeWriter();
        JsonElement element = writer.get();
        assertEquals(JsonNull.INSTANCE, element);
    }

    @Test
    public void testBeginArray() {
        JsonTreeWriter writer = new JsonTreeWriter();
        try {
            writer.beginArray();
            assertTrue(writer.peek() instanceof JsonArray);
        } catch (IOException e) {
        	fail("Exception thrown when it shouldn't be");
        }
    }
    
    // more test methods for other public methods in JsonTreeWriter class

}