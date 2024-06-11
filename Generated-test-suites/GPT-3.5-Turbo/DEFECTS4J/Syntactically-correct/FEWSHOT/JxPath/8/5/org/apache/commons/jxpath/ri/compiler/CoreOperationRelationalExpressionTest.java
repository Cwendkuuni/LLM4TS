import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression() {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        EvalContext context = new EvalContext();
        context.setSingleNodePointer(5);
        
        assertFalse(expression.computeValue(context));
        
        context.setSingleNodePointer(10);
        
        assertTrue(expression.computeValue(context));
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression() {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };

        assertEquals(3, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression() {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };

        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testCompute() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression() {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        assertTrue(expression.compute(5, 3));
        assertFalse(expression.compute(3, 5));
        assertFalse(expression.compute(5, 5));
    }

    @Test
    public void testReduce() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression() {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };

        Object o = new Object();
        assertEquals(o, expression.reduce(o));
        
        EvalContext context = new EvalContext();
        context.setSingleNodePointer(5);
        assertEquals(context.getSingleNodePointer(), expression.reduce(context));
        
        Collection<Object> collection = new ArrayList<>();
        collection.add(1);
        collection.add(2);
        Iterator<Object> iterator = collection.iterator();
        assertEquals(iterator, expression.reduce(collection));
    }

    @Test
    public void testContainsMatch() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression() {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };

        Iterator<Object> iterator = new ArrayList<>().iterator();
        Object element1 = 1;
        Object element2 = 2;
        assertFalse(expression.containsMatch(iterator, element1));
        
        iterator = new ArrayList<>(Arrays.asList(element1, element2)).iterator();
        assertTrue(expression.containsMatch(iterator, element2));
        assertFalse(expression.containsMatch(iterator, 3));
    }

    @Test
    public void testFindMatch() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression() {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };

        Iterator<Object> leftIterator = new ArrayList<>().iterator();
        Iterator<Object> rightIterator = new ArrayList<>().iterator();
        assertFalse(expression.findMatch(leftIterator, rightIterator));
        
        leftIterator = new ArrayList<>(Arrays.asList(1, 2)).iterator();
        rightIterator = new ArrayList<>(Arrays.asList(2, 3)).iterator();
        assertTrue(expression.findMatch(leftIterator, rightIterator));
        
        leftIterator = new ArrayList<>(Arrays.asList(1, 2)).iterator();
        rightIterator = new ArrayList<>(Arrays.asList(3, 4)).iterator();
        assertFalse(expression.findMatch(leftIterator, rightIterator));
    }

}