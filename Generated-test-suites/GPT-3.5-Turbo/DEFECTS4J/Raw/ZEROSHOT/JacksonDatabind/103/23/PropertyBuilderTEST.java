package com.fasterxml.jackson.databind.ser;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;

public class PropertyBuilderTest {

    private PropertyBuilder propertyBuilder;
    private SerializationConfig config;
    private BeanDescription beanDesc;

    @Before
    public void setup() {
        config = new SerializationConfig(null, null, null);
        beanDesc = new BeanDescription(null, null);
        propertyBuilder = new PropertyBuilder(config, beanDesc);
    }
    
    @Test
    public void testBuildWriter() {
        SerializerProvider prov = new SerializerProvider();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(null);
        JavaType declaredType  = new JavaType(null, null);
        JsonSerializer ser = new JsonSerializer();
        TypeSerializer typeSer = new TypeSerializer();
        TypeSerializer contentTypeSer = new TypeSerializer();
        AnnotatedMember am = new AnnotatedMember();
        boolean defaultUseStaticTyping = true;

        propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
    }
}