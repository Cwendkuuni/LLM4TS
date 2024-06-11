
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StringArrayDeserializerTest {

    private StringArrayDeserializer deserializer;

    @Before
    public void setup() {
        deserializer = new StringArrayDeserializer();
    }

    @Test
    public void testDeserialize() throws Exception {
        String json = "[\"value1\", \"value2\", \"value3\"]";
        JsonParser parser = new JsonFactory().createParser(json);

        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        String[] result = deserializer.deserialize(parser, ctxt);

        Assert.assertEquals(3, result.length);
        Assert.assertEquals("value1", result[0]);
        Assert.assertEquals("value2", result[1]);
        Assert.assertEquals("value3", result[2]);
    }

    @Test
    public void testDeserializeWithNullValues() throws Exception {
        String json = "[null, null, null]";
        JsonParser parser = new JsonFactory().createParser(json);

        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        String[] result = deserializer.deserialize(parser, ctxt);

        Assert.assertEquals(3, result.length);
        Assert.assertNull(result[0]);
        Assert.assertNull(result[1]);
        Assert.assertNull(result[2]);
    }

    @Test
    public void testDeserializeWithEmptyArray() throws Exception {
        String json = "[]";
        JsonParser parser = new JsonFactory().createParser(json);

        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        String[] result = deserializer.deserialize(parser, ctxt);

        Assert.assertEquals(0, result.length);
    }

    @Test
    public void testDeserializeWithNonArrayInput() throws Exception {
        String json = "\"value\"";
        JsonParser parser = new JsonFactory().createParser(json);

        DeserializationContext ctxt = new DeserializationContext(null, null, null);

        try {
            deserializer.deserialize(parser, ctxt);
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testDeserializeWithEmptyStringAsNull() throws Exception {
        String json = "\"\"";
        JsonParser parser = new JsonFactory().createParser(json);

        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        ctxt.getConfig().enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);

        String[] result = deserializer.deserialize(parser, ctxt);

        Assert.assertNull(result);
    }

    @Test
    public void testDeserializeWithSingleValueArray() throws Exception {
        String json = "\"value\"";
        JsonParser parser = new JsonFactory().createParser(json);

        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        ctxt.getConfig().enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

        String[] result = deserializer.deserialize(parser, ctxt);

        Assert.assertEquals(1, result.length);
        Assert.assertEquals("value", result[0]);
    }

}
