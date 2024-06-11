
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;

import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {
  
    private CoreOperationLessThanOrEqual coreOperationLessThanOrEqual;
  
    @Test
    public void computeValue_shouldReturnTrue_ifLeftValueIsLessThanOrEqualToRightValue() {
        EvalContext context = new EvalContext();
        context.setNodeSet(new Object[] {1, 2, 3});
    
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(new Constant(1), new Constant(2));
      
        assertTrue((Boolean) coreOperationLessThanOrEqual.computeValue(context));
    }
    
    @Test
    public void computeValue_shouldReturnFalse_ifLeftValueIsGreaterThanRightValue() {
        EvalContext context = new EvalContext();
        context.setNodeSet(new Object[] {1, 2, 3});
    
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(new Constant(3), new Constant(2));
      
        assertFalse((Boolean) coreOperationLessThanOrEqual.computeValue(context));
    }
    
    @Test
    public void computeValue_shouldReturnTrue_ifLeftValueAndRightValueAreEqual() {
        EvalContext context = new EvalContext();
        context.setNodeSet(new Object[] {1, 2, 3});
    
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(new Constant(2), new Constant(2));
      
        assertTrue((Boolean) coreOperationLessThanOrEqual.computeValue(context));
    }
  
    @Test
    public void getSymbol_shouldReturnLessThanOrEqualTo() {
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(new Constant(1), new Constant(2));
    
        assertEquals("<=", coreOperationLessThanOrEqual.getSymbol());
    }
}
