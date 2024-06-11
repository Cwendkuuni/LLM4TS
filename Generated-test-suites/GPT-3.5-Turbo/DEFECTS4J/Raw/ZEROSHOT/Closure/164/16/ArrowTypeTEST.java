import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ArrowTypeTest {
    
    ArrowType arrowType;

    @Before
    public void setUp() {
        arrowType = new ArrowType(null, null, null);
    }

    @Test
    public void testIsSubtype() {
        JSType type = new JSType(null) {
            @Override
            public boolean isSubtype(JSType other) {
                return true;
            }
        };
        assertTrue(arrowType.isSubtype(type));
    }

    @Test
    public void testHasEqualParameters() {
        ArrowType other = new ArrowType(null, null, null);
        assertTrue(arrowType.hasEqualParameters(other));
    }

    @Test
    public void testIsEquivalentTo() {
        ArrowType other = new ArrowType(null, null, null);
        assertTrue(arrowType.isEquivalentTo(other));
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

    @Test
    public void testResolveInternal() {
        arrowType.resolveInternal(null, null);
    }

    @Test
    public void testHasUnknownParamsOrReturn() {
        assertFalse(arrowType.hasUnknownParamsOrReturn());
    }

    @Test
    public void testToStringHelper() {
        assertNotNull(arrowType.toStringHelper(true));
    }

}