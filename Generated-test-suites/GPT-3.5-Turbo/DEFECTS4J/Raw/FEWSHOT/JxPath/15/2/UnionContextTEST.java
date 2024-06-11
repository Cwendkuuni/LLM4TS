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
    private UnionContext unionContext;

    @Before
    public void setup() {
        EvalContext[] contexts = new EvalContext[2];
        contexts[0] = new EvalContext();
        contexts[1] = new EvalContext();

        unionContext = new UnionContext(new EvalContext(), contexts);
    }

    @Test
    public void testConstructor() {
        assertTrue(unionContext.getNodeSet() instanceof BasicNodeSet);
        assertEquals(0, unionContext.getDocumentOrder());
    }

    @Test
    public void testSetPosition() {
        unionContext.setPosition(1);

        assertTrue(unionContext.setPosition(1));
    }
}