import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UnionContextTest {

    @Test
    public void testEmptyUnion() {
        EvalContext parentContext = new EvalContext(new Object(), new BasicNodeSet());
        EvalContext context1 = new EvalContext(parentContext, new Object());
        EvalContext context2 = new EvalContext(parentContext, new Object());
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[] {context1, context2});

        Assertions.assertEquals(0, unionContext.getNodeSet().size());

        unionContext.nextSet();
        Assertions.assertTrue(unionContext.nextNode());
        Assertions.assertNull(unionContext.getCurrentNodePointer());
        Assertions.assertFalse(unionContext.hasNextNode());
    }

    @Test
    public void testSimpleUnion() {
        String xml = "<root><a/><b/><a/></root>";
        EvalContext parentContext = new EvalContext(new Object(), new JXPathContext(xml));
        EvalContext context1 = new EvalContext(parentContext, "/root/a");
        EvalContext context2 = new EvalContext(parentContext, "/root/b");
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[] {context1, context2});

        List<NodePointer> expectedNodes = new ArrayList<>();
        expectedNodes.add(new NodePointer(xml, "/root/a[1]"));
        expectedNodes.add(new NodePointer(xml, "/root/a[2]"));
        expectedNodes.add(new NodePointer(xml, "/root/b[1]"));

        int count = 0;
        while (unionContext.nextSet()) {
            while (unionContext.nextNode()) {
                NodePointer node = unionContext.getCurrentNodePointer();
                assertEquals(expectedNodes.get(count), node);
                count++;
                unionContext.nextNode();
            }
        }
        Assertions.assertTrue(unionContext.hasNextSet());
        unionContext.nextSet();
        Assertions.assertFalse(unionContext.hasNextNode());
    }

    @Test
    public void testErrorUnion() {
        EvalContext parentContext = new EvalContext(new Object(), new BasicNodeSet());
        EvalContext context1 = new EvalContext(parentContext, new Object());
        EvalContext context2 = new EvalContext(parentContext, new Object());
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[] {context1, context2});

        context1.setException(new RuntimeException("Test exception in context1"));

        assertThrows(RuntimeException.class, () -> unionContext.nextSet());
    }
}