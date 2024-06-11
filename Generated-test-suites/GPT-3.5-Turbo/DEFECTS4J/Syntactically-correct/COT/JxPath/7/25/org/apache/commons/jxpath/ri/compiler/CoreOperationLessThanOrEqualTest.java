import org.junit.Test;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThanOrEqual;

import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {
    
    @Test
    public void testComputeValue() {
        Expression exp1 = new CoreOperationLessThanOrEqual(new Expression(), new Expression());
        EvalContext context = new EvalContext();
        
        // Test when both arguments are equal
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        arg1.setValue(5.0);
        arg2.setValue(5.0);
        exp1.getArgs()[0] = arg1;
        exp1.getArgs()[1] = arg2;
        assertTrue((Boolean) exp1.computeValue(context));
        
        // Test when left argument is less than right argument
        arg1.setValue(1.0);
        arg2.setValue(5.0);
        assertTrue((Boolean) exp1.computeValue(context));
        
        // Test when left argument is greater than right argument
        arg1.setValue(10.0);
        arg2.setValue(5.0);
        assertFalse((Boolean) exp1.computeValue(context));
    }
    
    @Test
    public void testGetSymbol() {
        Expression exp1 = new CoreOperationLessThanOrEqual(new Expression(), new Expression());
        assertEquals("<=", exp1.getSymbol());
    }
}