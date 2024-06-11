package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PropertyBuilderTest {

    private SerializationConfig config;
    private BeanDescription beanDesc;
    private AnnotationIntrospector annotationIntrospector;
    private PropertyBuilder propertyBuilder;

    @Before
    public void setUp() throws Exception {
        config = new SerializationConfig(null, "");
        beanDesc = new BasicBeanDescription(Object.class, null, null);
        annotationIntrospector = new AnnotationIntrospector(null);
        propertyBuilder = new PropertyBuilder(config, beanDesc);
    }

    @Test
    public void getClassAnnotations() {
        Annotations annotations = propertyBuilder.getClassAnnotations();
        assertNotNull(annotations);
    }

    @Test
    public void buildWriter() throws JsonMappingException {
        SerializerProvider prov = new DefaultSerializerProvider.Impl();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(null, null, null, null);
        JavaType declaredType = new JavaType(null, null, null, null);
        JsonSerializer<?> ser = null;
        TypeSerializer typeSer = null;
        TypeSerializer contentTypeSer = null;
        AnnotatedMember am = null;
        boolean defaultUseStaticTyping = true;

        BeanPropertyWriter writer = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);

        assertNotNull(writer);
    }

    @Test
    public void findSerializationType() throws JsonMappingException {
        Annotated annotated = null;
        boolean useStaticTyping = true;
        JavaType declaredType = new JavaType(null, null, null, null);

        JavaType serializationType = propertyBuilder.findSerializationType(annotated, useStaticTyping, declaredType);

        assertNull(serializationType);
    }

    @Test
    public void getPropertyDefaultValue() {
        String name = "property";
        AnnotatedMember member = null;
        JavaType type = new JavaType(null, null, null, null);
    
        Object defaultValue = propertyBuilder.getPropertyDefaultValue(name, member, type);

        assertNull(defaultValue);
    }

    @Test
    public void getDefaultValue() {
        JavaType type = new JavaType(null, null, null, null);

        Object defaultValue = propertyBuilder.getDefaultValue(type);

        assertNull(defaultValue);
    }

    @Test
    public void testNoDefaultBean() {
        Object defaultBean = propertyBuilder.getDefaultBean();

        assertNull(defaultBean);
    }

}