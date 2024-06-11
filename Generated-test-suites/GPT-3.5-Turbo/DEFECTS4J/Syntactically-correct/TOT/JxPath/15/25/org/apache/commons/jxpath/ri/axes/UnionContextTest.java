import org.junit.Assert;
import org.junit.Test;

public class UnionContextTest {
    @Test
    public void testGetDocumentOrder() {
        // Test with single context
        EvalContext ctx1 = new EvalContext();
        UnionContext unionContext1 = new UnionContext(null, new EvalContext[] {ctx1});
        int documentOrder1 = unionContext1.getDocumentOrder();
        Assert.assertEquals(0, documentOrder1);
        
        // Test with multiple contexts
        EvalContext ctx2 = new EvalContext();
        EvalContext ctx3 = new EvalContext();
        UnionContext unionContext2 = new UnionContext(null, new EvalContext[] {ctx2, ctx3});
        int documentOrder2 = unionContext2.getDocumentOrder();
        Assert.assertEquals(1, documentOrder2);
    }
    
    @Test
    public void testSetPosition() {
        // Test with single context
        EvalContext ctx1 = new EvalContext();
        UnionContext unionContext1 = new UnionContext(null, new EvalContext[] {ctx1});
        boolean setPosition1 = unionContext1.setPosition(1);
        Assert.assertEquals(true, setPosition1);
        
        // Test with multiple contexts
        EvalContext ctx2 = new EvalContext();
        EvalContext ctx3 = new EvalContext();
        UnionContext unionContext2 = new UnionContext(null, new EvalContext[] {ctx2, ctx3});
        boolean setPosition2 = unionContext2.setPosition(1);
        Assert.assertEquals(true, setPosition2);
    }
}