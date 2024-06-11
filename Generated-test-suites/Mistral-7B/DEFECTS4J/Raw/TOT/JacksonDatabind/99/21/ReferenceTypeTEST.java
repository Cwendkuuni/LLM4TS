package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SimpleType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeModifier;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    @Test
    public void testReferenceTypeCreation() {
        JavaType intType = SimpleType.construct(int.class, TypeBindings.emptyBindings());
        JavaType listIntType = TypeModifier.collectionType(intType, JavaType.ArrayType.UNBOUNDED_ARRAY);
        JavaType stringReferenceType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, new JavaType[0], listIntType);

        ReferenceType referenceType = (ReferenceType) ReferenceType.upgradeFrom(listIntType, stringReferenceType);
        assertEquals(String.class, referenceType.getContentType().getRawClass());
        assertEquals(listIntType.getClass(), referenceType.getReferencedType().getClass());

        assertTrue(referenceType.isReferenceType());
        assertFalse(referenceType.hasStaticTyping());
    }

    @Test
    public void testReferenceTypeWithContentTypeHandler() {
        JavaType intType = SimpleType.construct(int.class, TypeBindings.emptyBindings());
        JavaType listIntType = TypeModifier.collectionType(intType, JavaType.ArrayType.UNBOUNDED_ARRAY);
        JavaType stringReferenceType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, new JavaType[0], listIntType);

        JavaType stringWithIntValueHandler = stringReferenceType.withContentValueHandler(int[].class);
        ReferenceType referenceType = (ReferenceType) ReferenceType.upgradeFrom(listIntType, stringWithIntValueHandler);

        assertEquals(String.class, referenceType.getContentType().getRawClass());
        assertEquals(int[].class, referenceType.getValueHandler().getType().getComponentType());
        assertEquals(listIntType.getClass(), referenceType.getReferencedType().getClass());

        assertTrue(referenceType.isReferenceType());
        assertFalse(referenceType.hasStaticTyping());
    }

    @Test
    public void testReferenceTypeWithStaticTyping() {
        JavaType intType = SimpleType.construct(int.class, TypeBindings.emptyBindings());
        JavaType listIntType = TypeModifier.collectionType(intType, JavaType.ArrayType.UNBOUNDED_ARRAY);
        JavaType stringReferenceType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, new JavaType[0], listIntType);

        ReferenceType referenceType = (ReferenceType) ReferenceType.upgradeFrom(listIntType, stringReferenceType).withStaticTyping();

        assertEquals(String.class, referenceType.getContentType().getRawClass());
        assertEquals(int.class, referenceType.getContentType().getRawClass().getComponentType().getRawClass());
        assertEquals(listIntType.getClass(), referenceType.getReferencedType().getClass());

        assertTrue(referenceType.isReferenceType());
        assertTrue(referenceType.hasStaticTyping());
    }

    @Test
    public void testReferenceTypeEquality() {
        JavaType intType = SimpleType.construct(int.class, TypeBindings.emptyBindings());
        JavaType listIntType = TypeModifier.collectionType(intType, JavaType.ArrayType.UNBOUNDED_ARRAY);
        JavaType stringReferenceType1 = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, new JavaType[0], listIntType);
        JavaType stringReferenceType2 = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, new JavaType[0], listIntType);

        assertEquals(stringReferenceType1, stringReferenceType2);

        JavaType stringReferenceType3 = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, new JavaType[0], new JavaType[] { intType });
        assertNotEquals(stringReferenceType1, stringReferenceType3);

        JavaType intType1 = SimpleType.construct(int.class, TypeBindings.emptyBindings());
        JavaType listIntType1 = TypeModifier.collectionType(intType1, JavaType.ArrayType.UNBOUNDED_ARRAY);
        JavaType stringReferenceType4 = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, new JavaType[0], listIntType1);
        JavaType intType2 = SimpleType.construct(int.class, TypeBindings.emptyBindings());
        JavaType listIntType2 = TypeModifier.collectionType(intType2, JavaType.ArrayType.UNBOUNDED_ARRAY);
        JavaType stringReferenceType5 = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, new JavaType[0], listIntType2);

        assertNotEquals(stringReferenceType1, stringReferenceType4);
        assertNotEquals(stringReferenceType1, stringReferenceType5);
    }

    @Test
    public void testReferenceTypeUpgradeFromNull() {
        JavaType intType = SimpleType.construct(int.class, TypeBindings.emptyBindings());
        JavaType listIntType = TypeModifier.collectionType(intType, JavaType.ArrayType.UNBOUNDED_ARRAY);
        JavaType stringReferenceType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, new JavaType[0], listIntType);

        try {
            ReferenceType.upgradeFrom(null, stringReferenceType);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Missing baseType", e.getMessage());
        }
    }

    @Test
    public void testReferenceTypeUpgradeFromInvalidBaseType() {
        JavaType intType = SimpleType.construct(int.class, TypeBindings.emptyBindings());
        JavaType listIntType = TypeModifier.collectionType(intType, JavaType.ArrayType.UNBOUNDED_ARRAY);
        JavaType stringReferenceType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, new JavaType[0], listIntType);

        try {
            ReferenceType.upgradeFrom(Double.class, stringReferenceType);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not upgrade from an instance of class com.fasterxml.jackson.core.type.TypeReference$1", e.getMessage());
        }
    }

    @Test
    public void testReferenceTypeWithInvalidReferencedType() {
        JavaType intType = SimpleType.construct(int.class, TypeBindings.emptyBindings());
        JavaType listIntType = TypeModifier.collectionType(intType, JavaType.ArrayType.UNBOUNDED_ARRAY);
        JavaType stringReferenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, new JavaType[0], null);

        try {
            ReferenceType referenceType = new ReferenceType(intType.getClass(), stringReferenceType);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Missing referencedType", e.getMessage());
        }
    }

    @Test
    public void testReferenceTypeWithNullReferencedType() {
        JavaType intType = SimpleType.construct(int.class, TypeBindings.emptyBindings());
        JavaType listIntType = TypeModifier.collectionType(intType, JavaType.ArrayType.UNBOUNDED_ARRAY);
        JavaType stringReferenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, new JavaType[0], null);

        try {
            ReferenceType referenceType = new ReferenceType(intType.getClass(), stringReferenceType);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Missing referencedType", e.getMessage());
        }
    }

    @Test
    public void testReferenceTypeToString() {
        JavaType intType = SimpleType.construct(int.class, TypeBindings.emptyBindings());
        JavaType listIntType = TypeModifier.collectionType(intType, JavaType.ArrayType.UNBOUNDED_ARRAY);
        JavaType stringReferenceType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, new JavaType[0], listIntType);

        String stringRepresentation = stringReferenceType.toString();
        String expectedStringRepresentation = "[reference type, class java.lang.String]";
        assertEquals(expectedStringRepresentation, stringRepresentation);
    }
}