import org.junit.Assert;
import org.junit.Test;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey_string() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer.StringKD(String.class);
        String key = "test";
        DeserializationContext ctxt = new DeserializationContext();

        Object result = keyDeserializer.deserializeKey(key, ctxt);

        Assert.assertEquals(key, result);
    }

    @Test
    public void testDeserializeKey_object() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer.StringKD(Object.class);
        String key = "test";
        DeserializationContext ctxt = new DeserializationContext();

        Object result = keyDeserializer.deserializeKey(key, ctxt);

        Assert.assertEquals(key, result);
    }

    @Test
    public void testDeserializeKey_boolean_true() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer.StdKeyDeserializer(TYPE_BOOLEAN, Boolean.class);
        String key = "true";
        DeserializationContext ctxt = new DeserializationContext();

        Object result = keyDeserializer.deserializeKey(key, ctxt);

        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testDeserializeKey_boolean_false() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer.StdKeyDeserializer(TYPE_BOOLEAN, Boolean.class);
        String key = "false";
        DeserializationContext ctxt = new DeserializationContext();

        Object result = keyDeserializer.deserializeKey(key, ctxt);

        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testDeserializeKey_boolean_invalidValue() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer.StdKeyDeserializer(TYPE_BOOLEAN, Boolean.class);
        String key = "invalid";
        DeserializationContext ctxt = new DeserializationContext();

        Object result = keyDeserializer.deserializeKey(key, ctxt);

        Assert.assertNull(result);
    }

    // TODO: Implement test cases for the remaining methods

}