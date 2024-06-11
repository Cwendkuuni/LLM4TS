package com.example.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MethodPropertyTest {

    @Test
    public void testMethodPropertyConstructor() {
        BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
        JavaType type = Mockito.mock(JavaType.class);
        TypeDeserializer typeDeser = Mockito.mock(TypeDeserializer.class);
        Annotations contextAnnotations = Mockito.mock(Annotations.class);
        AnnotatedMethod method = Mockito.mock(AnnotatedMethod.class);

        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);

        assertEquals(propDef, methodProperty.getBeanPropertyDefinition());
        assertEquals(type, methodProperty.getType());
        assertEquals(typeDeser, methodProperty.getTypeDeserializer());
        assertEquals(contextAnnotations, methodProperty.getContextAnnotations());
        assertEquals(method, methodProperty.getAnnotated());
        assertEquals(method, methodProperty.getSetter());
        assertFalse(methodProperty.isSkipNulls());
    }

    @Test
    public void testMethodPropertyWithName() {
        PropertyName newName = PropertyName.construct("newName");
        MethodProperty methodProperty = new MethodProperty(methodProperty, newName);

        assertEquals(methodProperty, methodProperty.getBeanProperty());
        assertEquals(newName, methodProperty.getName());
        assertEquals(methodProperty.getBeanPropertyDefinition(), methodProperty.getBeanPropertyDefinition());
        assertEquals(methodProperty.getType(), methodProperty.getType());
        assertEquals(methodProperty.getTypeDeserializer(), methodProperty.getTypeDeserializer());
        assertEquals(methodProperty.getContextAnnotations(), methodProperty.getContextAnnotations());
        assertEquals(methodProperty.getAnnotated(), methodProperty.getAnnotated());
        assertEquals(methodProperty.getSetter(), methodProperty.getSetter());
        assertEquals(methodProperty.isSkipNulls(), methodProperty.isSkipNulls());
    }

    @Test
    public void testMethodPropertyWithValueDeserializer() {
        JsonDeserializer<?> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        NullValueProvider nva = Mockito.mock(NullValueProvider.class);

        MethodProperty methodProperty = new MethodProperty(methodProperty, valueDeserializer, nva);

        assertEquals(methodProperty, methodProperty.getBeanProperty());
        assertEquals(methodProperty.getTypeDeserializer(), valueDeserializer);
        assertEquals(nva, methodProperty.getNullProvider());
        assertEquals(methodProperty.getBeanPropertyDefinition(), methodProperty.getBeanPropertyDefinition());
        assertEquals(methodProperty.getType(), methodProperty.getType());
        assertEquals(methodProperty.getContextAnnotations(), methodProperty.getContextAnnotations());
        assertEquals(methodProperty.getAnnotated(), methodProperty.getAnnotated());
        assertEquals(methodProperty.getSetter(), methodProperty.getSetter());
        assertFalse(methodProperty.isSkipNulls());
    }

    @Test
    public void testMethodPropertyWithNullProvider() {
        NullValueProvider nva = Mockito.mock(NullValueProvider.class);

        MethodProperty methodProperty = new MethodProperty(methodProperty, null, nva);

        assertEquals(methodProperty, methodProperty.getBeanProperty());
        assertNull(methodProperty.getTypeDeserializer());
        assertEquals(nva, methodProperty.getNullProvider());
        assertEquals(methodProperty.getBeanPropertyDefinition(), methodProperty.getBeanPropertyDefinition());
        assertEquals(methodProperty.getType(), methodProperty.getType());
        assertEquals(methodProperty.getContextAnnotations(), methodProperty.getContextAnnotations());
        assertEquals(methodProperty.getAnnotated(), methodProperty.getAnnotated());
        assertEquals(methodProperty.getSetter(), methodProperty.getSetter());
        assertFalse(methodProperty.isSkipNulls());
    }

    @Test
    public void testMethodPropertyWithNameAndValueDeserializer() {
        PropertyName newName = PropertyName.construct("newName");
        JsonDeserializer<?> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        NullValueProvider nva = Mockito.mock(NullValueProvider.class);

        MethodProperty methodProperty = new MethodProperty(methodProperty, newName, valueDeserializer, nva);

        assertEquals(methodProperty, methodProperty.getBeanProperty());
        assertEquals(newName, methodProperty.getName());
        assertEquals(valueDeserializer, methodProperty.getTypeDeserializer());
        assertEquals(nva, methodProperty.getNullProvider());
        assertEquals(methodProperty.getBeanPropertyDefinition(), methodProperty.getBeanPropertyDefinition());
        assertEquals(methodProperty.getType(), methodProperty.getType());
        assertEquals(methodProperty.getContextAnnotations(), methodProperty.getContextAnnotations());
        assertEquals(methodProperty.getAnnotated(), methodProperty.getAnnotated());
        assertEquals(methodProperty.getSetter(), methodProperty.getSetter());
        assertFalse(methodProperty.isSkipNulls());
    }

    @Test
    public void testMethodPropertyWithNameAndNullProvider() {
        PropertyName newName = PropertyName.construct("newName");
        NullValueProvider nva = Mockito.mock(NullValueProvider.class);

        MethodProperty methodProperty = new MethodProperty(methodProperty, newName, null, nva);

        assertEquals(methodProperty, methodProperty.getBeanProperty());
        assertEquals(newName, methodProperty.getName());
        assertNull(methodProperty.getTypeDeserializer());
        assertEquals(nva, methodProperty.getNullProvider());
        assertEquals(methodProperty.getBeanPropertyDefinition(), methodProperty.getBeanPropertyDefinition());
        assertEquals(methodProperty.getType(), methodProperty.getType());
        assertEquals(methodProperty.getContextAnnotations(), methodProperty.getContextAnnotations());
        assertEquals(methodProperty.getAnnotated(), methodProperty.getAnnotated());
        assertEquals(methodProperty.getSetter(), methodProperty.getSetter());
        assertFalse(methodProperty.isSkipNulls());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMethodPropertyWithNullBeanPropertyDefinition() {
        new MethodProperty(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMethodPropertyWithNullType() {
        new MethodProperty(Mockito.mock(BeanPropertyDefinition.class), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMethodPropertyWithNullTypeDeserializer() {
        new MethodProperty(Mockito.mock(BeanPropertyDefinition.class), Mockito.mock(JavaType.class), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMethodPropertyWithNullContextAnnotations() {
        new MethodProperty(Mockito.mock(BeanPropertyDefinition.class), Mockito.mock(JavaType.class), Mockito.mock(TypeDeserializer.class), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMethodPropertyWithNullAnnotatedMethod() {
        new MethodProperty(Mockito.mock(BeanPropertyDefinition.class), Mockito.mock(JavaType.class), Mockito.mock(TypeDeserializer.class), Mockito.mock(Annotations.class), null);
    }

    @Test
    public void testMethodPropertyDeserializeAndSet() throws IOException {
        Object instance = Mockito.mock(Object.class);
        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        MethodProperty methodProperty = new MethodProperty(Mockito.mock(BeanPropertyDefinition.class), Mockito.mock(JavaType.class), Mockito.mock(TypeDeserializer.class), Mockito.mock(Annotations.class), Mockito.mock(AnnotatedMethod.class));

        Object value = "value";
        when(parser.getValueAsString()).thenReturn(value);

        methodProperty.deserializeAndSet(parser, context, instance);

        verify(instance).invoke(_setter.getName(), value);
        verifyNoMoreInteractions(parser, context);
    }

    @Test
    public void testMethodPropertyDeserializeSetAndReturn() throws IOException {
        Object instance = Mockito.mock(Object.class);
        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        MethodProperty methodProperty = new MethodProperty(Mockito.mock(BeanPropertyDefinition.class), Mockito.mock(JavaType.class), Mockito.mock(TypeDeserializer.class), Mockito.mock(Annotations.class), Mockito.mock(AnnotatedMethod.class));

        Object value = "value";
        when(parser.getValueAsString()).thenReturn(value);

        Object result = methodProperty.deserializeSetAndReturn(parser, context, instance);

        assertEquals(instance, result);
        verify(instance).invoke(_setter.getName(), value);
        verifyNoMoreInteractions(parser, context);
    }

    @Test
    public void testMethodPropertySet() throws IOException {
        Object instance = Mockito.mock(Object.class);
        Object value = "value";

        MethodProperty methodProperty = new MethodProperty(Mockito.mock(BeanPropertyDefinition.class), Mockito.mock(JavaType.class), Mockito.mock(TypeDeserializer.class), Mockito.mock(Annotations.class), Mockito.mock(AnnotatedMethod.class));

        methodProperty.set(instance, value);

        verify(instance).invoke(_setter.getName(), value);
    }

    @Test
    public void testMethodPropertySetAndReturn() throws IOException {
        Object instance = Mockito.mock(Object.class);
        Object value = "value";

        MethodProperty methodProperty = new MethodProperty(Mockito.mock(BeanPropertyDefinition.class), Mockito.mock(JavaType.class), Mockito.mock(TypeDeserializer.class), Mockito.mock(Annotations.class), Mockito.mock(AnnotatedMethod.class));

        Object result = methodProperty.setAndReturn(instance, value);

        assertEquals(instance, result);
        verify(instance).invoke(_setter.getName(), value);
    }

    @Test
    public void testMethodPropertyWithSkipNullsTrue() {
        BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
        JavaType type = Mockito.mock(JavaType.class);
        TypeDeserializer typeDeser = Mockito.mock(TypeDeserializer.class);
        Annotations contextAnnotations = Mockito.mock(Annotations.class);
        AnnotatedMethod method = Mockito.mock(AnnotatedMethod.class);
        NullValueProvider nva = Mockito.mock(NullValueProvider.class);

        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method) {
            @Override
            public boolean isSkipNulls() {
                return true;
            }
        };

        assertEquals(propDef, methodProperty.getBeanPropertyDefinition());
        assertEquals(type, methodProperty.getType());
        assertEquals(typeDeser, methodProperty.getTypeDeserializer());
        assertEquals(contextAnnotations, methodProperty.getContextAnnotations());
        assertEquals(method, methodProperty.getAnnotated());
        assertEquals(method, methodProperty.getSetter());
        assertTrue(methodProperty.isSkipNulls());
    }

    @Test(expected = IllegalStateException.class)
    public void testMethodPropertyWithSetterNull() {
        BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
        JavaType type = Mockito.mock(JavaType.class);
        TypeDeserializer typeDeser = Mockito.mock(TypeDeserializer.class);
        Annotations contextAnnotations = Mockito.mock(Annotations.class);
        AnnotatedMethod method = Mockito.mock(AnnotatedMethod.class);
        NullValueProvider nva = Mockito.mock(NullValueProvider.class);

        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, null) {
            @Override
            public boolean isSkipNulls() {
                return false;
            }
        };

        fail("MethodProperty should not be created with null setter.");
    }
}