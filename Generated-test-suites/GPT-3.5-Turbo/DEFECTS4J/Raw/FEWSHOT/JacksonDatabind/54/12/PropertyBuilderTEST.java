
package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class PropertyBuilderTest {

    @Test
    public void testGetClassAnnotations() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(null, null, null, null, null), new BeanDescription(null, null, null, null, null));
        Annotations annotations = propertyBuilder.getClassAnnotations();
        assertNull(annotations);
    }

    @Test
    public void testBuildWriter() throws JsonMappingException {
        SerializerProvider serializerProvider = new SerializerProvider();
        BeanPropertyDefinition beanPropertyDefinition = new BeanPropertyDefinition(null, null, null);
        JavaType javaType = new JavaType();
        JsonSerializer serializer = new JsonSerializer();
        TypeSerializer typeSerializer = new TypeSerializer();
        TypeSerializer contentTypeSer = new TypeSerializer();
        AnnotatedMember annotatedMember = new AnnotatedMember();
        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(null, null, null, null, null), new BeanDescription(null, null, null, null, null));
        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(serializerProvider, beanPropertyDefinition, javaType, serializer, typeSerializer, contentTypeSer, annotatedMember, true);
        assertNotNull(beanPropertyWriter);
    }

    @Test
    public void testFindSerializationType() throws JsonMappingException {
        Annotated annotated = new Annotated();
        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(null, null, null, null, null), new BeanDescription(null, null, null, null, null));
        JavaType javaType = propertyBuilder.findSerializationType(annotated, true, new JavaType());
        assertNull(javaType);
    }

    @Test
    public void testGetPropertyDefaultValue() {
        AnnotatedMember annotatedMember = new AnnotatedMember();
        BeanDescription beanDescription = new BeanDescription(null, null, null, null, null);
        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(null, null, null, null, null), beanDescription);
        Object defaultValue = propertyBuilder.getPropertyDefaultValue(null, annotatedMember, new JavaType());
        assertNull(defaultValue);
    }

    @Test
    public void testGetDefaultValue() {
        JavaType javaType = new JavaType();
        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(null, null, null, null, null), new BeanDescription(null, null, null, null, null));
        Object defaultValue = propertyBuilder.getDefaultValue(javaType);
        assertNull(defaultValue);
    }

    @Test
    public void testGetDefaultBean() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(null, null, null, null, null), new BeanDescription(null, null, null, null, null));
        Object defaultBean = propertyBuilder.getDefaultBean();
        assertNull(defaultBean);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testThrowWrapped() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(null, null, null, null, null), new BeanDescription(null, null, null, null, null));
        propertyBuilder._throwWrapped(new Exception(), "", null);
    }
}
