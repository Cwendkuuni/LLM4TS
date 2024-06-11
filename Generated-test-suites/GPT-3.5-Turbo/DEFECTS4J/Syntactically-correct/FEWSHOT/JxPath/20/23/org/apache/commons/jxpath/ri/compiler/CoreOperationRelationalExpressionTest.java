
package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CoreOperationRelationalExpressionTest {
    
    @Test
    public void testComputeValue() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        
        assertEquals(Boolean.FALSE, expression.computeValue(null));
    }
    
    @Test
    public void testGetPrecedence() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        
        assertEquals(5, expression.getPrecedence());
    }
    
    @Test
    public void testIsSymmetric() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        
        assertEquals(false, expression.isSymmetric());
    }
    
    @Test
    public void testReduce() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        
        assertEquals(null, expression.reduce(null));
    }
    
    @Test
    public void testContainsMatch() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        
        assertEquals(false, expression.containsMatch(null, null));
    }
    
    @Test
    public void testFindMatch() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        
        assertEquals(false, expression.findMatch(null, null));
    }
}    
