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
    public void testFindSerializationType() {
        BeanDescription beanDesc = new BeanDescription();
        SerializationConfig config = new SerializationConfig();
        AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector();

        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        AnnotatedMember am = new AnnotatedMember(new Object(), "propertyName");

        // Test when using annotations
        JavaType javaType = TypeFactory.type(int[].class, String.class);
        JsonSerialize.Typing annotationTyping = new JsonSerialize.Typing() {
            @Override
            public JsonSerialize.Typing forProperty(BeanPropertyDefinition propDef) {
                return JsonSerialize.Typing.STATIC;
            }
        };
        AnnotationIntrospector.MarkedAnnotation ann = new AnnotationIntrospector.MarkedAnnotation(annotationTyping, null);
        am.setValue(ann);
        JavaType expectedType = javaType.withStaticTyping();
        JavaType actualType = propertyBuilder.findSerializationType(am, true, javaType);
        Assert.assertEquals(expectedType, actualType);

        // Test when not using annotations
        am.setValue(null);
        actualType = propertyBuilder.findSerializationType(am, true, javaType);
        Assert.assertNull(actualType);
    }

    @Test
    public void testGetDefaultBean() {
        BeanDescription beanDesc = new BeanDescription();
        SerializationConfig config = new SerializationConfig();
        AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector();

        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        Object defaultBean = propertyBuilder.getDefaultBean();
        Assert.assertNull(defaultBean);

        beanDesc.setDefaultInstance(new Object());
        propertyBuilder = new PropertyBuilder(config, beanDesc);
        defaultBean = propertyBuilder.getDefaultBean();
        Assert.assertNotNull(defaultBean);
        Assert.assertEquals(beanDesc.getDefaultInstance().getClass(), defaultBean.getClass());
    }

    @Test
    public void testgetPropertyDefaultValue() {
        BeanDescription beanDesc = new BeanDescription();
        SerializationConfig config = new SerializationConfig();
        AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector();

        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        String propName = "propertyName";
        AnnotatedMember am = new AnnotatedMember(new Object(), propName);
        JavaType javaType = TypeFactory.type(String.class);

        // Test when defaultBean is null
        Assert.assertNull(propertyBuilder.getPropertyDefaultValue(propName, am, javaType));

        // Test when defaultBean is not null
        Object defaultBean = new Object();
        beanDesc.setDefaultInstance(defaultBean);
        propertyBuilder = new PropertyBuilder(config, beanDesc);
        Assert.assertEquals(am.getValue(defaultBean), propertyBuilder.getPropertyDefaultValue(propName, am, javaType));
    }

    @Test
    public void testGetDefaultValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);

        Assert.assertNull(propertyBuilder.getDefaultValue(null));
        Assert.assertNull(propertyBuilder.getDefaultValue(TypeFactory.type(int.class)));
        Assert.assertEquals("", propertyBuilder.getDefaultValue(TypeFactory.type(String.class)));
        Assert.assertNull(propertyBuilder.getDefaultValue(TypeFactory.type(Object.class)));
        Assert.assertNull(propertyBuilder.getDefaultValue(TypeFactory.type(List.class)));
        Assert.assertNull(propertyBuilder.getDefaultValue(TypeFactory.type(Map.class)));
    }

    @Test
    public void testBuildWriter() {
        BeanDescription beanDesc = new BeanDescription();
        SerializationConfig config = new SerializationConfig();
        AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector();

        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        SerializerProvider prov = new SerializerProvider();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition("propertyName", TypeFactory.type(int[].class));
        JavaType declaredType = TypeFactory.type(String[].class);
        JsonSerializer<?> ser = new JsonSerializer<String>() {};
        TypeSerializer typeSer = new TypeSerializer() {};
        TypeSerializer contentTypeSer = new TypeSerializer() {};

        // Test when serializationType is null
        BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, null, am, false);
        Assert.assertEquals(declaredType, bpw.getType());
        Assert.assertEquals(ser, bpw.getSerializer());
        Assert.assertNull(bpw.getTypeSerializer());

        // Test when contentTypeSer is not null
        JavaType contentType = TypeFactory.type(int.class);
        TypeSerializer contentTypeSerWithHandler = new TypeSerializer() {
            @Override
            public void serialize(SerializerProvider p, TypeSerializer s, Object value, JsonGenerator g, Serializers.SerializerProvider serializers) throws IOException {
                // Implement serialization logic here
            }
        }.withContentTypeHandler(contentTypeSer);
        bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, null, contentTypeSerWithHandler, am, false);
        Assert.assertEquals(declaredType.getContentType(), bpw.getType().getContentType());
        Assert.assertEquals(contentTypeSerWithHandler, bpw.getTypeSerializer());

        // Test when am is null
        bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSerWithHandler, null, false);
        Assert.fail("Expected IllegalArgumentException but got no exception");
    }
}