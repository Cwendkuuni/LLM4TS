import static org.junit.Assert.*;
import org.junit.Test;

public class ArrowTypeTest {

    @Test
    public void testArrowType() {
        ArrowType arrowType = new ArrowType(null, null, null);
        assertNotNull(arrowType);
    }

    @Test
    public void testIsSubtype() {
        ArrowType arrowType1 = new ArrowType(null, null, null);
        ArrowType arrowType2 = new ArrowType(null, null, null);
        assertTrue(arrowType1.isSubtype(arrowType1));
        assertFalse(arrowType1.isSubtype(arrowType2));
    }

    @Test
    public void testHasEqualParameters() {
        ArrowType arrowType1 = new ArrowType(null, null, null);
        ArrowType arrowType2 = new ArrowType(null, null, null);
        assertTrue(arrowType1.hasEqualParameters(arrowType1));
        assertFalse(arrowType1.hasEqualParameters(arrowType2));
    }

    @Test
    public void testIsEquivalentTo() {
        ArrowType arrowType1 = new ArrowType(null, null, null);
        ArrowType arrowType2 = new ArrowType(null, null, null);
        assertTrue(arrowType1.isEquivalentTo(arrowType1));
        assertFalse(arrowType1.isEquivalentTo(arrowType2));
    }

    @Test
    public void testHashCode() {
        ArrowType arrowType = new ArrowType(null, null, null);
        int hashCode = arrowType.hashCode();
        assertEquals(hashCode, arrowType.hashCode());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetLeastSupertype() {
        ArrowType arrowType1 = new ArrowType(null, null, null);
        ArrowType arrowType2 = new ArrowType(null, null, null);
        arrowType1.getLeastSupertype(arrowType2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetGreatestSubtype() {
        ArrowType arrowType1 = new ArrowType(null, null, null);
        ArrowType arrowType2 = new ArrowType(null, null, null);
        arrowType1.getGreatestSubtype(arrowType2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testTestForEquality() {
        ArrowType arrowType1 = new ArrowType(null, null, null);
        ArrowType arrowType2 = new ArrowType(null, null, null);
        arrowType1.testForEquality(arrowType2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testVisit() {
        ArrowType arrowType = new ArrowType(null, null, null);
        arrowType.visit(null);
    }

    @Test
    public void testGetPossibleToBooleanOutcomes() {
        ArrowType arrowType = new ArrowType(null, null, null);
        BooleanLiteralSet booleanLiteralSet = arrowType.getPossibleToBooleanOutcomes();
        assertNotNull(booleanLiteralSet);
    }

    @Test
    public void testResolveInternal() {
        ArrowType arrowType = new ArrowType(null, null, null);
        ArrowType resolvedType = arrowType.resolveInternal(null, null);
        assertEquals(arrowType, resolvedType);
    }

    @Test
    public void testHasUnknownParamsOrReturn() {
        ArrowType arrowType = new ArrowType(null, null, null);
        boolean hasUnknownParamsOrReturn = arrowType.hasUnknownParamsOrReturn();
        assertFalse(hasUnknownParamsOrReturn);
    }

    @Test
    public void testToStringHelper() {
        ArrowType arrowType = new ArrowType(null, null, null);
        String string = arrowType.toStringHelper(false);
        assertNotNull(string);
    }

}