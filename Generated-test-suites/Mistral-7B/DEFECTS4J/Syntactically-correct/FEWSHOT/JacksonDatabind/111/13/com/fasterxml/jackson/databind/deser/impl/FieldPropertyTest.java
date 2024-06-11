package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Field;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.TestBase;

public class FieldPropertyTest extends TestBase {

    private static final Object TEST_OBJECT = new Object();
    private static final Class<?> TEST_CLASS = TEST_OBJECT.getClass();
    private static final Field TEST_FIELD = TestBase.getField(TEST_CLASS, "testField");
    private static final JsonProperty TEST_ANNOTATION = new JsonProperty("testProperty");

    @SuppressWarnings("unchecked")
    private static final NullValueProvider TEST_NUL_PROVIDER = mock(NullValueProvider.class);
    private static final TypeDeserializer TEST_TYPE_DESERIALIZER = mock(TypeDeserializer.class);
    private static final BeanPropertyDefinition TEST_PROPERTY_DEF = mock(BeanPropertyDefinition.class);
    private static final Annotations TEST_ANNOTATIONS = mock(Annotations.class);
    private static final AnnotatedField TEST_ANNOTATED_FIELD = mock(AnnotatedField.class);
    private static final AnnotatedMember TEST_ANNOTATED_MEMBER = mock(AnnotatedMember.class);

    @Before
    public void setUp() throws Exception {
        when(TEST_ANNOTATED_FIELD.getAnnotated()).thenReturn(TEST_ANNOTATED_MEMBER);
        when(TEST_ANNOTATED_FIELD.getName()).thenReturn("testProperty");
        when(TEST_ANNOTATED_FIELD.getMember()).thenReturn(TEST_FIELD);
        when(TEST_ANNOTATED_FIELD.getVisibility()).thenReturn(Modifier.PUBLIC);
        when(TEST_ANNOTATED_MEMBER.getMember()).thenReturn(TEST_FIELD);
    }

    @Test
    public void testInitialization() {
        FieldProperty fieldProperty = new FieldProperty(TEST_PROPERTY_DEF, ObjectType.INSTANCETYPE, TEST_TYPE_DESERIALIZER, TEST_ANNOTATIONS, TEST_ANNOTATED_FIELD);
        assertEquals(TEST_PROPERTY_DEF, fieldProperty.getPropertyDefinition());
        assertEquals(ObjectType.INSTANCETYPE, fieldProperty.getType());
        assertEquals(TEST_TYPE_DESERIALIZER, fieldProperty.getTypeDeserializer());
        assertEquals(TEST_ANNOTATIONS, fieldProperty.getContextAnnotations());
        assertEquals(TEST_ANNOTATED_FIELD, fieldProperty.getAnnotatedField());
        assertEquals(TEST_FIELD, fieldProperty.getField());
        assertEquals(false, fieldProperty.isSkipNulls());
    }

    @Test
    public void testConstructorWithNullValueProvider() {
        FieldProperty fieldProperty = new FieldProperty(TEST_PROPERTY_DEF, ObjectType.INSTANCETYPE, TEST_TYPE_DESERIALIZER, null, TEST_ANNOTATED_FIELD);
        assertEquals(TEST_PROPERTY_DEF, fieldProperty.getPropertyDefinition());
        assertEquals(ObjectType.INSTANCETYPE, fieldProperty.getType());
        assertEquals(TEST_TYPE_DESERIALIZER, fieldProperty.getTypeDeserializer());
        assertEquals(TEST_ANNOTATIONS, fieldProperty.getContextAnnotations());
        assertEquals(TEST_ANNOTATED_FIELD, fieldProperty.getAnnotatedField());
        assertEquals(TEST_FIELD, fieldProperty.getField());
        assertEquals(false, fieldProperty.isSkipNulls());
    }

    @Test
    public void testConstructorWithValueDeserializer() {
        JsonDeserializer<String> stringDeserializer = mock(JsonDeserializer.class);
        FieldProperty fieldProperty = new FieldProperty(TEST_PROPERTY_DEF, ObjectType.INSTANCETYPE, null, TEST_ANNOTATIONS, TEST_ANNOTATED_FIELD, stringDeserializer);
        assertEquals(TEST_PROPERTY_DEF, fieldProperty.getPropertyDefinition());
        assertEquals(ObjectType.INSTANCETYPE, fieldProperty.getType());
        assertNull(fieldProperty.getTypeDeserializer());
        assertEquals(TEST_ANNOTATIONS, fieldProperty.getContextAnnotations());
        assertEquals(TEST_ANNOTATED_FIELD, fieldProperty.getAnnotatedField());
        assertEquals(TEST_FIELD, fieldProperty.getField());
        assertEquals(false, fieldProperty.isSkipNulls());
        assertEquals(stringDeserializer, fieldProperty.getValueDeserializer());
    }

    @Test
    public void testConstructorWithNullProvider() {
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);
        FieldProperty fieldProperty = new FieldProperty(TEST_PROPERTY_DEF, ObjectType.INSTANCETYPE, TEST_TYPE_DESERIALIZER, nullValueProvider, TEST_ANNOTATED_FIELD);
        assertEquals(TEST_PROPERTY_DEF, fieldProperty.getPropertyDefinition());
        assertEquals(ObjectType.INSTANCETYPE, fieldProperty.getType());
        assertEquals(TEST_TYPE_DESERIALIZER, fieldProperty.getTypeDeserializer());
        assertEquals(TEST_ANNOTATIONS, fieldProperty.getContextAnnotations());
        assertEquals(TEST_ANNOTATED_FIELD, fieldProperty.getAnnotatedField());
        assertEquals(TEST_FIELD, fieldProperty.getField());
        assertEquals(false, fieldProperty.isSkipNulls());
        assertEquals(nullValueProvider, fieldProperty.getNullProvider());
    }

    @Test
    public void testConstructorWithName() {
        PropertyName newName = mock(PropertyName.class);
        FieldProperty fieldProperty = new FieldProperty(TEST_PROPERTY_DEF, ObjectType.INSTANCETYPE, TEST_TYPE_DESERIALIZER, null, TEST_ANNOTATED_FIELD, newName);
        assertEquals(TEST_PROPERTY_DEF, fieldProperty.getPropertyDefinition());
        assertEquals(ObjectType.INSTANCETYPE, fieldProperty.getType());
        assertNull(fieldProperty.getTypeDeserializer());
        assertEquals(TEST_ANNOTATIONS, fieldProperty.getContextAnnotations());
        assertEquals(TEST_ANNOTATED_FIELD, fieldProperty.getAnnotatedField());
        assertEquals(TEST_FIELD, fieldProperty.getField());
        assertEquals(false, fieldProperty.isSkipNulls());
        assertEquals(newName, fieldProperty.getName());
    }

    @Test
    public void testConstructorWithJsonDeserializer() {
        JsonDeserializer<String> stringDeserializer = mock(JsonDeserializer.class);
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);
        FieldProperty fieldProperty = new FieldProperty(TEST_PROPERTY_DEF, ObjectType.INSTANCETYPE, null, TEST_ANNOTATIONS, TEST_ANNOTATED_FIELD, stringDeserializer, nullValueProvider);
        assertEquals(TEST_PROPERTY_DEF, fieldProperty.getPropertyDefinition());
        assertEquals(ObjectType.INSTANCETYPE, fieldProperty.getType());
        assertNull(fieldProperty.getTypeDeserializer());
        assertEquals(TEST_ANNOTATIONS, fieldProperty.getContextAnnotations());
        assertEquals(TEST_ANNOTATED_FIELD, fieldProperty.getAnnotatedField());
        assertEquals(TEST_FIELD, fieldProperty.getField());
        assertEquals(false, fieldProperty.isSkipNulls());
        assertEquals(stringDeserializer, fieldProperty.getValueDeserializer());
        assertEquals(nullValueProvider, fieldProperty.getNullProvider());
    }

    @Test
    public void testToString() {
        FieldProperty fieldProperty = new FieldProperty(TEST_PROPERTY_DEF, ObjectType.INSTANCETYPE, TEST_TYPE_DESERIALIZER, TEST_ANNOTATIONS, TEST_ANNOTATED_FIELD);
        String expected = "FieldProperty(name=testProperty, propertyDefinition=BeanPropertyDefinition(name=testProperty, getter=null, setter=null, type=class java.lang.Object, modifiers=0, annotations=[], VisibleAnnotation(visibility=PUBLIC, name=JsonProperty, value=testProperty), BeanPropertyDefinition(name=testProperty, getter=null, setter=sun.reflect.UnsafeFieldAccessorImpl@54a13b, type=class java.lang.Object, modifiers=1, annotations=[], VisibleAnnotation(visibility=PUBLIC, name=JsonProperty, value=testProperty)])";
        assertEquals(expected, fieldProperty.toString());
    }

    @Test
    public void testGetAnnotation() {
        when(TEST_ANNOTATED_FIELD.getAnnotation(JsonProperty.class)).thenReturn(TEST_ANNOTATION);
        assertEquals(TEST_ANNOTATION, fieldProperty.getAnnotation(JsonProperty.class));
    }

    @Test
    public void testGetMember() {
        assertEquals(TEST_ANNOTATED_MEMBER, fieldProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        when(TEST_ANNOTATION.value()).thenReturn("testValue");
        String testValue = "testValue";
        when(TEST_FIELD.getType()).thenReturn(String.class);
        JsonParser mockParser = mock(JsonParser.class);
        when(mockParser.getValueAsString()).thenReturn(testValue);
        DeserializationContext mockContext = mock(DeserializationContext.class);
        Object instance = TEST_OBJECT;

        fieldProperty.deserializeAndSet(mockParser, mockContext, instance);

        assertEquals(testValue, TEST_FIELD.get(TEST_OBJECT));
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        when(TEST_ANNOTATION.value()).thenReturn("testValue");
        String testValue = "testValue";
        when(TEST_FIELD.getType()).thenReturn(String.class);
        JsonParser mockParser = mock(JsonParser.class);
        when(mockParser.getValueAsString()).thenReturn(testValue);
        DeserializationContext mockContext = mock(DeserializationContext.class);
        Object instance = TEST_OBJECT;

        Object result = fieldProperty.deserializeSetAndReturn(mockParser, mockContext, instance);

        assertEquals(testValue, TEST_FIELD.get(TEST_OBJECT));
        assertEquals(TEST_OBJECT, result);
    }

    @Test
    public void testSet() throws IOException {
        when(TEST_ANNOTATION.value()).thenReturn("testValue");
        String testValue = "testValue";

        fieldProperty.set(TEST_OBJECT, testValue);

        assertEquals(testValue, TEST_FIELD.get(TEST_OBJECT));
    }

    @Test
    public void testSetAndReturn() throws IOException {
        when(TEST_ANNOTATION.value()).thenReturn("testValue");
        String testValue = "testValue";

        Object result = fieldProperty.setAndReturn(TEST_OBJECT, testValue);

        assertEquals(testValue, TEST_FIELD.get(TEST_OBJECT));
        assertEquals(TEST_OBJECT, result);
    }

    @Test
    public void testReadResolve() throws Exception {
        FieldProperty fieldProperty = new FieldProperty(TEST_PROPERTY_DEF, ObjectType.INSTANCETYPE, TEST_TYPE_DESERIALIZER, TEST_ANNOTATIONS, TEST_ANNOTATED_FIELD);
        FieldProperty resolvedFieldProperty = fieldProperty.readResolve();
        assertEquals(fieldProperty.getClass(), resolvedFieldProperty.getClass());
        assertEquals(TEST_PROPERTY_DEF, resolvedFieldProperty.getPropertyDefinition());
        assertEquals(ObjectType.INSTANCETYPE, resolvedFieldProperty.getType());
        assertEquals(TEST_TYPE_DESERIALIZER, resolvedFieldProperty.getTypeDeserializer());
        assertEquals(TEST_ANNOTATIONS, resolvedFieldProperty.getContextAnnotations());
        assertEquals(TEST_ANNOTATED_FIELD, resolvedFieldProperty.getAnnotatedField());
        assertEquals(TEST_FIELD, resolvedFieldProperty.getField());
        assertEquals(false, resolvedFieldProperty.isSkipNulls());
    }
}