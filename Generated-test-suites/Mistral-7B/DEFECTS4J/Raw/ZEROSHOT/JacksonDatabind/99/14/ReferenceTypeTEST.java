package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFunction;
import com.fasterxml.jackson.databind.type.TypeModifier;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class ReferenceTypeTest {

    @Test
    public void testReferenceTypeCreation() {
        JavaType referencedType = TypeFunction.unchecked(String.class);
        JavaType refType = ReferenceType.construct(Integer.class, TypeBindings.emptyBindings(), null, null, referencedType);
        assertNotNull(refType);
        assertEquals(Integer.class, refType.getRawClass());
        assertEquals(referencedType, refType.getContentType());
        assertEquals(true, refType.isReferenceType());
        assertEquals(false, refType.hasStaticTyping());
        assertNull(refType.getAnchorType());
    }

    @Test
    public void testReferenceTypeUpgrade() {
        JavaType baseType = TypeFunction.unchecked(Integer.class);
        JavaType referencedType = TypeFunction.unchecked(String.class);
        JavaType refType = ReferenceType.upgradeFrom(baseType, referencedType);
        assertNotNull(refType);
        assertEquals(Integer.class, refType.getRawClass());
        assertEquals(referencedType, refType.getContentType());
        assertEquals(true, refType.isReferenceType());
        assertEquals(false, refType.hasStaticTyping());
        assertNull(refType.getAnchorType());
    }

    @Test
    public void testReferenceTypeWithContentType() {
        JavaType referencedType = TypeFunction.unchecked(String.class);
        JavaType refType = ReferenceType.construct(Integer.class, TypeBindings.emptyBindings(), null, null, referencedType);
        JavaType withContentType = refType.withContentType(TypeFunction.unchecked(Double.class));
        assertNotNull(withContentType);
        assertEquals(Integer.class, withContentType.getRawClass());
        assertEquals(Double.class, withContentType.getContentType());
        assertEquals(true, withContentType.isReferenceType());
        assertEquals(false, withContentType.hasStaticTyping());
        assertNull(withContentType.getAnchorType());
    }

    @Test
    public void testReferenceTypeWithAnchorType() {
        JavaType refType1 = ReferenceType.construct(String[].class, TypeBindings.emptyBindings(), null, null, TypeFunction.unchecked(Integer.class));
        JavaType refType2 = ReferenceType.construct(String[].class, TypeBindings.emptyBindings(), null, null, TypeFunction.unchecked(Long.class));
        JavaType anchorType = ReferenceType.construct(Object[].class, TypeBindings.emptyBindings(), null, null, null);
        JavaType refTypeWithAnchor = new ReferenceType(anchorType, refType1);
        assertEquals(anchorType, refTypeWithAnchor.getAnchorType());
        assertEquals(refType1, refTypeWithAnchor.getContentType());
    }

    @Test
    public void testReferenceTypeWithTypeHandler() {
        JavaType referencedType = TypeFunction.unchecked(String.class);
        JavaType refType = ReferenceType.construct(Integer.class, TypeBindings.emptyBindings(), null, null, referencedType);
        Object handler = new Object() {};
        JavaType withTypeHandler = refType.withTypeHandler(handler);
        assertNotNull(withTypeHandler);
        assertEquals(Integer.class, withTypeHandler.getRawClass());
        assertEquals(referencedType, withTypeHandler.getContentType());
        assertEquals(true, withTypeHandler.isReferenceType());
        assertEquals(false, withTypeHandler.hasStaticTyping());
        assertEquals(handler, withTypeHandler.getTypeHandler());
    }

    @Test
    public void testReferenceTypeWithValueHandler() {
        JavaType referencedType = TypeFunction.unchecked(String.class);
        JavaType refType = ReferenceType.construct(Integer.class, TypeBindings.emptyBindings(), null, null, referencedType);
        Object handler = new Object() {};
        JavaType withValueHandler = refType.withValueHandler(handler);
        assertNotNull(withValueHandler);
        assertEquals(Integer.class, withValueHandler.getRawClass());
        assertEquals(referencedType, withValueHandler.getContentType());
        assertEquals(true, withValueHandler.isReferenceType());
        assertEquals(false, withValueHandler.hasStaticTyping());
        assertEquals(handler, withValueHandler.getValueHandler());
    }

    @Test
    public void testReferenceTypeWithStaticTyping() {
        JavaType referencedType = TypeFunction.unchecked(String.class);
        JavaType refType = ReferenceType.construct(Integer.class, TypeBindings.emptyBindings(), null, null, referencedType);
        JavaType withStaticTyping = refType.withStaticTyping();
        assertNotNull(withStaticTyping);
        assertEquals(Integer.class, withStaticTyping.getRawClass());
        assertEquals(referencedType, withStaticTyping.getContentType());
        assertEquals(true, withStaticTyping.isReferenceType());
        assertEquals(true, withStaticTyping.hasStaticTyping());
        assertNull(withStaticTyping.getAnchorType());
    }

    @Test
    public void testReferenceTypeWithValueHandlerAndStaticTyping() {
        JavaType referencedType = TypeFunction.unchecked(String.class);
        JavaType refType = ReferenceType.construct(Integer.class, TypeBindings.emptyBindings(), null, null, referencedType);
        Object handler = new Object() {};
        JavaType withValueHandlerAndStaticTyping = refType.withValueHandler(handler).withStaticTyping();
        assertNotNull(withValueHandlerAndStaticTyping);
        assertEquals(Integer.class, withValueHandlerAndStaticTyping.getRawClass());
        assertEquals(referencedType, withValueHandlerAndStaticTyping.getContentType());
        assertEquals(true, withValueHandlerAndStaticTyping.isReferenceType());
        assertEquals(true, withValueHandlerAndStaticTyping.hasStaticTyping());
        assertEquals(handler, withValueHandlerAndStaticTyping.getValueHandler());
    }

    @Test
    public void testReferenceTypeEquals() {
        JavaType referencedType1 = TypeFunction.unchecked(String.class);
        JavaType refType1 = ReferenceType.construct(Integer.class, TypeBindings.emptyBindings(), null, null, referencedType1);
        JavaType refType2 = ReferenceType.construct(Integer.class, TypeBindings.emptyBindings(), null, null, referencedType1);
        JavaType refType3 = ReferenceType.construct(Long.class, TypeBindings.emptyBindings(), null, null, referencedType1);

        assertEquals(refType1, refType1);
        assertEquals(refType1, refType2);
        assertNotSame(refType1, refType3);
    }

    @Test
    public void testReferenceTypeGetCanonicalName() {
        JavaType referencedType = TypeFunction.unchecked(String.class);
        JavaType refType = ReferenceType.construct(Integer.class, TypeBindings.emptyBindings(), null, null, referencedType);
        String canonicalName = refType.getCanonicalName();
        assertEquals("[reference type, class java.lang.Integer<java.lang.String>]", canonicalName);
    }

    @Test
    public void testReferenceTypeIsAnnotatedWithJsonTypeId() {
        Class<MyClass> clazz = (Class<MyClass>) ClassUtil.loadClass(ReferenceTypeTest.class.getResource("/MyClass.java").getFile());
        JavaType javaType = TypeModifier.constructMapType(clazz, Map.class, new Class[]{Integer.class, String.class});
        AnnotatedClass annotatedClass = TypeUtil.constructAnnotated(clazz);
        AnnotatedMember member = annotatedClass.findMember("myList");
        assertNotNull(member);
        Id idAnnotation = member.getAnnotation(Id.class);
        As asAnnotation = member.getAnnotation(As.class);
        assertTrue(idAnnotation != null);
        assertEquals("MyClass.myList", idAnnotation.value());
        assertTrue(asAnnotation != null);
        assertEquals(javaType, asAnnotation.value());
    }
}