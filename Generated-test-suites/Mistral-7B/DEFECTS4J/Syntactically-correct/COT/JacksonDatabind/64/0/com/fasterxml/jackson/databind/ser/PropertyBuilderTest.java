package com.fasterxml.jackson.databind.ser; // make sure to import necessary classes

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import static com.fasterxml.jackson.databind.ser.PropertyBuilder.*;

public class PropertyBuilderTest {

    

    @Test
    public void testPropertyBuilder_DefaultConstruction() {
        PropertyBuilder pb = new PropertyBuilder(null, null);
        Assert.assertNull(pb.getDefaultBean());
        Assert.assertEquals(JsonInclude.Include.NON_DEFAULT, pb.getDefaultInclusion().getValueInclusion());
        Assert.assertFalse(pb.isUseRealPropertyDefaults());
    }

    @Test
    public void testPropertyBuilder_DefaultConstructionWithAnnotationIntrospector() {
        PropertyBuilder pb = new PropertyBuilder(null, null, null, null);
        Assert.assertNull(pb.getDefaultBean());
        Assert.assertEquals(JsonInclude.Include.NON_DEFAULT, pb.getDefaultInclusion().getValueInclusion());
        Assert.assertFalse(pb.isUseRealPropertyDefaults());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPropertyBuilder_NullSerializationConfig() {
        new PropertyBuilder(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPropertyBuilder_NullBeanDescription() {
        new PropertyBuilder(new SerializationConfig(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPropertyBuilder_NullAnnotationIntrospector() {
        new PropertyBuilder(new SerializationConfig(), new BeanDescription());
        new PropertyBuilder(new SerializationConfig(), new BeanDescription(), null);
    }

    @Test
    public void testPropertyBuilder_SerializationConfigAndBeanDescription() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription();
        PropertyBuilder pb = new PropertyBuilder(config, beanDesc);
        Assert.assertNotNull(pb.getDefaultBean());
        Assert.assertEquals(JsonInclude.Include.NON_DEFAULT, pb.getDefaultInclusion().getValueInclusion());
        Assert.assertFalse(pb.isUseRealPropertyDefaults());
    }

    @Test
    public void testPropertyBuilder_SerializationConfigAndBeanDescriptionAndAnnotationIntrospector() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription();
        AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector();
        PropertyBuilder pb = new PropertyBuilder(config, beanDesc, annotationIntrospector);
        Assert.assertNotNull(pb.getDefaultBean());
        Assert.assertEquals(JsonInclude.Include.NON_DEFAULT, pb.getDefaultInclusion().getValueInclusion());
        Assert.assertFalse(pb.isUseRealPropertyDefaults());
    }

    @Test(expected = JsonMappingException.class)
    public void testPropertyBuilder_InvalidSerializationType() {
        AnnotatedMember am = new AnnotatedMember();
        JavaType declaredType = new JavaType();
        BeanPropertyWriter builderWriter = buildWriter(null, null, declaredType, null, null, null, am, false);
    }

    @Test
    public void testPropertyBuilder_ValidSerializationType() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription();
        AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector();
        AnnotatedMember am = new AnnotatedMember();
        JavaType declaredType = new JavaType();
        JsonSerializer<?> ser = new JsonSerializer<>();
        TypeSerializer typeSer = new TypeSerializer();
        TypeSerializer contentTypeSer = new TypeSerializer();
        BeanPropertyWriter builderWriter = buildWriter(new SerializerProvider(), null, declaredType, ser, typeSer, contentTypeSer, am, false);
        Assert.assertNotNull(builderWriter);
    }

    @Test
    public void testGetClassAnnotations() {
        BeanDescription beanDesc = new BeanDescription();
        PropertyBuilder pb = new PropertyBuilder(null, beanDesc);
        Assert.assertEquals(beanDesc.getClassAnnotations(), pb.getClassAnnotations());
    }

    
}