import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.core.Context;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testLessThanOrEqualWithNumbers() {
        Expression arg1 = new Constant(1.5);
        Expression arg2 = new Constant(3.0);
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(arg1, arg2);
        
        EvalContext context = new EvalContext(new Context(null, new ArrayList<>()));
        InfoSet infoSet = new InfoSet();
        infoSet.setRoot(new NodeInfo(1, "root", ""));

        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        arg1 = new Constant(1.5);
        arg2 = new Constant(1.5);
        expression = new CoreOperationLessThanOrEqual(arg1, arg2);

        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testLessThanOrEqualWithStrings() {
        Expression arg1 = new Constant("3");
        Expression arg2 = new Constant("5");
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(arg1, arg2);

        EvalContext context = new EvalContext(new Context(null, new ArrayList<>()));
        InfoSet infoSet = new InfoSet();
        infoSet.setRoot(new NodeInfo(1, "root", ""));

        Object result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);

        arg1 = new Constant("3");
        arg2 = new Constant("3");
        expression = new CoreOperationLessThanOrEqual(arg1, arg2);

        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testLessThanOrEqualWithNulls() {
        Expression arg1 = new Constant(null);
        Expression arg2 = new Constant(2.0);
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(arg1, arg2);

        EvalContext context = new EvalContext(new Context(null, new ArrayList<>()));
        InfoSet infoSet = new InfoSet();
        infoSet.setRoot(new NodeInfo(1, "root", ""));

        Object result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);

        arg1 = new Constant(1.5);
        arg2 = new Constant(null);
        expression = new CoreOperationLessThanOrEqual(arg1, arg2);

        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);

        arg1 = new Constant(null);
        arg2 = new Constant(null);
        expression = new CoreOperationLessThanOrEqual(arg1, arg2);

        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLessThanOrEqualWithInvalidArguments() {
        Expression arg1 = new CoreOperationNegate(new Constant(1.5));
        Expression arg2 = null;
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(arg1, arg2);

        EvalContext context = new EvalContext(new Context(null, new ArrayList<>()));
        InfoSet infoSet = new InfoSet();
        infoSet.setRoot(new NodeInfo(1, "root", ""));

        expression.computeValue(context);
    }
}