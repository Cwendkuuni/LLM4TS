package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);

        JavaType ref = new ClassType(String.class); // any valid JavaType
        type.setReference(ref);
        JavaType selfType = type.getSelfReferencedType();

        assertNotNull(selfType);
        assertNull(type._referencedType); // sanity check
        assertNotNull(selfType.getRawClass()); // type should not be null
        assertNotNull(selfType.getErasedSignature(new StringBuilder()).toString());
        assertNotNull(selfType.getGenericSignature(new StringBuilder()).toString());
    }

    @Test
    public void testSetReferenceTwice() {
        Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);

        JavaType ref1 = new ClassType(String.class);
        JavaType ref2 = new ClassType(Integer.class);

        type.setReference(ref1);
        try {
            type.setReference(ref2);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // expected
        }
    }
}