import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class UnionContextTest {

    @Test
    public void testGetDocumentOrder() {
        // Test case for getDocumentOrder()
        // Test the document order when there are multiple contexts
        // and when there is only one context

        EvalContext[] contexts1 = { context1, context2, context3 };
        UnionContext unionContext1 = new UnionContext(parentContext, contexts1);
        assertEquals(1, unionContext1.getDocumentOrder());

        EvalContext[] contexts2 = { context1 };
        UnionContext unionContext2 = new UnionContext(parentContext, contexts2);
        assertEquals(2, unionContext2.getDocumentOrder());
    }

    @Test
    public void testSetPosition() {
        // Test case for setPosition()
        // Test setting position with and without preparation

        EvalContext[] contexts = { context1, context2 };
        UnionContext unionContext = new UnionContext(parentContext, contexts);

        boolean setPosition1 = unionContext.setPosition(3);
        assertEquals(true, setPosition1);

        boolean setPosition2 = unionContext.setPosition(2);
        assertEquals(false, setPosition2);
    }
}