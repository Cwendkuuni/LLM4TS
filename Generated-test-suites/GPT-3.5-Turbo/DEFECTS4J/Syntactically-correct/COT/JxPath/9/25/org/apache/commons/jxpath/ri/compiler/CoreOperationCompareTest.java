import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationCompare op = new CoreOperationCompare(arg1, arg2);
        EvalContext context = new EvalContext();
        assertTrue(op.equal(context, arg1, arg2));
    }

    @Test
    public void testContains() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationCompare op = new CoreOperationCompare(arg1, arg2);
        Iterator it = new Iterator();
        Object value = new Object();
        assertFalse(op.contains(it, value));
    }

    @Test
    public void testFindMatch() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationCompare op = new CoreOperationCompare(arg1, arg2);
        Iterator lit = new Iterator();
        Iterator rit = new Iterator();
        assertFalse(op.findMatch(lit, rit));
    }

    @Test
    public void testEqualPointer() {
        CoreOperationCompare op = new CoreOperationCompare();
        Object l = new Pointer();
        Object r = new Pointer();
        assertTrue(op.equal(l, r));
    }

    @Test
    public void testEqualBoolean() {
        CoreOperationCompare op = new CoreOperationCompare();
        Object l = true;
        Object r = false;
        assertFalse(op.equal(l, r));
    }

    @Test
    public void testEqualNumber() {
        CoreOperationCompare op = new CoreOperationCompare();
        Object l = 10;
        Object r = 10.0;
        assertTrue(op.equal(l, r));
    }

    @Test
    public void testEqualString() {
        CoreOperationCompare op = new CoreOperationCompare();
        Object l = "hello";
        Object r = "world";
        assertFalse(op.equal(l, r));
    }

}