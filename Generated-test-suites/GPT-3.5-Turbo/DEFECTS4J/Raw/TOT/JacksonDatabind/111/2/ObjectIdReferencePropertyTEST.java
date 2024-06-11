import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReferenceProperty;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ObjectIdReferencePropertyTest {

    @Test
    void testConstructor() {
        ObjectIdReferenceProperty forward = new ObjectIdReferenceProperty(null, null);
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null, null);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        
        assertNotNull(property);
        assertNull(property.getValueDeserializer());
        assertNull(property.getNullValueProvider());
        assertNotNull(property.getForward());
        assertEquals(property.getForward(), forward);
        assertNotNull(property.getObjectIdInfo());
        assertEquals(property.getObjectIdInfo(), objectIdInfo);
    }
    
    @Test
    void testConstructorWithDeserAndNva() {
        ObjectIdReferenceProperty forward = new ObjectIdReferenceProperty(null, null);
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null, null);
        JsonDeserializer<?> deser = null;
        NullValueProvider nva = null;
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        ObjectIdReferenceProperty newProperty = new ObjectIdReferenceProperty(property, deser, nva);
        
        assertNotNull(newProperty);
        assertNull(newProperty.getValueDeserializer());
        assertNull(newProperty.getNullValueProvider());
        assertNotNull(newProperty.getForward());
        assertEquals(newProperty.getForward(), forward);
        assertNotNull(newProperty.getObjectIdInfo());
        assertEquals(newProperty.getObjectIdInfo(), objectIdInfo);
    }
    
    @Test
    void testConstructorWithNewName() {
        ObjectIdReferenceProperty forward = new ObjectIdReferenceProperty(null, null);
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null, null);
        PropertyName newName = new PropertyName("newName");
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        ObjectIdReferenceProperty newProperty = new ObjectIdReferenceProperty(property, newName);
        
        assertNotNull(newProperty);
        assertNull(newProperty.getValueDeserializer());
        assertNull(newProperty.getNullValueProvider());
        assertNotNull(newProperty.getForward());
        assertEquals(newProperty.getForward(), forward);
        assertNotNull(newProperty.getObjectIdInfo());
        assertEquals(newProperty.getObjectIdInfo(), objectIdInfo);
        assertNotNull(newProperty.getMember());
    }
    
    @Test
    void testWithName() {
        ObjectIdReferenceProperty forward = new ObjectIdReferenceProperty(null, null);
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null, null);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        PropertyName newName = new PropertyName("newName");
        SettableBeanProperty newProperty = property.withName(newName);
        
        assertNotNull(newProperty);
        assertEquals(newProperty.getName(), newName);
        assertNotNull(newProperty.getValueDeserializer());
        assertNotNull(newProperty.getAccess());
        assertNull(newProperty.getNullValueProvider());
        assertNull(newProperty.getMetadata());
        assertNull(newProperty.getInjectableValueId());
    }
    
    @Test
    void testWithValueDeserializer() {
        ObjectIdReferenceProperty forward = new ObjectIdReferenceProperty(null, null);
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null, null);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        JsonDeserializer<?> deser = null;
        SettableBeanProperty newProperty = property.withValueDeserializer(deser);
        
        assertNotNull(newProperty);
        assertEquals(newProperty.getValueDeserializer(), deser);
        assertNotNull(newProperty.getAccess());
        assertNotNull(newProperty.getInitializer());
        assertNotNull(newProperty.getMutator());
        assertNotNull(newProperty.getNullValueProvider());
    }
    
    @Test
    void testWithNullProvider() {
        ObjectIdReferenceProperty forward = new ObjectIdReferenceProperty(null, null);
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null, null);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        NullValueProvider nva = null;
        SettableBeanProperty newProperty = property.withNullProvider(nva);
        
        assertNotNull(newProperty);
        assertEquals(newProperty.getNullValueProvider(), nva);
        assertNotNull(newProperty.getValueDeserializer());
        assertNotNull(newProperty.getAccess());
        assertNotNull(newProperty.getInstantiator());
        assertNotNull(newProperty.getInjectableValueId());
    }
    
    @Test
    void testFixAccess() {
        ObjectIdReferenceProperty forward = new ObjectIdReferenceProperty(null, null);
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null, null);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        DeserializationConfig config = null;
        property.fixAccess(config);
        
        assertNotNull(property);
    }
    
    @Test
    void testGetAnnotation() {
        ObjectIdReferenceProperty forward = new ObjectIdReferenceProperty(null, null);
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null, null);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        Class<Annotation> acls = null;
        
        assertNull(property.getAnnotation(acls));
    }
    
    @Test
    void testGetMember() {
        ObjectIdReferenceProperty forward = new ObjectIdReferenceProperty(null, null);
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null, null);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        
        assertNull(property.getMember());
    }
    
    @Test
    void testGetCreatorIndex() {
        ObjectIdReferenceProperty forward = new ObjectIdReferenceProperty(null, null);
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null, null);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        
        assertEquals(property.getCreatorIndex(), -1);
    }
    
    @Test
    void testDeserializeAndSet() {
        ObjectIdReferenceProperty forward = new ObjectIdReferenceProperty(null, null);
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null, null);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = new Object();
        
        assertThrows(JsonMappingException.class, () -> {
            property.deserializeAndSet(p, ctxt, instance);
        });
    }
    
    @Test
    void testDeserializeSetAndReturn() {
        ObjectIdReferenceProperty forward = new ObjectIdReferenceProperty(null, null);
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null, null);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = new Object();
        
        assertNull(property.deserializeSetAndReturn(p, ctxt, instance));
    }
    
    @Test
    void testSet() {
        ObjectIdReferenceProperty forward = new ObjectIdReferenceProperty(null, null);
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null, null);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        Object instance = new Object();
        Object value = new Object();
        
        assertThrows(IOException.class, () -> {
            property.set(instance, value);
        });
    }
    
    @Test
    void testSetAndReturn() {
        ObjectIdReferenceProperty forward = new ObjectIdReferenceProperty(null, null);
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null, null);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        Object instance = new Object();
        Object value = new Object();
        
        assertNull(property.setAndReturn(instance, value));
    }
    
    @Test
    void testPropertyReferring() {
        ObjectIdReferenceProperty forward = new ObjectIdReferenceProperty(null, null);
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null, null);
        UnresolvedForwardReference ref = new UnresolvedForwardReference(null, null, null, null, null);
        Class<?> type = Object.class;
        Object ob = new Object();
        ObjectIdReferenceProperty.PropertyReferring referring = new ObjectIdReferenceProperty.PropertyReferring(forward, ref, type, ob);
        
        assertNotNull(referring);
        assertNotNull(referring.getRoid());
        assertNotNull(referring.getType());
        assertNotNull(referring.getUnresolvedId());
        assertNotNull(referring.getKnownType());
        assertNotNull(referring.getMessage());
        assertNotNull(referring.getLocation());
    }
}