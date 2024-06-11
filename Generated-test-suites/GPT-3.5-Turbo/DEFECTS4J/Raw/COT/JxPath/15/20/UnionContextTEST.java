
import org.apache.commons.jxpath.ri.axes.*;
import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnionContextTest {

    @Test
    public void testGetPosition() {
        EvalContext parentContext = new EvalContext();
        EvalContext[] contexts = new EvalContext[2];
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        
        int position = unionContext.getPosition();
        
        assertEquals(0, position);
    }

    @Test
    public void testGetDocumentOrder_withMultipleContexts() {
        EvalContext parentContext = new EvalContext();
        EvalContext[] contexts = new EvalContext[2];
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        
        int documentOrder = unionContext.getDocumentOrder();
        
        assertEquals(1, documentOrder);
    }

    @Test
    public void testGetDocumentOrder_withSingleContext() {
        EvalContext parentContext = new EvalContext();
        EvalContext[] contexts = new EvalContext[1];
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        
        int documentOrder = unionContext.getDocumentOrder();
        
        assertEquals(0, documentOrder);
    }

    @Test
    public void testSetPosition() {
        EvalContext parentContext = new EvalContext();
        EvalContext[] contexts = new EvalContext[2];
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        
        boolean isPositionSet = unionContext.setPosition(1);
        
        assertTrue(isPositionSet);
    }
}
