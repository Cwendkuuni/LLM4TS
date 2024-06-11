import org.junit.Assert;
import org.junit.Test;

public class UnionContextTest {

    @Test
    public void testGetDocumentOrder() {
        EvalContext parentContext = new EvalContext();
        EvalContext[] contexts = new EvalContext[2];
        contexts[0] = new EvalContext();
        contexts[1] = new EvalContext();
        UnionContext unionContext = new UnionContext(parentContext, contexts);

        int result = unionContext.getDocumentOrder();

        Assert.assertEquals(1, result);
    }

    @Test
    public void testSetPosition() {
        EvalContext parentContext = new EvalContext();
        EvalContext[] contexts = new EvalContext[2];
        contexts[0] = new EvalContext();
        contexts[1] = new EvalContext();
        UnionContext unionContext = new UnionContext(parentContext, contexts);

        boolean result = unionContext.setPosition(1);

        Assert.assertTrue(result);
    }
}