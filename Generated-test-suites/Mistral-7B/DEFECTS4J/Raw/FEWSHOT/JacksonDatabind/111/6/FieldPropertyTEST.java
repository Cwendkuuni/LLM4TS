package com.fasterxml.jackson.databind.deser.impl;

import static com.fasterxml.jackson.core.BaseTest.createParser;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class FieldPropertyTest {

    @Test
    public void testConstructorAndGetters() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(
                JsonProperty.Std.SimpleValue.class, "testField", null);
        JavaType type = ObjectType.constructUnboundType(String.class);
        TypeDeserializer typeDeser = new StringDeserializer();
        Annotations contextAnnotations = new StandardAnnotations();
        AnnotatedField field = new AnnotatedField(new TestAnnotation(), new TestField());

        FieldProperty property = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);
        assertEquals(propDef, property.getPropertyDefinition());
        assertEquals(type, property.getType());
        assertEquals(typeDeser, property.getTypeDeserializer());
        assertEquals(contextAnnotations, property.getAnnotationIntrospector());
        assertEquals(field, property.getMember());
        assertEquals(field.getAnnotated(), property._annotated);
        assertEquals(field.getAnnotated().getClass(), property._annotated.getClass());
        assertEquals(field.getAnnotated().getDeclaringClass(), property._annotated.getDeclaringClass());
        assertEquals(field.getName(), property.getName());
        assertEquals(field.getName(), property.getNameAsString());
        assertEquals(property._annotated.getClass(), field.getClass());
        assertEquals(property._field, field.getAnnotated());
        assertEquals(property._field, field.getAnnotated().getMember());
        assertSame(property._field, property._field);
        assertEquals(property._skipNulls, property.isSkipNulls());
    }

    @Test
    public void testConstructionWithDifferentFields() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(
                JsonProperty.Std.SimpleValue.class, "testField1", null);
        JavaType type1 = ObjectType.constructUnboundType(String.class);
        TypeDeserializer typeDeser1 = new StringDeserializer();
        Annotations contextAnnotations1 = new StandardAnnotations();
        AnnotatedField field1 = new AnnotatedField(new TestAnnotation(), new TestField1());

        BeanPropertyDefinition propDef2 = new BeanPropertyDefinition(
                JsonProperty.Std.SimpleValue.class, "testField2", null);
        JavaType type2 = ObjectType.constructUnboundType(Integer.class);
        TypeDeserializer typeDeser2 = new NumberDeserializer();
        Annotations contextAnnotations2 = new StandardAnnotations();
        AnnotatedField field2 = new AnnotatedField(new TestAnnotation(), new TestField2());

        FieldProperty property1 = new FieldProperty(propDef1, type1, typeDeser1, contextAnnotations1, field1);
        FieldProperty property2 = new FieldProperty(propDef2, type2, typeDeser2, contextAnnotations2, field2);

        assertNotSame(property1, property2);
        assertNotSame(property1._annotated, property2._annotated);
        assertNotSame(property1._field, property2._field);
    }

    @Test
    public void testSetters() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(
                JsonProperty.Std.SimpleValue.class, "testField", null);
        JavaType type = ObjectType.constructUnboundType(String.class);
        TypeDeserializer typeDeser = new StringDeserializer();
        Annotations contextAnnotations = new StandardAnnotations();
        AnnotatedField field = new AnnotatedField(new TestAnnotation(), new TestField());

        FieldProperty property = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);

        AnnotatedField field2 = new AnnotatedField(new TestAnnotation(), new TestField2());
        FieldProperty propertyWithDifferentField = new FieldProperty(property, null, null);
        PropertyName newName = new PropertyName("testField2");
        FieldProperty propertyWithNewName = new FieldProperty(property, newName);

        assertEquals(property, property.withName(newName));
        assertEquals(property, propertyWithNewName);
        assertEquals(property, propertyWithDifferentField.withName(property.getName()));
        assertEquals(property, propertyWithDifferentField);

        assertEquals(property, property.withValueDeserializer(new StringDeserializer()));
        assertEquals(property, property.withNullProvider(new AlwaysNullNullValueProvider()));
        assertEquals(property, property.withNullProvider(new NeverNullNullValueProvider()));
    }

    @Test
    public void testFixAccess() {
        Field field = new TestField();
        FieldProperty property = new FieldProperty(null, null, null, null, field);
        DeserializationConfig config = new DeserializationConfig();

        property.fixAccess(config);

        assertFalse(config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
        assertTrue(field.isAccessible());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFixAccessWithNullField() {
        FieldProperty property = new FieldProperty(null, null, null, null, null);
        DeserializationConfig config = new DeserializationConfig();

        property.fixAccess(config);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Object instance = new TestClass();
        JsonParser parser = createParser("\"foo\"");
        DeserializationContext ctxt = new DeserializationContext();

        FieldProperty property = new FieldProperty(null, null, null, null, new TestField());
        property.deserializeAndSet(parser, ctxt, instance);

        assertEquals("foo", instance.getFoo());
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        Object instance = new TestClass();
        JsonParser parser = createParser("\"foo\"");
        DeserializationContext ctxt = new DeserializationContext();

        FieldProperty property = new FieldProperty(null, null, null, null, new TestField());
        Object result = property.deserializeSetAndReturn(parser, ctxt, instance);

        assertEquals(instance, result);
        assertEquals("foo", instance.getFoo());
    }

    @Test
    public void testSet() throws IOException {
        Object instance = new TestClass();
        String value = "foo";

        FieldProperty property = new FieldProperty(null, null, null, null, new TestField());
        property.set(instance, value);

        assertEquals("foo", instance.getFoo());
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new TestClass();
        String value = "foo";

        FieldProperty property = new FieldProperty(null, null, null, null, new TestField());
        Object result = property.setAndReturn(instance, value);

        assertEquals(instance, result);
        assertEquals("foo", instance.getFoo());
    }

    @Test
    public void testReadResolve() {
        FieldProperty property = new FieldProperty(null);
        Object resolved = property.readResolve();

        assertEquals(property, resolved);
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        Object instance = new TestClass();
        JsonParser parser = createParser("42");
        DeserializationContext ctxt = new DeserializationContext();

        FieldProperty property = new FieldProperty(null, null, null, null, new TestField());
        ObjectDeserializer deserializer = new ObjectDeserializer(null, null);
        TypeDeserializer typeDeser = new IntDeserializer();

        Object result = property.deserializeWithType(parser, ctxt, typeDeser);

        assertEquals(42, instance.getFoo());
        assertEquals(Integer.TYPE, instance.getClass().getField("foo").getType());
    }

    static class TestAnnotation {}
    static class TestClass {
        private String foo;

        public String getFoo() {
            return foo;
        }

        public void setFoo(String foo) {
            this.foo = foo;
        }
    }
    static class TestField {/* empty */}
    static class TestField1 {/* empty */}
    static class TestField2 {/* empty */}

    static class AlwaysNullNullValueProvider implements NullValueProvider {
        @Override
        public Object getNullValue(DeserializationContext ctxt) {
            return null;
        }
    }

    static class NeverNullNullValueProvider implements NullValueProvider {
        @Override
        public Object getNullValue(DeserializationContext ctxt) {
            throw new IllegalStateException();
        }
    }
}