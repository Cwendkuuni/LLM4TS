import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Object left = 5;
        Object right = 10;
        CoreOperationRelationalExpression expression = new MyCoreOperationRelationalExpression(new CoreExpression(left), new CoreExpression(right));
        EvalContext context = new EvalContext();
        Boolean result = expression.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        left = 10;
        right = 10;
        expression = new MyCoreOperationRelationalExpression(new CoreExpression(left), new CoreExpression(right));
        result = expression.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }
}