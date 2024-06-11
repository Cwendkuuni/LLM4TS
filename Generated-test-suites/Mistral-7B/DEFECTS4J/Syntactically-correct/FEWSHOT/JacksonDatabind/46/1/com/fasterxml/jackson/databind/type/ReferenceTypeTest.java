package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Test;

public class ReferenceTypeTest {

    private static final JavaType INT_TYPE = TypeFactory.defaultInstance().constructType(int.class);
    private static final JavaType STRING_TYPE = TypeFactory.defaultInstance().constructType(String.class);
    private static final JavaType REFERENCE_INT_TYPE = new ReferenceType(int.class, INT_TYPE, null, null, false);

    @Test
    public void testConstruct() {
        assertEquals(Integer.TYPE, REFERENCE_INT_TYPE.getRawClass());
        assertEquals(INT_TYPE, REFERENCE_INT_TYPE.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType newReferenceIntType = REFERENCE_INT_TYPE.withTypeHandler(null);
        assertEquals(Integer.TYPE, newReferenceIntType.getRawClass());
        assertEquals(INT_TYPE, newReferenceIntType.getContentType());

        JavaType newReferenceIntTypeWithHandler = REFERENCE_INT_TYPE.withTypeHandler(new Object() {});
        assertEquals(Integer.TYPE, newReferenceIntTypeWithHandler.getRawClass());
        assertNotSame(INT_TYPE, newReferenceIntTypeWithHandler.getContentType());
    }

    @Test
    public void testGetReferencedType() {
        assertEquals(INT_TYPE, REFERENCE_INT_TYPE.getReferencedType());
        assertFalse(REFERENCE_INT_TYPE.isReferenceType());
    }

    @Test
    public void testIsReferenceType() {
        assertTrue(REFERENCE_INT_TYPE.isReferenceType());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType newReferenceIntTypeWithHandler = REFERENCE_INT_TYPE.withContentTypeHandler(null);
        assertEquals(Integer.TYPE, newReferenceIntTypeWithHandler.getRawClass());
        assertEquals(INT_TYPE, newReferenceIntTypeWithHandler.getContentType());

        JavaType newReferenceStringIntTypeWithHandler = new ReferenceType(int.class, STRING_TYPE, null, new Object() {}, false)
                .withContentTypeHandler(new Object() {});
        assertEquals(Integer.TYPE, newReferenceStringIntTypeWithHandler.getRawClass());
        assertNotSame(STRING_TYPE, newReferenceStringIntTypeWithHandler.getContentType());
    }

    @Test
    public void testWithValueHandler() {
        JavaType newReferenceIntTypeWithHandler = REFERENCE_INT_TYPE.withValueHandler(null);
        assertEquals(Integer.TYPE, newReferenceIntTypeWithHandler.getRawClass());
        assertEquals(INT_TYPE, newReferenceIntTypeWithHandler.getContentType());

        JavaType newReferenceIntTypeWithValueHandler = REFERENCE_INT_TYPE.withValueHandler(new Object() {});
        assertEquals(Integer.TYPE, newReferenceIntTypeWithValueHandler.getRawClass());
        assertNotSame(INT_TYPE, newReferenceIntTypeWithValueHandler.getContentType());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType newReferenceIntTypeWithHandler = REFERENCE_INT_TYPE.withContentValueHandler(null);
        assertEquals(Integer.TYPE, newReferenceIntTypeWithHandler.getRawClass());
        assertEquals(INT_TYPE, newReferenceIntTypeWithHandler.getContentType());

        JavaType newReferenceIntTypeWithContentValueHandler = REFERENCE_INT_TYPE.withContentValueHandler(new Object() {});
        assertEquals(Integer.TYPE, newReferenceIntTypeWithContentValueHandler.getRawClass());
        assertNotSame(INT_TYPE, newReferenceIntTypeWithContentValueHandler.getContentType());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType newReferenceIntTypeStatic = REFERENCE_INT_TYPE.withStaticTyping();
        assertTrue(newReferenceIntTypeStatic.isStatic());

        JavaType newReferenceIntType = new ReferenceType(Integer.TYPE, INT_TYPE, null, null, true);
        assertFalse(newReferenceIntType.isStatic());
    }

    @Test
    public void testBuildCanonicalName() {
        StringBuilder expected = new StringBuilder("class java.lang.Integer[Ljava.lang.Object;");
        assertEquals(expected.toString(), REFERENCE_INT_TYPE.buildCanonicalName());
    }

    @Test
    public void testNarrow() {
        JavaType narrowedReferenceIntType = REFERENCE_INT_TYPE.narrow(Integer.TYPE);
        assertEquals(Integer.TYPE, narrowedReferenceIntType.getRawClass());
        assertEquals(Integer.TYPE, narrowedReferenceIntType.getContentType());
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder expected = new StringBuilder("I");
        StringBuilder actual = REFERENCE_INT_TYPE.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder expected = new StringBuilder("Ljava/lang/ReferenceType;<Ljava/lang/Integer;>Ljava/lang/Object;>");
        StringBuilder actual = new StringBuilder();
        REFERENCE_INT_TYPE.getGenericSignature(actual);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testToString() {
        String expected = "[reference type, class java.lang.Integer[Ljava.lang.Object;<Ljava.lang.Integer;>Ljava.lang.Object;>]";
        String actual = REFERENCE_INT_TYPE.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        ReferenceType referenceIntType = new ReferenceType(int.class, INT_TYPE, null, null, false);
        JavaType anotherReferenceIntType = new ReferenceType(int.class, INT_TYPE, null, null, false);

        assertTrue(referenceIntType.equals(referenceIntType));
        assertTrue(referenceIntType.equals(anotherReferenceIntType));
        assertTrue(anotherReferenceIntType.equals(referenceIntType));

        JavaType differentReferenceIntType = new ReferenceType(long.class, INT_TYPE, null, null, false);
        assertFalse(referenceIntType.equals(differentReferenceIntType));
        assertFalse(differentReferenceIntType.equals(referenceIntType));

        JavaType sameReferenceIntTypeWithDifferentHandlers = new ReferenceType(int.class, INT_TYPE, null, new Object() {}, false);
        assertFalse(referenceIntType.equals(sameReferenceIntTypeWithDifferentHandlers));
        assertFalse(sameReferenceIntTypeWithDifferentHandlers.equals(referenceIntType));
    }

    @Test
    public void testContainedTypeCount() {
        assertEquals(1, REFERENCE_INT_TYPE.containedTypeCount());
    }

    @Test
    public void testContainedType() {
        JavaType containedType = REFERENCE_INT_TYPE.containedType(0);
        assertEquals(INT_TYPE, containedType);
    }

    @Test
    public void testContainedTypeName() {
        String containedTypeName = REFERENCE_INT_TYPE.containedTypeName(0);
        assertEquals("T", containedTypeName);
    }

    @Test
    public void testGetParameterSource() {
        assertEquals(Integer.TYPE, REFERENCE_INT_TYPE.getParameterSource());
    }
}