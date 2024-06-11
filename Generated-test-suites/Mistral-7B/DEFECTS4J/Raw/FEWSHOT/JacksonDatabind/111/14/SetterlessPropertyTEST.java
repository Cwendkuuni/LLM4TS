package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.JsonDeserializer.None;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

public class SetterlessPropertyTest {

    @Test
    public void testBasic() throws IOException {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedMethod method = mock(AnnotatedMethod.class);
        SetterlessProperty property = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
        assertSame(property.getBeanPropertyDefinition(), propDef);
        assertSame(property.getType(), type);
        assertSame(property._annotated, method);
        assertSame(property._getter, method.getAnnotated());

        Method getter = mock(Method.class);
        property = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method, getter);
        assertSame(property.getGetter(), getter);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        AnnotatedClass targetClass = mock(AnnotatedClass.class);
        Class<?> targetType = ClassUtil.simpleNameToClass(Target.class.getName());
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedMember member = mock(AnnotatedMethod.class);
        Method getter = mock(Method.class);
        JsonDeserializer<Object> valueDeserializer = new None();
        SetterlessProperty property = new SetterlessProperty(null, null, null, contextAnnotations, member, getter);
        property = new SetterlessProperty(property, valueDeserializer, null);

        Object instance = mock(Object.class);
        Object value = new Object() {
            @JsonProperty
            int foo = 42;
        };

        when(member.getAnnotated()).thenReturn(member);
        when(getter.invoke(instance, (Object[]) null)).thenReturn(new ArrayList<>());

        JsonParser parser = mock(JsonParser.class);
        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);

        Object result = property.deserializeAndSet(parser, null, instance);
        assertNull(result);

        when(parser.getCurrentToken()).thenReturn(JsonToken.START_ARRAY);
        when(parser.nextValue()).thenReturn(new Object());
        when(parser.nextValue()).thenReturn(value);

        result = property.deserializeAndSet(parser, null, instance);
        assertSame(instance, result);
        verify(instance).getClass();
        verify(instance).getClass().getDeclaredMethod("set", Object.class);
        verify(instance).getClass().getDeclaredMethod("set", Object.class).invoke(instance, value);
    }

    @Test
    public void testFixAccess() throws IOException {
        AnnotatedClass targetClass = mock(AnnotatedClass.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedMember member = mock(AnnotatedMethod.class);
        Method getter = mock(Method.class);
        SetterlessProperty property = new SetterlessProperty(null, null, null, contextAnnotations, member, getter);
        property = new SetterlessProperty(property, null, null);

        property.fixAccess(null);
        verify(member).fixAccess(true);
    }

    @Test
    public void testGetAnnotation() throws IOException {
        AnnotatedClass targetClass = mock(AnnotatedClass.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedMember member = mock(AnnotatedMethod.class);
        SetterlessProperty property = new SetterlessProperty(null, null, null, contextAnnotations, member, null);

        JsonProperty jsonProperty = new JsonProperty();
        Annotation annotation = jsonProperty;
        when(member.getAnnotations()).thenReturn(new Annotation[0]);
        when(member.getDeclaredAnnotations()).thenReturn(new Annotation[]{annotation});

        assertSame(annotation, property.getAnnotation(JsonProperty.class));
        assertNull(property.getAnnotation(JsonCreator.class));
    }

    @Test
    public void testGetMember() {
        AnnotatedClass targetClass = mock(AnnotatedClass.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedMember member = mock(AnnotatedMethod.class);
        SetterlessProperty property = new SetterlessProperty(null, null, null, contextAnnotations, member, null);

        assertSame(member, property.getMember());
    }

    @Test
    public void testSet() throws IOException {
        Object instance = mock(Object.class);
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null, null);
        try {
            property.set(instance, new Object());
            fail();
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = mock(Object.class);
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null, null);
        try {
            property.setAndReturn(instance, new Object());
            fail();
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    public static class Target {
        @JsonProperty
        private List<Object> list;

        public List<Object> getList() {
            return list;
        }

        public void setList(List<Object> list) {
            this.list = list;
        }
    }
}