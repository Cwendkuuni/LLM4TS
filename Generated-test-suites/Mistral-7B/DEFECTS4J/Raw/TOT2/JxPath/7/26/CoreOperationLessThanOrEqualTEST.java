import org.apache.commons.jxpath.ri.EvaluationContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThanOrEqual;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testLessThanOrEqualWithDoubleValues() {
        EvaluationContext context = new EvaluationContext(new InfoSet());
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(
                new LiteralExpression(5.0), new LiteralExpression(5.0));
        assertEquals(Boolean.TRUE, expression.computeValue(context));
    }

    @Test
    public void testLessThanOrEqualWithDoubleValuesNegative() {
        EvaluationContext context = new EvaluationContext(new InfoSet());
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(
                new LiteralExpression(-3.0), new LiteralExpression(-5.0));
        assertEquals(Boolean.TRUE, expression.computeValue(context));
    }

    @Test
    public void testLessThanOrEqualWithDoubleValuesPositiveAndNegative() {
        EvaluationContext context = new EvaluationContext(new InfoSet());
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(
                new LiteralExpression(5.0), new LiteralExpression(-3.0));
        assertEquals(Boolean.FALSE, expression.computeValue(context));
    }

    @Test
    public void testLessThanOrEqualWithNaN() {
        EvaluationContext context = new EvaluationContext(new InfoSet());
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(
                new LiteralExpression(Double.NaN), new LiteralExpression(Double.NaN));
        assertEquals(Boolean.NaN, expression.computeValue(context));
    }

    @Test
    public void testLessThanOrEqualWithInfinity() {
        EvaluationContext context = new EvaluationContext(new InfoSet());
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(
                new LiteralExpression(Double.POSITIVE_INFINITY),
                new LiteralExpression(Double.NEGATIVE_INFINITY));
        assertEquals(Boolean.FALSE, expression.computeValue(context));
    }

    @Test
    public void testLessThanOrEqualWithMismatchedTypes() {
        EvaluationContext context = new EvaluationContext(new InfoSet());
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(
                new StringLiteralExpression("5"), new LiteralExpression(5.0));
        // Add assertion here
    }

    @Test
    public void testLessThanOrEqualWithNullArg() {
        EvaluationContext context = new EvaluationContext(new InfoSet());
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(
                null, new LiteralExpression(5.0));
        // Add assertion here
    }

    @Test
    public void testLessThanOrEqualWithNullArg2() {
        EvaluationContext context = new EvaluationContext(new InfoSet());
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(
                new LiteralExpression(5.0), null);
        // Add assertion here
    }
}