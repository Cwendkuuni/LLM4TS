import org.junit.Test;
import static org.junit.Assert.*;

public class ArrowTypeTest {

    @Test
    public void testIsSubtype() {
        // Test when this.returnType is a subtype of that.returnType
        ArrowType arrowType1 = new ArrowType(null, null, new JSType());
        ArrowType arrowType2 = new ArrowType(null, null, new JSType());
        assertTrue(arrowType1.isSubtype(arrowType2));

        // Test when this.returnType is not a subtype of that.returnType
        ArrowType arrowType3 = new ArrowType(null, null, new JSType());
        ArrowType arrowType4 = new ArrowType(null, null, new JSType());
        assertFalse(arrowType3.isSubtype(arrowType4));

        // Test when thisParamType is a subtype of thatParamType
        ArrowType arrowType5 = new ArrowType(null, null, null);
        ArrowType arrowType6 = new ArrowType(null, null, null);
        assertTrue(arrowType5.isSubtype(arrowType6));

        // Test when thisParamType is not a subtype of thatParamType
        ArrowType arrowType7 = new ArrowType(null, null, null);
        ArrowType arrowType8 = new ArrowType(null, null, null);
        assertFalse(arrowType7.isSubtype(arrowType8));
    }

    @Test
    public void testHasEqualParameters() {
        // Test when parameter lists are equal
        ArrowType arrowType1 = new ArrowType(null, new Node(), null);
        ArrowType arrowType2 = new ArrowType(null, new Node(), null);
        assertTrue(arrowType1.hasEqualParameters(arrowType2));

        // Test when parameter lists are not equal
        ArrowType arrowType3 = new ArrowType(null, new Node(), null);
        ArrowType arrowType4 = new ArrowType(null, new Node(), null);
        assertFalse(arrowType3.hasEqualParameters(arrowType4));
    }

    @Test
    public void testIsEquivalentTo() {
        // Test when return types are equivalent and parameter lists are equal
        ArrowType arrowType1 = new ArrowType(null, null, new JSType());
        ArrowType arrowType2 = new ArrowType(null, null, new JSType());
        assertTrue(arrowType1.isEquivalentTo(arrowType2));

        // Test when return types are not equivalent
        ArrowType arrowType3 = new ArrowType(null, null, new JSType());
        ArrowType arrowType4 = new ArrowType(null, null, new JSType());
        assertFalse(arrowType3.isEquivalentTo(arrowType4));

        // Test when parameter lists are not equal
        ArrowType arrowType5 = new ArrowType(null, new Node(), null);
        ArrowType arrowType6 = new ArrowType(null, new Node(), null);
        assertFalse(arrowType5.isEquivalentTo(arrowType6));
    }

    @Test
    public void testHashCode() {
        // Test when return type, return type inferred, and parameter types are null
        ArrowType arrowType1 = new ArrowType(null, null, null);
        ArrowType arrowType2 = new ArrowType(null, null, null);
        assertEquals(arrowType1.hashCode(), arrowType2.hashCode());

        // Test when return type, return type inferred, and parameter types are not null
        ArrowType arrowType3 = new ArrowType(null, new Node(), new JSType());
        ArrowType arrowType4 = new ArrowType(null, new Node(), new JSType());
        assertEquals(arrowType3.hashCode(), arrowType4.hashCode());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetLeastSupertype() {
        ArrowType arrowType = new ArrowType(null, null, null);
        arrowType.getLeastSupertype(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetGreatestSubtype() {
        ArrowType arrowType = new ArrowType(null, null, null);
        arrowType.getGreatestSubtype(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testTestForEquality() {
        ArrowType arrowType = new ArrowType(null, null, null);
        arrowType.testForEquality(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testVisit() {
        ArrowType arrowType = new ArrowType(null, null, null);
        arrowType.visit(null);
    }

    @Test
    public void testGetPossibleToBooleanOutcomes() {
        ArrowType arrowType = new ArrowType(null, null, null);
        assertEquals(BooleanLiteralSet.TRUE, arrowType.getPossibleToBooleanOutcomes());
    }

    @Test
    public void testResolveInternal() {
        // Test when returnType and parameters are not null
        ArrowType arrowType1 = new ArrowType(null, new Node(), new JSType());
        assertNotNull(arrowType1.resolveInternal(null, null));

        // Test when returnType and parameters are null
        ArrowType arrowType2 = new ArrowType(null, null, null);
        assertNotNull(arrowType2.resolveInternal(null, null));
    }

    @Test
    public void testHasUnknownParamsOrReturn() {
        // Test when returnType and parameters are not null and do not contain an unknown type
        ArrowType arrowType1 = new ArrowType(null, new Node(), new JSType());
        assertFalse(arrowType1.hasUnknownParamsOrReturn());

        // Test when returnType and parameters are not null but contain an unknown type
        ArrowType arrowType2 = new ArrowType(null, new Node(), UNKNOWN_TYPE);
        assertTrue(arrowType2.hasUnknownParamsOrReturn());

        // Test when returnType and parameters are null
        ArrowType arrowType3 = new ArrowType(null, null, null);
        assertFalse(arrowType3.hasUnknownParamsOrReturn());

        // Test when returnType and parameters are null but contain an unknown type
        ArrowType arrowType4 = new ArrowType(null, null, UNKNOWN_TYPE);
        assertTrue(arrowType4.hasUnknownParamsOrReturn());
    }

    @Test
    public void testToStringHelper() {
        ArrowType arrowType = new ArrowType(null, null, null);
        assertEquals(super.toString(), arrowType.toStringHelper(false));
    }
}