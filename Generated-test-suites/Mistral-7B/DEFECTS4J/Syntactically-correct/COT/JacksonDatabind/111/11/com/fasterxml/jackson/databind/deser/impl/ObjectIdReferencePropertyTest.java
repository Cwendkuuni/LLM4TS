import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.node.ObjectNode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ObjectIdReferencePropertyTest {

    @Test
    public void testObjectIdReferencePropertyConstructor() {
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
        assertEquals(forward, objectIdReferenceProperty._forward);
        assertEquals(objectIdInfo, objectIdReferenceProperty._objectIdInfo);
    }

    @Test
    public void testObjectIdReferencePropertyCopyConstructor() throws Exception {
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
        ObjectIdReferenceProperty copy = new ObjectIdReferenceProperty(objectIdReferenceProperty, mock(JsonDeserializer.class), mock(NullValueProvider.class));
        assertEquals(objectIdReferenceProperty, copy);
        assertEquals(forward, copy._forward);
        assertEquals(objectIdInfo, copy._objectIdInfo);
    }

    @Test
    public void testObjectIdReferencePropertyNameConstructor() throws Exception {
        PropertyName newName = mock(PropertyName.class);
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(mock(ObjectIdReferenceProperty.class), newName);
        assertEquals(newName, objectIdReferenceProperty.getName());
        assertEquals(mock(ObjectIdReferenceProperty.class), objectIdReferenceProperty._forward);
        assertEquals(mock(ObjectIdInfo.class), objectIdReferenceProperty._objectIdInfo);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectIdReferencePropertyWithNullName() {
        new ObjectIdReferenceProperty(mock(ObjectIdReferenceProperty.class), null);
    }

    @Test
    public void testObjectIdReferencePropertyValueDeserializer() {
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(mock(SettableBeanProperty.class), mock(ObjectIdInfo.class));
        JsonDeserializer deserializer = mock(JsonDeserializer.class);
        ObjectIdReferenceProperty newObjectIdReferenceProperty = objectIdReferenceProperty.withValueDeserializer(deserializer);
        assertEquals(deserializer, newObjectIdReferenceProperty._valueDeserializer);
        assertEquals(objectIdReferenceProperty, newObjectIdReferenceProperty._forward);
        assertEquals(mock(ObjectIdInfo.class), newObjectIdReferenceProperty._objectIdInfo);
    }

    @Test
    public void testObjectIdReferencePropertyNullProvider() {
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(mock(SettableBeanProperty.class), mock(ObjectIdInfo.class));
        NullValueProvider nva = mock(NullValueProvider.class);
        ObjectIdReferenceProperty newObjectIdReferenceProperty = objectIdReferenceProperty.withNullProvider(nva);
        assertEquals(nva, newObjectIdReferenceProperty._nullProvider);
        assertEquals(objectIdReferenceProperty, newObjectIdReferenceProperty._forward);
        assertEquals(mock(ObjectIdInfo.class), newObjectIdReferenceProperty._objectIdInfo);
    }

    @Test
    public void testObjectIdReferencePropertyFixAccess() {
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(mock(SettableBeanProperty.class), mock(ObjectIdInfo.class));
        DeserializationConfig config = mock(DeserializationConfig.class);
        objectIdReferenceProperty.fixAccess(config);
        Mockito.verify(objectIdReferenceProperty._forward).fixAccess(config);
    }

    @Test
    public void testObjectIdReferencePropertyGetAnnotation() {
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(mock(SettableBeanProperty.class), mock(ObjectIdInfo.class));
        Class<Annotation> acls = mock(Annotation.class);
        assertEquals(objectIdReferenceProperty._forward.getAnnotation(acls), objectIdReferenceProperty.getAnnotation(acls));
    }

    @Test
    public void testObjectIdReferencePropertyGetMember() {
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(mock(SettableBeanProperty.class), mock(ObjectIdInfo.class));
        assertEquals(objectIdReferenceProperty._forward.getMember(), objectIdReferenceProperty.getMember());
    }

    @Test
    public void testObjectIdReferencePropertyGetCreatorIndex() {
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(mock(SettableBeanProperty.class), mock(ObjectIdInfo.class));
        assertEquals(objectIdReferenceProperty._forward.getCreatorIndex(), objectIdReferenceProperty.getCreatorIndex());
    }

    @Test
    public void testObjectIdReferencePropertyDeserializeAndSet() throws Exception {
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        Object value = mock(Object.class);
        ObjectNode node = mock(ObjectNode.class);
        when(p.readValueAsTree()).thenReturn(node);

        when(node.get("id")).thenReturn(1L);
        when(objectIdReferenceProperty.deserialize(p, ctxt)).thenReturn(value);
        when(forward.set(instance, value)).thenReturn(instance);

        Object result = objectIdReferenceProperty.deserializeAndSet(p, ctxt, instance);
        assertEquals(instance, result);
    }

    @Test
    public void testObjectIdReferencePropertyDeserializeSetAndReturn() throws Exception {
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        Object value = mock(Object.class);

        when(objectIdReferenceProperty.deserialize(p, ctxt)).thenReturn(value);
        when(forward.setAndReturn(instance, value)).thenReturn(value);

        Object result = objectIdReferenceProperty.deserializeSetAndReturn(p, ctxt, instance);
        assertEquals(value, result);
    }

    @Test(expected = UnresolvedForwardReference.class)
    public void testObjectIdReferencePropertyDeserializeSetAndReturnWithUnresolvedForwardReference() throws Exception {
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        ObjectIdReference propertyReferring = mock(ObjectIdReferenceProperty.PropertyReferring.class);
        UnresolvedForwardReference reference = mock(UnresolvedForwardReference.class);

        when(objectIdReferenceProperty.deserialize(p, ctxt)).thenThrow(reference);
        when(reference.getRoid()).thenReturn(propertyReferring);
        when(propertyReferring.getParent()).thenReturn(objectIdReferenceProperty);
        when(objectIdReferenceProperty.set(instance, null)).thenReturn(instance);

        objectIdReferenceProperty.deserializeSetAndReturn(p, ctxt, instance);
    }

    @Test
    public void testObjectIdReferencePropertySet() throws Exception {
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
        Object instance = mock(Object.class);
        Object value = mock(Object.class);

        objectIdReferenceProperty.set(instance, value);
        assertEquals(value, forward.get(instance));
    }

    @Test
    public void testObjectIdReferencePropertySetAndReturn() throws Exception {
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
        Object instance = mock(Object.class);
        Object value = mock(Object.class);

        Object result = objectIdReferenceProperty.setAndReturn(instance, value);
        assertEquals(value, result);
    }

    @Test
    public void testObjectIdReferencePropertyPropertyReferring() {
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
        UnresolvedForwardReference ref = mock(UnresolvedForwardReference.class);
        ObjectIdReferenceProperty.PropertyReferring propertyReferring = new ObjectIdReferenceProperty.PropertyReferring(objectIdReferenceProperty, ref, mock(Class.class), mock(Object.class));

        assertEquals(objectIdReferenceProperty, propertyReferring._parent);
        assertEquals(ref, propertyReferring._referring);
    }
}