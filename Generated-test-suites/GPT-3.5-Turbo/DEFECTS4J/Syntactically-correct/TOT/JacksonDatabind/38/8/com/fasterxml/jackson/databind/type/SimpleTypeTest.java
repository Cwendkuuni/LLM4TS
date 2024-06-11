package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;

public class SimpleTypeTest {

    private SimpleType simpleType;

    @Before
    public void setUp() {
        simpleType = new SimpleType(SimpleTypeTest.class);
    }

    @Test
    public void testConstructor_withClass_shouldSetClass() {
        assertEquals(SimpleTypeTest.class, simpleType.getRawClass());
    }

    @Test
    public void testConstructor_withClass_shouldSetEmptyBindings() {
        assertEquals(TypeBindings.emptyBindings(), simpleType.getBindings());
    }

    @Test
    public void testConstructor_withClass_shouldSetSuperClassAsNull() {
        assertNull(simpleType.getSuperClass());
    }

    @Test
    public void testConstructor_withClass_shouldSetSuperInterfacesAsNull() {
        assertNull(simpleType.getInterfaces());
    }

    @Test
    public void testConstructor_withClass_shouldSetValueHandlerAsNull() {
        assertNull(simpleType.getValueHandler());
    }

    @Test
    public void testConstructor_withClass_shouldSetTypeHandlerAsNull() {
        assertNull(simpleType.getTypeHandler());
    }

    @Test
    public void testConstructor_withClass_shouldSetAsStaticAsFalse() {
        assertFalse(simpleType.isStatic());
    }

    @Test
    public void testConstructor_withClassBindingsSuperClassSuperInterfaces() {
        SimpleType parentType = new SimpleType(ParentType.class);
        SimpleType childType = new SimpleType(ChildType.class, parentType.getBindings(), parentType.getSuperClass(), new JavaType[] { parentType });
        assertEquals(ChildType.class, childType.getRawClass());
        assertEquals(parentType.getBindings(), childType.getBindings());
        assertEquals(parentType, childType.getSuperClass());
        assertArrayEquals(new JavaType[] { parentType }, childType.getInterfaces());
        assertNull(childType.getValueHandler());
        assertNull(childType.getTypeHandler());
        assertFalse(childType.isStatic());
    }

    @Test
    public void testConstructor_withClassBindingsSuperClassSuperInterfacesValueHandlerTypeHandlerAsStatic() {
        SimpleType parentType = new SimpleType(ParentType.class);
        SimpleType childType = new SimpleType(ChildType.class, parentType.getBindings(), parentType.getSuperClass(), new JavaType[] { parentType }, "valueHandler", "typeHandler", true);
        assertEquals(ChildType.class, childType.getRawClass());
        assertEquals(parentType.getBindings(), childType.getBindings());
        assertEquals(parentType, childType.getSuperClass());
        assertArrayEquals(new JavaType[] { parentType }, childType.getInterfaces());
        assertEquals("valueHandler", childType.getValueHandler());
        assertEquals("typeHandler", childType.getTypeHandler());
        assertTrue(childType.isStatic());
    }

    @Test
    public void testCopyConstructor_withBaseType_shouldCopyAllFields() {
        SimpleType baseType = new SimpleType(SimpleTypeTest.class);
        SimpleType copiedType = new SimpleType(baseType);
        assertEquals(SimpleTypeTest.class, copiedType.getRawClass());
        assertEquals(baseType.getBindings(), copiedType.getBindings());
        assertEquals(baseType.getSuperClass(), copiedType.getSuperClass());
        assertArrayEquals(baseType.getInterfaces(), copiedType.getInterfaces());
        assertEquals(baseType.getValueHandler(), copiedType.getValueHandler());
        assertEquals(baseType.getTypeHandler(), copiedType.getTypeHandler());
        assertEquals(baseType.isStatic(), copiedType.isStatic());
    }

    @Test
    public void testConstructUnsafe_withClass_shouldConstructSimpleType() {
        SimpleType result = SimpleType.constructUnsafe(SimpleTypeTest.class);
        assertEquals(SimpleTypeTest.class, result.getRawClass());
        assertNull(result.getBindings());
        assertNull(result.getSuperClass());
        assertNull(result.getInterfaces());
        assertNull(result.getValueHandler());
        assertNull(result.getTypeHandler());
        assertFalse(result.isStatic());
    }

    @Test
    public void testConstruct_withClass_shouldThrowExceptionForMap() {
        try {
            SimpleType.construct(Map.class);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Map (class: java.util.Map)", e.getMessage());
        }
    }

    @Test
    public void testConstruct_withClass_shouldThrowExceptionForCollection() {
        try {
            SimpleType.construct(Collection.class);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Collection (class: java.util.Collection)", e.getMessage());
        }
    }

    @Test
    public void testConstruct_withClass_shouldThrowExceptionForArray() {
        try {
            SimpleType.construct(Object[].class);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for an array (class: [Ljava.lang.Object;)", e.getMessage());
        }
    }

    @Test
    public void testEquals_withSameObject_shouldReturnTrue() {
        assertTrue(simpleType.equals(simpleType));
    }

    @Test
    public void testEquals_withEqualObjects_shouldReturnTrue() {
        SimpleType otherType = new SimpleType(SimpleTypeTest.class);
        assertTrue(simpleType.equals(otherType));
    }

    @Test
    public void testEquals_withNull_shouldReturnFalse() {
        assertFalse(simpleType.equals(null));
    }

    @Test
    public void testEquals_withDifferentClass_shouldReturnFalse() {
        SimpleType otherType = new SimpleType(ParentType.class);
        assertFalse(simpleType.equals(otherType));
    }

    @Test
    public void testEquals_withDifferentBindings_shouldReturnFalse() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        SimpleType otherType = new SimpleType(SimpleTypeTest.class, bindings, null, null);
        assertFalse(simpleType.equals(otherType));
    }

    @Test
    public void testToString_shouldIncludeCanonicalName() {
        String expected = "[simple type, class com.fasterxml.jackson.databind.type.SimpleTypeTest]";
        assertEquals(expected, simpleType.toString());
    }
}