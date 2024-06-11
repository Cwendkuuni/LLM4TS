
package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ReferenceTypeTest {

    @org.junit.Test
    public void testConstruct() {
        JavaType referencedType = new JavaType(Integer.class);
        ReferenceType referenceType = ReferenceType.construct(String.class, referencedType, null, null);

        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(referencedType, referenceType.getReferencedType());
        assertFalse(referenceType.isContainerType());
        assertTrue(referenceType.isReferenceType());
        assertFalse(referenceType.isCollectionLikeType());
        assertFalse(referenceType.isMapLikeType());
        assertEquals(0, referenceType.containedTypeCount());
        assertEquals(String.class, referenceType.getParameterSource());
    }

    @org.junit.Test
    public void testWithTypeHandler() {
        JavaType referencedType = new JavaType(Boolean.class);
        BooleanHandler handler = new BooleanHandler();
        ReferenceType referenceType = ReferenceType.construct(String.class, referencedType, null, null)
                .withTypeHandler(handler);

        assertEquals(handler, referenceType.getTypeHandler());
        assertEquals(referencedType.<Object>getTypeHandler(), referenceType.getReferencedType().getTypeHandler());
        assertEquals(handler, referenceType.getContentTypeHandler());
        assertEquals(referencedType.<Object>getValueHandler(), referenceType.getReferencedType().getValueHandler());
    }

    @org.junit.Test
    public void testWithStaticTyping() {
        JavaType referencedType = new JavaType(Long.class);
        ReferenceType referenceType = ReferenceType.construct(String.class, referencedType, null, null)
                .withStaticTyping();

        assertTrue(referenceType.isStaticTyping());
        assertTrue(referenceType.getReferencedType().isStaticTyping());
    }

    @org.junit.Test
    public void testBuildCanonicalName() {
        JavaType referencedType = new JavaType(Double.class);
        ReferenceType referenceType = ReferenceType.construct(String.class, referencedType, null, null);

        assertEquals("java.lang.String<java.lang.Double>", referenceType.buildCanonicalName());
    }

    @org.junit.Test
    public void testNarrow() {
        JavaType referencedType = new JavaType(Character.class);
        ReferenceType referenceType = ReferenceType.construct(String.class, referencedType, null, null);

        JavaType subclass = new JavaType(Character.TYPE);
        referenceType = referenceType._narrow(subclass);

        assertEquals(Character.TYPE, referenceType.getRawClass());
        assertEquals(referencedType, referenceType.getReferencedType());
    }

    @org.junit.Test
    public void testToString() {
        JavaType referencedType = new JavaType(Float.class);
        ReferenceType referenceType = ReferenceType.construct(String.class, referencedType, null, null);

        String expected = "[reference type, class java.lang.String<java.lang.Float>]";
        assertEquals(expected, referenceType.toString());
    }

    @org.junit.Test
    public void testEquals() {
        JavaType referencedType1 = new JavaType(Byte.class);
        ReferenceType referenceType1 = ReferenceType.construct(String.class, referencedType1, null, null);

        JavaType referencedType2 = new JavaType(Short.class);
        ReferenceType referenceType2 = ReferenceType.construct(String.class, referencedType2, null, null);

        assertTrue(referenceType1.equals(referenceType1));
        assertFalse(referenceType1.equals(null));
        assertFalse(referenceType1.equals(new Object()));
        assertFalse(referenceType1.equals(referenceType2));
    }
}
