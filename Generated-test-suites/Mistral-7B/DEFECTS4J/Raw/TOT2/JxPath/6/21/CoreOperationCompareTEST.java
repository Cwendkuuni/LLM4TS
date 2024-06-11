import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        Expression arg1 = new Expression() {}; // Replace with valid expression
        Expression arg2 = new Expression() {}; // Replace with valid expression
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = coreOperationCompare.evaluate(context);

        assertTrue(result);
    }

}