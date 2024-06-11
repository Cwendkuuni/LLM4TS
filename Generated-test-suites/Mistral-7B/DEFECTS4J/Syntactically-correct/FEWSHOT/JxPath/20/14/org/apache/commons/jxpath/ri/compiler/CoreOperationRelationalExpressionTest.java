package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;

public class CoreOperationRelationalExpressionTest {
    @Test
    public void testCoreOperationRelationalExpression() {
        assertFalse(new CoreOperationRelationalExpression(new CoreExpression[0]) instanceof CoreOperationRelationalExpression);
    }

    @Test
    public void testComputeValue() {
        Expression left = new ConstantExpression(1);
        Expression right = new ConstantExpression(2);
        CoreOperationRelationalExpression expression = new CoreOperationGTE(new CoreExpression[]{left, right});

        EvalContext context = new EvalContext(new InitialContext(Collections.singletonList(new SelfContext("root"))));

        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue(((Boolean) result).booleanValue());

        left = new ConstantExpression(1);
        right = new ConstantExpression(3);
        expression = new CoreOperationGTE(new CoreExpression[]{left, right});

        result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse(((Boolean) result).booleanValue());
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationGTE(new CoreExpression[0]);

        int precedence = expression.getPrecedence();

        assertEquals(CoreOperationRelationalExpression.RELATIONAL_EXPR_PRECEDENCE, precedence);
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationGTE(new CoreExpression[0]);

        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testEvaluateCompareGTE() {
        CoreOperationRelationalExpression expression = new CoreOperationGTE(new CoreExpression[0]);

        assertTrue(expression.evaluateCompare(0));
        assertTrue(expression.evaluateCompare(1));
        assertFalse(expression.evaluateCompare(-1));
    }

    @Test
    public void testEvaluateCompareLTE() {
        CoreOperationRelationalExpression expression = new CoreOperationLTE(new CoreExpression[0]);

        assertFalse(expression.evaluateCompare(0));
        assertTrue(expression.evaluateCompare(-1));
        assertFalse(expression.evaluateCompare(1));
    }

    @Test
    public void testEvaluateCompareEQ() {
        CoreOperationRelationalExpression expression = new CoreOperationEQ(new CoreExpression[0]);

        assertTrue(expression.evaluateCompare(0));
        assertTrue(expression.evaluateCompare(0));
        assertFalse(expression.evaluateCompare(1));
    }

    @Test
    public void testEvaluateCompareNEQ() {
        CoreOperationRelationalExpression expression = new CoreOperationNEQ(new CoreExpression[0]);

        assertFalse(expression.evaluateCompare(0));
        assertFalse(expression.evaluateCompare(0));
        assertTrue(expression.evaluateCompare(1));
    }

    @Test
    public void testComputeWithIterators() {
        Iterator left = Arrays.asList(1, 2, 3).iterator();
        Iterator right = Arrays.asList(2, 3, 4).iterator();

        CoreOperationRelationalExpression expression = new CoreOperationGTE(new CoreExpression[]{
                new CoreExpression(){
                    @Override
                    public Object compute(EvalContext context) {
                        return left;
                    }
                },
                new CoreExpression(){
                    @Override
                    public Object compute(EvalContext context) {
                        return right;
                    }
                }
        });

        assertTrue(expression.compute(new EvalContext(new InitialContext(Collections.singletonList(new SelfContext("root"))))));

        left = Arrays.asList(1, 2, 3).iterator();
        right = Arrays.asList(2, 3, 4).iterator();

        expression = new CoreOperationLT(new CoreExpression[]{
                new CoreExpression(){
                    @Override
                    public Object compute(EvalContext context) {
                        return left;
                    }
                },
                new CoreExpression(){
                    @Override
                    public Object compute(EvalContext context) {
                        return right;                    }
                }
        });

        assertFalse(expression.compute(new EvalContext(new InitialContext(Collections.singletonList(new SelfContext("root"))))));
    }

    @Test
    public void testComputeWithCollections() {
        Collection left = Arrays.asList(1, 2, 3);
        Collection right = Arrays.asList(2, 3, 4);

        CoreOperationRelationalExpression expression = new CoreOperationGTE(new CoreExpression[]{
                new CoreExpression(){
                    @Override
                    public Object compute(EvalContext context) {
                        return left.iterator();
                    }
                },
                new CoreExpression(){
                    @Override
                    public Object compute(EvalContext context) {
                        return right.iterator();                    }
                }
        });

        assertTrue(expression.compute(new EvalContext(new InitialContext(Collections.singletonList(new SelfContext("root"))))));

        left = Arrays.asList(1, 2, 3);
        right = Arrays.asList(2, 3, 4);

        expression = new CoreOperationLT(new CoreExpression[]{
                new CoreExpression(){
                    @Override
                    public Object compute(EvalContext context) {
                        return left.iterator();
                    }
                },
                new CoreExpression(){
                    @Override
                    public Object compute(EvalContext context) {
                        return right.iterator();                    }
                }
        });

        assertFalse(expression.compute(new EvalContext(new InitialContext(Collections.singletonList(new SelfContext("root"))))));
    }

    @Test
    public void testComputeWithDoubles() {
        double left = 1.0;
        double right = 2.0;

        CoreOperationRelationalExpression expression = new CoreOperationGTE(new CoreExpression[]{
                new ConstantExpression(left),
                new ConstantExpression(right)
        });

        assertFalse(expression.compute(new EvalContext(new InitialContext(Collections.singletonList(new SelfContext("root"))))));

        left = 1.0;
        right = 2.0;

        expression = new CoreOperationLT(new CoreExpression[]{
                new ConstantExpression(left),
                new ConstantExpression(right)
        });

        assertTrue(expression.compute(new EvalContext(new InitialContext(Collections.singletonList(new SelfContext("root"))))));

        left = Double.NaN;
        right = 2.0;

        expression = new CoreOperationGTE(new CoreExpression[]{
                new ConstantExpression(left),
                new ConstantExpression(right)
        });

        assertFalse(expression.compute(new EvalContext(new InitialContext(Collections.singletonList(new SelfContext("root"))))));

        left = 1.0;
        right = Double.NaN;

        expression = new CoreOperationGTE(new CoreExpression[]{
                new ConstantExpression(left),
                new ConstantExpression(right)
        });

        assertTrue(expression.compute(new EvalContext(new InitialContext(Collections.singletonList(new SelfContext("root"))))));
    }
}