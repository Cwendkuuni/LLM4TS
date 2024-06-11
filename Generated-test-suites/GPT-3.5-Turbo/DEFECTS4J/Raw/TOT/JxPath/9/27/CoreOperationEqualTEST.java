import org.junit.Test;

public class CoreOperationEqualTest {
    
    @Test
    public void testComputeValue_equals_returnTrue() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new Expression("5"), new Expression("5"));
        assertEquals(Boolean.TRUE, coreOperationEqual.computeValue(new EvalContext()));
    }
    
    @Test
    public void testComputeValue_notEquals_returnFalse() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new Expression("5"), new Expression("10"));
        assertEquals(Boolean.FALSE, coreOperationEqual.computeValue(new EvalContext()));
    }
    
    @Test
    public void testComputeValue_stringEquals_returnTrue() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new Expression("hello"), new Expression("hello"));
        assertEquals(Boolean.TRUE, coreOperationEqual.computeValue(new EvalContext()));
    }
    
    @Test
    public void testComputeValue_stringNotEquals_returnFalse() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new Expression("hello"), new Expression("world"));
        assertEquals(Boolean.FALSE, coreOperationEqual.computeValue(new EvalContext()));
    }
    
    @Test
    public void testComputeValue_nullValues_returnTrue() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new Expression(null), new Expression(null));
        assertEquals(Boolean.TRUE, coreOperationEqual.computeValue(new EvalContext()));
    }
    
    @Test
    public void testComputeValue_nullAndNonNullValues_returnFalse() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new Expression(null), new Expression("hello"));
        assertEquals(Boolean.FALSE, coreOperationEqual.computeValue(new EvalContext()));
    }
    
    @Test
    public void testComputeValue_numericAndStringValues_returnFalse() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new Expression(10), new Expression("10"));
        assertEquals(Boolean.FALSE, coreOperationEqual.computeValue(new EvalContext()));
    }
    
    @Test
    public void testGetSymbol_equalOperator_returnEqualSymbol() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new Expression(10), new Expression(20));
        assertEquals("=", coreOperationEqual.getSymbol());
    }
}