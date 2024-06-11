package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        assertEquals(cls, simpleType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType.getBindings());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertFalse(simpleType.isStatic());
        assertEquals(0, simpleType.hashCode());
    }

    @Test
    public void testSimpleTypeWithBindings() {
        Class<?> cls = String.class;
        JavaType javaType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        assertEquals(cls, javaType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), javaType.getBindings());
        assertNull(javaType.getSuperClass());
        assertNull(javaType.getSuperInterfaces());
        assertFalse(javaType.isStatic());
        assertEquals(0, javaType.hashCode());

        TypeBindings bindings = new TypeBindings();
        bindings.put(Integer.class, new SimpleType(Integer.TYPE));
        JavaType javaTypeWithBindings = new SimpleType(cls, bindings, null, null);
        assertEquals(cls, javaTypeWithBindings.getRawClass());
        assertEquals(bindings, javaTypeWithBindings.getBindings());
        assertNull(javaTypeWithBindings.getSuperClass());
        assertNull(javaTypeWithBindings.getSuperInterfaces());
        assertFalse(javaTypeWithBindings.isStatic());
        assertEquals(Integer.TYPE.hashCode(), javaTypeWithBindings.hashCode());
    }

    @Test
    public void testSimpleTypeWithSuperClass() {
        Class<?> superClass = ArrayList.class;
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), superClass, null);
        assertEquals(cls, simpleType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType.getBindings());
        assertEquals(superClass, simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertFalse(simpleType.isStatic());
        assertEquals(cls.hashCode() ^ superClass.hashCode(), simpleType.hashCode());
    }

    @Test
    public void testSimpleTypeWithSuperInterfaces() {
        Class<?> superInterface1 = List.class;
        Class<?> superInterface2 = Collection.class;
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, new JavaType[] {
                new SimpleType(superInterface1), new SimpleType(superInterface2)
        });
        assertEquals(cls, simpleType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType.getBindings());
        assertNull(simpleType.getSuperClass());
        assertArrayEquals(new JavaType[] { new SimpleType(superInterface1), new SimpleType(superInterface2) }, simpleType.getSuperInterfaces());
        assertFalse(simpleType.isStatic());
        int hashCode = cls.hashCode();
        hashCode ^= superInterface1.hashCode();
        hashCode ^= superInterface2.hashCode();
        assertEquals(hashCode, simpleType.hashCode());
    }

    @Test
    public void testSimpleTypeWithValueHandler() {
        Class<?> cls = String.class;
        Object valueHandler = new Object() {};
        JavaType javaType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null, null, valueHandler, false);
        assertEquals(cls, javaType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), javaType.getBindings());
        assertNull(javaType.getSuperClass());
        assertNull(javaType.getSuperInterfaces());
        assertFalse(javaType.isStatic());
        assertEquals(valueHandler, javaType.getValueHandler());
        assertNull(javaType.getTypeHandler());
        assertEquals(0, javaType.hashCode());
    }

    @Test
    public void testSimpleTypeWithTypeHandler() {
        Class<?> cls = String.class;
        Object typeHandler = new Object() {};
        JavaType javaType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null, null, null, false, null, typeHandler);
        assertEquals(cls, javaType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), javaType.getBindings());
        assertNull(javaType.getSuperClass());
        assertNull(javaType.getSuperInterfaces());
        assertFalse(javaType.isStatic());
        assertNull(javaType.getValueHandler());
        assertEquals(typeHandler, javaType.getTypeHandler());
        assertEquals(0, javaType.hashCode());
    }

    @Test
    public void testSimpleTypeWithStaticTyping() {
        Class<?> cls = String.class;
        JavaType javaType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        assertFalse(javaType.isStatic());
        JavaType javaTypeWithStaticTyping = javaType.withStaticTyping();
        assertTrue(javaTypeWithStaticTyping.isStatic());
        assertSame(javaType, javaTypeWithStaticTyping);
    }

    @Test
    public void testSimpleTypeNarrowing() {
        Class<?> superClass = ArrayList.class;
        Class<?> cls = String.class;
        JavaType javaType = new SimpleType(cls, TypeBindings.emptyBindings(), superClass, null);
        JavaType narrowedType = javaType._narrow(cls);
        assertEquals(cls, narrowedType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), narrowedType.getBindings());
        assertNull(narrowedType.getSuperClass());
        assertNull(narrowedType.getSuperInterfaces());
        assertFalse(narrowedType.isStatic());
        assertEquals(cls, narrowedType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeWithInvalidSubclass() {
        Class<?> cls = String.class;
        JavaType javaType = new SimpleType(cls);
        javaType._narrow(int[].class);
    }

    @Test
    public void testSimpleTypeWithContentType() {
        Class<?> cls = String.class;
        JavaType contentType = new SimpleType(String[].class);
        JavaType javaType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        assertThrows(IllegalArgumentException.class, () -> javaType.withContentType(contentType));
    }

    @Test
    public void testSimpleTypeWithTypeHandlerChange() {
        Class<?> cls = String.class;
        Object oldTypeHandler = new Object() {};
        Object newTypeHandler = new Object() {};
        JavaType javaType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null, null, oldTypeHandler, false);
        JavaType javaTypeWithNewTypeHandler = javaType.withTypeHandler(newTypeHandler);
        assertSame(cls, javaTypeWithNewTypeHandler.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), javaTypeWithNewTypeHandler.getBindings());
        assertNull(javaTypeWithNewTypeHandler.getSuperClass());
        assertNull(javaTypeWithNewTypeHandler.getSuperInterfaces());
        assertFalse(javaTypeWithNewTypeHandler.isStatic());
        assertNotSame(oldTypeHandler, javaTypeWithNewTypeHandler.getTypeHandler());
        assertSame(newTypeHandler, javaTypeWithNewTypeHandler.getTypeHandler());
        assertEquals(javaType.hashCode(), javaTypeWithNewTypeHandler.hashCode());
    }

    @Test
    public void testSimpleTypeWithValueHandlerChange() {
        Class<?> cls = String.class;
        Object oldValueHandler = new Object() {};
        Object newValueHandler = new Object() {};
        JavaType javaType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null, null, oldValueHandler, false);
        JavaType javaTypeWithNewValueHandler = javaType.withValueHandler(newValueHandler);
        assertSame(cls, javaTypeWithNewValueHandler.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), javaTypeWithNewValueHandler.getBindings());
        assertNull(javaTypeWithNewValueHandler.getSuperClass());
        assertNull(javaTypeWithNewValueHandler.getSuperInterfaces());
        assertFalse(javaTypeWithNewValueHandler.isStatic());
        assertNotSame(oldValueHandler, javaTypeWithNewValueHandler.getValueHandler());
        assertSame(newValueHandler, javaTypeWithNewValueHandler.getValueHandler());
        assertEquals(javaType.hashCode(), javaTypeWithNewValueHandler.hashCode());
    }

    @Test
    public void testSimpleTypeWithStaticTypingChange() {
        Class<?> cls = String.class;
        JavaType javaType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        JavaType javaTypeWithStaticTyping = javaType.withStaticTyping();
        JavaType javaTypeWithoutStaticTyping = javaTypeWithStaticTyping.withStaticTyping();
        assertFalse(javaType.isStatic());
        assertTrue(javaTypeWithStaticTyping.isStatic());
        assertSame(javaType, javaTypeWithoutStaticTyping);
    }

    @Test
    public void testSimpleTypeToString() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        assertEquals("[simple type, class " + cls.getName() + "]", simpleType.toString());
    }

    @Test
    public void testSimpleTypeEquals() {
        Class<?> cls = String.class;
        SimpleType simpleType1 = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        SimpleType simpleType2 = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        assertSame(simpleType1, simpleType2);
        SimpleType simpleType3 = new SimpleType(Integer.class, null, null, null);
        assertNotSame(simpleType1, simpleType3);
    }
}