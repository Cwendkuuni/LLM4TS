package com.fasterxml.jackson.databind.ser;

import org.junit.Test;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;

public class PropertyBuilderTest {
    @Test
    public void testBuildWriter() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription();
        PropertyBuilder builder = new PropertyBuilder(config, beanDesc);

        SerializerProvider prov = new SerializerProvider();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType declaredType = new JavaType();
        JsonSerializer<?> ser = new JsonSerializer<>();
        TypeSerializer typeSer = new TypeSerializer();
        TypeSerializer contentTypeSer = new TypeSerializer();
        AnnotatedMember am = new AnnotatedMember();
        boolean defaultUseStaticTyping = true;

        try {
            BeanPropertyWriter result = builder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFindSerializationType() {
        PropertyBuilder builder = new PropertyBuilder();

        Annotated a = new Annotated();
        boolean useStaticTyping = true;
        JavaType declaredType = new JavaType();

        try {
            JavaType result = builder.findSerializationType(a, useStaticTyping, declaredType);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetPropertyDefaultValue() {
        PropertyBuilder builder = new PropertyBuilder();

        String name = "testProp";
        AnnotatedMember member = new AnnotatedMember();
        JavaType type = new JavaType();

        Object result = builder.getPropertyDefaultValue(name, member, type);
    }

    @Test
    public void testGetDefaultValue() {
        PropertyBuilder builder = new PropertyBuilder();

        JavaType type = new JavaType();

        Object result = builder.getDefaultValue(type);
    }
}