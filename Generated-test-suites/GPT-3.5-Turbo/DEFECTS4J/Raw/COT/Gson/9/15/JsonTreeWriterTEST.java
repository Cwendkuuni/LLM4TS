import org.junit.Assert;
import org.junit.Test;

public class JsonTreeWriterTest {
    
    private JsonTreeWriter jsonWriter;
    
    @Test
    public void testGet() {
        jsonWriter = new JsonTreeWriter();
        Assert.assertEquals(JsonNull.INSTANCE, jsonWriter.get());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testBeginArray_IllegalStateException() throws IOException {
        jsonWriter = new JsonTreeWriter();
        jsonWriter.beginArray();
    }
    
    @Test(expected = IllegalStateException.class)
    public void testEndArray_IllegalStateException() throws IOException {
        jsonWriter = new JsonTreeWriter();
        jsonWriter.endArray();
    }
    
    @Test
    public void testBeginObject() throws IOException {
        jsonWriter = new JsonTreeWriter();
        jsonWriter.beginObject();
        Assert.assertTrue(jsonWriter.peek() instanceof JsonObject);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testEndObject_IllegalStateException() throws IOException {
        jsonWriter = new JsonTreeWriter();
        jsonWriter.endObject();
    }
    
    @Test(expected = IllegalStateException.class)
    public void testName_IllegalStateException() throws IOException {
        jsonWriter = new JsonTreeWriter();
        jsonWriter.name("name");
    }
    
    @Test
    public void testValue_String() throws IOException {
        jsonWriter = new JsonTreeWriter();
        jsonWriter.beginObject();
        jsonWriter.name("name");
        jsonWriter.value("value");
        Assert.assertEquals("value", jsonWriter.peek().getAsJsonObject().get("name").getAsString());
    }
    
    @Test
    public void testNullValue() throws IOException {
        jsonWriter = new JsonTreeWriter();
        jsonWriter.beginObject();
        jsonWriter.name("name");
        jsonWriter.nullValue();
        Assert.assertTrue(jsonWriter.peek().getAsJsonObject().get("name").isJsonNull());
    }
    
    @Test
    public void testValue_Boolean() throws IOException {
        jsonWriter = new JsonTreeWriter();
        jsonWriter.beginObject();
        jsonWriter.name("name");
        jsonWriter.value(true);
        Assert.assertTrue(jsonWriter.peek().getAsJsonObject().get("name").getAsBoolean());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testValue_Double_IllegalArgumentException() throws IOException {
        jsonWriter = new JsonTreeWriter();
        jsonWriter.beginObject();
        jsonWriter.name("name");
        jsonWriter.value(Double.NaN);
    }
    
    @Test
    public void testValue_Double() throws IOException {
        jsonWriter = new JsonTreeWriter();
        jsonWriter.beginObject();
        jsonWriter.name("name");
        jsonWriter.value(1.0);
        Assert.assertEquals(1.0, jsonWriter.peek().getAsJsonObject().get("name").getAsDouble(), 0.0);
    }
    
    @Test
    public void testValue_Long() throws IOException {
        jsonWriter = new JsonTreeWriter();
        jsonWriter.beginObject();
        jsonWriter.name("name");
        jsonWriter.value(1L);
        Assert.assertEquals(1L, jsonWriter.peek().getAsJsonObject().get("name").getAsLong());
    }
    
    @Test
    public void testValue_Number() throws IOException {
        jsonWriter = new JsonTreeWriter();
        jsonWriter.beginObject();
        jsonWriter.name("name");
        jsonWriter.value(1.0f);
        Assert.assertEquals(1.0f, jsonWriter.peek().getAsJsonObject().get("name").getAsNumber());
    }
    
    @Test(expected = IOException.class)
    public void testFlush() throws IOException {
        jsonWriter = new JsonTreeWriter();
        jsonWriter.flush();
    }
    
    @Test(expected = IOException.class)
    public void testClose_IncompleteDocument() throws IOException {
        jsonWriter = new JsonTreeWriter();
        jsonWriter.close();
    }
    
    @Test
    public void testClose() throws IOException {
        jsonWriter = new JsonTreeWriter();
        jsonWriter.beginObject();
        jsonWriter.endObject();
        jsonWriter.close();
        Assert.assertEquals(JsonTreeWriter.SENTINEL_CLOSED, jsonWriter.peek());
    }

}