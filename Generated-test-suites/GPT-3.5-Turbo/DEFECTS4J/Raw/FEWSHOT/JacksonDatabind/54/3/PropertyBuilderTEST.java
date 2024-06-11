package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.TypeSerializer;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.NameTransformer;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair;
import org.junit.Test;

public class PropertyBuilderTest {
    @Test
    public void testGetDefaultBean() {
        SerializationConfig config = new SerializationConfig(null, null, null, null, null, null);
        BeanDescription beanDesc = new BeanDescription();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        assertNull(propertyBuilder.getDefaultBean());
    }

    @Test
    public void testGetPropertyDefaultValue() throws NoSuchFieldException, IllegalAccessException {
        SerializationConfig config = new SerializationConfig(null, null, null, null, null, null);
        BeanDescription beanDesc = new BeanDescription();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        JavaType type = new JavaType();
        Object defaultValue = propertyBuilder.getPropertyDefaultValue("name", new AnnotatedMember(), type);
        assertNull(defaultValue);
    }

    @Test
    public void testGetDefaultValue() throws NoSuchFieldException, IllegalAccessException {
        SerializationConfig config = new SerializationConfig(null, null, null, null, null, null);
        BeanDescription beanDesc = new BeanDescription();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        JavaType type = new JavaType();
        Object defaultValue = propertyBuilder.getDefaultValue(type);
        assertNull(defaultValue);
    }
}