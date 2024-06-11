import static org.junit.Assert.*;
import org.junit.Test;

public class UnionContextTest {

    @Test
    public void testGetDocumentOrder() {
        // Creating an array of two EvalContext objects
        EvalContext[] contexts = new EvalContext[2];
        // Creating two EvalContext objects and adding them to the array
        EvalContext context1 = new EvalContext();
        EvalContext context2 = new EvalContext();
        contexts[0] = context1;
        contexts[1] = context2;
        
        // Creating an instance of UnionContext with the array of contexts
        UnionContext unionContext = new UnionContext(contexts);
        
        // Testing when number of contexts is greater than 1
        assertEquals(1, unionContext.getDocumentOrder());
        
        // Testing when number of contexts is less than or equal to 1
        EvalContext[] contexts2 = new EvalContext[1];
        EvalContext context3 = new EvalContext();
        contexts2[0] = context3;
        UnionContext unionContext2 = new UnionContext(contexts2);
        assertEquals(0, unionContext2.getDocumentOrder());
    }

    @Test
    public void testSetPosition() {
        // Creating an array of two EvalContext objects
        EvalContext[] contexts = new EvalContext[2];
        // Creating two EvalContext objects and adding them to the array
        EvalContext context1 = new EvalContext();
        EvalContext context2 = new EvalContext();
        contexts[0] = context1;
        contexts[1] = context2;
        
        // Creating an instance of UnionContext with the array of contexts
        UnionContext unionContext = new UnionContext(contexts);
        
        // Testing when the context is not prepared
        assertTrue(unionContext.setPosition(1));
        
        // Testing when the context is already prepared
        assertTrue(unionContext.setPosition(2));
    }
}