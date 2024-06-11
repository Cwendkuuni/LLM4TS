package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.Iterator;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testCoreOperationRelationalExpression() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{new NumberValueExpression(1), new NumberValueExpression(1)}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };

        EvalContext context = new EvalContext();
        Object left = new InitialContext();
        Object right = new NumberValueExpression(1);
        assertTrue(expression.computeValue(context));

        left = new SelfContext();
        assertTrue(expression.computeValue(context));

        left = new NumberValueExpression(1.0);
        right = new NumberValueExpression(1.0);
        assertTrue(expression.computeValue(context));

        left = new NumberValueExpression(1.0);
        right = new NumberValueExpression(2.0);
        assertFalse(expression.computeValue(context));

        left = new NumberValueExpression(2.0);
        right = new NumberValueExpression(1.0);
        assertFalse(expression.computeValue(context));

        left = new NumberValueExpression(Double.NaN);
        right = new NumberValueExpression(Double.NaN);
        assertFalse(expression.computeValue(context));

        left = new NumberValueExpression(Double.NaN);
        right = new NumberValueExpression(1.0);
        assertFalse(expression.computeValue(context));

        left = new IteratorIterator(Collections.singletonList(new NumberValueExpression(1.0)).iterator());
        right = new IteratorIterator(Collections.singletonList(new NumberValueExpression(1.0)).iterator());
        assertTrue(expression.computeValue(context));

        left = new IteratorIterator(Collections.singletonList(new NumberValueExpression(1.0)).iterator());
        right = new IteratorIterator(Collections.singletonList(new NumberValueExpression(2.0)).iterator());
        assertFalse(expression.computeValue(context));

        left = new IteratorIterator(Collections.singletonList(new NumberValueExpression(2.0)).iterator());
        right = new IteratorIterator(Collections.singletonList(new NumberValueExpression(1.0)).iterator());
        assertFalse(expression.computeValue(context));

        left = new IteratorIterator(Collections.singletonList(new NumberValueExpression(1.0)).iterator());
        right = new IteratorIterator(Collections.singletonList(new NumberValueExpression(1.0)).iterator());
        assertTrue(expression.computeValue(context));

        left = new IteratorIterator(new HashSet<>(List.of(new NumberValueExpression(1.0), new NumberValueExpression(1.0))).iterator());
        right = new IteratorIterator(new HashSet<>(List.of(new NumberValueExpression(1.0), new NumberValueExpression(1.0))).iterator());
        assertTrue(expression.computeValue(context));

        left = new IteratorIterator(new HashSet<>(List.of(new NumberValueExpression(1.0), new NumberValueExpression(2.0))).iterator());
        right = new IteratorIterator(new HashSet<>(List.of(new NumberValueExpression(1.0), new NumberValueExpression(1.0))).iterator());
        assertFalse(expression.computeValue(context));

        left = new IteratorIterator(new HashSet<>(List.of(new NumberValueExpression(1.0), new NumberValueExpression(1.0))).iterator());
        right = new IteratorIterator(new HashSet<>(List.of(new NumberValueExpression(2.0), new NumberValueExpression(1.0))).iterator());
        assertFalse(expression.computeValue(context));
    }
}