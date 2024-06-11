
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.bind.JsonTreeWriter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;

public class JsonTreeWriterTest {

    private JsonTreeWriter jsonTreeWriter;

    @Before
    public void setup() {
        jsonTreeWriter = new JsonTreeWriter();
    }

    @Test
    public void testGet_EmptyWriter_ReturnsJsonNull() {
        JsonElement expected = JsonNull.INSTANCE;
        JsonElement actual = jsonTreeWriter.get();
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalStateException.class)
    public void testGet_NotEmptyWriter_ThrowsIllegalStateException() {
        jsonTreeWriter.beginArray();
        jsonTreeWriter.get();
    }

    @Test
    public void testBeginArray_ValidWriter_AddsJsonArrayToStack() {
        jsonTreeWriter.beginArray();
        JsonElement expected = new JsonArray();
        JsonElement actual = jsonTreeWriter.peek();
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalStateException.class)
    public void testEndArray_EmptyWriter_ThrowsIllegalStateException() throws IOException {
        jsonTreeWriter.endArray();
    }

    @Test
    public void testEndArray_ValidWriter_RemovesJsonArrayFromStack() throws IOException {
        jsonTreeWriter.beginArray();
        jsonTreeWriter.endArray();
        Assert.assertTrue(jsonTreeWriter.get().isJsonNull());
    }

    @Test
    public void testBeginObject_ValidWriter_AddsJsonObjectToStack() {
        jsonTreeWriter.beginObject();
        JsonElement expected = new JsonObject();
        JsonElement actual = jsonTreeWriter.peek();
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalStateException.class)
    public void testEndObject_EmptyWriter_ThrowsIllegalStateException() throws IOException {
        jsonTreeWriter.endObject();
    }

    @Test
    public void testEndObject_ValidWriter_RemovesJsonObjectFromStack() throws IOException {
        jsonTreeWriter.beginObject();
        jsonTreeWriter.endObject();
        Assert.assertTrue(jsonTreeWriter.get().isJsonNull());
    }

    @Test(expected = IllegalStateException.class)
    public void testName_EmptyWriter_ThrowsIllegalStateException() throws IOException {
        jsonTreeWriter.name("key");
    }

    @Test(expected = IllegalStateException.class)
    public void testName_InvalidStack_ThrowsIllegalStateException() throws IOException {
        jsonTreeWriter.beginArray();
        jsonTreeWriter.name("key");
    }

    @Test
    public void testName_ValidWriter_AddsNameToPendingName() throws IOException {
        jsonTreeWriter.beginObject();
        jsonTreeWriter.name("key");
        Assert.assertEquals("key", jsonTreeWriter.pendingName);
    }

    @Test(expected = IllegalStateException.class)
    public void testValue_NullValue_ThrowsIllegalStateException() throws IOException {
        jsonTreeWriter.value((String) null);
    }

    @Test
    public void testValue_NullValue_AddsJsonNullToStack() throws IOException {
        jsonTreeWriter.value((String) null);
        Assert.assertTrue(jsonTreeWriter.get().isJsonNull());
    }

    @Test
    public void testValue_StringValue_AddsJsonPrimitiveToStack() throws IOException {
        String value = "Hello, World!";
        jsonTreeWriter.value(value);
        JsonPrimitive expected = new JsonPrimitive(value);
        JsonElement actual = jsonTreeWriter.peek();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testNullValue_ValidWriter_AddsJsonNullToStack() throws IOException {
        jsonTreeWriter.nullValue();
        Assert.assertTrue(jsonTreeWriter.get().isJsonNull());
    }

    @Test
    public void testValue_BooleanValue_AddsJsonPrimitiveToStack() throws IOException {
        boolean value = true;
        jsonTreeWriter.value(value);
        JsonPrimitive expected = new JsonPrimitive(value);
        JsonElement actual = jsonTreeWriter.peek();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testValue_DoubleValue_AddsJsonPrimitiveToStack() throws IOException {
        double value = 3.14;
        jsonTreeWriter.value(value);
        JsonPrimitive expected = new JsonPrimitive(value);
        JsonElement actual = jsonTreeWriter.peek();
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValue_NaNValue_ThrowsIllegalArgumentException() throws IOException {
        double value = Double.NaN;
        jsonTreeWriter.value(value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValue_InfinityValue_ThrowsIllegalArgumentException() throws IOException {
        double value = Double.POSITIVE_INFINITY;
        jsonTreeWriter.value(value);
    }

    @Test
    public void testValue_LongValue_AddsJsonPrimitiveToStack() throws IOException {
        long value = 1000L;
        jsonTreeWriter.value(value);
        JsonPrimitive expected = new JsonPrimitive(value);
        JsonElement actual = jsonTreeWriter.peek();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testValue_NumberValue_AddsJsonPrimitiveToStack() throws IOException {
        int value = 1234;
        jsonTreeWriter.value(value);
        JsonPrimitive expected = new JsonPrimitive(value);
        JsonElement actual = jsonTreeWriter.peek();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFlush_EmptyWriter_DoesNotThrowException() throws IOException {
        jsonTreeWriter.flush();
    }

    @Test(expected = IOException.class)
    public void testClose_NotEmptyWriter_ThrowsIOException() throws IOException {
        jsonTreeWriter.beginArray();
        jsonTreeWriter.close();
    }

    @Test
    public void testClose_EmptyWriter_AddsSentinelClosedToJsonStack() throws IOException {
        jsonTreeWriter.close();
        Assert.assertTrue(jsonTreeWriter.stack.contains(JsonTreeWriter.SENTINEL_CLOSED));
    }
}

