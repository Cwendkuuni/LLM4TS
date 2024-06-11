import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;


public class CoreOperationRelationalExpressionTest {
    
    @Test
    public void testComputeValue() {
        // Test case 1
        Expression[] args1 = new Expression[]{new Expression(), new Expression()};
        CoreOperationRelationalExpression exp1 = new CoreOperationRelationalExpression(args1) {
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        EvalContext context1 = new EvalContext();
        Object result1 = exp1.computeValue(context1);
        assertEquals(false, result1);
        
        // Test case 2
        Expression[] args2 = new Expression[]{new Expression(), new Expression()};
        CoreOperationRelationalExpression exp2 = new CoreOperationRelationalExpression(args2) {
            protected boolean evaluateCompare(int compare) {
                return true;
            }
        };
        EvalContext context2 = new EvalContext();
        Object result2 = exp2.computeValue(context2);
        assertEquals(true, result2);
    }
    
    @Test
    public void testGetPrecedence() {
        Expression[] args = new Expression[]{new Expression(), new Expression()};
        CoreOperationRelationalExpression exp = new CoreOperationRelationalExpression(args) {
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        int result = exp.getPrecedence();
        assertEquals(3, result);
    }
    
    @Test
    public void testIsSymmetric() {
        Expression[] args = new Expression[]{new Expression(), new Expression()};
        CoreOperationRelationalExpression exp = new CoreOperationRelationalExpression(args) {
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        boolean result = exp.isSymmetric();
        assertEquals(false, result);
    }
    
    @Test
    public void testCompute() {
        Expression[] args = new Expression[]{new Expression(), new Expression()};
        CoreOperationRelationalExpression exp = new CoreOperationRelationalExpression(args) {
            protected boolean evaluateCompare(int compare) {
                return true;
            }
        };
        EvalContext context = new EvalContext();
        Object left = new Object();
        Object right = new Object();
        Object result = exp.compute(left, right);
        assertEquals(true, result);
    }
    
    @Test
    public void testReduce() {
        Expression[] args = new Expression[]{new Expression(), new Expression()};
        CoreOperationRelationalExpression exp = new CoreOperationRelationalExpression(args) {
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        Object o = new Object();
        Object result = exp.reduce(o);
        assertEquals(o, result);
    }
    
    @Test
    public void testContainsMatch() {
        Expression[] args = new Expression[]{new Expression(), new Expression()};
        CoreOperationRelationalExpression exp = new CoreOperationRelationalExpression(args) {
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        Iterator it = new Iterator();
        Object value = new Object();
        boolean result = exp.containsMatch(it, value);
        assertEquals(false, result);
    }
    
    @Test
    public void testFindMatch() {
        Expression[] args = new Expression[]{new Expression(), new Expression()};
        CoreOperationRelationalExpression exp = new CoreOperationRelationalExpression(args) {
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        Iterator lit = new Iterator();
        Iterator rit = new Iterator();
        boolean result = exp.findMatch(lit, rit);
        assertEquals(false, result);
    }
}