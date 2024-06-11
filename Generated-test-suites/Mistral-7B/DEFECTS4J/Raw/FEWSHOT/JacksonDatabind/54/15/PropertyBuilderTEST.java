package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

    @Test
    public void testFindSerializationType() {
        BeanDescription beanDesc = new BeanDescription(Object.class);
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(SerializationConfig.builder().build(), beanDesc);

        // Test with no annotations and declared type
        JavaType declaredType = Mockito.mock(JavaType.class);
        JavaType expectedType = Mockito.mock(JavaType.class);
        JavaType actualType = propertyBuilder.findSerializationType(am, false, declaredType);
        assertEquals(expectedType, actualType);

        // Test with annotations and declared type
        JsonSerialize jsonSerialize = Mockito.mock(JsonSerialize.class);
        JsonInclude jsonInclude = Mockito.mock(JsonInclude.class);
        JsonInclude.Include inclusion = JsonInclude.Include.NON_DEFAULT;
        when(jsonInclude.getValueInclusion()).thenReturn(inclusion);
        when(am.getAnnotation(JsonSerialize.class)).thenReturn(jsonSerialize);
        when(jsonSerialize.type()).thenReturn(JsonSerialize.Typing.STATIC);

        expectedType = Mockito.mock(JavaType.class).withContentTypeHandler(Mockito.mock(TypeSerializer.class));
        when(declaredType.getContentType()).thenReturn(expectedType);
        when(jsonInclude.isEnabled(inclusion, propertyBuilder._config)).thenReturn(true);

        actualType = propertyBuilder.findSerializationType(am, true, declaredType);
        assertEquals(expectedType, actualType);

        // Test with annotations and no declared type
        when(declaredType.getContentType()).thenReturn(null);
        when(jsonInclude.isEnabled(inclusion, propertyBuilder._config)).thenReturn(false);

        actualType = propertyBuilder.findSerializationType(am, true, declaredType);
        assertNull(actualType);

        // Test with no annotations and super type
        Class<?> superClass = Object[].class;
        when(declaredType.getRawClass()).thenReturn(superClass);
        JavaType superType = Mockito.mock(JavaType.class).withContentTypeHandler(Mockito.mock(TypeSerializer.class));
        when(declaredType.withContentTypeHandler(Mockito.mock(TypeSerializer.class))).thenReturn(superType);

        actualType = propertyBuilder.findSerializationType(am, false, declaredType);
        assertEquals(superType, actualType);

        // Test with annotations and super type
        when(jsonInclude.isEnabled(inclusion, propertyBuilder._config)).thenReturn(true);

        actualType = propertyBuilder.findSerializationType(am, true, declaredType);
        assertEquals(superType, actualType);

        // Test with annotations and no super type
        when(declaredType.getContentType()).thenReturn(null);
        when(jsonInclude.isEnabled(inclusion, propertyBuilder._config)).thenReturn(false);

        actualType = propertyBuilder.findSerializationType(am, true, declaredType);
        assertNull(actualType);
    }

    @Test
    public void testGetClassAnnotations() {
        BeanDescription beanDesc = new BeanDescription(Object.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(SerializationConfig.builder().build(), beanDesc);

        Annotations expectedAnnotations = Mockito.mock(Annotations.class);
        when(beanDesc.getClassAnnotations()).thenReturn(expectedAnnotations);

        Annotations actualAnnotations = propertyBuilder.getClassAnnotations();
        assertEquals(expectedAnnotations, actualAnnotations);
    }

    @Test
    public void testGetPropertyDefaultValue() {
        BeanDescription beanDesc = new BeanDescription(Object.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(SerializationConfig.builder().build(), beanDesc);

        String name = "name";
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        JavaType type = Mockito.mock(JavaType.class);

        Object defaultBean = Mockito.mock(Object.class);
        Object expectedDefaultValue = "default value";
        when(defaultBean.toString()).thenReturn(expectedDefaultValue);
        when(am.getValue(defaultBean)).thenReturn(expectedDefaultValue);

        Object actualDefaultValue = propertyBuilder.getPropertyDefaultValue(name, am, type);
        assertEquals(expectedDefaultValue, actualDefaultValue);

        // Test with null value and null default bean
        when(am.getValue(defaultBean)).thenReturn(null);

        actualDefaultValue = propertyBuilder.getPropertyDefaultValue(name, am, type);
        assertNull(actualDefaultValue);

        // Test with null value and non-null default bean
        when(am.getValue(defaultBean)).thenReturn(123);

        actualDefaultValue = propertyBuilder.getPropertyDefaultValue(name, am, type);
        assertEquals(123, actualDefaultValue);

        // Test with non-null value and null default bean
        when(am.getValue(defaultBean)).thenReturn("non-null value");

        actualDefaultValue = propertyBuilder.getPropertyDefaultValue(name, am, type);
        assertNull(actualDefaultValue);
    }

    @Test
    public void testGetDefaultValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(SerializationConfig.builder().build(null), null);

        // Test with primitive type
        Class<?> intType = int.class;
        Class<?> integerType = Integer.class;
        Object expectedDefaultValue = 0;

        Object actualDefaultValue = propertyBuilder.getDefaultValue(JavaTypeFactory.instance.constructSimpleType(intType, null));
        assertEquals(expectedDefaultValue, actualDefaultValue);

        actualDefaultValue = propertyBuilder.getDefaultValue(JavaTypeFactory.instance.constructSimpleType(integerType, null));
        assertEquals(expectedDefaultValue, actualDefaultValue);

        // Test with string type
        String emptyString = "";
        Object expectedEmptyString = emptyString;

        actualDefaultValue = propertyBuilder.getDefaultValue(JavaTypeFactory.instance.constructType(String.class));
        assertEquals(expectedEmptyString, actualDefaultValue);

        // Test with array type
        Class<?> arrayType = int[].class;
        Object expectedEmptyArray = new Object[0];

        actualDefaultValue = propertyBuilder.getDefaultValue(JavaTypeFactory.instance.constructType(arrayType));
        assertEquals(expectedEmptyArray, actualDefaultValue);

        // Test with container type
        JavaType javaListType = JavaTypeFactory.instance.constructType(List.class, int.class);
        Object expectedEmptyList = new ArrayList<>();

        actualDefaultValue = propertyBuilder.getDefaultValue(javaListType);
        assertEquals(expectedEmptyList, actualDefaultValue);

        // Test with reference type
        Class<?> stringClass = String.class;
        Object expectedNull = null;

        actualDefaultValue = propertyBuilder.getDefaultValue(JavaTypeFactory.instance.constructType(stringClass));
        assertEquals(expectedNull, actualDefaultValue);
    }
}