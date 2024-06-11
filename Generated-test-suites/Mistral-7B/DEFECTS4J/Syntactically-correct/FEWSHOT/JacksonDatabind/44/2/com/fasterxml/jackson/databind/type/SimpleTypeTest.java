package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<?> cls = String.class;
        SimpleType simpleType1 = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        SimpleType simpleType2 = new SimpleType(cls, TypeBindings.emptyBindings(), null, null, null, null, false);
        SimpleType simpleType3 = new SimpleType(cls, TypeBindings.emptyBindings(), null, null, null, null, false);

        assertEquals(simpleType1, simpleType2);
        assertEquals(simpleType1, simpleType3);
    }

    @Test
    public void testSimpleTypeWithValues() {
        Class<?> cls = List.class;
        JavaType listType = new TypeFactory().constructCollectionType(ArrayList.class, Integer.class);
        SimpleType simpleType1 = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        SimpleType simpleType2 = new SimpleType(cls, TypeBindings.bind(0, listType), null, null);

        assertNotEquals(simpleType1, simpleType2);
        assertEquals(cls, simpleType1.getRawClass());
        assertEquals(cls, simpleType2.getRawClass());
        assertEquals(listType, simpleType2.containedType(0));
    }

    @Test
    public void testSimpleTypeWithSuperClass() {
        Class<?> superClass = Object.class;
        Class<?> cls = String.class;
        SimpleType simpleType1 = new SimpleType(cls, TypeBindings.emptyBindings(), null, new JavaType[] {new SimpleType(superClass, TypeBindings.emptyBindings(), null, null)});
        SimpleType simpleType2 = new SimpleType(cls, TypeBindings.emptyBindings(), new SimpleType(superClass, TypeBindings.emptyBindings(), null, null), null);

        assertEquals(simpleType1, simpleType2);
    }

    @Test
    public void testSimpleTypeWithSuperInterfaces() {
        Class<?> interface1 = List.class;
        Class<?> interface2 = Collection.class;
        Class<?> cls = String.class;
        SimpleType simpleType1 = new SimpleType(cls, TypeBindings.emptyBindings(), null, new JavaType[] {new SimpleType(interface1, TypeBindings.emptyBindings(), null, null), new SimpleType(interface2, TypeBindings.emptyBindings(), null, null)});
        SimpleType simpleType2 = new SimpleType(cls, TypeBindings.emptyBindings(), null, Arrays.asList(new SimpleType(interface1, TypeBindings.emptyBindings(), null, null), new SimpleType(interface2, TypeBindings.emptyBindings(), null, null)).toArray(new JavaType[0]));

        assertEquals(simpleType1, simpleType2);
    }

    @Test
    public void testSimpleTypeWithBindings() {
        Class<?> cls = List.class;
        JavaType listType = new TypeFactory().constructCollectionType(ArrayList.class, Integer.class);
        SimpleType simpleType1 = new SimpleType(cls, TypeBindings.bind(0, listType), null, null);
        assertEquals(TypeBindings.bind(0, listType), simpleType1._bindings);
        assertEquals(listType, simpleType1.containedType(0));

        SimpleType simpleType2 = new SimpleType(cls, null, null, null);
        assertEquals(TypeBindings.emptyBindings(), simpleType2._bindings);
    }

    @Test
    public void testSimpleTypeWithValueHandler() {
        Class<?> cls = String.class;
        Object valueHandler = new Object() {};
        SimpleType simpleType1 = new SimpleType(cls, TypeBindings.emptyBindings(), null, null, null, valueHandler, false);
        SimpleType simpleType2 = new SimpleType(cls, TypeBindings.emptyBindings(), null, null, null, null, false);
        simpleType2 = simpleType2.withValueHandler(valueHandler);

        assertEquals(simpleType1, simpleType2);
    }

    @Test
    public void testSimpleTypeWithTypeHandler() {
        Class<?> cls = String.class;
        Object typeHandler = new Object() {};
        SimpleType simpleType1 = new SimpleType(cls, TypeBindings.emptyBindings(), null, null, null, null, false);
        SimpleType simpleType2 = new SimpleType(cls, TypeBindings.emptyBindings(), null, null, null, null, false);
        simpleType2 = simpleType2.withTypeHandler(typeHandler);

        assertEquals(simpleType1, simpleType2);
    }

    @Test
    public void testSimpleTypeWithStaticTyping() {
        Class<?> cls = String.class;
        SimpleType simpleType1 = new SimpleType(cls, TypeBindings.emptyBindings(), null, null, null, null, false);
        SimpleType simpleType2 = simpleType1.withStaticTyping();

        assertNotEquals(simpleType1, simpleType2);
        assertTrue(simpleType2._asStatic);
    }

    @Test
    public void testSimpleTypeWithNarrowing() {
        Class<?> subClass = Integer.class;
        Class<?> superClass = Number.class;
        SimpleType superType = new SimpleType(superClass, TypeBindings.emptyBindings(), null, null);
        SimpleType subType = new SimpleType(subClass, TypeBindings.emptyBindings(), null, null);

        SimpleType narrowedSubType = subType._narrow(superType);
        assertEquals(subType, narrowedSubType);
    }
}