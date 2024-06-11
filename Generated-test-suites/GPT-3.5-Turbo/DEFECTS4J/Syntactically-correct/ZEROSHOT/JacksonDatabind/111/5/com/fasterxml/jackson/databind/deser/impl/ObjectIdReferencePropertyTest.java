import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;

import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReferenceProperty;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;

public class ObjectIdReferencePropertyTest {

    private ObjectIdReferenceProperty property;

    @Before
    public void setup() {
        property = new ObjectIdReferenceProperty(mock(SettableBeanProperty.class), new ObjectIdInfo(mock(AnnotatedMember.class), "property"));
    }

    @Test
    public void testWithName() {
        BeanProperty newProperty = property.withName(null);
        assertNotNull(newProperty);
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> deser = mock(JsonDeserializer.class);
        BeanProperty newProperty = property.withValueDeserializer(deser);
        assertNotNull(newProperty);
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider nva = mock(NullValueProvider.class);
        BeanProperty newProperty = property.withNullProvider(nva);
        assertNotNull(newProperty);
    }

    @Test
    public void testFixAccess() {
        property.fixAccess(mock(DeserializationConfig.class));
    }

    @Test
    public void testGetAnnotation() {
        assertNull(property.getAnnotation(null));
    }

    @Test
    public void testGetMember() {
        assertNull(property.getMember());
    }
    
    @Test
    public void testGetCreatorIndex() {
        assertEquals(0, property.getCreatorIndex());
    }
    
    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        property.deserializeAndSet(p, ctxt, instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        assertNull(property.deserializeSetAndReturn(p, ctxt, instance));
    }
    
    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        property.set(instance, value);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        assertEquals(value, property.setAndReturn(instance, value));
    }

    @Test
    public void testPropertyReferring() throws IOException {
        ObjectIdReferenceProperty.PropertyReferring referring = new ObjectIdReferenceProperty.PropertyReferring(property, null, Object.class, new Object());
        referring.handleResolvedForwardReference(null, new Object());
    }
}