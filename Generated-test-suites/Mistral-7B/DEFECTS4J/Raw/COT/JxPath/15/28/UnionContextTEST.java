import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.ri.NodeSetContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(BlockJUnit4ClassRunner.class)
public class UnionContextTest {

    private EvalContext contextA;
    private EvalContext contextB;
    private EvalContext unionContext;
    private NodeSetContext nodeSetContext;

    @Before
    public void setUp() {
        contextA = new EvalContext(new BasicNodeSet()) {
            @Override
            public boolean nextSet() {
                return true;
            }

            @Override
            public boolean nextNode() {
                return true;
            }
        };

        contextB = new EvalContext(new BasicNodeSet()) {
            @Override
            public boolean nextSet() {
                return true;
            }

            @Override
            public boolean nextNode() {
                return true;
            }
        };

        nodeSetContext = new NodeSetContext(null, new BasicNodeSet()) {
            @Override
            public int getDocumentOrder() {
                return 0;
            }
        };

        unionContext = new UnionContext(nodeSetContext, new EvalContext[] {contextA, contextB});
    }

    @Test
    public void testGetDocumentOrder() {
        assertTrue(unionContext.getDocumentOrder() > 0);
    }

    @Test
    public void testSetPosition_whenPositionIsValid_shouldReturnTrue() {
        for (int i = 0; i < 10; i++) {
            assertTrue(unionContext.setPosition(i));
        }
    }

    @Test
    public void testSetPosition_whenPositionIsInvalid_shouldReturnFalse() {
        assertFalse(unionContext.setPosition(-1));
        assertFalse(unionContext.setPosition(contextsLength()));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetPosition_whenContextsAreEmpty_shouldThrowException() {
        UnionContext emptyUnionContext = new UnionContext(null, new EvalContext[0]);
        emptyUnionContext.setPosition(0);
    }

    @Test
    public void testSetPosition_whenNextSetContextIsEmpty_shouldReturnFalse() {
        EvalContext emptyContext = new EvalContext(new BasicNodeSet()) {
            @Override
            public boolean nextSet() {
                return false;
            }
        };

        UnionContext unionContextWithEmptyContext = new UnionContext(nodeSetContext, new EvalContext[] {contextA, emptyContext, contextB});
        assertFalse(unionContextWithEmptyContext.setPosition(0));
    }

    @Test
    public void testSetPosition_whenCurrentNodeInContextAIsEqualToCurrentNodeInContextB_shouldReturnTrue() {
        NodePointer nodeA = new NodePointer("nodeA");
        NodePointer nodeB = new NodePointer("nodeA");
        contextA.setCurrentNodePointer(nodeA);
        contextB.setCurrentNodePointer(nodeB);

        UnionContext unionContext = new UnionContext(nodeSetContext, new EvalContext[] {contextA, contextB});
        assertTrue(unionContext.setPosition(0));
    }

    @Test
    public void testSetPosition_whenCurrentNodeInContextAIsNotEqualToCurrentNodeInContextB_shouldAdvanceBothContexts() {
        NodePointer nodeA = new NodePointer("nodeA");
        NodePointer nodeB = new NodePointer("nodeB");
        contextA.setCurrentNodePointer(nodeA);
        contextB.setCurrentNodePointer(nodeB);

        UnionContext unionContext = new UnionContext(nodeSetContext, new EvalContext[] {contextA, contextB});
        unionContext.next(); // Advance unionContext
        unionContext.next(); // Advance unionContext

        assertTrue(contextA.nextNode()); // Next node in contextA
        NodePointer nextNodeInContextB = contextB.getCurrentNodePointer();
        assertTrue(nextNodeInContextB.isSameNode(nodeA)); // Next node in contextB should be the same as nodeA in contextA
    }

    private int contextsLength() {
        return unionContext.contexts.length;
    }
}