package com.google.javascript.rhino.jstype;

import static com.google.javascript.rhino.jstype.JSTypeNative.UNKNOWN_TYPE;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ArrowTypeTest {
    @Test
    public void testIsSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry() {};

        Node parameters = registry.createParametersWithVarArgs(getNativeType(UNKNOWN_TYPE));

        ArrowType arrowType1 = new ArrowType(registry, parameters, getNativeType(UNKNOWN_TYPE));
        ArrowType arrowType2 = new ArrowType(registry, parameters, getNativeType(UNKNOWN_TYPE));
        ArrowType arrowType3 = new ArrowType(registry, parameters, getNativeType(JSType.NUMBER));
        ArrowType arrowType4 = new ArrowType(registry, null, getNativeType(JSType.NUMBER));
        ArrowType arrowType5 = new ArrowType(registry, null, getNativeType(JSType.NUMBER), true);

        ArrowType arrowType6 = new ArrowType(registry, parameters, getNativeType(JSType.STRING));
        ArrowType arrowType7 = new ArrowType(registry, null, getNativeType(JSType.STRING));
        ArrowType arrowType8 = new ArrowType(registry, null, getNativeType(JSType.NUMBER), true);
        ArrowType arrowType9 = new ArrowType(registry, null, getNativeType(JSType.STRING), true);

        ArrowType arrowType10 = new ArrowType(registry, Collections.singletonList(
                registry.createParameter("a", getNativeType(JSType.NUMBER))),
                getNativeType(JSType.NUMBER));
        ArrowType arrowType11 = new ArrowType(registry, Collections.singletonList(
                registry.createParameter("a", getNativeType(JSType.STRING))),
                getNativeType(JSType.NUMBER));

        ArrowType arrowType12 = new ArrowType(registry, parameters, getNativeType(UNKNOWN_TYPE), true);
        ArrowType arrowType13 = new ArrowType(registry, null, getNativeType(UNKNOWN_TYPE), true);

        List<ArrowType> arrowTypes = Arrays.asList(
                arrowType1, arrowType1, arrowType2, arrowType3, arrowType4, arrowType5,
                arrowType6, arrowType7, arrowType8, arrowType9, arrowType10, arrowType11
        );

        for (int i = 0; i < arrowTypes.size(); i++) {
            for (int j = i + 1; j < arrowTypes.size(); j++) {
                ArrowType a = arrowTypes.get(i);
                ArrowType b = arrowTypes.get(j);
                assertFalse("Expected false: " + a.toString() + " isSubtype " + b.toString(), a.isSubtype(b));
                assertFalse("Expected false: " + b.toString() + " isSubtype " + a.toString(), b.isSubtype(a));
            }
        }

        ArrowType arrowType14 = new ArrowType(registry, parameters, getNativeType(JSType.NUMBER), false);
        ArrowType arrowType15 = new ArrowType(registry, null, getNativeType(JSType.NUMBER), false);
        assertTrue("Expected true: " + arrowType14.toString() + " isSubtype " + arrowType15.toString(), arrowType14.isSubtype(arrowType15));
        assertTrue("Expected true: " + arrowType15.toString() + " isSubtype " + arrowType14.toString(), arrowType15.isSubtype(arrowType14));

        ArrowType arrowType16 = new ArrowType(registry, parameters, getNativeType(JSType.NUMBER), true);
        ArrowType arrowType17 = new ArrowType(registry, null, getNativeType(JSType.NUMBER), false);
        assertFalse("Expected false: " + arrowType16.toString() + " isSubtype " + arrowType17.toString(), arrowType16.isSubtype(arrowType17));
        assertFalse("Expected false: " + arrowType17.toString() + " isSubtype " + arrowType16.toString(), arrowType17.isSubtype(arrowType16));

        ArrowType arrowType18 = new ArrowType(registry, parameters, getNativeType(JSType.NUMBER), false);
        ArrowType arrowType19 = new ArrowType(registry, null, getNativeType(JSType.NUMBER), true);
        assertFalse("Expected false: " + arrowType18.toString() + " isSubtype " + arrowType19.toString(), arrowType18.isSubtype(arrowType19));
        assertFalse("Expected false: " + arrowType19.toString() + " isSubtype " + arrowType18.toString(), arrowType19.isSubtype(arrowType18));
    }

    @Test
    public void testHasEqualParameters() {
        JSTypeRegistry registry = new JSTypeRegistry() {};

        Node parameters1 = registry.createParametersWithVarArgs(
                registry.createParameter("a", getNativeType(JSType.NUMBER)),
                registry.createParameter("b", getNativeType(JSType.STRING)));
        Node parameters2 = registry.createParametersWithVarArgs(
                registry.createParameter("a", getNativeType(JSType.NUMBER)),
                registry.createParameter("c", getNativeType(JSType.STRING)));
        Node parameters3 = registry.createParametersWithVarArgs(
                registry.createParameter("a", getNativeType(JSType.NUMBER)));

        ArrowType arrowType1 = new ArrowType(registry, parameters1, getNativeType(JSType.NUMBER));
        ArrowType arrowType2 = new ArrowType(registry, parameters1, getNativeType(JSType.NUMBER));
        ArrowType arrowType3 = new ArrowType(registry, parameters2, getNativeType(JSType.NUMBER));
        ArrowType arrowType4 = new ArrowType(registry, parameters3, getNativeType(JSType.NUMBER));

        ArrowType arrowType5 = new ArrowType(registry, parameters1, getNativeType(JSType.STRING));
        ArrowType arrowType6 = new ArrowType(registry, parameters2, getNativeType(JSType.STRING));
        ArrowType arrowType7 = new ArrowType(registry, parameters3, getNativeType(JSType.STRING));

        assertTrue("Expected true: " + arrowType1.toString() + " hasEqualParameters " + arrowType2.toString(), arrowType1.hasEqualParameters(arrowType2, true));
        assertFalse("Expected false: " + arrowType1.toString() + " hasEqualParameters " + arrowType3.toString(), arrowType1.hasEqualParameters(arrowType3, true));
        assertFalse("Expected false: " + arrowType1.toString() + " hasEqualParameters " + arrowType4.toString(), arrowType1.hasEqualParameters(arrowType4, true));
        assertFalse("Expected false: " + arrowType1.toString() + " hasEqualParameters " + arrowType5.toString(), arrowType1.hasEqualParameters(arrowType5, true));
        assertFalse("Expected false: " + arrowType1.toString() + " hasEqualParameters " + arrowType6.toString(), arrowType1.hasEqualParameters(arrowType6, true));
        assertFalse("Expected false: " + arrowType1.toString() + " hasEqualParameters " + arrowType7.toString(), arrowType1.hasEqualParameters(arrowType7, true));
    }

    @Test
    public void testResolve() {
        JSTypeRegistry registry = new JSTypeRegistry() {};
        ErrorReporter errorReporter = new ErrorReporter() {};

        Node parameters = registry.createParametersWithVarArgs(
                registry.createParameter("a", getNativeType(JSType.NUMBER)),
                registry.createParameter("b", getNativeType(JSType.STRING)));

        ArrowType arrowType1 = new ArrowType(registry, parameters, getNativeType(JSType.NUMBER));
        ArrowType arrowType2 = new ArrowType(registry, parameters, getNativeType(JSType.STRING));

        ArrowType resolvedArrowType1 = arrowType1.resolve(errorReporter, new StaticScope<>(registry));
        ArrowType resolvedArrowType2 = arrowType2.resolve(errorReporter, new StaticScope<>(registry));

        JSType returnType1 = resolvedArrowType1.returnType;
        JSType returnType2 = resolvedArrowType2.returnType;

        assertTrue("Expected Number type: " + returnType1.getClass().getName(), returnType1 instanceof JSType.Number);
        assertTrue("Expected String type: " + returnType2.getClass().getName(), returnType2 instanceof JSType.String);
    }
}