
import org.junit.Assert;
import org.junit.Test;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);

        EvalContext context = new EvalContext();
        boolean result = coreOperationCompare.equal(context, arg1, arg2);

        Assert.assertTrue(result);
    }

    @Test
    public void testContains() {
        Iterator it = new Iterator();
        Object value = new Object();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(), new Expression());

        boolean result = coreOperationCompare.contains(it, value);

        Assert.assertFalse(result);
    }

    @Test
    public void testFindMatch() {
        Iterator lit = new Iterator();
        Iterator rit = new Iterator();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(), new Expression());

        boolean result = coreOperationCompare.findMatch(lit, rit);

        Assert.assertFalse(result);
    }
    
    @Test
    public void testEqualWithNaN() {
        Object l = Double.NaN;
        Object r = Double.NaN;
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(), new Expression());

        boolean result = coreOperationCompare.equal(l, r);

        Assert.assertTrue(result);
    }
    
    @Test
    public void testEqualWithDifferentNumberTypes() {
        Object l = new Integer(5);
        Object r = new Float(5.0);
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(), new Expression());

        boolean result = coreOperationCompare.equal(l, r);

        Assert.assertTrue(result);
    }
}
