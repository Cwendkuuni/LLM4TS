import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        // Given
        Expression left = new ConstantExpression(10);
        Expression right = new ConstantExpression(20);
        CoreOperationRelationalExpression expression = new GreaterThanExpression(left, right);
        EvalContext context = new EvaluationContext(new MockInfoSet());

        // When
        Object result = expression.computeValue(context);

        // Then
        assertEquals(Boolean.FALSE, result);
    }
}