package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.lang.reflect.Type;
import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.ReferenceType;

public class ReferenceTypeTest {

    @Test
    public void testReferenceTypeConstruction() {
        JavaType refType = new TypeReference<Integer>() {}.getType();
        JavaType baseType = new TypeReference<ReferenceType<Integer>>() {}.getType();
        ReferenceType<Integer> referenceType = ReferenceType.upgradeFrom(baseType, refType);
        assertEquals(Integer.class, referenceType.getContentType().getRawClass());
        assertEquals(ReferenceType.class, referenceType.getClass());
        assertEquals(refType, referenceType.getReferencedType());
    }

    @Test
    public void testReferenceTypeMethods() {
        JavaType refType = new TypeReference<Integer>() {}.getType();
        JavaType baseType = new TypeReference<ReferenceType<Integer>>() {}.getType();
        ReferenceType<Integer> referenceType = ReferenceType.upgradeFrom(baseType, refType);

        JavaType withContentType = referenceType.withContentType(String.class);
        assertEquals(String.class, withContentType.getContentType().getRawClass());
        assertEquals(ReferenceType.class, withContentType.getClass());
        assertEquals(refType, withContentType.getReferencedType());

        JavaType withTypeHandler = referenceType.withTypeHandler(new Object() {});
        assertEquals(ReferenceType.class, withTypeHandler.getClass());
        assertEquals(refType, withTypeHandler.getReferencedType());

        JavaType withContentTypeHandler = referenceType.withContentTypeHandler(new Object() {});
        assertEquals(ReferenceType.class, withContentTypeHandler.getClass());
        assertEquals(new TypeReference<String>() {}.getType(), withContentTypeHandler.getReferencedType());

        JavaType withValueHandler = referenceType.withValueHandler(new Object() {});
        assertEquals(ReferenceType.class, withValueHandler.getClass());
        assertEquals(refType, withValueHandler.getReferencedType());

        JavaType withContentValueHandler = referenceType.withContentValueHandler(new Object() {});
        assertEquals(ReferenceType.class, withContentValueHandler.getClass());
        assertEquals(new TypeReference<Integer>() {}.getType(), withContentValueHandler.getReferencedType());

        JavaType withStaticTyping = referenceType.withStaticTyping();
        assertEquals(ReferenceType.class, withStaticTyping.getClass());
        assertTrue(withStaticTyping.isStatic());

        JavaType refined = referenceType.refine(Integer[].class, TypeBindings.emptyBindings(), null, new Type[0]);
        assertEquals(JavaType.constructParametricType(ReferenceType.class, Integer[].class, refType), refined);
    }

    @Test
    public void testFactoryMethods() {
        JavaType refType = new TypeReference<Integer>() {}.getType();
        JavaType baseType = new TypeReference<ReferenceType<Integer>>() {}.getType();
        JavaType result = ReferenceType.construct(Integer.class, TypeBindings.emptyBindings(), null, new Type[0], refType);
        assertEquals(Integer.class, result.getRawClass());
        assertEquals(ReferenceType.class, result.getClass());
        assertEquals(refType, result.getReferencedType());

        result = ReferenceType.construct(Integer.class, TypeBindings.emptyBindings(), null, new Type[0], refType, null);
        assertEquals(Integer.class, result.getRawClass());
        assertEquals(ReferenceType.class, result.getClass());
        assertEquals(refType, result.getReferencedType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFactoryMethodsWrongArguments() {
        JavaType refType = new TypeReference<Integer>() {}.getType();
        JavaType baseType = new TypeReference<String>() {}.getType();
        JavaType result = ReferenceType.upgradeFrom(baseType, refType);
    }

    @Test
    public void testToString() {
        JavaType refType = new TypeReference<Integer>() {}.getType();
        JavaType baseType = new TypeReference<ReferenceType<Integer>>() {}.getType();
        ReferenceType<Integer> referenceType = ReferenceType.upgradeFrom(baseType, refType);
        String toString = referenceType.toString();
        assertTrue(toString.startsWith("[reference type, class "));
        assertTrue(toString.endsWith("]"));
        assertTrue(toString.contains(Integer.class.getName()));
        assertTrue(toString.contains(refType.toCanonical()));
    }

    @Test
    public void testEquals() {
        JavaType refType1 = new TypeReference<Integer>() {}.getType();
        JavaType baseType1 = new TypeReference<ReferenceType<Integer>>() {}.getType();
        JavaType refType2 = new TypeReference<Integer>() {}.getType();
        JavaType baseType2 = new TypeReference<ReferenceType<Integer>>() {}.getType();
        ReferenceType<Integer> referenceType1 = ReferenceType.upgradeFrom(baseType1, refType1);
        ReferenceType<Integer> referenceType2 = ReferenceType.upgradeFrom(baseType2, refType2);

        assertEquals(referenceType1, referenceType1);
        assertFalse(referenceType1.equals(null));
        assertFalse(referenceType1.equals(new Object() {}));
        assertTrue(referenceType1.equals(referenceType1));
        assertTrue(referenceType1.equals(referenceType1));
        assertFalse(referenceType1.equals(referenceType2));
        assertFalse(referenceType1.equals(new TypeReference<String>() {}.getType()));
    }
}