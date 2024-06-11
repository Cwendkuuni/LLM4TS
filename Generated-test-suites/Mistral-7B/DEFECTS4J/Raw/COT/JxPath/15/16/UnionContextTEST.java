package org.apache.commons.jxpath.ri.axes.test;

import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.NodeSetContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.test.JxPathTest;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.apache.commons.jxpath.test.TestUtils.createNodePointer;
import static org.apache.commons.jxpath.test.TestUtils.createTestEvalContext;

/**
 * Test UnionContext class
 */
public class UnionContextTest {

    @Test
    public void testUnionContext() {
        EvalContext contextA = createTestEvalContext();
        contextA.pushContext(createTestEvalContext());
        contextA.setCurrentNodePointer(createNodePointer("/a/1"));

        EvalContext contextB = createTestEvalContext();
        contextB.pushContext(createTestEvalContext());
        contextB.setCurrentNodePointer(createNodePointer("/b/1"));

        NodeSetContext unionContext = new UnionContext(new NodeSetContext(new BasicNodeSet(), contextA), new EvalContext[]{contextB});

        Assert.assertEquals(2, unionContext.getDocumentOrder());

        List<NodePointer> expectedNodes = new ArrayList<>(Arrays.asList(
                createNodePointer("/a/1"),
                createNodePointer("/b/1")
        ));

        for (int i = 0; i < 5; i++) {
            Assert.assertTrue(unionContext.nextSet());
            List<NodePointer> actualNodes = new ArrayList<>();
            while (unionContext.nextNode()) {
                actualNodes.add(unionContext.getCurrentNodePointer());
            }
            Assert.assertEquals(expectedNodes, actualNodes);
            expectedNodes.clear();
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testUnionContextWithoutPrepare() {
        EvalContext contextA = createTestEvalContext();
        contextA.pushContext(createTestEvalContext());
        contextA.setCurrentNodePointer(createNodePointer("/a/1"));

        EvalContext contextB = createTestEvalContext();
        contextB.pushContext(createTestEvalContext());
        contextB.setCurrentNodePointer(createNodePointer("/b/1"));

        NodeSetContext unionContext = new UnionContext(new NodeSetContext(new BasicNodeSet(), contextA), new EvalContext[]{contextB});

        for (int i = 0; i < 5; i++) {
            unionContext.nextSet();
            unionContext.nextNode();
        }
    }

    @Test
    public void testUnionContextEmptyContexts() {
        EvalContext contextA = createTestEvalContext();
        NodeSetContext unionContext = new UnionContext(new NodeSetContext(new BasicNodeSet(), contextA), new EvalContext[]{});

        Assert.assertEquals(0, unionContext.getDocumentOrder());
        Assert.assertFalse(unionContext.nextSet());
    }

    @Test
    public void testUnionContextOneContext() {
        EvalContext context = createTestEvalContext();

        NodeSetContext unionContext = new UnionContext(new NodeSetContext(new BasicNodeSet(), context), new EvalContext[]{});

        Assert.assertEquals(1, unionContext.getDocumentOrder());

        List<NodePointer> expectedNodes = new ArrayList<>();
        expectedNodes.add(context.getCurrentNodePointer());

        for (int i = 0; i < 5; i++) {
            Assert.assertTrue(unionContext.nextSet());
            List<NodePointer> actualNodes = new ArrayList<>();
            while (unionContext.nextNode()) {
                actualNodes.add(unionContext.getCurrentNodePointer());
            }
            Assert.assertEquals(expectedNodes, actualNodes);
            expectedNodes.clear();
        }
    }
}