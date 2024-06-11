import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression coreOp = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                // TODO Implement evaluation logic
                return false;
            }
        };
        EvalContext context = new EvalContext();
        Object result = coreOp.computeValue(context);
        // TODO Add assertion for expected result
    }
}