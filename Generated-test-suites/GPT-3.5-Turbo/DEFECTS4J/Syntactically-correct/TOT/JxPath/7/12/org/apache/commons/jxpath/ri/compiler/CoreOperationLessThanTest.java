
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanTest {

    @Test
    public void testConstructor(){
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg2);
        assertNotNull(lessThan);
        assertArrayEquals(new Expression[] { arg1, arg2 }, lessThan.getArgs());
    }

    @Test
    public void testComputeValue_LessThan_ReturnsTrue(){
        EvalContext context = new EvalContext();
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg2);
        context.setNodeSetIterator(new NodeSetIterator()); // Set some sample values for examples
        arg1.computeValue(context); // Set arg1 value
        arg2.computeValue(context); // Set arg2 value
        
        // Test when arg1.computeValue(context) is less than arg2.computeValue(context)
        assertEquals(Boolean.TRUE, lessThan.computeValue(context));
    }

    @Test
    public void testComputeValue_LessThan_ReturnsFalse(){
        EvalContext context = new EvalContext();
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg2);
        context.setNodeSetIterator(new NodeSetIterator()); // Set some sample values for examples
        arg1.computeValue(context); // Set arg1 value
        arg2.computeValue(context); // Set arg2 value
        
        // Test when arg1.computeValue(context) is not less than arg2.computeValue(context)
        assertEquals(Boolean.FALSE, lessThan.computeValue(context));
    }

    @Test
    public void testGetSymbol_ReturnsCorrectSymbol(){
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg2);
        assertEquals("<", lessThan.getSymbol());
    }
}
