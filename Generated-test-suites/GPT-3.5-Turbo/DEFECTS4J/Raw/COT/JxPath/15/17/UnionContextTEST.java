
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.ri.axes.UnionContext;
import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;

import static org.junit.Assert.*;

public class UnionContextTest {

    private UnionContext unionContext;

    @Before
    public void setUp() {
        EvalContext[] contexts = new EvalContext[2];
        contexts[0] = new EvalContext();
        contexts[1] = new EvalContext();
        unionContext = new UnionContext(new EvalContext(), contexts);
    }

    @Test
    public void testGetDocumentOrder() {
        assertEquals(1, unionContext.getDocumentOrder());
    }

    @Test
    public void testSetPosition() {
        assertTrue(unionContext.setPosition(0));
        assertFalse(unionContext.setPosition(0));
    }
}
