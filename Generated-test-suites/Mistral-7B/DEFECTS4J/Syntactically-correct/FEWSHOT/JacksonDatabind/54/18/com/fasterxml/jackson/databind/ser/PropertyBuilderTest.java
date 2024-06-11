package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;

import org.junit.Test;

public class PropertyBuilderTest {
    @Test
    public void testFindSerializationType() {
        // Test with valid annotations
        BeanDescription beanDesc = new BeanDescription(Object.class);
        AnnotatedMember am = new AnnotatedMember(new MethodTest(), AnnotationIntrospector.getDefault());
        SerializationConfig config = new SerializationConfig();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        JavaType result = propertyBuilder.findSerializationType(am, true, JavaType.constructType(int.class));
        assertEquals(int.class, result.getRawClass());

        // Test with null or non-matching types
        am = new AnnotatedMember(new MethodTest(), null);
        result = propertyBuilder.findSerializationType(am, true, JavaType.constructType(int.class));
        assertNull(result);

        result = propertyBuilder.findSerializationType(am, true, JavaType.constructType(String.class));
        assertNull(result);

        // Test with invalid annotations
        am = new AnnotatedMember(new MethodTest(), new JsonSerializeAnnotation(JsonSerialize.Type.STATIC));
        result = propertyBuilder.findSerializationType(am, true, JavaType.constructType(int.class));
        assertNull(result);
    }

    @Test
    public void testGetClassAnnotations() {
        BeanDescription beanDesc = new BeanDescription(Object.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, beanDesc);
        Annotations actual = propertyBuilder.getClassAnnotations();
        Annotations expected = new StandardAnnotations();
        assertEquals(expected, actual);
    }

    @Test
    public void testBuildWriter() throws JsonMappingException {
        BeanDescription beanDesc = new BeanDescription(Object.class);
        SerializationConfig config = new SerializationConfig();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        // Test with null input
        BeanPropertyDefinition propDef = null;
        JavaType declaredType = JavaType.constructType(int.class);
        JsonSerializer<?> ser = null;
        TypeSerializer typeSer = null;
        TypeSerializer contentTypeSer = null;
        AnnotatedMember am = null;
        boolean defaultUseStaticTyping = false;
        BeanPropertyWriter result = propertyBuilder.buildWriter(null, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
        assertNull(result);

        // Test with valid input
        propDef = new BeanPropertyDefinition("testProperty", int.class, null, null, null, null, null, null, null, null);
        SerializerProvider prov = new SerializerProvider();
        Object serDef = new JsonSerializerAnnotation(JsonSerialize.With.Std());
        SerializerProvider.SerializerProviderContext context = new SerializerProvider.SerializerProviderContext();
        TypeSerializer typeSerializer = new StandardSerializers.BaseJsonTypeSerializer();
        result = propertyBuilder.buildWriter(prov, propDef, declaredType, null, typeSerializer, null, am, defaultUseStaticTyping);
        assertNotNull(result);

        // Test with null serializer and null content type serializer
        ser = null;
        contentTypeSer = null;
        result = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSerializer, contentTypeSer, am, defaultUseStaticTyping);
        assertNotNull(result);

        // Test with null type serializer
        typeSer = null;
        result = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, null, contentTypeSer, am, defaultUseStaticTyping);
        assertNotNull(result);

        // Test with non-null type serializer and null content type serializer
        typeSerializer = new StandardSerializers.BaseJsonTypeSerializer();
        contentTypeSer = null;
        result = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSerializer, contentTypeSer, am, defaultUseStaticTyping);
        assertNotNull(result);

        // Test with null annotated member
        am = null;
        result = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSerializer, contentTypeSer, am, defaultUseStaticTyping);
        assertNotNull(result);

        // Test with defaultUseStaticTyping = true
        boolean defaultUseStaticTypingTrue = true;
        result = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSerializer, contentTypeSer, am, defaultUseStaticTypingTrue);
        assertNotNull(result);

        // Test with null declared type
        JavaType nullDeclaredType = null;
        result = propertyBuilder.buildWriter(prov, propDef, nullDeclaredType, ser, typeSerializer, contentTypeSer, am, defaultUseStaticTyping);
        assertNull(result);

        // Test with null property definition
        propDef = null;
        result = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSerializer, contentTypeSer, am, defaultUseStaticTyping);
        assertNull(result);
    }

    @Test
    public void testFindSerializationTypeWithSuperType() throws JsonMappingException {
        BeanDescription beanDesc = new BeanDescription(Object.class);
        AnnotatedMember am = new AnnotatedMember(new MethodTest(), AnnotationIntrospector.getDefault());
        SerializationConfig config = new SerializationConfig();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        JavaType result = propertyBuilder.findSerializationType(am, true, JavaType.constructType(int[].class));
        assertEquals(int[].class, result.getRawClass());
    }

    @Test
    public void testGetPropertyDefaultValue() {
        BeanDescription beanDesc = new BeanDescription(Object.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, beanDesc);

        // Test with null name, member, or type
        String name = null;
        AnnotatedMember member = null;
        JavaType type = null;
        Object result = propertyBuilder.getPropertyDefaultValue(name, member, type);
        assertNull(result);

        // Test with valid name, member, and type
        name = "testProperty";
        member = new AnnotatedMember(new MethodTest(), AnnotationIntrospector.getDefault());
        type = JavaType.constructType(int.class);
        result = propertyBuilder.getPropertyDefaultValue(name, member, type);
        assertEquals(0, result);

        // Test with String type
        name = "testProperty";
        member = new AnnotatedMember(new MethodTest(), AnnotationIntrospector.getDefault());
        type = JavaType.constructType(String.class);
        result = propertyBuilder.getPropertyDefaultValue(name, member, type);
        assertEquals("", result);

        // Test with Array type
        name = "testProperty";
        member = new AnnotatedMember(new MethodTest(), AnnotationIntrospector.getDefault());
        type = JavaType.constructType(int[].class);
        result = propertyBuilder.getPropertyDefaultValue(name, member, type);
        assertArrayEquals(new int[0], (int[]) result);

        // Test with Map type
        name = "testProperty";
        member = new AnnotatedMember(new MethodTest(), AnnotationIntrospector.getDefault());
        type = JavaType.constructType(Map.class);
        result = propertyBuilder.getPropertyDefaultValue(name, member, type);
        assertEquals(JsonInclude.Include.NON_EMPTY, result);

        // Test with null default bean
        result = propertyBuilder.getPropertyDefaultValue("testProperty", member, type);
        assertNull(result);

        // Test with invalid property name
        name = "invalidProperty";
        result = propertyBuilder.getPropertyDefaultValue(name, member, type);
        assertNull(result);

        // Test with null member
        result = propertyBuilder.getPropertyDefaultValue(name, null, type);
        assertNull(result);

        // Test with null type
        result = propertyBuilder.getPropertyDefaultValue(name, member, null);
        assertNull(result);
    }

    @Test
    public void testGetDefaultValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);

        // Test with null type
        JavaType type = null;
        Object result = propertyBuilder.getDefaultValue(type);
        assertNull(result);

        // Test with primitive type
        type = JavaType.constructType(int.class);
        result = propertyBuilder.getDefaultValue(type);
        assertEquals(0, result);

        // Test with wrapper type
        type = JavaType.constructType(Integer.class);
        result = propertyBuilder.getDefaultValue(type);
        assertEquals(0, result);

        // Test with String type
        type = JavaType.constructType(String.class);
        result = propertyBuilder.getDefaultValue(type);
        assertEquals("", result);

        // Test with Array type
        type = JavaType.constructType(int[].class);
        result = propertyBuilder.getDefaultValue(type);
        assertArrayEquals(new int[0], (int[]) result);

        // Test with Map type
        type = JavaType.constructType(Map.class);
        result = propertyBuilder.getDefaultValue(type);
        assertEquals(JsonInclude.Include.NON_EMPTY, result);

        // Test with Collection type
        type = JavaType.constructType(List.class);
        result = propertyBuilder.getDefaultValue(type);
        assertEquals(JsonInclude.Include.NON_EMPTY, result);
    }
}