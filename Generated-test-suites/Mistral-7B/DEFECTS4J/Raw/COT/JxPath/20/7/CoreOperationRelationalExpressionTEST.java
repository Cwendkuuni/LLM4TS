package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * JUnit test cases for CoreOperationRelationalExpression class.
 */
public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Object left = new Integer(5);
        Object right = new Integer(10);
        CoreOperationRelationalExpression expression = new CoreRelationGreaterThanExpression(new CorePrimitiveExpression(left), new CorePrimitiveExpression(right));
        EvalContext context = new EvalContext(Collections.emptyMap());
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        left = new Integer(10);
        right = new Integer(5);
        expression = new CoreRelationGreaterThanExpression(new CorePrimitiveExpression(left), new CorePrimitiveExpression(right));
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = new Integer(5);
        right = new Integer(5);
        expression = new CoreRelationGreaterThanOrEqualExpression(new CorePrimitiveExpression(left), new CorePrimitiveExpression(right));
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = new Integer(5);
        right = new Integer(6);
        expression = new CoreRelationGreaterThanOrEqualExpression(new CorePrimitiveExpression(left), new CorePrimitiveExpression(right));
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        left = new Integer(null);
        right = new Integer(5);
        expression = new CoreRelationGreaterThanExpression(new CoreNullExpression(), new CorePrimitiveExpression(right));
        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);

        left = new Integer(5);
        right = new Integer(null);
        expression = new CoreRelationGreaterThanExpression(new CorePrimitiveExpression(left), new CoreNullExpression());
        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);

        left = new Integer(null);
        right = new Integer(null);
        expression = new CoreRelationGreaterThanOrEqualExpression(new CoreNullExpression(), new CoreNullExpression());
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        // Test with Iterators
        List list1 = Arrays.asList(new Integer[] { 1, 2, 3, 4, 5 });
        List list2 = Arrays.asList(new Integer[] { 2, 3, 4, 5, 6 });
        Iterator it1 = list1.iterator();
        Iterator it2 = list2.iterator();
        CoreOperationRelationalExpression relationExpression = new CoreRelationGreaterThanExpression(it1, it2);
        result = relationExpression.computeValue(context);
        assertFalse(result instanceof Boolean);

        list1 = Arrays.asList(new Integer[] { 1, 2, 3, 4, 5 });
        list2 = Arrays.asList(new Integer[] { 1, 2, 3, 4, 5, 6 });
        it1 = list1.iterator();
        it2 = list2.iterator();
        relationExpression = new CoreRelationGreaterThanOrEqualExpression(it1, it2);
        result = relationExpression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeValueWithNullExpression() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null, new CorePrimitiveExpression(new Integer(5)));
        EvalContext context = new EvalContext(Collections.emptyMap());
        expression.computeValue(context);
    }
}