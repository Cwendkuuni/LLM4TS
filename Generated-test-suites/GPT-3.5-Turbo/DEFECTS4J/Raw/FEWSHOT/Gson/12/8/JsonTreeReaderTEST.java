import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonToken;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;
import org.junit.Test;

public class JsonTreeReaderTest {
    @Test
    public void testBeginArray() throws IOException {
        JsonArray jsonArray = new JsonArray();
        JsonTreeReader reader = new JsonTreeReader(jsonArray);
      
        reader.beginArray();
      
        assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
    }

    @Test
    public void testEndArray() throws IOException {
        JsonArray jsonArray = new JsonArray();
        JsonTreeReader reader = new JsonTreeReader(jsonArray);
      
        reader.endArray();
      
        assertEquals(JsonToken.END_ARRAY, reader.peek());
    }
  
    @Test
    public void testBeginObject() throws IOException {
        JsonObject jsonObject = new JsonObject();
        JsonTreeReader reader = new JsonTreeReader(jsonObject);
      
        reader.beginObject();
      
        assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
    }

    @Test
    public void testEndObject() throws IOException {
        JsonObject jsonObject = new JsonObject();
        JsonTreeReader reader = new JsonTreeReader(jsonObject);
      
        reader.endObject();
      
        assertEquals(JsonToken.END_OBJECT, reader.peek());
    }
  
    @Test
    public void testHasNext() throws IOException {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(new JsonPrimitive("foo"));
        JsonTreeReader reader = new JsonTreeReader(jsonArray);
      
        reader.beginArray();
      
        assertTrue(reader.hasNext());
        reader.nextString();
        assertFalse(reader.hasNext());
        reader.endArray();
    }
  
    @Test
    public void testNextName() throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("foo", "bar");
        JsonTreeReader reader = new JsonTreeReader(jsonObject);
      
        reader.beginObject();
      
        assertEquals("foo", reader.nextName());
        assertEquals("bar", reader.nextString());
        reader.endObject();
    }

    @Test
    public void testNextString() throws IOException {
        JsonPrimitive jsonPrimitive = new JsonPrimitive("foo");
        JsonTreeReader reader = new JsonTreeReader(jsonPrimitive);

        assertEquals("foo", reader.nextString());
        assertEquals(JsonToken.END_DOCUMENT, reader.peek());
    }

    @Test
    public void testNextBoolean() throws IOException {
        JsonPrimitive jsonPrimitive = new JsonPrimitive(true);
        JsonTreeReader reader = new JsonTreeReader(jsonPrimitive);

        assertTrue(reader.nextBoolean());
        assertEquals(JsonToken.END_DOCUMENT, reader.peek());
    }

    @Test
    public void testNextNull() throws IOException {
        JsonNull jsonNull = JsonNull.INSTANCE;
        JsonTreeReader reader = new JsonTreeReader(jsonNull);

        reader.nextNull();
        assertEquals(JsonToken.END_DOCUMENT, reader.peek());
    }
  
    @Test
    public void testNextDouble() throws IOException {
        JsonPrimitive jsonPrimitive = new JsonPrimitive(1.234);
        JsonTreeReader reader = new JsonTreeReader(jsonPrimitive);

        assertEquals(1.234, reader.nextDouble(), 0.001);
        assertEquals(JsonToken.END_DOCUMENT, reader.peek());
    }

    @Test
    public void testNextLong() throws IOException {
        JsonPrimitive jsonPrimitive = new JsonPrimitive(1234L);
        JsonTreeReader reader = new JsonTreeReader(jsonPrimitive);

        assertEquals(1234L, reader.nextLong());
        assertEquals(JsonToken.END_DOCUMENT, reader.peek());
    }

    @Test
    public void testNextInt() throws IOException {
        JsonPrimitive jsonPrimitive = new JsonPrimitive(1234);
        JsonTreeReader reader = new JsonTreeReader(jsonPrimitive);

        assertEquals(1234, reader.nextInt());
        assertEquals(JsonToken.END_DOCUMENT, reader.peek());
    }

    @Test
    public void testClose() throws IOException {
        JsonPrimitive jsonPrimitive = new JsonPrimitive("foo");
        JsonTreeReader reader = new JsonTreeReader(jsonPrimitive);

        reader.close();
        assertEquals(JsonToken.END_DOCUMENT, reader.peek());
    }

    @Test
    public void testSkipValue() throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("foo", "bar");

        JsonTreeReader reader = new JsonTreeReader(jsonObject);

        reader.beginObject();
        reader.skipValue();
        reader.endObject();

        assertEquals(JsonToken.END_DOCUMENT, reader.peek());
    }
  
    @Test
    public void testPromoteNameToValue() throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("foo", "bar");

        JsonTreeReader reader = new JsonTreeReader(jsonObject);

        reader.beginObject();
        reader.promoteNameToValue();

        assertEquals(JsonToken.STRING, reader.peek());
        assertEquals("foo", reader.nextString());

        assertEquals(JsonToken.STRING, reader.peek());
        assertEquals("bar", reader.nextString());
        reader.endObject();

        assertEquals(JsonToken.END_DOCUMENT, reader.peek());
    }

    @Test
    public void testGetPath() throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("foo", "bar");

        JsonTreeReader reader = new JsonTreeReader(jsonObject);

        reader.beginObject();
        reader.nextName();
        reader.promoteNameToValue();

        assertEquals("$.foo", reader.getPath());

        reader.nextString();
        reader.endObject();

        assertEquals(JsonToken.END_DOCUMENT, reader.peek());
    }
}