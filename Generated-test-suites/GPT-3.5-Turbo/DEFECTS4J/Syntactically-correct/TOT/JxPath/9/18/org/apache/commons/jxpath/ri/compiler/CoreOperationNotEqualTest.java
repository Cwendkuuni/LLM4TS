import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationNotEqual;

import static org.junit.Assert.*;

public class CoreOperationNotEqualTest {
    
    private CoreOperationNotEqual coreOperation;
    
    @Before
    public void setup() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        coreOperation = new CoreOperationNotEqual(arg1, arg2);
    }
    
    @Test
    public void testComputeValue_EqualArgs_ShouldReturnFalse() {
        EvalContext context = new EvalContext();
        Object result = coreOperation.computeValue(context);
        assertFalse((Boolean) result);
    }
    
    @Test
    public void testComputeValue_UnequalArgs_ShouldReturnTrue() {
        Object arg1 = "Hello";
        Object arg2 = "World";
        coreOperation.setArgs(new Expression(arg1), new Expression(arg2));
        
        EvalContext context = new EvalContext();
        Object result = coreOperation.computeValue(context);
        assertTrue((Boolean) result);
    }
    
    @Test
    public void testComputeValue_Arg1Null_ShouldReturnTrue() {
        Object arg1 = null;
        Object arg2 = "World";
        coreOperation.setArgs(new Expression(arg1), new Expression(arg2));
        
        EvalContext context = new EvalContext();
        Object result = coreOperation.computeValue(context);
        assertTrue((Boolean) result);
    }
    
    @Test
    public void testComputeValue_Arg2Null_ShouldReturnTrue() {
        Object arg1 = "Hello";
        Object arg2 = null;
        coreOperation.setArgs(new Expression(arg1), new Expression(arg2));
        
        EvalContext context = new EvalContext();
        Object result = coreOperation.computeValue(context);
        assertTrue((Boolean) result);
    }
    
    @Test
    public void testComputeValue_BothArgsNull_ShouldReturnFalse() {
        Object arg1 = null;
        Object arg2 = null;
        coreOperation.setArgs(new Expression(arg1), new Expression(arg2));
        
        EvalContext context = new EvalContext();
        Object result = coreOperation.computeValue(context);
        assertFalse((Boolean) result);
    }
    
    @Test
    public void testGetSymbol_ShouldReturnNotEqualSymbol() {
        String symbol = coreOperation.getSymbol();
        assertEquals("!=", symbol);
    }
}