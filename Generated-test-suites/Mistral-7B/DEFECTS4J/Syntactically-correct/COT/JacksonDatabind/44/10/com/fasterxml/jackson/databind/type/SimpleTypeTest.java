package com.fasterxml.jackson.databind.type; // make sure the package is correct

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertEquals(String.class, simpleType.getRawClass());
        assertFalse(simpleType.hasBindings());
        assertNull(simpleType.getSuperclass());
        assertArrayEquals(new Class[0], simpleType.getSuperInterfaces());
    }

    @Test
    public void testConstruct() {
        SimpleType simpleType = SimpleType.construct(String.class);
        assertEquals(String.class, simpleType.getRawClass());
        assertTrue(simpleType.hasBindings());
        assertNull(simpleType.getSuperclass());
        assertArrayEquals(new Class[0], simpleType.getSuperInterfaces());

        try {
            SimpleType simpleTypeArray = SimpleType.construct(int[].class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testConstructWithBindings() {
        JavaType listType = TypeFactory.defaultInstance().constructType(List.class, Integer.TYPE);
        TypeBindings bindings = TypeBindings.emptyBindings();
        bindings.addBinding(0, listType);

        SimpleType simpleType = new SimpleType(String.class, bindings, null, null, null, null, false);
        assertEquals(String.class, simpleType.getRawClass());
        assertTrue(simpleType.hasBindings());
        assertNull(simpleType.getSuperclass());
        assertArrayEquals(new Class[0], simpleType.getSuperInterfaces());

        JavaType containedType = simpleType.containedType(0);
        assertEquals(List.class, containedType.getRawClass());
        assertTrue(containedType.hasBindings());
        assertEquals(Integer.TYPE, containedType.getContentType());
    }

    @Test
    public void testCopyConstructor() {
        SimpleType simpleType = new SimpleType(String.class, null, null, null, null, null, false);
        SimpleType copiedSimpleType = new SimpleType(simpleType);
        assertEquals(simpleType, copiedSimpleType);
    }

    @Test
    public void testDeprecatedConstruct() {
        try {
            SimpleType simpleType = SimpleType.construct(String.class);
            fail("Expected IllegalStateException or DeprecatedException");
        } catch (IllegalArgumentException | IllegalStateException e) {
            // Expected
        } catch (DeprecatedException de) {
            // Expected, but rare
        }
    }

    @Test
    public void testNarrow() {
        SimpleType stringType = new SimpleType(String.class, null, null, null, null, null, false);
        SimpleType stringSubType = new SimpleType(StringSubclass.class, null, null, null, null, null, false);

        JavaType narrowedType = stringType.narrow(StringSubclass.class);
        assertEquals(stringSubType, narrowedType);

        JavaType narrowedType2 = stringType.narrow(Object.class);
        assertNull(narrowedType2);
    }

    @Test
    public void testIsContainerType() {
        SimpleType stringType = new SimpleType(String.class, null, null, null, null, null, false);
        assertFalse(stringType.isContainerType());
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = TypeFactory.defaultInstance().constructType(String.class);
        SimpleType simpleType = new SimpleType(String.class, null, null, null, null, null, false);

        try {
            JavaType withContentType = simpleType.withContentType(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        JavaType withContentType = simpleType.withContentType(contentType);
        assertEquals(contentType, withContentType.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        Object typeHandler = new Object() {};
        SimpleType simpleType = new SimpleType(String.class, null, null, null, null, null, false);

        JavaType withTypeHandler = simpleType.withTypeHandler(typeHandler);
        assertEquals(simpleType, withTypeHandler);
        assertEquals(typeHandler, withTypeHandler.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Object contentTypeHandler = new Object() {};
        SimpleType simpleType = new SimpleType(String.class, null, null, null, null, null, false);

        try {
            JavaType withContentTypeHandler = simpleType.withContentTypeHandler(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        JavaType withContentTypeHandler = simpleType.withContentTypeHandler(contentTypeHandler);
        assertEquals(contentTypeHandler, withContentTypeHandler.getContentValueHandler());
    }

    @Test
    public void testWithValueHandler() {
        Object valueHandler = new Object() {};
        SimpleType simpleType = new SimpleType(String.class, null, null, null, null, null, false);

        JavaType withValueHandler = simpleType.withValueHandler(valueHandler);
        assertEquals(simpleType, withValueHandler);
        assertEquals(valueHandler, withValueHandler.getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(String.class, null, null, null, null, null, false);
        SimpleType withStaticTyping = simpleType.withStaticTyping();
        assertTrue(withStaticTyping.isStatic());

        SimpleType stringAsStatic = new SimpleType(String.class, null, null, null, null, null, true);
        assertSame(stringAsStatic, withStaticTyping);
    }

    @Test
    public void testRefine() {
        Class<String> rawType = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInterfaces = new JavaType[] { TypeFactory.defaultInstance().constructType(List.class, rawType) };

        SimpleType simpleType = new SimpleType(rawType, bindings, superClass, superInterfaces, null, null, false);
        JavaType refinedType = simpleType.refine(rawType, bindings, null, null);
        assertNull(refinedType);
    }

    @Test
    public void testBuildCanonicalName() {
        JavaType innerType = TypeFactory.defaultInstance().constructType(Integer.TYPE);
        JavaType simpleType = TypeFactory.defaultInstance().constructType(List.class, Integer.TYPE);

        assertEquals("java.util.List<java.lang.Integer>", simpleType.getCanonicalName());
        assertEquals("java.lang.Integer", innerType.getCanonicalName());

        TypeBindings bindings = TypeBindings.emptyBindings();
        bindings.addBinding(0, innerType);

        JavaType refinedType = new SimpleType(Integer.TYPE, bindings, null, null, null, null, false);
        assertEquals("java.lang.Integer", refinedType.getCanonicalName());

        TypeBindings bindings2 = TypeBindings.emptyBindings();
        bindings2.addBinding(0, simpleType);

        JavaType refinedType2 = new SimpleType(List.class, bindings2, null, null, null, null, false);
        assertEquals("java.util.List<java.lang.Integer>", refinedType2.getCanonicalName());
    }

    @Test
    public void testEquals() {
        JavaType javaType1 = TypeFactory.defaultInstance().constructType(String.class);
        JavaType javaType2 = TypeFactory.defaultInstance().constructType(String.class);
        JavaType javaType3 = TypeFactory.defaultInstance().constructType(Integer.TYPE);

        JavaType javaType1_1 = javaType1;
        JavaType javaType1_2 = new SimpleType(javaType1.getRawClass(), javaType1.getBindings(), javaType1.getSuperclass(), javaType1.getSuperInterfaces(), javaType1.getValueHandler(), javaType1.getTypeHandler(), javaType1.isStatic());

        JavaType javaType2_1 = javaType2;
        JavaType javaType2_2 = new SimpleType(javaType2.getRawClass(), javaType2.getBindings(), javaType2.getSuperclass(), javaType2.getSuperInterfaces(), javaType2.getValueHandler(), javaType2.getTypeHandler(), javaType2.isStatic());

        JavaType javaType3_1 = javaType3;
        JavaType javaType3_2 = new SimpleType(javaType3.getRawClass(), javaType3.getBindings(), javaType3.getSuperclass(), javaType3.getSuperInterfaces(), javaType3.getValueHandler(), javaType3.getTypeHandler(), javaType3.isStatic());

        JavaType javaType4 = TypeFactory.defaultInstance().constructType(Integer.TYPE, Collections.singletonMap("foo", String.class));

        JavaType javaType5 = new SimpleType(String.class, Collections.emptyMap(), null, null, null, null, false);
        JavaType javaType6 = new SimpleType(String.class, Collections.emptyMap(), null, null, null, null, true);

        JavaType javaType7 = new SimpleType(String.class, Collections.singletonMap("foo", javaType1), null, null, null, null, false);
        JavaType javaType8 = new SimpleType(String.class, Collections.singletonMap("foo", javaType2), null, null, null, null, false);

        assertEquals(javaType1, javaType1_1);
        assertEquals(javaType1_1, javaType1_2);
        assertEquals(javaType1, javaType1_2);
        assertEquals(javaType1, javaType1);

        assertEquals(javaType1, javaType2);
        assertFalse(javaType1.equals(javaType3));
        assertFalse(javaType1.equals(null));
        assertFalse(javaType1.equals(Collections.emptyList().getClass()));

        assertEquals(javaType1, javaType5);
        assertFalse(javaType1.equals(javaType6));
        assertFalse(javaType1.equals(javaType7));
        assertFalse(javaType1.equals(javaType8));
        assertFalse(javaType1.equals(javaType4));
    }

    
}

class StringSubclass extends String {

}