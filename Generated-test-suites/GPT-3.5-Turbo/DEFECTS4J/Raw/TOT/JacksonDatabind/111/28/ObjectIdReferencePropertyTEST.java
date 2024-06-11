import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Collections;

import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;

public class ObjectIdReferencePropertyTest {

    @Test
    public void testConstructorForwardNotNull() {
        SettableBeanProperty forward = Mockito.mock(SettableBeanProperty.class);
        ObjectIdInfo objectIdInfo = Mockito.mock(ObjectIdInfo.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        assertNotNull(property);
        assertEquals(forward, property._forward);
        assertEquals(objectIdInfo, property._objectIdInfo);
    }

    @Test
    public void testConstructorForwardNull() {
        ObjectIdInfo objectIdInfo = Mockito.mock(ObjectIdInfo.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, objectIdInfo);

        assertNotNull(property);
        assertNull(property._forward);
        assertEquals(objectIdInfo, property._objectIdInfo);
    }

    @Test
    public void testConstructorWithDeserAndProvider() {
        SettableBeanProperty forward = Mockito.mock(SettableBeanProperty.class);
        ObjectIdInfo objectIdInfo = Mockito.mock(ObjectIdInfo.class);
        JsonDeserializer<?> deser = Mockito.mock(JsonDeserializer.class);
        NullValueProvider nva = Mockito.mock(NullValueProvider.class);

        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        ObjectIdReferenceProperty newProperty = new ObjectIdReferenceProperty(property, deser, nva);

        assertNotNull(newProperty);
        assertEquals(forward, newProperty._forward);
        assertEquals(objectIdInfo, newProperty._objectIdInfo);
        assertEquals(deser, newProperty._valueDeserializer);
        assertEquals(nva, newProperty._nullProvider);
    }

    @Test
    public void testConstructorWithNewName() {
        SettableBeanProperty forward = Mockito.mock(SettableBeanProperty.class);
        ObjectIdInfo objectIdInfo = Mockito.mock(ObjectIdInfo.class);
        PropertyName newName = Mockito.mock(PropertyName.class);

        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        ObjectIdReferenceProperty newProperty = new ObjectIdReferenceProperty(property, newName);

        assertNotNull(newProperty);
        assertEquals(forward, newProperty._forward);
        assertEquals(objectIdInfo, newProperty._objectIdInfo);
        assertEquals(newName, newProperty._propName);
        assertEquals(ObjectIdReferenceProperty.class, newProperty.getClass());
    }

    @Test
    public void testWithName() {
        SettableBeanProperty forward = Mockito.mock(SettableBeanProperty.class);
        ObjectIdInfo objectIdInfo = Mockito.mock(ObjectIdInfo.class);
        PropertyName newName = Mockito.mock(PropertyName.class);

        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        ObjectIdReferenceProperty newProperty = (ObjectIdReferenceProperty) property.withName(newName);

        assertNotNull(newProperty);
        assertEquals(forward, newProperty._forward);
        assertEquals(objectIdInfo, newProperty._objectIdInfo);
        assertEquals(newName, newProperty._propName);
    }

    @Test
    public void testWithValueDeserializerEqual() {
        SettableBeanProperty forward = Mockito.mock(SettableBeanProperty.class);
        ObjectIdInfo objectIdInfo = Mockito.mock(ObjectIdInfo.class);
        JsonDeserializer<?> deser = Mockito.mock(JsonDeserializer.class);

        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        property._valueDeserializer = deser;
        ObjectIdReferenceProperty newProperty = (ObjectIdReferenceProperty) property.withValueDeserializer(deser);

        assertEquals(property, newProperty);
    }

    @Test
    public void testWithValueDeserializerNotEqual() {
        SettableBeanProperty forward = Mockito.mock(SettableBeanProperty.class);
        ObjectIdInfo objectIdInfo = Mockito.mock(ObjectIdInfo.class);
        JsonDeserializer<?> deser1 = Mockito.mock(JsonDeserializer.class);
        JsonDeserializer<?> deser2 = Mockito.mock(JsonDeserializer.class);

        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        property._valueDeserializer = deser1;
        ObjectIdReferenceProperty newProperty = (ObjectIdReferenceProperty) property.withValueDeserializer(deser2);

        assertNotNull(newProperty);
        assertEquals(forward, newProperty._forward);
        assertEquals(objectIdInfo, newProperty._objectIdInfo);
        assertEquals(deser2, newProperty._valueDeserializer);
        assertEquals(property._nullProvider, newProperty._nullProvider);
    }

    @Test
    public void testWithNullProvider() {
        SettableBeanProperty forward = Mockito.mock(SettableBeanProperty.class);
        ObjectIdInfo objectIdInfo = Mockito.mock(ObjectIdInfo.class);
        NullValueProvider nva = Mockito.mock(NullValueProvider.class);

        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        ObjectIdReferenceProperty newProperty = (ObjectIdReferenceProperty) property.withNullProvider(nva);

        assertNotNull(newProperty);
        assertEquals(forward, newProperty._forward);
        assertEquals(objectIdInfo, newProperty._objectIdInfo);
        assertEquals(nva, newProperty._nullProvider);
        assertEquals(property._valueDeserializer, newProperty._valueDeserializer);
    }

    @Test
    public void testFixAccessForwardNotNull() {
        SettableBeanProperty forward = Mockito.mock(SettableBeanProperty.class);
        ObjectIdInfo objectIdInfo = Mockito.mock(ObjectIdInfo.class);
        DeserializationConfig config = Mockito.mock(DeserializationConfig.class);

        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        property.fixAccess(config);

        Mockito.verify(forward, Mockito.times(1)).fixAccess(config);
    }

    @Test
    public void testFixAccessForwardNull() {
        ObjectIdInfo objectIdInfo = Mockito.mock(ObjectIdInfo.class);
        DeserializationConfig config = Mockito.mock(DeserializationConfig.class);

        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, objectIdInfo);
        property.fixAccess(config);

        // No interactions with forward member should occur
        Mockito.verifyNoInteractions(objectIdInfo);
        Mockito.verifyNoInteractions(config);
    }

    @Test
    public void testGetAnnotation() {
        SettableBeanProperty forward = Mockito.mock(SettableBeanProperty.class);
        ObjectIdInfo objectIdInfo = Mockito.mock(ObjectIdInfo.class);
        Class<Annotation> acls = Annotation.class;

        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        property.getAnnotation(acls);

        Mockito.verify(forward, Mockito.times(1)).getAnnotation(acls);
    }

    @Test
    public void testGetMember() {
        SettableBeanProperty forward = Mockito.mock(SettableBeanProperty.class);
        ObjectIdInfo objectIdInfo = Mockito.mock(ObjectIdInfo.class);

        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        property.getMember();

        Mockito.verify(forward, Mockito.times(1)).getMember();
    }

    @Test
    public void testGetCreatorIndex() {
        SettableBeanProperty forward = Mockito.mock(SettableBeanProperty.class);
        ObjectIdInfo objectIdInfo = Mockito.mock(ObjectIdInfo.class);

        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        property.getCreatorIndex();

        Mockito.verify(forward, Mockito.times(1)).getCreatorIndex();
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        SettableBeanProperty forward = Mockito.mock(SettableBeanProperty.class);
        ObjectIdInfo objectIdInfo = Mockito.mock(ObjectIdInfo.class);
        JsonParser p = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Object instance = new Object();

        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        property.deserializeAndSet(p, ctxt, instance);

        Mockito.verify(p, Mockito.times(1)).deserialize(p, ctxt);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        SettableBeanProperty forward = Mockito.mock(SettableBeanProperty.class);
        ObjectIdInfo objectIdInfo = Mockito.mock(ObjectIdInfo.class);
        JsonParser p = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Object instance = new Object();

        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        Object result = property.deserializeSetAndReturn(p, ctxt, instance);

        assertNull(result);
        Mockito.verify(p, Mockito.times(1)).deserialize(p, ctxt);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeSetAndReturnWithNoIdentityInfo() throws IOException {
        SettableBeanProperty forward = Mockito.mock(SettableBeanProperty.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, null);
        JsonParser p = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Object instance = new Object();
        UnresolvedForwardReference reference = Mockito.mock(UnresolvedForwardReference.class);
        ReadableObjectId.Roid roid = Mockito.mock(ReadableObjectId.Roid.class);
        
        Mockito.when(reference.getRoid()).thenReturn(roid);
        
        property.deserializeSetAndReturn(p, ctxt, instance);
    }

    @Test
    public void testDeserializeSetAndReturnUsingIdentityInfo() throws IOException {
        SettableBeanProperty forward = Mockito.mock(SettableBeanProperty.class);
        ObjectIdInfo objectIdInfo = Mockito.mock(ObjectIdInfo.class);
        JsonParser p = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Object instance = new Object();

        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        property._valueDeserializer = Mockito.mock(JsonDeserializer.class);
        Object result = property.deserializeSetAndReturn(p, ctxt, instance);

        assertNull(result);
        Mockito.verify(p, Mockito.times(1)).deserialize(p, ctxt);
    }

    @Test
    public void testSet() throws IOException {
        SettableBeanProperty forward = Mockito.mock(SettableBeanProperty.class);
        ObjectIdInfo objectIdInfo = Mockito.mock(ObjectIdInfo.class);
        Object instance = new Object();
        Object value = new Object();

        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        property.set(instance, value);

        Mockito.verify(forward, Mockito.times(1)).set(instance, value);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        SettableBeanProperty forward = Mockito.mock(SettableBeanProperty.class);
        ObjectIdInfo objectIdInfo = Mockito.mock(ObjectIdInfo.class);
        Object instance = new Object();
        Object value = new Object();

        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        Object result = property.setAndReturn(instance, value);

        assertEquals(value, result);
        Mockito.verify(forward, Mockito.times(1)).setAndReturn(instance, value);
    }

    @Test
    public void testPropertyReferringConstructor() {
        ObjectIdReferenceProperty parent = Mockito.mock(ObjectIdReferenceProperty.class);
        UnresolvedForwardReference ref = Mockito.mock(UnresolvedForwardReference.class);
        Class<?> type = String.class;
        Object ob = new Object();
        
        ObjectIdReferenceProperty.PropertyReferring propertyReferring = new ObjectIdReferenceProperty.PropertyReferring(parent, ref, type, ob);

        assertNotNull(propertyReferring);
        assertEquals(parent, propertyReferring._parent);
        assertEquals(ob, propertyReferring._pojo);
        assertEquals(type, propertyReferring._type);
        assertEquals(ref, propertyReferring._reference);
    }

    @Test
    public void testHandleResolvedForwardReference() throws IOException {
        ObjectIdReferenceProperty parent = Mockito.mock(ObjectIdReferenceProperty.class);
        UnresolvedForwardReference ref = Mockito.mock(UnresolvedForwardReference.class);
        Mockito.when(ref.getRoid()).thenReturn(Mockito.mock(ReadableObjectId.Roid.class));
        Class<?> type = String.class;
        Object ob = new Object();
        Object id = 1;
        Object value = new Object();
        
        ObjectIdReferenceProperty.PropertyReferring propertyReferring = new ObjectIdReferenceProperty.PropertyReferring(parent, ref, type, ob);

        propertyReferring.handleResolvedForwardReference(id, value);

        Mockito.verify(parent, Mockito.times(1)).set(ob, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHandleResolvedForwardReferenceWithInvalidId() throws IOException {
        ObjectIdReferenceProperty parent = Mockito.mock(ObjectIdReferenceProperty.class);
        UnresolvedForwardReference ref = Mockito.mock(UnresolvedForwardReference.class);
        Mockito.when(ref.getRoid()).thenReturn(Mockito.mock(ReadableObjectId.Roid.class));
        Class<?> type = String.class;
        Object ob = new Object();
        Object id = 2;
        Object value = new Object();
        
        ObjectIdReferenceProperty.PropertyReferring propertyReferring = new ObjectIdReferenceProperty.PropertyReferring(parent,
                ref, type, ob);

        propertyReferring.handleResolvedForwardReference(id, value);
    }

}