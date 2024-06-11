package com.fasterxml.jackson.databind.deser.impl;

import static com.fasterxml.jackson.core.JsonToken.*;
import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.NullValueProvider;

import java.io.IOException;
import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;

public class FieldPropertyTest {
    private static class TestBean {
        @JsonProperty
        private int value;
    }

    private AnnotatedField _annotated;
    private Field _field;
    private Object _instance;
    private JsonParser _parser;
    private DeserializationContext _ctxt;
    private FieldProperty _property;

    @Before
    public void setUp() throws Exception {
        _annotated = new AnnotatedField(TestBean.class, "value", TestBean.class.getDeclaredField("value"));
        _field = _annotated.getAnnotated();
        _instance = new TestBean();
        ObjectMapper mapper = new ObjectMapper();
        _parser = mapper.getFactory().createParser("{}");
        _ctxt = mapper.getDeserializationContext();
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        _property = new FieldProperty(
                new BeanPropertyDefinition(new AnnotatedMember(_annotated), null, null),
                null, null, Annotations.empty(), _annotated);
        _property.deserializeAndSet(_parser, _ctxt, _instance);
        assertEquals(0, _instance.getClass().getDeclaredField("value").get( _instance ));
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        _property = new FieldProperty(
                new BeanPropertyDefinition(new AnnotatedMember(_annotated), null, null),
                null, null, Annotations.empty(), _annotated);
        Object result = _property.deserializeSetAndReturn(_parser, _ctxt, _instance);
        assertEquals(_instance, result);
        assertEquals(0, _instance.getClass().getDeclaredField("value").get( _instance ));
    }

    @Test
    public void testSet() throws Exception {
        _property = new FieldProperty(
                new BeanPropertyDefinition(new AnnotatedMember(_annotated), null, null),
                null, null, Annotations.empty(), _annotated);
        _property.set(_instance, Integer.valueOf(1));
        assertEquals(1, _instance.getClass().getDeclaredField("value").get( _instance ));
    }

    @Test
    public void testSetAndReturn() throws Exception {
        _property = new FieldProperty(
                new BeanPropertyDefinition(new AnnotatedMember(_annotated), null, null),
                null, null, Annotations.empty(), _annotated);
        Object result = _property.setAndReturn(_instance, Integer.valueOf(1));
        assertEquals(_instance, result);
        assertEquals(1, _instance.getClass().getDeclaredField("value").get( _instance ));
    }

    @Test
    public void testWithName() throws Exception {
        PropertyName newName = PropertyName.construct("newName");
        _property = new FieldProperty(_property, newName);
        assertEquals("newName", _property.getName());
    }

    @Test
    public void testWithValueDeserializer() throws Exception {
        JsonDeserializer<Integer> deserializer = new StandardDeserializer<Integer>(Integer.class) {};
        _property = new FieldProperty(_property, deserializer, NullValueProvider.noOpValue);
        assertEquals(deserializer, _property.getValueDeserializer());
    }

    @Test
    public void testWithNullProvider() throws Exception {
        NullValueProvider nva = new StandardNullValueProvider();
        _property = new FieldProperty(_property, null, nva);
        assertEquals(nva, _property.getNullValueProvider());
    }

    @Test
    public void testFixAccess() throws Exception {
        DeserializationConfig config = new DeserializationConfig();
        _property = new FieldProperty(_property);
        _property.fixAccess(config);
        assertTrue(config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
    }

    @Test
    public void testGetAnnotation() throws Exception {
        assertEquals(JsonProperty.class, _property.getAnnotation(JsonProperty.class));
    }

    @Test
    public void testGetMember() throws Exception {
        assertEquals(_annotated, _property.getMember());
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        TypeDeserializer typeDeserializer = new StdTypeDeserializer();
        _property = new FieldProperty(
                new BeanPropertyDefinition(new AnnotatedMember(_annotated), null, null),
                null, null, Annotations.empty(), _annotated, null, typeDeserializer);
        Object result = _property.deserializeAndSet(_parser, _ctxt, _instance);
        assertEquals(1, result.getClass().getTypeName());
        assertEquals(1, _instance.getClass().getDeclaredField("value").get( _instance ));
    }
}