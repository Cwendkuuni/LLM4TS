package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class CoreOperationRelationalExpressionTest {
    private CoreOperationRelationalExpression expression;

    @Before
    public void setup() {
        expression = new CoreOperationRelationalExpression(new Expression[0]) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
    }

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext(new InitialContext(), new SelfContext());
        Object left = 5.0;
        Object right = 10.0;
        Object result = expression.computeValue(context);
        assertTrue(result.equals(Boolean.TRUE));

        left = 5.0;
        right = 5.0;
        result = expression.computeValue(context);
        assertTrue(result.equals(Boolean.FALSE));

        left = "five";
        right = "ten";
        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testGetPrecedence() {
        int precedence = expression.getPrecedence();
        assertEquals(3, precedence);
    }

    @Test
    public void testIsSymmetric() {
        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testFindMatch() {
        List leftList = new ArrayList();
        leftList.add(1.0);
        leftList.add(2.0);
        leftList.add(3.0);

        List rightList = new ArrayList();
        rightList.add(3.0);
        rightList.add(2.0);
        rightList.add(1.0);

        Iterator leftIterator = leftList.iterator();
        Iterator rightIterator = rightList.iterator();

        boolean result = expression.findMatch(leftIterator, rightIterator);
        assertTrue(result);

        leftList.add(4.0);
        result = expression.findMatch(leftIterator, rightIterator);
        assertFalse(result);
    }

    @Test
    public void testContainsMatch() {
        Collection leftCollection = new HashSet();
        leftCollection.add(1.0);
        leftCollection.add(2.0);
        leftCollection.add(3.0);

        Object rightObject = 3.0;

        Iterator leftIterator = leftCollection.iterator();
        boolean result = expression.containsMatch(leftIterator, rightObject);
        assertTrue(result);

        leftCollection.add(4.0);
        result = expression.containsMatch(leftIterator, rightObject);
        assertFalse(result);

        leftCollection = Collections.singleton(5.0);
        result = expression.containsMatch(leftIterator, rightObject);
        assertFalse(result);
    }
}