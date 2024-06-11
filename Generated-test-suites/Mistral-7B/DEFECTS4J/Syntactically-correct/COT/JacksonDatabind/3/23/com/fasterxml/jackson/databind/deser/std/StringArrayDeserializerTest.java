import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Assert;
import org.junit.Test;

public class StringArrayDeserializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testDeserializeDefault() throws IOException {
        String json = "[\"hello\", \"world\", \"test\"]";
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.deserialize(mapper.parser().readValueAsTree(json), mapper.deserializationContext());
        Assert.assertArrayEquals(new String[]{"hello", "world", "test"}, result);
    }

    @Test
    public void testDeserializeCustomElementDeserializer() throws IOException {
        String json = "[null, \"hello\", \"world\", \"test\"]";
        JsonDeserializer<String> customDeserializer = mapper.deserializationContext().findBeanDeserializer(String.class);
        StringArrayDeserializer deserializer = new StringArrayDeserializer(customDeserializer);
        String[] result = deserializer.deserialize(mapper.parser().readValueAsTree(json), mapper.deserializationContext());
        Assert.assertArrayEquals(new String[]{"null", "hello", "world", "test"}, result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeNotStartArrayToken() throws IOException {
        String json = "{\"name\": \"John\"}";
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        deserializer.deserialize(mapper.parser().readValueAsTree(json), mapper.deserializationContext());
    }

    @Test(expected = IOException.class)
    public void testDeserializeNullValueString() throws IOException {
        String json = "[null, null, null]";
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        deserializer.deserialize(mapper.parser().readValueAsTree(json), mapper.deserializationContext());
    }

    @Test(expected = IOException.class)
    public void testDeserializeNonStringValue() throws IOException {
        String json = "[1, 2, 3]";
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        deserializer.deserialize(mapper.parser().readValueAsTree(json), mapper.deserializationContext());
    }

    @Test
    public void testDeserializeEmptyArray() throws IOException {
        String json = "[]";
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.deserialize(mapper.parser().readValueAsTree(json), mapper.deserializationContext());
        Assert.assertArrayEquals(new String[0], result);
    }

    @Test
    public void testDeserializeNullResult() throws IOException {
        String json = "[null]";
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.deserialize(mapper.parser().readValueAsTree(json), mapper.deserializationContext());
        Assert.assertArrayEquals(new String[]{null}, result);
    }

    @Test
    public void testDeserializeTypeDeserializer() throws IOException {
        Class<String[]> clazz = String[].class;
        TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructTypeDeserializer(clazz);
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserializeWithType(mapper.parser().readValueAsTree("[\"hello\", \"world\"]"), mapper.deserializationContext(), typeDeserializer);
        Assert.assertArrayEquals(new String[]{"hello", "world"}, result);
    }

    @Test
    public void testHandleNonArrayNullValue() throws IOException {
        JsonParser jp = mapper.parser();
        jp.nextToken(); // [JACKSON-526] implicit arrays from single values?
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.handleNonArray(jp, mapper.deserializationContext());
        Assert.assertArrayEquals(new String[]{null}, result);
    }

    @Test
    public void testHandleNonArrayEmptyString() throws IOException {
        JsonParser jp = mapper.parser();
        jp.nextToken(JsonToken.VALUE_STRING);
        jp.nextToken(JsonToken.FIELD_NAME);
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.handleNonArray(jp, mapper.deserializationContext());
        Assert.assertArrayEquals(new String[]{""}, result);
    }

    @Test
    public void testHandleNonArrayNotStringValue() throws IOException {
        JsonParser jp = mapper.parser();
        jp.nextToken(JsonToken.VALUE_NUMBER_INT);
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.handleNonArray(jp, mapper.deserializationContext());
        Assert.assertArrayEquals(new String[0], result);
    }

    @Test
    public void testCreateContextualNullValueDeserializer() throws IOException {
        DeserializationContext ctxt = mapper.deserializationContext();
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonDeserializer<?> result = deserializer.createContextual(ctxt, null);
        Assert.assertNull(result);
    }

    @Test
    public void testCreateContextualStringDeserializer() throws IOException {
        DeserializationContext ctxt = mapper.deserializationContext();
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonDeserializer<String> stringDeserializer = mapper.deserializationContext().findBeanDeserializer(String.class);
        JsonDeserializer<?> result = deserializer.createContextual(ctxt, null);
        Assert.assertEquals(StringArrayDeserializer.class, result.getClass());
        Assert.assertNotNull(result.getValueDeserializer());
        Assert.assertEquals(stringDeserializer, result.getValueDeserializer());
    }
}