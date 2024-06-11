import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnionContextTest {

    private EvalContext context1;
    private EvalContext context2;
    private EvalContext context3;
    private UnionContext unionContext;

    @Before
    public void setUp() {
        context1 = new EvalContext();
        context2 = new EvalContext();
        context3 = new EvalContext();
        unionContext = new UnionContext(null, new EvalContext[]{context1, context2, context3});
    }

    @Test
    public void testGetDocumentOrder_multipleContexts_ordered() {
        context1.setDocumentOrder(1);
        context2.setDocumentOrder(2);
        context3.setDocumentOrder(3);

        int result = unionContext.getDocumentOrder();

        assertEquals(1, result);
    }

    @Test
    public void testGetDocumentOrder_singleContext_ordered() {
        context1.setDocumentOrder(1);

        int result = unionContext.getDocumentOrder();

        assertEquals(1, result);
    }

    @Test
    public void testGetDocumentOrder_singleContext_notOrdered() {
        context1.setDocumentOrder(0);

        int result = unionContext.getDocumentOrder();

        assertEquals(0, result);
    }

    @Test
    public void testSetPosition_prepareUnion() {
        unionContext.setPosition(1);

        assertTrue(unionContext.isPrepared());
    }

    @Test
    public void testSetPosition_multipleContexts() {
        context1.setNodes(new NodePointer[]{new NodePointer()});
        context2.setNodes(new NodePointer[]{new NodePointer()});
        context3.setNodes(new NodePointer[]{new NodePointer()});

        boolean result = unionContext.setPosition(1);

        assertTrue(result);
        assertEquals(1, unionContext.getPosition());
        assertEquals(1, unionContext.getCurrentNodePointer().getDocumentOrder());
    }

    @Test
    public void testSetPosition_multipleContexts_noNodes() {
        boolean result = unionContext.setPosition(1);

        assertFalse(result);
        assertEquals(0, unionContext.getPosition());
        assertNull(unionContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPosition_multipleContexts_unequalNodes() {
        context1.setNodes(new NodePointer[]{new NodePointer(), new NodePointer()});
        context2.setNodes(new NodePointer[]{new NodePointer()});
        context3.setNodes(new NodePointer[]{new NodePointer(), new NodePointer(), new NodePointer()});

        boolean result = unionContext.setPosition(3);

        assertFalse(result);
        assertEquals(0, unionContext.getPosition());
        assertNull(unionContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPosition_multipleContexts_invalidPosition() {
        context1.setNodes(new NodePointer[]{new NodePointer()});
        context2.setNodes(new NodePointer[]{new NodePointer()});
        context3.setNodes(new NodePointer[]{new NodePointer()});

        boolean result = unionContext.setPosition(0);

        assertFalse(result);
        assertEquals(0, unionContext.getPosition());
        assertNull(unionContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPosition_singleContext() {
        context1.setNodes(new NodePointer[]{new NodePointer(), new NodePointer()});
        context2.setNodes(new NodePointer[]{new NodePointer(), new NodePointer()});
        context3.setNodes(new NodePointer[]{new NodePointer(), new NodePointer()});

        boolean result = unionContext.setPosition(4);

        assertTrue(result);
        assertEquals(4, unionContext.getPosition());
        assertEquals(2, unionContext.getCurrentNodePointer().getDocumentOrder());
    }

    @Test
    public void testSetPosition_singleContext_noNodes() {
        boolean result = unionContext.setPosition(2);

        assertFalse(result);
        assertEquals(0, unionContext.getPosition());
        assertNull(unionContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPosition_singleContext_unequalNodes() {
        context1.setNodes(new NodePointer[]{new NodePointer(), new NodePointer()});
        context2.setNodes(new NodePointer[]{new NodePointer()});
        context3.setNodes(new NodePointer[]{new NodePointer(), new NodePointer(), new NodePointer()});

        boolean result = unionContext.setPosition(5);

        assertFalse(result);
        assertEquals(0, unionContext.getPosition());
        assertNull(unionContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPosition_singleContext_invalidPosition() {
        context1.setNodes(new NodePointer[]{new NodePointer(), new NodePointer()});
        context2.setNodes(new NodePointer[]{new NodePointer(), new NodePointer()});
        context3.setNodes(new NodePointer[]{new NodePointer(), new NodePointer()});

        boolean result = unionContext.setPosition(0);

        assertFalse(result);
        assertEquals(0, unionContext.getPosition());
        assertNull(unionContext.getCurrentNodePointer());
    }
}