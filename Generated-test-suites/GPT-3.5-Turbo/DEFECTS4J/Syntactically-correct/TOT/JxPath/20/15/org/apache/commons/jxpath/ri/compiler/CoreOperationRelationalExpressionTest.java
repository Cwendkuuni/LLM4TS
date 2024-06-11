import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        //Test when compute result is true
        Expression[] args = {new Expression(2), new Expression(1)};
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        EvalContext context = new EvalContext();
        assertEquals(expression.computeValue(context), Boolean.TRUE);

        //Test when compute result is false
        Expression[] args2 = {new Expression(1), new Expression(2)};
        CoreOperationRelationalExpression expression2 = new CoreOperationRelationalExpression(args2) {
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        EvalContext context2 = new EvalContext();
        assertEquals(expression2.computeValue(context2), Boolean.FALSE);
    }

    @Test
    public void testGetPrecedence() {
        Expression[] args = {new Expression(1), new Expression(2)};
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        assertEquals(expression.getPrecedence(), CoreOperation.RELATIONAL_EXPR_PRECEDENCE);
    }

    @Test
    public void testIsSymmetric() {
        Expression[] args = {new Expression(1), new Expression(2)};
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        assertEquals(expression.isSymmetric(), false);
    }

    @Test
    public void testCompute() {
        Expression[] args = {new Expression(1), new Expression(2)};
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        EvalContext context = new EvalContext();
        assertEquals(expression.compute(1, 2), false);
    }

    @Test
    public void testReduce() {
        Expression[] args = {new Expression(1), new Expression(2)};
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        assertEquals(expression.reduce(2), 2);
    }

    @Test
    public void testContainsMatch() {
        Expression[] args = {new Expression(1), new Expression(2)};
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        EvalContext context = new EvalContext();
        Iterator it = context.iterator();
        Object value = 2;
        assertEquals(expression.containsMatch(it, value), false);
    }

    @Test
    public void testFindMatch() {
        Expression[] args = {new Expression(1), new Expression(2)};
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        EvalContext context = new EvalContext();
        Iterator lit = context.iterator();
        Iterator rit = context.iterator();
        assertEquals(expression.findMatch(lit, rit), false);
    }
}