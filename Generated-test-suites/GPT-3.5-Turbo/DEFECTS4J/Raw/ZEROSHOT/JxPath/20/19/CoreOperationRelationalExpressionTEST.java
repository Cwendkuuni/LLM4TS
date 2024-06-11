import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionImpl(args);

        // Test case 1 - left < right
        args[0] = new ExpressionImpl(2);
        args[1] = new ExpressionImpl(5);
        assertEquals(Boolean.TRUE, expression.computeValue(context));

        // Test case 2 - left > right
        args[0] = new ExpressionImpl(5);
        args[1] = new ExpressionImpl(2);
        assertEquals(Boolean.FALSE, expression.computeValue(context));

        // Test case 3 - left == right
        args[0] = new ExpressionImpl(2);
        args[1] = new ExpressionImpl(2);
        assertEquals(Boolean.FALSE, expression.computeValue(context));

        // Test case 4 - left is NaN
        args[0] = new ExpressionImpl(Double.NaN);
        args[1] = new ExpressionImpl(5);
        assertEquals(Boolean.FALSE, expression.computeValue(context));

        // Test case 5 - right is NaN
        args[0] = new ExpressionImpl(5);
        args[1] = new ExpressionImpl(Double.NaN);
        assertEquals(Boolean.FALSE, expression.computeValue(context));

        // Test case 6 - left and right are Iterators
        args[0] = new ExpressionImpl(new Iterator<Integer>() {
            int i = 0;
            public boolean hasNext() {
                return i < 5;
            }
            public Integer next() {
                return i++;
            }
        });
        args[1] = new ExpressionImpl(new Iterator<Integer>() {
            int i = 3;
            public boolean hasNext() {
                return i < 8;
            }
            public Integer next() {
                return i++;
            }
        });
        assertEquals(Boolean.TRUE, expression.computeValue(context));

        // Test case 7 - left is Iterator and right is not
        args[0] = new ExpressionImpl(new Iterator<Integer>() {
            int i = 0;
            public boolean hasNext() {
                return i < 5;
            }
            public Integer next() {
                return i++;
            }
        });
        args[1] = new ExpressionImpl(2);
        assertEquals(Boolean.TRUE, expression.computeValue(context));
    }
}