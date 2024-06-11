package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class InnerClassPropertyTest {
    @Test
    public void testConstructor() {
        SettableBeanProperty delegate = new SettableBeanProperty();
        Constructor<?> ctor = new Constructor<>();
        InnerClassProperty property = new InnerClassProperty(delegate, ctor);
        
        assertNotNull(property);
        assertEquals(delegate, property._delegate);
        assertEquals(ctor, property._creator);
    }

    @Test
    public void testDeserializationAndSet() {
        SettableBeanProperty delegate = new SettableBeanProperty();
        Constructor<?> ctor = new Constructor<>();
        InnerClassProperty property = new InnerClassProperty(delegate, ctor);

        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object bean = new Object();
        
        // Test with JSON token VALUE_NULL
        jp.setCurrentToken(JsonToken.VALUE_NULL);
        property.deserializeAndSet(jp, ctxt, bean);
        assertNull(property.get(bean));

        // Test with non-null value
        jp.setCurrentToken(JsonToken.VALUE_STRING);
        property.deserializeAndSet(jp, ctxt, bean);
        assertNotNull(property.get(bean));
    }

    @Test
    public void testDeserializeSetAndReturn() {
        SettableBeanProperty delegate = new SettableBeanProperty();
        Constructor<?> ctor = new Constructor<>();
        InnerClassProperty property = new InnerClassProperty(delegate, ctor);

        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();

        // Test with JSON token VALUE_NULL
        jp.setCurrentToken(JsonToken.VALUE_NULL);
        assertNull(property.deserializeSetAndReturn(jp, ctxt, instance));

        // Test with non-null value
        jp.setCurrentToken(JsonToken.VALUE_STRING);
        assertNotNull(property.deserializeSetAndReturn(jp, ctxt, instance));
    }

    @Test
    public void testWithMethods() {
        SettableBeanProperty delegate = new SettableBeanProperty();
        Constructor<?> ctor = new Constructor<>();
        InnerClassProperty property = new InnerClassProperty(delegate, ctor);
        
        PropertyName newName = new PropertyName("test");
        property.withName(newName);
        assertEquals(newName, property.getName());
        
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        property.withValueDeserializer(deser);
        assertEquals(deser, property.getValueDeserializer());
        
        int index = 1;
        property.assignIndex(index);
        assertEquals(index, property.getPropertyIndex());
        
        assertNull(property.getAnnotation(null));
        
        AnnotatedMember member = new AnnotatedMember();
        assertEquals(member, property.getMember());
        
        Object instance = new Object();
        Object value = new Object();
        property.set(instance, value);
        assertEquals(value, property.get(instance));
        
        assertEquals(value, property.setAndReturn(instance, value));
        
        assertNotNull(property.readResolve());
        assertNotNull(property.writeReplace());
    }
}