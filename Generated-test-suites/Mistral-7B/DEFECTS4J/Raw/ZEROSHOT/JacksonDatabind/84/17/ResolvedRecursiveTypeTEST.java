package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for {@link ResolvedRecursiveType} class.
 */
public class ResolvedRecursiveTypeTest {

    @Test
    public void testConstructor() {
        Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        assertNull(type.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        JavaType refType = new TypeReference<JavaType>() {}.getType();
        type.setReference(refType);
        type.setReference(refType);
    }

    @Test
    public void testGetSelfReferencedType() {
        Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType refType = new TypeReference<JavaType>() {}.getType();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        type.setReference(refType);
        assertEquals(refType, type.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType refType = new TypeReference<JavaType>() {}.getType();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        StringBuilder sb = new StringBuilder(100);
        JavaType result = type.getSelfReferencedType();
        type.getGenericSignature(sb);
        assertEquals("Ljava/lang/Class;Lcom/fasterxml/jackson/databind/type/TypeBindings;Lcom/fasterxml/jackson/databind/JavaType;", sb.toString());
        result = refType;
        sb = new StringBuilder(100);
        type.getGenericSignature(sb);
        assertEquals("Ljava/lang/Class;Lcom/fasterxml/jackson/databind/type/TypeBindings;Lcom/fasterxml/jackson/databind/JavaType;", sb.toString());
    }

    @Test
    public void testGetErasedSignature() {
        Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType refType = new TypeReference<JavaType>() {}.getType();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        StringBuilder sb = new StringBuilder(100);
        JavaType result = type.getSelfReferencedType();
        type.getErasedSignature(sb);
        assertEquals("[Ljava/lang/Class;", sb.toString());
        result = refType;
        sb = new StringBuilder(100);
        type.getErasedSignature(sb);
        assertEquals("[Ljava/lang/Class;", sb.toString());
    }

    @Test
    public void testWithContentType() {
        Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType refType = new TypeReference<JavaType>() {}.getType();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        JavaType contentType = new TypeReference<JavaType>() {}.getType();
        ResolvedRecursiveType result = type.withContentType(contentType);
        assertEquals(type, result);
    }

    @Test
    public void testWithTypeHandler() {
        Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType refType = new TypeReference<JavaType>() {}.getType();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        Object handler = new Object();
        ResolvedRecursiveType result = type.withTypeHandler(handler);
        assertEquals(type, result);
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType refType = new TypeReference<JavaType>() {}.getType();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        Object handler = new Object();
        ResolvedRecursiveType result = type.withContentTypeHandler(handler);
        assertEquals(type, result);
    }

    @Test
    public void testWithValueHandler() {
        Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType refType = new TypeReference<JavaType>() {}.getType();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        Object handler = new Object();
        ResolvedRecursiveType result = type.withValueHandler(handler);
        assertEquals(type, result);
    }

    @Test
    public void testWithContentValueHandler() {
        Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType refType = new TypeReference<JavaType>() {}.getType();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        Object handler = new Object();
        ResolvedRecursiveType result = type.withContentValueHandler(handler);
        assertEquals(type, result);
    }

    @Test
    public void testWithStaticTyping() {
        Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType refType = new TypeReference<JavaType>() {}.getType();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        ResolvedRecursiveType result = type.withStaticTyping();
        assertEquals(type, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNarrow() {
        Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType refType = new TypeReference<JavaType>() {}.getType();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        Class<?> subclass = ResolvedRecursiveType.class;
        ResolvedRecursiveType result = type._narrow(subclass);
    }

    @Test
    public void testRefine() {
        Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType refType = new TypeReference<JavaType>() {}.getType();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        Class<?> rawType = Object.class;
        TypeBindings newBindings = new TypeBindings();
        JavaType superClass = new TypeReference<JavaType>() {}.getType();
        JavaType[] superInterfaces = new JavaType[] { new TypeReference<JavaType>() {}.getType() };
        ResolvedRecursiveType result = type.refine(rawType, newBindings, superClass, superInterfaces);
        assertNull(result);
    }

    @Test
    public void testIsContainerType() {
        Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType refType = new TypeReference<JavaType>() {}.getType();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        assertFalse(type.isContainerType());
    }

    @Test
    public void testToString() {
        Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType refType = new TypeReference<JavaType>() {}.getType();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        JavaType selfRefType = new TypeReference<JavaType>() {}.getType();
        type.setReference(selfRefType);
        String expected = "[recursive type; " + selfRefType.getRawClass().getName() + "]";
        assertEquals(expected, type.toString());
    }

    @Test
    public void testEquals() {
        Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType refType1 = new TypeReference<JavaType>() {}.getType();
        JavaType refType2 = new TypeReference<JavaType>() {}.getType();
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(erasedType, bindings);
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(erasedType, bindings);
        JavaType selfRefType1 = refType1;
        JavaType selfRefType2 = refType2;
        type1.setReference(selfRefType1);
        type2.setReference(selfRefType2);
        assertTrue(type1.equals(type2));
    }

    @Test
    public void testEqualsDifferentTypes() {
        Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType refType1 = new TypeReference<JavaType>() {}.getType();
        JavaType refType2 = new TypeReference<JavaType>() {}.getType();
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(erasedType, bindings);
        Object other = new Object();
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(erasedType, bindings);
        JavaType selfRefType1 = refType1;
        JavaType selfRefType2 = refType2;
        type1.setReference(selfRefType1);
        type2.setReference(selfRefType2);
        assertFalse(type1.equals(other));
    }

    @Test
    public void testEqualsNull() {
        Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType refType = new TypeReference<JavaType>() {}.getType();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        assertFalse(type.equals(null));
    }
}