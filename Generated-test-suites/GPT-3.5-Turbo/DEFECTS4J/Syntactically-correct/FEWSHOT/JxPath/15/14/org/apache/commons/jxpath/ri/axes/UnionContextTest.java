import static org.junit.Assert.*;

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
    public void setUp() {
        parentContext = createMock(EvalContext.class);
        contexts = new EvalContext[2];
        contexts[0] = createMock(EvalContext.class);
        contexts[1] = createMock(EvalContext.class);
        unionContext = new UnionContext(parentContext, contexts);
    }

    @Test
    public void testConstructor() {
        assertTrue(unionContext instanceof NodeSetContext);
        assertNotNull(unionContext.getNodeSet());
        assertEquals(parentContext, unionContext.getParentContext());
        assertArrayEquals(contexts, unionContext.getContexts());
        assertFalse(unionContext.isPrepared());
    }

    @Test
    public void testGetDocumentOrder() {
        contexts = new EvalContext[1];
        contexts[0] = createMock(EvalContext.class);
        unionContext = new UnionContext(parentContext, contexts);
        assertEquals(1, unionContext.getDocumentOrder());

        contexts = new EvalContext[2];
        contexts[0] = createMock(EvalContext.class);
        contexts[1] = createMock(EvalContext.class);
        unionContext = new UnionContext(parentContext, contexts);
        assertEquals(1, unionContext.getDocumentOrder());
    }

    @Test
    public void testSetPosition() {
        unionContext.setPosition(1);
        assertTrue(unionContext.isPrepared());
        verify(parentContext).setPosition(1);

        reset(parentContext);
        unionContext.setPosition(2);
        assertFalse(unionContext.isPrepared());
        verify(parentContext).setPosition(2);
    }

    @Test
    public void testSetPositionWithUnpreparedContexts() {
        reset(contexts[0]);
        reset(contexts[1]);
        contexts[0].nextSet();
        contexts[0].nextNode();
        expect(contexts[0].getCurrentNodePointer()).andReturn(createMock(NodePointer.class));
        ReplayMocks();
        unionContext.setPosition(1);
        assertTrue(unionContext.isPrepared());
        verify(contexts[0], contexts[1]);

        reset(contexts[0]);
        reset(contexts[1]);
        contexts[0].nextSet();
        contexts[0].nextNode();
        expect(contexts[0].getCurrentNodePointer()).andReturn(createMock(NodePointer.class));
        contexts[1].nextSet();
        contexts[0].nextNode();
        expect(contexts[1].getCurrentNodePointer()).andReturn(createMock(NodePointer.class));
        ReplayMocks();
        unionContext.setPosition(2);
        assertTrue(unionContext.isPrepared());
        verify(contexts[0], contexts[1]);
    }

    private void ReplayMocks() {
        replay(parentContext);
        replay(contexts[0]);
        replay(contexts[1]);
    }

    private void verify(Object... mocks) {
        for (Object mock : mocks) {
            verify(mock);
        }
    }
}