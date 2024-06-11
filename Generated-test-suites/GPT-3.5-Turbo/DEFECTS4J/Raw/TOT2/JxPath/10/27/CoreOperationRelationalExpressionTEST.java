import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        // Create test objects
        EvalContext context = new EvalContext();
        
        // Test when left and right are equal
        CoreOperationRelationalExpression coreOp = new CoreOperationRelationalExpressionImpl();
        context.getVariables().declareVariable("left", 5);
        context.getVariables().declareVariable("right", 5);
        assertEquals(Boolean.TRUE, coreOp.computeValue(context));
        
        // Test when left is less than right
        context.getVariables().declareVariable("left", 3);
        context.getVariables().declareVariable("right", 5);
        assertEquals(Boolean.TRUE, coreOp.computeValue(context));
        
        // Test when left is greater than right
        context.getVariables().declareVariable("left", 8);
        context.getVariables().declareVariable("right", 5);
        assertEquals(Boolean.FALSE, coreOp.computeValue(context));
    }

    @Test
    public void testEvaluateCompare() {
        // Create test objects
        CoreOperationRelationalExpression coreOp = new CoreOperationRelationalExpressionImpl();
        
        // Test positive compare
        assertTrue(coreOp.evaluateCompare(1));
        
        // Test zero compare
        assertTrue(coreOp.evaluateCompare(0));
        
        // Test negative compare
        assertFalse(coreOp.evaluateCompare(-1));
    }

    @Test
    public void testContainsMatch() {
        // Create test objects
        CoreOperationRelationalExpression coreOp = new CoreOperationRelationalExpressionImpl();
        Iterator it = new Iterator<Integer>() {
            List<Integer> values = Arrays.asList(1, 2, 3, 4, 5);
            int index = 0;
            
            @Override
            public boolean hasNext() {
                return index < values.size();
            }

            @Override
            public Integer next() {
                return values.get(index++);
            }
        };
        int value = 3;
        
        // Test for match
        assertTrue(coreOp.containsMatch(it, value));
        
        // Test for no match
        assertFalse(coreOp.containsMatch(it, 6));
    }

    @Test
    public void testFindMatch() {
        // Create test objects
        CoreOperationRelationalExpression coreOp = new CoreOperationRelationalExpressionImpl();
        Iterator lit = new Iterator<Integer>() {
            List<Integer> values = Arrays.asList(1, 2, 3);
            int index = 0;
            
            @Override
            public boolean hasNext() {
                return index < values.size();
            }

            @Override
            public Integer next() {
                return values.get(index++);
            }
        };
        Iterator rit = new Iterator<Integer>() {
            List<Integer> values = Arrays.asList(2, 4, 6);
            int index = 0;
            
            @Override
            public boolean hasNext() {
                return index < values.size();
            }

            @Override
            public Integer next() {
                return values.get(index++);
            }
        };
        
        // Test for match
        assertTrue(coreOp.findMatch(lit, rit));
        
        // Test for no match
        assertFalse(coreOp.findMatch(lit, rit));
    }

    // Private implementation of CoreOperationRelationalExpression for testing purposes
    private class CoreOperationRelationalExpressionImpl extends CoreOperationRelationalExpression {
        
        public CoreOperationRelationalExpressionImpl() {
            super(null);
        }

        @Override
        protected boolean evaluateCompare(int compare) {
            return compare > 0;
        }
    }
}