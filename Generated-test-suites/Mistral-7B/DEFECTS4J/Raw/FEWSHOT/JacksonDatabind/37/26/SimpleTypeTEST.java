package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstructors() {
        Class<String> stringClass = String.class;
        SimpleType stringType = new SimpleType(stringClass);
        assertNotNull(stringType);
        assertTrue(stringType.isContainerType()); // false by default

        SimpleType stringType2 = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null);
        assertNotNull(stringType2);
        assertTrue(stringType2.isContainerType()); // false by default

        SimpleType stringType3 = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null, null, null, false);
        assertNotNull(stringType3);
        assertFalse(stringType3.isContainerType()); // false by default

        SimpleType stringType4 = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null, null, null, true);
        assertNotNull(stringType4);
        assertTrue(stringType4.isContainerType()); // false by default

        SimpleType stringType5 = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null, null, null, false);
        assertNotNull(stringType5);
        assertFalse(stringType5.isContainerType()); // false by default

        SimpleType stringType6 = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null, null, null, true);
        assertNotNull(stringType6);
        assertTrue(stringType6.isContainerType()); // false by default
    }

    @Test
    public void testConstructUnsafeAndConstruct() {
        Class<String> stringClass = String.class;
        SimpleType stringType = SimpleType.constructUnsafe(String.class);
        assertNotNull(stringType);
        assertTrue(stringType.isContainerType()); // false by default

        try {
            SimpleType stringType2 = SimpleType.construct(String.class);
            fail("Should not reach this point, as method is deprecated");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testNarrow() {
        Class<String> stringClass = String.class;
        Class<Integer> intClass = Integer.class;
        SimpleType stringType = new SimpleType(stringClass);
        SimpleType intType = new SimpleType(intClass);
        SimpleType narrowedIntType = stringType._narrow(intClass);
        assertNull(narrowedIntType);

        SimpleType narrowedStringType = intType._narrow(stringClass);
        assertNotNull(narrowedStringType);
        assertEquals(stringType, narrowedStringType);
    }

    @Test
    public void testIsContainerType() {
        Class<String> stringClass = String.class;
        SimpleType stringType = new SimpleType(stringClass);
        assertFalse(stringType.isContainerType());

        Class<List<String>> listStringClass = List.class.getTypeParameters()[0].getType();
        SimpleType listStringType = new SimpleType(listStringClass);
        assertTrue(listStringType.isContainerType());
    }

    @Test
    public void testGetErasedSignatureAndGetGenericSignature() {
        Class<List<String>> listStringClass = List.class.getTypeParameters()[0].getType();
        SimpleType listStringType = new SimpleType(listStringClass);

        StringBuilder sb = new StringBuilder();
        listStringType.getErasedSignature(sb);
        assertEquals("[Ljava.lang.String;", sb.toString());

        sb.setLength(0);
        listStringType.getGenericSignature(sb);
        assertEquals("[Ljava.lang.String;", sb.toString());
    }

    @Test
    public void testToString() {
        Class<List<String>> listStringClass = List.class.getTypeParameters()[0].getType();
        SimpleType listStringType = new SimpleType(listStringClass);

        assertEquals("[simple type, class [Ljava.lang.String;]", listStringType.toString());
    }

    @Test
    public void testEquals() {
        Class<List<String>> listStringClass = List.class.getTypeParameters()[0].getType();
        SimpleType listStringType1 = new SimpleType(listStringClass);
        SimpleType listStringType2 = new SimpleType(listStringClass);
        SimpleType listIntType = new SimpleType(Integer.TYPE);

        assertTrue(listStringType1.equals(listStringType1));
        assertTrue(listStringType1.equals(listStringType2));
        assertFalse(listStringType1.equals(listIntType));
    }
}