package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.IteratedNode;
import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.NodeTest;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testCoreOperationRelationalExpression() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan("1", "2");
        assertTrue(expression.isSymmetric() == false);
        assertTrue(expression.getPrecedence() == 3);
    }

    @Test
    public void testComputeValue() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan("1.5", "2.0");
        EvalContext context = new EvalContext(new SelfContext(new Object()), null, null);
        Object left = new Double(1.5);
        Object right = new Double(2.0);

        assertTrue(expression.computeValue(context) instanceof Boolean);
        assertTrue((Boolean) expression.computeValue(context));

        expression = new CoreOperationLessThan("1.5", "2.0");
        assertTrue((Boolean) expression.computeValue(context) instanceof Boolean);
        assertFalse((Boolean) expression.computeValue(context));

        left = new Double(2.0);
        assertTrue((Boolean) expression.computeValue(context) instanceof Boolean);
        assertFalse((Boolean) expression.computeValue(context));

        left = new Double(1.5);
        assertTrue((Boolean) expression.computeValue(context) instanceof Boolean);
        assertTrue((Boolean) expression.computeValue(context));

        left = new String("1.5");
        right = new String("2.0");
        assertTrue((Boolean) expression.computeValue(context) instanceof Boolean);
        assertFalse((Boolean) expression.computeValue(context));

        left = new String("2.0");
        assertTrue((Boolean) expression.computeValue(context) instanceof Boolean);
        assertFalse((Boolean) expression.computeValue(context));

        left = new String("1.5");
        assertTrue((Boolean) expression.computeValue(context) instanceof Boolean);
        assertTrue((Boolean) expression.computeValue(context));

        left = new InitialContext();
        assertTrue(expression.computeValue(context) instanceof Boolean);
        assertFalse((Boolean) expression.computeValue(context));

        left = new SelfContext(new Object());
        right = new Double(1.5);
        assertTrue((Boolean) expression.computeValue(context) instanceof Boolean);
        assertFalse((Boolean) expression.computeValue(context));

        left = new SelfContext(new Object());
        right = new Double(2.0);
        assertTrue((Boolean) expression.computeValue(context) instanceof Boolean);
        assertFalse((Boolean) expression.computeValue(context));

        left = new SelfContext(new Object());
        right = new SelfContext(new Object());
        assertTrue((Boolean) expression.computeValue(context) instanceof Boolean);
        assertFalse((Boolean) expression.computeValue(context));

        left = new SelfContext(new Object());
        right = new Integer(1);
        assertTrue((Boolean) expression.computeValue(context) instanceof Boolean);
        assertFalse((Boolean) expression.computeValue(context));
    }

    @Test
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan("1.5", "2.0");
        assertTrue(expression.evaluateCompare(evaluateCompareCodes(">", -1, 0, 1)) == true);
    }

    @Test
    public void testFindMatch() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan("1", Arrays.asList(1, 2, 3));
        assertTrue(expression.findMatch(Arrays.asList(1, 2, 3).iterator(), Arrays.asList(1, 2, 3).iterator()) == true);
    }

    @Test
    public void testContainsMatch() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan("1", Arrays.asList(1, 2, 3).iterator());
        HashSet set = new HashSet();
        set.addAll(Arrays.asList(1, 2, 3));
        assertTrue(expression.containsMatch(set.iterator(), 1) == true);
        assertTrue(expression.containsMatch(set.iterator(), 2) == true);
        assertTrue(expression.containsMatch(set.iterator(), 3) == true);
        assertTrue(expression.containsMatch(set.iterator(), 0) == false);
    }

    @Test
    public void testReduce() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan("1", new Object());
        Object value = new Object();
        expression.reduce(value);
        assertTrue(value instanceof Iterator);
    }

    private int[] evaluateCompareCodes(String op, int left, int right, int result) {
        switch (op.charAt(0)) {
            case '>':
                return new int[]{right, left, result == 1 ? 1 : 0};
            case "<":
                return new int[]{left, right, result == -1 ? 1 : 0};
            case ">=":
                return new int[]{right, left, result >= 0 ? 1 : 0};
            case "<=":
                return new int[]{left, right, result <= 0 ? 1 : 0};
            default:
                throw new IllegalArgumentException("Invalid comparison operator: " + op);
        }
    }

    @Test
    public void testFindMatchWithSelfContext() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new SelfContext(new Object()), new Double(1.5));
        assertTrue(expression.findMatch(new SelfContext(new Object()).iterator(), new Double(1.5).iterator()) == true);
    }

    @Test
    public void testContainsMatchWithSelfContext() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new SelfContext(new Object()), new Double(1.5));
        assertTrue(expression.containsMatch(new SelfContext(new Object()).iterator(), new Double(1.5)) == true);
    }

    @Test
    public void testContainsMatchWithIteratedNode() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new IteratedNode() {
            @Override
            public NodeTest getNodeTest() {
                return new NodeTest() {
                    @Override
                    public boolean matches(Node node) {
                        return NodeTest.isNumber(node) && NodeTest.isNumberValue(node, "2.5");
                    }
                };
            }
        }, new Double(1.5));

        assertTrue(expression.containsMatch(new IteratedNode() {
            @Override
            public NodeTest getNodeTest() {
                return new NodeTest() {
                    @Override
                    public boolean matches(Node node) {
                        return NodeTest.isNumber(node) && NodeTest.isNumberValue(node, "1.5");
                    }
                };
            }
        }.iterator(), new Double(1.5)) == true);

        assertTrue(expression.containsMatch(new IteratedNode() {
            @Override
            public NodeTest getNodeTest() {
                return new NodeTest() {
                    @Override
                    public boolean matches(Node node) {
                        return NodeTest.isNumber(node) && NodeTest.isNumberValue(node, "2.5");
                    }
                };
            }
        }.iterator(), new Double(1.5)) == false);

        assertTrue(expression.containsMatch(new IteratedNode() {
            @Override
            public NodeTest getNodeTest() {
                return new NodeTest() {
                    @Override
                    public boolean matches(Node node) {
                        return NodeTest.isNumber(node) && NodeTest.isNumberValue(node, "2.5");
                    }
                };
            }
        }.iterator(), new Double(2.5)) == true);
    }

    @Test
    public void testComputeWithIterators() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new NodeIterator() {
            @Override
            public Node nextNode() {
                return new Object();
            }

            @Override
            public boolean hasNext() {
                return true;
            }
        }, new NodeIterator() {
            @Override
            public Node nextNode() {
                return new Object();
            }

            @Override
            public boolean hasNext() {
                return true;
            }
        });

        HashSet left = new HashSet();
        HashSet right = new HashSet();
        left.addAll(Arrays.asList(new Object(), new Object()));
        right.addAll(Arrays.asList(new Object(), new Object(), new Object()));
        assertTrue(expression.compute(left.iterator(), right.iterator()) == false);

        left.clear();
        left.addAll(Arrays.asList(new Object(), new Object(), new Object()));
        right.clear();
        right.addAll(Arrays.asList(new Object()));
        assertTrue(expression.compute(left.iterator(), right.iterator()) == false);

        left.clear();
        left.addAll(Arrays.asList(new Object(), new Object(), new Object()));
        right.clear();
        right.addAll(Arrays.asList(new Object()));
        assertTrue(expression.compute(right.iterator(), left.iterator()) == false);

        left.clear();
        left.addAll(Arrays.asList(new Object(), new Object(), new Object()));
        right.clear();
        right.addAll(Arrays.asList(new Object(), new Object()));
        assertTrue(expression.compute(left.iterator(), right.iterator()) == true);

        left.clear();
        left.addAll(Arrays.asList(new Object(), new Object()));
        right.clear();
        right.addAll(Arrays.asList(new Object(), new Object(), new Object()));
        assertTrue(expression.compute(left.iterator(), right.iterator()) == false);

        left.clear();
        left.addAll(Arrays.asList(new Object(), new Object(), new Object()));
        right.clear();
        right.addAll(Arrays.asList(new Object(), new Object(), new Object()));
        assertTrue(expression.compute(left.iterator(), right.iterator()) == false);
    }

    @Test
    public void testComputeWithInitialContexts() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new InitialContext(), new InitialContext());
        EvalContext context = new EvalContext(new SelfContext(new Object()), null, null);
        assertTrue(expression.compute(context, context) == false);
    }
}