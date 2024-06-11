import org.junit.*; 
import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {
    
    @Test
    public void testConstructor() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty prop = new ObjectIdValueProperty(objectIdReader, metadata);
        
        assertNotNull(prop);
        assertEquals(objectIdReader.propertyName, prop.getName());
        assertEquals(objectIdReader.getIdType(), prop.getType());
        assertEquals(metadata, prop.getMetadata());
        assertEquals(objectIdReader.getDeserializer(), prop.getValueDeserializer());
        assertEquals(objectIdReader, prop._objectIdReader);
    }
    
    @Test
    public void testWithDeserializer() {
        ObjectIdValueProperty prop = new ObjectIdValueProperty(new ObjectIdReader(), new JsonDeserializer());
        JsonDeserializer<?> newDeserializer = new JsonDeserializer();
        ObjectIdValueProperty newProp = prop.withValueDeserializer(newDeserializer);
        
        assertNotNull(newProp);
        assertEquals(newDeserializer, newProp.getValueDeserializer());
        assertEquals(prop._objectIdReader, newProp._objectIdReader);
    }
    
    @Test
    public void testWithName() {
        ObjectIdValueProperty prop = new ObjectIdValueProperty(new ObjectIdReader(), new JsonDeserializer());
        PropertyName newName = new PropertyName("newName");
        ObjectIdValueProperty newProp = prop.withName(newName);
        
        assertNotNull(newProp);
        assertEquals(newName, newProp.getFullName());
        assertEquals(prop.getType(), newProp.getType());
        assertEquals(prop.getMetadata(), newProp.getMetadata());
        assertEquals(prop.getValueDeserializer(), newProp.getValueDeserializer());
        assertEquals(prop._objectIdReader, newProp._objectIdReader);
    }
    
    @Test
    public void testGetAnnotation() {
        ObjectIdValueProperty prop = new ObjectIdValueProperty(new ObjectIdReader(), new JsonDeserializer());
        Class<A> acls = A.class;
        A annotation = prop.getAnnotation(acls);
        
        assertNull(annotation);
    }
    
    @Test
    public void testGetMember() {
        ObjectIdValueProperty prop = new ObjectIdValueProperty(new ObjectIdReader(), new JsonDeserializer());
        AnnotatedMember member = prop.getMember();
        
        assertNull(member);
    }
    
    @Test
    public void testDeserializeAndSet() {
        ObjectIdValueProperty prop = new ObjectIdValueProperty(new ObjectIdReader(), new JsonDeserializer());
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = "instance";
        
        try {
            prop.deserializeAndSet(p, ctxt, instance);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
    
    @Test
    public void testDeserializeSetAndReturn() {
        ObjectIdValueProperty prop = new ObjectIdValueProperty(new ObjectIdReader(), new JsonDeserializer());
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = "instance";
        
        try {
            Object result = prop.deserializeSetAndReturn(p, ctxt, instance);
            assertEquals(instance, result);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
    
    @Test
    public void testSet() {
        ObjectIdValueProperty prop = new ObjectIdValueProperty(new ObjectIdReader(), new JsonDeserializer());
        Object instance = "instance";
        Object value = "value";
        
        try {
            prop.set(instance, value);
        } catch (IOException e) {
           fail("IOException should not be thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("Should not call set() on ObjectIdProperty that has no SettableBeanProperty", e.getMessage());
        }
    }
    
    @Test
    public void testSetAndReturn() {
        ObjectIdValueProperty prop = new ObjectIdValueProperty(new ObjectIdReader(), new JsonDeserializer());
        Object instance = "instance";
        Object value = "value";
        
        try {
            prop.setAndReturn(instance, value);
            fail("UnsupportedOperationException should be thrown");
        } catch (IOException e) {
            fail("IOException should not be thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("Should not call set() on ObjectIdProperty that has no SettableBeanProperty", e.getMessage());
        }
    }
}