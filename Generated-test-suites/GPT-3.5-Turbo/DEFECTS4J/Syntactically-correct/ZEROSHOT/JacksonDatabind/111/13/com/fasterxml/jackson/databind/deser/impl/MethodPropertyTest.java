import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MethodPropertyTest {

    private MethodProperty methodProperty;

    @Before
    public void setUp() {
        methodProperty = new MethodProperty(null, null, null, null, null);
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider nullProvider = new NullValueProvider();
        MethodProperty result = methodProperty.withNullProvider(nullProvider);
        Assert.assertEquals(nullProvider, result.getNullValueProvider());
    }

    @Test
    public void testWithName() {
        PropertyName propertyName = new PropertyName("name");
        MethodProperty result = methodProperty.withName(propertyName);
        Assert.assertEquals(propertyName, result.getFullName());
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> deserializer = new JsonDeserializer<>();
        MethodProperty result = methodProperty.withValueDeserializer(deserializer);
        Assert.assertEquals(deserializer, result.getValueDeserializer());
    }

    @Test
    public void testDeserializeAndSet() {
        JsonParser jsonParser = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        try {
            methodProperty.deserializeAndSet(jsonParser, ctxt, instance);
        } catch (Exception e) {
            Assert.fail("Exception not expected");
        }
        Assert.assertTrue(true);
    }

    @Test
    public void testDeserializeSetAndReturn() {
        JsonParser jsonParser = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        try {
            methodProperty.deserializeSetAndReturn(jsonParser, ctxt, instance);
        } catch (Exception e) {
            Assert.fail("Exception not expected");
        }
        Assert.assertTrue(true);
    }

    @Test
    public void testSet() {
        Object instance = new Object();
        Object value = new Object();
        try {
            methodProperty.set(instance, value);
        } catch (Exception e) {
            Assert.fail("Exception not expected");
        }
        Assert.assertTrue(true);
    }

    @Test
    public void testSetAndReturn() {
        Object instance = new Object();
        Object value = new Object();
        try {
            methodProperty.setAndReturn(instance, value);
        } catch (Exception e) {
            Assert.fail("Exception not expected");
        }
        Assert.assertTrue(true);
    }

}