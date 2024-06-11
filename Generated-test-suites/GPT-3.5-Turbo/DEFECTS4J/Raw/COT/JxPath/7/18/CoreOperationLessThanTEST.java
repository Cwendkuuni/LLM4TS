import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue() {
        CoreOperationLessThan operation = new CoreOperationLessThan(new Expression(), new Expression());
        EvalContext context = new EvalContext(null);
        
        // Test case 1: Both arguments are integers
        context.setNodeSet(new NodeSet(new Node[] { new Node(5), new Node(10), new Node(15) }));
        assertEquals(true, operation.computeValue(context));
        
        // Test case 2: Both arguments are doubles
        context.setNodeSet(new NodeSet(new Node[] { new Node(2.5), new Node(4.5), new Node(6.5) }));
        assertEquals(true, operation.computeValue(context));
        
        // Test case 3: Arguments are of different types
        context.setNodeSet(new NodeSet(new Node[] { new Node(5), new Node(2.5), new Node(15), new Node(6.5) }));
        assertEquals(true, operation.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThan operation = new CoreOperationLessThan(new Expression(), new Expression());
        assertEquals("<", operation.getSymbol());
    }

}