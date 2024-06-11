import static org.junit.Assert.*;
import org.junit.Test;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.io.IOException;
import java.io.StringWriter;

public class JsonTreeWriterTest {

    @Test
    public void testGet() {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        JsonElement result = jsonTreeWriter.get();
        assertEquals(JsonNull.INSTANCE, result);
    }

    @Test
    public void testBeginAndEndArray() throws IOException {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        jsonTreeWriter.beginArray();
        jsonTreeWriter.endArray();
        String expected = "[]";
        StringWriter stringWriter = new StringWriter();
        jsonTreeWriter.close();
        assertEquals(expected, stringWriter.toString());
    }

    @Test
    public void testBeginAndEndObject() throws IOException {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        jsonTreeWriter.beginObject();
        jsonTreeWriter.endObject();
        String expected = "{}";
        StringWriter stringWriter = new StringWriter();
        jsonTreeWriter.close();
        assertEquals(expected, stringWriter.toString());
    }

    @Test
    public void testValue() throws IOException {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        jsonTreeWriter.value("test");
        StringWriter stringWriter = new StringWriter();
        jsonTreeWriter.close();
        assertEquals("\"test\"", stringWriter.toString());
    }

    @Test
    public void testNullValue() throws IOException {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        jsonTreeWriter.nullValue();
        StringWriter stringWriter = new StringWriter();
        jsonTreeWriter.close();
        assertEquals("null", stringWriter.toString());
    }

    @Test
    public void testValueBoolean() throws IOException {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        jsonTreeWriter.value(true);
        StringWriter stringWriter = new StringWriter();
        jsonTreeWriter.close();
        assertEquals("true", stringWriter.toString());
    }

    @Test
    public void testValueDouble() throws IOException {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        jsonTreeWriter.value(3.14);
        StringWriter stringWriter = new StringWriter();
        jsonTreeWriter.close();
        assertEquals("3.14", stringWriter.toString());
    }

    @Test
    public void testValueLong() throws IOException {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        jsonTreeWriter.value(123L);
        StringWriter stringWriter = new StringWriter();
        jsonTreeWriter.close();
        assertEquals("123", stringWriter.toString());
    }

    @Test
    public void testValueNumber() throws IOException {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        jsonTreeWriter.value(456);
        StringWriter stringWriter = new StringWriter();
        jsonTreeWriter.close();
        assertEquals("456", stringWriter.toString());
    }

    @Test(expected = IOException.class)
    public void testCloseIncompleteDocument() throws IOException {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        jsonTreeWriter.beginObject();
        jsonTreeWriter.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueDoubleWithNaN() throws IOException {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        jsonTreeWriter.value(Double.NaN);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueDoubleWithInfinite() throws IOException {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        jsonTreeWriter.value(Double.POSITIVE_INFINITY);
    }

}