import org.junit.Assert;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };
        
        EvalContext context = new EvalContext();
        args[0] = new TestExpression();
        args[1] = new TestExpression();
        
        Object result = expression.computeValue(context);
        
        Assert.assertEquals(Boolean.TRUE, result);
    }
    
    @Test
    public void testGetPrecedence() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };
        
        int result = expression.getPrecedence();
        
        Assert.assertEquals(CoreOperation.RELATIONAL_EXPR_PRECEDENCE, result);
    }
    
    @Test
    public void testIsSymmetric() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };
        
        boolean result = expression.isSymmetric();
        
        Assert.assertEquals(false, result);
    }
    
    @Test
    public void testCompute() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };
        
        Object result = expression.compute("left", "right");
        
        Assert.assertEquals(Boolean.TRUE, result);
    }
    
    @Test
    public void testReduce() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };
        
        Object result = expression.reduce("value");
        
        Assert.assertEquals("value", result);
    }
    
    @Test
    public void testContainsMatch() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };
        
        TestIterator iterator = new TestIterator();
        boolean result = expression.containsMatch(iterator, "value");
        
        Assert.assertEquals(false, result);
    }
    
    @Test
    public void testFindMatch() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };
        
        TestIterator lit = new TestIterator();
        TestIterator rit = new TestIterator();
        boolean result = expression.findMatch(lit, rit);
        
        Assert.assertEquals(false, result);
    }
    
    private class TestExpression extends Expression {
        @Override
        public Object computeValue(EvalContext context) {
            return "test";
        }
    }
    
    private class TestIterator implements Iterator {
        @Override
        public boolean hasNext() {
            return false;
        }
        
        @Override
        public Object next() {
            return null;
        }
        
        @Override
        public void remove() {
            
        }
    }
}