
import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        CoreOperationCompare operation = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Expression left = new Expression();
        Expression right = new Expression();
        boolean result = operation.equal(context, left, right);
        // Add assertions here
    }

    @Test
    public void testContains() {
        CoreOperationCompare operation = new CoreOperationContains(arg1, arg2);
        Iterator iterator = new Iterator();
        Object value = new Object();
        boolean result = operation.contains(iterator, value);
        // Add assertions here
    }

    @Test
    public void testFindMatch() {
        CoreOperationCompare operation = new CoreOperationFindMatch(arg1, arg2);
        Iterator lit = new Iterator();
        Iterator rit = new Iterator();
        boolean result = operation.findMatch(lit, rit);
        // Add assertions here
    }

    @Test
    public void testEqualValues() {
        CoreOperationCompare operation = new CoreOperationCompare();
        Object l = new Object();
        Object r = new Object();
        boolean result = operation.equal(l, r);
        // Add assertions here
    }
}
