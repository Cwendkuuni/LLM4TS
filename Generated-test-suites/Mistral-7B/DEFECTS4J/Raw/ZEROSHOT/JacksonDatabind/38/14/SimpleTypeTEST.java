package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;

import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<Integer> intClass = Integer.class;

        // Test default construction
        SimpleType defaultType = new SimpleType(intClass);
        assertEquals(intClass, defaultType.getRawClass());
        assertNull(defaultType.getSuperClass());
        assertArrayEquals(new JavaType[0], defaultType.getSuperTypes());
        assertFalse(defaultType.isContainerType());

        // Test copy constructor
        SimpleType copyType = new SimpleType(defaultType);
        assertEquals(defaultType.getRawClass(), copyType.getRawClass());
        assertEquals(defaultType.getBindings(), copyType.getBindings());
        assertSame(defaultType.getSuperClass(), copyType.getSuperClass());
        assertArrayEquals(defaultType.getSuperTypes(), copyType.getSuperTypes());
        assertFalse(defaultType.isContainerType());

        // Test construction with bindings
        TypeBindings bindings = TypeBindings.emptyBindings();
        SimpleType bindingsType = new SimpleType(intClass, bindings);
        assertSame(intClass, bindingsType.getRawClass());
        assertSame(bindings, bindingsType.getBindings());
        assertNull(bindingsType.getSuperClass());
        assertArrayEquals(new JavaType[0], bindingsType.getSuperTypes());
        assertFalse(bindingsType.isContainerType());

        // Test construction with superClass and superInterfaces
        JavaType superType = TypeFactory.defaultInstance().constructType(List.class);
        SimpleType superTypeType = new SimpleType(intClass, bindings, superType, new JavaType[0]);
        assertEquals(intClass, superTypeType.getRawClass());
        assertSame(bindings, superTypeType.getBindings());
        assertSame(superType, superTypeType.getSuperClass());
        assertArrayEquals(new JavaType[0], superTypeType.getSuperTypes());
        assertFalse(superTypeType.isContainerType());
    }

    @Test
    public void testGetCanonicalName() {
        Class<Integer> intClass = Integer.class;

        SimpleType defaultType = new SimpleType(intClass);
        assertEquals("int", defaultType.getName());

        SimpleType bindingsType = new SimpleType(intClass, TypeBindings.emptyBindings());
        assertEquals("int", bindingsType.getName());

        SimpleType superTypeType = new SimpleType(intClass, TypeBindings.emptyBindings(), TypeFactory.defaultInstance().constructType(List.class), new JavaType[0]);
        assertEquals("java.util.List<int>", superTypeType.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsupportedTypes() {
        SimpleType.construct(Map.class);
        SimpleType.construct(Collection.class);
        SimpleType.construct(int[].class);
    }

    @Test(expected = JsonMappingException.class)
    public void testConstructWithoutIntrospection() {
        SimpleType.constructUnsafe(int.class);
    }

    @Test
    public void testNarrowing() {
        Class<Integer> intClass = Integer.class;
        Class<Long> longClass = Long.class;

        SimpleType intType = new SimpleType(intClass);
        SimpleType longType = new SimpleType(longClass);

        SimpleType intNarrowed = intType.narrow(longClass);
        assertEquals(intType, intNarrowed);

        SimpleType longNarrowed = longType.narrow(intClass);
        assertEquals(longType, longNarrowed);

        SimpleType intNarrowedToLong = intType.narrow(longClass);
        assertEquals(longType, intNarrowedToLong);

        SimpleType longNarrowedToInt = longType.narrow(intClass);
        assertEquals(intType, longNarrowedToInt);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowingToInvalidTypes() {
        Class<Integer> intClass = Integer.class;
        Class<List<Integer>> listIntClass = List.class.getGenericType();

        SimpleType intType = new SimpleType(intClass);
        SimpleType listIntType = new SimpleType(listIntClass);

        intType.narrow(listIntClass);
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = TypeFactory.defaultInstance().constructType(String.class);
        SimpleType simpleType = new SimpleType(Integer.class);

        try {
            simpleType.withContentType(contentType);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType simpleType = new SimpleType(Integer.class);

        IntegerHandler integerHandler = new IntegerHandler();
        SimpleType withTypeHandler = simpleType.withTypeHandler(integerHandler);

        assertEquals(simpleType.getRawClass(), withTypeHandler.getRawClass());
        assertSame(integerHandler, withTypeHandler.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType contentType = TypeFactory.defaultInstance().constructType(String.class);
        SimpleType simpleType = new SimpleType(Integer.class);

        StringHandler stringHandler = new StringHandler();
        SimpleType withContentTypeHandler = simpleType.withContentTypeHandler(stringHandler);

        assertEquals(simpleType.getRawClass(), withContentTypeHandler.getRawClass());
        assertSame(stringHandler, withContentTypeHandler.getContentTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        SimpleType simpleType = new SimpleType(Integer.class);

        IntegerHandler integerHandler = new IntegerHandler();
        SimpleType withValueHandler = simpleType.withValueHandler(integerHandler);

        assertEquals(simpleType.getRawClass(), withValueHandler.getRawClass());
        assertSame(integerHandler, withValueHandler.getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(Integer.class);
        SimpleType withStaticTyping = simpleType.withStaticTyping();

        assertTrue(simpleType.isInstanceable());
        assertTrue(withStaticTyping.isInstanceable());
        assertFalse(withStaticTyping.isInstanceable());
    }

    @Test
    public void testRefine() {
        Class<Integer> intClass = Integer.class;
        Class<Integer[]> intArrayClass = int[].class;

        SimpleType intType = new SimpleType(intClass);
        SimpleType intArrayType = new SimpleType(intArrayClass);

        SimpleType intRefined = intType.refine(intArrayClass, TypeBindings.emptyBindings(), null, new JavaType[0]);
        assertNull(intRefined);

        SimpleType intArrayRefined = intArrayType.refine(intClass, TypeBindings.emptyBindings(), null, new JavaType[0]);
        assertSame(intArrayType, intArrayRefined);
    }

    private static JavaType _classSignature(Class<?> cls, StringBuilder sb, boolean erased) {
        if (cls.isPrimitive()) {
            sb.append(cls.getName().charAt(0));
        } else {
            if (erased) {
                sb.append(cls.getName().substring(cls.getName().lastIndexOf(".")+1));
            } else {
                sb.append(cls.getName());
            }
        }
        return TypeFactory.defaultInstance().constructType(cls);
    }

    private JavaType containedType(int index) {
        return TypeFactory.defaultInstance().constructType(Integer.class);
    }

    private static void assertArrayEquals(JavaType[] expected, JavaType[] actual) {
        if (null == expected || null == actual) {
            assertEquals(expected, actual);
        } else {
            assertArrayEquals(expected.length, actual.length);
            for (int i = 0; i < expected.length; ++i) {
                assertEquals(expected[i], actual[i]);
            }
        }
    }

    private static void assertSame(Object expected, Object actual) {
        assertEquals(expected, actual);
    }

    private static class IntegerHandler implements ObjectHandler {
        @Override
        public JavaType getValueType(SerializerProvider serializers, Object value, TypeDescriptor descriptor) {
            return ValueType.construct(Integer.class);
        }

        @Override
        public void serializeValue(SerializerProvider serializers, Object value, JsonGenerator jg, SerializerFactory sf) throws IOException {
            jg.writeNumber(value.intValue());
        }
    }

    private static class StringHandler implements ObjectSerializer {
        @Override
        public void serialize(SerializerProvider serializers, Object value, JsonGenerator jg, SerializerFactory sf) throws IOException {
            jg.writeStringValue(value.toString());
        }

        @Override
        public JavaType getType(SerializerProvider serializers) {
            return TypeFactory.defaultInstance().constructType(String.class);
        }
    }
}