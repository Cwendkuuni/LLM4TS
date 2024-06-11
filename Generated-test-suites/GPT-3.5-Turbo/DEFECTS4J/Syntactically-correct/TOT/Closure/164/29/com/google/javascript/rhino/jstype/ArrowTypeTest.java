import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ArrowTypeTest {

    private ArrowType arrowType;

    @Before
    public void setUp() {
        arrowType = new ArrowType(new JSTypeRegistry(), null, null);
    }

    @Test
    public void testIsSubtype_WhenOtherIsNotArrowType_ReturnsFalse() {
        JSType other = new JSType(new JSTypeRegistry());
        assertFalse(arrowType.isSubtype(other));
    }

    @Test
    public void testIsSubtype_WhenReturnTypesAreNotSubtypes_ReturnsFalse() {
        JSType returnType = new JSType(new JSTypeRegistry());
        arrowType.returnType = returnType;

        JSType other = new ArrowType(new JSTypeRegistry(), null, null);
        JSType otherReturnType = new JSType(new JSTypeRegistry());
        other.returnType = otherReturnType;

        assertFalse(arrowType.isSubtype(other));
    }

    @Test
    public void testIsSubtype_WhenParameterTypesAreNotSubtypes_ReturnsFalse() {
        Node parameters = new Node();
        JSType parameterType = new JSType(new JSTypeRegistry());
        parameters.setJSType(parameterType);
        arrowType.parameters = parameters;

        JSType other = new ArrowType(new JSTypeRegistry(), null, null);
        Node otherParameters = new Node();
        JSType otherParameterType = new JSType(new JSTypeRegistry());
        otherParameters.setJSType(otherParameterType);
        ((ArrowType) other).parameters = otherParameters;

        assertFalse(arrowType.isSubtype(other));
    }

    @Test
    public void testIsSubtype_WhenOtherParamTypeIsRequiredAndParamTypeNullable_ReturnsFalse() {
        Node parameters = new Node();
        JSType parameterType = new JSType(new JSTypeRegistry());
        parameters.setJSType(parameterType);
        parameters.setHasNullable(true);
        arrowType.parameters = parameters;

        JSType other = new ArrowType(new JSTypeRegistry(), null, null);
        Node otherParameters = new Node();
        JSType otherParameterType = new JSType(new JSTypeRegistry());
        otherParameters.setJSType(otherParameterType);
        otherParameters.setHasNullable(false);
        ((ArrowType) other).parameters = otherParameters;

        assertFalse(arrowType.isSubtype(other));
    }

    @Test
    public void testIsEquivalentTo_WhenReturnTypesAreNotEquivalent_ReturnsFalse() {
        JSType returnType = new JSType(new JSTypeRegistry());
        arrowType.returnType = returnType;

        JSType other = new ArrowType(new JSTypeRegistry(), null, null);
        JSType otherReturnType = new JSType(new JSTypeRegistry());
        other.returnType = otherReturnType;

        assertFalse(arrowType.isEquivalentTo(other));
    }

    @Test
    public void testIsEquivalentTo_WhenParameterTypesAreNotEquivalent_ReturnsFalse() {
        Node parameters = new Node();
        JSType parameterType = new JSType(new JSTypeRegistry());
        parameters.setJSType(parameterType);
        arrowType.parameters = parameters;

        JSType other = new ArrowType(new JSTypeRegistry(), null, null);
        Node otherParameters = new Node();
        JSType otherParameterType = new JSType(new JSTypeRegistry());
        otherParameters.setJSType(otherParameterType);
        ((ArrowType) other).parameters = otherParameters;

        assertFalse(arrowType.isEquivalentTo(other));
    }

    @Test
    public void testHashCode_WhenReturnTypesAndParametersHaveDifferentHashCodes_ReturnsExpectedHashCode() {
        JSType returnType = new JSType(new JSTypeRegistry());
        arrowType.returnType = returnType;

        Node parameters = new Node();
        JSType parameterType = new JSType(new JSTypeRegistry());
        parameters.setJSType(parameterType);
        arrowType.parameters = parameters;

        int expectedHashCode = returnType.hashCode() + parameterType.hashCode();
        assertEquals(expectedHashCode, arrowType.hashCode());
    }

    @Test
    public void testGetPossibleToBooleanOutcomes_ReturnsTrue() {
        BooleanLiteralSet expectedOutcomes = BooleanLiteralSet.TRUE;
        assertEquals(expectedOutcomes, arrowType.getPossibleToBooleanOutcomes());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetLeastSupertype_ThrowsUnsupportedOperationException() {
        JSType otherType = new JSType(new JSTypeRegistry());
        arrowType.getLeastSupertype(otherType);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetGreatestSubtype_ThrowsUnsupportedOperationException() {
        JSType otherType = new JSType(new JSTypeRegistry());
        arrowType.getGreatestSubtype(otherType);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testTestForEquality_ThrowsUnsupportedOperationException() {
        JSType otherType = new JSType(new JSTypeRegistry());
        arrowType.testForEquality(otherType);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testVisit_ThrowsUnsupportedOperationException() {
        Visitor<String> visitor = new Visitor<String>() {
            @Override
            public String visit(JSType type) {
                return "Visiting JSType";
            }

            @Override
            public String visit(ArrowType type) {
                return "Visiting ArrowType";
            }
        };
        arrowType.visit(visitor);
    }

    @Test
    public void testResolveInternal_WhenParametersAndReturnTypeAreNotNull_ReturnsSelf() {
        ErrorReporter errorReporter = new ErrorReporter();
        StaticScope<JSType> scope = new StaticScope<>();
        JSType resolvedType = arrowType.resolveInternal(errorReporter, scope);
        assertSame(arrowType, resolvedType);
    }

    @Test
    public void testHasUnknownParamsOrReturn_WhenParametersAndReturnTypeAreNotNull_ReturnsFalse() {
        assertFalse(arrowType.hasUnknownParamsOrReturn());
    }

    @Test
    public void testToStringHelper_WhenForAnnotationsIsTrue_ReturnsSuperToString() {
        boolean forAnnotations = true;
        String expectedString = arrowType.toString();
        assertEquals(expectedString, arrowType.toStringHelper(forAnnotations));
    }

    @Test
    public void testToStringHelper_WhenForAnnotationsIsFalse_ReturnsSuperToString() {
        boolean forAnnotations = false;
        String expectedString = arrowType.toString();
        assertEquals(expectedString, arrowType.toStringHelper(forAnnotations));
    }

}