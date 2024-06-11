import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = coreOperationCompare.equal(context, arg1, arg2);

        assertTrue(result);
    }

    @Test
    public void testContains() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);

        Iterator it = new Iterator() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object next() {
                return null;
            }
        };

        Object value = null;

        boolean result = coreOperationCompare.contains(it, value);

        assertFalse(result);
    }

    @Test
    public void testFindMatch() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);

        Iterator lit = new Iterator() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object next() {
                return null;
            }
        };

        Iterator rit = new Iterator() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object next() {
                return null;
            }
        };

        boolean result = coreOperationCompare.findMatch(lit, rit);

        assertFalse(result);
    }

    @Test
    public void testEqualWithValuePointer() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);

        Object l = new Pointer();
        Object r = new Pointer();

        boolean result = coreOperationCompare.equal(l, r);

        assertFalse(result);
    }

    @Test
    public void testEqualWithValueBoolean() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);

        Object l = true;
        Object r = true;

        boolean result = coreOperationCompare.equal(l, r);

        assertTrue(result);
    }

    @Test
    public void testEqualWithValueNumber() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);

        Object l = 10;
        Object r = 10;

        boolean result = coreOperationCompare.equal(l, r);

        assertTrue(result);
    }

    @Test
    public void testEqualWithValueString() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);

        Object l = "foo";
        Object r = "foo";

        boolean result = coreOperationCompare.equal(l, r);

        assertTrue(result);
    }
    
    @Test
    public void testEqualWithValueNull() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);

        Object l = null;
        Object r = null;

        boolean result = coreOperationCompare.equal(l, r);

        assertTrue(result);
    }

}