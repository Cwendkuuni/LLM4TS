package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.Iterate;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Test file for CoreOperationRelationalExpression class.
 */
public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGT() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreExpressionNode(new NodeInfo(1)));
        EvalContext context = new EvalContext(new InfoSet() {
            @Override
            public Iterate iterate(String axis, String expr) {
                List<NodeInfo> nodes = new ArrayList<>();
                nodes.add(new NodeInfo(2));
                nodes.add(new NodeInfo(1));
                return new Iterate() {
                    @Override
                    public boolean hasNext() {
                        return !nodes.isEmpty();
                    }

                    @Override
                    public Object next() {
                        return nodes.remove(0);
                    }
                };
            }
        });
        Object result = expression.computeValue(context);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGTWithInitialContexts() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreExpressionNode(new NodeInfo(1)));
        EvalContext context = new EvalContext(new InitialContext(), new InitialContext());
        Object result = expression.computeValue(context);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGTWithSelfContexts() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreExpressionNode(new SelfContext()));
        EvalContext context = new EvalContext(new SelfContext());
        Object result = expression.computeValue(context);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGTWithCollections() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreExpressionNode(Collections.singleton(new NodeInfo(1))));
        EvalContext context = new EvalContext(new InitialContext());
        Object result = expression.computeValue(context);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.FALSE, result);

        EvalContext contextWithGreaterValue = new EvalContext(new InitialContext());
        contextWithGreaterValue.setCurrentNode(new NodeInfo(2));
        Object resultWithGreaterValue = expression.computeValue(contextWithGreaterValue);
        Assert.assertTrue(resultWithGreaterValue instanceof Boolean);
        Assert.assertEquals(Boolean.TRUE, resultWithGreaterValue);
    }

    @Test
    public void testGTE() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqual(new CoreExpressionNode(new NodeInfo(1)));
        EvalContext context = new EvalContext(new InfoSet() {
            @Override
            public Iterate iterate(String axis, String expr) {
                List<NodeInfo> nodes = new ArrayList<>();
                nodes.add(new NodeInfo(2));
                nodes.add(new NodeInfo(1));
                return new Iterate() {
                    @Override
                    public boolean hasNext() {
                        return !nodes.isEmpty();
                    }

                    @Override
                    public Object next() {
                        return nodes.remove(0);
                    }
                };
            }
        });
        Object result = expression.computeValue(context);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGTEWithInitialContexts() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqual(new CoreExpressionNode(new NodeInfo(1)));
        EvalContext context = new EvalContext(new InitialContext(), new InitialContext());
        Object result = expression.computeValue(context);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGTEWithSelfContexts() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqual(new CoreExpressionNode(new SelfContext()));
        EvalContext context = new EvalContext(new SelfContext());
        Object result = expression.computeValue(context);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGTEWithCollections() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqual(new CoreExpressionNode(Collections.singleton(new NodeInfo(1))));
        EvalContext context = new EvalContext(new InitialContext());
        Object result = expression.computeValue(context);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.TRUE, result);

        EvalContext contextWithSameValue = new EvalContext(new InitialContext());
        contextWithSameValue.setCurrentNode(new NodeInfo(1));
        Object resultWithSameValue = expression.computeValue(contextWithSameValue);
        Assert.assertTrue(resultWithSameValue instanceof Boolean);
        Assert.assertEquals(Boolean.TRUE, resultWithSameValue);

        EvalContext contextWithGreaterValue = new EvalContext(new InitialContext());
        contextWithGreaterValue.setCurrentNode(new NodeInfo(2));
        Object resultWithGreaterValue = expression.computeValue(contextWithGreaterValue);
        Assert.assertTrue(resultWithGreaterValue instanceof Boolean);
        Assert.assertEquals(Boolean.TRUE, resultWithGreaterValue);
    }

    @Test
    public void testLT() {
        CoreOperationRelationalExpression expression = new CoreOperationLessThan(new CoreExpressionNode(new NodeInfo(1)));
        EvalContext context = new EvalContext(new InfoSet() {
            @Override
            public Iterate iterate(String axis, String expr) {
                List<NodeInfo> nodes = new ArrayList<>();
                nodes.add(new NodeInfo(2));
                nodes.add(new NodeInfo(1));
                return new Iterate() {
                    @Override
                    public boolean hasNext() {
                        return !nodes.isEmpty();
                    }

                    @Override
                    public Object next() {
                        return nodes.remove(0);
                    }
                };
            }
        });
        Object result = expression.computeValue(context);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testLTWithInitialContexts() {
        CoreOperationRelationalExpression expression = new CoreOperationLessThan(new CoreExpressionNode(new NodeInfo(1)));
        EvalContext context = new EvalContext(new InitialContext(), new InitialContext());
        Object result = expression.computeValue(context);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testLTWithSelfContexts() {
        CoreOperationRelationalExpression expression = new CoreOperationLessThan(new CoreExpressionNode(new SelfContext()));
        EvalContext context = new EvalContext(new SelfContext());
        Object result = expression.computeValue(context);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testLTWithCollections() {
        CoreOperationRelationalExpression expression = new CoreOperationLessThan(new CoreExpressionNode(Collections.singleton(new NodeInfo(1))));
        EvalContext context = new EvalContext(new InitialContext());
        Object result = expression.computeValue(context);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.TRUE, result);

        EvalContext contextWithGreaterValue = new EvalContext(new InitialContext());
        context.setCurrentNode(new NodeInfo(2));
        Object resultWithGreaterValue = expression.computeValue(context);
        Assert.assertTrue(resultWithGreaterValue instanceof Boolean);
        Assert.assertEquals(Boolean.FALSE, resultWithGreaterValue);
    }

    @Test
    public void testLTE() {
        CoreOperationRelationalExpression expression = new CoreOperationLessThanOrEqual(new CoreExpressionNode(new NodeInfo(1)));
        EvalContext context = new EvalContext(new InfoSet() {
            @Override
            public Iterate iterate(String axis, String expr) {
                List<NodeInfo> nodes = new ArrayList<>();
                nodes.add(new NodeInfo(2));
                nodes.add(new NodeInfo(1));
                return new Iterate() {
                    @Override
                    public boolean hasNext() {
                        return !nodes.isEmpty();
                    }

                    @Override
                    public Object next() {
                        return nodes.remove(0);
                    }
                };
            }
        });
        Object result = expression.computeValue(context);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testLTEWithInitialContexts() {
        CoreOperationRelationalExpression expression = new CoreOperationLessThanOrEqual(new CoreExpressionNode(new NodeInfo(1)));
        EvalContext context = new EvalContext(new InitialContext(), new InitialContext());
        Object result = expression.computeValue(context);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testLTEWithSelfContexts() {
        CoreOperationRelationalExpression expression = new CoreOperationLessThanOrEqual(new CoreExpressionNode(new SelfContext()));
        EvalContext context = new EvalContext(new SelfContext());
        Object result = expression.computeValue(context);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testLTEWithCollections() {
        CoreOperationRelationalExpression expression = new CoreOperationLessThanOrEqual(new CoreExpressionNode(Collections.singleton(new NodeInfo(1))));
        EvalContext context = new EvalContext(new InitialContext());
        Object result = expression.computeValue(context);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.TRUE, result);

        EvalContext contextWithSameValue = new EvalContext(new InitialContext());
        contextWithSameValue.setCurrentNode(new NodeInfo(1));
        Object resultWithSameValue = expression.computeValue(contextWithSameValue);
        Assert.assertTrue(resultWithSameValue instanceof Boolean);
        Assert.assertEquals(Boolean.TRUE, resultWithSameValue);

        EvalContext contextWithSmallerValue = new EvalContext(new InitialContext());
        contextWithSmallerValue.setCurrentNode(new NodeInfo(0));
        Object resultWithSmallerValue = expression.computeValue(contextWithSmallerValue);
        Assert.assertTrue(resultWithSmallerValue instanceof Boolean);
        Assert.assertEquals(Boolean.TRUE, resultWithSmallerValue);

        EvalContext contextWithGreaterValue = new EvalContext(new InitialContext());
        contextWithGreaterValue.setCurrentNode(new NodeInfo(2));
        Object resultWithGreaterValue = expression.computeValue(contextWithGreaterValue);
        Assert.assertTrue(resultWithGreaterValue instanceof Boolean);
        Assert.assertEquals(Boolean.FALSE, resultWithGreaterValue);
    }
}