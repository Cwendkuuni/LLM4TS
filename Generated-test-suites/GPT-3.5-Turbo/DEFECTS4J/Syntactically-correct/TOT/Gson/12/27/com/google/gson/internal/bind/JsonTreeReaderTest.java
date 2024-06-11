import static org.junit.Assert.*;
import java.io.IOException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class JsonTreeReaderTest {
    
    private JsonTreeReader jsonTreeReader;
    
    @Before
    public void setUp() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("name", new JsonPrimitive("John Doe"));
        jsonObject.add("age", new JsonPrimitive(30));
        jsonObject.add("email", new JsonPrimitive("johndoe@example.com"));
        
        jsonTreeReader = new JsonTreeReader(jsonObject);
    }
    
    @After
    public void tearDown() {
        jsonTreeReader.close();
    }
    
    @Test
    public void testNextName() throws IOException {
        assertEquals("name", jsonTreeReader.nextName());
    }
    
    @Test
    public void testNextString() throws IOException {
        jsonTreeReader.nextName();
        assertEquals("John Doe", jsonTreeReader.nextString());
    }
    
    @Test
    public void testNextBoolean() throws IOException {
        try {
            jsonTreeReader.nextBoolean();
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            // Test passed
        }
    }
    
    @Test
    public void testNextNull() throws IOException {
        try {
            jsonTreeReader.nextNull();
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            // Test passed
        }
    }
    
    @Test
    public void testNextDouble() throws IOException {
        try {
            jsonTreeReader.nextDouble();
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            // Test passed
        }
    }
    
    @Test
    public void testNextLong() throws IOException {
        try {
            jsonTreeReader.nextLong();
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            // Test passed
        }
    }
    
    @Test
    public void testNextInt() throws IOException {
        try {
            jsonTreeReader.nextInt();
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            // Test passed
        }
    }
    
    @Test
    public void testClose() throws IOException {
        jsonTreeReader.close();
        try {
            jsonTreeReader.nextName();
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            // Test passed
        }
    }
    
    @Test
    public void testSkipValue() throws IOException {
        jsonTreeReader.nextName();
        jsonTreeReader.skipValue();
        assertEquals("age", jsonTreeReader.nextName());
    }
    
    @Test
    public void testPromoteNameToValue() throws IOException {
        jsonTreeReader.promoteNameToValue();
        assertEquals("name", jsonTreeReader.nextString());
    }
    
    @Test
    public void testPeek() throws IOException {
        assertEquals(JsonToken.NAME, jsonTreeReader.peek());
        jsonTreeReader.nextName();
        assertEquals(JsonToken.STRING, jsonTreeReader.peek());
    }
    
    @Test
    public void testHasNext() throws IOException {
        assertTrue(jsonTreeReader.hasNext());
        jsonTreeReader.skipValue();
        assertTrue(jsonTreeReader.hasNext());
        jsonTreeReader.skipValue();
        assertTrue(jsonTreeReader.hasNext());
        jsonTreeReader.skipValue();
        assertFalse(jsonTreeReader.hasNext());
    }
    
    @Test
    public void testBeginArray() throws IOException {
        try {
            jsonTreeReader.beginArray();
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            // Test passed
        }
    }
    
    @Test
    public void testEndArray() throws IOException {
        try {
            jsonTreeReader.endArray();
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            // Test passed
        }
    }
    
    @Test
    public void testBeginObject() throws IOException {
        try {
            jsonTreeReader.beginObject();
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            // Test passed
        }
    }
    
    @Test
    public void testEndObject() throws IOException {
        try {
            jsonTreeReader.endObject();
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            // Test passed
        }
    }
    
    @Test
    public void testToString() {
        assertEquals("JsonTreeReader", jsonTreeReader.toString());
    }
    
    @Test
    public void testGetPath() {
        assertEquals("$", jsonTreeReader.getPath());
    }
}