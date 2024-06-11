import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;

public class UnionContextTest {
    private EvalContext parentContext;
    private EvalContext context1;
    private EvalContext context2;
    private EvalContext context3;
    private UnionContext unionContext;

    @Before
    public void setup() {
        parentContext = new MockEvalContext();
        context1 = new MockEvalContext();
        context2 = new MockEvalContext();
        context3 = new MockEvalContext();
        unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2, context3});
    }

    @Test
    public void testGetDocumentOrder() {
        assertEquals(1, unionContext.getDocumentOrder());
        context2 = new MockEvalContext();
        unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});
        assertEquals(2, unionContext.getDocumentOrder());
    }

    @Test
    public void testSetPosition() {
        unionContext.setPosition(1);
        assertTrue(unionContext.isPrepared());
        unionContext.setPosition(2);
        assertFalse(unionContext.isPrepared());
    }
}

class MockEvalContext extends EvalContext {
    private boolean nextSetCalled;
    private boolean nextNodeCalled;
    private NodePointer currentPointer;

    public MockEvalContext() {
        super(null);
    }

    public boolean nextSet() {
        nextSetCalled = true;
        return true;
    }

    public boolean nextNode() {
        nextNodeCalled = true;
        return true;
    }

    public NodePointer getCurrentNodePointer() {
        return currentPointer;
    }

    public boolean isPrepared() {
        return nextSetCalled && nextNodeCalled;
    }
}