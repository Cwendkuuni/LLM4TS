import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.ri.EvalContext;

public class UnionContextTest {

    @Test
    public void testUnionContextConstructor() {
        EvalContext parentContext = new BasicNodeSet();
        EvalContext context1 = new BasicNodeSet();
        EvalContext context2 = new BasicNodeSet();
        EvalContext contexts[] = {context1, context2};

        UnionContext unionContext = new UnionContext(parentContext, contexts);
        assertNotNull(unionContext);
    }

    @Test
    public void testGetDocumentOrder() {
        EvalContext parentContext = new BasicNodeSet();
        EvalContext context1 = new BasicNodeSet();
        EvalContext context2 = new BasicNodeSet();
        EvalContext contexts[] = {context1, context2};

        UnionContext unionContext = new UnionContext(parentContext, contexts);
        assertEquals(1, unionContext.getDocumentOrder());
    }

    @Test
    public void testSetPosition() {
        EvalContext parentContext = new BasicNodeSet();
        EvalContext context1 = new BasicNodeSet();
        EvalContext context2 = new BasicNodeSet();
        EvalContext contexts[] = {context1, context2};

        UnionContext unionContext = new UnionContext(parentContext, contexts);
        assertTrue(unionContext.setPosition(1));
    }
}