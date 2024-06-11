import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ObjectIdReferencePropertyTest {

    private ObjectIdReferenceProperty objectIdReferenceProperty;

    @Before
    public void setUp() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null, null);
        objectIdReferenceProperty = new ObjectIdReferenceProperty(null, objectIdInfo);
    }

    @Test
    public void testWithName() {
        PropertyName newName = new PropertyName("newName");
        ObjectIdReferenceProperty result = (ObjectIdReferenceProperty) objectIdReferenceProperty.withName(newName);
        assertEquals(newName, result.getFullName());
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> deser = new JsonDeserializer<Object>() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };
        ObjectIdReferenceProperty result = (ObjectIdReferenceProperty) objectIdReferenceProperty.withValueDeserializer(deser);
        assertEquals(deser, result.getValueDeserializer());
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider nva = new NullValueProvider() {
            @Override
            public Object getNullValue(DeserializationContext ctxt) throws JsonMappingException {
                return null;
            }
        };
        ObjectIdReferenceProperty result = (ObjectIdReferenceProperty) objectIdReferenceProperty.withNullProvider(nva);
        assertEquals(nva, result.getNullValueProvider());
    }

    @Test
    public void testFixAccess() {
        DeserializationConfig config = new DeserializationConfig(null);
        objectIdReferenceProperty.fixAccess(config);
    }

    @Test
    public void testGetAnnotation() {
        Class<JsonProperty> acls = JsonProperty.class;
        JsonProperty result = objectIdReferenceProperty.getAnnotation(acls);
        assertNull(result);
    }

    @Test
    public void testGetMember() {
        AnnotatedMember result = objectIdReferenceProperty.getMember();
        assertNull(result);
    }

    @Test
    public void testGetCreatorIndex() {
        int result = objectIdReferenceProperty.getCreatorIndex();
        assertEquals(-1, result);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = null;
        objectIdReferenceProperty.deserializeAndSet(p, ctxt, instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = null;
        Object result = objectIdReferenceProperty.deserializeSetAndReturn(p, ctxt, instance);
        assertNull(result);
    }

    @Test
    public void testSet() throws IOException {
        Object instance = null;
        Object value = null;
        objectIdReferenceProperty.set(instance, value);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = null;
        Object value = null;
        Object result = objectIdReferenceProperty.setAndReturn(instance, value);
        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPropertyReferringHandleResolvedForwardReference() throws IOException {
        ObjectIdReferenceProperty.UnresolvedForwardReference ref = null;
        Class<?> type = null;
        Object ob = null;
        ObjectIdReferenceProperty.PropertyReferring propertyReferring = new ObjectIdReferenceProperty.PropertyReferring(objectIdReferenceProperty, ref, type, ob);
        propertyReferring.handleResolvedForwardReference("id", "value");
    }
}