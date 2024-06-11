
package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ReferenceTypeTest {
    private ReferenceType referenceType;

    @Before
    public void setup() {
        JavaType javaType = new SimpleType(Integer.class);
        referenceType = ReferenceType.construct(String.class, javaType, null, null);
    }
    
    @Test
    public void testWithStaticTyping() {
        assertTrue(referenceType.withStaticTyping().isStaticTyping());
    }

    @Test
    public void testWithContentValueHandler() {
        Object handler = new Object();
        referenceType = referenceType.withContentValueHandler(handler);
        assertEquals(handler, referenceType.getReferencedType().getValueHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Object handler = new Object();
        referenceType = referenceType.withContentTypeHandler(handler);
        assertEquals(handler, referenceType.getReferencedType().getTypeHandler());
    }

    @Test
    public void testWithReferencedType() {
        JavaType newReferencedType = new SimpleType(Long.class);
        referenceType = referenceType.withReferencedType(newReferencedType);
        assertEquals(newReferencedType, referenceType.getReferencedType());
    }

    @Test
    public void testWithStaticTypingTwice() {
        referenceType = referenceType.withStaticTyping().withStaticTyping();
        assertTrue(referenceType.isStaticTyping());
    }

    @Test
    public void testToString() {
        String expected = "[reference type, class java.lang.String<java.lang.Integer>]";
        assertEquals(expected, referenceType.toString());
    }

    @Test
    public void testEqualsWithSameObject() {
        assertTrue(referenceType.equals(referenceType));
    }

    @Test
    public void testEqualsWithDifferentClass() {
        assertFalse(referenceType.equals(null));
        assertFalse(referenceType.equals(new Object()));
    }

    @Test
    public void testEqualsWithDifferentClasses() {
        JavaType javaType = new SimpleType(Double.class);
        ReferenceType other = ReferenceType.construct(Integer.class, javaType, null, null);
        assertFalse(referenceType.equals(other));
    }

    @Test
    public void testEqualsWithDifferentReferencedTypes() {
        ReferenceType other = ReferenceType.construct(String.class, new SimpleType(Double.class), null, null);
        assertFalse(referenceType.equals(other));
    }

    @Test
    public void testEquals() {
        JavaType javaType = new SimpleType(Integer.class);
        ReferenceType other = ReferenceType.construct(String.class, javaType, null, null);
        assertTrue(referenceType.equals(other));
    }
}
