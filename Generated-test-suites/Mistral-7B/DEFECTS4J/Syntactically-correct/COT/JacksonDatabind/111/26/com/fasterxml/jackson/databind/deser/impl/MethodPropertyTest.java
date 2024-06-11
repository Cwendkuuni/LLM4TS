package com.example.junit.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
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

        assertNotNull(methodProperty);
        assertEquals(propDef, methodProperty.getBeanPropertyDefinition());
        assertEquals(type, methodProperty.getType());
        assertEquals(typeDeser, methodProperty.getTypeDeserializer());
        assertEquals(contextAnnotations, methodProperty.getContextAnnotations());
        assertEquals(method, methodProperty.getAnnotated());
        assertEquals(method, methodProperty.getSetter());
    }

    @Test
    public void testMethodPropertyWithName() {
        BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
        MethodProperty methodProperty = new MethodProperty(propDef, new PropertyName("test"));

        assertEquals(new PropertyName("test"), methodProperty.getName());
    }

    @Test
    public void testMethodPropertyWithValueDeserializer() {
        BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
        MethodProperty methodProperty = new MethodProperty(propDef, new JsonDeserializer<>(), null);

        JsonDeserializer<Object> deserializer = Mockito.mock(JsonDeserializer.class);
        MethodProperty newMethodProperty = methodProperty.withValueDeserializer(deserializer);

        assertEquals(deserializer, newMethodProperty.getValueDeserializer());
    }

    @Test
    public void testMethodPropertyWithNullProvider() {
        BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
        MethodProperty methodProperty = new MethodProperty(propDef, null, null);

        NullValueProvider nva = Mockito.mock(NullValueProvider.class);
        MethodProperty newMethodProperty = methodProperty.withNullProvider(nva);

        assertEquals(nva, newMethodProperty.getNullProvider());
    }

    @Test
    public void testMethodPropertyFixAccess() {
        BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
        AnnotatedMethod annotated = Mockito.mock(AnnotatedMethod.class);
        MethodProperty methodProperty = new MethodProperty(propDef, null, null, null, annotated);

        boolean isEnabled = true;
        methodProperty.fixAccess(Mockito.mock(DeserializationConfig.class).with(config -> config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS).andReturn(isEnabled)));

        assertTrue(isEnabled);
        assertEquals(Modifier.PUBLIC, annotated.getModifiers());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMethodPropertyWithNullAnnotated() {
        BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
        MethodProperty methodProperty = new MethodProperty(propDef, null, null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMethodPropertyWithNullSetter() {
        BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
        AnnotatedMethod annotated = Mockito.mock(AnnotatedMethod.class);
        MethodProperty methodProperty = new MethodProperty(propDef, null, null, null, annotated);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Object instance = new Object();
        BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
        JavaType type = Mockito.mock(JavaType.class);
        TypeDeserializer typeDeser = Mockito.mock(TypeDeserializer.class);
        Annotations contextAnnotations = Mockito.mock(Annotations.class);
        AnnotatedMethod method = Mockito.mock(AnnotatedMethod.class);
        NullValueProvider nva = Mockito.mock(NullValueProvider.class);
        SettableBeanProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
        Method setter = Mockito.mock(Method.class);
        methodProperty._setter = setter;

        JsonParser p = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);

        String value = "testValue";
        when(p.getText()).thenReturn(value);
        when(p.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        when(typeDeser.deserialize(p, ctxt)).thenReturn(value);

        Object result = methodProperty.deserializeAndSet(p, ctxt, instance);

        assertEquals(instance, result);
        verify(setter).invoke(instance, value);
    }

    @Test
    public void testDeserializeAndSetWithNullValue() throws IOException {
        Object instance = new Object();
        BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
        JavaType type = Mockito.mock(JavaType.class);
        TypeDeserializer typeDeser = Mockito.mock(TypeDeserializer.class);
        Annotations contextAnnotations = Mockito.mock(Annotations.class);
        AnnotatedMethod method = Mockito.mock(AnnotatedMethod.class);
        NullValueProvider nva = Mockito.mock(NullValueProvider.class);
        SettableBeanProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
        Method setter = Mockito.mock(Method.class);
        methodProperty._setter = setter;
        boolean skipNulls = true;
        methodProperty._skipNulls = skipNulls;

        Object nullValue = null;
        when(nva.getNullValue(Mockito.any(DeserializationContext.class))).thenReturn(nullValue);

        JsonParser p = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);

        when(p.getText()).thenCallRealMethod();
        when(p.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);

        methodProperty.deserializeAndSet(p, ctxt, instance);

        verify(setter, never()).invoke(instance, null);
        verify(nva).getNullValue(ctxt);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        Object instance = new Object();
        BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
        JavaType type = Mockito.mock(JavaType.class);
        TypeDeserializer typeDeser = Mockito.mock(TypeDeserializer.class);
        Annotations contextAnnotations = Mockito.mock(Annotations.class);
        AnnotatedMethod method = Mockito.mock(AnnotatedMethod.class);
        NullValueProvider nva = Mockito.mock(NullValueProvider.class);
        SettableBeanProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
        Method setter = Mockito.mock(Method.class);
        methodProperty._setter = setter;

        JsonParser p = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);

        Object value = new Object();
        Object result = new Object();
        when(p.getText()).thenReturn("testValue");
        when(typeDeser.deserialize(p, ctxt)).thenReturn(value);
        when(setter.invoke(instance, value)).thenReturn(result);

        Object testResult = methodProperty.deserializeSetAndReturn(p, ctxt, instance);

        assertEquals(result, testResult);
    }

    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
        JavaType type = Mockito.mock(JavaType.class);
        TypeDeserializer typeDeser = Mockito.mock(TypeDeserializer.class);
        Annotations contextAnnotations = Mockito.mock(Annotations.class);
        AnnotatedMethod method = Mockito.mock(AnnotatedMethod.class);
        NullValueProvider nva = Mockito.mock(NullValueProvider.class);
        SettableBeanProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
        Method setter = Mockito.mock(Method.class);
        methodProperty._setter = setter;

        Object value = new Object();

        when(setter.invoke(instance, value)).thenReturnVoid();

        methodProperty.set(instance, value);

        verify(setter).invoke(instance, value);
    }

    @Test
    public void testSetWithNullValue() throws IOException {
        Object instance = new Object();
        BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
        JavaType type = Mockito.mock(JavaType.class);
        TypeDeserializer typeDeser = Mockito.mock(TypeDeserializer.class);
        Annotations contextAnnotations = Mockito.mock(Annotations.class);
        AnnotatedMethod method = Mockito.mock(AnnotatedMethod.class);
        NullValueProvider nva = Mockito.mock(NullValueProvider.class);
        SettableBeanProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
        Method setter = Mockito.mock(Method.class);
        methodProperty._setter = setter;
        boolean skipNulls = true;
        methodProperty._skipNulls = skipNulls;

        NullValueProvider nullValueProvider = Mockito.mock(NullValueProvider.class);
        when(nva.getNullValue(Mockito.any(DeserializationContext.class))).thenReturn(nullValueProvider.getNullValue(Mockito.any(DeserializationContext.class)));

        Object nullValue = null;

        methodProperty.set(instance, nullValue);

        verify(setter, never()).invoke(instance, null);
        verify(nullValueProvider).getNullValue(Mockito.any(DeserializationContext.class));
    }

    @Test
    public void testReadResolve() throws IOException {
        Object instance = new Object();
        BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
        AnnotatedMethod method = Mockito.mock(AnnotatedMethod.class);
        MethodProperty originalMethodProperty = new MethodProperty(propDef, null, null, null, method);
        MethodProperty methodProperty = originalMethodProperty.readResolve();

        assertEquals(originalMethodProperty, methodProperty);
        assertEquals(propDef, methodProperty.getBeanPropertyDefinition());
        assertEquals(method, methodProperty.getAnnotated());
        assertEquals(method, methodProperty.getSetter());
    }
}