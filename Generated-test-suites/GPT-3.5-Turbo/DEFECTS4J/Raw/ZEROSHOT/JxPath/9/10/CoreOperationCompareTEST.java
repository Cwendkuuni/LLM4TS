import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationCompareTest {

    private CoreOperationCompare coreOperationCompare;

    @Before
    public void setUp() {
        coreOperationCompare = new CoreOperationCompare(null, null);
    }

    @Test
    public void testGetPrecedence() {
        int precedence = coreOperationCompare.getPrecedence();
        assertEquals(2, precedence);
    }

    @Test
    public void testIsSymmetric() {
        boolean isSymmetric = coreOperationCompare.isSymmetric();
        assertTrue(isSymmetric);
    }

    @Test
    public void testEqual() {
        EvalContext context = new EvalContext(null);
        Expression left = new Expression();
        Expression right = new Expression();

        boolean equal = coreOperationCompare.equal(context, left, right);
        assertFalse(equal);
    }

    @Test
    public void testContains() {
        Iterator iterator = new Iterator() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object next() {
                return null;
            }
        };

        boolean contains = coreOperationCompare.contains(iterator, null);
        assertFalse(contains);
    }

    @Test
    public void testFindMatch() {
        Iterator leftIterator = new Iterator() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object next() {
                return null;
            }
        };

        Iterator rightIterator = new Iterator() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object next() {
                return null;
            }
        };

        boolean match = coreOperationCompare.findMatch(leftIterator, rightIterator);
        assertFalse(match);
    }

    @Test
    public void testEqual() {
        Object l = null;
        Object r = null;

        boolean eq = coreOperationCompare.equal(l, r);
        assertTrue(eq);
    }

}