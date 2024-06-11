import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.annotation.Annotation;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ResolvableDeserializer;
import com.fasterxml.jackson.databind.deser.PropertyBasedObjectIdGenerator;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReferenceProperty.PropertyReferring;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;

public class ObjectIdReferencePropertyTest {
    private final String TEST_JSON = "{\"id\":123,\"name\":\"John\"}";

    // Test deserialization when forwards reference is resolved correctly
    @Test
    public void testDeserializeSetAndReturn_ForwardReferenceResolved() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty();
        property.setForward(createMockSettableBeanProperty());

        JsonParser jsonParser = new JsonFactory().createParser(TEST_JSON);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();

        Object instance = new Object();
        Object result = property.deserializeSetAndReturn(jsonParser, ctxt, instance);

        assertNotNull(result);
        assertEquals(instance, result);
    }

    // Test deserialization when forwards reference is not resolved
    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeSetAndReturn_ForwardReferenceNotResolved() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty();
        property.setForward(createMockSettableBeanProperty());

        JsonParser jsonParser = new JsonFactory().createParser("{}");
        DeserializationContext ctxt = objectMapper.getDeserializationContext();

        Object instance = new Object();
        property.deserializeSetAndReturn(jsonParser, ctxt, instance);
    }

    // Test handling resolved forward reference
    @Test
    public void testPropertyReferring_HandleResolvedForwardReference() throws IOException {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty();
        property.setForward(createMockSettableBeanProperty());

        PropertyReferring referring = new PropertyReferring(property, createMockUnresolvedForwardReference(),
                Object.class, new Object());
        referring.handleResolvedForwardReference("id", new Object());

        assertTrue(referring.hasId("id"));
    }

    // Test withName method
    @Test
    public void testWithNamedProperty() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty();
        PropertyName newName = PropertyName.construct("newName");

        SettableBeanProperty newProperty = property.withName(newName);

        assertNotNull(newProperty);
        assertEquals("newName", newProperty.getName());
    }

    // Test withValueDeserializer method
    @Test
    public void testWithValueDeserializer() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty();
        JsonDeserializer<?> deserializer = createMockDeserializer();

        SettableBeanProperty newProperty = property.withValueDeserializer(deserializer);

        assertNotNull(newProperty);
        assertEquals(deserializer, newProperty.getValueDeserializer());
    }

    // Test withNullProvider method
    @Test
    public void testWithNullProvider() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty();
        NullValueProvider nullProvider = new NullValueProvider();

        SettableBeanProperty newProperty = property.withNullProvider(nullProvider);

        assertNotNull(newProperty);
        assertEquals(nullProvider, newProperty.getNullProvider());
    }

    // Test fixAccess method when forward property is not null
    @Test
    public void testFixAccess_ForwardPropertyNotNull() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty();
        property.setForward(createMockSettableBeanProperty());

        DeserializationConfig config = createMockDeserializationConfig();
        property.fixAccess(config);
    }

    // Test fixAccess method when forward property is null
    @Test
    public void testFixAccess_ForwardPropertyNull() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty();

        DeserializationConfig config = createMockDeserializationConfig();
        property.fixAccess(config);
    }

    // Test getAnnotation method
    @Test
    public void testGetAnnotation_ForwardPropertyNotNull() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty();
        property.setForward(createMockSettableBeanProperty());

        Class<Annotation> annotationClass = Annotation.class;
        Annotation annotation = property.getAnnotation(annotationClass);

        assertNotNull(annotation);
    }

    // Test getAnnotation method when forward property is null
    @Test
    public void testGetAnnotation_ForwardPropertyNull() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty();

        Class<Annotation> annotationClass = Annotation.class;
        Annotation annotation = property.getAnnotation(annotationClass);

        assertNull(annotation);
    }

    // Test getMember method
    @Test
    public void testGetMember() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty();
        PropertyBasedObjectIdGenerator generator = new PropertyBasedObjectIdGenerator();

        property.setForward(createMockSettableBeanProperty());
        property.setObjectIdGenerator(generator);

        AnnotatedMember member = property.getMember();

        assertNotNull(member);
    }

    // Test getCreatorIndex method
    @Test
    public void testGetCreatorIndex() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty();
        property.setForward(createMockSettableBeanProperty());

        int creatorIndex = property.getCreatorIndex();

        assertEquals(1, creatorIndex);
    }

    // Test set method
    @Test
    public void testSet() throws IOException {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty();
        property.setForward(createMockSettableBeanProperty());

        Object instance = new Object();
        property.set(instance, new Object());
    }

    // Test setAndReturn method
    @Test
    public void testSetAndReturn() throws IOException {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty();
        property.setForward(createMockSettableBeanProperty());

        Object instance = new Object();
        Object result = property.setAndReturn(instance, new Object());

        assertNotNull(result);
        assertEquals(instance, result);
    }

    private SettableBeanProperty createMockSettableBeanProperty() {
        return new SettableBeanProperty(null) {
            private static final long serialVersionUID = -1L;

            @Override
            public void deserializeAndSet(JsonParser p, DeserializationContext ctxt, Object instance)
                    throws IOException {
            }

            @Override
            public void set(Object instance, Object value) throws IOException {
            }

            @Override
            public Object setAndReturn(Object instance, Object value) throws IOException {
                return instance;
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
                return 1;
            }
        };
    }

    private UnresolvedForwardReference createMockUnresolvedForwardReference() {
        return new UnresolvedForwardReference() {
            private static final long serialVersionUID = -1L;

            @Override
            public Object handleResolved() {
                return null;
            }

            @Override
            public Object getObjectId() {
                return null;
            }

            @Override
            public void setObjectId(Object object) {
            }
        };
    }

    private JsonDeserializer<?> createMockDeserializer() {
        return new JsonDeserializer<Object>() {
            private static final long serialVersionUID = -1L;

            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt)
                    throws IOException, JsonProcessingException {
                return null;
            }
        };
    }

    private DeserializationContext createMockDeserializationContext() {
        return new DeserializationContext(null, null) {
            private static final long serialVersionUID = -1L;

            @Override
            public Object findObjectId(Object id, ObjectIdReader reader, JsonParser p) {
                return null;
            }
        };
    }
}