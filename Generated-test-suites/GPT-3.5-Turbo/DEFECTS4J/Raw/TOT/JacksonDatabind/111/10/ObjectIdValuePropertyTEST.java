import org.junit.Assert;
import org.junit.Test;

public class ObjectIdValuePropertyTest {

    @Test
    public void testDeserializeAndSet_NullValue_ReturnsNull() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        Object instance = new Object();
        Assert.assertNull(property.deserializeAndSet(null, null, instance));
    }

    @Test
    public void testDeserializeAndSet_ValidValue_ReturnsInstance() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        Object instance = new Object();
        Assert.assertEquals(instance, property.deserializeAndSet(null, null, instance));
    }

    @Test
    public void testSetAndReturn_NoIdProperty_ThrowsUnsupportedOperationException() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        Object instance = new Object();
        try {
            property.setAndReturn(instance, null);
            Assert.fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testSetAndReturn_IdPropertyExists_ReturnsIdPropertySetResult() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        SettableBeanProperty idProperty = new SettableBeanProperty(null, null, null, null) {
            @Override
            public void set(Object instance, Object value) throws IOException {
                // Mock implementation
            }

            @Override
            public Object setAndReturn(Object instance, Object value) throws IOException {
                return "IdPropertySetResult";
            }
        };
        property.setObjectIdReader(new ObjectIdReader(null, idProperty));
        Object instance = new Object();
        Assert.assertEquals("IdPropertySetResult", property.setAndReturn(instance, null));
    }

    @Test
    public void testWithNullProvider_ReturnsNewObjectIdValuePropertyInstance() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        NullValueProvider nva = new NullValueProvider() {
            @Override
            public Object getNullValue(DeserializationContext ctxt) throws JsonMappingException {
                return null;
            }
        };
        Assert.assertNotSame(property, property.withNullProvider(nva));
    }

    @Test
    public void testWithValueDeserializer_DifferentDeserializer_ReturnsNewObjectIdValuePropertyInstance() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        JsonDeserializer<?> deser = new JsonDeserializer<Object>() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
                return null;
            }
        };
        Assert.assertNotSame(property, property.withValueDeserializer(deser));
    }

    @Test
    public void testWithName_DifferentName_ReturnsNewObjectIdValuePropertyInstance() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        PropertyName newName = new PropertyName("newName");
        Assert.assertNotSame(property, property.withName(newName));
    }

    @Test
    public void testGetMember_ReturnsNull() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        Assert.assertNull(property.getMember());
    }

    @Test
    public void testGetAnnotation_ReturnsNull() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        Assert.assertNull(property.getAnnotation(null));
    }
}