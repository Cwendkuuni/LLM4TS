package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PropertyBuilderTest {

    private PropertyBuilder propertyBuilder;

    @Before
    public void setUp() {
        SerializationConfig config = new SerializationConfig(null, null, null);
        BeanDescription beanDesc = new BeanDescription(null, null);
        propertyBuilder = new PropertyBuilder(config, beanDesc);
    }
    
    @Test
    public void testGetClassAnnotations() {
        Annotations annotations = propertyBuilder.getClassAnnotations();
        Assert.assertNotNull(annotations);
    }

    @Test
    public void testBuildWriter() throws JsonMappingException {
        SerializerProvider prov = new DefaultSerializerProvider.DefaultImpl();
        BeanPropertyDefinition propDef = new BeanDescriptionTest.BeanPropertyDefinitionTest();
        JavaType declaredType = null;
        JsonSerializer<?> ser = null;
        TypeSerializer typeSer = null;
        TypeSerializer contentTypeSer = null;
        AnnotatedMember am = new AnnotatedFieldTest();
        boolean defaultUseStaticTyping = true;
        BeanPropertyWriter writer = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
        Assert.assertNotNull(writer);
    }

    @Test
    public void testFindSerializationType() throws JsonMappingException {
        Annotated a = new AnnotatedFieldTest();
        boolean useStaticTyping = true;
        JavaType declaredType = null;
        JavaType serializationType = propertyBuilder.findSerializationType(a, useStaticTyping, declaredType);
        Assert.assertNull(serializationType);
    }

    @Test
    public void testGetPropertyDefaultValue() {
        String name = "testProperty";
        AnnotatedMember member = new AnnotatedFieldTest();
        JavaType type = null;
        Object defaultValue = propertyBuilder.getPropertyDefaultValue(name, member, type);
        Assert.assertNull(defaultValue);
    }

    @Test
    public void testGetDefaultValue() {
        JavaType type = null;
        Object defaultValue = propertyBuilder.getDefaultValue(type);
        Assert.assertNull(defaultValue);
    }

    @Test
    public void testThrowWrapped() {
        Exception e = new Exception("Error");
        String propName = "testProperty";
        Object defaultBean = new Object();
        Throwable thrown = null;
        try {
            propertyBuilder._throwWrapped(e, propName, defaultBean);
        } catch (Throwable t) {
            thrown = t;
        }
        Assert.assertEquals(IllegalArgumentException.class, thrown.getClass());
    }
}