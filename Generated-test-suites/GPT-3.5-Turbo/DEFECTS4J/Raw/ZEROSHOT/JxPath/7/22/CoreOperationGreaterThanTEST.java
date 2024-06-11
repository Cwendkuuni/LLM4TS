import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValue() {
        CoreOperationGreaterThan testObj = new CoreOperationGreaterThan(arg1, arg2);
        
        // Test case 1
        EvalContext context1 = new EvalContext();
        context1.setContextNode(arg1);
        Object expectedResult1 = false;
        Object actualResult1 = testObj.computeValue(context1);
        assertEquals("Test case 1 failed", expectedResult1, actualResult1);
        
        // Test case 2
        EvalContext context2 = new EvalContext();
        context2.setContextNode(arg2);
        Object expectedResult2 = true;
        Object actualResult2 = testObj.computeValue(context2);
        assertEquals("Test case 2 failed", expectedResult2, actualResult2);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan testObj = new CoreOperationGreaterThan(arg1, arg2);
        String expectedSymbol = ">";
        String actualSymbol = testObj.getSymbol();
        assertEquals("Symbol mismatch", expectedSymbol, actualSymbol);
    }

}