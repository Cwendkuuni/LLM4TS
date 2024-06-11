package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Test;

public class PropertyBuilderTest {

    @Test
    public void testFindSerializationType() {
        BeanDescription beanDesc = new BeanDescription(Object.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(new AnnotationIntrospector()), beanDesc);
        AnnotatedMember am = new AnnotatedMember("test", JsonSerialize.class, new Class<?>[]{String.class});

        // Test with null declaredType
        JavaType declaredType = null;
        JavaType serializationType = propertyBuilder.findSerializationType(am, false, declaredType);
        assertNull(serializationType);

        // Test with a specific declaredType
        declaredType = TypeFactory.defaultInstance().constructType(String.class);
        serializationType = propertyBuilder.findSerializationType(am, false, declaredType);
        assertNotNull(serializationType);
        assertEquals(String.class, serializationType.getRawClass());

        // Test with annotations that force the type
        am.setValue(new JsonSerialize(JsonSerialize.With.SerializerByType(StringSerializer.class)));
        serializationType = propertyBuilder.findSerializationType(am, false, declaredType);
        assertNotNull(serializationType);
        assertEquals(StringSerializer.class, serializationType.getContentTypeHandler().getClass());

        // Test when the declaredType is a super type of the annotated type
        am.setValue(new JsonSerialize(JsonSerialize.With.SerializerByType(NumberSerializer.class)));
        JavaType superType = TypeFactory.defaultInstance().constructType(Number.class);
        serializationType = propertyBuilder.findSerializationType(am, false, declaredType);
        assertNotNull(serializationType);
        assertEquals(NumberSerializer.class, serializationType.getContentTypeHandler().getClass());

        // Test when the declaredType is not a super type of the annotated type
        am.setValue(new JsonSerialize(JsonSerialize.With.SerializerByType(IntegerSerializer.class)));
        serializationType = propertyBuilder.findSerializationType(am, false, declaredType);
        assertNull(serializationType);
    }

    @Test
    public void testBuildWriter() throws JsonMappingException {
        BeanDescription beanDesc = new BeanDescription(Object.class);
        SerializerProvider prov = new StandardSerializerProvider();
        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(new AnnotationIntrospector()), beanDesc);
        JavaType declaredType = TypeFactory.defaultInstance().constructType(String.class);
        BeanPropertyDefinition propDef = new BeanPropertyDefinition("test", declaredType, null, null, null, null);
        JsonSerializer<?> ser = new StringSerializer();
        TypeSerializer typeSer = null;
        TypeSerializer contentTypeSer = null;
        AnnotatedMember am = new AnnotatedMember("test", JsonSerialize.class, new Class<?>[]{String.class});

        // Test with null contentTypeSer
        BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, false);
        assertNotNull(bpw);

        // Test with a specific contentTypeSer
        contentTypeSer = new StringSerializer();
        bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, false);
        assertNotNull(bpw);
        assertEquals(contentTypeSer, bpw.getTypeSerializer());

        // Test with annotations that force the type
        am.setValue(new JsonSerialize(JsonSerialize.With.SerializerByType(StringSerializer.class)));
        bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, false);
        assertNotNull(bpw);
        assertEquals(StringSerializer.class, bpw.getSerializer().getClass());

        // Test when the declaredType is a container type with contentTypeSer
        JavaType containerType = TypeFactory.defaultInstance().constructType(List.class, String.class);
        declaredType = TypeFactory.defaultInstance().constructType(containerType.getContentType());
        contentTypeSer = new StringSerializer();
        bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, false);
        assertNotNull(bpw);
        assertEquals(contentTypeSer, bpw.getTypeSerializer());
    }

    @Test
    public void testGetClassAnnotations() {
        BeanDescription beanDesc = new BeanDescription(Object.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(new AnnotationIntrospector()), beanDesc);
        assertNotNull(propertyBuilder.getClassAnnotations());
    }

    @Test
    public void testGetPropertyDefaultValue() {
        BeanDescription beanDesc = new BeanDescription(Object.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(new AnnotationIntrospector()), beanDesc);
        AnnotatedMember am = new AnnotatedMember("test", JsonSerialize.class, new Class<?>[]{String.class});
        JavaType type = TypeFactory.defaultInstance().constructType(String.class);

        // Test with a default bean
        Object defaultBean = propertyBuilder.getDefaultBean();
        Object defaultValue = propertyBuilder.getPropertyDefaultValue("test", am, type);
        assertNull(defaultValue);

        // Test with a non-default bean
        beanDesc.setBeanClass(TestBean.class);
        propertyBuilder = new PropertyBuilder(new SerializationConfig(new AnnotationIntrospector()), beanDesc);
        defaultBean = propertyBuilder.getDefaultBean();
        assertNotNull(defaultBean);
        defaultValue = propertyBuilder.getPropertyDefaultValue("test", am, type);
        assertEquals("", defaultValue);
    }

    @Test
    public void testGetDefaultValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(new AnnotationIntrospector()), new BeanDescription(Object.class));
        JavaType type = TypeFactory.defaultInstance().constructType(int.class);

        // Test with a primitive type
        Object defaultValue = propertyBuilder.getDefaultValue(type);
        assertEquals(0, defaultValue);

        // Test with a String type
        type = TypeFactory.defaultInstance().constructType(String.class);
        defaultValue = propertyBuilder.getDefaultValue(type);
        assertEquals("", defaultValue);

        // Test with a container type
        JavaType containerType = TypeFactory.defaultInstance().constructType(List.class, String.class);
        type = containerType.getContentType();
        defaultValue = propertyBuilder.getDefaultValue(type);
        assertEquals(JsonInclude.Include.NON_EMPTY, defaultValue);

        // Test with a reference type
        JavaType referenceType = TypeFactory.defaultInstance().constructType(TestBean.class);
        defaultValue = propertyBuilder.getDefaultValue(referenceType);
        assertNull(defaultValue);
    }
}