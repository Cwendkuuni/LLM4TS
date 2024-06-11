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
        BeanDescription beanDesc = new BeanDescription(null);
        SerializationConfig config = new SerializationConfig();
        AnnotationIntrospector annotationIntrospector = Mockito.mock(AnnotationIntrospector.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        JavaType declaredType = TypeFactory.defaultInstance().constructType(String.class);
        Annotated a = Mockito.mock(Annotated.class);

        // Test when no annotations are present and static typing is enabled
        JavaType result = propertyBuilder.findSerializationType(a, true, declaredType);
        Assert.assertEquals(declaredType, result);

        // Test when annotations are present and static typing is enabled
        JavaType annotatedType = TypeFactory.defaultInstance().constructType(Integer.class);
        when(a.getType()).thenReturn(annotatedType);
        when(annotationIntrospector.refineSerializationType(config, a, declaredType)).thenReturn(annotatedType);
        result = propertyBuilder.findSerializationType(a, true, declaredType);
        Assert.assertEquals(annotatedType, result);

        // Test when annotations are present and static typing is disabled
        result = propertyBuilder.findSerializationType(a, false, declaredType);
        Assert.assertNull(result);
    }

    @Test
    public void testGetDefaultBean() {
        BeanDescription beanDesc = new BeanDescription(null);
        SerializationConfig config = new SerializationConfig();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        Object defaultBean = propertyBuilder.getDefaultBean();
        Assert.assertNull(defaultBean);

        propertyBuilder = new PropertyBuilder(config, beanDesc) {
            @Override
            protected Object getDefaultBean() {
                return new Object() {
                };
            }
        };

        defaultBean = propertyBuilder.getDefaultBean();
        Assert.assertNotNull(defaultBean);
    }

    @Test
    public void testGetPropertyDefaultValue() {
        BeanDescription beanDesc = new BeanDescription(null);
        SerializationConfig config = new SerializationConfig();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
        String name = "name";

        Object defaultBean = new Object() {
            int value = 1;
        };
        propertyBuilder._defaultBean = defaultBean;

        JavaType type = TypeFactory.defaultInstance().constructType(Integer.class);
        Object result = propertyBuilder.getPropertyDefaultValue(name, member, type);
        Assert.assertEquals(1, result);

        type = TypeFactory.defaultInstance().constructType(String.class);
        result = propertyBuilder.getPropertyDefaultValue(name, member, type);
        Assert.assertEquals("", result);

        type = TypeFactory.defaultInstance().constructType(Map.class);
        result = propertyBuilder.getPropertyDefaultValue(name, member, type);
        Assert.assertEquals(JsonInclude.Include.NON_EMPTY, result);

        type = TypeFactory.defaultInstance().constructType(List.class);
        result = propertyBuilder.getPropertyDefaultValue(name, member, type);
        Assert.assertEquals(JsonInclude.Include.NON_EMPTY, result);

        type = TypeFactory.defaultInstance().constructType(MyClass.class);
        Assert.assertNull(propertyBuilder.getPropertyDefaultValue(name, member, type));
    }

    @Test
    public void testGetDefaultValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);

        Class<?> cls = String.class;
        Object result = propertyBuilder.getDefaultValue(TypeFactory.defaultInstance().constructType(cls));
        Assert.assertEquals("", result);

        cls = Integer.class;
        result = propertyBuilder.getDefaultValue(TypeFactory.defaultInstance().constructType(cls));
        Assert.assertEquals(0, result);

        cls = MyClass.class;
        result = propertyBuilder.getDefaultValue(TypeFactory.defaultInstance().constructType(cls));
        Assert.assertNull(result);
    }

    static class MyClass {
    }
}