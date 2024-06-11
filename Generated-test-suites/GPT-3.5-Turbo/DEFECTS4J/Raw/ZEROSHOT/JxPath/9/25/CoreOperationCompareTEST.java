import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = operation.equal(context, arg1, arg2);
        assertTrue(result);
    }

    @Test
    public void testContains() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);
        Iterator it = new Iterator();
        Object value = new Object();
        boolean result = operation.contains(it, value);
        assertFalse(result);
    }

    @Test
    public void testFindMatch() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);
        Iterator lit = new Iterator();
        Iterator rit = new Iterator();
        boolean result = operation.findMatch(lit, rit);
        assertFalse(result);
    }

    @Test
    public void testEqualWithPointers() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);
        EvalContext context = new EvalContext();
        Pointer l = new Pointer();
        Pointer r = new Pointer();
        Object result = operation.equal(context, l, r);
        assertTrue(result);
    }

    @Test
    public void testEqualWithBoolean() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);
        EvalContext context = new EvalContext();
        Boolean l = true;
        Boolean r = false;
        Object result = operation.equal(context, l, r);
        assertFalse(result);
    }

    @Test
    public void testEqualWithNumber() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);
        EvalContext context = new EvalContext();
        Number l = 10;
        Number r = 10.0;
        Object result = operation.equal(context, l, r);
        assertTrue(result);
    }

    @Test
    public void testEqualWithString() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);
        EvalContext context = new EvalContext();
        String l = "hello";
        String r = "world";
        Object result = operation.equal(context, l, r);
        assertFalse(result);
    }

    @Test
    public void testEqualWithNull() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);
        EvalContext context = new EvalContext();
        Object l = null;
        Object r = null;
        Object result = operation.equal(context, l, r);
        assertTrue(result);
    }

    @Test
    public void testEqualWithNonNull() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);
        EvalContext context = new EvalContext();
        Object l = new Object();
        Object r = new Object();
        Object result = operation.equal(context, l, r);
        assertFalse(result);
    }

    @Test
    public void testEqualWithNaN() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);
        EvalContext context = new EvalContext();
        Double l = Double.NaN;
        Double r = Double.NaN;
        Object result = operation.equal(context, l, r);
        assertFalse(result);
    }

}