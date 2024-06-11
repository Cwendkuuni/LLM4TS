import org.junit.BeforeClass;
import org.junit.Test;

public class ArrowTypeTest {

    private static ArrowType arrowType;

    @BeforeClass
    public static void setUp() {
        Node parameters = new Node(Node.DUPLICATE_NAME, "parameters");
        JSType returnType = new JSType(Node.TYPE_NAME, "returnType");
        arrowType = new ArrowType(parameters, returnType);
    }

    @Test
    public void testIsSubtype_WhenOtherIsArrowTypeWithEqualReturnTypeAndParameters_ShouldReturnTrue() {
        Node parameters = new Node(Node.DUPLICATE_NAME, "parameters");
        JSType returnType = new JSType(Node.TYPE_NAME, "returnType");
        ArrowType other = new ArrowType(parameters, returnType);
        assert(arrowType.isSubtype(other) == true);
    }

    @Test
    public void testIsSubtype_WhenOtherIsArrowTypeWithDifferentReturnTypeAndEqualParameters_ShouldReturnFalse() {
        Node parameters = new Node(Node.DUPLICATE_NAME, "parameters");
        JSType returnType = new JSType(Node.TYPE_NAME, "otherReturnType");
        ArrowType other = new ArrowType(parameters, returnType);
        assert(arrowType.isSubtype(other) == false);
    }

    @Test
    public void testIsSubtype_WhenOtherIsArrowTypeWithEqualReturnTypeAndDifferentParameters_ShouldReturnFalse() {
        Node parameters = new Node(Node.DUPLICATE_NAME, "otherParameters");
        JSType returnType = new JSType(Node.TYPE_NAME, "returnType");
        ArrowType other = new ArrowType(parameters, returnType);
        assert(arrowType.isSubtype(other) == false);
    }

    @Test
    public void testIsSubtype_WhenOtherIsNotArrowType_ShouldReturnFalse() {
        JSType other = new JSType(Node.TYPE_NAME, "otherType");
        assert(arrowType.isSubtype(other) == false);
    }

    @Test
    public void testHasEqualParameters_WhenOtherHasEqualParameters_ShouldReturnTrue() {
        Node parameters = new Node(Node.DUPLICATE_NAME, "parameters");
        JSType returnType = new JSType(Node.TYPE_NAME, "returnType");
        ArrowType other = new ArrowType(parameters, returnType);
        assert(arrowType.hasEqualParameters(other));
    }

    @Test
    public void testHasEqualParameters_WhenOtherHasDifferentParameters_ShouldReturnFalse() {
        Node parameters = new Node(Node.DUPLICATE_NAME, "otherParameters");
        JSType returnType = new JSType(Node.TYPE_NAME, "returnType");
        ArrowType other = new ArrowType(parameters, returnType);
        assert(!arrowType.hasEqualParameters(other));
    }

    @Test
    public void testHasEqualParameters_WhenOtherHasNullParameters_ShouldReturnFalse() {
        JSType returnType = new JSType(Node.TYPE_NAME, "returnType");
        ArrowType other = new ArrowType(null, returnType);
        assert(!arrowType.hasEqualParameters(other));
    }

    @Test
    public void testIsEquivalentTo_WhenOtherIsArrowTypeWithEqualReturnTypeAndParameters_ShouldReturnTrue() {
        Node parameters = new Node(Node.DUPLICATE_NAME, "parameters");
        JSType returnType = new JSType(Node.TYPE_NAME, "returnType");
        ArrowType other = new ArrowType(parameters, returnType);
        assert(arrowType.isEquivalentTo(other));
    }

    @Test
    public void testIsEquivalentTo_WhenOtherIsArrowTypeWithDifferentReturnTypeAndEqualParameters_ShouldReturnFalse() {
        Node parameters = new Node(Node.DUPLICATE_NAME, "parameters");
        JSType returnType = new JSType(Node.TYPE_NAME, "otherReturnType");
        ArrowType other = new ArrowType(parameters, returnType);
        assert(!arrowType.isEquivalentTo(other));
    }

    @Test
    public void testIsEquivalentTo_WhenOtherIsArrowTypeWithEqualReturnTypeAndDifferentParameters_ShouldReturnFalse() {
        Node parameters = new Node(Node.DUPLICATE_NAME, "otherParameters");
        JSType returnType = new JSType(Node.TYPE_NAME, "returnType");
        ArrowType other = new ArrowType(parameters, returnType);
        assert(!arrowType.isEquivalentTo(other));
    }

    @Test
    public void testIsEquivalentTo_WhenOtherIsNotArrowType_ShouldReturnFalse() {
        JSType other = new JSType(Node.TYPE_NAME, "otherType");
        assert(!arrowType.isEquivalentTo(other));
    }

    @Test
    public void testHashCode_WhenObjectsAreEqual_ShouldReturnSameHashCode() {
        Node parameters = new Node(Node.DUPLICATE_NAME, "parameters");
        JSType returnType = new JSType(Node.TYPE_NAME, "returnType");
        ArrowType other = new ArrowType(parameters, returnType);
        assert(arrowType.hashCode() == other.hashCode());
    }

    @Test
    public void testHashCode_WhenObjectsAreNotEqual_ShouldReturnDifferentHashCode() {
        Node parameters = new Node(Node.DUPLICATE_NAME, "parameters");
        JSType returnType = new JSType(Node.TYPE_NAME, "otherReturnType");
        ArrowType other = new ArrowType(parameters, returnType);
        assert(arrowType.hashCode() != other.hashCode());
    }

    @Test
    public void testGetLeastSupertype_ShouldThrowUnsupportedOperationException() {
        ArrowType other = new ArrowType(null, null);
        try {
            arrowType.getLeastSupertype(other);
        } catch(UnsupportedOperationException e) {
            assert(e.getMessage().equals("Unsupported operation: getLeastSupertype"));
        }
    }
  
    @Test
    public void testGetGreatestSubtype_ShouldThrowUnsupportedOperationException() {
        ArrowType other = new ArrowType(null, null);
        try {
            arrowType.getGreatestSubtype(other);
        } catch(UnsupportedOperationException e) {
            assert(e.getMessage().equals("Unsupported operation: getGreatestSubtype"));
        }
    }

    @Test
    public void testTestForEquality_ShouldThrowUnsupportedOperationException() {
        JSType other = new JSType(Node.TYPE_NAME, "otherType");
        try {
            arrowType.testForEquality(other);
        } catch(UnsupportedOperationException e) {
            assert(e.getMessage().equals("Unsupported operation: testForEquality"));
        }
    }

    @Test
    public void testVisit_ShouldThrowUnsupportedOperationException() {
        ArrowType other = new ArrowType(null, null);
        try {
            other.visit(null);
        } catch(UnsupportedOperationException e) {
            assert(e.getMessage().equals("Unsupported operation: visit"));
        }
    }

    @Test
    public void testGetPossibleToBooleanOutcomes_ShouldReturnTrue() {
        BooleanLiteralSet expected = BooleanLiteralSet.TRUE;
        assert(arrowType.getPossibleToBooleanOutcomes().equals(expected));
    }

    @Test
    public void testResolveInternal_WhenCalled_ShouldResolveInternalObjects() {
        ErrorReporter errorReporter = new ErrorReporter();
        StaticScope<JSType> scope = new StaticScope<>();
        arrowType.resolveInternal(errorReporter, scope);

        assert(arrowType.parameters.getJSType() != null);
        assert(arrowType.returnType.getJSType() != null);
    }

    @Test
    public void testHasUnknownParamsOrReturn_WhenParametersAndReturnTypeAreNotUnknown_ShouldReturnFalse() {
        assert(!arrowType.hasUnknownParamsOrReturn());
    }

    @Test
    public void testHasUnknownParamsOrReturn_WhenParametersIsUnknown_ShouldReturnTrue() {
        arrowType.parameters.setJSType(new JSType(Node.TYPE_NAME, "unknownType"));
        assert(arrowType.hasUnknownParamsOrReturn());
    }

    @Test
    public void testHasUnknownParamsOrReturn_WhenReturnTypeIsUnknown_ShouldReturnTrue() {
        arrowType.returnType.setJSType(new JSType(Node.TYPE_NAME, "unknownType"));
        assert(arrowType.hasUnknownParamsOrReturn());
    }
}