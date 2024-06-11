import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue_LeftGreaterThanRight_ReturnsTrue() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args){ 
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        EvalContext context = new EvalContext();
        Object left = 5;
        Object right = 3;
        args[0] = new MockExpression(left);
        args[1] = new MockExpression(right);

        boolean result = (Boolean)expression.computeValue(context);

        assertTrue(result);
        
    }

    @Test
    public void testComputeValue_LeftLessThanRight_ReturnsFalse() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args){ 
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        EvalContext context = new EvalContext();
        Object left = 3;
        Object right = 5;
        args[0] = new MockExpression(left);
        args[1] = new MockExpression(right);

        boolean result = (Boolean)expression.computeValue(context);

        assertFalse(result);
        
    }

    @Test
    public void testComputeValue_LeftEqualsRight_ReturnsFalse() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args){ 
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        EvalContext context = new EvalContext();
        Object left = 5;
        Object right = 5;
        args[0] = new MockExpression(left);
        args[1] = new MockExpression(right);

        boolean result = (Boolean)expression.computeValue(context);

        assertFalse(result);
        
    }

    @Test
    public void testReduce_SelfContext_ReducedToSingleNodePointer() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args){ 
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        EvalContext context = new EvalContext();
        Object selfContext = new SelfContext();
        args[0] = new MockExpression(selfContext);
        args[1] = new MockExpression(5);

        Object result = expression.reduce(selfContext);

        assertEquals(((SelfContext)selfContext).getSingleNodePointer(), result);
        
    }

    @Test
    public void testReduce_Collection_ReducedToIterator() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args){ 
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        EvalContext context = new EvalContext();
        Object collection = new ArrayList<>();
        args[0] = new MockExpression(collection);
        args[1] = new MockExpression(5);

        Object result = expression.reduce(collection);

        assertTrue(result instanceof Iterator);
        
    }

    @Test
    public void testContainsMatch_IteratorContainsMatch_ReturnsTrue() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args){ 
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        EvalContext context = new EvalContext();
        Object left = new ArrayList<>();
        ((ArrayList)left).add(5);
        ((ArrayList)left).add(3);
        ((ArrayList)left).add(7);
        Object right = 3;
        args[0] = new MockExpression(left);
        args[1] = new MockExpression(right);

        boolean result = expression.containsMatch(((ArrayList)left).iterator(), right);

        assertTrue(result);
        
    }

    @Test
    public void testContainsMatch_IteratorDoesNotContainMatch_ReturnsFalse() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args){ 
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        EvalContext context = new EvalContext();
        Object left = new ArrayList<>();
        ((ArrayList)left).add(5);
        ((ArrayList)left).add(3);
        ((ArrayList)left).add(7);
        Object right = 2;
        args[0] = new MockExpression(left);
        args[1] = new MockExpression(right);

        boolean result = expression.containsMatch(((ArrayList)left).iterator(), right);

        assertFalse(result);
        
    }

    @Test
    public void testFindMatch_LeftContainsMatch_ReturnsTrue() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args){ 
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        EvalContext context = new EvalContext();
        Object left = new ArrayList<>();
        ((ArrayList)left).add(5);
        ((ArrayList)left).add(3);
        ((ArrayList)left).add(7);
        Object right = new ArrayList<>();
        ((ArrayList)right).add(2);
        ((ArrayList)right).add(3);
        ((ArrayList)right).add(4);
        args[0] = new MockExpression(left);
        args[1] = new MockExpression(right);

        boolean result = expression.findMatch(((ArrayList)left).iterator(), ((ArrayList)right).iterator());

        assertTrue(result);
        
    }

    @Test
    public void testFindMatch_LeftDoesNotContainMatch_ReturnsFalse() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args){ 
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        EvalContext context = new EvalContext();
        Object left = new ArrayList<>();
        ((ArrayList)left).add(5);
        ((ArrayList)left).add(3);
        ((ArrayList)left).add(7);
        Object right = new ArrayList<>();
        ((ArrayList)right).add(2);
        ((ArrayList)right).add(4);
        ((ArrayList)right).add(6);
        args[0] = new MockExpression(left);
        args[1] = new MockExpression(right);

        boolean result = expression.findMatch(((ArrayList)left).iterator(), ((ArrayList)right).iterator());

        assertFalse(result);
        
    }

    @Test
    public void testCompute_LeftAndRightAreIterators_ReturnsResultOfFindMatch() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args){ 
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        EvalContext context = new EvalContext();
        Object left = new ArrayList<>();
        ((ArrayList)left).add(5);
        ((ArrayList)left).add(3);
        ((ArrayList)left).add(7);
        Object right = new ArrayList<>();
        ((ArrayList)right).add(2);
        ((ArrayList)right).add(3);
        ((ArrayList)right).add(4);
        args[0] = new MockExpression(left);
        args[1] = new MockExpression(right);

        boolean result = expression.compute(left, right);

        boolean expectedResult = expression.findMatch(((ArrayList)left).iterator(), ((ArrayList)right).iterator());
        assertEquals(expectedResult, result);
        
    }

    @Test
    public void testCompute_LeftIsIteratorAndRightIsNotIterator_ReturnsResultOfContainsMatch() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args){ 
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        EvalContext context = new EvalContext();
        Object left = new ArrayList<>();
        ((ArrayList)left).add(5);
        ((ArrayList)left).add(3);
        ((ArrayList)left).add(7);
        Object right = 3;
        args[0] = new MockExpression(left);
        args[1] = new MockExpression(right);

        boolean result = expression.compute(left, right);

        boolean expectedResult = expression.containsMatch(((ArrayList)left).iterator(), right);
        assertEquals(expectedResult, result);
        
    }

    @Test
    public void testCompute_LeftIsNotIteratorAndRightIsIterator_ReturnsResultOfContainsMatch() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args){ 
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        EvalContext context = new EvalContext();
        Object right = new ArrayList<>();
        ((ArrayList)right).add(5);
        ((ArrayList)right).add(3);
        ((ArrayList)right).add(7);
        Object left = 3;
        args[0] = new MockExpression(left);
        args[1] = new MockExpression(right);

        boolean result = expression.compute(left, right);

        boolean expectedResult = expression.containsMatch(((ArrayList)right).iterator(), left);
        assertEquals(expectedResult, result);
        
    }

    @Test
    public void testCompute_LeftAndRightAreNumbers_ReturnsResultOfEvaluateCompare() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args){ 
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        EvalContext context = new EvalContext();
        Object left = 5;
        Object right = 3;
        args[0] = new MockExpression(left);
        args[1] = new MockExpression(right);

        boolean result = expression.compute(left, right);

        boolean expectedResult = expression.evaluateCompare(2);
        assertEquals(expectedResult, result);
        
    }
    
    private class MockExpression extends Expression {
        private Object value;
        
        public MockExpression(Object value) {
            this.value = value;
        }
        
        public Object computeValue(EvalContext context) {
            return value;
        }
        
        protected int getPrecedence() {
            return 0;
        }
        
        protected boolean isSymmetric() {
            return false;
        }
        
    }

}