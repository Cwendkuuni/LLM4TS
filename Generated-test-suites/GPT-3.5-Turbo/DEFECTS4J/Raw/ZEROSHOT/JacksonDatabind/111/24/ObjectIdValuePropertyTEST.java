import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.PropertyMetadata;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.NullValueProvider;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.PropertyName;
import java.io.IOException;
import java.lang.annotation.Annotation;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;

public class ObjectIdValuePropertyTest {

    @Test
    public void testConstructor1() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        assertNotNull(property);
        assertEquals(objectIdReader, property._objectIdReader);
    }
    
    @Test
    public void testConstructor2() {
        ObjectIdValueProperty src = new ObjectIdValueProperty();
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        NullValueProvider nva = new NullValueProvider();
        ObjectIdValueProperty property = new ObjectIdValueProperty(src, deser, nva);
        assertNotNull(property);
        assertEquals(src._objectIdReader, property._objectIdReader);
    }
    
    @Test
    public void testConstructor3() {
        ObjectIdValueProperty src = new ObjectIdValueProperty();
        PropertyName newName = new PropertyName();
        ObjectIdValueProperty property = new ObjectIdValueProperty(src, newName);
        assertNotNull(property);
        assertEquals(src._objectIdReader, property._objectIdReader);
    }
    
    @Test
    public void testWithDeser() {
        ObjectIdValueProperty src = new ObjectIdValueProperty();
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        NullValueProvider nva = new NullValueProvider();
        ObjectIdValueProperty property = new ObjectIdValueProperty(src, deser, nva);
        SettableBeanProperty result = property.withValueDeserializer(deser);
        assertNotSame(property, result);
        assertEquals(src._objectIdReader, result._objectIdReader);
    }
    
    @Test
    public void testWithNullProvider() {
        ObjectIdValueProperty src = new ObjectIdValueProperty();
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        NullValueProvider nva = new NullValueProvider();
        ObjectIdValueProperty property = new ObjectIdValueProperty(src, deser, nva);
        SettableBeanProperty result = property.withNullProvider(nva);
        assertNotSame(property, result);
        assertEquals(src._objectIdReader, result._objectIdReader);
    }
    
    @Test
    public void testGetName() {
        ObjectIdValueProperty property = new ObjectIdValueProperty();
        PropertyName newName = new PropertyName();
        SettableBeanProperty result = property.withName(newName);
        assertNotSame(property, result);
        assertEquals(property._objectIdReader, result._objectIdReader);
    }
    
    @Test
    public void testGetAnnotation() {
        ObjectIdValueProperty property = new ObjectIdValueProperty();
        Class<Annotation> acls = Annotation.class;
        assertNull(property.getAnnotation(acls));
    }
    
    @Test
    public void testGetMember() {
        ObjectIdValueProperty property = new ObjectIdValueProperty();
        AnnotatedMember member = property.getMember();
        assertNull(member);
    }
    
    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty();
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        property.deserializeAndSet(p, ctxt, instance);
        p.close();
        ctxt.close();
    }
    
    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty();
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        Object result = property.deserializeSetAndReturn(p, ctxt, instance);
        p.close();
        ctxt.close();
        assertNull(result);
    }
    
    @Test
    public void testSet() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty();
        Object instance = new Object();
        Object value = new Object();
        try {
            property.set(instance, value);
        } catch(UnsupportedOperationException e) {
            assertEquals("Should not call set() on ObjectIdProperty that has no SettableBeanProperty", e.getMessage());
        }
    }
    
    @Test
    public void testSetAndReturn() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty();
        Object instance = new Object();
        Object value = new Object();
        try {
            property.setAndReturn(instance, value);
        } catch(UnsupportedOperationException e) {
            assertEquals("Should not call set() on ObjectIdProperty that has no SettableBeanProperty", e.getMessage());
        }
    }
}