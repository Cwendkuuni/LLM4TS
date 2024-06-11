
package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class CoreOperationCompareTest {
    private CoreOperationCompare operationCompare;
    private Expression arg1;
    private Expression arg2;
    private EvalContext context;

    @Before
    public void setUp() {
        arg1 = new ExpressionImpl();
        arg2 = new ExpressionImpl();
        operationCompare = new CoreOperationCompare(arg1, arg2);
        context = new EvalContext();
    }

    @Test
    public void testEqual() {
        assertTrue(operationCompare.equal(context, arg1, arg2));
    }

    @Test
    public void testContains() {
        assertTrue(operationCompare.contains(new HashSet<>().iterator(), new Object()));
    }

    @Test
    public void testFindMatch() {
        assertTrue(operationCompare.findMatch(new HashSet<>().iterator(), new HashSet<>().iterator()));
    }

    @Test
    public void testEqualPointers() {
        assertTrue(operationCompare.equal(new PointerImpl(), new PointerImpl()));
    }

    @Test
    public void testEqualPointersWithValue() {
        assertTrue(operationCompare.equal(new PointerImpl(), "value"));
    }

    @Test
    public void testEqualPointersWithNullValue() {
        assertFalse(operationCompare.equal(new PointerImpl(), null));
    }

    @Test
    public void testEqualBooleanValues() {
        assertTrue(operationCompare.equal(true, false));
    }

    @Test
    public void testEqualNumberValues() {
        assertTrue(operationCompare.equal(1, 2));
    }

    @Test
    public void testEqualStringValues() {
        assertTrue(operationCompare.equal("string1", "string2"));
    }
}

class ExpressionImpl implements Expression {

    @Override
    public Object compute(EvalContext context) {
        return null;
    }
}

class PointerImpl implements Pointer {

    @Override
    public Object getValue() {
        return null;
    }
}
