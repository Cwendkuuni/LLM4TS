import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class UnionContextTest {

    private UnionContext unionContext;
    private EvalContext contextA;
    private EvalContext contextB;
    private EvalContext parentContext;

    @Mock
    private BasicNodeSet nodeSet;

    @Before
    public void setup() {
        parentContext = new EvalContext(nodeSet, null);
        contextA = new EvalContext(nodeSet, null);
        contextB = new EvalContext(nodeSet, null);

        unionContext = new UnionContext(parentContext, new EvalContext[]{contextA, contextB});
    }

    @Test
    public void testGetDocumentOrder() {
        // UnionContext should return 1 since it represents a union of other contexts
        int documentOrder = unionContext.getDocumentOrder();
        assert documentOrder == 1;
    }

    @Test
    public void testSetPosition_noContextsPrepared() {
        // Set position should prepare contexts if they haven't been prepared yet
        unionContext.setPosition(0);
        verify(contextA).nextSet();
        verify(contextB).nextSet();
        verify(contextA).nextNode();
        verify(contextB).nextNode();
    }

    @Test
    public void testSetPosition_positionWithinFirstContext() {
        // Set position within the first context
        contextA.nextSet();
        contextA.nextNode();

        // Set position within the UnionContext using the same position
        unionContext.setPosition(0);

        // The current node pointer should be the same as the one from the first context
        NodePointer currentNodePointer = unionContext.getCurrentNodePointer();
        NodePointer firstContextCurrentNodePointer = contextA.getCurrentNodePointer();
        assert currentNodePointer.equals(firstContextCurrentNodePointer);
    }

    @Test
    public void testSetPosition_positionWithinSecondContext() {
        // Set position within the first context
        contextA.nextSet();
        contextA.nextNode();
        contextA.nextNode();

        // Set position within the second context
        contextB.nextSet();
        contextB.nextNode();

        // Set position within the UnionContext using the same position
        unionContext.setPosition(1);

        // The current node pointer should be the same as the one from the second context
        NodePointer currentNodePointer = unionContext.getCurrentNodePointer();
        NodePointer secondContextCurrentNodePointer = contextB.getCurrentNodePointer();
        assert currentNodePointer.equals(secondContextCurrentNodePointer);
    }

    @Test
    public void testSetPosition_positionOutsideBothContexts() {
        // Set position within the first context
        contextA.nextSet();
        contextA.nextNode();
        contextA.nextNode();

        // Set position within the second context
        contextB.nextSet();
        contextB.nextNode();
        contextB.nextNode();

        // Set position outside both contexts
        unionContext.setPosition(contextA.getPosition() + contextA.size() + contextB.getPosition() + contextB.size());

        // The current node pointer should be null since we're outside both contexts
        NodePointer currentNodePointer = unionContext.getCurrentNodePointer();
        assert currentNodePointer == null;
    }
}