package com.google.javascript.rhino.jstype;

import static com.google.javascript.rhino.jstype.JSTypeNative.UNKNOWN_TYPE;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistryImpl;

import org.junit.Before;
import org.junit.Test;

public class ArrowTypeTest {
    private JSTypeRegistry registry;

    @Before
    public void setUp() {
        registry = new JSTypeRegistryImpl();
    }

    @Test
    public void testArrowTypeCreation() {
        Node node = registry.parseTypeSignature("(number, number) -> number");
        ArrowType arrowType = new ArrowType(registry, node, null, false);
        assertTrue(arrowType.isSubtype(arrowType));

        Node nodeWithOptionalParam = registry.parseTypeSignature("(number, number?) -> number");
        ArrowType arrowTypeWithOptionalParam = new ArrowType(registry, nodeWithOptionalParam, null, false);
        assertTrue(arrowType.isSubtype(arrowTypeWithOptionalParam));
    }

    @Test
    public void testArrowTypeSubtyping() {
        JSType defaultType = getNativeType(JSTypeNative.OBJECT);
        JSType returnType = getNativeType(JSTypeNative.STRING);
        Node parametersNode = registry.parseTypeSignature("(number, number)")
                .getFirstChild();

        ArrowType arrowType1 = new ArrowType(registry, parametersNode, returnType, false);
        ArrowType arrowType2 = new ArrowType(registry, parametersNode, defaultType, false);
        ArrowType arrowType3 = new ArrowType(registry, parametersNode, null, true);

        ArrowType arrowType4 = new ArrowType(registry, parametersNode, null, false);
        ArrowType arrowType5 = new ArrowType(registry, null, null, false);

        ArrowType arrowType6 = new ArrowType(registry, null, returnType, true);
        ArrowType arrowType7 = new ArrowType(registry, null, returnType, false);

        ArrowType arrowType8 = new ArrowType(registry, null, returnType, false);
        ArrowType arrowType9 = new ArrowType(registry, null, getNativeType(JSTypeNative.VOID), false);

        // subtyping function types
        FunctionType functionType1 = new FunctionType(registry, null, null, getNativeType(JSTypeNative.VOID), null, null);
        FunctionType functionType2 = new FunctionType(registry, null, null, getNativeType(JSTypeNative.VOID), parametersNode, null);
        FunctionType functionType3 = new FunctionType(registry, null, null, returnType, null, null);
        FunctionType functionType4 = new FunctionType(registry, null, null, returnType, parametersNode, null);

        // arrow type <: function type
        for (ArrowType arrowType : new ArrowType[] { arrowType1, arrowType4 }) {
            for (FunctionType functionType : new FunctionType[] { functionType3, functionType4 }) {
                assertTrue(arrowType.isSubtype(functionType));
            }
        }

        // function type <: arrow type
        for (FunctionType functionType : new FunctionType[] { functionType2, functionType1 }) {
            for (ArrowType arrowType : new ArrowType[] { arrowType1, arrowType4 }) {
                assertTrue(functionType.isSubtype(arrowType));
            }
        }

        // arrow type <: arrow type (covariant)
        assertTrue(arrowType1.isSubtype(arrowType4));

        // arrow type <: arrow type (contravariant)
        // This is described in Draft 2 of the ES4 spec,
        // Section 3.4.7: Subtyping Function Types.
        //
        // that.paramType[i] <: this.paramType[i] (contravariant)
        ArrowType arrowType10 = new ArrowType(registry, null, null, false);
        ArrowType arrowType11 = new ArrowType(registry, null, getNativeType(JSTypeNative.NUMBER), false);
        ArrowType arrowType12 = new ArrowType(registry, null, null, true);

        // this.paramType[i] is required,
        // then that.paramType[i] is required.
        assertFalse(arrowType10.isSubtype(arrowType11));
        assertFalse(arrowType11.isSubtype(arrowType10));

        // this.paramType[i] is optional,
        // then that.paramType[i] is optional.
        assertTrue(arrowType10.isSubtype(arrowType12));
        assertTrue(arrowType12.isSubtype(arrowType10));

        // this.paramType[i] is required,
        // then that.paramType[i] is optional.
        assertFalse(arrowType11.isSubtype(arrowType12));
        // that.paramType[i] cannot be a supertype, because it's missing a required argument.
        assertFalse(arrowType12.isSubtype(arrowType11));

        // arrow type with var_args <: arrow type without var_args
        ArrowType arrowType13 = new ArrowType(registry, null, null, true);
        ArrowType arrowType14 = new ArrowType(registry, null, null, false);
        assertTrue(arrowType13.isSubtype(arrowType14));
        assertFalse(arrowType14.isSubtype(arrowType13));

        // arrow type <: null
        assertFalse(arrowType1.isSubtype(null));
        assertFalse(null.isSubtype(arrowType1));

        // arrow type <: void
        assertFalse(arrowType1.isSubtype(getNativeType(JSTypeNative.VOID)));
        assertFalse(getNativeType(JSTypeNative.VOID).isSubtype(arrowType1));

        // arrow type <: default type
        assertFalse(arrowType1.isSubtype(defaultType));
        assertFalse(defaultType.isSubtype(arrowType1));

        // arrow type <: unknown type
        assertFalse(arrowType1.isSubtype(getNativeType(UNKNOWN_TYPE)));
        assertFalse(getNativeType(UNKNOWN_TYPE).isSubtype(arrowType1));

        // arrow type <: array type
        JSType arrayType = new JSTypeArrayType(registry, getNativeType(JSTypeNative.NUMBER), null);
        assertFalse(arrowType1.isSubtype(arrayType));
        assertFalse(arrayType.isSubtype(arrowType1));

        // arrow type <: function type with different return types
        FunctionType functionType5 = new FunctionType(registry, null, null, getNativeType(JSTypeNative.NUMBER), null, null);
        assertFalse(arrowType1.isSubtype(functionType5));

        // arrow type <: function type with different parameter types
        FunctionType functionType6 = new FunctionType(registry, null, null, getNativeType(JSTypeNative.STRING), null, null);
        assertFalse(arrowType1.isSubtype(functionType6));

        // arrow type <: function type with different parameter count
        FunctionType functionType7 = new FunctionType(registry, null, null, null, null, null);
        assertFalse(arrowType1.isSubtype(functionType7));

        // arrow type <: function type with different var_args behavior
        FunctionType functionType8 = new FunctionType(registry, null, null, null, null, parametersNode);
        assertFalse(arrowType1.isSubtype(functionType8));
    }

    @Test
    public void testArrowTypeEquivalence() {
        Node node1 = registry.parseTypeSignature("(number, number) -> number");
        Node node2 = registry.parseTypeSignature("(number, number) -> number");
        ArrowType arrowType1 = new ArrowType(registry, node1, null, false);
        ArrowType arrowType2 = new ArrowType(registry, node2, null, false);

        assertTrue(arrowType1.hasEqualParameters(arrowType2, false));
        assertTrue(arrowType1.checkArrowEquivalenceHelper(arrowType2, false));

        Node node3 = registry.parseTypeSignature("(number, number?) -> number");
        ArrowType arrowType3 = new ArrowType(registry, node3, null, false);
        assertFalse(arrowType1.hasEqualParameters(arrowType3, false));
        assertFalse(arrowType1.checkArrowEquivalenceHelper(arrowType3, false));

        Node node4 = registry.parseTypeSignature("(number, number) -> void");
        ArrowType arrowType4 = new ArrowType(registry, node4, null, false);
        assertFalse(arrowType1.hasEqualParameters(arrowType4, false));
        assertFalse(arrowType1.checkArrowEquivalenceHelper(arrowType4, false));

        Node node5 = registry.parseTypeSignature("(number, number) -> number?");
        ArrowType arrowType5 = new ArrowType(registry, node5, null, false);
        assertFalse(arrowType1.hasEqualParameters(arrowType5, false));
        assertFalse(arrowType1.checkArrowEquivalenceHelper(arrowType5, false));

        Node node6 = registry.parseTypeSignature("(number, number) -> number");
        ArrowType arrowType6 = new ArrowType(registry, node6, null, true);
        assertFalse(arrowType1.hasEqualParameters(arrowType6, false));
        assertFalse(arrowType1.checkArrowEquivalenceHelper(arrowType6, false));

        Node node7 = registry.parseTypeSignature("(number, number) -> number");
        ArrowType arrowType7 = new ArrowType(registry, node7, getNativeType(UNKNOWN_TYPE), false);
        assertFalse(arrowType1.hasEqualParameters(arrowType7, false));
        assertFalse(arrowType1.checkArrowEquivalenceHelper(arrowType7, false));

        Node node8 = registry.parseTypeSignature("(number, number) -> number");
        ArrowType arrowType8 = new ArrowType(registry, node8, null, false);
        ArrowType arrowType9 = new ArrowType(registry, node8, null, true);
        assertFalse(arrowType1.hasEqualParameters(arrowType9, false));
        assertFalse(arrowType1.checkArrowEquivalenceHelper(arrowType9, false));

        Node node9 = registry.parseTypeSignature("(number, number) -> number");
        ArrowType arrowType10 = new ArrowType(registry, node9, null, false);
        ArrowType arrowType11 = new ArrowType(registry, node9, null, true);
        assertFalse(arrowType1.hasEqualParameters(arrowType11, false));
        assertFalse(arrowType1.checkArrowEquivalenceHelper(arrowType11, false));

        Node node10 = registry.parseTypeSignature("(number, number) -> number");
        ArrowType arrowType12 = new ArrowType(registry, node10, null, false);
        ArrowType arrowType13 = new ArrowType(registry, null, null, false);
        assertFalse(arrowType1.hasEqualParameters(arrowType13, false));
        assertFalse(arrowType1.checkArrowEquivalenceHelper(arrowType13, false));

        Node node11 = registry.parseTypeSignature("(number, number) -> number");
        ArrowType arrowType14 = new ArrowType(registry, null, null, true);
        assertFalse(arrowType1.hasEqualParameters(arrowType14, false));
        assertFalse(arrowType1.checkArrowEquivalenceHelper(arrowType14, false));

        Node node12 = registry.parseTypeSignature("(number, number) -> number");
        ArrowType arrowType15 = new ArrowType(registry, node12, null, false);
        ArrowType arrowType16 = new ArrowType(registry, null, getNativeType(UNKNOWN_TYPE), false);
        assertFalse(arrowType1.hasEqualParameters(arrowType16, false));
        assertFalse(arrowType1.checkArrowEquivalenceHelper(arrowType16, false));

        Node node13 = registry.parseTypeSignature("(number) -> number");
        ArrowType arrowType17 = new ArrowType(registry, node13, null, false);
        assertFalse(arrowType1.hasEqualParameters(arrowType17, false));
        assertFalse(arrowType1.checkArrowEquivalenceHelper(arrowType17, false));

        Node node14 = registry.parseTypeSignature("(string, number) -> number");
        ArrowType arrowType18 = new ArrowType(registry, node14, null, false);
        assertFalse(arrowType1.hasEqualParameters(arrowType18, false));
        assertFalse(arrowType1.checkArrowEquivalenceHelper(arrowType18, false));

        Node node15 = registry.parseTypeSignature("(number, number) -> number");
        ArrowType arrowType19 = new ArrowType(registry, node15, null, false);
        ArrowType arrowType20 = new ArrowType(registry, null, null, true);
        assertFalse(arrowType1.hasEqualParameters(arrowType20, false));
        assertFalse(arrowType1.checkArrowEquivalenceHelper(arrowType20, false));
    }

    @Test
    public void testArrowTypeHashCode() {
        Node node = registry.parseTypeSignature("(number, number) -> number");
        ArrowType arrowType1 = new ArrowType(registry, node, null, false);
        ArrowType arrowType2 = new ArrowType(registry, node, null, false);
        ArrowType arrowType3 = new ArrowType(registry, node, null, true);

        int hashCode1 = arrowType1.hashCode();
        int hashCode2 = arrowType2.hashCode();
        int hashCode3 = arrowType3.hashCode();

        // arrow type with the same parameters and returnType should have the same hash code
        assertTrue(hashCode1 == hashCode2);

        // arrow type with different returnType should have a different hash code
        assertFalse(hashCode1 == hashCode3);

        // arrow type with different parameters should have a different hash code
        ArrowType arrowType4 = new ArrowType(registry, null, null, false);
        assertFalse(hashCode1 == hashCode4);

        // arrow type with optional parameter should have a different hash code
        ArrowType arrowType5 = new ArrowType(registry, null, null, true);
        assertFalse(hashCode1 == hashCode5);

        // arrow type with var_args should have a different hash code
        ArrowType arrowType6 = new ArrowType(registry, null, null, false);
        ArrowType arrowType7 = new ArrowType(registry, null, null, true);
        assertFalse(hashCode1 == hashCode7);
    }

    @Test
    public void testArrowTypeToString() {
        Node node = registry.parseTypeSignature("(number, number) -> number");
        ArrowType arrowType = new ArrowType(registry, node, null, false);
        assertEquals("[ArrowType]", arrowType.toStringHelper(false));
    }
}

class JSTypeNative {
    public final static int UNKNOWN_TYPE = 0;
    public final static int BOOLEAN = 1;
    public final static int NUMBER = 2;
    public final static int STRING = 3;
    public final static int OBJECT = 4;
    public final static int ARRAY = 5;
    public final static int NULL = 6;
}