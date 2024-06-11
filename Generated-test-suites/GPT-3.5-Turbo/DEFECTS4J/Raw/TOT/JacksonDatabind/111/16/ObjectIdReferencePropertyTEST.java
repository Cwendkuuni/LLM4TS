import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.annotation.Annotation;

import static org.junit.Assert.*;

public class ObjectIdReferencePropertyTest {

    private ObjectIdReferenceProperty objectIdReferenceProperty;

    @Before
    public void setUp() {
        SettableBeanProperty forward = new SettableBeanProperty() {
            @Override
            public void deserializeAndSet(JsonParser p, DeserializationContext ctxt, Object instance) throws IOException {}

            @Override
            public void set(Object instance, Object value) throws IOException {}

            @Override
            public Object deserializeSetAndReturn(JsonParser p, DeserializationContext ctxt, Object instance) throws IOException {
                return null;
            }

            @Override
            public Object setAndReturn(Object instance, Object value) throws IOException {
                return null;
            }

            @Override
            public SettableBeanProperty withName(PropertyName newName) {
                return null;
            }

            @Override
            public SettableBeanProperty withValueDeserializer(JsonDeserializer<?> deser) {
                return null;
            }

            @Override
            public SettableBeanProperty withNullProvider(NullValueProvider nva) {
                return null;
            }

            @Override
            public void fixAccess(DeserializationConfig config) {}

            @Override
            public <A extends Annotation> A getAnnotation(Class<A> acls) {
                return null;
            }

            @Override
            public AnnotatedMember getMember() {
                return null;
            }

            @Override
            public int getCreatorIndex() {
                return 0;
            }
        };
        
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null, null);
        objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
    }

    @Test
    public void testConstructor() {
        assertEquals(null, objectIdReferenceProperty.getName());
        assertNull(objectIdReferenceProperty.getSetter());
        assertNull(objectIdReferenceProperty.getGetter());
        assertNull(objectIdReferenceProperty.getField());
        assertEquals(0, objectIdReferenceProperty.getMetadata().size());
        assertEquals(null, objectIdReferenceProperty.getType());
        assertFalse(objectIdReferenceProperty.hasValueTypeDeserializer());
        assertTrue(objectIdReferenceProperty.getViews().isEmpty());
        assertFalse(objectIdReferenceProperty.hasValueDeserializer());
        assertFalse(objectIdReferenceProperty.isMergeable());
        assertTrue(objectIdReferenceProperty.isRequired());
        assertFalse(objectIdReferenceProperty.isAccessed());
    }

    @Test
    public void testWithMethods() {
        PropertyName newName = new PropertyName("newName");
        assertNull(objectIdReferenceProperty.withName(newName));

        JsonDeserializer<?> valueDeserializer = new JsonDeserializer() { };
        assertNull(objectIdReferenceProperty.withValueDeserializer(valueDeserializer));

        NullValueProvider nullValueProvider = new NullValueProvider() {
            @Override
            public Object getNullValue(DeserializationContext ctxt) throws JsonMappingException {
                return null;
            }
        };
        assertNull(objectIdReferenceProperty.withNullProvider(nullValueProvider));
    }

    @Test
    public void testFixAccess() {
        DeserializationConfig config = new DeserializationConfig() { };
        objectIdReferenceProperty.fixAccess(config);
    }

    @Test
    public void testGetAnnotation() {
        Class<Annotation> annotationClass = Annotation.class;
        assertNull(objectIdReferenceProperty.getAnnotation(annotationClass));
    }

    @Test
    public void testGetMember() {
        assertNull(objectIdReferenceProperty.getMember());
    }

    @Test
    public void testGetCreatorIndex() {
        assertEquals(0, objectIdReferenceProperty.getCreatorIndex());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser p = new JsonParser() { };
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        Object instance = new Object();
        objectIdReferenceProperty.deserializeAndSet(p, ctxt, instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser p = new JsonParser() { };
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        Object instance = new Object();
        assertNull(objectIdReferenceProperty.deserializeSetAndReturn(p, ctxt, instance));

        UnresolvedForwardReference reference = new UnresolvedForwardReference(null, null, null);
        ObjectIdReferenceProperty.PropertyReferring propertyReferring = new ObjectIdReferenceProperty.PropertyReferring(null, reference, null, instance);
        reference.getRoid().appendReferring(propertyReferring);
    }

    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        objectIdReferenceProperty.set(instance, value);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        assertNull(objectIdReferenceProperty.setAndReturn(instance, value));
    }

    @Test
    public void testHandleResolvedForwardReference() throws IOException {
        ObjectIdReferenceProperty.PropertyReferring propertyReferring = new ObjectIdReferenceProperty.PropertyReferring(null, null, null, null);
        propertyReferring.handleResolvedForwardReference(null, null);
    }

}