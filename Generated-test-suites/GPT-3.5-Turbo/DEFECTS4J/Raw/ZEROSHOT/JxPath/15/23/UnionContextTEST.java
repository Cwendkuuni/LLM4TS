import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;

public class UnionContextTest {
    private UnionContext unionContext;

    @Before
    public void setUp() {
        EvalContext parentContext = new EvalContext();
        EvalContext[] contexts = new EvalContext[]{new EvalContext(), new EvalContext()};
        unionContext = new UnionContext(parentContext, contexts);
    }

    @Test
    public void testGetDocumentOrder() {
        int expected = 1;
        int actual = unionContext.getDocumentOrder();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetPosition() {
        int position = 1;
        boolean expected = true;
        boolean actual = unionContext.setPosition(position);
        assertEquals(expected, actual);
    }
}