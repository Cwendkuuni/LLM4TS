package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression[] args = new Expression[2];
        args[0] = new MockExpression();
        args[1] = new MockExpression();
        
        CoreOperationRelationalExpression expr = new MockCoreOperationRelationalExpression(args);
        
        assertEquals(Boolean.TRUE, expr.computeValue(new EvalContext()));
        
        args[0] = null;
        args[1] = null;
        assertNull(expr.computeValue(new EvalContext()));
        
        args[0] = new MockExpression();
        args[1] = null;
        assertNull(expr.computeValue(new EvalContext()));
        
        args[0] = null;
        args[1] = new MockExpression();
        assertNull(expr.computeValue(new EvalContext()));
    }

    @Test
    public void testGetPrecedence() {
        Expression[] args = new Expression[2];
        args[0] = new MockExpression();
        args[1] = new MockExpression();
        
        CoreOperationRelationalExpression expr = new MockCoreOperationRelationalExpression(args);
        
        assertEquals(CoreOperation.RELATIONAL_EXPR_PRECEDENCE, expr.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        Expression[] args = new Expression[2];
        args[0] = new MockExpression();
        args[1] = new MockExpression();
        
        CoreOperationRelationalExpression expr = new MockCoreOperationRelationalExpression(args);
        
        assertFalse(expr.isSymmetric());
    }

    @Test
    public void testCompute() {
        Expression[] args = new Expression[2];
        args[0] = new MockExpression();
        args[1] = new MockExpression();
        
        CoreOperationRelationalExpression expr = new MockCoreOperationRelationalExpression(args);
        
        assertFalse(expr.compute(null, null));
        assertFalse(expr.compute(new Object(), null));
        assertFalse(expr.compute(null, new Object()));
        
        assertTrue(expr.compute(new Object(), new Object()));
    }

    @Test
    public void testReduce() {
        Expression[] args = new Expression[2];
        args[0] = new MockExpression();
        args[1] = new MockExpression();
        
        CoreOperationRelationalExpression expr = new MockCoreOperationRelationalExpression(args);
        
        Object o = new Object();
        
        assertSame(o, expr.reduce(o));
        
        o = new CoreOperationRelationalExpressionTest();
        assertSame(o, expr.reduce(o));
        
        o = new InitialContext();
        assertSame(o, expr.reduce(o));
        
        o = new Iterator();
        assertSame(o, expr.reduce(o));
        
        o = new SelfContext();
        EvalContext context = new EvalContext();
        context.setSingleNodePointer(new Object());
        assertSame(o, expr.reduce(o));
    }

    @Test
    public void testContainsMatch() {
        Expression[] args = new Expression[2];
        args[0] = new MockExpression();
        args[1] = new MockExpression();
        
        CoreOperationRelationalExpression expr = new MockCoreOperationRelationalExpression(args);
        
        Iterator it = new Iterator() {
            
            private int count = 0;
            
            @Override
            public Object next() {
                return count++;
            }
            
            @Override
            public boolean hasNext() {
                return count < 5;
            }
        };
        
        assertTrue(expr.containsMatch(it, new Object()));
        
        assertFalse(expr.containsMatch(new Iterator() {
            
            private int count = 0;
            
            @Override
            public Object next() {
                return count++;
            }
            
            @Override
            public boolean hasNext() {
                return count == 2 ? false : true;
            }
        }, new Object()));
    }

    @Test
    public void testFindMatch() {
        Expression[] args = new Expression[2];
        args[0] = new MockExpression();
        args[1] = new MockExpression();
        
        CoreOperationRelationalExpression expr = new MockCoreOperationRelationalExpression(args);
        
        Iterator lit = new Iterator() {
            
            private int count = 0;
            
            @Override
            public Object next() {
                return count++;
            }
            
            @Override
            public boolean hasNext() {
                return count < 2;
            }
        };
        
        Iterator rit = new Iterator() {
            
            private int count = 0;
            
            @Override
            public Object next() {
                return count++;
            }
            
            @Override
            public boolean hasNext() {
                return count < 3;
            }
        };
        
        assertTrue(expr.findMatch(lit, rit));
        
        rit = new Iterator() {
            
            private int count = 0;
            
            @Override
            public Object next() {
                return count++;
            }
            
            @Override
            public boolean hasNext() {
                return count < 2;
            }
        };
        
        assertFalse(expr.findMatch(lit, rit));
    }
    
    private class MockExpression extends Expression {
        
        @Override
        public Object computeValue(EvalContext context) {
            return null;
        }
        
    }
    
    private class MockCoreOperationRelationalExpression extends CoreOperationRelationalExpression {
        
        protected MockCoreOperationRelationalExpression(Expression[] args) {
            super(args);
        }
        
        @Override
        protected boolean evaluateCompare(int compare) {
            return false;
        }
        
    }

}