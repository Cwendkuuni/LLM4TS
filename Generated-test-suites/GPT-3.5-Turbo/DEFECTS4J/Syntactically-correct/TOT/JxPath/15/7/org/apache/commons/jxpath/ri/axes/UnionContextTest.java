import org.junit.Test;
import static org.junit.Assert.*;

public class UnionContextTest {

    @Test
    public void testGetDocumentOrder() {
        // Typical use case
        EvalContext[] contexts = {context1, context2};
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        int expectedOrder = 1;
        int actualOrder = unionContext.getDocumentOrder();
        assertEquals(expectedOrder, actualOrder);

        // Edge case: Only one context
        EvalContext[] singleContext = {context1};
        UnionContext singleContextUnion = new UnionContext(parentContext, singleContext);
        int expectedSingleOrder = 0;
        int actualSingleOrder = singleContextUnion.getDocumentOrder();
        assertEquals(expectedSingleOrder, actualSingleOrder);
    }

    @Test
    public void testSetPosition() {
        // Error scenarios: Empty contexts
        EvalContext[] emptyContexts = {};
        UnionContext emptyContextUnion = new UnionContext(parentContext, emptyContexts);
        boolean expectedEmptyPosition = false;
        boolean actualEmptyPosition = emptyContextUnion.setPosition(0);
        assertEquals(expectedEmptyPosition, actualEmptyPosition);

        // Typical use case: Multiple contexts
        EvalContext[] multipleContexts = {context1, context2};
        UnionContext multipleContextUnion = new UnionContext(parentContext, multipleContexts);
        boolean expectedMultiplePosition = true;
        boolean actualMultiplePosition = multipleContextUnion.setPosition(0);
        assertEquals(expectedMultiplePosition, actualMultiplePosition);

        // Error scenario: Negative position
        boolean expectedNegativePosition = false;
        boolean actualNegativePosition = multipleContextUnion.setPosition(-1);
        assertEquals(expectedNegativePosition, actualNegativePosition);

        // Error scenario: Position higher than node count
        boolean expectedHigherPosition = false;
        boolean actualHigherPosition = multipleContextUnion.setPosition(10);
        assertEquals(expectedHigherPosition, actualHigherPosition);
    }

}