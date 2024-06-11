package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    private CoreOperationRelationalExpression expression;
    
    @Before
    public void setUp() {
        Expression[] args = new Expression[2];
        args[0] = new XPathExpression();
        args[1] = new XPathExpression();
        expression = new CoreOperationRelationalExpression(args) {

			@Override
			protected boolean evaluateCompare(int compare) {
				return compare == 0;
			}
		};
    }
    
    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        context.getSingleNodePointer().setValue(5);
        
        assertEquals(Boolean.TRUE, expression.computeValue(context));
    }
    
    @Test
    public void testGetPrecedence() {
        assertEquals(CoreOperation.RELATIONAL_EXPR_PRECEDENCE, expression.getPrecedence());
    }
    
    @Test
    public void testIsSymmetric() {
        assertEquals(false, expression.isSymmetric());
    }
    
    @Test
    public void testCompute() {
        Object left = 5;
        Object right = 5;
        
        assertEquals(Boolean.TRUE, expression.compute(left, right));
    }
    
    @Test
    public void testReduce() {
        Object o = new Object();
        
        assertEquals(o, expression.reduce(o));
    }
    
    @Test
    public void testContainsMatch() {
        Iterator it = new Iterator() {

			@Override
			public boolean hasNext() {
				return true;
			}

			@Override
			public Object next() {
				return 5;
			}
		};
        
        Object value = 5;
        
        assertEquals(true, expression.containsMatch(it, value));
    }
    
    @Test
    public void testFindMatch() {
        Iterator lit = new Iterator() {

			@Override
			public boolean hasNext() {
				return true;
			}

			@Override
			public Object next() {
				return 1;
			}
		};
        
        Iterator rit = new Iterator() {

			@Override
			public boolean hasNext() {
				return true;
			}

			@Override
			public Object next() {
				return 2;
			}
		};
        
        assertEquals(false, expression.findMatch(lit, rit));
    }
    
}