package com.example.junit;

import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ReferenceType;

public class ReferenceTypeTest {

    private Class<Integer> intClass = Integer.class;
    private JavaType javaType = JavaType.construct(int[].class);

    @Test
    public void testConstruct() {
        ReferenceType referenceType = ReferenceType.construct(intClass, javaType, null, null);
        assertEquals(intClass, referenceType.getRawClass());
        assertEquals(javaType, referenceType.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        ReferenceType referenceType = new ReferenceType(intClass, javaType, null, null, false);
        Object handler = new Object();
        ReferenceType newReferenceType = referenceType.withTypeHandler(handler);
        assertNotEquals(referenceType, newReferenceType);
        assertEquals(handler, newReferenceType.getTypeHandler());
    }

    @Test
    public void testGetReferencedType() {
        JavaType expectedJavaType = JavaType.construct(int.class);
        ReferenceType referenceType = new ReferenceType(intClass, javaType, null, null, false);
        assertEquals(expectedJavaType, referenceType.getReferencedType());
    }

    @Test
    public void testConstructNullInput() {
        JavaType javaType = null;
        ReferenceType referenceType = Assert.assertThrows(NullPointerException.class, () -> ReferenceType.construct(intClass, javaType, null, null));
        assertNull(referenceType.getContentType());
    }

    @Test
    public void testWithTypeHandlerNullHandler() {
        ReferenceType referenceType = new ReferenceType(intClass, javaType, null, null, false);
        ReferenceType newReferenceType = referenceType.withTypeHandler(null);
        assertNull(newReferenceType.getTypeHandler());
    }

    @Test
    public void testGetReferencedTypeNonReferenceType() {
        Class<String> stringClass = String.class;
        ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);
        JavaType actualJavaType = referenceType.getReferencedType();
        assertNull(actualJavaType);
    }
}