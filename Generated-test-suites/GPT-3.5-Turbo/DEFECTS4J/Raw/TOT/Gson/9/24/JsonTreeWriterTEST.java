
import com.google.gson.*;
import com.google.gson.internal.bind.JsonTreeWriter;
import org.junit.*;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.*;

public class JsonTreeWriterTest {

    private JsonTreeWriter jsonTreeWriter;

    @Before
    public void setUp() {
        jsonTreeWriter = new JsonTreeWriter();
    }

    @After
    public void tearDown() {
        jsonTreeWriter = null;
    }

    @Test
    public void testGet_EmptyTree_ReturnsJsonNull() {
        JsonElement result = jsonTreeWriter.get();
        assertEquals(JsonNull.INSTANCE, result);
    }

    @Test
    public void testBeginArray_ValidArray_BeginsArray() {
        jsonTreeWriter.beginArray();
        JsonElement result = jsonTreeWriter.get();
        assertTrue(result.isJsonArray());
    }

    @Test(expected = IllegalStateException.class)
    public void testEndArray_EmptyStack_ThrowsIllegalStateException() {
        jsonTreeWriter.endArray();
    }

    @Test
    public void testEndArray_ValidStack_EndsArray() {
        jsonTreeWriter.beginArray();
        jsonTreeWriter.endArray();
        JsonElement result = jsonTreeWriter.get();
        assertFalse(result.isJsonArray());
    }

    @Test
    public void testBeginObject_ValidObject_BeginsObject() {
        jsonTreeWriter.beginObject();
        JsonElement result = jsonTreeWriter.get();
        assertTrue(result.isJsonObject());
    }

    @Test(expected = IllegalStateException.class)
    public void testEndObject_EmptyStack_ThrowsIllegalStateException() {
        jsonTreeWriter.endObject();
    }

    @Test
    public void testEndObject_ValidStack_EndsObject() {
        jsonTreeWriter.beginObject();
        jsonTreeWriter.endObject();
        JsonElement result = jsonTreeWriter.get();
        assertFalse(result.isJsonObject());
    }

    @Test(expected = IllegalStateException.class)
    public void testName_EmptyStack_ThrowsIllegalStateException() {
        jsonTreeWriter.name("name");
    }

    @Test(expected = IllegalStateException.class)
    public void testName_LatestElementNotObject_ThrowsIllegalStateException() {
        jsonTreeWriter.beginArray();
        jsonTreeWriter.name("name");
    }

    @Test
    public void testName_ValidStack_SetsPendingName() {
        jsonTreeWriter.beginObject();
        jsonTreeWriter.name("name");
        assertEquals("name", jsonTreeWriter.pendingName);
    }

    @Test
    public void testValue_StringValue_AppendsJsonValue() {
        jsonTreeWriter.value("value");
        JsonElement result = jsonTreeWriter.get();
        assertTrue(result.isJsonPrimitive());
        assertEquals("value", result.getAsString());
    }

    @Test
    public void testValue_NullValue_AppendsJsonValue() {
        jsonTreeWriter.value(null);
        JsonElement result = jsonTreeWriter.get();
        assertTrue(result.isJsonNull());
    }

    @Test
    public void testValue_BooleanValue_AppendsJsonValue() {
        jsonTreeWriter.value(true);
        JsonElement result = jsonTreeWriter.get();
        assertTrue(result.isJsonPrimitive());
        assertEquals(true, result.getAsBoolean());
    }

    @Test
    public void testValue_DoubleValue_ValidDouble_AppendsJsonValue() {
        jsonTreeWriter.value(3.14);
        JsonElement result = jsonTreeWriter.get();
        assertTrue(result.isJsonPrimitive());
        assertEquals(3.14, result.getAsDouble(), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValue_DoubleValue_NaNValue_ThrowsIllegalArgumentException() {
        jsonTreeWriter.value(Double.NaN);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValue_DoubleValue_InfinityValue_ThrowsIllegalArgumentException() {
        jsonTreeWriter.value(Double.POSITIVE_INFINITY);
    }

    @Test
    public void testValue_LongValue_ValidLong_AppendsJsonValue() {
        jsonTreeWriter.value(100L);
        JsonElement result = jsonTreeWriter.get();
        assertTrue(result.isJsonPrimitive());
        assertEquals(100L, result.getAsLong());
    }

    @Test
    public void testValue_NumberValue_ValidNumber_AppendsJsonValue() {
        jsonTreeWriter.value(12345);
        JsonElement result = jsonTreeWriter.get();
        assertTrue(result.isJsonPrimitive());
        assertEquals(12345, result.getAsInt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValue_NumberValue_NaNValue_ThrowsIllegalArgumentException() {
        jsonTreeWriter.value(Double.NaN);
    }

    @Test
    public void testFlush_EmptyMethod_NoExceptionsThrown() {
        try {
            jsonTreeWriter.flush();
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test(expected = IOException.class)
    public void testClose_IncompleteDocument_ThrowsIOException() throws IOException {
        jsonTreeWriter.beginArray();
        jsonTreeWriter.close();
    }

    @Test
    public void testClose_CompleteDocument_Succeeds() {
        jsonTreeWriter.close();
        JsonElement result = jsonTreeWriter.get();
        assertEquals("closed", result.getAsString());
    }

}

