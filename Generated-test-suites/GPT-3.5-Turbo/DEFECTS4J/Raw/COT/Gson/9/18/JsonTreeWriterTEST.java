
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Assert;
import org.junit.Test;
import java.io.IOException;

public class JsonTreeWriterTest {

    @Test
    public void testGetEmptyJsonElement() {
        JsonTreeWriter writer = new JsonTreeWriter();
        JsonElement element = writer.get();

        Assert.assertNotNull(element);
        Assert.assertTrue(element.isJsonNull());
    }

    @Test
    public void testBeginAndEndArray() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();

        writer.beginArray();
        writer.value("value1");
        writer.value("value2");
        writer.endArray();

        JsonElement element = writer.get();

        Assert.assertNotNull(element);
        Assert.assertTrue(element.isJsonArray());

        JsonArray jsonArray = element.getAsJsonArray();
        Assert.assertEquals(2, jsonArray.size());
        Assert.assertEquals("value1", jsonArray.get(0).getAsString());
        Assert.assertEquals("value2", jsonArray.get(1).getAsString());
    }

    @Test
    public void testBeginAndEndObject() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();

        writer.beginObject();
        writer.name("name1");
        writer.value("value1");
        writer.name("name2");
        writer.value("value2");
        writer.endObject();

        JsonElement element = writer.get();

        Assert.assertNotNull(element);
        Assert.assertTrue(element.isJsonObject());

        JsonObject jsonObject = element.getAsJsonObject();
        Assert.assertEquals(2, jsonObject.entrySet().size());
        Assert.assertEquals("value1", jsonObject.get("name1").getAsString());
        Assert.assertEquals("value2", jsonObject.get("name2").getAsString());
    }

    @Test
    public void testName() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();

        writer.beginObject();
        writer.name("name1");
        writer.value("value1");
        writer.name("name2");
        writer.value("value2");
        writer.endObject();

        JsonElement element = writer.get();

        Assert.assertNotNull(element);
        Assert.assertTrue(element.isJsonObject());

        JsonObject jsonObject = element.getAsJsonObject();
        Assert.assertEquals("value1", jsonObject.get("name1").getAsString());
        Assert.assertEquals("value2", jsonObject.get("name2").getAsString());
    }

    @Test
    public void testValueString() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();

        writer.beginObject();
        writer.name("name1");
        writer.value("value1");
        writer.endObject();

        JsonElement element = writer.get();

        Assert.assertNotNull(element);
        Assert.assertTrue(element.isJsonObject());

        JsonObject jsonObject = element.getAsJsonObject();
        Assert.assertEquals("value1", jsonObject.get("name1").getAsString());
    }

    @Test
    public void testValueBoolean() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();

        writer.beginObject();
        writer.name("name1");
        writer.value(true);
        writer.endObject();

        JsonElement element = writer.get();

        Assert.assertNotNull(element);
        Assert.assertTrue(element.isJsonObject());

        JsonObject jsonObject = element.getAsJsonObject();
        Assert.assertEquals(true, jsonObject.get("name1").getAsBoolean());
    }

    @Test
    public void testValueDouble() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();

        writer.beginObject();
        writer.name("name1");
        writer.value(3.14);
        writer.endObject();

        JsonElement element = writer.get();

        Assert.assertNotNull(element);
        Assert.assertTrue(element.isJsonObject());

        JsonObject jsonObject = element.getAsJsonObject();
        Assert.assertEquals(3.14, jsonObject.get("name1").getAsDouble(), 0.001);
    }

    @Test
    public void testValueLong() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();

        writer.beginObject();
        writer.name("name1");
        writer.value(1000L);
        writer.endObject();

        JsonElement element = writer.get();

        Assert.assertNotNull(element);
        Assert.assertTrue(element.isJsonObject());

        JsonObject jsonObject = element.getAsJsonObject();
        Assert.assertEquals(1000L, jsonObject.get("name1").getAsLong());
    }

    @Test
    public void testValueNumber() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();

        writer.beginObject();
        writer.name("name1");
        writer.value(3.14);
        writer.endObject();

        JsonElement element = writer.get();

        Assert.assertNotNull(element);
        Assert.assertTrue(element.isJsonObject());

        JsonObject jsonObject = element.getAsJsonObject();
        Assert.assertEquals(3.14, jsonObject.get("name1").getAsDouble(), 0.001);
    }

    @Test(expected = IllegalStateException.class)
    public void testEndArrayWithoutBeginArray() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();

        writer.endArray();
    }

    @Test(expected = IllegalStateException.class)
    public void testEndObjectWithoutBeginObject() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();

        writer.endObject();
    }

    @Test(expected = IllegalStateException.class)
    public void testNameWithoutBeginObject() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();

        writer.name("name");
    }

    @Test(expected = IOException.class)
    public void testFlush() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();

        writer.flush();
    }

    @Test(expected = IOException.class)
    public void testCloseIncompleteDocument() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();

        writer.beginObject();
        writer.name("name1");
        writer.value("value1");

        writer.close();
    }
}
