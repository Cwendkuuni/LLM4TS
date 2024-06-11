package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;
import org.junit.Test;

import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        Class<ResolvedRecursiveType> type = ResolvedRecursiveType.class;
        ResolvedRecursiveType instance1 = new ResolvedRecursiveType(type, null);
        JavaType refType = new TypeBase(String.class, null, null, 0, null, null, null, false);

        instance1.setReference(refType);
        ResolvedRecursiveType instance2 = new ResolvedRecursiveType(type, null);

        instance2.setReference(instance1);

        JavaType refTypeFromInstance1 = instance1.getSelfReferencedType();
        JavaType refTypeFromInstance2 = instance2.getSelfReferencedType();

        assertEquals(refType, refTypeFromInstance1);
        assertEquals(instance1, refTypeFromInstance2.getSelfReferencedType());
    }

    @Test
    public void testGetSelfReferencedType() {
        Class<ResolvedRecursiveType> type = ResolvedRecursiveType.class;
        ResolvedRecursiveType instance = new ResolvedRecursiveType(type, null);

        JavaType refType = new TypeBase(String.class, null, null, 0, null, null, null, false);
        instance.setReference(refType);

        JavaType selfReferencedType = instance.getSelfReferencedType();
        assertEquals(refType, selfReferencedType);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        Class<ResolvedRecursiveType> type = ResolvedRecursiveType.class;
        ResolvedRecursiveType instance = new ResolvedRecursiveType(type, null);

        JavaType refType = new TypeBase(String.class, null, null, 0, null, null, null, false);
        instance.setReference(refType);

        try {
            instance.setReference(refType);
        } catch (IllegalStateException e) {
            assertEquals("Trying to re-set self reference; old value = " + refType + ", new = " + refType, e.getMessage());
            throw e;
        }
    }
}