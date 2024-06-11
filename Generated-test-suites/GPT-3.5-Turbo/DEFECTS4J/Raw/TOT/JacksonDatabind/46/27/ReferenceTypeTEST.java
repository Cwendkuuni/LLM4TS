
package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Assert;
import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        Class<?> cls = Object.class;
        JavaType refType = new SimpleType(Object.class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();

        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);

        Assert.assertEquals(cls, referenceType.getClass());
        Assert.assertEquals(refType.hashCode(), referenceType.hashCode());
        Assert.assertEquals(valueHandler, referenceType.getValueHandler());
        Assert.assertEquals(typeHandler, referenceType.getTypeHandler());
        Assert.assertFalse(referenceType.isStatic());
        Assert.assertEquals(refType, referenceType.getReferencedType());
        Assert.assertTrue(referenceType.isReferenceType());
        Assert.assertEquals(1, referenceType.containedTypeCount());
        Assert.assertEquals(refType, referenceType.containedType(0));
        Assert.assertEquals("T", referenceType.containedTypeName(0));
        Assert.assertEquals(cls, referenceType.getParameterSource());

        StringBuilder sb = new StringBuilder();
        sb.append(cls.getName());
        sb.append('<');
        sb.append(refType.toCanonical());
        String expectedCanonicalName = sb.toString();
        Assert.assertEquals(expectedCanonicalName, referenceType.buildCanonicalName());

        StringBuilder expectedCanonicalSignature = new StringBuilder(40)
                .append("[reference type, class ")
                .append(expectedCanonicalName)
                .append('<')
                .append(refType)
                .append('>')
                .append(']');
        Assert.assertEquals(expectedCanonicalSignature.toString(), referenceType.toString());

        Assert.assertTrue(referenceType.equals(referenceType));
        Assert.assertFalse(referenceType.equals(null));
        Assert.assertFalse(referenceType.equals(new Object()));

        ReferenceType referenceType2 = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        Assert.assertTrue(referenceType.equals(referenceType2));
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> cls = Object.class;
        JavaType refType = new SimpleType(Object.class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        Object newTypeHandler = new Object();

        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);

        ReferenceType updatedType = referenceType.withTypeHandler(newTypeHandler);

        Assert.assertEquals(cls, updatedType.getClass());
        Assert.assertEquals(refType.hashCode(), updatedType.hashCode());
        Assert.assertEquals(valueHandler, updatedType.getValueHandler());
        Assert.assertEquals(newTypeHandler, updatedType.getTypeHandler());
        Assert.assertFalse(updatedType.isStatic());
        Assert.assertEquals(refType, updatedType.getReferencedType());
        Assert.assertTrue(updatedType.isReferenceType());
        Assert.assertEquals(1, updatedType.containedTypeCount());
        Assert.assertEquals(refType, updatedType.containedType(0));
        Assert.assertEquals("T", updatedType.containedTypeName(0));
        Assert.assertEquals(cls, updatedType.getParameterSource());

        StringBuilder sb = new StringBuilder();
        sb.append(cls.getName());
        sb.append('<');
        sb.append(refType.toCanonical());
        String expectedCanonicalName = sb.toString();
        Assert.assertEquals(expectedCanonicalName, updatedType.buildCanonicalName());

        StringBuilder expectedCanonicalSignature = new StringBuilder(40)
                .append("[reference type, class ")
                .append(expectedCanonicalName)
                .append('<')
                .append(refType)
                .append('>')
                .append(']');
        Assert.assertEquals(expectedCanonicalSignature.toString(), updatedType.toString());

        Assert.assertTrue(updatedType.equals(updatedType));
        Assert.assertFalse(updatedType.equals(null));
        Assert.assertFalse(updatedType.equals(new Object()));

        ReferenceType referenceType2 = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        Assert.assertFalse(updatedType.equals(referenceType2));
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<?> cls = Object.class;
        JavaType refType = new SimpleType(Object.class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        Object newContentTypeHandler = new Object();

        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);

        ReferenceType updatedType = referenceType.withContentTypeHandler(newContentTypeHandler);

        Assert.assertEquals(cls, updatedType.getClass());
        Assert.assertEquals(refType.hashCode(), updatedType.hashCode());
        Assert.assertEquals(valueHandler, updatedType.getValueHandler());
        Assert.assertEquals(typeHandler, updatedType.getTypeHandler());
        Assert.assertFalse(updatedType.isStatic());
        Assert.assertEquals(refType, updatedType.getReferencedType());
        Assert.assertTrue(updatedType.isReferenceType());
        Assert.assertEquals(1, updatedType.containedTypeCount());
        Assert.assertEquals(refType, updatedType.containedType(0));
        Assert.assertEquals("T", updatedType.containedTypeName(0));
        Assert.assertEquals(cls, updatedType.getParameterSource());

        StringBuilder sb = new StringBuilder();
        sb.append(cls.getName());
        sb.append('<');
        sb.append(refType.toCanonical());
        String expectedCanonicalName = sb.toString();
        Assert.assertEquals(expectedCanonicalName, updatedType.buildCanonicalName());

        StringBuilder expectedCanonicalSignature = new StringBuilder(40)
                .append("[reference type, class ")
                .append(expectedCanonicalName)
                .append('<')
                .append(refType)
                .append('>')
                .append(']');
        Assert.assertEquals(expectedCanonicalSignature.toString(), updatedType.toString());

        Assert.assertTrue(updatedType.equals(updatedType));
        Assert.assertFalse(updatedType.equals(null));
        Assert.assertFalse(updatedType.equals(new Object()));

        ReferenceType referenceType2 = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        Assert.assertFalse(updatedType.equals(referenceType2));
    }

    @Test
    public void testWithValueHandler() {
        Class<?> cls = Object.class;
        JavaType refType = new SimpleType(Object.class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        Object newValueHandler = new Object();

        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);

        ReferenceType updatedType = referenceType.withValueHandler(newValueHandler);

        Assert.assertEquals(cls, updatedType.getClass());
        Assert.assertEquals(refType.hashCode(), updatedType.hashCode());
        Assert.assertEquals(newValueHandler, updatedType.getValueHandler());
        Assert.assertEquals(typeHandler, updatedType.getTypeHandler());
        Assert.assertFalse(updatedType.isStatic());
        Assert.assertEquals(refType, updatedType.getReferencedType());
        Assert.assertTrue(updatedType.isReferenceType());
        Assert.assertEquals(1, updatedType.containedTypeCount());
        Assert.assertEquals(refType, updatedType.containedType(0));
        Assert.assertEquals("T", updatedType.containedTypeName(0));
        Assert.assertEquals(cls, updatedType.getParameterSource());

        StringBuilder sb = new StringBuilder();
        sb.append(cls.getName());
        sb.append('<');
        sb.append(refType.toCanonical());
        String expectedCanonicalName = sb.toString();
        Assert.assertEquals(expectedCanonicalName, updatedType.buildCanonicalName());

        StringBuilder expectedCanonicalSignature = new StringBuilder(40)
                .append("[reference type, class ")
                .append(expectedCanonicalName)
                .append('<')
                .append(refType)
                .append('>')
                .append(']');
        Assert.assertEquals(expectedCanonicalSignature.toString(), updatedType.toString());

        Assert.assertTrue(updatedType.equals(updatedType));
        Assert.assertFalse(updatedType.equals(null));
        Assert.assertFalse(updatedType.equals(new Object()));

        ReferenceType referenceType2 = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        Assert.assertFalse(updatedType.equals(referenceType2));
    }

    @Test
    public void testWithContentValueHandler() {
        Class<?> cls = Object.class;
        JavaType refType = new SimpleType(Object.class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        Object newContentValueHandler = new Object();

        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);

        ReferenceType updatedType = referenceType.withContentValueHandler(newContentValueHandler);

        Assert.assertEquals(cls, updatedType.getClass());
        Assert.assertEquals(refType.hashCode(), updatedType.hashCode());
        Assert.assertEquals(valueHandler, updatedType.getValueHandler());
        Assert.assertEquals(typeHandler, updatedType.getTypeHandler());
        Assert.assertFalse(updatedType.isStatic());
        Assert.assertEquals(refType, updatedType.getReferencedType());
        Assert.assertTrue(updatedType.isReferenceType());
        Assert.assertEquals(1, updatedType.containedTypeCount());
        Assert.assertEquals(refType, updatedType.containedType(0));
        Assert.assertEquals("T", updatedType.containedTypeName(0));
        Assert.assertEquals(cls, updatedType.getParameterSource());

        StringBuilder sb = new StringBuilder();
        sb.append(cls.getName());
        sb.append('<');
        sb.append(refType.toCanonical());
        String expectedCanonicalName = sb.toString();
        Assert.assertEquals(expectedCanonicalName, updatedType.buildCanonicalName());

        StringBuilder expectedCanonicalSignature = new StringBuilder(40)
                .append("[reference type, class ")
                .append(expectedCanonicalName)
                .append('<')
                .append(refType)
                .append('>')
                .append(']');
        Assert.assertEquals(expectedCanonicalSignature.toString(), updatedType.toString());

        Assert.assertTrue(updatedType.equals(updatedType));
        Assert.assertFalse(updatedType.equals(null));
        Assert.assertFalse(updatedType.equals(new Object()));

        ReferenceType referenceType2 = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        Assert.assertFalse(updatedType.equals(referenceType2));
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> cls = Object.class;
        JavaType refType = new SimpleType(Object.class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();

        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);

        ReferenceType updatedType = referenceType.withStaticTyping();

        Assert.assertEquals(cls, updatedType.getClass());
        Assert.assertEquals(refType.hashCode(), updatedType.hashCode());
        Assert.assertEquals(valueHandler, updatedType.getValueHandler());
        Assert.assertEquals(typeHandler, updatedType.getTypeHandler());
        Assert.assertTrue(updatedType.isStatic());
        Assert.assertEquals(refType, updatedType.getReferencedType());
        Assert.assertTrue(updatedType.isReferenceType());
        Assert.assertEquals(1, updatedType.containedTypeCount());
        Assert.assertEquals(refType, updatedType.containedType(0));
        Assert.assertEquals("T", updatedType.containedTypeName(0));
        Assert.assertEquals(cls, updatedType.getParameterSource());

        StringBuilder sb = new StringBuilder();
        sb.append(cls.getName());
        sb.append('<');
        sb.append(refType.toCanonical());
        String expectedCanonicalName = sb.toString();
        Assert.assertEquals(expectedCanonicalName, updatedType.buildCanonicalName());

        StringBuilder expectedCanonicalSignature = new StringBuilder(40)
                .append("[reference type, class ")
                .append(expectedCanonicalName)
                .append('<')
                .append(refType)
                .append('>')
                .append(']');
        Assert.assertEquals(expectedCanonicalSignature.toString(), updatedType.toString());

        Assert.assertTrue(updatedType.equals(updatedType));
        Assert.assertFalse(updatedType.equals(null));
        Assert.assertFalse(updatedType.equals(new Object()));

        ReferenceType referenceType2 = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        Assert.assertTrue(updatedType.equals(referenceType2));
    }

    @Test
    public void testNarrow() {
        Class<?> cls = Object.class;
        JavaType refType = new SimpleType(Object.class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        Class<?> newClass = String.class;

        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);

        ReferenceType updatedType = (ReferenceType) referenceType._narrow(newClass);

        Assert.assertEquals(newClass, updatedType.getClass());
        Assert.assertEquals(refType.hashCode(), updatedType.hashCode());
        Assert.assertEquals(valueHandler, updatedType.getValueHandler());
        Assert.assertEquals(typeHandler, updatedType.getTypeHandler());
        Assert.assertTrue(updatedType.isStatic());
        Assert.assertEquals(refType, updatedType.getReferencedType());
        Assert.assertTrue(updatedType.isReferenceType());
        Assert.assertEquals(1, updatedType.containedTypeCount());
        Assert.assertEquals(refType, updatedType.containedType(0));
        Assert.assertEquals("T", updatedType.containedTypeName(0));
        Assert.assertEquals(newClass, updatedType.getParameterSource());

        StringBuilder sb = new StringBuilder();
        sb.append(newClass.getName());
        sb.append('<');
        sb.append(refType.toCanonical());
        String expectedCanonicalName = sb.toString();
        Assert.assertEquals(expectedCanonicalName, updatedType.buildCanonicalName());

        StringBuilder expectedCanonicalSignature = new StringBuilder(40)
                .append("[reference type, class ")
                .append(expectedCanonicalName)
                .append('<')
                .append(refType)
                .append('>')
                .append(']');
        Assert.assertEquals(expectedCanonicalSignature.toString(), updatedType.toString());

        Assert.assertTrue(updatedType.equals(updatedType));
        Assert.assertFalse(updatedType.equals(null));
        Assert.assertFalse(updatedType.equals(new Object()));

        ReferenceType referenceType2 = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        Assert.assertFalse(updatedType.equals(referenceType2));
    }

    // Additional test cases, edge cases, and error scenarios can be added here

}

