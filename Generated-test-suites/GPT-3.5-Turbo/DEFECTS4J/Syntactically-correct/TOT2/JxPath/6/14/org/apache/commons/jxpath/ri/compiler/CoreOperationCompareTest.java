import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        CoreOperationCompare testCompare = new CoreOperationCompareMock(new Expression(), new Expression());

        // Typical use case
        assertTrue(testCompare.equal(new EvalContext(), new Expression(), new Expression()));

        // Edge case: Comparing two null values
        assertTrue(testCompare.equal(new EvalContext(), null, null));

        // Edge case: Comparing two different types (boolean and number)
        assertFalse(testCompare.equal(new EvalContext(), true, 1));

        // Error scenario: Null evaluation context
        assertThrows(NullPointerException.class, () -> {
            testCompare.equal(null, new Expression(), new Expression());
        });
    }

    // Mock class for testing CoreOperationCompare
    class CoreOperationCompareMock extends CoreOperationCompare {
        public CoreOperationCompareMock(Expression arg1, Expression arg2) {
            super(arg1, arg2);
        }
    }
}