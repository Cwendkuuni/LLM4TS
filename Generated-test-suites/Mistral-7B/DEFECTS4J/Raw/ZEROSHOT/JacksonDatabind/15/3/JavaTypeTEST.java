package com.example.junit;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import org.junit.Test;

import static org.junit.Assert.*;

public class JavaTypeTest {

    @Test
    public void testJavaTypeInstance() {
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaType = typeFactory.constructType(String.class);
        assertNotNull(javaType);
        assertEquals(String.class, javaType.getRawClass());
        assertTrue(javaType.isConcrete());
        assertFalse(javaType.isArrayType());
        assertFalse(javaType.isEnumType());
        assertFalse(javaType.isInterface());
        assertFalse(javaType.isPrimitive());
        assertFalse(javaType.isFinal());
        assertFalse(javaType.isThrowable());
        assertFalse(javaType.isContainerType());
        assertFalse(javaType.isCollectionLikeType());
        assertFalse(javaType.isMapLikeType());
        assertFalse(javaType.useStaticType());
        assertEquals(0, javaType.containedTypeCount());
        assertNull(javaType.getKeyType());
        assertNull(javaType.getContentType());
        assertEquals(null, javaType.getParameterSource());

        // Test methods for handling handlers
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        JavaType javaTypeWithHandlers = javaType.withValueHandler(valueHandler).withTypeHandler(typeHandler);
        assertEquals(valueHandler, javaTypeWithHandlers.getValueHandler());
        assertEquals(typeHandler, javaTypeWithHandlers.getTypeHandler());

        // Test methods for handling subclassing
        Class<?> subclass = Integer.class;
        JavaType narrowedType = javaType.narrowBy(subclass);
        assertEquals(subclass, narrowedType.getRawClass());
        assertEquals(javaType, narrowedType.withValueHandler(javaType.getValueHandler()).withTypeHandler(javaType.getTypeHandler()));

        // Test methods for handling superclassing
        Class<?> superclass = Object.class;
        JavaType widenedType = javaType.widenBy(superclass);
        assertEquals(superclass, widenedType.getRawClass());
        assertEquals(javaType, widenedType.withValueHandler(javaType.getValueHandler()).withTypeHandler(javaType.getTypeHandler()));

        // Test methods for handling type parameters
        assertEquals(0, javaType.containedTypeCount());
        JavaType containedType = typeFactory.constructType(Integer[].class);
        JavaType javaTypeWithContentType = javaType.narrowContentsBy(containedType.getRawClass());
        assertEquals(containedType, javaTypeWithContentType);
        assertEquals(containedType, javaTypeWithContentType.withValueHandler(javaType.getValueHandler()).withTypeHandler(javaType.getTypeHandler()));
    }
}