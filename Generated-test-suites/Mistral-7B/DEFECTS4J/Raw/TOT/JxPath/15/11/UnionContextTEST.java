import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.axes.UnionContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UnionContextTest {

    @Test
    public void testUnionContext() {
        // Given: Two EvalContexts with nodes
        EvalContext contextA = new EvalContext(new BasicNodeSet()) {{
            add(new NodePointer("document1", 1));
            add(new NodePointer("document1", 2));
        }};

        EvalContext contextB = new EvalContext(new BasicNodeSet()) {{
            add(new NodePointer("document1", 3));
            add(new NodePointer("document1", 4));
            add(new NodePointer("document1", 1));
        }};

        // When: A UnionContext is created with the two contexts
        UnionContext unionContext = new UnionContext(null, new EvalContext[]{contextA, contextB});

        // Then: The UnionContext should contain all nodes from both contexts
        List<NodePointer> expectedNodes = new ArrayList<>(Arrays.asList(
                new NodePointer("document1", 1),
                new NodePointer("document1", 2),
                new NodePointer("document1", 3),
                new NodePointer("document1", 4)
        ));
        List<NodePointer> actualNodes = new ArrayList<>();
        while (unionContext.nextSet()) {
            while (unionContext.nextNode()) {
                actualNodes.add(unionContext.getCurrentNodePointer());
            }
        }
        Assert.assertEquals(expectedNodes, actualNodes);

        // When: The UnionContext is iterated multiple times
        // Then: The same nodes should be returned in the same order
        List<NodePointer> firstIteration = new ArrayList<>();
        while (unionContext.nextSet()) {
            while (unionContext.nextNode()) {
                firstIteration.add(unionContext.getCurrentNodePointer());
            }
        }
        ArrayList<NodePointer> secondIteration = new ArrayList<>(unionContext.getNodeSet());
        Assert.assertEquals(firstIteration, secondIteration);

        // When: An attempt is made to set the position to a negative number
        // Then: An IndexOutOfBoundsException should be thrown
        try {
            unionContext.setPosition(-1);
            Assert.fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }

        // When: An attempt is made to set the position to a number greater than the number of nodes in the UnionContext
        // Then: An IndexOutOfBoundsException should be thrown
        try {
            unionContext.setPosition(Integer.MAX_VALUE);
            Assert.fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }
}