package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.util.Collections;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testResolvedRecursiveTypeCreation() {
        Class<ResolvedRecursiveType> typeToTest = ResolvedRecursiveType.class;
        TypeBindings bindings = TypeBindings.empty();
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaType = typeFactory.constructType(typeToTest);
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(typeToTest, bindings);
        assertNotNull(resolvedType);
        assertEquals(javaType, resolvedType.getSelfReferencedType());
    }

    @Test
    public void testSettingReference() {
        Class<ResolvedRecursiveType> typeToTest = ResolvedRecursiveType.class;
        TypeBindings bindings = TypeBindings.empty();
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaType = typeFactory.constructType(typeToTest);
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(typeToTest, bindings);
        JavaType refType = typeFactory.constructType(ResolvedRecursiveType.class);
        resolvedType.setReference(refType);
        JavaType expected = refType;
        JavaType actual = resolvedType.getSelfReferencedType();
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalStateException.class)
    public void testSettingReferenceTwice() {
        Class<ResolvedRecursiveType> typeToTest = ResolvedRecursiveType.class;
        TypeBindings bindings = TypeBindings.empty();
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaType = typeFactory.constructType(typeToTest);
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(typeToTest, bindings);
        JavaType refType = typeFactory.constructType(ResolvedRecursiveType.class);
        resolvedType.setReference(refType);
        resolvedType.setReference(refType);
    }

    @Test
    public void testGetGenericSignature() {
        Class<ResolvedRecursiveType> typeToTest = ResolvedRecursiveType.class;
        TypeBindings bindings = TypeBindings.empty();
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaType = typeFactory.constructType(typeToTest);
        StringBuilder stringBuilder = new StringBuilder();
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(typeToTest, bindings);
        resolvedType.getGenericSignature(stringBuilder);
        assertNull(stringBuilder.toString());

        JavaType refType = typeFactory.constructType(ResolvedRecursiveType.class);
        resolvedType.setReference(refType);
        stringBuilder = new StringBuilder();
        resolvedType.getGenericSignature(stringBuilder);
        String expected = "<" + refType.getRawClass().getName().replaceAll("\\$", "") + ">";
        String actual = stringBuilder.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetErasedSignature() {
        Class<ResolvedRecursiveType> typeToTest = ResolvedRecursiveType.class;
        TypeBindings bindings = TypeBindings.empty();
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaType = typeFactory.constructType(typeToTest);
        StringBuilder stringBuilder = new StringBuilder();
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(typeToTest, bindings);
        resolvedType.getErasedSignature(stringBuilder);
        String expected = typeToTest.getSimpleName();
        String actual = stringBuilder.toString();
        assertEquals(expected, actual);

        JavaType refType = typeFactory.constructType(ResolvedRecursiveType.class);
        resolvedType.setReference(refType);
        stringBuilder = new StringBuilder();
        resolvedType.getErasedSignature(stringBuilder);
        expected = refType.getRawClass().getName();
        actual = stringBuilder.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testMethodsThatReturnThisType() {
        Class<ResolvedRecursiveType> typeToTest = ResolvedRecursiveType.class;
        TypeBindings bindings = TypeBindings.empty();
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaType = typeFactory.constructType(typeToTest);
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(typeToTest, bindings);

        JavaType result = resolvedType.withContentType(JavaType.construct(Collections.class, stringBuilder.getClass()));
        assertEquals(resolvedType, result);

        result = resolvedType.withTypeHandler(new Object() {});
        assertEquals(resolvedType, result);

        result = resolvedType.withContentTypeHandler(new Object() {});
        assertEquals(resolvedType, result);

        result = resolvedType.withValueHandler(new Object() {});
        assertEquals(resolvedType, result);

        result = resolvedType.withContentValueHandler(new Object() {});
        assertEquals(resolvedType, result);

        result = resolvedType.withStaticTyping();
        assertEquals(resolvedType, result);

        JavaType expected = resolvedType;
        JavaType actual = resolvedType.withContentType(JavaType.construct(Collections.class, String.class));
        assertEquals(expected, actual);
    }

    @Test
    public void testMethodsThatReturnNull() {
        Class<ResolvedRecursiveType> typeToTest = ResolvedRecursiveType.class;
        TypeBindings bindings = TypeBindings.empty();
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaType = typeFactory.constructType(typeToTest);
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(typeToTest, bindings);

        JavaType result = resolvedType.withContentType(_nullType());
        assertNull(result);

        result = resolvedType.withTypeHandler(_nullObject());
        assertNull(result);

        result = resolvedType.withContentTypeHandler(_nullObject());
        assertNull(result);

        result = resolvedType.withValueHandler(_nullObject());
        assertNull(result);

        result = resolvedType.withContentValueHandler(_nullObject());
        assertNull(result);

        result = resolvedType.withStaticTyping();
        assertNull(result);
    }

    @Test
    public void testEquals() {
        Class<ResolvedRecursiveType> typeToTest = ResolvedRecursiveType.class;
        TypeBindings bindings = TypeBindings.empty();
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaType = typeFactory.constructType(typeToTest);

        ResolvedRecursiveType resolvedType1 = new ResolvedRecursiveType(typeToTest, bindings);
        JavaType refType1 = typeFactory.constructType(ResolvedRecursiveType.class);
        resolvedType1.setReference(refType1);

        ResolvedRecursiveType resolvedType2 = new ResolvedRecursiveType(typeToTest, bindings);
        JavaType refType2 = typeFactory.constructType(ResolvedRecursiveType.class);
        resolvedType2.setReference(refType2);

        ResolvedRecursiveType resolvedType3 = new ResolvedRecursiveType(typeToTest, bindings);
        assertFalse(resolvedType1.equals(resolvedType3));

        assertTrue(resolvedType1.equals(resolvedType1));
        assertTrue(resolvedType1.equals(resolvedType2));
        assertTrue(resolvedType1.equals(resolvedType2.getSelfReferencedType()));
    }

    @Test
    public void testToString() {
        Class<ResolvedRecursiveType> typeToTest = ResolvedRecursiveType.class;
        TypeBindings bindings = TypeBindings.empty();
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaType = typeFactory.constructType(typeToTest);

        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(typeToTest, bindings);
        String expected = "[recursive type; UNRESOLVED]";
        String actual = resolvedType.toString();
        assertEquals(expected, actual);

        JavaType refType = typeFactory.constructType(ResolvedRecursiveType.class);
        resolvedType.setReference(refType);
        String recursiveTypeString = "[recursive type; ";
        String expectedTypeString = refType.getRawClass().getName();
        expectedTypeString = expectedTypeString.replaceAll("\\$", "").replaceAll(".", "\\.");
        String actualTypeString = resolvedType.toString();
        assertEquals(recursiveTypeString + expectedTypeString + "]", actualTypeString);
    }

    private static JavaType _nullType() {
        return null;
    }

    private static Object _nullObject() {
        return null;
    }
}