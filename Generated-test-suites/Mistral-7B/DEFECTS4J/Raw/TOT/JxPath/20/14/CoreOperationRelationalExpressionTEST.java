package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.IteratingIterator;
import org.apache.commons.jxpath.ri.Node;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.Nodes;
import org.apache.commons.jxpath.ri.Path;
import org.apache.commons.jxpath.ri. axes.Axis;
import org.apache.commons.jxpath.ri.axes.SelfAxis;
import org.apache.commons.jxpath.ri.compilers.CompiledExpression;
import org.apache.commons.jxpath.ri.compilers.CompiledExpressionFactory;
import org.apache.commons.jxpath.ri.compilers.StaticContext;
import org.junit.Before;
import org.junit.Test;

/**
 * Test suite for {@link CoreOperationRelationalExpression}.
 */
public class CoreOperationRelationalExpressionTest {

    private static final String XPATH_EXPRESSION_GT = "1 > 2";
    private static final String XPATH_EXPRESSION_GTE = "1 >= 2";
    private static final String XPATH_EXPRESSION_LT = "1 < 2";
    private static final String XPATH_EXPRESSION_LTE = "1 <= 2";

    private CoreOperationRelationalExpression gtExpression;
    private CoreOperationRelationalExpression gteExpression;
    private CoreOperationRelationalExpression ltExpression;
    private CoreOperationRelationalExpression lteExpression;

    private CompiledExpression gtCompiled;
    private CompiledExpression gteCompiled;
    private CompiledExpression ltCompiled;
    private CompiledExpression lteCompiled;

    @Before
    public void setUp() {
        gtExpression = new GreaterExpression(new Expression[0]);
        gteExpression = new GreaterOrEqualExpression(new Expression[0]);
        ltExpression = new LessExpression(new Expression[0]);
        lteExpression = new LessOrEqualExpression(new Expression[0]);

        gtCompiled = CompiledExpressionFactory.compileExpression(XPATH_EXPRESSION_GT, new StaticContext());
        gteCompiled = CompiledExpressionFactory.compileExpression(XPATH_EXPRESSION_GTE, new StaticContext());
        ltCompiled = CompiledExpressionFactory.compileExpression(XPATH_EXPRESSION_LT, new StaticContext());
        lteCompiled = CompiledExpressionFactory.compileExpression(XPATH_EXPRESSION_LTE, new StaticContext());
    }

    @Test
    public void testGreaterExpression() {
        testRelationalExpression(gtExpression, gtCompiled, false, false, false, false);
    }

    @Test
    public void testGreaterOrEqualExpression() {
        testRelationalExpression(gteExpression, gteCompiled, false, true, false, false);
    }

    @Test
    public void testLessExpression() {
        testRelationalExpression(ltExpression, ltCompiled, true, false, false, false);
    }

    @Test
    public void testLessOrEqualExpression() {
        testRelationalExpression(lteExpression, lteCompiled, true, true, false, false);
    }

    private void testRelationalExpression(CoreOperationRelationalExpression expression,
                                          CompiledExpression compiled,
                                          boolean expectedLeftIsLess,
                                          boolean expectedLeftIsGreaterOrEqual,
                                          boolean expectedRightIsLess,
                                          boolean expectedRightIsGreaterOrEqual) {
        Node leftNode = createNode(1);
        Node rightNode = createNode(2);
        InfoSet infoSet = createInfoSet(leftNode, rightNode);

        EvalContext context = new EvalContext(new StaticContext(), new Path("/"), infoSet);

        Node result = expression.compute(context);
        assertEquals(Boolean.class, result.getClass());

        boolean resultBoolean = ((NodeInfo) result).getNodeValue().booleanValue();

        assertTrue(expectedLeftIsLess ? resultBoolean : !resultBoolean);
        assertTrue(expectedLeftIsGreaterOrEqual ? !resultBoolean : resultBoolean);
        assertTrue(expectedRightIsLess ? !resultBoolean : expectedRightIsGreaterOrEqual);
    }

    private Node createNode(int value) {
        Node node = new NodeInfo(new Node(Axis.SELF, null, value, null), null, null);
        return node;
    }

    private InfoSet createInfoSet(Node... nodes) {
        NodeIterator nodeIterator = Nodes.iterator(Arrays.asList(nodes));
        Iterator<Node> iterator = nodeIterator.asIterator();
        return new InfoSet(new Nodes(iterator, null, null), null, true);
    }
}