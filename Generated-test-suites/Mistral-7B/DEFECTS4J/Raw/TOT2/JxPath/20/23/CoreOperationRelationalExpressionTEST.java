import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.Axis;
import org.apache.commons.jxpath.ri.CompiledExpression;
import org.apache.commons.jxpath.ri.Constant;
import org.apache.commons.jxpath.ri.SimpleNode;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext(new InfoSet(), new SimpleNode("root", new Object(), new Object()), Axis.SELF);
        Expression arg1 = new Constant(1);
        Expression arg2 = new Constant(2);
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new Expression[]{arg1, arg2});
        Object result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        arg1 = new Constant(2);
        arg2 = new Constant(1);
        expr = new CoreOperationRelationalExpression(new Expression[]{arg1, arg2});
        result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }
}