import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationGreaterThanTest {

    private CoreOperationGreaterThan expression;

    @Test
    public void testComputeValue() {
        expression = new CoreOperationGreaterThan(new MockExpression(5), new MockExpression(3));
        assertTrue((Boolean) expression.computeValue(new MockEvalContext()));
        
        expression = new CoreOperationGreaterThan(new MockExpression(3), new MockExpression(5));
        assertFalse((Boolean) expression.computeValue(new MockEvalContext()));
    }
    
    @Test
    public void testGetSymbol() {
        expression = new CoreOperationGreaterThan(new MockExpression(5), new MockExpression(3));
        assertEquals(">", expression.getSymbol());
    }
    
    @Test
    public void testComputeValueWithInvalidArgs() {
        expression = new CoreOperationGreaterThan(new MockExpression("abc"), new MockExpression(2));
        assertNull(expression.computeValue(new MockEvalContext()));
        
        expression = new CoreOperationGreaterThan(new MockExpression(2), new MockExpression("abc"));
        assertNull(expression.computeValue(new MockEvalContext()));
        
        expression = new CoreOperationGreaterThan(new MockExpression("abc"), new MockExpression("def"));
        assertNull(expression.computeValue(new MockEvalContext()));
    }
    
    private class MockExpression extends Expression {
        
        private Object value;
        
        public MockExpression(Object value) {
            this.value = value;
        }
        
        public Object computeValue(EvalContext context) {
            return value;
        }
        
        public String getSymbol() {
            return null;
        }
    }
    
    private class MockEvalContext extends EvalContext {
        
        public MockEvalContext() {
            super(null);
        }
        
        public MockEvalContext(Object node) {
            super(node);
        }
        
        public Object getValue(String xpath) {
            return null;
        }
        
        public Object getVariable(String var) {
            return null;
        }
        
        public Object getFunction(String name, String prefix, Object[] args) {
            return null;
        }
        
        public String getNamespaceURI(String prefix) {
            return null;
        }
        
        public boolean isSet(int property) {
            return false;
        }
        
        public Object setProperty(int property, Object value) {
            return null;
        }
        
        public void clearProperty(int property) {
            
        }
        
        public void appendWarning(String warning, Exception e) {
            
        }
    }
}