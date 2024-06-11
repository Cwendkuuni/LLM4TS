import org.junit.jupiter.api.Test;
import org.apache.commons.jxpath.ri.compiler.*;

import org.apache.commons.jxpath.util.NodeSet;

import static org.junit.jupiter.api.Assertions.*;

class CoreOperationLessThanOrEqualTest {

    @Test
    void testComputeValue() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(new Constant(5), new Constant(10));
        EvalContext context = new EvalContext(new NodeSet(), 0, null);
        assertEquals(Boolean.TRUE, operation.computeValue(context));

        operation = new CoreOperationLessThanOrEqual(new Constant(5), new Constant(5));
        assertEquals(Boolean.TRUE, operation.computeValue(context));

        operation = new CoreOperationLessThanOrEqual(new Constant(10), new Constant(5));
        assertEquals(Boolean.FALSE, operation.computeValue(context));
    }
    
    @Test
    void testGetSymbol() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(new Constant(5), new Constant(10));
        assertEquals("<=", operation.getSymbol());

        operation = new CoreOperationLessThanOrEqual(new Constant(10), new Constant(5));
        assertEquals("<=", operation.getSymbol());

        operation = new CoreOperationLessThanOrEqual(new Constant(5), new Constant(5));
        assertEquals("<=", operation.getSymbol());
    }
}