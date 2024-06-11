package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.lang.reflect.Type;
import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ReferenceType;

public class ReferenceTypeTest {

    @Test
    public void testReferenceTypeConstruction() {
        Class<String> stringClass = String.class;
        JavaType javaType = new TypeReference<Object>() {}.getType();
        ReferenceType refType = ReferenceType.construct(stringClass, javaType, null, null);
        assertEquals(String.class, refType.getRawClass());
        assertEquals(JavaType.class, refType.getContentType().getClass());
        assertEquals(refType.hashCode(), refType.getContentType().hashCode());
    }

    @Test
    public void testTypeHandlerMethods() {
        Class<String> stringClass = String.class;
        JavaType javaType = new TypeReference<Object>() {}.getType();
        ReferenceType refType = ReferenceType.construct(stringClass, javaType, null, null);

        // Test withTypeHandler
        Object handler = new Object();
        ReferenceType newRefType = refType.withTypeHandler(handler);
        assertNotSame(handler, refType._typeHandler);
        assertEquals(handler, newRefType._typeHandler);
        assertEquals(refType.getClass(), newRefType.getClass());

        // Test withContentTypeHandler
        JavaType newJavaType = new TypeReference<Object>() {}.getType();
        newJavaType = newJavaType.withTypeHandler(handler);
        ReferenceType newRefTypeWithContentTypeHandler = refType.withContentTypeHandler(newJavaType);
        assertNotSame(newJavaType, refType._referencedType);
        assertEquals(newJavaType, newRefTypeWithContentTypeHandler.getContentType());
        assertNotSame(handler, refType._typeHandler);
        assertEquals(handler, newRefTypeWithContentTypeHandler._typeHandler);
        assertEquals(refType.getClass(), newRefTypeWithContentTypeHandler.getClass());
    }

    @Test
    public void testValueHandlerMethods() {
        Class<String> stringClass = String.class;
        JavaType javaType = new TypeReference<Object>() {}.getType();
        Object valueHandler = new Object();
        ReferenceType refType = ReferenceType.construct(stringClass, javaType, valueHandler, null);

        // Test withValueHandler
        Object newValueHandler = new Object();
        ReferenceType newRefType = refType.withValueHandler(newValueHandler);
        assertNotSame(newValueHandler, refType._valueHandler);
        assertEquals(newValueHandler, newRefType._valueHandler);
        assertEquals(refType.getClass(), newRefType.getClass());

        // Test withContentValueHandler
        Object newContentValueHandler = new Object();
        JavaType newJavaType = new TypeReference<Object>() {}.getType();
        newJavaType = newJavaType.withValueHandler(newContentValueHandler);
        ReferenceType newRefTypeWithContentValueHandler = refType.withContentValueHandler(newJavaType);
        assertNotSame(newContentValueHandler, refType._referencedType.<Object>getValueHandler());
        assertEquals(newContentValueHandler, newRefTypeWithContentValueHandler._referencedType.<Object>getValueHandler());
        assertNotSame(newValueHandler, refType._valueHandler);
        assertEquals(newValueHandler, newRefTypeWithContentValueHandler._valueHandler);
        assertEquals(refType.getClass(), newRefTypeWithContentValueHandler.getClass());
    }

    @Test
    public void testWithStaticTyping() {
        Class<String> stringClass = String.class;
        JavaType javaType = new TypeReference<Object>() {}.getType();
        ReferenceType refType = ReferenceType.construct(stringClass, javaType, null, null, false);
        ReferenceType newRefType = refType.withStaticTyping();
        assertTrue(newRefType._asStatic);
        assertEquals(refType.getClass(), newRefType.getClass());
    }

    @Test
    public void testNarrow() {
        Class<String> stringClass = String.class;
        JavaType javaType = new TypeReference<Object>() {}.getType();
        JavaType narrowedJavaType = new TypeReference<Object>() {}.getSuperClass();
        ReferenceType refType = ReferenceType.construct(stringClass, javaType, null, null);
        ReferenceType narrowedRefType = refType._narrow(String[].class);
        assertEquals(String[].class, narrowedRefType.getRawClass());
        assertEquals(javaType, narrowedRefType.getContentType());
    }

    @Test
    public void testReferencedType() {
        Class<String> stringClass = String.class;
        JavaType javaType = new TypeReference<Object>() {}.getType();
        ReferenceType refType = ReferenceType.construct(stringClass, javaType, null, null);
        JavaType referencedType = refType.getReferencedType();
        assertEquals(javaType, referencedType);
    }

    @Test
    public void testIsReferenceType() {
        Class<String> stringClass = String.class;
        JavaType javaType = new TypeReference<Object>() {}.getType();
        ReferenceType refType = ReferenceType.construct(stringClass, javaType, null, null);
        assertTrue(refType.isReferenceType());
    }

    @Test
    public void testContainedTypeCount() {
        Class<String> stringClass = String.class;
        JavaType javaType = new TypeReference<Object>() {}.getType();
        ReferenceType refType = ReferenceType.construct(stringClass, javaType, null, null);
        assertEquals(1, refType.containedTypeCount());
    }

    @Test
    public void testContainedType() {
        Class<String> stringClass = String.class;
        JavaType javaType = new TypeReference<Object>() {}.getType();
        ReferenceType refType = ReferenceType.construct(stringClass, javaType, null, null);
        JavaType containedType = refType.containedType(0);
        assertEquals(javaType, containedType);
    }

    @Test
    public void testContainedTypeName() {
        Class<String> stringClass = String.class;
        JavaType javaType = new TypeReference<Object>() {}.getType();
        ReferenceType refType = ReferenceType.construct(stringClass, javaType, null, null);
        String containedTypeName = refType.containedTypeName(0);
        assertEquals("T", containedTypeName);
    }

    @Test
    public void testGetParameterSource() {
        Class<String> stringClass = String.class;
        JavaType javaType = new TypeReference<Object>() {}.getType();
        ReferenceType refType = ReferenceType.construct(stringClass, javaType, null, null);
        assertEquals(stringClass, refType.getParameterSource());
    }

    @Test
    public void testGetErasedSignature() {
        Class<String> stringClass = String.class;
        JavaType javaType = new TypeReference<Object>() {}.getType();
        ReferenceType refType = ReferenceType.construct(stringClass, javaType, null, null);
        StringBuilder sb = refType.getErasedSignature(new StringBuilder());
        String expected = stringClass.getName().replaceAll("\\.", "\\\\.");
        assertEquals(expected, sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<String> stringClass = String.class;
        JavaType javaType = new TypeReference<Object>() {}.getType();
        ReferenceType refType = ReferenceType.construct(stringClass, javaType, null, null);
        StringBuilder sb = refType.getGenericSignature(new StringBuilder());
        String expected = "<" + stringClass.getName().replaceAll("\\.", "\\\\.") + "<T>" + ">";
        assertEquals(expected, sb.toString());
    }

    @Test
    public void testEquals() {
        Class<String> stringClass1 = String.class;
        JavaType javaType1 = new TypeReference<Object>() {}.getType();
        JavaType javaType2 = new TypeReference<Object>() {}.getType();
        ReferenceType refType1 = ReferenceType.construct(stringClass1, javaType1, null, null);
        ReferenceType refType2 = ReferenceType.construct(stringClass1, javaType2, null, null);
        ReferenceType refType3 = ReferenceType.construct(stringClass1, javaType1, null, null);
        ReferenceType refType4 = new ReferenceType(stringClass1, javaType1, null, null, false);

        assertTrue(refType1.equals(refType1));
        assertTrue(refType1.equals(refType3));
        assertFalse(refType1.equals(null));
        assertFalse(refType1.equals(javaType1));
        assertFalse(refType1.equals(new Object()));
        assertFalse(refType1.equals(refType2));
        assertFalse(refType1.equals(refType4));
    }

    @Test
    public void testHashCode() {
        Class<String> stringClass = String.class;
        JavaType javaType = new TypeReference<Object>() {}.getType();
        ReferenceType refType1 = ReferenceType.construct(stringClass, javaType, null, null);
        JavaType javaType2 = new TypeReference<Object>() {}.getType();
        ReferenceType refType2 = ReferenceType.construct(stringClass, javaType2, null, null);
        assertEquals(refType1.hashCode(), refType1.hashCode());
        assertNotEquals(refType1.hashCode(), refType2.hashCode());
    }

    @Test
    public void testToString() {
        Class<String> stringClass = String.class;
        JavaType javaType = new TypeReference<Object>() {}.getType();
        ReferenceType refType = ReferenceType.construct(stringClass, javaType, null, null);
        String expected = "[reference type, class " + stringClass.getName() +
                "<T>" + "]";
        assertEquals(expected, refType.toString());
    }
}