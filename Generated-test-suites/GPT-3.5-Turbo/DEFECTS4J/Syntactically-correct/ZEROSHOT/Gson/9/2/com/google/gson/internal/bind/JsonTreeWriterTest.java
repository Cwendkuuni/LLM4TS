import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JsonTreeWriterTest {

    private JsonTreeWriter jsonTreeWriter;

    @Before
    public void setup() {
        jsonTreeWriter = new JsonTreeWriter();
    }

    @Test
    public void testGet_EmptyWriter_ReturnsJsonNull() {
        JsonElement result = jsonTreeWriter.get();
        assertTrue(result instanceof JsonNull);
    }

    @Test
    public void testBeginArray_ValidCall_StackContainsJsonArray() throws IOException {
        jsonTreeWriter.beginArray();
        List<JsonElement> stack = jsonTreeWriter.getStack();
        assertTrue(stack.get(0) instanceof JsonArray);
    }

    @Test(expected = IllegalStateException.class)
    public void testEndArray_StackIsEmpty_ThrowsIllegalStateException() throws IOException {
        jsonTreeWriter.endArray();
    }

    @Test(expected = IllegalStateException.class)
    public void testEndArray_PendingNameNotNull_ThrowsIllegalStateException() throws IOException {
        jsonTreeWriter.beginObject();
        jsonTreeWriter.name("test");
        jsonTreeWriter.endArray();
    }

    @Test
    public void testEndArray_StackContainsJsonArray_Success() throws IOException {
        jsonTreeWriter.beginArray();
        jsonTreeWriter.endArray();
        assertTrue(jsonTreeWriter.getStack().isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void testBeginObject_StackIsEmpty_ThrowsIllegalStateException() throws IOException {
        jsonTreeWriter.beginObject();
    }

    @Test
    public void testBeginObject_ValidCall_StackContainsJsonObject() throws IOException {
        jsonTreeWriter.beginObject();
        List<JsonElement> stack = jsonTreeWriter.getStack();
        assertTrue(stack.get(0) instanceof JsonObject);
    }

    @Test(expected = IllegalStateException.class)
    public void testEndObject_StackIsEmpty_ThrowsIllegalStateException() throws IOException {
        jsonTreeWriter.endObject();
    }

    @Test(expected = IllegalStateException.class)
    public void testEndObject_PendingNameNotNull_ThrowsIllegalStateException() throws IOException {
        jsonTreeWriter.beginObject();
        jsonTreeWriter.name("test");
        jsonTreeWriter.endObject();
    }

    @Test
    public void testEndObject_StackContainsJsonObject_Success() throws IOException {
        jsonTreeWriter.beginObject();
        jsonTreeWriter.endObject();
        assertTrue(jsonTreeWriter.getStack().isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void testName_StackIsEmpty_ThrowsIllegalStateException() throws IOException {
        jsonTreeWriter.name("test");
    }

    @Test(expected = IllegalStateException.class)
    public void testName_PendingNameNotNull_ThrowsIllegalStateException() throws IOException {
        jsonTreeWriter.beginObject();
        jsonTreeWriter.name("test1");
        jsonTreeWriter.name("test2");
    }

    @Test(expected = IllegalStateException.class)
    public void testName_StackDoesNotContainJsonObject_ThrowsIllegalStateException() throws IOException {
        jsonTreeWriter.beginArray();
        jsonTreeWriter.name("test");
    }

    @Test
    public void testName_ValidCall_PendingNameSet() throws IOException {
        jsonTreeWriter.beginObject();
        jsonTreeWriter.name("test");
        assertEquals("test", jsonTreeWriter.getPendingName());
    }

    @Test(expected = IOException.class)
    public void testValue_StringIsNull_ReturnsNullValue() throws IOException {
        jsonTreeWriter.value((String) null);
        jsonTreeWriter.flush();
    }

    @Test
    public void testValue_ValidString_CallPutWithExpectedValue() throws IOException {
        jsonTreeWriter.value("test");
        JsonElement result = jsonTreeWriter.getStack().get(0);
        assertTrue(result instanceof JsonPrimitive);
        assertEquals("test", ((JsonPrimitive) result).getAsString());
    }

    @Test
    public void testNullValue_ValidCall_CallPutWithJsonNull() throws IOException {
        jsonTreeWriter.nullValue();
        JsonElement result = jsonTreeWriter.getStack().get(0);
        assertTrue(result instanceof JsonNull);
    }

    @Test
    public void testValue_BooleanValue_CallPutWithExpectedValue() throws IOException {
        jsonTreeWriter.value(true);
        JsonElement result = jsonTreeWriter.getStack().get(0);
        assertTrue(result instanceof JsonPrimitive);
        assertEquals(true, ((JsonPrimitive) result).getAsBoolean());
    }

    @Test
    public void testValue_DoubleValue_CallPutWithExpectedValue() throws IOException {
        jsonTreeWriter.value(1.234);
        JsonElement result = jsonTreeWriter.getStack().get(0);
        assertTrue(result instanceof JsonPrimitive);
        assertEquals(1.234, ((JsonPrimitive) result).getAsDouble(), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValue_NaNValueWithNonLenientMode_ThrowsIllegalArgumentException() throws IOException {
        jsonTreeWriter.setLenient(false);
        jsonTreeWriter.value(Double.NaN);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValue_InfiniteValueWithNonLenientMode_ThrowsIllegalArgumentException() throws IOException {
        jsonTreeWriter.setLenient(false);
        jsonTreeWriter.value(Double.POSITIVE_INFINITY);
    }

    @Test
    public void testValue_LongValue_CallPutWithExpectedValue() throws IOException {
        jsonTreeWriter.value(1234L);
        JsonElement result = jsonTreeWriter.getStack().get(0);
        assertTrue(result instanceof JsonPrimitive);
        assertEquals(1234L, ((JsonPrimitive) result).getAsLong());
    }

    @Test
    public void testValue_NumberValueIsNull_ReturnsNullValue() throws IOException {
        jsonTreeWriter.value((Number) null);
        JsonElement result = jsonTreeWriter.getStack().get(0);
        assertTrue(result instanceof JsonPrimitive);
        assertEquals(0, ((JsonPrimitive) result).getAsInt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValue_NumberValueWithNonLenientMode_ThrowsIllegalArgumentException() throws IOException {
        jsonTreeWriter.setLenient(false);
        jsonTreeWriter.value(Double.POSITIVE_INFINITY);
    }

    @Test
    public void testFlush_ValidCall_NoExceptions() throws IOException {
        jsonTreeWriter.flush();
    }

    @Test(expected = IOException.class)
    public void testClose_StackIsNotEmpty_ThrowsIOException() throws IOException {
        jsonTreeWriter.beginObject();
        jsonTreeWriter.close();
    }

    @Test
    public void testClose_ValidCall_ProductIsSentinelClosed() throws IOException {
        jsonTreeWriter.close();
        assertTrue(jsonTreeWriter.getProduct().equals(JsonTreeWriter.SENTINEL_CLOSED));
    }
}