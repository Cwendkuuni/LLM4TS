package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.JavaType.TypeHandler;

public class ReferenceTypeTest {

    @Test
    public void testReferenceTypeConstruction() {
        JavaType superClass = TypeFactory.defaultInstance().constructType(String.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType referenceType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), superClass, new JavaType[0], refType);
        JavaType contentType = referenceType.getContentType();
        assertEquals(Integer.class, contentType.getRawClass());
    }

    @Test
    public void testReferenceTypeUpgrade() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType referenceType = ReferenceType.upgradeFrom(baseType, refType);
        JavaType contentType = referenceType.getContentType();
        assertEquals(Integer.class, contentType.getRawClass());
    }

    @Test
    public void testReferenceTypeWithContentType() {
        JavaType superClass = TypeFactory.defaultInstance().constructType(String.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType referenceType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), superClass, new JavaType[0], refType);
        JavaType newContentType = referenceType.withContentType(TypeFactory.defaultInstance().constructType(Long.class));
        JavaType newContentTypeExpected = TypeFactory.defaultInstance().constructType(Long.class);
        assertEquals(newContentTypeExpected, newContentType);
    }

    @Test
    public void testReferenceTypeWithTypeHandler() {
        JavaType superClass = TypeFactory.defaultInstance().constructType(String.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType referenceType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), superClass, new JavaType[0], refType);
        TypeHandler<Object> originalHandler = referenceType.getTypeHandler();
        TypeHandler<Object> newHandler = mock(TypeHandler.class);
        JavaType newReferenceType = referenceType.withTypeHandler(newHandler);
        assertNotSame(originalHandler, newHandler);
        assertEquals(newHandler, newReferenceType.getTypeHandler());
    }

    @Test
    public void testReferenceTypeWithContentTypeHandler() {
        JavaType superClass = TypeFactory.defaultInstance().constructType(String.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType referenceType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), superClass, new JavaType[0], refType);
        TypeHandler<Object> originalValueHandler = referenceType.getValueHandler();
        TypeHandler<Object> newValueHandler = mock(TypeHandler.class);
        JavaType newContentTypeHandler = referenceType.withContentValueHandler(newValueHandler);
        assertNotSame(originalValueHandler, newValueHandler);
        assertEquals(newValueHandler, newContentTypeHandler.getValueHandler());
    }

    @Test
    public void testReferenceTypeWithStaticTyping() {
        JavaType superClass = TypeFactory.defaultInstance().constructType(String.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType referenceType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), superClass, new JavaType[0], refType);
        JavaType staticReferenceType = referenceType.withStaticTyping();
        assertTrue(staticReferenceType.isStatic());
    }

    @Test
    public void testReferenceTypeGetContentType() {
        JavaType superClass = TypeFactory.defaultInstance().constructType(String.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType referenceType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), superClass, new JavaType[0], refType);
        JavaType contentType = referenceType.getContentType();
        assertEquals(Integer.class, contentType.getRawClass());
    }

    @Test
    public void testReferenceTypeIsReferenceType() {
        JavaType superClass = TypeFactory.defaultInstance().constructType(String.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType referenceType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), superClass, new JavaType[0], refType);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testReferenceTypeGetAnchorType() {
        JavaType superClass = TypeFactory.defaultInstance().constructType(String.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType referenceType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), superClass, new JavaType[0], refType);
        JavaType anchorType = referenceType.getAnchorType();
        assertEquals(referenceType, anchorType);
    }

    @Test
    public void testReferenceTypeIsAnchorType() {
        JavaType superClass = TypeFactory.defaultInstance().constructType(String.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType referenceType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), superClass, new JavaType[0], refType);
        assertTrue(referenceType.isAnchorType());
    }
}