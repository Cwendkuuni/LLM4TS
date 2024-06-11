import org.apache.commons.jxpath.EvaluationContext;
import org.apache.commons.jxpath.NodeSet;
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

@RunWith(MockitoJUnitRunner.class)
public class UnionContextTest {

    @Mock
    private EvaluationContext parentContext;

    private EvaluationContext context1;
    private EvaluationContext context2;
    private EvaluationContext unionContext;

    private List<NodePointer> nodeSet;

    @Before
    public void setUp() {
        context1 = new EvaluationContext(parentContext, new BasicNodeSet());
        context2 = new EvaluationContext(parentContext, new BasicNodeSet());

        unionContext = new UnionContext(parentContext, new EvaluationContext[]{context1, context2});

        nodeSet = new ArrayList<>();
        nodeSet.add(new NodePointer(1L, 1));
        nodeSet.add(new NodePointer(2L, 2));
        nodeSet.add(new NodePointer(3L, 3));

        ((BasicNodeSet) unionContext.getNodeSet()).setNodeSet(nodeSet);
    }

    @Test
    public void testGetDocumentOrder() {
        assertTrue(unionContext.getDocumentOrder() > 0);
    }

    @Test
    public void testSetPosition_whenContextsContainNodes_thenPositionIsSetCorrectly() {
        // Given
        context1.setPosition(0);
        context2.setPosition(1);

        // When
        unionContext.setPosition(1);

        // Then
        assertTrue(unionContext.getCurrentNodePointer().equals(context2.getCurrentNodePointer()));
    }

    @Test
    public void testSetPosition_whenContextsDontContainNodesAtPosition_thenPositionIsNotSet() {
        // Given
        context1.setPosition(0);
        context2.setPosition(1);

        // When
        unionContext.setPosition(2);

        // Then
        assertFalse(unionContext.hasCurrentNode());
    }
}