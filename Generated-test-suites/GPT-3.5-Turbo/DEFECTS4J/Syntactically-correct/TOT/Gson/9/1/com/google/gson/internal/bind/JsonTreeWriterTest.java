import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsonTreeWriterTest {

    private JsonTreeWriter jsonTreeWriter;

    @Before
    public void setUp() {
        jsonTreeWriter = new JsonTreeWriter();
    }

    @Test
    public void testGet_EmptyStack_ReturnsJsonNull() {
        // Act
        JsonElement result = jsonTreeWriter.get();

        // Assert
        assertTrue(result instanceof JsonNull);
    }

    @Test
    public void testBeginArray_ValidInput_SuccessfullyAddsJsonArrayToStack() throws Exception {
        // Act
        jsonTreeWriter.beginArray();
        JsonElement result = jsonTreeWriter.peek();

        // Assert
        assertTrue(result instanceof JsonArray);
    }

    @Test(expected = IllegalStateException.class)
    public void testEndArray_EmptyStack_ThrowsIllegalStateException() throws Exception {
        // Act
        jsonTreeWriter.endArray();
    }

    @Test(expected = IllegalStateException.class)
    public void testEndArray_JsonObjectOnTopOfStack_ThrowsIllegalStateException() throws Exception {
        // Arrange
        jsonTreeWriter.beginObject();

        // Act
        jsonTreeWriter.endArray();
    }

    @Test(expected = IllegalStateException.class)
    public void testEndArray_JsonArrayOnTopOfStack_RemovesArrayFromStack() throws Exception {
        // Arrange
        jsonTreeWriter.beginArray();

        // Act
        jsonTreeWriter.endArray();
        jsonTreeWriter.peek();
    }

    @Test
    public void testBeginObject_ValidInput_SuccessfullyAddsJsonObjectToStack() throws Exception {
        // Act
        jsonTreeWriter.beginObject();
        JsonElement result = jsonTreeWriter.peek();

        // Assert
        assertTrue(result instanceof JsonObject);
    }

    @Test(expected = IllegalStateException.class)
    public void testEndObject_EmptyStack_ThrowsIllegalStateException() throws Exception {
        // Act
        jsonTreeWriter.endObject();
    }

    @Test(expected = IllegalStateException.class)
    public void testEndObject_JsonArrayOnTopOfStack_ThrowsIllegalStateException() throws Exception {
        // Arrange
        jsonTreeWriter.beginArray();

        // Act
        jsonTreeWriter.endObject();
    }

    @Test(expected = IllegalStateException.class)
    public void testEndObject_JsonObjectOnTopOfStack_RemovesObjectFromStack() throws Exception {
        // Arrange
        jsonTreeWriter.beginObject();

        // Act
        jsonTreeWriter.endObject();
        jsonTreeWriter.peek();
    }

    @Test(expected = IllegalStateException.class)
    public void testName_EmptyStack_ThrowsIllegalStateException() throws Exception {
        // Act
        jsonTreeWriter.name("key");
    }

    @Test(expected = IllegalStateException.class)
    public void testName_JsonArrayOnTopOfStack_ThrowsIllegalStateException() throws Exception {
        // Arrange
        jsonTreeWriter.beginArray();

        // Act
        jsonTreeWriter.name("key");
    }

    @Test
    public void testName_JsonObjectOnTopOfStack_CorrectlySetsPendingName() throws Exception {
        // Arrange
        jsonTreeWriter.beginObject();

        // Act
        jsonTreeWriter.name("key");
        String result = jsonTreeWriter.getPendingName();

        // Assert
        assertEquals("key", result);
    }

    @Test(expected = IllegalStateException.class)
    public void testValue_NullStringInput_ThrowsIllegalStateException() throws Exception {
        // Act
        jsonTreeWriter.value((String) null);
    }

    @Test
    public void testValue_NullStringInput_AddsJsonNullToStack() throws Exception {
        // Act
        jsonTreeWriter.value((String) null);
        JsonElement result = jsonTreeWriter.peek();

        // Assert
        assertTrue(result instanceof JsonNull);
    }

    @Test
    public void testValue_ValidStringInput_AddsJsonPrimitiveToStack() throws Exception {
        // Act
        jsonTreeWriter.value("value");
        JsonElement result = jsonTreeWriter.peek();

        // Assert
        assertTrue(result instanceof JsonPrimitive);
        assertEquals("value", result.getAsString());
    }

    @Test
    public void testNullValue_ValidInput_AddsJsonNullToStack() throws Exception {
        // Act
        jsonTreeWriter.nullValue();
        JsonElement result = jsonTreeWriter.peek();

        // Assert
        assertTrue(result instanceof JsonNull);
    }

    @Test
    public void testValue_BoolValueInput_AddsJsonPrimitiveToStack() throws Exception {
        // Act
        jsonTreeWriter.value(true);
        JsonElement result = jsonTreeWriter.peek();

        // Assert
        assertTrue(result instanceof JsonPrimitive);
        assertEquals(true, result.getAsBoolean());
    }

    @Test
    public void testValue_DoubleValueInput_AddsJsonPrimitiveToStack() throws Exception {
        // Act
        jsonTreeWriter.value(3.14);
        JsonElement result = jsonTreeWriter.peek();

        // Assert
        assertTrue(result instanceof JsonPrimitive);
        assertEquals(3.14, result.getAsDouble(), 0.0001);
    }

    @Test
    public void testValue_LongValueInput_AddsJsonPrimitiveToStack() throws Exception {
        // Act
        jsonTreeWriter.value(100L);
        JsonElement result = jsonTreeWriter.peek();

        // Assert
        assertTrue(result instanceof JsonPrimitive);
        assertEquals(100L, result.getAsLong());
    }

    @Test(expected = IllegalStateException.class)
    public void testValue_NullNumberInput_ThrowsIllegalStateException() throws Exception {
        // Act
        jsonTreeWriter.value((Number) null);
    }

    @Test
    public void testValue_ValidNumberInput_AddsJsonPrimitiveToStack() throws Exception {
        // Arrange
        Number number = 12345;

        // Act
        jsonTreeWriter.value(number);
        JsonElement result = jsonTreeWriter.peek();

        // Assert
        assertTrue(result instanceof JsonPrimitive);
        assertEquals(number, result.getAsNumber());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValue_NaNNumberInput_ThrowsIllegalArgumentException() throws Exception {
        // Arrange
        Number number = Double.NaN;

        // Act
        jsonTreeWriter.value(number);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValue_InfinityNumberInput_ThrowsIllegalArgumentException() throws Exception {
        // Arrange
        Number number = Double.POSITIVE_INFINITY;

        // Act
        jsonTreeWriter.value(number);
    }

    @Test
    public void testFlush_NoActionTaken_NoExceptionsThrown() throws Exception {
        // Act
        jsonTreeWriter.flush();
    }

    @Test(expected = IOException.class)
    public void testClose_IncompleteDocument_ThrowsIOException() throws Exception {
        // Act
        jsonTreeWriter.close();
    }

    @Test
    public void testClose_CompleteDocument_NoExceptionsThrown() throws Exception {
        // Arrange
        jsonTreeWriter.beginObject();
        jsonTreeWriter.endObject();

        // Act
        jsonTreeWriter.close();
    }

}