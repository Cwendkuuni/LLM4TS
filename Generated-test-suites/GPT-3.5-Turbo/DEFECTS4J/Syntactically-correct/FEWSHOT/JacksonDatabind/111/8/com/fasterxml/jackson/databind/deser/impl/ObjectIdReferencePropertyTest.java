import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.deser.impl.*;

public class ObjectIdReferencePropertyTest {
    
    @Test
    public void testConstructor1() {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        
        assertNotNull(property);
        assertEquals(forward, property._forward);
        assertEquals(objectIdInfo, property._objectIdInfo);
    }
    
    @Test
    public void testConstructor2() {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        NullValueProvider nva = new NullValueProvider();
        ObjectIdReferenceProperty src = new ObjectIdReferenceProperty(forward, objectIdInfo);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(src, deser, nva);
        
        assertNotNull(property);
        assertEquals(forward, property._forward);
        assertEquals(objectIdInfo, property._objectIdInfo);
        assertEquals(deser, property._valueDeserializer);
        assertEquals(nva, property._nullProvider);
    }
    
    @Test
    public void testConstructor3() {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        PropertyName newName = new PropertyName();
        ObjectIdReferenceProperty src = new ObjectIdReferenceProperty(forward, objectIdInfo);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(src, newName);
        
        assertNotNull(property);
        assertEquals(forward, property._forward);
        assertEquals(objectIdInfo, property._objectIdInfo);
        assertEquals(newName, property._propName);
    }
    
    @Test
    public void testWithName() {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        property._propName = new PropertyName();
        
        PropertyName newName = new PropertyName();
        ObjectIdReferenceProperty newProperty = (ObjectIdReferenceProperty) property.withName(newName);
        
        assertNotNull(newProperty);
        assertEquals(forward, newProperty._forward);
        assertEquals(objectIdInfo, newProperty._objectIdInfo);
        assertEquals(newName, newProperty._propName);
    }
    
    @Test
    public void testWithValueDeserializer() {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        property._valueDeserializer = new JsonDeserializer<>();
        
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        ObjectIdReferenceProperty newProperty = (ObjectIdReferenceProperty) property.withValueDeserializer(deser);
        
        assertNotNull(newProperty);
        assertEquals(forward, newProperty._forward);
        assertEquals(objectIdInfo, newProperty._objectIdInfo);
        assertEquals(deser, newProperty._valueDeserializer);
    }
    
    @Test
    public void testWithNullProvider() {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        property._nullProvider = new NullValueProvider();
        
        NullValueProvider nva = new NullValueProvider();
        ObjectIdReferenceProperty newProperty = (ObjectIdReferenceProperty) property.withNullProvider(nva);
        
        assertNotNull(newProperty);
        assertEquals(forward, newProperty._forward);
        assertEquals(objectIdInfo, newProperty._objectIdInfo);
        assertEquals(nva, newProperty._nullProvider);
    }
    
    @Test
    public void testFixAccess() {
        SettableBeanProperty forward = new SettableBeanProperty();
        forward._valueDeserializer = new JsonDeserializer<>();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        
        DeserializationConfig config = new DeserializationConfig();
        property.fixAccess(config);
        
        assertTrue(property._forward.isMarkedIgnorable());
    }
    
    @Test
    public void testGetAnnotation() {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        forward.annotate(Object.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        
        Class<Object> acls = Object.class;
        assertEquals(forward.getAnnotation(acls), property.getAnnotation(acls));
    }
    
    @Test
    public void testGetMember() {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        
        assertEquals(forward.getMember(), property.getMember());
    }
    
    @Test
    public void testGetCreatorIndex() {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        forward.setCreatorIndex(1);
        
        assertEquals(forward.getCreatorIndex(), property.getCreatorIndex());
    }
    
    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty();
        property.deserializeAndSet(p, ctxt, instance);
        
        // Assertions
    }
    
    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty();
        Object result = property.deserializeSetAndReturn(p, ctxt, instance);
        
        // Assertions
        assertNull(result);
    }
    
    @Test
    public void testSet() throws IOException {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        forward._valueDeserializer = new JsonDeserializer<>();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        Object instance = new Object();
        Object value = new Object();
        
        property.set(instance, value);
        
        // Assertions
    }
    
    @Test
    public void testSetAndReturn() throws IOException {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        forward._valueDeserializer = new JsonDeserializer<>();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        Object instance = new Object();
        Object value = new Object();
        
        Object result = property.setAndReturn(instance, value);
        
        // Assertions
        assertEquals(value, result);
    }
    
    @Test
    public void testPropertyReferring() {
        ObjectIdReferenceProperty parent = new ObjectIdReferenceProperty();
        UnresolvedForwardReference ref = new UnresolvedForwardReference();
        Class<?> type = String.class;
        Object ob = new Object();
        
        ObjectIdReferenceProperty.PropertyReferring referring = new ObjectIdReferenceProperty.PropertyReferring(parent, ref, type, ob);
        
        assertNotNull(referring);
        assertEquals(parent, referring._parent);
        assertEquals(ref, referring._parent);
        assertEquals(type, referring._type);
        assertEquals(ob, referring._pojo);
    }
    
    @Test
    public void testHandleResolvedForwardReference() throws IOException {
        ObjectIdReferenceProperty parent = new ObjectIdReferenceProperty();
        UnresolvedForwardReference ref = new UnresolvedForwardReference();
        Class<?> type = String.class;
        Object ob = new Object();
        Object id = new Object();
        Object value = new Object();
        
        ObjectIdReferenceProperty.PropertyReferring referring = new ObjectIdReferenceProperty.PropertyReferring(parent, ref, type, ob);
        parent.set(ob, value);
        referring.handleResolvedForwardReference(id, value);
        
        // Assertions
    }
}