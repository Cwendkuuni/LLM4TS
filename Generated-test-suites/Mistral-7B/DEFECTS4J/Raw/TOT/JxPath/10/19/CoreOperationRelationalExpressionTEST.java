import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.EvaluationException;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.IterateIter;
import org.apache.commons.jxpath.ri.Node;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.Path;
import org.apache.commons.jxpath.ri.util.SimpleIterator;
import org.apache.commons.jxpath.ri.util.TestUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CoreOperationRelationalExpressionTest {

    private CoreOperationRelationalExpression expression;

    @Before
    public void setUp() throws Exception {
        expression = new GreaterThanExpression(new VariableExpression("x"), new NumberExpression(10));
    }

    @Test
    public void testGreaterThan() throws EvaluationException {
        InfoSet infoSet = TestUtils.createTestInfoSet();
        Node node1 = new NodeInfo(new Node("1"), null, null);
        Node node2 = new NodeInfo(new Node("2"), null, null);
        Node node3 = new NodeInfo(new Node("3"), null, null);
        Node node4 = new NodeInfo(new Node("4"), null, null);
        Node node5 = new NodeInfo(new Node("5"), null, null);
        Node node10 = new NodeInfo(new Node("10"), null, null);
        Node node11 = new NodeInfo(new Node("11"), null, null);

        infoSet.addAllChildren(new NodeInfo(TestUtils.createRootNode(), Collections.singletonList(node1)));
        infoSet.addAllChildren(new NodeInfo(node1, Arrays.asList(node2, node3, node4)));
        infoSet.addChild(node5, node4);

        JXPathContext context = new JXPathContext(infoSet);
        Assert.assertTrue(expression.evaluate(context).getValue().equals(Boolean.TRUE));

        context.setNode(node10);
        Assert.assertFalse(expression.evaluate(context).getValue().equals(Boolean.TRUE));

        context.setNode(node11);
        Assert.assertTrue(expression.evaluate(context).getValue().equals(Boolean.TRUE));
    }

    @Test
    public void testGreaterThanOrEqual() throws EvaluationException {
        expression = new GreaterThanOrEqualExpression(new VariableExpression("x"), new NumberExpression(10));

        InfoSet infoSet = TestUtils.createTestInfoSet();
        Node node1 = new NodeInfo(new Node("1"), null, null);
        Node node2 = new NodeInfo(new Node("2"), null, null);
        Node node3 = new NodeInfo(new Node("3"), null, null);
        Node node4 = new NodeInfo(new Node("4"), null, null);
        Node node5 = new NodeInfo(new Node("5"), null, null);
        Node node10 = new NodeInfo(new Node("10"), null, null);
        Node node11 = new NodeInfo(new Node("11"), null, null);

        infoSet.addAllChildren(new NodeInfo(TestUtils.createRootNode(), Collections.singletonList(node1)));
        infoSet.addAllChildren(new NodeInfo(node1, Arrays.asList(node2, node3, node4)));
        infoSet.addChild(node5, node4);

        JXPathContext context = new JXPathContext(infoSet);
        Assert.assertTrue(expression.evaluate(context).getValue().equals(Boolean.TRUE));

        context.setNode(node1);
        Assert.assertTrue(expression.evaluate(context).getValue().equals(Boolean.TRUE));

        context.setNode(node10);
        Assert.assertTrue(expression.evaluate(context).getValue().equals(Boolean.TRUE));

        context.setNode(node11);
        Assert.assertTrue(expression.evaluate(context).getValue().equals(Boolean.TRUE));
    }

    @Test(expected = EvaluationException.class)
    public void testInvalidComparison() throws EvaluationException {
        expression = new CoreOperationRelationalExpression(new NumberExpression(5), new NumberExpression(5)) {
            @Override
            protected boolean evaluateCompare(int compare) {
                throw new UnsupportedOperationException("Not supported.");
            }
        };

        InfoSet infoSet = TestUtils.createTestInfoSet();
        Node node1 = new NodeInfo(new Node("5"), null, null);

        infoSet.addChild(node1, TestUtils.createRootNode());

        JXPathContext context = new JXPathContext(infoSet);
        expression.evaluate(context);
    }

    @Test
    public void testIterators() throws EvaluationException {
        expression = new GreaterThanExpression(new VariableExpression("x"), new VariableExpression("y"));

        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);

        JXPathContext context = new JXPathContext(new TestInfoSet(list.iterator(), list.iterator()));
        SimpleIterator iterator1 = new SimpleIterator(Collections.singletonList(new NodeInfo(TestUtils.createRootNode(), null)).iterator());
        SimpleIterator iterator2 = new SimpleIterateIter(new NodeInfo(new Node("list"), null, null), new NodeInfo(new Node("node"), null, null), new IterateIter(list.iterator()));

        context.setNode(iterator1);
        Assert.assertFalse(expression.evaluate(context).getValue().equals(Boolean.TRUE));

        context.setNode(iterator2);
        Assert.assertFalse(expression.evaluate(context).getValue().equals(Boolean.TRUE));

        context.setNode(new NodeInfo(TestUtils.createRootNode(), null));
        Assert.setSystemOutPrintln(System.out);
        expression.evaluate(context);
        Assert.assertEquals("", System.out.getHistory().toString());

        context.setNode(new NodeInfo(new Node("list"), null, null));
        context.addVariable("x", new VariableExpression("x"));
        context.addVariable("y", new VariableExpression("y"));
        Path path = new Path();
        path.addStep("x");
        path.addStep(">");
        path.addStep("y");
        Assert.assertTrue(path.evaluate(context).getValue().equals(Boolean.TRUE));
    }
}