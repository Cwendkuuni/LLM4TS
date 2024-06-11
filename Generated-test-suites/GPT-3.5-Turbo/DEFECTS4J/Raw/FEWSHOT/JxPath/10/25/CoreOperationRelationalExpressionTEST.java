
package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.util.ValueUtils;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {
    
    private CoreOperationRelationalExpression coreOperationRelationalExpression;
    
    @Before
    public void setUp() {
        coreOperationRelationalExpression = new CoreOperationRelationalExpression(null) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
    }

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext(new JXPathContext(), new Pointer() {
            
            @Override
            public Object getValue() {
                return null;
            }
            
            @Override
            public void setValue(Object value) {
                
            }
            
            @Override
            public Object getBaseValue() {
                return null;
            }
        }, new Object(), null);
        Object result = coreOperationRelationalExpression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testGetPrecedence() {
        int result = coreOperationRelationalExpression.getPrecedence();
        assertTrue(result == 3);
    }

    @Test
    public void testIsSymmetric() {
        boolean result = coreOperationRelationalExpression.isSymmetric();
        assertFalse(result);
    }

    @Test
    public void testReduce() {
        Object baseValue = new Object();
        EvalContext context = new EvalContext(new JXPathContext(), new Pointer() {
            
            @Override
            public Object getValue() {
                return baseValue;
            }
            
            @Override
            public void setValue(Object value) {
                
            }
            
            @Override
            public Object getBaseValue() {
                return baseValue;
            }
        }, new Object(), null);
        Object result = coreOperationRelationalExpression.reduce(context);
        assertTrue(result == baseValue);
        
        Collection<Object> collection = new ArrayList<>();
        collection.add(baseValue);
        context = new EvalContext(new JXPathContext(), new Pointer() {
            
            @Override
            public Object getValue() {
                return collection;
            }
            
            @Override
            public void setValue(Object value) {
                
            }
            
            @Override
            public Object getBaseValue() {
                return collection;
            }
        }, new Object(), null);
        result = coreOperationRelationalExpression.reduce(context);
        assertTrue(result instanceof Iterator);
    }

    @Test
    public void testContainsMatch() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        Iterator<Integer> iterator = list.iterator();
        boolean result = coreOperationRelationalExpression.containsMatch(iterator, 2);
        assertTrue(result);
        
        result = coreOperationRelationalExpression.containsMatch(iterator, 4);
        assertFalse(result);
    }

    @Test
    public void testFindMatch() {
        List<Integer> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(2);
        list1.add(3);
        List<Integer> list2 = new ArrayList<>();
        list2.add(3);
        list2.add(4);
        list2.add(5);
        Iterator<Integer> iterator1 = list1.iterator();
        Iterator<Integer> iterator2 = list2.iterator();
        boolean result = coreOperationRelationalExpression.findMatch(iterator1, iterator2);
        assertTrue(result);
    }

}
