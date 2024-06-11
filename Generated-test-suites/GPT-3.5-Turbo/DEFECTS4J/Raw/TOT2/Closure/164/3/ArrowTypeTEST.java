import static org.junit.Assert.*;
import org.junit.Test;

public class ArrowTypeTest {

    @Test
    public void testArrowTypeConstructor() {
        ArrowType arrowType = new ArrowType(null, null, null);
        assertNotNull(arrowType);
    }

    @Test
    public void testArrowTypeIsSubtype() {
        ArrowType arrowType1 = new ArrowType(null, null, null);
        ArrowType arrowType2 = new ArrowType(null, null, null);
        assertTrue(arrowType1.isSubtype(arrowType2));
    }

    @Test
    public void testArrowTypeHasEqualParameters() {
        ArrowType arrowType1 = new ArrowType(null, null, null);
        ArrowType arrowType2 = new ArrowType(null, null, null);
        assertTrue(arrowType1.hasEqualParameters(arrowType2));
    }

    @Test
    public void testArrowTypeIsEquivalentTo() {
        ArrowType arrowType1 = new ArrowType(null, null, null);
        ArrowType arrowType2 = new ArrowType(null, null, null);
        assertTrue(arrowType1.isEquivalentTo(arrowType2));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testArrowTypeGetLeastSupertype() {
        ArrowType arrowType = new ArrowType(null, null, null);
        arrowType.getLeastSupertype(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testArrowTypeGetGreatestSubtype() {
        ArrowType arrowType = new ArrowType(null, null, null);
        arrowType.getGreatestSubtype(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testArrowTypeTestForEquality() {
        ArrowType arrowType = new ArrowType(null, null, null);
        arrowType.testForEquality(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testArrowTypeVisit() {
        ArrowType arrowType = new ArrowType(null, null, null);
        arrowType.visit(null);
    }

    @Test
    public void testArrowTypeResolveInternal() {
        ArrowType arrowType = new ArrowType(null, null, null);
        arrowType.resolveInternal(null, null);
        assertFalse(arrowType.hasUnknownParamsOrReturn());
    }

    @Test
    public void testArrowTypeHashCode() {
        ArrowType arrowType = new ArrowType(null, null, null);
        assertNotNull(arrowType.hashCode());
    }

    @Test
    public void testArrowTypeToStringHelper() {
        ArrowType arrowType = new ArrowType(null, null, null);
        assertNotNull(arrowType.toStringHelper(false));
    }
}