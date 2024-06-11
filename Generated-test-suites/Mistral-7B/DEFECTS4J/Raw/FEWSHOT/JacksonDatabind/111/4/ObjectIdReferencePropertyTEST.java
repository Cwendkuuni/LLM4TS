package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.NameTransformer;

public class ObjectIdReferencePropertyTest {

    private static final JsonDeserializer<Object> NO_OP_DESERIALIZER = new StdSerializer<Object>(Object.class) {};
    private static final NullValueProvider NULL_VALUE_PROVIDER = new StdSerializer<Object>(Object.class) {};

    @Test
    public void testConstructorWithForwardAndObjectIdInfo() {
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        assertSame(forward, property._forward);
        assertSame(objectIdInfo, property._objectIdInfo);
    }

    @Test
    public void testConstructorWithSameProperties() {
        SettableBeanProperty srcProperty = spy(new ObjectIdReferenceProperty(new Object(), NO_OP_DESERIALIZER, NULL_VALUE_PROVIDER));
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(srcProperty, new PropertyName("newName"));
        assertSame(srcProperty, property);
        assertEquals("newName", property.getName().asPrefix());
    }

    @Test
    public void testConstructorWithDeserializerAndNullValueProvider() {
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, NO_OP_DESERIALIZER, NULL_VALUE_PROVIDER);
        assertSame(forward, property._forward);
        assertNull(property._objectIdInfo);
    }

    @Test
    public void testConstructorWithSamePropertiesAndNameTransformer() {
        SettableBeanProperty srcProperty = spy(new ObjectIdReferenceProperty(new Object(), NO_OP_DESERIALIZER, NULL_VALUE_PROVIDER));
        NameTransformer nameTransformer = mock(NameTransformer.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(srcProperty, new PropertyName("newName"), nameTransformer);
        assertSame(srcProperty, property);
        assertEquals("newName", property.getName().asPrefix());
        verify(nameTransformer).transform(srcProperty.getName().asPrefix(), (Type)srcProperty.getTypeDeserializer().getType());
    }

    @Test
    public void testWithName() {
        SettableBeanProperty srcProperty = mock(SettableBeanProperty.class);
        ObjectIdReferenceProperty property = srcProperty.withName(new PropertyName("newName"));
        assertSame(srcProperty, property);
        assertEquals("newName", property.getName().asPrefix());
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        SettableBeanProperty srcProperty = mock(SettableBeanProperty.class);
        ObjectIdReferenceProperty property = srcProperty.withValueDeserializer(deserializer);
        assertSame(srcProperty, property);
        assertSame(deserializer, property._valueDeserializer);
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider nva = mock(NullValueProvider.class);
        SettableBeanProperty srcProperty = mock(SettableBeanProperty.class);
        ObjectIdReferenceProperty property = srcProperty.withNullProvider(nva);
        assertSame(srcProperty, property);
        assertSame(nva, property._nullProvider);
    }

    @Test
    public void testFixAccess() throws IOException {
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, new ObjectIdInfo());
        property.fixAccess(mock(DeserializationConfig.class));
        verify(forward).fixAccess(mock(DeserializationConfig.class));
    }

    @Test
    public void testGetAnnotation() {
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        JsonIdentityInfo jsonIdentityInfo = mock(JsonIdentityInfo.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, jsonIdentityInfo);
        assertEquals(jsonIdentityInfo, property.getAnnotation(JsonIdentityInfo.class));
        assertNull(property.getAnnotation(JsonDeserializer.class));
    }

    @Test
    public void testGetMember() {
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, new ObjectIdInfo());
        assertEquals(forward.getMember(), property.getMember());
    }

    @Test
    public void testGetCreatorIndex() {
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, new ObjectIdInfo());
        assertEquals(forward.getCreatorIndex(), property.getCreatorIndex());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, deserializer, NULL_VALUE_PROVIDER);
        Object instance = new Object();
        Object value = "value";
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        when(deserializer.deserialize(p, ctxt)).thenReturn(value);
        property.deserializeAndSet(p, ctxt, instance);
        verify(instance).set(eq(value));
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, deserializer, NULL_VALUE_PROVIDER);
        Object instance = new Object();
        Object value = "value";
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        when(deserializer.deserialize(p, ctxt)).thenReturn(value);
        when(forward.setAndReturn(instance, value)).thenReturn("returnValue");

        Object result = property.deserializeSetAndReturn(p, ctxt, instance);
        assertSame("returnValue", result);
    }

    @Test
    public void testResolveForwardReference() throws IOException {
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, objectIdInfo);
        UnresolvedForwardReference reference = mock(UnresolvedForwardReference.class);
        Object id = new Object();
        Object value = new Object();

        property.deserializeSetAndReturn(mock(JsonParser.class), mock(DeserializationContext.class), new Object());

        property.handleResolvedForwardReference(id, value);
        verify(reference).getRoid().appendReferring(eq(new PropertyReferring(property, reference, ClassUtil.getRawClass(Object.class), new Object())));
    }

    private static class TestObject {
        @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    @Test
    public void testDeserializeIdentityObject() throws IOException {
        ObjectIdGenerator generator = mock(ObjectIdGenerator.class);
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, new ObjectIdInfo());
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new TestObject();

        when(deserializer.deserialize(p, ctxt)).thenReturn(instance);
        when(generator.generateId(p, ctxt, instance)).thenReturn("idValue");
        when(ctxt.findValueDeserializer(instance.getClass())).thenReturn(deserializer);
        when(ctxt.findObjectIdGenerator(instance.getClass())).thenReturn(generator);

        Object result = property.deserializeAndSet(p, ctxt, instance);
        assertSame(instance, result);
        verify(instance).setId("idValue");
    }

    @Test
    public void testDeserializeIdentityObjectWithNameTransformer() throws IOException {
        ObjectIdGenerator generator = mock(ObjectIdGenerator.class);
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, new ObjectIdInfo());
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new TestObject();
        NameTransformer nameTransformer = mock(NameTransformer.class);

        when(deserializer.deserialize(p, ctxt)).thenReturn(instance);
        when(generator.generateId(p, ctxt, instance)).thenReturn("idValue");
        when(ctxt.findValueDeserializer(instance.getClass())).thenReturn(deserializer);
        when(ctxt.findObjectIdGenerator(instance.getClass())).thenReturn(generator);
        when(instance.getClass()).thenReturn(Object.class);

        Object result = property.deserializeAndSet(p, ctxt, instance);
        assertSame(instance, result);
        verify(instance).setId("idValue");
        verify(nameTransformer).transform("id", ClassUtil.getRawClass(Object.class));
    }

    @Test
    public void testDeserializeIdentityObjectWithNullValueProvider() throws IOException {
        ObjectIdGenerator generator = mock(ObjectIdGenerator.class);
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, new ObjectIdInfo(), NULL_VALUE_PROVIDER);
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new TestObject();

        when(deserializer.deserialize(p, ctxt)).thenReturn(instance);
        when(generator.generateId(p, ctxt, instance)).thenReturn("idValue");

        Object result = property.deserializeAndSet(p, ctxt, instance);
        assertSame(instance, result);

        Object nullValue = NULL_VALUE_PROVIDER.getValue(p, ctxt, Object.class);
        verify(instance).setId(nullValue);
    }

    @Test
    public void testDeserializeIdentityObjectWithValueDeserializer() throws IOException {
        ObjectIdGenerator generator = mock(ObjectIdGenerator.class);
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, new ObjectIdInfo(), deserializer);
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new TestObject();

        when(deserializer.deserialize(p, ctxt)).thenReturn("value");
        when(generator.generateId(p, ctxt, instance)).thenReturn("idValue");

        Object result = property.deserializeAndSet(p, ctxt, instance);
        assertSame("value", result);
        verify(instance).setId("idValue");
    }

    @Test
    public void testDeserializeIdentityObjectWithNameTransformerAndValueDeserializer() throws IOException {
        ObjectIdGenerator generator = mock(ObjectIdGenerator.class);
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, new ObjectIdInfo(), deserializer);
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new TestObject();
        NameTransformer nameTransformer = mock(NameTransformer.class);

        when(deserializer.deserialize(p, ctxt)).thenReturn("value");
        when(generator.generateId(p, ctxt, instance)).thenReturn("idValue");

        Object result = property.deserializeAndSet(p, ctxt, instance);
        assertSame("value", result);
        verify(instance).setId("idValue");
        verify(nameTransformer).transform("id", ClassUtil.getRawClass(Object.class));
    }

    @Test
    public void testDeserializeIdentityObjectWithJsonIdentityInfo() throws IOException {
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, objectIdInfo, deserializer);
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new TestObject();

        when(deserializer.deserialize(p, ctxt)).thenReturn(instance);

        Object result = property.deserializeAndSet(p, ctxt, instance);
        assertSame(instance, result);
    }
}