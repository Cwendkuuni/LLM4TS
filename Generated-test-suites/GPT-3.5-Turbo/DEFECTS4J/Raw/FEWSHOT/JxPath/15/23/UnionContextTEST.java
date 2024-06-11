package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;

public class UnionContextTest {

    @Test
    public void testGetDocumentOrder() {
        UnionContext context = new UnionContext(null, new EvalContext[]{});
        assertEquals(1, context.getDocumentOrder());
        
        context = new UnionContext(null, new EvalContext[]{new EvalContext(null)});
        assertEquals(1, context.getDocumentOrder());

        context = new UnionContext(null, new EvalContext[]{new EvalContext(null), new EvalContext(null)});
        assertEquals(1, context.getDocumentOrder());

        context = new UnionContext(null, new EvalContext[]{new EvalContext(null), new EvalContext(null), new EvalContext(null)});
        assertEquals(1, context.getDocumentOrder());

        context = new UnionContext(null, new EvalContext[]{new EvalContext(null), new EvalContext(null), new EvalContext(null), new EvalContext(null)});
        assertEquals(1, context.getDocumentOrder());
    }

    @Test
    public void testSetPosition() {
        UnionContext context = new UnionContext(null, new EvalContext[]{});
        assertFalse(context.setPosition(0));

        context = new UnionContext(null, new EvalContext[]{new EvalContext(null)});
        assertFalse(context.setPosition(0));

        context = new UnionContext(null, new EvalContext[]{new EvalContext(null), new EvalContext(null)});
        assertFalse(context.setPosition(0));

        context = new UnionContext(null, new EvalContext[]{new EvalContext(null), new EvalContext(null), new EvalContext(null)});
        assertFalse(context.setPosition(0));

        context = new UnionContext(null, new EvalContext[]{new EvalContext(null), new EvalContext(null), new EvalContext(null), new EvalContext(null)});
        assertFalse(context.setPosition(0));

        context = new UnionContext(null, new EvalContext[]{new EvalContext(null)});
        assertTrue(context.setPosition(1));

        context = new UnionContext(null, new EvalContext[]{new EvalContext(null), new EvalContext(null)});
        assertTrue(context.setPosition(1));

        context = new UnionContext(null, new EvalContext[]{new EvalContext(null), new EvalContext(null), new EvalContext(null)});
        assertTrue(context.setPosition(1));

        context = new UnionContext(null, new EvalContext[]{new EvalContext(null), new EvalContext(null), new EvalContext(null), new EvalContext(null)});
        assertTrue(context.setPosition(1));
    }
}