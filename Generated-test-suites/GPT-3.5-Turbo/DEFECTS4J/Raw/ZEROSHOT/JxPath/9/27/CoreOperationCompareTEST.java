import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationCompareTest {

    private CoreOperationCompare operationCompare;

    @Before
    public void setUp() {
        operationCompare = new CoreOperationCompare(new ExpressionMock(), new ExpressionMock());
    }

    @Test
    public void testGetPrecedence() {
        int result = operationCompare.getPrecedence();
        assertEquals(2, result);
    }

    @Test
    public void testIsSymmetric() {
        boolean result = operationCompare.isSymmetric();
        assertTrue(result);
    }

    @Test
    public void testEqual() {
        EvalContext context = new EvalContext();
        Expression left = new ExpressionMock();
        Expression right = new ExpressionMock();

        boolean result = operationCompare.equal(context, left, right);

        assertTrue(result);
    }

    @Test
    public void testContains() {
        Iterator it = new IteratorMock();
        Object value = new Object();

        boolean result = operationCompare.contains(it, value);

        assertFalse(result);
    }

    @Test
    public void testFindMatch() {
        Iterator lit = new IteratorMock();
        Iterator rit = new IteratorMock();

        boolean result = operationCompare.findMatch(lit, rit);

        assertFalse(result);
    }

    @Test
    public void testEqualObjects() {
        Object l = new Object();
        Object r = new Object();

        boolean result = operationCompare.equal(l, r);

        assertFalse(result);
    }

}