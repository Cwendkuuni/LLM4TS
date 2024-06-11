
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Assert;
import org.junit.Test;

public class JsonTreeWriterTest {

    @Test
    public void testGet() {
        JsonTreeWriter writer = new JsonTreeWriter();
        JsonElement element = writer.get();
        Assert.assertNotNull(element);
        Assert.assertTrue(element.isJsonNull());
    }

    @Test
    public void testBeginArray() {
        JsonTreeWriter writer = new JsonTreeWriter();
        try {
            writer.beginArray();
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
        JsonElement element = writer.get();
        Assert.assertNotNull(element);
        Assert.assertTrue(element.isJsonArray());
    }

    @Test
    public void testEndArray() {
        JsonTreeWriter writer = new JsonTreeWriter();
        try {
            writer.beginArray();
            writer.endArray();
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
        JsonElement element = writer.get();
        Assert.assertNotNull(element);
        Assert.assertTrue(element.isJsonArray());
        JsonArray array = (JsonArray) element;
        Assert.assertEquals(0, array.size());
    }

    @Test
    public void testBeginObject() {
        JsonTreeWriter writer = new JsonTreeWriter();
        try {
            writer.beginObject();
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
        JsonElement element = writer.get();
        Assert.assertNotNull(element);
        Assert.assertTrue(element.isJsonObject());
    }

    @Test
    public void testEndObject() {
        JsonTreeWriter writer = new JsonTreeWriter();
        try {
            writer.beginObject();
            writer.endObject();
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
        JsonElement element = writer.get();
        Assert.assertNotNull(element);
        Assert.assertTrue(element.isJsonObject());
        JsonObject object = (JsonObject) element;
        Assert.assertEquals(0, object.size());
    }

    @Test
    public void testName() {
        JsonTreeWriter writer = new JsonTreeWriter();
        try {
            writer.beginObject();
            writer.name("key");
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
        JsonElement element = writer.get();
        Assert.assertNotNull(element);
        Assert.assertTrue(element.isJsonObject());
        JsonObject object = (JsonObject) element;
        Assert.assertTrue(object.has("key"));
    }

    @Test
    public void testValueString() {
        JsonTreeWriter writer = new JsonTreeWriter();
        try {
            writer.value("value");
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
        JsonElement element = writer.get();
        Assert.assertNotNull(element);
        Assert.assertTrue(element.isJsonPrimitive());
        JsonPrimitive primitive = (JsonPrimitive) element;
        Assert.assertEquals("value", primitive.getAsString());
    }

    @Test
    public void testNullValue() {
        JsonTreeWriter writer = new JsonTreeWriter();
        try {
            writer.nullValue();
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
        JsonElement element = writer.get();
        Assert.assertNotNull(element);
        Assert.assertTrue(element.isJsonNull());
    }

    @Test
    public void testValueBoolean() {
        JsonTreeWriter writer = new JsonTreeWriter();
        try {
            writer.value(true);
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
        JsonElement element = writer.get();
        Assert.assertNotNull(element);
        Assert.assertTrue(element.isJsonPrimitive());
        JsonPrimitive primitive = (JsonPrimitive) element;
        Assert.assertEquals(true, primitive.getAsBoolean());
    }

    @Test
    public void testValueDouble() {
        JsonTreeWriter writer = new JsonTreeWriter();
        try {
            writer.value(3.14);
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
        JsonElement element = writer.get();
        Assert.assertNotNull(element);
        Assert.assertTrue(element.isJsonPrimitive());
        JsonPrimitive primitive = (JsonPrimitive) element;
        Assert.assertEquals(3.14, primitive.getAsDouble(), 0.01);
    }

    @Test
    public void testValueLong() {
        JsonTreeWriter writer = new JsonTreeWriter();
        try {
            writer.value(10L);
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
        JsonElement element = writer.get();
        Assert.assertNotNull(element);
        Assert.assertTrue(element.isJsonPrimitive());
        JsonPrimitive primitive = (JsonPrimitive) element;
        Assert.assertEquals(10L, primitive.getAsLong());
    }

    @Test
    public void testValueNumber() {
        JsonTreeWriter writer = new JsonTreeWriter();
        try {
            writer.value(5.67);
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
        JsonElement element = writer.get();
        Assert.assertNotNull(element);
        Assert.assertTrue(element.isJsonPrimitive());
        JsonPrimitive primitive = (JsonPrimitive) element;
        Assert.assertEquals(5.67, primitive.getAsDouble(), 0.01);
    }

    @Test
    public void testFlush() {
        JsonTreeWriter writer = new JsonTreeWriter();
        try {
            writer.flush();
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testClose() {
        JsonTreeWriter writer = new JsonTreeWriter();
        try {
            writer.close();
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
        JsonElement element = writer.get();
        Assert.assertNotNull(element);
        Assert.assertTrue(element.isJsonPrimitive());
        JsonPrimitive primitive = (JsonPrimitive) element;
        Assert.assertEquals("closed", primitive.getAsString());
    }
}

