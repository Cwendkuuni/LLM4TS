import static org.junit.Assert.*;
import org.junit.Test;

public class UnionContextTest {

    @Test
    public void testGetDocumentOrder() {
        // Expert 1 propose test case
        EvalContext context1 = new EvalContext();
        context1.setDocumentOrder(1);
        UnionContext unionContext = new UnionContext(null, new EvalContext[] {context1});
        int result1 = unionContext.getDocumentOrder();
        assertEquals(1, result1);

        // Expert 2 propose test case 
        EvalContext context2 = new EvalContext();
        UnionContext unionContext2 = new UnionContext(null, new EvalContext[] {context1, context2});
        int result2 = unionContext2.getDocumentOrder();
        assertEquals(1, result2);

        // Expert 3 propose test case
        EvalContext context3 = new EvalContext();
        context3.setDocumentOrder(2);
        UnionContext unionContext3 = new UnionContext(null, new EvalContext[] {context1, context2, context3});
        int result3 = unionContext3.getDocumentOrder();
        assertEquals(1, result3);
    }
    
    @Test
    public void testSetPosition() {
        // Expert 1 propose test case
        EvalContext context1 = new EvalContext();
        context1.nextSet();
        context1.nextNode();
        NodePointer nodePointer1 = new NodePointer();
        context1.setCurrentNodePointer(nodePointer1);
        UnionContext unionContext = new UnionContext(null, new EvalContext[] {context1});
        boolean result1 = unionContext.setPosition(0);
        assertTrue(result1);

        // Expert 2 propose test case 
        EvalContext context2 = new EvalContext();
        context2.nextSet();
        context2.nextNode();
        NodePointer nodePointer2 = new NodePointer();
        context2.setCurrentNodePointer(nodePointer2);
        UnionContext unionContext2 = new UnionContext(null, new EvalContext[] {context2});
        boolean result2 = unionContext2.setPosition(1);
        assertTrue(result2);

        // Expert 3 propose test case
        EvalContext context3 = new EvalContext();
        context3.nextSet();
        UnionContext unionContext3 = new UnionContext(null, new EvalContext[] {context3});
        boolean result3 = unionContext3.setPosition(-1);
        assertFalse(result3);
    }
}