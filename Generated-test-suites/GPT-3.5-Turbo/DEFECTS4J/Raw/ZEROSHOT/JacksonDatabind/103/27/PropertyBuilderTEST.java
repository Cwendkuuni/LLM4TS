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
    public void testBuildWriter() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        SerializerProvider prov = new SerializerProvider();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType declaredType = new JavaType();
        JsonSerializer<?> ser = new JsonSerializer<>();
        TypeSerializer typeSer = new TypeSerializer();
        TypeSerializer contentTypeSer = new TypeSerializer();
        AnnotatedMember am = new AnnotatedMember();
        boolean defaultUseStaticTyping = true;

        try {
            BeanPropertyWriter result = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
            assertNotNull(result);
        } catch (JsonMappingException e) {
            fail("Exception occurred while running testBuildWriter: " + e.getMessage());
        }
    }

    @Test
    public void testFindSerializationType() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), new BeanDescription());
        Annotated a = new Annotated();
        boolean useStaticTyping = true;
        JavaType declaredType = new JavaType();

        try {
            JavaType result = propertyBuilder.findSerializationType(a, useStaticTyping, declaredType);
            assertNull(result);
        } catch (JsonMappingException e) {
            fail("Exception occurred while running testFindSerializationType: " + e.getMessage());
        }
    }

    @Test
    public void testGetDefaultBean() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), new BeanDescription());
        Object result = propertyBuilder.getDefaultBean();
        assertNull(result);
    }

    @Test
    public void testGetPropertyDefaultValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), new BeanDescription());
        String name = "name";
        AnnotatedMember member = new AnnotatedMember();
        JavaType type = new JavaType();

        Object result = propertyBuilder.getPropertyDefaultValue(name, member, type);
        assertEquals(null, result);
    }

    @Test
    public void testGetDefaultValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), new BeanDescription());
        JavaType type = new JavaType();

        Object result = propertyBuilder.getDefaultValue(type);
        assertEquals(null, result);
    }
    
}