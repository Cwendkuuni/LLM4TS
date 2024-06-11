import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Instance;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.NodeInfoConstants;
import org.apache.commons.jxpath.ri.core.Context;
import org.apache.commons.jxpath.ri.core.ContextFactory;
import org.apache.commons.jxpath.ri. Expression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThan;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Test for CoreOperationGreaterThan class.
 */
public class CoreOperationGreaterThanTest {

    @Test
    public void testCoreOperationGreaterThan() {
        // Test with double values
        double l = 10.0;
        double r = 5.0;
        Expression arg1 = new LiteralExpression(new ConstantExpression(l));
        Expression arg2 = new LiteralExpression(new ConstantExpression(r));
        CoreOperationGreaterThan greaterThan = new CoreOperationGreaterThan(arg1, arg2);

        EvalContext context = ContextFactory.createRootContext(new Instance(new NodeInfo(NodeInfoConstants.DOCUMENT_NODE, new ArrayList<NodeInfo>())));
        Object result = greaterThan.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);

        // Test with negative double values
        l = -10.0;
        r = -5.0;
        arg1 = new LiteralExpression(new ConstantExpression(l));
        arg2 = new LiteralExpression(new ConstantExpression(r));
        greaterThan = new CoreOperationGreaterThan(arg1, arg2);
        result = greaterThan.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);

        // Test with BigDecimal values
        BigDecimal lBigDec = new BigDecimal("10.5");
        BigDecimal rBigDec = new BigDecimal("5.5");
        arg1 = new LiteralExpression(new ConstantExpression(lBigDec));
        arg2 = new LiteralExpression(new ConstantExpression(rBigDec));
        greaterThan = new CoreOperationGreaterThan(arg1, arg2);
        context = ContextFactory.createRootContext(new Instance(new NodeInfo(NodeInfoConstants.DOCUMENT_NODE, new ArrayList<NodeInfo>())));
        result = greaterThan.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);

        // Test with BigDecimal and double values
        BigDecimal lBigDec2 = new BigDecimal("10.5");
        r = 5.5;
        arg1 = new LiteralExpression(new ConstantExpression(lBigDec2));
        arg2 = new LiteralExpression(new ConstantExpression(r));
        greaterThan = new CoreOperationGreaterThan(arg1, arg2);
        context = ContextFactory.createRootContext(new Instance(new NodeInfo(NodeInfoConstants.DOCUMENT_NODE, new ArrayList<NodeInfo>())));
        result = greaterThan.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);

        // Test with error scenario - null argument
        arg1 = new LiteralExpression(null);
        greaterThan = new CoreOperationGreaterThan(arg1, arg2);
        Assert.assertThrows(RuntimeException.class, () -> greaterThan.computeValue(context));

        // Test with error scenario - invalid expression type
        arg1 = new ConstantExpression(null);
        greaterThan = new CoreOperationGreaterThan(arg1, arg2);
        Assert.assertThrows(RuntimeException.class, () -> greaterThan.computeValue(context));
    }
}