import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {
    
    private CoreOperationRelationalExpression coreOperationRelationalExpression;
    
    @Before
    public void setUp() {
        Expression[] args = new Expression[2];
        
        coreOperationRelationalExpression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
    }
    
    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        
        Object result = coreOperationRelationalExpression.computeValue(context);
        
        assertNotNull(result);
    }
    
    @Test
    public void testGetPrecedence() {
        int precedence = coreOperationRelationalExpression.getPrecedence();
        
        assertEquals(3, precedence);
    }
    
    @Test
    public void testIsSymmetric() {
        boolean symmetric = coreOperationRelationalExpression.isSymmetric();
        
        assertFalse(symmetric);
    }
    
    @Test
    public void testCompute() {
        Object left = new Object();
        Object right = new Object();
        
        boolean result = coreOperationRelationalExpression.compute(left, right);
        
        assertFalse(result);
    }
    
    @Test
    public void testReduce() {
        Object o = new Object();
        
        Object result = coreOperationRelationalExpression.reduce(o);
        
        assertNotNull(result);
    }
    
    @Test
    public void testContainsMatch() {
        Iterator it = new Iterator() {
            @Override
            public boolean hasNext() {
                return false;
            }
            
            @Override
            public Object next() {
                return null;
            }
        };
        Object value = new Object();
        
        boolean result = coreOperationRelationalExpression.containsMatch(it, value);
        
        assertFalse(result);
    }
    
    @Test
    public void testFindMatch() {
        Iterator lit = new Iterator() {
            @Override
            public boolean hasNext() {
                return false;
            }
            
            @Override
            public Object next() {
                return null;
            }
        };
        Iterator rit = new Iterator() {
            @Override
            public boolean hasNext() {
                return false;
            }
            
            @Override
            public Object next() {
                return null;
            }
        };
        
        boolean result = coreOperationRelationalExpression.findMatch(lit, rit);
        
        assertFalse(result);
    }
}