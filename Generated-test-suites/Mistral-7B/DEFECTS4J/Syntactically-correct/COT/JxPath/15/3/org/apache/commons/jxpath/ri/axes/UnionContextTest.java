package org.apache.commons.jxpath.ri.axes.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.ri.model.NodeSet;
import org.apache.commons.jxpath.ri.axes.UnionContext;
import org.apache.commons.jxpath.ri.axes.UnionContextTest;
import org.junit.Test;

/**
 * Test cases for UnionContext class
 *
 * @author junit-test-generator
 */
public class UnionContextTest {

    // Test constructor
    @Test
    public void testUnionContextConstructor() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext contexts[] = new EvalContext[2];
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        assertNotNull(unionContext);
    }

    // Test getDocumentOrder method
    @Test
    public void testGetDocumentOrder() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});
        assertTrue(unionContext.getDocumentOrder() > 0);
    }

    // Test setPosition method with empty contexts array
    @Test
    public void testSetPositionWithEmptyContexts() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[0]);
        assertFalse(unionContext.setPosition(0));
    }

    // Test setPosition method with single context
    @Test
    public void testSetPositionWithSingleContext() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context = new EvalContext(new BasicNodeSet());
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context});
        NodePointer nodePointer = new NodePointer(new Object(), 0);
        unionContext.setPosition(0);
        assertTrue(unionContext.setPosition(0));
        assertTrue(unionContext.getCurrentNodePointer().equals(nodePointer));
    }

    // Test setPosition method with multiple contexts
    @Test
    public void testSetPositionWithMultipleContexts() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});

        NodePointer nodePointer1 = new NodePointer(new Object(), 0);
        context1.setCurrentNodePointer(nodePointer1);
        context1.nextSet();

        NodePointer nodePointer2 = new NodePointer(new Object(), 1);
        context2.setCurrentNodePointer(nodePointer2);
        context2.nextSet();

        unionContext.setPosition(1);
        assertTrue(unionContext.getCurrentNodePointer().equals(nodePointer2));
    }

    // Test setPosition method with out of bounds index
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetPositionWithOutOfBoundsIndex() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});
        unionContext.setPosition(2);
    }

    // Test setPosition method with prepared context
    @Test
    public void testSetPositionWithPreparedContext() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});
        unionContext.setPosition(0);
        unionContext.prepare();
        unionContext.setPosition(1);
        assertNull(unionContext.getCurrentNodePointer());
    }

    // Test getNodeSet method
    @Test
    public void testgetNodeSet() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});
        assertNotNull(unionContext.getNodeSet());
    }

    // Test getParentContext method
    @Test
    public void testGetParentContext() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context1});
        assertSame(parentContext, unionContext.getParentContext());
    }

}