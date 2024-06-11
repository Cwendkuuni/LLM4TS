import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {
    @Test
    public void testComputeValue() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreConstantExpression(5), new CoreConstantExpression(10));
        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        expression = new CoreOperationRelationalExpression(new CoreConstantExpression(10), new CoreConstantExpression(5));
        result = expression.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        expression = new CoreOperationRelationalExpression(new CoreConstantExpression(5), new CoreConstantExpression(5));
        result = expression.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testPrecedenceAndSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreConstantExpression(5), new CoreConstantExpression(10));
        assertEquals(3, expression.getPrecedence());
        assertEquals(false, expression.isSymmetric());
    }

    @Test
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreConstantExpression(5), new CoreConstantExpression(10));
        assertTrue(expression.evaluateCompare(-1) == false);
        assertTrue(expression.evaluateCompare(0) == false);
        assertTrue(expression.evaluateCompare(1) == true);
    }

    @Test
    public void testCompute() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreConstantExpression(5), new CoreConstantExpression(5));
        Object left = new Integer(5);
        Object right = new Integer(5);
        assertTrue(expression.compute(left, right));

        left = new Integer(6);
        right = new Integer(5);
        assertFalse(expression.compute(left, right));

        left = new Integer(4);
        right = new Integer(5);
        assertFalse(expression.compute(left, right));

        left = new Integer[1];
        right = new Integer[1];
        left[0] = 5;
        right[0] = 5;
        assertTrue(expression.compute(left, right));
    }

    @Test
    public void testReduceContainsMatchFindMatch() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreConstantExpression(5), new CoreConstantExpression(10));

        Object left = new Integer(5);
        Object right = new Integer(10);
        Object reducedLeft = expression.reduce(left);
        Object reducedRight = expression.reduce(right);

        assertTrue(reducedLeft instanceof Iterator);
        assertTrue(reducedRight instanceof Iterator);

        Iterator it = (Iterator) reducedLeft;
        Iterator rit = (Iterator) reducedRight;

        assertFalse(expression.containsMatch(it, right));
        assertFalse(expression.containsMatch(left, rit));

        HashSet leftSet = new HashSet();
        leftSet.add(new Integer(5));
        leftSet.add(new Integer(6));

        assertFalse(expression.findMatch(it, rit));
        assertFalse(expression.findMatch(lit, new Iterator() {}));
    }
}