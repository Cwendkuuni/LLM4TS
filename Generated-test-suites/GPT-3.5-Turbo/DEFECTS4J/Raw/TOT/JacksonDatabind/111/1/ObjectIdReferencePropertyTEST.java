import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdReferencePropertyTest {
    
    private ObjectIdReferenceProperty property;
    private TestObject obj;
    
    @Before
    public void setUp() {
        property = new ObjectIdReferenceProperty(null, null);
        obj = new TestObject();
    }
    
    @Test
    public void testWithNullName() {
        property.withName(null);
        assertNull(property.getFullName());
    }
    
    @Test
    public void testWithNonNullName() {
        property.withName(PropertyName.construct("testProperty"));
        assertNotNull(property.getFullName());
    }
    
    @Test
    public void testWithValueDeserializer() {
        property.withValueDeserializer(null);
        assertNull(property.getValueDeserializer());
    }
    
    @Test
    public void testWithNonNullValueDeserializer() {
        property.withValueDeserializer(new TestDeserializer());
        assertNotNull(property.getValueDeserializer());
    }
    
    @Test
    public void testWithNullProvider() {
        property.withNullProvider(null);
        assertNull(property.getNullProvider());
    }
    
    @Test
    public void testWithNonNullProvider() {
        property.withNullProvider(new TestNullValueProvider());
        assertNotNull(property.getNullProvider());
    }
    
    @Test
    public void testFixAccess() {
        property.fixAccess(null);
        // Check if fixAccess() method runs without any exception.
        assertTrue(true);
    }
    
    @Test
    public void testGetAnnotation() {
        assertNull(property.getAnnotation(null));
    }
    
    @Test
    public void testGetMember() {
        assertNull(property.getMember());
    }
    
    @Test
    public void testGetCreatorIndex() {
        assertEquals(-1, property.getCreatorIndex());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testHandleResolvedForwardReferenceWithInvalidId() throws IOException {
        TestObject.referencingProperty = new ObjectIdReferenceProperty.PropertyReferring(property, null, null, obj);
        TestObject.referencingProperty.handleResolvedForwardReference("invalidId", new TestObject());
    }
    
    @Test
    public void testHandleResolvedForwardReferenceWithValidId() throws IOException {
        TestObject.referencingProperty = new ObjectIdReferenceProperty.PropertyReferring(property, null, null, obj);
        TestObject.referencingProperty.handleResolvedForwardReference("validId", new TestObject());
        assertNotNull(obj.getReferencedObject());
    }
    
    private static class TestObject {
        public static ObjectIdReferenceProperty.PropertyReferring referencingProperty;
        private TestObject referencedObject;
        
        public void setReferencedObject(TestObject obj) {
            this.referencedObject = obj;
        }
        
        public TestObject getReferencedObject() {
            return referencedObject;
        }
    }
    
    private static class TestDeserializer extends JsonDeserializer<Object> {
        @Override
        public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            return null;
        }
    }
    
    private static class TestNullValueProvider implements NullValueProvider {
        @Override
        public Object getNullValue(DeserializationContext ctxt) throws JsonMappingException {
            return null;
        }
    }
}