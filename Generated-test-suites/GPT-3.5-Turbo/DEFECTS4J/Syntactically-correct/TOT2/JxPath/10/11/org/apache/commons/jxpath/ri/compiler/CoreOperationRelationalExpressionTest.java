import static org.junit.Assert.*;

import org.junit.Test;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        // Test unequal values
        Object leftValue = 5;
        Object rightValue = 10;
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
        assertEquals(false, expression.computeValue(leftValue, rightValue));

        // Test equal values
        leftValue = 10;
        rightValue = 10;
        assertEquals(true, expression.computeValue(leftValue, rightValue));
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
        assertEquals(3, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
        assertEquals(false, expression.isSymmetric());
    }

    @Test
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
        int compare = 0;
        assertEquals(true, expression.evaluateCompare(compare));

        compare = -1;
        assertEquals(false, expression.evaluateCompare(compare));

        compare = 1;
        assertEquals(false, expression.evaluateCompare(compare));
    }

    @Test
    public void testEdgeCases() {
        // Test with null values
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
        assertEquals(false, expression.computeValue(null, null));

        // Test comparing non-numeric values
        Object leftValue = "abc";
        Object rightValue = "def";
        assertEquals(false, expression.computeValue(leftValue, rightValue));
    }

    @Test(expected = NullPointerException.class)
    public void testExceptionHandling() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
        expression.computeValue(null, 5);
    }
}