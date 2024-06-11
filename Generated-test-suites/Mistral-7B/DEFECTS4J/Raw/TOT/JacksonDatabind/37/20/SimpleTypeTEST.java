package com.fasterxml.jackson.databind.type;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        // Test the default constructor
        SimpleType simpleType1 = new SimpleType(String.class);

        // Test the copy constructor
        SimpleType simpleType2 = new SimpleType(simpleType1);
        assertEquals(simpleType1, simpleType2);

        // Test the constructor with bindings
        TypeBindings bindings = new TypeBindings();
        SimpleType simpleType3 = new SimpleType(String.class, bindings);
        assertEquals(simpleType1, simpleType3);

        // Test the constructor with superclass and superinterfaces
        JavaType superClass = new SimpleType(List.class);
        JavaType[] superInterfaces = new JavaType[0];
        SimpleType simpleType4 = new SimpleType(String.class, bindings, superClass, superInterfaces);
        assertEquals(simpleType1, simpleType4);

        // Test the constructor with value and type handlers
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        SimpleType simpleType5 = new SimpleType(String.class, bindings, superClass, superInterfaces, valueHandler, typeHandler, false);
        assertEquals(simpleType1, simpleType5);

        // Test the constructor with static typing
        SimpleType simpleType6 = simpleType5.withStaticTyping();
        assertEquals(simpleType1, simpleType6);
    }

    @Test
    public void testConstructUnsafe() {
        // Test the constructUnsafe method
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMap() {
        // Test the construct method with a Map class
        SimpleType.construct(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollection() {
        // Test the construct method with a Collection class
        SimpleType.construct(Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArray() {
        // Test the construct method with an array class
        SimpleType.construct(int[].class);
    }

    @Test
    public void testNarrow() {
        // Test the narrow method
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType narrowedType = simpleType._narrow(String[].class);
        assertNotNull(narrowedType);
        assertEquals(String[].class, narrowedType.getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowWithInvalidSubclass() {
        // Test the narrow method with an invalid subclass
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType narrowedType = simpleType._narrow(Integer.class);
    }

    @Test
    public void testWithTypeHandler() {
        // Test the withTypeHandler method
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType withTypeHandler = simpleType.withTypeHandler(new Object());
        assertNotNull(withTypeHandler);
        assertEquals(simpleType.getClass(), withTypeHandler.getClass());
    }

    @Test
    public void testWithContentTypeHandler() {
        // Test the withContentTypeHandler method (not applicable as SimpleType has no content type)
        SimpleType simpleType = new SimpleType(String.class);
        // This test will fail as SimpleType has no content type
        simpleType.withContentTypeHandler(new Object());
    }

    @Test
    public void testWithValueHandler() {
        // Test the withValueHandler method
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType withValueHandler = simpleType.withValueHandler(new Object());
        assertNotNull(withValueHandler);
        assertEquals(simpleType.getClass(), withValueHandler.getClass());
    }

    @Test
    public void testWithContentValueHandler() {
        // Test the withContentValueHandler method (not applicable as SimpleType has no content value)
        SimpleType simpleType = new SimpleType(String.class);
        // This test will fail as SimpleType has no content value
        simpleType.withContentValueHandler(new Object());
    }

    @Test
    public void testWithStaticTyping() {
        // Test the withStaticTyping method
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType withStaticTyping = simpleType.withStaticTyping();
        assertNotNull(withStaticTyping);
        assertEquals(simpleType.getClass(), withStaticTyping.getClass());
        assertTrue(withStaticTyping.isStatic());
    }

    @Test
    public void testRefine() {
        // Test the refine method (not applicable as SimpleType is not a container type)
        SimpleType simpleType = new SimpleType(String.class);
        JavaType refinedType = simpleType.refine(String[].class, null, null, null);
        assertNull(refinedType);
    }

    @Test
    public void testBuildCanonicalName() {
        // Test the buildCanonicalName method
        SimpleType simpleType = new SimpleType(String[].class);
        String canonicalName = simpleType.buildCanonicalName();
        assertEquals("[Ljava.lang.String;", canonicalName);
    }

    @Test
    public void testGetErasedSignature() {
        // Test the getErasedSignature method
        SimpleType simpleType = new SimpleType(String[].class);
        StringBuilder sb = new StringBuilder();
        simpleType.getErasedSignature(sb);
        assertEquals("[Ljava.lang.String;", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        // Test the getGenericSignature method
        SimpleType simpleType = new SimpleType(List.class);
        StringBuilder sb = new StringBuilder();
        simpleType.getGenericSignature(sb);
        assertEquals("java.util.List", sb.toString());
    }

    @Test
    public void testToString() {
        // Test the toString method
        SimpleType simpleType = new SimpleType(String[].class);
        String stringRepresentation = simpleType.toString();
        assertEquals("[class [Ljava.lang.String;]", stringRepresentation);
    }

    @Test
    public void testEquals() {
        // Test the equals method
        SimpleType simpleType1 = new SimpleType(String.class);
        SimpleType simpleType2 = new SimpleType(String.class);
        SimpleType simpleType3 = new SimpleType(String[].class);

        // Test with the same instance
        assertTrue(simpleType1.equals(simpleType1));

        // Test with a different class
        assertFalse(simpleType1.equals(new Object()));

        // Test with the same class but different bindings
        SimpleType simpleType4 = new SimpleType(String.class
                , new TypeBindings());
        assertFalse(simpleType1.equals(simpleType4));

        // Test with the same class and same bindings but different superclass
        SimpleType simpleType5 = new SimpleType(String.class, null, new SimpleType(Object.class), null);
        assertFalse(simpleType1.equals(simpleType5));

        // Test with the same class and same bindings but different content type
        SimpleType simpleType6 = new SimpleType(String.class, null, null, new SimpleType(Object.class), null, null, false);
        assertFalse(simpleType1.equals(simpleType6));

        // Test with the same class, same bindings, and same superclass but different static typing
        SimpleType simpleType7 = simpleType6.withStaticTyping();
        assertFalse(simpleType1.equals(simpleType7));

        // Test with the same class, same bindings, same superclass, and same static typing but different hash codes
        SimpleType simpleType8 = new SimpleType(simpleType1);
        assertFalse(simpleType1.equals(simpleType8));

        // Test with the same class, same bindings, same superclass, same static typing, and same hash code
        SimpleType simpleType9 = simpleType8;
        assertTrue(simpleType1.equals(simpleType9));

        // Test with the same class, same bindings, same superclass, same static typing, and different hash codes
        SimpleType simpleType10 = new SimpleType(simpleType1);
        assertTrue(simpleType1.equals(simpleType10));

        // Test with the same class, different bindings, same superclass, and same static typing
        SimpleType simpleType11 = new SimpleType(String.class, new TypeBindings());
        assertFalse(simpleType1.equals(simpleType11));

        // Test with the same class, same bindings, different superclass, and same static typing
        SimpleType simpleType12 = new SimpleType(String.class, null, new SimpleType(Object.class), null);
        assertFalse(simpleType1.equals(simpleType12));

        // Test with the same class, same bindings, same superclass, and different static typing
        SimpleType simpleType13 = simpleType1.withStaticTyping();
        assertFalse(simpleType1.equals(simpleType13));

        // Test with the same class, same bindings, different superclass, and different static typing
        SimpleType simpleType14 = new SimpleType(String.class, null, new SimpleType(Object.class), null).withStaticTyping();
        assertFalse(simpleType1.equals(simpleType14));

        // Test with the same class, same bindings, same superclass, and same static typing but different names
        SimpleType simpleType15 = new SimpleType(String.class, null, null, null, null, null, true);
        assertFalse(simpleType1.equals(simpleType15));

        // Test with the same class, same bindings, same superclass, and same static typing but different serialVersionUID
        SimpleType simpleType16 = new SimpleType(String.class, null, null, null, null, null, true);
        simpleType16.setSerialVersionUID(1L);
        assertFalse(simpleType1.equals(simpleType16));

        // Test with the same class, same bindings, different superclass, and same static typing
        SimpleType simpleType17 = new SimpleType(String.class, null, new SimpleType(ArrayList.class), null);
        assertFalse(simpleType1.equals(simpleType17));

        // Test with the same class, same bindings, same superclass, and different static typing
        SimpleType simpleType18 = new SimpleType(String.class, null, null, null, null, null, true);
        assertFalse(simpleType1.equals(simpleType18));

        // Test with the same class, same bindings, same superclass, and same static typing but different hash codes
        SimpleType simpleType19 = new SimpleType(simpleType1);
        assertFalse(simpleType1.equals(simpleType19));

        // Test with the same class, same bindings, same superclass, same static typing, and the same hash code but different instances
        SimpleType simpleType20 = new SimpleType(simpleType1);
        assertFalse(simpleType1.equals(simpleType20));
    }
}