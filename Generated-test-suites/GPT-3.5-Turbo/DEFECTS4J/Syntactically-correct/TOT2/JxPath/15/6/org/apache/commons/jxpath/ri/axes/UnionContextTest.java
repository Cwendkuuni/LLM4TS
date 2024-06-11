import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.jxpath.ri.axes.UnionContext;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;

public class UnionContextTest {

    @Test
    public void testGetDocumentOrder() {
        EvalContext context1 = new EvalContext();
        EvalContext context2 = new EvalContext();
        UnionContext unionContext = new UnionContext(null, new EvalContext[]{context1, context2});
        assertEquals(1, unionContext.getDocumentOrder());
    }

    @Test
    public void testSetPosition() {
        EvalContext context1 = new EvalContext();
        EvalContext context2 = new EvalContext();
        NodePointer pointer1 = new NodePointer();
        NodePointer pointer2 = new NodePointer();
        context1.setCurrentNodePointer(pointer1);
        context2.setCurrentNodePointer(pointer2);
        
        UnionContext unionContext = new UnionContext(null, new EvalContext[]{context1, context2});
        assertTrue(unionContext.setPosition(0));
        assertEquals(pointer1, unionContext.getCurrentNodePointer());
        assertTrue(unionContext.setPosition(1));
        assertEquals(pointer2, unionContext.getCurrentNodePointer());
    }

}