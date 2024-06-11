import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression[] args = new Expression[2];
        args[0] = new ExpressionMock();
        args[1] = new ExpressionMock();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionMock(args);
        Object result = expression.computeValue(new EvalContext());
        assertNotNull(result);
    }
    
    @Test
    public void testGetPrecedence() {
        Expression[] args = new Expression[2];
        args[0] = new ExpressionMock();
        args[1] = new ExpressionMock();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionMock(args);
        int result = expression.getPrecedence();
        assertEquals(3, result);
    }
    
    @Test
    public void testIsSymmetric() {
        Expression[] args = new Expression[2];
        args[0] = new ExpressionMock();
        args[1] = new ExpressionMock();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionMock(args);
        boolean result = expression.isSymmetric();
        assertFalse(result);
    }
    
    @Test
    public void testEvaluateCompare() {
        Expression[] args = new Expression[2];
        args[0] = new ExpressionMock();
        args[1] = new ExpressionMock();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionMock(args);
        boolean result = expression.evaluateCompare(0);
        assertTrue(result);
    }
    
    @Test
    public void testComputeReduce() {
        Expression[] args = new Expression[2];
        args[0] = new ExpressionMock();
        args[1] = new ExpressionMock();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionMock(args);
        Object result = expression.compute(new InitialContext(), new InitialContext());
        assertNotNull(result);
    }
    
    @Test
    public void testComputeIterator() {
        Expression[] args = new Expression[2];
        args[0] = new ExpressionMock();
        args[1] = new ExpressionMock();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionMock(args);
        
        Iterator<Integer> left = new HashSet<Integer>().iterator();
        Iterator<Integer> right = new HashSet<Integer>().iterator();
        
        boolean result = expression.compute(left, right);
        assertFalse(result);
    }
    
    @Test
    public void testContainsMatch() {
        Expression[] args = new Expression[2];
        args[0] = new ExpressionMock();
        args[1] = new ExpressionMock();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionMock(args);
        
        Iterator<Integer> it = new HashSet<Integer>().iterator();
        int value = 10;
        
        boolean result = expression.containsMatch(it, value);
        assertFalse(result);
    }
    
    @Test
    public void testFindMatch() {
        Expression[] args = new Expression[2];
        args[0] = new ExpressionMock();
        args[1] = new ExpressionMock();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionMock(args);
        
        Iterator<Integer> left = new HashSet<Integer>().iterator();
        Iterator<Integer> right = new HashSet<Integer>().iterator();
        
        boolean result = expression.findMatch(left, right);
        assertFalse(result);
    }
    
    private class ExpressionMock implements Expression {
        public Object computeValue(EvalContext context) {
            return null;
        }
    }
    
    private class CoreOperationRelationalExpressionMock extends CoreOperationRelationalExpression {
        public CoreOperationRelationalExpressionMock(Expression[] args){
            super(args);
        }
        protected boolean evaluateCompare(int compare) {
            return compare == 0;
        }
    }
}