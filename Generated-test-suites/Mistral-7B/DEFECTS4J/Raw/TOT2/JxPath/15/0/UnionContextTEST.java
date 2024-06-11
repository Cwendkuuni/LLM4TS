import org.apache.commons.jxpath.ri.axes.UnionContext;
import org.apache.commons.jxpath.ri.axes.UnionContextTest;
import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UnionContextTest {

    @Test
    public void testUnionContextConstructor() throws Exception {
        EvalContext parentContext = new EvalContext(new Object(), new BasicNodeSet());
        EvalContext[] contexts = new EvalContext[0];
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        assertEquals(0, unionContext.getDocumentOrder());
    }

    // Test cases for getDocumentOrder() and setPosition(int position) methods will be added later
}