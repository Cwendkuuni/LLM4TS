import com.google.gson.internal.bind.JsonTreeReader;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.*;

public class JsonTreeReaderTest {

    private JsonTreeReader jsonTreeReader;

    @Before
    public void setUp() {
        jsonTreeReader = new JsonTreeReader(null);
    }

    @Test
    public void beginArrayTest() throws IOException {
        jsonTreeReader.beginArray();
        assertEquals(JsonToken.BEGIN_ARRAY, jsonTreeReader.peek());
    }

    @Test
    public void endArrayTest() throws IOException {
        jsonTreeReader.endArray();
        assertEquals(JsonToken.END_ARRAY, jsonTreeReader.peek());
    }

    @Test
    public void beginObjectTest() throws IOException {
        jsonTreeReader.beginObject();
        assertEquals(JsonToken.BEGIN_OBJECT, jsonTreeReader.peek());
    }

    @Test
    public void endObjectTest() throws IOException {
        jsonTreeReader.endObject();
        assertEquals(JsonToken.END_OBJECT, jsonTreeReader.peek());
    }

    @Test
    public void hasNextTest() throws IOException {
        assertTrue(jsonTreeReader.hasNext());
    }

    @Test
    public void peekTest() throws IOException {
        assertEquals(JsonToken.END_DOCUMENT, jsonTreeReader.peek());
    }

    @Test
    public void nextNameTest() throws IOException {
        String name = jsonTreeReader.nextName();
        assertEquals("", name);
    }

    @Test
    public void nextStringTest() throws IOException {
        String result = jsonTreeReader.nextString();
        assertEquals(null, result);
    }

    @Test
    public void nextBooleanTest() throws IOException {
        boolean result = jsonTreeReader.nextBoolean();
        assertFalse(result);
    }

    @Test
    public void nextNullTest() throws IOException {
        jsonTreeReader.nextNull();
    }

    @Test
    public void nextDoubleTest() throws IOException {
        double result = jsonTreeReader.nextDouble();
        assertEquals(0.0, result, 0.0001);
    }

    @Test
    public void nextLongTest() throws IOException {
        long result = jsonTreeReader.nextLong();
        assertEquals(0L, result);
    }

    @Test
    public void nextIntTest() throws IOException {
        int result = jsonTreeReader.nextInt();
        assertEquals(0, result);
    }

    @Test
    public void closeTest() throws IOException {
        jsonTreeReader.close();
        assertEquals(JsonToken.END_DOCUMENT, jsonTreeReader.peek());
    }

    @Test
    public void skipValueTest() throws IOException {
        jsonTreeReader.skipValue();
        assertEquals(JsonToken.NULL, jsonTreeReader.peek());
    }

    @Test
    public void toStringTest() {
        String result = jsonTreeReader.toString();
        assertEquals("JsonTreeReader", result);
    }
}