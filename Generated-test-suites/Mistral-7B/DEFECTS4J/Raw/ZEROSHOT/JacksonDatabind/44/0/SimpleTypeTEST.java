package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;

import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        // Test constructor with Class argument
        SimpleType simpleType1 = new SimpleType(String.class);
        assertNotNull(simpleType1);
        assertEquals(String.class, simpleType1.getRawClass());

        // Test constructor with Class, TypeBindings, JavaType arguments
        TypeBindings bindings = new TypeBindings();
        JavaType javaTypeSuperClass = Mockito.mock(JavaType.class);
        JavaType[] javaTypeSuperInterfaces = new JavaType[0];
        SimpleType simpleType2 = new SimpleType(Integer.class, bindings, javaTypeSuperClass, javaTypeSuperInterfaces);
        assertNotNull(simpleType2);
        assertEquals(Integer.class, simpleType2.getRawClass());

        // Test copy constructor
        SimpleType simpleType3 = new SimpleType(simpleType1);
        assertEquals(simpleType1, simpleType3);

        // Test constructor using base type
        TypeBase base = Mockito.mock(TypeBase.class);
        SimpleType simpleType4 = new SimpleType(base);
        assertEquals(base, simpleType4.getBaseType());

        // Test constructor with Class, TypeBindings, JavaType, Object, boolean arguments
        Object valueHandler = Mockito.mock(Object.class);
        Object typeHandler = Mockito.mock(Object.class);
        boolean asStatic = true;
        SimpleType simpleType5 = new SimpleType(String.class, bindings, javaTypeSuperClass, javaTypeSuperInterfaces, valueHandler, typeHandler, asStatic);
        assertNotNull(simpleType5);
        assertEquals(String.class, simpleType5.getRawClass());
        assertEquals(valueHandler, simpleType5.getValueHandler());
        assertEquals(typeHandler, simpleType5.getTypeHandler());
        assertTrue(simpleType5.isStaticTyping());

        // Test constructor with Class, TypeBindings, JavaType, int, Object, Object, boolean arguments
        SimpleType simpleType6 = new SimpleType(String.class, bindings, javaTypeSuperClass, javaTypeSuperInterfaces, 0, valueHandler, typeHandler, asStatic);
        assertEquals(simpleType5, simpleType6);
    }

    @Test
    public void testConstructUnsafe() {
        try {
            SimpleType simpleType = SimpleType.constructUnsafe(Object[].class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test(expected = Deprecated.class)
    public void testDeprecatedConstruct() {
        SimpleType simpleType = SimpleType.construct(String.class);
    }

    @Test
    public void testNarrow() {
        SimpleType simpleType1 = new SimpleType(String.class);
        SimpleType simpleType2 = new SimpleType(Integer.class);

        assertNull(simpleType1.narrow(Object.class));
        assertNotNull(simpleType1.narrow(String.class));
        assertEquals(simpleType1, simpleType1.narrow(String.class));

        assertNull(simpleType1.narrow(Integer.class));
        assertNotNull(simpleType1.narrow(Number.class));
        assertNotSame(simpleType1, simpleType1.narrow(Number.class));

        assertNull(simpleType2.narrow(Object.class));
        assertNotNull(simpleType2.narrow(Integer.class));
        assertEquals(simpleType2, simpleType2.narrow(Integer.class));

        assertNull(simpleType2.narrow(Number.class));
        assertNotNull(simpleType2.narrow(SimpleType.class));
        assertNotSame(simpleType2, simpleType2.narrow(SimpleType.class));
    }

    @Test
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(String.class);
        JavaType javaType = Mockito.mock(JavaType.class);
        assertNull(simpleType.withContentType(javaType));
        assertSame(simpleType, simpleType);
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        Object object = Mockito.mock(Object.class);
        SimpleType simpleTypeWithHandler = simpleType.withTypeHandler(object);
        assertNotNull(simpleTypeWithHandler);
        assertNotSame(simpleType, simpleTypeWithHandler);
        assertEquals(simpleType.getRawClass(), simpleTypeWithHandler.getRawClass());
        assertEquals(object, simpleTypeWithHandler.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        Object object = Mockito.mock(Object.class);
        assertNull(simpleType.withContentTypeHandler(object));
        assertSame(simpleType, simpleType);
    }

    @Test
    public void testWithValueHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        Object object = Mockito.mock(Object.class);
        SimpleType simpleTypeWithHandler = simpleType.withValueHandler(object);
        assertNotNull(simpleTypeWithHandler);
        assertNotSame(simpleType, simpleTypeWithHandler);
        assertEquals(simpleType.getRawClass(), simpleTypeWithHandler.getRawClass());
        assertEquals(object, simpleTypeWithHandler.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        Object object = Mockito.mock(Object.class);
        assertNull(simpleType.withContentValueHandler(object));
        assertSame(simpleType, simpleType);
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType simpleTypeWithStaticTyping = simpleType.withStaticTyping();
        assertNotNull(simpleTypeWithStaticTyping);
        assertNotSame(simpleType, simpleTypeWithStaticTyping);
        assertEquals(simpleType.getRawClass(), simpleTypeWithStaticTyping.getRawClass());
        assertTrue(simpleTypeWithStaticTyping.isStaticTyping());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(String.class);
        Class<String> stringClass = String.class;
        TypeBindings bindings = new TypeBindings();
        JavaType javaTypeSuperClass = Mockito.mock(JavaType.class);
        JavaType[] javaTypeSuperInterfaces = new JavaType[0];
        SimpleType refinedType = simpleType.refine(stringClass, bindings, javaTypeSuperClass, javaTypeSuperInterfaces);
        assertNull(refinedType);
        assertSame(simpleType, simpleType);
    }

    @Test
    public void testBuildCanonicalName() {
        SimpleType simpleType = new SimpleType(List.class);
        String expectedCanonicalName = "[list, class java.util.List]";
        assertEquals(expectedCanonicalName, simpleType.buildCanonicalName());

        TypeBindings bindings = new TypeBindings();
        bindings.putBinding(0, Arrays.asList(Integer.TYPE, String.class));
        PowerMockito.whenNew(List.class).withArguments(bindings).thenReturn(new ArrayList<>());
        SimpleType simpleTypeWithGenericArguments = new SimpleType(List.class, bindings);
        String expectedCanonicalNameWithGenericArguments = "[list, class java.util.List<java.lang.Integer, java.lang.String]]";
        assertEquals(expectedCanonicalNameWithGenericArguments, simpleTypeWithGenericArguments.buildCanonicalName());
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(String.class);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = new SimpleType(List.class);
        StringBuilder expectedErasedSignature = new StringBuilder("[list]");
        StringBuilder actualErasedSignature = simpleType.getErasedSignature(expectedErasedSignature);
        assertEquals(expectedErasedSignature.toString(), actualErasedSignature.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(List.class);
        StringBuilder expectedGenericSignature = new StringBuilder("[list, class java.util.List<E>]");
        StringBuilder actualGenericSignature = simpleType.getGenericSignature(expectedGenericSignature);
        assertEquals(expectedGenericSignature.toString(), actualGenericSignature.toString());
    }
}