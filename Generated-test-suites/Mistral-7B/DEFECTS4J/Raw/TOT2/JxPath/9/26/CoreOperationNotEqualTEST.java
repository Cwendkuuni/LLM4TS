import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationNotEqual;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationNotEqualTest {

    @Test
    public void testNotEqual() {
        Expression arg1 = new ConstantExpression(1);
        Expression arg2 = new ConstantExpression(2);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Boolean result = (Boolean) coreOperationNotEqual.computeValue(context);
        assertTrue(result == Boolean.FALSE);
    }
}