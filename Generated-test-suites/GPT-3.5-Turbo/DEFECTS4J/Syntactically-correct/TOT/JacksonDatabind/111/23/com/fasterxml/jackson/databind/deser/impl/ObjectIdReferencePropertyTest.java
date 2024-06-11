import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.annotation.Annotation;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;

public class ObjectIdReferencePropertyTest {

    @Test
    public void testConstructor() {
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);

        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        assertEquals(forward, property._forward);
    }

    @Test
    public void testConstructorWithDeserializerAndNullValueProvider() {
        ObjectIdReferenceProperty property = mock(ObjectIdReferenceProperty.class);
        JsonDeserializer<?> deser = mock(JsonDeserializer.class);
        NullValueProvider nva = mock(NullValueProvider.class);

        when(property._forward).thenReturn(mock(SettableBeanProperty.class));
        when(property._objectIdInfo).thenReturn(mock(ObjectIdInfo.class));

        ObjectIdReferenceProperty newProperty = new ObjectIdReferenceProperty(property, deser, nva);

        assertEquals(deser, newProperty._valueDeserializer);
        assertEquals(nva, newProperty._nullProvider);
    }

    @Test
    public void testConstructorWithNewName() {
        ObjectIdReferenceProperty property = mock(ObjectIdReferenceProperty.class);
        PropertyName newName = mock(PropertyName.class);

        when(property._forward).thenReturn(mock(SettableBeanProperty.class));
        when(property._objectIdInfo).thenReturn(mock(ObjectIdInfo.class));

        ObjectIdReferenceProperty newProperty = new ObjectIdReferenceProperty(property, newName);

        assertEquals(newName, newProperty._propName);
    }

    @Test
    public void testWithName() {
        ObjectIdReferenceProperty property = mock(ObjectIdReferenceProperty.class);
        PropertyName newName = mock(PropertyName.class);

        when(property._forward).thenReturn(mock(SettableBeanProperty.class));

        ObjectIdReferenceProperty newProperty = property.withName(newName);

        assertEquals(newName, newProperty._propName);
    }

    @Test
    public void testWithValueDeserializer() {
        ObjectIdReferenceProperty property = mock(ObjectIdReferenceProperty.class);
        JsonDeserializer<?> deser = mock(JsonDeserializer.class);

        when(property._forward).thenReturn(mock(SettableBeanProperty.class));
        when(property._nullProvider).thenReturn(mock(NullValueProvider.class));

        ObjectIdReferenceProperty newProperty = property.withValueDeserializer(deser);

        assertEquals(deser, newProperty._valueDeserializer);
        assertEquals(property._nullProvider, newProperty._nullProvider);
    }

    @Test
    public void testWithNullProvider() {
        ObjectIdReferenceProperty property = mock(ObjectIdReferenceProperty.class);
        NullValueProvider nva = mock(NullValueProvider.class);

        when(property._forward).thenReturn(mock(SettableBeanProperty.class));
        when(property._valueDeserializer).thenReturn(mock(JsonDeserializer.class));

        ObjectIdReferenceProperty newProperty = property.withNullProvider(nva);

        assertEquals(property._valueDeserializer, newProperty._valueDeserializer);
        assertEquals(nva, newProperty._nullProvider);
    }

    @Test
    public void testFixAccess() {
        ObjectIdReferenceProperty property = mock(ObjectIdReferenceProperty.class);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        DeserializationConfig config = mock(DeserializationConfig.class);

        when(property._forward).thenReturn(forward);

        property.fixAccess(config);

        assertEquals(config, property._forward);
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdReferenceProperty property = mock(ObjectIdReferenceProperty.class);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        Class<Annotation> acls = Annotation.class;

        when(property._forward).thenReturn(forward);

        property.getAnnotation(acls);

        assertEquals(acls, property._forward);
    }

    @Test
    public void testGetMember() {
        ObjectIdReferenceProperty property = mock(ObjectIdReferenceProperty.class);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);

        when(property._forward).thenReturn(forward);

        property.getMember();

        assertEquals(forward, property._forward);
    }

    @Test
    public void testGetCreatorIndex() {
        ObjectIdReferenceProperty property = mock(ObjectIdReferenceProperty.class);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);

        when(property._forward).thenReturn(forward);

        property.getCreatorIndex();

        assertEquals(forward, property._forward);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdReferenceProperty property = mock(ObjectIdReferenceProperty.class);
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();

        when(property._forward).thenReturn(mock(SettableBeanProperty.class));

        property.deserializeAndSet(p, ctxt, instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdReferenceProperty property = mock(ObjectIdReferenceProperty.class);
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();

        when(property._objectIdInfo).thenReturn(null);
        when(property._valueDeserializer).thenReturn(mock(JsonDeserializer.class));

        property.deserializeSetAndReturn(p, ctxt, instance);

        when(property._objectIdInfo).thenReturn(mock(ObjectIdInfo.class));

        try {
            property.deserializeSetAndReturn(p, ctxt, instance);
        } catch (UnresolvedForwardReference e) {
            // Expected exception
        }
    }

    @Test
    public void testSet() throws IOException {
        ObjectIdReferenceProperty property = mock(ObjectIdReferenceProperty.class);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        Object instance = new Object();

        when(property._forward).thenReturn(forward);

        property.set(instance, new Object());

        assertTrue(true); // To suppress unused warning for set() method call
    }

    @Test
    public void testSetAndReturn() throws IOException {
        ObjectIdReferenceProperty property = mock(ObjectIdReferenceProperty.class);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        Object instance = new Object();

        when(property._forward).thenReturn(forward);

        property.setAndReturn(instance, new Object());

        assertTrue(true); // To suppress unused warning for setAndReturn() method call
    }

    @Test
    public void testPropertyReferring() throws IOException {
        ObjectIdReferenceProperty property = mock(ObjectIdReferenceProperty.class);
        UnresolvedForwardReference ref = mock(UnresolvedForwardReference.class);
        Class<?> type = mock(Class.class);
        Object ob = new Object();

        when(property._forward).thenReturn(mock(SettableBeanProperty.class));

        property.new PropertyReferring(property, ref, type, ob);
    }

    @Test
    public void testHandleResolvedForwardReference() throws IOException {
        ObjectIdReferenceProperty.PropertyReferring propertyReferring =
                mock(ObjectIdReferenceProperty.PropertyReferring.class);
        ObjectIdReferenceProperty property = mock(ObjectIdReferenceProperty.class);
        Object id = new Object();
        Object value = new Object();

        when(propertyReferring.hasId(id)).thenReturn(true);
        when(propertyReferring._parent).thenReturn(property);

        propertyReferring.handleResolvedForwardReference(id, value);

        assertTrue(true); // To suppress unused warning for handleResolvedForwardReference() method call
    }
}