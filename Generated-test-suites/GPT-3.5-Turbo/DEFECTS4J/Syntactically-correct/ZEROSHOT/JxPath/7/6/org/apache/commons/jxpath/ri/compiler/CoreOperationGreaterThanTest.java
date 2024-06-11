import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValue() {
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        
        // Test case 1
        // Test arg1 = 5, arg2 = 3
        // Expected result: true
        Double expectedResult1 = true;
        context.set("arg1", 5);
        context.set("arg2", 3);
        Object result1 = expression.computeValue(context);
        assertEquals(expectedResult1, result1);
        
        // Test case 2
        // Test arg1 = 2, arg2 = 5
        // Expected result: false
        Double expectedResult2 = false;
        context.set("arg1", 2);
        context.set("arg2", 5);
        Object result2 = expression.computeValue(context);
        assertEquals(expectedResult2, result2);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(arg1, arg2);
        String expectedSymbol = ">";
        String result = expression.getSymbol();
        assertEquals(expectedSymbol, result);
    }

}