import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationCompare operation = new CoreOperationCompareImpl(arg1, arg2);

        EvalContext context = new EvalContext();
        // Test case 1: l and r are null
        boolean result = operation.equal(context, null, null);
        assertTrue(result);

        // Test case 2: l and r are the same object
        Object object = new Object();
        result = operation.equal(context, object, object);
        assertTrue(result);

        // Test case 3: l and r are different objects
        Object object1 = new Object();
        Object object2 = new Object();
        result = operation.equal(context, object1, object2);
        assertFalse(result);

        // add more test cases...
    }

    // add more test methods for other public methods in CoreOperationCompare

    private class CoreOperationCompareImpl extends CoreOperationCompare {
        public CoreOperationCompareImpl(Expression arg1, Expression arg2) {
            super(arg1, arg2);
        }

        // Override any necessary methods for testing
    }
}



// Add all test methods here

