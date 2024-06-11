import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import static org.junit.Assert.*;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        Expression arg1 = new ExpressionMock();
        Expression arg2 = new ExpressionMock();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);

        EvalContext context = new EvalContext();
        boolean result = coreOperationCompare.equal(context, arg1, arg2);

        assertFalse(result);
    }

    @Test
    public void testContains() {
        Iterator it = new IteratorMock();
        Object value = new Object();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);

        boolean result = coreOperationCompare.contains(it, value);

        assertFalse(result);
    }

    @Test
    public void testFindMatch() {
        Iterator lit = new IteratorMock();
        Iterator rit = new IteratorMock();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);

        boolean result = coreOperationCompare.findMatch(lit, rit);

        assertFalse(result);
    }

    @Test
    public void testEqualWithPointers() {
        Pointer l = new PointerMock();
        Pointer r = new PointerMock();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);

        boolean result = coreOperationCompare.equal(l, r);

        assertTrue(result);
    }

    @Test
    public void testEqualWithBoolean() {
        Boolean l = true;
        Boolean r = false;
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);

        boolean result = coreOperationCompare.equal(l, r);

        assertFalse(result);
    }

    @Test
    public void testEqualWithNumber() {
        Number l = 10;
        Number r = 5;
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);

        boolean result = coreOperationCompare.equal(l, r);

        assertFalse(result);
    }

    @Test
    public void testEqualWithString() {
        String l = "Hello";
        String r = "World";
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);

        boolean result = coreOperationCompare.equal(l, r);

        assertFalse(result);
    }

    @Test
    public void testEqualWithNull() {
        Object l = null;
        Object r = null;
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);

        boolean result = coreOperationCompare.equal(l, r);

        assertTrue(result);
    }
}