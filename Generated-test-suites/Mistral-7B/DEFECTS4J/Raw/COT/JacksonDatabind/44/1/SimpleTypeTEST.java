package com.fasterxml.jackson.databind.type; // adjust the package if needed

import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;

public class SimpleTypeTest {

    

    @Test
    public void testSimpleTypeConstructorWithClass() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType.getBindings());
        assertEquals(null, simpleType.getSuperClass());
        assertEquals(new Class[0], simpleType.getSuperTypes());
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        SimpleType copiedSimpleType = new SimpleType(simpleType);
        assertEquals(simpleType.getRawClass(), copiedSimpleType.getRawClass());
        assertEquals(simpleType.getBindings(), copiedSimpleType.getBindings());
        assertEquals(simpleType.getSuperClass(), copiedSimpleType.getSuperClass());
        assertArrayEquals(simpleType.getSuperTypes(), copiedSimpleType.getSuperTypes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithUnsupportedClass() {
        Class<?> unsupportedClass = List.class;
        SimpleType.constructUnsafe(unsupportedClass);
        // The test should fail with an IllegalArgumentException
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithUnsupportedClass() {
        Class<?> unsupportedClass = List.class;
        SimpleType.construct(unsupportedClass);
        // The test should fail with an IllegalArgumentException
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowWithUnrelatedClass() {
        Class<?> unrelatedClass = Integer.class;
        SimpleType simpleType = new SimpleType(String.class);
        JavaType narrowedType = simpleType.narrow(unrelatedClass);
        assertNull(narrowedType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeWithUnsupportedType() {
        SimpleType simpleType = new SimpleType(String.class);
        JavaType unsupportedType = new SimpleType(int[].class);
        simpleType.withContentType(unsupportedType);
        // The test should fail with an IllegalArgumentException
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        Object typeHandler = new Object();
        SimpleType withTypeHandler = simpleType.withTypeHandler(typeHandler);
        assertNotSame(simpleType, withTypeHandler);
        assertEquals(simpleType.getRawClass(), withTypeHandler.getRawClass());
        assertEquals(simpleType.getBindings(), withTypeHandler.getBindings());
        assertNull(simpleType.getTypeHandler());
        assertSame(typeHandler, withTypeHandler.getTypeHandler());
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType withStaticTyping = simpleType.withStaticTyping();
        assertFalse(simpleType.isStatic());
        assertTrue(withStaticTyping.isStatic());
    }

    @Test
    public void testBuildCanonicalName() {
        Class<?> cls = new ArrayList<String>().getClass();
        SimpleType simpleType = new SimpleType(cls);
        String canonicalName = simpleType.buildCanonicalName();
        assertEquals("java.util.ArrayList<java.lang.String>", canonicalName);
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(String.class);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> cls = String[].class;
        SimpleType simpleType = new SimpleType(cls);
        StringBuilder sb = new StringBuilder();
        simpleType.getErasedSignature(sb);
        assertEquals("Ljava/lang/String;[", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> cls = new ArrayList<String>().getClass();
        SimpleType simpleType = new SimpleType(cls);
        StringBuilder sb = new StringBuilder();
        simpleType.getGenericSignature(sb);
        assertEquals("java.util.ArrayList< java.lang.String >", sb.toString());
    }

    @Test
    public void testEquals() {
        Class<?> cls1 = String.class;
        Class<?> cls2 = String.class;
        SimpleType simpleType1 = new SimpleType(cls1);
        SimpleType simpleType2 = new SimpleType(cls2);
        SimpleType simpleType3 = new SimpleType(cls1);
        assertTrue(simpleType1.equals(simpleType1));
        assertTrue(simpleType1.equals(simpleType3));
        assertTrue(simpleType1.equals(simpleType2));
    }

    @Test
    public void testToString() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        String stringRepresentation = simpleType.toString();
        assertEquals("[simple type, class " + cls.getName() + "]", stringRepresentation);
    }

    @Test
    public void testRefine() {
        Class<?> rawType = ArrayList.class;
        JavaType containingType = new TypeFactory().constructType(List.class);
        TypeBindings bindings = new TypeBindings();
        JavaType refinedType = new SimpleType(rawType).refine(rawType, bindings, null, null);
        assertNull(refinedType);
    }

    @Test
    public void testConstructWithSubclass() {
        Class<?> subclass = StringBuffer.class;
        SimpleType simpleType = new SimpleType(subclass);
        assertEquals(StringBuffer.class, simpleType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType.getBindings());
        assertNull(simpleType.getSuperClass());
        assertArrayEquals(new Class[0], simpleType.getSuperTypes());
    }

    @Test
    public void testConstructWithSuperClass() {
        Class<?> superclass = Object.class;
        Class<?> cls = String.class;
        JavaType superType = new SimpleType(superclass);
        SimpleType simpleType = new SimpleType(cls, null, superType, null);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType.getBindings());
        assertEquals(Object.class, simpleType.getSuperClass());
        assertArrayEquals(new Class[0], simpleType.getSuperTypes());
    }

    @Test
    public void testConstructWithSuperInterfaces() {
        Class<?> interface1 = Serializable.class;
        Class<?> interface2 = Cloneable.class;
        Class<?> cls = String.class;
        JavaType[] superInterfaces = new JavaType[] { new SimpleType(interface1), new SimpleType(interface2) };
        SimpleType simpleType = new SimpleType(cls, null, null, superInterfaces);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType.getBindings());
        assertNull(simpleType.getSuperClass());
        assertArrayEquals(new JavaType[] { new SimpleType(interface1), new SimpleType(interface2) }, simpleType.getSuperTypes());
    }

    @Test
    public void testConstructWithValueHandler() {
        Class<?> cls = String.class;
        Object valueHandler = new Object();
        SimpleType simpleType = new SimpleType(cls, null, null, null, valueHandler, null, false);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType.getBindings());
        assertNull(simpleType.getValueHandler());
        assertEquals(valueHandler, simpleType.getValueHandler());
    }

    @Test
    public void testConstructWithTypeHandler() {
        Class<?> cls = String.class;
        Object typeHandler = new Object();
        SimpleType simpleType = new SimpleType(cls, null, null, null, null, typeHandler, false);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType.getBindings());
        assertNull(simpleType.getTypeHandler());
        assertEquals(typeHandler, simpleType.getTypeHandler());
    }

    @Test
    public void testConstructWithAsStatic() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, null, null, null, null, null, true);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType.getBindings());
        assertTrue(simpleType.isStatic());
    }

    @Test
    public void testGetSuperClassWithClassExtendingObject() {
        Class<?> cls = StringBuffer.class;
        SimpleType simpleType = new SimpleType(cls);
        JavaType superClass = simpleType.getSuperClass();
        assertEquals(Object.class, superClass.getRawClass());
    }

    @Test
    public void testGetSuperClassWithClassExtendingAnotherClass() {
        Class<?> superclass = Object.class;
        Class<?> cls = StringBuffer.class;
        JavaType superType = new SimpleType(superclass);
        SimpleType simpleType = new SimpleType(cls, null, superType, null);
        JavaType superClass = simpleType.getSuperClass();
        assertEquals(superclass, superClass.getRawClass());
    }

    @Test
    public void testGetSuperInterfacesWithClassImplementingInterfaces() {
        Class<?> interface1 = Serializable.class;
        Class<?> interface2 = Cloneable.class;
        Class<?> cls = StringBuffer.class;
        JavaType[] superInterfaces = new JavaType[] { new SimpleType(interface1), new SimpleType(interface2) };
        SimpleType simpleType = new SimpleType(cls, null, null, superInterfaces);
        JavaType[] superTypes = simpleType.getSuperTypes();
        assertEquals(2, superTypes.length);
        assertEquals(interface1, superTypes[0].getRawClass());
        assertEquals(interface2, superTypes[1].getRawClass());
    }

    @Test
    public void testGetSuperInterfacesWithClassNotImplementingInterfaces() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, null, null, null);
        JavaType[] superTypes = simpleType.getSuperTypes();
        assertArrayEquals(new JavaType[0], superTypes);
    }

    
}