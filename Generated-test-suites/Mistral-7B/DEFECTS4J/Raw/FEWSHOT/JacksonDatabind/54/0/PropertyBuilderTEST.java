package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
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
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        Annotated annotated = mock(Annotated.class);
        JavaType declaredType = TypeFactory.uncheckedType(Object[].class);
        JavaType secondary = TypeFactory.uncheckedType(int[].class);

        when(annotated.getType()).thenReturn(declaredType);
        when(annotated.getRawType()).thenReturn(declaredType.getRawClass());
        when(_annotationIntrospector.refineSerializationType(any(SerializationConfig.class), eq(annotated), any(JavaType.class)))
                .thenReturn(secondary);

        JavaType result = propertyBuilder.findSerializationType(annotated, false, declaredType);

        assertEquals(int[].class, result.getRawClass());
        verify(annotated).getType();
        verify(annotated).getRawType();
        verify(_annotationIntrospector).refineSerializationType(any(SerializationConfig.class), eq(annotated), any(JavaType.class));
    }

    @Test
    public void testFindSerializationType_whenTypeIsAssignableFromDeclaredType() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        Annotated annotated = mock(Annotated.class);
        JavaType declaredType = TypeFactory.uncheckedType(Object[].class);
        JavaType secondary = TypeFactory.uncheckedType(List.class);

        when(annotated.getType()).thenReturn(declaredType);
        when(annotated.getRawType()).thenReturn(declaredType.getRawClass());
        when(_annotationIntrospector.refineSerializationType(any(SerializationConfig.class), eq(annotated), any(JavaType.class)))
                .thenReturn(secondary);
        when(secondary.isAssignableFrom(declaredType)).thenReturn(true);

        JavaType result = propertyBuilder.findSerializationType(annotated, false, declaredType);

        assertEquals(List.class, result);
        verify(annotated).getType();
        verify(annotated).getRawType();
        verify(_annotationIntrospector).refineSerializationType(any(SerializationConfig.class), eq(annotated), any(JavaType.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindSerializationType_whenTypeIsNotAssignableFromDeclaredType() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        Annotated annotated = mock(Annotated.class);
        JavaType declaredType = TypeFactory.uncheckedType(Object[].class);
        JavaType secondary = TypeFactory.uncheckedType(String[].class);

        when(annotated.getType()).thenReturn(declaredType);
        when(annotated.getRawType()).thenReturn(declaredType.getRawClass());
        when(_annotationIntrospector.refineSerializationType(any(SerializationConfig.class), eq(annotated), any(JavaType.class)))
                .thenReturn(secondary);

        propertyBuilder.findSerializationType(annotated, false, declaredType);

        fail("IllegalArgumentException expected");
    }

    @Test
    public void testFindSerializationType_whenAnnotationIsNull() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        JavaType declaredType = TypeFactory.uncheckedType(Object[].class);

        JavaType result = propertyBuilder.findSerializationType(null, false, declaredType);

        assertNull(result);
    }

    @Test
    public void testFindSerializationType_whenUseStaticTypingIsTrue() throws JsonProcessingException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        Annotated annotated = mock(Annotated.class);
        JavaType declaredType = TypeFactory.uncheckedType(Object[].class);
        JavaType secondary = TypeFactory.uncheckedType(int[].class);

        when(annotated.getType()).thenReturn(declaredType);
        when(annotated.getRawType()).thenReturn(declaredType.getRawClass());
        when(_annotationIntrospector.refineSerializationType(any(SerializationConfig.class), eq(annotated), any(JavaType.class)))
                .thenReturn(secondary);
        when(secondary.getRawClass()).thenReturn(int[].class.getDeclaredClasses()[0]);

        JavaType result = propertyBuilder.findSerializationType(annotated, true, declaredType);

        assertEquals(int[].class.getDeclaredClasses()[0], result.getRawClass());
        verify(annotated).getType();
        verify(annotated).getRawType();
        verify(_annotationIntrospector).refineSerializationType(any(SerializationConfig.class), eq(annotated), any(JavaType.class));
    }

    // Add more test cases as needed for other methods in the PropertyBuilder class

    @Test
    public void testBuildWriter() {
        // Add test case implementation here
    }

    // Add more test cases as needed for other methods in the PropertyBuilder class

    @Test
    public void testGetClassAnnotations() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        BeanDescription beanDesc = mock(BeanDescription.class);

        when(beanDesc.getClassAnnotations()).thenReturn(new Annotations());

        Annotations result = propertyBuilder.getClassAnnotations();

        assertEquals(beanDesc.getClassAnnotations(), result);
        verify(beanDesc).getClassAnnotations();
    }
}