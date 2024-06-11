package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(cls);
        assertEquals(cls, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
    }

    @Test
    public void testConstruct() {
        DeprecatedMethodWarning.warn("SimpleType.construct() is deprecated.");

        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.construct(cls);
        assertEquals(cls, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
    }

    @Test
    public void testConstructWithBindings() {
        Class<?> cls = String[].class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        assertEquals(cls, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());

        TypeBindings bindings = new TypeBindings();
        bindings.put(Integer.TYPE, new TypeBinding() {
            @Override
            public JavaType bindType(JavaType baseType, TypeBindings bindings) {
                return null;
            }
        });
        SimpleType simpleTypeWithBindings = new SimpleType(cls, bindings, null, null);
        assertEquals(cls, simpleTypeWithBindings.getRawClass());
        assertNull(simpleTypeWithBindings.getSuperClass());
        assertNotNull(simpleTypeWithBindings.getBindings());
    }

    @Test
    public void testNarrow() {
        Class<?> parentClass = Object.class;
        Class<?> childClass = String.class;
        SimpleType parentType = SimpleType.constructUnsafe(parentClass);
        SimpleType childType = SimpleType.constructUnsafe(childClass);

        SimpleType narrowedType = childType._narrow(parentClass);
        assertTrue(narrowedType instanceof SimpleType);
        assertEquals(childClass, narrowedType.getRawClass());
        assertEquals(parentType, narrowedType.getSuperClass());
        assertNull(narrowedType.getSuperInterfaces());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowWithDifferentClass() {
        Class<?> parentClass = Object.class;
        Class<?> childClass = Integer.class;
        SimpleType parentType = SimpleType.constructUnsafe(parentClass);
        SimpleType childType = SimpleType.constructUnsafe(childClass);

        childType._narrow(parentClass); // Should throw an IllegalArgumentException
    }

    @Test
    public void testWithContentType() {
        DeprecatedMethodWarning.warn("SimpleType.withContentType() is not supported.");

        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(cls);

        assertThrows(IllegalArgumentException.class, () -> simpleType.withContentType(new SimpleType(Integer.class)));
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(cls);

        Object typeHandler = new Object();
        SimpleType simpleTypeWithTypeHandler = simpleType.withTypeHandler(typeHandler);

        assertEquals(simpleType, simpleTypeWithTypeHandler);
        assertEquals(typeHandler, simpleTypeWithTypeHandler.getTypeHandler());
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(cls);

        SimpleType simpleTypeWithStaticTyping = simpleType.withStaticTyping();
        assertTrue(simpleTypeWithStaticTyping._asStatic);

        SimpleType simpleTypeWithoutStaticTyping = simpleType.withStaticTyping();
        assertFalse(simpleTypeWithoutStaticTyping._asStatic);

        assertEquals(simpleType, simpleTypeWithStaticTyping);
        assertEquals(simpleType, simpleTypeWithoutStaticTyping);
    }
}