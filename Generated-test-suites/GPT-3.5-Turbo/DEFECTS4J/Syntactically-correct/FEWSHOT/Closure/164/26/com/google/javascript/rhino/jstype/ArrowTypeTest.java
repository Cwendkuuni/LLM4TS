package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType.NativeType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.JSTypeTester;

public class ArrowTypeTest {
    private JSTypeRegistry registry = new JSTypeRegistry();

    @Test
    public void testIsSubtype() {
        ArrowType arrowType1 = createArrowType(registry.createVoidType(), false);
        ArrowType arrowType2 = createArrowType(registry.createVoidType(), false);
        assertTrue(arrowType1.isSubtype(arrowType2));
    }

    @Test
    public void testIsNotSubtype() {
        ArrowType arrowType1 = createArrowType(registry.createVoidType(), false);
        ArrowType arrowType2 = createArrowType(registry.createNumberType(), false);
        assertFalse(arrowType1.isSubtype(arrowType2));
    }

    @Test
    public void testIsEquivalentTo() {
        ArrowType arrowType1 = createArrowType(registry.createVoidType(), false);
        ArrowType arrowType2 = createArrowType(registry.createVoidType(), false);
        assertTrue(arrowType1.isEquivalentTo(arrowType2));
    }

    @Test
    public void testIsNotEquivalentTo() {
        ArrowType arrowType1 = createArrowType(registry.createVoidType(), false);
        ArrowType arrowType2 = createArrowType(registry.createNumberType(), false);
        assertFalse(arrowType1.isEquivalentTo(arrowType2));
    }

    @Test
    public void testHashCode() {
        ArrowType arrowType1 = createArrowType(registry.createVoidType(), false);
        ArrowType arrowType2 = createArrowType(registry.createVoidType(), false);
        assertEquals(arrowType1.hashCode(), arrowType2.hashCode());
    }

    @Test
    public void testHashCodeDifferentReturnType() {
        ArrowType arrowType1 = createArrowType(registry.createVoidType(), false);
        ArrowType arrowType2 = createArrowType(registry.createNumberType(), false);
        assertNotEquals(arrowType1.hashCode(), arrowType2.hashCode());
    }

    @Test
    public void testResolveInternal() {
        ArrowType arrowType = createArrowType(registry.createTypeFromTokens(NativeType.NULL));
        assertNull(arrowType.resolveInternal(new ErrorReporter(), null));
    }

    private ArrowType createArrowType(JSType returnType) {
        return new ArrowType(registry, null, returnType);
    }

    private ArrowType createArrowType(JSType returnType, boolean returnTypeInferred) {
        return new ArrowType(registry, null, returnType, returnTypeInferred);
    }
}