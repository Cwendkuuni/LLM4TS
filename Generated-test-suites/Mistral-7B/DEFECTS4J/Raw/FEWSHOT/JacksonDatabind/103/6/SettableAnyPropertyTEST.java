package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;

public class SettableAnyPropertyTest {

    @Test
    public void testSettableAnyProperty() {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        assertEquals(property, settableAnyProperty.getProperty());
        assertTrue(settableAnyProperty.hasValueDeserializer());
        assertEquals(type, settableAnyProperty.getType());
    }

    @Test
    public void testSettableAnyPropertyWithValueDeserializer() {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, null, valueDeserializer, valueTypeDeserializer);

        SettableAnyProperty settableAnyPropertyWithValueDeserializer = settableAnyProperty.withValueDeserializer(new JsonDeserializer<>());

        assertNotSame(valueDeserializer, settableAnyPropertyWithValueDeserializer._valueDeserializer);
        assertEquals(valueDeserializer, settableAnyPropertyWithValueDeserializer._valueDeserializer.getValueDeserialator());
    }

    @Test
    public void testFixAccess() {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, null, null, null, null);

        DeserializationConfig config = mock(DeserializationConfig.class);

        settableAnyProperty.fixAccess(config);

        assertTrue(setter.isAccessible());
    }

    @Test
    public void testReadResolve() {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        assertEquals(settableAnyProperty, settableAnyProperty.readResolve());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);

        Object instance = new Object();
        String propName = "propName";

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        Object key = "key";
        Object value = new Object();

        when(keyDeserializer.deserializeKey(eq(propName), eq(ctxt))).thenReturn(key);
        when(valueDeserializer.deserialize(eq(p), eq(ctxt))).thenReturn(value);

        settableAnyProperty.deserializeAndSet(p, ctxt, instance, propName);

        verify(setter).callOnWith(instance, eq(propName), eq(value));
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeAndSetWithUnresolvedForwardReference() throws IOException {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);

        Object instance = new Object();
        String propName = "propName";

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        UnresolvedForwardReference reference = mock(UnresolvedForwardReference.class);

        when(valueDeserializer.getObjectIdReader()).thenReturn(new ObjectIdReaderForTest());
        when(keyDeserializer.deserializeKey(eq(propName), eq(ctxt))).thenReturn(propName);

        doThrow(new UnresolvedForwardReference("", null)).when(valueDeserializer).deserialize(eq(p), eq(ctxt));

        settableAnyProperty.deserializeAndSet(p, ctxt, instance, propName);

        fail("Should have thrown JsonMappingException");
    }

    @Test
    public void testSet() throws IOException {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        Object instance = new Object();
        Object propName = "propName";
        Object value = new Object();

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, null, null, null, null);

        when(setter.getValue(instance)).thenReturn(new HashMap<>());

        settableAnyProperty.set(instance, propName, value);

        verify(setter).callOnWith(instance, eq(propName), eq(value));
    }

    @Test(expected = JsonMappingException.class)
    public void testSetWithIllegalArgumentException() throws IOException {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        Object instance = new Object();
        Object propName = "propName";
        Object value = new Object();

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, null, null, null, null);

        when(setter.getValue(instance)).thenThrow(new IllegalArgumentException("Test exception"));

        settableAnyProperty.set(instance, propName, value);

        fail("Should have thrown JsonMappingException");
    }

    @Test
    public void testThrowAsIOE() throws IOException {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        Object instance = new Object();
        Object propName = "propName";
        Object value = new Object();

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, null, null, null, null);

        Exception e = new IOException("Test exception");

        settableAnyProperty._throwAsIOE(e, propName, value);

        fail("Should have thrown JsonMappingException");
    }

    @Test
    public void testThrowAsIOEWithIllegalArgumentException() throws IOException {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        Object instance = new Object();
        Object propName = "propName";
        Object value = new Object();

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, null, null, null, null);

        IllegalArgumentException e = new IllegalArgumentException("Test exception");

        settableAnyProperty._throwAsIOE(e, propName, value);

        fail("Should have thrown JsonMappingException");
    }

    @Test
    public void testThrowAsIOEWithRuntimeException() throws IOException {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        Object instance = new Object();
        Object propName = "propName";
        Object value = new Object();

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, null, null, null, null);

        RuntimeException e = new RuntimeException("Test exception");

        settableAnyProperty._throwAsIOE(e, propName, value);

        fail("Should have thrown JsonMappingException");
    }

    @Test
    public void testToString() {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);

        String className = "className";

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, null, null, null, null);

        assertEquals("[any property on class " + className + "]", settableAnyProperty.toString());
    }
}

class ObjectIdReaderForTest implements ObjectIdReader {
    @Override
    public ObjectId readId(JsonParser p, DeserializationContext ctxt) throws IOException {
        return null;
    }
}