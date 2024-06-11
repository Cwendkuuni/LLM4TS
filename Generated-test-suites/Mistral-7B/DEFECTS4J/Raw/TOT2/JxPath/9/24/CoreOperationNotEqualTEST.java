import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri. composer.CoreOperationNotEqual;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CoreOperationNotEqualTest {

    @Test
    public void testNotEqualOperation() {
        Expression arg1 = new ConstantExpression(1);
        Expression arg2 = new ConstantExpression(2);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }
}