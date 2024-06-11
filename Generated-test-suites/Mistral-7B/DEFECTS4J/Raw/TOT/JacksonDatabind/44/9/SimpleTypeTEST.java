package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<String> stringClass = String.class;
        SimpleType stringType = new SimpleType(stringClass);
        assertEquals(String.class, stringType.getType());
        assertEquals(String.class.getName(), stringType.getRawClass());
        assertFalse(stringType.isContainerType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeInvalidConstruction() {
        Class<List<String>> stringListClass = new ArrayList<String>().getClass();
        new SimpleType(stringListClass);
    }

    @Test
    public void testSimpleTypeWithBindings() {
        Class<List<String>> stringListClass = new ArrayList<String>().getClass();
        SimpleType stringListType = new SimpleType(stringListClass, TypeFactory.defaultInstance().unboundMapper().getTypeFactory());
        assertEquals(List.class, stringListType.getType());
        assertEquals(ArrayList.class, stringListType.getRawClass());
        assertFalse(stringListType.isContainerType());
        assertEquals(TypeReference.collectionValueType(String.class), stringListType.containedType(0));
    }

    @Test
    public void testSimpleTypeWithSuperClass() {
        Class<SubClass> subClass = new SubClass();
        SimpleType simpleType = new SimpleType(SubClass.class, TypeFactory.defaultInstance().unboundMapper().getTypeFactory(),
                TypeFactory.defaultInstance().unboundMapper().getTypeFactory().constructType(SuperClass.class));
        assertEquals(SubClass.class, simpleType.getType());
        assertEquals(SubClass.class, simpleType.getRawClass());
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testSimpleTypeWithValueHandler() {
        SimpleType simpleType = new SimpleType(String.class, TypeFactory.defaultInstance().unboundMapper().getTypeFactory(), null, null, null, new StringValueHandler(), false);
        assertEquals(String.class, simpleType.getType());
        assertEquals(String.class, simpleType.getRawClass());
        assertFalse(simpleType.isContainerType());
        assertEquals(new StringValueHandler(), simpleType.getValueHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeWithInvalidValueHandler() {
        SimpleType simpleType = new SimpleType(String.class, TypeFactory.defaultInstance().unboundMapper().getTypeFactory(), null, null, null, new Object(), false);
    }

    @Test
    public void testSimpleTypeWithTypeHandler() {
        SimpleType simpleType = new SimpleType(String.class, TypeFactory.defaultInstance().unboundMapper().getTypeFactory(), null, null, null, null, true);
        assertEquals(String.class, simpleType.getType());
        assertEquals(String.class, simpleType.getRawClass());
        assertFalse(simpleType.isContainerType());
        assertNull(simpleType.getValueHandler());
        assertEquals(new StringTypeHandler(), simpleType.getTypeHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeWithInvalidTypeHandler() {
        SimpleType simpleType = new SimpleType(String.class, TypeFactory.defaultInstance().unboundMapper().getTypeFactory(), null, null, null, null, true);
        simpleType.withTypeHandler(new Object());
    }

    @Test
    public void testSimpleTypeWithStaticTyping() {
        SimpleType simpleType = new SimpleType(String.class, TypeFactory.defaultInstance().unboundMapper().getTypeFactory(), null, null, null, null, false);
        SimpleType staticType = simpleType.withStaticTyping();
        assertEquals(String.class, staticType.getType());
        assertEquals(String.class, staticType.getRawClass());
        assertTrue(staticType.isContainerType());
        assertNull(staticType.getValueHandler());
        assertNull(staticType.getTypeHandler());
    }

    @Test
    public void testSimpleTypeWithValueHandlerSetter() {
        SimpleType simpleType = new SimpleType(String.class, TypeFactory.defaultInstance().unboundMapper().getTypeFactory(), null, null, null, null, false);
        SimpleType withValueHandler = simpleType.withValueHandler(new StringValueHandler());
        assertEquals(String.class, withValueHandler.getType());
        assertEquals(String.class, withValueHandler.getRawClass());
        assertFalse(withValueHandler.isContainerType());
        assertEquals(new StringValueHandler(), withValueHandler.getValueHandler());
        assertEquals(new StringTypeHandler(), withValueHandler.getTypeHandler());
    }

    @Test
    public void testSimpleTypeWithTypeHandlerSetter() {
        SimpleType simpleType = new SimpleType(String.class, TypeFactory.defaultInstance().unboundMapper().getTypeFactory(), null, null, null, null, false);
        SimpleType withTypeHandler = simpleType.withTypeHandler(new StringTypeHandler());
        assertEquals(String.class, withTypeHandler.getType());
        assertEquals(String.class, withTypeHandler.getRawClass());
        assertFalse(withTypeHandler.isContainerType());
        assertNull(withTypeHandler.getValueHandler());
        assertEquals(new StringTypeHandler(), withTypeHandler.getTypeHandler());
    }

    @Test
    public void testSimpleTypeWithValueHandlerAndTypeHandlerSetters() {
        SimpleType simpleType = new SimpleType(String.class, TypeFactory.defaultInstance().unboundMapper().getTypeFactory(), null, null, null, null, false);
        SimpleType withValueHandler = simpleType.withValueHandler(new StringValueHandler());
        SimpleType withTypeHandler = withValueHandler.withTypeHandler(new StringTypeHandler());
        assertEquals(String.class, withTypeHandler.getType());
        assertEquals(String.class, withTypeHandler.getRawClass());
        assertFalse(withTypeHandler.isContainerType());
        assertEquals(new StringValueHandler(), withTypeHandler.getValueHandler());
        assertEquals(new StringTypeHandler(), withTypeHandler.getTypeHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeInvalidWithTypeHandler() {
        SimpleType simpleType = new SimpleType(String.class, TypeFactory.defaultInstance().unboundMapper().getTypeFactory(), null, null, null, null, false);
        simpleType.withTypeHandler(new Object());
    }

    @Test
    public void testSimpleTypeWithStaticTypingSetter() {
        SimpleType simpleType = new SimpleType(String.class, TypeFactory.defaultInstance().unboundMapper().getTypeFactory(), null, null, null, null, false);
        SimpleType withStaticTyping = simpleType.withStaticTyping();
        assertEquals(String.class, withStaticTyping.getType());
        assertEquals(String.class, withStaticTyping.getRawClass());
        assertTrue(withStaticTyping.isContainerType());
        assertNull(withStaticTyping.getValueHandler());
        assertNull(withStaticTyping.getTypeHandler());
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<List<String>> stringListClass = new ArrayList<String>().getClass();
        SimpleType stringListType = new SimpleType(stringListClass, TypeFactory.defaultInstance().unboundMapper().getTypeFactory());
        SimpleType copiedStringListType = new SimpleType(stringListType);
        assertEquals(List.class, copiedStringListType.getType());
        assertEquals(ArrayList.class, copiedStringListType.getRawClass());
        assertFalse(copiedStringListType.isContainerType());
        assertEquals(TypeReference.collectionValueType(String.class), copiedStringListType.containedType(0));
    }

    @Test
    public void testSimpleTypeCopyConstructorWithBindings() {
        Class<List<String>> stringListClass = new ArrayList<String>().getClass();
        SimpleType stringListType = new SimpleType(stringListClass, TypeFactory.defaultInstance().unboundMapper().getTypeFactory());
        SimpleType copiedStringListType = new SimpleType(stringListType, TypeFactory.defaultInstance().unboundMapper().getTypeFactory());
        assertEquals(List.class, copiedStringListType.getType());
        assertEquals(ArrayList.class, copiedStringListType.getRawClass());
        assertFalse(copiedStringListType.isContainerType());
        assertEquals(TypeReference.collectionValueType(String.class), copiedStringListType.containedType(0));
        assertSame(stringListType._bindings, copiedStringListType._bindings);
    }

    @Test
    public void testSimpleTypeCopyConstructorWithSuperClass() {
        Class<SubClass> subClass = new SubClass();
        SimpleType simpleType = new SimpleType(SubClass.class, TypeFactory.defaultInstance().unboundMapper().getTypeFactory(),
                TypeFactory.defaultInstance().unboundMapper().getTypeFactory().constructType(SuperClass.class));
        SimpleType copiedSimpleType = new SimpleType(simpleType);
        assertEquals(SubClass.class, copiedSimpleType.getType());
        assertEquals(SubClass.class, copiedSimpleType.getRawClass());
        assertFalse(copiedSimpleType.isContainerType());
        assertEquals(simpleType.getSuperClass(), copiedSimpleType.getSuperClass());
    }

    @Test
    public void testSimpleTypeCopyConstructorWithValueHandler() {
        SimpleType simpleType = new SimpleType(String.class, TypeFactory.defaultInstance().unboundMapper().getTypeFactory(), null, null, null, new StringValueHandler(), false);
        SimpleType copiedSimpleType = new SimpleType(simpleType);
        assertEquals(String.class, copiedSimpleType.getType());
        assertEquals(String.class, copiedSimpleType.getRawClass());
        assertFalse(copiedSimpleType.isContainerType());
        assertEquals(simpleType.getValueHandler(), copiedSimpleType.getValueHandler());
        assertEquals(simpleType.getTypeHandler(), copiedSimpleType.getTypeHandler());
    }

    @Test
    public void testSimpleTypeCopyConstructorWithTypeHandler() {
        SimpleType simpleType = new SimpleType(String.class, TypeFactory.defaultInstance().unboundMapper().getTypeFactory(), null, null, null, null, true);
        SimpleType copiedSimpleType = new SimpleType(simpleType);
        assertEquals(String.class, copiedSimpleType.getType());
        assertEquals(String.class, copiedSimpleType.getRawClass());
        assertFalse(copiedSimpleType.isContainerType());
        assertEquals(simpleType.getValueHandler(), copiedSimpleType.getValueHandler());
        assertEquals(simpleType.getTypeHandler(), copiedSimpleType.getTypeHandler());
    }

    @Test
    public void testSimpleTypeWithNarrowing() {
        Class<String> stringClass = String.class;
        SimpleType stringType = new SimpleType(stringClass);
        SimpleType stringSubclassType = new SimpleType(StringSubclass.class, TypeFactory.defaultInstance().unboundMapper().getTypeFactory(), stringType, null, null, null, false);
        assertEquals(StringSubclass.class, stringSubclassType.getType());
        assertEquals(StringSubclass.class, stringSubclassType.getRawClass());
        assertFalse(stringSubclassType.isContainerType());
        assertEquals(stringType, stringSubclassType.getSuperClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeWithInvalidNarrowing() {
        Class<String> stringClass = String.class;
        SimpleType stringType = new SimpleType(stringClass);
        SimpleType stringSubclassType = new SimpleType(Integer.class, TypeFactory.defaultInstance().unboundMapper().getTypeFactory(), stringType, null, null, null, false);
    }

    @Test
    public void testSimpleTypeToString() {
        SimpleType simpleType = new SimpleType(String.class, TypeFactory.defaultInstance().unboundMapper().getTypeFactory(), null, null, null, null, false);
        assertEquals("[simple type, class java.lang.String]", simpleType.toString());
    }

    @Test
    public void testSimpleTypeEquals() {
        SimpleType simpleType1 = new SimpleType(String.class, TypeFactory.defaultInstance().unboundMapper().getTypeFactory(), null, null, null, null, false);
        SimpleType simpleType2 = new SimpleType(String.class, TypeFactory.defaultInstance().unboundMapper().getTypeFactory(), null, null, null, null, false);
        assertTrue(simpleType1.equals(simpleType2));
    }

    @Test
    public void testSimpleTypeNotEquals() {
        SimpleType simpleType1 = new SimpleType(String.class, TypeFactory.defaultInstance().unboundMapper().getTypeFactory(), null, null, null, null, false);
        SimpleType simpleType2 = new SimpleType(Integer.class, TypeFactory.defaultInstance().unboundMapper().getTypeFactory(), null, null, null, null, false);
        assertFalse(simpleType1.equals(simpleType2));
    }

    @Test
    public void testSimpleTypeHashCode() {
        SimpleType simpleType1 = new SimpleType(String.class, TypeFactory.defaultInstance().unboundMapper().getTypeFactory(), null, null, null, null, false);
        SimpleType simpleType2 = new SimpleType(String.class, TypeFactory.defaultInstance().unboundMapper().getTypeFactory(), null, null, null, null, false);
        assertEquals(simpleType1.hashCode(), simpleType2.hashCode());
    }

    @Test
    public void testSimpleTypeGetCanonicalName() {
        SimpleType simpleType = new SimpleType(String.class, TypeFactory.defaultInstance().unboundMapper().getTypeFactory(), null, null, null, null, false);
        assertEquals("java.lang.String", simpleType.buildCanonicalName());
    }

    @Test
    public void testSimpleTypeGetErasedSignature() {
        SimpleType simpleType = new SimpleType(String.class, TypeFactory.defaultInstance().unboundMapper().getTypeFactory(), null, null, null, null, false);
        StringBuilder erasedSignature = new StringBuilder();
        simpleType.getErasedSignature(erasedSignature);
        assertEquals("Ljava/lang/String;", erasedSignature.toString());
    }

    @Test
    public void testSimpleTypeGetGenericSignature() {
        SimpleType simpleType = new SimpleType(String.class, TypeFactory.defaultInstance().unboundMapper().getTypeFactory(), null, null, null, null, false);
        StringBuilder genericSignature = new StringBuilder();
        simpleType.getGenericSignature(genericSignature);
        assertEquals("Ljava/lang/String;", genericSignature.toString());
    }

    @Test
    public void testSimpleTypeIsContainerTypeFalse() {
        SimpleType simpleType = new SimpleType(String.class, TypeFactory.defaultInstance().unboundMapper().getTypeFactory(), null, null, null, null, false);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testSimpleTypeIsContainerTypeTrue() {
        SimpleType simpleType = new SimpleType(ArrayList.class, TypeFactory.defaultInstance().unboundMapper().getTypeFactory(), null, null, null, null, true);
        assertTrue(simpleType.isContainerType());
    }

    @Test
    public void testSimpleTypeRefine() {
        SimpleType simpleType = new SimpleType(String.class, TypeFactory.defaultInstance().unboundMapper().getTypeFactory(), null, null, null, null, false);
        assertNull(simpleType.refine(StringSubclass.class, null, null, null));
    }

    public static class SuperClass {}
    public static class SubClass extends SuperClass {}

    public static class StringValueHandler {}
    public static class StringTypeHandler {}
}