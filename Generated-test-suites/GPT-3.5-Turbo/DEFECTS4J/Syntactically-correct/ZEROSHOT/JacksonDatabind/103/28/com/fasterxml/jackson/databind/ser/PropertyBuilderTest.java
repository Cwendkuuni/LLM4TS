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
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        Annotations annotations = propertyBuilder.getClassAnnotations();
        assertEquals(null, annotations);
    }

    @Test
    public void testBuildWriter() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(null, null, null, null, null, null, null, false);
        assertEquals(null, beanPropertyWriter);
    }

    @Test
    public void testFindSerializationType() throws JsonMappingException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        Annotated annotated = null;
        boolean useStaticTyping = false;
        JavaType declaredType = null;
        JavaType serializationType = propertyBuilder.findSerializationType(annotated, useStaticTyping, declaredType);
        assertEquals(null, serializationType);
    }

    @Test
    public void testGetPropertyDefaultValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        String name = null;
        AnnotatedMember member = null;
        JavaType type = null;
        Object defaultValue = propertyBuilder.getPropertyDefaultValue(name, member, type);
        assertEquals(null, defaultValue);
    }

    @Test
    public void testGetDefaultValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        JavaType type = null;
        Object defaultValue = propertyBuilder.getDefaultValue(type);
        assertEquals(null, defaultValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testThrowWrapped() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        Exception e = new Exception();
        String propName = null;
        Object defaultBean = null;
        propertyBuilder._throwWrapped(e, propName, defaultBean);
    }
}