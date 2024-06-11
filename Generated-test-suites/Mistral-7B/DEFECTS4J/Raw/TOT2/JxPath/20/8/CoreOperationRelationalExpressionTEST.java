import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression left = new Const(1);
        Expression right = new Const(2);
        CoreOperationRelationalExpression expression = new Gt(new And(left, new Const(1)), right);
        EvalContext context = new EvalContext();

        boolean result = expression.computeValue(context).booleanValue();
        assertTrue(result);
    }
}