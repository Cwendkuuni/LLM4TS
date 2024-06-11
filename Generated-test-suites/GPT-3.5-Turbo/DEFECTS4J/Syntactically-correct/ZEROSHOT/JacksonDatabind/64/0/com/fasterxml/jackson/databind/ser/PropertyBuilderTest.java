package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.*;
import static org.junit.Assert.*;

public class PropertyBuilderTest {
    
    @Test
    public void testBuildWriter() {
        SerializationConfig config = null;
        BeanDescription beanDesc = null;
        
        PropertyBuilder pb = new PropertyBuilder(config, beanDesc);
        
        SerializerProvider prov = null;
        BeanPropertyDefinition propDef = null;
        JavaType declaredType = null;
        JsonSerializer<?> ser = null;
        TypeSerializer typeSer = null;
        TypeSerializer contentTypeSer = null;
        AnnotatedMember am = null;
        boolean defaultUseStaticTyping = false;
        
        try {
            BeanPropertyWriter bpw = pb.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
            assertNotNull(bpw);
        } catch (JsonMappingException e) {
            fail("Exception should not be thrown in this test case");
        }
    }

    @Test
    public void testFindSerializationType() {
        AnnotationIntrospector annotationIntrospector = null;
        Annotated a = null;
        boolean useStaticTyping = false;
        JavaType declaredType = null;
        
        PropertyBuilder pb = new PropertyBuilder(null, null);
        
        try {
            JavaType serializationType = pb.findSerializationType(a, useStaticTyping, declaredType);
            assertNull(serializationType);
        } catch (JsonMappingException e) {
            fail("Exception should not be thrown in this test case");
        }
    }

    @Test
    public void testGetPropertyDefaultValue() {
        String name = "";
        AnnotatedMember member = null;
        JavaType type = null;
        
        PropertyBuilder pb = new PropertyBuilder(null, null);
        
        Object defaultValue = pb.getPropertyDefaultValue(name, member, type);
        assertNull(defaultValue);
    }

    @Test
    public void testGetDefaultValue() {
        JavaType type = null;
        
        PropertyBuilder pb = new PropertyBuilder(null, null);
        
        Object defaultValue = pb.getDefaultValue(type);
        assertNull(defaultValue);
    }
}