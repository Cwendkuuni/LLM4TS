import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.NodeSetContext;
import org.apache.commons.jxpath.ri.axes.UnionContext;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class UnionContextTest {
    @Test
    public void testUnionContextConstructor() {
        // Test constructor with valid arguments
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext contexts[] = new EvalContext[2];
        contexts[0] = new EvalContext(new BasicNodeSet());
        contexts[1] = new EvalContext(new BasicNodeSet());
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        assertNotNull(unionContext);
    }

    @Test
    public void testGetDocumentOrder() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[] { context1, context2 });
        int documentOrder = unionContext.getDocumentOrder();
        assertTrue(documentOrder > 0);
    }

    @Test
    public void testSetPosition() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[] { context1, context2 });

        // Test basic functionality
        unionContext.setPosition(0);
        assertTrue(unionContext.nextNode());
        unionContext.setPosition(1);
        assertTrue(unionContext.nextNode());
        assertFalse(unionContext.nextNode());

        // Test edge cases
        unionContext.setPosition(-1);
        assertFalse(unionContext.nextNode());
        unionContext.setPosition(Integer.MAX_VALUE);
        assertFalse(unionContext.nextNode());

        // Test exception handling
        try {
            unionContext.setPosition(-10);
        } catch (Exception e) {
            assertEquals("Position index out of bounds: -10", e.getMessage());
        }
    }
}