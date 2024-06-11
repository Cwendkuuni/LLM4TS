package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class MethodPropertyTest {

    @Test
    public void testConstructor1() {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedMethod method = mock(AnnotatedMethod.class);
        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser,
                contextAnnotations, method);
        assertNull(methodProperty.getNullValueProvider());
    }

    @Test
    public void testConstructor2() {
        MethodProperty src = mock(MethodProperty.class);
        JsonDeserializer<?> deser = mock(JsonDeserializer.class);
        NullValueProvider nva = mock(NullValueProvider.class);
        MethodProperty methodProperty = new MethodProperty(src, deser, nva);
        assertEquals(src.getType(), methodProperty.getType());
    }

    @Test
    public void testConstructor3() {
        MethodProperty src = mock(MethodProperty.class);
        PropertyName newName = mock(PropertyName.class);
        MethodProperty methodProperty = new MethodProperty(src, newName);
        assertEquals(src.getInternalName(), methodProperty.getInternalName());
    }

    @Test
    public void testConstructor4() {
        MethodProperty src = mock(MethodProperty.class);
        Method m = mock(Method.class);
        MethodProperty methodProperty = new MethodProperty(src, m);
        assertEquals(src.getMember(), methodProperty.getMember());
    }

    @Test
    public void testWithNullProvider() {
        MethodProperty src = mock(MethodProperty.class);
        JsonDeserializer<?> deser = mock(JsonDeserializer.class);
        NullValueProvider nva = mock(NullValueProvider.class);
        MethodProperty methodProperty = new MethodProperty(src, deser, nva);
        assertNull(methodProperty.getNullValueProvider());
    }

    @Test
    public void testFixAccess() {
        MethodProperty methodProperty = Mockito.spy(new MethodProperty(mock(BeanPropertyDefinition.class),
                mock(JavaType.class), mock(TypeDeserializer.class), mock(Annotations.class), mock(AnnotatedMethod.class)));
        DeserializationConfig config = mock(DeserializationConfig.class);
        methodProperty.fixAccess(config);
        Mockito.verify(methodProperty, Mockito.times(1)).fixAccess(Mockito.anyBoolean());
    }
}