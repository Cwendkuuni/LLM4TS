package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ReferenceType;

public class ReferenceTypeTest {

    @Test
    public void testReferenceTypeConstruction() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        Object valueHandler = new Object() {};
        Object typeHandler = new Object() {};

        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);

        assertEquals(cls, referenceType.getClass());
        assertEquals(refType, referenceType.getReferencedType());
    }

    @Test
    public void testReferenceTypeWithTypeHandler() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        Object valueHandler = new Object() {};
        Object typeHandler = new Object() {};

        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler);

        ReferenceType withTypeHandler = referenceType.withTypeHandler(new Object() {});

        assertNotSame(referenceType, withTypeHandler);
        assertEquals(refType, withTypeHandler.getReferencedType());
    }

    @Test
    public void testReferenceTypeWithContentTypeHandler() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        Object valueHandler = new Object() {};
        Object typeHandler = new Object() {};

        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler);

        ReferenceType withContentTypeHandler = referenceType.withContentTypeHandler(new Object() {});

        assertNotSame(referenceType, withContentTypeHandler);
        assertEquals(refType, withContentTypeHandler.getReferencedType());
    }

    @Test
    public void testReferenceTypeWithValueHandler() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        Object valueHandler = new Object() {};
        Object typeHandler = new Object() {};

        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler);

        ReferenceType withValueHandler = referenceType.withValueHandler(new Object() {});

        assertNotSame(referenceType, withValueHandler);
        assertEquals(refType, withValueHandler.getReferencedType());
    }

    @Test
    public void testReferenceTypeWithStaticTyping() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        Object valueHandler = new Object() {};
        Object typeHandler = new Object() {};

        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler);

        ReferenceType withStaticTyping = referenceType.withStaticTyping();

        assertNotSame(referenceType, withStaticTyping);
        assertTrue(withStaticTyping.isStatic());
        assertEquals(refType, withStaticTyping.getReferencedType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReferenceTypeWithInvalidTypeHandler() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        Object valueHandler = new Object() {};
        Object typeHandler = new Object() {};

        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler);

        referenceType.withTypeHandler(new String() {});
    }

    // Add more tests for edge cases and error scenarios here
}