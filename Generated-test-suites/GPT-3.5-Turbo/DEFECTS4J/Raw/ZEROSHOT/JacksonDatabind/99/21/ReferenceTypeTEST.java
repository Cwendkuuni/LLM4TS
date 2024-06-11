package com.fasterxm.jackson.databind.type;

import com.fasterxm.jackson.databind.JavaType;
import org.junit.Test;

import static org.junit.Assert.*;

public class ReferenceTypeTest {

    @Test
    public void testConstructor() {
        Class<?> cls = String.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType refType = null;
        JavaType anchorType = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;

        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, anchorType, valueHandler, typeHandler, asStatic);

        assertEquals(cls, referenceType._class);
        assertEquals(bindings, referenceType._bindings);
        assertEquals(superClass, referenceType._superClass);
        assertEquals(superInts, referenceType._superInterfaces);
        assertEquals(refType, referenceType._referencedType);
        assertEquals(anchorType, referenceType._anchorType);
        assertEquals(valueHandler, referenceType._valueHandler);
        assertEquals(typeHandler, referenceType._typeHandler);
        assertEquals(asStatic, referenceType._asStatic);
    }

    @Test
    public void testUpgradeFrom_NullRefdType() {
        JavaType baseType = null;
        JavaType refdType = null;

        try {
            ReferenceType.upgradeFrom(baseType, refdType);
            fail("IllegalArgumentException should be thrown when refdType is null");
        } catch (IllegalArgumentException e) {}
    }

    @Test
    public void testUpgradeFrom_InstanceNotTypeBase() {
        JavaType baseType = new JavaType() {
            // dummy implementation
        };
        JavaType refdType = new JavaType() {
            // dummy implementation
        };

        try {
            ReferenceType.upgradeFrom(baseType, refdType);
            fail("IllegalArgumentException should be thrown when baseType is not TypeBase");
        } catch (IllegalArgumentException e) {}
    }

    @Test
    public void testUpgradeFrom_ValidCase() {
        JavaType baseType = new TypeBase();
        JavaType refdType = new JavaType() {
            // dummy implementation
        };

        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refdType);

        assertSame(baseType, referenceType.getReferencedType());
    }

    @Test
    public void testConstruct() {
        Class<?> cls = String.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType refType = null;

        ReferenceType referenceType = ReferenceType.construct(cls, bindings, superClass, superInts, refType);

        assertEquals(cls, referenceType._class);
        assertEquals(bindings, referenceType._bindings);
        assertEquals(superClass, referenceType._superClass);
        assertEquals(superInts, referenceType._superInterfaces);
        assertEquals(refType, referenceType._referencedType);
    }

    @Test
    public void testConstruct_Deprecated() {
        Class<?> cls = String.class;
        JavaType refType = null;

        ReferenceType referenceType = ReferenceType.construct(cls, refType);

        assertEquals(cls, referenceType._class);
        assertEquals(TypeBindings.emptyBindings(), referenceType._bindings);
        assertNull(referenceType._superClass);
        assertNull(referenceType._superInterfaces);
        assertNull(referenceType._referencedType.getTypeHandler());
        assertNull(referenceType._valueHandler);
        assertNull(referenceType._typeHandler);
        assertFalse(referenceType._asStatic);
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = new JavaType() {
            // dummy implementation
        };

        ReferenceType referenceType = new ReferenceType();

        JavaType result = referenceType.withContentType(contentType);

        assertEquals(contentType, result.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        Object h = null;

        ReferenceType referenceType = new ReferenceType();

        ReferenceType result = referenceType.withTypeHandler(h);

        assertEquals(h, result._typeHandler);
    }

    @Test
    public void testWithContentTypeHandler() {
        Object h = null;

        ReferenceType referenceType = new ReferenceType();

        ReferenceType result = referenceType.withContentTypeHandler(h);

        assertEquals(h, result._referencedType.getTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Object h = null;

        ReferenceType referenceType = new ReferenceType();

        ReferenceType result = referenceType.withValueHandler(h);

        assertEquals(h, result._valueHandler);
    }

    @Test
    public void testWithContentValueHandler() {
        Object h = null;

        ReferenceType referenceType = new ReferenceType();

        ReferenceType result = referenceType.withContentValueHandler(h);

        assertEquals(h, result._referencedType.getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        ReferenceType referenceType = new ReferenceType();

        ReferenceType result = referenceType.withStaticTyping();

        assertTrue(result._asStatic);
    }

    @Test
    public void testRefine() {
        Class<?> rawType = String.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;

        ReferenceType referenceType = new ReferenceType();

        JavaType result = referenceType.refine(rawType, bindings, superClass, superInterfaces);

        assertEquals(rawType, result.getRawClass());
        assertEquals(bindings, result.getBindings());
        assertEquals(superClass, result.getSuperClass());
        assertEquals(superInterfaces, result.getInterfaces());
    }

    @Test
    public void testBuildCanonicalName() {
        ReferenceType referenceType = new ReferenceType();
        referenceType._class = String.class;
        referenceType._referencedType = new JavaType() {
            // dummy implementation
        };

        String result = referenceType.buildCanonicalName();

        assertTrue(result.startsWith("java.lang.String"));
        assertTrue(result.endsWith("<>"));
    }

    @Test
    public void testGetContentType() {
        ReferenceType referenceType = new ReferenceType();
        referenceType._referencedType = new JavaType() {
            // dummy implementation
        };

        JavaType result = referenceType.getContentType();

        assertSame(referenceType._referencedType, result);
    }

    @Test
    public void testGetReferencedType() {
        ReferenceType referenceType = new ReferenceType();
        referenceType._referencedType = new JavaType() {
            // dummy implementation
        };

        JavaType result = referenceType.getReferencedType();

        assertSame(referenceType._referencedType, result);
    }

    @Test
    public void testHasContentType() {
        ReferenceType referenceType = new ReferenceType();

        assertTrue(referenceType.hasContentType());
    }

    @Test
    public void testIsReferenceType() {
        ReferenceType referenceType = new ReferenceType();

        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testGetAnchorType() {
        ReferenceType referenceType = new ReferenceType();
        referenceType._anchorType = new JavaType() {
            // dummy implementation
        };

        JavaType result = referenceType.getAnchorType();

        assertSame(referenceType._anchorType, result);
    }

    @Test
    public void testIsAnchorType() {
        ReferenceType referenceType = new ReferenceType();
        referenceType._anchorType = referenceType;

        assertTrue(referenceType.isAnchorType());
    }

    @Test
    public void testToString() {
        ReferenceType referenceType = new ReferenceType();
        referenceType._class = String.class;
        referenceType._referencedType = new JavaType() {
            // dummy implementation
        };

        String result = referenceType.toString();

        assertTrue(result.startsWith("[reference type, class java.lang.String@"));
        assertTrue(result.endsWith(">]"));
    }

    @Test
    public void testEquals_RefTypeIsDifferent() {
        ReferenceType referenceType = new ReferenceType();
        referenceType._class = String.class;
        referenceType._referencedType = new JavaType() {
            // dummy implementation
        };

        ReferenceType other = new ReferenceType();
        other._class = String.class;
        other._referencedType = new JavaType() {
            // dummy implementation
        };

        assertFalse(referenceType.equals(other));
    }

    @Test
    public void testEquals_RefTypeIsSame() {
        ReferenceType referenceType = new ReferenceType();
        referenceType._class = String.class;
        referenceType._referencedType = new JavaType() {
            // dummy implementation
        };

        ReferenceType other = new ReferenceType();
        other._class = Integer.class;
        other._referencedType = referenceType._referencedType;

        assertFalse(referenceType.equals(other));
    }

    @Test
    public void testEquals_OtherClassIsDifferent() {
        ReferenceType referenceType = new ReferenceType();
        referenceType._class = String.class;
        referenceType._referencedType = new JavaType() {
            // dummy implementation
        };

        assertFalse(referenceType.equals(new Object()));
    }

    @Test
    public void testEquals_OtherIsNull() {
        ReferenceType referenceType = new ReferenceType();
        referenceType._class = String.class;
        referenceType._referencedType = new JavaType() {
            // dummy implementation
        };

        assertFalse(referenceType.equals(null));
    }

    @Test
    public void testEquals_EqualInstances() {
        ReferenceType referenceType = new ReferenceType();
        referenceType._class = String.class;
        referenceType._referencedType = new JavaType() {
            // dummy implementation
        };

        assertTrue(referenceType.equals(referenceType));
    }

    private static class TypeBase extends JavaType {
        @Override
        public StringBuilder getGenericSignature(StringBuilder stringBuilder) {
            return null;
        }

        @Override
        public boolean isContainerType() {
            return false;
        }
    }

}