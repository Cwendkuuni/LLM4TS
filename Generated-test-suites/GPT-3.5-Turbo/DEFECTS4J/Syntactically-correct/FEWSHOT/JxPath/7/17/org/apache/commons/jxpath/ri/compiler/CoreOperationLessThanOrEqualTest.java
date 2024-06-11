import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThanOrEqual;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;

public class CoreOperationLessThanOrEqualTest {
    @Test
    public void testConstructor() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(arg1, arg2);
        
        assertNotNull(op);
        assertEquals("<=", op.getSymbol());
    }

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(arg1, arg2);
        
        context.setContextNode(InfoSetUtil.doubleValue(2.0));
        arg1.computeValue(context);
        context.setContextNode(InfoSetUtil.doubleValue(3.0));
        arg2.computeValue(context);
        assertEquals(Boolean.TRUE, op.computeValue(context));
        
        context.setContextNode(InfoSetUtil.doubleValue(3.0));
        arg1.computeValue(context);
        context.setContextNode(InfoSetUtil.doubleValue(2.0));
        arg2.computeValue(context);
        assertEquals(Boolean.FALSE, op.computeValue(context));
    }
}