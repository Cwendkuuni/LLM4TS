import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

public class JsonTreeReaderTest {

    private JsonTreeReader reader;

    @BeforeEach
    public void setup() {
        JsonElement element = new JsonObject();
        reader = new JsonTreeReader(element);
    }

    @Test
    public void testBeginArray() throws Exception {
        reader.beginArray();
        JsonToken token = reader.peek();
        Assertions.assertEquals(JsonToken.BEGIN_ARRAY, token);
    }

    @Test
    public void testEndArray() throws Exception {
        reader.endArray();
        JsonToken token = reader.peek();
        Assertions.assertEquals(JsonToken.END_ARRAY, token);
    }

    @Test
    public void testBeginObject() throws Exception {
        reader.beginObject();
        JsonToken token = reader.peek();
        Assertions.assertEquals(JsonToken.BEGIN_OBJECT, token);
    }

    @Test
    public void testEndObject() throws Exception {
        reader.endObject();
        JsonToken token = reader.peek();
        Assertions.assertEquals(JsonToken.END_OBJECT, token);
    }

    @Test
    public void testHasNext() throws Exception {
        boolean hasNext = reader.hasNext();
        Assertions.assertFalse(hasNext);
    }

    @Test
    public void testPeek() throws Exception {
        JsonToken token = reader.peek();
        Assertions.assertEquals(JsonToken.END_DOCUMENT, token);
    }

    @Test
    public void testNextName() throws Exception {
        String name = reader.nextName();
        Assertions.assertNull(name);
    }

    @Test
    public void testNextString() throws Exception {
        Assertions.assertThrows(IllegalStateException.class, () -> {
            reader.nextString();
        });
    }

    @Test
    public void testNextBoolean() throws Exception {
        Assertions.assertThrows(IllegalStateException.class, () -> {
            reader.nextBoolean();
        });
    }

    @Test
    public void testNextNull() throws Exception {
        Assertions.assertThrows(IllegalStateException.class, () -> {
            reader.nextNull();
        });
    }

    @Test
    public void testNextDouble() throws Exception {
        Assertions.assertThrows(IllegalStateException.class, () -> {
            reader.nextDouble();
        });
    }

    @Test
    public void testNextLong() throws Exception {
        Assertions.assertThrows(IllegalStateException.class, () -> {
            reader.nextLong();
        });
    }

    @Test
    public void testNextInt() throws Exception {
        Assertions.assertThrows(IllegalStateException.class, () -> {
            reader.nextInt();
        });
    }

    @Test
    public void testClose() throws Exception {
        reader.close();
        JsonToken token = reader.peek();
        Assertions.assertEquals(JsonToken.END_DOCUMENT, token);
    }

    @Test
    public void testSkipValue() throws Exception {
        reader.skipValue();
        JsonToken token = reader.peek();
        Assertions.assertNull(token);
    }

    @Test
    public void testPromoteNameToValue() throws Exception {
        Assertions.assertThrows(IllegalStateException.class, () -> {
            reader.promoteNameToValue();
        });
    }
}