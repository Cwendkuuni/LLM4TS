import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class JsonTreeWriterTest {

    private JsonTreeWriter jsonTreeWriter;

    @Before
    public void setUp() {
        jsonTreeWriter = new JsonTreeWriter();
    }

    @Test
    public void testGet() {
        JsonElement result = jsonTreeWriter.get();
        assertEquals(JsonNull.INSTANCE, result);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetWithNonEmptyStack() {
        jsonTreeWriter.beginObject();
        jsonTreeWriter.get();
    }

    @Test
    public void testPeek() {
        JsonElement result = jsonTreeWriter.peek();
        assertNull(result);
    }

    @Test
    public void testPut() {
        JsonElement value = new JsonPrimitive("value");
        jsonTreeWriter.put(value);
        JsonObject object = new JsonObject();
        object.add("name", value);
        assertEquals(object, jsonTreeWriter.get());
    }

    @Test
    public void testBeginArray() throws IOException {
        JsonWriter result = jsonTreeWriter.beginArray();
        assertSame(jsonTreeWriter, result);
        JsonArray array = new JsonArray();
        array.add(JsonNull.INSTANCE);
        assertEquals(array, jsonTreeWriter.get());
    }

    @Test
    public void testEndArray() throws IOException {
        jsonTreeWriter.beginArray();
        JsonWriter result = jsonTreeWriter.endArray();
        assertSame(jsonTreeWriter, result);
        assertEquals(JsonNull.INSTANCE, jsonTreeWriter.get());
    }

    @Test(expected = IllegalStateException.class)
    public void testEndArrayWithEmptyStack() throws IOException {
        jsonTreeWriter.endArray();
    }

    @Test(expected = IllegalStateException.class)
    public void testEndArrayWithPendingName() throws IOException {
        jsonTreeWriter.beginObject();
        jsonTreeWriter.name("name");
        jsonTreeWriter.endArray();
    }

    @Test
    public void testBeginObject() throws IOException {
        JsonWriter result = jsonTreeWriter.beginObject();
        assertSame(jsonTreeWriter, result);
        JsonObject object = new JsonObject();
        object.add("name", JsonNull.INSTANCE);
        assertEquals(object, jsonTreeWriter.get());
    }

    @Test
    public void testEndObject() throws IOException {
        jsonTreeWriter.beginObject();
        JsonWriter result = jsonTreeWriter.endObject();
        assertSame(jsonTreeWriter, result);
        assertEquals(JsonNull.INSTANCE, jsonTreeWriter.get());
    }

    @Test(expected = IllegalStateException.class)
    public void testEndObjectWithEmptyStack() throws IOException {
        jsonTreeWriter.endObject();
    }

    @Test(expected = IllegalStateException.class)
    public void testEndObjectWithPendingName() throws IOException {
        jsonTreeWriter.beginObject();
        jsonTreeWriter.name("name");
        jsonTreeWriter.endObject();
    }

    @Test
    public void testName() throws IOException {
        JsonWriter result = jsonTreeWriter.name("name");
        assertSame(jsonTreeWriter, result);
        JsonObject object = new JsonObject();
        object.addProperty("name", "value");
        jsonTreeWriter.value("value");
        assertEquals(object, jsonTreeWriter.get());
    }

    @Test(expected = IllegalStateException.class)
    public void testNameWithEmptyStack() throws IOException {
        jsonTreeWriter.name("name");
    }

    @Test(expected = IllegalStateException.class)
    public void testNameWithPendingName() throws IOException {
        jsonTreeWriter.beginObject();
        jsonTreeWriter.name("name1");
        jsonTreeWriter.name("name2");
    }

    @Test
    public void testValueString() throws IOException {
        JsonWriter result = jsonTreeWriter.value("value");
        assertSame(jsonTreeWriter, result);
        JsonObject object = new JsonObject();
        object.addProperty("name", "value");
        jsonTreeWriter.name("name");
        assertEquals(object, jsonTreeWriter.get());
    }

    @Test
    public void testValueStringWithNull() throws IOException {
        JsonWriter result = jsonTreeWriter.value((String) null);
        assertSame(jsonTreeWriter, result);
        JsonObject object = new JsonObject();
        object.addProperty("name", (String) null);
        jsonTreeWriter.name("name");
        assertEquals(object, jsonTreeWriter.get());
    }

    @Test
    public void testNullValue() throws IOException {
        JsonWriter result = jsonTreeWriter.nullValue();
        assertSame(jsonTreeWriter, result);
        JsonObject object = new JsonObject();
        object.add("name", JsonNull.INSTANCE);
        jsonTreeWriter.name("name");
        assertEquals(object, jsonTreeWriter.get());
    }

    @Test
    public void testValueBoolean() throws IOException {
        JsonWriter result = jsonTreeWriter.value(true);
        assertSame(jsonTreeWriter, result);
        JsonObject object = new JsonObject();
        object.addProperty("name", true);
        jsonTreeWriter.name("name");
        assertEquals(object, jsonTreeWriter.get());
    }

    @Test
    public void testValueDouble() throws IOException {
        JsonWriter result = jsonTreeWriter.value(1.0);
        assertSame(jsonTreeWriter, result);
        JsonObject object = new JsonObject();
        object.addProperty("name", 1.0);
        jsonTreeWriter.name("name");
        assertEquals(object, jsonTreeWriter.get());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueDoubleWithLenientFalse() throws IOException {
        jsonTreeWriter.setLenient(false);
        jsonTreeWriter.value(Double.NaN);
    }

    @Test
    public void testValueLong() throws IOException {
        JsonWriter result = jsonTreeWriter.value(10L);
        assertSame(jsonTreeWriter, result);
        JsonObject object = new JsonObject();
        object.addProperty("name", 10L);
        jsonTreeWriter.name("name");
        assertEquals(object, jsonTreeWriter.get());
    }

    @Test
    public void testValueNumber() throws IOException {
        JsonWriter result = jsonTreeWriter.value(10);
        assertSame(jsonTreeWriter, result);
        JsonObject object = new JsonObject();
        object.addProperty("name", 10);
        jsonTreeWriter.name("name");
        assertEquals(object, jsonTreeWriter.get());
    }

    @Test
    public void testValueNumberWithNullValue() throws IOException {
        JsonWriter result = jsonTreeWriter.value((Number) null);
        assertSame(jsonTreeWriter, result);
        JsonObject object = new JsonObject();
        object.addProperty("name", (String) null);
        jsonTreeWriter.name("name");
        assertEquals(object, jsonTreeWriter.get());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueNumberWithLenientFalseAndNaN() throws IOException {
        jsonTreeWriter.setLenient(false);
        jsonTreeWriter.value(Double.NaN);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueNumberWithLenientFalseAndInfinite() throws IOException {
        jsonTreeWriter.setLenient(false);
        jsonTreeWriter.value(Double.POSITIVE_INFINITY);
    }

    @Test(expected = IOException.class)
    public void testFlush() throws IOException {
        jsonTreeWriter.flush();
    }

    @Test(expected = IOException.class)
    public void testClose() throws IOException {
        jsonTreeWriter.close();
    }

    @Test(expected = IOException.class)
    public void testCloseWithNonEmptyStack() throws IOException {
        jsonTreeWriter.beginArray();
        jsonTreeWriter.close();
    }

    @Test
    public void testEqualsAndHashCode() {
        JsonTreeWriter jsonTreeWriter2 = new JsonTreeWriter();
        assertEquals(jsonTreeWriter, jsonTreeWriter2);
        assertEquals(jsonTreeWriter.hashCode(), jsonTreeWriter2.hashCode());
    }
}