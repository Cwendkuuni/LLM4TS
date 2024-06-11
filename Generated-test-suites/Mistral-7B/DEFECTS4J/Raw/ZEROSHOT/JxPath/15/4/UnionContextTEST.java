import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class UnionContextTest {

    private EvalContext contextA;
    private EvalContext contextB;
    private EvalContext unionContext;

    @Parameterized.Parameters(name = "Context A: {0}, Context B: {1}")
    public static List<Object[]> data() {
        return Arrays.asList(new Object[][]{
                new Object[]{new BasicNodeSetContext(new BasicNodeSet()), new BasicNodeSetContext(new BasicNodeSet())},
                new Object[]{new BasicNodeSetContext(new BasicNodeSet(), new Object[]{1, 2, 3}), new BasicNodeSetContext(new BasicNodeSet(), new Object[]{4, 5})},
                new Object[]{new BasicNodeSetContext(new BasicNodeSet(), new Object[]{1, 2, 3}), new BasicNodeSetContext(new BasicNodeSet(), new Object[]{4, 5, 6})}
        });
    }

    @Before
    public void setUp() {
        contextA = new BasicNodeSetContext(new BasicNodeSet());
        contextB = new BasicNodeSetContext(new BasicNodeSet());
        unionContext = new UnionContext(null, new EvalContext[]{contextA, contextB});
    }

    @Test
    public void testGetDocumentOrder() {
        assertTrue(unionContext.getDocumentOrder() > 0);
    }

    @Test
    public void testSetPosition() {
        int totalNodes = contextA.getNodeSet().size() + contextB.getNodeSet().size();

        for (int i = 0; i < totalNodes; i++) {
            assertTrue(unionContext.setPosition(i));
        }

        assertFalse(unionContext.setPosition(-1));
        assertFalse(unionContext.setPosition(totalNodes));
    }

    @Test
    public void testSetPositionWithDuplicates() {
        contextA.getNodeSet().add(new NodePointer(0, 0));
        contextB.getNodeSet().add(new NodePointer(0, 0));

        int totalNodes = contextA.getNodeSet().size() + contextB.getNodeSet().size();

        for (int i = 0; i < totalNodes; i++) {
            assertTrue(unionContext.setPosition(i));
        }

        for (int i = 0; i < totalNodes; i++) {
            assertTrue(unionContext.setPosition(i));
        }

        assertFalse(unionContext.setPosition(-1));
        assertFalse(unionContext.setPosition(totalNodes * 2));
    }
}