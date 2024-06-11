import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UnionContextTest {
    
    private EvalContext parentContext;
    private EvalContext[] contexts;
    
    @Before
    public void setUp() {
        parentContext = new EvalContext();
        contexts = new EvalContext[2];
        contexts[0] = new EvalContext();
        contexts[1] = new EvalContext();
    }
    
    @Test
    public void testConstructor() {
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        
        Assert.assertEquals(parentContext, unionContext.getParentContext());
        Assert.assertEquals(2, unionContext.getContexts().length);
        Assert.assertEquals(BasicNodeSet.class, unionContext.getNodeSet().getClass());
        Assert.assertFalse(unionContext.isPrepared());
    }
    
    @Test
    public void testGetDocumentOrder() {
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        
        Assert.assertEquals(1, unionContext.getDocumentOrder());
        
        EvalContext[] singleContext = new EvalContext[1];
        singleContext[0] = new EvalContext();
        UnionContext singleUnionContext = new UnionContext(parentContext, singleContext);
        
        Assert.assertEquals(0, singleUnionContext.getDocumentOrder());
    }
    
    @Test
    public void testSetPosition() {
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        
        Assert.assertFalse(unionContext.isPrepared());
        Assert.assertTrue(unionContext.setPosition(0));
        Assert.assertTrue(unionContext.isPrepared());
        
        // Add assertion to check if node set is properly populated
    }
    
    // Add more test cases to test all methods in the UnionContext class
    
}