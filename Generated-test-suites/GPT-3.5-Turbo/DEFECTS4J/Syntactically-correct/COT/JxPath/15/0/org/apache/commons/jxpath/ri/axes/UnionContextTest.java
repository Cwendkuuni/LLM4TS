import org.junit.Test;
import org.apache.commons.jxpath.ri.axes.UnionContext;
import org.apache.commons.jxpath.BasicNodeSet;

import static org.junit.Assert.*;

public class UnionContextTest {

    @Test
    public void testGetDocumentOrder() {
        EvalContext context1 = new EvalContext();
        EvalContext context2 = new EvalContext();
        EvalContext[] contexts = {context1, context2};
        UnionContext unionContext = new UnionContext(null, contexts);

        int result = unionContext.getDocumentOrder();

        assertEquals(1, result);
    }

    @Test
    public void testSetPosition() {
        EvalContext context1 = new EvalContext();
        EvalContext context2 = new EvalContext();
        EvalContext[] contexts = {context1, context2};
        UnionContext unionContext = new UnionContext(null, contexts);

        boolean result = unionContext.setPosition(1);

        assertTrue(result);
    }

}