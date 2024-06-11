import static org.junit.Assert.*;
import org.junit.Test;

public class ArrowTypeTest {

    @Test
    public void testIsSubtype() {
        // Test case 1: returnType is subType of other returnType
        ArrowType arrowType1 = new ArrowType(registry, parameters, returnType);
        ArrowType arrowType2 = new ArrowType(registry, parameters, returnType);

        assertTrue(arrowType1.isSubtype(arrowType2));

        // Test case 2: returnType is not subType of other returnType
        ArrowType arrowType3 = new ArrowType(registry, parameters, returnType);
        ArrowType arrowType4 = new ArrowType(registry, parameters, getNativeType("string"));

        assertFalse(arrowType3.isSubtype(arrowType4));

    }

    @Test
    public void testHasEqualParameters() {
        // Test case 1: Arrow types have equal parameters
        ArrowType arrowType1 = new ArrowType(registry, parameters, returnType);
        ArrowType arrowType2 = new ArrowType(registry, parameters, returnType);

        assertTrue(arrowType1.hasEqualParameters(arrowType2));

        // Test case 2: Arrow types have different parameters
        ArrowType arrowType3 = new ArrowType(registry, parameters1, returnType1);

        assertFalse(arrowType1.hasEqualParameters(arrowType3));

    }

    @Test
    public void testIsEquivalentTo() {
        // Test case 1: Arrow types are equivalent
        ArrowType arrowType1 = new ArrowType(registry, parameters, returnType);
        ArrowType arrowType2 = new ArrowType(registry, parameters, returnType);

        assertTrue(arrowType1.isEquivalentTo(arrowType2));

        // Test case 2: Arrow types are not equivalent
        ArrowType arrowType3 = new ArrowType(registry, parameters1, returnType1);

        assertFalse(arrowType1.isEquivalentTo(arrowType3));

    }

    @Test
    public void testHashCode() {
        // Test case 1: Arrow types have same hashCode
        ArrowType arrowType1 = new ArrowType(registry, parameters, returnType);
        ArrowType arrowType2 = new ArrowType(registry, parameters, returnType);

        assertEquals(arrowType1.hashCode(), arrowType2.hashCode());

        // Test case 2: Arrow types have different hashCode
        ArrowType arrowType3 = new ArrowType(registry, parameters1, returnType1);

        assertNotEquals(arrowType1.hashCode(), arrowType3.hashCode());

    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetLeastSupertype() {
        ArrowType arrowType = new ArrowType(registry, parameters, returnType);

        arrowType.getLeastSupertype(arrowType);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetGreatestSubtype() {
        ArrowType arrowType = new ArrowType(registry, parameters, returnType);

        arrowType.getGreatestSubtype(arrowType);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testTestForEquality() {
        ArrowType arrowType = new ArrowType(registry, parameters, returnType);

        arrowType.testForEquality(arrowType);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testVisit() {
        ArrowType arrowType = new ArrowType(registry, parameters, returnType);

        arrowType.visit(visitor);
    }

    @Test
    public void testGetPossibleToBooleanOutcomes() {
        ArrowType arrowType = new ArrowType(registry, parameters, returnType);

        assertEquals(BooleanLiteralSet.TRUE, arrowType.getPossibleToBooleanOutcomes());
    }

    @Test
    public void testResolveInternal() {
        ErrorReporter errorReporter = new ErrorReporter();
        StaticScope<JSType> staticScope = new StaticScope<>();

        ArrowType arrowType = new ArrowType(registry, parameters, returnType);

        // Test case 1: returnType is resolved
        JSType resolvedReturnType = arrowType.resolveInternal(errorReporter, staticScope);

        assertFalse(arrowType.returnType.isUnknownType());

        // Test case 2: parameters are resolved
        JSType resolvedParameters = arrowType.resolveInternal(errorReporter, staticScope);

        for (Node paramNode = arrowType.parameters.getFirstChild();
           paramNode != null; paramNode = paramNode.getNext()) {
            JSType paramType = paramNode.getJSType();
            assertFalse(paramType.isUnknownType());
        }

    }

    @Test
    public void testHasUnknownParamsOrReturn() {
        // Test case 1: ArrowType has unknown parameters and return type
        ArrowType arrowType = new ArrowType(registry, parameters, returnType);

        assertTrue(arrowType.hasUnknownParamsOrReturn());

        // Test case 2: ArrowType does not have unknown parameters and return type
        ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1);

        assertFalse(arrowType1.hasUnknownParamsOrReturn());

    }

    @Test
    public void testToStringHelper() {
        // Test case 1: toStringHelper returns correct string representation
        ArrowType arrowType = new ArrowType(registry, parameters, returnType);

        assertEquals("ArrowType", arrowType.toStringHelper());

    }

}