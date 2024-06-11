import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArrowTypeTest {

    private ArrowType arrowType;

    @Before
    public void setup() {
        arrowType = new ArrowType(new JSTypeRegistry(), null, null);
    }

    @Test
    public void testIsSubtype_ReturnTypeIsSubtype_ReturnsTrue() {
        ArrowType other = new ArrowType(new JSTypeRegistry(), null, arrowType.returnType);
        assertTrue(arrowType.isSubtype(other));
    }

    @Test
    public void testIsSubtype_ReturnTypeIsNotSubtype_ReturnsFalse() {
        ArrowType other = new ArrowType(new JSTypeRegistry(), null, new JSType(new JSTypeRegistry()));
        assertFalse(arrowType.isSubtype(other));
    }

    @Test
    public void testIsSubtype_ParameterTypeIsSubtype_ReturnsTrue() {
        Node parameters = new Node();
        parameters.addChildToBack(new Node(null, new JSType(new JSTypeRegistry())));
        ArrowType other = new ArrowType(new JSTypeRegistry(), parameters, null);
        assertTrue(arrowType.isSubtype(other));
    }

    @Test
    public void testIsSubtype_ParameterTypeIsNotSubtype_ReturnsFalse() {
        Node parameters = new Node();
        parameters.addChildToBack(new Node(null, new JSType(new JSTypeRegistry())));
        ArrowType other = new ArrowType(new JSTypeRegistry(), parameters, null);
        parameters.removeChildren();
        parameters.addChildToBack(new Node(null, new JSType(new JSTypeRegistry())));
        assertFalse(arrowType.isSubtype(other));
    }

    @Test
    public void testIsSubtype_VariadicParameterTypeIsSubtype_ReturnsTrue() {
        Node parameters = new Node();
        parameters.addChildToBack(new Node(null, new JSType(new JSTypeRegistry(), true)));
        ArrowType other = new ArrowType(new JSTypeRegistry(), parameters, null);
        assertTrue(arrowType.isSubtype(other));
    }

    @Test
    public void testIsSubtype_VariadicParameterTypeIsNotSubtype_ReturnsFalse() {
        Node parameters = new Node();
        parameters.addChildToBack(new Node(null, new JSType(new JSTypeRegistry(), true)));
        ArrowType other = new ArrowType(new JSTypeRegistry(), parameters, null);
        parameters.removeChildren();
        parameters.addChildToBack(new Node(null, new JSType(new JSTypeRegistry())));
        assertFalse(arrowType.isSubtype(other));
    }

    @Test
    public void testIsSubtype_MissingRequiredParameter_ReturnsFalse() {
        Node parameters = new Node();
        parameters.addChildToBack(new Node(null, null));
        ArrowType other = new ArrowType(new JSTypeRegistry(), parameters, null);
        assertFalse(arrowType.isSubtype(other));
    }

    @Test
    public void testHasEqualParameters_SameParameters_ReturnsTrue() {
        Node parameters = new Node();
        parameters.addChildToBack(new Node(null, new JSType(new JSTypeRegistry())));
        ArrowType other = new ArrowType(new JSTypeRegistry(), parameters, null);
        assertTrue(arrowType.hasEqualParameters(other));
    }

    @Test
    public void testHasEqualParameters_DifferentParameters_ReturnsFalse() {
        Node parameters1 = new Node();
        parameters1.addChildToBack(new Node(null, new JSType(new JSTypeRegistry())));

        Node parameters2 = new Node();
        parameters2.addChildToBack(new Node(null, new JSType(new JSTypeRegistry(), true)));

        ArrowType other = new ArrowType(new JSTypeRegistry(), parameters2, null);
        assertFalse(arrowType.hasEqualParameters(other));
    }

    @Test
    public void testIsEquivalentTo_SameParametersAndReturnType_ReturnsTrue() {
        ArrowType other = new ArrowType(new JSTypeRegistry(), arrowType.parameters, arrowType.returnType);
        assertTrue(arrowType.isEquivalentTo(other));
    }

    @Test
    public void testIsEquivalentTo_DifferentParameters_ReturnsFalse() {
        Node parameters = new Node();
        parameters.addChildToBack(new Node(null, new JSType(new JSTypeRegistry())));
        ArrowType other = new ArrowType(new JSTypeRegistry(), parameters, null);
        assertFalse(arrowType.isEquivalentTo(other));
    }

    @Test
    public void testIsEquivalentTo_DifferentReturnType_ReturnsFalse() {
        ArrowType other = new ArrowType(new JSTypeRegistry(), null, new JSType(new JSTypeRegistry()));
        assertFalse(arrowType.isEquivalentTo(other));
    }

    @Test
    public void testHashCode_ReturnsExpectedHashCode() {
        int expectedHashCode = arrowType.returnType.hashCode();
        Node param = arrowType.parameters.getFirstChild();
        while (param != null) {
            JSType paramType = param.getJSType();
            if (paramType != null) {
                expectedHashCode += paramType.hashCode();
            }
            param = param.getNext();
        }
        assertEquals(expectedHashCode, arrowType.hashCode());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetLeastSupertype_ThrowsUnsupportedOperationException() {
        arrowType.getLeastSupertype(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetGreatestSubtype_ThrowsUnsupportedOperationException() {
        arrowType.getGreatestSubtype(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testTestForEquality_ThrowsUnsupportedOperationException() {
        arrowType.testForEquality(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testVisit_ThrowsUnsupportedOperationException() {
        arrowType.visit(null);
    }

    @Test
    public void testGetPossibleToBooleanOutcomes_ReturnsTrue() {
        assertEquals(BooleanLiteralSet.TRUE, arrowType.getPossibleToBooleanOutcomes());
    }

    @Test
    public void testResolveInternal_ReturnsResolvedType() {
        ErrorReporter errorReporter = new ErrorReporter();
        StaticScope<JSType> scope = new StaticScope<>();
        arrowType.parameters = new Node();
        arrowType.parameters.addChildToBack(new Node(null, null));
        arrowType.returnType = null;
        arrowType.resolveInternal(errorReporter, scope);
        assertNotNull(arrowType.returnType);
        JSType paramType = arrowType.parameters.getFirstChild().getJSType();
        assertNotNull(paramType);
        assertFalse(paramType.isUnknownType());
    }

    @Test
    public void testHasUnknownParamsOrReturn_AllParametersAndReturnTypeAreKnown_ReturnsFalse() {
        arrowType.parameters = new Node();
        arrowType.parameters.addChildToBack(new Node(null, new JSType(new JSTypeRegistry())));
        assertFalse(arrowType.hasUnknownParamsOrReturn());
    }

    @Test
    public void testHasUnknownParamsOrReturn_ParameterTypeIsUnknown_ReturnsTrue() {
        arrowType.parameters = new Node();
        arrowType.parameters.addChildToBack(new Node(null, null));
        assertTrue(arrowType.hasUnknownParamsOrReturn());
    }

    @Test
    public void testHasUnknownParamsOrReturn_ReturnTypeIsUnknown_ReturnsTrue() {
        arrowType.returnType = null;
        assertTrue(arrowType.hasUnknownParamsOrReturn());
    }

    @Test
    public void testToStringHelper_ReturnsExpectedString() {
        String expectedString = super.toString();
        assertEquals(expectedString, arrowType.toStringHelper(false));
    }
}