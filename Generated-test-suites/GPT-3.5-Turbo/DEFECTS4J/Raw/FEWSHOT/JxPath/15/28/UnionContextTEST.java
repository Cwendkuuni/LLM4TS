import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;

public class UnionContextTest {
    private EvalContext parentContext;
    private EvalContext[] contexts;
    private UnionContext unionContext;

    @Before
    public void setup() {
        parentContext = null;
        contexts = new EvalContext[2];
        unionContext = new UnionContext(parentContext, contexts);
    }

    @Test
    public void testGetDocumentOrder() {
        assertEquals(1, unionContext.getDocumentOrder());
        contexts = new EvalContext[1];
        unionContext = new UnionContext(parentContext, contexts);
        assertEquals(0, unionContext.getDocumentOrder());
    }

    @Test
    public void testSetPosition() {
        unionContext.setPosition(1);
        assertTrue(unionContext.getPosition() == 1);
    }

    @Test
    public void testSetPositionWithInvalidPosition() {
        unionContext.setPosition(-1);
        assertTrue(unionContext.getPosition() == 1);
    }

    @Test
    public void testSetPositionWhenNotPrepared() {
        unionContext.setPosition(5);
        assertTrue(unionContext.getPosition() == 5);
    }

    @Test
    public void testSetPositionWhenPrepared() {
        unionContext.setPosition(5);
        assertTrue(unionContext.getPosition() == 5);
        unionContext.setPosition(2);
        assertTrue(unionContext.getPosition() == 2);
    }
    
    @Test
    public void testSetPositionWithEmptyContexts() {
        contexts = new EvalContext[0];
        unionContext = new UnionContext(parentContext, contexts);
        unionContext.setPosition(5);
        assertTrue(unionContext.getPosition() == 1);
    }
}