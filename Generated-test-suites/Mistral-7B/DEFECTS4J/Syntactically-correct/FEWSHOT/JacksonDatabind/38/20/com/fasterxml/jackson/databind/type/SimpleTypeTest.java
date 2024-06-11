package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        assertEquals(cls, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertEquals(0, simpleType.hashCode());
        assertFalse(simpleType.isContainerType());
        assertEquals("class java.lang.String", simpleType.toString());

        // Test withValueHandler
        Object valueHandler = new Object() {};
        SimpleType simpleTypeWithValueHandler = new SimpleType(cls, TypeBindings.emptyBindings(), null, null, valueHandler, null, false);
        assertEquals(valueHandler, simpleTypeWithValueHandler.getValueHandler());

        // Test withTypeHandler
        Object typeHandler = new Object() {};
        SimpleType simpleTypeWithTypeHandler = new SimpleType(cls, TypeBindings.emptyBindings(), null, null, null, typeHandler, false);
        assertEquals(typeHandler, simpleTypeWithTypeHandler.getTypeHandler());
    }

    @Test
    public void testSimpleTypeWithSuperClass() {
        Class<?> superClass = List.class;
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), superClass, null);
        assertEquals(cls, simpleType.getRawClass());
        assertEquals(superClass, simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertEquals(1, simpleType.hashCode());
        assertFalse(simpleType.isContainerType());
        assertEquals("class java.lang.String, superclass class java.util.List", simpleType.toString());
    }

    @Test
    public void testSimpleTypeWithSuperInterfaces() {
        Class<?> interface1 = List.class;
        Class<?> interface2 = Collection.class;
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, new JavaType[] { TypeFactory.defaultInstance().constructType(interface1), TypeFactory.defaultInstance().constructType(interface2) });
        assertEquals(cls, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertArrayEquals(new JavaType[] { TypeFactory.defaultInstance().constructType(interface1), TypeFactory.defaultInstance().constructType(interface2) }, simpleType.getSuperInterfaces());
        assertEquals(2, simpleType.hashCode());
        assertFalse(simpleType.isContainerType());
        assertEquals("class java.lang.String, interfaces [interface java.util.List, interface java.util.Collection]", simpleType.toString());
    }

    @Test
    public void testSimpleTypeWithBindings() {
        Class<?> cls = Map.class;
        JavaType containedType = TypeFactory.defaultInstance().constructType(Integer.class);
        TypeBindings bindings = TypeBindings.builder()
                .putBinding(containedType, new TypeBinding() {})
                .build();
        SimpleType simpleType = new SimpleType(cls, bindings, null, null);
        assertEquals(cls, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertEquals(bindings, simpleType.getBindings());
        assertEquals(1, simpleType.hashCode());
        assertFalse(simpleType.isContainerType());
        assertEquals("class java.util.Map, bindings {java.lang.Integer=?}", simpleType.toString());
    }

    @Test
    public void testSimpleTypeWithBindingsAndSuperClass() {
        Class<?> superClass = List.class;
        Class<?> cls = Map.class;
        JavaType containedType = TypeFactory.defaultInstance().constructType(Integer.class);
        TypeBindings bindings = TypeBindings.builder()
                .putBinding(containedType, new TypeBinding() {})
                .build();
        SimpleType simpleType = new SimpleType(cls, bindings, superClass, null);
        assertEquals(cls, simpleType.getRawClass());
        assertEquals(superClass, simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertEquals(bindings, simpleType.getBindings());
        assertEquals(2, simpleType.hashCode());
        assertFalse(simpleType.isContainerType());
        assertEquals("class java.util.Map, superclass class java.util.List, bindings {java.lang.Integer=?}", simpleType.toString());
    }

    @Test
    public void testSimpleTypeWithBindingsAndSuperInterfaces() {
        Class<?> interface1 = List.class;
        Class<?> interface2 = Collection.class;
        Class<?> cls = Map.class;
        JavaType containedType = TypeFactory.defaultInstance().constructType(Integer.class);
        TypeBindings bindings = TypeBindings.builder()
                .putBinding(containedType, new TypeBinding() {})
                .build();
        SimpleType simpleType = new SimpleType(cls, bindings, null, new JavaType[] { TypeFactory.defaultInstance().constructType(interface1), TypeFactory.defaultInstance().constructType(interface2) });
        assertEquals(cls, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertArrayEquals(new JavaType[] { TypeFactory.defaultInstance().constructType(interface1), TypeFactory.defaultInstance().constructType(interface2) }, simpleType.getSuperInterfaces());
        assertEquals(bindings, simpleType.getBindings());
        assertEquals(4, simpleType.hashCode());
        assertFalse(simpleType.isContainerType());
        assertEquals("class java.util.Map, interfaces [interface java.util.List, interface java.util.Collection], bindings {java.lang.Integer=?}", simpleType.toString());
    }

    @Test
    public void testSimpleTypeWithStaticTyping() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null, null, null, false);
        SimpleType staticSimpleType = simpleType.withStaticTyping();
        assertNotEquals(simpleType, staticSimpleType);
        assertTrue(staticSimpleType.isStaticTyping());
        assertFalse(simpleType.isStaticTyping());
        assertEquals(simpleType.toString(), staticSimpleType.toString());
    }

    @Test
    public void testSimpleTypeWithValueHandler() {
        Class<?> cls = String.class;
        Object valueHandler = new Object() {};
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null, null, null, false);
        SimpleType simpleTypeWithValueHandler = simpleType.withValueHandler(valueHandler);
        assertNotEquals(simpleType, simpleTypeWithValueHandler);
        assertEquals(valueHandler, simpleTypeWithValueHandler.getValueHandler());
        assertEquals(simpleType.toString(), simpleTypeWithValueHandler.toString());
    }

    @Test
    public void testSimpleTypeWithTypeHandler() {
        Class<?> cls = String.class;
        Object typeHandler = new Object() {};
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null, null, null, false);
        SimpleType simpleTypeWithTypeHandler = simpleType.withTypeHandler(typeHandler);
        assertNotEquals(simpleType, simpleTypeWithTypeHandler);
        assertEquals(typeHandler, simpleTypeWithTypeHandler.getTypeHandler());
        assertEquals(simpleType.toString(), simpleTypeWithTypeHandler.toString());
    }

    @Test
    public void testSimpleTypeWithContentTypeHandler() {
        Class<?> cls = String.class;
        Object contentTypeHandler = new Object() {};
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null, null, null, false);
        try {
            simpleType.withContentTypeHandler(contentTypeHandler);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testSimpleTypeWithContainerTyping() {
        Class<?> cls = List.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null, null, null, false);
        assertTrue(simpleType.isContainerType());
        assertEquals("[simple type, class java.util.List]", simpleType.toString());
    }

    @Test
    public void testConstructUnsafe() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(cls);
        assertEquals(cls, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertEquals(0, simpleType.hashCode());
        assertFalse(simpleType.isContainerType());
        assertEquals("class java.lang.String", simpleType.toString());
    }

    @Test(expected = DeprecatedException.class)
    public void testConstructDeprecated() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.construct(cls);
    }

    @Test
    public void testNarrow() {
        Class<?> subclass = Integer.class;
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null, null, null, false);
        SimpleType narrowedType = simpleType._narrow(subclass);
        assertNull(narrowedType);
    }
}