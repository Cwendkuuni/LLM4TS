package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Type;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ReferenceType;
import com.fasterxml.jackson.databind.type.SimpleType;

public class ReferenceTypeTest {

    // Basic test cases for constructors

    @Test
    public void testReferenceTypeWithArgs() {
        Type type = new Type[] { int[].class, String.class }[0];
        JavaType referencedType = JavaType.construct(int[].class, null);
        JavaType anchorType = JavaType.construct(String.class, null);
        JavaType javaType = ReferenceType.construct(Object.class, null, null, new JavaType[] { type }, referencedType, anchorType);
        ReferenceType referenceType = (ReferenceType) javaType;
        assertNotNull(referenceType);
        assertEquals(Object.class, referenceType.getRawClass());
        assertEquals(type, referenceType.getContentType());
        assertEquals(referencedType, referenceType.getReferencedType());
        assertEquals(JavaType.TypeId.ID_ARRAYTYPE, referenceType.getId());
        assertEquals(anchorType, referenceType.getAnchorType());
        assertEquals(JavaType.TypeId.ID_REFERENCE_TYPE, referenceType.getIdAsInt());
    }

    @Test
    public void testReferenceTypeNoArgs() {
        JavaType javaType = ReferenceType.construct(Object.class, null);
        ReferenceType referenceType = (ReferenceType) javaType;
        assertNotNull(referenceType);
        assertEquals(Object.class, referenceType.getRawClass());
        assertNull(referenceType.getReferencedType());
        assertEquals(JavaType.TypeId.ID_REFERENCE_TYPE, referenceType.getIdAsInt());
        assertEquals(referenceType, referenceType.getAnchorType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReferenceTypeNullType() {
        JavaType javaType = ReferenceType.construct(Object.class, null, null, null, null, null);
        // This should fail with IllegalArgumentException
    }

    // Basic test cases for methods

    @Test
    public void testGetContentType() {
        JavaType javaType = ReferenceType.construct(Object.class, null, null, null, JavaType.construct(int.class, null), null);
        ReferenceType referenceType = (ReferenceType) javaType;
        JavaType contentType = referenceType.getContentType();
        assertEquals(int[].class, contentType.getRawClass());
    }

    @Test
    public void testGetReferencedType() {
        JavaType javaType = ReferenceType.construct(Object.class, null, null, null, JavaType.construct(int.class, null), null);
        ReferenceType referenceType = (ReferenceType) javaType;
        JavaType referencedType = referenceType.getReferencedType();
        assertEquals(int.class, referencedType.getRawClass());
    }

    @Test
    public void testHasContentType() {
        JavaType javaType = ReferenceType.construct(Object.class, null, null, null, JavaType.construct(int.class, null), null);
        ReferenceType referenceType = (ReferenceType) javaType;
        assertTrue(referenceType.hasContentType());
    }

    @Test
    public void testIsReferenceType() {
        JavaType javaType = ReferenceType.construct(Object.class, null, null, null, JavaType.construct(int.class, null), null);
        ReferenceType referenceType = (ReferenceType) javaType;
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType javaType = ReferenceType.construct(Object.class, null, null, null, JavaType.construct(int.class, null), null);
        ReferenceType referenceType = (ReferenceType) javaType;
        StringBuilder expected = new StringBuilder("[Ljava.lang.Object;");
        StringBuilder actual = referenceType.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType javaType = ReferenceType.construct(Object.class, null, null, null, JavaType.construct(int[].class, null), null);
        ReferenceType referenceType = (ReferenceType) javaType;
        StringBuilder expected = new StringBuilder("[Ljava.lang.Object;<T:Ljava.lang.Integer;>]");
        StringBuilder actual = new StringBuilder();
        referenceType.getGenericSignature(actual);
        assertEquals(expected.toString(), actual.toString());
    }

    // Test cases for upgrading a basic type into a reference type

    @Test
    public void testUpgradeFromSimpleType() {
        JavaType simpleType = JavaType.construct(int.class, null);
        JavaType referencedType = JavaType.construct(String.class, null);
        JavaType javaType = ReferenceType.upgradeFrom(simpleType, referencedType);
        ReferenceType referenceType = (ReferenceType) javaType;
        assertEquals(int.class, referenceType.getRawClass());
        assertEquals(referencedType, referenceType.getReferencedType());
        assertEquals(JavaType.TypeId.ID_REFERENCE_TYPE, referenceType.getIdAsInt());
        assertEquals(referenceType, referenceType.getAnchorType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromNonType() {
        JavaType baseType = JavaType.construct(Object.class, null);
        JavaType refdType = JavaType.construct(String.class, null);
        ReferenceType.upgradeFrom(baseType, refdType);
        // This should fail with IllegalArgumentException
    }

    // Test cases for factory methods

    @Test
    public void testConstruct() {
        JavaType referencedType = JavaType.construct(String.class, null);
        JavaType javaType = ReferenceType.construct(int[].class, null, null, new JavaType[] { referencedType }, null);
        ReferenceType referenceType = (ReferenceType) javaType;
        assertEquals(int[].class, referenceType.getRawClass());
        assertEquals(referencedType, referenceType.getReferencedType());
        assertEquals(JavaType.TypeId.ID_ARRAYTYPE, referenceType.getId());
        assertEquals(null, referenceType.getAnchorType());
    }

    // Test cases for methods with ValueHandler and TypeHandler

    @Test
    public void testWithValueHandler() {
        JavaType referencedType = JavaType.construct(String.class, null);
        ReferenceType referenceType = new ReferenceType(int[].class, null, null, new JavaType[] { referencedType }, null, null, null, null, false) {
            @Override
            public Object getValueHandler() {
                return new Object() {};
            }
        };
        JavaType javaType = referenceType.withValueHandler(new Object() {});
        ReferenceType newReferenceType = (ReferenceType) javaType;
        assertEquals(newReferenceType.getClass(), referenceType.getClass());
        assertNotSame(referenceType.getValueHandler(), newReferenceType.getValueHandler());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType referencedType = JavaType.construct(String.class, null);
        ReferenceType referenceType = new ReferenceType(int[].class, null, null, new JavaType[] { referencedType }, null, null, null, null, false) {
            @Override
            public Object getTypeHandler() {
                return new Object() {};
            }
        };
        JavaType javaType = referenceType.withTypeHandler(new Object() {});
        ReferenceType newReferenceType = (ReferenceType) javaType;
        assertEquals(newReferenceType.getClass(), referenceType.getClass());
        assertNotSame(referenceType.getTypeHandler(), newReferenceType.getTypeHandler());
    }

    // Test cases for methods with ContentTypeHandler

    @Test
    public void testWithContentTypeHandler() {
        JavaType referencedType = JavaType.construct(String.class, null);
        ReferenceType referenceType = new ReferenceType(int[].class, null, null, new JavaType[] { referencedType }, null, null, null, null, false) {
            @Override
            public Object getContentTypeHandler() {
                return new Object() {};
            }
        };
        JavaType javaType = referenceType.withContentTypeHandler(new Object() {});
        ReferenceType newReferenceType = (ReferenceType) javaType;
        assertEquals(newReferenceType.getClass(), referenceType.getClass());
        assertNotSame(referenceType.getContentTypeHandler(), newReferenceType.getContentTypeHandler());
    }

    // Test cases for methods with StaticTyping

    @Test
    public void testWithStaticTyping() {
        JavaType referencedType = JavaType.construct(String.class, null);
        ReferenceType referenceType = new ReferenceType(int[].class, null, null, new JavaType[] { referencedType }, null, null, null, null, false);
        JavaType javaType = referenceType.withStaticTyping();
        ReferenceType newReferenceType = (ReferenceType) javaType;
        assertTrue(newReferenceType.isStaticTyping());
        assertFalse(referenceType.isStaticTyping());
    }

    // Test cases for Refine method

    @Test
    public void testRefine() {
        JavaType baseType = JavaType.construct(Object.class, null);
        JavaType referencedType = JavaType.construct(int.class, null);
        JavaType newJavaType = ReferenceType.refine(baseType.getTypeId().getId(), JavaType.construct(Object.class, null), null, new JavaType[] { referencedType }, null);
        ReferenceType newReferenceType = (ReferenceType) newJavaType;
        assertEquals(int.class, newReferenceType.getReferencedType().getRawClass());
    }

    // Test cases for Narrow method (deprecated)

    @Test(expected = DeprecationWarning.class)
    public void testNarrow() {
        JavaType baseType = JavaType.construct(Object.class, null);
        JavaType subclass = JavaType.construct(String.class, null);
        ReferenceType referenceType = new ReferenceType(baseType, referencedType) {
            @Override
            protected JavaType _narrow(Class<?> subclass) {
                return JavaType.construct(subclass, null);
            }
        };
        JavaType javaType = referenceType._narrow(String.class);
        ReferenceType newReferenceType = (ReferenceType) javaType;
        assertEquals(String.class, newReferenceType.getRawClass());
    }

}