package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new Expression[2]) {
            
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        
        assertTrue((boolean) expr.computeValue(null));
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new Expression[2]) {
            
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        
        assertEquals(CoreOperation.RELATIONAL_EXPR_PRECEDENCE, expr.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new Expression[2]) {
            
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        
        assertFalse(expr.isSymmetric());
    }

    @Test
    public void testCompute() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new Expression[2]) {
            
            @Override
            protected boolean evaluateCompare(int compare) {
                if (compare > 0) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        
        assertTrue(expr.compute(5, 3));
    }

    @Test
    public void testReduce() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new Expression[2]) {
            
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        
        assertEquals(3, expr.reduce(3));
    }

    @Test
    public void testContainsMatch() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new Expression[2]) {
            
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        
        assertTrue(expr.containsMatch(new IteratorImpl<>("a", "b", "c"), "b"));
    }

    @Test
    public void testFindMatch() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new Expression[2]) {
            
            @Override
            protected boolean evaluateCompare(int compare) {
                return true;
            }
        };
        
        assertTrue(expr.findMatch(new IteratorImpl<>("a", "b", "c"), new IteratorImpl<>("x", "y", "b")));
    }

}