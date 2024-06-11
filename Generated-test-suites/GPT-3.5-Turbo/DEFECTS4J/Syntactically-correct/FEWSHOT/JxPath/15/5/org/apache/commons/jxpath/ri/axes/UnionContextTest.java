package org.apache.commons.jxpath.ri.axes;

import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;

public class UnionContextTest {

    private UnionContext unionContext;

    @Before
    public void setUp() {
        EvalContext parentContext = new EvalContext();
        EvalContext[] contexts = new EvalContext[3];
        contexts[0] = new EvalContext();
        contexts[1] = new EvalContext();
        contexts[2] = new EvalContext();
        unionContext = new UnionContext(parentContext, contexts);
    }

    @Test
    public void testGetDocumentOrder() {
        int expected = 1;
        int actual = unionContext.getDocumentOrder();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetPosition() {
        int position = 3;
        boolean expected = true;
        boolean actual = unionContext.setPosition(position);
        assertEquals(expected, actual);
    }

    @Test
    public void testSetPositionNotPrepared() {
        int position = 3;
        EvalContext[] contexts = new EvalContext[1];
        contexts[0] = new EvalContext();
        UnionContext unionContext = new UnionContext(null, contexts);
        boolean expected = true;
        boolean actual = unionContext.setPosition(position);
        assertEquals(expected, actual);
    }
}