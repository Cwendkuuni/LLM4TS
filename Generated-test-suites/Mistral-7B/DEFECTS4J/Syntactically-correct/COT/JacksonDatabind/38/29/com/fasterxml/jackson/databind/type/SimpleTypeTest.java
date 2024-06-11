package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstructor() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        SimpleType expectedSimpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        assertEquals(expectedSimpleType.toString(), simpleType.toString());
    }

    @Test
    public void testCopyConstructor() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        SimpleType copiedSimpleType = new SimpleType(simpleType);
        assertEquals(simpleType.toString(), copiedSimpleType.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructMethodNotToBeUsedByApplicationCode() {
        Class<?> cls = String.class;
        SimpleType.construct(cls);
    }

    @Test
    public void testNarrowMethod() {
        Class<?> superClass = String[].class;
        Class<?> subclass = String.class;
        SimpleType superSimpleType = new SimpleType(superClass);
        SimpleType subSimpleType = new SimpleType(subclass);

        JavaType narrowedType = superSimpleType.narrow(subclass);
        JavaType expectedNarrowedType = new SimpleType(subclass, TypeBindings.emptyBindings(), superSimpleType, null, null, null, false);
        assertEquals(expectedNarrowedType.toString(), narrowedType.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTypes() {
        Class<?> cls = Map.class;
        SimpleType simpleType = new SimpleType(cls);
    }
}