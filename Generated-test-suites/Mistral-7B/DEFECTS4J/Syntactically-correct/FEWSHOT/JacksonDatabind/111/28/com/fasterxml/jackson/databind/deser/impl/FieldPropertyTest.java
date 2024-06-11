package com.fasterxml.jackson.databind.deser.impl;

import static com.fasterxml.jackson.core.JsonToken.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class FieldPropertyTest {

    static class Dto {
        @JsonProperty
        private String field1;
        @JsonIgnore
        private String field2;

        @JsonProperty
        public String getField1() {
            return field1;
        }

        @JsonIgnore
        public String getField2() {
            return field2;
        }

        public void setField1(String field1) {
            this.field1 = field1;
        }

        @JsonIgnore
        public void setField2(String field2) {
            this.field2 = field2;
        }

        @JsonProperty
        final private Field privateField = new Field("privateField", String.class, this);

        public Field getPrivateField() {
            return privateField;
        }

        @JsonIgnore
        public Field getIgnoreField() {
            return new Field("ignoreField", String.class, this);
        }
    }

    @Test
    public void testConstructors() {
        JavaType type = new TypeFactory().constructType(Dto.class);
        BeanPropertyDefinition propDef = new BeanPropertyDefinition("field1", String.class, null, null, null, null, null);
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedField annotatedField = new AnnotatedField("field1", new AnnotatedMember(new AnnotatedClass(Dto.class), "field1"), propDef, null, null);
        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, annotatedField);
        assertEquals("field1", fieldProperty.getName());
        assertEquals(String.class, fieldProperty.getType());
        assertNull(fieldProperty.getValueDeserializer());
        assertNull(fieldProperty.getNullProvider());

        AnnotatedField ignoreField = new AnnotatedField("ignoreField", new AnnotatedMember(new AnnotatedClass(Dto.class), "ignoreField"), propDef, null, null);
        FieldProperty ignoreFieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, ignoreField);
        assertTrue(ignoreFieldProperty.isIgnored());

        TypeDeserializer valueTypeDeserializer = new StringDeserializer();
        NullValueProvider nva = new StandardNullValueProvider();
        FieldProperty deserFieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, annotatedField, valueTypeDeserializer, nva);
        assertEquals(valueTypeDeserializer, deserFieldProperty.getValueDeserializer());
        assertEquals(nva, deserFieldProperty.getNullProvider());

        JavaType nullableType = null;
        FieldProperty nullableFieldProperty = new FieldProperty(propDef, nullableType, typeDeser, contextAnnotations, annotatedField);
        assertNull(nullableFieldProperty.getType());

        FieldProperty src = deserFieldProperty;
        FieldProperty deserFieldProperty2 = new FieldProperty(src, nullValueProvider());
        assertEquals(deserFieldProperty, deserFieldProperty2);

        PropertyName newName = new PropertyName("newName");
        FieldProperty newNameProperty = new FieldProperty(src, newName);
        assertEquals(newName, newNameProperty.getName());

        FieldProperty jdkSerial = new FieldProperty(src);
        assertEquals(src.getName(), jdkSerial.getName());
        assertEquals(src.getType(), jdkSerial.getType());
        assertEquals(src.getValueDeserializer(), jdkSerial.getValueDeserializer());
        assertEquals(src.getNullProvider(), jdkSerial.getNullProvider());
    }

    @Test
    public void testSettableBeanPropertyMethods() throws IOException {
        Dto dto = new Dto();
        FieldProperty fieldProperty = dto.getPrivateField();

        JsonParser p = new TestJsonParser();
        ObjectInstanceFactory oif = new ObjectMapper().getFactory();
        Object inst = oif.createInstance(Dto.class);

        JsonToken[] tokens = {VALUE_STRING, VALUE_NULL};
        for (JsonToken token : tokens) {
            p.nextToken();
            p.setValue(token == VALUE_NULL ? null : "foo");

            fieldProperty.deserializeAndSet(p, null, inst);
            assertEquals("foo", dto.getField1());

            fieldProperty.deserializeSetAndReturn(p, null, inst);
            assertEquals("foo", dto.getField1());

            fieldProperty.set(inst, "bar");
            assertEquals("bar", dto.getField1());

            fieldProperty.setAndReturn(inst, "baz");
            assertEquals("baz", dto.getField1());
        }

        p.nextToken(VALUE_NULL);
        fieldProperty.deserializeAndSet(p, null, inst);
        assertNull(dto.getField1());

        fieldProperty.deserializeSetAndReturn(p, null, inst);
        assertNull(dto.getField1());
    }

    @Test
    public void testJsonDeserializer() throws IOException {
        StringDeserializer deserializer = new StringDeserializer();

        Dto dto = new Dto();
        FieldProperty fieldProperty = new FieldProperty(new BeanPropertyDefinition("field1", String.class, null, null, null, null, null),
                new TypeFactory().constructType(Dto.class), deserializer, null,
                new AnnotatedField("field1", new AnnotatedMember(new AnnotatedClass(Dto.class), "field1"), null, null, null));

        ObjectInstanceFactory oif = new ObjectMapper().getFactory();
        Object inst = oif.createInstance(Dto.class);

        JsonParser p = new TestJsonParser();
        p.nextToken(VALUE_STRING);
        p.setValue("foo");

        fieldProperty.deserializeAndSet(p, null, inst);
        assertEquals("foo", dto.getField1());

        p.nextToken(VALUE_NUMBER);
        p.setValue(1L);
        fieldProperty.deserializeAndSet(p, null, inst);
        assertNull(dto.getField1());

        p.nextToken(VALUE_EOF);
        fieldProperty.deserializeAndSet(p, null, inst);
        assertNull(dto.getField1());

        p.nextToken(VALUE_NULL);
        fieldProperty.deserializeAndSet(p, null, inst);
        assertNull(dto.getField1());

        p.nextToken(VALUE_STRING);
        p.setValue("bar");
        fieldProperty.deserializeWithType(p, null, null);
        assertEquals("bar", dto.getField1());
    }

    @Test
    public void testWithName() {
        FieldProperty fieldProperty = new FieldProperty(new BeanPropertyDefinition("field1", String.class, null, null, null, null, null),
                new TypeFactory().constructType(Dto.class), null, null,
                new AnnotatedField("field1", new AnnotatedMember(new AnnotatedClass(Dto.class), "field1"), null, null, null));

        PropertyName newName = new PropertyName("newName");
        FieldProperty newNameProperty = fieldProperty.withName(newName);
        assertEquals(newName, newNameProperty.getName());
        assertEquals(fieldProperty, newNameProperty);
    }

    @Test
    public void testWithValueDeserializer() throws IOException {
        StringDeserializer deserializer = new StringDeserializer();

        Dto dto = new Dto();
        FieldProperty fieldProperty = new FieldProperty(new BeanPropertyDefinition("field1", String.class, null, null, null, null, null),
                new TypeFactory().constructType(Dto.class), null, null,
                new AnnotatedField("field1", new AnnotatedMember(new AnnotatedClass(Dto.class), "field1"), null, null, null));

        JsonParser p = new TestJsonParser();
        ObjectInstanceFactory oif = new ObjectMapper().getFactory();
        Object inst = oif.createInstance(Dto.class);

        fieldProperty = fieldProperty.withValueDeserializer(deserializer);

        p.nextToken(VALUE_STRING);
        p.setValue("foo");

        fieldProperty.deserializeAndSet(p, null, inst);
        assertEquals("foo", dto.getField1());
    }

    @Test
    public void testWithNullProvider() throws IOException {
        StandardNullValueProvider nva = new StandardNullValueProvider();

        Dto dto = new Dto();
        FieldProperty fieldProperty = new FieldProperty(new BeanPropertyDefinition("field1", String.class, null, null, null, null, null),
                new TypeFactory().constructType(Dto.class), null, null,
                new AnnotatedField("field1", new AnnotatedMember(new AnnotatedClass(Dto.class), "field1"), null, null, null));

        JsonParser p = new TestJsonParser();
        ObjectInstanceFactory oif = new ObjectMapper().getFactory();
        Object inst = oif.createInstance(Dto.class);

        fieldProperty = fieldProperty.withNullProvider(nva);

        p.nextToken(VALUE_NULL);
        fieldProperty.deserializeAndSet(p, null, inst);
        assertNull(dto.getField1());
    }

    @Test
    public void testFixAccess() throws IOException {
        Dto dto = new Dto();
        FieldProperty fieldProperty = dto.getPrivateField();

        DeserializationConfig config = new DeserializationConfig();
        config.setIsEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS, true);

        fieldProperty.fixAccess(config);
        try {
            fieldProperty.set(null, null);
            fail("Expected IllegalAccessException");
        } catch (IllegalAccessException e) {
            // Expected
        }
    }

    @Test
    public void testGetAnnotation() {
        Dto dto = new Dto();
        FieldProperty fieldProperty = dto.getPrivateField();

        JsonProperty jsonProperty = new JsonProperty();
        fieldProperty.setAnnotation(jsonProperty.annotationType());
        Annotation annotation = fieldProperty.getAnnotation(jsonProperty.annotationType());
        assertEquals(jsonProperty, annotation);

        JsonIgnore jsonIgnore = new JsonIgnore();
        fieldProperty.setAnnotation(jsonIgnore.annotationType());
        annotation = fieldProperty.getAnnotation(JsonIgnore.class);
        assertEquals(jsonIgnore, annotation);
    }

    @Test
    public void testGetMember() {
        Dto dto = new Dto();
        FieldProperty fieldProperty = dto.getPrivateField();

        AnnotatedMember member = fieldProperty.getMember();
        assertEquals(dto.getClass().getDeclaredField("privateField"), member.getAnnotated());
    }
}

class Field {
    String name;
    Class<?> type;
    Object obj;

    public Field(String name, Class<?> type, Object obj) {
        this.name = name;
        this.type = type;
        this.obj = obj;
    }
}

class StringDeserializer extends JsonDeserializer<String> {}

class StandardNullValueProvider implements NullValueProvider {}

class TestJsonParser extends JsonParser {}