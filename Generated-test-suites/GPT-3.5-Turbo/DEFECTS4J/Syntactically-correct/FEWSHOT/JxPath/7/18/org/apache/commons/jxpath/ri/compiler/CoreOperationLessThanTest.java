import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;

public class CoreOperationLessThanTest {
    
    @Test
    public void testComputeValue() {
        CoreOperationLessThan lessThan = new CoreOperationLessThan(new Expression(), new Expression());
        EvalContext evalContext = new EvalContext();
        evalContext.setIterator(null);
        
        double l = InfoSetUtil.doubleValue(new Expression().computeValue(evalContext));
        double r = InfoSetUtil.doubleValue(new Expression().computeValue(evalContext));
        Object expected = l < r ? Boolean.TRUE : Boolean.FALSE;
        
        assertEquals(expected, lessThan.computeValue(evalContext));
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationLessThan lessThan = new CoreOperationLessThan(new Expression(), new Expression());
        assertEquals("<", lessThan.getSymbol());
    }
}