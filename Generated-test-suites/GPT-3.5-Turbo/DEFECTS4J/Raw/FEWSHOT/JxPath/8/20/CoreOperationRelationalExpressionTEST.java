import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {
    @Test
    public void testComputeValue() {
        Expression[] args = new Expression[2];
        args[0] = new CoreOperationRelationalExpressionMock();
        args[1] = new CoreOperationRelationalExpressionMock();
        
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionMock();
        Object result = expression.computeValue(args);
        assertTrue(result instanceof Boolean);
    }
    
    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionMock();
        int precedence = expression.getPrecedence();
        assertEquals(3, precedence);
    }
    
    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionMock();
        boolean symmetric = expression.isSymmetric();
        assertFalse(symmetric);
    }
    
    @Test
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionMock();
        boolean evaluateCompare = expression.evaluateCompare(0);
        assertTrue(evaluateCompare);
    }
    
    @Test
    public void testCompute() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionMock();
        boolean compute = expression.compute(1, 2);
        assertTrue(compute);
    }
    
    @Test
    public void testReduce() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionMock();
        Object reduce = expression.reduce(new CoreOperationRelationalExpressionMock());
        assertTrue(reduce instanceof CoreOperationRelationalExpressionMock);
    }
    
    @Test
    public void testContainsMatch() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionMock();
        boolean containsMatch = expression.containsMatch(new CoreOperationRelationalExpressionIteratorMock(), new CoreOperationRelationalExpressionMock());
        assertFalse(containsMatch);
    }
    
    @Test
    public void testFindMatch() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionMock();
        boolean findMatch = expression.findMatch(new CoreOperationRelationalExpressionIteratorMock(), new CoreOperationRelationalExpressionIteratorMock());
        assertFalse(findMatch);
    }
    
    private class CoreOperationRelationalExpressionMock extends CoreOperationRelationalExpression {
        public CoreOperationRelationalExpressionMock() {
			super(null);
		}
        protected boolean evaluateCompare(int compare) {
            return true;
        }
    }
    
    private class CoreOperationRelationalExpressionIteratorMock implements java.util.Iterator {
    	public boolean hasNext() {
    		return false;
    	}
    	
    	public Object next() {
    		return null;
    	}
    	
    	public void remove() {
    		
    	}
    }
}