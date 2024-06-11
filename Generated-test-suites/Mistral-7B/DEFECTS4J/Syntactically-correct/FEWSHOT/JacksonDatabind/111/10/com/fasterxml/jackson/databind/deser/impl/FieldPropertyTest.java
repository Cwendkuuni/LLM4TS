package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.test.BaseTest;

public class FieldPropertyTest extends BaseTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeAndSet() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser p = mapper.getFactory().createParser("{\"foo\": null}");
        p.nextToken();

        MyClass instance = new MyClass();
        FieldProperty fieldProperty = new FieldProperty(
                new BeanPropertyDefinition(new AnnotatedMethod("setFoo", MyClass.class, null), null),
                null, null, null, mock(AnnotatedField.class));
        AnnotatedMember annotatedMember = mock(AnnotatedMember.class);
        when(annotatedMember.getAnnotated()).thenReturn(new AnnotatedField(MyClass.class.getField("foo")));
        fieldProperty.fixAccess(new DeserializationConfig(mapper.getDeserializationConfig()));
        fieldProperty.set(instance, null);

        fieldProperty.deserializeAndSet(p, new DeserializationContext(mapper), instance);
        assertEquals(null, instance.foo);

        p = mapper.getFactory().createParser("{\"foo\": 1}");
        p.nextToken();

        Object value = fieldProperty.deserializeAndSet(p, new DeserializationContext(mapper), instance);
        assertEquals(1, instance.foo);
        assertEquals(1, value);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser p = mapper.getFactory().createParser("{\"foo\": null}");
        p.nextToken();

        MyClass instance = new MyClass();
        FieldProperty fieldProperty = new FieldProperty(
                new BeanPropertyDefinition(new AnnotatedMethod("setFoo", MyClass.class, null), null),
                null, null, null, mock(AnnotatedField.class));
        AnnotatedMember annotatedMember = mock(AnnotatedMember.class);
        when(annotatedMember.getAnnotated()).thenReturn(new AnnotatedField(MyClass.class.getField("foo")));
        fieldProperty.fixAccess(new DeserializationConfig(mapper.getDeserializationConfig()));

        Object value = fieldProperty.deserializeSetAndReturn(p, new DeserializationContext(mapper), instance);
        assertEquals(instance, value);
        assertNull(instance.foo);

        p = mapper.getFactory().createParser("{\"foo\": 1}");
        p.nextToken();

        value = fieldProperty.deserializeSetAndReturn(p, new DeserializationContext(mapper), instance);
        assertEquals(instance, value);
        assertEquals(1, instance.foo);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testSet() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        MyClass instance = new MyClass();

        FieldProperty fieldProperty = new FieldProperty(
                new BeanPropertyDefinition(new AnnotatedMethod("setFoo", MyClass.class, null), null),
                null, null, null, mock(AnnotatedField.class));
        AnnotatedMember annotatedMember = mock(AnnotatedMember.class);
        when(annotatedMember.getAnnotated()).thenReturn(new AnnotatedField(MyClass.class.getField("foo")));
        fieldProperty.fixAccess(new DeserializationConfig(mapper.getDeserializationConfig()));

        fieldProperty.set(instance, 1);
        assertEquals(1, instance.foo);
    }

    @Test
    public void testAnnotation() {
        FieldProperty fieldProperty = new FieldProperty(
                new BeanPropertyDefinition(new AnnotatedMethod("setFoo", MyClass.class, null), null),
                null, null, null, mock(AnnotatedField.class));
        AnnotatedField annotatedField = mock(AnnotatedField.class);
        when(annotatedField.getAnnotation(JsonProperty.class)).thenReturn(new JsonProperty(name = "foo"));
        when(annotatedField.getMember()).thenReturn(annotatedMember);
        fieldProperty._annotated = annotatedField;

        assertEquals("foo", fieldProperty.getName());
    }

    @Test
    public void testWithName() {
        FieldProperty fieldProperty = new FieldProperty(
                new BeanPropertyDefinition(new AnnotatedMethod("setFoo", MyClass.class, null), null),
                null, null, null, mock(AnnotatedField.class));
        PropertyName newName = mock(PropertyName.class);
        fieldProperty.withName(newName);
        assertEquals(newName, fieldProperty.getName());
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<String> valueDeserializer = mock(JsonDeserializer.class);
        FieldProperty fieldProperty = new FieldProperty(
                new BeanPropertyDefinition(new AnnotatedMethod("setFoo", MyClass.class, null), null),
                null, null, null, mock(AnnotatedField.class));
        fieldProperty.withValueDeserializer(valueDeserializer);
        assertEquals(valueDeserializer, fieldProperty.getValueDeserializer());
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);
        FieldProperty fieldProperty = new FieldProperty(
                new BeanPropertyDefinition(new AnnotatedMethod("setFoo", MyClass.class, null), null),
                null, null, null, mock(AnnotatedField.class));
        fieldProperty.withNullProvider(nullValueProvider);
        assertEquals(nullValueProvider, fieldProperty.getNullProvider());
    }

    @Test
    public void testFixAccess() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        MyClass instance = new MyClass();
        FieldProperty fieldProperty = new FieldProperty(
                new BeanPropertyDefinition(new AnnotatedMethod("setFoo", MyClass.class, null), null),
                null, null, null, mock(AnnotatedField.class));
        AnnotatedMember annotatedMember = mock(AnnotatedMember.class);
        when(annotatedMember.getAnnotated()).thenReturn(new AnnotatedField(MyClass.class.getField("foo")));
        fieldProperty.fixAccess(new DeserializationConfig(mapper.getDeserializationConfig()));

        try {
            fieldProperty.set(instance, "foo");
            fail("expected IllegalAccessException");
        } catch (IllegalAccessException e) {
            // expected
        }

        mapper.getDeserializationConfig().setEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS);
        fieldProperty.fixAccess(new DeserializationConfig(mapper.getDeserializationConfig()));
        fieldProperty.set(instance, "foo");
        assertEquals("foo", instance.foo);
    }

    @Test
    public void testGetAnnotation() {
        MyClass myClass = new MyClass();
        Field field = MyClass.class.getField("foo");
        JsonProperty jsonProperty = new JsonProperty(name = "foo");
        AnnotatedField annotatedField = new AnnotatedField(field, null, jsonProperty);
        FieldProperty fieldProperty = new FieldProperty(
                new BeanPropertyDefinition(new AnnotatedMethod("setFoo", MyClass.class, null), null),
                null, null, null, annotatedField);

        assertEquals(jsonProperty, fieldProperty.getAnnotation(JsonProperty.class));
    }

    @Test
    public void testGetMember() {
        MyClass myClass = new MyClass();
        Field field = MyClass.class.getField("foo");
        AnnotatedField annotatedField = new AnnotatedField(field, null, null);
        FieldProperty fieldProperty = new FieldProperty(
                new BeanPropertyDefinition(new AnnotatedMethod("setFoo", MyClass.class, null), null),
                null, null, null, annotatedField);

        assertEquals(annotatedField, fieldProperty.getMember());
    }

    @Test
    public void testDeserializeWithType() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser p = mapper.getFactory().createParser("{\"foo\": 1}");
        p.nextToken();

        MyClass instance = new MyClass();
        FieldProperty fieldProperty = new FieldProperty(
                new BeanPropertyDefinition(new AnnotatedMethod("setFoo", MyClass.class, null), null),
                null, null, null, mock(AnnotatedField.class));
        AnnotatedMember annotatedMember = mock(AnnotatedMember.class);
        when(annotatedMember.getAnnotated()).thenReturn(new AnnotatedField(MyClass.class.getField("foo")));
        fieldProperty.fixAccess(new DeserializationConfig(mapper.getDeserializationConfig()));

        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        Object value = fieldProperty.deserializeWithType(p, new DeserializationContext(mapper), typeDeserializer);
        assertEquals(1, value);
    }

    @Test
    public void testReadResolve() throws Exception {
        MyClass instance = new MyClass();
        FieldProperty fieldProperty = new FieldProperty(new FieldProperty(instance.getClass().getField("foo")));
        assertEquals(fieldProperty, fieldProperty.readResolve());
    }

    static class MyClass {
        @JsonProperty
        int foo;
    }
}