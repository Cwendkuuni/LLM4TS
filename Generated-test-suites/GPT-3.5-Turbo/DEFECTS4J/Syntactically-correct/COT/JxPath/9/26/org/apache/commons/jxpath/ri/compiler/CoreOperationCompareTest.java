import org.junit.Assert;
import org.junit.Test;

public class CoreOperationCompareTest {

    @Test
    public void testGetPrecedence() {
        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        int precedence = operation.getPrecedence();
        // TODO: write test case
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        boolean isSymmetric = operation.isSymmetric();
        // TODO: write test case
    }

    @Test
    public void testEqual() {
        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        EvalContext context = null;
        Expression left = null;
        Expression right = null;
        boolean isEqual = operation.equal(context, left, right);
        // TODO: write test case
    }

    @Test
    public void testContains() {
        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        Iterator it = null;
        Object value = null;
        boolean containsValue = operation.contains(it, value);
        // TODO: write test case
    }

    @Test
    public void testFindMatch() {
        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        Iterator lit = null;
        Iterator rit = null;
        boolean hasMatch = operation.findMatch(lit, rit);
        // TODO: write test case
    }

    @Test
    public void testEqualObjects() {
        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        Object l = null;
        Object r = null;
        boolean isEqual = operation.equal(l, r);
        // TODO: write test case
    }
}