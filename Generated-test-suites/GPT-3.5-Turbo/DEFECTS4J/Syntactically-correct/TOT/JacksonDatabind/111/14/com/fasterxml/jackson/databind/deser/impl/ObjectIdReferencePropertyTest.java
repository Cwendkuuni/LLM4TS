import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.ResolvedObjectId;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class ObjectIdReferencePropertyTest {

    private ObjectMapper objectMapper;
    private JsonFactory jsonFactory;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        jsonFactory = new JsonFactory();
    }

    @Test
    public void testWithNullValueProvider() throws IOException {
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);
        JsonParser jsonParser = jsonFactory.createParser("{}");
        DeserializationContext deserializationContext = objectMapper.getDeserializationContext();

        SettableBeanProperty forwardProperty = mock(SettableBeanProperty.class);
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(forwardProperty, objectIdInfo);
        ObjectIdReferenceProperty nullProviderProperty = objectIdReferenceProperty.withNullProvider(nullValueProvider);

        assertEquals(nullValueProvider, nullProviderProperty.getNullValueProvider());

        ResolvedObjectId resolvedObjectId = new ResolvedObjectId(null);
        UnresolvedForwardReference unresolvedForwardReference = new UnresolvedForwardReference(jsonParser, "test", resolvedObjectId, nullProviderProperty.getType());
        PropertyValueBuffer propertyValueBuffer = new PropertyValueBuffer(nullProviderProperty, true);
        ObjectIdReferenceProperty.PropertyReferring propertyReferring = new ObjectIdReferenceProperty.PropertyReferring(nullProviderProperty, unresolvedForwardReference, nullProviderProperty.getType().getRawClass(), propertyValueBuffer);

        assertNull(propertyReferring._id);
        assertNull(propertyReferring._pojo);

        propertyReferring.handleResolvedForwardReference(1L, "test value");

        assertEquals(1L, propertyReferring._id);
        assertEquals("test value", propertyReferring._pojo);
    }

    @Test
    public void testWithJsonDeserializer() throws IOException {
        JsonParser jsonParser = jsonFactory.createParser("{}");
        DeserializationContext deserializationContext = objectMapper.getDeserializationContext();

        SettableBeanProperty forwardProperty = mock(SettableBeanProperty.class);
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(forwardProperty, objectIdInfo);

        JsonDeserializer<?> deserializer = mock(JsonDeserializer.class);
        ObjectIdReferenceProperty deserializerProperty = objectIdReferenceProperty.withValueDeserializer(deserializer);

        assertEquals(deserializer, deserializerProperty.getValueDeserializer());

        ResolvedObjectId resolvedObjectId = new ResolvedObjectId(null);
        UnresolvedForwardReference unresolvedForwardReference = new UnresolvedForwardReference(jsonParser, "test", resolvedObjectId, deserializerProperty.getType());
        PropertyValueBuffer propertyValueBuffer = new PropertyValueBuffer(deserializerProperty, true);
        ObjectIdReferenceProperty.PropertyReferring propertyReferring = new ObjectIdReferenceProperty.PropertyReferring(deserializerProperty, unresolvedForwardReference, deserializerProperty.getType().getRawClass(), propertyValueBuffer);

        assertNull(propertyReferring._id);
        assertNull(propertyReferring._pojo);

        propertyReferring.handleResolvedForwardReference(1L, "test value");

        assertEquals(1L, propertyReferring._id);
        assertEquals("test value", propertyReferring._pojo);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser jsonParser = jsonFactory.createParser("{}");
        DeserializationContext deserializationContext = objectMapper.getDeserializationContext();

        SettableBeanProperty forwardProperty = mock(SettableBeanProperty.class);
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(forwardProperty, objectIdInfo);

        PropertyValueBuffer propertyValueBuffer = new PropertyValueBuffer(objectIdReferenceProperty, true);

        assertNull(propertyValueBuffer.valueComplete);
        assertNull(propertyValueBuffer.objectValue);

        Object value = objectIdReferenceProperty.deserializeSetAndReturn(jsonParser, deserializationContext, "test value");

        assertEquals("test value", propertyValueBuffer.valueComplete);
        assertNull(propertyValueBuffer.objectValue);
        assertNull(value);
    }

    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        Object value = new Object();

        SettableBeanProperty forwardProperty = mock(SettableBeanProperty.class);
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(forwardProperty, objectIdInfo);

        objectIdReferenceProperty.set(instance, value);

        // Verify that forward property's set method is called
        // with 'instance' and 'value'
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();
        Object value = new Object();

        SettableBeanProperty forwardProperty = mock(SettableBeanProperty.class);
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(forwardProperty, objectIdInfo);

        Object result = objectIdReferenceProperty.setAndReturn(instance, value);

        // Verify that forward property's setAndReturn method is called
        // with 'instance' and 'value', and that the result is returned
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHandleResolvedForwardReferenceWithInvalidId() throws IOException {
        SettableBeanProperty forwardProperty = mock(SettableBeanProperty.class);
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(forwardProperty, objectIdInfo);

        ObjectIdReferenceProperty.PropertyReferring propertyReferring = new ObjectIdReferenceProperty.PropertyReferring(objectIdReferenceProperty, null, objectIdReferenceProperty.getType().getRawClass(), null);

        propertyReferring.handleResolvedForwardReference(1L, "test value");
    }

    @Test
    public void testHandleResolvedForwardReference() throws IOException {
        SettableBeanProperty forwardProperty = mock(SettableBeanProperty.class);
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(forwardProperty, objectIdInfo);

        ObjectIdReferenceProperty.PropertyReferring propertyReferring = new ObjectIdReferenceProperty.PropertyReferring(objectIdReferenceProperty, null, objectIdReferenceProperty.getType().getRawClass(), null);

        assertNull(propertyReferring._id);
        assertNull(propertyReferring._pojo);

        propertyReferring.handleResolvedForwardReference(null, "test value");

        assertNull(propertyReferring._id);
        assertEquals("test value", propertyReferring._pojo);
    }

}