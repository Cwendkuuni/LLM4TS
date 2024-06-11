import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UnionContextTest {

    private UnionContext unionContext;
    private List<EvalContext> contexts = new ArrayList<>();
    private EvalContext context1;
    private EvalContext context2;

    @Mock
    private EvalContext parentContext;

    @Before
    public void setup() {
        context1 = mock(EvalContext.class);
        context2 = mock(EvalContext.class);
        contexts.add(context1);
        contexts.add(context2);
        unionContext = new UnionContext(parentContext, contexts.toArray(new EvalContext[0]));
    }

    @Test
    public void testUnionContextGetDocumentOrder() {
        assertTrue(unionContext.getDocumentOrder() > 0);
    }

    @Test
    public void testSetPosition_notPrepared() {
        when(context1.nextSet()).thenReturn(true);
        when(context1.nextNode()).thenReturn(true);
        when(context1.getCurrentNodePointer()).thenReturn(new NodePointer(1));
        when(context2.nextSet()).thenReturn(true);
        when(context2.nextNode()).thenReturn(true);
        when(context2.getCurrentNodePointer()).thenReturn(new NodePointer(2));

        unionContext.getNodeSet().clear();

        assertTrue(unionContext.setPosition(0));
        List<NodePointer> nodePointers = (List<NodePointer>) unionContext.getNodeSet();
        assertTrue(nodePointers.contains(context1.getCurrentNodePointer()));
        assertTrue(nodePointers.contains(context2.getCurrentNodePointer()));
    }

    @Test
    public void testSetPosition_alreadyPrepared() {
        when(context1.nextSet()).thenReturn(false);
        when(context1.nextNode()).thenReturn(false);
        when(context2.nextSet()).thenReturn(false);
        when(context2.nextNode()).thenReturn(false);

        unionContext.getNodeSet().clear();

        assertTrue(unionContext.setPosition(0));
        List<NodePointer> nodePointers = (List<NodePointer>) unionContext.getNodeSet();
        assertFalse(nodePointers.isEmpty());
        assertTrue(nodePointers.containsAll(contexts.stream().map(EvalContext::getCurrentNodePointer).toList().toArray(new NodePointer[0])));
    }
}