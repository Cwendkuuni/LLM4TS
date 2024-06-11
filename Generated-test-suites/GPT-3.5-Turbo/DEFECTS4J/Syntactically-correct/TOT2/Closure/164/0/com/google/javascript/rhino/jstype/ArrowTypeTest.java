import org.junit.Test;
import static org.junit.Assert.*;

public class ArrowTypeTest {

    @Test
    public void testConstructor() {
        ArrowType arrowType = new ArrowType(null, null, null);
        assertNotNull(arrowType);
    }

    @Test
    public void testIsSubtype() {
        ArrowType arrowType1 = new ArrowType(null, null, null);
        ArrowType arrowType2 = new ArrowType(null, null, null);
        
        assertTrue(arrowType1.isSubtype(arrowType2));
    }

    @Test
    public void testHasEqualParameters() {
        ArrowType arrowType1 = new ArrowType(null, null, null);
        ArrowType arrowType2 = new ArrowType(null, null, null);

        assertTrue(arrowType1.hasEqualParameters(arrowType2));
    }

    @Test
    public void testIsEquivalentTo() {
        ArrowType arrowType1 = new ArrowType(null, null, null);
        ArrowType arrowType2 = new ArrowType(null, null, null);

        assertTrue(arrowType1.isEquivalentTo(arrowType2));
    }

    @Test
    public void testHashCode() {
        ArrowType arrowType = new ArrowType(null, null, null);
        assertNotNull(arrowType.hashCode());
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

}