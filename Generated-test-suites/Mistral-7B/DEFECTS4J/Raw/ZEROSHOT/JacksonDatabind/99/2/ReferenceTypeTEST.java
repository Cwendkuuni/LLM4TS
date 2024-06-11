package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ReferenceType;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    @Test
    public void testReferenceType() {
        JavaType refType = ReferenceType.construct(String.class, null, null, null);
        ReferenceType referenceType = ReferenceType.upgradeFrom(JavaType.INT_TYPE, refType);

        assertEquals("java.lang.String", referenceType.getCanonicalName());
        assertEquals(JavaType.INT_TYPE, referenceType.getContentType());
        assertTrue(referenceType.isReferenceType());
        assertFalse(referenceType.hasGenericTypes());
        assertNull(referenceType.getValueHandler());
        assertNull(referenceType.getTypeHandler());
        assertFalse(referenceType.isStatic());
        assertFalse(referenceType.isAnchorType());
        assertNull(referenceType.getAnchorType());

        JavaType refTypeWithAnchor = ReferenceType.construct(String.class, null, null, null, JavaType.INT_TYPE);
        ReferenceType referenceTypeWithAnchor = new ReferenceType((TypeBase) JavaType.INT_TYPE.getValueHandler(), refTypeWithAnchor);

        assertEquals("java.lang.String", referenceTypeWithAnchor.getCanonicalName());
        assertEquals(JavaType.INT_TYPE, referenceTypeWithAnchor.getContentType());
        assertTrue(referenceTypeWithAnchor.isReferenceType());
        assertFalse(referenceTypeWithAnchor.hasGenericTypes());
        assertNull(referenceTypeWithAnchor.getValueHandler());
        assertNull(referenceTypeWithAnchor.getTypeHandler());
        assertFalse(referenceTypeWithAnchor.isStatic());
        assertTrue(referenceTypeWithAnchor.isAnchorType());
        assertEquals(referenceTypeWithAnchor, referenceTypeWithAnchor.getAnchorType());
    }

    @Test
    public void testReferenceTypeWithTypeHandler() {
        JavaType refType = ReferenceType.construct(String.class, null, null, null);
        JavaType intTypeHandler = new TypeReference<Integer>() {}.getType();

        ReferenceType referenceType = new ReferenceType(JavaType.INT_TYPE, refType) {
            @Override
            public Object getValueHandler() {
                return new Object() {
                    @Override
                    public Integer intValue() {
                        return 1;
                    }
                };
            }
        };

        assertEquals("java.lang.String", referenceType.getCanonicalName());
        assertEquals(JavaType.INT_TYPE, referenceType.getContentType());
        assertTrue(referenceType.isReferenceType());
        assertFalse(referenceType.hasGenericTypes());
        assertNotNull(referenceType.getValueHandler());
        assertNull(referenceType.getTypeHandler());
        assertFalse(referenceType.isStatic());
        assertFalse(referenceType.isAnchorType());
        assertNull(referenceType.getAnchorType());

        assertEquals(1, ((Number) referenceType.getValueHandler()).intValue());
    }

    @Test
    public void testReferenceTypeWithTypeAndValueHandlers() {
        JavaType refType = ReferenceType.construct(String.class, null, null, null);
        JavaType intTypeHandler = new TypeReference<Integer>() {}.getType();

        ReferenceType referenceType = new ReferenceType(JavaType.INT_TYPE, refType) {
            @Override
            public Object getValueHandler() {
                return new Object() {
                    @Override
                    public Integer intValue() {
                        return 1;
                    }
                };
            }

            @Override
            public Object getTypeHandler() {
                return new Object() {};
            }
        };

        assertEquals("java.lang.String", referenceType.getCanonicalName());
        assertEquals(JavaType.INT_TYPE, referenceType.getContentType());
        assertTrue(referenceType.isReferenceType());
        assertFalse(referenceType.hasGenericTypes());
        assertNotNull(referenceType.getValueHandler());
        assertNotNull(referenceType.getTypeHandler());
        assertFalse(referenceType.isStatic());
        assertFalse(referenceType.isAnchorType());
        assertNull(referenceType.getAnchorType());
    }

    @Test
    public void testReferenceTypeWithStaticTyping() {
        JavaType refType = ReferenceType.construct(String[].class, null, null, null);
        ReferenceType referenceType = new ReferenceType(JavaType.INT_ARRAY_TYPE, refType) {
            @Override
            public boolean isStatic() {
                return true;
            }
        };

        assertEquals("class [Ljava.lang.int;", referenceType.getCanonicalName());
        assertEquals(JavaType.INT_ARRAY_TYPE, referenceType.getContentType());
        assertTrue(referenceType.isReferenceType());
        assertFalse(referenceType.hasGenericTypes());
        assertNull(referenceType.getValueHandler());
        assertNull(referenceType.getTypeHandler());
        assertTrue(referenceType.isStatic());
        assertFalse(referenceType.isAnchorType());
        assertNull(referenceType.getAnchorType());
    }

    @Test
    public void testReferenceTypeUpgradeFrom() {
        JavaType refType = ReferenceType.construct(String.class, null, null, null);
        JavaType intType = JavaType.INT_TYPE;

        ReferenceType referenceType = ReferenceType.upgradeFrom(refType, intType);

        assertEquals("java.lang.String", referenceType.getCanonicalName());
        assertEquals(JavaType.INT_TYPE, referenceType.getContentType());
        assertTrue(referenceType.isReferenceType());
        assertFalse(referenceType.hasGenericTypes());
        assertNull(referenceType.getValueHandler());
        assertNull(referenceType.getTypeHandler());
        assertFalse(referenceType.isStatic());
        assertFalse(referenceType.isAnchorType());
        assertNull(referenceType.getAnchorType());
    }
}