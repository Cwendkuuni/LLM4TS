import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression[] args = new Expression[2];
        args[0] = new DummyExpression();
        args[1] = new DummyExpression();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };
        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
    }

    @Test
    public void testGetPrecedence() {
        Expression[] args = new Expression[2];
        args[0] = new DummyExpression();
        args[1] = new DummyExpression();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };
        int result = expression.getPrecedence();
        assertTrue(result == 3);
    }

    @Test
    public void testIsSymmetric() {
        Expression[] args = new Expression[2];
        args[0] = new DummyExpression();
        args[1] = new DummyExpression();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };
        boolean result = expression.isSymmetric();
        assertTrue(result == false);
    }

    @Test
    public void testCompute() {
        Expression[] args = new Expression[2];
        args[0] = new DummyExpression();
        args[1] = new DummyExpression();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };
        Object result = expression.compute("left", "right");
        assertTrue(result instanceof Boolean);
    }

    @Test
    public void testReduce() {
        Expression[] args = new Expression[2];
        args[0] = new DummyExpression();
        args[1] = new DummyExpression();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };
        Object result = expression.reduce(new Object());
        assertNotNull(result);
    }

    @Test
    public void testContainsMatch() {
        Expression[] args = new Expression[2];
        args[0] = new DummyExpression();
        args[1] = new DummyExpression();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };
        Iterator it = new DummyIterator();
        boolean result = expression.containsMatch(it, "value");
        assertTrue(result == false);
    }

    @Test
    public void testFindMatch() {
        Expression[] args = new Expression[2];
        args[0] = new DummyExpression();
        args[1] = new DummyExpression();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };
        Iterator lit = new DummyIterator();
        Iterator rit = new DummyIterator();
        boolean result = expression.findMatch(lit, rit);
        assertTrue(result == false);
    }
    
    private class DummyExpression extends Expression {
        @Override
        public Object computeValue(EvalContext evalContext) {
            return null;
        }
    }
    
    private class DummyIterator implements Iterator {
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