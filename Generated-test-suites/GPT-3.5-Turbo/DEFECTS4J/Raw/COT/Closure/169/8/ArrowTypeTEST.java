import static org.junit.Assert.*;
import org.junit.Test;


public class ArrowTypeTest {

    @Test
    public void testIsSubtype() {
        ArrowType type1 = new ArrowType(null, null, null);
        ArrowType type2 = new ArrowType(null, null, null);
        assertTrue(type1.isSubtype(type2));
    }

    @Test
    public void testHasEqualParameters() {
        ArrowType type1 = new ArrowType(null, null, null);
        ArrowType type2 = new ArrowType(null, null, null);
        assertTrue(type1.hasEqualParameters(type2, true));
    }

    @Test
    public void testCheckArrowEquivalenceHelper() {
        ArrowType type1 = new ArrowType(null, null, null);
        ArrowType type2 = new ArrowType(null, null, null);
        assertTrue(type1.checkArrowEquivalenceHelper(type2, true));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testHashCode() {
        ArrowType type = new ArrowType(null, null, null);
        type.hashCode();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetLeastSupertype() {
        ArrowType type1 = new ArrowType(null, null, null);
        ArrowType type2 = new ArrowType(null, null, null);
        type1.getLeastSupertype(type2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetGreatestSubtype() {
        ArrowType type1 = new ArrowType(null, null, null);
        ArrowType type2 = new ArrowType(null, null, null);
        type1.getGreatestSubtype(type2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testTestForEquality() {
        ArrowType type1 = new ArrowType(null, null, null);
        ArrowType type2 = new ArrowType(null, null, null);
        type1.testForEquality(type2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testVisit() {
        ArrowType type = new ArrowType(null, null, null);
        type.visit(null);
    }

    @Test
    public void testGetPossibleToBooleanOutcomes() {
        ArrowType type = new ArrowType(null, null, null);
        assertNotNull(type.getPossibleToBooleanOutcomes());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testResolveInternal() {
        ArrowType type = new ArrowType(null, null, null);
        type.resolveInternal(null, null);
    }

    @Test
    public void testHasUnknownParamsOrReturn() {
        ArrowType type = new ArrowType(null, null, null);
        assertFalse(type.hasUnknownParamsOrReturn());
    }

    @Test
    public void testToStringHelper() {
        ArrowType type = new ArrowType(null, null, null);
        assertNotNull(type.toStringHelper(true));
    }

    @Test
    public void testHasAnyTemplateInternal() {
        ArrowType type = new ArrowType(null, null, null);
        assertFalse(type.hasAnyTemplateInternal());
    }

}