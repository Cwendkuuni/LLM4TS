// JUnit test for JsonTreeWriter class
import org.junit.Assert;
import org.junit.Test;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class JsonTreeWriterTest {

    // Test for get() method
    @Test
    public void testGet() {
        JsonTreeWriter writer = new JsonTreeWriter();
        JsonElement result = writer.get();
        Assert.assertEquals(JsonNull.INSTANCE, result);
    }

    // Test for beginArray() method
    @Test
    public void testBeginArray() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();
        JsonElement result = writer.get();
        Assert.assertEquals(JsonArray.class, result.getClass());
    }

    // Test for endArray() method
    @Test
    public void testEndArray() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();
        writer.endArray();
        JsonElement result = writer.get();
        Assert.assertEquals(JsonNull.INSTANCE, result);
    }

    // Test for beginObject() method
    @Test
    public void testBeginObject() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        JsonElement result = writer.get();
        Assert.assertEquals(JsonObject.class, result.getClass());
    }

    // Test for endObject() method
    @Test
    public void testEndObject() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.endObject();
        JsonElement result = writer.get();
        Assert.assertEquals(JsonNull.INSTANCE, result);
    }

    // Test for name() method
    @Test
    public void testName() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.name("key");
        JsonElement result = writer.get();
        Assert.assertEquals("key", result.getAsJsonObject().entrySet().iterator().next().getKey());
    }

    // Test for value(String value) method
    @Test
    public void testValueString() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.value("value");
        JsonElement result = writer.get();
        Assert.assertEquals("value", result.getAsString());
    }

    // Test for nullValue() method
    @Test
    public void testNullValue() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.nullValue();
        JsonElement result = writer.get();
        Assert.assertEquals(JsonNull.INSTANCE, result);
    }

    // Test for value(boolean value) method
    @Test
    public void testValueBoolean() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.value(true);
        JsonElement result = writer.get();
        Assert.assertEquals(true, result.getAsBoolean());
    }

    // Test for value(double value) method
    @Test
    public void testValueDouble() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.value(1.0);
        JsonElement result = writer.get();
        Assert.assertEquals(1.0, result.getAsDouble(), 0.0001);
    }

    // Test for value(long value) method
    @Test
    public void testValueLong() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.value(1L);
        JsonElement result = writer.get();
        Assert.assertEquals(1L, result.getAsLong());
    }

    // Test for value(Number value) method
    @Test
    public void testValueNumber() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.value(1);
        JsonElement result = writer.get();
        Assert.assertEquals(1, result.getAsInt());
    }
}