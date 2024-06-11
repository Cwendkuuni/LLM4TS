package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.TernaryValue;
import com.google.javascript.rhino.Visitor;
import com.google.javascript.rhino.BooleanLiteralSet;

import org.junit.Test;

public class ArrowTypeTest {
    private static final ErrorReporter t = new ErrorReporter();
    private static final JSTypeRegistry registry = new JSTypeRegistry();

    @Test
    public void testIsSubtype() {
        JSType type1 = new ArrowType(registry, null, null);
        JSType type2 = new ArrowType(registry, null, null);
        assertTrue(type1.isSubtype(type1));
        assertTrue(type1.isSubtype(type2));
        assertTrue(type2.isSubtype(type1));
        assertTrue(type2.isSubtype(type2));

        JSType type3 = new ArrowType(registry, null, new IntType(registry));
        assertFalse(type1.isSubtype(type3));

        JSType type4 = new ArrowType(registry, null, new VoidType(registry));
        JSType type5 = new ArrowType(registry, null, new IntType(registry));
        assertFalse(type4.isSubtype(type5));

        JSType type6 = new ArrowType(registry, null, new IntType(registry), true);
        JSType type7 = new ArrowType(registry, null, new VoidType(registry), true);
        assertTrue(type6.isSubtype(type7));

        JSType type8 = new ArrowType(registry, null, new IntType(registry), false);
        JSType type9 = new ArrowType(registry, null, new VoidType(registry), true);
        assertFalse(type8.isSubtype(type9));

        JSType type10 = new ArrowType(registry, null, new IntType(registry));
        JSType type11 = new ArrowType(registry, new Node(JSTypeNative.VOID), new IntType(registry));
        assertFalse(type10.isSubtype(type11));

        JSType type12 = new ArrowType(registry, null, null, true);
        JSType type13 = new ArrowType(registry, null, null, false);
        assertFalse(type12.isSubtype(type13));

        JSType type14 = new ArrowType(registry, new Node(JSTypeNative.INT), null);
        JSType type15 = new ArrowType(registry, new Node(JSTypeNative.INT), new IntType(registry));
        assertTrue(type14.isSubtype(type15));

        JSType type16 = new ArrowType(registry, new Node(JSTypeNative.INT), null, true);
        JSType type17 = new ArrowType(registry, new Node(JSTypeNative.INT), new IntType(registry), false);
        assertFalse(type16.isSubtype(type17));

        JSType type18 = new ArrowType(registry, new Node(JSTypeNative.INT), null);
        JSType type19 = new ArrowType(registry, new Node(JSTypeNative.VOID), new IntType(registry));
        assertFalse(type18.isSubtype(type19));

        JSType type20 = new ArrowType(registry, new Node(JSTypeNative.VOID), new IntType(registry));
        JSType type21 = new ArrowType(registry, new Node(JSTypeNative.VOID), null);
        assertFalse(type20.isSubtype(type21));

        JSType type22 = new ArrowType(registry, new Node(JSTypeNative.INT), new IntType(registry), true);
        JSType type23 = new ArrowType(registry, new Node(JSTypeNative.INT), new IntType(registry), false);
        assertTrue(type22.isSubtype(type23));

        JSType type24 = new ArrowType(registry, null, new IntType(registry), false);
        JSType type25 = new ArrowType(registry, new Node(JSTypeNative.INT), new IntType(registry), true);
        assertTrue(type24.isSubtype(type25));

        JSType type26 = new ArrowType(registry, new Node(JSTypeNative.INT), new IntType(registry), true);
        JSType type27 = new ArrowType(registry, new Node(JSTypeNative.INT), new IntType(registry), false);
        assertTrue(type26.isSubtype(type27));

        JSType type28 = new ArrowType(registry, new Node(JSTypeNative.INT), new IntType(registry), true);
        JSType type29 = new ArrowType(registry, new Node(JSTypeNative.INT), new ObjectType(registry, "String"), false);
        assertFalse(type28.isSubtype(type29));

        JSType type30 = new ArrowType(registry, new Node(JSTypeNative.INT), new ObjectType(registry, "String"), true);
        JSType type31 = new ArrowType(registry, new Node(JSTypeNative.INT), new IntType(registry), false);
        assertTrue(type30.isSubtype(type31));

        JSType type32 = new ArrowType(registry, new Node(JSTypeNative.INT), null, true);
        JSType type33 = new ArrowType(registry, new Node(JSTypeNative.INT), null, false);
        assertTrue(type32.isSubtype(type33));

        JSType type34 = new ArrowType(registry, new Node(JSTypeNative.INT), new Node(JSTypeNative.INT), true);
        JSType type35 = new ArrowType(registry, new Node(JSTypeNative.INT), new Node(JSTypeNative.INT), false);
        assertTrue(type34.isSubtype(type35));

        JSType type36 = new ArrowType(registry, new Node(JSTypeNative.INT), new Node(JSTypeNative.INT), true);
        JSType type37 = new ArrowType(registry, new Node(JSTypeNative.INT), new Node(JSTypeNative.INT), false);
        assertTrue(type36.isSubtype(type37));

        JSType type38 = new ArrowType(registry, new Node(JSTypeNative.INT), new Node(JSTypeNative.INT), false);
        JSType type39 = new ArrowType(registry, new Node(JSTypeNative.INT), new Node(JSTypeNative.INT), true);
        assertFalse(type38.isSubtype(type39));

        JSType type40 = new ArrowType(registry, new Node(JSTypeNative.INT), new Node(JSTypeNative.INT), false);
        JSType type41 = new ArrowType(registry, new Node(JSTypeNative.INT), new ObjectType(registry, "String"), true);
        assertFalse(type40.isSubtype(type41));

        JSType type42 = new ArrowType(registry, new Node(JSTypeNative.INT), new ObjectType(registry, "String"), true);
        JSType type43 = new ArrowType(registry, new Node(JSTypeNative.INT), new Node(JSTypeNative.INT), false);
        assertFalse(type42.isSubtype(type43));

        JSType type44 = new ArrowType(registry, new Node(JSTypeNative.INT), new Node(JSTypeNative.INT), true);
        JSType type45 = new ArrowType(registry, new Node(JSTypeNative.INT), new Node(JSTypeNative.INT), false);
        assertTrue(type44.isSubtype(type45));

        JSType type46 = new ArrowType(registry, new Node(JSTypeNative.INT), new Node(JSTypeNative.INT), false);
        JSType type47 = new ArrowType(registry, new Node(JSTypeNative.INT), new Node(JSTypeNative.INT), true);
        assertFalse(type46.isSubtype(type47));
    }

    @Test
    public void testHasEqualParameters() {
        ArrowType arrowType1 = new ArrowType(registry, null, new IntType(registry));
        ArrowType arrowType2 = new ArrowType(registry, null, new IntType(registry));
        ArrowType arrowType3 = new ArrowType(registry, new Node(JSTypeNative.INT), new IntType(registry));
        ArrowType arrowType4 = new ArrowType(registry, new Node(JSTypeNative.INT), new ObjectType(registry, "String"));

        assertTrue(arrowType1.hasEqualParameters(arrowType1));
        assertTrue(arrowType1.hasEqualParameters(arrowType2));
        assertTrue(arrowType2.hasEqualParameters(arrowType1));
        assertTrue(arrowType2.hasEqualParameters(arrowType2));

        assertFalse(arrowType1.hasEqualParameters(arrowType3));
        assertFalse(arrowType1.hasEqualParameters(arrowType4));
        assertFalse(arrowType2.hasEqualParameters(arrowType3));
        assertFalse(arrowType2.hasEqualParameters(arrowType4));
        assertFalse(arrowType3.hasEqualParameters(arrowType4));

        ArrowType arrowType5 = new ArrowType(registry, null, new IntType(registry), true);
        ArrowType arrowType6 = new ArrowType(registry, null, new ObjectType(registry, "String"), true);
        assertFalse(arrowType5.hasEqualParameters(arrowType6));

        ArrowType arrowType7 = new ArrowType(registry, new Node(JSTypeNative.INT), null, true);
        ArrowType arrowType8 = new ArrowType(registry, null, new IntType(registry), true);
        assertTrue(arrowType7.hasEqualParameters(arrowType8));

        ArrowType arrowType9 = new ArrowType(registry, new Node(JSTypeNative.INT), null, false);
        ArrowType arrowType10 = new ArrowType(registry, null, new IntType(registry), false);
        assertTrue(arrowType9.hasEqualParameters(arrowType10));
    }

    @Test
    public void testToStringHelper() {
        ArrowType arrowType = new ArrowType(registry, null, new IntType(registry));
        assertEquals("function(int)", arrowType.toStringHelper(false));
    }

    @Test
    public void testGetLeastSupertypeAndGetGreatestSubtype() {
        JSType type1 = new ArrowType(registry, null, null);
        JSType type2 = new ArrowType(registry, null, new IntType(registry));
        JSType type3 = new ArrowType(registry, null, new ObjectType(registry, "String"));

        JSType leastSupertype = type1.getLeastSupertype(type2);
        assertNull(leastSupertype);

        leastSupertype = type1.getLeastSupertype(type1);
        assertEquals(type1, leastSupertype);

        leastSupertype = type1.getLeastSupertype(type3);
        assertNull(leastSupertype);

        leastSupertype = type2.getLeastSupertype(type1);
        assertNull(leastSupertype);

        leastSupertype = type2.getLeastSupertype(type2);
        assertEquals(type2, leastSupertype);

        leastSupertype = type2.getLeastSupertype(type3);
        assertNull(leastSupertype);

        leastSupertype = type3.getLeastSupertype(type1);
        assertNull(leastSupertype);

        leastSupertype = type3.getLeastSupertype(type2);
        assertNull(leastSupertype);

        leastSupertype = type3.getLeastSupertype(type3);
        assertEquals(type3, leastSupertype);

        JSType greatestSubtype = type1.getGreatestSubtype(type2);
        assertNull(greatestSubtype);

        greatestSubtype = type1.getGreatestSubtype(type1);
        assertEquals(type1, greatestSubtype);

        greatestSubtype = type1.getGreatestSubtype(type3);
        assertNull(greatestSubtype);

        greatestSubtype = type2.getGreatestSubtype(type1);
        assertNull(greatestSubtype);

        greatestSubtype = type2.getGreatestSubtype(type2);
        assertEquals(type2, greatestSubtype);

        greatestSubtype = type2.getGreatestSubtype(type3);
        assertNull(greatestSubtype);

        greatestSubtype = type3.getGreatestSubtype(type1);
        assertNull(greatestSubtype);

        greatestSubtype = type3.getGreatestSubtype(type2);
        assertNull(greatestSubtype);

        greatestSubtype = type3.getGreatestSubtype(type3);
        assertEquals(type3, greatestSubtype);
    }

    @Test
    public void testVisit() {
        Visitor visitor = mock(Visitor.class);
        ArrowType arrowType = new ArrowType(registry, null, new IntType(registry));
        arrowType.visit(visitor);
        verify(visitor).visitArrowType(arrowType);
    }

    @Test
    public void testResolveInternal() {
        JSType returnType = new IntType(registry);
        ArrowType arrowType = new ArrowType(registry, null, returnType);

        arrowType.resolveInternal(t, registry.getGlobalScope());
        assertTrue(returnType.equals(arrowType.returnType));
    }

    @Test
    public void testHasUnknownParamsOrReturn() {
        ArrowType arrowType1 = new ArrowType(registry, null, null);
        assertFalse(arrowType1.hasUnknownParamsOrReturn());

        ArrowType arrowType2 = new ArrowType(registry, null, new ObjectType(registry, "String"));
        assertFalse(arrowType2.hasUnknownParamsOrReturn());

        ArrowType arrowType3 = new ArrowType(registry, null, new IntType(registry), true);
        assertFalse(arrowType3.hasUnknownParamsOrReturn());

        ArrowType arrowType4 = new ArrowType(registry, null, null, true);
        assertTrue(arrowType4.hasUnknownParamsOrReturn());

        ArrowType arrowType5 = new ArrowType(registry, new Node(JSTypeNative.INT), null);
        assertFalse(arrowType5.hasUnknownParamsOrReturn());

        ArrowType arrowType6 = new ArrowType(registry, new Node(JSTypeNative.INT), null, true);
        assertTrue(arrowType6.hasUnknownParamsOrReturn());
    }
}