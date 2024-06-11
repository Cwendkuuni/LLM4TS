import static org.junit.Assert.*;
import org.junit.*;

public class CoreOperationNotEqualTest {

    // Test case for computeValue method
    @Test
    public void testComputeValue() {
        // Typical use case with two different operands
        CoreOperationNotEqual operation1 = new CoreOperationNotEqual(new Expression("operand1"), new Expression("operand2"));
        EvalContext evalContext1 = new EvalContext();
        Object result1 = operation1.computeValue(evalContext1);
        assertTrue(result1 instanceof Boolean);
        assertTrue((Boolean) result1);
        
        // Typical use case with two equal operands
        CoreOperationNotEqual operation2 = new CoreOperationNotEqual(new Expression("operand1"), new Expression("operand1"));
        EvalContext evalContext2 = new EvalContext();
        Object result2 = operation2.computeValue(evalContext2);
        assertTrue(result2 instanceof Boolean);
        assertFalse((Boolean) result2);
        
        // Error scenario with null operands
        CoreOperationNotEqual operation3 = new CoreOperationNotEqual(new Expression(null), new Expression(null));
        EvalContext evalContext3 = new EvalContext();
        Object result3 = operation3.computeValue(evalContext3);
        assertTrue(result3 instanceof Boolean);
        assertFalse((Boolean) result3);
        
        // Edge case with one null operand and one non-null operand
        CoreOperationNotEqual operation4 = new CoreOperationNotEqual(new Expression(null), new Expression("operand1"));
        EvalContext evalContext4 = new EvalContext();
        Object result4 = operation4.computeValue(evalContext4);
        assertTrue(result4 instanceof Boolean);
        assertTrue((Boolean) result4);
    }
    
    // Test case for getSymbol method
    @Test
    public void testGetSymbol() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(new Expression("operand1"), new Expression("operand2"));
        String symbol = operation.getSymbol();
        assertEquals("!=", symbol);
    }
}