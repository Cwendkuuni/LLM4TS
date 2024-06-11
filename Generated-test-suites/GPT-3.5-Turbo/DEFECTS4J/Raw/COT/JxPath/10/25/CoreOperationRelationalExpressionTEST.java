import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Arrays;

import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        // Create a dummy EvalContext
        EvalContext context = new EvalContext(null, null);
        
        // Create a dummy Expression[] for the args
        Expression[] args = new Expression[2];
        args[0] = new SelfContext();
        args[1] = new SelfContext();
        
        // Create an instance of CoreOperationRelationalExpression
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {

            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        
        // Test when the compare is greater than 0
        assertTrue((Boolean) expression.computeValue(context));
        
        // Test when the compare is less than or equal to 0
        expression = new CoreOperationRelationalExpression(args) {

            @Override
            protected boolean evaluateCompare(int compare) {
                return compare <= 0;
            }
        };
        
        assertFalse((Boolean) expression.computeValue(context));
    }

    @Test
    public void testGetPrecedence() {
        Expression[] args = new Expression[2];
        args[0] = new SelfContext();
        args[1] = new SelfContext();
        
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {

            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        
        assertEquals(3, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        Expression[] args = new Expression[2];
        args[0] = new SelfContext();
        args[1] = new SelfContext();
        
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {

            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        
        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testCompute() {
        Expression[] args = new Expression[2];
        args[0] = new SelfContext();
        args[1] = new SelfContext();
        
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {

            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        
        // Test when left and right are equal
        assertTrue(expression.compute(5, 5));
        
        // Test when left is less than right
        assertTrue(expression.compute(4, 5));
        
        // Test when left is greater than right
        assertFalse(expression.compute(6, 5));
        
        // Test when left and right are NaN
        assertFalse(expression.compute(Double.NaN, Double.NaN));
    }

    @Test
    public void testReduce() {
        Expression[] args = new Expression[2];
        args[0] = new SelfContext();
        args[1] = new SelfContext();
        
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {

            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        
        // Test when o is a SelfContext
        SelfContext self = new SelfContext();
        assertEquals(self.getSingleNodePointer(), expression.reduce(self));
        
        // Test when o is a Collection
        Collection<Integer> collection = Arrays.asList(1, 2, 3);
        Iterator<Integer> iterator = expression.reduce(collection);
        assertTrue(iterator.hasNext());
        assertEquals(1, (int) iterator.next());
        
        // Test when o is neither SelfContext nor Collection
        assertEquals("Test", expression.reduce("Test"));
    }

    @Test
    public void testContainsMatch() {
        Expression[] args = new Expression[2];
        args[0] = new SelfContext();
        args[1] = new SelfContext();
        
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {

            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        
        // Test when iterator is empty
        Iterator<Integer> it = Arrays.asList(1, 2, 3).iterator();
        assertFalse(expression.containsMatch(it, 4));
        
        // Test when iterator contains matching value
        assertFalse(expression.containsMatch(it, 2));
        
        // Test when iterator does not contain matching value
        assertTrue(expression.containsMatch(it, 4));
    }

    @Test
    public void testFindMatch() {
        Expression[] args = new Expression[2];
        args[0] = new SelfContext();
        args[1] = new SelfContext();
        
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {

            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        
        // Test when left is empty and right contains matching value
        Iterator<Integer> lit = Arrays.asList(1, 2, 3).iterator();
        Iterator<Integer> rit = Arrays.asList(3, 4, 5).iterator();
        assertTrue(expression.findMatch(lit, rit));
        
        // Test when left contains matching value and right does not contain matching value
        lit = Arrays.asList(1, 2, 3).iterator();
        rit = Arrays.asList(4, 5, 6).iterator();
        assertFalse(expression.findMatch(lit, rit));
        
        // Test when none of the iterators contain matching value
        assertFalse(expression.findMatch(lit, rit));
    }

}