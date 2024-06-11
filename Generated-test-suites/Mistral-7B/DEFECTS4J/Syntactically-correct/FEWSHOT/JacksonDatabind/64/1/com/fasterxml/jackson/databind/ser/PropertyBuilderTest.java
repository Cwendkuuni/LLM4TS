package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PropertyBuilderTest {
    private ObjectMapper mapper;

    @Before
    public void setUp() {
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testFindSerializationType() throws JsonMappingException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Class<?> clazz = MyPojo.class;
        AnnotatedMember member = Reflections.getDeclaredMethod(clazz, "getProp");
        PropertyBuilder propertyBuilder = new PropertyBuilder(mapper.getSerializationConfig(), mapper.findBeanDescription(clazz));
        JsonSerialize serializeAnnotation = member.getAnnotation(JsonSerialize.class);
        JavaType declaredType = mapper.getTypeFactory().constructType(clazz.getName());
        JsonSerializer<?> ser = mapper.getSerializerProvider().findValueSerializer(declaredType);
        TypeSerializer typeSer = mapper.getSerializerProvider().getTypeSerializer();
        TypeSerializer contentTypeSer = mapper.getSerializerProvider().getTypeSerializer();
        Object instance = clazz.newInstance();
        AnnotationIntrospector annotationIntrospector = mapper.getSerializationConfig().getAnnotationIntrospector();

        JavaType result = propertyBuilder.findSerializationType(member, false, declaredType);
        Assert.assertEquals(declaredType, result);

        JsonSerialize annotation = new JsonSerialize() {{
            useJsonGetter = true;
        }};
        member.addAnnotation(annotation);
        result = propertyBuilder.findSerializationType(member, false, declaredType);
        Assert.assertEquals(declaredType, result);

        annotation.typeName("CustomType");
        result = propertyBuilder.findSerializationType(member, false, declaredType);
        Assert.assertEquals(mapper.getTypeFactory().constructType(annotation.typeName()), result);

        annotationIntrospector.findSerializationType(member, declaredType);
        result = propertyBuilder.findSerializationType(member, false, declaredType);
        Assert.assertEquals(mapper.getTypeFactory().constructType(annotation.typeName()), result);

        TypeSerializer ts = mapper.getSerializerProvider().getTypeSerializer();
        contentTypeSer = ts;
        result = propertyBuilder.findSerializationType(member, false, declaredType);
        Assert.assertEquals(mapper.getTypeFactory().constructType(annotation.typeName()), result);

        Class<?> contentClass = int[].class;
        JavaType contentType = mapper.getTypeFactory().constructType(contentClass.getName());
        TypeSerializer cts = TsFactory.getContentTypeSerializer(contentTypeSer, contentType, mapper.getSerializationConfig());
        contentTypeSer = cts;
        result = propertyBuilder.findSerializationType(member, false, declaredType);
        Assert.assertEquals(mapper.getTypeFactory().constructType(annotation.typeName()), result);

        TypeSerializer listTypeSerializer = TsFactory.listTypeSerializer(mapper.getSerializerProvider(), mapper.getTypeFactory().constructType(int[].class), mapper.getSerializerProvider().getPolyTypeMapper());
        contentTypeSer = listTypeSerializer;
        result = propertyBuilder.findSerializationType(member, false, declaredType);
        Assert.assertEquals(mapper.getTypeFactory().constructType(annotation.typeName()), result);

        TypeSerializer mapTypeSerializer = TsFactory.mapTypeSerializer(mapper.getSerializerProvider(), mapper.getTypeFactory().constructType(String.class), mapper.getTypeFactory().constructType(int.class), mapper.getSerializationConfig());
        JavaType mapType = mapper.getTypeFactory().constructType(Map.class, String.class, int.class);
        contentTypeSer = mapTypeSerializer;
        result = propertyBuilder.findSerializationType(member, false, declaredType);
        Assert.assertEquals(mapper.getTypeFactory().constructType(annotation.typeName()), result);

        TypeSerializer arrayTypeSerializer = TsFactory.arrayTypeSerializer(mapper.getSerializerProvider(), int[].class, mapper.getSerializationConfig());
        contentTypeSer = arrayTypeSerializer;
        result = propertyBuilder.findSerializationType(member, false, declaredType);
        Assert.assertEquals(mapper.getTypeFactory().constructType(annotation.typeName()), result);

        TypeSerializer beanTypeSerializer = TsFactory.beanTypeSerializer(mapper.getSerializerProvider(), clazz, mapper.getSerializationConfig());
        contentTypeSer = beanTypeSerializer;
        result = propertyBuilder.findSerializationType(member, false, declaredType);
        Assert.assertEquals(declaredType, result);

        result = propertyBuilder.findSerializationType(member, true, null);
        Assert.assertNull(result);

        result = propertyBuilder.findSerializationType(member, true, declaredType);
        Assert.assertEquals(declaredType, result);

        result = propertyBuilder.findSerializationType(null, false, declaredType);
        Assert.assertNull(result);

        result = propertyBuilder.findSerializationType(member, false, null);
        Assert.assertNull(result);

        result = propertyBuilder.findSerializationType(member, true, null);
        Assert.assertNull(result);
    }

    @Test
    public void testBuildWriter() throws JsonMappingException, IOException {
        Class<?> clazz = MyPojo.class;
        PropertyBuilder propertyBuilder = new PropertyBuilder(mapper.getSerializationConfig(), mapper.findBeanDescription(clazz));
        SerializerProvider serializerProvider = mapper.getSerializerProvider();
        BeanPropertyDefinition propDef = mapper.findBeanPropertyDefinition(clazz, "prop");
        JavaType declaredType = propDef.getType();
        JsonSerializer<?> ser = mapper.getSerializerProvider().findValueSerializer(declaredType);
        TypeSerializer typeSer = mapper.getSerializerProvider().getTypeSerializer();
        TypeSerializer contentTypeSer = mapper.getSerializerProvider().getTypeSerializer();
        AnnotatedMember am = Reflections.getDeclaredMethod(clazz, "getProp");
        Object instance = clazz.newInstance();
        AnnotationIntrospector annotationIntrospector = mapper.getSerializationConfig().getAnnotationIntrospector();

        BeanPropertyWriter result = propertyBuilder.buildWriter(serializerProvider, propDef, declaredType, ser, typeSer, contentTypeSer, am, false);
        Assert.assertNotNull(result);

        JavaType serializationType = mapper.getTypeFactory().constructType(String[].class);
        contentTypeSer = TsFactory.arrayTypeSerializer(serializerProvider, String[].class, mapper.getSerializationConfig());
        result = propertyBuilder.buildWriter(serializerProvider, propDef, declaredType, ser, typeSer, contentTypeSer, am, false);
        Assert.assertNotNull(result);

        TypeSerializer listTypeSerializer = TsFactory.listTypeSerializer(serializerProvider, String[].class, mapper.getSerializerProvider().getPolyTypeMapper());
        contentTypeSer = listTypeSerializer;
        result = propertyBuilder.buildWriter(serializerProvider, propDef, declaredType, ser, typeSer, contentTypeSer, am, false);
        Assert.assertNotNull(result);

        TypeSerializer mapTypeSerializer = TsFactory.mapTypeSerializer(serializerProvider, String.class, int.class, mapper.getSerializationConfig());
        JavaType mapType = mapper.getTypeFactory().constructType(Map.class, String.class, int.class);
        contentTypeSer = mapTypeSerializer;
        result = propertyBuilder.buildWriter(serializerProvider, propDef, declaredType, ser, typeSer, contentTypeSer, am, false);
        Assert.assertNotNull(result);

        TypeSerializer beanTypeSerializer = TsFactory.beanTypeSerializer(serializerProvider, clazz, mapper.getSerializationConfig());
        contentTypeSer = beanTypeSerializer;
        result = propertyBuilder.buildWriter(serializerProvider, propDef, declaredType, ser, typeSer, contentTypeSer, am, false);
        Assert.assertNotNull(result);

        result = propertyBuilder.buildWriter(serializerProvider, null, declaredType, null, typeSer, null, null, false);
        Assert.assertNull(result);

        result = propertyBuilder.buildWriter(serializerProvider, propDef, null, ser, typeSer, contentTypeSer, am, false);
        Assert.assertNull(result);

        result = propertyBuilder.buildWriter(null, propDef, declaredType, null, null, null, am, false);
        Assert.assertNull(result);

        result = propertyBuilder.buildWriter(serializerProvider, propDef, declaredType, ser, typeSer, contentTypeSer, null, false);
        Assert.assertNull(result);
    }

    @Test
    public void testGetClassAnnotations() {
        Class<?> clazz = MyPojo.class;
        PropertyBuilder propertyBuilder = new PropertyBuilder(mapper.getSerializationConfig(), mapper.findBeanDescription(clazz));
        Assert.assertEquals(PropertyBuilderTest.class.getAnnotation(JsonInclude.class), propertyBuilder.getClassAnnotations());
    }

    @Test
    public void testGetPropertyDefaultValue() throws JsonMappingException {
        Class<?> clazz = MyPojo.class;
        PropertyBuilder propertyBuilder = new PropertyBuilder(mapper.getSerializationConfig(), mapper.findBeanDescription(clazz));
        Object defaultBean = propertyBuilder.getDefaultBean();
        Assert.assertNull(defaultBean);

        Object defaultValue = propertyBuilder.getPropertyDefaultValue("getProp", Reflections.getDeclaredField(clazz, "defaultProp"), mapper.getTypeFactory().constructType(int.class));
        Assert.assertEquals(0, defaultValue);

        defaultValue = propertyBuilder.getPropertyDefaultValue("getProp", Reflections.getDeclaredField(clazz, "defaultProp"), mapper.getTypeFactory().constructType(String.class));
        Assert.assertEquals("", defaultValue);

        MyPojo myPojo = new MyPojo();
        myPojo.setDefaultProp(new MyInnerPojo());
        Object innerDefaultValue = propertyBuilder.getPropertyDefaultValue("getInnerProp", Reflections.getDeclaredField(clazz, "innerProp"), mapper.getTypeFactory().constructType(MyInnerPojo.class));
        Assert.assertNotNull(innerDefaultValue);
        Assert.assertEquals(myPojo.getDefaultProp(), innerDefaultValue);

        myPojo.setDefaultProp(null);
        innerDefaultValue = propertyBuilder.getPropertyDefaultValue("getInnerProp", Reflections.getDeclaredField(clazz, "innerProp"), mapper.getTypeFactory().constructType(MyInnerPojo.class));
        Assert.assertNull(innerDefaultValue);
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    static class MyPojo {
        private int prop;
        @JsonSerialize(typeName = "CustomType")
        private String prop2;
        private MyInnerPojo innerProp;
        private MyInnerPojo defaultProp;

        public void setDefaultProp(MyInnerPojo defaultProp) {
            this.defaultProp = defaultProp;
        }

        public MyInnerPojo getDefaultProp() {
            return defaultProp;
        }

        public int getProp() {
            return prop;
        }

        public String getProp2() {
            return prop2;
        }

        public MyInnerPojo getInnerProp() {
            return innerProp;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    static class MyInnerPojo {

    }

    static class PropertyBuilderTest {
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        static class MyStaticInnerPojo {

        }
    }

    static class Reflections {
        @SuppressWarnings("unchecked")
        public static <T> T getDeclaredField(Class<?> clazz, String fieldName) throws NoSuchFieldException, IllegalAccessException {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(null);
        }

        @SuppressWarnings("unchecked")
        public static <T> Method getDeclaredMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
            Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
            method.setAccessible(true);
            return method;
        }
    }
}