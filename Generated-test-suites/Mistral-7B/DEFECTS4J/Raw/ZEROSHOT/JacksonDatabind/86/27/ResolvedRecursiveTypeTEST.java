package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testResolvedRecursiveTypeConstruction() {
        Class<ResolvedRecursiveType> type = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType javaType = new TypeReference<ResolvedRecursiveType>() {}.getType();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(type, bindings);
        assertNotNull(resolvedRecursiveType);
        assertEquals(type, resolvedRecursiveType.getErasedType());
        assertNull(resolvedRecursiveType.getSelfReferencedType());
    }

    @Test
    public void testSetReference() {
        Class<ResolvedRecursiveType> type = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType javaType = new TypeReference<ResolvedRecursiveType>() {}.getType();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(type, bindings);
        JavaType referencedType = new TypeReference<String>() {}.getType();
        resolvedRecursiveType.setReference(referencedType);
        JavaType selfReferencedType = resolvedRecursiveType.getSelfReferencedType();
        assertNull(selfReferencedType);
        selfReferencedType = resolvedRecursiveType.getSelfReferencedType();
        assertEquals(referencedType, selfReferencedType);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        Class<ResolvedRecursiveType> type = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType javaType = new TypeReference<ResolvedRecursiveType>() {}.getType();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(type, bindings);
        JavaType referencedType = new TypeReference<String>() {}.getType();
        resolvedRecursiveType.setReference(referencedType);
        resolvedRecursiveType.setReference(new TypeReference<Integer>() {}.getType());
    }

    @Test
    public void testGetSelfReferencedType() {
        Class<ResolvedRecursiveType> type = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType javaType = new TypeReference<ResolvedRecursiveType>() {}.getType();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(type, bindings);
        JavaType referencedType = new TypeReference<String>() {}.getType();
        resolvedRecursiveType.setReference(referencedType);
        JavaType selfReferencedType = resolvedRecursiveType.getSelfReferencedType();
        assertEquals(referencedType, selfReferencedType);
    }

    @Test
    public void testGetGenericSignature() {
        Class<ResolvedRecursiveType> type = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType javaType = new TypeReference<ResolvedRecursiveType>() {}.getType();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(type, bindings);
        StringBuilder sb = new StringBuilder();
        resolvedRecursiveType.getGenericSignature(sb);
        assertNull(sb.toString());

        JavaType referencedType = new TypeReference<String>() {}.getType();
        resolvedRecursiveType.setReference(referencedType);
        sb = new StringBuilder();
        resolvedRecursiveType.getGenericSignature(sb);
        String expected = "<T extends java.lang.Object>";
        assertEquals(expected, sb.toString());

        TypeBindings bindingsWithTypeParameter = new TypeBindings();
        bindingsWithTypeParameter.putBinding(type, new TypeBindings.TypeBinding() {
            @Override
            public JavaType getBindingType() {
                return new TypeReference<Integer>() {}.getType();
            }
        });
        resolvedRecursiveType = new ResolvedRecursiveType(type, bindingsWithTypeParameter);
        sb = new StringBuilder();
        resolvedRecursiveType.getGenericSignature(sb);
        expected = "<T extends java.lang.Integer>";
        assertEquals(expected, sb.toString());
    }

    @Test
    public void testGetErasedSignature() {
        Class<ResolvedRecursiveType> type = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType javaType = new TypeReference<ResolvedRecursiveType>() {}.getType();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(type, bindings);
        StringBuilder sb = new StringBuilder();
        resolvedRecursiveType.getErasedSignature(sb);
        assertNull(sb.toString());

        JavaType referencedType = new TypeReference<String>() {}.getType();
        resolvedRecursiveType.setReference(referencedType);
        sb = new StringBuilder();
        resolvedRecursiveType.getErasedSignature(sb);
        String expected = "Ljava/lang/ResolvedRecursiveType;";
        assertEquals(expected, sb.toString());
    }

    @Test
    public void testWithContentType() {
        Class<ResolvedRecursiveType> type = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType javaType = new TypeReference<ResolvedRecursiveType>() {}.getType();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(type, bindings);
        JavaType contentType = new TypeReference<String>() {}.getType();
        resolvedRecursiveType.withContentType(contentType);
        assertNotNull(resolvedRecursiveType);
        assertNull(resolvedRecursiveType.getSelfReferencedType());
    }

    @Test
    public void testWithTypeHandler() {
        Class<ResolvedRecursiveType> type = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType javaType = new TypeReference<ResolvedRecursiveType>() {}.getType();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(type, bindings);
        Object handler = new Object() {};
        resolvedRecursiveType.withTypeHandler(handler);
        assertNotNull(resolvedRecursiveType);
        assertNull(resolvedRecursiveType.getSelfReferencedType());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<ResolvedRecursiveType> type = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType javaType = new TypeReference<ResolvedRecursiveType>() {}.getType();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(type, bindings);
        Object handler = new Object() {};
        resolvedRecursiveType.withContentTypeHandler(handler);
        assertNotNull(resolvedRecursiveType);
        assertNull(resolvedRecursiveType.getSelfReferencedType());
    }

    @Test
    public void testWithValueHandler() {
        Class<ResolvedRecursiveType> type = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType javaType = new TypeReference<ResolvedRecursiveType>() {}.getType();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(type, bindings);
        Object handler = new Object() {};
        resolvedRecursiveType.withValueHandler(handler);
        assertNotNull(resolvedRecursiveType);
        assertNull(resolvedRecursiveType.getSelfReferencedType());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<ResolvedRecursiveType> type = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType javaType = new TypeReference<ResolvedRecursiveType>() {}.getType();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(type, bindings);
        Object handler = new Object() {};
        resolvedRecursiveType.withContentValueHandler(handler);
        assertNotNull(resolvedRecursiveType);
        assertNull(resolvedRecursiveType.getSelfReferencedType());
    }

    @Test
    public void testWithStaticTyping() {
        Class<ResolvedRecursiveType> type = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType javaType = new TypeReference<ResolvedRecursiveType>() {}.getType();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(type, bindings);
        resolvedRecursiveType.withStaticTyping();
        assertNotNull(resolvedRecursiveType);
        assertNull(resolvedRecursiveType.getSelfReferencedType());
    }

    @Test
    public void testNarrow() {
        Class<ResolvedRecursiveType> type = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType javaType = new TypeReference<ResolvedRecursiveType>() {}.getType();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(type, bindings);
        Class<Object> subclass = Object.class;
        JavaType narrowType = resolvedRecursiveType._narrow(subclass);
        assertNull(narrowType);
    }

    @Test
    public void testRefine() {
        Class<ResolvedRecursiveType> type = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType javaType = new TypeReference<ResolvedRecursiveType>() {}.getType();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(type, bindings);
        Class<String> rawType = String.class;
        TypeBindings newBindings = new TypeBindings();
        JavaType superClass = new TypeReference<Object>() {}.getType();
        JavaType[] superInterfaces = new JavaType[0];
        JavaType refinedType = resolvedRecursiveType.refine(rawType, newBindings, superClass, superInterfaces);
        assertNull(refinedType);
    }

    @Test
    public void testIsContainerType() {
        Class<ResolvedRecursiveType> type = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType javaType = new TypeReference<ResolvedRecursiveType>() {}.getType();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(type, bindings);
        assertFalse(resolvedRecursiveType.isContainerType());
    }

    @Test
    public void testToString() {
        Class<ResolvedRecursiveType> type = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType javaType = new TypeReference<ResolvedRecursiveType>() {}.getType();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(type, bindings);
        String expected = "[recursive type; UNRESOLVED]";
        assertEquals(expected, resolvedRecursiveType.toString());

        JavaType referencedType = new TypeReference<String>() {}.getType();
        resolvedRecursiveType.setReference(referencedType);
        expected = "[recursive type; java.lang.String]";
        assertEquals(expected, resolvedRecursiveType.toString());
    }

    @Test
    public void testEquals() {
        Class<ResolvedRecursiveType> type = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType javaType = new TypeReference<ResolvedRecursiveType>() {}.getType();
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(type, bindings);
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(type, bindings);
        assertNotNull(resolvedRecursiveType1);
        assertNotNull(resolvedRecursiveType2);
        assertTrue(resolvedRecursiveType1.equals(resolvedRecursiveType2));

        JavaType referencedType = new TypeReference<String>() {}.getType();
        resolvedRecursiveType1.setReference(referencedType);
        JavaType referencedType2 = new TypeReference<String>() {}.getType();
        resolvedRecursiveType2.setReference(referencedType2);
        assertTrue(resolvedRecursiveType1.equals(resolvedRecursiveType2));

        assertFalse(resolvedRecursiveType1.equals(""));

        JavaType referencedType3 = new TypeReference<Integer>() {}.getType();
        resolvedRecursiveType1.setReference(referencedType3);
        resolvedRecursiveType2.setReference(referencedType);
        assertFalse(resolvedRecursiveType1.equals(resolvedRecursiveType2));

        ResolvedRecursiveType resolvedRecursiveType3 = new ResolvedRecursiveType(type, bindings);
        assertFalse(resolvedRecursiveType1.equals(resolvedRecursiveType3));
    }
}