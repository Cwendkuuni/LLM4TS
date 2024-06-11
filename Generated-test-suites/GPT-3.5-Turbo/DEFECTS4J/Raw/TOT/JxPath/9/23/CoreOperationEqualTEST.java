
import org.junit.Test;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.apache.commons.jxpath.ri.EvalContext;

import static org.junit.Assert.*;

public class CoreOperationEqualTest {

    private CoreOperationEqual expression;

    @Test
    public void testComputeValue_ReturnsTrue_WhenArgumentsAreEqual() {
        expression = new CoreOperationEqual(new MockExpression("arg1"), new MockExpression("arg1"));
        EvalContext context = new EvalContext();
        
        assertEquals(Boolean.TRUE, expression.computeValue(context));
    }
    
    @Test
    public void testComputeValue_ReturnsFalse_WhenArgumentsAreNotEqual() {
        expression = new CoreOperationEqual(new MockExpression("arg1"), new MockExpression("arg2"));
        EvalContext context = new EvalContext();
        
        assertEquals(Boolean.FALSE, expression.computeValue(context));
    }
    
    @Test
    public void testComputeValue_ReturnsTrue_WhenArgumentsAreNull() {
        expression = new CoreOperationEqual(new MockExpression(null), new MockExpression(null));
        EvalContext context = new EvalContext();
        
        assertEquals(Boolean.TRUE, expression.computeValue(context));
    }
    
    @Test
    public void testComputeValue_ReturnsFalse_WhenOneArgumentIsNull() {
        expression = new CoreOperationEqual(new MockExpression(null), new MockExpression("arg1"));
        EvalContext context = new EvalContext();
        
        assertEquals(Boolean.FALSE, expression.computeValue(context));
    }
    
    @Test
    public void testComputeValue_ReturnsFalse_WhenObjectsAreDifferentTypes() {
        expression = new CoreOperationEqual(new MockExpression(1), new MockExpression("1"));
        EvalContext context = new EvalContext();
        
        assertEquals(Boolean.FALSE, expression.computeValue(context));
    }
    
    @Test
    public void testGetSymbol_ReturnsEqualsSymbol() {
        expression = new CoreOperationEqual(new MockExpression("arg1"), new MockExpression("arg1"));
        
        assertEquals("=", expression.getSymbol());
    }
    
    private class MockExpression extends Expression {
        private Object value;

        public MockExpression(Object value) {
            this.value = value;
        }

        @Override
        public Object computeValue(EvalContext context) {
            return value;
        }

        @Override
        public boolean computeContextDependent() {
            return false;
        }
    }
}

