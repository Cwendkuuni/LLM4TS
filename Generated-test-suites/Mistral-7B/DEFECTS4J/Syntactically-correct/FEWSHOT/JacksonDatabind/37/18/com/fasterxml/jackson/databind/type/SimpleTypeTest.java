package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<String> stringClass = String.class;
        SimpleType stringType = new SimpleType(stringClass);
        assertEquals(String.class, stringType.getRawClass());
        assertNull(stringType.getSuperClass());
        assertNull(stringType.getSuperInterfaces());
        assertFalse(stringType.isContainerType());
        assertEquals(stringType.toString(), "[simple type, class " + String.class.getName() + "]");
        assertEquals(stringType.hashCode(), stringType.getClass().hashCode());
        assertTrue(stringType.equals(stringType));
        assertFalse(stringType.equals(null));
        assertFalse(stringType.equals("not a type"));
        SimpleType stringType2 = new SimpleType(stringClass);
        assertTrue(stringType.equals(stringType2));
    }

    @Test
    public void testSimpleTypeWithBindings() {
        Class<List<Integer>> listIntClass = List.class.getTypeParameters()[0].getType();
        JavaType javaType = new SimpleType(listIntClass, TypeBindings.defaultBindings());
        assertEquals(List.class, javaType.getRawClass());
        assertEquals(List.class, javaType.getSuperClass());
        assertArrayEquals(new Class[]{Collection.class, Object.class}, javaType.getSuperInterfaces());
        assertFalse(javaType.isContainerType());
        assertEquals(javaType.toString(), "[simple type, class java.util.List, bounds [class java.lang.Integer]]");
        assertEquals(javaType.hashCode(), javaType.getClass().hashCode());
        assertTrue(javaType.equals(javaType));
        assertFalse(javaType.equals(null));
        assertFalse(javaType.equals("not a type"));
        SimpleType listIntType2 = new SimpleType(listIntClass, TypeBindings.defaultBindings());
        assertTrue(javaType.equals(listIntType2));
    }

    @Test
    public void testSimpleTypeWithSuperClass() {
        Class<Number> numberClass = Number.class;
        SimpleType numberType = new SimpleType(numberClass, TypeBindings.emptyBindings(), numberClass.getSuperclass());
        assertEquals(Number.class, numberType.getRawClass());
        assertEquals(Number.class.getSuperclass(), numberType.getSuperClass());
        assertArrayEquals(new Class[0], numberType.getSuperInterfaces());
        assertFalse(numberType.isContainerType());
        assertEquals(numberType.toString(), "[simple type, class java.lang.Number, superclass class java.lang.Number]");
        assertEquals(numberType.hashCode(), numberType.getClass().hashCode());
        assertTrue(numberType.equals(numberType));
        assertFalse(numberType.equals(null));
        assertFalse(numberType.equals("not a type"));
        SimpleType numberType2 = new SimpleType(numberClass, TypeBindings.emptyBindings(), numberClass.getSuperclass());
        assertTrue(numberType.equals(numberType2));
    }

    @Test
    public void testSimpleTypeWithSuperInterfaces() {
        Class<Comparable<Integer>> comparableIntClass = Comparable.class.getTypeParameters()[0].getType();
        JavaType javaType = new SimpleType(comparableIntClass, TypeBindings.emptyBindings(), null, new Class[]{Comparable.class});
        assertEquals(Comparable.class, javaType.getRawClass());
        assertNull(javaType.getSuperClass());
        assertArrayEquals(new Class[]{Comparable.class}, javaType.getSuperInterfaces());
        assertFalse(javaType.isContainerType());
        assertEquals(javaType.toString(), "[simple type, class java.lang.Comparable, interfaces [class java.lang.Comparable]]");
        assertEquals(javaType.hashCode(), javaType.getClass().hashCode());
        assertTrue(javaType.equals(javaType));
        assertFalse(javaType.equals(null));
        assertFalse(javaType.equals("not a type"));
        SimpleType comparableIntType2 = new SimpleType(comparableIntClass, TypeBindings.emptyBindings(), null, new Class[]{Comparable.class});
        assertTrue(javaType.equals(comparableIntType2));
    }

    @Test
    public void testSimpleTypeWithValueHandler() {
        Class<String> stringClass = String.class;
        SimpleType stringType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null, new StringSerializer(), null, false);
        assertEquals(String.class, stringType.getRawClass());
        assertNull(stringType.getSuperClass());
        assertNull(stringType.getSuperInterfaces());
        assertFalse(stringType.isContainerType());
        assertEquals(stringType.toString(), "[simple type, class java.lang.String, handler class com.fasterxml.jackson.databind.ser.std.StringSerializer]");
        assertEquals(stringType.hashCode(), stringType.getClass().hashCode());
        assertTrue(stringType.equals(stringType));
        assertFalse(stringType.equals(null));
        assertFalse(stringType.equals("not a type"));
        SimpleType stringType2 = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null, new StringSerializer(), null, false);
        assertTrue(stringType.equals(stringType2));
    }

    @Test
    public void testSimpleTypeWithTypeHandler() {
        Class<Date> dateClass = Date.class;
        SimpleType dateType = new SimpleType(dateClass, TypeBindings.emptyBindings(), null, null, null, new DateDeserializer(), false);
        assertEquals(Date.class, dateType.getRawClass());
        assertNull(dateType.getSuperClass());
        assertNull(dateType.getSuperInterfaces());
        assertFalse(dateType.isContainerType());
        assertEquals(dateType.toString(), "[simple type, class java.util.Date, handler class com.fasterxml.jackson.databind.deser.std.DateDeserializer]");
        assertEquals(dateType.hashCode(), dateType.getClass().hashCode());
        assertTrue(dateType.equals(dateType));
        assertFalse(dateType.equals(null));
        assertFalse(dateType.equals("not a type"));
        SimpleType dateType2 = new SimpleType(dateClass, TypeBindings.emptyBindings(), null, null, null, new DateDeserializer(), false);
        assertTrue(dateType.equals(dateType2));
    }

    @Test
    public void testSimpleTypeWithStaticTyping() {
        Class<Integer> integerClass = Integer.class;
        SimpleType intType = new SimpleType(integerClass, TypeBindings.emptyBindings(), null, null, null, null, true);
        assertEquals(Integer.class, intType.getRawClass());
        assertNull(intType.getSuperClass());
        assertNull(intType.getSuperInterfaces());
        assertTrue(intType.isContainerType());
        assertEquals(intType.toString(), "[simple type, class java.lang.Integer, static]");
        assertEquals(intType.hashCode(), intType.getClass().hashCode());
        assertTrue(intType.equals(intType));
        assertFalse(intType.equals(null));
        assertFalse(intType.equals("not a type"));
        SimpleType intType2 = new SimpleType(integerClass, TypeBindings.emptyBindings(), null, null, null, null, true);
        assertTrue(intType.equals(intType2));
    }

    @Test
    public void testSimpleTypeWithContentTypeHandler() {
        Class<String> stringClass = String.class;
        SimpleType stringType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null, null, null, false, new StringDeserializer());
        assertEquals(String.class, stringType.getRawClass());
        assertNull(stringType.getSuperClass());
        assertNull(stringType.getSuperInterfaces());
        assertFalse(stringType.isContainerType());
        assertEquals(stringType.toString(), "[simple type, class java.lang.String, handler class com.fasterxml.jackson.databind.deser.std.StringDeserializer]");
        assertEquals(stringType.hashCode(), stringType.getClass().hashCode());
        assertTrue(stringType.equals(stringType));
        assertFalse(stringType.equals(null));
        assertFalse(stringType.equals("not a type"));
        SimpleType stringType2 = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null, null, null, false, new StringDeserializer());
        assertTrue(stringType.equals(stringType2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructMapOrCollectionType() {
        Map.class.isAssignableFrom(new SimpleType(String.class).getClass());
        Collection.class.isAssignableFrom(new SimpleType(String[].class).getClass());
    }
}