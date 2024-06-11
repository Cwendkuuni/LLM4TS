import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class CoreOperationRelationalExpressionTest {

    private CoreOperationRelationalExpression expression;

    @Before
    public void setUp() {
        Expression[] args = new Expression[2];
        // Create mock expressions here
        expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
    }

    @Test
    public void testComputeValue_True() {
        EvalContext context = new EvalContext();
        // Set up context and test values here

        Object result = expression.computeValue(context);

        assertTrue((boolean) result);
    }

    @Test
    public void testComputeValue_False() {
        EvalContext context = new EvalContext();
        // Set up context and test values here

        Object result = expression.computeValue(context);

        assertFalse((boolean) result);
    }

    // Add more test cases for each method

    // Test typical use cases

    // Test edge cases

    // Test error scenarios

}