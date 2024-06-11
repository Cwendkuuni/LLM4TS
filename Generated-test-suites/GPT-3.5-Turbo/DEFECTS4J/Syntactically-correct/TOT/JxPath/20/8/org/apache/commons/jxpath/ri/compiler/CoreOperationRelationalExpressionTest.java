import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.EvalContext;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class CoreOperationRelationalExpressionTest {
    
    @Test
    public void testComputeValue_ReturnsTrue_WhenComparisonIsTrue() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };
        
        EvalContext context = new EvalContext();
        context.setSingleNodePointer(new SelfContext(context));
        
        Object result = expression.computeValue(context);
        
        assertTrue(result instanceof Boolean);
        assertTrue((boolean) result);
    }
    
    @Test
    public void testComputeValue_ReturnsFalse_WhenComparisonIsFalse() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 1;
            }
        };
        
        EvalContext context = new EvalContext();
        context.setSingleNodePointer(new SelfContext(context));
        
        Object result = expression.computeValue(context);
        
        assertTrue(result instanceof Boolean);
        assertFalse((boolean) result);
    }
    
    @Test
    public void testGetPrecedence_ReturnsRelationalExprPrecedence() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        
        int result = expression.getPrecedence();
        
        assertEquals(CoreOperation.RELATIONAL_EXPR_PRECEDENCE, result);
    }
    
    @Test
    public void testIsSymmetric_ReturnsFalse() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        
        boolean result = expression.isSymmetric();
        
        assertFalse(result);
    }
    
    @Test
    public void testCompute_ReturnsTrue_WhenComparisonIsTrue() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };
        
        Object left = 5;
        Object right = 5;
        
        boolean result = expression.compute(left, right);
        
        assertTrue(result);
    }
    
    @Test
    public void testCompute_ReturnsFalse_WhenComparisonIsFalse() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 1;
            }
        };
        
        Object left = 5;
        Object right = 10;
        
        boolean result = expression.compute(left, right);
        
        assertFalse(result);
    }
    
    @Test
    public void testCompute_ReturnsFalse_WhenLeftIsNaN() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        
        Object left = Double.NaN;
        Object right = 10;
        
        boolean result = expression.compute(left, right);
        
        assertFalse(result);
    }
    
    @Test
    public void testCompute_ReturnsFalse_WhenRightIsNaN() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        
        Object left = 5;
        Object right = Double.NaN;
        
        boolean result = expression.compute(left, right);
        
        assertFalse(result);
    }
    
    @Test
    public void testCompute_ReturnsTrue_WhenIteratorElementsMatch() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };
        
        Iterator left = new HashSet<Integer>(Arrays.asList(1, 2, 3)).iterator();
        Iterator right = new HashSet<Integer>(Arrays.asList(2, 3, 4)).iterator();
        
        boolean result = expression.compute(left, right);
        
        assertTrue(result);
    }
    
    @Test
    public void testCompute_ReturnsFalse_WhenNoMatchInIterator() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };
        
        Iterator left = new HashSet<Integer>(Arrays.asList(1, 2, 3)).iterator();
        Iterator right = new HashSet<Integer>(Arrays.asList(4, 5, 6)).iterator();
        
        boolean result = expression.compute(left, right);
        
        assertFalse(result);
    }
    
    @Test
    public void testReduce_ReturnsSelfContextAsSingleNodePointer() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        
        Object o = new SelfContext(new EvalContext());
        
        Object result = expression.reduce(o);
        
        assertTrue(result instanceof SelfContext);
        assertEquals(o, result);
    }
    
    @Test
    public void testReduce_ReturnsIteratorForCollection() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        
        Object o = new HashSet<Integer>(Arrays.asList(1, 2, 3));
        
        Object result = expression.reduce(o);
        
        assertTrue(result instanceof Iterator);
    }
    
    @Test
    public void testContainsMatch_ReturnsTrue_WhenMatchFound() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };
        
        Iterator it = new HashSet<Integer>(Arrays.asList(1, 2, 3)).iterator();
        Object value = 2;
        
        boolean result = expression.containsMatch(it, value);
        
        assertTrue(result);
    }
    
    @Test
    public void testContainsMatch_ReturnsFalse_WhenNoMatchFound() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };
        
        Iterator it = new HashSet<Integer>(Arrays.asList(1, 2, 3)).iterator();
        Object value = 4;
        
        boolean result = expression.containsMatch(it, value);
        
        assertFalse(result);
    }
    
    @Test
    public void testFindMatch_ReturnsTrue_WhenIntersectionExists() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };
        
        Iterator lit = new HashSet<Integer>(Arrays.asList(1, 2, 3)).iterator();
        Iterator rit = new HashSet<Integer>(Arrays.asList(2, 3, 4)).iterator();
        
        boolean result = expression.findMatch(lit, rit);
        
        assertTrue(result);
    }
    
    @Test
    public void testFindMatch_ReturnsFalse_WhenIntersectionDoesNotExist() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };
        
        Iterator lit = new HashSet<Integer>(Arrays.asList(1, 2, 3)).iterator();
        Iterator rit = new HashSet<Integer>(Arrays.asList(4, 5, 6)).iterator();
        
        boolean result = expression.findMatch(lit, rit);
        
        assertFalse(result);
    }
}