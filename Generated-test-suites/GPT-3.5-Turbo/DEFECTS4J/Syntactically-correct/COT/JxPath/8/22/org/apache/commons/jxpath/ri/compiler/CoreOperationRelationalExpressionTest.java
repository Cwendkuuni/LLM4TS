
package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.InitialContext;
import org.apache.commons.jxpath.ri.compiler.SelfContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class CoreOperationRelationalExpressionTest {

     private CoreOperationRelationalExpression coreOperationRelationalExpression;
     private Expression[] args;

    @Before
    public void setUp() {
        args = new Expression[2];
        args[0] = new Expression() {
            public Object computeValue(EvalContext context) { return 5; }
        };
        args[1] = new Expression() {
            public Object computeValue(EvalContext context) { return 10; }
        };
        coreOperationRelationalExpression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) { return compare == 0; }
        };
    }

    @Test
    public void testComputeValue() {
        Assert.assertEquals(Boolean.FALSE, coreOperationRelationalExpression.computeValue(null));
    }

    @Test
    public void testGetPrecedence() {
        Assert.assertEquals(3, coreOperationRelationalExpression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        Assert.assertEquals(false, coreOperationRelationalExpression.isSymmetric());
    }

    @Test
    public void testCompute() {
        Object result = coreOperationRelationalExpression.compute(5, 10);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testReduce() {
        Object result = coreOperationRelationalExpression.reduce(new ArrayList<Integer>(Arrays.asList(1, 2, 3)));
        Assert.assertTrue(result instanceof Iterator);
    }

    @Test
    public void testContainsMatch() {
        Iterator<Integer> it = new ArrayList<Integer>(Arrays.asList(1, 2, 3)).iterator();
        boolean result = coreOperationRelationalExpression.containsMatch(it, 2);
        Assert.assertTrue(result);
    }

    @Test
    public void testFindMatch() {
        Iterator<Integer> lit = new ArrayList<Integer>(Arrays.asList(1, 2, 3)).iterator();
        Iterator<Integer> rit = new ArrayList<Integer>(Arrays.asList(3, 4, 5)).iterator();
        boolean result = coreOperationRelationalExpression.findMatch(lit, rit);
        Assert.assertTrue(result);
    }
}
