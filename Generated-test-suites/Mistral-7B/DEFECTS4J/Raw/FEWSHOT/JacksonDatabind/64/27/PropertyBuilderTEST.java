package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;

/**
 * Unit tests for {@link PropertyBuilder} class.
 */
public class PropertyBuilderTest {

    @Test
    public void testFindSerializationType() {
        // Test with no annotations and no declared type
        Annotated a = Mockito.mock(Annotated.class);
        JavaType declaredType = TypeFactory.unknownType();
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        JsonType _ = propertyBuilder.findSerializationType(a, false, declaredType);
        assertNull(_);

        // Test with annotation but no declared type
        Annotated annotated = Mockito.mock(Annotated.class);
        AnnotationIntrospector introspector = Mockito.mock(AnnotationIntrospector.class);
        when(introspector.refineSerializationType(null, annotated, TypeFactory.unknownType()))
                .thenReturn(TypeFactory.unknownType());
        when(introspector.findSerializationTyping(annotated)).thenReturn(JsonSerialize.Typing.STATIC);

        propertyBuilder = new PropertyBuilder(null, null);
        propertyBuilder._annotationIntrospector = introspector;
        JavaType serializationType = propertyBuilder.findSerializationType(annotated, true, declaredType);
        assertEquals(TypeFactory.unknownType(), serializationType);

        // Test with both annotation and declared type
        Annotated annotatedWithType = Mockito.mock(Annotated.class);
        when(introspector.refineSerializationType(null, annotatedWithType, TypeFactory.unknownType()))
                .thenReturn(TypeFactory.type(new Class[] { String.class, Integer.TYPE }, null));
        JavaType declaredTypeWithAnnotation = TypeFactory.type(new Class[] { String.class, Integer.TYPE }, null);

        propertyBuilder = new PropertyBuilder(null, null);
        propertyBuilder._annotationIntrospector = introspector;
        JavaType serializationTypeWithAnnotation = propertyBuilder.findSerializationType(annotatedWithType, true, declaredTypeWithAnnotation);
        assertEquals(TypeFactory.type(new Class[] { String.class, Integer.TYPE }, null), serializationTypeWithAnnotation);

        // Test with annotation but no declared type and static typing disabled
        Annotated annotatedWithoutType = Mockito.mock(Annotated.class);
        when(introspector.refineSerializationType(null, annotatedWithoutType, TypeFactory.unknownType()))
                .thenReturn(TypeFactory.unknownType());
        when(introspector.findSerializationTyping(annotatedWithoutType)).thenReturn(JsonSerialize.Typing.DEFAULT_TYPING);

        propertyBuilder = new PropertyBuilder(null, null);
        propertyBuilder._annotationIntrospector = introspector;
        JavaType serializationTypeWithoutType = propertyBuilder.findSerializationType(annotatedWithoutType, false, declaredType);
        assertNull(serializationTypeWithoutType);
    }

    @Test
    public void testGetPropertyDefaultValue() {
        // Test with default bean
        BeanDescription beanDesc = Mockito.mock(BeanDescription.class);
        Object defaultBean = new Object() {
            // Empty constructor
        };
        when(beanDesc.instantiateBean(true)).thenReturn(defaultBean);

        PropertyBuilder propertyBuilder = new PropertyBuilder(null, beanDesc);
        Object defaultValue = propertyBuilder.getPropertyDefaultValue("test", null, TypeFactory.unknownType());
        assertNull(defaultValue);

        // Test with default bean and getter returns null
        when(beanDesc.instantiateBean(true)).thenReturn(defaultBean);
        when(beanDesc.getClassAnnotations().findAnnotation(JsonSerialize.class))
                .thenReturn(new JsonSerializer.Value() {
                    @Override
                    public Object getPropertyValue(Object bean) {
                        return null;
                    }
                });

        propertyBuilder = new PropertyBuilder(null, beanDesc);
        Object defaultValueWithNullGetter = propertyBuilder.getPropertyDefaultValue("test", null, TypeFactory.unknownType());
        assertNull(defaultValueWithNullGetter);

        // Test with default bean and getter returns non-null value
        when(beanDesc.getClassAnnotations().findAnnotation(JsonSerialize.class))
                .thenReturn(new JsonSerializer.Value() {
                    @Override
                    public Object getPropertyValue(Object bean) {
                        return "test";
                    }
                });

        propertyBuilder = new PropertyBuilder(null, beanDesc);
        Object defaultValueWithNonNullGetter = propertyBuilder.getPropertyDefaultValue("test", null, TypeFactory.unknownType());
        assertEquals("test", defaultValueWithNonNullGetter);

        // Test with null bean
        when(beanDesc.instantiateBean(true)).thenReturn(null);
        Object defaultValueWithNullBean = propertyBuilder.getPropertyDefaultValue("test", null, TypeFactory.unknownType());
        assertNull(defaultValueWithNullBean);

        // Test with non-default bean
        Object nonDefaultBean = new Object() {
            private String test = "test";

            public String getTest() {
                return test;
            }

            // Empty constructor
        };
        when(beanDesc.instantiateBean(true)).thenReturn(nonDefaultBean);

        propertyBuilder = new PropertyBuilder(null, beanDesc);
        Object defaultValueWithNonDefaultBean = propertyBuilder.getPropertyDefaultValue("test", null, TypeFactory.unknownType());
        assertEquals("test", defaultValueWithNonDefaultBean);

        // Test with unknown property name
        propertyBuilder = new PropertyBuilder(null, beanDesc);
        Object defaultValueWithUnknownPropertyName = propertyBuilder.getPropertyDefaultValue("unknown", null, TypeFactory.unknownType());
        assertNull(defaultValueWithUnknownPropertyName);

        // Test with non-existent bean
        when(beanDesc.instantiateBean(true)).thenThrow(new IllegalArgumentException());
        Object defaultValueWithNonExistentBean = propertyBuilder.getPropertyDefaultValue("test", null, TypeFactory.unknownType());
        assertNull(defaultValueWithNonExistentBean);

        // Test with no default bean
        PropertyBuilder propertyBuilderNoDefaultBean = new PropertyBuilder(null, beanDesc);
        Object defaultValueWithNoDefaultBean = propertyBuilderNoDefaultBean.getPropertyDefaultValue("test", null, TypeFactory.unknownType());
        assertNull(defaultValueWithNoDefaultBean);
    }

    @Test
    public void testGetDefaultValue() {
        // Test with primitive type
        Class<?> primitiveType = int.class;
        Object defaultValuePrimitive = PropertyBuilder.getClassUtil().defaultValue(primitiveType);
        assertEquals(0, defaultValuePrimitive);

        // Test with wrapper type
        Class<?> wrapperType = Integer.class;
        Object defaultValueWrapper = PropertyBuilder.getClassUtil().defaultValue(wrapperType);
        assertEquals(0, defaultValueWrapper);

        // Test with String type
        Class<?> stringType = String.class;
        Object defaultValueString = PropertyBuilder.getClassUtil().defaultValue(stringType);
        assertEquals("", defaultValueString);

        // Test with array type
        Class<?> arrayType = int[].class;
        Object defaultValueArray = PropertyBuilder.getClassUtil().defaultValue(arrayType);
        assertArrayEquals(new int[0], (int[]) defaultValueArray);

        // Test with reference type
        Class<?> referenceType = String.class;
        Object defaultValueReference = PropertyBuilder.getClassUtil().defaultValue(referenceType);
        assertEquals(JsonInclude.Include.NON_DEFAULT, defaultValueReference);

        // Test with map type
        Class<?> mapType = Map.class;
        Object defaultValueMap = PropertyBuilder.getClassUtil().defaultValue(mapType);
        assertEquals(JsonInclude.Include.NON_DEFAULT, defaultValueMap);

        // Test with collection type
        Class<?> collectionType = List.class;
        Object defaultValueCollection = PropertyBuilder.getClassUtil().defaultValue(collectionType);
        assertEquals(JsonInclude.Include.NON_DEFAULT, defaultValueCollection);
    }
}