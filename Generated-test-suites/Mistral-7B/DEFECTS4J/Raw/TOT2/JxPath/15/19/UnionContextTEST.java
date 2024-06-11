package org.apache.commons.jxpath.ri.axes.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.axes.UnionContext;
import org.junit.Test;

public class UnionContextTest {

    @Test
    public void testUnionContextConstructor() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext[] contexts = new EvalContext[2];
        UnionContext unionContext = new UnionContext(parentContext, contexts);

        assertNotNull(unionContext);
        assertEquals(0, unionContext.getDocumentOrder());
    }

    @Test
    public void testGetDocumentOrder() {
        UnionContext unionContext = new UnionContext(new EvalContext(new BasicNodeSet()), new EvalContext[0]);
        assertEquals(0, unionContext.getDocumentOrder());

        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());
        UnionContext unionContextWithContexts = new UnionContext(new EvalContext(new BasicNodeSet()), new EvalContext[] { context1, context2 });
        assertEquals(1, unionContextWithContexts.getDocumentOrder());
    }

    @Test
    public void testSetPosition() {
        UnionContext unionContext = new UnionContext(new EvalContext(new BasicNodeSet()), new EvalContext[0]);
        assertFalse(unionContext.setPosition(0));

        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());
        UnionContext unionContextWithContexts = new UnionContext(new EvalContext(new BasicNodeSet()), new EvalContext[] { context1, context2 });

        assertTrue(unionContextWithContexts.setPosition(0));
        assertTrue(unionContextWithContexts.setPosition(1));
        assertFalse(unionContextWithContexts.setPosition(2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnionContextEmptyContexts() {
        UnionContext unionContext = new UnionContext(new EvalContext(new BasicNodeSet()), new EvalContext[0]);
    }

    @Test
    public void testSetPositionNegativePosition() {
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());
        UnionContext unionContextWithContexts = new UnionContext(new EvalContext(new BasicNodeSet()), new EvalContext[] { context1, context2 });

        assertFalse(unionContextWithContexts.setPosition(-1));
    }

    @Test
    public void testUnionContextException() {
        try {
            new UnionContext(null, new EvalContext[0]);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            new UnionContext(new EvalContext(new BasicNodeSet()), null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testUnionContextPrepareBeforeSetPosition() {
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());
        UnionContext unionContextWithContexts = new UnionContext(new EvalContext(new BasicNodeSet()), new EvalContext[] { context1, context2 });

        assertFalse(unionContextWithContexts.setPosition(0)); // position should be set after preparation
    }

    @Test
    public void testUnionContextOutOfBoundsPosition() {
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());
        UnionContext unionContextWithContexts = new UnionContext(new EvalContext(new BasicNodeSet()), new EvalContext[] { context1, context2 });

        unionContextWithContexts.setPosition(0); // position 0 is valid
        unionContextWithContexts.setPosition(unionContextWithContexts.size() + 1); // position out of bounds, should return false
    }
}