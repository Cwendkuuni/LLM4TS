package com.google.javascript.rhino.jstype;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.TernaryValue;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.StaticScope;
import com.google.javascript.rhino.BooleanLiteralSet;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArrowTypeTest {
    @Test
    public void testIsSubtype() {
        ArrowType type1 = new ArrowType(new JSTypeRegistry(), null, null);
        ArrowType type2 = new ArrowType(new JSTypeRegistry(), null, null);
        assertTrue(type1.isSubtype(type2));
    }

    @Test
    public void testHasEqualParameters() {
        ArrowType type1 = new ArrowType(new JSTypeRegistry(), null, null);
        ArrowType type2 = new ArrowType(new JSTypeRegistry(), null, null);
        assertTrue(type1.hasEqualParameters(type2));
    }

    @Test
    public void testIsEquivalentTo() {
        ArrowType type1 = new ArrowType(new JSTypeRegistry(), null, null);
        ArrowType type2 = new ArrowType(new JSTypeRegistry(), null, null);
        assertTrue(type1.isEquivalentTo(type2));
    }

    @Test
    public void testHashCode() {
        ArrowType type = new ArrowType(new JSTypeRegistry(), null, null);
        int result = type.hashCode();
        assertNotNull(result);
    }

    @Test
    public void testGetLeastSupertype() {
        ArrowType type1 = new ArrowType(new JSTypeRegistry(), null, null);
        ArrowType type2 = new ArrowType(new JSTypeRegistry(), null, null);
        assertThrows(UnsupportedOperationException.class, () -> {
            type1.getLeastSupertype(type2);
        });
    }

    @Test
    public void testGetGreatestSubtype() {
        ArrowType type1 = new ArrowType(new JSTypeRegistry(), null, null);
        ArrowType type2 = new ArrowType(new JSTypeRegistry(), null, null);
        assertThrows(UnsupportedOperationException.class, () -> {
            type1.getGreatestSubtype(type2);
        });
    }

    @Test
    public void testTestForEquality() {
        ArrowType type1 = new ArrowType(new JSTypeRegistry(), null, null);
        ArrowType type2 = new ArrowType(new JSTypeRegistry(), null, null);
        assertThrows(UnsupportedOperationException.class, () -> {
            type1.testForEquality(type2);
        });
    }

    @Test
    public void testVisit() {
        ArrowType type = new ArrowType(new JSTypeRegistry(), null, null);
        assertThrows(UnsupportedOperationException.class, () -> {
            type.visit(null);
        });
    }

    @Test
    public void testGetPossibleToBooleanOutcomes() {
        ArrowType type = new ArrowType(new JSTypeRegistry(), null, null);
        assertNotNull(type.getPossibleToBooleanOutcomes());
    }

    @Test
    public void testResolveInternal() {
        ArrowType type = new ArrowType(new JSTypeRegistry(), null, null);
        ErrorReporter errorReporter = null;
        StaticScope<JSType> scope = null;
        assertThrows(UnsupportedOperationException.class, () -> {
            type.resolveInternal(errorReporter, scope);
        });
    }

    @Test
    public void testHasUnknownParamsOrReturn() {
        ArrowType type = new ArrowType(new JSTypeRegistry(), null, null);
        assertFalse(type.hasUnknownParamsOrReturn());
    }

    @Test
    public void testToStringHelper() {
        ArrowType type = new ArrowType(new JSTypeRegistry(), null, null);
        assertNotNull(type.toStringHelper(true));
    }

}