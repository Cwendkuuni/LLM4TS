package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CoreOperationGreaterThanTest {

    @Test
    public void testCoreOperationGreaterThan() {
        Expression arg1 = new LiteralExpression(new Double(5.5));
        Expression arg2 = new LiteralExpression(new Double(3.0));
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet());
        Object result = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue() {
        Expression arg1 = new LiteralExpression(new Double(5.5));
        Expression arg2 = new LiteralExpression(new Double(3.0));
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);

        EvalContext context = new EvalContext(new InfoSet());
        InfoSet infoSet = new InfoSet();
        infoSet.setNodeInfoAtPosition(0, new NodeInfo(1, "root", null, null, null, null, null));
        context.setInfoSet(infoSet);

        Object arg1Value = operation.args[0].computeValue(context);
        Object arg2Value = operation.args[1].computeValue(context);

        double l = InfoSetUtil.doubleValue(arg1Value);
        double r = InfoSetUtil.doubleValue(arg2Value);

        Object result = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        // Test with different values
        arg1 = new LiteralExpression(new Double(10.1));
        arg2 = new LiteralExpression(new Double(5.0));
        operation = new CoreOperationGreaterThan(arg1, arg2);
        result = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        arg1 = new LiteralExpression(new Double(3.5));
        arg2 = new LiteralExpression(new Double(3.5));
        operation = new CoreOperationGreaterThan(arg1, arg2);
        result = operation.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(null, null);
        assertEquals(">", operation.getSymbol());
    }
}