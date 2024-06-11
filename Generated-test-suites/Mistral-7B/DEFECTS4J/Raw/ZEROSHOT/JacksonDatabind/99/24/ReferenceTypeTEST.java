package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class ReferenceTypeTest {

    @Test
    public void testReferenceTypeConstruction() {
        JavaType intType = JavaType.construct(int.class);
        JavaType referencedType = JavaType.construct(String.class);
        ReferenceType referenceType = ReferenceType.construct(int.class, TypeBindings.emptyBindings(), null, new JavaType[0], referencedType);
        assertEquals(int.class, referenceType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), referenceType.getTypeBindings());
        assertEquals(null, referenceType.getSuperClass());
        assertEquals(new JavaType[0], referenceType.getSuperInterfaces());
        assertEquals(String.class, referenceType.getContentType());
        assertEquals(referencedType, referenceType.getReferencedType());
        assertEquals(true, referenceType.isReferenceType());
        assertEquals(false, referenceType.hasContentTypeHandler());
        assertEquals(false, referenceType.hasValueHandler());
        assertEquals(false, referenceType.isAnchorType());
        assertEquals("class int[reference type, class java.lang.String]", referenceType.toString());
        assertEquals(Id.CLASS, referenceType.getId());
    }

    @Test
    public void testReferenceTypeUpgrade() {
        JavaType intType = JavaType.construct(int.class);
        JavaType referencedType = JavaType.construct(String.class);
        JavaType baseType = JavaType.construct(Integer.class);
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, referencedType);
        assertEquals(Integer.class, referenceType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), referenceType.getTypeBindings());
        assertEquals(null, referenceType.getSuperClass());
        assertEquals(new JavaType[0], referenceType.getSuperInterfaces());
        assertEquals(String.class, referenceType.getContentType());
        assertEquals(referencedType, referenceType.getReferencedType());
        assertEquals(true, referenceType.isReferenceType());
        assertEquals(false, referenceType.hasContentTypeHandler());
        assertEquals(false, referenceType.hasValueHandler());
        assertEquals(false, referenceType.isAnchorType());
        assertEquals("class java.lang.Integer[reference type, class java.lang.String]", referenceType.toString());
        assertEquals(Id.CLASS, referenceType.getId());
    }

    @Test
    public void testReferenceTypeWithContentType() {
        JavaType intType = JavaType.construct(int.class);
        JavaType referencedType = JavaType.construct(String.class);
        JavaType contentType = JavaType.construct(Long.class);
        ReferenceType referenceType = new ReferenceType(int.class, TypeBindings.emptyBindings(), null, new JavaType[0], referencedType, null, null, null, false);
        ReferenceType newReferenceType = referenceType.withContentType(contentType);
        assertEquals(int.class, newReferenceType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), newReferenceType.getTypeBindings());
        assertEquals(null, newReferenceType.getSuperClass());
        assertEquals(new JavaType[0], newReferenceType.getSuperInterfaces());
        assertEquals(Long.class, newReferenceType.getContentType());
        assertEquals(referencedType, newReferenceType.getReferencedType());
        assertEquals(true, newReferenceType.isReferenceType());
        assertEquals(false, newReferenceType.hasContentTypeHandler());
        assertEquals(false, newReferenceType.hasValueHandler());
        assertEquals(false, newReferenceType.isAnchorType());
        assertEquals("class int[reference type, class java.lang.String]", referenceType.toString());
        assertEquals("class int[reference type, class java.lang.Long]", newReferenceType.toString());
    }

    @Test
    public void testReferenceTypeWithTypeHandler() {
        JavaType intType = JavaType.construct(int.class);
        JavaType referencedType = JavaType.construct(String.class);
        Object typeHandler = new Object();
        ReferenceType referenceType = new ReferenceType(int.class, TypeBindings.emptyBindings(), null, new JavaType[0], referencedType, null, null, null, false);
        ReferenceType newReferenceType = referenceType.withTypeHandler(typeHandler);
        assertEquals(int.class, newReferenceType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), newReferenceType.getTypeBindings());
        assertEquals(null, newReferenceType.getSuperClass());
        assertEquals(new JavaType[0], newReferenceType.getSuperInterfaces());
        assertEquals(String.class, newReferenceType.getContentType());
        assertEquals(referencedType, newReferenceType.getReferencedType());
        assertEquals(true, newReferenceType.isReferenceType());
        assertEquals(false, newReferenceType.hasContentTypeHandler());
        assertEquals(typeHandler, newReferenceType.getTypeHandler());
        assertEquals(false, newReferenceType.hasValueHandler());
        assertEquals(false, newReferenceType.isAnchorType());
        assertEquals("class int[reference type, class java.lang.String]", referenceType.toString());
        assertEquals("class int[reference type, class java.lang.String, Object]", newReferenceType.toString());
    }

    @Test
    public void testReferenceTypeWithContentTypeHandler() {
        JavaType intType = JavaType.construct(int.class);
        JavaType referencedType = JavaType.construct(String.class);
        Object contentTypeHandler = new Object();
        ReferenceType referenceType = new ReferenceType(int.class, TypeBindings.emptyBindings(), null, new JavaType[0], referencedType, null, null, null, false);
        ReferenceType newReferenceType = referenceType.withContentTypeHandler(contentTypeHandler);
        assertEquals(int.class, newReferenceType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), newReferenceType.getTypeBindings());
        assertEquals(null, newReferenceType.getSuperClass());
        assertEquals(new JavaType[0], newReferenceType.getSuperInterfaces());
        assertEquals(String.class, newReferenceType.getContentType());
        assertEquals(referencedType, newReferenceType.getReferencedType());
        assertEquals(true, newReferenceType.isReferenceType());
        assertEquals(contentTypeHandler, newReferenceType.getContentTypeHandler());
        assertEquals(false, newReferenceType.hasTypeHandler());
        assertEquals(false, newReferenceType.hasValueHandler());
        assertEquals(false, newReferenceType.isAnchorType());
        assertEquals("class int[reference type, class java.lang.String]", referenceType.toString());
        assertEquals("class int[reference type, class java.lang.String, Object]", newReferenceType.toString());
    }

    @Test
    public void testReferenceTypeWithValueHandler() {
        JavaType intType = JavaType.construct(int.class);
        JavaType referencedType = JavaType.construct(String.class);
        Object valueHandler = new Object();
        ReferenceType referenceType = new ReferenceType(int.class, TypeBindings.emptyBindings(), null, new JavaType[0], referencedType, null, null, null, false);
        ReferenceType newReferenceType = referenceType.withValueHandler(valueHandler);
        assertEquals(int.class, newReferenceType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), newReferenceType.getTypeBindings());
        assertEquals(null, newReferenceType.getSuperClass());
        assertEquals(new JavaType[0], newReferenceType.getSuperInterfaces());
        assertEquals(String.class, newReferenceType.getContentType());
        assertEquals(referencedType, newReferenceType.getReferencedType());
        assertEquals(true, newReferenceType.isReferenceType());
        assertEquals(false, newReferenceType.hasContentTypeHandler());
        assertEquals(valueHandler, newReferenceType.getValueHandler());
        assertEquals(false, newReferenceType.hasTypeHandler());
        assertEquals(false, newReferenceType.isAnchorType());
        assertEquals("class int[reference type, class java.lang.String]", referenceType.toString());
        assertEquals("class int[reference type, class java.lang.String, Object]", newReferenceType.toString());
    }

    @Test
    public void testReferenceTypeWithContentValueHandler() {
        JavaType intType = JavaType.construct(int.class);
        JavaType referencedType = JavaType.construct(String.class);
        Object contentValueHandler = new Object();
        ReferenceType referenceType = new ReferenceType(int.class, TypeBindings.emptyBindings(), null, new JavaType[0], referencedType, null, null, null, false);
        JavaType newContentType = JavaType.construct(Long.class);
        ReferenceType newReferenceType = referenceType.withContentValueHandler(contentValueHandler);
        JavaType newContentTypeWithHandler = newReferenceType.getContentType().withValueHandler(contentValueHandler);
        assertEquals(int.class, newReferenceType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), newReferenceType.getTypeBindings());
        assertEquals(null, newReferenceType.getSuperClass());
        assertEquals(new JavaType[0], newReferenceType.getSuperInterfaces());
        assertEquals(Long.class, newContentTypeWithHandler.getRawClass());
        assertEquals(String.class, newContentTypeWithHandler.getContentType().getRawClass());
        assertEquals(referencedType, newReferenceType.getReferencedType());
        assertEquals(true, newReferenceType.isReferenceType());
        assertEquals(contentValueHandler, newReferenceType.getContentValueHandler());
        assertEquals(contentValueHandler, newContentTypeWithHandler.getValueHandler());
        assertEquals(false, newReferenceType.hasTypeHandler());
        assertEquals(false, newReferenceType.isAnchorType());
        assertEquals("class int[reference type, class java.lang.String]", referenceType.toString());
        assertEquals("class int[reference type, class java.lang.Long]", newReferenceType.toString());
    }

    @Test
    public void testReferenceTypeWithStaticTyping() {
        JavaType intType = JavaType.construct(int.class);
        JavaType referencedType = JavaType.construct(String.class);
        ReferenceType referenceType = new ReferenceType(int.class, TypeBindings.emptyBindings(), null, new JavaType[0], referencedType, null, null, null, false);
        ReferenceType newReferenceType = referenceType.withStaticTyping();
        assertEquals(int.class, newReferenceType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), newReferenceType.getTypeBindings());
        assertEquals(null, newReferenceType.getSuperClass());
        assertEquals(new JavaType[0], newReferenceType.getSuperInterfaces());
        assertEquals(String.class, newReferenceType.getContentType().getRawClass());
        assertEquals(referencedType, newReferenceType.getReferencedType());
        assertEquals(true, newReferenceType.isReferenceType());
        assertEquals(false, newReferenceType.hasContentTypeHandler());
        assertEquals(false, newReferenceType.hasValueHandler());
        assertEquals(false, newReferenceType.isAnchorType());
        assertEquals(true, newReferenceType.isStatic());
        assertEquals("class int[reference type, class java.lang.String]", referenceType.toString());
        assertEquals("class int[reference type, class java.lang.String]", newReferenceType.toString());
    }

    @Test
    public void testReferenceTypeRefine() {
        JavaType intType = JavaType.construct(int.class);
        JavaType referencedType = JavaType.construct(String.class);
        JavaType baseType = JavaType.construct(Integer.class);
        JavaType refinedType = referenceType.refine(Integer.class, TypeBindings.emptyBindings(), null, new JavaType[0]);
        assertEquals(Integer.class, refinedType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), refinedType.getTypeBindings());
        assertEquals(null, refinedType.getSuperClass());
        assertEquals(new JavaType[0], refinedType.getSuperInterfaces());
        assertEquals(String.class, refinedType.getContentType().getRawClass());
        assertEquals(referencedType, refinedType.getReferencedType());
        assertEquals(true, refinedType.isReferenceType());
        assertEquals(false, refinedType.hasContentTypeHandler());
        assertEquals(false, refinedType.hasValueHandler());
        assertEquals(false, refinedType.isAnchorType());
        assertEquals("class java.lang.Integer[reference type, class java.lang.String]", refinedType.toString());
        assertEquals(Id.CLASS, refinedType.getId());
    }

    @Test
    public void testReferenceTypeIsAnchorType() {
        JavaType intType = JavaType.construct(int.class);
        JavaType referencedType = JavaType.construct(String.class);
        ReferenceType referenceType = new ReferenceType(int.class, TypeBindings.emptyBindings(), null, new JavaType[0], referencedType, null, null, null, false);
        ReferenceType anchorType = new ReferenceType(int.class, TypeBindings.emptyBindings(), null, new JavaType[0], referencedType, referenceType, null, null, false);
        assertEquals(false, referenceType.isAnchorType());
        assertEquals(true, anchorType.isAnchorType());
    }

    @Test
    public void testReferenceTypeGetAnchorType() {
        JavaType intType = JavaType.construct(int.class);
        JavaType referencedType = JavaType.construct(String.class);
        ReferenceType referenceType = new ReferenceType(int.class, TypeBindings.emptyBindings(), null, new JavaType[0], referencedType, null, null, null, false);
        ReferenceType anchorType = new ReferenceType(int.class, TypeBindings.emptyBindings(), null, new JavaType[0], referencedType, referenceType, null, null, false);
        assertEquals(referenceType, anchorType.getAnchorType());
    }
}