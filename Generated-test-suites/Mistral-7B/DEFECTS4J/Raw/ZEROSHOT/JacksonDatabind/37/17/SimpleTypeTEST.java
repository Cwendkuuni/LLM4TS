package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.lang.ClassNotFoundException;
import java.lang.reflect.Type;
import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        // Test default constructor
        SimpleType simpleType1 = new SimpleType(String.class);
        assertNotNull(simpleType1);
        assertEquals(String.class, simpleType1.getRawClass());
        assertNull(simpleType1.getSuperClass());
        assertNull(simpleType1.getSuperInterfaces());

        // Test constructor with bindings, superClass, and superInterfaces
        SimpleType simpleType2 = new SimpleType(String.class, TypeBindings.emptyBindings(),
                JavaType.construct(List.class, new TypeReference<List<String>>(){}.getType()),
                new JavaType[] { JavaType.construct(Map.class, Map.class, String.class, String.class) });
        assertNotNull(simpleType2);
        assertEquals(String.class, simpleType2.getRawClass());
        assertEquals(List.class, simpleType2.getSuperClass());
        assertArrayEquals(new JavaType[] { Map.class }, simpleType2.getSuperInterfaces());

        // Test copy constructor
        SimpleType simpleType3 = new SimpleType(simpleType1);
        assertNotNull(simpleType3);
        assertEquals(simpleType1.getRawClass(), simpleType3.getRawClass());
        assertEquals(simpleType1.getBindings(), simpleType3.getBindings());
        assertEquals(simpleType1.getSuperClass(), simpleType3.getSuperClass());
        assertArrayEquals(simpleType1.getSuperInterfaces(), simpleType3.getSuperInterfaces());

        // Test unsafe constructor
        SimpleType simpleType4 = SimpleType.constructUnsafe(String[].class);
        assertNotNull(simpleType4);
        assertEquals(String[].class, simpleType4.getRawClass());
        assertNull(simpleType4.getSuperClass());
        assertNull(simpleType4.getSuperInterfaces());

        // Test deprecated constructor
        try {
            SimpleType simpleType5 = SimpleType.construct(String[].class);
            fail("Expected IllegalStateException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testNarrowing() {
        SimpleType simpleType1 = new SimpleType(String.class);
        SimpleType simpleType2 = new SimpleType(String[].class);
        SimpleType simpleType3 = simpleType1.narrow(String[].class);
        SimpleType simpleType4 = simpleType2.narrow(String.class);

        assertNotNull(simpleType3);
        assertSame(simpleType1, simpleType3);

        assertNotNull(simpleType4);
        assertSame(simpleType2, simpleType4);

        try {
            simpleType1.narrow(Object.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            simpleType2.narrow(List.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testHashCodeAndEquals() {
        SimpleType simpleType1 = new SimpleType(String.class);
        SimpleType simpleType2 = new SimpleType(String.class);
        SimpleType simpleType3 = new SimpleType(Integer.class);

        // Test equals with same object
        assertEquals(simpleType1, simpleType1);

        // Test equals with different class but same bindings
        SimpleType simpleType4 = new SimpleType(Object.class, TypeBindings.emptyBindings(),
                null, null);
        assertEquals(simpleType1, simpleType4);

        // Test equals with different class and different bindings
        SimpleType simpleType5 = new SimpleType(Integer.class, TypeBindings.emptyBindings(),
                null, null);
        assertNotEquals(simpleType1, simpleType5);

        // Test equals with null
        assertNotEquals(simpleType1, null);

        // Test hashCode and equals with same object
        int hashCode1 = simpleType1.hashCode();
        SimpleType simpleType1_1 = simpleType1;
        assertEquals(hashCode1, simpleType1_1.hashCode());
        assertEquals(simpleType1, simpleType1_1);

        // Test hashCode and equals with different objects
        int hashCode2 = simpleType1.hashCode();
        int hashCode3 = simpleType2.hashCode();
        assertNotEquals(hashCode1, hashCode2);
        assertNotEquals(simpleType1, simpleType2);

        // Test hashCode and equals with different bindings
        SimpleType simpleType6 = new SimpleType(String.class, TypeBindings.emptyBindings());
        assertNotEquals(hashCode1, simpleType6.hashCode());
        assertNotEquals(simpleType1, simpleType6);

        // Test hashCode and equals with different class and different bindings
        SimpleType simpleType7 = new SimpleType(Integer.class, TypeBindings.emptyBindings());
        assertNotEquals(hashCode1, simpleType7.hashCode());
        assertNotEquals(simpleType1, simpleType7);
    }

    @Test
    public void testToString() {
        SimpleType simpleType1 = new SimpleType(String.class);
        SimpleType simpleType2 = new SimpleType(List.class);
        SimpleType simpleType3 = new SimpleType(Map.class, Map.class, String.class, String.class);

        String expected1 = "[simple type, class java.lang.String]";
        String expected2 = "[simple type, class java.util.List, contains [simple type, class java.lang.String]]";
        String expected3 = "[simple type, class java.util.Map, interfaces [java.util.Map, java.io.Serializable], key type [simple type, class java.lang.String], value type [simple type, class java.lang.String]]";

        assertEquals(expected1, simpleType1.toString());
        assertEquals(expected2, simpleType2.toString());
        assertEquals(expected3, simpleType3.toString());
    }

    @Test
    public void testCanonicalName() {
        SimpleType simpleType1 = new SimpleType(String.class);
        SimpleType simpleType2 = new SimpleType(String[].class);
        SimpleType simpleType3 = new SimpleType(List.class, new TypeReference<List<String>>(){}.getType());
        SimpleType simpleType4 = new SimpleType(Map.class, Map.class, String.class, String.class);

        String expected1 = "java.lang.String";
        String expected2 = "java.lang.String[]";
        String expected3 = "java.util.List<java.lang.String>";
        String expected4 = "java.util.Map<java.util.Map<java.lang.String,java.lang.String>,java.lang.String>";

        assertEquals(expected1, simpleType1.buildCanonicalName());
        assertEquals(expected2, simpleType2.buildCanonicalName());
        assertEquals(expected3, simpleType3.buildCanonicalName());
        assertEquals(expected4, simpleType4.buildCanonicalName());
    }

    @Test
    public void testGetErasedSignatureAndGetGenericSignature() {
        SimpleType simpleType1 = new SimpleType(String.class);
        SimpleType simpleType2 = new SimpleType(List.class, new TypeReference<List<String>>(){}.getType());
        SimpleType simpleType3 = new SimpleType(Map.class, Map.class, String.class, String.class);

        StringBuilder expectedSignature1 = new StringBuilder("java.lang.String");
        StringBuilder expectedSignature2 = new StringBuilder("java.util.List<T>");
        StringBuilder expectedSignature3 = new StringBuilder("java.util.Map<K,V>");

        StringBuilder actualSignature1 = simpleType1.getErasedSignature(new StringBuilder());
        StringBuilder actualSignature2 = simpleType2.getErasedSignature(new StringBuilder());
        StringBuilder actualSignature3 = simpleType3.getErasedSignature(new StringBuilder());

        assertEquals(expectedSignature1.toString(), actualSignature1.toString());
        assertEquals(expectedSignature2.toString(), actualSignature2.toString());
        assertEquals(expectedSignature3.toString(), actualSignature3.toString());

        StringBuilder expectedGenericSignature1 = new StringBuilder("java.lang.String");
        StringBuilder expectedGenericSignature2 = new StringBuilder("java.util.List<T>");
        StringBuilder expectedGenericSignature3 = new StringBuilder("java.util.Map<K, V>");

        StringBuilder actualGenericSignature1 = simpleType1.getGenericSignature(new StringBuilder());
        StringBuilder actualGenericSignature2 = simpleType2.getGenericSignature(new StringBuilder());
        StringBuilder actualGenericSignature3 = simpleType3.getGenericSignature(new StringBuilder());

        assertEquals(expectedGenericSignature1.toString(), actualGenericSignature1.toString());
        assertEquals(expectedGenericSignature2.toString(), actualGenericSignature2.toString());
        assertEquals(expectedGenericSignature3.toString(), actualGenericSignature3.toString());
    }

    @Test
    public void testEqualsWithReflectionType() throws ClassNotFoundException {
        SimpleType simpleType1 = new SimpleType(String.class);
        JavaType javaType1 = JavaType.construct(String.class);

        SimpleType simpleType2 = new SimpleType(String[].class);
        JavaType javaType2 = JavaType.construct(String[].class);

        SimpleType simpleType3 = new SimpleType(List.class, new TypeReference<List<String>>(){}.getType());
        JavaType javaType3 = JavaType.construct(new TypeReference<List<String>>(){}.getType());

        SimpleType simpleType4 = new SimpleType(Map.class, Map.class, String.class, String.class);
        JavaType javaType4 = JavaType.construct(new TypeReference<Map<Map<String,String>, String>>(){}.getType());

        assertEquals(simpleType1, javaType1);
        assertEquals(simpleType2, javaType2);
        assertEquals(simpleType3, javaType3);
        assertEquals(simpleType4, javaType4);
    }

    @Test
    public void testContainsType() {
        SimpleType simpleType1 = new SimpleType(String.class);
        SimpleType simpleType2 = new SimpleType(List.class, new TypeReference<List<String>>(){}.getType());
        SimpleType simpleType3 = new SimpleType(Map.class, Map.class, String.class, String.class);

        Class<?> rawType1 = simpleType1.getRawClass();
        Class<?> rawType2 = simpleType2.getRawClass();
        Class<?> rawType3 = simpleType3.getRawClass();

        JavaType containedType1 = simpleType2.containedType(0);
        JavaType containedType2 = simpleType3.containedType(0);
        JavaType containedType3 = simpleType3.containedType(1);

        assertTrue(simpleType1.containsType(String.class));
        assertTrue(simpleType2.containsType(String.class));
        assertFalse(simpleType1.containsType(List.class));
        assertFalse(simpleType1.containsType(Map.class));
        assertTrue(simpleType2.containsType(List.class));
        assertTrue(simpleType2.containsType(String.class));
        assertTrue(simpleType3.containsType(Map.class));
        assertTrue(simpleType3.containsType(String.class));
        assertFalse(simpleType3.containsType(List.class));
        assertFalse(simpleType1.containsType(rawType1));
        assertFalse(simpleType1.containsType(rawType2));
        assertFalse(simpleType1.containsType(rawType3));
        assertTrue(simpleType2.containsType(containedType1));
        assertTrue(simpleType3.containsType(containedType2));
        assertTrue(simpleType3.containsType(containedType3));
        assertFalse(simpleType1.containsType(new TypeReference<List<String>>(){}.getType()));
        assertFalse(simpleType1.containsType(new TypeReference<Map<Map<String,String>, String>>(){}.getType()));
    }

    private void _classSignature(Class<?> clazz, StringBuilder sb, boolean erased) {
        if (clazz.isArray()) {
            if (erased) {
                sb.append("[]");
            } else {
                _classSignature(clazz.getComponentType(), sb, false);
                sb.append("[]");
            }
        } else if (clazz.isInterface()) {
            sb.append(clazz.getName()).append("<");
            for (Type genericInterface : clazz.getGenericInterfaces()) {
                if (sb.length() > 1) {
                    sb.append(", ");
                }
                _classSignature(TypeUtil.getRawType(genericInterface), sb, erased);
            }
            sb.append(">");
        } else if (clazz.isEnum()) {
            sb.append(clazz.getName()).append(' ');
            sb.append(clazz.getSimpleName()).append(' ');
            for (Field field : clazz.getFields()) {
                if (!field.isAnnotationPresent(EnumConstantAnnotation.class)) continue;
                EnumConstantAnnotation annotation = field.getAnnotation(EnumConstantAnnotation.class);
                sb.append(annotation.value().toString());
                if (field.name().equals(annotation.name())) continue;
                sb.append(", ");
            }
        } else {
            sb.append(clazz.getName()).append(' ');
            sb.append(clazz.getSimpleName());
            Type[] genericTypes = clazz.getGenericInterfaces();
            if (genericTypes.length > 0) {
                if (erased) {
                    sb.append("<");
                    for (int i = 0; i < genericTypes.length; i++) {
                        if (i > 0) {
                            sb.append(", ");
                        }
                        _classSignature(TypeUtil.getRawType(genericTypes[i]), sb, erased);
                    }
                    sb.append(">");
                } else {
                    sb.append("<");
                    for (int i = 0; i < genericTypes.length; i++) {
                        if (i > 0) {
                            sb.append(", ");
                        }
                        _classSignature(TypeUtil.getRawType(genericTypes[i]), sb, false);
                        sb.append(' ');
                        sb.append(TypeUtil.getSimpleName(genericTypes[i]));
                    }
                    sb.append(">");
                }
            }
            Type[] genericTypes2 = clazz.getGenericDeclaredTypes();
            if (genericTypes2.length > 0) {
                if (erased) {
                    sb.append("<");
                    for (int i = 0; i < genericTypes2.length; i++) {
                        if (i > 0) {
                            sb.append(", ");
                        }
                        _classSignature(TypeUtil.getRawType(genericTypes2[i]), sb, erased);
                    }
                    sb.append(">");
                } else {
                    sb.append("<");
                    for (int i = 0; i < genericTypes2.length; i++) {
                        if (i > 0) {
                            sb.append(", ");
                        }
                        _classSignature(TypeUtil.getRawType(genericTypes2[i]), sb, false);
                        sb.append(' ');
                        sb.append(TypeUtil.getSimpleName(genericTypes2[i]));
                    }
                    sb.append(">");
                }
            }
        }
    }
}