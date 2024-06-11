package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

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
                return compare > 0;
            }
        };
        EvalContext context = new EvalContext();
        context.getContextNodeList().add("test");
        
        assertFalse((boolean) expression.computeValue(context));
        
        context.getContextNodeList().clear();
        context.getContextNodeList().add(4);
        
        assertTrue((boolean) expression.computeValue(context));
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression() {
            
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        
        assertEquals(Expression.RELATIONAL_EXPR_PRECEDENCE, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression() {
            
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        
        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testCompute() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression() {
            
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };
        
        Object left = Arrays.asList(1, 2, 3).iterator();
        Object right = Arrays.asList(1, 2, 3).iterator();
        
        assertTrue(expression.compute(left, right));
        
        left = Arrays.asList(1, 2, 3).iterator();
        right = Arrays.asList(1, 4, 3).iterator();
        
        assertFalse(expression.compute(left, right));
    }

    @Test
    public void testReduce() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression() {
            
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        
        Object operand = new SelfContext(new EvalContext());
        
        assertEquals(operand, expression.reduce(operand));
    }

    @Test
    public void testContainsMatch() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression() {
            
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        
        Iterator it = Arrays.asList(1, 2, 3).iterator();
        Object value = 2;
        
        assertTrue(expression.containsMatch(it, value));
        
        value = 4;
        
        assertFalse(expression.containsMatch(it, value));
    }

    @Test
    public void testFindMatch() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression() {
            
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        
        Iterator lit = Arrays.asList(1, 2, 3).iterator();
        Iterator rit = Arrays.asList(3, 4, 5).iterator();
        
        assertTrue(expression.findMatch(lit, rit));
        
        lit = Arrays.asList(1, 2, 3).iterator();
        rit = Arrays.asList(4, 5, 6).iterator();
        
        assertFalse(expression.findMatch(lit, rit));
    }
}