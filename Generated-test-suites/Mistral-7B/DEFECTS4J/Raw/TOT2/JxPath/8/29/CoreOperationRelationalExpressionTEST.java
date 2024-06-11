import static org.junit.Assert.assertEquals;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression left = new LiteralExpression(2);
        Expression right = new LiteralExpression(3);
        CoreOperationRelationalExpression test = new CoreOperationGreaterThan(new CoreOperation[] { left, right });
        EvalContext context = new EvalContext(new InitialContext(), new Iterator[0]);
        Object result = test.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        left = new LiteralExpression(3);
        right = new LiteralExpression(2);
        test = new CoreOperationGreaterThan(new CoreOperation[] { left, right });
        result = test.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }
}