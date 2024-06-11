import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;

public class UnionContextTest {
    private EvalContext parentContext;
    private EvalContext[] contexts;

    @Before
    public void setup() {
        parentContext = new EvalContext();
        contexts = new EvalContext[2];
        contexts[0] = new EvalContext();
        contexts[1] = new EvalContext();
    }

    @Test
    public void testConstructor() {
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        assertNotNull(unionContext);
        assertEquals(parentContext, unionContext.getParentContext());
        assertTrue(unionContext.getNodeSet() instanceof BasicNodeSet);
        assertTrue(unionContext.getContexts() instanceof EvalContext[]);
        assertEquals(2, unionContext.getContexts().length);
        assertEquals(contexts[0], unionContext.getContexts()[0]);
        assertEquals(contexts[1], unionContext.getContexts()[1]);
    }

    @Test
    public void testGetDocumentOrder() {
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        assertTrue(unionContext.getDocumentOrder() > 0);
    }

    @Test
    public void testSetPosition() {
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        assertTrue(unionContext.setPosition(1));
        assertTrue(unionContext.isPositionSet());
        assertTrue(unionContext.getPosition() == 1);
    }

    @Test
    public void testSetPositionForUnpreparedContext() {
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        assertFalse(unionContext.isPositionSet());
        assertFalse(unionContext.setPosition(1));
        assertFalse(unionContext.isPositionSet());
    }

    @Test
    public void testSetPositionForPreparedContext() {
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        unionContext.setPosition(1);
        assertTrue(unionContext.isPositionSet());
        assertTrue(unionContext.setPosition(2));
        assertTrue(unionContext.isPositionSet());
        assertTrue(unionContext.getPosition() == 2);
    }

    @Test
    public void testSetPositionForMultipleContexts() {
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        unionContext.setPosition(1);
        unionContext.setPosition(2);
        assertTrue(unionContext.isPositionSet());
        assertTrue(unionContext.getPosition() == 2);
    }
}