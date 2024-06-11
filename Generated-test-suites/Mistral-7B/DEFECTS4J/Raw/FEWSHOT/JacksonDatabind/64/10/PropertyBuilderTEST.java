package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Assert;
import org.junit.Test;

public class PropertyBuilderTest {

    @Test
    public void testFindSerializationType() {
        BeanDescription beanDesc = new BeanDescription();
        Annotated a = new Annotated("", null, null, null, null, null);
        JavaType declaredType = TypeFactory.unresolvedSerializedFromString("java.lang.String");

        PropertyBuilder propertyBuilder = new PropertyBuilder(null, beanDesc);
        JavaType result = propertyBuilder.findSerializationType(a, false, declaredType);
        Assert.assertEquals(declaredType, result);

        AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector() {};
        a.setAnnotationIntrospector(annotationIntrospector);
        JavaType secondary = TypeFactory.unresolvedSerializedFromString("java.lang.Integer");
        propertyBuilder = new PropertyBuilder(null, beanDesc);
        result = propertyBuilder.findSerializationType(a, false, declaredType);
        Assert.assertEquals(secondary, result);

        propertyBuilder = new PropertyBuilder(null, beanDesc);
        result = propertyBuilder.findSerializationType(a, true, declaredType);
        Assert.assertTrue(result.isStaticTyping());

        AnnotatedMember am = new AnnotatedMember("name", null, null, null, null, null) {};
        result = propertyBuilder.findSerializationType(am, true, declaredType);
        Assert.assertEquals(declaredType, result);
    }

    @Test
    public void testGetClassAnnotations() {
        BeanDescription beanDesc = new BeanDescription();
        beanDesc.addClassAnnotations(new Annotation[] {});

        PropertyBuilder propertyBuilder = new PropertyBuilder(null, beanDesc);
        Annotations result = propertyBuilder.getClassAnnotations();
        Assert.assertEquals(beanDesc.getClassAnnotations(), result);
    }

    @Test
    public void testBuildWriter() throws JsonMappingException {
        BeanDescription beanDesc = new BeanDescription();
        SerializationConfig config = new SerializationConfig();
        JavaType declaredType = TypeFactory.unresolvedSerializedFromString("java.lang.String");
        BeanPropertyDefinition propDef = new BeanPropertyDefinition("name", null, null, null, null, null, null);
        JsonSerializer<?> ser = new JsonSerializer<Object>() {};
        TypeSerializer typeSer = new TypeSerializer() {};
        TypeSerializer contentTypeSer = new TypeSerializer() {};

        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        BeanPropertyWriter result = propertyBuilder.buildWriter(null, propDef, declaredType, ser, typeSer, contentTypeSer, null, false);

        Assert.assertNotNull(result);
        Assert.assertEquals(propDef, result.getPropertyDefinition());
        Assert.assertEquals(ser, result.getSerializer());
        Assert.assertEquals(typeSer, result.getTypeSerializer());
        Assert.assertEquals(contentTypeSer, result.getContentTypeSerializer());
        Assert.assertFalse(result.suppressNulls());
        Assert.assertNull(result.getValueToSuppress());
    }

    @Test
    public void testFindSerializationTypeWithNullDeclaredType() {
        BeanDescription beanDesc = new BeanDescription();
        Annotated a = new Annotated("", null, null, null, null, null);

        PropertyBuilder propertyBuilder = new PropertyBuilder(null, beanDesc);
        JavaType result = propertyBuilder.findSerializationType(a, false, null);
        Assert.assertNull(result);
    }

    @Test
    public void testGetPropertyDefaultValue() throws Exception {
        BeanDescription beanDesc = new BeanDescription();
        AnnotatedMember member = new AnnotatedMember("name", null, null, null, null, null) {};
        JavaType type = TypeFactory.unresolvedSerializedFromString("java.lang.String");

        PropertyBuilder propertyBuilder = new PropertyBuilder(null, beanDesc);
        Object result = propertyBuilder.getPropertyDefaultValue("name", member, type);
        Assert.assertEquals("", result);

        Object defaultBean = new Object() {};
        propertyBuilder.setDefaultBean(defaultBean);
        result = propertyBuilder.getPropertyDefaultValue("name", member, type);
        Assert.assertEquals(member.getValue(defaultBean), result);
    }

    @Test
    public void testGetDefaultValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        Object result = propertyBuilder.getDefaultValue(TypeFactory.unresolvedSerializedFromString("java.lang.Integer"));
        Assert.assertEquals(Integer.valueOf(0), result);

        result = propertyBuilder.getDefaultValue(TypeFactory.unresolvedSerializedFromString("java.lang.String"));
        Assert.assertEquals("", result);

        result = propertyBuilder.getDefaultValue(TypeFactory.unresolvedSerializedFromString("java.util.List<java.lang.String>"));
        Assert.assertEquals(JsonInclude.Include.NON_EMPTY, result);

        result = propertyBuilder.getDefaultValue(TypeFactory.unresolvedSerializedFromString("java.util.Map<java.lang.String, java.lang.Integer>"));
        Assert.assertEquals(JsonInclude.Include.NON_EMPTY, result);

        result = propertyBuilder.getDefaultValue(TypeFactory.unresolvedSerializedFromString("java.lang.Object"));
        Assert.assertNull(result);
    }
}