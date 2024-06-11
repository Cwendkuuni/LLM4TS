
import org.junit.Test;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue() {
        CoreOperationLessThan lessThan = new CoreOperationLessThan(expression1, expression2);
        EvalContext context = new EvalContext();
        
        // Test when args[0].computeValue(context) < args[1].computeValue(context)
        double l1 = 10.5;
        double r1 = 20.3;
        context.setVal(l1);
        context.setVal(r1);
        Object expected1 = Boolean.TRUE;
        Object actual1 = lessThan.computeValue(context);
        assertEquals(expected1, actual1);
        
        // Test when args[0].computeValue(context) >= args[1].computeValue(context)
        double l2 = 30.7;
        double r2 = 15.2;
        context.setVal(l2);
        context.setVal(r2);
        Object expected2 = Boolean.FALSE;
        Object actual2 = lessThan.computeValue(context);
        assertEquals(expected2, actual2);
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationLessThan lessThan = new CoreOperationLessThan(expression1, expression2);
        String expected = "<";
        String actual = lessThan.getSymbol();
        assertEquals(expected, actual);
    }
}
