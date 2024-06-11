package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;

import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PropertyBuilderTest {

    @Test
    public void testFindSerializationType() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        Annotated annotated = Mockito.mock(Annotated.class);
        JavaType javaType = Mockito.mock(JavaType.class);
        JavaType declaredType = Mockito.mock(JavaType.class);

        Mockito.when(annotated.getType()).thenReturn(javaType);
        Mockito.when(javaType.getRawClass()).thenReturn(String.class);
        Mockito.when(javaType.isContainerType()).thenReturn(false);
        Mockito.when(javaType.isReferenceType()).thenReturn(false);

        JsonSerialize.Typing typing = Mockito.mock(JsonSerialize.Typing.class);
        Mockito.when(typing.getValue()).thenReturn(JsonSerialize.Typing.STATIC);
        Mockito.when(typing.getStaticTyping()).thenReturn(javaType.withStaticTyping());

        Mockito.when(propertyBuilder._annotationIntrospector.findSerializationType(annotated, false, declaredType)).thenReturn(javaType);
        Mockito.when(propertyBuilder._annotationIntrospector.findSerializationTyping(annotated)).thenReturn(typing);

        Object result = propertyBuilder.findSerializationType(annotated, false, declaredType);

        Assert.assertNotNull(result);
        Assert.assertEquals(javaType, result);
    }

    @Test
    public void testFindSerializationType_null() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        Annotated annotated = Mockito.mock(Annotated.class);
        JavaType javaType = Mockito.mock(JavaType.class);
        JavaType declaredType = Mockito.mock(JavaType.class);

        Mockito.when(annotated.getType()).thenReturn(null);

        Assert.assertNull(propertyBuilder.findSerializationType(annotated, false, declaredType));
    }

    @Test
    public void testFindSerializationType_nonAssignableFrom() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        Annotated annotated = Mockito.mock(Annotated.class);
        JavaType javaType = Mockito.mock(JavaType.class);
        JavaType declaredType = Mockito.mock(JavaType.class);

        Mockito.when(annotated.getType()).thenReturn(javaType);
        Mockito.when(javaType.getRawClass()).thenReturn(String[].class);
        Mockito.when(javaType.isContainerType()).thenReturn(true);
        Mockito.when(javaType.isReferenceType()).thenReturn(false);
        Mockito.when(declaredType.getRawClass()).thenReturn(Integer.class);

        Assert.assertThrows(IllegalArgumentException.class, () -> {
            propertyBuilder.findSerializationType(annotated, false, declaredType);
        });
    }

    @Test
    public void testFindSerializationType_subtype() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        Annotated annotated = Mockito.mock(Annotated.class);
        JavaType javaType = Mockito.mock(JavaType.class);
        JavaType declaredType = Mockito.mock(JavaType.class);

        Mockito.when(annotated.getType()).thenReturn(javaType);
        Mockito.when(javaType.getRawClass()).thenReturn(String.class);
        Mockito.when(javaType.isContainerType()).thenReturn(false);
        Mockito.when(javaType.isReferenceType()).thenReturn(false);
        Mockito.when(declaredType.getRawClass()).thenReturn(Object.class);

        JavaType result = propertyBuilder.findSerializationType(annotated, false, declaredType);

        Assert.assertNull(result);
    }

    @Test
    public void testFindSerializationType_staticTyping() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        Annotated annotated = Mockito.mock(Annotated.class);
        JavaType javaType = Mockito.mock(JavaType.class);
        JavaType declaredType = Mockito.mock(JavaType.class);

        Mockito.when(annotated.getType()).thenReturn(javaType);
        Mockito.when(javaType.getRawClass()).thenReturn(String.class);
        Mockito.when(javaType.isContainerType()).thenReturn(false);
        Mockito.when(javaType.isReferenceType()).thenReturn(false);
        Mockito.when(declaredType.getRawClass()).thenReturn(String.class);

        JsonSerialize.Typing typing = Mockito.mock(JsonSerialize.Typing.class);
        Mockito.when(typing.getValue()).thenReturn(JsonSerialize.Typing.STATIC);
        Mockito.when(typing.getStaticTyping()).thenReturn(javaType.withStaticTyping());

        Mockito.when(propertyBuilder._annotationIntrospector.findSerializationType(annotated, false, declaredType)).thenReturn(null);
        Mockito.when(propertyBuilder._annotationIntrospector.findSerializationTyping(annotated)).thenReturn(typing);

        JavaType result = propertyBuilder.findSerializationType(annotated, true, declaredType);

        Assert.assertEquals(javaType, result);
    }

    @Test
    public void testGetPropertyDefaultValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        String propName = "propName";
        AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
        JavaType type = Mockito.mock(JavaType.class);
        Object defaultBean = new Object();

        Mockito.when(member.getName()).thenReturn(propName);
        Mockito.when(member.getValue(defaultBean)).thenReturn("value");

        Object result = propertyBuilder.getPropertyDefaultValue(propName, member, type);

        Assert.assertEquals("value", result);
    }

    @Test
    public void testGetPropertyDefaultValue_null() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        String propName = "propName";
        AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
        JavaType type = Mockito.mock(JavaType.class);

        Mockito.when(member.getName()).thenReturn(propName);

        Assert.assertNull(propertyBuilder.getPropertyDefaultValue(propName, member, type));
    }

    @Test
    public void testGetPropertyDefaultValue_emptyString() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        String propName = "propName";
        AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
        JavaType type = Mockito.mock(JavaType.class);

        Mockito.when(member.getName()).thenReturn(propName);
        Mockito.when(member.getValue(Mockito.anyObject())).thenReturn(null);

        Mockito.when(propertyBuilder._beanDesc.getClassInfo().getAnnotated()).thenReturn(Mockito.mock(Class.class));

        String result = propertyBuilder.getPropertyDefaultValue(propName, member, type).toString();

        Assert.assertEquals("", result);
    }

    @Test
    public void testGetPropertyDefaultValue_nonString() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        String propName = "propName";
        AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
        JavaType type = Mockito.mock(JavaType.class);

        Mockito.when(member.getName()).thenReturn(propName);

        Assert.assertNull(propertyBuilder.getPropertyDefaultValue(propName, member, type));
    }

    @Test
    public void testGetDefaultValue_integer() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        JavaType type = Mockito.mock(JavaType.class);

        Class<?> intType = Integer.TYPE;
        Integer defaultValue = 0;

        Mockito.when(type.getRawClass()).thenReturn(intType);
        Mockito.when(ClassUtil.defaultValue(intType)).thenReturn(defaultValue);

        Object result = propertyBuilder.getDefaultValue(type);

        Assert.assertEquals(defaultValue, result);
    }

    @Test
    public void testGetDefaultValue_string() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        JavaType type = Mockito.mock(JavaType.class);

        Class<?> stringType = String.class;
        String defaultValue = "";

        Mockito.when(type.getRawClass()).thenReturn(stringType);
        Mockito.when(ClassUtil.defaultValue(stringType)).thenReturn(defaultValue);

        Object result = propertyBuilder.getDefaultValue(type);

        Assert.assertEquals(defaultValue, result);
    }

    @Test
    public void testGetDefaultValue_null() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        JavaType type = Mockito.mock(JavaType.class);

        Mockito.when(type.getRawClass()).thenReturn(null);

        Assert.assertNull(propertyBuilder.getDefaultValue(type));
    }

    @Test
    public void testGetDefaultValue_collection() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        JavaType type = Mockito.mock(JavaType.class);

        JavaType elementType = Mockito.mock(JavaType.class);
        Mockito.when(type.getContentType()).thenReturn(elementType);

        Object defaultValue = JsonInclude.Include.NON_EMPTY;

        Mockito.when(propertyBuilder._config.isEnabled(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS)).thenReturn(false);
        Mockito.when(propertyBuilder._config.getDefaultPropertyInclusion(type.getRawClass(), JsonInclude.Value.empty())).thenReturn(JsonInclude.Value.nonEmpty());
        Mockito.when(propertyBuilder._config.getDefaultPropertyInclusion(elementType.getRawClass(), JsonInclude.Value.empty())).thenReturn(JsonInclude.Value.nonEmpty());

        Object result = propertyBuilder.getDefaultValue(type);

        Assert.assertEquals(defaultValue, result);
    }

    @Test
    public void testGetDefaultValue_map() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        JavaType type = Mockito.mock(JavaType.class);

        Class<?> keyType = String.class;
        Class<?> valueType = Integer.TYPE;

        Mockito.when(type.getRawClass()).thenReturn(Map.class);
        Mockito.when(type.getType()).thenReturn(Map.class);
        Mockito.when(type.getContentType()).thenReturn(Map.Type.STANDARD_IMPLIENTATION_TYPE);
        Mockito.when(((Map.Type) type.getContentType()).getContentType()).thenReturn(Map.Entry.class);
        Mockito.when(((Map.Entry) type.getContentType()).getKeyType()).thenReturn(keyType);
        Mockito.when(((Map.Entry) type.getContentType()).getValueType()).thenReturn(valueType);

        Object defaultValue = JsonInclude.Include.NON_EMPTY;

        Mockito.when(propertyBuilder._config.isEnabled(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS)).thenReturn(false);
        Mockito.when(propertyBuilder._config.getDefaultPropertyInclusion(Map.class, JsonInclude.Value.empty())).thenReturn(JsonInclude.Value.nonEmpty());
        Mockito.when(propertyBuilder._config.getDefaultPropertyInclusion(Map.Entry.class, JsonInclude.Value.empty())).thenReturn(JsonInclude.Value.nonEmpty());
        Mockito.when(propertyBuilder._config.getDefaultPropertyInclusion(keyType, JsonInclude.Value.empty())).thenReturn(JsonInclude.Value.nonEmpty());
        Mockito.when(propertyBuilder._config.getDefaultPropertyInclusion(valueType, JsonInclude.Value.empty())).thenReturn(JsonInclude.Value.nonEmpty());

        Object result = propertyBuilder.getDefaultValue(type);

        Assert.assertEquals(defaultValue, result);
    }
}