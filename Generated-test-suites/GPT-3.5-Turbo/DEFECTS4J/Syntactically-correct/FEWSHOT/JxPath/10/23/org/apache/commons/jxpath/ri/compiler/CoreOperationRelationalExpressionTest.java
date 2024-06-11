package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {
    
    @Test
    public void testComputeValue() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        
        EvalContext context = new EvalContext();
        context.setSingleNodePointer(new SelfContext());

        args[0] = new CoreOperation() {
            @Override
            public Object computeValue(EvalContext context) {
                return 10;
            }
            
            @Override
            protected int getPrecedence() {
                return 3;
            }
            
            @Override
            protected boolean isSymmetric() {
                return false;
            }
        };
        
        args[1] = new CoreOperation() {
            @Override
            public Object computeValue(EvalContext context) {
                return 5;
            }
            
            @Override
            protected int getPrecedence() {
                return 3;
            }
            
            @Override
            protected boolean isSymmetric() {
                return false;
            }
        };
        
        assertTrue((Boolean) expression.computeValue(context));
        
        args[0] = new CoreOperation() {
            @Override
            public Object computeValue(EvalContext context) {
                return 5;
            }
            
            @Override
            protected int getPrecedence() {
                return 3;
            }
            
            @Override
            protected boolean isSymmetric() {
                return false;
            }
        };
        
        args[1] = new CoreOperation() {
            @Override
            public Object computeValue(EvalContext context) {
                return 10;
            }
            
            @Override
            protected int getPrecedence() {
                return 3;
            }
            
            @Override
            protected boolean isSymmetric() {
                return false;
            }
        };
        
        assertFalse((Boolean) expression.computeValue(context));
    }
    
    @Test
    public void testReduce() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        
        EvalContext context = new EvalContext();
        
        SelfContext selfContext = new SelfContext();
        context.setSingleNodePointer(selfContext);
        
        assertEquals(selfContext, expression.reduce(selfContext));
        
        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        
        Iterator<Integer> iterator = list.iterator();
        Collection<Integer> collection = expression.reduce(list);
        
        assertTrue(collection instanceof Iterator);
        
        Iterator<Integer> collectionIterator = (Iterator<Integer>) collection;
        while (iterator.hasNext()) {
            assertEquals(iterator.next(), collectionIterator.next());
        }
        
        assertFalse(collectionIterator.hasNext());
        
        Integer value = 5;
        collection = expression.reduce(value);
        assertEquals(value, collection);
    }
    
    @Test
    public void testContainsMatch() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        
        ArrayList<String> list = new ArrayList<>();
        list.add("apple");
        list.add("banana");
        list.add("cherry");
        
        Iterator<String> iterator = list.iterator();
        
        String value = "apple";
        assertTrue(expression.containsMatch(iterator, value));
        
        value = "pear";
        assertFalse(expression.containsMatch(iterator, value));
        
        HashSet<String> set = new HashSet<>(list);
        iterator = set.iterator();
        
        value = "cherry";
        assertTrue(expression.containsMatch(iterator, value));
    }
    
    @Test
    public void testFindMatch() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        
        ArrayList<String> list1 = new ArrayList<>();
        list1.add("apple");
        list1.add("banana");
        list1.add("cherry");
        
        ArrayList<String> list2 = new ArrayList<>();
        list2.add("pear");
        list2.add("banana");
        list2.add("orange");
        
        Iterator<String> iterator1 = list1.iterator();
        Iterator<String> iterator2 = list2.iterator();
        
        assertTrue(expression.findMatch(iterator1, iterator2));
        
        iterator1 = list1.iterator();
        iterator2 = list2.iterator();
        
        assertFalse(expression.findMatch(iterator2, iterator1));
        
        HashSet<String> set1 = new HashSet<>(list1);
        HashSet<String> set2 = new HashSet<>(list2);
        
        iterator1 = set1.iterator();
        iterator2 = set2.iterator();
        
        assertTrue(expression.findMatch(iterator1, iterator2));
    }
    
    @Test
    public void testCompute() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        
        Object left = 10;
        Object right = 5;
        
        assertTrue(expression.compute(left, right));
        
        left = 5;
        right = 10;
        
        assertFalse(expression.compute(left, right));
    }
    
    @Test
    public void testComputeWithNaN() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        
        Object left = Double.NaN;
        Object right = 5;
        
        assertFalse(expression.compute(left, right));
        
        left = 5;
        right = Double.NaN;
        
        assertFalse(expression.compute(left, right));
    }
    
    @Test
    public void testComputeWithIterator() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        
        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        
        Iterator<Integer> iterator = list.iterator();
        Object value = 2;
        
        assertTrue(expression.compute(iterator, value));
        
        iterator = list.iterator();
        value = 4;
        
        assertFalse(expression.compute(iterator, value));
    }
    
    @Test
    public void testComputeWithBothIterators() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        
        ArrayList<Integer> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(2);
        list1.add(3);
        
        ArrayList<Integer> list2 = new ArrayList<>();
        list2.add(4);
        list2.add(5);
        list2.add(6);
        
        Iterator<Integer> iterator1 = list1.iterator();
        Iterator<Integer> iterator2 = list2.iterator();
        
        assertFalse(expression.compute(iterator1, iterator2));
        
        iterator1 = list1.iterator();
        iterator2 = list2.iterator();
        
        assertFalse(expression.compute(iterator2, iterator1));
        
        list1.add(5);
        
        iterator1 = list1.iterator();
        iterator2 = list2.iterator();
        
        assertTrue(expression.compute(iterator1, iterator2));
    }

}