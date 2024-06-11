
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReferenceProperty;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.annotation.Annotation;

import static org.junit.Assert.*;

public class ObjectIdReferencePropertyTest {

    private ObjectIdReferenceProperty objectIdReferenceProperty;

    @Before
    public void setup() {
        SettableBeanProperty forward = new SettableBeanProperty() {
            @Override
            public void set(Object instance, Object value) throws IOException {

            }

            @Override
            public Object setAndReturn(Object instance, Object value) throws IOException {
                return null;
            }

            @Override
            public void fixAccess(DeserializationConfig config) {

            }

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

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("test"), null, null, null);
        objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
    }

    @Test
    public void testWithName() {
        PropertyName newName = new PropertyName("newTest");
        
        ObjectIdReferenceProperty newProperty = (ObjectIdReferenceProperty) objectIdReferenceProperty.withName(newName);
        
        assertEquals(newName, newProperty.getFullName());
    }

    @Test
    public void testWithValueDeserializer_sameDeserializer() {
        JsonDeserializer<?> deserializer = objectIdReferenceProperty.getValueDeserializer();

        ObjectIdReferenceProperty newProperty = (ObjectIdReferenceProperty) objectIdReferenceProperty.withValueDeserializer(deserializer);

        assertSame(deserializer, newProperty.getValueDeserializer());
    }

    @Test
    public void testWithValueDeserializer_differentDeserializer() {
        JsonDeserializer<?> deserializer = new JsonDeserializer<Object>() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };

        ObjectIdReferenceProperty newProperty = (ObjectIdReferenceProperty) objectIdReferenceProperty.withValueDeserializer(deserializer);

        assertNotSame(deserializer, newProperty.getValueDeserializer());
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider nullValueProvider = new NullValueProvider() {
            @Override
            public Object getNullValue(DeserializationContext ctxt) throws JsonMappingException {
                return null;
            }
        };

        ObjectIdReferenceProperty newProperty = (ObjectIdReferenceProperty) objectIdReferenceProperty.withNullProvider(nullValueProvider);

        assertSame(nullValueProvider, newProperty.getNullValueProvider());
    }

    @Test
    public void testFixAccess() {
        ObjectIdReferenceProperty newProperty = (ObjectIdReferenceProperty) objectIdReferenceProperty.fixAccess(null);

        assertNotNull(newProperty);
    }

    @Test
    public void testGetAnnotation() {
        Class<Annotation> annotationClass = Annotation.class;

        Annotation annotation = objectIdReferenceProperty.getAnnotation(annotationClass);

        assertNull(annotation);
    }

    @Test
    public void testGetMember() {
        AnnotatedMember member = objectIdReferenceProperty.getMember();

        assertNull(member);
    }

    @Test
    public void testGetCreatorIndex() {
        int creatorIndex = objectIdReferenceProperty.getCreatorIndex();

        assertEquals(0, creatorIndex);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser jsonParser = new ObjectMapper().getFactory().createParser("{}");
        DeserializationContext deserializationContext = new DeserializationContext(null, null);
        Object instance = new Object();

        objectIdReferenceProperty.deserializeAndSet(jsonParser, deserializationContext, instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser jsonParser = new ObjectMapper().getFactory().createParser("{}");
        DeserializationContext deserializationContext = new DeserializationContext(null, null);
        Object instance = new Object();

        assertNull(objectIdReferenceProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance));
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
    public void testPropertyReferring_handleResolvedForwardReference() throws IOException {
        ObjectIdReferenceProperty.PropertyReferring propertyReferring = objectIdReferenceProperty.new PropertyReferring(null, null, null, null);
        Object id = new Object();
        Object value = new Object();

        propertyReferring.handleResolvedForwardReference(id, value);
    }
}
