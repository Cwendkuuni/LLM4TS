package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

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

public class SetterlessPropertyTest {

    @Test
    public void testWithNullProvider() {
        SetterlessProperty property = createProperty();
        NullValueProvider nvp = mock(NullValueProvider.class);
        SetterlessProperty newProperty = property.withNullProvider(nvp);

        assertEquals(property._annotated, newProperty._annotated);
        assertEquals(property._getter, newProperty._getter);
        assertEquals(property._nullProvider, newProperty._nullProvider);
        assertNull(newProperty._valueDeserializer);
    }

    @Test
    public void testWithNullValueDeserializer() {
        SetterlessProperty property = createProperty();
        JsonDeserializer<?> deser = mock(JsonDeserializer.class);
        SetterlessProperty newProperty = property.withValueDeserializer(deser);

        assertEquals(property._annotated, newProperty._annotated);
        assertEquals(property._getter, newProperty._getter);
        assertEquals(property._nullProvider, newProperty._nullProvider);
        assertEquals(deser, newProperty._valueDeserializer);
    }

    @Test
    public void testWithPropertyName() {
        SetterlessProperty property = createProperty();
        PropertyName newName = new PropertyName("newName");
        SetterlessProperty newProperty = property.withName(newName);

        assertEquals(property._annotated, newProperty._annotated);
        assertEquals(property._getter, newProperty._getter);
        assertEquals(property._nullProvider, newProperty._nullProvider);
        assertEquals(newName, newProperty.getFullName());
    }

    @Test
    public void testFixAccess() {
        SetterlessProperty property = createProperty();
        DeserializationConfig config = mock(DeserializationConfig.class);
        property.fixAccess(config);
    }

    @Test
    public void testGetAnnotation() {
        SetterlessProperty property = createProperty();
        Class<Annotation> acls = Annotation.class;
        Annotation annotation = property.getAnnotation(acls);
        assertNull(annotation);
    }

    @Test
    public void testGetMember() {
        SetterlessProperty property = createProperty();
        AnnotatedMember member = property.getMember();
        assertEquals(property._annotated, member);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetMethod() throws IOException {
        SetterlessProperty property = createProperty();
        property.set(null, null);
    }

    @Test
    public void testSetAndReturnMethod() throws IOException {
        SetterlessProperty property = createProperty();
        Object instance = new Object();
        Object value = new Object();
        Object result = property.setAndReturn(instance, value);

        assertEquals(instance, result);
    }

    private SetterlessProperty createProperty() {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedMethod method = mock(AnnotatedMethod.class);
        SetterlessProperty property = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
        return property;
    }
}