package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
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
        Object left = new Object();
        Object right = new Object();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[2]) {
            protected boolean evaluateCompare(int compare) {
                return true;
            }
        };
        EvalContext context = new EvalContext(null);
        context.setValuePointer(new SelfContext(context, left));
        
        assertTrue((Boolean) expression.computeValue(context));
        
        context.setValuePointer(new SelfContext(context, right));
        expression.args[1] = new LocationPath();
        
        assertTrue((Boolean) expression.computeValue(context));
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[2]) {
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        assertEquals(3, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[2]) {
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testCompute() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[2]) {
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        
        assertTrue(expression.compute(5, 3));
        assertTrue(expression.compute(3, new Iterator<Object>() {
            public boolean hasNext() {
                return true;
            }

            public Object next() {
                return 2;
            }

            public void remove() {
            }
        }));
        assertFalse(expression.compute(new Iterator<Object>() {
            public boolean hasNext() {
                return true;
            }

            public Object next() {
                return 6;
            }

            public void remove() {
            }
        }, 10));
        assertFalse(expression.compute(new Iterator<Object>() {
            public boolean hasNext() {
                return true;
            }

            public Object next() {
                return 6;
            }

            public void remove() {
            }
        }, new Iterator<Object>() {
            public boolean hasNext() {
                return true;
            }

            public Object next() {
                return 8;
            }

            public void remove() {
            }
        }));
        assertFalse(expression.compute(Double.NaN, 1));
    }

    @Test
    public void testReduce() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[2]) {
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };

        Object element = new Object();
        EvalContext evalContext = new EvalContext(null);
        
        assertEquals(element, expression.reduce(element));
        assertEquals(evalContext.getSingleNodePointer(), expression.reduce(evalContext.getSingleNodePointer()));
        
        Collection<Object> collection = new ArrayList<Object>();
        collection.add(element);
        Iterator<Object> iterator = collection.iterator();
        
        assertEquals(iterator, expression.reduce(collection));
    }

    @Test
    public void testContainsMatch() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[2]) {
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        Collection<Object> collection = new ArrayList<Object>();
        collection.add(2);
        Iterator<Object> iterator = collection.iterator();
        
        assertTrue(expression.containsMatch(iterator, 2));
        assertFalse(expression.containsMatch(iterator, 3));
    }

    @Test
    public void testFindMatch() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[2]) {
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        Collection<Object> leftCollection = new ArrayList<Object>();
        leftCollection.add(1);
        Collection<Object> rightCollection = new ArrayList<Object>();
        rightCollection.add(2);
        Iterator<Object> left = leftCollection.iterator();
        Iterator<Object> right = rightCollection.iterator();
        
        assertTrue(expression.findMatch(left, right));
        assertFalse(expression.findMatch(right, left));
    }

}