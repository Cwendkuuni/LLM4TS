import org.junit.Test;
import static org.junit.Assert.*;

public class UnionContextTest {

    @Test
    public void testGetPosition() {
        // Test with multiple contexts
        EvalContext context1 = new EvalContext();
        EvalContext context2 = new EvalContext();
        EvalContext[] contexts = {context1, context2};
        UnionContext unionContext = new UnionContext(null, contexts);

        int position = unionContext.getPosition();
        assertEquals(1, position);

        // Test with single context
        EvalContext singleContext = new EvalContext();
        EvalContext[] singleContexts = {singleContext};
        UnionContext singleUnionContext = new UnionContext(null, singleContexts);

        int singlePosition = singleUnionContext.getPosition();
        assertEquals(0, singlePosition);
    }

    @Test
    public void testSetPosition() {
        // Test with multiple contexts
        EvalContext context1 = new EvalContext();
        EvalContext context2 = new EvalContext();
        EvalContext[] contexts = {context1, context2};
        UnionContext unionContext = new UnionContext(null, contexts);

        boolean result = unionContext.setPosition(1);
        assertTrue(result);

        boolean invalidPositionResult = unionContext.setPosition(2);
        assertFalse(invalidPositionResult);

        // Test with single context
        EvalContext singleContext = new EvalContext();
        EvalContext[] singleContexts = {singleContext};
        UnionContext singleUnionContext = new UnionContext(null, singleContexts);

        boolean singleResult = singleUnionContext.setPosition(1);
        assertTrue(singleResult);

        boolean invalidSinglePositionResult = singleUnionContext.setPosition(2);
        assertFalse(invalidSinglePositionResult);
    }

    @Test
    public void testSetPositionWithPreparation() {
        // Test with multiple contexts
        EvalContext context1 = new EvalContext();
        EvalContext context2 = new EvalContext();
        EvalContext[] contexts = {context1, context2};
        UnionContext unionContext = new UnionContext(null, contexts);

        unionContext.setPosition(1);

        boolean result = unionContext.setPosition(1);
        assertTrue(result);

        boolean invalidPositionResult = unionContext.setPosition(2);
        assertFalse(invalidPositionResult);

        // Test with single context
        EvalContext singleContext = new EvalContext();
        EvalContext[] singleContexts = {singleContext};
        UnionContext singleUnionContext = new UnionContext(null, singleContexts);

        singleUnionContext.setPosition(1);

        boolean singleResult = singleUnionContext.setPosition(1);
        assertTrue(singleResult);

        boolean invalidSinglePositionResult = singleUnionContext.setPosition(2);
        assertFalse(invalidSinglePositionResult);
    }

}