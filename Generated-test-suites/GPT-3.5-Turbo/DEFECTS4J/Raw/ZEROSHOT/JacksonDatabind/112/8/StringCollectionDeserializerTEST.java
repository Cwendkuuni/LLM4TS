import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class StringCollectionDeserializerTest {

    @Test
    public void testDeserialize() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null);
        JsonParser parser = null;
        DeserializationContext ctxt = null;
        Collection<String> result = new ArrayList<>();
        Collection<String> expected = new ArrayList<>();
        expected.add("value1");
        expected.add("value2");
        expected.add("value3");

        /* Test with valid input */
        parser = createJsonParser("[\"value1\", \"value2\", \"value3\"]");
        deserializer.deserialize(parser, ctxt, result);
        Assert.assertEquals(expected, result);

        /* Test with null value */
        result.clear();
        expected.clear();
        expected.add(null);
        parser = createJsonParser("[null]");
        deserializer.deserialize(parser, ctxt, result);
        Assert.assertEquals(expected, result);

        /* Test with invalid input */
        result.clear();
        expected.clear();
        expected.add("value1");
        expected.add("value2");
        parser = createJsonParser("{\"key1\":\"value1\",\"key2\":\"value2\"}");
        deserializer.deserialize(parser, ctxt, result);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testDeserializeUsingCustom() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null);
        JsonParser parser = null;
        DeserializationContext ctxt = null;
        Collection<String> result = new ArrayList<>();
        Collection<String> expected = new ArrayList<>();
        expected.add("value1");
        expected.add("value2");
        expected.add("value3");

        JsonDeserializer<String> valueDeser = new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return p.getValueAsString().toUpperCase();
            }
        };

        /* Test with valid input */
        parser = createJsonParser("[\"value1\", \"value2\", \"value3\"]");
        deserializer.deserializeUsingCustom(parser, ctxt, result, valueDeser);
        Assert.assertEquals(expected, result);

        /* Test with null value */
        result.clear();
        expected.clear();
        expected.add(null);
        parser = createJsonParser("[null]");
        deserializer.deserializeUsingCustom(parser, ctxt, result, valueDeser);
        Assert.assertEquals(expected, result);

        /* Test with invalid input */
        result.clear();
        expected.clear();
        expected.add("KEY1");
        expected.add("KEY2");
        parser = createJsonParser("{\"key1\":\"value1\",\"key2\":\"value2\"}");
        deserializer.deserializeUsingCustom(parser, ctxt, result, valueDeser);
        Assert.assertEquals(expected, result);
    }

    private JsonParser createJsonParser(String json) throws IOException {
        JsonFactory factory = new JsonFactory();
        return factory.createParser(json);
    }
}