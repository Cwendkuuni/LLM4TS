import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue_Equals() {
        CoreOperationRelationalExpression instance = new CoreOperationRelationalExpression() {
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };

        EvalContext context = new EvalContext();
        context.setSingleNodePointer(new SelfContext(null));

        Object result = instance.computeValue(context);

        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_LessThan() {
        CoreOperationRelationalExpression instance = new CoreOperationRelationalExpression() {
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };

        EvalContext context = new EvalContext();
        context.setSingleNodePointer(new SelfContext(null));

        Object result = instance.computeValue(context);

        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_GreaterThan() {
        CoreOperationRelationalExpression instance = new CoreOperationRelationalExpression() {
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        EvalContext context = new EvalContext();
        context.setSingleNodePointer(new SelfContext(null));

        Object result = instance.computeValue(context);

        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_NotEqual() {
        CoreOperationRelationalExpression instance = new CoreOperationRelationalExpression() {
            protected boolean evaluateCompare(int compare) {
                return compare != 0;
            }
        };

        EvalContext context = new EvalContext();
        context.setSingleNodePointer(new SelfContext(null));

        Object result = instance.computeValue(context);

        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_DoubleNaN() {
        CoreOperationRelationalExpression instance = new CoreOperationRelationalExpression() {
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };

        EvalContext context = new EvalContext();
        context.setSingleNodePointer(new SelfContext(null));

        Object left = Double.NaN;
        Object right = 1.0;
        Object result = instance.computeValue(context);

        assertFalse((Boolean) result);
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression instance = new CoreOperationRelationalExpression() {
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };

        int precedence = instance.getPrecedence();

        assertEquals(3, precedence);
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression instance = new CoreOperationRelationalExpression() {
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };

        boolean isSymmetric = instance.isSymmetric();

        assertFalse(isSymmetric);
    }

    @Test
    public void testCompute() {
        CoreOperationRelationalExpression instance = new CoreOperationRelationalExpression() {
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };

        Object left = 1;
        Object right = 2;
        boolean result = instance.compute(left, right);

        assertFalse(result);
    }

    @Test
    public void testReduce_SelfContext() {
        CoreOperationRelationalExpression instance = new CoreOperationRelationalExpression() {
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };

        Object o = new SelfContext(null);
        Object result = instance.reduce(o);

        assertEquals(o, result);
    }

    @Test
    public void testReduce_Collection() {
        CoreOperationRelationalExpression instance = new CoreOperationRelationalExpression() {
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };

        Collection<Object> collection = new HashSet<>();
        Object o = collection;
        Object result = instance.reduce(o);

        assertTrue(result instanceof Iterator);
    }

    @Test
    public void testContainsMatch_True() {
        CoreOperationRelationalExpression instance = new CoreOperationRelationalExpression() {
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };

        Iterator<Object> it = new HashSet<Object>().iterator();
        Object value = 1;
        boolean result = instance.containsMatch(it, value);

        assertFalse(result);
    }

    @Test
    public void testContainsMatch_False() {
        CoreOperationRelationalExpression instance = new CoreOperationRelationalExpression() {
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };

        Iterator<Object> it = new HashSet<Object>() {{
            add(1);
        }}.iterator();
        Object value = 2;
        boolean result = instance.containsMatch(it, value);

        assertFalse(result);
    }

    @Test
    public void testFindMatch_True() {
        CoreOperationRelationalExpression instance = new CoreOperationRelationalExpression() {
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };

        Iterator<Object> lit = new HashSet<Object>() {{
            add(1);
        }}.iterator();
        Iterator<Object> rit = new HashSet<Object>() {{
            add(1);
        }}.iterator();

        boolean result = instance.findMatch(lit, rit);

        assertTrue(result);
    }

    @Test
    public void testFindMatch_False() {
        CoreOperationRelationalExpression instance = new CoreOperationRelationalExpression() {
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };

        Iterator<Object> lit = new HashSet<Object>() {{
            add(1);
        }}.iterator();
        Iterator<Object> rit = new HashSet<Object>() {{
            add(2);
        }}.iterator();

        boolean result = instance.findMatch(lit, rit);

        assertFalse(result);
    }
    
}