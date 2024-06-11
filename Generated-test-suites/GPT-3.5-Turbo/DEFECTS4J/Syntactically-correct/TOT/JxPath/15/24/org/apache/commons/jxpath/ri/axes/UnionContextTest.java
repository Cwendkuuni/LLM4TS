import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;

public class UnionContextTest {

    private EvalContext parentContext;
    private EvalContext[] contexts;

    @Before
    public void setUp() {
        parentContext = mock(EvalContext.class);
        contexts = new EvalContext[3];
        for (int i = 0; i < 3; i++) {
            contexts[i] = mock(EvalContext.class);
        }
    }

    @Test
    public void testGetDocumentOrder_singleContext() {
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{contexts[0]});
        assertEquals(0, unionContext.getDocumentOrder());
    }

    @Test
    public void testGetDocumentOrder_multipleContexts() {
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        assertEquals(1, unionContext.getDocumentOrder());
    }

    @Test
    public void testSetPosition_preparedIsFalse() {
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        boolean result = unionContext.setPosition(0);
        assertEquals(true, result);
    }

    @Test
    public void testSetPosition_preparedIsTrue() {
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        unionContext.setPosition(0);
        boolean result = unionContext.setPosition(0);
        assertEquals(true, result);
    }

    @Test
    public void testSetPosition_edgeCase() {
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        boolean result = unionContext.setPosition(Integer.MAX_VALUE);
        assertEquals(true, result);
    }

    @Test
    public void testSetPosition_errorScenario() {
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        boolean result = unionContext.setPosition(-1);
        assertEquals(false, result);
    }

    @Test
    public void testSetPosition_errorScenario_preparedIsTrue() {
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        unionContext.setPosition(0);
        boolean result = unionContext.setPosition(-1);
        assertEquals(false, result);
    }

    // additional test cases for setPosition

    @Test
    public void testSetPosition_emptyContexts() {
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{});
        boolean result = unionContext.setPosition(0);
        assertEquals(false, result);
    }

    @Test
    public void testSetPosition_singleContext_emptyNodeSet() {
        EvalContext singleContext = mock(EvalContext.class);
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{singleContext});
        boolean result = unionContext.setPosition(0);
        assertEquals(false, result);
    }

    @Test
    public void testSetPosition_singleContext_singleNode() {
        EvalContext singleContext = mock(EvalContext.class);
        NodePointer nodePointer = mock(NodePointer.class);
        BasicNodeSet nodeSet = new BasicNodeSet();
        nodeSet.add(nodePointer);
        singleContext.setCurrentNodePointer(nodePointer);
        singleContext.setNodeSet(nodeSet);

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{singleContext});
        boolean result = unionContext.setPosition(0);
        assertEquals(true, result);
    }

    // additional test cases for constructor

    @Test
    public void testConstructor_singleContext() {
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{contexts[0]});
        assertEquals(parentContext, unionContext.getParentContext());
        assertEquals(contexts[0], unionContext.getContexts()[0]);
    }

    @Test
    public void testConstructor_multipleContexts() {
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        assertEquals(parentContext, unionContext.getParentContext());
        assertEquals(contexts[0], unionContext.getContexts()[0]);
        assertEquals(contexts[1], unionContext.getContexts()[1]);
        assertEquals(contexts[2], unionContext.getContexts()[2]);
    }
}