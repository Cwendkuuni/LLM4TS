package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Assert;
import org.junit.Test;

public class PropertyBuilderTest {

    @Test
    public void testDefaultPropertyValue() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(MyClass.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        Object defaultBean = propertyBuilder.getDefaultBean();
        Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue("propertyName", null, MyClass.class.getDeclaredField("propertyName").getJavaType());

        Assert.assertNotNull(defaultBean);
        Assert.assertNull(propertyDefaultValue);

        // Test with a default constructor and a getter method
        MyClass defaultInstance = new MyClass();
        propertyBuilder = new PropertyBuilder(config, beanDesc);
        propertyBuilder._defaultBean = defaultInstance;
        Object getterDefaultValue = propertyBuilder.getPropertyDefaultValue("propertyName", null, MyClass.class.getDeclaredField("propertyName").getJavaType());
        Assert.assertEquals(defaultInstance.getPropertyName(), getterDefaultValue);
    }

    @Test
    public void testFindSerializationType() {
        // Test with no annotations and no declared type
        AnnotatedMember am = new AnnotatedMember(MyClass.class.getDeclaredField("propertyName"), null);
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        JavaType declaredType = MyClass.class.getDeclaredField("propertyName").getJavaType();
        JavaType serializationType = propertyBuilder.findSerializationType(am, false, declaredType);
        Assert.assertNull(serializationType);

        // Test with a JsonTypeId annotation
        am = new AnnotatedMember(MyClass.class.getDeclaredField("propertyNameWithTypeId"), new JsonTypeId());
        declaredType = MyClass.class.getDeclaredField("propertyNameWithTypeId").getJavaType();
        propertyBuilder = new PropertyBuilder(null, null);
        serializationType = propertyBuilder.findSerializationType(am, false, declaredType);
        Assert.assertNotNull(serializationType);

        // Test with a JsonSerialize annotation
        am = new AnnotatedMember(MyClass.class.getDeclaredField("propertyNameWithSerializer"), new JsonSerialize());
        declaredType = MyClass.class.getDeclaredField("propertyNameWithSerializer").getJavaType();
        propertyBuilder = new PropertyBuilder(null, null);
        serializationType = propertyBuilder.findSerializationType(am, false, declaredType);
        Assert.assertNotNull(serializationType);
    }

    @Test
    public void testBuildWriter() throws JsonMappingException {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(MyClass.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        SerializerProvider prov = new SerializerProvider();
        prov.setConfig(config);

        BeanPropertyDefinition propDef = new BeanPropertyDefinition("propertyName", MyClass.class.getDeclaredField("propertyName"), MyClass.class, null, null);
        JavaType declaredType = MyClass.class.getDeclaredField("propertyName").getJavaType();
        JsonSerializer<?> ser = new JsonSerializer<Object>() {};
        TypeSerializer typeSer = TypeSerializers.empty();
        TypeSerializer contentTypeSer = TypeSerializers.empty();
        AnnotatedMember am = new AnnotatedMember(MyClass.class.getDeclaredField("propertyName"), null);
        boolean defaultUseStaticTyping = false;

        BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
        Assert.assertNotNull(bpw);
    }

    @Test
    public void testFindSerializationTypeWithOverrides() throws JsonMappingException {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(MyClass.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        AnnotatedMember am = new AnnotatedMember(MyClass.class.getDeclaredField("propertyNameWithOverride"), new JsonSerialize(MyClassWithOverride.class));
        JavaType declaredType = MyClass.class.getDeclaredField("propertyNameWithOverride").getJavaType();
        JavaType serializationType = propertyBuilder.findSerializationType(am, false, declaredType);
        Assert.assertEquals(MyClassWithOverride.class, serializationType.getRawClass());
    }

    @Test
    public void testGetDefaultBean() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(MyClass.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        Object defaultBean = propertyBuilder.getDefaultBean();
        Assert.assertNull(defaultBean);

        propertyBuilder._defaultBean = new MyClass();
        defaultBean = propertyBuilder.getDefaultBean();
        Assert.assertNotNull(defaultBean);
        Assert.assertEquals(MyClass.class, defaultBean.getClass());
    }

    @Test
    public void testFindNullSerializer() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(MyClass.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        AnnotatedMember am = new AnnotatedMember(MyClass.class.getDeclaredField("propertyWithNullSerializer"), null);

        JsonSerializer<?> serDef = propertyBuilder.findNullSerializer(am);
        Assert.assertNotNull(serDef);
    }

    @Test
    public void testFindUnwrappingNameTransformer() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(MyClass.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        AnnotatedMember am = new AnnotatedMember(MyClass.class.getDeclaredField("propertyWithUnwrapper"), null);

        NameTransformer unwrapper = propertyBuilder.findUnwrappingNameTransformer(am);
        Assert.assertNotNull(unwrapper);
    }

    static class MyClass {
        private String propertyName;
        private String propertyNameWithTypeId = "@type";
        @JsonTypeId
        private Class<? extends MyClassWithTypeId> propertyNameWithTypeIdAnnotation;
        private String propertyNameWithSerializer;
        @JsonSerialize(using = MyCustomSerializer.class)
        private String propertyNameWithSerializerAnnotation;
        private MyClassWithOverride propertyNameWithOverride;

        public String getPropertyName() {
            return propertyName;
        }

        public void setPropertyName(String propertyName) {
            this.propertyName = propertyName;
        }
    }

    static class MyClassWithTypeId extends MyClass {
        // Empty class
    }

    static class MyCustomSerializer extends JsonSerializer<Object> {
        // Empty class
    }
}