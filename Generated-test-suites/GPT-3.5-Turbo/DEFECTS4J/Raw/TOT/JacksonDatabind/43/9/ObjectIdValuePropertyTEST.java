
import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectIdValuePropertyTest {
    
    @Test
    public void testWithNullDeser() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        objectIdReader.propertyName = new PropertyName("id");
        objectIdReader.setIdType(Object.class);
        objectIdReader.deserializer = null;
        
        PropertyMetadata metadata = new PropertyMetadata();
        
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        
        assertNull(property.getAnnotation(Override.class));
        assertNull(property.getMember());
        
        try {
            property.deserializeAndSet(null, null, null);
            fail("Exception should be thrown");
        } catch (IOException e) {
            // expected IOException
        }
        
        assertNull(property.deserializeSetAndReturn(null, null, null));
        
        try {
            property.set(null, null);
            fail("Exception should be thrown");
        } catch (IOException e) {
            // expected IOException
        }
        
        try {
            property.setAndReturn(null, null);
            fail("Exception should be thrown");
        } catch (IOException e) {
            // expected IOException
        }
    }
    
    @Test
    public void testWithNonNullDeser() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        objectIdReader.propertyName = new PropertyName("id");
        objectIdReader.setIdType(Object.class);
        objectIdReader.deserializer = new JsonDeserializer<>();
        
        PropertyMetadata metadata = new PropertyMetadata();
        
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        
        assertNull(property.getAnnotation(Override.class));
        assertNull(property.getMember());
        
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = null;
        
        try {
            property.deserializeAndSet(p, ctxt, instance);
            fail("Exception should be thrown");
        } catch (IOException e) {
            // expected IOException
        }
        
        assertNull(property.deserializeSetAndReturn(p, ctxt, instance));
        
        try {
            property.set(instance, null);
            fail("Exception should be thrown");
        } catch (IOException e) {
            // expected IOException
        }
        
        try {
            property.setAndReturn(instance, null);
            fail("Exception should be thrown");
        } catch (IOException e) {
            // expected IOException
        }
    }
    
    @Test
    public void testWithNullId() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        objectIdReader.propertyName = new PropertyName("id");
        objectIdReader.setIdType(Object.class);
        objectIdReader.deserializer = new JsonDeserializer<>();
        
        PropertyMetadata metadata = new PropertyMetadata();
        
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        
        assertNull(property.getAnnotation(Override.class));
        assertNull(property.getMember());
        
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = new Object();
        
        assertNull(property.deserializeSetAndReturn(p, ctxt, instance));
    }
    
    @Test
    public void testWithNonNullId() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        objectIdReader.propertyName = new PropertyName("id");
        objectIdReader.setIdType(Object.class);
        objectIdReader.deserializer = new JsonDeserializer<>();
        
        PropertyMetadata metadata = new PropertyMetadata();
        
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        
        assertNull(property.getAnnotation(Override.class));
        assertNull(property.getMember());
        
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = new Object();
        Object id = new Object();
        
        SettableBeanProperty idProp = new SettableBeanProperty();
        objectIdReader.idProperty = idProp;
        
        try {
            property.deserializeSetAndReturn(p, ctxt, instance);
            fail("Exception should be thrown");
        } catch (IOException e) {
            // expected IOException
        }
        
        try {
            property.setAndReturn(instance, id);
            fail("Exception should be thrown");
        } catch (IOException e) {
            // expected IOException
        }
    }
    
}
