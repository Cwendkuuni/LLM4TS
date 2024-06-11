package com.google.javascript.rhino.jstype;

import static com.google.javascript.rhino.jstype.JSTypeNative.UNKNOWN_TYPE;
import static org.junit.Assert.*;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;

public class ArrowTypeTest {
    @Test
    public void testArrowTypeCreation() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = registry.createParametersWithVarArgs(getNativeType(UNKNOWN_TYPE));
        JSType returnType = getNativeType(UNKNOWN_TYPE);
        JSType arrowType = new ArrowType(registry, parameters, returnType);

        assertEquals(parameters, arrowType.parameters);
        assertEquals(returnType, arrowType.returnType);
        assertFalse(arrowType.returnTypeInferred);
    }

    @Test
    public void testArrowTypeCreationWithInferredReturnType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = registry.createParametersWithVarArgs(getNativeType(UNKNOWN_TYPE));
        JSType returnType = null;
        JSType arrowType = new ArrowType(registry, parameters, returnType, true);

        assertNull(arrowType.returnType);
        assertTrue(arrowType.returnTypeInferred);
    }

    @Test
    public void testArrowTypeIsSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node param1Type1 = registry.createPrimitiveType(JSTypeNative.INT);
        Node param1Type2 = registry.createPrimitiveType(JSTypeNative.STRING);
        Node param2 = registry.createParameter("p2", getNativeType(UNKNOWN_TYPE));
        Node params = registry.createParameters(param1Type1, param1Type2, param2);
        JSType returnType1 = registry.createPrimitiveType(JSTypeNative.BOOL);
        JSType returnType2 = registry.createPrimitiveType(JSTypeNative.VOID);

        ArrowType arrowType1 = new ArrowType(registry, params, returnType1);
        ArrowType arrowType2 = new ArrowType(registry, params, returnType2);
        ArrowType arrowType3 = new ArrowType(registry, params, returnType1);
        ArrowType arrowType4 = new ArrowType(registry, params.getFirstChild(), returnType1);
        ArrowType arrowType5 = new ArrowType(registry, null, returnType1);
        ArrowType arrowType6 = new ArrowType(registry, params, returnType2);

        assertTrue(arrowType1.isSubtype(arrowType1));
        assertTrue(arrowType1.isSubtype(arrowType3));
        assertFalse(arrowType1.isSubtype(arrowType2));
        assertFalse(arrowType1.isSubtype(arrowType4));
        assertFalse(arrowType1.isSubtype(arrowType5));
        assertFalse(arrowType1.isSubtype(arrowType6));
    }

    @Test
    public void testArrowTypeHasEqualParameters() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node param1Type1 = registry.createPrimitiveType(JSTypeNative.INT);
        Node param1Type2 = registry.createPrimitiveType(JSTypeNative.STRING);
        Node param2 = registry.createParameter("p2", getNativeType(UNKNOWN_TYPE));
        Node params1 = registry.createParameters(param1Type1, param1Type2, param2);
        Node params2 = registry.createParameters(param1Type1, param1Type2, param2);

        ArrowType arrowType1 = new ArrowType(registry, params1, getNativeType(UNKNOWN_TYPE));
        ArrowType arrowType2 = new ArrowType(registry, params2, getNativeType(UNKNOWN_TYPE));

        assertTrue(arrowType1.hasEqualParameters(arrowType2, false));
        assertTrue(arrowType1.hasEqualParameters(arrowType2, true));
    }

    @Test
    public void testArrowTypeCheckArrowEquivalenceHelper() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node param1Type1 = registry.createPrimitiveType(JSTypeNative.INT);
        Node param1Type2 = registry.createPrimitiveType(JSTypeNative.STRING);
        Node param2 = registry.createParameter("p2", getNativeType(UNKNOWN_TYPE));
        Node params1 = registry.createParameters(param1Type1, param1Type2, param2);
        JSType returnType1 = registry.createPrimitiveType(JSTypeNative.BOOL);
        JSType returnType2 = registry.createPrimitiveType(JSTypeNative.VOID);

        ArrowType arrowType1 = new ArrowType(registry, params1, returnType1);
        ArrowType arrowType2 = new ArrowType(registry, params1, returnType2);

        assertFalse(arrowType1.checkArrowEquivalenceHelper(arrowType2, false));
        assertFalse(arrowType1.checkArrowEquivalenceHelper(arrowType2, true));

        ArrowType arrowType3 = new ArrowType(registry, params1, returnType1);
        ArrowType arrowType4 = new ArrowType(registry, params2, returnType1);

        assertFalse(arrowType3.checkArrowEquivalenceHelper(arrowType4, false));
        assertFalse(arrowType3.checkArrowEquivalenceHelper(arrowType4, true));
    }

    @Test
    public void testArrowTypeHashCode() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node param1Type1 = registry.createPrimitiveType(JSTypeNative.INT);
        Node param1Type2 = registry.createPrimitiveType(JSTypeNative.STRING);
        Node param2 = registry.createParameter("p2", getNativeType(UNKNOWN_TYPE));
        Node params1 = registry.createParameters(param1Type1, param1Type2, param2);
        JSType returnType1 = registry.createPrimitiveType(JSTypeNative.BOOL);
        JSType returnType2 = registry.createPrimitiveType(JSTypeNative.VOID);

        ArrowType arrowType1 = new ArrowType(registry, params1, returnType1);
        ArrowType arrowType2 = new ArrowType(registry, params1, returnType2);

        int hashCode1 = arrowType1.hashCode();
        int hashCode2 = arrowType2.hashCode();

        assertNotEquals(hashCode1, hashCode2);

        // Test with equal types
        ArrowType arrowType3 = new ArrowType(registry, params1, returnType1);
        assertEquals(hashCode1, arrowType3.hashCode());
    }

    @Test
    public void testArrowTypeGetLeastSupertype() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testArrowTypeGetGreatestSubtype() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testArrowTypeTestForEquality() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testArrowTypeVisit() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testArrowTypeGetPossibleToBooleanOutcomes() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testArrowTypeResolveInternal() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node param1 = registry.createIdentifier("p1");
        Node param2 = registry.createIdentifier("p2");
        Node params = registry.createParameters(param1, param2);
        JSType returnType = registry.createPrimitiveType(JSTypeNative.VOID);
        JSType arrowType = new ArrowType(registry, params, returnType);

        ErrorReporter errorReporter = new ErrorReporter();
        StaticScope<JSType> scope = new StaticScope<>(registry, errorReporter);

        arrowType.resolveInternal(errorReporter, scope);

        assertEquals(JSTypeNative.VOID, arrowType.returnType.getJSType());
        assertEquals(JSTypeNative.INT, param1.getJSType().getJSType());
        assertEquals(JSTypeNative.UNKNOWN_TYPE, param2.getJSType());
    }

    @Test
    public void testArrowTypeHasUnknownParamsOrReturn() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node param1 = registry.createIdentifier("p1");
        Node param2 = registry.createIdentifier("p2");
        Node params = registry.createParameters(param1, param2);
        JSType returnType = null;
        JSType arrowType = new ArrowType(registry, params, returnType);

        assertTrue(arrowType.hasUnknownParamsOrReturn());

        arrowType = new ArrowType(registry, params, getNativeType(UNKNOWN_TYPE));

        assertFalse(arrowType.hasUnknownParamsOrReturn());
    }

    @Test
    public void testArrowTypeToStringHelper() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node param1 = registry.createIdentifier("p1");
        Node param2 = registry.createIdentifier("p2");
        Node params = registry.createParameters(param1, param2);
        JSType returnType = registry.createPrimitiveType(JSTypeNative.VOID);
        JSType arrowType = new ArrowType(registry, params, returnType);

        String arrowTypeString = arrowType.toStringHelper(false);

        assertEquals("[ArrowType]", arrowTypeString);
    }

    @Test
    public void testArrowTypeHasAnyTemplateInternal() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node param1 = registry.createParameter("p1", getNativeType(JSTypeNative.INT));
        Node param2 = registry.createParameter("p2", getNativeType(JSTypeNative.STRING));
        Node param3 = registry.createParameter("p3", registry.createTypeVariable("T"));
        Node params = registry.createParameters(param1, param2, param3);
        JSType returnType = registry.createPrimitiveType(JSTypeNative.VOID);
        JSType arrowType = new ArrowType(registry, params, returnType);

        assertTrue(arrowType.hasAnyTemplateInternal());
    }

    private JSType getNativeType(JSTypeNative jstype) {
        return new JSTypeNative(jstype);
    }
}