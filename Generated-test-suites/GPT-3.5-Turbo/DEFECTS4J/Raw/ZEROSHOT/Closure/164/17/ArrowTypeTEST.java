
package com.google.javascript.rhino.jstype;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ArrowTypeTest {

    private JSTypeRegistry registry;
    private ArrowType arrowType;

    @Before
    public void setUp() {
        registry = new JSTypeRegistry();
        arrowType = new ArrowType(registry, null, null);
    }

    @Test
    public void testIsSubtype_WithDifferentType_ReturnFalse() {
        JSType otherType = new JSType(registry) {
            @Override
            public boolean isSubtype(JSType other) {
                return false;
            }
        };
        assertFalse(arrowType.isSubtype(otherType));
    }

    @Test
    public void testIsSubtype_WithEqualReturnType_ReturnTrue() {
        JSType returnType = new JSType(registry);
        ArrowType arrowType1 = new ArrowType(registry, null, returnType);
        ArrowType arrowType2 = new ArrowType(registry, null, returnType);
        assertTrue(arrowType1.isSubtype(arrowType2));
    }

    @Test
    public void testIsSubtype_WithDifferentReturnType_ReturnFalse() {
        JSType returnType1 = new JSType(registry);
        JSType returnType2 = new JSType(registry);
        ArrowType arrowType1 = new ArrowType(registry, null, returnType1);
        ArrowType arrowType2 = new ArrowType(registry, null, returnType2);
        assertFalse(arrowType1.isSubtype(arrowType2));
    }

    @Test
    public void testIsSubtype_WithEqualParameters_ReturnTrue() {
        Node parameters = new Node();
        JSType paramType = new JSType(registry);
        parameters.setJSType(paramType);
        ArrowType arrowType1 = new ArrowType(registry, parameters, null);
        ArrowType arrowType2 = new ArrowType(registry, parameters, null);
        assertTrue(arrowType1.isSubtype(arrowType2));
    }

    @Test
    public void testIsSubtype_WithDifferentParameters_ReturnFalse() {
        Node parameters1 = new Node();
        JSType paramType1 = new JSType(registry);
        parameters1.setJSType(paramType1);
        Node parameters2 = new Node();
        JSType paramType2 = new JSType(registry);
        parameters2.setJSType(paramType2);
        ArrowType arrowType1 = new ArrowType(registry, parameters1, null);
        ArrowType arrowType2 = new ArrowType(registry, parameters2, null);
        assertFalse(arrowType1.isSubtype(arrowType2));
    }

    @Test
    public void testIsSubtype_WithVarArgsParameters_ReturnTrue() {
        Node parameters1 = new Node(new Node(Token.VAR_ARGS));
        Node parameters2 = new Node(new Node(Token.VAR_ARGS));
        ArrowType arrowType1 = new ArrowType(registry, parameters1, null);
        ArrowType arrowType2 = new ArrowType(registry, parameters2, null);
        assertTrue(arrowType1.isSubtype(arrowType2));
    }

    @Test
    public void testHasEqualParameters_WithEqualParameters_ReturnTrue() {
        Node parameters1 = new Node();
        JSType paramType1 = new JSType(registry);
        parameters1.setJSType(paramType1);
        Node parameters2 = new Node();
        JSType paramType2 = new JSType(registry);
        parameters2.setJSType(paramType2);
        ArrowType arrowType1 = new ArrowType(registry, parameters1, null);
        ArrowType arrowType2 = new ArrowType(registry, parameters2, null);
        assertTrue(arrowType1.hasEqualParameters(arrowType2));
    }

    @Test
    public void testHasEqualParameters_WithDifferentParameters_ReturnFalse() {
        Node parameters1 = new Node();
        JSType paramType1 = new JSType(registry);
        parameters1.setJSType(paramType1);
        Node parameters2 = new Node();
        JSType paramType2 = new JSType(registry);
        parameters2.setJSType(paramType2);
        ArrowType arrowType1 = new ArrowType(registry, parameters1, null);
        ArrowType arrowType2 = new ArrowType(registry, parameters2, null);
        assertFalse(arrowType1.hasEqualParameters(arrowType2));
    }

    @Test
    public void testHasEqualParameters_WithUnequalParameters_ReturnFalse() {
        Node parameters1 = new Node();
        JSType paramType1 = new JSType(registry);
        parameters1.setJSType(paramType1);
        Node parameters2 = null;
        ArrowType arrowType1 = new ArrowType(registry, parameters1, null);
        ArrowType arrowType2 = new ArrowType(registry, parameters2, null);
        assertFalse(arrowType1.hasEqualParameters(arrowType2));
    }

    @Test
    public void testIsEquivalentTo_WithEqualObject_ReturnTrue() {
        JSType returnType = new JSType(registry);
        Node parameters = new Node();
        JSType paramType = new JSType(registry);
        parameters.setJSType(paramType);
        ArrowType arrowType1 = new ArrowType(registry, parameters, returnType);
        ArrowType arrowType2 = new ArrowType(registry, parameters, returnType);
        assertTrue(arrowType1.isEquivalentTo(arrowType2));
    }

    @Test
    public void testIsEquivalentTo_WithDifferentReturnType_ReturnFalse() {
        JSType returnType1 = new JSType(registry);
        JSType returnType2 = new JSType(registry);
        ArrowType arrowType1 = new ArrowType(registry, null, returnType1);
        ArrowType arrowType2 = new ArrowType(registry, null, returnType2);
        assertFalse(arrowType1.isEquivalentTo(arrowType2));
    }

    @Test
    public void testIsEquivalentTo_WithDifferentParameters_ReturnFalse() {
        Node parameters1 = new Node();
        JSType paramType1 = new JSType(registry);
        parameters1.setJSType(paramType1);
        Node parameters2 = new Node();
        JSType paramType2 = new JSType(registry);
        parameters2.setJSType(paramType2);
        ArrowType arrowType1 = new ArrowType(registry, parameters1, null);
        ArrowType arrowType2 = new ArrowType(registry, parameters2, null);
        assertFalse(arrowType1.isEquivalentTo(arrowType2));
    }

    @Test
    public void testGetLeastSupertype_ThrowsUnsupportedOperationException() {
        try {
            arrowType.getLeastSupertype(null);
            fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testGetGreatestSubtype_ThrowsUnsupportedOperationException() {
        try {
            arrowType.getGreatestSubtype(null);
            fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testTestForEquality_ThrowsUnsupportedOperationException() {
        try {
            arrowType.testForEquality(null);
            fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testVisit_ThrowsUnsupportedOperationException() {
        try {
            arrowType.visit(null);
            fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testGetPossibleToBooleanOutcomes_ReturnTrue() {
        assertEquals(BooleanLiteralSet.TRUE, arrowType.getPossibleToBooleanOutcomes());
    }

    @Test
    public void testResolveInternal_ReturnTypeAndParametersResolved() {
        ErrorReporter errorReporter = new ErrorReport();
        StaticScope<JSType> scope = new StaticScope<>();
        arrowType.resolveInternal(errorReporter, scope);
        assertTrue(arrowType.hasUnknownParamsOrReturn());
    }

    @Test
    public void testToStringHelper_ReturnString() {
        assertNotNull(arrowType.toStringHelper(false));
    }
}
