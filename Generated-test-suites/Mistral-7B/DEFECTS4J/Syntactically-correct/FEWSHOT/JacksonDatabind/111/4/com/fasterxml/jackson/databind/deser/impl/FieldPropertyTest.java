package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;

/**
 * This concrete sub-class implements property that is set
 * directly assigning to a Field.
 */
public final class FieldPropertyTest {

    @Test
    public void testConstructor() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(new AnnotatedField(new Field(Integer.class, "field", Integer.TYPE)), "_name", null, null, null, null, null, null, null);
        JavaType type = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(Integer.class);
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = new StandardAnnotationInstaller();
        AnnotatedField field = new AnnotatedField(new Field(Integer.class, "field", Integer.TYPE), null, null, null);

        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);
        assertEquals(propDef, fieldProperty.getBeanPropertyDefinition());
        assertEquals(type, fieldProperty.getType());
        assertNull(fieldProperty.getTypeDeserializer());
        assertNull(fieldProperty.getContextAnnotations());
        assertEquals(field, fieldProperty.getAnnotated());
        assertEquals(field.getAnnotated(), fieldProperty.getAnnotatedField());
        assertNull(fieldProperty.getField());
        assertFalse(fieldProperty.isSkipNulls());

        FieldProperty fieldProperty2 = new FieldProperty(fieldProperty);
        assertEquals(fieldProperty, fieldProperty2);
    }

    @Test
    public void testConstructorWithName() {
        PropertyName newName = PropertyName.construct("newName");
        FieldProperty fieldProperty = new FieldProperty(new FieldProperty(new Object()), newName);
        assertEquals(newName, fieldProperty.getName());
        assertEquals(newName, fieldProperty.getBeanPropertyDefinition().getName());
    }

    @Test
    public void testConstructorWithValueDeserializer() {
        JsonDeserializer<String> valueDeserializer = new StdDeserializer<String>() {};
        NullValueProvider nva = new StandardNullValueProvider();
        FieldProperty fieldProperty = new FieldProperty(new FieldProperty(new Object()), valueDeserializer, nva);
        assertEquals(valueDeserializer, fieldProperty.getValueDeserializer());
        assertEquals(nva, fieldProperty.getNullProvider());
    }

    @Test
    public void testConstructorWithNullProvider() {
        NullValueProvider nva = new StandardNullValueProvider();
        FieldProperty fieldProperty = new FieldProperty(new FieldProperty(new Object()), null, nva);
        assertEquals(null, fieldProperty.getValueDeserializer());
        assertEquals(nva, fieldProperty.getNullProvider());
    }

    @Test
    public void testFixAccess() throws Exception {
        Field field = new Field(Integer.class, "field", Integer.TYPE);
        field.setAccessible(true);
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        DeserializationConfig config = mapper.getConfig();
        config.setEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS, false);
        FieldProperty fieldProperty = new FieldProperty(new BeanPropertyDefinition(new AnnotatedField(field), "_name", null, null, null, null, null, null, null), null, null, null, field);
        fieldProperty.fixAccess(config);
        assertTrue(field.isAccessible(true));
    }

    @Test
    public void testSetAndGet() throws IOException {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        Object instance = new Object() {};
        FieldProperty fieldProperty = new FieldProperty(new BeanPropertyDefinition(new AnnotatedField(new Field(Integer.class, "field", Integer.TYPE), null, null, null), "_name", null, null, null, null, null, null, null), mapper.getTypeFactory().constructType(Integer.class), null, null, new Field(Integer.class, "field", Integer.TYPE), null);
        JsonParser p = mapper.getFactory().createParser("123");
        p.nextToken();
        fieldProperty.deserializeAndSet(p, mapper.getDeserializationContext(), instance);
        assertEquals(123, fieldProperty.getField().get(instance));
    }

    @Test
    public void testSetAndReturn() throws IOException {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        Object instance = new Object() {};
        FieldProperty fieldProperty = new FieldProperty(new BeanPropertyDefinition(new AnnotatedField(new Field(Integer.class, "field", Integer.TYPE), null, null, null), "_name", null, null, null, null, null, null, null), mapper.getTypeFactory().constructType(Integer.class), null, null, new Field(Integer.class, "field", Integer.TYPE), null);
        JsonParser p = mapper.getFactory().createParser("123");
        p.nextToken();
        Object result = fieldProperty.deserializeSetAndReturn(p, mapper.getDeserializationContext(), instance);
        assertEquals(instance, result);
        assertEquals(123, fieldProperty.getField().get(instance));
    }

    @Test
    public void testWithName() {
        PropertyName newName = PropertyName.construct("newName");
        FieldProperty fieldProperty = new FieldProperty(new FieldProperty(new Object()), newName);
        assertEquals(newName, fieldProperty.getName());
        assertEquals(newName, fieldProperty.getBeanPropertyDefinition().getName());
    }

    /**
     * Test for issue #2023
     * @throws IOException
     */
    @Test
    public void testDeserializeAndSetWithTypeDeserializer() throws IOException {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        Object instance = new Object() {};
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        TypeDeserializer typeDeserializer = new StringDeserializer();
        FieldProperty fieldProperty = new FieldProperty(new BeanPropertyDefinition(new AnnotatedField(new Field(String.class, "field", String.class), null, null, null), "_name", null, null, null, null, null, null, null), javaType, typeDeserializer, null, new Field(String.class, "field", String.class), null);
        JsonParser p = mapper.getFactory().createParser("\"foo\"");
        p.nextToken();
        fieldProperty.deserializeAndSet(p, mapper.getDeserializationContext(), instance);
        assertEquals("foo", fieldProperty.getField().get(instance).toString());
    }

    @Test
    public void testDeserializeAndSetWithSkipNulls() throws IOException {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        Object instance = new Object() {};
        FieldProperty fieldProperty = new FieldProperty(new BeanPropertyDefinition(new AnnotatedField(new Field(Integer.class, "field", Integer.TYPE), null, null, null), "_name", null, null, null, null, null, null, null), mapper.getTypeFactory().constructType(Integer.class), null, null, new Field(Integer.class, "field", Integer.TYPE), null);
        JsonParser p = mapper.getFactory().createParser("null");
        p.nextToken();
        boolean skipNulls = true;
        fieldProperty.setSkipNulls(skipNulls);
        fieldProperty.deserializeAndSet(p, mapper.getDeserializationContext(), instance);
        assertNull(fieldProperty.getField().get(instance));
    }

    @Test
    public void testSet() throws IOException {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        Object instance = new Object() {};
        FieldProperty fieldProperty = new FieldProperty(new BeanPropertyDefinition(new AnnotatedField(new Field(Integer.class, "field", Integer.TYPE), null, null, null), "_name", null, null, null, null, null, null, null), mapper.getTypeFactory().constructType(Integer.class), null, null, new Field(Integer.class, "field", Integer.TYPE), null);
        fieldProperty.set(instance, 123);
        assertEquals(123, fieldProperty.getField().get(instance));
    }

    @Test
    public void testSetWithSkipNulls() throws IOException {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        Object instance = new Object() {};
        FieldProperty fieldProperty = new FieldProperty(new BeanPropertyDefinition(new AnnotatedField(new Field(Integer.class, "field", Integer.TYPE), null, null, null), "_name", null, null, null, null, null, null, null), mapper.getTypeFactory().constructType(Integer.class), null, null, new Field(Integer.class, "field", Integer.TYPE), null);
        fieldProperty.set(instance, null);
        assertNull(fieldProperty.getField().get(instance));
        boolean skipNulls = true;
        fieldProperty.setSkipNulls(skipNulls);
        fieldProperty.set(instance, null);
        assertNull(fieldProperty.getField().get(instance));
    }

    @Test
    public void testReadResolve() throws IOException {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        Object instance = new Object() {};
        FieldProperty fieldProperty = new FieldProperty(new BeanPropertyDefinition(new AnnotatedField(new Field(Integer.class, "field", Integer.TYPE), null, null, null), "_name", null, null, null, null, null, null, null), mapper.getTypeFactory().constructType(Integer.class), null, null, new Field(Integer.class, "field", Integer.TYPE), null);
        Object resolved = fieldProperty.readResolve();
        assertEquals(fieldProperty, resolved);
    }
}