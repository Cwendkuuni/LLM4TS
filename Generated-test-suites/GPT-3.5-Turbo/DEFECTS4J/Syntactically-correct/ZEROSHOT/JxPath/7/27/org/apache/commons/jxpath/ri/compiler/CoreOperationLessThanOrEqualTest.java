import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.ri.EvalContext;

import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {
    
    private CoreOperationLessThanOrEqual operation;
    
    @Before
    public void setUp() {
        operation = new CoreOperationLessThanOrEqual(
                new ExpressionMock(), new ExpressionMock());
    }
    
    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        context.setExpressionContext(new ExpressionContextMock());
        
        Object result = operation.computeValue(context);
        
        assertNotNull(result);
        assertTrue(result instanceof Boolean);
    }
    
    @Test
    public void testGetSymbol() {
        String symbol = operation.getSymbol();
        
        assertNotNull(symbol);
        assertEquals("<=", symbol);
    }
    
    private class ExpressionMock implements Expression {
        
        public Object computeValue(EvalContext context) {
            return 1;
        }
        
        public String getSymbol() {
            return "";
        }
    }
    
    private class ExpressionContextMock implements ExpressionContext {
        
        public Object getValue(String xpath) {
            return null;
        }
    }
}