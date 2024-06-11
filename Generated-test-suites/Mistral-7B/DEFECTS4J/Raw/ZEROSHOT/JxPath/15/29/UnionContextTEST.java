import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class UnionContextTest {

    private EvalContext contextA;
    private EvalContext contextB;
    private EvalContext parentContext;
    private UnionContext unionContext;
    private List<NodePointer> nodesA;
    private List<NodePointer> nodesB;
    private List<NodePointer> expectedNodes;

    public UnionContextTest(List<NodePointer> nodesA, List<NodePointer> nodesB, List<NodePointer> expectedNodes) {
        this.nodesA = nodesA;
        this.nodesB = nodesB;
        this.expectedNodes = expectedNodes;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {Arrays.asList(Mockito.mock(NodePointer.class)), Arrays.asList(Mockito.mock(NodePointer.class)), Arrays.asList(Mockito.mock(NodePointer.class))},
                {Arrays.asList(Mockito.mock(NodePointer.class), Mockito.mock(NodePointer.class)), Arrays.asList(Mockito.mock(NodePointer.class), Mockito.mock(NodePointer.class)), Arrays.asList(Mockito.mock(NodePointer.class), Mockito.mock(NodePointer.class))},
                {Arrays.asList(Mockito.mock(NodePointer.class), Mockito.mock(NodePointer.class), Mockito.mock(NodePointer.class)), Arrays.asList(Mockito.mock(NodePointer.class), Mockito.mock(NodePointer.class)), Arrays.asList(Mockito.mock(NodePointer.class), Mockito.mock(NodePointer.class), Mockito.mock(NodePointer.class))},
        });
    }

    @Before
    public void setUp() {
        contextA = Mockito.mock(EvalContext.class);
        contextB = Mockito.mock(EvalContext.class);
        parentContext = Mockito.mock(EvalContext.class);

        Mockito.doReturn(contextA).when(contextB).getParentContext();
        Mockito.doReturn(parentContext).when(contextA).getParentContext();

        unionContext = new UnionContext(new EvalContext[]{contextA, contextB}, parentContext);
    }

    @Test
    public void testGetDocumentOrder() {
        assertEquals(1, unionContext.getDocumentOrder());
    }

    @Test
    public void testSetPosition_whenFirstContextHasNodes_thenFirstNodeIsReturned() {
        for (NodePointer node : nodesA) {
            Mockito.doReturn(true).when(contextA).nextSet();
            Mockito.doReturn(node).when(contextA).getCurrentNodePointer();
        }

        Mockito.doReturn(false).when(contextB).hasMoreNodes();

        int position = 0;
        NodePointer actualNode = unionContext.getNode(position);

        assertEquals(expectedNodes.get(position), actualNode);

        assertTrue(unionContext.setPosition(position + 1));
    }

    @Test
    public void testSetPosition_whenFirstContextHasNoMoreNodes_thenSecondContextIsUsed() {
        for (NodePointer node : nodesA) {
            Mockito.doReturn(true).when(contextA).nextSet();
            Mockito.doReturn(node).when(contextA).getCurrentNodePointer();
        }

        Mockito.doReturn(false).when(contextA).hasMoreNodes();

        for (NodePointer node : nodesB) {
            Mockito.doReturn(true).when(contextB).nextSet();
            Mockito.doReturn(node).when(contextB).getCurrentNodePointer();
        }

        int position = nodesA.size();
        NodePointer actualNode = unionContext.getNode(position);

        assertEquals(expectedNodes.get(position), actualNode);

        assertTrue(unionContext.setPosition(position + 1));
    }

    @Test
    public void testSetPosition_whenBothContextsHaveNodes_thenNodesAreReturnedInOrder() {
        for (NodePointer nodeA : nodesA) {
            Mockito.doReturn(true).when(contextA).nextSet();
            Mockito.doReturn(nodeA).when(contextA).getCurrentNodePointer();
        }

        for (NodePointer nodeB : nodesB) {
            Mockito.doReturn(true).when(contextB).nextSet();
            Mockito.doReturn(nodeB).when(contextB).getCurrentNodePointer();
        }

        int position = 0;
        NodePointer actualNode = unionContext.getNode(position);

        assertEquals(expectedNodes.get(position), actualNode);

        assertTrue(unionContext.setPosition(position + 1));

        position++;
        actualNode = unionContext.getNode(position);

        assertEquals(expectedNodes.get(position), actualNode);

        assertTrue(unionContext.setPosition(position + 1));

        position++;
        actualNode = unionContext.getNode(position);

        if (position < expectedNodes.size()) {
            assertEquals(expectedNodes.get(position), actualNode);
        } else {
            assertNull(actualNode);
        }

        assertFalse(unionContext.setPosition(position + 1));
    }

    @Test
    public void testSetPosition_whenBothContextsHaveNoMoreNodes_thenReturnNull() {
        for (NodePointer nodeA : nodesA) {
            Mockito.doReturn(true).when(contextA).nextSet();
            Mockito.doReturn(nodeA).when(contextA).getCurrentNodePointer();
        }

        Mockito.doReturn(false).when(contextA).hasMoreNodes();

        for (NodePointer nodeB : nodesB) {
            Mockito.doReturn(true).when(contextB).nextSet();
            Mockito.doReturn(nodeB).when(contextB).getCurrentNodePointer();
        }

        Mockito.doReturn(false).when(contextB).hasMoreNodes();

        int position = 0;
        NodePointer actualNode = unionContext.getNode(position);

        assertEquals(expectedNodes.get(position), actualNode);

        assertTrue(unionContext.setPosition(position + 1));

        position++;
        actualNode = unionContext.getNode(position);

        if (position < expectedNodes.size()) {
            assertEquals(expectedNodes.get(position), actualNode);
        } else {
            assertNull(actualNode);
        }

        assertFalse(unionContext.setPosition(position + 1));
    }

    @Test
    public void testSize_whenFirstContextHasMoreNodes_thenSizeIsEqualToFirstContextSize() {
        for (NodePointer nodeA : nodesA) {
            Mockito.doReturn(true).when(contextA).nextSet();
            Mockito.doReturn(nodeA).when(contextA).getCurrentNodePointer();
        }

        Mockito.doReturn(false).when(contextB).hasMoreNodes();

        int size = unionContext.size();

        assertEquals(nodesA.size(), size);
    }

    @Test
    public void testSize_whenFirstContextHasNoMoreNodes_thenSizeIsEqualToSecondContextSize() {
        for (NodePointer nodeA : nodesA) {
            Mockito.doReturn(true).when(contextA).nextSet();
            Mockito.doReturn(nodeA).when(contextA).getCurrentNodePointer();
        }

        Mockito.doReturn(false).when(contextA).hasMoreNodes();

        for (NodePointer nodeB : nodesB) {
            Mockito.doReturn(true).when(contextB).nextSet();
            Mockito.doReturn(nodeB).when(contextB).getCurrentNodePointer();
        }

        int size = unionContext.size();

        assertEquals(nodesB.size(), size);
    }

    @Test
    public void testSize_whenBothContextsHaveNodes_thenSizeIsEqualToSumOfBothContextsSizes() {
        for (NodePointer nodeA : nodesA) {
            Mockito.doReturn(true).when(contextA).nextSet();
            Mockito.doReturn(nodeA).when(contextA).getCurrentNodePointer();
        }

        for (NodePointer nodeB : nodesB) {
            Mockito.doReturn(true).when(contextB).nextSet();
            Mockito.doReturn(nodeB).when(contextB).getCurrentNodePointer();
        }

        int size = unionContext.size();

        assertEquals(nodesA.size() + nodesB.size(), size);
    }

    @Test
    public void testSize_whenBothContextsHaveNoMoreNodes_thenSizeIsZero() {
        for (NodePointer nodeA : nodesA) {
            Mockito.doReturn(true).when(contextA).nextSet();
            Mockito.doReturn(nodeA).when(contextA).getCurrentNodePointer();
        }

        Mockito.doReturn(false).when(contextA).hasMoreNodes();

        for (NodePointer nodeB : nodesB) {
            Mockito.doReturn(true).when(contextB).nextSet();
            Mockito.doReturn(nodeB).when(contextB).getCurrentNodePointer();
        }

        Mockito.doReturn(false).when(contextB).hasMoreNodes();

        int size = unionContext.size();

        assertEquals(0, size);
    }
}