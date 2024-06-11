
import org.junit.Test;
import java.util.ArrayList;
import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.axes.UnionContext;

public class UnionContextTest {

    @Test
    public void testGetDocumentOrder() {
        EvalContext parentContext = new EvalContext();
        EvalContext[] contexts = {new EvalContext(), new EvalContext()};
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        int result = unionContext.getDocumentOrder();
        assert(result == 1);
    }

    @Test
    public void testSetPosition() {
        EvalContext parentContext = new EvalContext();
        EvalContext[] contexts = {new EvalContext(), new EvalContext()};
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        boolean result = unionContext.setPosition(1);
        assert(result == true);
    }
}
