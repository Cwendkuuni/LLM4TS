package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.NullsConstantProvider;

import org.junit.Before;
import org.junit.Test;

public class FieldPropertyTest {

    private static final Class<Person> PERSON_CLASS = Person.class;
    private static final JsonDeserializer<String> STRING_DESERIALIZER = mock(JsonDeserializer.class);
    private static final TypeDeserializer STRING_TYPE_DESERIALIZER = mock(TypeDeserializer.class);

    private Object instance;
    private Field field;
    private AnnotatedField annotatedField;
    private BeanPropertyDefinition propertyDefinition;
    private JsonDeserializer<Person> personDeserializer;
    private NullValueProvider nullValueProvider;
    private FieldProperty fieldProperty;

    @Before
    public void setUp() throws Exception {
        propertyDefinition = new BeanPropertyDefinition(new AnnotatedMember(new JsonProperty(), mock(Field.class)), PERSON_CLASS, "name");
        field = mock(Field.class);
        annotatedField = mock(AnnotatedField.class);
        when(annotatedField.getAnnotated()).thenReturn(field);
        when(field.getName()).thenReturn("name");
        when(field.getType()).thenReturn(String.class);
        nullValueProvider = mock(NullValueProvider.class);
        when(nullValueProvider.getNullValue(mock(DeserializationContext.class))).thenReturn(null);

        personDeserializer = mock(JsonDeserializer.class);
        when(STRING_DESERIALIZER.getType()).thenReturn(String.class);
        when(STRING_DESERIALIZER.getNullValue()).thenReturn(null);
        when(STRING_TYPE_DESERIALIZER.findValueDeserializer(String.class)).thenReturn(STRING_DESERIALIZER);
        when(STRING_TYPE_DESERIALIZER.findContentConverter(null)).thenReturn(mock(Converter.Std.class));

        instance = mock(Object.class);
        when(field.get(instance)).thenReturn("foo");

        instanceClass(PERSON_CLASS);
        fieldProperty = new FieldProperty(propertyDefinition, ObjectTypeFactory.instance.constructType(PERSON_CLASS), null, Annotations.empty(), annotatedField);
    }

    @Test
    public void testConstructor() {
        assertSame(propertyDefinition, fieldProperty.getMetadata());
        assertSame(ObjectTypeFactory.instance.constructType(PERSON_CLASS), fieldProperty.getType());
        assertNull(fieldProperty.getValueDeserializer());
        assertFalse(fieldProperty._skipNulls);
        assertSame(annotatedField, fieldProperty._annotated);
        assertSame(field, fieldProperty._field);
    }

    @Test
    public void testConstructorWithDeserializer() {
        FieldProperty fieldPropertyWithDeserializer = new FieldProperty(propertyDefinition, ObjectTypeFactory.instance.constructType(PERSON_CLASS), personDeserializer, nullValueProvider, annotatedField);
        assertSame(propertyDefinition, fieldPropertyWithDeserializer.getMetadata());
        assertSame(ObjectTypeFactory.instance.constructType(PERSON_CLASS), fieldPropertyWithDeserializer.getType());
        assertSame(personDeserializer, fieldPropertyWithDeserializer.getValueDeserializer());
        assertFalse(fieldPropertyWithDeserializer._skipNulls);
        assertSame(annotatedField, fieldPropertyWithDeserializer._annotated);
        assertSame(field, fieldPropertyWithDeserializer._field);
    }

    @Test
    public void testConstructorWithNullProvider() {
        FieldProperty fieldPropertyWithNullProvider = new FieldProperty(propertyDefinition, ObjectTypeFactory.instance.constructType(PERSON_CLASS), null, nullValueProvider, annotatedField);
        assertSame(propertyDefinition, fieldPropertyWithNullProvider.getMetadata());
        assertSame(ObjectTypeFactory.instance.constructType(PERSON_CLASS), fieldPropertyWithNullProvider.getType());
        assertNull(fieldPropertyWithNullProvider.getValueDeserializer());
        assertTrue(fieldPropertyWithNullProvider._skipNulls);
        assertSame(annotatedField, fieldPropertyWithNullProvider._annotated);
        assertSame(field, fieldPropertyWithNullProvider._field);
    }

    @Test
    public void testConstructorWithName() {
        String newName = "newName";
        FieldProperty fieldPropertyWithName = new FieldProperty(fieldProperty, new PropertyName(newName));
        assertSame(propertyDefinition, fieldPropertyWithName.getMetadata());
        assertSame(ObjectTypeFactory.instance.constructType(PERSON_CLASS), fieldPropertyWithName.getType());
        assertNull(fieldPropertyWithName.getValueDeserializer());
        assertFalse(fieldPropertyWithName._skipNulls);
        assertSame(annotatedField, fieldPropertyWithName._annotated);
        assertSame(field, fieldPropertyWithName._field);
        assertSame(newName, fieldPropertyWithName.getName());
    }

    @Test
    public void testGetName() {
        assertSame("name", fieldProperty.getName());
    }

    @Test
    public void testGetType() {
        assertSame(String.class, fieldProperty.getType());
    }

    @Test
    public void testGetValueDeserializer() {
        assertNull(fieldProperty.getValueDeserializer());
    }

    @Test
    public void testSetValueDeserializer() {
        JsonDeserializer<Integer> integerDeserializer = mock(JsonDeserializer.class);
        fieldProperty.setValueDeserializer(integerDeserializer);
        assertSame(integerDeserializer, fieldProperty.getValueDeserializer());
    }

    @Test
    public void testGetNullProvider() {
        assertNull(fieldProperty.getNullProvider());
    }

    @Test
    public void testSetNullProvider() {
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);
        fieldProperty.setNullProvider(nullValueProvider);
        assertSame(nullValueProvider, fieldProperty.getNullProvider());
    }

    @Test
    public void testFixAccess() throws IOException {
        ClassUtil.checkAndFixAccess(field, true);
        assertTrue(Modifier.isAccessible(field));
    }

    @Test
    public void testWithName() {
        PropertyName newName = new PropertyName("newName");
        FieldProperty fieldPropertyWithName = fieldProperty.withName(newName);
        assertSame(propertyDefinition, fieldPropertyWithName.getMetadata());
        assertSame(ObjectTypeFactory.instance.constructType(PERSON_CLASS), fieldPropertyWithName.getType());
        assertNull(fieldPropertyWithName.getValueDeserializer());
        assertFalse(fieldPropertyWithName._skipNulls);
        assertSame(annotatedField, fieldPropertyWithName._annotated);
        assertSame(field, fieldPropertyWithName._field);
        assertSame(newName, fieldPropertyWithName.getName());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        when(parser.getCurrentName()).thenReturn("name");
        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(parser.getValueAsString()).thenReturn("foo");

        DeserializationContext ctxt = mock(DeserializationContext.class);
        when(ctxt.getDefaultValueFactory()).thenCallRealMethod();

        fieldProperty.deserializeAndSet(parser, ctxt, instance);

        assertEquals("foo", field.get(instance));
    }

    @Test
    public void testDeserializeAndSetWithNullValue() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        when(parser.getCurrentName()).thenReturn("name");
        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);

        DeserializationContext ctxt = mock(DeserializationContext.class);
        when(ctxt.getDefaultValueFactory()).thenCallRealMethod();

        fieldProperty.deserializeAndSet(parser, ctxt, instance);

        assertNull(field.get(instance));
    }

    @Test
    public void testDeserializeAndSetWithTypeDeserializer() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        when(parser.getCurrentName()).thenReturn("name");
        when(parser.getValueAsString()).thenReturn("{\"name\":\"foo\",\"age\":1}");

        DeserializationContext ctxt = mock(DeserializationContext.class);
        when(ctxt.getDefaultValueFactory()).thenCallRealMethod();
        when(ctxt.getConfig()).thenReturn(mock(ObjectMapper.class));

        when(STRING_TYPE_DESERIALIZER.findValueDeserializer(String.class)).thenReturn(STRING_DESERIALIZER);

        Person expectedValue = new Person();
        expectedValue.setName("foo");

        fieldProperty.deserializeAndSet(parser, ctxt, instance);

        assertEquals(expectedValue, field.get(instance));
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        when(parser.getCurrentName()).thenReturn("name");
        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(parser.getValueAsString()).thenReturn("foo");

        DeserializationContext ctxt = mock(DeserializationContext.class);
        when(ctxt.getDefaultValueFactory()).thenCallRealMethod();

        Object returnValue = fieldProperty.deserializeSetAndReturn(parser, ctxt, instance);

        assertEquals(instance, returnValue);
        assertEquals("foo", field.get(instance));
    }

    @Test
    public void testDeserializeSetAndReturnWithNullValue() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        when(parser.getCurrentName()).thenReturn("name");
        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);

        DeserializationContext ctxt = mock(DeserializationContext.class);
        when(ctxt.getDefaultValueFactory()).thenCallRealMethod();

        Object returnValue = fieldProperty.deserializeSetAndReturn(parser, ctxt, instance);

        assertEquals(instance, returnValue);
        assertNull(field.get(instance));
    }

    @Test
    public void testDeserializeSetAndReturnWithTypeDeserializer() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        when(parser.getCurrentName()).thenReturn("name");
        when(parser.getValueAsString()).thenReturn("{\"name\":\"foo\",\"age\":1}");

        DeserializationContext ctxt = mock(DeserializationContext.class);
        when(ctxt.getDefaultValueFactory()).thenCallRealMethod();
        when(ctxt.getConfig()).thenReturn(mock(ObjectMapper.class));

        Person expectedValue = new Person();
        expectedValue.setName("foo");

        when(STRING_TYPE_DESERIALIZER.findValueDeserializer(String.class)).thenReturn(STRING_DESERIALIZER);

        Object returnValue = fieldProperty.deserializeSetAndReturn(parser, ctxt, instance);

        assertEquals(instance, returnValue);
        assertEquals(expectedValue, field.get(instance));
    }

    @Test
    public void testSet() throws IOException {
        Object value = "bar";
        fieldProperty.set(instance, value);
        assertEquals(value, field.get(instance));
    }

    @Test
    public void testSetWithNullValue() throws IOException {
        fieldProperty.set(instance, null);
        assertNull(field.get(instance));
    }

    @Test
    public void testSetWithTypeDeserializer() throws IOException {
        Object value = new Person();
        fieldProperty.set(instance, value);
        assertEquals(value, field.get(instance));
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object value = "bar";
        Object returnValue = fieldProperty.setAndReturn(instance, value);
        assertEquals(instance, returnValue);
        assertEquals(value, field.get(instance));
    }

    @Test
    public void testSetAndReturnWithNullValue() throws IOException {
        Object returnValue = fieldProperty.setAndReturn(instance, null);
        assertEquals(instance, returnValue);
        assertNull(field.get(instance));
    }

    @Test
    public void testSetAndReturnWithTypeDeserializer() throws IOException {
        Object value = new Person();
        Object returnValue = fieldProperty.setAndReturn(instance, value);
        assertEquals(instance, returnValue);
        assertEquals(value, field.get(instance));
    }

    @Test
    public void testReadResolve() throws IOException {
        FieldProperty readResolveFieldProperty = fieldProperty.readResolve();
        assertSame(propertyDefinition, readResolveFieldProperty.getMetadata());
        assertSame(ObjectTypeFactory.instance.constructType(PERSON_CLASS), readResolveFieldProperty.getType());
        assertNull(readResolveFieldProperty.getValueDeserializer());
        assertFalse(readResolveFieldProperty._skipNulls);
        assertSame(annotatedField, readResolveFieldProperty._annotated);
        assertSame(field, readResolveFieldProperty._field);
    }

    private void instanceClass(Class<?> cls) {
        try {
            ClassUtil.makeAccessible(cls.getDeclaredField("serialVersionUID"));
        } catch (NoSuchFieldException e) {
            // Ignore: no serialVersionUID field present
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}

class Person {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}