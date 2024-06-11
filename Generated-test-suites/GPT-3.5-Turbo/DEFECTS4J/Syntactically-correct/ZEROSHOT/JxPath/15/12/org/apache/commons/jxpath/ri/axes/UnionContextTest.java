import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.UnionContext;
import org.apache.commons.jxpath.ri.model.NodePointer;

public class UnionContextTest {
    
    private UnionContext unionContext;
    
    @Before
    public void setUp() {
        EvalContext parentContext = new EvalContext();
        EvalContext[] contexts = {new EvalContext(), new EvalContext()};
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
        boolean expected = true;
        boolean actual = unionContext.setPosition(1);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSetPositionNotPrepared() {
        unionContext.setPosition(1);
        BasicNodeSet nodeSet = (BasicNodeSet) unionContext.getNodeSet();
        NodePointer pointer = new NodePointer();
        nodeSet.add(pointer);
        
        boolean expected = true;
        boolean actual = unionContext.setPosition(1);
        assertEquals(expected, actual);
    }
}