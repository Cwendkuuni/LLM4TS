import org.junit.Test;
import static org.junit.Assert.*;

public class UnionContextTest {

    @Test
    public void testGetDocumentOrder() {
        EvalContext[] contexts = { new EvalContext(), new EvalContext() };
        UnionContext unionContext = new UnionContext(new EvalContext(), contexts);
        int documentOrder = unionContext.getDocumentOrder();
        assertEquals(1, documentOrder);
    }

    @Test
    public void testSetPosition() {
        EvalContext[] contexts = { new EvalContext() };
        UnionContext unionContext = new UnionContext(new EvalContext(), contexts);
        boolean setPosition = unionContext.setPosition(1);
        assertTrue(setPosition);
    }

    @Test
    public void testSetPositionWithPreparedFalse() {
        EvalContext[] contexts = { new EvalContext() };
        UnionContext unionContext = new UnionContext(new EvalContext(), contexts);
        boolean setPosition = unionContext.setPosition(1);
        assertTrue(setPosition);
    }

    @Test
    public void testSetPositionWithPreparedTrue() {
        EvalContext[] contexts = { new EvalContext() };
        UnionContext unionContext = new UnionContext(new EvalContext(), contexts);
        unionContext.setPosition(1);
        boolean setPosition = unionContext.setPosition(2);
        assertFalse(setPosition);
    }
}



// additional test cases for edge cases and exception handling scenarios