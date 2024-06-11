package com.yourpackage;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;

public class SettableAnyPropertyTest {

    @Test
    public void testConstructor() {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, typeDeserializer);
        assertNotNull(settableAnyProperty);
    }

    @Test
    public void testWithValueDeserializer() {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, typeDeserializer);
        JsonDeserializer<Object> newValueDeserializer = mock(JsonDeserializer.class);
        SettableAnyProperty newSettableAnyProperty = settableAnyProperty.withValueDeserializer(newValueDeserializer);
        assertEquals(newValueDeserializer, newSettableAnyProperty._valueDeserializer);
    }

    @Test
    public void testFixAccess() {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        DeserializationConfig config = mock(DeserializationConfig.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, null, null, null, null);
        settableAnyProperty.fixAccess(config);
        verify(setter).fixAccess(config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
    }

    @Test
    public void testDeserializeAndSet_SimpleValue() throws Exception {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        ObjectInstanceCreator objectInstanceCreator = mock(ObjectInstanceCreator.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        String propName = "propName";

        when(setter.callIfAccessible(anyObject())).thenCallRealMethod();
        when(objectInstanceCreator.createInstanceIfAbsent(any(), any())).thenReturn(instance);
        when(ctxt.findBeanProperty(_property)).thenReturn(property);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, null, null, null, null);
        settableAnyProperty.setInstanceCreator(objectInstanceCreator);

        JsonParser p = mock(JsonParser.class);
        Object value = "value";

        settableAnyProperty.deserializeAndSet(p, ctxt, instance, propName);

        verify(p).nextToken();
        verify(setter).callIfAccessible(instance);
        verify(setter).callOnWith(instance, propName, value);
    }

    @Test
    public void testDeserializeAndSet_NullValue() throws Exception {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        ObjectInstanceCreator objectInstanceCreator = mock(ObjectInstanceCreator.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        String propName = "propName";

        when(setter.callIfAccessible(anyObject())).thenCallRealMethod();
        when(objectInstanceCreator.createInstanceIfAbsent(any(), any())).thenReturn(instance);
        when(ctxt.findBeanProperty(_property)).thenReturn(property);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, null, null, null, null);
        settableAnyProperty.setInstanceCreator(objectInstanceCreator);

        JsonParser p = mock(JsonParser.class);

        settableAnyProperty.deserializeAndSet(p, ctxt, instance, propName);

        verify(p).nextToken();
        verify(p).nextToken();
        verify(setter).callIfAccessible(instance);
        verify(setter).callOnWith(instance, propName, null);
    }

    @Test
    public void testDeserializeAndSet_ValueWithType() throws Exception {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        ObjectInstanceCreator objectInstanceCreator = mock(ObjectInstanceCreator.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        String propName = "propName";

        when(setter.callIfAccessible(anyObject())).thenCallRealMethod();
        when(objectInstanceCreator.createInstanceIfAbsent(any(), any())).thenReturn(instance);
        when(ctxt.findBeanProperty(_property)).thenReturn(property);

        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, null, null, null, valueTypeDeserializer);
        settableAnyProperty.setInstanceCreator(objectInstanceCreator);

        JsonParser p = mock(JsonParser.class);
        Object value = "value";

        JsonToken token = JsonToken.START_OBJECT;
        when(p.getCurrentToken()).thenReturn(token);
        when(p.nextValue()).thenReturn(value);

        settableAnyProperty.deserializeAndSet(p, ctxt, instance, propName);

        verify(p).nextToken();
        verify(p).nextValue();
        verify(valueTypeDeserializer).deserialize(p, ctxt);
        verify(setter).callIfAccessible(instance);
        verify(setter).callOnWith(instance, propName, value);
    }

    @Test(expected = UnresolvedForwardReference.class)
    public void testDeserializeAndSet_UnresolvedForwardReference() throws Exception {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        ObjectInstanceCreator objectInstanceCreator = mock(ObjectInstanceCreator.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        String propName = "propName";

        when(setter.callIfAccessible(anyObject())).thenCallRealMethod();
        when(objectInstanceCreator.createInstanceIfAbsent(any(), any())).thenReturn(instance);
        when(ctxt.findBeanProperty(_property)).thenReturn(property);

        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, null, null, valueDeserializer, null);
        settableAnyProperty.setInstanceCreator(objectInstanceCreator);

        JsonParser p = mock(JsonParser.class);

        settableAnyProperty.deserializeAndSet(p, ctxt, instance, propName);

        verify(p).nextToken();
        verify(p).nextToken();
        verify(valueDeserializer).getObjectIdReader();
        verify(valueDeserializer).getObjectId(p, ctxt);
    }

    @Test
    public void testSet() throws Exception {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        Object instance = mock(Object.class);
        Object propName = "propName";
        Object value = "value";

        when(setter.callIfAccessible(anyObject())).thenCallRealMethod();

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, null, null, null, null);
        settableAnyProperty.set(instance, propName, value);

        verify(setter).callIfAccessible(instance);
        verify(setter).callOnWith(instance, propName, value);
    }

    @Test(expected = IOException.class)
    public void testThrowAsIOE() throws Exception {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        Object instance = mock(Object.class);
        Object propName = "propName";
        Object value = "value";
        Exception exception = new IOException("Exception message");

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, null, null, null, null);
        settableAnyProperty._throwAsIOE(exception, propName, value);

        fail("Should throw IOException");
    }
}