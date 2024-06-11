import org.junit.Assert;
import org.junit.Test;

public class MethodPropertyTest {

    @Test
    public void testDeserializeAndSet_NullValue_SkipNullsEnabled_NoChange() {
        MethodProperty property = new MethodProperty(null, null, null, null, null);
        property.setSkipNulls(true);
        JsonParser parser = new JsonParser(null, null);
        DeserializationContext context = new DeserializationContext(null, null);
        Object instance = new Object();

        try {
            property.deserializeAndSet(parser, context, instance);

            // Verify that the instance has not been modified
            Assert.assertEquals(new Object(), instance);
        } catch (IOException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testDeserializeAndSet_NullValue_SkipNullsDisabled_NullValueProvided() {
        MethodProperty property = new MethodProperty(null, null, null, null, null);
        property.setSkipNulls(false);
        NullValueProvider provider = new NullValueProvider();
        property.setNullProvider(provider);
        JsonParser parser = new JsonParser(null, null);
        DeserializationContext context = new DeserializationContext(null, null);
        Object instance = new Object();

        try {
            property.deserializeAndSet(parser, context, instance);

            // Verify that the null value is provided by the null value provider
            Assert.assertEquals(provider.getNullValue(context), instance);
        } catch (IOException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testDeserializeAndSet_NotNullValue_DeserializeValueInvoked_SetterInvoked() {
        MethodProperty property = new MethodProperty(null, null, null, null, null);
        JsonDeserializer deserializer = new JsonDeserializer();
        property.setValueDeserializer(deserializer);
        JsonParser parser = new JsonParser(null, null);
        DeserializationContext context = new DeserializationContext(null, null);
        Object instance = new Object();
        Object value = new Object();

        try {
            property.deserializeAndSet(parser, context, instance);

            // Verify that the deserializer's deserialize method is invoked
            Assert.assertEquals(1, deserializer.getInvocationCount());

            // Verify that the setter is invoked with the deserialized value
            Assert.assertEquals(value, property.getSetter().getInvocationParameter());
        } catch (IOException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testDeserializeAndSet_DeserializedValueIsNull_NullValueProvided() {
        MethodProperty property = new MethodProperty(null, null, null, null, null);
        JsonDeserializer deserializer = new JsonDeserializer();
        property.setValueDeserializer(deserializer);
        NullValueProvider provider = new NullValueProvider();
        property.setNullProvider(provider);
        JsonParser parser = new JsonParser(null, null);
        DeserializationContext context = new DeserializationContext(null, null);
        Object instance = new Object();

        try {
            property.deserializeAndSet(parser, context, instance);

            // Verify that the deserializer's deserialize method is invoked
            Assert.assertEquals(1, deserializer.getInvocationCount());

            // Verify that the null value is provided by the null value provider
            Assert.assertEquals(provider.getNullValue(context), instance);
        } catch (IOException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testDeserializeAndSet_InvocationTargetExceptionThrown_ExceptionThrown() {
        MethodProperty property = new MethodProperty(null, null, null, null, null);
        JsonDeserializer deserializer = new JsonDeserializer();
        property.setValueDeserializer(deserializer);
        JsonParser parser = new JsonParser(null, null);
        DeserializationContext context = new DeserializationContext(null, null);
        Object instance = new Object();
        Object value = new Object();
        property.getSetter().setInvocationThrowsException(true);

        try {
            property.deserializeAndSet(parser, context, instance);

            Assert.fail("Expected IOException was not thrown");
        } catch (IOException e) {
            // Verify that the exception message contains the deserialized value
            Assert.assertTrue(e.getMessage().contains(value.toString()));
        }
    }

    // Additional test cases for other methods...

}