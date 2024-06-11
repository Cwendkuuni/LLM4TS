package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;

import static org.mockito.Mockito.*;

public class ObjectIdReferencePropertyTest {

    @Test
    public void testWithName() {
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        PropertyName newName = mock(PropertyName.class);
        SettableBeanProperty result = property.withName(newName);

        verify(forward, times(1)).fixAccess(any(DeserializationConfig.class));
        verify(forward, times(1)).getAnnotation(any(Class.class));
        verify(forward, times(1)).getMember();
        verify(forward, times(1)).getCreatorIndex();

        // Assertions
        assertSame(property.getClass(), result.getClass());
    }

    @Test
    public void testWithValueDeserializer() {
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        JsonDeserializer<?> deser = mock(JsonDeserializer.class);
        SettableBeanProperty result = property.withValueDeserializer(deser);

        verify(forward, times(1)).fixAccess(any(DeserializationConfig.class));
        verify(forward, times(1)).getAnnotation(any(Class.class));
        verify(forward, times(1)).getMember();
        verify(forward, times(1)).getCreatorIndex();

        // Assertions
        assertSame(property.getClass(), result.getClass());
    }

    @Test
    public void testWithNullProvider() {
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        NullValueProvider nva = mock(NullValueProvider.class);
        SettableBeanProperty result = property.withNullProvider(nva);

        verify(forward, times(1)).fixAccess(any(DeserializationConfig.class));
        verify(forward, times(1)).getAnnotation(any(Class.class));
        verify(forward, times(1)).getMember();
        verify(forward, times(1)).getCreatorIndex();

        // Assertions
        assertSame(property.getClass(), result.getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHandleResolvedForwardReference() throws IOException {
        ObjectIdReferenceProperty.PropertyReferring propertyReferring = mock(ObjectIdReferenceProperty.PropertyReferring.class);
        propertyReferring.handleResolvedForwardReference("id", "value");
    }
}