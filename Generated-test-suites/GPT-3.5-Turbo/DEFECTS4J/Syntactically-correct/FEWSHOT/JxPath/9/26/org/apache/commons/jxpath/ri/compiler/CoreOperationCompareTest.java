package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class CoreOperationCompareTest {

    private CoreOperationCompare coreOperationCompare;

    @Before
    public void setUp(){
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        coreOperationCompare = new CoreOperationCompare(arg1, arg2);
    }

    @Test
    public void testGetPrecedence() {
        int precedence = coreOperationCompare.getPrecedence();
        assertEquals(precedence, 2);
    }

    @Test
    public void testIsSymmetric(){
        boolean symmetric = coreOperationCompare.isSymmetric();
        assertTrue(symmetric);
    }

    @Test
    public void testEqual(){
        EvalContext context = new EvalContext();
        Expression left = new Expression();
        Expression right = new Expression();
        boolean result = coreOperationCompare.equal(context, left, right);
        assertFalse(result);
    }

    @Test
    public void testContains(){
        Iterator iterator = new Iterator() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object next() {
                return null;
            }
        };
        Object value = new Object();
        boolean result = coreOperationCompare.contains(iterator, value);
        assertFalse(result);
    }

    @Test
    public void testFindMatch(){
        Iterator left = new Iterator() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object next() {
                return null;
            }
        };
        Iterator right = new Iterator() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object next() {
                return null;
            }
        };
        boolean result = coreOperationCompare.findMatch(left, right);
        assertFalse(result);
    }

    @Test
    public void testEqualObject(){
        Object l = new Object();
        Object r = new Object();
        boolean result = coreOperationCompare.equal(l, r);
        assertTrue(result);
    }

    @Test
    public void testEqualPointer(){
        Pointer l = new Pointer();
        Pointer r = new Pointer();
        boolean result = coreOperationCompare.equal(l, r);
        assertTrue(result);
    }

    @Test
    public void testEqualNumber(){
        Number l = new Number() {
            @Override
            public int intValue() {
                return 0;
            }

            @Override
            public long longValue() {
                return 0;
            }

            @Override
            public float floatValue() {
                return 0;
            }

            @Override
            public double doubleValue() {
                return 0;
            }
        };
        Number r = new Number() {
            @Override
            public int intValue() {
                return 0;
            }

            @Override
            public long longValue() {
                return 0;
            }

            @Override
            public float floatValue() {
                return 0;
            }

            @Override
            public double doubleValue() {
                return 0;
            }
        };
        boolean result = coreOperationCompare.equal(l, r);
        assertTrue(result);
    }

    @Test
    public void testEqualString(){
        String l = "";
        String r = "";
        boolean result = coreOperationCompare.equal(l, r);
        assertTrue(result);
    }

    @Test
    public void testNotEqualString(){
        String l = "";
        String r = "test";
        boolean result = coreOperationCompare.equal(l, r);
        assertFalse(result);
    }
    
}