package com.fasterxml.jackson.databind.type;

import org.junit.Assert;
import org.junit.Test;


public class ReferenceTypeTest {

    @Test
    public void testConstructor() {
        try {
            ReferenceType referenceType = new ReferenceType(null, null, null, null, null, null, null, null, false);
            Assert.fail("Should throw IllegalArgumentException for null parameters");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new JavaType();
        JavaType refdType = new JavaType();
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refdType);
        Assert.assertNotNull("ReferenceType should not be null", referenceType);
        Assert.assertEquals("Referenced type should match", refdType, referenceType.getReferencedType());
    }

    @Test
    public void testConstruct() {
        try {
            ReferenceType referenceType = ReferenceType.construct(null, null, null, null, null);
            Assert.fail("Should throw IllegalArgumentException for null parameters");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = new JavaType();
        ReferenceType referenceType = new ReferenceType();
        ReferenceType newType = referenceType.withContentType(contentType);
        Assert.assertNotNull("New ReferenceType should not be null", newType);
        Assert.assertEquals("Content type should match", contentType, newType.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        Object handler = new Object();
        ReferenceType referenceType = new ReferenceType();
        ReferenceType newType = referenceType.withTypeHandler(handler);
        Assert.assertNotNull("New ReferenceType should not be null", newType);
        Assert.assertEquals("Type handler should match", handler, newType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Object handler = new Object();
        ReferenceType referenceType = new ReferenceType();
        ReferenceType newType = referenceType.withContentTypeHandler(handler);
        Assert.assertNotNull("New ReferenceType should not be null", newType);
        Assert.assertEquals("Content type handler should match", handler, newType.getContentTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Object handler = new Object();
        ReferenceType referenceType = new ReferenceType();
        ReferenceType newType = referenceType.withValueHandler(handler);
        Assert.assertNotNull("New ReferenceType should not be null", newType);
        Assert.assertEquals("Value handler should match", handler, newType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Object handler = new Object();
        ReferenceType referenceType = new ReferenceType();
        ReferenceType newType = referenceType.withContentValueHandler(handler);
        Assert.assertNotNull("New ReferenceType should not be null", newType);
        Assert.assertEquals("Content value handler should match", handler, newType.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        ReferenceType referenceType = new ReferenceType();
        ReferenceType newType = referenceType.withStaticTyping();
        Assert.assertNotNull("New ReferenceType should not be null", newType);
        Assert.assertTrue("Static typing should be true", newType.hasStaticTyping());
    }

    @Test
    public void testRefine() {
        Class<?> rawType = TestClass.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInterfaces = new JavaType[]{new JavaType()};
        ReferenceType referenceType = new ReferenceType();
        JavaType newType = referenceType.refine(rawType, bindings, superClass, superInterfaces);
        Assert.assertNotNull("New JavaType should not be null", newType);
        Assert.assertEquals("Raw type should match", rawType, newType.getRawType());
        Assert.assertEquals("Type bindings should match", bindings, newType.getBindings());
        Assert.assertEquals("Super class should match", superClass, newType.getSuperClass());
        Assert.assertArrayEquals("Super interfaces should match", superInterfaces, newType.getInterfaces());
    }

    @Test
    public void testGetAnchorType() {
        ReferenceType referenceType = new ReferenceType();
        JavaType anchorType = new JavaType();
        referenceType.setAnchorType(anchorType);
        Assert.assertEquals("Anchor type should match", anchorType, referenceType.getAnchorType());
    }

    @Test
    public void testIsAnchorType() {
        ReferenceType referenceType = new ReferenceType();
        Assert.assertTrue("Should be anchor type itself", referenceType.isAnchorType());
        JavaType anchorType = new JavaType();
        referenceType.setAnchorType(anchorType);
        Assert.assertFalse("Should not be anchor type", referenceType.isAnchorType());
    }

    @Test
    public void testToString() {
        ReferenceType referenceType = new ReferenceType();
        referenceType.setReferencedType(new JavaType());
        String expected = "[reference type, class com.fasterxml.jackson.databind.type.JavaType<com.fasterxml.jackson.databind.type.JavaType>]";
        Assert.assertEquals("ToString result should match", expected, referenceType.toString());
    }

    @Test
    public void testEqualsSameObject() {
        ReferenceType referenceType = new ReferenceType();
        Assert.assertTrue("Should equal itself", referenceType.equals(referenceType));
    }

    @Test
    public void testEqualsNullObject() {
        ReferenceType referenceType = new ReferenceType();
        Assert.assertFalse("Should not equal null", referenceType.equals(null));
    }

    @Test
    public void testEqualsDifferentClass() {
        ReferenceType referenceType = new ReferenceType();
        Assert.assertFalse("Should not equal object of different class", referenceType.equals(new Object()));
    }

    @Test
    public void testEqualsDifferentClassType() {
        ReferenceType referenceType = new ReferenceType();
        JavaType javaType = new JavaType();
        Assert.assertFalse("Should not equal ReferenceType with different JavaType", referenceType.equals(javaType));
    }

    @Test
    public void testEqualsDifferentReferencedType() {
        ReferenceType referenceType1 = new ReferenceType();
        JavaType javaType1 = new JavaType();
        referenceType1.setReferencedType(javaType1);

        ReferenceType referenceType2 = new ReferenceType();
        JavaType javaType2 = new JavaType();
        referenceType2.setReferencedType(javaType2);

        Assert.assertFalse("Should not equal ReferenceType with different ReferencedType", referenceType1.equals(referenceType2));
    }

    @Test
    public void testEqualsSameFields() {
        ReferenceType referenceType1 = new ReferenceType();
        JavaType javaType = new JavaType();
        referenceType1.setReferencedType(javaType);

        ReferenceType referenceType2 = new ReferenceType();
        JavaType javaTypeCopy = new JavaType();
        referenceType2.setReferencedType(javaTypeCopy);

        Assert.assertTrue("Should equal ReferenceType with same fields", referenceType1.equals(referenceType2));
    }

    private static class TestClass {
    }

}