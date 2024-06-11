package com.example.junit.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.Named;

public class SetterlessPropertyTest {

    private static Object testInstance;
    private static Object testPropertyValue;
    private static Object testPropertyValue2;
    private static Object testTypeDeserializer;
    private static Object testJsonParser;
    private static Object testDeserializationContext;
    private static SettableBeanProperty testSetterlessProperty;
    private static Method testGetter;
    private static NullValueProvider testNullValueProvider;
    private static Object testAnnotatedMethod;
    private static Object testContextAnnotations;
    private static AnnotatedMember testAnnotatedMember;
    private static TypeDeserializer testTypeDeserializerSpy;

    @org.junit.BeforeClass
    public static void setup() throws Exception {
        testInstance = mock(Object.class);
        testPropertyValue = new Object() {};
        testPropertyValue2 = new Object() {};
        testTypeDeserializer = mock(TypeDeserializer.class);
        testJsonParser = mock(JsonParser.class);
        testDeserializationContext = mock(DeserializationContext.class);
        testSetterlessProperty = new SetterlessProperty(
                new BeanPropertyDefinition() {},
                Object.class.getTypeName(),
                testTypeDeserializer,
                mock(Annotations.class),
                (AnnotatedMethod) testAnnotatedMethod
        );
        testGetter = mock(Method.class);
        testNullValueProvider = mock(NullValueProvider.class);
        testAnnotatedMember = mock(AnnotatedMember.class);
        testAnnotatedMethod = mock(AnnotatedMethod.class);
        testContextAnnotations = mock(Annotations.class);
        testTypeDeserializerSpy = mock(TypeDeserializer.class);
    }

    /*
     * Test constructor
     */
    @org.junit.Test
    public void testSetterlessPropertyConstructor() {
        SetterlessProperty setterlessProperty = new SetterlessProperty(
                new BeanPropertyDefinition() {},
                Object.class.getTypeName(),
                testTypeDeserializer,
                testContextAnnotations,
                testAnnotatedMethod
        );
        assertEquals(Object.class, setterlessProperty.getType());
        assertEquals(testTypeDeserializer, setterlessProperty.getTypeDeserializer());
        assertEquals(testContextAnnotations, setterlessProperty.getAnnotationMembers());
        assertEquals(testAnnotatedMethod, setterlessProperty.getAnnotatedMember());
    }

    /*
     * Test copy constructor
     */
    @org.junit.Test
    public void testSetterlessPropertyCopyConstructor() throws Exception {
        SetterlessProperty setterlessProperty = new SetterlessProperty(
                new BeanPropertyDefinition() {},
                Object.class.getTypeName(),
                testTypeDeserializer,
                testContextAnnotations,
                testAnnotatedMethod
        );
        SetterlessProperty copy = new SetterlessProperty(setterlessProperty, testTypeDeserializer, testNullValueProvider);
        assertEquals(setterlessProperty, copy);
    }

    /*
     * Test withName constructor
     */
    @org.junit.Test
    public void testSetterlessPropertyWithNameConstructor() throws Exception {
        PropertyName newName = mock(PropertyName.class);
        SetterlessProperty setterlessProperty = new SetterlessProperty(
                new SetterlessProperty(
                    new BeanPropertyDefinition() {},
                    Object.class.getTypeName(),
                    testTypeDeserializer,
                    testContextAnnotations,
                    testAnnotatedMethod
                ), newName);
        assertEquals(newName, setterlessProperty.getName());
        assertEquals(Object.class, setterlessProperty.getType());
        assertEquals(testTypeDeserializer, setterlessProperty.getTypeDeserializer());
        assertEquals(testContextAnnotations, setterlessProperty.getAnnotationMembers());
        assertEquals(testAnnotatedMethod, setterlessProperty.getAnnotatedMember());
    }

    /*
     * Test getter
     */
    @org.junit.Test
    public void testGetGetter() {
        assertEquals(testGetter, testSetterlessProperty.getGetter());
    }

    /*
     * Test getter_null
     */
    @org.junit.Test(expected = NullPointerException.class)
    public void testGetGetter_null() {
        testSetterlessProperty = new SetterlessProperty(
                new BeanPropertyDefinition() {},
                Object.class.getTypeName(),
                testTypeDeserializer,
                testContextAnnotations,
                null
        );
    }

    /*
     * Test deserializeAndSet
     */
    @org.junit.Test
    public void testDeserializeAndSet_value_is_not_null() throws Exception {
        when(testGetter.invoke(testInstance, (Object[]) null)).thenReturn(testPropertyValue);

        testSetterlessProperty.deserializeAndSet(testJsonParser, testDeserializationContext, testInstance);
        assertEquals(testPropertyValue2, testPropertyValue);
    }

    @org.junit.Test
    public void testDeserializeAndSet_value_is_null() throws Exception {
        when(testGetter.invoke(testInstance, (Object[]) null)).thenReturn(testPropertyValue);

        testSetterlessProperty.deserializeAndSet(testJsonParser, testDeserializationContext, testInstance);
        assertNull(testPropertyValue);
    }

    @org.junit.Test(expected = IOException.class)
    public void testDeserializeAndSet_IOException() throws Exception {
        when(testJsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);
        when(testGetter.invoke(testInstance, (Object[]) null)).thenThrow(new IOException());

        testSetterlessProperty.deserializeAndSet(testJsonParser, testDeserializationContext, testInstance);
    }

    /*
     * Test deserializeAndSet_typeDeserializer_not_null
     */
    @org.junit.Test
    public void testDeserializeAndSet_typeDeserializer_not_null() throws Exception {
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        SetterlessProperty setterlessProperty = new SetterlessProperty(
                new BeanPropertyDefinition() {},
                Object.class.getTypeName(),
                typeDeserializer,
                testContextAnnotations,
                testAnnotatedMethod
        );

        when(testJsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(testGetter.invoke(testInstance, (Object[]) null)).thenReturn(testPropertyValue);
        when(typeDeserializer.deserializeValue(testJsonParser, testDeserializationContext, String.class)).thenReturn("test");

        Object deserializedValue = setterlessProperty.deserializeAndSet(testJsonParser, testDeserializationContext, testInstance);
        assertEquals("test", deserializedValue);
    }

    @org.junit.Test
    public void testDeserializeAndSet_typeDeserializer_null() throws Exception {
        SetterlessProperty setterlessProperty = new SetterlessProperty(
                new BeanPropertyDefinition() {},
                Object.class.getTypeName(),
                null,
                testContextAnnotations,
                testAnnotatedMethod
        );

        when(testJsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(testGetter.invoke(testInstance, (Object[]) null)).thenReturn(testPropertyValue);

        Object deserializedValue = setterlessProperty.deserializeAndSet(testJsonParser, testDeserializationContext, testInstance);
        assertNull(deserializedValue);
    }

    /*
     * Test deserializeAndSet_getValueDeserializer_not_null
     */
    @org.junit.Test
    public void testDeserializeAndSet_getValueDeserializer_not_null() throws Exception {
        JsonDeserializer<?> valueDeserializer = mock(JsonDeserializer.class);
        SetterlessProperty setterlessProperty = new SetterlessProperty(
                new SetterlessProperty(
                    new BeanPropertyDefinition() {},
                    Object.class.getTypeName(),
                    testTypeDeserializer,
                    testContextAnnotations,
                    testAnnotatedMethod
                ), valueDeserializer, testNullValueProvider
        );

        when(testJsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(testGetter.invoke(testInstance, (Object[]) null)).thenReturn(testPropertyValue);
        when(valueDeserializer.deserialize(testJsonParser, testDeserializationContext, testPropertyValue.getClass())).thenReturn(testPropertyValue2);

        Object deserializedValue = setterlessProperty.deserializeAndSet(testJsonParser, testDeserializationContext, testInstance);
        assertEquals(testPropertyValue2, deserializedValue);
    }

    /*
     * Test deserializeAndSet_getValueDeserializer_null
     */
    @org.junit.Test
    public void testDeserializeAndSet_getValueDeserializer_null() throws Exception {
        SetterlessProperty setterlessProperty = new SetterlessProperty(
                new SetterlessProperty(
                    new BeanPropertyDefinition() {},
                    Object.class.getTypeName(),
                    testTypeDeserializer,
                    testContextAnnotations,
                    testAnnotatedMethod
                ), null, testNullValueProvider
        );

        when(testJsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(testGetter.invoke(testInstance, (Object[]) null)).thenReturn(testPropertyValue);

        Object deserializedValue = setterlessProperty.deserializeAndSet(testJsonParser, testDeserializationContext, testInstance);
        assertNull(deserializedValue);
    }

    /*
     * Test fixAccess
     */
    @org.junit.Test
    public void testFixAccess_true() throws Exception {
        testSetterlessProperty.fixAccess(testDeserializationContext);
        verify(testAnnotated).fixAccess(true);
    }

    @org.junit.Test
    public void testFixAccess_false() throws Exception {
        testSetterlessProperty.fixAccess(testDeserializationContext);
        verify(testAnnotated, never()).fixAccess(anyBoolean());
    }

    /*
     * Test getType
     */
    @org.junit.Test
    public void testGetType() {
        assertEquals(Object.class, testSetterlessProperty.getType());
    }

    /*
     * Test getName
     */
    @org.junit.Test
    public void testGetName() {
        assertEquals("test", testSetterlessProperty.getName().asString());
    }

    /*
     * Test getAnnotated
     */
    @org.junit.Test
    public void testGetAnnotated() {
        assertEquals(testAnnotatedMethod, testSetterlessProperty.getAnnotated());
    }

    /*
     * Test getValueDeserializer
     */
    @org.junit.Test
    public void testGetValueDeserializer() {
        assertEquals(testValueDeserializer, testSetterlessProperty.getValueDeserializer());
    }

    /*
     * Test getNullProvider
     */
    @org.junit.Test
    public void testGetNullProvider() {
        assertEquals(testNullValueProvider, testSetterlessProperty.getNullProvider());
    }

    /*
     * Test withValueDeserializer
     */
    @org.junit.Test
    public void testWithValueDeserializer() throws Exception {
        SetterlessProperty setterlessProperty = new SetterlessProperty(
                new BeanPropertyDefinition() {},
                Object.class.getTypeName(),
                testTypeDeserializer,
                testContextAnnotations,
                testAnnotatedMethod
        );

        JsonDeserializer<?> valueDeserializer = mock(JsonDeserializer.class);
        SetterlessProperty setterlessPropertyWithValueDeserializer = setterlessProperty.withValueDeserializer(valueDeserializer);

        assertEquals(valueDeserializer, setterlessPropertyWithValueDeserializer.getValueDeserializer());
    }

    /*
     * Test withNullProvider
     */
    @org.junit.Test
    public void testWithNullProvider() throws Exception {
        SetterlessProperty setterlessProperty = new SetterlessProperty(
                new BeanPropertyDefinition() {},
                Object.class.getTypeName(),
                testTypeDeserializer,
                testContextAnnotations,
                testAnnotatedMethod
        );

        NullValueProvider nullValueProvider = mock(NullValueProvider.class);
        SetterlessProperty setterlessPropertyWithNullProvider = setterlessProperty.withNullProvider(nullValueProvider);

        assertEquals(nullValueProvider, setterlessPropertyWithNullProvider.getNullProvider());
    }

    /*
     * Test toString
     */
    @org.junit.Test
    public void testToString() {
        String toString = testSetterlessProperty.toString();
        assertFalse(toString.isEmpty());
    }
}