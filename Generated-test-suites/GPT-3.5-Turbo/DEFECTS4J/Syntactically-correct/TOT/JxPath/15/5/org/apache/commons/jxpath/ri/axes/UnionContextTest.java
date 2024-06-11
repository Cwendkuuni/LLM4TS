import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

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
        parentContext = mock(EvalContext.class);
        contexts = new EvalContext[3];
        contexts[0] = mock(EvalContext.class);
        contexts[1] = mock(EvalContext.class);
        contexts[2] = mock(EvalContext.class);

        unionContext = new UnionContext(parentContext, contexts);
    }

    @Test
    public void testGetDocumentOrder() {
        assertEquals(1, unionContext.getDocumentOrder());
    }

    @Test
    public void testGetPositionWithPreparedFalse() {
        unionContext.setPosition(1);

        assertFalse(unionContext.setPosition(1));
    }

    @Test
    public void testGetPositionWhenNoSetPositions() {
        prepareContexts(0, 0, 0);
        unionContext.setPosition(1);

        assertTrue(unionContext.setPosition(1));
    }

    @Test
    public void testGetPositionWhenOneSetPosition() {
        prepareContexts(1, 0, 0);
        unionContext.setPosition(1);

        assertTrue(unionContext.setPosition(1));
    }

    @Test
    public void testGetPositionWhenMultipleSetPositions() {
        prepareContexts(1, 1, 1);
        unionContext.setPosition(1);

        assertTrue(unionContext.setPosition(1));
    }

    @Test
    public void testGetPositionWhenSomeSetPositions() {
        prepareContexts(0, 1, 0);
        unionContext.setPosition(1);

        assertTrue(unionContext.setPosition(1));
    }

    @Test
    public void testGetPositionWhenDuplicateSetPositions() {
        prepareContexts(1, 1, 0);
        unionContext.setPosition(1);
        unionContext.setPosition(2);

        assertTrue(unionContext.setPosition(2));
    }

    @Test
    public void testGetPositionWhenNoNodes() {
        prepareContexts(0, 0, 0);
        unionContext.setPosition(1);

        assertFalse(unionContext.setPosition(1));
    }

    private void prepareContexts(int... setPositions) {
        List<NodePointer> pointers = new ArrayList<>();
        for (int i = 0; i < contexts.length; i++) {
            EvalContext context = contexts[i];
            when(context.nextSet()).thenReturn(true);
            for (int j = 0; j < setPositions[i]; j++) {
                when(context.nextNode()).thenReturn(true);
                NodePointer pointer = mock(NodePointer.class);
                if (!pointers.contains(pointer)) {
                    pointers.add(pointer);
                }
                when(context.getCurrentNodePointer()).thenReturn(pointer);
            }
            when(context.nextNode()).thenReturn(false);
        }
        ((BasicNodeSet) unionContext.getNodeSet()).addAll(pointers);
    }

}