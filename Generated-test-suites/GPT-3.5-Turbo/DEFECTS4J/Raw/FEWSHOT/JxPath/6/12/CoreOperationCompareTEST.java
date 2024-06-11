import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        CoreOperationCompare compare = new CoreOperationCompare(null, null) {
            @Override
            protected boolean equal(EvalContext context, Expression left, Expression right) {
                // custom implementation
                return false;
            }
        };

        // test case 1
        EvalContext context1 = new EvalContext(null);
        Expression left1 = new Expression();
        Expression right1 = new Expression();
        boolean result1 = compare.equal(context1, left1, right1);
        assertFalse(result1);

        // test case 2
        EvalContext context2 = new EvalContext(null);
        context2.setSingleNodePointer(new Pointer());
        Expression left2 = new SelfContext();
        Expression right2 = new SelfContext();
        boolean result2 = compare.equal(context2, left2, right2);
        assertFalse(result2);

        // test case 3
        EvalContext context3 = new EvalContext(null);
        context3.setSingleNodePointer(new Pointer());
        Expression left3 = new InitialContext();
        Expression right3 = new InitialContext();
        boolean result3 = compare.equal(context3, left3, right3);
        assertFalse(result3);

    }

    @Test
    public void testContains() {
        CoreOperationCompare compare = new CoreOperationCompare(null, null) {
            @Override
            protected boolean contains(Iterator it, Object value) {
                // custom implementation
                return false;
            }
        };

        Iterator<String> it = new Iterator<String>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public String next() {
                return null;
            }
        };

        boolean result = compare.contains(it, "test");
        assertFalse(result);

    }

    @Test
    public void testFindMatch() {
        CoreOperationCompare compare = new CoreOperationCompare(null, null) {
            @Override
            protected boolean findMatch(Iterator lit, Iterator rit) {
                // custom implementation
                return false;
            }
        };

        Iterator<String> lit = new Iterator<String>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public String next() {
                return null;
            }
        };

        Iterator<String> rit = new Iterator<String>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public String next() {
                return null;
            }
        };

        boolean result = compare.findMatch(lit, rit);
        assertFalse(result);

    }

    @Test
    public void testEqualForPointers() {
        CoreOperationCompare compare = new CoreOperationCompare(null, null) {
            @Override
            protected boolean equal(Object l, Object r) {
                // custom implementation
                return false;
            }
        };

        Pointer pointer1 = new Pointer();
        Pointer pointer2 = new Pointer();

        boolean result1 = compare.equal(pointer1, pointer2);
        assertFalse(result1);

        boolean result2 = compare.equal(pointer1, pointer1);
        assertTrue(result2);

        boolean result3 = compare.equal(null, pointer2);
        assertFalse(result3);

        boolean result4 = compare.equal(pointer1, null);
        assertFalse(result4);

    }

}