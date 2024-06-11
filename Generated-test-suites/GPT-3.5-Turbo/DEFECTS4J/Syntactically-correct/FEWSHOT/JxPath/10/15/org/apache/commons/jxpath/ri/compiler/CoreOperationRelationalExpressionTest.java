import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class CoreOperationRelationalExpressionTest {
    
    private CoreOperationRelationalExpression expression;
    private EvalContext context;
    
    @Before
    public void setUp() {
        Expression[] args = new Expression[2];
        expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        
        context = new EvalContext();
        context.setSingleNodePointer(new SelfContext(context));
    }
    
    @Test
    public void testComputeValue() {
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }
    
    @Test
    public void testGetPrecedence() {
        int precedence = expression.getPrecedence();
        assertEquals(3, precedence);
    }
    
    @Test
    public void testIsSymmetric() {
        boolean symmetric = expression.isSymmetric();
        assertFalse(symmetric);
    }
    
    @Test
    public void testCompute() {
        Object left = new InitialContext(new SelfContext(context));
        Object right = new InitialContext(new SelfContext(context));
        
        boolean result = expression.compute(left, right);
        assertFalse(result);
    }
    
    @Test
    public void testReduce() {
        Object o = new SelfContext(context);
        Object result = expression.reduce(o);
        assertTrue(result instanceof SelfContext);
    }
    
    @Test
    public void testReduceWithCollection() {
        Object o = new HashSet<>();
        Object result = expression.reduce(o);
        assertTrue(result instanceof Iterator);
    }
    
    @Test
    public void testContainsMatch() {
        Iterator it = new HashSet<>().iterator();
        Object value = new InitialContext(new SelfContext(context));
        
        boolean result = expression.containsMatch(it, value);
        assertFalse(result);
    }
    
    @Test
    public void testFindMatch() {
        Iterator lit = new HashSet<>().iterator();
        Iterator rit = new HashSet<>().iterator();
        
        boolean result = expression.findMatch(lit, rit);
        assertFalse(result);
    }
}