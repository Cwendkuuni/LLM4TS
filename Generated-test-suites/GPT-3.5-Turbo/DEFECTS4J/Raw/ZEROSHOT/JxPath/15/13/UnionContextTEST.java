import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;

public class UnionContextTest {
    private UnionContext unionContext;
    private EvalContext evalContext1;
    private EvalContext evalContext2;

    @Before
    public void setUp() {
        evalContext1 = new EvalContext();
        evalContext2 = new EvalContext();
        unionContext = new UnionContext(null, new EvalContext[] {evalContext1, evalContext2});
    }

    @Test
    public void testGetDocumentOrder() {
        int expected = 1;
        int actual = unionContext.getDocumentOrder();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetPosition() {
        int position = 0;
        boolean expected = false;
        boolean actual = unionContext.setPosition(position);
        assertEquals(expected, actual);

        position = 1;
        expected = true;
        actual = unionContext.setPosition(position);
        assertEquals(expected, actual);
    }
}