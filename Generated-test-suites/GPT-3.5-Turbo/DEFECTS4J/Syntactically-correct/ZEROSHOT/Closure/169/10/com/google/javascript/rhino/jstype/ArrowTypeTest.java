import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ArrowTypeTest {

    private ArrowType arrowType;

    @Before
    public void setUp() {
        arrowType = new ArrowType(null, null, null);
    }

    @Test
    public void testIsSubtype() {
        assertFalse(arrowType.isSubtype(null));
        assertFalse(arrowType.isSubtype(new ArrowType(null, null, null)));
        assertTrue(arrowType.isSubtype(arrowType));
    }

    @Test
    public void testHasEqualParameters() {
        assertTrue(arrowType.hasEqualParameters(arrowType, false));
        assertTrue(arrowType.hasEqualParameters(null, false));
        assertFalse(arrowType.hasEqualParameters(new ArrowType(null, null, null), false));
    }

    @Test
    public void testCheckArrowEquivalenceHelper() {
        assertFalse(arrowType.checkArrowEquivalenceHelper(null, false));
        assertFalse(arrowType.checkArrowEquivalenceHelper(new ArrowType(null, null, null), false));
        assertTrue(arrowType.checkArrowEquivalenceHelper(arrowType, false));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetLeastSupertype() {
        arrowType.getLeastSupertype(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetGreatestSubtype() {
        arrowType.getGreatestSubtype(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testTestForEquality() {
        arrowType.testForEquality(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testVisit() {
        arrowType.visit(null);
    }

    @Test
    public void testGetPossibleToBooleanOutcomes() {
        assertEquals(BooleanLiteralSet.TRUE, arrowType.getPossibleToBooleanOutcomes());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testResolveInternal() {
        arrowType.resolveInternal(null, null);
    }

    @Test
    public void testHasUnknownParamsOrReturn() {
        assertFalse(arrowType.hasUnknownParamsOrReturn());
    }

    @Test
    public void testToStringHelper() {
        assertEquals("[ArrowType]", arrowType.toStringHelper(false));
    }

    @Test
    public void testHasAnyTemplateInternal() {
        assertFalse(arrowType.hasAnyTemplateInternal());
    }
}