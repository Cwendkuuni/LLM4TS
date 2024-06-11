package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    private CoreOperationRelationalExpression expression;
    
    @Test
    public void testComputeValue() {
        Object result = expression.computeValue(new EvalContext());
        // assert result
    }

    @Test
    public void testGetPrecedence() {
        int precedence = expression.getPrecedence();
        // assert precedence
    }

    @Test
    public void testIsSymmetric() {
        boolean symmetric = expression.isSymmetric();
        // assert symmetric
    }

    @Test
    public void testEvaluateCompare() {
        boolean compare = expression.evaluateCompare(0);
        // assert compare
    }

    @Test
    public void testCompute() {
        Object left = new Object();
        Object right = new Object();
        boolean compute = expression.compute(left, right);
        // assert compute
    }

    @Test
    public void testReduce() {
        Object operand = new Object();
        Object reduce = expression.reduce(operand);
        // assert reduce
    }

    @Test
    public void testContainsMatch() {
        Iterator<Object> it = Collections.emptyIterator();
        Object value = new Object();
        boolean match = expression.containsMatch(it, value);
        // assert match
    }

    @Test
    public void testFindMatch() {
        Iterator<Object> lit = Collections.emptyIterator();
        Iterator<Object> rit = Collections.emptyIterator();
        boolean match = expression.findMatch(lit, rit);
        // assert match
    }
}