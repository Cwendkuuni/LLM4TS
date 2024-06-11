import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class InnerClassPropertyTest {
    
    private InnerClassProperty property;
    
    @Before
    public void setUp() {
        SettableBeanProperty delegate = new SettableBeanProperty("delegate", null, null, null);
        Constructor<?> ctor = new Constructor<>();
        property = new InnerClassProperty(delegate, ctor);
    }
    
    @Test
    public void testConstructor() {
        assertNotNull(property);
        assertEquals("delegate", property.getName());
    }
    
    @Test
    public void testWithname() {
        InnerClassProperty newProperty = property.withName(PropertyName.construct("newName"));
        assertNotNull(newProperty);
        assertEquals("newName", newProperty.getName());
    }
    
    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        InnerClassProperty newProperty = property.withValueDeserializer(deser);
        assertNotNull(newProperty);
        assertNotNull(newProperty.getValueDeserializer());
    }
    
    @Test
    public void testAssignIndex() {
        property.assignIndex(1);
        assertEquals(1, property.getPropertyIndex());
    }
    
    @Test
    public void testGetAnnotation() {
        Class<Annotation> acls = Annotation.class;
        assertNull(property.getAnnotation(acls));
    }
    
    @Test
    public void testGetMember() {
        assertNull(property.getMember());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testMissingConstructor() {
        InnerClassProperty src = new InnerClassProperty(property, null);
    }
    
    @Test
    public void testDeserializeAndSet() {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        Object bean = new Object();
        try {
            property.deserializeAndSet(jp, ctxt, bean);
        } catch (IOException e) {
            assertNotNull(e);
        }
    }
    
    @Test
    public void testDeserializeSetAndReturn() {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        Object instance = new Object();
        try {
            Object value = property.deserializeSetAndReturn(jp, ctxt, instance);
            assertNotNull(value);
        } catch (IOException e) {
            assertNotNull(e);
        }
    }
    
    @Test
    public void testSetAndReturn() {
        Object instance = new Object();
        try {
            Object value = property.setAndReturn(instance, new Object());
            assertNotNull(value);
        } catch (IOException e) {
            assertNotNull(e);
        }
    }
    
    @Test
    public void testReadResolve() {
        InnerClassProperty newProperty = (InnerClassProperty) property.readResolve();
        assertNotNull(newProperty);
        assertNull(newProperty.getAnnotated());
    }
    
    @Test
    public void testWriteReplace() {
        Object replaced = property.writeReplace();
        assertNotNull(replaced);
        assertTrue(replaced instanceof InnerClassProperty);
        InnerClassProperty newProperty = (InnerClassProperty) replaced;
        assertNotNull(newProperty.getAnnotated());
    }
}