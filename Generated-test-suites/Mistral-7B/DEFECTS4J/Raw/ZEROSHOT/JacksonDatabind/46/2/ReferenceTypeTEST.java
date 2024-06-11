package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.ClassCastException;
import java.lang.reflect.Type;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.databind.type.ReferenceType;

public class ReferenceTypeTest {

    @Test
    public void testReferenceTypeConstruction() {
        Class<String> stringClass = String.class;
        JavaType stringType = SimpleType.construct(String.class);
        ReferenceType referenceType = ReferenceType.construct(stringClass, stringType, null, null);
        assertNotNull(referenceType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReferenceTypeWithNullClass() {
        ReferenceType.construct(null, new ClassType(String.class), null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReferenceTypeWithNullReferencedType() {
        ReferenceType.construct(String.class, null, null, null);
    }

    @Test
    public void testGetReferencedType() {
        Class<String> stringClass = String.class;
        JavaType stringType = SimpleType.construct(String.class);
        ReferenceType referenceType = ReferenceType.construct(stringClass, stringType, null, null);
        JavaType referencedType = referenceType.getReferencedType();
        assertNotNull(referencedType);
        assertTrue(stringType.isSameOrSameTypeAs(referencedType));
    }

    @Test
    public void testIsReferenceType() {
        Class<String> stringClass = String.class;
        JavaType stringType = SimpleType.construct(String.class);
        ReferenceType referenceType = ReferenceType.construct(stringClass, stringType, null, null);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testContainsType() {
        Class<String> stringClass = String.class;
        JavaType stringType = SimpleType.construct(String.class);
        ReferenceType referenceType = ReferenceType.construct(stringClass, stringType, null, null);
        JavaType javaIntType = SimpleType.construct(int.class);
        assertFalse(referenceType.containsType(javaIntType));
        JavaType stringReferenceType = ReferenceType.construct(String.class, stringType, null, null);
        assertTrue(referenceType.containsType(stringReferenceType));
        assertTrue(stringReferenceType.containsType(referenceType));
    }

    @Test
    public void testToString() {
        Class<String> stringClass = String.class;
        JavaType stringType = SimpleType.construct(String.class);
        ReferenceType referenceType = ReferenceType.construct(stringClass, stringType, null, null);
        String expected = "[reference type, class " + stringClass.getName() + " <" + stringType + ">]";
        String actual = referenceType.toString();
        assertTrue(expected.equals(actual));
    }

    @Test
    public void testEquals() {
        Class<String> stringClass1 = String.class;
        JavaType stringType1 = SimpleType.construct(String.class);
        ReferenceType referenceType1 = ReferenceType.construct(stringClass1, stringType1, null, null);

        Class<String> stringClass2 = String.class;
        JavaType stringType2 = SimpleType.construct(String.class);
        ReferenceType referenceType2 = ReferenceType.construct(stringClass2, stringType2, null, null);

        Class<Integer> intClass = Integer.class;
        JavaType intType = SimpleType.construct(Integer.class);
        ReferenceType referenceType3 = new ReferenceType(intClass, stringType1, null, null);

        Class<Double> doubleClass = Double.class;
        JavaType doubleType = SimpleType.construct(Double.class);
        ReferenceType referenceType4 = new ReferenceType(doubleClass, stringType1, null, null);

        assertFalse(referenceType1.equals(null));
        assertFalse(referenceType1.equals(""));
        assertFalse(referenceType1.equals(intType));
        assertFalse(referenceType1.equals(referenceType3));
        assertFalse(referenceType1.equals(referenceType4));

        assertTrue(referenceType1.equals(referenceType1));
        assertTrue(referenceType1.equals(referenceType2));
    }

    @Test(expected = ClassCastException.class)
    public void testGetReferencedTypeFromIncompatibleType() {
        Class<String> stringClass = String.class;
        JavaType stringType = SimpleType.construct(String.class);
        ReferenceType referenceType = ReferenceType.construct(stringClass, stringType, null, null);
        Class<Integer> intClass = Integer.class;
        JavaType intType = ReferenceType.construct(intClass, stringType, null, null);
        JavaType referencedType = referenceType.getReferencedType();
        intType.getReferencedType(); // this call is ok, but the following one is not
        referencedType;
    }
}