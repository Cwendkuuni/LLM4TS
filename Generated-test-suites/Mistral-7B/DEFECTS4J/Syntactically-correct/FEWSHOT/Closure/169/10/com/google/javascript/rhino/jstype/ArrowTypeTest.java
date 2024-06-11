package com.google.javascript.rhino.jstype;

import static com.google.javascript.rhino.jstype.JSTypeNative.UNKNOWN_TYPE;
import static org.junit.Assert.*;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;

public class ArrowTypeTest {
    private static final JSTypeRegistry registry = new JSTypeRegistry();

    @Test
    public void testCreateArrowType() {
        ArrowType arrowType1 = new ArrowType(registry, null, UNKNOWN_TYPE);
        ArrowType arrowType2 = new ArrowType(registry, null, UNKNOWN_TYPE, true);

        assertEquals(arrowType1.getClass(), ArrowType.class);
        assertEquals(arrowType2.getClass(), ArrowType.class);
        assertEquals(arrowType1.isSubtype(arrowType2), true);
        assertEquals(arrowType2.isSubtype(arrowType1), false);

        // Test with parameters and return types
        Node parametersNode = registry.createParametersWithVarArgs(getNativeType(UNKNOWN_TYPE));
        Node returnTypeNode = registry.createName("Int32");
        ArrowType arrowType3 = new ArrowType(registry, parametersNode, returnTypeNode);
        ArrowType arrowType4 = new ArrowType(registry, parametersNode, returnTypeNode, true);

        assertEquals(arrowType3.getClass(), ArrowType.class);
        assertEquals(arrowType4.getClass(), ArrowType.class);
        assertEquals(arrowType3.isSubtype(arrowType4), true);
        assertEquals(arrowType4.isSubtype(arrowType3), false);
    }

    @Test
    public void testIsSubtype() {
        ArrowType arrowType1 = new ArrowType(registry, null, UNKNOWN_TYPE);
        ArrowType arrowType2 = new ArrowType(registry, null, UNKNOWN_TYPE, true);
        ArrowType arrowType3 = new ArrowType(registry, null, getNativeType("Int32"));
        ArrowType arrowType4 = new ArrowType(registry, null, getNativeType(UNKNOWN_TYPE));
        ArrowType arrowType5 = new ArrowType(registry, null, getNativeType("String"));
        ArrowType arrowType6 = new ArrowType(registry, null, getNativeType("Boolean"));

        // Simple types
        assertFalse(arrowType1.isSubtype(arrowType3));
        assertFalse(arrowType1.isSubtype(arrowType4));
        assertFalse(arrowType1.isSubtype(arrowType5));
        assertFalse(arrowType1.isSubtype(arrowType6));

        assertFalse(arrowType3.isSubtype(arrowType1));
        assertFalse(arrowType3.isSubtype(arrowType4));
        assertFalse(arrowType3.isSubtype(arrowType5));
        assertFalse(arrowType3.isSubtype(arrowType6));

        // Inferred return types
        assertFalse(arrowType1.isSubtype(arrowType2));
        assertTrue(arrowType2.isSubtype(arrowType1));

        // Different parameter lists
        Node parametersNode1 = registry.createParametersWithVarArgs(getNativeType(UNKNOWN_TYPE));
        Node parametersNode2 = registry.createParametersWithVarArgs(getNativeType("Int32"));

        ArrowType arrowType7 = new ArrowType(registry, parametersNode1, UNKNOWN_TYPE);
        ArrowType arrowType8 = new ArrowType(registry, parametersNode2, UNKNOWN_TYPE);

        assertFalse(arrowType7.isSubtype(arrowType8));
        assertFalse(arrowType8.isSubtype(arrowType7));

        // Covariant return types
        ArrowType arrowType9 = new ArrowType(registry, null, getNativeType("Int32"));
        ArrowType arrowType10 = new ArrowType(registry, null, getNativeType("Number"));

        assertTrue(arrowType9.isSubtype(arrowType10));
        assertFalse(arrowType10.isSubtype(arrowType9));

        // Contravariant parameter types
        ArrowType arrowType11 = new ArrowType(registry, null, getNativeType("Number"));
        ArrowType arrowType12 = new ArrowType(registry, null, getNativeType("Int32"));

        assertFalse(arrowType11.isSubtype(arrowType12));
        assertTrue(arrowType12.isSubtype(arrowType11));
    }

    @Test
    public void testHasEqualParameters() {
        ArrowType arrowType1 = new ArrowType(registry, null, UNKNOWN_TYPE);
        ArrowType arrowType2 = new ArrowType(registry, null, UNKNOWN_TYPE);
        ArrowType arrowType3 = new ArrowType(registry, null, getNativeType("Int32"));
        ArrowType arrowType4 = new ArrowType(registry, null, getNativeType("String"));
        ArrowType arrowType5 = new ArrowType(registry, null, getNativeType("Int32"), true);
        ArrowType arrowType6 = new ArrowType(registry, null, getNativeType("Int32"), true);

        assertTrue(arrowType1.hasEqualParameters(arrowType1, true));
        assertTrue(arrowType1.hasEqualParameters(arrowType1, false));
        assertTrue(arrowType1.hasEqualParameters(arrowType2, true));
        assertTrue(arrowType1.hasEqualParameters(arrowType2, false));
        assertFalse(arrowType1.hasEqualParameters(arrowType3, true));
        assertFalse(arrowType1.hasEqualParameters(arrowType3, false));
        assertFalse(arrowType1.hasEqualParameters(arrowType4, true));
        assertFalse(arrowType1.hasEqualParameters(arrowType4, false));
        assertTrue(arrowType5.hasEqualParameters(arrowType6, true));
        assertTrue(arrowType5.hasEqualParameters(arrowType6, false));
    }

    @Test
    public void testCheckArrowEquivalenceHelper() {
        ArrowType arrowType1 = new ArrowType(registry, null, UNKNOWN_TYPE);
        ArrowType arrowType2 = new ArrowType(registry, null, UNKNOWN_TYPE);
        ArrowType arrowType3 = new ArrowType(registry, null, getNativeType("Int32"));
        ArrowType arrowType4 = new ArrowType(registry, null, getNativeType("String"));
        ArrowType arrowType5 = new ArrowType(registry, null, getNativeType("Int32"), true);
        ArrowType arrowType6 = new ArrowType(registry, null, getNativeType("Int32"), true);

        assertTrue(arrowType1.checkArrowEquivalenceHelper(arrowType1, true));
        assertTrue(arrowType1.checkArrowEquivalenceHelper(arrowType1, false));
        assertTrue(arrowType1.checkArrowEquivalenceHelper(arrowType2, true));
        assertTrue(arrowType1.checkArrowEquivalenceHelper(arrowType2, false));
        assertFalse(arrowType1.checkArrowEquivalenceHelper(arrowType3, true));
        assertFalse(arrowType1.checkArrowEquivalenceHelper(arrowType3, false));
        assertFalse(arrowType1.checkArrowEquivalenceHelper(arrowType4, true));
        assertFalse(arrowType1.checkArrowEquivalenceHelper(arrowType4, false));
        assertTrue(arrowType5.checkArrowEquivalenceHelper(arrowType6, true));
        assertTrue(arrowType5.checkArrowEquivalenceHelper(arrowType6, false));
    }

    @Test
    public void testHashCode() {
        ArrowType arrowType1 = new ArrowType(registry, null, UNKNOWN_TYPE);
        ArrowType arrowType2 = new ArrowType(registry, null, UNKNOWN_TYPE);
        ArrowType arrowType3 = new ArrowType(registry, null, getNativeType("Int32"));
        ArrowType arrowType4 = new ArrowType(registry, null, getNativeType("String"));
        ArrowType arrowType5 = new ArrowType(registry, null, getNativeType("Int32"), true);

        int hashCode1 = arrowType1.hashCode();
        int hashCode2 = arrowType1.hashCode();

        assertEquals(hashCode1, hashCode2);

        int hashCode3 = arrowType2.hashCode();
        int hashCode4 = arrowType1.hashCode();

        assertNotEquals(hashCode3, hashCode4);

        int hashCode5 = arrowType3.hashCode();
        int hashCode6 = arrowType1.hashCode();

        assertNotEquals(hashCode5, hashCode6);

        int hashCode7 = arrowType4.hashCode();
        int hashCode8 = arrowType1.hashCode();

        assertNotEquals(hashCode7, hashCode8);

        int hashCode9 = arrowType5.hashCode();
        int hashCode10 = arrowType1.hashCode();

        assertNotEquals(hashCode9, hashCode10);
    }

    @Test
    public void testResolveInternal() {
        ArrowType arrowType = new ArrowType(registry, null, UNKNOWN_TYPE);
        ErrorReporter errorReporter = new ErrorReporter();
        StaticScope<JSType> scope = new StaticScope<>();

        ArrowType resolvedArrowType = arrowType.resolveInternal(errorReporter, scope);

        assertEquals(arrowType, resolvedArrowType);

        Node returnTypeNode = registry.createName("Int32");
        arrowType = new ArrowType(registry, null, returnTypeNode);

        resolvedArrowType = arrowType.resolveInternal(errorReporter, scope);

        assertEquals(arrowType, resolvedArrowType);

        Node parametersNode = registry.createParametersWithVarArgs(getNativeType(UNKNOWN_TYPE));
        arrowType = new ArrowType(registry, parametersNode, null);

        resolvedArrowType = arrowType.resolveInternal(errorReporter, scope);

        assertEquals(arrowType, resolvedArrowType);

        JSType resolvedParameters = parametersNode.getJSType();
        Node resolvedParamNode = registry.createName("Int32");
        JSType resolvedParamType = registry.createParameterType(resolvedParamNode, null, null);

        parametersNode.setJSType(resolvedParamType);
        arrowType = new ArrowType(registry, parametersNode, null);

        resolvedArrowType = arrowType.resolveInternal(errorReporter, scope);

        assertEquals(arrowType, resolvedArrowType);

        assertEquals(resolvedParamType, parametersNode.getJSType());
    }

    @Test
    public void testHasUnknownParamsOrReturn() {
        ArrowType arrowType1 = new ArrowType(registry, null, UNKNOWN_TYPE);
        ArrowType arrowType2 = new ArrowType(registry, null, getNativeType("Int32"));
        ArrowType arrowType3 = new ArrowType(registry, null, getNativeType(UNKNOWN_TYPE));

        assertTrue(arrowType1.hasUnknownParamsOrReturn());
        assertFalse(arrowType2.hasUnknownParamsOrReturn());
        assertTrue(arrowType3.hasUnknownParamsOrReturn());
    }

    @Test
    public void testToStringHelper() {
        ArrowType arrowType = new ArrowType(registry, null, UNKNOWN_TYPE);

        assertEquals("[ArrowType]", arrowType.toStringHelper(false));
        assertEquals("[ArrowType]", arrowType.toStringHelper(true));
    }
}