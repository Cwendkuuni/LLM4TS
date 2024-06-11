
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsonTreeWriterTest {

    private JsonTreeWriter jsonTreeWriter;

    @Before
    public void setup() {
        jsonTreeWriter = new JsonTreeWriter();
    }

    @Test
    public void testGet_WhenNoElementAdded_ShouldReturnJsonNull() {
        assertEquals(JsonNull.INSTANCE, jsonTreeWriter.get());
    }

    @Test
    public void testBeginArray_WhenArrayStarted_ShouldReturnJsonWriter() throws Exception {
        assertEquals(jsonTreeWriter, jsonTreeWriter.beginArray());
    }

    @Test
    public void testEndArray_WhenArrayEnded_ShouldReturnJsonWriter() throws Exception {
        jsonTreeWriter.beginArray();
        assertEquals(jsonTreeWriter, jsonTreeWriter.endArray());
    }

    @Test(expected = IllegalStateException.class)
    public void testEndArray_WhenArrayNotStarted_ShouldThrowException() throws Exception {
        jsonTreeWriter.endArray();
    }

    @Test
    public void testBeginObject_WhenObjectStarted_ShouldReturnJsonWriter() throws Exception {
        assertEquals(jsonTreeWriter, jsonTreeWriter.beginObject());
    }

    @Test
    public void testEndObject_WhenObjectEnded_ShouldReturnJsonWriter() throws Exception {
        jsonTreeWriter.beginObject();
        assertEquals(jsonTreeWriter, jsonTreeWriter.endObject());
    }

    @Test(expected = IllegalStateException.class)
    public void testEndObject_WhenObjectNotStarted_ShouldThrowException() throws Exception {
        jsonTreeWriter.endObject();
    }

    @Test
    public void testName_WhenNameAddedToObject_ShouldReturnJsonWriter() throws Exception {
        jsonTreeWriter.beginObject();
        assertEquals(jsonTreeWriter, jsonTreeWriter.name("name"));
    }

    @Test(expected = IllegalStateException.class)
    public void testName_WhenNameAddedToInvalidObject_ShouldThrowException() throws Exception {
        jsonTreeWriter.name("name");
    }

    @Test
    public void testValue_WhenValueAddedToString_ShouldReturnJsonWriter() throws Exception {
        assertEquals(jsonTreeWriter, jsonTreeWriter.value("value"));
    }

    @Test
    public void testValue_WhenValueAddedToNull_ShouldReturnJsonWriter() throws Exception {
        assertEquals(jsonTreeWriter, jsonTreeWriter.value(null));
    }

    @Test
    public void testNullValue_WhenNullValueAdded_ShouldReturnJsonWriter() throws Exception {
        assertEquals(jsonTreeWriter, jsonTreeWriter.nullValue());
    }

    @Test
    public void testValue_WhenValueAddedToBoolean_ShouldReturnJsonWriter() throws Exception {
        assertEquals(jsonTreeWriter, jsonTreeWriter.value(true));
    }

    @Test
    public void testValue_WhenValueAddedToDouble_ShouldReturnJsonWriter() throws Exception {
        assertEquals(jsonTreeWriter, jsonTreeWriter.value(3.14));
    }

    @Test
    public void testValue_WhenValueAddedToLong_ShouldReturnJsonWriter() throws Exception {
        assertEquals(jsonTreeWriter, jsonTreeWriter.value(100L));
    }

    @Test
    public void testValue_WhenValueAddedToNumber_ShouldReturnJsonWriter() throws Exception {
        assertEquals(jsonTreeWriter, jsonTreeWriter.value(10));
    }

    @Test(expected = IllegalStateException.class)
    public void testValue_WhenValueAddedToInvalidState_ShouldThrowException() throws Exception {
        jsonTreeWriter.endArray();
        jsonTreeWriter.value("value");
    }

    @Test
    public void testFlush_WhenCalled_ShouldNotThrowException() throws Exception {
        jsonTreeWriter.flush();
    }

    @Test(expected = IOException.class)
    public void testClose_WhenCalledWithIncompleteDocument_ShouldThrowException() throws Exception {
        jsonTreeWriter.beginArray();
        jsonTreeWriter.close();
    }

    @Test
    public void testClose_WhenCalledWithCompleteDocument_ShouldNotThrowException() throws Exception {
        jsonTreeWriter.close();
    }

}
