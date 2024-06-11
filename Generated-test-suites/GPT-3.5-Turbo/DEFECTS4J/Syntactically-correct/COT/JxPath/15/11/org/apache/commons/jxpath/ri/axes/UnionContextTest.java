
import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.jxpath.ri.axes.UnionContext;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.BasicNodeSet;

public class UnionContextTest {

    @Test
    public void testGetDocumentOrder() {
        EvalContext[] contexts = new EvalContext[2];
        UnionContext unionContext = new UnionContext(null, contexts);
        
        // Test when there is only one context
        UnionContext context1 = new UnionContext(null, new EvalContext[]{});
        int result1 = context1.getDocumentOrder();
        assertEquals(1, result1);

        // Test when there are multiple contexts
        UnionContext context2 = new UnionContext(null, new EvalContext[]{new EvalContext(), new EvalContext()});
        int result2 = context2.getDocumentOrder();
        assertEquals(1, result2);
    }
    
    @Test
    public void testSetPosition() {
        EvalContext[] contexts = new EvalContext[2];
        UnionContext unionContext = new UnionContext(null, contexts);
        
        // Test when the position is not prepared
        boolean result1 = unionContext.setPosition(1);
        assertTrue(result1);
        
        // Test when the position is prepared
        unionContext.setPosition(1);
        boolean result2 = unionContext.setPosition(2);
        assertTrue(result2);
    }
}

