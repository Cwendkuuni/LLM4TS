import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CoreOperation;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGreaterThan() {
        CoreOperation op = new CoreOperationRelationalExpression(new CoreOperation[] {
                new CoreOperationNumber(1.5),
                new CoreOperationNumber(1.0)
        });

        Assert.assertEquals(Boolean.TRUE, op.computeValue(createContext(1.5f, 1.0f)));
        Assert.assertEquals(Boolean.FALSE, op.computeValue(createContext(1.0f, 1.5f)));
        Assert.assertEquals(Boolean.FALSE, op.computeValue(createContext(1.5f, 1.5f)));
        Assert.assertEquals(Boolean.FALSE, op.computeValue(createContext(null, 1.5f)));
        Assert.assertEquals(Boolean.FALSE, op.computeValue(createContext(1.5f, null)));
        Assert.assertEquals(Boolean.FALSE, op.computeValue(createContext(null, null)));
    }

    @Test
    public void testGreaterThanOrEqual() {
        CoreOperation op = new CoreOperationRelationalExpression(new CoreOperation[] {
                new CoreOperationNumber(1.5),
                new CoreOperationNumber(1.5)
        });

        Assert.assertEquals(Boolean.TRUE, op.computeValue(createContext(1.5f, 1.5f)));
        Assert.assertEquals(Boolean.TRUE, op.computeValue(createContext(1.5f, 1.0f)));
        Assert.assertEquals(Boolean.FALSE, op.computeValue(createContext(1.0f, 1.5f)));
        Assert.assertEquals(Boolean.FALSE, op.computeValue(createContext(1.5f, null)));
        Assert.assertEquals(Boolean.FALSE, op.computeValue(createContext(null, 1.5f)));
        Assert.assertEquals(Boolean.FALSE, op.computeValue(createContext(null, null)));
    }

    @Test
    public void testLessThan() {
        CoreOperation op = new CoreOperationRelationalExpression(new CoreOperation[] {
                new CoreOperationNumber(1.0),
                new CoreOperationNumber(1.5)
        });

        Assert.assertEquals(Boolean.FALSE, op.computeValue(createContext(1.5f, 1.0f)));
        Assert.assertEquals(Boolean.TRUE, op.computeValue(createContext(1.0f, 1.5f)));
        Assert.assertEquals(Boolean.FALSE, op.computeValue(createContext(1.5f, 1.5f)));
        Assert.assertEquals(Boolean.FALSE, op.computeValue(createContext(null, 1.5f)));
        Assert.assertEquals(Boolean.FALSE, op.computeValue(createContext(1.5f, null)));
        Assert.assertEquals(Boolean.FALSE, op.computeValue(createContext(null, null)));
    }

    @Test
    public void testLessThanOrEqual() {
        CoreOperation op = new CoreOperationRelationalExpression(new CoreOperation[] {
                new CoreOperationNumber(1.5),
                new CoreOperationNumber(1.5)
        });

        Assert.assertEquals(Boolean.TRUE, op.computeValue(createContext(1.5f, 1.5f)));
        Assert.assertEquals(Boolean.TRUE, op.computeValue(createContext(1.5f, 1.0f)));
        Assert.assertEquals(Boolean.TRUE, op.computeValue(createContext(1.0f, 1.5f)));
        Assert.assertEquals(Boolean.FALSE, op.computeValue(createContext(1.5f, null)));
        Assert.assertEquals(Boolean.FALSE, op.computeValue(createContext(null, 1.5f)));
        Assert.assertEquals(Boolean.FALSE, op.computeValue(createContext(null, null)));
    }

    @Test
    public void testIterators() {
        Node root = createTestNode();
        EvalContext context = new EvalContext(root);

        List<Node> list1 = new ArrayList<>();
        list1.add(createTestNode("a"));
        list1.add(createTestNode("b"));

        List<Node> list2 = new ArrayList<>();
        list2.add(createTestNode("b"));
        list2.add(createTestNode("c"));

        CoreOperation op = new CoreOperationRelationalExpression(new CoreOperation[] {
                new CoreOperationCollection(list1),
                new CoreOperationCollection(list2)
        });

        Iterator it1 = list1.iterator();
        Iterator it2 = list2.iterator();

        // test containsMatch
        Assert.assertEquals(Boolean.TRUE, op.computeValue(context).booleanValue());

        // test findMatch
        CoreOperationRelationalExpression relationalExpression = (CoreOperationRelationalExpression) op;
        Assert.assertEquals(Boolean.TRUE, relationalExpression.findMatch(it1, it2).booleanValue());

        // test containsMatch
        Assert.assertEquals(Boolean.FALSE, relationalExpression.containsMatch(it1, createTestNode("d")).booleanValue());
    }

    @Test
    public void testNullValues() {
        EvalContext context = new EvalContext(null);

        CoreOperation op = new CoreOperationRelationalExpression(new CoreOperation[] {
                new CoreOperationNumber(1),
                new CoreOperationNumber(1)
        });

        Assert.assertEquals(Boolean.FALSE, op.computeValue(context).booleanValue());
    }

    private Node createTestNode(String name) {
        Node node = DocumentBuilderFactory.newInstance().newDocumentBuilder().newElement("node");
        node.setNodeName(name);
        return node;
    }

    private Node createTestNode() {
        return DocumentBuilderFactory.newInstance().newDocumentBuilder().newElement("node");
    }

    private JXPathContext createContext(Object left, Object right) {
        Node root = createTestNode();
        EvalContext context = new EvalContext(root);
        context.setCurrentNode(context.getSingleNodePointer());

        if (left != null) {
            context.setVariable("left", left);
        }

        if (right != null) {
            context.setVariable("right", right);
        }

        return new JXPathContext(context);
    }
}