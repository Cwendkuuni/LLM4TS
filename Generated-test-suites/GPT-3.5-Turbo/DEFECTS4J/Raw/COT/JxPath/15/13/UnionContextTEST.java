
import org.junit.Assert;
import org.junit.Test;

public class UnionContextTest {

    @Test
    public void testGetDocumentOrder() {
        EvalContext ctx1 = new EvalContext();
        EvalContext ctx2 = new EvalContext();
        EvalContext[] contexts = { ctx1, ctx2 };
        UnionContext unionContext = new UnionContext(null, contexts);
        
        int expectedOrder = 1;
        int actualOrder = unionContext.getDocumentOrder();
        
        Assert.assertEquals(expectedOrder, actualOrder);
    }
    
    @Test
    public void testSetPosition() {
        EvalContext ctx1 = new EvalContext();
        EvalContext ctx2 = new EvalContext();
        EvalContext[] contexts = { ctx1, ctx2 };
        UnionContext unionContext = new UnionContext(null, contexts);
        
        int position = 1;
        boolean result = unionContext.setPosition(position);
        
        Assert.assertTrue(result);
    }
}
