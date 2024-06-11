import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ArrowTypeTest {

    private ArrowType arrowType;

    @Before
    public void setUp() {
        arrowType = new ArrowType(new JSTypeRegistry(), null, null);
    }

    @Test
    public void testIsSubtype() {
        assertFalse(arrowType.isSubtype(new JSType()));
    }

    @Test
    public void testHasEqualParameters() {
        ArrowType that = new ArrowType(new JSTypeRegistry(), null, null);
        assertTrue(arrowType.hasEqualParameters(that, false));
    }

    @Test
    public void testCheckArrowEquivalenceHelper() {
        ArrowType that = new ArrowType(new JSTypeRegistry(), null, null);
        assertTrue(arrowType.checkArrowEquivalenceHelper(that, false));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetLeastSupertype() {
        arrowType.getLeastSupertype(new JSType());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetGreatestSubtype() {
        arrowType.getGreatestSubtype(new JSType());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testTestForEquality() {
        arrowType.testForEquality(new JSType());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testVisit() {
        arrowType.visit(new JSType.Visitor<Object>() {
            @Override
            public Object caseArrayType(ArrayType type) {
                return null;
            }
        });
    }

    @Test
    public void testGetPossibleToBooleanOutcomes() {
        assertEquals(BooleanLiteralSet.TRUE, arrowType.getPossibleToBooleanOutcomes());
    }

    @Test
    public void testResolveInternal() {
        arrowType.resolveInternal(new ErrorReporter(), new StaticScope<JSType>());
    }

    @Test
    public void testToStringHelper() {
        assertEquals("[ArrowType]", arrowType.toStringHelper(false));
    }

    @Test
    public void testHasAnyTemplateInternal() {
        assertFalse(arrowType.hasAnyTemplateInternal());
    }

    @Test
    public void testHashCode() {
        int hashCode = 0;
        assertEquals(hashCode, arrowType.hashCode());
    }
}