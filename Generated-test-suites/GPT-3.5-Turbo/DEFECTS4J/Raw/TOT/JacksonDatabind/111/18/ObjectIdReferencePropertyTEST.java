import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReferenceProperty;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;

public class ObjectIdReferencePropertyTest {

    @Test
    public void testConstructorForwardAndObjectIdInfo() {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo= new ObjectIdInfo();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        assertEquals(forward, property._forward);
        assertEquals(objectIdInfo, property._objectIdInfo);
    }
    
    @Test
    public void testConstructorSrcDeserializerAndNva() {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo= new ObjectIdInfo();
        ObjectIdReferenceProperty src = new ObjectIdReferenceProperty(forward, objectIdInfo);
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        NullValueProvider nva = new NullValueProvider();

        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(src, deser, nva);
        assertEquals(forward, property._forward);
        assertEquals(objectIdInfo, property._objectIdInfo);
        assertEquals(deser, property._valueDeserializer);
        assertEquals(nva, property._nullProvider);
    }
    
    @Test
    public void testConstructorSrcAndNewName() {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo= new ObjectIdInfo();
        ObjectIdReferenceProperty src = new ObjectIdReferenceProperty(forward, objectIdInfo);
        PropertyName newName = new PropertyName();
        
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(src, newName);
        assertEquals(forward, property._forward);
        assertEquals(objectIdInfo, property._objectIdInfo);
        assertEquals(newName, property.getFullName());
    }
    
    @Test
    public void testWithNullValueDeserializer() {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo= new ObjectIdInfo();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        JsonDeserializer<?> deser = new JsonDeserializer<>();
        ObjectIdReferenceProperty newProperty = property.withValueDeserializer(deser);
        assertEquals(forward, newProperty._forward);
        assertEquals(objectIdInfo, newProperty._objectIdInfo);
        assertEquals(deser, newProperty._valueDeserializer);
    }
    
    @Test
    public void testWithNullProvider() {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo= new ObjectIdInfo();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        NullValueProvider nva = new NullValueProvider();
        ObjectIdReferenceProperty newProperty = property.withNullProvider(nva);
        assertEquals(forward, newProperty._forward);
        assertEquals(objectIdInfo, newProperty._objectIdInfo);
        assertEquals(nva, newProperty._nullProvider);
    }
    
    @Test
    public void testFixAccessWithForward() {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo= new ObjectIdInfo();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        DeserializationConfig config = new DeserializationConfig();

        property.fixAccess(config);
        // No assertion as this method does not return anything
    }
    
    @Test
    public void testGetAnnotationWithForward() {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo= new ObjectIdInfo();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        Class<?> acls = Override.class;

        Annotation annotation = property.getAnnotation(acls);
        assertEquals(acls, annotation.annotationType());
    }
    
    @Test
    public void testGetMemberWithForward() {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo= new ObjectIdInfo();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        AnnotatedMember member = property.getMember();
        assertEquals(forward.getMember(), member);
    }
    
    @Test
    public void testGetCreatorIndexWithForward() {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo= new ObjectIdInfo();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        int creatorIndex = property.getCreatorIndex();
        assertEquals(forward.getCreatorIndex(), creatorIndex);
    }
    
    @Test
    public void testDeserializeAndSet() {
        // Test when UnresolvedForwardReference is not thrown
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo= new ObjectIdInfo();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();

        try {
            property.deserializeAndSet(p, ctxt, instance);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
    
    @Test
    public void testDeserializeSetAndReturnNoUnresolvedForwardReference() {
        // Test when UnresolvedForwardReference is not thrown
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo= new ObjectIdInfo();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();

        try {
            Object result = property.deserializeSetAndReturn(p, ctxt, instance);
            assertNull(result);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
    
    @Test
    public void testDeserializeSetAndReturnUnresolvedForwardReferenceNoIdentityInfo() {
        // Test when UnresolvedForwardReference is thrown and no identity info is available
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = null;
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();

        try {
            property.deserializeSetAndReturn(p, ctxt, instance);
            fail("JsonMappingException should be thrown");
        } catch (IOException e) {
            fail("IOException should not be thrown");
        } catch (JsonMappingException e) {
            assertEquals("Unresolved forward reference but no identity info", e.getMessage());
        }
    }
    
    @Test
    public void testDeserializeSetAndReturnUnresolvedForwardReferenceWithIdentityInfo() {
        // Test when UnresolvedForwardReference is thrown and identity info is available
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        property.withValueDeserializer(deser);
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();

        try {
            property.deserializeSetAndReturn(p, ctxt, instance);
            fail("UnresolvedForwardReference should be thrown");
        } catch (IOException e) {
            fail("IOException should not be thrown");
        } catch (UnresolvedForwardReference e) {
            assertEquals("Trying to resolve a forward reference with id [null] that wasn't previously seen as unresolved.", e.getMessage());
        }
    }
    
    @Test
    public void testSet() {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo= new ObjectIdInfo();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        Object instance = new Object();
        Object value = new Object();

        try {
            property.set(instance, value);
            // No assertion as this method does not return anything
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
    
    @Test
    public void testSetAndReturn() {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo= new ObjectIdInfo();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        Object instance = new Object();
        Object value = new Object();

        try {
            Object result = property.setAndReturn(instance, value);
            assertEquals(instance, result);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
    
    @Test
    public void testPropertyReferringHandleResolvedForwardReference() {
        ObjectIdReferenceProperty parent = new ObjectIdReferenceProperty();
        UnresolvedForwardReference ref = new UnresolvedForwardReference();
        Class<?> type = Object.class;
        Object ob = new Object();
        ObjectIdReferenceProperty.PropertyReferring referring = new ObjectIdReferenceProperty.PropertyReferring(parent, ref, type, ob);
        Object id = new Object();

        try {
            referring.handleResolvedForwardReference(id, ob);
            // No assertion as this method does not return anything
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
}