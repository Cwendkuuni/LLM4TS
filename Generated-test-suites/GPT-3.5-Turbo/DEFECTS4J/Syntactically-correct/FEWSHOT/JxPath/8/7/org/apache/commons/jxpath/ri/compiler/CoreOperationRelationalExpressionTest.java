package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression() {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        
        EvalContext context = new EvalContext();
        context.setSingleNodePointer(new SelfContext());
        
        Object result = expression.computeValue(context);
        
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression() {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        
        int result = expression.getPrecedence();
        
        assertEquals(3, result);
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression() {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        
        boolean result = expression.isSymmetric();
        
        assertEquals(false, result);
    }

    @Test
    public void testCompute() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression() {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        
        Object left = new InitialContext();
        Object right = new InitialContext();
        
        boolean result = expression.compute(left, right);
        
        assertEquals(false, result);
    }

    @Test
    public void testReduce() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression() {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        
        Object o = new SelfContext();
        
        Object result = expression.reduce(o);
        
        assertEquals(((SelfContext) o).getSingleNodePointer(), result);
    }

    @Test
    public void testContainsMatch() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression() {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
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
        
        Object value = new Object();
        
        boolean result = expression.containsMatch(it, value);
        
        assertEquals(false, result);
    }

    @Test
    public void testFindMatch() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression() {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
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
        
        assertEquals(false, result);
    }

}