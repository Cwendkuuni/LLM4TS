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
    private SerializationConfig config;
    private BeanDescription beanDesc;
    
    @Before
    public void setUp() {
        config = new SerializationConfig();
        beanDesc = new BeanDescription();
        propertyBuilder = new PropertyBuilder(config, beanDesc);
    }
    
    @Test
    public void testBuildWriter() throws JsonMappingException {
        SerializerProvider prov = new SerializerProvider();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType declaredType = new JavaType();
        JsonSerializer<?> ser = new JsonSerializer<>();
        TypeSerializer typeSer = new TypeSerializer();
        TypeSerializer contentTypeSer = new TypeSerializer();
        AnnotatedMember am = new AnnotatedMember();
        boolean defaultUseStaticTyping = true;

        BeanPropertyWriter writer = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);

        Assert.assertNotNull(writer);
    }
    
    @Test
    public void testFindSerializationType() throws JsonMappingException {
        Annotated a = new Annotated();
        boolean useStaticTyping = true;
        JavaType declaredType = new JavaType();
        
        JavaType serializationType = propertyBuilder.findSerializationType(a, useStaticTyping, declaredType);
        
        Assert.assertNull(serializationType);
    }
    
    @Test
    public void testGetDefaultBean() {
        Object defaultBean = propertyBuilder.getDefaultBean();
        
        Assert.assertNull(defaultBean);
    }
    
    @Test
    public void testGetPropertyDefaultValue() {
        String name = "test";
        AnnotatedMember member = new AnnotatedMember();
        JavaType type = new JavaType();
        
        Object defaultValue = propertyBuilder.getPropertyDefaultValue(name, member, type);
        
        Assert.assertNull(defaultValue);
    }
    
    @Test
    public void testGetDefaultValue() {
        JavaType type = new JavaType();
        
        Object defaultValue = propertyBuilder.getDefaultValue(type);
        
        Assert.assertNull(defaultValue);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testThrowWrapped() {
        Exception e = new Exception();
        String propName = "test";
        Object defaultBean = new Object();
        
        propertyBuilder._throwWrapped(e, propName, defaultBean);
    }
}