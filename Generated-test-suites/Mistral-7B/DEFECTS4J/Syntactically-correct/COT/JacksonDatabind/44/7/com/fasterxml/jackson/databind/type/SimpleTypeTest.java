package com.fasterxml.jackson.databind.type; // make sure the package is correct

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    

    // Basic test cases for constructors

    @Test
    public void testSimpleTypeConstructorWithClass() {
        Class<String> stringClass = String.class;
        SimpleType stringType = new SimpleType(stringClass);
        assertThat(stringType.getRawClass(), is(String.class));
    }

    @Test
    public void testSimpleTypeConstructorWithClassBindingsSuperClassSuperInterfaces() {
        Class<List<String>> listOfStringClass = List.class.getGenericType();
        JavaType listOfStringType = TypeFactory.defaultInstance().constructType(listOfStringClass);
        SimpleType listOfStringTypeObject = new SimpleType(listOfStringClass, TypeBindings.emptyBindings(), null, null);
        assertEquals(listOfStringType, listOfStringTypeObject);
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<String> stringClass = String.class;
        SimpleType originalStringType = new SimpleType(stringClass);
        SimpleType copyStringType = new SimpleType(originalStringType);
        assertEquals(originalStringType, copyStringType);
    }

    @Test
    public void testSimpleTypePassThroughConstructor() {
        Class<List<String>> listOfStringClass = List.class.getGenericType();
        JavaType listOfStringType = TypeFactory.defaultInstance().constructType(listOfStringClass);
        SimpleType listOfStringTypeObject = new SimpleType(listOfStringType);
        assertEquals(listOfStringType, listOfStringTypeObject);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeConstructWithMapClass() {
        Class<Map<String, String>> mapClass = Map.class.getGenericType();
        new SimpleType(mapClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeConstructWithCollectionClass() {
        Class<List<String>> collectionClass = List.class;
        new SimpleType(collectionClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeConstructWithArrayClass() {
        Class<String[]> arrayClass = String[].class;
        new SimpleType(arrayClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeConstructUnsafeWithNullClass() {
        new SimpleType(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeConstructUnsafeWithArrayClass() {
        Class<String[]> arrayClass = String[].class;
        new SimpleType.constructUnsafe(arrayClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeConstructWithClassOutsidePackage() {
        Class<com.example.String> stringClass = com.example.String.class;
        new SimpleType(stringClass);
    }

    // Basic test cases for methods

    @Test
    public void testIsContainerType() {
        SimpleType stringType = new SimpleType(String.class);
        assertFalse(stringType.isContainerType());
    }

    @Test
    public void testBuildCanonicalName() {
        Class<List<String>> listOfStringClass = List.class.getGenericType();
        SimpleType listOfStringType = new SimpleType(listOfStringClass);
        assertEquals("java.util.List<java.lang.String>", listOfStringType.buildCanonicalName());
    }

    @Test
    public void testGetErasedSignature() {
        Class<List<String>> listOfStringClass = List.class.getGenericType();
        SimpleType listOfStringType = new SimpleType(listOfStringClass);
        StringBuilder sb = new StringBuilder();
        listOfStringType.getErasedSignature(sb);
        assertEquals("java.util.List", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<List<String>> listOfStringClass = List.class.getGenericType();
        SimpleType listOfStringType = new SimpleType(listOfStringClass);
        StringBuilder sb = new StringBuilder();
        listOfStringType.getGenericSignature(sb);
        assertEquals("[Ljava.lang.Object;", sb.toString());
    }

    // Edge cases and exception handling

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowWithSubclassThatIsNotSubtype() {
        Class<Integer> integerClass = Integer.class;
        SimpleType intType = new SimpleType(integerClass);
        Class<Byte> byteClass = Byte.class;
        SimpleType byteType = new SimpleType(byteClass);
        SimpleType narrowedType = intType._narrow(byteClass);
        fail("_narrow method should have thrown an IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowWithNullClass() {
        Class<String> stringClass = String.class;
        SimpleType stringType = new SimpleType(stringClass);
        SimpleType narrowedType = stringType._narrow(null);
        fail("_narrow method should have thrown an IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithNullClass() {
        new SimpleType(null);
        fail("construct method should have thrown an IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArrayClassWithoutSuperClass() {
        Class<String[]> arrayClass = String[].class;
        new SimpleType(arrayClass, null, null, null, null, null, false);
        fail("construct method should have thrown an IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithClassWithMissingSuperClass() {
        Class<String> stringClass = String.class;
        new SimpleType(stringClass, null, null, null, null, null, false);
        fail("construct method should have thrown an IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithClassAndSuperclassThatIsNotSuperClass() {
        Class<String> stringClass = String.class;
        Class<Integer> intClass = Integer.class;
        new SimpleType(stringClass, null, intClass, null, null, null, false);
        fail("construct method should have thrown an IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithClassAndSuperclassThatIsNotSuperInterface() {
        Class<String> stringClass = String.class;
        Class<Comparable<String>> comparableStringClass = Comparable.class.getGenericType();
        new SimpleType(stringClass, null, null, new JavaType[] { comparableStringClass }, null, null, false);
        fail("construct method should have thrown an IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithClassAndSuperInterfacesThatAreNotInterfaces() {
        Class<String> stringClass = String.class;
        Class<String> stringClassAgain = String.class;
        new SimpleType(stringClass, null, null, new JavaType[] { stringClassAgain }, null, null, false);
        fail("construct method should have thrown an IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithClassAndNullSuperInterfaces() {
        Class<String> stringClass = String.class;
        new SimpleType(stringClass, null, null, null, null, null, false, null);
        fail("construct method should have thrown an IllegalArgumentException");
    }

    
}