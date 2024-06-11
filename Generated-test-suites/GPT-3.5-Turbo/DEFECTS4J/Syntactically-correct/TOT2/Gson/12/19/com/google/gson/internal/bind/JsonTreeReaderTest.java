import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class JsonTreeReaderTest {

    private JsonElement element;

    @Before
    public void setUp() {
        element = new JsonPrimitive("test");
    }

    @Test
    public void testBeginArray() throws IOException {
        JsonTreeReader jsonTreeReader = new JsonTreeReader(element);
        jsonTreeReader.beginArray();
        assertFalse(jsonTreeReader.hasNext());
    }

    @Test
    public void testBeginObject() throws IOException {
        JsonTreeReader jsonTreeReader = new JsonTreeReader(element);
        jsonTreeReader.beginObject();
        assertFalse(jsonTreeReader.hasNext());
    }

    @Test
    public void testNextName() throws IOException {
        JsonTreeReader jsonTreeReader = new JsonTreeReader(element);
        jsonTreeReader.promoteNameToValue();
        assertEquals("test", jsonTreeReader.nextName());
    }

    @Test
    public void testNextString() throws IOException {
        JsonTreeReader jsonTreeReader = new JsonTreeReader(element);
        jsonTreeReader.promoteNameToValue();
        assertEquals("test", jsonTreeReader.nextString());
    }

    @Test
    public void testNextBoolean() throws IOException {
        JsonTreeReader jsonTreeReader = new JsonTreeReader(new JsonPrimitive(true));
        assertEquals(true, jsonTreeReader.nextBoolean());
    }

    @Test
    public void testNextNull() throws IOException {
        JsonTreeReader jsonTreeReader = new JsonTreeReader(JsonNull.INSTANCE);
        jsonTreeReader.nextNull();
        assertFalse(jsonTreeReader.hasNext());
    }

    @Test
    public void testNextDouble() throws IOException {
        JsonTreeReader jsonTreeReader = new JsonTreeReader(new JsonPrimitive(3.14));
        assertEquals(3.14, jsonTreeReader.nextDouble(), 0.001);
    }

    @Test
    public void testNextLong() throws IOException {
        JsonTreeReader jsonTreeReader = new JsonTreeReader(new JsonPrimitive(1000));
        assertEquals(1000L, jsonTreeReader.nextLong());
    }

    @Test
    public void testNextInt() throws IOException {
        JsonTreeReader jsonTreeReader = new JsonTreeReader(new JsonPrimitive(42));
        assertEquals(42, jsonTreeReader.nextInt());
    }

    @Test
    public void testClose() throws IOException {
        JsonTreeReader jsonTreeReader = new JsonTreeReader(element);
        jsonTreeReader.close();
        assertFalse(jsonTreeReader.hasNext());
    }

    @Test
    public void testSkipValue() throws IOException {
        JsonTreeReader jsonTreeReader = new JsonTreeReader(new JsonPrimitive("value"));
        jsonTreeReader.skipValue();
        assertFalse(jsonTreeReader.hasNext());
    }

    @Test
    public void testPeek() throws IOException {
        JsonTreeReader jsonTreeReader = new JsonTreeReader(element);
        assertEquals(JsonToken.STRING, jsonTreeReader.peek());
    }

    @Test
    public void testHasNext() throws IOException {
        JsonTreeReader jsonTreeReader = new JsonTreeReader(element);
        assertTrue(jsonTreeReader.hasNext());
    }

    @Test
    public void testGetPath() throws IOException {
        JsonTreeReader jsonTreeReader = new JsonTreeReader(element);
        assertEquals("$", jsonTreeReader.getPath());
    }
}