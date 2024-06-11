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
    private PropertyBuilder propertyBuilder;
    private SerializationConfig config;
    private BeanDescription beanDesc;

    @Test
    public void testBuildWriter() throws Exception {
        propertyBuilder = new PropertyBuilder(config, beanDesc);
        SerializerProvider prov = null;
        BeanPropertyDefinition propDef = null;
        JavaType declaredType = null;
        JsonSerializer serializer = null;
        TypeSerializer typeSer = null;
        TypeSerializer contentTypeSer = null;
        AnnotatedMember am = null;
        boolean defaultUseStaticTyping = true;
        BeanPropertyWriter writer = propertyBuilder.buildWriter(prov, propDef, declaredType, serializer, typeSer, contentTypeSer, am, defaultUseStaticTyping);
        assertNotNull(writer);
    }

    @Test
    public void testFindSerializationType() throws Exception {
        propertyBuilder = new PropertyBuilder(config, beanDesc);
        Annotated a = null;
        boolean useStaticTyping = true;
        JavaType declaredType = null;
        JavaType result = propertyBuilder.findSerializationType(a, useStaticTyping, declaredType);
        assertNull(result);
    }

    @Test
    public void testGetDefaultValue() throws Exception {
        propertyBuilder = new PropertyBuilder(config, beanDesc);
        JavaType type = null;
        Object result = propertyBuilder.getDefaultValue(type);
        assertNull(result);
    }

}