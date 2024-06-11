import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class CoreOperationRelationalExpressionTest {
    
    @Test
    public void testComputeValue() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{new Expression(), new Expression()}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        
        EvalContext context = new EvalContext();
        context.setSingleNodePointer(new SelfContext());

        context.setSingleNodePointer(new InitialContext());

        Object result = expression.computeValue(context);
        
        assertTrue(result instanceof Boolean);
    }
    
    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{new Expression(), new Expression()}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        
        int precedence = expression.getPrecedence();
        
        assertEquals(3, precedence);
    }
    
    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{new Expression(), new Expression()}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        
        boolean isSymmetric = expression.isSymmetric();
        
        assertFalse(isSymmetric);
    }
    
    @Test
    public void testCompute() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{new Expression(), new Expression()}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        
        Object left = 5;
        Object right = 10;

        boolean result = expression.compute(left, right);
        
        assertFalse(result);
    }
    
    @Test
    public void testReduce() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{new Expression(), new Expression()}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        
        Object o = new SelfContext();
        
        Object result = expression.reduce(o);
        
        assertNotNull(result);
    }
    
    @Test
    public void testContainsMatch() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{new Expression(), new Expression()}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        
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
        Object value = 5;

        boolean result = expression.containsMatch(it, value);
        
        assertFalse(result);
    }
    
    @Test
    public void testFindMatch() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{new Expression(), new Expression()}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        
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

        boolean result = expression.findMatch(lit, rit);
        
        assertFalse(result);
    }

}