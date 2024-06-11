import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.annotation.Annotation;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.JsonMappingException;

public class ObjectIdReferencePropertyTest {

    private SettableBeanProperty forward;
    private JsonDeserializer<?> deserializer;
    private NullValueProvider nullValueProvider;
    private PropertyName newName;
    private ObjectIdReferenceProperty objectIdReferenceProperty;
    private Object instance;

    @Before
    public void setUp() {
        forward = mock(SettableBeanProperty.class);
        deserializer = mock(JsonDeserializer.class);
        nullValueProvider = mock(NullValueProvider.class);
        newName = mock(PropertyName.class);
        objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, mock(ObjectIdInfo.class));
        instance = new Object();
    }

    @Test
    public void testWithName() {
        SettableBeanProperty result = objectIdReferenceProperty.withName(newName);
        assertNotNull(result);
        assertEquals(ObjectIdReferenceProperty.class, result.getClass());
        assertEquals(newName, result.getFullName());
    }

    @Test
    public void testWithValueDeserializer() {
        SettableBeanProperty result = objectIdReferenceProperty.withValueDeserializer(deserializer);
        assertNotNull(result);
        assertEquals(ObjectIdReferenceProperty.class, result.getClass());
        assertEquals(deserializer, result.getValueDeserializer());
    }

    @Test
    public void testWithNullProvider() {
        SettableBeanProperty result = objectIdReferenceProperty.withNullProvider(nullValueProvider);
        assertNotNull(result);
        assertEquals(ObjectIdReferenceProperty.class, result.getClass());
        assertEquals(nullValueProvider, result.getNullValueProvider());
    }

    @Test
    public void testFixAccess() {
        DeserializationConfig config = mock(DeserializationConfig.class);
        objectIdReferenceProperty.fixAccess(config);
        verify(forward).fixAccess(config);
    }

    @Test
    public void testGetAnnotation() {
        Class<Annotation> acls = Annotation.class;
        Annotation result = objectIdReferenceProperty.getAnnotation(acls);
        verify(forward).getAnnotation(acls);
    }

    @Test
    public void testGetMember() {
        AnnotatedMember result = objectIdReferenceProperty.getMember();
        verify(forward).getMember();
    }

    @Test
    public void testGetCreatorIndex() {
        int result = objectIdReferenceProperty.getCreatorIndex();
        verify(forward).getCreatorIndex();
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        objectIdReferenceProperty.deserializeAndSet(p, ctxt, instance);
        verify(forward).deserializeAndSet(p, ctxt, instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object result = objectIdReferenceProperty.deserializeSetAndReturn(p, ctxt, instance);
        verify(forward).deserializeAndSet(p, ctxt, instance);
        assertEquals(instance, result);
    }

    @Test
    public void testSet() throws IOException {
        Object value = new Object();
        objectIdReferenceProperty.set(instance, value);
        verify(forward).set(instance, value);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object value = new Object();
        Object result = objectIdReferenceProperty.setAndReturn(instance, value);
        verify(forward).setAndReturn(instance, value);
        assertEquals(instance, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHandleResolvedForwardReference_IllegalArgument() throws IOException {
        ObjectIdReferenceProperty.PropertyReferring propReferring = objectIdReferenceProperty.new PropertyReferring(
                objectIdReferenceProperty, mock(UnresolvedForwardReference.class), Object.class, instance);
        propReferring.handleResolvedForwardReference("123", new Object());
    }

    @Test
    public void testHandleResolvedForwardReference() throws IOException {
        ObjectIdReferenceProperty.PropertyReferring propReferring = objectIdReferenceProperty.new PropertyReferring(
                objectIdReferenceProperty, mock(UnresolvedForwardReference.class), Object.class, instance);
        propReferring.addId("123");
        propReferring.handleResolvedForwardReference("123", new Object());
        verify(forward).set(instance, new Object());
    }
}