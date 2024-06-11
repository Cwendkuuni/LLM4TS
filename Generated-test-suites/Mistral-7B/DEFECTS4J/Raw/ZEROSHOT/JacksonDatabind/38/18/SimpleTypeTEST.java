package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

import java.lang.ClassNotFoundException;
import java.lang.reflect.Type;
import java.util.Arrays;

import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings.TypeBindingsBuilder;
import com.fasterxml.jackson.databind.TypeFactory;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() throws Exception {
        JavaType type = SimpleType.constructUnsafe(String.class);
        assertEquals(String.class, type.getRawClass());
        assertNull(type.getSuperClass());
        assertEquals(0, type.getGenericTypes().length);
        assertFalse(type.isContainerType());
        assertEquals("java.lang.String", type.toString());
    }

    @Test
    public void testConstruct() throws Exception {
        try {
            JavaType type = SimpleType.construct(int[].class);
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        JavaType stringType = SimpleType.construct(String.class);
        assertEquals(String.class, stringType.getRawClass());
        assertNull(stringType.getSuperClass());
        assertEquals(0, stringType.getGenericTypes().length);
        assertFalse(stringType.isContainerType());
        assertEquals("java.lang.String", stringType.toString());
    }

    @Test
    public void testNarrow() throws Exception {
        TypeBindingsBuilder bindings = TypeBindings.builder();
        JavaType javaString = TypeFactory.defaultInstance().constructType(String.class);
        JavaType superClass = TypeFactory.defaultInstance().constructType(Object.class);
        JavaType intType = TypeFactory.defaultInstance().constructType(int.class);

        JavaType stringSubclass = new SimpleType(javaString, bindings, superClass, new JavaType[] {intType});
        JavaType intTypeNarrowed = stringSubclass.narrow(int.class);
        assertEquals(intType, intTypeNarrowed);
        assertEquals(int.class, intTypeNarrowed.getRawClass());
        assertSame(intType, intTypeNarrowed.getSuperClass());
        assertEquals(new JavaType[]{}, intTypeNarrowed.getGenericTypes());
        assertFalse(intTypeNarrowed.isContainerType());
        assertEquals("int", intTypeNarrowed.toString());
    }

    @Test
    public void testIsContainerType() {
        assertFalse(new SimpleType(String.class).isContainerType());
    }

    @Test
    public void testToString() {
        JavaType stringType = SimpleType.construct(String.class);
        assertEquals("java.lang.String", stringType.toString());
    }

    @Test
    public void testEquals() throws Exception {
        JavaType stringType1 = SimpleType.construct(String.class);
        JavaType stringType2 = SimpleType.construct(String.class);
        JavaType intType = TypeFactory.defaultInstance().constructType(int.class);
        JavaType intType2 = TypeFactory.defaultInstance().constructType(int.class);

        assertEquals(stringType1, stringType1);
        assertEquals(stringType1, stringType2);
        assertEquals(stringType1.hashCode(), stringType2.hashCode());

        assertNotEquals(stringType1, intType);
        assertNotEquals(stringType1, intType2);

        assertNotEquals(stringType1, null);
    }
}