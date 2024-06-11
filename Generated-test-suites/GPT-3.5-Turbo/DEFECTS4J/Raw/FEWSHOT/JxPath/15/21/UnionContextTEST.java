package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class UnionContextTest {
    private EvalContext parentContext;
    private EvalContext[] contexts;

    @Before
    public void setUp() {
        parentContext = new EvalContext();
        contexts = new EvalContext[3];
        contexts[0] = new EvalContext();
        contexts[1] = new EvalContext();
        contexts[2] = new EvalContext();
    }

    @Test
    public void testConstructor() {
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        assertTrue(unionContext.getNodeSet() instanceof BasicNodeSet);
    }

    @Test
    public void testGetDocumentOrder() {
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        assertEquals(1, unionContext.getDocumentOrder());
    }

    @Test
    public void testSetPosition() {
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        assertTrue(unionContext.setPosition(1));
    }
}