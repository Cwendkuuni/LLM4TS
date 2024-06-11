import org.junit.Assert;
import org.junit.Test;

public class StringArrayDeserializerTest {

    @Test
    public void testDeserialize() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser parser = createJsonParser("['value1', 'value2', 'value3']");
        DeserializationContext context = createDeserializationContext();

        String[] result = deserializer.deserialize(parser, context);

        Assert.assertArrayEquals(new String[]{"value1", "value2", "value3"}, result);
    }

    @Test
    public void testDeserializeEmptyArray() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser parser = createJsonParser("[]");
        DeserializationContext context = createDeserializationContext();

        String[] result = deserializer.deserialize(parser, context);

        Assert.assertArrayEquals(new String[]{}, result);
    }

    @Test
    public void testDeserializeWithNullValues() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser parser = createJsonParser("[null, null, null]");
        DeserializationContext context = createDeserializationContext();

        String[] result = deserializer.deserialize(parser, context);

        Assert.assertArrayEquals(new String[]{null, null, null}, result);
    }

    @Test
    public void testDeserializeWithMixedValues() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser parser = createJsonParser("['value1', null, 'value2']");
        DeserializationContext context = createDeserializationContext();

        String[] result = deserializer.deserialize(parser, context);

        Assert.assertArrayEquals(new String[]{"value1", null, "value2"}, result);
    }

    @Test
    public void testDeserializeNonArray() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser parser = createJsonParser("'value'");
        DeserializationContext context = createDeserializationContext();

        String[] result = deserializer.deserialize(parser, context);

        Assert.assertArrayEquals(new String[]{"value"}, result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeNonArrayWhenNotAcceptSingleValueAsArray() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser parser = createJsonParser("'value'");
        DeserializationContext context = createDeserializationContext();
        context.disable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

        String[] result = deserializer.deserialize(parser, context);
    }

    @Test
    public void testDeserializeEmptyStringAsNull() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser parser = createJsonParser("['', '', '']");
        DeserializationContext context = createDeserializationContext();
        context.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);

        String[] result = deserializer.deserialize(parser, context);

        Assert.assertArrayEquals(new String[]{null, null, null}, result);
    }

    private JsonParser createJsonParser(String json) throws IOException {
        JsonFactory factory = new JsonFactory();
        return factory.createParser(json);
    }

    private DeserializationContext createDeserializationContext() {
        return new DeserializationContext(null, null, null);
    }

}