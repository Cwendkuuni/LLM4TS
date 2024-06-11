package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.StdConverter;

/**
 * This is a test class for the MethodProperty class.
 */
public class MethodPropertyTest {

    private static final Converter<String, Integer> STRING_TO_INTEGER = new StdConverter.StdConverter<String, Integer>() {
        @Override
        public Integer convert(String text) {
            return Integer.parseInt(text);
        }
    };

    @Test
    public void testMethodPropertyWithSimpleSetter() throws Exception {
        Class<?> targetClass = ClassUtil.newSimpleImmutableClass("TestClass", new String[]{"intField"});
        Method setterMethod = targetClass.getMethod("setIntField", int.class);

        AnnotatedMethod annotatedMethod = mock(AnnotatedMethod.class);
        when(annotatedMethod.getName()).thenReturn("setIntField");
        when(annotatedMethod.getMember()).thenReturn(annotatedMethod);
        when(annotatedMethod.getDeclaringClass()).thenReturn(targetClass);
        when(annotatedMethod.getParameterTypes()).thenReturn(new Class[]{int.class});

        Annotation annotation = mock(JsonSetter.class);
        when(annotation.value()).thenReturn("intField");

        MethodProperty methodProperty = new MethodProperty(null,
                new BeanPropertyDefinition(targetClass.getDeclaredField("intField"), null),
                null,
                Annotations.empty(),
                annotatedMethod);

        assertEquals("setIntField", methodProperty.getName());
        assertSame(setterMethod, methodProperty.getSetter());
        assertEquals("intField", methodProperty.getAnnotationValues().get(JsonSetter.class).value());
    }

    @Test
    public void testMethodPropertyWithValueDeserializer() throws Exception {
        Class<?> targetClass = ClassUtil.newSimpleImmutableClass("TestClass", new String[]{"StringField"});
        Method setterMethod = targetClass.getMethod("setStringField", String.class);
        MethodProperty methodProperty = new MethodProperty(null,
                new BeanPropertyDefinition(targetClass.getDeclaredField("StringField"), String.class),
                null,
                Annotations.empty(),
                setterMethod);

        JsonDeserializer<String> stringDeserializer = mock(JsonDeserializer.class);
        methodProperty = new MethodProperty(methodProperty, stringDeserializer, null);

        JsonParser jsonParser = mock(JsonParser.class);
        when(jsonParser.getValueAsString()).thenReturn("testString");

        Object instance = new Object();
        Object result = methodProperty.deserializeAndSet(jsonParser, null, instance);

        assertEquals("testString", instance.getClass().getDeclaredField("StringField").get(instance));
        assertEquals("testString", result);
    }

    @Test
    public void testMethodPropertyWithValueDeserializerAndTypeDeserializer() throws Exception {
        Class<?> targetClass = ClassUtil.newSimpleImmutableClass("TestClass", new String[]{"IntegerField"});
        Method setterMethod = targetClass.getMethod("setIntegerField", int.class);

        AnnotatedMethod annotatedMethod = mock(AnnotatedMethod.class);
        when(annotatedMethod.getName()).thenReturn("setIntegerField");
        when(annotatedMethod.getMember()).thenReturn(annotatedMethod);
        when(annotatedMethod.getDeclaringClass()).thenReturn(targetClass);
        when(annotatedMethod.getParameterTypes()).thenReturn(new Class[]{int.class});

        Annotation annotation = mock(JsonSetter.class);
        when(annotation.value()).thenReturn("IntegerField");

        MethodProperty methodProperty = new MethodProperty(null,
                new BeanPropertyDefinition(targetClass.getDeclaredField("IntegerField"), int.class),
                null,
                Annotations.empty(),
                annotatedMethod);

        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        methodProperty = new MethodProperty(methodProperty, null, typeDeserializer);

        JsonParser jsonParser = mock(JsonParser.class);
        when(jsonParser.getValueAsString()).thenReturn("42");

        Object instance = new Object();
        Object result = methodProperty.deserializeAndSet(jsonParser, null, instance);

        assertEquals(42, instance.getClass().getDeclaredField("IntegerField").get(instance));
        assertEquals(42, result);
    }

    @Test
    public void testMethodPropertyWithNullValueProvider() throws Exception {
        Class<?> targetClass = ClassUtil.newSimpleImmutableClass("TestClass", new String[]{"StringField"});
        Method setterMethod = targetClass.getMethod("setStringField", String.class);

        AnnotatedMethod annotatedMethod = mock(AnnotatedMethod.class);
        when(annotatedMethod.getName()).thenReturn("setStringField");
        when(annotatedMethod.getMember()).thenReturn(annotatedMethod);
        when(annotatedMethod.getDeclaringClass()).thenReturn(targetClass);
        when(annotatedMethod.getParameterTypes()).thenReturn(new Class[]{String.class});

        Annotation annotation = mock(JsonSetter.class);
        when(annotation.value()).thenReturn("StringField");

        MethodProperty methodProperty = new MethodProperty(null,
                new BeanPropertyDefinition(targetClass.getDeclaredField("StringField"), String.class),
                null,
                Annotations.empty(),
                annotatedMethod);

        NullValueProvider nullValueProvider = mock(NullValueProvider.class);
        methodProperty = new MethodProperty(methodProperty, null, null);

        JsonParser jsonParser = mock(JsonParser.class);
        when(jsonParser.getValueAsString()).thenReturn(null);

        Object instance = new Object();
        Object result = methodProperty.deserializeAndSet(jsonParser, null, instance);

        assertNull(instance.getClass().getDeclaredField("StringField").get(instance));
        assertNull(result);
    }

    @Test
    public void testMethodPropertyWithConverter() throws Exception {
        Class<?> targetClass = ClassUtil.newSimpleImmutableClass("TestClass", new String[]{"intField"});
        Method setterMethod = targetClass.getMethod("setIntField", int.class);

        AnnotatedMethod annotatedMethod = mock(AnnotatedMethod.class);
        when(annotatedMethod.getName()).thenReturn("setIntField");
        when(annotatedMethod.getMember()).thenReturn(annotatedMethod);
        when(annotatedMethod.getDeclaringClass()).thenReturn(targetClass);
        when(annotatedMethod.getParameterTypes()).thenReturn(new Class[]{int.class});

        Annotation annotation = mock(JsonSetter.class);
        when(annotation.value()).thenReturn("intField");

        MethodProperty methodProperty = new MethodProperty(null,
                new BeanPropertyDefinition(targetClass.getDeclaredField("intField"), int.class),
                null,
                Annotations.empty(),
                annotatedMethod);

        JsonDeserializer<String> stringDeserializer = mock(JsonDeserializer.class);
        when(stringDeserializer.deserialize(mock(JsonParser.class), null)).thenReturn("42");

        methodProperty = new MethodProperty(methodProperty, stringDeserializer, STRING_TO_INTEGER);

        Object instance = new Object();
        Object result = methodProperty.deserializeAndSet(mock(JsonParser.class), null, instance);

        assertEquals(42, instance.getClass().getDeclaredField("intField").get(instance));
        assertEquals(42, result);
    }

    @Test
    public void testMethodPropertyWithOverridePublicAccessModifiers() throws Exception {
        Class<?> targetClass = ClassUtil.newSimpleImmutableClass("TestClass", new String[]{"privateIntField"});
        Method setterMethod = targetClass.getDeclaredMethod("setPrivateIntField", int.class);
        setterMethod.setAccessible(true);

        AnnotatedMethod annotatedMethod = mock(AnnotatedMethod.class);
        when(annotatedMethod.getName()).thenReturn("setPrivateIntField");
        when(annotatedMethod.getMember()).thenReturn(annotatedMethod);
        when(annotatedMethod.getDeclaringClass()).thenReturn(targetClass);
        when(annotatedMethod.getParameterTypes()).thenReturn(new Class[]{int.class});

        Annotation annotation = mock(JsonSetter.class);
        when(annotation.value()).thenReturn("privateIntField");

        MethodProperty methodProperty = new MethodProperty(null,
                new BeanPropertyDefinition(targetClass.getDeclaredField("privateIntField"), int.class),
                null,
                Annotations.empty(),
                annotatedMethod);

        JsonDeserializer<String> stringDeserializer = mock(JsonDeserializer.class);
        methodProperty = new MethodProperty(methodProperty, stringDeserializer, null);

        Object instance = new Object();
        JsonParser jsonParser = mock(JsonParser.class);
        when(jsonParser.getValueAsString()).thenReturn("42");

        methodProperty.fixAccess(null);
        methodProperty.deserializeAndSet(jsonParser, null, instance);

        assertEquals(42, instance.getClass().getDeclaredField("privateIntField").get(instance));
    }
}