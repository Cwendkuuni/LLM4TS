
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue() {
        CoreOperationGreaterThanOrEqual comparator = new CoreOperationGreaterThanOrEqual(null, null);
        EvalContext context = new EvalContext();
        
        // Test case 1: arg1 and arg2 are integers
        int arg1 = 5;
        int arg2 = 10;
        context.setNodeSet(new Object[] { arg1 });
        comparator.setArguments(new Expression[] { new CoreVariableReference(), arg2 });
        assertEquals("Test case 1 failed", false, comparator.computeValue(context));
        
        // Test case 2: arg1 and arg2 are strings
        String arg3 = "apple";
        String arg4 = "banana";
        context.setNodeSet(new Object[] { arg3 });
        comparator.setArguments(new Expression[] { new CoreVariableReference(), arg4 });
        assertEquals("Test case 2 failed", false, comparator.computeValue(context));
        
        // Test case 3: arg1 is null
        context.setNodeSet(new Object[] { null });
        comparator.setArguments(new Expression[] { new CoreVariableReference(), arg2 });
        assertEquals("Test case 3 failed", false, comparator.computeValue(context));
        
        // Test case 4: arg2 is null
        context.setNodeSet(new Object[] { arg1 });
        comparator.setArguments(new Expression[] { new CoreVariableReference(), null });
        assertEquals("Test case 4 failed", false, comparator.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThanOrEqual comparator = new CoreOperationGreaterThanOrEqual(null, null);
        assertEquals(">=", comparator.getSymbol());
    }
}

