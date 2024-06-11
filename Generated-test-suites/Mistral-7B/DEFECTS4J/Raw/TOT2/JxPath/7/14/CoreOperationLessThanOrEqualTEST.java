import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.Nodes;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testLessThanOrEqualWithNumbers() {
        Expression arg1 = new CoreNumberExpression(new NumberValue(5.0));
        Expression arg2 = new CoreNumberExpression(new NumberValue(10.0));
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet(), Nodes.EMPTY_NODES);
        assertTrue(operation.computeValue(context).equals(Boolean.TRUE));
    }

    @Test
    public void testLessThanOrEqualWithNumbersDifferentSign() {
        Expression arg1 = new CoreNumberExpression(new NumberValue(-5.0));
        Expression arg2 = new CoreNumberExpression(new NumberValue(10.0));
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet(), Nodes.EMPTY_NODES);
        assertFalse(operation.computeValue(context).equals(Boolean.TRUE));
    }

    @Test
    public void testLessThanOrEqualWithEqualNumbers() {
        Expression arg1 = new CoreNumberExpression(new NumberValue(5.0));
        Expression arg2 = new CoreNumberExpression(new NumberValue(5.0));
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet(), Nodes.EMPTY_NODES);
        assertTrue(operation.computeValue(context).equals(Boolean.TRUE));
    }

    // Add edge cases and exception handling test cases here
}