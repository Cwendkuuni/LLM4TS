package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testReferenceTypeConstruction() {
        JavaType baseType = new SimpleType(String.class);
        JavaType refType = new SimpleType(Integer.class);
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(Integer.class, referenceType.getContentType().getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReferenceTypeConstructionWithNullBaseType() {
        JavaType refType = new SimpleType(Integer.class);
        ReferenceType.upgradeFrom(null, refType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReferenceTypeConstructionWithNullReferencedType() {
        JavaType baseType = new SimpleType(String.class);
        ReferenceType.upgradeFrom(baseType, null);
    }

    @Test
    public void testReferenceTypeGetContentType() {
        JavaType baseType = new SimpleType(String.class);
        JavaType refType = new SimpleType(Integer.class);
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
        assertEquals(Integer.class, referenceType.getContentType().getRawClass());
    }

    @Test
    public void testReferenceTypeIsReferenceType() {
        JavaType baseType = new SimpleType(String.class);
        JavaType refType = new SimpleType(Integer.class);
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testReferenceTypeHasContentType() {
        JavaType baseType = new SimpleType(String.class);
        JavaType refType = new SimpleType(Integer.class);
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
        assertTrue(referenceType.hasContentType());
    }

    @Test
    public void testReferenceTypeGetErasedSignature() {
        JavaType baseType = new SimpleType(String[].class);
        JavaType refType = new SimpleType(List.class);
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
        assertEquals("[Ljava/lang/Object;", referenceType.getErasedSignature().toString());
    }

    @Test
    public void testReferenceTypeGetGenericSignature() {
        JavaType baseType = new SimpleType(String[].class);
        JavaType refType = new SimpleType(new TypeReference<List<String>>() {});
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
        assertEquals("[Ljava/util/List;<Ljava/lang/String;>;>",
                referenceType.getGenericSignature().toString());
    }

    @Test
    public void testReferenceTypeToString() {
        JavaType baseType = new SimpleType(String[].class);
        JavaType refType = new SimpleType(new TypeReference<List<String>>() {});
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
        String expected = "[reference type, class [Ljava.lang.String;<Ljava.util.List;>;>";
        assertEquals(expected, referenceType.toString());
    }

    @Test
    public void testReferenceTypeEquals() {
        JavaType baseType1 = new SimpleType(String[].class);
        JavaType refType1 = new SimpleType(new TypeReference<List<String>>() {});
        JavaType baseType2 = new SimpleType(String[].class);
        JavaType refType2 = new SimpleType(new TypeReference<List<String>>() {});
        ReferenceType referenceType1 = ReferenceType.upgradeFrom(baseType1, refType1);
        ReferenceType referenceType2 = ReferenceType.upgradeFrom(baseType2, refType2);
        assertTrue(referenceType1.equals(referenceType2));
    }

    @Test
    public void testReferenceTypeGetAnchorType() {
        JavaType baseType = new SimpleType(String[].class);
        JavaType refType = new SimpleType(new TypeReference<List<String>>() {});
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
        assertNull(referenceType.getAnchorType());

        JavaType baseAnchorType = new SimpleType(List.class);
        JavaType refTypeAnchor = new SimpleType(new TypeReference<List<String>>() {});
        ReferenceType referenceTypeAnchor = ReferenceType.construct(List.class, TypeBindings.emptyBindings(), null, null, refTypeAnchor);
        ReferenceType referenceTypeWithAnchor = ReferenceType.upgradeFrom(baseAnchorType, refType);
        assertEquals(referenceTypeAnchor, referenceTypeWithAnchor.getAnchorType());
    }

    @Test
    public void testReferenceTypeIsAnchorType() {
        JavaType baseType = new SimpleType(String[].class);
        JavaType refType = new SimpleType(new TypeReference<List<String>>() {});
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
        assertFalse(referenceType.isAnchorType());

        JavaType baseAnchorType = new SimpleType(List.class);
        JavaType refTypeAnchor = new SimpleType(new TypeReference<List<String>>() {});
        ReferenceType referenceTypeAnchor = ReferenceType.construct(List.class, TypeBindings.emptyBindings(), null, null, refTypeAnchor);
        ReferenceType referenceTypeWithAnchor = ReferenceType.upgradeFrom(baseAnchorType, refType);
        assertTrue(referenceTypeWithAnchor.isAnchorType());
    }
}