package com.fasterxml.jackson.databind.ser; // Make sure to import all necessary classes

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.util.BeanPropertyWriter.MarkerForEmpty;
import com.fasterxml.jackson.databind.util.NameTransformer;
import org.junit.Assert;
import org.junit.Test;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Test cases for PropertyBuilder class.
 */
public class PropertyBuilderTest {

    

    private Object createDefaultBean() {
        return new DefaultBean();
    }

    @Test
    public void testPropertyBuilder() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(DefaultBean.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Assert.assertNotNull(propertyBuilder);
    }

    @Test
    public void testGetClassAnnotations() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(DefaultBean.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Annotations classAnnotations = propertyBuilder.getClassAnnotations();
        Assert.assertNotNull(classAnnotations);
    }

    @Test
    public void testBuildWriter() throws JsonMappingException {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(DefaultBean.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Object defaultBean = createDefaultBean();

        SerializerProvider prov = new StandardSerializerProvider();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition("name", new TypeReference<String>(){}, String.class);
        JavaType declaredType = new TypeReference<String>(){}
                .getType();
        JsonSerializer<?> ser = null;
        TypeSerializer typeSer = null;
        TypeSerializer contentTypeSer = null;
        AnnotatedMember am = null;
        Boolean defaultUseStaticTyping = null;

        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
        Assert.assertNotNull(beanPropertyWriter);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testFindSerializationType() throws JsonMappingException {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(DefaultBean.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        Annotated a = new TestAnnotation();
        JavaType declaredType = new TypeReference<DefaultBean>(){}
                .getType();
        Boolean useStaticTyping = true;

        JavaType serializationType = propertyBuilder.findSerializationType(a, useStaticTyping, declaredType);
        Assert.assertEquals(DefaultBean.class.getCanonicalName(), serializationType.getRawClass().getName());
    }

    @Test
    public void testGetPropertyDefaultValue() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(DefaultBean.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Object defaultBean = createDefaultBean();

        String name = "name";
        AnnotatedMember member = new TestMember();
        JavaType type = new TypeReference<String>(){}
                .getType();

        Object expectedDefaultValue = propertyBuilder.getPropertyDefaultValue(name, member, type);
        Assert.assertEquals("", expectedDefaultValue);
    }

    @Test
    public void testGetDefaultValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);

        Object expectedDefaultValueForString = "";
        Object expectedDefaultValueForInteger = 0;
        Object expectedDefaultValueForList = Arrays.asList();
        Object expectedDefaultValueForMap = new HashMap<>();
        Object expectedDefaultValueForCustomClass = new CustomClass();

        Object actualDefaultValueForString = propertyBuilder.getDefaultValue(new TypeReference<String>(){}
                .getType());
        Assert.assertEquals(expectedDefaultValueForString, actualDefaultValueForString);

        Object actualDefaultValueForInteger = propertyBuilder.getDefaultValue(new TypeReference<Integer>(){}
                .getType());
        Assert.assertEquals(expectedDefaultValueForInteger, actualDefaultValueForInteger);

        Object actualDefaultValueForList = propertyBuilder.getDefaultValue(new TypeReference<List<String>>(){}
                .getType());
        Assert.assertEquals(expectedDefaultValueForList, actualDefaultValueForList);

        Object actualDefaultValueForMap = propertyBuilder.getDefaultValue(new TypeReference<Map<String, Integer>>(){}
                .getType());
        Assert.assertEquals(expectedDefaultValueForMap, actualDefaultValueForMap);

        Object actualDefaultValueForCustomClass = propertyBuilder.getDefaultValue(new TypeReference<CustomClass>(){}
                .getType());
        Assert.assertEquals(expectedDefaultValueForCustomClass, actualDefaultValueForCustomClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testThrowWrappedException() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(DefaultBean.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        String propName = "name";
        Object defaultBean = createDefaultBean();
        Exception e = new RuntimeException("Test Exception");

        propertyBuilder._throwWrapped(e, propName, defaultBean);
    }

    

    // Test classes used for asserting return types and checking exception handling
    static class TestAnnotation {
    }

    static class TestMember {
        Object value;

        public Object getValue(Object obj) {
            return value;
        }
    }

    static class CustomClass {
    }

    static class DefaultBean implements Serializable {
    }
}