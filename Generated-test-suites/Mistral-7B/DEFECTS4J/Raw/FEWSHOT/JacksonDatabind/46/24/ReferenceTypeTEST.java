package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import com.fasterxml.jackson.databind.JavaType;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstruction() {
        JavaType refType = JavaType.constructCollectionType(ArrayList.class, String.class);
        ReferenceType referenceType = ReferenceType.construct(String[].class, refType, null, null);
        assertEquals(String[].class, referenceType.getRawClass());
        assertEquals(refType.hashCode(), referenceType.hashCode());
        assertEquals(refType, referenceType.getContentType());
        assertFalse(referenceType.isStatic());
    }

    @Test
    public void testSetters() {
        JavaType refType = JavaType.constructCollectionType(ArrayList.class, String.class);
        ReferenceType referenceType = new ReferenceType(String[].class, refType, null, null, false);

        ReferenceType withTypeHandler = referenceType.withTypeHandler(new Object() {});
        assertEquals(referenceType.getRawClass(), withTypeHandler.getRawClass());
        assertSame(referenceType, withTypeHandler);
        assertNull(withTypeHandler._typeHandler);

        ReferenceType withContentTypeHandler = withTypeHandler.withContentTypeHandler(new Object() {});
        assertEquals(referenceType.getRawClass(), withContentTypeHandler.getRawClass());
        assertSame(referenceType, withContentTypeHandler);
        assertNull(withContentTypeHandler._referencedType);

        ReferenceType withValueHandler = withContentTypeHandler.withValueHandler(new Object() {});
        assertEquals(referenceType.getRawClass(), withValueHandler.getRawClass());
        assertSame(referenceType, withValueHandler);
        assertNull(withValueHandler._valueHandler);

        ReferenceType withContentValueHandler = withValueHandler.withContentValueHandler(new Object() {});
        assertEquals(referenceType.getRawClass(), withContentValueHandler.getRawClass());
        assertSame(referenceType, withContentValueHandler);
        assertNull(withContentValueHandler._referencedType);

        ReferenceType withStaticTyping = withContentValueHandler.withStaticTyping();
        assertEquals(referenceType.getRawClass(), withStaticTyping.getRawClass());
        assertSame(referenceType, withStaticTyping);
        assertTrue(withStaticTyping.isStatic());
    }

    @Test
    public void testGetters() {
        JavaType refType = JavaType.constructCollectionType(ArrayList.class, String.class);
        ReferenceType referenceType = new ReferenceType(String[].class, refType, null, null, false);

        assertEquals(String[].class, referenceType.getRawClass());
        assertEquals(refType, referenceType.getContentType());
        assertEquals(false, referenceType.isStatic());
        assertEquals(referenceType.getContentTypeHandler(), refType.<Object>getTypeHandler());
        assertEquals(referenceType.getValueHandler(), null);
        assertEquals(referenceType.getReferencedType(), refType);
        assertEquals(true, referenceType.isReferenceType());

        ReferenceType withTypeHandler = referenceType.withTypeHandler(new Object() {});
        assertEquals(String[].class, withTypeHandler.getRawClass());
        assertEquals(refType, withTypeHandler.getContentType());
        assertEquals(false, withTypeHandler.isStatic());
        assertNull(withTypeHandler.getContentTypeHandler());
        assertEquals(null, withTypeHandler.getValueHandler());
        assertSame(refType, withTypeHandler.getReferencedType());
        assertEquals(true, withTypeHandler.isReferenceType());

        ReferenceType withContentTypeHandler = withTypeHandler.withContentTypeHandler(new Object() {});
        assertEquals(String[].class, withContentTypeHandler.getRawClass());
        assertEquals(refType, withContentTypeHandler.getContentType());
        assertEquals(false, withContentTypeHandler.isStatic());
        assertNull(withContentTypeHandler.getTypeHandler());
        assertEquals(null, withContentTypeHandler.getValueHandler());
        assertNull(withContentTypeHandler.getReferencedType());
        assertEquals(true, withContentTypeHandler.isReferenceType());

        ReferenceType withValueHandler = withContentTypeHandler.withValueHandler(new Object() {});
        assertEquals(String[].class, withValueHandler.getRawClass());
        assertEquals(refType, withValueHandler.getContentType());
        assertEquals(false, withValueHandler.isStatic());
        assertNull(withValueHandler.getTypeHandler());
        assertEquals(new Object(), withValueHandler.getValueHandler());
        assertNull(withValueHandler.getReferencedType());
        assertEquals(true, withValueHandler.isReferenceType());

        ReferenceType withContentValueHandler = withValueHandler.withContentValueHandler(new Object() {});
        assertEquals(String[].class, withContentValueHandler.getRawClass());
        assertEquals(refType, withContentValueHandler.getContentType());
        assertEquals(false, withContentValueHandler.isStatic());
        assertNull(withContentValueHandler.getTypeHandler());
        assertNull(withContentValueHandler.getValueHandler());
        assertNull(withContentValueHandler.getReferencedType());
        assertEquals(true, withContentValueHandler.isReferenceType());

        ReferenceType withStaticTyping = withContentValueHandler.withStaticTyping();
        assertEquals(String[].class, withStaticTyping.getRawClass());
        assertEquals(refType, withStaticTyping.getContentType());
        assertEquals(true, withStaticTyping.isStatic());
        assertNull(withStaticTyping.getTypeHandler());
        assertNull(withStaticTyping.getValueHandler());
        assertNull(withStaticTyping.getReferencedType());
        assertEquals(true, withStaticTyping.isReferenceType());
    }

    @Test
    public void testToString() {
        JavaType refType = JavaType.constructCollectionType(ArrayList.class, String.class);
        ReferenceType referenceType = new ReferenceType(String[].class, refType, null, null, false);
        assertEquals("[reference type, class [Ljava.lang.String;<T>], class java.lang.Object]", referenceType.toString());
        assertEquals("[reference type, class [Ljava.lang.String;<T>], class java.lang.String], class java.lang.Object]", referenceType.withContentTypeHandler(new Object()).toString());
        assertEquals("[reference type, class [Ljava.lang.String;<T>], class java.lang.String], class java.lang.Object]", referenceType.withValueHandler(new Object()).toString());
        assertEquals("[reference type, class [Ljava.lang.String;<T>], class java.lang.String], class java.lang.Object]", referenceType.withContentValueHandler(new Object()).toString());
        assertEquals("[reference type, class [Ljava.lang.String;<T>], class java.lang.String], class java.lang.Object]", referenceType.withStaticTyping().toString());
    }

    @Test
    public void testEquals() {
        JavaType refType = JavaType.constructCollectionType(ArrayList.class, String.class);
        ReferenceType referenceType = new ReferenceType(String[].class, refType, null, null, false);
        JavaType refType2 = JavaType.constructCollectionType(HashSet.class, String.class);
        ReferenceType referenceType2 = new ReferenceType(String[].class, refType2, null, null, false);
        JavaType refType3 = JavaType.constructCollectionType(ArrayList.class, Integer.class);
        ReferenceType referenceType3 = new ReferenceType(Integer[].class, refType3, null, null, false);

        assertEquals(referenceType, referenceType);
        assertFalse(referenceType.equals(null));
        assertFalse(referenceType.equals(refType));
        assertFalse(referenceType.equals(refType2));
        assertFalse(referenceType.equals(referenceType3));

        ReferenceType referenceType4 = new ReferenceType(String[].class, refType, null, null, false);
        assertEquals(referenceType, referenceType4);

        ReferenceType referenceType5 = new ReferenceType(String[].class, refType, null, null, true);
        assertFalse(referenceType.equals(referenceType5));

        ReferenceType referenceType6 = new ReferenceType(String[].class, refType, null, null, false);
        referenceType6._referencedType = new JavaType() {};
        assertFalse(referenceType.equals(referenceType6));
    }

    @Test
    public void testHashCode() {
        JavaType refType = JavaType.constructCollectionType(ArrayList.class, String.class);
        ReferenceType referenceType = new ReferenceType(String[].class, refType, null, null, false);
        ReferenceType referenceType2 = new ReferenceType(String[].class, refType, null, null, false);
        JavaType refType3 = JavaType.constructCollectionType(HashSet.class, String.class);
        ReferenceType referenceType3 = new ReferenceType(String[].class, refType3, null, null, false);

        assertEquals(referenceType.hashCode(), referenceType.hashCode());
        assertEquals(referenceType.hashCode(), referenceType2.hashCode());
        assertNotEquals(referenceType.hashCode(), refType.hashCode());
        assertNotEquals(referenceType.hashCode(), refType3.hashCode());
        assertNotEquals(referenceType.hashCode(), referenceType3.hashCode());
    }

    @Test
    public void testNarrowing() {
        JavaType refType = JavaType.constructCollectionType(ArrayList.class, String.class);
        ReferenceType referenceType = new ReferenceType(String[].class, refType, null, null, false);
        JavaType narrowedType = referenceType._narrow(Object[].class);
        assertEquals(Object[].class, narrowedType.getRawClass());
        assertEquals(referenceType.getContentType(), narrowedType.getContentType());
        assertEquals(false, narrowedType.isStatic());
    }
}