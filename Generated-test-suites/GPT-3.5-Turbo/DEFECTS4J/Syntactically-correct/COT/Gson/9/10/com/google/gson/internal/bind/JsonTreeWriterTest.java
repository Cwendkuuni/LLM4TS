
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class JsonTreeWriterTest {
    private JsonTreeWriter jsonTreeWriter;

    @Before
    public void setUp() {
        jsonTreeWriter = new JsonTreeWriter();
    }

    @Test
    public void testValueWithNullString() {
        assertNull(jsonTreeWriter.value(null));
        assertEquals(JsonNull.INSTANCE, jsonTreeWriter.get());
    }

    @Test
    public void testValueWithString() {
        assertEquals(jsonTreeWriter, jsonTreeWriter.value("hello"));
        assertEquals(new JsonPrimitive("hello"), jsonTreeWriter.get());
    }

    @Test
    public void testValueWithBoolean() {
        assertEquals(jsonTreeWriter, jsonTreeWriter.value(true));
        assertEquals(new JsonPrimitive(true), jsonTreeWriter.get());
    }

    @Test
    public void testValueWithDouble() {
        assertEquals(jsonTreeWriter, jsonTreeWriter.value(3.14));
        assertEquals(new JsonPrimitive(3.14), jsonTreeWriter.get());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueWithInvalidDouble() {
        jsonTreeWriter.value(Double.NaN);
    }

    @Test
    public void testValueWithLong() {
        assertEquals(jsonTreeWriter, jsonTreeWriter.value(100L));
        assertEquals(new JsonPrimitive(100L), jsonTreeWriter.get());
    }

    @Test
    public void testValueWithNumber() {
        assertEquals(jsonTreeWriter, jsonTreeWriter.value(999));
        assertEquals(new JsonPrimitive(999), jsonTreeWriter.get());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueWithInvalidNumber() {
        jsonTreeWriter.value(null);
    }

    @Test
    public void testBeginArray() {
        assertEquals(jsonTreeWriter, jsonTreeWriter.beginArray());
        assertTrue(jsonTreeWriter.get() instanceof JsonArray);
    }

    @Test
    public void testEndArray() {
        jsonTreeWriter.beginArray();
        assertEquals(jsonTreeWriter, jsonTreeWriter.endArray());
        assertTrue(jsonTreeWriter.get() instanceof JsonNull);
    }

    @Test(expected = IllegalStateException.class)
    public void testEndArrayWithoutBeginArray() {
        jsonTreeWriter.endArray();
    }

    @Test
    public void testBeginObject() {
        assertEquals(jsonTreeWriter, jsonTreeWriter.beginObject());
        assertTrue(jsonTreeWriter.get() instanceof JsonObject);
    }

    @Test
    public void testEndObject() {
        jsonTreeWriter.beginObject();
        assertEquals(jsonTreeWriter, jsonTreeWriter.endObject());
        assertTrue(jsonTreeWriter.get() instanceof JsonNull);
    }

    @Test(expected = IllegalStateException.class)
    public void testEndObjectWithoutBeginObject() {
        jsonTreeWriter.endObject();
    }

    @Test
    public void testName() {
        jsonTreeWriter.beginObject();
        assertEquals(jsonTreeWriter, jsonTreeWriter.name("key"));
        JsonObject object = (JsonObject) jsonTreeWriter.get();
        assertTrue(object.has("key"));
    }

    @Test(expected = IllegalStateException.class)
    public void testNameWithoutBeginObject() {
        jsonTreeWriter.name("key");
    }

    @Test(expected = IllegalStateException.class)
    public void testNameWithoutName() {
        jsonTreeWriter.beginObject();
        jsonTreeWriter.name("key").name("key");
    }

    @Test
    public void testNullValue() {
        assertEquals(jsonTreeWriter, jsonTreeWriter.nullValue());
        assertEquals(JsonNull.INSTANCE, jsonTreeWriter.get());
    }

    @Test
    public void testFlush() {
        // Test if the flush method does not throw any exception
        try {
            jsonTreeWriter.flush();
        } catch (IOException e) {
            fail("Exception occurred");
        }
    }

    @Test(expected = IOException.class)
    public void testCloseWithIncompleteDocument() throws IOException {
        jsonTreeWriter.beginObject();
        jsonTreeWriter.close();
    }

    @Test(expected = AssertionError.class)
    public void testUnwritableWriterWrite() throws IOException {
        Writer unwritableWriter = JsonTreeWriter.UNWRITABLE_WRITER;
        unwritableWriter.write(null, 0, 0);
    }

    @Test(expected = AssertionError.class)
    public void testUnwritableWriterFlush() throws IOException {
        Writer unwritableWriter = JsonTreeWriter.UNWRITABLE_WRITER;
        unwritableWriter.flush();
    }

    @Test(expected = AssertionError.class)
    public void testUnwritableWriterClose() throws IOException {
        Writer unwritableWriter = JsonTreeWriter.UNWRITABLE_WRITER;
        unwritableWriter.close();
    }
}
