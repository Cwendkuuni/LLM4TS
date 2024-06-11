import org.junit.Test;
import org.junit.Assert;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        Expression left = new Expression();
        Expression right = new Expression();
        CoreOperationCompare operationCompare = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext();
        boolean result = operationCompare.equal(context, left, right);
        Assert.assertTrue(result);
    }

    @Test
    public void testContains() {
        Iterator it = new Iterator();
        Object value = new Object();
        CoreOperationCompare operationCompare = new CoreOperationCompare(new Expression(), new Expression());
        boolean result = operationCompare.contains(it, value);
        Assert.assertFalse(result);
    }

    @Test
    public void testFindMatch() {
        Iterator lit = new Iterator();
        Iterator rit = new Iterator();
        CoreOperationCompare operationCompare = new CoreOperationCompare(new Expression(), new Expression());
        boolean result = operationCompare.findMatch(lit, rit);
        Assert.assertFalse(result);
    }

    @Test
    public void testEqual() {
        Object l = new Object();
        Object r = new Object();
        CoreOperationCompare operationCompare = new CoreOperationCompare(new Expression(), new Expression());
        boolean result = operationCompare.equal(l, r);
        Assert.assertTrue(result);
    }

}