package com.example; // replace with your test package

import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.SimpleType;
import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstructor() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType.getTypeBindings());
        assertEquals(null, simpleType.getSuperClass());
        assertEquals(new Class[0], simpleType.getSuperInterfaces());
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<String> stringClass = String.class;
        SimpleType originalSimpleType = new SimpleType(stringClass);
        SimpleType copiedSimpleType = new SimpleType(originalSimpleType);
        assertEquals(originalSimpleType, copiedSimpleType);
    }

    @Test
    public void testConstructUnsafe() {
        try {
            Class<List<Integer>> listClass = List.class.getComponentType();
            SimpleType simpleType = SimpleType.constructUnsafe(listClass);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testConstruct() {
        try {
            Class<List<Integer>> listClass = List.class.getComponentType();
            SimpleType simpleType = SimpleType.construct(listClass);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testNarrow() {
        Class<String> stringClass = String.class;
        Class<Integer> intClass = Integer.class;
        SimpleType stringSimpleType = new SimpleType(stringClass);
        SimpleType intSimpleType = new SimpleType(intClass);
        SimpleType narrowedSimpleType = stringSimpleType.narrow(intClass);
        assertNotNull(narrowedSimpleType);
        assertNotEquals(stringSimpleType, narrowedSimpleType);
        assertEquals(intClass, narrowedSimpleType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), narrowedSimpleType.getTypeBindings());
        assertEquals(null, narrowedSimpleType.getSuperClass());
        assertEquals(new Class[0], narrowedSimpleType.getSuperInterfaces());
    }

    @Test
    public void testIsContainerType() {
        assertFalse(new SimpleType(String.class).isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        String testClassName = "com.example.TestClass";
        SimpleType simpleType = new SimpleType(Class.forName(testClassName));
        StringBuilder expectedSignature = new StringBuilder("[L")
                .append(testClassName.replaceAll("\\.", ""))
                .append(';');
        assertEquals(expectedSignature.toString(), simpleType.getErasedSignature(new StringBuilder()).toString());
    }

    @Test
    public void testGetGenericSignature() {
        String testClassName = "com.example.TestClass";
        String testGenericTypeName = "java.util.List";
        Class<?> testClass = Class.forName(testClassName);
        Type genericType = Type.getParametricTypes(testClass)[0];
        JavaType javaType = new SimpleType(testClass, TypeBindings.emptyBindings(), null, null);
        javaType = javaType.withContentType(new SimpleType(genericType));
        StringBuilder expectedSignature = new StringBuilder("[");
        expectedSignature.append(testGenericTypeName.replaceAll("\\.", ""));
        expectedSignature.append(']');
        assertEquals(expectedSignature.toString(), javaType.getGenericSignature(new StringBuilder()).toString());
    }

    @Test
    public void testWithTypeHandler() {
        Object testHandler = new Object() {};
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType withTypeHandlerSimpleType = simpleType.withTypeHandler(testHandler);
        assertNotEquals(simpleType, withTypeHandlerSimpleType);
        assertEquals(testHandler, withTypeHandlerSimpleType.getTypeHandler());
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType staticTypingSimpleType = simpleType.withStaticTyping();
        assertNotEquals(simpleType, staticTypingSimpleType);
        assertTrue(staticTypingSimpleType.isStatic());
    }
}