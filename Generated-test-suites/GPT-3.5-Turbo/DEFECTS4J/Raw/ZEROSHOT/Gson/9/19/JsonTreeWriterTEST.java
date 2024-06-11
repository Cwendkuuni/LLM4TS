import org.junit.Test;

import static org.junit.Assert.*;

public class JsonTreeWriterTest {

    @Test
    public void testGet() {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        JsonElement jsonElement = jsonTreeWriter.get();
        assertNotNull(jsonElement);
        assertTrue(jsonElement instanceof JsonNull);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetNotEmpty() throws IOException {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        jsonTreeWriter.beginObject();
        jsonTreeWriter.get();
    }

    @Test
    public void testPeek() throws IOException {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        jsonTreeWriter.beginObject();
        JsonElement jsonElement = jsonTreeWriter.peek();
        assertNotNull(jsonElement);
        assertTrue(jsonElement instanceof JsonObject);
    }

    @Test(expected = IllegalStateException.class)
    public void testPeekEmptyStack() throws IOException {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        jsonTreeWriter.peek();
    }

    @Test(expected = IllegalStateException.class)
    public void testPutWithPendingName() throws IOException {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        jsonTreeWriter.beginObject();
        jsonTreeWriter.name("name");
        jsonTreeWriter.put(JsonNull.INSTANCE);
    }

    @Test
    public void testPutWithJsonNull() throws IOException {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        jsonTreeWriter.put(JsonNull.INSTANCE);
        JsonElement jsonElement = jsonTreeWriter.peek();
        assertNotNull(jsonElement);
        assertTrue(jsonElement instanceof JsonNull);
    }

    @Test
    public void testPutWithJsonArray() throws IOException {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        jsonTreeWriter.beginArray();
        jsonTreeWriter.put(JsonNull.INSTANCE);
        jsonTreeWriter.endArray();
        JsonElement jsonElement = jsonTreeWriter.peek();
        assertNotNull(jsonElement);
        assertTrue(jsonElement instanceof JsonArray);
    }

    @Test(expected = IllegalStateException.class)
    public void testPutWithNonJsonArray() throws IOException {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        jsonTreeWriter.beginObject();
        jsonTreeWriter.put(JsonNull.INSTANCE);
    }

    @Test
    public void testBeginArray() throws IOException {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        JsonWriter jsonWriter = jsonTreeWriter.beginArray();
        assertNotNull(jsonWriter);
        JsonElement jsonElement = jsonTreeWriter.peek();
        assertNotNull(jsonElement);
        assertTrue(jsonElement instanceof JsonArray);
    }

    @Test
    public void testEndArray() throws IOException {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        jsonTreeWriter.beginArray();
        JsonWriter jsonWriter = jsonTreeWriter.endArray();
        assertNotNull(jsonWriter);
        JsonElement jsonElement = jsonTreeWriter.peek();
        assertNull(jsonElement);
    }

    @Test(expected = IllegalStateException.class)
    public void testEndArrayWithEmptyStack() throws IOException {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        jsonTreeWriter.endArray();
    }

    @Test(expected = IllegalStateException.class)
    public void testEndArrayWithPendingName() throws IOException {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        jsonTreeWriter.beginObject();
        jsonTreeWriter.name("name");
        jsonTreeWriter.endArray();
    }

    @Test
    public void testBeginObject() throws IOException {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        JsonWriter jsonWriter = jsonTreeWriter.beginObject();
        assertNotNull(jsonWriter);
        JsonElement jsonElement = jsonTreeWriter.peek();
        assertNotNull(jsonElement);
        assertTrue(jsonElement instanceof JsonObject);
    }

    @Test
    public void testEndObject() throws IOException {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        jsonTreeWriter.beginObject();
        JsonWriter jsonWriter = jsonTreeWriter.endObject();
        assertNotNull(jsonWriter);
        JsonElement jsonElement = jsonTreeWriter.peek();
        assertNull(jsonElement);
    }

    @Test(expected = IllegalStateException.class)
    public void testEndObjectWithEmptyStack() throws IOException {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        jsonTreeWriter.endObject();
    }

    @Test(expected = IllegalStateException.class)
    public void testEndObjectWithPendingName() throws IOException {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        jsonTreeWriter.beginObject();
        jsonTreeWriter.name("name");
        jsonTreeWriter.endObject();
    }

    @Test
    public void testName() throws IOException {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        jsonTreeWriter.beginObject();
        JsonWriter jsonWriter = jsonTreeWriter.name("name");
        assertNotNull(jsonWriter);
    }

    @Test(expected = IllegalStateException.class)
    public void testNameWithEmptyStack() throws IOException {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        jsonTreeWriter.name("name");
    }

    @Test(expected = IllegalStateException.class)
    public void testNameWithPendingName() throws IOException {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        jsonTreeWriter.beginObject();
        jsonTreeWriter.name("name1");
        jsonTreeWriter.name("name2");
    }

    @Test
    public void testValueWithNull() throws IOException {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        JsonWriter jsonWriter = jsonTreeWriter.value((String) null);
        assertNotNull(jsonWriter);
        JsonElement jsonElement = jsonTreeWriter.peek();
        assertNotNull(jsonElement);
        assertTrue(jsonElement instanceof JsonNull);
    }

    @Test
    public void testValueWithNonNull() throws IOException {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        JsonWriter jsonWriter = jsonTreeWriter.value("value");
        assertNotNull(jsonWriter);
        JsonElement jsonElement = jsonTreeWriter.peek();
        assertNotNull(jsonElement);
        assertTrue(jsonElement instanceof JsonPrimitive);
    }

    @Test(expected = IllegalStateException.class)
    public void testValueWithPendingName() throws IOException {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        jsonTreeWriter.beginObject();
        jsonTreeWriter.name("name");
        jsonTreeWriter.value("value");
    }

    @Test
    public void testNullValue() throws IOException {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        JsonWriter jsonWriter = jsonTreeWriter.nullValue();
        assertNotNull(jsonWriter);
        JsonElement jsonElement = jsonTreeWriter.peek();
        assertNotNull(jsonElement);
        assertTrue(jsonElement instanceof JsonNull);
    }

    @Test
    public void testValueWithBoolean() throws IOException {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        JsonWriter jsonWriter = jsonTreeWriter.value(true);
        assertNotNull(jsonWriter);
        JsonElement jsonElement = jsonTreeWriter.peek();
        assertNotNull(jsonElement);
        assertTrue(jsonElement instanceof JsonPrimitive);
    }

    @Test
    public void testValueWithDouble() throws IOException {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        JsonWriter jsonWriter = jsonTreeWriter.value(1.23);
        assertNotNull(jsonWriter);
        JsonElement jsonElement = jsonTreeWriter.peek();
        assertNotNull(jsonElement);
        assertTrue(jsonElement instanceof JsonPrimitive);
    }

    @Test
    public void testValueWithLong() throws IOException {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        JsonWriter jsonWriter = jsonTreeWriter.value(123L);
        assertNotNull(jsonWriter);
        JsonElement jsonElement = jsonTreeWriter.peek();
        assertNotNull(jsonElement);
        assertTrue(jsonElement instanceof JsonPrimitive);
    }

    @Test
    public void testValueWithNumber() throws IOException {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        JsonWriter jsonWriter = jsonTreeWriter.value(123);
        assertNotNull(jsonWriter);
        JsonElement jsonElement = jsonTreeWriter.peek();
        assertNotNull(jsonElement);
        assertTrue(jsonElement instanceof JsonPrimitive);
    }

    @Test
    public void testFlush() throws IOException {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        jsonTreeWriter.flush();
    }

    @Test(expected = IOException.class)
    public void testCloseWithNonEmptyStack() throws IOException {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        jsonTreeWriter.beginObject();
        jsonTreeWriter.close();
    }

    @Test
    public void testClose() throws IOException {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        jsonTreeWriter.close();
        JsonElement jsonElement = jsonTreeWriter.peek();
        assertNotNull(jsonElement);
        assertTrue(jsonElement instanceof JsonPrimitive);
    }
}