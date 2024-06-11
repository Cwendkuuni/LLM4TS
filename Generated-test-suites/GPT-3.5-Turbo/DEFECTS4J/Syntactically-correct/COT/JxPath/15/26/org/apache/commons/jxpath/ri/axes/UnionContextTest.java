import static org.junit.Assert.*;
import org.junit.Test;

public class UnionContextTest {

    @Test
    public void testGetDocumentOrder() {
        EvalContext parentContext = new EvalContext();
        EvalContext context1 = new EvalContext();
        context1.setPosition(1);
        EvalContext context2 = new EvalContext();
        context2.setPosition(2);
        EvalContext contexts[] = {context1, context2};
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        int expectedResult = 1;
        int actualResult = unionContext.getDocumentOrder();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testSetPosition() {
        EvalContext parentContext = new EvalContext();
        EvalContext context1 = new EvalContext();
        context1.setPosition(1);
        EvalContext context2 = new EvalContext();
        context2.setPosition(2);
        EvalContext contexts[] = {context1, context2};
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        boolean expectedResult = true;
        boolean actualResult = unionContext.setPosition(3);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testSetPosition_noNodes() {
        EvalContext parentContext = new EvalContext();
        EvalContext context1 = new EvalContext();
        EvalContext context2 = new EvalContext();
        EvalContext contexts[] = {context1, context2};
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        boolean expectedResult = true;
        boolean actualResult = unionContext.setPosition(1);
        assertEquals(expectedResult, actualResult);
    }
}