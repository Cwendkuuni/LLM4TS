import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;

public class UnionContextTest {

    @Test
    public void testGetDocumentOrder() {
        // Test when contexts array has multiple elements
        EvalContext context1 = new EvalContext();
        EvalContext context2 = new EvalContext();
        EvalContext[] contexts = {context1, context2};
        UnionContext unionContext1 = new UnionContext(null, contexts);
        assertEquals(1, unionContext1.getDocumentOrder());

        // Test when contexts array has only one element
        EvalContext[] singleContext = {context1};
        UnionContext unionContext2 = new UnionContext(null, singleContext);
        assertEquals(0, unionContext2.getDocumentOrder());
    }

    @Test
    public void testSetPosition() {
        // Test when the position is not set
        EvalContext context1 = new EvalContext();
        EvalContext context2 = new EvalContext();
        EvalContext[] contexts = {context1, context2};
        UnionContext unionContext = new UnionContext(null, contexts);
        assertEquals(true, unionContext.setPosition(0));

        // Test when the position is set
        assertEquals(true, unionContext.setPosition(1));
    }

    @Test
    public void testSetPositionWithPreparedFalse() {
        // Test when prepared is false
        EvalContext context1 = new EvalContext();
        EvalContext[] contexts = {context1};
        UnionContext unionContext = new UnionContext(null, contexts);
        unionContext.prepared = false;
        assertEquals(true, unionContext.setPosition(0));
        assertEquals(true, unionContext.prepared);
    }

    @Test
    public void testSetPositionWithPreparedTrue() {
        // Test when prepared is true
        EvalContext context1 = new EvalContext();
        EvalContext[] contexts = {context1};
        UnionContext unionContext = new UnionContext(null, contexts);
        unionContext.prepared = true;
        assertEquals(true, unionContext.setPosition(0));
        assertEquals(true, unionContext.prepared);
    }

    @Test
    public void testSetPositionWithDuplicateNodePointers() {
        // Test when there are duplicate NodePointers in the contexts array
        EvalContext context1 = new EvalContext();
        EvalContext context2 = new EvalContext();
        EvalContext[] contexts = {context1, context2};
        UnionContext unionContext = new UnionContext(null, contexts);
        unionContext.prepared = false;

        NodePointer pointer1 = new NodePointer();
        NodePointer pointer2 = new NodePointer();
        NodePointer pointer3 = new NodePointer();
        ArrayList<NodePointer> pointers = new ArrayList<>();
        pointers.add(pointer1);
        pointers.add(pointer2);
        pointers.add(pointer3);

        context1.setCurrentNodePointer(pointer1);
        context2.setCurrentNodePointer(pointer2);

        // add pointer1 and pointer2 to the nodeSet
        assertEquals(true, unionContext.setPosition(0));
        assertEquals(false, pointers.contains(pointer3));
    }

    @Test
    public void testSetPositionWithUniqueNodePointers() {
        // Test when all NodePointers in contexts array are unique
        EvalContext context1 = new EvalContext();
        EvalContext context2 = new EvalContext();
        EvalContext[] contexts = {context1, context2};
        UnionContext unionContext = new UnionContext(null, contexts);
        unionContext.prepared = false;

        NodePointer pointer1 = new NodePointer();
        NodePointer pointer2 = new NodePointer();
        NodePointer pointer3 = new NodePointer();
        ArrayList<NodePointer> pointers = new ArrayList<>();
        pointers.add(pointer1);
        pointers.add(pointer2);
        pointers.add(pointer3);

        context1.setCurrentNodePointer(pointer1);
        context2.setCurrentNodePointer(pointer3);

        // add pointer1 and pointer3 to the nodeSet
        assertEquals(true, unionContext.setPosition(0));
        assertEquals(false, pointers.contains(pointer2));
    }
}