package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Test;

public class PropertyBuilderTest {

    @Test
    public void testFindSerializationType() {
        BeanDescription beanDesc = new BeanDescription(Object.class);
        SerializationConfig config = new SerializationConfig();
        AnnotationIntrospector annotationIntrospector = Mockito.mock(AnnotationIntrospector.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        // Test with no annotations and no declared type
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        JavaType declaredType = null;
        JavaType serializationType = propertyBuilder.findSerializationType(am, false, declaredType);
        assertNull(serializationType);

        // Test with annotation that forces type to declared type
        am = Mockito.mock(AnnotatedMember.class);
        declaredType = JavaTypeFactory.unknownType();
        when(annotationIntrospector.refineSerializationType(config, am, declaredType)).thenReturn(declaredType);
        propertyBuilder = new PropertyBuilder(config, beanDesc) {
            @Override
            AnnotationIntrospector getAnnotationIntrospector() {
                return annotationIntrospector;
            }
        };
        serializationType = propertyBuilder.findSerializationType(am, true, declaredType);
        assertEquals(declaredType, serializationType);

        // Test with annotation that forces type to declared type's super type
        JavaType superType = JavaTypeFactory.unknownType().withContentType(TypeFactory.unknownType(Integer.class));
        when(annotationIntrospector.refineSerializationType(config, am, declaredType)).thenReturn(superType);
        propertyBuilder = new PropertyBuilder(config, beanDesc) {
            @Override
            AnnotationIntrospector getAnnotationIntrospector() {
                return annotationIntrospector;
            }
        };
        serializationType = propertyBuilder.findSerializationType(am, true, declaredType);
        assertEquals(superType, serializationType);

        // Test with no annotations and declared type
        declaredType = JavaTypeFactory.unknownType();
        when(annotationIntrospector.refineSerializationType(config, am, declaredType)).thenReturn(null);
        propertyBuilder = new PropertyBuilder(config, beanDesc) {
            @Override
            AnnotationIntrospector getAnnotationIntrospector() {
                return annotationIntrospector;
            }
        };
        serializationType = propertyBuilder.findSerializationType(am, true, declaredType);
        assertNull(serializationType);
    }

    @Test
    public void testGetDefaultBean() {
        BeanDescription beanDesc = new BeanDescription(Object.class);
        SerializationConfig config = new SerializationConfig();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        // Test with null defaultBean
        Object defaultBean = null;
        Object result = propertyBuilder.getDefaultBean();
        assertNull(result);

        // Test with instantiated defaultBean
        Object defaultInstance = new Object();
        propertyBuilder._defaultBean = defaultInstance;
        result = propertyBuilder.getDefaultBean();
        assertNotNull(result);
        assertEquals(defaultInstance, result);
    }

    @Test
    public void testGetPropertyDefaultValue() {
        BeanDescription beanDesc = new BeanDescription(Object.class);
        SerializationConfig config = new SerializationConfig();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        // Test with null defaultBean
        Object defaultBean = null;
        Object result = propertyBuilder.getPropertyDefaultValue("propertyName", Mockito.mock(AnnotatedMember.class), JavaTypeFactory.unknownType());
        assertNull(result);

        // Test with instantiated defaultBean and gettable default value
        Object defaultInstance = new Object();
        propertyBuilder._defaultBean = defaultInstance;
        AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
        when(member.getValue(defaultInstance)).thenReturn("defaultValue");
        result = propertyBuilder.getPropertyDefaultValue("propertyName", member, JavaTypeFactory.textType());
        assertEquals("defaultValue", result);

        // Test with instantiated defaultBean and non-gettable default value
        Object nonGettableDefaultInstance = new Object();
        propertyBuilder._defaultBean = nonGettableDefaultInstance;
        when(member.getValue(nonGettableDefaultInstance)).thenThrow(new IllegalAccessException());
        result = propertyBuilder.getPropertyDefaultValue("propertyName", member, JavaTypeFactory.textType());
        assertNull(result);
    }

    @Test
    public void testGetDefaultValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);

        // Test with primitive type
        Class<?> primitiveType = int.class;
        Object result = propertyBuilder.getDefaultValue(JavaTypeFactory.constructType(primitiveType));
        assertEquals(0, result);

        // Test with String type
        result = propertyBuilder.getDefaultValue(JavaTypeFactory.textType());
        assertEquals("", result);

        // Test with container type
        JavaType containerType = JavaTypeFactory.constructCollectionType(ArrayList.class, String.class);
        result = propertyBuilder.getDefaultValue(containerType);
        assertEquals(JsonInclude.Include.NON_EMPTY, result);

        // Test with reference type
        Class<?> referenceType = Object.class;
        result = propertyBuilder.getDefaultValue(JavaTypeFactory.constructType(referenceType));
        assertNull(result);
    }
}