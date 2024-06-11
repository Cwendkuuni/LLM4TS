
package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {
    
    @Test
    public void testComputeValue_LesserThan() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[2]) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };
        
        assertEquals(Boolean.TRUE, expression.computeValue(null));
    }
    
    @Test
    public void testComputeValue_GreaterThan() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[2]) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        
        assertEquals(Boolean.TRUE, expression.computeValue(null));
    }
    
    @Test
    public void testComputeValue_Equal() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[2]) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };
        
        assertEquals(Boolean.TRUE, expression.computeValue(null));
    }
    
    @Test
    public void testComputeValue_LesserThan_NotTrue() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[2]) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };
        
        assertEquals(Boolean.FALSE, expression.computeValue(null));
    }
    
    @Test
    public void testComputeValue_GreaterThan_NotTrue() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[2]) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        
        assertEquals(Boolean.FALSE, expression.computeValue(null));
    }
    
    @Test
    public void testComputeValue_Equal_NotTrue() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[2]) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };
        
        assertEquals(Boolean.FALSE, expression.computeValue(null));
    }
    
    @Test
    public void testReduce_SelfContext() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[2]) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        
        SelfContext selfContext = new SelfContext(null);
        
        assertEquals(selfContext.getSingleNodePointer(), expression.reduce(selfContext));
    }
    
    @Test
    public void testReduce_Collection() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[2]) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        
        List<Object> list = Arrays.asList(1, 2, 3);
        
        Iterator<?> iterator = expression.reduce(list);
        
        assertTrue(iterator.hasNext());
        assertEquals(1, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(2, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(3, iterator.next());
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testContainsMatch_ContainsMatch() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[2]) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        
        List<Integer> list = Arrays.asList(1, 2, 3);
        
        Iterator<Integer> iterator = list.iterator();
        assertTrue(expression.containsMatch(iterator, 2));
    }
    
    @Test
    public void testContainsMatch_ContainsMatch_NotTrue() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[2]) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        
        List<Integer> list = Arrays.asList(1, 2, 3);
        
        Iterator<Integer> iterator = list.iterator();
        assertFalse(expression.containsMatch(iterator, 4));
    }
    
    @Test
    public void testFindMatch_FindMatch() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[2]) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        
        List<Integer> left = Arrays.asList(1, 2, 3);
        List<Integer> right = Arrays.asList(2, 4, 6);
        
        Iterator<Integer> leftIterator = left.iterator();
        Iterator<Integer> rightIterator = right.iterator();
        
        assertTrue(expression.findMatch(leftIterator, rightIterator));
    }
    
    @Test
    public void testFindMatch_FindMatch_NotTrue() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[2]) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        
        List<Integer> left = Arrays.asList(1, 2, 3);
        List<Integer> right = Arrays.asList(4, 5, 6);
        
        Iterator<Integer> leftIterator = left.iterator();
        Iterator<Integer> rightIterator = right.iterator();
        
        assertFalse(expression.findMatch(leftIterator, rightIterator));
    }
}
