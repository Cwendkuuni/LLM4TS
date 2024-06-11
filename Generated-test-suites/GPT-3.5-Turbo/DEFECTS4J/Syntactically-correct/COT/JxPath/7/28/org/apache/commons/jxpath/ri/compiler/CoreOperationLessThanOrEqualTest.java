
import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testComputeValue() {
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(new Expression(), new Expression());
        EvalContext context = new EvalContext();
        
        // Test when arg1 and arg2 are the same
        context.setCurrentNode(2);
        context.setPosition(1);
        context.setSize(5);
        context.setNodeSet(new Node[] { new Node() });
        Object result = expression.computeValue(context);
        assertEquals(Boolean.TRUE, result);
        
        // Test when arg1 is less than arg2
        context.setCurrentNode(1);
        result = expression.computeValue(context);
        assertEquals(Boolean.TRUE, result);
        
        // Test when arg1 is greater than arg2
        context.setCurrentNode(4);
        result = expression.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(new Expression(), new Expression());
        String symbol = expression.getSymbol();
        assertEquals("<=", symbol);
    }
}
