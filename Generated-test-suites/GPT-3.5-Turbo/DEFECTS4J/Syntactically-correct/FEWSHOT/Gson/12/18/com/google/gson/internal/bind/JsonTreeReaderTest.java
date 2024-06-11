import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonToken;

import org.junit.Before;
import org.junit.Test;

public class JsonTreeReaderTest {
    private JsonPrimitive primitive;
    private JsonNull jsonNull;
    private JsonObject jsonObject;
    private JsonArray jsonArray;

    @Before
    public void setup() {
        primitive = new JsonPrimitive("foo");
        jsonNull = JsonNull.INSTANCE;
        jsonObject = new JsonObject();
        jsonArray = new JsonArray();
    }

    @Test
    public void testPeek_Primitive() throws Exception {
        JsonTreeReader reader = new JsonTreeReader(primitive);

        assertEquals(JsonToken.STRING, reader.peek());
    }

    @Test
    public void testPeek_Null() throws Exception {
        JsonTreeReader reader = new JsonTreeReader(jsonNull);

        assertEquals(JsonToken.NULL, reader.peek());
    }

    @Test
    public void testPeek_Object() throws Exception {
        JsonTreeReader reader = new JsonTreeReader(jsonObject);

        assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
    }
    
    @Test
    public void testPeek_Array() throws Exception {
        JsonTreeReader reader = new JsonTreeReader(jsonArray);

        assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
    }

    @Test
    public void testPeek_EndDocument() throws Exception {
        JsonTreeReader reader = new JsonTreeReader(primitive);

        reader.nextString();
        assertEquals(JsonToken.END_DOCUMENT, reader.peek());
    }

    @Test
    public void testNextName() throws Exception {
        JsonObject obj = new JsonObject();
        obj.addProperty("name", "value");
        JsonTreeReader reader = new JsonTreeReader(obj);

        assertEquals("name", reader.nextName());
    }

    @Test
    public void testNextString() throws Exception {
        JsonTreeReader reader = new JsonTreeReader(primitive);

        assertEquals("foo", reader.nextString());
    }

    @Test
    public void testNextBoolean() throws Exception {
        JsonPrimitive boolPrimitive = new JsonPrimitive(true);
        JsonTreeReader reader = new JsonTreeReader(boolPrimitive);

        assertTrue(reader.nextBoolean());
    }

    @Test
    public void testNextNull() throws Exception {
        JsonTreeReader reader = new JsonTreeReader(jsonNull);

        reader.nextNull();

        assertEquals("null", reader.getPath());
    }

    @Test
    public void testNextDouble() throws Exception {
        JsonPrimitive doublePrimitive = new JsonPrimitive(1.23);
        JsonTreeReader reader = new JsonTreeReader(doublePrimitive);

        assertEquals(1.23, reader.nextDouble(), 0.01);
    }

    @Test
    public void testNextLong() throws Exception {
        JsonPrimitive longPrimitive = new JsonPrimitive(1000L);
        JsonTreeReader reader = new JsonTreeReader(longPrimitive);

        assertEquals(1000L, reader.nextLong());
    }

    @Test
    public void testNextInt() throws Exception {
        JsonPrimitive intPrimitive = new JsonPrimitive(123);
        JsonTreeReader reader = new JsonTreeReader(intPrimitive);

        assertEquals(123, reader.nextInt());
    }

    @Test
    public void testSkipValue_Name() throws Exception {
        JsonObject obj = new JsonObject();
        obj.addProperty("name", "value");
        JsonTreeReader reader = new JsonTreeReader(obj);

        reader.nextName();
        reader.skipValue();

        assertEquals("null", reader.getPath());
    }
    
    @Test
    public void testSkipValue_Value() throws Exception {
        JsonObject obj = new JsonObject();
        obj.addProperty("name", "value");
        JsonTreeReader reader = new JsonTreeReader(obj);

        reader.nextName();
        reader.skipValue();

        assertEquals("null", reader.getPath());
    }

    @Test
    public void testClose() throws Exception {
        JsonTreeReader reader = new JsonTreeReader(primitive);

        reader.close();

        assertEquals("{}", reader.getPath());
    }

    @Test
    public void testPromoteNameToValue() throws Exception {
        JsonObject obj = new JsonObject();
        obj.addProperty("name", "value");
        JsonTreeReader reader = new JsonTreeReader(obj);

        reader.nextName();
        reader.promoteNameToValue();

        assertEquals(JsonToken.NAME, reader.peek());
    }
    
    @Test
    public void testHasNext() throws Exception {
        JsonTreeReader reader = new JsonTreeReader(jsonArray);

        assertTrue(reader.hasNext());
    }
}