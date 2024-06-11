import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression[] args = new Expression[] {new CoreOperation(), new CoreOperation()};
        
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        assertFalse(expression.computeValue(new EvalContext()));
    }

    @Test
    public void testGetPrecedence() {
        Expression[] args = new Expression[] {new CoreOperation(), new CoreOperation()};
        
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        assertEquals(3, expression.getPrecedence());
    }
    
    @Test
    public void testIsSymmetric() {
        Expression[] args = new Expression[] {new CoreOperation(), new CoreOperation()};
        
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testCompute() {
        Expression[] args = new Expression[] {new CoreOperation(), new CoreOperation()};
        
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        assertTrue(expression.compute(new InitialContext(), new InitialContext()));
    }

    @Test
    public void testReduce() {
        Expression[] args = new Expression[] {new CoreOperation(), new CoreOperation()};
        
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        assertEquals(new SelfContext(), expression.reduce(new SelfContext()));
    }

    @Test
    public void testContainsMatch() {
        Expression[] args = new Expression[] {new CoreOperation(), new CoreOperation()};
        
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        assertFalse(expression.containsMatch(new InitialContext(), new SelfContext()));
    }

    @Test
    public void testFindMatch() {
        Expression[] args = new Expression[] {new CoreOperation(), new CoreOperation()};
        
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        assertFalse(expression.findMatch(new InitialContext(), new InitialContext()));
    }

}