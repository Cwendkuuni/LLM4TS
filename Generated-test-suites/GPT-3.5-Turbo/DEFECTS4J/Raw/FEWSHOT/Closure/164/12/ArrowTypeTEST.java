import static org.junit.Assert.*;

import org.junit.Test;

public class ArrowTypeTest {
    @Test
    public void testIsSubtype() {
        ArrowType arrowType1 = new ArrowType(null, null, null);
        assertFalse(arrowType1.isSubtype(null));

        ArrowType arrowType2 = new ArrowType(null, null, null);
        assertFalse(arrowType1.isSubtype(arrowType2));

        ArrowType arrowType3 = new ArrowType(null, null, null);
        assertFalse(arrowType2.isSubtype(arrowType3));
    }

    @Test
    public void testHasEqualParameters() {
        ArrowType arrowType1 = new ArrowType(null, null, null);
        assertFalse(arrowType1.hasEqualParameters(null));

        ArrowType arrowType2 = new ArrowType(null, null, null);
        assertTrue(arrowType1.hasEqualParameters(arrowType2));

        ArrowType arrowType3 = new ArrowType(null, null, null);
        assertTrue(arrowType2.hasEqualParameters(arrowType3));
    }

    @Test
    public void testIsEquivalentTo() {
        ArrowType arrowType1 = new ArrowType(null, null, null);
        assertFalse(arrowType1.isEquivalentTo(null));

        ArrowType arrowType2 = new ArrowType(null, null, null);
        assertTrue(arrowType1.isEquivalentTo(arrowType2));

        ArrowType arrowType3 = new ArrowType(null, null, null);
        assertTrue(arrowType2.isEquivalentTo(arrowType3));
    }

    @Test
    public void testHashCode() {
        ArrowType arrowType1 = new ArrowType(null, null, null);
        assertEquals(arrowType1.hashCode(), arrowType1.hashCode());

        ArrowType arrowType2 = new ArrowType(null, null, null);
        assertEquals(arrowType2.hashCode(), arrowType1.hashCode());

        ArrowType arrowType3 = new ArrowType(null, null, null);
        assertEquals(arrowType3.hashCode(), arrowType2.hashCode());
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
        ArrowType arrowType = new ArrowType(null, null, null);
        assertNull(arrowType.resolveInternal(null, null));
    }

    @Test
    public void testHasUnknownParamsOrReturn() {
        ArrowType arrowType = new ArrowType(null, null, null);
        assertFalse(arrowType.hasUnknownParamsOrReturn());
    }

    @Test
    public void testToStringHelper() {
        ArrowType arrowType = new ArrowType(null, null, null);
        assertNotNull(arrowType.toStringHelper(true));
        assertNotNull(arrowType.toStringHelper(false));
    }
}