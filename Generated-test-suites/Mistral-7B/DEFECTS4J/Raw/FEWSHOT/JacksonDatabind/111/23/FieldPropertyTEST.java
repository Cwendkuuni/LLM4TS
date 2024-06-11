package com.fasterxml.jackson.databind.deser.impl;

import static com.fasterxml.jackson.core.JsonToken.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.ObjectMapperTestBase;

public class FieldPropertyTest extends ObjectMapperTestBase {

    static class Foo {
        @JsonProperty
        private int _field;
    }

    @Test
    public void testBasic() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        AnnotatedField<Integer> annotatedField = Annotations.findAnnotation(Foo.class, JsonProperty.class);
        FieldProperty fieldProperty = new FieldProperty(
                new BeanPropertyDefinition(Foo.class, "_field", null, null, null, null, null, null, null, null),
                mapper.getTypeFactory().constructType(Integer.class), null, null, annotatedField);

        JsonParser p = mapper.getFactory().createParser("{\"_field\": 42}");
        p.nextToken();
        Foo foo = mapper.readValue(p, Foo.class);
        assertEquals(42, foo._field);
    }

    @Test
    public void testNullValue() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        AnnotatedField<Integer> annotatedField = Annotations.findAnnotation(Foo.class, JsonProperty.class);
        FieldProperty fieldProperty = new FieldProperty(
                new BeanPropertyDefinition(Foo.class, "_field", null, null, null, null, null, null, null, null),
                mapper.getTypeFactory().constructType(Integer.class), null, null, annotatedField);

        JsonParser p = mapper.getFactory().createParser("{}");
        p.nextToken();
        Foo foo = mapper.readValue(p, Foo.class);
        assertNull(foo._field);
    }

    @Test
    public void testSkippingNulls() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectMapper mapper2 = new ObjectMapper().disable(MapperFeature.AUTO_DESERIALIZE);
        AnnotatedField<Integer> annotatedField = Annotations.findAnnotation(Foo.class, JsonProperty.class);
        FieldProperty fieldProperty = new FieldProperty(
                new BeanPropertyDefinition(Foo.class, "_field", null, null, null, null, null, null, null, null),
                mapper.getTypeFactory().constructType(Integer.class), null, null, annotatedField);
        NullValueProvider nvp = mapper2.getNullValueProvider();
        FieldProperty fieldPropertyWithNullProvider = new FieldProperty(
                fieldProperty, null, nvp);

        JsonParser p = mapper2.getFactory().createParser("{}");
        p.nextToken();
        Foo foo = mapper2.readValue(p, Foo.class);
        assertNull(foo._field);

        p = mapper.getFactory().createParser("{}");
        p.nextToken();
        Foo foo2 = mapper.readValue(p, Foo.class);
        assertNotNull(foo2);
    }

    @Test
    public void testTypeDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        AnnotatedField<String> annotatedField = Annotations.findAnnotation(Foo.class, JsonProperty.class);
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(
                Foo.class, "_field", null, null, null, null, null, null, String.class, null);
        TypeDeserializer typeDeserializer = new StandardSerializer<>(String.class) {}.withCurrentVersion(VERSION_1_1);
        FieldProperty fieldProperty = new FieldProperty(
                propDef, mapper.getTypeFactory().constructType(Foo.class), typeDeserializer, null, annotatedField);

        JsonParser p = mapper.getFactory().createParser("{\"_field\": \"foo\"}");
        p.nextToken();
        Foo foo = mapper.readValue(p, Foo.class);
        assertEquals("foo", foo._field);
    }

    @Test
    public void testWithName() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        AnnotatedField<Integer> annotatedField = Annotations.findAnnotation(Foo.class, JsonProperty.class);
        FieldProperty fieldProperty = new FieldProperty(
                new BeanPropertyDefinition(Foo.class, "_field", null, null, null, null, null, null, null, null),
                mapper.getTypeFactory().constructType(Integer.class), null, null, annotatedField);
        PropertyName newName = PropertyName.construct("newName");
        FieldProperty fieldPropertyWithNewName = new FieldProperty(fieldProperty, newName);

        assertEquals("_field", fieldProperty.getName());
        assertEquals("newName", fieldPropertyWithNewName.getName());
    }

    @Test
    public void testWithValueDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        AnnotatedField<Integer> annotatedField = Annotations.findAnnotation(Foo.class, JsonProperty.class);
        FieldProperty fieldProperty = new FieldProperty(
                new BeanPropertyDefinition(Foo.class, "_field", null, null, null, null, null, null, null, null),
                mapper.getTypeFactory().constructType(Integer.class), null, null, annotatedField);
        JsonDeserializer<Integer> jsonDeserializer = new StandardDeserializer<>(Integer.class);
        FieldProperty fieldPropertyWithJsonDeserializer = new FieldProperty(fieldProperty, jsonDeserializer);

        JsonParser p = mapper.getFactory().createParser("{\"_field\": 42}");
        p.nextToken();
        Foo foo = mapper.readValue(p, Foo.class);
        assertEquals(42, foo._field);
    }

    @Test
    public void testWithNullProvider() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        AnnotatedField<Integer> annotatedField = Annotations.findAnnotation(Foo.class, JsonProperty.class);
        FieldProperty fieldProperty = new FieldProperty(
                new BeanPropertyDefinition(Foo.class, "_field", null, null, null, null, null, null, null, null),
                mapper.getTypeFactory().constructType(Integer.class), null, null, annotatedField);
        NullValueProvider nvp = mapper.getNullValueProvider();
        FieldProperty fieldPropertyWithNullProvider = new FieldProperty(fieldProperty, null, nvp);

        JsonParser p = mapper.getFactory().createParser("{}");
        p.nextToken();
        Foo foo = mapper.readValue(p, Foo.class);
        assertNull(foo._field);
    }

    @Test
    public void testSetAndReturn() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        AnnotatedField<Integer> annotatedField = Annotations.findAnnotation(Foo.class, JsonProperty.class);
        FieldProperty fieldProperty = new FieldProperty(
                new BeanPropertyDefinition(Foo.class, "_field", null, null, null, null, null, null, null, null),
                mapper.getTypeFactory().constructType(Integer.class), null, null, annotatedField);

        Foo foo = new Foo();
        fieldProperty.set(foo, 42);
        assertEquals(42, foo._field);
    }

    @Test
    public void testSetAndReturnWithValueDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        AnnotatedField<String> annotatedField = Annotations.findAnnotation(Foo.class, JsonProperty.class);
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(
                Foo.class, "_field", null, null, null, null, null, null, String.class, null);
        TypeDeserializer typeDeserializer = new StandardSerializer<>(String.class) {}.withCurrentVersion(VERSION_1_1);
        FieldProperty fieldProperty = new FieldProperty(
                propDef, mapper.getTypeFactory().constructType(Foo.class), typeDeserializer, null, annotatedField);

        Foo foo = new Foo();
        JsonParser p = mapper.getFactory().createParser("{\"_field\": \"foo\"}");
        p.nextToken();
        fieldProperty.deserializeAndSet(p, null, foo);
        assertEquals("foo", foo._field);
    }

    @Test
    public void testFixAccess() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        AnnotatedField<Integer> annotatedField = AnnotatedField.noAnnotations(int[].class.getField("foo"), Integer.TYPE);
        FieldProperty fieldProperty = new FieldProperty(
                new BeanPropertyDefinition(Foo.class, "_field", null, null, null, null, null, null, null, null),
                mapper.getTypeFactory().constructType(Integer.class), null, null, annotatedField);

        Class<?> clazz = Foo.class;
        Field field = clazz.getDeclaredField("_field");
        field.setAccessible(true);
        Modifier modifiers = field.getModifiers();
        field.setAccessible(false);
        fieldProperty.fixAccess(mapper.getConfig());
        assertTrue(Modifier.isPublic(field.getModifiers()));
    }

    @Test
    public void testAccess() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        AnnotatedField<Integer> annotatedField = Annotations.findAnnotation(Foo.class, JsonProperty.class);
        FieldProperty fieldProperty = new FieldProperty(
                new BeanPropertyDefinition(Foo.class, "_field", null, null, null, null, null, null, null, null),
                mapper.getTypeFactory().constructType(Integer.class), null, null, annotatedField);

        AnnotatedMember annotatedMember = fieldProperty.getMember();
        assertEquals(annotatedMember, fieldProperty.getMember());
    }
}