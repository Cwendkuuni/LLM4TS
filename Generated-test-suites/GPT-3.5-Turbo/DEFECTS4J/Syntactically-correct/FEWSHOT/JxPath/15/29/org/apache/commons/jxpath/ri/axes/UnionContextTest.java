package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;

public class UnionContextTest {
    private EvalContext parentContext;
    private EvalContext[] contexts;

    @Before
    public void setup() {
        parentContext = new EvalContext();
        contexts = new EvalContext[] { new EvalContext(), new EvalContext() };
    }

    @Test
    public void testConstructor() {
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        assertNotNull(unionContext);
        assertEquals(parentContext, unionContext.getParentContext());
        assertEquals(new BasicNodeSet(), unionContext.getNodeSet());
    }

    @Test
    public void testGetDocumentOrder() {
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        assertEquals(1, unionContext.getDocumentOrder());
        contexts = new EvalContext[0];
        UnionContext emptyUnionContext = new UnionContext(parentContext, contexts);
        assertEquals(0, emptyUnionContext.getDocumentOrder());
    }

    @Test
    public void testSetPosition() {
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        unionContext.setPosition(1);
        assertTrue(unionContext.isPrepared());
        unionContext.setPosition(2);
        assertFalse(unionContext.isPrepared());
    }
}