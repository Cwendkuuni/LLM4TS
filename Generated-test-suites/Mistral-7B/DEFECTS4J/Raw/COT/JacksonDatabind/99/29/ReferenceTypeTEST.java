package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.lang.ClassCastException;
import java.lang.ClassNotFoundException;
import java.lang.IllegalArgumentException;
import java.lang.reflect.Type;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeBuilder;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonTypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.type.ClassType;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeModifier;
import com.fasterxml.jackson.databind.type.TypeUtil;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class ReferenceTypeTest {

    @Test
    public void testConstructors() {
        JavaType superClass = TypeFactory.defaultInstance().constructType(Object.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType anchorType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType baseType = new ClassType(ReferenceType.class);

        // Test construct with all arguments
        ReferenceType referenceType1 = new ReferenceType(ReferenceType.class, TypeBindings.emptyBindings(), superClass,
                new Class<?>[]{Double.class}, refType, anchorType, null, null, false);
        assertEquals(ReferenceType.class, referenceType1.getClass());
        assertEquals(superClass, referenceType1.getSuperClass());
        assertEquals(new Class<?>[]{Double.class}, referenceType1.getSuperInterfaces());
        assertEquals(refType, referenceType1.getReferencedType());
        assertEquals(anchorType, referenceType1.getAnchorType());

        // Test constructor for upgrading a basic type
        JavaType baseTypeWithoutAnchor = new ClassType(SimpleType.class);
        ReferenceType referenceType2 = new ReferenceType(baseTypeWithoutAnchor, refType);
        assertEquals(ReferenceType.class, referenceType2.getClass());
        assertEquals(superClass, referenceType2.getSuperClass());
        assertNull(referenceType2.getSuperInterfaces());
        assertEquals(refType, referenceType2.getReferencedType());
        assertEquals(referenceType2, referenceType2.getAnchorType());

        // Test static factory method
        ReferenceType referenceType3 = ReferenceType.construct(ReferenceType.class, TypeBindings.emptyBindings(), superClass,
                new Class<?>[]{Double.class}, refType);
        assertEquals(ReferenceType.class, referenceType3.getClass());
        assertEquals(superClass, referenceType3.getSuperClass());
        assertEquals(new Class<?>[]{Double.class}, referenceType3.getSuperInterfaces());
        assertEquals(refType, referenceType3.getReferencedType());
        assertEquals(referenceType3, referenceType3.getAnchorType());

        // Test deprecated constructor
        ReferenceType referenceType4 = new ReferenceType(ReferenceType.class, refType);
        assertFalse(referenceType4.isStatic());
        assertEquals(ReferenceType.class, referenceType4.getClass());
        assertEquals(superClass, referenceType4.getSuperClass());
        assertNull(referenceType4.getSuperInterfaces());
        assertEquals(refType, referenceType4.getReferencedType());
        assertEquals(referenceType4, referenceType4.getAnchorType());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new ClassType(SimpleType.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);

        // Test with valid types
        ReferenceType referenceType1 = ReferenceType.upgradeFrom(baseType, refType);
        assertEquals(ReferenceType.class, referenceType1.getClass());
        assertEquals(baseType, referenceType1.getSuperClass());
        assertNull(referenceType1.getSuperInterfaces());
        assertEquals(refType, referenceType1.getReferencedType());
        assertEquals(referenceType1, referenceType1.getAnchorType());

        // Test with invalid types
        assertThrows(IllegalArgumentException.class, () -> ReferenceType.upgradeFrom(null, refType));
        assertThrows(IllegalArgumentException.class, () -> ReferenceType.upgradeFrom(baseType, null));
        assertThrows(IllegalArgumentException.class, () -> ReferenceType.upgradeFrom(new ClassType(List.class), refType));
    }

    @Test
    public void testWithContentType() {
        JavaType superClass = TypeFactory.defaultInstance().constructType(Object.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType contentType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType referenceType = new ReferenceType(ReferenceType.class, TypeBindings.emptyBindings(), superClass,
                new Class<?>[]{Double.class}, refType);

        JavaType result = referenceType.withContentType(contentType);
        assertEquals(ReferenceType.class, result.getClass());
        assertEquals(superClass, result.getSuperClass());
        assertNull(result.getSuperInterfaces());
        assertEquals(contentType, result.getReferencedType());
        assertEquals(referenceType, result.getAnchorType());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType superClass = TypeFactory.defaultInstance().constructType(Object.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType handler = new JsonTypeDeserializer();
        JavaType referenceType = new ReferenceType(ReferenceType.class, TypeBindings.emptyBindings(), superClass,
                new Class<?>[]{Double.class}, refType);

        JavaType result = referenceType.withTypeHandler(handler);
        assertEquals(ReferenceType.class, result.getClass());
        assertEquals(superClass, result.getSuperClass());
        assertNull(result.getSuperInterfaces());
        assertEquals(refType, result.getReferencedType());
        assertEquals(referenceType, result.getAnchorType());
        assertEquals(handler, result.<TypeDeserializer>getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType superClass = TypeFactory.defaultInstance().constructType(Object.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType handler = new JsonTypeDeserializer();
        JavaType referenceType = new ReferenceType(ReferenceType.class, TypeBindings.emptyBindings(), superClass,
                new Class<?>[]{Double.class}, refType);

        JavaType result = referenceType.withContentTypeHandler(handler);
        assertEquals(ReferenceType.class, result.getClass());
        assertEquals(superClass, result.getSuperClass());
        assertNull(result.getSuperInterfaces());
        assertEquals(refType, result.getReferencedType());
        assertEquals(referenceType, result.getAnchorType());
        assertEquals(handler, result.<TypeDeserializer>getValueHandler());
    }

    @Test
    public void testWithValueHandler() {
        JavaType superClass = TypeFactory.defaultInstance().constructType(Object.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType handler = new JsonTypeDeserializer();
        JavaType referenceType = new ReferenceType(ReferenceType.class, TypeBindings.emptyBindings(), superClass,
                new Class<?>[]{Double.class}, refType);

        JavaType result = referenceType.withValueHandler(handler);
        assertEquals(ReferenceType.class, result.getClass());
        assertEquals(superClass, result.getSuperClass());
        assertNull(result.getSuperInterfaces());
        assertEquals(refType, result.getReferencedType());
        assertEquals(referenceType, result.getAnchorType());
        assertEquals(handler, result.<JsonTypeDeserializer>getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType superClass = TypeFactory.defaultInstance().constructType(Object.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType handler = new JsonTypeDeserializer();
        JavaType referenceType = new ReferenceType(ReferenceType.class, TypeBindings.emptyBindings(), superClass,
                new Class<?>[]{Double.class}, refType);

        JavaType result = referenceType.withContentValueHandler(handler);
        assertEquals(ReferenceType.class, result.getClass());
        assertEquals(superClass, result.getSuperClass());
        assertNull(result.getSuperInterfaces());
        assertEquals(refType, result.getReferencedType());
        assertEquals(referenceType, result.getAnchorType());
        assertEquals(handler, result.<JsonTypeDeserializer>getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType superClass = TypeFactory.defaultInstance().constructType(Object.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = new ReferenceType(ReferenceType.class, TypeBindings.emptyBindings(), superClass,
                new Class<?>[]{Double.class}, refType);

        JavaType result = referenceType.withStaticTyping();
        assertEquals(ReferenceType.class, result.getClass());
        assertEquals(superClass, result.getSuperClass());
        assertNull(result.getSuperInterfaces());
        assertEquals(refType, result.getReferencedType());
        assertEquals(referenceType, result.getAnchorType());
        assertTrue(result.isStatic());
    }

    @Test
    public void testRefine() {
        JavaType rawType = ReferenceType.class;
        JavaType superClass = TypeFactory.defaultInstance().constructType(Object.class);
        JavaType[] superInterfaces = new Class<?>[]{Double.class};
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);

        JavaType result = new ReferenceType(rawType, TypeBindings.emptyBindings(), superClass, superInterfaces, refType)
                .refine(rawType, TypeBindings.emptyBindings(), superClass, superInterfaces);
        assertEquals(ReferenceType.class, result.getClass());
        assertEquals(superClass, result.getSuperClass());
        assertArrayEquals(superInterfaces, result.getSuperInterfaces());
        assertEquals(refType, result.getReferencedType());
        assertEquals(result, result.getAnchorType());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = new ReferenceType(ReferenceType.class, TypeBindings.emptyBindings(), null, null, refType);
        StringBuilder expected = new StringBuilder("[Ljava/lang/String;");
        StringBuilder result = referenceType.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = new ReferenceType(ReferenceType.class, TypeBindings.emptyBindings(), null, null, refType);
        StringBuilder expected = new StringBuilder("[Ljava/lang/String;<T extends java.lang.Object>");
        StringBuilder result = referenceType.getGenericSignature(new StringBuilder());
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testGetContentType() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = new ReferenceType(ReferenceType.class, TypeBindings.emptyBindings(), null, null, refType);
        assertEquals(refType, referenceType.getContentType());
    }

    @Test
    public void testGetReferencedType() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = new ReferenceType(ReferenceType.class, TypeBindings.emptyBindings(), null, null, refType);
        assertEquals(refType, referenceType.getReferencedType());
    }

    @Test
    public void testHasContentType() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = new ReferenceType(ReferenceType.class, TypeBindings.emptyBindings(), null, null, refType);
        assertTrue(referenceType.hasContentType());
    }

    @Test
    public void testIsReferenceType() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = new ReferenceType(ReferenceType.class, TypeBindings.emptyBindings(), null, null, refType);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testToString() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = new ReferenceType(ReferenceType.class, TypeBindings.emptyBindings(), null, null, refType);
        StringBuilder expected = new StringBuilder("[reference type, class java.lang.String, referenced type java.lang.String]");
        StringBuilder result = new StringBuilder(referenceType.toString());
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testEquals() {
        JavaType refType1 = TypeFactory.defaultInstance().constructType(String.class);
        JavaType refType2 = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType1 = new ReferenceType(ReferenceType.class, TypeBindings.emptyBindings(), null, null, refType1);
        JavaType referenceType2 = new ReferenceType(ReferenceType.class, TypeBindings.emptyBindings(), null, null, refType2);
        JavaType referenceType3 = new ReferenceType(ReferenceType.class, TypeBindings.emptyBindings(), null, null, refType1);

        assertTrue(referenceType1.equals(referenceType1));
        assertTrue(referenceType1.equals(referenceType3));
        assertFalse(referenceType1.equals(referenceType2));
    }

    @Test
    public void testNarrow() {
        JavaType baseType = new ClassType(ReferenceType.class);
        JavaType subclass = new ClassType(ReferenceType.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);

        ReferenceType referenceType = new ReferenceType(baseType, TypeBindings.emptyBindings(), null, null, refType);
        JavaType result = referenceType._narrow(subclass);
        assertEquals(subclass, result);
    }

    @Test(expected = DeprecationWarning.class)
    public void testDeprecatedNarrow() {
        JavaType baseType = new ClassType(ReferenceType.class);
        JavaType subclass = new ClassType(ReferenceType.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);

        ReferenceType referenceType = new ReferenceType(baseType, TypeBindings.emptyBindings(), null, null, refType);
        JavaType result = referenceType._narrow(subclass);
        assertEquals(subclass, result);
    }

    @Test(expected = ClassCastException.class)
    public void testNarrowInvalidType() {
        JavaType baseType = new ClassType(SimpleType.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);

        ReferenceType referenceType = new ReferenceType(baseType, TypeBindings.emptyBindings(), null, null, refType);
        JavaType result = referenceType._narrow(new ClassType(List.class));
        fail("Expected ClassCastException");
    }
}