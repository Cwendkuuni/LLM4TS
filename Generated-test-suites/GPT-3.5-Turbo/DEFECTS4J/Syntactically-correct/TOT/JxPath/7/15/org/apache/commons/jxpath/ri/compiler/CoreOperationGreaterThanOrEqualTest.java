import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        
        // Test with values where l >= r
        CoreOperationGreaterThanOrEqual expr1 = new CoreOperationGreaterThanOrEqual(new Expression("10"), new Expression("5"));
        assertEquals(Boolean.TRUE, expr1.computeValue(context));
        
        CoreOperationGreaterThanOrEqual expr2 = new CoreOperationGreaterThanOrEqual(new Expression("5"), new Expression("5"));
        assertEquals(Boolean.TRUE, expr2.computeValue(context));
        
        CoreOperationGreaterThanOrEqual expr3 = new CoreOperationGreaterThanOrEqual(new Expression("0"), new Expression("-5"));
        assertEquals(Boolean.TRUE, expr3.computeValue(context));
        
        // Test with values where l < r
        CoreOperationGreaterThanOrEqual expr4 = new CoreOperationGreaterThanOrEqual(new Expression("5"), new Expression("10"));
        assertEquals(Boolean.FALSE, expr4.computeValue(context));
        
        CoreOperationGreaterThanOrEqual expr5 = new CoreOperationGreaterThanOrEqual(new Expression("-5"), new Expression("0"));
        assertEquals(Boolean.FALSE, expr5.computeValue(context));
        
        // Test with non-number values
        CoreOperationGreaterThanOrEqual expr6 = new CoreOperationGreaterThanOrEqual(new Expression("'abc'"), new Expression("'def'"));
        assertEquals(Boolean.FALSE, expr6.computeValue(context));
        
        CoreOperationGreaterThanOrEqual expr7 = new CoreOperationGreaterThanOrEqual(new Expression("'abc'"), new Expression("'abc'"));
        assertEquals(Boolean.TRUE, expr7.computeValue(context));
        
        CoreOperationGreaterThanOrEqual expr8 = new CoreOperationGreaterThanOrEqual(new Expression("true"), new Expression("false"));
        assertEquals(Boolean.TRUE, expr8.computeValue(context));
        
        CoreOperationGreaterThanOrEqual expr9 = new CoreOperationGreaterThanOrEqual(new Expression("false"), new Expression("true"));
        assertEquals(Boolean.FALSE, expr9.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThanOrEqual expr = new CoreOperationGreaterThanOrEqual(new Expression("10"), new Expression("5"));
        assertEquals(">=", expr.getSymbol());
    }
}