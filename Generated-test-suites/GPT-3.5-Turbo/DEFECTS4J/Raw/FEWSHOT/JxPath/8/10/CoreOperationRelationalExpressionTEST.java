package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class CoreOperationRelationalExpressionTest {
    @Test
    public void testComputeValue() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        
        Object result1 = expression.computeValue(null);
        assertEquals(Boolean.FALSE, result1);
        
        Object result2 = expression.computeValue(null);
        assertEquals(Boolean.TRUE, result2);
    }
    
    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        
        int precedence = expression.getPrecedence();
        assertEquals(3, precedence);
    }
    
    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        
        boolean symmetric = expression.isSymmetric();
        assertFalse(symmetric);
    }
    
    @Test
    public void testCompute() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        
        Object result1 = expression.compute(null, null);
        assertEquals(false, result1);
        
        Object result2 = expression.compute(null, "test");
        assertEquals(false, result2);
        
        Object result3 = expression.compute("test", null);
        assertEquals(false, result3);
        
        Object result4 = expression.compute("test1", "test2");
        assertEquals(false, result4);
    }
    
    @Test
    public void testReduce() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        
        Object result1 = expression.reduce(null);
        assertEquals(null, result1);
        
        Object result2 = expression.reduce("test");
        assertEquals("test", result2);
    }
    
    @Test
    public void testContainsMatch() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        
        try {
            expression.containsMatch(null, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            assertTrue(true);
        }
    }
    
    @Test
    public void testFindMatch() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        
        try {
            expression.findMatch(null, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            assertTrue(true);
        }
    }
}