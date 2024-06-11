import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class JsonTreeWriterTest {

    private JsonTreeWriter jsonTreeWriter;

    @Before
    public void setup() {
        jsonTreeWriter = new JsonTreeWriter();
    }

    @Test
    public void testBeginArray() {
        jsonTreeWriter.beginArray();
        JsonElement result = jsonTreeWriter.get();
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof JsonElement);
    }

    @Test
    public void testEndArray() {
        jsonTreeWriter.beginArray();
        jsonTreeWriter.endArray();
        JsonElement result = jsonTreeWriter.get();
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof JsonElement);
    }

    @Test
    public void testBeginObject() {
        jsonTreeWriter.beginObject();
        JsonElement result = jsonTreeWriter.get();
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof JsonElement);
    }

    @Test
    public void testEndObject() {
        jsonTreeWriter.beginObject();
        jsonTreeWriter.endObject();
        JsonElement result = jsonTreeWriter.get();
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof JsonElement);
    }

    @Test
    public void testName() {
        String name = "testName";
        jsonTreeWriter.beginObject();
        jsonTreeWriter.name(name);
        jsonTreeWriter.value("testValue");
        jsonTreeWriter.endObject();
        JsonObject result = (JsonObject) jsonTreeWriter.get();
        Assert.assertNotNull(result);
        Assert.assertEquals("testValue", result.get(name).getAsString());
    }

    @Test
    public void testNullValue() {
        jsonTreeWriter.nullValue();
        JsonNull result = (JsonNull) jsonTreeWriter.get();
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof JsonNull);
    }

    @Test
    public void testValueString() {
        String value = "testValue";
        jsonTreeWriter.value(value);
        JsonPrimitive result = (JsonPrimitive) jsonTreeWriter.get();
        Assert.assertNotNull(result);
        Assert.assertEquals(value, result.getAsString());
    }

    @Test
    public void testValueBoolean() {
        boolean value = true;
        jsonTreeWriter.value(value);
        JsonPrimitive result = (JsonPrimitive) jsonTreeWriter.get();
        Assert.assertNotNull(result);
        Assert.assertEquals(value, result.getAsBoolean());
    }

    @Test
    public void testValueDouble() {
        double value = 10.5;
        jsonTreeWriter.value(value);
        JsonPrimitive result = (JsonPrimitive) jsonTreeWriter.get();
        Assert.assertNotNull(result);
        Assert.assertEquals(value, result.getAsDouble(), 0.001);
    }

    @Test
    public void testValueLong() {
        long value = 100L;
        jsonTreeWriter.value(value);
        JsonPrimitive result = (JsonPrimitive) jsonTreeWriter.get();
        Assert.assertNotNull(result);
        Assert.assertEquals(value, result.getAsLong());
    }

    @Test
    public void testValueNumber() {
        int value = 5;
        jsonTreeWriter.value(value);
        JsonPrimitive result = (JsonPrimitive) jsonTreeWriter.get();
        Assert.assertNotNull(result);
        Assert.assertEquals(value, result.getAsInt());
    }

    @Test(expected = IllegalStateException.class)
    public void testIncompleteDocument() throws Exception {
        jsonTreeWriter.flush();
        jsonTreeWriter.close();
    }

    @Test
    public void testSerializeNullsEnabled() {
        jsonTreeWriter.setSerializeNulls(true);
        Assert.assertTrue(jsonTreeWriter.getSerializeNulls());
    }

    @Test
    public void testSerializeNullsDisabled() {
        jsonTreeWriter.setSerializeNulls(false);
        Assert.assertFalse(jsonTreeWriter.getSerializeNulls());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueInfinityNotAllowed() throws Exception {
        jsonTreeWriter.value(Double.POSITIVE_INFINITY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueNaNNotAllowed() throws Exception {
        jsonTreeWriter.value(Double.NaN);
    }

}